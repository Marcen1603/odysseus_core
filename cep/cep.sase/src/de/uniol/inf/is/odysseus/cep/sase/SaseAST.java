// $ANTLR 3.4 C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g 2012-03-16 14:33:53

/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
	package de.uniol.inf.is.odysseus.cep.sase; 
	import java.util.LinkedList;
	import java.util.Map;
	import java.util.HashMap;
	import java.util.Iterator;
	
	import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
	import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
	import de.uniol.inf.is.odysseus.cep.CepAO;
	import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
	import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
	import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
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
	import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
	import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
	import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
		
	import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class SaseAST extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALT", "AND", "AS", "ASSIGN", "AVG", "BBRACKETLEFT", "BBRACKETRIGHT", "COMMA", "COMPAREOP", "COUNT", "CREATE", "DAY", "DIGIT", "DIVISION", "EQUALS", "FLOAT", "HOUR", "INTEGER", "LBRACKET", "LEFTCURLY", "LEN", "LETTER", "LOWER", "MAX", "MILLISECOND", "MIN", "MINUS", "MINUTE", "MULT", "NAME", "NEWLINE", "NONCONTROL_CHAR", "NOTSIGN", "NUMBER", "PARTITION_CONTIGUITY", "PATTERN", "PLUS", "POINT", "RBRACKET", "RETURN", "RIGHTCURLY", "SECOND", "SEQ", "SINGLEEQUALS", "SKIP_TILL_ANY_MATCH", "SKIP_TILL_NEXT_MATCH", "SPACE", "STREAM", "STRICT_CONTIGUITY", "STRING_LITERAL", "SUM", "UPPER", "VIEW", "WEEK", "WHERE", "WHITESPACE", "WITHIN", "AGGREGATION", "ASSIGNMENT", "ATTRIBUTE", "COMPAREEXPRESSION", "CREATEVIEW", "CURRENT", "FIRST", "IDEXPRESSION", "KATTRIBUTE", "KMEMBER", "KSTATE", "KTYPE", "MATHEXPRESSION", "MEMBER", "NOT", "PARAMLIST", "PREV", "QUERY", "STATE", "TERM", "TYPE", "WHEREEXPRESSION"
    };

    public static final int EOF=-1;
    public static final int ALT=4;
    public static final int AND=5;
    public static final int AS=6;
    public static final int ASSIGN=7;
    public static final int AVG=8;
    public static final int BBRACKETLEFT=9;
    public static final int BBRACKETRIGHT=10;
    public static final int COMMA=11;
    public static final int COMPAREOP=12;
    public static final int COUNT=13;
    public static final int CREATE=14;
    public static final int DAY=15;
    public static final int DIGIT=16;
    public static final int DIVISION=17;
    public static final int EQUALS=18;
    public static final int FLOAT=19;
    public static final int HOUR=20;
    public static final int INTEGER=21;
    public static final int LBRACKET=22;
    public static final int LEFTCURLY=23;
    public static final int LEN=24;
    public static final int LETTER=25;
    public static final int LOWER=26;
    public static final int MAX=27;
    public static final int MILLISECOND=28;
    public static final int MIN=29;
    public static final int MINUS=30;
    public static final int MINUTE=31;
    public static final int MULT=32;
    public static final int NAME=33;
    public static final int NEWLINE=34;
    public static final int NONCONTROL_CHAR=35;
    public static final int NOTSIGN=36;
    public static final int NUMBER=37;
    public static final int PARTITION_CONTIGUITY=38;
    public static final int PATTERN=39;
    public static final int PLUS=40;
    public static final int POINT=41;
    public static final int RBRACKET=42;
    public static final int RETURN=43;
    public static final int RIGHTCURLY=44;
    public static final int SECOND=45;
    public static final int SEQ=46;
    public static final int SINGLEEQUALS=47;
    public static final int SKIP_TILL_ANY_MATCH=48;
    public static final int SKIP_TILL_NEXT_MATCH=49;
    public static final int SPACE=50;
    public static final int STREAM=51;
    public static final int STRICT_CONTIGUITY=52;
    public static final int STRING_LITERAL=53;
    public static final int SUM=54;
    public static final int UPPER=55;
    public static final int VIEW=56;
    public static final int WEEK=57;
    public static final int WHERE=58;
    public static final int WHITESPACE=59;
    public static final int WITHIN=60;
    public static final int AGGREGATION=61;
    public static final int ASSIGNMENT=62;
    public static final int ATTRIBUTE=63;
    public static final int COMPAREEXPRESSION=64;
    public static final int CREATEVIEW=65;
    public static final int CURRENT=66;
    public static final int FIRST=67;
    public static final int IDEXPRESSION=68;
    public static final int KATTRIBUTE=69;
    public static final int KMEMBER=70;
    public static final int KSTATE=71;
    public static final int KTYPE=72;
    public static final int MATHEXPRESSION=73;
    public static final int MEMBER=74;
    public static final int NOT=75;
    public static final int PARAMLIST=76;
    public static final int PREV=77;
    public static final int QUERY=78;
    public static final int STATE=79;
    public static final int TERM=80;
    public static final int TYPE=81;
    public static final int WHEREEXPRESSION=82;

    // delegates
    public TreeParser[] getDelegates() {
        return new TreeParser[] {};
    }

    // delegators


    public SaseAST(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }
    public SaseAST(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return SaseAST.tokenNames; }
    public String getGrammarFileName() { return "C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g"; }


    static Logger _logger = null;

    static Logger getLogger() {
    	if (_logger == null) {
    		_logger = LoggerFactory.getLogger(SaseAST.class);
    	}
    	return _logger;
    }

    List<String> simpleState = null;
    List<String> kleeneState = null;
    Map<String, String> simpleAttributeState = null;
    Map<String, String> kleeneAttributeState = null;
    ISymbolTableOperationFactory symTableOpFac = null;
    ISession user = null;
    IDataDictionary dd = null;
    boolean attachSources;

    public void setUser(ISession user) {
    	this.user = user;
    }

    public void setDataDictionary(IDataDictionary dd) {
    	this.dd = dd;
    }

    private String transformAttribute(PathAttribute attrib, State s) {
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

    	return aggregation + CepVariable.getSeperator() + stateIdentifier
    			+ CepVariable.getSeperator() + index + CepVariable.getSeperator()
    			+ realAttributeName;
    }

    private String tranformAttributesToMetamodell(AttributeExpression ce, State s) {
    	// In die Syntax des Metamodells umwandeln
    	String ret = new String(ce.getFullExpression());
    	for (PathAttribute attrib : ce.getAttributes()) {
    		String fullAttribName = attrib.getFullAttributeName();
    		String convertedAttributeName = transformAttribute(attrib, s);
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

    private long getTime(String value, CommonTree unit) {

    	long time = Long.parseLong(value);
    	if (unit != null) {
    		int uType = unit.getToken().getType();
    		if (uType == WEEK) {
    			time *= 7 * 24 * 60 * 60 * 1000;
    		} else if (uType == DAY) {
    			time *= 24 * 60 * 60 * 1000;
    		} else if (uType == HOUR) {
    			time *= 60 * 60 * 1000;
    		} else if (uType == MINUTE) {
    			time *= 60 * 1000;
    		} else if (uType == SECOND) {
    			time *= 1000;
    		}
    	}
    	return time;
    }

    private Transition getTransition(PathAttribute attr, State s) {
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
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:187:1: start[boolean attachSources] returns [ILogicalOperator op] : ( ^( CREATEVIEW n= NAME q= query ) |o= query );
    public final ILogicalOperator start(boolean attachSources) throws RecognitionException {
        ILogicalOperator op = null;


        CommonTree n=null;
        ILogicalOperator q =null;

        ILogicalOperator o =null;



        this.attachSources = attachSources;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:191:3: ( ^( CREATEVIEW n= NAME q= query ) |o= query )
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:192:3: ^( CREATEVIEW n= NAME q= query )
                    {
                    match(input,CREATEVIEW,FOLLOW_CREATEVIEW_in_start83); 

                    match(input, Token.DOWN, null); 
                    n=(CommonTree)match(input,NAME,FOLLOW_NAME_in_start87); 

                    pushFollow(FOLLOW_query_in_start91);
                    q=query();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        try{
                            dd.setView(n.getText(), q, user);
                        }catch(DataDictionaryException e){
                            throw new QueryParseException(e.getMessage());
                        }
                        getLogger().debug("Created New View " + n + " " + q);
                        op = q;
                       

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:203:5: o= query
                    {
                    pushFollow(FOLLOW_query_in_start109);
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
        	// do for sure before leaving
        }
        return op;
    }
    // $ANTLR end "start"



    // $ANTLR start "query"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:209:1: query returns [ILogicalOperator op] : ^( QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo] ) ;
    public final ILogicalOperator query() throws RecognitionException {
        ILogicalOperator op = null;



        CepAO cepAo = new CepAO();
        simpleState = new ArrayList<String>();
        kleeneState = new ArrayList<String>();
        simpleAttributeState = new HashMap<String, String>();
        kleeneAttributeState = new HashMap<String, String>();
        List<String> sourceNames = new ArrayList<String>();

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:218:3: ( ^( QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo] ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:219:3: ^( QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo] )
            {
            match(input,QUERY,FOLLOW_QUERY_in_query150); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_patternPart_in_query152);
            patternPart(cepAo, sourceNames);

            state._fsp--;


            pushFollow(FOLLOW_wherePart_in_query155);
            wherePart(cepAo);

            state._fsp--;


            pushFollow(FOLLOW_withinPart_in_query158);
            withinPart(cepAo);

            state._fsp--;


            pushFollow(FOLLOW_returnPart_in_query161);
            returnPart(cepAo);

            state._fsp--;


            match(input, Token.UP, null); 



                // Initialize Schema
                cepAo.getStateMachine().getSymTabScheme(true);
                getLogger().debug("Created State Machine " + cepAo.getStateMachine());
                if (attachSources) {
                	// Set Inputs for cepAO;
                	int port = 0;
                	for (String sn : sourceNames) {
                		getLogger().debug("Bind " + sn + " to Port " + port);
                		try {
                			ILogicalOperator ao = dd.getViewOrStream(sn, user);
                			cepAo.subscribeToSource(ao, port, 0, ao.getOutputSchema());
                			cepAo.setInputTypeName(port, sn);
                			port++;
                		} catch (Exception e) {
                			throw new QueryParseException("Source/View " + sn + " not found");
                		}
                	}
                }
                cepAo.prepareNegation();
                op = cepAo;
               

            }

        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return op;
    }
    // $ANTLR end "query"



    // $ANTLR start "patternPart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:245:1: patternPart[CepAO cepAo, List<String> sourceNames] : ^( PATTERN seqPatternPart[cepAo, sourceNames] ) ;
    public final void patternPart(CepAO cepAo, List<String> sourceNames) throws RecognitionException {
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:246:3: ( ^( PATTERN seqPatternPart[cepAo, sourceNames] ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:247:3: ^( PATTERN seqPatternPart[cepAo, sourceNames] )
            {
            match(input,PATTERN,FOLLOW_PATTERN_in_patternPart188); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_seqPatternPart_in_patternPart190);
            seqPatternPart(cepAo, sourceNames);

            state._fsp--;


            match(input, Token.UP, null); 


            }

        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "patternPart"



    // $ANTLR start "seqPatternPart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:250:1: seqPatternPart[CepAO cepAo, List<String> sourceNames] : ^( SEQ ( state[states, sourceNames] )* ) ;
    public final void seqPatternPart(CepAO cepAo, List<String> sourceNames) throws RecognitionException {

        List<State> states = new LinkedList<State>();

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:254:3: ( ^( SEQ ( state[states, sourceNames] )* ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:255:3: ^( SEQ ( state[states, sourceNames] )* )
            {
            match(input,SEQ,FOLLOW_SEQ_in_seqPatternPart214); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:255:9: ( state[states, sourceNames] )*
                loop2:
                do {
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==KSTATE||LA2_0==STATE) ) {
                        alt2=1;
                    }


                    switch (alt2) {
                	case 1 :
                	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:255:9: state[states, sourceNames]
                	    {
                	    pushFollow(FOLLOW_state_in_seqPatternPart216);
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
                	states.add(new State("<ACCEPTING>", "", null, true, false));
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
                			source.addTransition(new Transition(source.getId() + "_proceed",
                					dest, con, EAction.discard));
                			con = new RelationalMEPCondition("");
                			con.setEventType(source.getType());
                			con.setEventPort(sourceNames.indexOf(source.getType()));
                			source.addTransition(new Transition(source.getId() + "_take",
                					source, con, EAction.consumeBufferWrite));
                		} else {
                			RelationalMEPCondition con = new RelationalMEPCondition("");
                			con.setEventType(source.getType());
                			con.setEventPort(sourceNames.indexOf(source.getType()));
                			source.addTransition(new Transition(source.getId() + "_begin",
                					dest, con, EAction.consumeBufferWrite));
                		}
                		if (i > 0 && i < states.size() - 1) {
                			RelationalMEPCondition con = new RelationalMEPCondition("");
                			con.setEventType(source.getType());
                			con.setEventPort(sourceNames.indexOf(source.getType()));
                			// Ignore auf sich selbst!
                			source.addTransition(new Transition(source.getId() + "_ignore",
                					source, con, EAction.discard));
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
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "seqPatternPart"



    // $ANTLR start "state"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:301:1: state[List<State> states, List<String> sourceNames] : ( ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? ) | ^( KSTATE statename= NAME attrName= NAME (not= NOTSIGN )? ) );
    public final void state(List<State> states, List<String> sourceNames) throws RecognitionException {
        CommonTree statename=null;
        CommonTree attrName=null;
        CommonTree not=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:302:3: ( ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? ) | ^( KSTATE statename= NAME attrName= NAME (not= NOTSIGN )? ) )
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:303:3: ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? )
                    {
                    match(input,STATE,FOLLOW_STATE_in_state244); 

                    match(input, Token.DOWN, null); 
                    statename=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state248); 

                    attrName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state252); 

                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:303:43: (not= NOTSIGN )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==NOTSIGN) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:303:43: not= NOTSIGN
                            {
                            not=(CommonTree)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_state256); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 



                        String _statename = statename.getText();
                        if (!sourceNames.contains(_statename)) {
                        	sourceNames.add(_statename);
                        }
                        String _attributeName = attrName.getText();
                        //	getLogger().debug("Einfacher Zustand "+_statename+" "+_attributeName);
                        // Anm. _statename darf mehrfach vorkommen  Variablenname nicht
                        simpleState.add(_statename);
                        if (simpleAttributeState.get(_attributeName) == null) {
                        	simpleAttributeState.put(_attributeName, _statename);
                        	State state = new State(_attributeName, _attributeName, _statename, false,
                        			not != null);
                        	states.add(state);
                        } else {
                        	throw new RuntimeException("Double attribute definition " + _attributeName);
                        }
                       

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:324:3: ^( KSTATE statename= NAME attrName= NAME (not= NOTSIGN )? )
                    {
                    match(input,KSTATE,FOLLOW_KSTATE_in_state275); 

                    match(input, Token.DOWN, null); 
                    statename=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state279); 

                    attrName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state283); 

                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:324:44: (not= NOTSIGN )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==NOTSIGN) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:324:44: not= NOTSIGN
                            {
                            not=(CommonTree)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_state287); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 



                        String _statename = statename.getText();
                        String _attributeName = attrName.getText();
                        // getLogger().debug("Kleene Zustand "+_statename+" "+_attributeName);
                        kleeneState.add(_statename);
                        sourceNames.add(_statename);
                        if (kleeneAttributeState.get(_attributeName) == null) {
                        	kleeneAttributeState.put(_attributeName, _statename);
                        	states.add(new State(_attributeName + "[1]", _attributeName, _statename,
                        			false, not != null));
                        	states.add(new State(_attributeName + "[i]", _attributeName, _statename,
                        			false, not != null));
                        } else {
                        	throw new RuntimeException("Double attribute definition " + _attributeName);
                        }
                       

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "state"



    // $ANTLR start "wherePart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:344:1: wherePart[CepAO cepAo] : ( ^( WHERE str= whereStrat[cepAo.getStateMachine()] we= whereExpression[cepAo.getStateMachine()] ) |);
    public final void wherePart(CepAO cepAo) throws RecognitionException {
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:345:3: ( ^( WHERE str= whereStrat[cepAo.getStateMachine()] we= whereExpression[cepAo.getStateMachine()] ) |)
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WHERE) ) {
                alt6=1;
            }
            else if ( (LA6_0==UP||LA6_0==RETURN||LA6_0==WITHIN) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:346:3: ^( WHERE str= whereStrat[cepAo.getStateMachine()] we= whereExpression[cepAo.getStateMachine()] )
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_wherePart314); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_whereStrat_in_wherePart318);
                    whereStrat(cepAo.getStateMachine());

                    state._fsp--;


                    pushFollow(FOLLOW_whereExpression_in_wherePart323);
                    whereExpression(cepAo.getStateMachine());

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:348:3: 
                    {
                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "wherePart"



    // $ANTLR start "whereStrat"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:350:1: whereStrat[StateMachine sm] : ( ^( SKIP_TILL_NEXT_MATCH param= paramlist ) | ^( SKIP_TILL_ANY_MATCH param= paramlist ) | ^( STRICT_CONTIGUITY paramlist ) | ^( PARTITION_CONTIGUITY paramlist ) );
    public final void whereStrat(StateMachine sm) throws RecognitionException {
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:351:3: ( ^( SKIP_TILL_NEXT_MATCH param= paramlist ) | ^( SKIP_TILL_ANY_MATCH param= paramlist ) | ^( STRICT_CONTIGUITY paramlist ) | ^( PARTITION_CONTIGUITY paramlist ) )
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:352:3: ^( SKIP_TILL_NEXT_MATCH param= paramlist )
                    {
                    match(input,SKIP_TILL_NEXT_MATCH,FOLLOW_SKIP_TILL_NEXT_MATCH_in_whereStrat346); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat350);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH);
                       

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:358:3: ^( SKIP_TILL_ANY_MATCH param= paramlist )
                    {
                    match(input,SKIP_TILL_ANY_MATCH,FOLLOW_SKIP_TILL_ANY_MATCH_in_whereStrat368); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat372);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_ANY_MATCH);
                       

                    }
                    break;
                case 3 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:364:3: ^( STRICT_CONTIGUITY paramlist )
                    {
                    match(input,STRICT_CONTIGUITY,FOLLOW_STRICT_CONTIGUITY_in_whereStrat390); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat392);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        sm.setEventSelectionStrategy(EEventSelectionStrategy.PARTITION_CONTIGUITY);
                       

                    }
                    break;
                case 4 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:370:3: ^( PARTITION_CONTIGUITY paramlist )
                    {
                    match(input,PARTITION_CONTIGUITY,FOLLOW_PARTITION_CONTIGUITY_in_whereStrat410); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat412);
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
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "whereStrat"



    // $ANTLR start "paramlist"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:377:1: paramlist : ^( PARAMLIST ( attributeName )* ) ;
    public final void paramlist() throws RecognitionException {
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:378:3: ( ^( PARAMLIST ( attributeName )* ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:379:3: ^( PARAMLIST ( attributeName )* )
            {
            match(input,PARAMLIST,FOLLOW_PARAMLIST_in_paramlist437); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:379:15: ( attributeName )*
                loop8:
                do {
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==ATTRIBUTE||LA8_0==KATTRIBUTE) ) {
                        alt8=1;
                    }


                    switch (alt8) {
                	case 1 :
                	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:379:15: attributeName
                	    {
                	    pushFollow(FOLLOW_attributeName_in_paramlist439);
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



                System.err
                		.println("WARNING: Attributes currently ignored in selection strategy");
               

            }

        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "paramlist"



    // $ANTLR start "attributeName"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:387:1: attributeName : ( ^( KATTRIBUTE NAME ) | ^( ATTRIBUTE NAME ) );
    public final void attributeName() throws RecognitionException {
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:388:3: ( ^( KATTRIBUTE NAME ) | ^( ATTRIBUTE NAME ) )
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:389:3: ^( KATTRIBUTE NAME )
                    {
                    match(input,KATTRIBUTE,FOLLOW_KATTRIBUTE_in_attributeName465); 

                    match(input, Token.DOWN, null); 
                    match(input,NAME,FOLLOW_NAME_in_attributeName467); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:391:3: ^( ATTRIBUTE NAME )
                    {
                    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeName477); 

                    match(input, Token.DOWN, null); 
                    match(input,NAME,FOLLOW_NAME_in_attributeName479); 

                    match(input, Token.UP, null); 


                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "attributeName"



    // $ANTLR start "whereExpression"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:394:1: whereExpression[StateMachine stmachine] : ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] | assignment[stmachine, assignExpressions] )* ) ;
    public final void whereExpression(StateMachine stmachine) throws RecognitionException {

        List<AttributeExpression> compareExpressions = new ArrayList<AttributeExpression>();
        List assignExpressions = new ArrayList();

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:399:3: ( ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] | assignment[stmachine, assignExpressions] )* ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:400:3: ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] | assignment[stmachine, assignExpressions] )* )
            {
            match(input,WHEREEXPRESSION,FOLLOW_WHEREEXPRESSION_in_whereExpression507); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:402:5: ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),compareExpressions] | assignment[stmachine, assignExpressions] )*
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
                	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:403:7: compareExpression[stmachine,compareExpressions]
                	    {
                	    pushFollow(FOLLOW_compareExpression_in_whereExpression521);
                	    compareExpression(stmachine, compareExpressions);

                	    state._fsp--;


                	    }
                	    break;
                	case 2 :
                	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:404:9: idexpression[stmachine.getStates(),compareExpressions]
                	    {
                	    pushFollow(FOLLOW_idexpression_in_whereExpression532);
                	    idexpression(stmachine.getStates(), compareExpressions);

                	    state._fsp--;


                	    }
                	    break;
                	case 3 :
                	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:405:9: assignment[stmachine, assignExpressions]
                	    {
                	    pushFollow(FOLLOW_assignment_in_whereExpression543);
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
                		while (assignIter.hasNext()) {
                			AssignExpression ae = assignIter.next();
                			PathAttribute attr = ae.get(s.getId());
                			if (attr != null) {
                				Transition t = getTransition(attr, s);
                				// Anpassen der Variablennamen für das Metamodell:
                				String fullExpression = tranformAttributesToMetamodell(ae, s);
                				t.addAssigment(transformAttribute(ae.getAttributToAssign(), s),
                						fullExpression);
                				assignIter.remove();
                			}
                		}
                	}
                }
                if (compareExpressions.size() > 0) {
                  StringBuffer errorHelper = new StringBuffer();
                  for (AttributeExpression exp: compareExpressions){
                    errorHelper.append("\nAttributes "+exp.getAttributes()+" in "+ exp.getFullExpression()+" correspond to no state!");
                  }
                	throw new QueryParseException("One or more expressions are not valid "
                			+ errorHelper);
                }
                
                List<State> states = stmachine.getStates();
                for (State s : states) {
                	Transition ignore = s.getTransition(s.getId() + "_ignore");
                	if (ignore != null) {
                		switch (stmachine.getEventSelectionStrategy()) {
                		case SKIP_TILL_NEXT_MATCH:
                			ignore.getCondition().setEventTypeChecking(true);
                			Transition t = s.getTransition(s.getId() + "_take");
                			if (t == null) {
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
                			throw new QueryParseException(
                					"PARTITION_CONTIGUITY not implemented yet");
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
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "whereExpression"



    // $ANTLR start "compareExpression"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:485:1: compareExpression[StateMachine sm,List<AttributeExpression> compareExpressions] : ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] ) ;
    public final void compareExpression(StateMachine sm, List<AttributeExpression> compareExpressions) throws RecognitionException {
        CommonTree op=null;


        StringBuffer left = new StringBuffer();
        StringBuffer right = new StringBuffer();
        List<PathAttribute> attribs = new ArrayList<PathAttribute>();

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:491:3: ( ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:492:3: ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] )
            {
            match(input,COMPAREEXPRESSION,FOLLOW_COMPAREEXPRESSION_in_compareExpression591); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_mathExpression_in_compareExpression593);
            mathExpression(left, attribs);

            state._fsp--;


            op=(CommonTree)input.LT(1);

            if ( input.LA(1)==COMPAREOP||input.LA(1)==SINGLEEQUALS ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            pushFollow(FOLLOW_mathExpression_in_compareExpression633);
            mathExpression(right, attribs);

            state._fsp--;


            match(input, Token.UP, null); 



                StringBuffer ret = new StringBuffer(left);
                if (op.getToken().getType() == SINGLEEQUALS) {
                	ret.append("==");
                } else {
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
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "compareExpression"



    // $ANTLR start "assignment"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:513:1: assignment[StateMachine sm,List assignExpressions] : ^( ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs] ) ;
    public final void assignment(StateMachine sm, List assignExpressions) throws RecognitionException {

        List<PathAttribute> singleAttrib = new ArrayList<PathAttribute>();
        List<PathAttribute> attribs = new ArrayList<PathAttribute>();
        StringBuffer right = new StringBuffer();

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:519:3: ( ^( ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs] ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:520:3: ^( ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs] )
            {
            match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_assignment669); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_attributeTerm_in_assignment671);
            attributeTerm(singleAttrib);

            state._fsp--;


            pushFollow(FOLLOW_mathExpression_in_assignment674);
            mathExpression(right, attribs);

            state._fsp--;


            match(input, Token.UP, null); 



                assignExpressions.add(new AssignExpression(singleAttrib.get(0), attribs, right
                		.toString()));
               

            }

        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "assignment"



    // $ANTLR start "mathExpression"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:528:1: mathExpression[StringBuffer exp, List<PathAttribute> attribs] : ( ^( (op= PLUS |op= MINUS |op= MULT |op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] ) | attributeTerm[tmpAttrib] |num= NUMBER |lit= STRING_LITERAL );
    public final void mathExpression(StringBuffer exp, List<PathAttribute> attribs) throws RecognitionException {
        CommonTree op=null;
        CommonTree num=null;
        CommonTree lit=null;


        StringBuffer left = new StringBuffer();
        StringBuffer right = new StringBuffer();
        List<PathAttribute> tmpAttrib = new ArrayList<PathAttribute>();

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:534:3: ( ^( (op= PLUS |op= MINUS |op= MULT |op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] ) | attributeTerm[tmpAttrib] |num= NUMBER |lit= STRING_LITERAL )
            int alt12=4;
            switch ( input.LA(1) ) {
            case DIVISION:
            case MINUS:
            case MULT:
            case PLUS:
                {
                alt12=1;
                }
                break;
            case AGGREGATION:
            case KMEMBER:
            case MEMBER:
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:535:3: ^( (op= PLUS |op= MINUS |op= MULT |op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] )
                    {
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:536:5: (op= PLUS |op= MINUS |op= MULT |op= DIVISION )
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
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:537:7: op= PLUS
                            {
                            op=(CommonTree)match(input,PLUS,FOLLOW_PLUS_in_mathExpression721); 

                            }
                            break;
                        case 2 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:538:9: op= MINUS
                            {
                            op=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_mathExpression733); 

                            }
                            break;
                        case 3 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:539:9: op= MULT
                            {
                            op=(CommonTree)match(input,MULT,FOLLOW_MULT_in_mathExpression745); 

                            }
                            break;
                        case 4 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:540:9: op= DIVISION
                            {
                            op=(CommonTree)match(input,DIVISION,FOLLOW_DIVISION_in_mathExpression757); 

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_mathExpression_in_mathExpression769);
                    mathExpression(left, attribs);

                    state._fsp--;


                    pushFollow(FOLLOW_mathExpression_in_mathExpression772);
                    mathExpression(right, attribs);

                    state._fsp--;


                    match(input, Token.UP, null); 



                        exp.append("(").append(left).append(" ").append(op).append(right).append(")");
                       

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:548:5: attributeTerm[tmpAttrib]
                    {
                    pushFollow(FOLLOW_attributeTerm_in_mathExpression792);
                    attributeTerm(tmpAttrib);

                    state._fsp--;



                                                  // TODO: Es darf nur ein Attribut drin sein!
                                                  PathAttribute p = tmpAttrib.get(0);
                                                  attribs.add(p);
                                                  exp.append(p);
                                                 

                    }
                    break;
                case 3 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:555:5: num= NUMBER
                    {
                    num=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_mathExpression833); 


                                    exp.append(num.getText());
                                   

                    }
                    break;
                case 4 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:559:5: lit= STRING_LITERAL
                    {
                    lit=(CommonTree)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_mathExpression859); 


                                            exp.append(lit.getText());
                                           

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "mathExpression"



    // $ANTLR start "attributeTerm"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:565:1: attributeTerm[List<PathAttribute> attribs] : ( ^( KMEMBER kAttributeUsage[name,usage] member= NAME ) | ^( MEMBER attribName= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT | AVG ) var= NAME (member= NAME )? ) );
    public final void attributeTerm(List<PathAttribute> attribs) throws RecognitionException {
        CommonTree member=null;
        CommonTree attribName=null;
        CommonTree op=null;
        CommonTree var=null;


        StringBuffer usage = new StringBuffer();
        StringBuffer name = new StringBuffer();

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:570:3: ( ^( KMEMBER kAttributeUsage[name,usage] member= NAME ) | ^( MEMBER attribName= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT | AVG ) var= NAME (member= NAME )? ) )
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:571:3: ^( KMEMBER kAttributeUsage[name,usage] member= NAME )
                    {
                    match(input,KMEMBER,FOLLOW_KMEMBER_in_attributeTerm907); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_kAttributeUsage_in_attributeTerm909);
                    kAttributeUsage(name, usage);

                    state._fsp--;


                    member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm914); 

                    match(input, Token.UP, null); 



                        PathAttribute p = new PathAttribute(name.toString(), usage.toString(),
                        		member.getText(), Write.class.getSimpleName().toUpperCase());
                        attribs.add(p);
                        name = new StringBuffer();
                        usage = new StringBuffer();
                       

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:581:3: ^( MEMBER attribName= NAME member= NAME )
                    {
                    match(input,MEMBER,FOLLOW_MEMBER_in_attributeTerm932); 

                    match(input, Token.DOWN, null); 
                    attribName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm936); 

                    member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm940); 

                    match(input, Token.UP, null); 



                        PathAttribute p = new PathAttribute(attribName.getText(), null,
                        		member.getText(), Write.class.getSimpleName().toUpperCase());
                        attribs.add(p);
                       

                    }
                    break;
                case 3 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:589:3: ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT | AVG ) var= NAME (member= NAME )? )
                    {
                    match(input,AGGREGATION,FOLLOW_AGGREGATION_in_attributeTerm963); 

                    match(input, Token.DOWN, null); 
                    op=(CommonTree)input.LT(1);

                    if ( input.LA(1)==AVG||input.LA(1)==COUNT||input.LA(1)==MAX||input.LA(1)==MIN||input.LA(1)==SUM ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    var=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm1034); 

                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:599:5: (member= NAME )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==NAME) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:600:7: member= NAME
                            {
                            member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm1050); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 



                        PathAttribute p = new PathAttribute(var.getText(), "[i-1]", member == null ? ""
                        		: "" + member, symTableOpFac.getOperation(op.getText()).getName());
                        attribs.add(p);
                       

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "attributeTerm"



    // $ANTLR start "kAttributeUsage"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:611:1: kAttributeUsage[StringBuffer name, StringBuffer usage] : ( ^( NAME CURRENT ) | ^( NAME FIRST ) | ^( NAME PREV ) | ^( NAME LEN ) );
    public final void kAttributeUsage(StringBuffer name, StringBuffer usage) throws RecognitionException {
        CommonTree NAME1=null;
        CommonTree NAME2=null;
        CommonTree NAME3=null;
        CommonTree NAME4=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:612:3: ( ^( NAME CURRENT ) | ^( NAME FIRST ) | ^( NAME PREV ) | ^( NAME LEN ) )
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:613:3: ^( NAME CURRENT )
                    {
                    NAME1=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage1087); 

                    match(input, Token.DOWN, null); 
                    match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage1089); 

                    match(input, Token.UP, null); 



                        usage.append("[i]");
                        name.append(NAME1);
                       

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:620:3: ^( NAME FIRST )
                    {
                    NAME2=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage1107); 

                    match(input, Token.DOWN, null); 
                    match(input,FIRST,FOLLOW_FIRST_in_kAttributeUsage1109); 

                    match(input, Token.UP, null); 



                        usage.append("[1]");
                        name.append(NAME2);
                       

                    }
                    break;
                case 3 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:627:3: ^( NAME PREV )
                    {
                    NAME3=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage1127); 

                    match(input, Token.DOWN, null); 
                    match(input,PREV,FOLLOW_PREV_in_kAttributeUsage1129); 

                    match(input, Token.UP, null); 



                        usage.append("[i-1]");
                        name.append(NAME3);
                       

                    }
                    break;
                case 4 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:634:3: ^( NAME LEN )
                    {
                    NAME4=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage1147); 

                    match(input, Token.DOWN, null); 
                    match(input,LEN,FOLLOW_LEN_in_kAttributeUsage1149); 

                    match(input, Token.UP, null); 



                        usage.append("[i-1]");
                        name.append(NAME4);
                       

                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "kAttributeUsage"



    // $ANTLR start "idexpression"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:642:1: idexpression[List<State> states, List<AttributeExpression> compareExpressions] : ^( IDEXPRESSION var= NAME ) ;
    public final void idexpression(List<State> states, List<AttributeExpression> compareExpressions) throws RecognitionException {
        CommonTree var=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:643:3: ( ^( IDEXPRESSION var= NAME ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:644:3: ^( IDEXPRESSION var= NAME )
            {
            match(input,IDEXPRESSION,FOLLOW_IDEXPRESSION_in_idexpression1175); 

            match(input, Token.DOWN, null); 
            var=(CommonTree)match(input,NAME,FOLLOW_NAME_in_idexpression1179); 

            match(input, Token.UP, null); 



                String t = CepVariable.getSeperator();
                for (int i = 0; i < states.size() - 2; i++) {
                	List<PathAttribute> attr = new ArrayList<PathAttribute>();
                	PathAttribute a = new PathAttribute(states.get(i).getId(), var.getText());
                	attr.add(a);
                	PathAttribute b = new PathAttribute(states.get(i + 1).getId(),
                			var.getText());
                	attr.add(b);
                	StringBuffer expr = new StringBuffer();
                	expr.append(t).append(t).append(t).append(states.get(i).getId())
                			.append(".").append(var.getText());
                	expr.append(t).append(t).append(t).append(states.get(i + 1).getId())
                			.append(".").append(var.getText());
                	compareExpressions.add(new AttributeExpression(attr, expr.toString()));
                }
               

            }

        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "idexpression"



    // $ANTLR start "withinPart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:665:1: withinPart[CepAO cepAo] : ( ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? ) |);
    public final void withinPart(CepAO cepAo) throws RecognitionException {
        CommonTree value=null;
        CommonTree unit=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:666:3: ( ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? ) |)
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:667:3: ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? )
                    {
                    match(input,WITHIN,FOLLOW_WITHIN_in_withinPart1210); 

                    match(input, Token.DOWN, null); 
                    value=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart1214); 

                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:669:5: (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==DAY||LA16_0==HOUR||LA16_0==MILLISECOND||LA16_0==MINUTE||LA16_0==SECOND||LA16_0==WEEK) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:670:7: unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND )
                            {
                            unit=(CommonTree)input.LT(1);

                            if ( input.LA(1)==DAY||input.LA(1)==HOUR||input.LA(1)==MILLISECOND||input.LA(1)==MINUTE||input.LA(1)==SECOND||input.LA(1)==WEEK ) {
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



                        Long time = getTime(value.getText(), unit);
                        getLogger().debug("Setting Windowsize to " + time + " milliseconds");
                        cepAo.getStateMachine().setWindowSize(time);
                       

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:688:3: 
                    {
                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "withinPart"



    // $ANTLR start "returnPart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:690:1: returnPart[CepAO cepAo] : ( ^( RETURN ( attributeTerm[retAttr] )* (value= NAME )? ) |);
    public final void returnPart(CepAO cepAo) throws RecognitionException {
        CommonTree value=null;


        List<PathAttribute> retAttr = new ArrayList<PathAttribute>();

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:694:3: ( ^( RETURN ( attributeTerm[retAttr] )* (value= NAME )? ) |)
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==RETURN) ) {
                alt20=1;
            }
            else if ( (LA20_0==UP) ) {
                alt20=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;

            }
            switch (alt20) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:695:3: ^( RETURN ( attributeTerm[retAttr] )* (value= NAME )? )
                    {
                    match(input,RETURN,FOLLOW_RETURN_in_returnPart1361); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:695:12: ( attributeTerm[retAttr] )*
                        loop18:
                        do {
                            int alt18=2;
                            int LA18_0 = input.LA(1);

                            if ( (LA18_0==AGGREGATION||LA18_0==KMEMBER||LA18_0==MEMBER) ) {
                                alt18=1;
                            }


                            switch (alt18) {
                        	case 1 :
                        	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:695:12: attributeTerm[retAttr]
                        	    {
                        	    pushFollow(FOLLOW_attributeTerm_in_returnPart1363);
                        	    attributeTerm(retAttr);

                        	    state._fsp--;


                        	    }
                        	    break;

                        	default :
                        	    break loop18;
                            }
                        } while (true);


                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:695:41: (value= NAME )?
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( (LA19_0==NAME) ) {
                            alt19=1;
                        }
                        switch (alt19) {
                            case 1 :
                                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:695:41: value= NAME
                                {
                                value=(CommonTree)match(input,NAME,FOLLOW_NAME_in_returnPart1369); 

                                }
                                break;

                        }


                        match(input, Token.UP, null); 
                    }



                        RelationalMEPOutputSchemeEntry e = null;
                        OutputScheme scheme = new OutputScheme();
                        List<SDFAttribute> attrList = new ArrayList<SDFAttribute>();
                        
                        for (PathAttribute p : retAttr) {
                        	String op = p.getAggregation();
                        	String a = p.getStatename();
                        	String i = p.getKleenePart();
                        	if ("[i]".equals(i)) {
                        		i = "";
                        	} else if ("[i-1]".equals(i)) {
                        		i = "-1";
                        	}
                        	String path = p.getPath();
                        	e = new RelationalMEPOutputSchemeEntry(CepVariable.getStringFor(op, a, i, a
                        			+ "." + path));
                        	scheme.append(e);
                        	// TODO: Set correct Datatypes
                        	SDFAttribute attr = new SDFAttribute(null, e.getLabel(), SDFDatatype.STRING);
                        	attrList.add(attr);
                        }
                        SDFSchema outputSchema = new SDFSchema(
                        		value.getText() != null ? value.getText() : "", attrList);
                        ;
                        cepAo.getStateMachine().setOutputScheme(scheme);
                        cepAo.setOutputSchema(outputSchema);
                       

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:726:3: 
                    {
                    }
                    break;

            }
        }

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "returnPart"

    // Delegated rules


 

    public static final BitSet FOLLOW_CREATEVIEW_in_start83 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_start87 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_query_in_start91 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_query_in_start109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUERY_in_query150 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_patternPart_in_query152 = new BitSet(new long[]{0x1400080000000008L});
    public static final BitSet FOLLOW_wherePart_in_query155 = new BitSet(new long[]{0x1000080000000008L});
    public static final BitSet FOLLOW_withinPart_in_query158 = new BitSet(new long[]{0x0000080000000008L});
    public static final BitSet FOLLOW_returnPart_in_query161 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart188 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_seqPatternPart_in_patternPart190 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEQ_in_seqPatternPart214 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_state_in_seqPatternPart216 = new BitSet(new long[]{0x0000000000000008L,0x0000000000008080L});
    public static final BitSet FOLLOW_STATE_in_state244 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_state248 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_state252 = new BitSet(new long[]{0x0000001000000008L});
    public static final BitSet FOLLOW_NOTSIGN_in_state256 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_KSTATE_in_state275 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_state279 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_state283 = new BitSet(new long[]{0x0000001000000008L});
    public static final BitSet FOLLOW_NOTSIGN_in_state287 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHERE_in_wherePart314 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_whereStrat_in_wherePart318 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_whereExpression_in_wherePart323 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SKIP_TILL_NEXT_MATCH_in_whereStrat346 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat350 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SKIP_TILL_ANY_MATCH_in_whereStrat368 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat372 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STRICT_CONTIGUITY_in_whereStrat390 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat392 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARTITION_CONTIGUITY_in_whereStrat410 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat412 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARAMLIST_in_paramlist437 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeName_in_paramlist439 = new BitSet(new long[]{0x8000000000000008L,0x0000000000000020L});
    public static final BitSet FOLLOW_KATTRIBUTE_in_attributeName465 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeName467 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeName477 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeName479 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHEREEXPRESSION_in_whereExpression507 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_compareExpression_in_whereExpression521 = new BitSet(new long[]{0x4000000000000008L,0x0000000000000011L});
    public static final BitSet FOLLOW_idexpression_in_whereExpression532 = new BitSet(new long[]{0x4000000000000008L,0x0000000000000011L});
    public static final BitSet FOLLOW_assignment_in_whereExpression543 = new BitSet(new long[]{0x4000000000000008L,0x0000000000000011L});
    public static final BitSet FOLLOW_COMPAREEXPRESSION_in_compareExpression591 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mathExpression_in_compareExpression593 = new BitSet(new long[]{0x0000800000001000L});
    public static final BitSet FOLLOW_set_in_compareExpression603 = new BitSet(new long[]{0x2020012140020000L,0x0000000000000440L});
    public static final BitSet FOLLOW_mathExpression_in_compareExpression633 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_assignment669 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeTerm_in_assignment671 = new BitSet(new long[]{0x2020012140020000L,0x0000000000000440L});
    public static final BitSet FOLLOW_mathExpression_in_assignment674 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PLUS_in_mathExpression721 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MINUS_in_mathExpression733 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MULT_in_mathExpression745 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIVISION_in_mathExpression757 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mathExpression_in_mathExpression769 = new BitSet(new long[]{0x2020012140020000L,0x0000000000000440L});
    public static final BitSet FOLLOW_mathExpression_in_mathExpression772 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_attributeTerm_in_mathExpression792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_mathExpression833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_mathExpression859 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KMEMBER_in_attributeTerm907 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_kAttributeUsage_in_attributeTerm909 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm914 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MEMBER_in_attributeTerm932 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm936 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm940 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AGGREGATION_in_attributeTerm963 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_attributeTerm972 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1034 = new BitSet(new long[]{0x0000000200000008L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1050 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage1087 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage1089 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage1107 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_FIRST_in_kAttributeUsage1109 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage1127 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_PREV_in_kAttributeUsage1129 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage1147 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_LEN_in_kAttributeUsage1149 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDEXPRESSION_in_idexpression1175 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_idexpression1179 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart1210 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart1214 = new BitSet(new long[]{0x0200200090108008L});
    public static final BitSet FOLLOW_set_in_withinPart1237 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RETURN_in_returnPart1361 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart1363 = new BitSet(new long[]{0x2000000200000008L,0x0000000000000440L});
    public static final BitSet FOLLOW_NAME_in_returnPart1369 = new BitSet(new long[]{0x0000000000000008L});

}