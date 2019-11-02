package com.mesonplugin;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.ContainerUtil;
import java.util.Arrays;
import java.util.Map;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MesonColorSettingsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] LEXER_DESCRIPTORS =
      new AttributesDescriptor[] {
          new AttributesDescriptor("Keyword", MesonSyntaxHighlighter.KEYWORD),
          new AttributesDescriptor("Comment", MesonSyntaxHighlighter.COMMENT),
          new AttributesDescriptor("Strings", MesonSyntaxHighlighter.STRING),
          new AttributesDescriptor("Numbers", MesonSyntaxHighlighter.NUMBER),
          new AttributesDescriptor("Boolean operators and constants", MesonSyntaxHighlighter.BOOL_OP),
          new AttributesDescriptor("Identifier", MesonSyntaxHighlighter.IDENTIFIER)
      };

  private static final AttributesDescriptor[] ANNOTATOR_DESCRIPTORS =
      new AttributesDescriptor[] {
          new AttributesDescriptor("Function name", MesonSyntaxHighlighter.FUNCTION),
          new AttributesDescriptor("Method name", MesonSyntaxHighlighter.METHODS),
          new AttributesDescriptor("Meson built-in object", MesonSyntaxHighlighter.MESON_OBJECT),
          new AttributesDescriptor("Variable (re)definition", MesonSyntaxHighlighter.VAR_DEF),
          new AttributesDescriptor("Variable refernce", MesonSyntaxHighlighter.VAR_REF),
          new AttributesDescriptor("String formatting placeholder", MesonSyntaxHighlighter.FORMATTING_PLACEHOLDER),
          new AttributesDescriptor("Keyword parameter in function call", MesonSyntaxHighlighter.KEYWORD_PARAMETER)
      };

  @Nullable
  @Override
  public Icon getIcon() {
    return MesonIcons.FILE;
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new MesonSyntaxHighlighter();
  }

  @NotNull
  @Override
  public String getDemoText() {
    return ""
        + "#Comment line\n"
        + "<f>executable</f>('progname' <kp>sources:</kp> 'prog.c', <kp>c_args:</kp> '-DFOO=1')\n"
        + "<vd>conf</vd> = <f>configuration_data</f>()\n"
        + "foreach <vd>name</vd> : ['name', 'not_name']\n"
        + "  if not false and <vr>conf</vr>.<m>get</m>('USE_<p>@0@</p>'.<m>format</m>(<vr>name</vr>.<m>to_upper</m>()), 0) == 0xFF\n"
        + "    <vd>var</vd> += <o>meson</o>.<m>version</m>()\n"
        + "  endif\n"
        + "endforeach\n"
        ;
  }

  @Nullable
  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return ContainerUtil.newHashMap(
        Arrays.asList("f", "m", "o", "vd", "vr", "p", "kp"),
        Arrays.asList(
            MesonSyntaxHighlighter.FUNCTION,
            MesonSyntaxHighlighter.METHODS,
            MesonSyntaxHighlighter.MESON_OBJECT,
            MesonSyntaxHighlighter.VAR_DEF,
            MesonSyntaxHighlighter.VAR_REF,
            MesonSyntaxHighlighter.FORMATTING_PLACEHOLDER,
            MesonSyntaxHighlighter.KEYWORD_PARAMETER));
  }

  @NotNull
  @Override
  public AttributesDescriptor[] getAttributeDescriptors() {
    return ArrayUtil.mergeArrays(LEXER_DESCRIPTORS, ANNOTATOR_DESCRIPTORS);
  }

  @NotNull
  @Override
  public ColorDescriptor[] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Meson";
  }
}
