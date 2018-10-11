/* Generated By:JavaCC: Do not edit this line. BibTeXParserConstants.java */
package org.jbibtex;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface BibTeXParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int INLINE_COMMENT = 3;
  /** RegularExpression Id. */
  int TEXT = 5;
  /** RegularExpression Id. */
  int AT = 11;
  /** RegularExpression Id. */
  int COMMA = 12;
  /** RegularExpression Id. */
  int EQUALS = 13;
  /** RegularExpression Id. */
  int HASH = 14;
  /** RegularExpression Id. */
  int LBRACE = 15;
  /** RegularExpression Id. */
  int LPAREN = 16;
  /** RegularExpression Id. */
  int QUOTE = 17;
  /** RegularExpression Id. */
  int RBRACE = 18;
  /** RegularExpression Id. */
  int RPAREN = 19;
  /** RegularExpression Id. */
  int COMMENT = 20;
  /** RegularExpression Id. */
  int INCLUDE = 21;
  /** RegularExpression Id. */
  int PREAMBLE = 22;
  /** RegularExpression Id. */
  int STRING = 23;
  /** RegularExpression Id. */
  int NAME = 24;
  /** RegularExpression Id. */
  int ASCII_LETTER = 25;
  /** RegularExpression Id. */
  int ASCII_DIGIT = 26;
  /** RegularExpression Id. */
  int LATIN_LETTER = 27;
  /** RegularExpression Id. */
  int UNICODE_LETTER = 28;
  /** RegularExpression Id. */
  int NAME_START_SYMBOL = 29;
  /** RegularExpression Id. */
  int NAME_CONTINUATION_SYMBOL = 30;

  /** Lexical state. */
  int IN_HEADER = 0;
  /** Lexical state. */
  int DEFAULT = 1;
  /** Lexical state. */
  int IN_INLINE_COMMENT = 2;
  /** Lexical state. */
  int IN_LITERAL = 3;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "<token of kind 1>",
    "\"%\"",
    "<INLINE_COMMENT>",
    "<token of kind 4>",
    "<TEXT>",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\f\"",
    "\"\\r\"",
    "\" \"",
    "\"@\"",
    "\",\"",
    "\"=\"",
    "\"#\"",
    "\"{\"",
    "\"(\"",
    "\"\\\"\"",
    "\"}\"",
    "\")\"",
    "<COMMENT>",
    "<INCLUDE>",
    "<PREAMBLE>",
    "<STRING>",
    "<NAME>",
    "<ASCII_LETTER>",
    "<ASCII_DIGIT>",
    "<LATIN_LETTER>",
    "<UNICODE_LETTER>",
    "<NAME_START_SYMBOL>",
    "<NAME_CONTINUATION_SYMBOL>",
  };

}