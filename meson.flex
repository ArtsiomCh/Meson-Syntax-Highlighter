package com.mesonplugin;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.mesonplugin.psi.MesonTypes.*;
import java.util.LinkedList;
%%

%{
  boolean isConditionExpression = false;
  // Stolen from Mathematica support plugin. This adds support for nested states.
  private final LinkedList<Integer> states = new LinkedList();

  private void yypushstate(int state) {
      states.addFirst(yystate());
      yybegin(state);
  }
  private void yypopstate() {
      final int state = states.removeFirst();
      yybegin(state);
  }
%}

%public
%class _MesonLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%ignorecase

EOL= (\r|\n|\r\n|\f)
LINE_WS=[\ \t]
WHITE_SPACE=({LINE_WS}|{EOL})+
//ARG_SEPARATOR={WHITE_SPACE}|","

LINE_COMMENT = #[^\r\n]*

STRING_RAW = (\'\'\') ~ (\'\'\')
STRING = \' ([^\']|\\\')* \'

NUMBER = -?[0-9]+ | 0o[0-7]+ | 0b[0-1]+ | 0x[0-9a-fA-F]+

IDENTIFIER = [A-Za-z_][A-Za-z0-9_]*

AROPS = \+ | \- | \* | \/ | \%
ASSIGN = (\+|-)?=
EQ = == | \!= | (< | >)=?


%state IN_ARGLIST

%%

<YYINITIAL> {

  {STRING_RAW}             { return STRINGRAW; }
  {STRING}                 { return STRING; }
  {NUMBER}                 { return NUMBER; }

  {WHITE_SPACE}            { return com.intellij.psi.TokenType.WHITE_SPACE; }
  "("                      { return LPAR; }
  ")"                      { return RPAR; }
  "["                      { return LSQUAREBRACKET; }
  "]"                      { return RSQUAREBRACKET; }
  "{"                      { return LCURL; }
  "}"                      { return RCURL; }
  ":"                      { return COLON; }
  ","                      { return COMMA; }
  "."                      { return DOT; }
  "?"                      { return QMARK; }

  {ASSIGN}                 { return ASSIGN; }
  {EQ}                     { return EQ; }
  "in"                     { return IN; }
  "not"                    { return NEGATION; }
  "and"|"or"               { return BOOLOP; }
  {AROPS}                  { return AROP; }

  "ENDFOREACH"             { return ENDFOREACH; }
  "FOREACH"                { return FOREACH; }
  "ELIF"                   { return ELIF; }
  "ELSE"                   { return ELSE; }
  "ENDIF"                  { return ENDIF; }
  "if"                     { return IF; }
  "true"                   { return TRUE; }
  "false"                  { return FALSE; }
  "break"                  { return BREAK; }
  "continue"               { return CONTINUE; }

  {LINE_COMMENT}/{EOL}     { return COMMENT; }
  {IDENTIFIER}             { return IDENTIFIER; }

//  [^\ \t\r\n\'0-9]+       { return UNRECOGNIZED; }
}

/*
<IN_ARGLIST> {
  {ARG_SEPARATOR}+          { yybegin(IN_ARGLIST); return com.intellij.psi.TokenType.WHITE_SPACE; }

  "("                      { yypushstate(IN_ARGLIST); return LPAR; }
  ")"                      { yypopstate(); return RPAR; }

  "true"                   { return TRUE; }
  "false"                  { return FALSE; }
  ":"                      { return COLON; }

  {STRING_RAW}             { return STRING_RAW; }
  {STRING}                 { return STRING; }
  {NUMBER}                 { return NUMBER; }
  {LINE_COMMENT}/{EOL}     { return COMMENT; }
  {IDENTIFIER}             { return IDENTIFIER; }
}
*/

[^] { return com.intellij.psi.TokenType.BAD_CHARACTER; }
