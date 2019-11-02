package com.mesonplugin;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.mesonplugin.psi.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MesonAnnotator implements Annotator {

  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    if (!CheckLicense.enabled) return;
    if (element instanceof MesonStringContainer) {
      int elementStartInFile = element.getTextRange().getStartOffset();
      for (TextRange rangeInsideElementText : getFormattingPlaceholders(element.getText())) {
        createInfoAnnotation(
            rangeInsideElementText.shiftRight(elementStartInFile),
            holder,
            MesonSyntaxHighlighter.FORMATTING_PLACEHOLDER);
      }

    } else if (element instanceof MesonKeywordArgument) {
      PsiElement colon = getChildOfElementType(element, MesonTypes.COLON);
      if (colon != null)
        createInfoAnnotation(
            new TextRange(
                element.getTextRange().getStartOffset(), colon.getTextRange().getEndOffset()),
            holder,
            MesonSyntaxHighlighter.KEYWORD_PARAMETER);

    } else if (element instanceof MesonFunctionName) {
      if (MesonKeywords.FUNCTIONS.contains(element.getText())
          || (element.textMatches("option") && element.getContainingFile().getName().equals("meson_options.txt"))) {
        createInfoAnnotation(element, holder, MesonSyntaxHighlighter.FUNCTION);
      } else {
        // TODO Move it to Inspections
        createWeakWarningAnnotation(element.getTextRange(), holder, "Unknown function call");
      }

    } else if (element instanceof MesonTernaryOp) {
      createInfoAnnotation(
          getChildOfElementType(element, MesonTypes.QMARK), holder, MesonSyntaxHighlighter.KEYWORD);
      createInfoAnnotation(
          getChildOfElementType(element, MesonTypes.COLON), holder, MesonSyntaxHighlighter.KEYWORD);

    } else if (element instanceof MesonVarDef) {
      createInfoAnnotation(element, holder, MesonSyntaxHighlighter.VAR_DEF);
    } else if (element instanceof MesonVarRef) {
      if (MesonKeywords.BUILT_IN_OBJECTS.contains(element.getText())) {
        createInfoAnnotation(element, holder, MesonSyntaxHighlighter.MESON_OBJECT);
      } else {
        createInfoAnnotation(element, holder, MesonSyntaxHighlighter.VAR_REF);
      }

    } else if (element instanceof MesonMethodCall) {
      annotateMethodCall(element, holder);

    } else if (element instanceof MesonUnrecognized) {
      createWeakWarningAnnotation(
          element.getTextRange(), holder, "Not recognized identifier: " + element.getText());
    }
  }

  private void annotateMethodCall(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    PsiElement methodName = element.getFirstChild().getNextSibling().getFirstChild();
    PsiElement methodObject = element.getPrevSibling().getFirstChild();
    if (methodObject == null || methodName == null) return;
    final IElementType methodObjectType = methodObject.getNode().getElementType();

    // TODO Move it to Inspections?
    if (methodObjectType == MesonTypes.VAR_REF) {
      if (MesonKeywords.BUILT_IN_OBJECTS.contains(methodObject.getText())) {
        if (methodObject.textMatches("meson")) {
          annotateMethodName(methodName, MesonKeywords.MESON_METHODS, holder, "meson");
        } else {
          annotateMethodName(
              methodName,
              MesonKeywords.BUILD_MACHINE_METHODS,
              holder,
              "(build|host|target)_machine");
        }
      } else annotateMethodName(methodName, MesonKeywords.ALL_METHODS, holder, "variable");

    } else if (methodName.textMatches("found")) {
      createInfoAnnotation(methodName, holder, MesonSyntaxHighlighter.METHODS);

    } else if (methodObjectType == MesonTypes.STRING_CONTAINER) {
      annotateMethodName(methodName, MesonKeywords.STRING_METHODS, holder, "string");
    } else if (methodObjectType == MesonTypes.NUMBER) {
      annotateMethodName(methodName, MesonKeywords.NUMBER_METHODS, holder, "number");
    } else if (methodObjectType == MesonTypes.ARRAY_DEF) {
      annotateMethodName(methodName, MesonKeywords.ARRAY_METHODS, holder, "array");
    } else if (methodObjectType == MesonTypes.DICT_DEF) {
      annotateMethodName(methodName, MesonKeywords.DICTIONARY_METHODS, holder, "dictionary");

    } else if (methodObjectType == MesonTypes.FUNCTION_CALL) {
      PsiElement funNameEl = PsiTreeUtil.getChildOfType(methodObject, MesonFunctionName.class);
      if (funNameEl != null) {
        annotateMethodName(
            methodName,
            MesonKeywords.FUN2METHODS.get(funNameEl.getText()),
            holder,
            funNameEl.getText());
      }
    } else if (methodObject.getParent() instanceof MesonMethodCall) {
      PsiElement prevMethodNameEl =
          PsiTreeUtil.getChildOfType(methodObject.getParent(), MesonMethodName.class);
      if (prevMethodNameEl != null) {
        annotateMethodName(
            methodName,
            MesonKeywords.METHOD2METHODS.get(prevMethodNameEl.getText()),
            holder,
            prevMethodNameEl.getText());
      }

      // TODO remove it?
    } else annotateMethodName(methodName, null, holder, methodObject.getText());
  }

  private void annotateMethodName(
      @NotNull PsiElement methodName,
      @Nullable Set<String> namesSetToCheckIn,
      @NotNull AnnotationHolder holder,
      @NotNull String objectName) {
    if (namesSetToCheckIn != null && namesSetToCheckIn.contains(methodName.getText())) {
      createInfoAnnotation(methodName, holder, MesonSyntaxHighlighter.METHODS);
    } else {
      createWeakWarningAnnotation(
          methodName.getTextRange(), holder, "Unknown method call for <" + objectName + "> object");
    }
  }

  /** See {@link PsiTreeUtil#getChildOfType(PsiElement, Class)} */
  private static PsiElement getChildOfElementType(
      @Nullable PsiElement element, @NotNull IElementType elementType) {
    if (element == null) return null;
    for (PsiElement child = element.getFirstChild();
        child != null;
        child = child.getNextSibling()) {
      if (child.getNode().getElementType() == elementType) {
        return child;
      }
    }
    return null;
  }

  private static final Pattern patternFormattingPlaceholders = Pattern.compile("@[0-9]+@");

  private static Collection<TextRange> getFormattingPlaceholders(@NotNull String string) {
    Matcher matcher = patternFormattingPlaceholders.matcher(string);
    if (!matcher.find()) return Collections.emptyList();
    Collection<TextRange> result = new ArrayList<>();
    do {
      result.add(new TextRange(matcher.start(), matcher.end()));
    } while (matcher.find());
    return result;
  }

  private static void createInfoAnnotation(
      @Nullable PsiElement element,
      @NotNull AnnotationHolder holder,
      @NotNull TextAttributesKey textAttKey) {
    if (element != null) createInfoAnnotation(element.getTextRange(), holder, textAttKey);
  }

  private static void createInfoAnnotation(
      @NotNull TextRange range,
      @NotNull AnnotationHolder holder,
      @NotNull TextAttributesKey textAttKey) {
    Annotation annotation = holder.createInfoAnnotation(range, textAttKey.getExternalName());
    annotation.setTooltip(null);
    annotation.setTextAttributes(textAttKey);
  }

  private static void createWeakWarningAnnotation(
      @NotNull TextRange range, @NotNull AnnotationHolder holder, @NotNull String message) {
    holder
        .createWeakWarningAnnotation(range, message)
        .setHighlightType(ProblemHighlightType.WEAK_WARNING);
  }
}
