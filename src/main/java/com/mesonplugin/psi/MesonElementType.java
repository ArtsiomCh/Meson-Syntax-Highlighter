package com.mesonplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.mesonplugin.MesonLanguage;

public class MesonElementType extends IElementType {

  public MesonElementType(String debugName) {
    super(debugName, MesonLanguage.INSTANCE);
  }
}
