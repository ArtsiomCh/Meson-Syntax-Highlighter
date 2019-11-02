package com.mesonplugin;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.mesonplugin.parsing.MesonParser;
import com.mesonplugin.psi.MesonTypes;
import org.jetbrains.annotations.NotNull;

public class MesonParserDefinition implements ParserDefinition {

  private static TokenSet WHITE_SPACES;
  private static TokenSet COMMENTS;
  private static TokenSet STRINGS;
  public static final IFileElementType FILE = new IFileElementType(MesonLanguage.INSTANCE);

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new MesonLexerAdapter();
  }

  @Override
  public PsiParser createParser(Project project) {
    return new MesonParser();
  }

  @Override
  public IFileElementType getFileNodeType() {
    return FILE;
  }

  @NotNull
  @Override
  public TokenSet getWhitespaceTokens() {
    if (WHITE_SPACES == null) WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    return WHITE_SPACES;
  }

  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    if (COMMENTS == null)
      COMMENTS = TokenSet.create(MesonTypes.COMMENT);
    return COMMENTS;
  }

  @NotNull
  @Override
  public TokenSet getStringLiteralElements() {
    if (STRINGS == null) STRINGS = TokenSet.create(MesonTypes.STRING);
    return STRINGS;
  }

  @NotNull
  @Override
  public PsiElement createElement(ASTNode astNode) {
    return MesonTypes.Factory.createElement(astNode);
  }

  @Override
  public PsiFile createFile(FileViewProvider fileViewProvider) {
    return new MesonFile(fileViewProvider);
  }
}
