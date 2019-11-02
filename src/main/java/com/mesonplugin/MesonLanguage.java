package com.mesonplugin;

import com.intellij.lang.Language;

public class MesonLanguage extends Language {
  public static final MesonLanguage INSTANCE = new MesonLanguage();

  private MesonLanguage() {
    super("Meson");
  }
}