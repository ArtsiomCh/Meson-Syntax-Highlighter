package com.mesonplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.mesonplugin.MesonLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class MesonTokenType extends IElementType {

  public MesonTokenType(@NotNull @NonNls String debugName) {
    super(debugName, MesonLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "MesonTokenType." + super.toString();
  }
}
