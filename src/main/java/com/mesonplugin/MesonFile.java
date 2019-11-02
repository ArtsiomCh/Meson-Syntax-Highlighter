package com.mesonplugin;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public class MesonFile extends PsiFileBase {

  public MesonFile(@NotNull FileViewProvider fileViewProvider) {
    super(fileViewProvider, MesonLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return MesonFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "Meson File";
  }

  @Override
  public Icon getIcon(int flags) {
    return super.getIcon(flags);
  }

}
