/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/* Generated By:JJTree&JavaCC: Do not edit this line. MEPImpl.java */
package de.uniol.inf.is.odysseus.core.server.mep.impl;

@SuppressWarnings("all")
public class MEPImpl/*@bgen(jjtree)*/implements MEPImplTreeConstants, MEPImplConstants {/*@bgen(jjtree)*/
  protected JJTMEPImplState jjtree = new JJTMEPImplState();public static void main(String args [])
  {
    System.out.println("Reading from standard input...");
    System.out.print("Enter an expression like \u005c"1+(2+3)*var;\u005c" :");
    MEPImpl mep = new MEPImpl(System.in);
    try
    {
      SimpleNode n = mep.Expression();
      n.dump("");
      System.out.println("Thank you.");
    }
    catch (Exception e)
    {
      System.out.println("Oops.");
      System.out.println(e.getMessage());
    }
  }

  final public SimpleNode Expression() throws ParseException {
 /*@bgen(jjtree) Expression */
  ASTExpression jjtn000 = new ASTExpression(JJTEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      OrExpression();
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
    throw new Error("Missing return statement in function");
  }

  final public void UnaryExpression() throws ParseException {
    if (jj_2_1(2147483647)) {
      Function();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        Variable();
        break;
      case INTEGER:
      case FLOAT:
      case BOOLEAN:
      case CHAR_LITERAL:
        Constant();
        break;
      case 18:
        Negation();
        break;
      case LRND:
        jj_consume_token(LRND);
        OrExpression();
        jj_consume_token(RRND);
        break;
      case 17:
        UnaryMinus();
        break;
      case 33:
        Matrix();
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void UnaryMinus() throws ParseException {
    jj_consume_token(17);
    UnaryExpression();
    ASTFunction jjtn001 = new ASTFunction(JJTFUNCTION);
    boolean jjtc001 = true;
    jjtree.openNodeScope(jjtn001);
    try {
    jjtree.closeNodeScope(jjtn001,  1);
    jjtc001 = false;
    jjtn001.setSymbol("UnaryMinus");
    } finally {
    if (jjtc001) {
      jjtree.closeNodeScope(jjtn001,  1);
    }
    }
  }

  final public void Negation() throws ParseException {
    jj_consume_token(18);
    UnaryExpression();
    ASTFunction jjtn001 = new ASTFunction(JJTFUNCTION);
    boolean jjtc001 = true;
    jjtree.openNodeScope(jjtn001);
    try {
    jjtree.closeNodeScope(jjtn001,  1);
    jjtc001 = false;
    jjtn001.setSymbol("!");
    } finally {
    if (jjtc001) {
      jjtree.closeNodeScope(jjtn001,  1);
    }
    }
  }

  final public void PowExpression() throws ParseException {
    UnaryExpression();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 19:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
      jj_consume_token(19);
      UnaryExpression();
      ASTFunction jjtn001 = new ASTFunction(JJTFUNCTION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtn001.setSymbol("^");
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
      }
      }
    }
  }

  final public void MultiplicativeExpression() throws ParseException {
  Token t;
    PowExpression();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 20:
      case 21:
      case 22:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 20:
        t = jj_consume_token(20);
        break;
      case 21:
        t = jj_consume_token(21);
        break;
      case 22:
        t = jj_consume_token(22);
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      PowExpression();
      ASTFunction jjtn001 = new ASTFunction(JJTFUNCTION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtn001.setSymbol(t.image);
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
      }
      }
    }
  }

  final public void AdditiveExpression() throws ParseException {
    MultiplicativeExpression();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 17:
      case 23:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 23:
        jj_consume_token(23);
        MultiplicativeExpression();
      ASTFunction jjtn001 = new ASTFunction(JJTFUNCTION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
        try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtn001.setSymbol("+");
        } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
      }
        }
        break;
      case 17:
        jj_consume_token(17);
        MultiplicativeExpression();
    ASTFunction jjtn002 = new ASTFunction(JJTFUNCTION);
    boolean jjtc002 = true;
    jjtree.openNodeScope(jjtn002);
        try {
    jjtree.closeNodeScope(jjtn002,  2);
    jjtc002 = false;
    jjtn002.setSymbol("-");
        } finally {
    if (jjtc002) {
      jjtree.closeNodeScope(jjtn002,  2);
    }
        }
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void ComparisonExpression() throws ParseException {
  Token t;
    AdditiveExpression();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 24:
      case 25:
      case 26:
      case 27:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 24:
        t = jj_consume_token(24);
        break;
      case 25:
        t = jj_consume_token(25);
        break;
      case 26:
        t = jj_consume_token(26);
        break;
      case 27:
        t = jj_consume_token(27);
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      AdditiveExpression();
      ASTFunction jjtn001 = new ASTFunction(JJTFUNCTION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtn001.setSymbol(t.image);
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
      }
      }
    }
  }

  final public void EqualityComparison() throws ParseException {
 Token t = null;
    ComparisonExpression();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 28:
      case 29:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 28:
        t = jj_consume_token(28);
        break;
      case 29:
        t = jj_consume_token(29);
        break;
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      ComparisonExpression();
      ASTFunction jjtn001 = new ASTFunction(JJTFUNCTION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtn001.setSymbol(t.image);
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
      }
      }
    }
  }

  final public void AndExpression() throws ParseException {
    EqualityComparison();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case K_AND:
      case 30:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 30:
        jj_consume_token(30);
        break;
      case K_AND:
        jj_consume_token(K_AND);
        break;
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      EqualityComparison();
      ASTFunction jjtn001 = new ASTFunction(JJTFUNCTION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtn001.setSymbol("&&");
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
      }
      }
    }
  }

  final public void OrExpression() throws ParseException {
    AndExpression();
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case K_OR:
      case 31:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 31:
        jj_consume_token(31);
        break;
      case K_OR:
        jj_consume_token(K_OR);
        break;
      default:
        jj_la1[13] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      AndExpression();
      ASTFunction jjtn001 = new ASTFunction(JJTFUNCTION);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
      jjtree.closeNodeScope(jjtn001,  2);
      jjtc001 = false;
      jjtn001.setSymbol("||");
      } finally {
      if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  2);
      }
      }
    }
  }

  final public void Constant() throws ParseException {
 /*@bgen(jjtree) Constant */
  ASTConstant jjtn000 = new ASTConstant(JJTCONSTANT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);Token t;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CHAR_LITERAL:
        t = jj_consume_token(CHAR_LITERAL);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    jjtn000.setValue(t.image.substring(1, t.image.length() - 1));
        break;
      case FLOAT:
        t = jj_consume_token(FLOAT);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    jjtn000.setValue(Double.parseDouble(t.image));
        break;
      case INTEGER:
        t = jj_consume_token(INTEGER);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    jjtn000.setValue(Double.parseDouble(t.image));
        break;
      case BOOLEAN:
        t = jj_consume_token(BOOLEAN);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    jjtn000.setValue(Boolean.parseBoolean(t.image));
        break;
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void Function() throws ParseException {
 /*@bgen(jjtree) Function */
  ASTFunction jjtn000 = new ASTFunction(JJTFUNCTION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);Token t;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        t = jj_consume_token(IDENTIFIER);
    jjtn000.setSymbol(t.image);
        jj_consume_token(LRND);
        ArgumentList();
        jj_consume_token(RRND);
        break;
      case LRND:
        jj_consume_token(LRND);
        jj_consume_token(RRND);
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void Variable() throws ParseException {
 /*@bgen(jjtree) Variable */
  ASTVariable jjtn000 = new ASTVariable(JJTVARIABLE);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(IDENTIFIER);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    jjtn000.setIdentifier(t.image);
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void ArgumentList() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER:
    case FLOAT:
    case BOOLEAN:
    case CHAR_LITERAL:
    case IDENTIFIER:
    case LRND:
    case 17:
    case 18:
    case 33:
      OrExpression();
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 32:
          ;
          break;
        default:
          jj_la1[16] = jj_gen;
          break label_8;
        }
        jj_consume_token(32);
        OrExpression();
      }
      break;
    default:
      jj_la1[17] = jj_gen;
      ;
    }
  }

  final public void Matrix() throws ParseException {
 /*@bgen(jjtree) Matrix */
  ASTMatrix jjtn000 = new ASTMatrix(JJTMATRIX);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(33);
      MatrixLine();
      label_9:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 34:
          ;
          break;
        default:
          jj_la1[18] = jj_gen;
          break label_9;
        }
        jj_consume_token(34);
        MatrixLine();
      }
      jj_consume_token(35);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public void MatrixLine() throws ParseException {
 /*@bgen(jjtree) MatrixLine */
  ASTMatrixLine jjtn000 = new ASTMatrixLine(JJTMATRIXLINE);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      OrExpression();
      label_10:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 32:
          ;
          break;
        default:
          jj_la1[19] = jj_gen;
          break label_10;
        }
        jj_consume_token(32);
        OrExpression();
      }
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_3_1() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(LRND)) return true;
    return false;
  }

  /** Generated Token Manager. */
  public MEPImplTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[20];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x71b80,0x80000,0x700000,0x700000,0x820000,0x820000,0xf000000,0xf000000,0x30000000,0x30000000,0x40000020,0x40000020,0x80000040,0x80000040,0xb80,0x11000,0x0,0x71b80,0x0,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x2,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x1,0x2,0x4,0x1,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public MEPImpl(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public MEPImpl(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new MEPImplTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public MEPImpl(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new MEPImplTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public MEPImpl(MEPImplTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(MEPImplTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[36];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 20; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 36; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 1; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
