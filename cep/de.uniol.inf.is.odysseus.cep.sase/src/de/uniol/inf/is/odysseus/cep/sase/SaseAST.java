// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g 2010-02-22 10:44:15

	package de.uniol.inf.is.odysseus.cep.sase; 
	import java.util.LinkedList;
	import java.util.Map;
	import java.util.Set;
	import java.util.TreeSet;
	import java.util.HashMap;
	import java.util.Iterator;
	
	import de.uniol.inf.is.odysseus.base.DataDictionary;
	import de.uniol.inf.is.odysseus.base.ILogicalOperator;
	import de.uniol.inf.is.odysseus.cep.CepAO;
	import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
	import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
	import de.uniol.inf.is.odysseus.cep.metamodel.EEventSelectionStrategy;
	import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
	import de.uniol.inf.is.odysseus.cep.metamodel.State;
	import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
	import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
	import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPCondition;
	import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPOutputSchemeEntry;
	import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;
	import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableOperationFactory;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SaseAST extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CREATE", "STREAM", "PATTERN", "WHERE", "WITHIN", "RETURN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "LEN", "BBRACKETLEFT", "BBRACKETRIGHT", "WEEK", "DAY", "HOUR", "MINUTE", "SECOND", "MILLISECOND", "SKIP_TILL_NEXT_MATCH", "SKIP_TILL_ANY_MATCH", "STRICT_CONTIGUITY", "PARTITION_CONTIGUITY", "AVG", "MIN", "MAX", "SUM", "COUNT", "PLUS", "MINUS", "POINT", "DIVISION", "MULT", "COMPAREOP", "SINGLEEQUALS", "EQUALS", "NOTSIGN", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NAME", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "KSTATE", "STATE", "KTYPE", "TYPE", "WHEREEXPRESSION", "MATHEXPRESSION", "TERM", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "KMEMBER", "MEMBER", "COMPAREEXPRESSION", "IDEXPRESSION", "AGGREGATION", "CREATESTREAM", "QUERY", "NOT", "PREV", "FIRST", "CURRENT"
    };
    public static final int TERM=63;
    public static final int WEEK=17;
    public static final int WHERE=7;
    public static final int MEMBER=68;
    public static final int MILLISECOND=22;
    public static final int POINT=34;
    public static final int KSTATE=57;
    public static final int LETTER=48;
    public static final int ATTRIBUTE=64;
    public static final int MAX=29;
    public static final int FLOAT=45;
    public static final int DAY=18;
    public static final int COUNT=31;
    public static final int EQUALS=39;
    public static final int NOT=74;
    public static final int SUM=30;
    public static final int AND=13;
    public static final int EOF=-1;
    public static final int MATHEXPRESSION=62;
    public static final int SPACE=52;
    public static final int TYPE=60;
    public static final int LBRACKET=42;
    public static final int PATTERN=6;
    public static final int SINGLEEQUALS=38;
    public static final int COMPAREEXPRESSION=69;
    public static final int NAME=49;
    public static final int CREATE=4;
    public static final int STRING_LITERAL=51;
    public static final int NOTSIGN=40;
    public static final int LEFTCURLY=11;
    public static final int SEQ=10;
    public static final int COMMA=41;
    public static final int AVG=27;
    public static final int KATTRIBUTE=65;
    public static final int RETURN=9;
    public static final int RIGHTCURLY=12;
    public static final int DIVISION=35;
    public static final int PLUS=32;
    public static final int DIGIT=47;
    public static final int RBRACKET=43;
    public static final int STREAM=5;
    public static final int BBRACKETRIGHT=16;
    public static final int INTEGER=44;
    public static final int STATE=58;
    public static final int IDEXPRESSION=70;
    public static final int SECOND=21;
    public static final int CREATESTREAM=72;
    public static final int KMEMBER=67;
    public static final int NUMBER=46;
    public static final int PARAMLIST=66;
    public static final int WHITESPACE=56;
    public static final int MIN=28;
    public static final int MULT=36;
    public static final int MINUS=33;
    public static final int HOUR=19;
    public static final int CURRENT=77;
    public static final int SKIP_TILL_NEXT_MATCH=23;
    public static final int PREV=75;
    public static final int WHEREEXPRESSION=61;
    public static final int NEWLINE=55;
    public static final int NONCONTROL_CHAR=50;
    public static final int BBRACKETLEFT=15;
    public static final int AGGREGATION=71;
    public static final int PARTITION_CONTIGUITY=26;
    public static final int WITHIN=8;
    public static final int SKIP_TILL_ANY_MATCH=24;
    public static final int LEN=14;
    public static final int QUERY=73;
    public static final int LOWER=53;
    public static final int KTYPE=59;
    public static final int MINUTE=20;
    public static final int STRICT_CONTIGUITY=25;
    public static final int COMPAREOP=37;
    public static final int UPPER=54;
    public static final int FIRST=76;

    // delegates
    // delegators


        public SaseAST(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public SaseAST(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return SaseAST.tokenNames; }
    public String getGrammarFileName() { return "C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g"; }


    	List<String> simpleState = null;
    	List<String> kleeneState = null;
    	Map<String, String> simpleAttributeState = null;
    	Map<String, String> kleeneAttributeState = null;
    	
    	// TODO: Hierfuer eine bessere Loesung finden
    	 private String tranformAttributesToMetamodell(CompareExpression ce, State s) {
        // In die Syntax des Metamodells umwandeln
        String ret = new String(ce.getFullExpression());
        for (PathAttribute attrib : ce.getAttributes()) {
          String index = ""; // getKleenePart.equals("[i]")
          String fullAttribName = attrib.getFullAttributeName();
          String aggregation = attrib.getAggregation();
          String stateIdentifier = attrib.getAttribute();
          String realAttributeName = attrib.getRealAttributeName();

          // UNterschiedliche Fälle
          // 1. Das Attribut gehört zum aktuellen Zustand und ist kein
          // KleeneAttribut --> aktuell
          // 2. Das Attribut gehört zum aktuellen Zustand und ist ein
          // Kleenattribut --> historisch
          // 2.a mit dem Kleenepart identisch zum Zustand --> aktuell
          // 2.b. mit einem anderen Kleenpart --> historisch

          if (attrib.getStatename().equals(s.getId())) {
            if (!attrib.isKleeneAttribute()) {
              aggregation = "";
              stateIdentifier = "";
              index = "";
            } else {
              if (s.getId().endsWith(attrib.getKleenePart())) {
                aggregation = "";
                stateIdentifier = "";
                index = "";
              }
            }
          } else {
            if (attrib.isKleeneAttribute()) {
              if (attrib.getKleenePart().equals("[1]")) {
                index = "0";
              } else if (attrib.getKleenePart().equals("[-1]")) {
                index = "-1";
              } else if (attrib.getKleenePart().equals("[i]")) {
                index = "";
              }
            }
          }

          ret = replaceStrings(ret, fullAttribName, aggregation
              + CepVariable.getSeperator() + stateIdentifier
              + CepVariable.getSeperator() + index
              + CepVariable.getSeperator() + realAttributeName);
        }

        return ret;
      }

      private String replaceStrings(String ret, String oldString, String newString) {
    //     System.out.println("replace " + oldString + " in " + ret + " with "
    //     + newString);
        ret = ret.replace(oldString, newString);
    //     System.out.println("--> " + ret);
        return ret;
      }
    	
           private long getTime(String value, CommonTree unit){
              
              long time = Long.parseLong(value);
              if (unit != null){ 
              int uType = unit.getToken().getType();
                if (uType == WEEK){
                  time *= 7*24*60*60*1000; 
                }else if (uType == DAY){
                  time *=24*60*60*1000;
                }else if (uType == HOUR){
                  time *=60*60*1000;
                }else if (uType == MINUTE){
                  time *=60*1000;
                }else if (uType == SECOND){
                  time *=1000;
                }
              }
              return time;    
          }




    // $ANTLR start "start"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:122:1: start returns [ILogicalOperator op] : ( ^( CREATESTREAM n= NAME q= query ) | o= query );
    public final ILogicalOperator start() throws RecognitionException {
        ILogicalOperator op = null;

        CommonTree n=null;
        ILogicalOperator q = null;

        ILogicalOperator o = null;


        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:123:3: ( ^( CREATESTREAM n= NAME q= query ) | o= query )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==CREATESTREAM) ) {
                alt1=1;
            }
            else if ( (LA1_0==QUERY) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:124:3: ^( CREATESTREAM n= NAME q= query )
                    {
                    match(input,CREATESTREAM,FOLLOW_CREATESTREAM_in_start71); 

                    match(input, Token.DOWN, null); 
                    n=(CommonTree)match(input,NAME,FOLLOW_NAME_in_start75); 
                    pushFollow(FOLLOW_query_in_start79);
                    q=query();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    	DataDictionary.getInstance().setView(n.getText(), q);
                    	  //System.out.println("Created New Stream "+n+" "+q);
                    	  op = q;

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:128:5: o= query
                    {
                    pushFollow(FOLLOW_query_in_start93);
                    o=query();

                    state._fsp--;

                    op = o;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return op;
    }
    // $ANTLR end "start"


    // $ANTLR start "query"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:131:1: query returns [ILogicalOperator op] : ^( QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo] ) ;
    public final ILogicalOperator query() throws RecognitionException {
        ILogicalOperator op = null;


            CepAO cepAo = new CepAO();
            simpleState = new ArrayList<String>();
            kleeneState = new ArrayList<String>();
            simpleAttributeState = new HashMap<String, String>();
            kleeneAttributeState = new HashMap<String, String>();
            Set<String> sourceNames = new TreeSet<String>();

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:140:3: ( ^( QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo] ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:141:3: ^( QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo] )
            {
            match(input,QUERY,FOLLOW_QUERY_in_query121); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_patternPart_in_query123);
            patternPart(cepAo, sourceNames);

            state._fsp--;

            pushFollow(FOLLOW_wherePart_in_query126);
            wherePart(cepAo);

            state._fsp--;

            pushFollow(FOLLOW_withinPart_in_query129);
            withinPart(cepAo);

            state._fsp--;

            pushFollow(FOLLOW_returnPart_in_query132);
            returnPart(cepAo);

            state._fsp--;


            match(input, Token.UP, null); 

                // Initialize Schema
                cepAo.getStateMachine().getSymTabScheme(true);
                // Set Inputs for cepAO;
                int port = 0;
                for (String sn : sourceNames) {
                  ILogicalOperator ao = DataDictionary.getInstance().getView(sn);
                  if (ao != null) {
                    cepAo.subscribeToSource(ao, port, 0, ao.getOutputSchema());
                    cepAo.setInputTypeName(port, sn);
                    port++;
                  } else {
                    throw new RuntimeException("Source " + sn + " not found");
                  }
                }
                op = cepAo;
                
               

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return op;
    }
    // $ANTLR end "query"


    // $ANTLR start "patternPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:162:1: patternPart[CepAO cepAo, Set<String> sourceNames] : ^( SEQ ( state[states, sourceNames] )* ) ;
    public final void patternPart(CepAO cepAo, Set<String> sourceNames) throws RecognitionException {

        List<State> states = new LinkedList<State>();

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:166:3: ( ^( SEQ ( state[states, sourceNames] )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:167:3: ^( SEQ ( state[states, sourceNames] )* )
            {
            match(input,SEQ,FOLLOW_SEQ_in_patternPart160); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:167:9: ( state[states, sourceNames] )*
                loop2:
                do {
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( ((LA2_0>=KSTATE && LA2_0<=STATE)) ) {
                        alt2=1;
                    }


                    switch (alt2) {
                	case 1 :
                	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:167:9: state[states, sourceNames]
                	    {
                	    pushFollow(FOLLOW_state_in_patternPart162);
                	    state(states, sourceNames);

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop2;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            	  if (states.size() > 0) {
                  states.add(new State("<ACCEPTING>", "", null, true));
                  StateMachine sm = cepAo.getStateMachine();
                  sm.setStates(states);
                  sm.setInitialState(states.get(0));
                  // Calculate transistions
                  for (int i = 0; i < states.size() - 1; i++) {
                     State source = states.get(i);
                     State dest = states.get(i + 1);
                     if (source.getId().endsWith("[i]")) {
                       JEPCondition con = new JEPCondition("");
                       con.setEventType(source.getType());
                       source.addTransition(new Transition(source.getId()+ "_proceed", dest, con, EAction.consumeNoBufferWrite));
                       con = new JEPCondition("");
                       con.setEventType(source.getType());
                       source.addTransition(new Transition(source.getId()+ "_take", source,con, EAction.consumeBufferWrite));
                     } else {
                        JEPCondition con = new JEPCondition("");
                        con.setEventType(source.getType());
                        source.addTransition(new Transition(source.getId()+ "_begin", dest, con,EAction.consumeBufferWrite));
                     }
                     if (i > 0 && i < states.size() - 1) {
                        JEPCondition con = new JEPCondition("");
                        // Achtung! Ignore hat keinen Typ!
                        //con.setEventType(source.getType());
                        // Ignore auf sich selbst!
                        source.addTransition(new Transition(source.getId()+ "_ignore", source, con,EAction.discard));
                      }
                   }
                } else {
                    throw new RuntimeException("Illegal Sequence");
                }
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "patternPart"


    // $ANTLR start "state"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:204:1: state[List<State> states, Set<String> sourceNames] : ( ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? ) | ^( KSTATE statename= NAME attrName= NAME (not= NOTSIGN )? ) );
    public final void state(List<State> states, Set<String> sourceNames) throws RecognitionException {
        CommonTree statename=null;
        CommonTree attrName=null;
        CommonTree not=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:205:3: ( ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? ) | ^( KSTATE statename= NAME attrName= NAME (not= NOTSIGN )? ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==STATE) ) {
                alt5=1;
            }
            else if ( (LA5_0==KSTATE) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:206:3: ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? )
                    {
                    match(input,STATE,FOLLOW_STATE_in_state186); 

                    match(input, Token.DOWN, null); 
                    statename=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state190); 
                    attrName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state194); 
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:206:43: (not= NOTSIGN )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==NOTSIGN) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:206:43: not= NOTSIGN
                            {
                            not=(CommonTree)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_state198); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    		  if (not != null) throw new RuntimeException("Negative states not supported now!");
                    		  String _statename = statename.getText();
                    		  sourceNames.add(_statename);
                    		  String _attributeName = attrName.getText();
                    		//	System.out.println("Einfacher Zustand "+_statename+" "+_attributeName);
                    			// Anm. _statename darf mehrfach vorkommen  Variablenname nicht
                    			simpleState.add(_statename);
                    			if (simpleAttributeState.get(_attributeName) == null){
                    				simpleAttributeState.put(_attributeName,_statename);
                    				states.add(new State(_attributeName, _attributeName, _statename, false));
                    			}else{
                            throw new RuntimeException("Double attribute definition "+_attributeName); 
                    			}
                    		

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:223:3: ^( KSTATE statename= NAME attrName= NAME (not= NOTSIGN )? )
                    {
                    match(input,KSTATE,FOLLOW_KSTATE_in_state213); 

                    match(input, Token.DOWN, null); 
                    statename=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state217); 
                    attrName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state221); 
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:223:44: (not= NOTSIGN )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==NOTSIGN) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:223:44: not= NOTSIGN
                            {
                            not=(CommonTree)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_state225); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    		  if (not != null) throw new RuntimeException("Negative states not supported now!");
                    		  String _statename = statename.getText();
                          String _attributeName = attrName.getText();
                    		 // System.out.println("Kleene Zustand "+_statename+" "+_attributeName);
                    		  kleeneState.add(_statename);
                    		  sourceNames.add(_statename);
                    		  if (kleeneAttributeState.get(_attributeName) == null){
                    		    kleeneAttributeState.put(_attributeName, _statename);
                    		    states.add(new State(_attributeName + "[1]", _attributeName, _statename, false));
                    		    states.add(new State(_attributeName + "[i]", _attributeName, _statename, false));  
                    		  }else{
                    		    throw new RuntimeException("Double attribute definition "+_attributeName); 
                    		  }
                    		 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "state"


    // $ANTLR start "wherePart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:241:1: wherePart[CepAO cepAo] : ( ^( WHERE str= whereStrat[cepAo.getStateMachine()] we= whereExpression[cepAo.getStateMachine()] ) | );
    public final void wherePart(CepAO cepAo) throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:242:3: ( ^( WHERE str= whereStrat[cepAo.getStateMachine()] we= whereExpression[cepAo.getStateMachine()] ) | )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WHERE) ) {
                alt6=1;
            }
            else if ( (LA6_0==UP||(LA6_0>=WITHIN && LA6_0<=RETURN)) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:243:3: ^( WHERE str= whereStrat[cepAo.getStateMachine()] we= whereExpression[cepAo.getStateMachine()] )
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_wherePart248); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_whereStrat_in_wherePart252);
                    whereStrat(cepAo.getStateMachine());

                    state._fsp--;

                    pushFollow(FOLLOW_whereExpression_in_wherePart257);
                    whereExpression(cepAo.getStateMachine());

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:245:3: 
                    {
                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "wherePart"


    // $ANTLR start "whereStrat"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:247:1: whereStrat[StateMachine sm] : ( ^( SKIP_TILL_NEXT_MATCH param= paramlist ) | ^( SKIP_TILL_ANY_MATCH param= paramlist ) | ^( STRICT_CONTIGUITY paramlist ) | ^( PARTITION_CONTIGUITY paramlist ) );
    public final void whereStrat(StateMachine sm) throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:248:3: ( ^( SKIP_TILL_NEXT_MATCH param= paramlist ) | ^( SKIP_TILL_ANY_MATCH param= paramlist ) | ^( STRICT_CONTIGUITY paramlist ) | ^( PARTITION_CONTIGUITY paramlist ) )
            int alt7=4;
            switch ( input.LA(1) ) {
            case SKIP_TILL_NEXT_MATCH:
                {
                alt7=1;
                }
                break;
            case SKIP_TILL_ANY_MATCH:
                {
                alt7=2;
                }
                break;
            case STRICT_CONTIGUITY:
                {
                alt7=3;
                }
                break;
            case PARTITION_CONTIGUITY:
                {
                alt7=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:249:3: ^( SKIP_TILL_NEXT_MATCH param= paramlist )
                    {
                    match(input,SKIP_TILL_NEXT_MATCH,FOLLOW_SKIP_TILL_NEXT_MATCH_in_whereStrat280); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat284);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH);

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:252:3: ^( SKIP_TILL_ANY_MATCH param= paramlist )
                    {
                    match(input,SKIP_TILL_ANY_MATCH,FOLLOW_SKIP_TILL_ANY_MATCH_in_whereStrat298); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat302);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_ANY_MATCH);

                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:255:3: ^( STRICT_CONTIGUITY paramlist )
                    {
                    match(input,STRICT_CONTIGUITY,FOLLOW_STRICT_CONTIGUITY_in_whereStrat316); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat318);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    sm.setEventSelectionStrategy(EEventSelectionStrategy.PARTITION_CONTIGUITY);

                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:258:3: ^( PARTITION_CONTIGUITY paramlist )
                    {
                    match(input,PARTITION_CONTIGUITY,FOLLOW_PARTITION_CONTIGUITY_in_whereStrat332); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat334);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    sm.setEventSelectionStrategy(EEventSelectionStrategy.STRICT_CONTIGUITY);

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "whereStrat"


    // $ANTLR start "paramlist"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:262:1: paramlist : ^( PARAMLIST ( attributeName )* ) ;
    public final void paramlist() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:263:3: ( ^( PARAMLIST ( attributeName )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:264:3: ^( PARAMLIST ( attributeName )* )
            {
            match(input,PARAMLIST,FOLLOW_PARAMLIST_in_paramlist355); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:264:15: ( attributeName )*
                loop8:
                do {
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( ((LA8_0>=ATTRIBUTE && LA8_0<=KATTRIBUTE)) ) {
                        alt8=1;
                    }


                    switch (alt8) {
                	case 1 :
                	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:264:15: attributeName
                	    {
                	    pushFollow(FOLLOW_attributeName_in_paramlist357);
                	    attributeName();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop8;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }
            System.err.println("WARNING: Attributes currently ignored in selection strategy");

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "paramlist"


    // $ANTLR start "attributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:268:1: attributeName : ( ^( KATTRIBUTE NAME ) | ^( ATTRIBUTE NAME ) );
    public final void attributeName() throws RecognitionException {
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:269:3: ( ^( KATTRIBUTE NAME ) | ^( ATTRIBUTE NAME ) )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==KATTRIBUTE) ) {
                alt9=1;
            }
            else if ( (LA9_0==ATTRIBUTE) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:270:3: ^( KATTRIBUTE NAME )
                    {
                    match(input,KATTRIBUTE,FOLLOW_KATTRIBUTE_in_attributeName379); 

                    match(input, Token.DOWN, null); 
                    match(input,NAME,FOLLOW_NAME_in_attributeName381); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:272:3: ^( ATTRIBUTE NAME )
                    {
                    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeName391); 

                    match(input, Token.DOWN, null); 
                    match(input,NAME,FOLLOW_NAME_in_attributeName393); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "attributeName"


    // $ANTLR start "whereExpression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:275:1: whereExpression[StateMachine stmachine] : ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] )* ) ;
    public final void whereExpression(StateMachine stmachine) throws RecognitionException {

          List<CompareExpression> compareExpressions = new ArrayList<CompareExpression>();

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:279:3: ( ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:280:3: ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] )* )
            {
            match(input,WHEREEXPRESSION,FOLLOW_WHEREEXPRESSION_in_whereExpression421); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:282:5: ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] )*
                loop10:
                do {
                    int alt10=3;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==COMPAREEXPRESSION) ) {
                        alt10=1;
                    }
                    else if ( (LA10_0==IDEXPRESSION) ) {
                        alt10=2;
                    }


                    switch (alt10) {
                	case 1 :
                	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:283:7: compareExpression[stmachine,compareExpressions]
                	    {
                	    pushFollow(FOLLOW_compareExpression_in_whereExpression435);
                	    compareExpression(stmachine, compareExpressions);

                	    state._fsp--;


                	    }
                	    break;
                	case 2 :
                	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:284:9: idexpression[stmachine.getStates(),compareExpressions]
                	    {
                	    pushFollow(FOLLOW_idexpression_in_whereExpression446);
                	    idexpression(stmachine.getStates(), compareExpressions);

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop10;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

                 //System.out.println("Compare Expressions");
            //     for (CompareExpression expr:compareExpressions){
            //       System.out.println(expr);
            //     }
                List<State> finalStates = stmachine.getFinalStates();
                for (State finalState : finalStates) {
                  List<State> pathes = stmachine.getAllPathesToStart(finalState);
                  for (State s : pathes) {
                    Iterator<CompareExpression> compareIter = compareExpressions
                        .iterator();
                    while (compareIter.hasNext()) {
                      CompareExpression ce = compareIter.next();
                      //System.out.println(ce);
                      PathAttribute attr = ce.get(s.getId());
                      if (attr != null) {
                        Transition t = s.getTransition(s.getId() + "_begin");

                        if (t == null && attr.isKleeneAttribute()) {
                          if (attr.getKleenePart().equals("i")) {
                            t = s.getTransition(s.getId() + "_take");
                          } else {
                            t = s.getTransition(s.getId() + "_proceed");
                          }
                        }
                        // Anpassen der Variablennamen für das Metamodell:
                        String fullExpression = tranformAttributesToMetamodell(
                            ce, s);
                        //String fullExpression = ce.getFullExpression();
                        t.append(fullExpression);
                        t = s.getTransition(s.getId() + "_ignore");
                        if (t != null) {
                          switch (stmachine.getEventSelectionStrategy()) {
                          case PARTITION_CONTIGUITY:
                            t.append(fullExpression);
                            // negation later!
                            break;
                          case SKIP_TILL_NEXT_MATCH:
                            t.append(fullExpression);
                            // negation later!
                            break;
                          case STRICT_CONTIGUITY:
                            t.append("false");
                            break;
                          case SKIP_TILL_ANY_MATCH:
                          default:
                            // let _ignore be true
                          }
                        }
                        compareIter.remove();
                      }
                    }
                  }
                }
                // SET NEGATION on whole expression!
                if (stmachine.getEventSelectionStrategy() == EEventSelectionStrategy.PARTITION_CONTIGUITY
                    || stmachine.getEventSelectionStrategy() == EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH) {
                  List<State> states = stmachine.getStates();
                  for (State s : states) {
                    Transition t = s.getTransition(s.getId() + "_ignore");
                    if (t != null) {
                      t.negateExpression();
                    }
                  }
                }
                 
                   
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "whereExpression"


    // $ANTLR start "compareExpression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:357:1: compareExpression[StateMachine sm,List<CompareExpression> compareExpressions] : ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] ) ;
    public final void compareExpression(StateMachine sm, List<CompareExpression> compareExpressions) throws RecognitionException {
        CommonTree op=null;


        		 StringBuffer left= new StringBuffer();
             StringBuffer right=new StringBuffer();
             List<PathAttribute> attribs = new ArrayList<PathAttribute>();

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:363:3: ( ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:364:3: ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] )
            {
            match(input,COMPAREEXPRESSION,FOLLOW_COMPAREEXPRESSION_in_compareExpression490); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_mathExpression_in_compareExpression492);
            mathExpression(left, attribs);

            state._fsp--;

            op=(CommonTree)input.LT(1);
            if ( (input.LA(1)>=COMPAREOP && input.LA(1)<=SINGLEEQUALS) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            pushFollow(FOLLOW_mathExpression_in_compareExpression532);
            mathExpression(right, attribs);

            state._fsp--;


            match(input, Token.UP, null); 
            StringBuffer ret = new StringBuffer(left);
               if (op.getToken().getType() == SINGLEEQUALS){
                  ret.append("==");
               }else{
                  ret.append(op);
               }
               ret.append(right);
               compareExpressions.add(new CompareExpression(attribs, ret.toString()));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "compareExpression"


    // $ANTLR start "mathExpression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:382:1: mathExpression[StringBuffer exp, List<PathAttribute> attribs] : ( ^( (op= PLUS | op= MINUS | op= MULT | op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] ) | attributeTerm[tmpAttrib] | num= NUMBER | lit= STRING_LITERAL );
    public final void mathExpression(StringBuffer exp, List<PathAttribute> attribs) throws RecognitionException {
        CommonTree op=null;
        CommonTree num=null;
        CommonTree lit=null;


              StringBuffer left=new StringBuffer();
              StringBuffer right=new StringBuffer();
              List<PathAttribute> tmpAttrib = new ArrayList<PathAttribute>();

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:388:3: ( ^( (op= PLUS | op= MINUS | op= MULT | op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] ) | attributeTerm[tmpAttrib] | num= NUMBER | lit= STRING_LITERAL )
            int alt12=4;
            switch ( input.LA(1) ) {
            case PLUS:
            case MINUS:
            case DIVISION:
            case MULT:
                {
                alt12=1;
                }
                break;
            case KMEMBER:
            case MEMBER:
            case AGGREGATION:
                {
                alt12=2;
                }
                break;
            case NUMBER:
                {
                alt12=3;
                }
                break;
            case STRING_LITERAL:
                {
                alt12=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }

            switch (alt12) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:389:3: ^( (op= PLUS | op= MINUS | op= MULT | op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] )
                    {
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:390:5: (op= PLUS | op= MINUS | op= MULT | op= DIVISION )
                    int alt11=4;
                    switch ( input.LA(1) ) {
                    case PLUS:
                        {
                        alt11=1;
                        }
                        break;
                    case MINUS:
                        {
                        alt11=2;
                        }
                        break;
                    case MULT:
                        {
                        alt11=3;
                        }
                        break;
                    case DIVISION:
                        {
                        alt11=4;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 11, 0, input);

                        throw nvae;
                    }

                    switch (alt11) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:391:7: op= PLUS
                            {
                            op=(CommonTree)match(input,PLUS,FOLLOW_PLUS_in_mathExpression579); 

                            }
                            break;
                        case 2 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:392:9: op= MINUS
                            {
                            op=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_mathExpression591); 

                            }
                            break;
                        case 3 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:393:9: op= MULT
                            {
                            op=(CommonTree)match(input,MULT,FOLLOW_MULT_in_mathExpression603); 

                            }
                            break;
                        case 4 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:394:9: op= DIVISION
                            {
                            op=(CommonTree)match(input,DIVISION,FOLLOW_DIVISION_in_mathExpression615); 

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_mathExpression_in_mathExpression627);
                    mathExpression(left, attribs);

                    state._fsp--;

                    pushFollow(FOLLOW_mathExpression_in_mathExpression630);
                    mathExpression(right, attribs);

                    state._fsp--;


                    match(input, Token.UP, null); 
                    exp.append("(").append(left).append(" ").append(op).append(right).append(")");

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:399:5: attributeTerm[tmpAttrib]
                    {
                    pushFollow(FOLLOW_attributeTerm_in_mathExpression646);
                    attributeTerm(tmpAttrib);

                    state._fsp--;


                        // TODO: Es darf nur ein Attribut drin sein!
                        PathAttribute p = tmpAttrib.get(0);
                        attribs.add(p);
                        exp.append(p);
                      

                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:405:5: num= NUMBER
                    {
                    num=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_mathExpression657); 
                    exp.append(num.getText());

                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:406:5: lit= STRING_LITERAL
                    {
                    lit=(CommonTree)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_mathExpression667); 
                    exp.append(lit.getText());

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "mathExpression"


    // $ANTLR start "attributeTerm"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:409:1: attributeTerm[List<PathAttribute> attribs] : ( ^( KMEMBER kAttributeUsage[name,usage] member= NAME ) | ^( MEMBER attribName= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT ) var= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT ) var= NAME ) );
    public final void attributeTerm(List<PathAttribute> attribs) throws RecognitionException {
        CommonTree member=null;
        CommonTree attribName=null;
        CommonTree op=null;
        CommonTree var=null;


              StringBuffer usage = new StringBuffer();
              StringBuffer name = new StringBuffer();

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:414:3: ( ^( KMEMBER kAttributeUsage[name,usage] member= NAME ) | ^( MEMBER attribName= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT ) var= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT ) var= NAME ) )
            int alt13=4;
            switch ( input.LA(1) ) {
            case KMEMBER:
                {
                alt13=1;
                }
                break;
            case MEMBER:
                {
                alt13=2;
                }
                break;
            case AGGREGATION:
                {
                int LA13_3 = input.LA(2);

                if ( (LA13_3==DOWN) ) {
                    int LA13_4 = input.LA(3);

                    if ( ((LA13_4>=MIN && LA13_4<=COUNT)) ) {
                        int LA13_5 = input.LA(4);

                        if ( (LA13_5==NAME) ) {
                            int LA13_6 = input.LA(5);

                            if ( (LA13_6==NAME) ) {
                                alt13=3;
                            }
                            else if ( (LA13_6==UP) ) {
                                alt13=4;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 13, 6, input);

                                throw nvae;
                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 13, 5, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 13, 4, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:415:3: ^( KMEMBER kAttributeUsage[name,usage] member= NAME )
                    {
                    match(input,KMEMBER,FOLLOW_KMEMBER_in_attributeTerm691); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_kAttributeUsage_in_attributeTerm693);
                    kAttributeUsage(name, usage);

                    state._fsp--;

                    member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm698); 

                    match(input, Token.UP, null); 

                          PathAttribute p = new PathAttribute(name.toString(),usage.toString(),member.getText(),Write.class.getSimpleName());
                          attribs.add(p);
                          name = new StringBuffer(); 
                          usage = new StringBuffer();
                         

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:423:3: ^( MEMBER attribName= NAME member= NAME )
                    {
                    match(input,MEMBER,FOLLOW_MEMBER_in_attributeTerm712); 

                    match(input, Token.DOWN, null); 
                    attribName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm716); 
                    member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm720); 

                    match(input, Token.UP, null); 

                         PathAttribute p = new PathAttribute(attribName.getText(),null,member.getText(),Write.class.getSimpleName()); 
                          attribs.add(p);

                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:428:3: ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT ) var= NAME member= NAME )
                    {
                    match(input,AGGREGATION,FOLLOW_AGGREGATION_in_attributeTerm739); 

                    match(input, Token.DOWN, null); 
                    op=(CommonTree)input.LT(1);
                    if ( (input.LA(1)>=MIN && input.LA(1)<=COUNT) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    var=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm800); 
                    member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm804); 

                    match(input, Token.UP, null); 
                     
                        PathAttribute p = new PathAttribute(var.getText(),"[i-1]",member.getText(),
                            SymbolTableOperationFactory.getOperation(op.getText()).getName());
                        attribs.add(p);
                       

                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:444:3: ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT ) var= NAME )
                    {
                    match(input,AGGREGATION,FOLLOW_AGGREGATION_in_attributeTerm827); 

                    match(input, Token.DOWN, null); 
                    op=(CommonTree)input.LT(1);
                    if ( (input.LA(1)>=MIN && input.LA(1)<=COUNT) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    var=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm888); 

                    match(input, Token.UP, null); 
                     
                        PathAttribute p = new PathAttribute(var.getText(),"[i-1]","",
                            SymbolTableOperationFactory.getOperation(op.getText()).getName());
                        attribs.add(p);
                       

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "attributeTerm"


    // $ANTLR start "kAttributeUsage"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:461:1: kAttributeUsage[StringBuffer name, StringBuffer usage] : ( ^( NAME CURRENT ) | ^( NAME FIRST ) | ^( NAME PREV ) | ^( NAME LEN ) );
    public final void kAttributeUsage(StringBuffer name, StringBuffer usage) throws RecognitionException {
        CommonTree NAME1=null;
        CommonTree NAME2=null;
        CommonTree NAME3=null;
        CommonTree NAME4=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:462:3: ( ^( NAME CURRENT ) | ^( NAME FIRST ) | ^( NAME PREV ) | ^( NAME LEN ) )
            int alt14=4;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==NAME) ) {
                int LA14_1 = input.LA(2);

                if ( (LA14_1==DOWN) ) {
                    switch ( input.LA(3) ) {
                    case CURRENT:
                        {
                        alt14=1;
                        }
                        break;
                    case FIRST:
                        {
                        alt14=2;
                        }
                        break;
                    case PREV:
                        {
                        alt14=3;
                        }
                        break;
                    case LEN:
                        {
                        alt14=4;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 2, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:463:3: ^( NAME CURRENT )
                    {
                    NAME1=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage914); 

                    match(input, Token.DOWN, null); 
                    match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage916); 

                    match(input, Token.UP, null); 
                    usage.append("[i]"); name.append(NAME1);

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:466:3: ^( NAME FIRST )
                    {
                    NAME2=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage930); 

                    match(input, Token.DOWN, null); 
                    match(input,FIRST,FOLLOW_FIRST_in_kAttributeUsage932); 

                    match(input, Token.UP, null); 
                    usage.append("[1]");name.append(NAME2);

                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:469:3: ^( NAME PREV )
                    {
                    NAME3=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage946); 

                    match(input, Token.DOWN, null); 
                    match(input,PREV,FOLLOW_PREV_in_kAttributeUsage948); 

                    match(input, Token.UP, null); 
                    usage.append("[i-1]");name.append(NAME3);

                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:472:3: ^( NAME LEN )
                    {
                    NAME4=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage962); 

                    match(input, Token.DOWN, null); 
                    match(input,LEN,FOLLOW_LEN_in_kAttributeUsage964); 

                    match(input, Token.UP, null); 
                    usage.append("[i-1]");name.append(NAME4);

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "kAttributeUsage"


    // $ANTLR start "idexpression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:476:1: idexpression[List<State> states, List<CompareExpression> compareExpressions] : ^( IDEXPRESSION var= NAME ) ;
    public final void idexpression(List<State> states, List<CompareExpression> compareExpressions) throws RecognitionException {
        CommonTree var=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:477:3: ( ^( IDEXPRESSION var= NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:478:3: ^( IDEXPRESSION var= NAME )
            {
            match(input,IDEXPRESSION,FOLLOW_IDEXPRESSION_in_idexpression986); 

            match(input, Token.DOWN, null); 
            var=(CommonTree)match(input,NAME,FOLLOW_NAME_in_idexpression990); 

            match(input, Token.UP, null); 

                      String t=CepVariable.getSeperator();
                      for (int i = 0; i < states.size() - 2; i++) {
                        List<PathAttribute> attr = new ArrayList<PathAttribute>();
                        PathAttribute a = new PathAttribute(states.get(i).getId(),var.getText());
                        attr.add(a);
                        PathAttribute b = new PathAttribute(states.get(i + 1).getId(), var.getText());
                        attr.add(b);
                        StringBuffer expr = new StringBuffer();
                        expr.append(t).append(t).append(t).append(states.get(i).getId()).append(".").append(var.getText());
                        expr.append(t).append(t).append(t).append(states.get(i+1).getId()).append(".").append(var.getText());
                        compareExpressions.add(new CompareExpression(attr,expr.toString()));
                    }
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "idexpression"


    // $ANTLR start "withinPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:495:1: withinPart[CepAO cepAo] : ( ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? ) | );
    public final void withinPart(CepAO cepAo) throws RecognitionException {
        CommonTree value=null;
        CommonTree unit=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:496:3: ( ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? ) | )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==WITHIN) ) {
                alt16=1;
            }
            else if ( (LA16_0==UP||LA16_0==RETURN) ) {
                alt16=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:497:3: ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? )
                    {
                    match(input,WITHIN,FOLLOW_WITHIN_in_withinPart1017); 

                    match(input, Token.DOWN, null); 
                    value=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart1021); 
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:499:5: (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( ((LA15_0>=WEEK && LA15_0<=MILLISECOND)) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:500:7: unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND )
                            {
                            unit=(CommonTree)input.LT(1);
                            if ( (input.LA(1)>=WEEK && input.LA(1)<=MILLISECOND) ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                        Long time = getTime(value.getText(),unit);
                        System.out.println("Setting Windowsize to "+time+" milliseconds");
                        cepAo.getStateMachine().setWindowSize(time);
                     

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:517:3: 
                    {
                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "withinPart"


    // $ANTLR start "returnPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:519:1: returnPart[CepAO cepAo] : ( ^( RETURN ( attributeTerm[retAttr] )* ) | );
    public final void returnPart(CepAO cepAo) throws RecognitionException {

        List<PathAttribute> retAttr = new ArrayList<PathAttribute>();

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:523:3: ( ^( RETURN ( attributeTerm[retAttr] )* ) | )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==RETURN) ) {
                alt18=1;
            }
            else if ( (LA18_0==UP) ) {
                alt18=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }
            switch (alt18) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:524:3: ^( RETURN ( attributeTerm[retAttr] )* )
                    {
                    match(input,RETURN,FOLLOW_RETURN_in_returnPart1164); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:524:12: ( attributeTerm[retAttr] )*
                        loop17:
                        do {
                            int alt17=2;
                            int LA17_0 = input.LA(1);

                            if ( ((LA17_0>=KMEMBER && LA17_0<=MEMBER)||LA17_0==AGGREGATION) ) {
                                alt17=1;
                            }


                            switch (alt17) {
                        	case 1 :
                        	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:524:12: attributeTerm[retAttr]
                        	    {
                        	    pushFollow(FOLLOW_attributeTerm_in_returnPart1166);
                        	    attributeTerm(retAttr);

                        	    state._fsp--;


                        	    }
                        	    break;

                        	default :
                        	    break loop17;
                            }
                        } while (true);


                        match(input, Token.UP, null); 
                    }

                        JEPOutputSchemeEntry e = null;
                        OutputScheme scheme = new OutputScheme();
                        for (PathAttribute p : retAttr) {
                          String op = p.getAggregation();
                          String a = p.getAttribute();
                          String i = p.getKleenePart();
                          String path = p.getPath();
                          e = new JEPOutputSchemeEntry(CepVariable.getStringFor(op, a, i, a
                              + "." + path));
                          scheme.append(e);
                        }
                        cepAo.getStateMachine().setOutputScheme(scheme);
                      

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:540:3: 
                    {
                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "returnPart"

    // Delegated rules


 

    public static final BitSet FOLLOW_CREATESTREAM_in_start71 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_start75 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_query_in_start79 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_query_in_start93 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUERY_in_query121 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_patternPart_in_query123 = new BitSet(new long[]{0x0000000000000388L});
    public static final BitSet FOLLOW_wherePart_in_query126 = new BitSet(new long[]{0x0000000000000308L});
    public static final BitSet FOLLOW_withinPart_in_query129 = new BitSet(new long[]{0x0000000000000208L});
    public static final BitSet FOLLOW_returnPart_in_query132 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEQ_in_patternPart160 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_state_in_patternPart162 = new BitSet(new long[]{0x0600000000000008L});
    public static final BitSet FOLLOW_STATE_in_state186 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_state190 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_state194 = new BitSet(new long[]{0x0000010000000008L});
    public static final BitSet FOLLOW_NOTSIGN_in_state198 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_KSTATE_in_state213 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_state217 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_state221 = new BitSet(new long[]{0x0000010000000008L});
    public static final BitSet FOLLOW_NOTSIGN_in_state225 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHERE_in_wherePart248 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_whereStrat_in_wherePart252 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_whereExpression_in_wherePart257 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SKIP_TILL_NEXT_MATCH_in_whereStrat280 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat284 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SKIP_TILL_ANY_MATCH_in_whereStrat298 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat302 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STRICT_CONTIGUITY_in_whereStrat316 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat318 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARTITION_CONTIGUITY_in_whereStrat332 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat334 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARAMLIST_in_paramlist355 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeName_in_paramlist357 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000003L});
    public static final BitSet FOLLOW_KATTRIBUTE_in_attributeName379 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeName381 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeName391 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeName393 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHEREEXPRESSION_in_whereExpression421 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_compareExpression_in_whereExpression435 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000060L});
    public static final BitSet FOLLOW_idexpression_in_whereExpression446 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000060L});
    public static final BitSet FOLLOW_COMPAREEXPRESSION_in_compareExpression490 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mathExpression_in_compareExpression492 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_set_in_compareExpression502 = new BitSet(new long[]{0x0008401B00000000L,0x0000000000000098L});
    public static final BitSet FOLLOW_mathExpression_in_compareExpression532 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PLUS_in_mathExpression579 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MINUS_in_mathExpression591 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MULT_in_mathExpression603 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIVISION_in_mathExpression615 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mathExpression_in_mathExpression627 = new BitSet(new long[]{0x0008401B00000000L,0x0000000000000098L});
    public static final BitSet FOLLOW_mathExpression_in_mathExpression630 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_attributeTerm_in_mathExpression646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_mathExpression657 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_mathExpression667 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KMEMBER_in_attributeTerm691 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_kAttributeUsage_in_attributeTerm693 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm698 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MEMBER_in_attributeTerm712 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm716 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm720 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AGGREGATION_in_attributeTerm739 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_attributeTerm748 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm800 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm804 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AGGREGATION_in_attributeTerm827 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_attributeTerm836 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm888 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage914 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage916 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage930 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_FIRST_in_kAttributeUsage932 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage946 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_PREV_in_kAttributeUsage948 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage962 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_LEN_in_kAttributeUsage964 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDEXPRESSION_in_idexpression986 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_idexpression990 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart1017 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart1021 = new BitSet(new long[]{0x00000000007E0008L});
    public static final BitSet FOLLOW_set_in_withinPart1044 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RETURN_in_returnPart1164 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart1166 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000098L});

}