/* Generated By:JavaCC: Do not edit this line. PQLParserImpl.java */
package de.uniol.inf.is.odysseus.parser.pql.impl;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.InputOperatorItem;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.logicaloperator.builder.PredicateItem;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ValidationException;
import de.uniol.inf.is.odysseus.parser.pql.PQLParser;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

@SuppressWarnings("all")
public class PQLParserImpl implements PQLParserImplConstants {
  static private ILogicalOperator createOperator(String identifier, Map < String, Object > parameters, List < InputOperatorItem > inputOps)
  {
    IOperatorBuilder builder = OperatorBuilderFactory.createOperatorBuilder(identifier.toUpperCase(), getUser());
    List < ILogicalOperator > inputOperators = new ArrayList < ILogicalOperator > ();
    for (int i = 0; i < inputOps.size(); ++i)
    {
      builder.setInputOperator(i, inputOps.get(i).operator, inputOps.get(i).outputPort);
    }
    PQLParser.initParameters(builder.getParameters(), parameters);
    if (!builder.validate())
    {
      throw new ValidationException(identifier, builder.getErrors());
    }
    ILogicalOperator operator = builder.createOperator();
    return operator;
  }

  static private Set < ILogicalOperator > findRoots(ILogicalOperator op)
  {
    return findRoots(op, new ArrayList < ILogicalOperator > ());
  }

  static private Set < ILogicalOperator > findRoots(ILogicalOperator op, List < ILogicalOperator > visited)
  {
    Set < ILogicalOperator > result = new HashSet < ILogicalOperator > ();
    if (!visited.contains(op))
    {
      visited.add(op);
      Collection < LogicalSubscription > subscriptions = op.getSubscriptions();
      if (subscriptions.isEmpty())
      {
        return Collections.singleton(op);
      }
      for (LogicalSubscription sub : subscriptions)
      {
        result.addAll(findRoots(sub.getTarget(), visited));
      }
    }
    return result;
  }

   static User user;

  static public User getUser(){
        return user;
  }

  static public void setUser(User newUser)
  {
        user = newUser;
  }

  static final public List < IQuery > query() throws ParseException {
  Map < String, ILogicalOperator > namedOps = new HashMap < String, ILogicalOperator > ();
  Map < String, Map < String, Object > > namedOpParameters = new HashMap < String, Map < String, Object > > ();
  ILogicalOperator outputStream;
    label_1:
    while (true) {
      namedStream(namedOps, namedOpParameters);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
    jj_consume_token(0);
    LinkedList < ILogicalOperator > result = new LinkedList < ILogicalOperator > ();
    Set < ILogicalOperator > roots = new HashSet < ILogicalOperator > ();
    for (ILogicalOperator op : namedOps.values())
    {
      roots.addAll(findRoots(op));
    }
    List < IQuery > queries = new ArrayList < IQuery > ();
    BeanInfo beanInfo;
    try
    {
      beanInfo = Introspector.getBeanInfo(IQuery.class);
    }
    catch (IntrospectionException e)
    {
      {
        if (true) {if (true) throw new RuntimeException(e);}
      }
    }
    PropertyDescriptor [ ] properties = beanInfo.getPropertyDescriptors();
    for (Map.Entry < String, ILogicalOperator > opEntry : namedOps.entrySet())
    {
      String queryName = opEntry.getKey();
      ILogicalOperator topOperator = opEntry.getValue();
      if (roots.contains(topOperator))
      {
        IQuery query = new Query();
        query.setLogicalPlan(topOperator);
        queries.add(query);
        PQLParser.initQueryParameters(namedOpParameters.get(queryName));
        for (String parameterName : namedOpParameters.get(queryName).keySet())
        {
          boolean found = false;
          for (PropertyDescriptor property : properties)
          {
            if (property.getName().equalsIgnoreCase(parameterName))
            {
              Method method = property.getWriteMethod();
              try
              {
                method.invoke(query, PQLParser.getQueryParameter(parameterName).getValue());
              }
              catch (Exception e)
              {
                {if (true) throw new RuntimeException(e);}
              }
              found = true;
            }
          }
          if (!found)
          {
            {if (true) throw new RuntimeException("unknown parameter for query: " + parameterName);}
          }
        }
      }
      else
      {
        if (!namedOpParameters.get(queryName).isEmpty())
        {
          {
            if (true)
            {
              if (true) {if (true) throw new IllegalArgumentException("query parameters can only be defined for root operators, not for: " + queryName);}
            }
          }
        }
      }
    }
    {if (true) return queries;}
    throw new Error("Missing return statement in function");
  }

  static final public void namedStream(Map < String, ILogicalOperator > namedOps, Map < String, Map < String, Object > > namedOpParameters) throws ParseException {
  Token name;
  ILogicalOperator op;
  boolean isView = false;
  Map < String, Object > parameters = new HashMap < String, Object > ();
    name = jj_consume_token(IDENTIFIER);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 16:
      jj_consume_token(16);
      parameters = parameterMap();
      jj_consume_token(17);
      break;
    default:
      jj_la1[1] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 18:
      jj_consume_token(18);
      break;
    case 19:
      jj_consume_token(19);
      isView = true;
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    op = operator(namedOps);
    String nameStr = name.image.toUpperCase();
    if (namedOps.containsKey(nameStr))
    {
      {if (true) throw new IllegalArgumentException("multiple definition of '" + nameStr + "'");}
    }
    if (isView)
    {
      nameStr = name.image;
      DataDictionary dd = DataDictionary.getInstance();
      if (dd.containsView(nameStr, getUser()))
      {
        {if (true) throw new IllegalArgumentException("multiple definition of view '" + nameStr + "'");}
      }
      SDFEntity entity = new SDFEntity(nameStr);
      entity.setAttributes(op.getOutputSchema());
      DataDictionary.getInstance().addSourceType(nameStr, "RelationalStreaming");
      DataDictionary.getInstance().addEntity(nameStr, entity, getUser());
      dd.setView(nameStr, op, user);
      //get access operator for view, so other operators don't get subscribed
      //to top operator of the view
      op = dd.getView(nameStr, getUser());
    }
    namedOpParameters.put(nameStr.toUpperCase(), parameters);
    namedOps.put(nameStr.toUpperCase(), op);
  }

  static final public ILogicalOperator operator(Map < String, ILogicalOperator > namedOps) throws ParseException {
  Token identifier;
  Token port;
  Map < String, Object > parameters = new HashMap < String, Object > ();
  List < InputOperatorItem > inputOps = new ArrayList < InputOperatorItem > ();
    if (jj_2_1(2147483647)) {
      identifier = jj_consume_token(IDENTIFIER);
      jj_consume_token(20);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 16:
        jj_consume_token(16);
        parameters = parameterMap();
        jj_consume_token(17);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 21:
          jj_consume_token(21);
          inputOps = operatorList(namedOps);
          break;
        default:
          jj_la1[3] = jj_gen;
          ;
        }
        break;
      default:
        jj_la1[4] = jj_gen;
        inputOps = operatorList(namedOps);
      }
      jj_consume_token(22);
      {if (true) return createOperator(identifier.image, parameters, inputOps);}
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        identifier = jj_consume_token(IDENTIFIER);
      ILogicalOperator op = namedOps.get(identifier.image.toUpperCase());
      if (op == null)
      {
        {if (true) throw new IllegalArgumentException("no such operator: " + identifier.image);}
      }
      {if (true) return op;}
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  static final public Map < String, Object > parameterMap() throws ParseException {
  Map < String, Object > parameters = new HashMap < String, Object > ();
    parameter(parameters);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 21:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_2;
      }
      jj_consume_token(21);
      parameter(parameters);
    }
    {if (true) return parameters;}
    throw new Error("Missing return statement in function");
  }

  static final public void parameter(Map < String, Object > parameters) throws ParseException {
  Token id;
  Object value;
    id = jj_consume_token(IDENTIFIER);
    jj_consume_token(18);
    value = parameterValue();
    parameters.put(id.image.toUpperCase(), value);
  }

  static final public Object parameterValue() throws ParseException {
  Token t;
  Object value;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FLOAT:
      t = jj_consume_token(FLOAT);
      value = Double.valueOf(t.image);
      break;
    case INTEGER:
      t = jj_consume_token(INTEGER);
      value = Long.valueOf(t.image);
      break;
    case CHAR_LITERAL:
      t = jj_consume_token(CHAR_LITERAL);
      value = t.image.substring(1, t.image.length() - 1);
      break;
    case 23:
      value = list();
      break;
    case IDENTIFIER:
      value = predicate();
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return value;}
    throw new Error("Missing return statement in function");
  }

  static final public List < Object > list() throws ParseException {
  Object value;
  List < Object > list = new ArrayList < Object > ();
    jj_consume_token(23);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
    case CHAR_LITERAL:
    case FLOAT:
    case INTEGER:
    case 23:
      value = parameterValue();
      list.add(value);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 21:
          ;
          break;
        default:
          jj_la1[8] = jj_gen;
          break label_3;
        }
        jj_consume_token(21);
        value = parameterValue();
        list.add(value);
      }
      break;
    default:
      jj_la1[9] = jj_gen;
      ;
    }
    jj_consume_token(24);
    {if (true) return list;}
    throw new Error("Missing return statement in function");
  }

  static final public List < InputOperatorItem > operatorList(Map < String, ILogicalOperator > namedOps) throws ParseException {
  List list = new LinkedList();
  Token tPort = null;
  ILogicalOperator operator;
  int port = 0;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
    case INTEGER:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INTEGER:
        tPort = jj_consume_token(INTEGER);
        jj_consume_token(25);
        break;
      default:
        jj_la1[10] = jj_gen;
        ;
      }
      operator = operator(namedOps);
      port = tPort == null ? 0 : Integer.parseInt(tPort.image);
      list.add(new InputOperatorItem(operator, port));
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 21:
          ;
          break;
        default:
          jj_la1[11] = jj_gen;
          break label_4;
        }
        jj_consume_token(21);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER:
          tPort = jj_consume_token(INTEGER);
          jj_consume_token(25);
          break;
        default:
          jj_la1[12] = jj_gen;
          ;
        }
        operator = operator(namedOps);
        port = tPort == null ? 0 : Integer.parseInt(tPort.image);
        list.add(new InputOperatorItem(operator, port));
      }
      break;
    default:
      jj_la1[13] = jj_gen;
      ;
    }
    {if (true) return list;}
    throw new Error("Missing return statement in function");
  }

  static final public PredicateItem predicate() throws ParseException {
  Token predicateType;
  Token predicate;
    predicateType = jj_consume_token(IDENTIFIER);
    jj_consume_token(20);
    predicate = jj_consume_token(CHAR_LITERAL);
    jj_consume_token(22);
    {if (true) return new PredicateItem(predicateType.image, predicate.image.substring(1, predicate.image.length() - 1));}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_3_1() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(20)) return true;
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public PQLParserImplTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[14];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x20,0x10000,0xc0000,0x200000,0x10000,0x20,0x200000,0x806120,0x200000,0x806120,0x4000,0x200000,0x4000,0x4020,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[1];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public PQLParserImpl(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public PQLParserImpl(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new PQLParserImplTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public PQLParserImpl(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new PQLParserImplTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public PQLParserImpl(PQLParserImplTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(PQLParserImplTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
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
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
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
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
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
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[26];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 14; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 26; i++) {
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
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
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

  static private void jj_save(int index, int xla) {
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
