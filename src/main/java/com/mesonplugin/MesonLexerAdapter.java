package com.mesonplugin;

import com.intellij.lexer.FlexAdapter;

public class MesonLexerAdapter extends FlexAdapter {
  public MesonLexerAdapter() {
    super(new _MesonLexer(null));
  }
}
