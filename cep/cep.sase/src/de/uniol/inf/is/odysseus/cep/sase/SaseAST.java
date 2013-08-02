// $ANTLR 3.4 E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g 2013-08-01 10:47:08

/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
import de.uniol.inf.is.odysseus.cep.PatternDetectAO;
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
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateViewCommand;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;	
		
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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALT", "AND", "AS", "ASSIGN", "AT", "AVG", "BBRACKETLEFT", "BBRACKETRIGHT", "BOOLEAN", "COMMA", "COMPAREOP", "COUNT", "CREATE", "DAY", "DIGIT", "DIVISION", "ENDS", "EQUALS", "FALSE", "FLOAT", "HOUR", "INTEGER", "LBRACKET", "LEFTCURLY", "LEN", "LETTER", "LOWER", "MAX", "MILLISECOND", "MIN", "MINUS", "MINUTE", "MULT", "NAME", "NEWLINE", "NONCONTROL_CHAR", "NOTSIGN", "NUMBER", "PARTITION_CONTIGUITY", "PATTERN", "PLUS", "POINT", "RBRACKET", "RETURN", "RIGHTCURLY", "SECOND", "SEQ", "SINGLEEQUALS", "SKIP_TILL_ANY_MATCH", "SKIP_TILL_NEXT_MATCH", "SPACE", "STREAM", "STRICT_CONTIGUITY", "STRING_LITERAL", "SUM", "TRUE", "UPPER", "VIEW", "WEEK", "WHERE", "WHITESPACE", "WITHIN", "AGGREGATION", "ASSIGNMENT", "ATTRIBUTE", "COMPAREEXPRESSION", "CREATEVIEW", "CURRENT", "ENDSAT", "FIRST", "IDEXPRESSION", "KATTRIBUTE", "KMEMBER", "KSTATE", "KTYPE", "MATHEXPRESSION", "MEMBER", "NOT", "PARAMLIST", "PREV", "QUERY", "STATE", "TERM", "TYPE", "WHEREEXPRESSION"
    };

    public static final int EOF=-1;
    public static final int ALT=4;
    public static final int AND=5;
    public static final int AS=6;
    public static final int ASSIGN=7;
    public static final int AT=8;
    public static final int AVG=9;
    public static final int BBRACKETLEFT=10;
    public static final int BBRACKETRIGHT=11;
    public static final int BOOLEAN=12;
    public static final int COMMA=13;
    public static final int COMPAREOP=14;
    public static final int COUNT=15;
    public static final int CREATE=16;
    public static final int DAY=17;
    public static final int DIGIT=18;
    public static final int DIVISION=19;
    public static final int ENDS=20;
    public static final int EQUALS=21;
    public static final int FALSE=22;
    public static final int FLOAT=23;
    public static final int HOUR=24;
    public static final int INTEGER=25;
    public static final int LBRACKET=26;
    public static final int LEFTCURLY=27;
    public static final int LEN=28;
    public static final int LETTER=29;
    public static final int LOWER=30;
    public static final int MAX=31;
    public static final int MILLISECOND=32;
    public static final int MIN=33;
    public static final int MINUS=34;
    public static final int MINUTE=35;
    public static final int MULT=36;
    public static final int NAME=37;
    public static final int NEWLINE=38;
    public static final int NONCONTROL_CHAR=39;
    public static final int NOTSIGN=40;
    public static final int NUMBER=41;
    public static final int PARTITION_CONTIGUITY=42;
    public static final int PATTERN=43;
    public static final int PLUS=44;
    public static final int POINT=45;
    public static final int RBRACKET=46;
    public static final int RETURN=47;
    public static final int RIGHTCURLY=48;
    public static final int SECOND=49;
    public static final int SEQ=50;
    public static final int SINGLEEQUALS=51;
    public static final int SKIP_TILL_ANY_MATCH=52;
    public static final int SKIP_TILL_NEXT_MATCH=53;
    public static final int SPACE=54;
    public static final int STREAM=55;
    public static final int STRICT_CONTIGUITY=56;
    public static final int STRING_LITERAL=57;
    public static final int SUM=58;
    public static final int TRUE=59;
    public static final int UPPER=60;
    public static final int VIEW=61;
    public static final int WEEK=62;
    public static final int WHERE=63;
    public static final int WHITESPACE=64;
    public static final int WITHIN=65;
    public static final int AGGREGATION=66;
    public static final int ASSIGNMENT=67;
    public static final int ATTRIBUTE=68;
    public static final int COMPAREEXPRESSION=69;
    public static final int CREATEVIEW=70;
    public static final int CURRENT=71;
    public static final int ENDSAT=72;
    public static final int FIRST=73;
    public static final int IDEXPRESSION=74;
    public static final int KATTRIBUTE=75;
    public static final int KMEMBER=76;
    public static final int KSTATE=77;
    public static final int KTYPE=78;
    public static final int MATHEXPRESSION=79;
    public static final int MEMBER=80;
    public static final int NOT=81;
    public static final int PARAMLIST=82;
    public static final int PREV=83;
    public static final int QUERY=84;
    public static final int STATE=85;
    public static final int TERM=86;
    public static final int TYPE=87;
    public static final int WHEREEXPRESSION=88;

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
    public String getGrammarFileName() { return "E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g"; }


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

    	// UNterschiedliche F�lle
    	// 1. Das Attribut geh�rt zum aktuellen Zustand und ist kein
    	// KleeneAttribut --> aktuell
    	// 2. Das Attribut geh�rt zum aktuellen Zustand und ist ein
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

    private String transformToString(PathAttribute p) {
    	String op = p.getAggregation();
    	String a = p.getStatename();
    	String i = p.getKleenePart();
    	if ("[i]".equals(i)) {
    		i = "";
    	} else if ("[i-1]".equals(i)) {
    		i = "-1";
    	}
    	String path = p.getPath();
    	return CepVariable.getStringFor(op, a, i, a + "." + path);
    }



    // $ANTLR start "start"
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:205:1: start[boolean attachSources, ILogicalQuery lq] returns [IExecutorCommand op] : ( ^( CREATEVIEW n= NAME q= query ) |o= query );
    public final IExecutorCommand start(boolean attachSources, ILogicalQuery lq) throws RecognitionException {
        IExecutorCommand op = null;


        CommonTree n=null;
        ILogicalOperator q =null;

        ILogicalOperator o =null;



        this.attachSources = attachSources;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:209:3: ( ^( CREATEVIEW n= NAME q= query ) |o= query )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:210:3: ^( CREATEVIEW n= NAME q= query )
                    {
                    match(input,CREATEVIEW,FOLLOW_CREATEVIEW_in_start83); 

                    match(input, Token.DOWN, null); 
                    n=(CommonTree)match(input,NAME,FOLLOW_NAME_in_start87); 

                    pushFollow(FOLLOW_query_in_start91);
                    q=query();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        CreateViewCommand cmd = new CreateViewCommand(n.getText(), q, user);
                        op = cmd;
                       

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:216:5: o= query
                    {
                    pushFollow(FOLLOW_query_in_start109);
                    o=query();

                    state._fsp--;



                                lq.setLogicalPlan(o, true);
                                CreateQueryCommand cmd = new CreateQueryCommand(lq, user);
                                 op = cmd;
                                

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:224:1: query returns [ILogicalOperator op] : ^( QUERY patternPart[patternDetectAO, sourceNames] wherePart[patternDetectAO] withinPart[patternDetectAO] endsAtPart[patternDetectAO] returnPart[patternDetectAO] ) ;
    public final ILogicalOperator query() throws RecognitionException {
        ILogicalOperator op = null;



        PatternDetectAO patternDetectAO = new PatternDetectAO();
        simpleState = new ArrayList<String>();
        kleeneState = new ArrayList<String>();
        simpleAttributeState = new HashMap<String, String>();
        kleeneAttributeState = new HashMap<String, String>();
        List<String> sourceNames = new ArrayList<String>();

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:233:3: ( ^( QUERY patternPart[patternDetectAO, sourceNames] wherePart[patternDetectAO] withinPart[patternDetectAO] endsAtPart[patternDetectAO] returnPart[patternDetectAO] ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:234:3: ^( QUERY patternPart[patternDetectAO, sourceNames] wherePart[patternDetectAO] withinPart[patternDetectAO] endsAtPart[patternDetectAO] returnPart[patternDetectAO] )
            {
            match(input,QUERY,FOLLOW_QUERY_in_query150); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_patternPart_in_query152);
            patternPart(patternDetectAO, sourceNames);

            state._fsp--;


            pushFollow(FOLLOW_wherePart_in_query155);
            wherePart(patternDetectAO);

            state._fsp--;


            pushFollow(FOLLOW_withinPart_in_query158);
            withinPart(patternDetectAO);

            state._fsp--;


            pushFollow(FOLLOW_endsAtPart_in_query161);
            endsAtPart(patternDetectAO);

            state._fsp--;


            pushFollow(FOLLOW_returnPart_in_query164);
            returnPart(patternDetectAO);

            state._fsp--;


            match(input, Token.UP, null); 



                // Initialize Schema
                patternDetectAO.getStateMachine().getSymTabScheme(true);
                getLogger().debug("Created State Machine " + patternDetectAO.getStateMachine());
                if (attachSources) {
                	// Set Inputs for patternDetectAO;
                	int port = 0;
                	for (String sn : sourceNames) {
                		getLogger().debug("Bind " + sn + " to Port " + port);
                		try {
                			ILogicalOperator ao = dd.getViewOrStream(sn, user);
                			patternDetectAO
                					.subscribeToSource(ao, port, 0, ao.getOutputSchema());
                			patternDetectAO.setInputTypeName(port, sn);
                			port++;
                		} catch (Exception e) {
                			throw new QueryParseException("Source/View " + sn + " not found");
                		}
                	}
                }
                patternDetectAO.prepareNegation();
                op = patternDetectAO;
               

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:261:1: patternPart[PatternDetectAO patternDetectAO, List<String> sourceNames] : ^( PATTERN seqPatternPart[patternDetectAO, sourceNames] ) ;
    public final void patternPart(PatternDetectAO patternDetectAO, List<String> sourceNames) throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:262:3: ( ^( PATTERN seqPatternPart[patternDetectAO, sourceNames] ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:263:3: ^( PATTERN seqPatternPart[patternDetectAO, sourceNames] )
            {
            match(input,PATTERN,FOLLOW_PATTERN_in_patternPart191); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_seqPatternPart_in_patternPart193);
            seqPatternPart(patternDetectAO, sourceNames);

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:266:1: seqPatternPart[PatternDetectAO patternDetectAO, List<String> sourceNames] : ^( SEQ ( state[states, sourceNames] )* ) ;
    public final void seqPatternPart(PatternDetectAO patternDetectAO, List<String> sourceNames) throws RecognitionException {

        List<State> states = new LinkedList<State>();

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:270:3: ( ^( SEQ ( state[states, sourceNames] )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:271:3: ^( SEQ ( state[states, sourceNames] )* )
            {
            match(input,SEQ,FOLLOW_SEQ_in_seqPatternPart217); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:271:9: ( state[states, sourceNames] )*
                loop2:
                do {
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==KSTATE||LA2_0==STATE) ) {
                        alt2=1;
                    }


                    switch (alt2) {
                	case 1 :
                	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:271:9: state[states, sourceNames]
                	    {
                	    pushFollow(FOLLOW_state_in_seqPatternPart219);
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
                	StateMachine sm = patternDetectAO.getStateMachine();
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
                			// TODO: Ist das mit dem EAction.discard/consumeBufferWrite immer richtig?
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:317:1: state[List<State> states, List<String> sourceNames] : ( ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? ) | ^( KSTATE statename= NAME plusSign= PLUS attrName= NAME bl= BBRACKETLEFT br= BBRACKETRIGHT (not= NOTSIGN )? ) );
    public final void state(List<State> states, List<String> sourceNames) throws RecognitionException {
        CommonTree statename=null;
        CommonTree attrName=null;
        CommonTree not=null;
        CommonTree plusSign=null;
        CommonTree bl=null;
        CommonTree br=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:318:3: ( ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? ) | ^( KSTATE statename= NAME plusSign= PLUS attrName= NAME bl= BBRACKETLEFT br= BBRACKETRIGHT (not= NOTSIGN )? ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:319:3: ^( STATE statename= NAME attrName= NAME (not= NOTSIGN )? )
                    {
                    match(input,STATE,FOLLOW_STATE_in_state247); 

                    match(input, Token.DOWN, null); 
                    statename=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state251); 

                    attrName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state255); 

                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:319:43: (not= NOTSIGN )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==NOTSIGN) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:319:43: not= NOTSIGN
                            {
                            not=(CommonTree)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_state259); 

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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:340:3: ^( KSTATE statename= NAME plusSign= PLUS attrName= NAME bl= BBRACKETLEFT br= BBRACKETRIGHT (not= NOTSIGN )? )
                    {
                    match(input,KSTATE,FOLLOW_KSTATE_in_state278); 

                    match(input, Token.DOWN, null); 
                    statename=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state282); 

                    plusSign=(CommonTree)match(input,PLUS,FOLLOW_PLUS_in_state286); 

                    attrName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_state290); 

                    bl=(CommonTree)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_state294); 

                    br=(CommonTree)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_state298); 

                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:340:91: (not= NOTSIGN )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==NOTSIGN) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:340:91: not= NOTSIGN
                            {
                            not=(CommonTree)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_state302); 

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:360:1: wherePart[PatternDetectAO patternDetectAO] : ( ^( WHERE str= whereStrat[patternDetectAO.getStateMachine()] we= whereExpression[patternDetectAO.getStateMachine()] ) |);
    public final void wherePart(PatternDetectAO patternDetectAO) throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:361:3: ( ^( WHERE str= whereStrat[patternDetectAO.getStateMachine()] we= whereExpression[patternDetectAO.getStateMachine()] ) |)
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WHERE) ) {
                alt6=1;
            }
            else if ( (LA6_0==UP||LA6_0==RETURN||LA6_0==WITHIN||LA6_0==ENDSAT) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:362:3: ^( WHERE str= whereStrat[patternDetectAO.getStateMachine()] we= whereExpression[patternDetectAO.getStateMachine()] )
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_wherePart329); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_whereStrat_in_wherePart333);
                    whereStrat(patternDetectAO.getStateMachine());

                    state._fsp--;


                    pushFollow(FOLLOW_whereExpression_in_wherePart338);
                    whereExpression(patternDetectAO.getStateMachine());

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:364:3: 
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:366:1: whereStrat[StateMachine sm] : ( ^( SKIP_TILL_NEXT_MATCH param= paramlist ) | ^( SKIP_TILL_ANY_MATCH param= paramlist ) | ^( STRICT_CONTIGUITY paramlist ) | ^( PARTITION_CONTIGUITY paramlist ) );
    public final void whereStrat(StateMachine sm) throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:367:3: ( ^( SKIP_TILL_NEXT_MATCH param= paramlist ) | ^( SKIP_TILL_ANY_MATCH param= paramlist ) | ^( STRICT_CONTIGUITY paramlist ) | ^( PARTITION_CONTIGUITY paramlist ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:368:3: ^( SKIP_TILL_NEXT_MATCH param= paramlist )
                    {
                    match(input,SKIP_TILL_NEXT_MATCH,FOLLOW_SKIP_TILL_NEXT_MATCH_in_whereStrat361); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat365);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH);
                       

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:374:3: ^( SKIP_TILL_ANY_MATCH param= paramlist )
                    {
                    match(input,SKIP_TILL_ANY_MATCH,FOLLOW_SKIP_TILL_ANY_MATCH_in_whereStrat383); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat387);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_ANY_MATCH);
                       

                    }
                    break;
                case 3 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:380:3: ^( STRICT_CONTIGUITY paramlist )
                    {
                    match(input,STRICT_CONTIGUITY,FOLLOW_STRICT_CONTIGUITY_in_whereStrat405); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat407);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        sm.setEventSelectionStrategy(EEventSelectionStrategy.STRICT_CONTIGUITY);
                       

                    }
                    break;
                case 4 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:386:3: ^( PARTITION_CONTIGUITY paramlist )
                    {
                    match(input,PARTITION_CONTIGUITY,FOLLOW_PARTITION_CONTIGUITY_in_whereStrat425); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_paramlist_in_whereStrat427);
                    paramlist();

                    state._fsp--;


                    match(input, Token.UP, null); 



                        sm.setEventSelectionStrategy(EEventSelectionStrategy.PARTITION_CONTIGUITY);
                       

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:393:1: paramlist : ^( PARAMLIST ( attributeName )* ) ;
    public final void paramlist() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:394:3: ( ^( PARAMLIST ( attributeName )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:395:3: ^( PARAMLIST ( attributeName )* )
            {
            match(input,PARAMLIST,FOLLOW_PARAMLIST_in_paramlist452); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:395:15: ( attributeName )*
                loop8:
                do {
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==ATTRIBUTE||LA8_0==KATTRIBUTE) ) {
                        alt8=1;
                    }


                    switch (alt8) {
                	case 1 :
                	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:395:15: attributeName
                	    {
                	    pushFollow(FOLLOW_attributeName_in_paramlist454);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:403:1: attributeName : ( ^( KATTRIBUTE NAME ) | ^( ATTRIBUTE NAME ) );
    public final void attributeName() throws RecognitionException {
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:404:3: ( ^( KATTRIBUTE NAME ) | ^( ATTRIBUTE NAME ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:405:3: ^( KATTRIBUTE NAME )
                    {
                    match(input,KATTRIBUTE,FOLLOW_KATTRIBUTE_in_attributeName480); 

                    match(input, Token.DOWN, null); 
                    match(input,NAME,FOLLOW_NAME_in_attributeName482); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:407:3: ^( ATTRIBUTE NAME )
                    {
                    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_attributeName492); 

                    match(input, Token.DOWN, null); 
                    match(input,NAME,FOLLOW_NAME_in_attributeName494); 

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:410:1: whereExpression[StateMachine stmachine] : ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),idExpressions,compareExpressions] | assignment[stmachine, assignExpressions] )* ) ;
    public final void whereExpression(StateMachine stmachine) throws RecognitionException {

        List<AttributeExpression> compareExpressions = new ArrayList<AttributeExpression>();
        List<AttributeExpression> idExpressions = new ArrayList<AttributeExpression>();
        List assignExpressions = new ArrayList();

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:416:3: ( ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),idExpressions,compareExpressions] | assignment[stmachine, assignExpressions] )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:417:3: ^( WHEREEXPRESSION ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),idExpressions,compareExpressions] | assignment[stmachine, assignExpressions] )* )
            {
            match(input,WHEREEXPRESSION,FOLLOW_WHEREEXPRESSION_in_whereExpression522); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:419:5: ( compareExpression[stmachine,compareExpressions] | idexpression[stmachine.getStates(),idExpressions,compareExpressions] | assignment[stmachine, assignExpressions] )*
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
                	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:420:7: compareExpression[stmachine,compareExpressions]
                	    {
                	    pushFollow(FOLLOW_compareExpression_in_whereExpression536);
                	    compareExpression(stmachine, compareExpressions);

                	    state._fsp--;


                	    }
                	    break;
                	case 2 :
                	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:421:9: idexpression[stmachine.getStates(),idExpressions,compareExpressions]
                	    {
                	    pushFollow(FOLLOW_idexpression_in_whereExpression547);
                	    idexpression(stmachine.getStates(), idExpressions, compareExpressions);

                	    state._fsp--;


                	    }
                	    break;
                	case 3 :
                	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:422:9: assignment[stmachine, assignExpressions]
                	    {
                	    pushFollow(FOLLOW_assignment_in_whereExpression558);
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
                				// Anpassen der Variablennamen f�r das Metamodell:
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
                				// Anpassen der Variablennamen f�r das Metamodell:
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
                	for (AttributeExpression exp : compareExpressions) {
                		errorHelper.append("\nAttributes " + exp.getAttributes() + " in "
                				+ exp.getFullExpression() + " correspond to no state!");
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
                		  // set to false, because the whole expression will be negated (so this
                		  // will be true again
                      ignore.getCondition().setEventTypeChecking(false);
                      // Remark: idExpression are automatically generated in state increasing order
                      if (idExpressions.size() > 0){
                        AttributeExpression ce = idExpressions.remove(0);
                        PathAttribute attr = ce.get(s.getId());
                        if (attr != null) {
                        // Anpassen der Variablennamen f�r das Metamodell:
                        String fullExpression = tranformAttributesToMetamodell(ce, s);
                        ignore.appendAND(fullExpression);
                        ignore.negateExpression();
                      }else{
                        throw new QueryParseException(
                                      "PARTITION_CONTIGUITY error: attribute "+attr+" not found!"); 
                      }
                				}
                			break;
                		case SKIP_TILL_ANY_MATCH:
                			ignore.getCondition().setEventTypeChecking(false);
                			break;
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:519:1: compareExpression[StateMachine sm,List<AttributeExpression> compareExpressions] : ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] ) ;
    public final void compareExpression(StateMachine sm, List<AttributeExpression> compareExpressions) throws RecognitionException {
        CommonTree op=null;


        StringBuffer left = new StringBuffer();
        StringBuffer right = new StringBuffer();
        List<PathAttribute> attribs = new ArrayList<PathAttribute>();

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:525:3: ( ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:526:3: ^( COMPAREEXPRESSION mathExpression[left, attribs] op= ( COMPAREOP | SINGLEEQUALS ) mathExpression[right, attribs] )
            {
            match(input,COMPAREEXPRESSION,FOLLOW_COMPAREEXPRESSION_in_compareExpression606); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_mathExpression_in_compareExpression608);
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


            pushFollow(FOLLOW_mathExpression_in_compareExpression648);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:547:1: assignment[StateMachine sm,List assignExpressions] : ^( ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs] ) ;
    public final void assignment(StateMachine sm, List assignExpressions) throws RecognitionException {

        List<PathAttribute> singleAttrib = new ArrayList<PathAttribute>();
        List<PathAttribute> attribs = new ArrayList<PathAttribute>();
        StringBuffer right = new StringBuffer();

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:553:3: ( ^( ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs] ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:554:3: ^( ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs] )
            {
            match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_assignment684); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_attributeTerm_in_assignment686);
            attributeTerm(singleAttrib);

            state._fsp--;


            pushFollow(FOLLOW_mathExpression_in_assignment689);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:562:1: mathExpression[StringBuffer exp, List<PathAttribute> attribs] : ( ^( (op= PLUS |op= MINUS |op= MULT |op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] ) | attributeTerm[tmpAttrib] |num= NUMBER |bool= BOOLEAN |lit= STRING_LITERAL );
    public final void mathExpression(StringBuffer exp, List<PathAttribute> attribs) throws RecognitionException {
        CommonTree op=null;
        CommonTree num=null;
        CommonTree bool=null;
        CommonTree lit=null;


        StringBuffer left = new StringBuffer();
        StringBuffer right = new StringBuffer();
        List<PathAttribute> tmpAttrib = new ArrayList<PathAttribute>();

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:568:3: ( ^( (op= PLUS |op= MINUS |op= MULT |op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] ) | attributeTerm[tmpAttrib] |num= NUMBER |bool= BOOLEAN |lit= STRING_LITERAL )
            int alt12=5;
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
            case BOOLEAN:
                {
                alt12=4;
                }
                break;
            case STRING_LITERAL:
                {
                alt12=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }

            switch (alt12) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:569:3: ^( (op= PLUS |op= MINUS |op= MULT |op= DIVISION ) mathExpression[left, attribs] mathExpression[right, attribs] )
                    {
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:570:5: (op= PLUS |op= MINUS |op= MULT |op= DIVISION )
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
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:571:7: op= PLUS
                            {
                            op=(CommonTree)match(input,PLUS,FOLLOW_PLUS_in_mathExpression736); 

                            }
                            break;
                        case 2 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:572:9: op= MINUS
                            {
                            op=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_mathExpression748); 

                            }
                            break;
                        case 3 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:573:9: op= MULT
                            {
                            op=(CommonTree)match(input,MULT,FOLLOW_MULT_in_mathExpression760); 

                            }
                            break;
                        case 4 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:574:9: op= DIVISION
                            {
                            op=(CommonTree)match(input,DIVISION,FOLLOW_DIVISION_in_mathExpression772); 

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_mathExpression_in_mathExpression784);
                    mathExpression(left, attribs);

                    state._fsp--;


                    pushFollow(FOLLOW_mathExpression_in_mathExpression787);
                    mathExpression(right, attribs);

                    state._fsp--;


                    match(input, Token.UP, null); 



                        exp.append("(").append(left).append(" ").append(op).append(right).append(")");
                       

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:582:5: attributeTerm[tmpAttrib]
                    {
                    pushFollow(FOLLOW_attributeTerm_in_mathExpression807);
                    attributeTerm(tmpAttrib);

                    state._fsp--;



                                                  // TODO: Es darf nur ein Attribut drin sein!
                                                  PathAttribute p = tmpAttrib.get(0);
                                                  attribs.add(p);
                                                  exp.append(p);
                                                 

                    }
                    break;
                case 3 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:589:5: num= NUMBER
                    {
                    num=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_mathExpression848); 


                                    exp.append(num.getText());
                                   

                    }
                    break;
                case 4 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:593:5: bool= BOOLEAN
                    {
                    bool=(CommonTree)match(input,BOOLEAN,FOLLOW_BOOLEAN_in_mathExpression874); 


                                      exp.append(bool.getText());
                                     

                    }
                    break;
                case 5 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:597:5: lit= STRING_LITERAL
                    {
                    lit=(CommonTree)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_mathExpression902); 


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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:603:1: attributeTerm[List<PathAttribute> attribs] : ( ^( KMEMBER kAttributeUsage[name,usage] member= NAME ) | ^( MEMBER attribName= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT | AVG ) var= NAME (member= NAME )? ) );
    public final void attributeTerm(List<PathAttribute> attribs) throws RecognitionException {
        CommonTree member=null;
        CommonTree attribName=null;
        CommonTree op=null;
        CommonTree var=null;


        StringBuffer usage = new StringBuffer();
        StringBuffer name = new StringBuffer();

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:608:3: ( ^( KMEMBER kAttributeUsage[name,usage] member= NAME ) | ^( MEMBER attribName= NAME member= NAME ) | ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT | AVG ) var= NAME (member= NAME )? ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:609:3: ^( KMEMBER kAttributeUsage[name,usage] member= NAME )
                    {
                    match(input,KMEMBER,FOLLOW_KMEMBER_in_attributeTerm950); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_kAttributeUsage_in_attributeTerm952);
                    kAttributeUsage(name, usage);

                    state._fsp--;


                    member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm957); 

                    match(input, Token.UP, null); 



                        if (kleeneAttributeState.get(name.toString()) != null) {
                        	PathAttribute p = new PathAttribute(name.toString(), usage.toString(),
                        			member.getText(), Write.class.getSimpleName().toUpperCase());
                        	attribs.add(p);
                        	name = new StringBuffer();
                        	usage = new StringBuffer();
                        } else {
                        	throw new QueryParseException("Attribute " + name.toString()
                        			+ " does not exist!");
                        }
                       

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:624:3: ^( MEMBER attribName= NAME member= NAME )
                    {
                    match(input,MEMBER,FOLLOW_MEMBER_in_attributeTerm975); 

                    match(input, Token.DOWN, null); 
                    attribName=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm979); 

                    member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm983); 

                    match(input, Token.UP, null); 



                        if (simpleAttributeState.get(attribName.getText()) != null) {
                        	PathAttribute p = new PathAttribute(attribName.getText(), null,
                        			member.getText(), Write.class.getSimpleName().toUpperCase());
                        	attribs.add(p);
                        } else {
                        	throw new QueryParseException("Attribute " + attribName.getText()
                        			+ " does not exist!");
                        }
                       

                    }
                    break;
                case 3 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:637:3: ^( AGGREGATION op= ( MIN | MAX | SUM | COUNT | AVG ) var= NAME (member= NAME )? )
                    {
                    match(input,AGGREGATION,FOLLOW_AGGREGATION_in_attributeTerm1006); 

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


                    var=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm1077); 

                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:647:5: (member= NAME )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==NAME) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:648:7: member= NAME
                            {
                            member=(CommonTree)match(input,NAME,FOLLOW_NAME_in_attributeTerm1093); 

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:659:1: kAttributeUsage[StringBuffer name, StringBuffer usage] : ( ^( NAME CURRENT ) | ^( NAME FIRST ) | ^( NAME PREV ) | ^( NAME LEN ) );
    public final void kAttributeUsage(StringBuffer name, StringBuffer usage) throws RecognitionException {
        CommonTree NAME1=null;
        CommonTree NAME2=null;
        CommonTree NAME3=null;
        CommonTree NAME4=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:660:3: ( ^( NAME CURRENT ) | ^( NAME FIRST ) | ^( NAME PREV ) | ^( NAME LEN ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:661:3: ^( NAME CURRENT )
                    {
                    NAME1=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage1130); 

                    match(input, Token.DOWN, null); 
                    match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage1132); 

                    match(input, Token.UP, null); 



                        usage.append("[i]");
                        name.append(NAME1);
                       

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:668:3: ^( NAME FIRST )
                    {
                    NAME2=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage1150); 

                    match(input, Token.DOWN, null); 
                    match(input,FIRST,FOLLOW_FIRST_in_kAttributeUsage1152); 

                    match(input, Token.UP, null); 



                        usage.append("[1]");
                        name.append(NAME2);
                       

                    }
                    break;
                case 3 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:675:3: ^( NAME PREV )
                    {
                    NAME3=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage1170); 

                    match(input, Token.DOWN, null); 
                    match(input,PREV,FOLLOW_PREV_in_kAttributeUsage1172); 

                    match(input, Token.UP, null); 



                        usage.append("[i-1]");
                        name.append(NAME3);
                       

                    }
                    break;
                case 4 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:682:3: ^( NAME LEN )
                    {
                    NAME4=(CommonTree)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage1190); 

                    match(input, Token.DOWN, null); 
                    match(input,LEN,FOLLOW_LEN_in_kAttributeUsage1192); 

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:690:1: idexpression[List<State> states, List<AttributeExpression> idExpressions, List<AttributeExpression> compareExpressions] : ^( IDEXPRESSION var= NAME ) ;
    public final void idexpression(List<State> states, List<AttributeExpression> idExpressions, List<AttributeExpression> compareExpressions) throws RecognitionException {
        CommonTree var=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:691:3: ( ^( IDEXPRESSION var= NAME ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:692:3: ^( IDEXPRESSION var= NAME )
            {
            match(input,IDEXPRESSION,FOLLOW_IDEXPRESSION_in_idexpression1218); 

            match(input, Token.DOWN, null); 
            var=(CommonTree)match(input,NAME,FOLLOW_NAME_in_idexpression1222); 

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
            //    	expr.append(t).append(t).append(t).append(states.get(i).getId())
            //    			.append(".").append(var.getText());
                  expr.append(a.getFullAttributeName());
                	expr.append(" == ");
            //    	expr.append(t).append(t).append(t).append(states.get(i + 1).getId())
            //    			.append(".").append(var.getText());
                  expr.append(b.getFullAttributeName());
                	AttributeExpression toAdd =	new AttributeExpression(attr, expr.toString());
                	// Special handling for partition contiguity necessary
                	idExpressions.add(toAdd);
                	compareExpressions.add(toAdd);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:719:1: withinPart[PatternDetectAO patternDetectAO] : ( ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? ) |);
    public final void withinPart(PatternDetectAO patternDetectAO) throws RecognitionException {
        CommonTree value=null;
        CommonTree unit=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:720:3: ( ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? ) |)
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==WITHIN) ) {
                alt17=1;
            }
            else if ( (LA17_0==UP||LA17_0==RETURN||LA17_0==ENDSAT) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }
            switch (alt17) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:721:3: ^( WITHIN value= NUMBER (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )? )
                    {
                    match(input,WITHIN,FOLLOW_WITHIN_in_withinPart1253); 

                    match(input, Token.DOWN, null); 
                    value=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart1257); 

                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:723:5: (unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND ) )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==DAY||LA16_0==HOUR||LA16_0==MILLISECOND||LA16_0==MINUTE||LA16_0==SECOND||LA16_0==WEEK) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:724:7: unit= ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND )
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
                        patternDetectAO.getStateMachine().setWindowSize(time);
                       

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:742:3: 
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



    // $ANTLR start "endsAtPart"
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:744:1: endsAtPart[PatternDetectAO patternDetectAO] : ( ^( ENDSAT attributeTerm[retAttr] ) |);
    public final void endsAtPart(PatternDetectAO patternDetectAO) throws RecognitionException {

        List<PathAttribute> retAttr = new ArrayList<PathAttribute>();

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:748:3: ( ^( ENDSAT attributeTerm[retAttr] ) |)
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==ENDSAT) ) {
                alt18=1;
            }
            else if ( (LA18_0==UP||LA18_0==RETURN) ) {
                alt18=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }
            switch (alt18) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:749:3: ^( ENDSAT attributeTerm[retAttr] )
                    {
                    match(input,ENDSAT,FOLLOW_ENDSAT_in_endsAtPart1404); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_attributeTerm_in_endsAtPart1406);
                    attributeTerm(retAttr);

                    state._fsp--;


                    match(input, Token.UP, null); 



                        CepVariable var = new CepVariable(transformToString(retAttr.get(0)));
                        patternDetectAO.getStateMachine().setEndsAtVar(var);
                        RelationalMEPCondition endsAtCondition = new RelationalMEPCondition(
                        		StateMachine.current.getVariableName() + " > "
                        				+ StateMachine.maxTime.getVariableName());
                        patternDetectAO.getStateMachine().setEndsAtCondition(endsAtCondition);
                       

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:760:3: 
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
    // $ANTLR end "endsAtPart"



    // $ANTLR start "returnPart"
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:762:1: returnPart[PatternDetectAO patternDetectAO] : ( ^( RETURN ( attributeTerm[retAttr] )* (value= NAME )? ) |);
    public final void returnPart(PatternDetectAO patternDetectAO) throws RecognitionException {
        CommonTree value=null;


        List<PathAttribute> retAttr = new ArrayList<PathAttribute>();

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:766:3: ( ^( RETURN ( attributeTerm[retAttr] )* (value= NAME )? ) |)
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==RETURN) ) {
                alt21=1;
            }
            else if ( (LA21_0==UP) ) {
                alt21=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }
            switch (alt21) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:767:3: ^( RETURN ( attributeTerm[retAttr] )* (value= NAME )? )
                    {
                    match(input,RETURN,FOLLOW_RETURN_in_returnPart1442); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:767:12: ( attributeTerm[retAttr] )*
                        loop19:
                        do {
                            int alt19=2;
                            int LA19_0 = input.LA(1);

                            if ( (LA19_0==AGGREGATION||LA19_0==KMEMBER||LA19_0==MEMBER) ) {
                                alt19=1;
                            }


                            switch (alt19) {
                        	case 1 :
                        	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:767:12: attributeTerm[retAttr]
                        	    {
                        	    pushFollow(FOLLOW_attributeTerm_in_returnPart1444);
                        	    attributeTerm(retAttr);

                        	    state._fsp--;


                        	    }
                        	    break;

                        	default :
                        	    break loop19;
                            }
                        } while (true);


                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:767:41: (value= NAME )?
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==NAME) ) {
                            alt20=1;
                        }
                        switch (alt20) {
                            case 1 :
                                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:767:41: value= NAME
                                {
                                value=(CommonTree)match(input,NAME,FOLLOW_NAME_in_returnPart1450); 

                                }
                                break;

                        }


                        match(input, Token.UP, null); 
                    }



                        RelationalMEPOutputSchemeEntry e = null;
                        OutputScheme scheme = new OutputScheme();
                        List<SDFAttribute> attrList = new ArrayList<SDFAttribute>();
                        
                        for (PathAttribute p : retAttr) {
                        	String variable = transformToString(p);
                        
                        	e = new RelationalMEPOutputSchemeEntry(variable);
                        	scheme.append(e);
                        	// TODO: Set correct Datatypes
                        	SDFAttribute attr = new SDFAttribute(null, e.getLabel(), SDFDatatype.STRING);
                        	attrList.add(attr);
                        }
                        String name = value != null && value.getText() != null ? value.getText() : "";
                        SDFSchema outputSchema = new SDFSchema(name, attrList);
                        ;
                        patternDetectAO.getStateMachine().setOutputScheme(scheme);
                        patternDetectAO.setOutputSchemaIntern(outputSchema);
                       

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseAST.g:790:3: 
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
    public static final BitSet FOLLOW_NAME_in_start87 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_query_in_start91 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_query_in_start109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUERY_in_query150 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_patternPart_in_query152 = new BitSet(new long[]{0x8000800000000008L,0x0000000000000102L});
    public static final BitSet FOLLOW_wherePart_in_query155 = new BitSet(new long[]{0x0000800000000008L,0x0000000000000102L});
    public static final BitSet FOLLOW_withinPart_in_query158 = new BitSet(new long[]{0x0000800000000008L,0x0000000000000100L});
    public static final BitSet FOLLOW_endsAtPart_in_query161 = new BitSet(new long[]{0x0000800000000008L});
    public static final BitSet FOLLOW_returnPart_in_query164 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart191 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_seqPatternPart_in_patternPart193 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SEQ_in_seqPatternPart217 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_state_in_seqPatternPart219 = new BitSet(new long[]{0x0000000000000008L,0x0000000000202000L});
    public static final BitSet FOLLOW_STATE_in_state247 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_state251 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_state255 = new BitSet(new long[]{0x0000010000000008L});
    public static final BitSet FOLLOW_NOTSIGN_in_state259 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_KSTATE_in_state278 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_state282 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_PLUS_in_state286 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_state290 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_state294 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_state298 = new BitSet(new long[]{0x0000010000000008L});
    public static final BitSet FOLLOW_NOTSIGN_in_state302 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHERE_in_wherePart329 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_whereStrat_in_wherePart333 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_whereExpression_in_wherePart338 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SKIP_TILL_NEXT_MATCH_in_whereStrat361 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat365 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SKIP_TILL_ANY_MATCH_in_whereStrat383 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat387 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STRICT_CONTIGUITY_in_whereStrat405 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat407 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARTITION_CONTIGUITY_in_whereStrat425 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_paramlist_in_whereStrat427 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARAMLIST_in_paramlist452 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeName_in_paramlist454 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000810L});
    public static final BitSet FOLLOW_KATTRIBUTE_in_attributeName480 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeName482 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_attributeName492 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeName494 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHEREEXPRESSION_in_whereExpression522 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_compareExpression_in_whereExpression536 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000428L});
    public static final BitSet FOLLOW_idexpression_in_whereExpression547 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000428L});
    public static final BitSet FOLLOW_assignment_in_whereExpression558 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000428L});
    public static final BitSet FOLLOW_COMPAREEXPRESSION_in_compareExpression606 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mathExpression_in_compareExpression608 = new BitSet(new long[]{0x0008000000004000L});
    public static final BitSet FOLLOW_set_in_compareExpression618 = new BitSet(new long[]{0x0200121400081000L,0x0000000000011004L});
    public static final BitSet FOLLOW_mathExpression_in_compareExpression648 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ASSIGNMENT_in_assignment684 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeTerm_in_assignment686 = new BitSet(new long[]{0x0200121400081000L,0x0000000000011004L});
    public static final BitSet FOLLOW_mathExpression_in_assignment689 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PLUS_in_mathExpression736 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MINUS_in_mathExpression748 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_MULT_in_mathExpression760 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DIVISION_in_mathExpression772 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mathExpression_in_mathExpression784 = new BitSet(new long[]{0x0200121400081000L,0x0000000000011004L});
    public static final BitSet FOLLOW_mathExpression_in_mathExpression787 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_attributeTerm_in_mathExpression807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_mathExpression848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOLEAN_in_mathExpression874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_mathExpression902 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KMEMBER_in_attributeTerm950 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_kAttributeUsage_in_attributeTerm952 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm957 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MEMBER_in_attributeTerm975 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm979 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm983 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AGGREGATION_in_attributeTerm1006 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_attributeTerm1015 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1077 = new BitSet(new long[]{0x0000002000000008L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1093 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage1130 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage1132 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage1150 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_FIRST_in_kAttributeUsage1152 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage1170 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_PREV_in_kAttributeUsage1172 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage1190 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_LEN_in_kAttributeUsage1192 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDEXPRESSION_in_idexpression1218 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NAME_in_idexpression1222 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart1253 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart1257 = new BitSet(new long[]{0x4002000901020008L});
    public static final BitSet FOLLOW_set_in_withinPart1280 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ENDSAT_in_endsAtPart1404 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeTerm_in_endsAtPart1406 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RETURN_in_returnPart1442 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart1444 = new BitSet(new long[]{0x0000002000000008L,0x0000000000011004L});
    public static final BitSet FOLLOW_NAME_in_returnPart1450 = new BitSet(new long[]{0x0000000000000008L});

}