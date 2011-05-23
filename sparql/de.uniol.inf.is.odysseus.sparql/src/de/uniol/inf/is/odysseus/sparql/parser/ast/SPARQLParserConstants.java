/* Generated By:JJTree&JavaCC: Do not edit this line. SPARQLParserConstants.java */
/*
 * (c) Copyright 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 */

package de.uniol.inf.is.odysseus.sparql.parser.ast;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface SPARQLParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int WS = 6;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 7;
  /** RegularExpression Id. */
  int IRIref = 8;
  /** RegularExpression Id. */
  int PNAME_NS = 9;
  /** RegularExpression Id. */
  int PNAME_LN = 10;
  /** RegularExpression Id. */
  int BLANK_NODE_LABEL = 11;
  /** RegularExpression Id. */
  int VAR1 = 12;
  /** RegularExpression Id. */
  int VAR2 = 13;
  /** RegularExpression Id. */
  int LANGTAG = 14;
  /** RegularExpression Id. */
  int A2Z = 15;
  /** RegularExpression Id. */
  int A2ZN = 16;
  /** RegularExpression Id. */
  int KW_A = 17;
  /** RegularExpression Id. */
  int BASE = 18;
  /** RegularExpression Id. */
  int PREFIX = 19;
  /** RegularExpression Id. */
  int SELECT = 20;
  /** RegularExpression Id. */
  int DISTINCT = 21;
  /** RegularExpression Id. */
  int REDUCED = 22;
  /** RegularExpression Id. */
  int DESCRIBE = 23;
  /** RegularExpression Id. */
  int CONSTRUCT = 24;
  /** RegularExpression Id. */
  int ASK = 25;
  /** RegularExpression Id. */
  int LIMIT = 26;
  /** RegularExpression Id. */
  int OFFSET = 27;
  /** RegularExpression Id. */
  int ORDER = 28;
  /** RegularExpression Id. */
  int BY = 29;
  /** RegularExpression Id. */
  int GROUP = 30;
  /** RegularExpression Id. */
  int ASC = 31;
  /** RegularExpression Id. */
  int DESC = 32;
  /** RegularExpression Id. */
  int AVG = 33;
  /** RegularExpression Id. */
  int MAX = 34;
  /** RegularExpression Id. */
  int MIN = 35;
  /** RegularExpression Id. */
  int SUM = 36;
  /** RegularExpression Id. */
  int COUNT = 37;
  /** RegularExpression Id. */
  int NAMED = 38;
  /** RegularExpression Id. */
  int FROM = 39;
  /** RegularExpression Id. */
  int STREAM = 40;
  /** RegularExpression Id. */
  int PERSISTENT = 41;
  /** RegularExpression Id. */
  int CREATE = 42;
  /** RegularExpression Id. */
  int ATTACH = 43;
  /** RegularExpression Id. */
  int ADD = 44;
  /** RegularExpression Id. */
  int WITH = 45;
  /** RegularExpression Id. */
  int CSV = 46;
  /** RegularExpression Id. */
  int CHANNEL = 47;
  /** RegularExpression Id. */
  int DIGIT = 48;
  /** RegularExpression Id. */
  int IPADDRESS = 49;
  /** RegularExpression Id. */
  int SOCKET = 50;
  /** RegularExpression Id. */
  int WINDOW = 51;
  /** RegularExpression Id. */
  int RANGE = 52;
  /** RegularExpression Id. */
  int SLIDE = 53;
  /** RegularExpression Id. */
  int ADVANCE = 54;
  /** RegularExpression Id. */
  int FIXED = 55;
  /** RegularExpression Id. */
  int ELEMS = 56;
  /** RegularExpression Id. */
  int PWINDOW = 57;
  /** RegularExpression Id. */
  int START = 58;
  /** RegularExpression Id. */
  int END = 59;
  /** RegularExpression Id. */
  int MS = 60;
  /** RegularExpression Id. */
  int S = 61;
  /** RegularExpression Id. */
  int MINUTE = 62;
  /** RegularExpression Id. */
  int HOUR = 63;
  /** RegularExpression Id. */
  int DAY = 64;
  /** RegularExpression Id. */
  int WEEK = 65;
  /** RegularExpression Id. */
  int MONTH = 66;
  /** RegularExpression Id. */
  int YEAR = 67;
  /** RegularExpression Id. */
  int WHERE = 68;
  /** RegularExpression Id. */
  int AND = 69;
  /** RegularExpression Id. */
  int GRAPH = 70;
  /** RegularExpression Id. */
  int OPTIONAL = 71;
  /** RegularExpression Id. */
  int UNION = 72;
  /** RegularExpression Id. */
  int FILTER = 73;
  /** RegularExpression Id. */
  int BOUND = 74;
  /** RegularExpression Id. */
  int STR = 75;
  /** RegularExpression Id. */
  int DTYPE = 76;
  /** RegularExpression Id. */
  int LANG = 77;
  /** RegularExpression Id. */
  int LANGMATCHES = 78;
  /** RegularExpression Id. */
  int IS_URI = 79;
  /** RegularExpression Id. */
  int IS_IRI = 80;
  /** RegularExpression Id. */
  int IS_BLANK = 81;
  /** RegularExpression Id. */
  int IS_LITERAL = 82;
  /** RegularExpression Id. */
  int REGEX = 83;
  /** RegularExpression Id. */
  int SAME_TERM = 84;
  /** RegularExpression Id. */
  int TRUE = 85;
  /** RegularExpression Id. */
  int FALSE = 86;
  /** RegularExpression Id. */
  int DIGITS = 87;
  /** RegularExpression Id. */
  int INTEGER = 88;
  /** RegularExpression Id. */
  int DECIMAL = 89;
  /** RegularExpression Id. */
  int DOUBLE = 90;
  /** RegularExpression Id. */
  int INTEGER_POSITIVE = 91;
  /** RegularExpression Id. */
  int DECIMAL_POSITIVE = 92;
  /** RegularExpression Id. */
  int DOUBLE_POSITIVE = 93;
  /** RegularExpression Id. */
  int INTEGER_NEGATIVE = 94;
  /** RegularExpression Id. */
  int DECIMAL_NEGATIVE = 95;
  /** RegularExpression Id. */
  int DOUBLE_NEGATIVE = 96;
  /** RegularExpression Id. */
  int EXPONENT = 97;
  /** RegularExpression Id. */
  int QUOTE_3D = 98;
  /** RegularExpression Id. */
  int QUOTE_3S = 99;
  /** RegularExpression Id. */
  int ECHAR = 100;
  /** RegularExpression Id. */
  int STRING_LITERAL1 = 101;
  /** RegularExpression Id. */
  int STRING_LITERAL2 = 102;
  /** RegularExpression Id. */
  int STRING_LITERAL_LONG1 = 103;
  /** RegularExpression Id. */
  int STRING_LITERAL_LONG2 = 104;
  /** RegularExpression Id. */
  int IDENTIFIER = 105;
  /** RegularExpression Id. */
  int LETTER = 106;
  /** RegularExpression Id. */
  int SPECIAL_CHARS = 107;
  /** RegularExpression Id. */
  int LPAREN = 108;
  /** RegularExpression Id. */
  int RPAREN = 109;
  /** RegularExpression Id. */
  int NIL = 110;
  /** RegularExpression Id. */
  int LBRACE = 111;
  /** RegularExpression Id. */
  int RBRACE = 112;
  /** RegularExpression Id. */
  int LBRACKET = 113;
  /** RegularExpression Id. */
  int RBRACKET = 114;
  /** RegularExpression Id. */
  int ANON = 115;
  /** RegularExpression Id. */
  int SEMICOLON = 116;
  /** RegularExpression Id. */
  int COMMA = 117;
  /** RegularExpression Id. */
  int DOT = 118;
  /** RegularExpression Id. */
  int EQ = 119;
  /** RegularExpression Id. */
  int NE = 120;
  /** RegularExpression Id. */
  int GT = 121;
  /** RegularExpression Id. */
  int LT = 122;
  /** RegularExpression Id. */
  int LE = 123;
  /** RegularExpression Id. */
  int GE = 124;
  /** RegularExpression Id. */
  int BANG = 125;
  /** RegularExpression Id. */
  int TILDE = 126;
  /** RegularExpression Id. */
  int SC_OR = 127;
  /** RegularExpression Id. */
  int SC_AND = 128;
  /** RegularExpression Id. */
  int PLUS = 129;
  /** RegularExpression Id. */
  int MINUS = 130;
  /** RegularExpression Id. */
  int STAR = 131;
  /** RegularExpression Id. */
  int SLASH = 132;
  /** RegularExpression Id. */
  int DATATYPE = 133;
  /** RegularExpression Id. */
  int AT = 134;
  /** RegularExpression Id. */
  int PN_CHARS_BASE = 135;
  /** RegularExpression Id. */
  int PN_CHARS_U = 136;
  /** RegularExpression Id. */
  int PN_CHARS = 137;
  /** RegularExpression Id. */
  int PN_PREFIX = 138;
  /** RegularExpression Id. */
  int PN_LOCAL = 139;
  /** RegularExpression Id. */
  int VARNAME = 140;
  /** RegularExpression Id. */
  int UNKNOWN = 141;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<WS>",
    "<SINGLE_LINE_COMMENT>",
    "<IRIref>",
    "<PNAME_NS>",
    "<PNAME_LN>",
    "<BLANK_NODE_LABEL>",
    "<VAR1>",
    "<VAR2>",
    "<LANGTAG>",
    "<A2Z>",
    "<A2ZN>",
    "\"a\"",
    "\"base\"",
    "\"prefix\"",
    "\"select\"",
    "\"distinct\"",
    "\"reduced\"",
    "\"describe\"",
    "\"construct\"",
    "\"ask\"",
    "\"limit\"",
    "\"offset\"",
    "\"order\"",
    "\"by\"",
    "\"group\"",
    "\"asc\"",
    "\"desc\"",
    "\"avg\"",
    "\"max\"",
    "\"min\"",
    "\"sum\"",
    "\"count\"",
    "\"named\"",
    "\"from\"",
    "\"stream\"",
    "\"persistent\"",
    "\"create\"",
    "\"attach\"",
    "\"add\"",
    "\"with\"",
    "\"csv\"",
    "\"channel\"",
    "<DIGIT>",
    "<IPADDRESS>",
    "\"socket\"",
    "\"window\"",
    "\"range\"",
    "\"slide\"",
    "\"advance\"",
    "\"fixed\"",
    "\"elems\"",
    "\"pwindow\"",
    "\"start\"",
    "\"end\"",
    "\"MS\"",
    "\"S\"",
    "\"minute\"",
    "\"hour\"",
    "\"day\"",
    "\"week\"",
    "\"month\"",
    "\"year\"",
    "\"where\"",
    "\"and\"",
    "\"graph\"",
    "\"optional\"",
    "\"union\"",
    "\"filter\"",
    "\"bound\"",
    "\"str\"",
    "\"datatype\"",
    "\"lang\"",
    "\"langmatches\"",
    "\"isURI\"",
    "\"isIRI\"",
    "\"isBlank\"",
    "\"isLiteral\"",
    "\"regex\"",
    "\"sameTerm\"",
    "\"true\"",
    "\"false\"",
    "<DIGITS>",
    "<INTEGER>",
    "<DECIMAL>",
    "<DOUBLE>",
    "<INTEGER_POSITIVE>",
    "<DECIMAL_POSITIVE>",
    "<DOUBLE_POSITIVE>",
    "<INTEGER_NEGATIVE>",
    "<DECIMAL_NEGATIVE>",
    "<DOUBLE_NEGATIVE>",
    "<EXPONENT>",
    "\"\\\"\\\"\\\"\"",
    "\"\\\'\\\'\\\'\"",
    "<ECHAR>",
    "<STRING_LITERAL1>",
    "<STRING_LITERAL2>",
    "<STRING_LITERAL_LONG1>",
    "<STRING_LITERAL_LONG2>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<SPECIAL_CHARS>",
    "\"(\"",
    "\")\"",
    "<NIL>",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "<ANON>",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"=\"",
    "\"!=\"",
    "\">\"",
    "\"<\"",
    "\"<=\"",
    "\">=\"",
    "\"!\"",
    "\"~\"",
    "\"||\"",
    "\"&&\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"^^\"",
    "\"@\"",
    "<PN_CHARS_BASE>",
    "<PN_CHARS_U>",
    "<PN_CHARS>",
    "<PN_PREFIX>",
    "<PN_LOCAL>",
    "<VARNAME>",
    "<UNKNOWN>",
  };

}
