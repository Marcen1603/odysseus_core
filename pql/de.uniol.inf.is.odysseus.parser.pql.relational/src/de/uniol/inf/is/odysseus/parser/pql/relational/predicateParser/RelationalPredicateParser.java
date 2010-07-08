/* Generated By:JavaCC: Do not edit this line. RelationalPredicateParser.java */
package de.uniol.inf.is.odysseus.parser.pql.relational.predicateParser;
import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

@SuppressWarnings("all")
public class RelationalPredicateParser implements RelationalPredicateParserConstants {
  private IAttributeResolver resolver;

  public void setAttributeResolver(IAttributeResolver resolver)
  {
    this.resolver = resolver;
  }

  final public IPredicate < RelationalTuple <? > > Predicate() throws ParseException {
  IPredicate < RelationalTuple <? > > predicate;
    if (jj_2_1(2147483647)) {
      predicate = OrPredicate();
    } else if (jj_2_2(2147483647)) {
      predicate = AndPredicate();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case K_NOT:
        predicate = NotPredicate();
        break;
      case FLOAT:
      case INTEGER:
      case IDENTIFIER:
      case CHAR_LITERAL:
      case 16:
      case 20:
        predicate = SimplePredicate();
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    {if (true) return predicate;}
    throw new Error("Missing return statement in function");
  }

  final public OrPredicate OrPredicate() throws ParseException {
  IPredicate < RelationalTuple <? > > first, second;
    first = SimplePredicate();
    jj_consume_token(K_OR);
    second = Predicate();
    {if (true) return new OrPredicate(first, second);}
    throw new Error("Missing return statement in function");
  }

  final public AndPredicate AndPredicate() throws ParseException {
  IPredicate < RelationalTuple <? > > first, second;
    first = SimplePredicate();
    jj_consume_token(K_AND);
    second = Predicate();
    {if (true) return new AndPredicate(first, second);}
    throw new Error("Missing return statement in function");
  }

  final public IPredicate < RelationalTuple <? > > NotPredicate() throws ParseException {
  IPredicate < RelationalTuple <? > > predicate;
    jj_consume_token(K_NOT);
    predicate = Predicate();
    {if (true) return new NotPredicate(predicate);}
    throw new Error("Missing return statement in function");
  }

  final public IPredicate < RelationalTuple <? > > SimplePredicate() throws ParseException {
  IPredicate < RelationalTuple <? > > predicate;
    if (jj_2_3(2147483647)) {
      jj_consume_token(16);
      predicate = Predicate();
      jj_consume_token(17);
    {if (true) return predicate;}
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FLOAT:
      case INTEGER:
      case IDENTIFIER:
      case CHAR_LITERAL:
      case 16:
      case 20:
        predicate = BasicPredicate();
    {if (true) return predicate;}
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public IPredicate < RelationalTuple <? > > BasicPredicate() throws ParseException {
  IPredicate < RelationalTuple <? > > predicate;
  String first, second, cOp;
    first = Expression();
    cOp = CompareOperator();
    second = Expression();
    SDFExpression exp = new SDFExpression("", first+ cOp+ second, resolver);
    {if (true) return new RelationalPredicate(exp);}
    throw new Error("Missing return statement in function");
  }

  final public String Expression() throws ParseException {
  String first;
  String tOp;
  String second;
    if (jj_2_4(2147483647)) {
      first = SimpleToken();
      tOp = MathOperator();
      second = Expression();
    {if (true) return first+ tOp+ second;}
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FLOAT:
      case INTEGER:
      case IDENTIFIER:
      case CHAR_LITERAL:
      case 16:
      case 20:
        first = SimpleToken();
    {if (true) return first;}
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public String MathOperator() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 18:
      jj_consume_token(18);
    {if (true) return "*";}
      break;
    case 19:
      jj_consume_token(19);
    {if (true) return "+";}
      break;
    case 20:
      jj_consume_token(20);
    {if (true) return "-";}
      break;
    case 21:
      jj_consume_token(21);
    {if (true) return "/";}
      break;
    case 22:
      jj_consume_token(22);
    {if (true) return "^";}
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public String SimpleToken() throws ParseException {
  String value;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FLOAT:
    case INTEGER:
      value = Number();
    {if (true) return value;}
      break;
    case CHAR_LITERAL:
      value = String();
    {if (true) return value;}
      break;
    default:
      jj_la1[4] = jj_gen;
      if (jj_2_5(2147483647)) {
        value = FunctionExpression();
    {if (true) return value;}
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 16:
          jj_consume_token(16);
          value = Expression();
          jj_consume_token(17);
    {if (true) return "("+ value+ ")";}
          break;
        case 20:
          jj_consume_token(20);
          value = Identifier();
    {if (true) return "-"+ value;}
          break;
        case IDENTIFIER:
          value = Identifier();
    {if (true) return value;}
          break;
        default:
          jj_la1[5] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public String String() throws ParseException {
  Token t;
    t = jj_consume_token(CHAR_LITERAL);
    {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public String Number() throws ParseException {
  Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FLOAT:
      t = jj_consume_token(FLOAT);
      break;
    case INTEGER:
      t = jj_consume_token(INTEGER);
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public String FunctionExpression() throws ParseException {
  String tmp;
  StringBuilder result = new StringBuilder();
    tmp = Identifier();
    result.append(tmp);
    result.append('(');
    jj_consume_token(16);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FLOAT:
    case INTEGER:
    case IDENTIFIER:
    case CHAR_LITERAL:
    case 16:
    case 20:
      tmp = Expression();
      result.append(tmp);
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 23:
          ;
          break;
        default:
          jj_la1[7] = jj_gen;
          break label_1;
        }
        jj_consume_token(23);
        tmp = Expression();
        result.append(",");
        result.append(tmp);
      }
      break;
    default:
      jj_la1[8] = jj_gen;
      ;
    }
    jj_consume_token(17);
    result.append(')');
    {if (true) return result.toString();}
    throw new Error("Missing return statement in function");
  }

  final public String Identifier() throws ParseException {
  Token t;
    t = jj_consume_token(IDENTIFIER);
    {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public String CompareOperator() throws ParseException {
  Token t;
    t = jj_consume_token(COMPARE_OPERATOR);
    if (t.image.equals("="))
    {
      {if (true) return "==";}
    }
    if (t.image.equals("<>"
    )
    )
    {
      {if (true) return "!=";}
    }
    {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  private boolean jj_3R_4() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_9()) {
    jj_scanpos = xsp;
    if (jj_3R_10()) {
    jj_scanpos = xsp;
    if (jj_3R_11()) {
    jj_scanpos = xsp;
    if (jj_3R_12()) return true;
    }
    }
    }
    return false;
  }

  private boolean jj_3R_25() {
    if (jj_3R_31()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_32()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3_4() {
    if (jj_3R_5()) return true;
    if (jj_3R_6()) return true;
    return false;
  }

  private boolean jj_3R_35() {
    if (jj_3R_5()) return true;
    return false;
  }

  private boolean jj_3R_7() {
    if (jj_3R_24()) return true;
    if (jj_scan_token(16)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_25()) jj_scanpos = xsp;
    if (jj_scan_token(17)) return true;
    return false;
  }

  private boolean jj_3R_34() {
    if (jj_3R_5()) return true;
    if (jj_3R_6()) return true;
    if (jj_3R_31()) return true;
    return false;
  }

  private boolean jj_3R_31() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_34()) {
    jj_scanpos = xsp;
    if (jj_3R_35()) return true;
    }
    return false;
  }

  private boolean jj_3R_29() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(8)) {
    jj_scanpos = xsp;
    if (jj_scan_token(9)) return true;
    }
    return false;
  }

  private boolean jj_3R_33() {
    if (jj_3R_31()) return true;
    if (jj_3R_36()) return true;
    if (jj_3R_31()) return true;
    return false;
  }

  private boolean jj_3_3() {
    if (jj_scan_token(16)) return true;
    if (jj_3R_4()) return true;
    return false;
  }

  private boolean jj_3R_30() {
    if (jj_scan_token(CHAR_LITERAL)) return true;
    return false;
  }

  private boolean jj_3R_27() {
    if (jj_3R_33()) return true;
    return false;
  }

  private boolean jj_3R_8() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_26()) {
    jj_scanpos = xsp;
    if (jj_3R_27()) return true;
    }
    return false;
  }

  private boolean jj_3R_26() {
    if (jj_scan_token(16)) return true;
    if (jj_3R_4()) return true;
    if (jj_scan_token(17)) return true;
    return false;
  }

  private boolean jj_3R_18() {
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3_5() {
    if (jj_3R_7()) return true;
    return false;
  }

  private boolean jj_3R_17() {
    if (jj_scan_token(20)) return true;
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_16() {
    if (jj_scan_token(16)) return true;
    if (jj_3R_31()) return true;
    if (jj_scan_token(17)) return true;
    return false;
  }

  private boolean jj_3R_28() {
    if (jj_scan_token(K_NOT)) return true;
    if (jj_3R_4()) return true;
    return false;
  }

  private boolean jj_3R_15() {
    if (jj_3R_7()) return true;
    return false;
  }

  private boolean jj_3R_36() {
    if (jj_scan_token(COMPARE_OPERATOR)) return true;
    return false;
  }

  private boolean jj_3R_14() {
    if (jj_3R_30()) return true;
    return false;
  }

  private boolean jj_3R_3() {
    if (jj_3R_8()) return true;
    if (jj_scan_token(K_AND)) return true;
    if (jj_3R_4()) return true;
    return false;
  }

  private boolean jj_3R_13() {
    if (jj_3R_29()) return true;
    return false;
  }

  private boolean jj_3R_5() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_13()) {
    jj_scanpos = xsp;
    if (jj_3R_14()) {
    jj_scanpos = xsp;
    if (jj_3R_15()) {
    jj_scanpos = xsp;
    if (jj_3R_16()) {
    jj_scanpos = xsp;
    if (jj_3R_17()) {
    jj_scanpos = xsp;
    if (jj_3R_18()) return true;
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_24() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  private boolean jj_3R_2() {
    if (jj_3R_8()) return true;
    if (jj_scan_token(K_OR)) return true;
    if (jj_3R_4()) return true;
    return false;
  }

  private boolean jj_3R_23() {
    if (jj_scan_token(22)) return true;
    return false;
  }

  private boolean jj_3_2() {
    if (jj_3R_3()) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_3R_2()) return true;
    return false;
  }

  private boolean jj_3R_22() {
    if (jj_scan_token(21)) return true;
    return false;
  }

  private boolean jj_3R_21() {
    if (jj_scan_token(20)) return true;
    return false;
  }

  private boolean jj_3R_12() {
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3R_32() {
    if (jj_scan_token(23)) return true;
    if (jj_3R_31()) return true;
    return false;
  }

  private boolean jj_3R_11() {
    if (jj_3R_28()) return true;
    return false;
  }

  private boolean jj_3R_10() {
    if (jj_3R_3()) return true;
    return false;
  }

  private boolean jj_3R_20() {
    if (jj_scan_token(19)) return true;
    return false;
  }

  private boolean jj_3R_9() {
    if (jj_3R_2()) return true;
    return false;
  }

  private boolean jj_3R_6() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_19()) {
    jj_scanpos = xsp;
    if (jj_3R_20()) {
    jj_scanpos = xsp;
    if (jj_3R_21()) {
    jj_scanpos = xsp;
    if (jj_3R_22()) {
    jj_scanpos = xsp;
    if (jj_3R_23()) return true;
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_19() {
    if (jj_scan_token(18)) return true;
    return false;
  }

  /** Generated Token Manager. */
  public RelationalPredicateParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[9];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x114b40,0x114b00,0x114b00,0x7c0000,0x4300,0x110800,0x300,0x800000,0x114b00,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[5];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public RelationalPredicateParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public RelationalPredicateParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new RelationalPredicateParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
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
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public RelationalPredicateParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new RelationalPredicateParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public RelationalPredicateParser(RelationalPredicateParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(RelationalPredicateParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
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
    boolean[] la1tokens = new boolean[24];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 9; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 24; i++) {
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
    for (int i = 0; i < 5; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
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
