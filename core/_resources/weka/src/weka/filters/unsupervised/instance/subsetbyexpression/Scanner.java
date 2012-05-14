/* The following code was generated by JFlex 1.4.2 on 11/01/09 11:41 AM */

/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 * Scanner.java
 * Copyright (C) 2008 University of Waikato, Hamilton, New Zealand
 */

package weka.filters.unsupervised.instance.subsetbyexpression;

import java_cup.runtime.SymbolFactory;
import java.io.*;

/**
 * A scanner for evaluating whether an Instance is to be included in a subset
 * or not.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 4942 $
 */

public class Scanner implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int STRING = 2;
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\43\1\50\1\0\2\43\22\0\1\43\6\0\1\33\1\45"+
    "\1\46\1\3\1\2\1\44\1\1\1\35\1\4\12\34\2\0\1\5"+
    "\1\6\1\7\2\0\1\36\1\0\1\40\10\0\1\41\6\0\1\42"+
    "\1\37\14\0\1\15\1\24\1\31\1\16\1\21\1\22\1\26\1\0"+
    "\1\10\2\0\1\23\1\47\1\12\1\13\1\30\1\25\1\17\1\11"+
    "\1\14\1\20\1\0\1\32\1\27\uff87\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7"+
    "\1\10\14\1\1\11\1\12\2\1\1\13\1\14\1\15"+
    "\1\16\1\17\1\20\1\21\1\22\1\23\3\0\1\24"+
    "\14\0\1\12\3\0\1\25\1\0\1\26\1\27\1\0"+
    "\1\30\1\31\1\0\1\32\2\0\1\33\1\34\1\35"+
    "\4\0\1\36\1\37\1\40\2\0\1\41\1\42\2\0"+
    "\1\43\1\44\1\45\3\0\1\46";

  private static int [] zzUnpackAction() {
    int [] result = new int[89];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\51\0\122\0\173\0\122\0\122\0\122\0\244"+
    "\0\122\0\315\0\366\0\u011f\0\u0148\0\u0171\0\u019a\0\u01c3"+
    "\0\u01ec\0\u0215\0\u023e\0\u0267\0\u0290\0\u02b9\0\122\0\u02e2"+
    "\0\u030b\0\u0334\0\122\0\122\0\122\0\122\0\122\0\122"+
    "\0\122\0\122\0\u035d\0\u0386\0\u03af\0\u03d8\0\122\0\u0401"+
    "\0\u042a\0\u0453\0\u047c\0\u04a5\0\u04ce\0\u04f7\0\u0520\0\u0549"+
    "\0\u0572\0\u059b\0\u05c4\0\u05ed\0\u0616\0\u063f\0\u0668\0\122"+
    "\0\u0691\0\122\0\122\0\u06ba\0\122\0\122\0\u06e3\0\122"+
    "\0\u070c\0\u0735\0\122\0\122\0\122\0\u075e\0\u0787\0\u07b0"+
    "\0\u07d9\0\122\0\122\0\122\0\u0802\0\u082b\0\122\0\u0787"+
    "\0\u0854\0\u087d\0\122\0\122\0\122\0\u08a6\0\u08cf\0\u08f8"+
    "\0\122";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[89];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12"+
    "\1\13\1\14\1\15\1\16\1\17\1\20\1\3\1\21"+
    "\1\3\1\22\1\23\1\24\4\3\1\25\1\26\1\3"+
    "\1\27\1\30\1\3\1\31\1\3\1\32\2\3\1\33"+
    "\1\34\1\35\1\36\1\3\1\33\33\37\1\40\14\37"+
    "\106\0\1\30\22\0\1\41\50\0\1\42\53\0\1\43"+
    "\47\0\1\44\14\0\1\45\36\0\1\46\54\0\1\47"+
    "\46\0\1\50\1\0\1\51\43\0\1\52\11\0\1\53"+
    "\34\0\1\54\67\0\1\55\36\0\1\56\5\0\1\57"+
    "\40\0\1\60\50\0\1\61\50\0\1\62\5\0\1\63"+
    "\63\0\1\30\1\64\52\0\1\65\52\0\1\66\56\0"+
    "\1\67\13\0\1\70\55\0\1\71\45\0\1\72\46\0"+
    "\1\73\56\0\1\74\46\0\1\75\43\0\1\76\51\0"+
    "\1\77\66\0\1\100\43\0\1\101\40\0\1\102\63\0"+
    "\1\103\54\0\1\104\27\0\1\105\47\0\1\106\74\0"+
    "\1\64\53\0\1\107\47\0\1\110\22\0\1\111\54\0"+
    "\1\112\55\0\1\113\43\0\1\114\45\0\1\115\52\0"+
    "\1\116\60\0\1\117\61\0\1\120\56\0\1\121\17\0"+
    "\1\122\60\0\1\123\46\0\1\124\73\0\1\125\17\0"+
    "\1\126\47\0\1\127\52\0\1\130\64\0\1\131\22\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[2337];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\1\1\3\11\1\1\1\11\15\1\1\11"+
    "\3\1\10\11\1\1\3\0\1\11\14\0\1\1\3\0"+
    "\1\11\1\0\2\11\1\0\2\11\1\0\1\11\2\0"+
    "\3\11\4\0\3\11\2\0\1\11\1\1\2\0\3\11"+
    "\3\0\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[89];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */
  // Author: FracPete (fracpete at waikato dot ac dot nz)
  // Version: $Revision: 4942 $
  protected SymbolFactory m_SymFactory;

  protected StringBuffer m_String = new StringBuffer();

  public Scanner(InputStream r, SymbolFactory sf) {
    this(r);
    m_SymFactory = sf;
  }


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Scanner(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public Scanner(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 112) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      yychar+= zzMarkedPosL-zzStartRead;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 9: 
          { yybegin(STRING); m_String.setLength(0);
          }
        case 39: break;
        case 25: 
          { return m_SymFactory.newSymbol("Abs", sym.ABS);
          }
        case 40: break;
        case 1: 
          { System.err.println("Illegal character: " + yytext());
          }
        case 41: break;
        case 15: 
          { m_String.append(yytext());
          }
        case 42: break;
        case 21: 
          { return m_SymFactory.newSymbol("Sin", sym.SIN);
          }
        case 43: break;
        case 14: 
          { return m_SymFactory.newSymbol("Right Bracket", sym.RPAREN);
          }
        case 44: break;
        case 11: 
          { /* ignore white space. */
          }
        case 45: break;
        case 17: 
          { return m_SymFactory.newSymbol("Less or equal than", sym.LE);
          }
        case 46: break;
        case 28: 
          { return m_SymFactory.newSymbol("Pow", sym.POW);
          }
        case 47: break;
        case 8: 
          { return m_SymFactory.newSymbol("Greater than", sym.GT);
          }
        case 48: break;
        case 16: 
          { yybegin(YYINITIAL); return m_SymFactory.newSymbol("String", sym.STRING, new String(m_String.toString()));
          }
        case 49: break;
        case 23: 
          { return m_SymFactory.newSymbol("Tan", sym.TAN);
          }
        case 50: break;
        case 12: 
          { return m_SymFactory.newSymbol("Comma", sym.COMMA);
          }
        case 51: break;
        case 19: 
          { return m_SymFactory.newSymbol("Is", sym.IS);
          }
        case 52: break;
        case 13: 
          { return m_SymFactory.newSymbol("Left Bracket", sym.LPAREN);
          }
        case 53: break;
        case 29: 
          { return m_SymFactory.newSymbol("Cos", sym.COS);
          }
        case 54: break;
        case 33: 
          { return m_SymFactory.newSymbol("Ceil", sym.CEIL);
          }
        case 55: break;
        case 6: 
          { return m_SymFactory.newSymbol("Less than", sym.LT);
          }
        case 56: break;
        case 4: 
          { return m_SymFactory.newSymbol("Times", sym.TIMES);
          }
        case 57: break;
        case 37: 
          { return m_SymFactory.newSymbol("Class", sym.ATTRIBUTE, new String(yytext()));
          }
        case 58: break;
        case 36: 
          { return m_SymFactory.newSymbol("Floor", sym.FLOOR);
          }
        case 59: break;
        case 27: 
          { return m_SymFactory.newSymbol("Log", sym.LOG);
          }
        case 60: break;
        case 35: 
          { return m_SymFactory.newSymbol("False", sym.FALSE);
          }
        case 61: break;
        case 31: 
          { return m_SymFactory.newSymbol("True", sym.TRUE);
          }
        case 62: break;
        case 7: 
          { return m_SymFactory.newSymbol("Equals", sym.EQ);
          }
        case 63: break;
        case 18: 
          { return m_SymFactory.newSymbol("Greater or equal than", sym.GE);
          }
        case 64: break;
        case 26: 
          { return m_SymFactory.newSymbol("Exp", sym.EXP);
          }
        case 65: break;
        case 20: 
          { return m_SymFactory.newSymbol("Or", sym.OR);
          }
        case 66: break;
        case 34: 
          { return m_SymFactory.newSymbol("Attribute", sym.ATTRIBUTE, new String(yytext()));
          }
        case 67: break;
        case 30: 
          { return m_SymFactory.newSymbol("Sqrt", sym.SQRT);
          }
        case 68: break;
        case 5: 
          { return m_SymFactory.newSymbol("Division", sym.DIVISION);
          }
        case 69: break;
        case 22: 
          { return m_SymFactory.newSymbol("Not", sym.NOT);
          }
        case 70: break;
        case 10: 
          { return m_SymFactory.newSymbol("Number", sym.NUMBER, new Double(yytext()));
          }
        case 71: break;
        case 3: 
          { return m_SymFactory.newSymbol("Plus", sym.PLUS);
          }
        case 72: break;
        case 38: 
          { return m_SymFactory.newSymbol("Missing", sym.ISMISSING);
          }
        case 73: break;
        case 32: 
          { return m_SymFactory.newSymbol("Rint", sym.RINT);
          }
        case 74: break;
        case 24: 
          { return m_SymFactory.newSymbol("And", sym.AND);
          }
        case 75: break;
        case 2: 
          { return m_SymFactory.newSymbol("Minus", sym.MINUS);
          }
        case 76: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
              {     return m_SymFactory.newSymbol("EOF",sym.EOF);
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
