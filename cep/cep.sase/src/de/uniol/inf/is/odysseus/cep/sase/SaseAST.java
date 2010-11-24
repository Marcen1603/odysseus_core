// $ANTLR 3.2 Sep 23, 2009 12:02:23 E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g 2010-11-23 10:12:22

	package de.uniol.inf.is.odysseus.cep.sase; 
	import java.util.LinkedList;
	import java.util.Map;
	import java.util.HashMap;
	import java.util.Iterator;
	
	import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
	import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
	import de.uniol.inf.is.odysseus.cep.CepAO;
	import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
	import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
	import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
	import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
	import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
	import de.uniol.inf.is.odysseus.cep.metamodel.EEventSelectionStrategy;
	import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
	import de.uniol.inf.is.odysseus.cep.metamodel.State;
	import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
	import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
	import de.uniol.inf.is.odysseus.cep.epa.metamodel.relational.RelationalMEPCondition;
	import de.uniol.inf.is.odysseus.cep.epa.metamodel.relational.RelationalMEPOutputSchemeEntry;
	import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;
	import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.ISymbolTableOperationFactory;
	import de.uniol.inf.is.odysseus.usermanagement.User;
	
	import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SaseAST extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CREATE", "STREAM", "VIEW", "PATTERN", "WHERE", "WITHIN", "RETURN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "LEN", "BBRACKETLEFT", "BBRACKETRIGHT", "WEEK", "DAY", "HOUR", "MINUTE", "SECOND", "MILLISECOND", "SKIP_TILL_NEXT_MATCH", "SKIP_TILL_ANY_MATCH", "STRICT_CONTIGUITY", "PARTITION_CONTIGUITY", "AVG", "MIN", "MAX", "SUM", "COUNT", "PLUS", "MINUS", "POINT", "DIVISION", "MULT", "COMPAREOP", "SINGLEEQUALS", "EQUALS", "ASSIGN", "NOTSIGN", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NAME", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "KSTATE", "STATE", "KTYPE", "TYPE", "WHEREEXPRESSION", "MATHEXPRESSION", "TERM", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "KMEMBER", "MEMBER", "COMPAREEXPRESSION", "ASSIGNMENT", "IDEXPRESSION", "AGGREGATION", "CREATEVIEW", "QUERY", "NOT", "PREV", "FIRST", "CURRENT", "KOMMA"
    };
    public static final int TERM=65;
    public static final int WEEK=18;
    public static final int WHERE=8;
    public static final int MEMBER=70;
    public static final int MILLISECOND=23;
    public static final int POINT=35;
    public static final int KSTATE=59;
    public static final int LETTER=50;
    public static final int ATTRIBUTE=66;
    public static final int MAX=30;
    public static final int FLOAT=47;
    public static final int DAY=19;
    public static final int COUNT=32;
    public static final int EQUALS=40;
    public static final int NOT=77;
    public static final int SUM=31;
    public static final int AND=14;
    public static final int EOF=-1;
    public static final int MATHEXPRESSION=64;
    public static final int SPACE=54;
    public static final int TYPE=62;
    public static final int PATTERN=7;
    public static final int LBRACKET=44;
    public static final int SINGLEEQUALS=39;
    public static final int COMPAREEXPRESSION=71;
    public static final int NAME=51;
    public static final int CREATE=4;
    public static final int STRING_LITERAL=53;
    public static final int NOTSIGN=42;
    public static final int LEFTCURLY=12;
    public static final int SEQ=11;
    public static final int COMMA=43;
    public static final int AVG=28;
    public static final int KATTRIBUTE=67;
    public static final int RETURN=10;
    public static final int RIGHTCURLY=13;
    public static final int DIVISION=36;
    public static final int PLUS=33;
    public static final int DIGIT=49;
    public static final int RBRACKET=45;
    public static final int STREAM=5;
    public static final int BBRACKETRIGHT=17;
    public static final int INTEGER=46;
    public static final int STATE=60;
    public static final int IDEXPRESSION=73;
    public static final int SECOND=22;
    public static final int VIEW=6;
    public static final int KMEMBER=69;
    public static final int NUMBER=48;
    public static final int PARAMLIST=68;
    public static final int WHITESPACE=58;
    public static final int MIN=29;
    public static final int MULT=37;
    public static final int MINUS=34;
    public static final int HOUR=20;
    public static final int CURRENT=80;
    public static final int SKIP_TILL_NEXT_MATCH=24;
    public static final int PREV=78;
    public static final int WHEREEXPRESSION=63;
    public static final int NEWLINE=57;
    public static final int NONCONTROL_CHAR=52;
    public static final int BBRACKETLEFT=16;
    public static final int AGGREGATION=74;
    public static final int PARTITION_CONTIGUITY=27;
    public static final int KOMMA=81;
    public static final int ASSIGN=41;
    public static final int WITHIN=9;
    public static final int SKIP_TILL_ANY_MATCH=25;
    public static final int LEN=15;
    public static final int QUERY=76;
    public static final int ASSIGNMENT=72;
    public static final int LOWER=55;
    public static final int KTYPE=61;
    public static final int MINUTE=21;
    public static final int STRICT_CONTIGUITY=26;
    public static final int COMPAREOP=38;
    public static final int CREATEVIEW=75;
    public static final int UPPER=56;
    public static final int FIRST=79;

    // delegates
    // delegators


        public SaseAST(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public SaseAST(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return SaseAST.tokenNames; }
    public String getGrammarFileName() { return "E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g"; }



      static Logger _logger = null;
      static Logger getLogger(){
        if (_logger == null){
          _logger = LoggerFactory.getLogger(SaseAST.class);
        }
        return _logger;
      } 

    	List<String> simpleState = null;
    	List<String> kleeneState = null;
    	Map<String, String> simpleAttributeState = null;
    	Map<String, String> kleeneAttributeState = null;
    	ISymbolTableOperationFactory symTableOpFac = null;
    	User user = null;
    	
    	
      public void setUser(User user){
        this.user = user;
      }
      private String transformAttribute(PathAttribute attrib,State s){
          String index = ""; // getKleenePart.equals("[i]")
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
          
         return aggregation
              + CepVariable.getSeperator() + stateIdentifier
              + CepVariable.getSeperator() + index
              + CepVariable.getSeperator() + realAttributeName;
      }

    	 private String tranformAttributesToMetamodell(AttributeExpression ce, State s) {
        // In die Syntax des Metamodells umwandeln
        String ret = new String(ce.getFullExpression());
        for (PathAttribute attrib : ce.getAttributes()) {
          String fullAttribName = attrib.getFullAttributeName();
          String convertedAttributeName = transformAttribute(attrib,s);
          ret = replaceStrings(ret, fullAttribName, convertedAttributeName);
        }

        return ret;
      }

      private String replaceStrings(String ret, String oldString, String newString) {
    //     getLogger().debug("replace " + oldString + " in " + ret + " with "
    //     + newString);
        ret = ret.replace(oldString, newString);
    //     getLogger().debug("--> " + ret);
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
          
          private Transition getTransition(PathAttribute attr, State s){
               Transition t = null;
               if (attr != null) {
                 t = s.getTransition(s.getId() + "_begin");
                  if (t == null && attr.isKleeneAttribute()) {
                    if (attr.getKleenePart().equals("[i]")) {
                     t = s.getTransition(s.getId() + "_take");
                    } else {
                      t = s.getTransition(s.getId() + "_proceed");
                    }
                  }
               }
               return t;
          }




    // $ANTLR start "start"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:166:1: start returns [ILogicalOperator op] : ( ^( CREATEVIEW n= NAME q= query ) | o= query );
    public final ILogicalOperator start() throws RecognitionException {
        ILogicalOperator op = null;

        CommonTree n=null;
        ILogicalOperator q = null;

        ILogicalOperator o = null;


        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:167:3: ( ^( CREATEVIEW n= NAME q= query ) | o= query )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==CREATEVIEW) ) {
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
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:168:3: ^( CREATEVIEW n= NAME q= query )
                    {
                    match(input,CREATEVIEW,FOLLOW_CREATEVIEW_in_start75); 

                    match(input, Token.DOWN, null); 
                    n=(CommonTree)match(input,NAME,FOLLOW_NAME_in_start79); 
                    pushFollow(FOLLOW_query_in_start83);
                    q=query();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    	DataDictionary.getInstance().setView(n.getText(), q, user);
                    	  getLogger().debug("Created New View "+n+" "+q);
                    	  op = q;

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:172:5: o= query
                    {
                    pushFollow(FOLLOW_query_in_start97);
                    o=query();

                    state._fsp--;

                    op = o;

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return op;
    }
    // $ANTLR end "start"


    // $ANTLR start "query"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:175:1: query returns [ILogicalOperator op] : ^( QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo] ) ;
    public final ILogicalOperator query() throws RecognitionException {
        ILogicalOperator op = null;


            CepAO cepAo = new CepAO();
            simpleState = new ArrayList<String>();
            kleeneState = new ArrayList<String>();
            simpleAttributeState = new HashMap<String, String>();
            kleeneAttributeState = new HashMap<String, String>();
            List<String> sourceNames = new ArrayList<String>();

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:184:3: ( ^( QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo] ) )
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:185:3: ^( QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo] )
            {
            match(input,QUERY,FOLLOW_QUERY_in_query125); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_patternPart_in_query127);
            patternPart(cepAo, sourceNames);

            state._fsp--;

            pushFollow(FOLLOW_wherePart_in_query130);
            wherePart(cepAo);

            state._fsp--;

            pushFollow(FOLLOW_withinPart_in_query133);
            withinPart(cepAo);

            state._fsp--;

            pushFollow(FOLLOW_returnPart_in_query136);
            returnPart(cepAo);

            state._fsp--;


            match(input, Token.UP, null); 

                // Initialize Schema
                cepAo.getStateMachine().getSymTabScheme(true);
                getLogger().debug("Created State Machine "+cepAo.getStateMachine());
                // Set Inputs for cepAO;
                int port = 0;
                for (String sn : sourceNames) {
                  getLogger().debug("Bind "+sn+" to Port "+port);      
                  ILogicalOperator ao = DataDictionary.getInstance().getViewOrStream(sn, user);
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

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return op;
    }
    // $ANTLR end "query"


    // $ANTLR start "patternPart"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:208:1: patternPart[CepAO cepAo, List<String> sourceNames] : ^( SEQ ( state[states, sourceNames] )* ) ;
    public final void patternPart(CepAO cepAo, List<String> sourceNames) throws RecognitionException {

        List<State> states = new LinkedList<State>();

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:212:3: ( ^( SEQ ( state[states, sourceNames] )* ) )
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:213:3: ^( SEQ ( state[states, sourceNames] )* )
            {
            match(input,SEQ,FOLLOW_SEQ_in_patternPart164); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:213:9: ( state[states, sourceNames] )*
                loop2:
                do {
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( ((LA2_0>=KSTATE && LA2_0<=STATE)) ) {
                        alt2=1;
                    }


                    switch (alt2) {
                	case 1 :
                	    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:213:9: state[states, sourceNames]
                	    {
                	    pushFollow(FOLLOW_state_in_patternPart166);
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
                  sm.setInitialState(states.get(0).getId());
                  // Calculate transistions
                  for (int i = 0; i < states.size() - 1; i++) {
                     State source = states.get(i);
                     State dest = states.get(i + 1);
                     if (source.getId().endsWith("[i]")) {
                       RelationalMEPCondition con = new RelationalMEPCondition("");
                       con.setEventType(source.getType());
                       con.setEventPort(sourceNames.indexOf(source.getType()));
                       // TODO: Ist das mit dem EAction.discard immer richtig?
                       source.addTransition(new Transition(source.getId()+ "_proceed", dest, con, EAction.discard));
                       con = new RelationalMEPCondition("");
                       con.setEventType(source.getType());
                       con.setEventPort(sourceNames.indexOf(source.getType()));
                       source.addTransition(new Transition(source.getId()+ "_take", source,con, EAction.consumeBufferWrite));
                     } else {
                        RelationalMEPCondition con = new RelationalMEPCondition("");
                        con.setEventType(source.getType());
                        con.setEventPort(sourceNames.indexOf(source.getType()));
                        source.addTransition(new Transition(source.getId()+ "_begin", dest, con,EAction.consumeBufferWrite));
                     }
                     if (i > 0 && i < states.size() - 1) {
                        RelationalMEPCondition con = new RelationalMEPCondition("");
                        con.setEventType(source.getType());
                        con.setEventPort(sourceNames.indexOf(source.getType()));
                        // Ignore auf sich selbst!
                        source.addTransition(new Transition(source.getId()+ "_ignore", source, con,EAction.discard));
                      }
                   }
                } else {
                    throw new RuntimeException("Illegal Sequence");
                }
              

            }

        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "patternPart"


    // $ANTLR start "state"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:254:1: state[List<State> states, List<String> sourceNames] : ( ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? ) | ^( KSTATE statename= NAME attrName= NAME (not= NOTSIGN )? ) );
    public final void state(List<State> states, List<String> sourceNames) throws RecognitionException {
        CommonTree statename=null;
        CommonTree attrName=null;
        CommonTree not=null;

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:255:3: ( ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? ) | ^( KSTATE statename= NAME attrName= NAME (not= NOTSIGN )? ) )
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
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:256:3: ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? )
                    {
                    match(input,STATE,FOLLOW_STATE_in_state190); 

                    match(input, Token.DOWN, null); 
                    statename=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state194); 
                    attrName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state198); 
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:256:43: (not= NOTSIGN )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==NOTSIGN) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:256:43: not= NOTSIGN
                            {
                            not=(CommonTree)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_state202); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    		  if (not != null) throw new RuntimeException("Negative states not supported now!");
                    		  String _statename = statename.getText();
                    		  if (!sourceNames.contains(_statename)){
                    		    sourceNames.add(_statename);
                    		  }
                    		  String _attributeName = attrName.getText();
                    		//	getLogger().debug("Einfacher Zustand "+_statename+" "+_attributeName);
                    			// Anm. _statename darf mehrfach vorkommen  Variablenname nicht
                    			simpleState.add(_statename);
                    			if (simpleAttributeState.get(_attributeName) == null){
                    				simpleAttributeState.put(_attributeName,_statename);
                    				states.add(new State(_attributeName,_attributeName, _statename, false));
                    			}else{
                            throw new RuntimeException("Double attribute definition "+_attributeName); 
                    			}
                    		

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:275:3: ^( KSTATE statename= NAME attrName= NAME (not= NOTSIGN )? )
                    {
                    match(input,KSTATE,FOLLOW_KSTATE_in_state217); 

                    match(input, Token.DOWN, null); 
                    statename=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state221); 
                    attrName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state225); 
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:275:44: (not= NOTSIGN )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==NOTSIGN) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:275:44: not= NOTSIGN
                            {
                            not=(CommonTree)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_state229); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    		  if (not != null) throw new RuntimeException("Negative states not supported now!");
                    		  String _statename = statename.getText();
                          String _attributeName = attrName.getText();
                    		 // getLogger().debug("Kleene Zustand "+_statename+" "+_attributeName);
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

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "state"


    // $ANTLR start "wherePart"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:293:1: wherePart[CepAO cepAo] : ( ^( WHERE str= whereStrat[cepAo.getStateMachine()] we= whereExpression[cepAo.getStateMachine()] ) | );
    public final void wherePart(CepAO cepAo) throws RecognitionException {
        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:294:3: ( ^( WHERE str= whereStrat[cepAo.getStateMachine()] we= whereExpression[cepAo.getStateMachine()] ) | )
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
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:295:3: ^( WHERE str= whereStrat[cepAo.getStateMachine()] we= whereExpression[cepAo.getStateMachine()] )
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_wherePart252); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_whereStrat_in_wherePart256);
                    whereStrat(cepAo.getStateMachine());

                    state._fsp--;

                    pushFollow(FOLLOW_whereExpression_in_wherePart261);
                    whereExpression(cepAo.getStateMachine());

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:297:3: 
                    {
                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "wherePart"


    // $ANTLR start "whereStrat"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:299:1: whereStrat[StateMachine sm] : ( ^( SKIP_TILL_NEXT_MATCH param= paramlist ) | ^( SKIP_TILL_ANY_MATCH param= paramlist ) | ^( STRICT_CONTIGUITY paramlist ) | ^( PARTITION_CONTIGUITY paramlist ) );
    public final void whereStrat(StateMachine sm) throws RecognitionException {
        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:300:3: ( ^( SKIP_TILL_NEXT_MATCH param= paramlist ) | ^( SKIP_TILL_ANY_MATCH param= paramlist ) | ^( STRICT_CONTIGUITY paramlist ) | ^( PARTITION_CONTIGUITY paramlist ) )
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
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:301:3: ^( SKIP_TILL_NEXT_MATCH param= paramlist )
                    {
                    match(input,SKIP_TILL_NEXT_MATCH,FOLLOW_SKIP_TILL_NEXT_MATCH_in_whereStrat284); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat288);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH);

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:304:3: ^( SKIP_TILL_ANY_MATCH param= paramlist )
                    {
                    match(input,SKIP_TILL_ANY_MATCH,FOLLOW_SKIP_TILL_ANY_MATCH_in_whereStrat302); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat306);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_ANY_MATCH);

                    }
                    break;
                case 3 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:307:3: ^( STRICT_CONTIGUITY paramlist )
                    {
                    match(input,STRICT_CONTIGUITY,FOLLOW_STRICT_CONTIGUITY_in_whereStrat320); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat322);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    sm.setEventSelectionStrategy(EEventSelectionStrategy.PARTITION_CONTIGUITY);

                    }
                    break;
                case 4 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:310:3: ^( PARTITION_CONTIGUITY paramlist )
                    {
                    match(input,PARTITION_CONTIGUITY,FOLLOW_PARTITION_CONTIGUITY_in_whereStrat336); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat338);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 
                    sm.setEventSelectionStrategy(EEventSelectionStrategy.STRICT_CONTIGUITY);

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "whereStrat"


    // $ANTLR start "paramlist"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:314:1: paramlist : ^( PARAMLIST ( attributeName )* ) ;
    public final void paramlist() throws RecognitionException {
        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:315:3: ( ^( PARAMLIST ( attributeName )* ) )
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:316:3: ^( PARAMLIST ( attributeName )* )
            {
            match(input,PARAMLIST,FOLLOW_PARAMLIST_in_paramlist359); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:316:15: ( attributeName )*
                loop8:
                do {
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( ((LA8_0>=ATTRIBUTE && LA8_0<=KATTRIBUTE)) ) {
                        alt8=1;
                    }


                    switch (alt8) {
                	case 1 :
                	    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:316:15: attributeName
                	    {
                	    pushFollow(FOLLOW_attributeName_in_paramlist361);
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

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "paramlist"


    // $ANTLR start "attributeName"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:320:1: attributeName : ( ^( KATTRIBUTE NAME ) | ^( ATTRIBUTE NAME ) );
    public final void attributeName() throws RecognitionException {
        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:321:3: ( ^( KATTRIBUTE NAME ) | ^( ATTRIBUTE NAME ) )
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
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:322:3: ^( KATTRIBUTE NAME )
                    {
                    match(input,KATTRIBUTE,FOLLOW_KATTRIBUTE_in_attributeName383); 

                    match(input, Token.DOWN, null); 
                    match(input,NAME,FOLLOW_NAME_in_attributeName385); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:324:3: ^( ATTRIBUTE NAME )
                    {
                    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeName395); 

                    match(input, Token.DOWN, null); 
                    match(input,NAME,FOLLOW_NAME_in_attributeName397); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "attributeName"


    // $ANTLR start "whereExpression"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:327:1: whereExpression[StateMachine stmachine] : ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] | assignment[stmachine, assignExpressions] )* ) ;
    public final void whereExpression(StateMachine stmachine) throws RecognitionException {

          List<AttributeExpression> compareExpressions = new ArrayList<AttributeExpression>();
          List assignExpressions = new ArrayList();

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:332:3: ( ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] | assignment[stmachine, assignExpressions] )* ) )
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:333:3: ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] | assignment[stmachine, assignExpressions] )* )
            {
            match(input,WHEREEXPRESSION,FOLLOW_WHEREEXPRESSION_in_whereExpression425); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:335:5: ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] | assignment[stmachine, assignExpressions] )*
                loop10:
                do {
                    int alt10=4;
                    switch ( input.LA(1) ) {
                    case COMPAREEXPRESSION:
                        {
                        alt10=1;
                        }
                        break;
                    case IDEXPRESSION:
                        {
                        alt10=2;
                        }
                        break;
                    case ASSIGNMENT:
                        {
                        alt10=3;
                        }
                        break;

                    }

                    switch (alt10) {
                	case 1 :
                	    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:336:7: compareExpression[stmachine,compareExpressions]
                	    {
                	    pushFollow(FOLLOW_compareExpression_in_whereExpression439);
                	    compareExpression(stmachine, compareExpressions);

                	    state._fsp--;


                	    }
                	    break;
                	case 2 :
                	    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:337:9: idexpression[stmachine.getStates(),compareExpressions]
                	    {
                	    pushFollow(FOLLOW_idexpression_in_whereExpression450);
                	    idexpression(stmachine.getStates(), compareExpressions);

                	    state._fsp--;


                	    }
                	    break;
                	case 3 :
                	    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:338:9: assignment[stmachine, assignExpressions]
                	    {
                	    pushFollow(FOLLOW_assignment_in_whereExpression461);
                	    assignment(stmachine, assignExpressions);

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop10;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

                List<State> finalStates = stmachine.getFinalStates();
                for (State finalState : finalStates) {
                  List<State> pathes = stmachine.getAllPathesToStart(finalState);
                  for (State s : pathes) {
                    Iterator<AttributeExpression> compareIter = compareExpressions
                        .iterator();
                    while (compareIter.hasNext()) {
                      AttributeExpression ce = compareIter.next();
                      PathAttribute attr = ce.get(s.getId());
                      if (attr != null) {
                        Transition t = getTransition(attr, s);
                        // Anpassen der Variablennamen für das Metamodell:
                        String fullExpression = tranformAttributesToMetamodell(ce, s);
                        t.appendAND(fullExpression);
                        compareIter.remove();
                      }
                    }// while
                    Iterator<AssignExpression> assignIter = assignExpressions.iterator();
                    while(assignIter.hasNext()){
                      AssignExpression ae = assignIter.next();
                      PathAttribute attr = ae.get(s.getId());
                      if (attr != null){
                        Transition t = getTransition(attr, s);
                        // Anpassen der Variablennamen für das Metamodell:
                        String fullExpression = tranformAttributesToMetamodell(ae, s);
                        t.addAssigment(transformAttribute(ae.getAttributToAssign(),s), fullExpression);
                        assignIter.remove();
                      }
                    }
                  }
                }
                
                List<State> states = stmachine.getStates();
                for (State s : states) {
                    Transition ignore = s.getTransition(s.getId() + "_ignore");
                    if (ignore != null) {
                      switch (stmachine.getEventSelectionStrategy()){
                        case SKIP_TILL_NEXT_MATCH:
                          ignore.getCondition().setEventTypeChecking(true);
            	            Transition t = s.getTransition(s.getId() + "_take");
            	            if (t == null){
            	              t = s.getTransition(s.getId() + "_begin");
            	            }
            	           ignore.appendAND(t.getCondition().getLabel());
            	           ignore.negateExpression();
            	           break;
            	          case STRICT_CONTIGUITY:
            	              ignore.getCondition().setEventTypeChecking(false);
                            ignore.appendAND("false");
                            break;  
                        case PARTITION_CONTIGUITY:
                            throw new RuntimeException("PARTITION_CONTIGUITY not implemented yet");
                            //break;
                        case SKIP_TILL_ANY_MATCH:      
                          ignore.getCondition().setEventTypeChecking(false);          
                        default:
                          ignore.getCondition().setEventTypeChecking(false);
            	        }
            	    
                  }
                }
                 
                   
              

            }

        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "whereExpression"


    // $ANTLR start "compareExpression"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:408:1: compareExpression[StateMachine sm,List<AttributeExpression> compareExpressions] : ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] ) ;
    public final void compareExpression(StateMachine sm, List<AttributeExpression> compareExpressions) throws RecognitionException {
        CommonTree op=null;


        		 StringBuffer left= new StringBuffer();
             StringBuffer right=new StringBuffer();
             List<PathAttribute> attribs = new ArrayList<PathAttribute>();

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:414:3: ( ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] ) )
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:415:3: ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] )
            {
            match(input,COMPAREEXPRESSION,FOLLOW_COMPAREEXPRESSION_in_compareExpression505); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_mathExpression_in_compareExpression507);
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

            pushFollow(FOLLOW_mathExpression_in_compareExpression547);
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
               compareExpressions.add(new AttributeExpression(attribs, ret.toString()));

            }

        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "compareExpression"


    // $ANTLR start "assignment"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:433:1: assignment[StateMachine sm,List assignExpressions] : ^( ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs] ) ;
    public final void assignment(StateMachine sm, List assignExpressions) throws RecognitionException {

          List<PathAttribute> singleAttrib = new ArrayList<PathAttribute>();
          List<PathAttribute> attribs = new ArrayList<PathAttribute>();
          StringBuffer right=new StringBuffer();

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:438:2: ( ^( ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs] ) )
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:439:1: ^( ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs] )
            {
            match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_assignment573); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_attributeTerm_in_assignment575);
            attributeTerm(singleAttrib);

            state._fsp--;

            pushFollow(FOLLOW_mathExpression_in_assignment578);
            mathExpression(right, attribs);

            state._fsp--;


            match(input, Token.UP, null); 

                assignExpressions.add(new AssignExpression(singleAttrib.get(0),attribs,right.toString()));


            }

        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "assignment"


    // $ANTLR start "mathExpression"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:443:1: mathExpression[StringBuffer exp, List<PathAttribute> attribs] : ( ^( (op= PLUS | op= MINUS | op= MULT | op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] ) | attributeTerm[tmpAttrib] | num= NUMBER | lit= STRING_LITERAL );
    public final void mathExpression(StringBuffer exp, List<PathAttribute> attribs) throws RecognitionException {
        CommonTree op=null;
        CommonTree num=null;
        CommonTree lit=null;


              StringBuffer left=new StringBuffer();
              StringBuffer right=new StringBuffer();
              List<PathAttribute> tmpAttrib = new ArrayList<PathAttribute>();

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:449:3: ( ^( (op= PLUS | op= MINUS | op= MULT | op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] ) | attributeTerm[tmpAttrib] | num= NUMBER | lit= STRING_LITERAL )
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
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:450:3: ^( (op= PLUS | op= MINUS | op= MULT | op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] )
                    {
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:451:5: (op= PLUS | op= MINUS | op= MULT | op= DIVISION )
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
                            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:452:7: op= PLUS
                            {
                            op=(CommonTree)match(input,PLUS,FOLLOW_PLUS_in_mathExpression615); 

                            }
                            break;
                        case 2 :
                            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:453:9: op= MINUS
                            {
                            op=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_mathExpression627); 

                            }
                            break;
                        case 3 :
                            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:454:9: op= MULT
                            {
                            op=(CommonTree)match(input,MULT,FOLLOW_MULT_in_mathExpression639); 

                            }
                            break;
                        case 4 :
                            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:455:9: op= DIVISION
                            {
                            op=(CommonTree)match(input,DIVISION,FOLLOW_DIVISION_in_mathExpression651); 

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_mathExpression_in_mathExpression663);
                    mathExpression(left, attribs);

                    state._fsp--;

                    pushFollow(FOLLOW_mathExpression_in_mathExpression666);
                    mathExpression(right, attribs);

                    state._fsp--;


                    match(input, Token.UP, null); 
                    exp.append("(").append(left).append(" ").append(op).append(right).append(")");

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:460:5: attributeTerm[tmpAttrib]
                    {
                    pushFollow(FOLLOW_attributeTerm_in_mathExpression682);
                    attributeTerm(tmpAttrib);

                    state._fsp--;


                        // TODO: Es darf nur ein Attribut drin sein!
                        PathAttribute p = tmpAttrib.get(0);
                        attribs.add(p);
                        exp.append(p);
                      

                    }
                    break;
                case 3 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:466:5: num= NUMBER
                    {
                    num=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_mathExpression693); 
                    exp.append(num.getText());

                    }
                    break;
                case 4 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:467:5: lit= STRING_LITERAL
                    {
                    lit=(CommonTree)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_mathExpression703); 
                    exp.append(lit.getText());

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "mathExpression"


    // $ANTLR start "attributeTerm"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:470:1: attributeTerm[List<PathAttribute> attribs] : ( ^( KMEMBER kAttributeUsage[name,usage] member= NAME ) | ^( MEMBER attribName= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT | AVG ) var= NAME (member= NAME )? ) );
    public final void attributeTerm(List<PathAttribute> attribs) throws RecognitionException {
        CommonTree member=null;
        CommonTree attribName=null;
        CommonTree op=null;
        CommonTree var=null;


              StringBuffer usage = new StringBuffer();
              StringBuffer name = new StringBuffer();

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:475:3: ( ^( KMEMBER kAttributeUsage[name,usage] member= NAME ) | ^( MEMBER attribName= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT | AVG ) var= NAME (member= NAME )? ) )
            int alt14=3;
            switch ( input.LA(1) ) {
            case KMEMBER:
                {
                alt14=1;
                }
                break;
            case MEMBER:
                {
                alt14=2;
                }
                break;
            case AGGREGATION:
                {
                alt14=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:476:3: ^( KMEMBER kAttributeUsage[name,usage] member= NAME )
                    {
                    match(input,KMEMBER,FOLLOW_KMEMBER_in_attributeTerm727); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_kAttributeUsage_in_attributeTerm729);
                    kAttributeUsage(name, usage);

                    state._fsp--;

                    member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm734); 

                    match(input, Token.UP, null); 

                          PathAttribute p = new PathAttribute(name.toString(),usage.toString(),member.getText(),Write.class.getSimpleName().toUpperCase());
                          attribs.add(p);
                          name = new StringBuffer(); 
                          usage = new StringBuffer();
                         

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:484:3: ^( MEMBER attribName= NAME member= NAME )
                    {
                    match(input,MEMBER,FOLLOW_MEMBER_in_attributeTerm748); 

                    match(input, Token.DOWN, null); 
                    attribName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm752); 
                    member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm756); 

                    match(input, Token.UP, null); 

                         PathAttribute p = new PathAttribute(attribName.getText(),null,member.getText(),Write.class.getSimpleName().toUpperCase()); 
                          attribs.add(p);

                    }
                    break;
                case 3 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:489:3: ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT | AVG ) var= NAME (member= NAME )? )
                    {
                    match(input,AGGREGATION,FOLLOW_AGGREGATION_in_attributeTerm775); 

                    match(input, Token.DOWN, null); 
                    op=(CommonTree)input.LT(1);
                    if ( (input.LA(1)>=AVG && input.LA(1)<=COUNT) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    var=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm846); 
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:499:5: (member= NAME )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==NAME) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:499:6: member= NAME
                            {
                            member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm855); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     
                        PathAttribute p = new PathAttribute(var.getText(),"[i-1]",member==null?"":""+member,
                            symTableOpFac.getOperation(op.getText()).getName());
                        attribs.add(p);
                       

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "attributeTerm"


    // $ANTLR start "kAttributeUsage"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:508:1: kAttributeUsage[StringBuffer name, StringBuffer usage] : ( ^( NAME CURRENT ) | ^( NAME FIRST ) | ^( NAME PREV ) | ^( NAME LEN ) );
    public final void kAttributeUsage(StringBuffer name, StringBuffer usage) throws RecognitionException {
        CommonTree NAME1=null;
        CommonTree NAME2=null;
        CommonTree NAME3=null;
        CommonTree NAME4=null;

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:509:3: ( ^( NAME CURRENT ) | ^( NAME FIRST ) | ^( NAME PREV ) | ^( NAME LEN ) )
            int alt15=4;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==NAME) ) {
                int LA15_1 = input.LA(2);

                if ( (LA15_1==DOWN) ) {
                    switch ( input.LA(3) ) {
                    case CURRENT:
                        {
                        alt15=1;
                        }
                        break;
                    case FIRST:
                        {
                        alt15=2;
                        }
                        break;
                    case PREV:
                        {
                        alt15=3;
                        }
                        break;
                    case LEN:
                        {
                        alt15=4;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 2, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:510:3: ^( NAME CURRENT )
                    {
                    NAME1=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage883); 

                    match(input, Token.DOWN, null); 
                    match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage885); 

                    match(input, Token.UP, null); 
                    usage.append("[i]"); name.append(NAME1);

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:513:3: ^( NAME FIRST )
                    {
                    NAME2=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage899); 

                    match(input, Token.DOWN, null); 
                    match(input,FIRST,FOLLOW_FIRST_in_kAttributeUsage901); 

                    match(input, Token.UP, null); 
                    usage.append("[1]");name.append(NAME2);

                    }
                    break;
                case 3 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:516:3: ^( NAME PREV )
                    {
                    NAME3=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage915); 

                    match(input, Token.DOWN, null); 
                    match(input,PREV,FOLLOW_PREV_in_kAttributeUsage917); 

                    match(input, Token.UP, null); 
                    usage.append("[i-1]");name.append(NAME3);

                    }
                    break;
                case 4 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:519:3: ^( NAME LEN )
                    {
                    NAME4=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage931); 

                    match(input, Token.DOWN, null); 
                    match(input,LEN,FOLLOW_LEN_in_kAttributeUsage933); 

                    match(input, Token.UP, null); 
                    usage.append("[i-1]");name.append(NAME4);

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "kAttributeUsage"


    // $ANTLR start "idexpression"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:523:1: idexpression[List<State> states, List<AttributeExpression> compareExpressions] : ^( IDEXPRESSION var= NAME ) ;
    public final void idexpression(List<State> states, List<AttributeExpression> compareExpressions) throws RecognitionException {
        CommonTree var=null;

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:524:3: ( ^( IDEXPRESSION var= NAME ) )
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:525:3: ^( IDEXPRESSION var= NAME )
            {
            match(input,IDEXPRESSION,FOLLOW_IDEXPRESSION_in_idexpression955); 

            match(input, Token.DOWN, null); 
            var=(CommonTree)match(input,NAME,FOLLOW_NAME_in_idexpression959); 

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
                        compareExpressions.add(new AttributeExpression(attr,expr.toString()));
                    }
              

            }

        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "idexpression"


    // $ANTLR start "withinPart"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:542:1: withinPart[CepAO cepAo] : ( ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? ) | );
    public final void withinPart(CepAO cepAo) throws RecognitionException {
        CommonTree value=null;
        CommonTree unit=null;

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:543:3: ( ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? ) | )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==WITHIN) ) {
                alt17=1;
            }
            else if ( (LA17_0==UP||LA17_0==RETURN) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:544:3: ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? )
                    {
                    match(input,WITHIN,FOLLOW_WITHIN_in_withinPart986); 

                    match(input, Token.DOWN, null); 
                    value=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart990); 
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:546:5: (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( ((LA16_0>=WEEK && LA16_0<=MILLISECOND)) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:547:7: unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND )
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
                        getLogger().debug("Setting Windowsize to "+time+" milliseconds");
                        cepAo.getStateMachine().setWindowSize(time);
                     

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:564:3: 
                    {
                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "withinPart"


    // $ANTLR start "returnPart"
    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:566:1: returnPart[CepAO cepAo] : ( ^( RETURN ( attributeTerm[retAttr] )* ) | );
    public final void returnPart(CepAO cepAo) throws RecognitionException {

        List<PathAttribute> retAttr = new ArrayList<PathAttribute>();

        try {
            // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:570:3: ( ^( RETURN ( attributeTerm[retAttr] )* ) | )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==RETURN) ) {
                alt19=1;
            }
            else if ( (LA19_0==UP) ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:571:3: ^( RETURN ( attributeTerm[retAttr] )* )
                    {
                    match(input,RETURN,FOLLOW_RETURN_in_returnPart1133); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:571:12: ( attributeTerm[retAttr] )*
                        loop18:
                        do {
                            int alt18=2;
                            int LA18_0 = input.LA(1);

                            if ( ((LA18_0>=KMEMBER && LA18_0<=MEMBER)||LA18_0==AGGREGATION) ) {
                                alt18=1;
                            }


                            switch (alt18) {
                        	case 1 :
                        	    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:571:12: attributeTerm[retAttr]
                        	    {
                        	    pushFollow(FOLLOW_attributeTerm_in_returnPart1135);
                        	    attributeTerm(retAttr);

                        	    state._fsp--;


                        	    }
                        	    break;

                        	default :
                        	    break loop18;
                            }
                        } while (true);


                        match(input, Token.UP, null); 
                    }

                        RelationalMEPOutputSchemeEntry e = null;
                        OutputScheme scheme = new OutputScheme();
                        SDFAttributeList attrList = new SDFAttributeList();
                        for (PathAttribute p : retAttr) {
                          String op = p.getAggregation();
                          String a = p.getStatename();
                          String i = p.getKleenePart();
                          if ("[i]".equals(i)){
                            i = "";
                          }else if ("[i-1]".equals(i)){
                            i = "-1";
                          }
                          String path = p.getPath();
                          e = new RelationalMEPOutputSchemeEntry(CepVariable.getStringFor(op, a, i, a
                              + "." + path));
                          scheme.append(e);
                          SDFAttribute attr = new SDFAttribute(e.getLabel());
                          // TODO: Set correct Datatypes
                          attr.setDatatype(new SDFDatatype("String"));
                          attrList.add(attr);
                        }
                        cepAo.getStateMachine().setOutputScheme(scheme);    
                        cepAo.setOutputSchema(attrList);
                      

                    }
                    break;
                case 2 :
                    // E:\\Dev\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:598:3: 
                    {
                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "returnPart"

    // Delegated rules


 

    public static final BitSet FOLLOW_CREATEVIEW_in_start75 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_start79 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_query_in_start83 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_query_in_start97 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUERY_in_query125 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_patternPart_in_query127 = new BitSet(new long[]{0x0000000000000708L});
    public static final BitSet FOLLOW_wherePart_in_query130 = new BitSet(new long[]{0x0000000000000608L});
    public static final BitSet FOLLOW_withinPart_in_query133 = new BitSet(new long[]{0x0000000000000408L});
    public static final BitSet FOLLOW_returnPart_in_query136 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEQ_in_patternPart164 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_state_in_patternPart166 = new BitSet(new long[]{0x1800000000000008L});
    public static final BitSet FOLLOW_STATE_in_state190 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_state194 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_state198 = new BitSet(new long[]{0x0000040000000008L});
    public static final BitSet FOLLOW_NOTSIGN_in_state202 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_KSTATE_in_state217 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_state221 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_state225 = new BitSet(new long[]{0x0000040000000008L});
    public static final BitSet FOLLOW_NOTSIGN_in_state229 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHERE_in_wherePart252 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_whereStrat_in_wherePart256 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_whereExpression_in_wherePart261 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SKIP_TILL_NEXT_MATCH_in_whereStrat284 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat288 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SKIP_TILL_ANY_MATCH_in_whereStrat302 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat306 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STRICT_CONTIGUITY_in_whereStrat320 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat322 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARTITION_CONTIGUITY_in_whereStrat336 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat338 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARAMLIST_in_paramlist359 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeName_in_paramlist361 = new BitSet(new long[]{0x0000000000000008L,0x000000000000000CL});
    public static final BitSet FOLLOW_KATTRIBUTE_in_attributeName383 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeName385 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeName395 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeName397 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHEREEXPRESSION_in_whereExpression425 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_compareExpression_in_whereExpression439 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000380L});
    public static final BitSet FOLLOW_idexpression_in_whereExpression450 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000380L});
    public static final BitSet FOLLOW_assignment_in_whereExpression461 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000380L});
    public static final BitSet FOLLOW_COMPAREEXPRESSION_in_compareExpression505 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mathExpression_in_compareExpression507 = new BitSet(new long[]{0x000000C000000000L});
    public static final BitSet FOLLOW_set_in_compareExpression517 = new BitSet(new long[]{0x0021003600000000L,0x0000000000000460L});
    public static final BitSet FOLLOW_mathExpression_in_compareExpression547 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_assignment573 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeTerm_in_assignment575 = new BitSet(new long[]{0x0021003600000000L,0x0000000000000460L});
    public static final BitSet FOLLOW_mathExpression_in_assignment578 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PLUS_in_mathExpression615 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MINUS_in_mathExpression627 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MULT_in_mathExpression639 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIVISION_in_mathExpression651 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mathExpression_in_mathExpression663 = new BitSet(new long[]{0x0021003600000000L,0x0000000000000460L});
    public static final BitSet FOLLOW_mathExpression_in_mathExpression666 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_attributeTerm_in_mathExpression682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_mathExpression693 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_mathExpression703 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KMEMBER_in_attributeTerm727 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_kAttributeUsage_in_attributeTerm729 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm734 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MEMBER_in_attributeTerm748 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm752 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm756 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AGGREGATION_in_attributeTerm775 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_attributeTerm784 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm846 = new BitSet(new long[]{0x0008000000000008L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm855 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage883 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage885 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage899 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_FIRST_in_kAttributeUsage901 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage915 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_PREV_in_kAttributeUsage917 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage931 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_LEN_in_kAttributeUsage933 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDEXPRESSION_in_idexpression955 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_idexpression959 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart986 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart990 = new BitSet(new long[]{0x0000000000FC0008L});
    public static final BitSet FOLLOW_set_in_withinPart1013 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RETURN_in_returnPart1133 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart1135 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000460L});

}