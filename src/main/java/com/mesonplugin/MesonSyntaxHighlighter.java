package com.mesonplugin;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.mesonplugin.psi.MesonTypes;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class MesonSyntaxHighlighter implements SyntaxHighlighter {

  private static final Map<IElementType, TextAttributesKey> mapMesonTypeToTextAttr =
      new HashMap<>();

  public static final TextAttributesKey KEYWORD =
      createTextAttributesKey("MESON_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey COMMENT =
      createTextAttributesKey("MESON_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
  public static final TextAttributesKey STRING =
      createTextAttributesKey("MESON_STRING", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey NUMBER =
      createTextAttributesKey("MESON_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey BOOL_OP =
      createTextAttributesKey("MESON_BOOLEAN_OPERATOR", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey IDENTIFIER =
      createTextAttributesKey("MESON_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

  public static final TextAttributesKey FUNCTION =
      createTextAttributesKey(
          "MESON_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
  public static final TextAttributesKey METHODS =
      createTextAttributesKey("MESON_METHODS", DefaultLanguageHighlighterColors.STATIC_METHOD);
  public static final TextAttributesKey MESON_OBJECT =
      myCreateTextAttributesKey(
          "MESON_OBJECT", Font.BOLD, DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);

  public static final TextAttributesKey VAR_DEF = createTextAttributesKey("MY_BOLD");
  public static final TextAttributesKey VAR_REF = createTextAttributesKey("MY_ITALIC");

  public static final TextAttributesKey FORMATTING_PLACEHOLDER =
      createTextAttributesKey(
          "MESON_FORMATTING_PLACEHOLDER", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
  public static final TextAttributesKey KEYWORD_PARAMETER =
      createTextAttributesKey(
          "MESON_KEYWORD_PARAMETR", DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT);

  @NotNull
  private static TextAttributesKey myCreateTextAttributesKey(
      String externalName, int newFontType, TextAttributesKey baseTextAttributesKey) {
    TextAttributes textAttributes = baseTextAttributesKey.getDefaultAttributes().clone();
    int fontType = textAttributes.getFontType() + newFontType;
    if (fontType >= 0 && fontType <= 3) {
      textAttributes.setFontType(fontType);
    }
    return TextAttributesKey.createTextAttributesKey(externalName, textAttributes);
  }

  static {
    mapMesonTypeToTextAttr.put(MesonTypes.IF, KEYWORD);
    mapMesonTypeToTextAttr.put(MesonTypes.ELIF, KEYWORD);
    mapMesonTypeToTextAttr.put(MesonTypes.ELSE, KEYWORD);
    mapMesonTypeToTextAttr.put(MesonTypes.ENDIF, KEYWORD);
    mapMesonTypeToTextAttr.put(MesonTypes.FOREACH, KEYWORD);
    mapMesonTypeToTextAttr.put(MesonTypes.ENDFOREACH, KEYWORD);
    mapMesonTypeToTextAttr.put(MesonTypes.BREAK, KEYWORD);
    mapMesonTypeToTextAttr.put(MesonTypes.CONTINUE, KEYWORD);

    //    mapMesonTypeToTextAttr.put(MesonTypes.COLON, KEYWORD);
    //    mapMesonTypeToTextAttr.put(MesonTypes.QMARK, KEYWORD);
    mapMesonTypeToTextAttr.put(MesonTypes.ASSIGN, KEYWORD);
    mapMesonTypeToTextAttr.put(MesonTypes.COMMA, KEYWORD);

    //    mapMesonTypeToTextAttr.put(MesonTypes.EQ, BOOL_OP);
    mapMesonTypeToTextAttr.put(MesonTypes.TRUE, BOOL_OP);
    mapMesonTypeToTextAttr.put(MesonTypes.FALSE, BOOL_OP);
    mapMesonTypeToTextAttr.put(MesonTypes.BOOLOP, BOOL_OP);
    mapMesonTypeToTextAttr.put(MesonTypes.IN, BOOL_OP);
    mapMesonTypeToTextAttr.put(MesonTypes.NEGATION, BOOL_OP);

    mapMesonTypeToTextAttr.put(MesonTypes.STRING, STRING);
    mapMesonTypeToTextAttr.put(MesonTypes.STRINGRAW, STRING);

    mapMesonTypeToTextAttr.put(MesonTypes.NUMBER, NUMBER);
    mapMesonTypeToTextAttr.put(MesonTypes.IDENTIFIER, IDENTIFIER);
    mapMesonTypeToTextAttr.put(MesonTypes.COMMENT, COMMENT);
  }

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new MesonLexerAdapter();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType iElementType) {
    final TextAttributesKey textAttrKey = mapMesonTypeToTextAttr.get(iElementType);
    return (textAttrKey != null && CheckLicense.enabled)
        ? new TextAttributesKey[] {textAttrKey}
        : TextAttributesKey.EMPTY_ARRAY;
  }
}
