package com.mesonplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MesonFileType extends LanguageFileType {

  public static final MesonFileType INSTANCE = new MesonFileType();

  private MesonFileType() {
    super(MesonLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Meson file";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Meson language file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "build";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return MesonIcons.FILE;
  }
}
