package com.mesonplugin;

import com.intellij.openapi.fileTypes.FileNameMatcherEx;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class MesonFileTypeFactory extends FileTypeFactory {

  @Override
  public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
    fileTypeConsumer.consume(MesonFileType.INSTANCE,
        new FileNameMatcherEx() {
          @Override
          @NotNull
          public String getPresentableString() {
            return "meson";
          }

          @Override
          public boolean acceptsCharSequence(@NotNull CharSequence fileName) {
            return fileName.equals("meson.build") || fileName.equals("meson_options.txt"); //.toString().matches("meson[.]build | meson_options[.]txt");
          }

        });
  }
}
