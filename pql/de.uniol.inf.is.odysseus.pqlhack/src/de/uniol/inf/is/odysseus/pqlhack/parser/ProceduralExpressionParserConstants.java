/* Generated By:JJTree&JavaCC: Do not edit this line. ProceduralExpressionParserConstants.java */
package de.uniol.inf.is.odysseus.pqlhack.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ProceduralExpressionParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int K_ACCESS = 5;
  /** RegularExpression Id. */
  int K_AS = 6;
  /** RegularExpression Id. */
  int K_AND = 7;
  /** RegularExpression Id. */
  int K_AVG = 8;
  /** RegularExpression Id. */
  int K_BENCHMARK = 9;
  /** RegularExpression Id. */
  int K_BENCHMARK_EXT = 10;
  /** RegularExpression Id. */
  int K_BROKER = 11;
  /** RegularExpression Id. */
  int K_BUFFER = 12;
  /** RegularExpression Id. */
  int K_EXIST = 13;
  /** RegularExpression Id. */
  int K_NOT_EXIST = 14;
  /** RegularExpression Id. */
  int K_COUNT = 15;
  /** RegularExpression Id. */
  int K_DEFAULT = 16;
  /** RegularExpression Id. */
  int K_IN = 17;
  /** RegularExpression Id. */
  int K_JOIN = 18;
  /** RegularExpression Id. */
  int K_LOGICAL = 19;
  /** RegularExpression Id. */
  int K_MAX = 20;
  /** RegularExpression Id. */
  int K_MIN = 21;
  /** RegularExpression Id. */
  int K_NOT = 22;
  /** RegularExpression Id. */
  int K_OR = 23;
  /** RegularExpression Id. */
  int K_ON = 24;
  /** RegularExpression Id. */
  int K_PLAN = 25;
  /** RegularExpression Id. */
  int K_PREDICTION = 26;
  /** RegularExpression Id. */
  int K_PROJECTION = 27;
  /** RegularExpression Id. */
  int K_PUNCTUATION = 28;
  /** RegularExpression Id. */
  int K_QUEUE = 29;
  /** RegularExpression Id. */
  int K_RELATIONAL_PROJECTION = 30;
  /** RegularExpression Id. */
  int K_RELATIONAL_SELECTION = 31;
  /** RegularExpression Id. */
  int K_RELATIONAL_JOIN = 32;
  /** RegularExpression Id. */
  int K_RELATIONAL_NEST = 33;
  /** RegularExpression Id. */
  int K_RELATIONAL_UNNEST = 34;
  /** RegularExpression Id. */
  int K_SCHEMA_CONVERT = 35;
  /** RegularExpression Id. */
  int K_SELECTION = 36;
  /** RegularExpression Id. */
  int K_SET_PREDICTION = 37;
  /** RegularExpression Id. */
  int K_SET_PREDICTION_OR = 38;
  /** RegularExpression Id. */
  int K_SIZE = 39;
  /** RegularExpression Id. */
  int K_SLIDE = 40;
  /** RegularExpression Id. */
  int K_SLDING_TIME_WINDOW = 41;
  /** RegularExpression Id. */
  int K_SUM = 42;
  /** RegularExpression Id. */
  int K_TUMBLING_TIME_WINDOW = 43;
  /** RegularExpression Id. */
  int K_WHERE = 44;
  /** RegularExpression Id. */
  int K_TEST = 45;
  /** RegularExpression Id. */
  int K_ASSOCIATION_GEN = 46;
  /** RegularExpression Id. */
  int K_ASSOCIATION_EVAL = 47;
  /** RegularExpression Id. */
  int K_ASSOCIATION_SEL = 48;
  /** RegularExpression Id. */
  int K_ASSOCIATION_SRC = 49;
  /** RegularExpression Id. */
  int K_EVALUATE = 50;
  /** RegularExpression Id. */
  int K_FILTER_GAIN = 51;
  /** RegularExpression Id. */
  int K_FILTER_ESTIMATE = 52;
  /** RegularExpression Id. */
  int K_FILTER_COVARIANCE = 53;
  /** RegularExpression Id. */
  int K_BROKER_INIT = 54;
  /** RegularExpression Id. */
  int K_TMP_DATA_BOUNCER = 55;
  /** RegularExpression Id. */
  int K_JDVE_SINK = 56;
  /** RegularExpression Id. */
  int K_SCARS_XML_PROFILER = 57;
  /** RegularExpression Id. */
  int IDENTIFIER = 58;
  /** RegularExpression Id. */
  int LETTER = 59;
  /** RegularExpression Id. */
  int SPECIAL_CHARS = 60;
  /** RegularExpression Id. */
  int CHAR_LITERAL = 61;
  /** RegularExpression Id. */
  int QUOTED_IDENTIFIER = 62;
  /** RegularExpression Id. */
  int COMPARE_OPERATOR = 63;
  /** RegularExpression Id. */
  int REGEXP = 64;
  /** RegularExpression Id. */
  int FLOAT = 65;
  /** RegularExpression Id. */
  int INTEGER = 66;
  /** RegularExpression Id. */
  int DIGIT = 67;
  /** RegularExpression Id. */
  int IPADDRESS = 68;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"ACCESS\"",
    "\"AS\"",
    "\"AND\"",
    "\"AVG\"",
    "\"BENCHMARK\"",
    "\"BENCHMARK-EXT\"",
    "\"BROKER\"",
    "\"BUFFER\"",
    "\"EXIST\"",
    "\"NOT_EXIST\"",
    "\"COUNT\"",
    "\"DEFAULT\"",
    "\"IN\"",
    "\"JOIN\"",
    "\"LOGICAL\"",
    "\"MAX\"",
    "\"MIN\"",
    "\"NOT\"",
    "\"OR\"",
    "\"ON\"",
    "\"PLAN\"",
    "\"PREDICTION\"",
    "\"PROJECTION\"",
    "\"PUNCTUATION\"",
    "\"QUEUE\"",
    "\"RPROJECTION\"",
    "\"RSELECTION\"",
    "\"RJOIN\"",
    "\"RNEST\"",
    "\"RUNNEST\"",
    "\"SCHEMACONVERT\"",
    "\"SELECTION\"",
    "\"SET-PREDICTION\"",
    "\"SET-PREDICTION-OR\"",
    "\"SIZE\"",
    "\"SLIDE\"",
    "\"SLIDING-TIME-WINDOW\"",
    "\"SUM\"",
    "\"TUMBLING-TIME-WINDOW\"",
    "\"WHERE\"",
    "\"TESTOP\"",
    "\"ASSOCIATION-GEN\"",
    "\"ASSOCIATION-EVAL\"",
    "\"ASSOCIATION-SEL\"",
    "\"ASSOCIATION-SRC\"",
    "\"EVALUATE\"",
    "\"FILTER-GAIN\"",
    "\"FILTER-ESTIMATE\"",
    "\"FILTER-COVARIANCE\"",
    "\"BROKER-INIT\"",
    "\"TMP-DATA-BOUNCER\"",
    "\"JDVE-SINK\"",
    "\"SCARS-XML-PROFILER\"",
    "<IDENTIFIER>",
    "<LETTER>",
    "<SPECIAL_CHARS>",
    "<CHAR_LITERAL>",
    "<QUOTED_IDENTIFIER>",
    "<COMPARE_OPERATOR>",
    "<REGEXP>",
    "<FLOAT>",
    "<INTEGER>",
    "<DIGIT>",
    "<IPADDRESS>",
    "\":\"",
    "\";\"",
    "\"(\"",
    "\",\"",
    "\")\"",
    "\"*\"",
    "\"+\"",
    "\"-\"",
    "\"/\"",
    "\"^\"",
    "\":=\"",
  };

}
