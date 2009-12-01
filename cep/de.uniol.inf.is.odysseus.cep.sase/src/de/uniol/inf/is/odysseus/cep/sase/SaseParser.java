// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2009-12-01 16:52:52
 
	package de.uniol.inf.is.odysseus.cep.sase; 
	import java.util.Map;
	import java.util.TreeMap;
	import java.util.List;
	import java.util.ArrayList;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.debug.*;
import java.io.IOException;

import org.antlr.runtime.tree.*;

public class SaseParser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PATTERN", "WHERE", "WITHIN", "SEQ", "EGAL", "TIMEUNIT", "SKIP_METHOD", "KLEENEBRACKET", "PLUS", "NOT", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "UPPER", "LETTER", "TYPE", "LOWER", "NAME", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "NEWLINE", "WHITESPACE", "NOTSTATE", "STATE", "KSTATE", "NOTKSTATE"
    };
    public static final int INTEGER=17;
    public static final int STATE=32;
    public static final int WHERE=5;
    public static final int EGAL=8;
    public static final int KSTATE=33;
    public static final int LETTER=22;
    public static final int TIMEUNIT=9;
    public static final int SKIP_METHOD=10;
    public static final int NUMBER=19;
    public static final int NOTKSTATE=34;
    public static final int WHITESPACE=30;
    public static final int FLOAT=18;
    public static final int NOT=13;
    public static final int NOTSTATE=31;
    public static final int EOF=-1;
    public static final int SPACE=28;
    public static final int TYPE=23;
    public static final int PATTERN=4;
    public static final int LBRACKET=15;
    public static final int KLEENEBRACKET=11;
    public static final int NAME=25;
    public static final int STRING_LITERAL=27;
    public static final int NEWLINE=29;
    public static final int NONCONTROL_CHAR=26;
    public static final int COMMA=14;
    public static final int SEQ=7;
    public static final int WITHIN=6;
    public static final int PLUS=12;
    public static final int DIGIT=20;
    public static final int LOWER=24;
    public static final int RBRACKET=16;
    public static final int UPPER=21;

    // delegates
    // delegators

    public static final String[] ruleNames = new String[] {
        "invalidRule", "withinPart", "notSinglePItem", "pItem", "patternDecl", 
        "whereDecl", "notKleenePITem", "kleenePITem", "patternPart", "singlePItem", 
        "wherePart", "query"
    };
     
        public int ruleLevel = 0;
        public int getRuleLevel() { return ruleLevel; }
        public void incRuleLevel() { ruleLevel++; }
        public void decRuleLevel() { ruleLevel--; }
        public SaseParser(TokenStream input) {
            this(input, DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT, new RecognizerSharedState());
        }
        public SaseParser(TokenStream input, int port, RecognizerSharedState state) {
            super(input, state);
            DebugEventSocketProxy proxy =
                new DebugEventSocketProxy(this,port,adaptor);
            setDebugListener(proxy);
            setTokenStream(new DebugTokenStream(input,proxy));
            try {
                proxy.handshake();
            }
            catch (IOException ioe) {
                reportError(ioe);
            }
            TreeAdaptor adap = new CommonTreeAdaptor();
            setTreeAdaptor(adap);
            proxy.setTreeAdaptor(adap);
        }
    public SaseParser(TokenStream input, DebugEventListener dbg) {
        super(input, dbg);

         
        TreeAdaptor adap = new CommonTreeAdaptor();
        setTreeAdaptor(adap);

    }
    protected boolean evalPredicate(boolean result, String predicate) {
        dbg.semanticPredicate(result, predicate);
        return result;
    }

    protected DebugTreeAdaptor adaptor;
    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = new DebugTreeAdaptor(dbg,adaptor);

    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }


    public String[] getTokenNames() { return SaseParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g"; }


    	private Map<String, String> variableMap = new TreeMap<String, String>();
    	private List<String> kleeneAttributes = new ArrayList<String>();
    	private List<String> states = new ArrayList<String>();
    	private Map<String, String> stateVariables = new TreeMap<String, String>();
    	
    	private String getVariableType(CommonTree nameNode){
    		String name = nameNode.getText();
    		String type = variableMap.get(name);
    		if (type == null){
    			String msg = "The variable \""+name+"\" is not declared.";
    			throw new RuntimeException(msg);
    		}
    		return type;
    	}
    	
    	private void addState(String name, String type, boolean isKleeneAttribute){
    		if (variableMap.get(name) != null){
    			String msg = "The variable \""+name+"\" is already declared.";
    			throw new RuntimeException(msg);	
    		}
    		variableMap.put(name, type);
    		if (isKleeneAttribute){
    			states.add(name+"[1]");
    			stateVariables.put(name+"[1]", name);
    			states.add(name+"[i]");
    			stateVariables.put(name+"[i]", name);
    			kleeneAttributes.add(name);
    		}else{
    			states.add(name);
    			stateVariables.put(name, name);			
    		}
    	}
    	
    	public Map<String, String> getVariables(){
    		return variableMap;
    	}
    	public List<String> getKleeneAttributes(){
    		return kleeneAttributes;
    	}
    	public List<String> getStates(){
    		return states;
    	}
    	public Map<String, String> getStateVariables(){
    		return stateVariables;
    	}



    public static class query_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "query"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:1: query : patternPart ( wherePart )? ( withinPart )? ;
    public final SaseParser.query_return query() throws RecognitionException {
        SaseParser.query_return retval = new SaseParser.query_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.patternPart_return patternPart1 = null;

        SaseParser.wherePart_return wherePart2 = null;

        SaseParser.withinPart_return withinPart3 = null;



        try { dbg.enterRule(getGrammarFileName(), "query");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(73, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:2: ( patternPart ( wherePart )? ( withinPart )? )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:5: patternPart ( wherePart )? ( withinPart )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(74,5);
            pushFollow(FOLLOW_patternPart_in_query70);
            patternPart1=patternPart();

            state._fsp--;

            adaptor.addChild(root_0, patternPart1.getTree());
            dbg.location(74,17);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:17: ( wherePart )?
            int alt1=2;
            try { dbg.enterSubRule(1);
            try { dbg.enterDecision(1);

            int LA1_0 = input.LA(1);

            if ( (LA1_0==WHERE) ) {
                alt1=1;
            }
            } finally {dbg.exitDecision(1);}

            switch (alt1) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:18: wherePart
                    {
                    dbg.location(74,18);
                    pushFollow(FOLLOW_wherePart_in_query73);
                    wherePart2=wherePart();

                    state._fsp--;

                    adaptor.addChild(root_0, wherePart2.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(1);}

            dbg.location(74,30);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:30: ( withinPart )?
            int alt2=2;
            try { dbg.enterSubRule(2);
            try { dbg.enterDecision(2);

            int LA2_0 = input.LA(1);

            if ( (LA2_0==WITHIN) ) {
                alt2=1;
            }
            } finally {dbg.exitDecision(2);}

            switch (alt2) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:31: withinPart
                    {
                    dbg.location(74,31);
                    pushFollow(FOLLOW_withinPart_in_query78);
                    withinPart3=withinPart();

                    state._fsp--;

                    adaptor.addChild(root_0, withinPart3.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(2);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(75, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "query");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "query"

    public static class withinPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "withinPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:1: withinPart : WITHIN INTEGER TIMEUNIT ;
    public final SaseParser.withinPart_return withinPart() throws RecognitionException {
        SaseParser.withinPart_return retval = new SaseParser.withinPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WITHIN4=null;
        Token INTEGER5=null;
        Token TIMEUNIT6=null;

        Object WITHIN4_tree=null;
        Object INTEGER5_tree=null;
        Object TIMEUNIT6_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "withinPart");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(77, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:2: ( WITHIN INTEGER TIMEUNIT )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:4: WITHIN INTEGER TIMEUNIT
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(78,4);
            WITHIN4=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart92); 
            WITHIN4_tree = (Object)adaptor.create(WITHIN4);
            adaptor.addChild(root_0, WITHIN4_tree);

            dbg.location(78,11);
            INTEGER5=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_withinPart94); 
            INTEGER5_tree = (Object)adaptor.create(INTEGER5);
            adaptor.addChild(root_0, INTEGER5_tree);

            dbg.location(78,19);
            TIMEUNIT6=(Token)match(input,TIMEUNIT,FOLLOW_TIMEUNIT_in_withinPart96); 
            TIMEUNIT6_tree = (Object)adaptor.create(TIMEUNIT6);
            adaptor.addChild(root_0, TIMEUNIT6_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(79, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "withinPart");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "withinPart"

    public static class wherePart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "wherePart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:1: wherePart : WHERE whereDecl -> ^( WHERE whereDecl ) ;
    public final SaseParser.wherePart_return wherePart() throws RecognitionException {
        SaseParser.wherePart_return retval = new SaseParser.wherePart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WHERE7=null;
        SaseParser.whereDecl_return whereDecl8 = null;


        Object WHERE7_tree=null;
        RewriteRuleTokenStream stream_WHERE=new RewriteRuleTokenStream(adaptor,"token WHERE");
        RewriteRuleSubtreeStream stream_whereDecl=new RewriteRuleSubtreeStream(adaptor,"rule whereDecl");
        try { dbg.enterRule(getGrammarFileName(), "wherePart");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(81, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:2: ( WHERE whereDecl -> ^( WHERE whereDecl ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:4: WHERE whereDecl
            {
            dbg.location(82,4);
            WHERE7=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart108);  
            stream_WHERE.add(WHERE7);

            dbg.location(82,10);
            pushFollow(FOLLOW_whereDecl_in_wherePart110);
            whereDecl8=whereDecl();

            state._fsp--;

            stream_whereDecl.add(whereDecl8.getTree());


            // AST REWRITE
            // elements: WHERE, whereDecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 82:20: -> ^( WHERE whereDecl )
            {
                dbg.location(82,23);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:23: ^( WHERE whereDecl )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(82,25);
                root_1 = (Object)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

                dbg.location(82,31);
                adaptor.addChild(root_1, stream_whereDecl.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(83, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "wherePart");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "wherePart"

    public static class patternPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:85:1: patternPart : PATTERN patternDecl -> ^( PATTERN patternDecl ) ;
    public final SaseParser.patternPart_return patternPart() throws RecognitionException {
        SaseParser.patternPart_return retval = new SaseParser.patternPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token PATTERN9=null;
        SaseParser.patternDecl_return patternDecl10 = null;


        Object PATTERN9_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleSubtreeStream stream_patternDecl=new RewriteRuleSubtreeStream(adaptor,"rule patternDecl");
        try { dbg.enterRule(getGrammarFileName(), "patternPart");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(85, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:2: ( PATTERN patternDecl -> ^( PATTERN patternDecl ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:4: PATTERN patternDecl
            {
            dbg.location(86,4);
            PATTERN9=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart131);  
            stream_PATTERN.add(PATTERN9);

            dbg.location(86,12);
            pushFollow(FOLLOW_patternDecl_in_patternPart133);
            patternDecl10=patternDecl();

            state._fsp--;

            stream_patternDecl.add(patternDecl10.getTree());


            // AST REWRITE
            // elements: PATTERN, patternDecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 86:24: -> ^( PATTERN patternDecl )
            {
                dbg.location(86,27);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:27: ^( PATTERN patternDecl )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(86,29);
                root_1 = (Object)adaptor.becomeRoot(stream_PATTERN.nextNode(), root_1);

                dbg.location(86,37);
                adaptor.addChild(root_1, stream_patternDecl.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(87, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "patternPart");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "patternPart"

    public static class patternDecl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternDecl"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:90:1: patternDecl : SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) ;
    public final SaseParser.patternDecl_return patternDecl() throws RecognitionException {
        SaseParser.patternDecl_return retval = new SaseParser.patternDecl_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEQ11=null;
        Token LBRACKET12=null;
        Token COMMA14=null;
        Token RBRACKET16=null;
        SaseParser.pItem_return pItem13 = null;

        SaseParser.pItem_return pItem15 = null;


        Object SEQ11_tree=null;
        Object LBRACKET12_tree=null;
        Object COMMA14_tree=null;
        Object RBRACKET16_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SEQ=new RewriteRuleTokenStream(adaptor,"token SEQ");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_pItem=new RewriteRuleSubtreeStream(adaptor,"rule pItem");
        try { dbg.enterRule(getGrammarFileName(), "patternDecl");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(90, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:2: ( SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:4: SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET
            {
            dbg.location(91,4);
            SEQ11=(Token)match(input,SEQ,FOLLOW_SEQ_in_patternDecl155);  
            stream_SEQ.add(SEQ11);

            dbg.location(91,8);
            LBRACKET12=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_patternDecl157);  
            stream_LBRACKET.add(LBRACKET12);

            dbg.location(91,17);
            pushFollow(FOLLOW_pItem_in_patternDecl159);
            pItem13=pItem();

            state._fsp--;

            stream_pItem.add(pItem13.getTree());
            dbg.location(91,23);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:23: ( COMMA pItem )*
            try { dbg.enterSubRule(3);

            loop3:
            do {
                int alt3=2;
                try { dbg.enterDecision(3);

                int LA3_0 = input.LA(1);

                if ( (LA3_0==COMMA) ) {
                    alt3=1;
                }


                } finally {dbg.exitDecision(3);}

                switch (alt3) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:24: COMMA pItem
            	    {
            	    dbg.location(91,24);
            	    COMMA14=(Token)match(input,COMMA,FOLLOW_COMMA_in_patternDecl162);  
            	    stream_COMMA.add(COMMA14);

            	    dbg.location(91,30);
            	    pushFollow(FOLLOW_pItem_in_patternDecl164);
            	    pItem15=pItem();

            	    state._fsp--;

            	    stream_pItem.add(pItem15.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);
            } finally {dbg.exitSubRule(3);}

            dbg.location(91,38);
            RBRACKET16=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_patternDecl168);  
            stream_RBRACKET.add(RBRACKET16);



            // AST REWRITE
            // elements: pItem, SEQ
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 91:47: -> ^( SEQ ( pItem )* )
            {
                dbg.location(91,50);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:50: ^( SEQ ( pItem )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(91,52);
                root_1 = (Object)adaptor.becomeRoot(stream_SEQ.nextNode(), root_1);

                dbg.location(91,56);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:56: ( pItem )*
                while ( stream_pItem.hasNext() ) {
                    dbg.location(91,56);
                    adaptor.addChild(root_1, stream_pItem.nextTree());

                }
                stream_pItem.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(92, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "patternDecl");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "patternDecl"

    public static class pItem_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pItem"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:1: pItem : ( singlePItem | kleenePITem | notSinglePItem | notKleenePITem ) ;
    public final SaseParser.pItem_return pItem() throws RecognitionException {
        SaseParser.pItem_return retval = new SaseParser.pItem_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.singlePItem_return singlePItem17 = null;

        SaseParser.kleenePITem_return kleenePITem18 = null;

        SaseParser.notSinglePItem_return notSinglePItem19 = null;

        SaseParser.notKleenePITem_return notKleenePITem20 = null;



        try { dbg.enterRule(getGrammarFileName(), "pItem");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(95, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:8: ( ( singlePItem | kleenePITem | notSinglePItem | notKleenePITem ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:11: ( singlePItem | kleenePITem | notSinglePItem | notKleenePITem )
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(95,11);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:11: ( singlePItem | kleenePITem | notSinglePItem | notKleenePITem )
            int alt4=4;
            try { dbg.enterSubRule(4);
            try { dbg.enterDecision(4);

            int LA4_0 = input.LA(1);

            if ( (LA4_0==TYPE) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==NAME) ) {
                    alt4=1;
                }
                else if ( (LA4_1==PLUS) ) {
                    alt4=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else if ( (LA4_0==NOT) ) {
                int LA4_2 = input.LA(2);

                if ( (LA4_2==LBRACKET) ) {
                    int LA4_5 = input.LA(3);

                    if ( (LA4_5==TYPE) ) {
                        int LA4_6 = input.LA(4);

                        if ( (LA4_6==NAME) ) {
                            alt4=3;
                        }
                        else if ( (LA4_6==PLUS) ) {
                            alt4=4;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 4, 6, input);

                            dbg.recognitionException(nvae);
                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 5, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 2, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(4);}

            switch (alt4) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:12: singlePItem
                    {
                    dbg.location(95,12);
                    pushFollow(FOLLOW_singlePItem_in_pItem194);
                    singlePItem17=singlePItem();

                    state._fsp--;

                    adaptor.addChild(root_0, singlePItem17.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:26: kleenePITem
                    {
                    dbg.location(95,26);
                    pushFollow(FOLLOW_kleenePITem_in_pItem198);
                    kleenePITem18=kleenePITem();

                    state._fsp--;

                    adaptor.addChild(root_0, kleenePITem18.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:40: notSinglePItem
                    {
                    dbg.location(95,40);
                    pushFollow(FOLLOW_notSinglePItem_in_pItem202);
                    notSinglePItem19=notSinglePItem();

                    state._fsp--;

                    adaptor.addChild(root_0, notSinglePItem19.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:57: notKleenePITem
                    {
                    dbg.location(95,57);
                    pushFollow(FOLLOW_notKleenePITem_in_pItem206);
                    notKleenePITem20=notKleenePITem();

                    state._fsp--;

                    adaptor.addChild(root_0, notKleenePITem20.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(4);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(96, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "pItem");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "pItem"

    public static class notSinglePItem_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "notSinglePItem"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:1: notSinglePItem : NOT LBRACKET TYPE NAME RBRACKET -> ^( NOTSTATE TYPE NAME ) ;
    public final SaseParser.notSinglePItem_return notSinglePItem() throws RecognitionException {
        SaseParser.notSinglePItem_return retval = new SaseParser.notSinglePItem_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NOT21=null;
        Token LBRACKET22=null;
        Token TYPE23=null;
        Token NAME24=null;
        Token RBRACKET25=null;

        Object NOT21_tree=null;
        Object LBRACKET22_tree=null;
        Object TYPE23_tree=null;
        Object NAME24_tree=null;
        Object RBRACKET25_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_TYPE=new RewriteRuleTokenStream(adaptor,"token TYPE");

        try { dbg.enterRule(getGrammarFileName(), "notSinglePItem");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(98, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:2: ( NOT LBRACKET TYPE NAME RBRACKET -> ^( NOTSTATE TYPE NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:4: NOT LBRACKET TYPE NAME RBRACKET
            {
            dbg.location(99,4);
            NOT21=(Token)match(input,NOT,FOLLOW_NOT_in_notSinglePItem218);  
            stream_NOT.add(NOT21);

            dbg.location(99,8);
            LBRACKET22=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_notSinglePItem220);  
            stream_LBRACKET.add(LBRACKET22);

            dbg.location(99,17);
            TYPE23=(Token)match(input,TYPE,FOLLOW_TYPE_in_notSinglePItem222);  
            stream_TYPE.add(TYPE23);

            dbg.location(99,22);
            NAME24=(Token)match(input,NAME,FOLLOW_NAME_in_notSinglePItem224);  
            stream_NAME.add(NAME24);

            dbg.location(99,27);
            RBRACKET25=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_notSinglePItem226);  
            stream_RBRACKET.add(RBRACKET25);

            dbg.location(99,36);
            addState((NAME24!=null?NAME24.getText():null),(TYPE23!=null?TYPE23.getText():null), false);


            // AST REWRITE
            // elements: TYPE, NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 99:78: -> ^( NOTSTATE TYPE NAME )
            {
                dbg.location(99,81);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:81: ^( NOTSTATE TYPE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(99,83);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NOTSTATE, "NOTSTATE"), root_1);

                dbg.location(99,92);
                adaptor.addChild(root_1, stream_TYPE.nextNode());
                dbg.location(99,97);
                adaptor.addChild(root_1, stream_NAME.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(100, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "notSinglePItem");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "notSinglePItem"

    public static class singlePItem_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "singlePItem"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:1: singlePItem : TYPE NAME -> ^( STATE TYPE NAME ) ;
    public final SaseParser.singlePItem_return singlePItem() throws RecognitionException {
        SaseParser.singlePItem_return retval = new SaseParser.singlePItem_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token TYPE26=null;
        Token NAME27=null;

        Object TYPE26_tree=null;
        Object NAME27_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_TYPE=new RewriteRuleTokenStream(adaptor,"token TYPE");

        try { dbg.enterRule(getGrammarFileName(), "singlePItem");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(103, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:2: ( TYPE NAME -> ^( STATE TYPE NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:4: TYPE NAME
            {
            dbg.location(104,4);
            TYPE26=(Token)match(input,TYPE,FOLLOW_TYPE_in_singlePItem252);  
            stream_TYPE.add(TYPE26);

            dbg.location(104,9);
            NAME27=(Token)match(input,NAME,FOLLOW_NAME_in_singlePItem254);  
            stream_NAME.add(NAME27);

            dbg.location(104,14);
            addState((NAME27!=null?NAME27.getText():null),(TYPE26!=null?TYPE26.getText():null), false);


            // AST REWRITE
            // elements: NAME, TYPE
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 104:56: -> ^( STATE TYPE NAME )
            {
                dbg.location(104,59);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:59: ^( STATE TYPE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(104,61);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STATE, "STATE"), root_1);

                dbg.location(104,67);
                adaptor.addChild(root_1, stream_TYPE.nextNode());
                dbg.location(104,72);
                adaptor.addChild(root_1, stream_NAME.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(105, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "singlePItem");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "singlePItem"

    public static class kleenePITem_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kleenePITem"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:107:1: kleenePITem : TYPE PLUS NAME KLEENEBRACKET -> ^( KSTATE TYPE NAME ) ;
    public final SaseParser.kleenePITem_return kleenePITem() throws RecognitionException {
        SaseParser.kleenePITem_return retval = new SaseParser.kleenePITem_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token TYPE28=null;
        Token PLUS29=null;
        Token NAME30=null;
        Token KLEENEBRACKET31=null;

        Object TYPE28_tree=null;
        Object PLUS29_tree=null;
        Object NAME30_tree=null;
        Object KLEENEBRACKET31_tree=null;
        RewriteRuleTokenStream stream_KLEENEBRACKET=new RewriteRuleTokenStream(adaptor,"token KLEENEBRACKET");
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_TYPE=new RewriteRuleTokenStream(adaptor,"token TYPE");

        try { dbg.enterRule(getGrammarFileName(), "kleenePITem");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(107, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:2: ( TYPE PLUS NAME KLEENEBRACKET -> ^( KSTATE TYPE NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:4: TYPE PLUS NAME KLEENEBRACKET
            {
            dbg.location(108,4);
            TYPE28=(Token)match(input,TYPE,FOLLOW_TYPE_in_kleenePITem278);  
            stream_TYPE.add(TYPE28);

            dbg.location(108,9);
            PLUS29=(Token)match(input,PLUS,FOLLOW_PLUS_in_kleenePITem280);  
            stream_PLUS.add(PLUS29);

            dbg.location(108,14);
            NAME30=(Token)match(input,NAME,FOLLOW_NAME_in_kleenePITem282);  
            stream_NAME.add(NAME30);

            dbg.location(108,19);
            KLEENEBRACKET31=(Token)match(input,KLEENEBRACKET,FOLLOW_KLEENEBRACKET_in_kleenePITem284);  
            stream_KLEENEBRACKET.add(KLEENEBRACKET31);

            dbg.location(108,33);
            addState((NAME30!=null?NAME30.getText():null),(TYPE28!=null?TYPE28.getText():null), true);


            // AST REWRITE
            // elements: TYPE, NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 108:74: -> ^( KSTATE TYPE NAME )
            {
                dbg.location(108,77);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:77: ^( KSTATE TYPE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(108,79);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KSTATE, "KSTATE"), root_1);

                dbg.location(108,86);
                adaptor.addChild(root_1, stream_TYPE.nextNode());
                dbg.location(108,91);
                adaptor.addChild(root_1, stream_NAME.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(109, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "kleenePITem");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "kleenePITem"

    public static class notKleenePITem_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "notKleenePITem"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:1: notKleenePITem : NOT LBRACKET TYPE PLUS NAME KLEENEBRACKET RBRACKET -> ^( NOTKSTATE TYPE NAME ) ;
    public final SaseParser.notKleenePITem_return notKleenePITem() throws RecognitionException {
        SaseParser.notKleenePITem_return retval = new SaseParser.notKleenePITem_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NOT32=null;
        Token LBRACKET33=null;
        Token TYPE34=null;
        Token PLUS35=null;
        Token NAME36=null;
        Token KLEENEBRACKET37=null;
        Token RBRACKET38=null;

        Object NOT32_tree=null;
        Object LBRACKET33_tree=null;
        Object TYPE34_tree=null;
        Object PLUS35_tree=null;
        Object NAME36_tree=null;
        Object KLEENEBRACKET37_tree=null;
        Object RBRACKET38_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_KLEENEBRACKET=new RewriteRuleTokenStream(adaptor,"token KLEENEBRACKET");
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_TYPE=new RewriteRuleTokenStream(adaptor,"token TYPE");

        try { dbg.enterRule(getGrammarFileName(), "notKleenePITem");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(111, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:2: ( NOT LBRACKET TYPE PLUS NAME KLEENEBRACKET RBRACKET -> ^( NOTKSTATE TYPE NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:4: NOT LBRACKET TYPE PLUS NAME KLEENEBRACKET RBRACKET
            {
            dbg.location(112,4);
            NOT32=(Token)match(input,NOT,FOLLOW_NOT_in_notKleenePITem309);  
            stream_NOT.add(NOT32);

            dbg.location(112,8);
            LBRACKET33=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_notKleenePITem311);  
            stream_LBRACKET.add(LBRACKET33);

            dbg.location(112,17);
            TYPE34=(Token)match(input,TYPE,FOLLOW_TYPE_in_notKleenePITem313);  
            stream_TYPE.add(TYPE34);

            dbg.location(112,22);
            PLUS35=(Token)match(input,PLUS,FOLLOW_PLUS_in_notKleenePITem315);  
            stream_PLUS.add(PLUS35);

            dbg.location(112,27);
            NAME36=(Token)match(input,NAME,FOLLOW_NAME_in_notKleenePITem317);  
            stream_NAME.add(NAME36);

            dbg.location(112,32);
            KLEENEBRACKET37=(Token)match(input,KLEENEBRACKET,FOLLOW_KLEENEBRACKET_in_notKleenePITem319);  
            stream_KLEENEBRACKET.add(KLEENEBRACKET37);

            dbg.location(112,47);
            RBRACKET38=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_notKleenePITem322);  
            stream_RBRACKET.add(RBRACKET38);

            dbg.location(112,56);
            addState((NAME36!=null?NAME36.getText():null),(TYPE34!=null?TYPE34.getText():null), true);


            // AST REWRITE
            // elements: TYPE, NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 112:97: -> ^( NOTKSTATE TYPE NAME )
            {
                dbg.location(112,100);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:100: ^( NOTKSTATE TYPE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(112,102);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NOTKSTATE, "NOTKSTATE"), root_1);

                dbg.location(112,112);
                adaptor.addChild(root_1, stream_TYPE.nextNode());
                dbg.location(112,117);
                adaptor.addChild(root_1, stream_NAME.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(113, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "notKleenePITem");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "notKleenePITem"

    public static class whereDecl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "whereDecl"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:115:1: whereDecl : SKIP_METHOD LBRACKET ( NAME ( KLEENEBRACKET )? ( COMMA NAME ( KLEENEBRACKET )? )* ) RBRACKET ;
    public final SaseParser.whereDecl_return whereDecl() throws RecognitionException {
        SaseParser.whereDecl_return retval = new SaseParser.whereDecl_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SKIP_METHOD39=null;
        Token LBRACKET40=null;
        Token NAME41=null;
        Token KLEENEBRACKET42=null;
        Token COMMA43=null;
        Token NAME44=null;
        Token KLEENEBRACKET45=null;
        Token RBRACKET46=null;

        Object SKIP_METHOD39_tree=null;
        Object LBRACKET40_tree=null;
        Object NAME41_tree=null;
        Object KLEENEBRACKET42_tree=null;
        Object COMMA43_tree=null;
        Object NAME44_tree=null;
        Object KLEENEBRACKET45_tree=null;
        Object RBRACKET46_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "whereDecl");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(115, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:2: ( SKIP_METHOD LBRACKET ( NAME ( KLEENEBRACKET )? ( COMMA NAME ( KLEENEBRACKET )? )* ) RBRACKET )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:4: SKIP_METHOD LBRACKET ( NAME ( KLEENEBRACKET )? ( COMMA NAME ( KLEENEBRACKET )? )* ) RBRACKET
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(116,4);
            SKIP_METHOD39=(Token)match(input,SKIP_METHOD,FOLLOW_SKIP_METHOD_in_whereDecl347); 
            SKIP_METHOD39_tree = (Object)adaptor.create(SKIP_METHOD39);
            adaptor.addChild(root_0, SKIP_METHOD39_tree);

            dbg.location(116,16);
            LBRACKET40=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_whereDecl349); 
            LBRACKET40_tree = (Object)adaptor.create(LBRACKET40);
            adaptor.addChild(root_0, LBRACKET40_tree);

            dbg.location(116,25);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:25: ( NAME ( KLEENEBRACKET )? ( COMMA NAME ( KLEENEBRACKET )? )* )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:26: NAME ( KLEENEBRACKET )? ( COMMA NAME ( KLEENEBRACKET )? )*
            {
            dbg.location(116,26);
            NAME41=(Token)match(input,NAME,FOLLOW_NAME_in_whereDecl352); 
            NAME41_tree = (Object)adaptor.create(NAME41);
            adaptor.addChild(root_0, NAME41_tree);

            dbg.location(116,31);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:31: ( KLEENEBRACKET )?
            int alt5=2;
            try { dbg.enterSubRule(5);
            try { dbg.enterDecision(5);

            int LA5_0 = input.LA(1);

            if ( (LA5_0==KLEENEBRACKET) ) {
                alt5=1;
            }
            } finally {dbg.exitDecision(5);}

            switch (alt5) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:31: KLEENEBRACKET
                    {
                    dbg.location(116,31);
                    KLEENEBRACKET42=(Token)match(input,KLEENEBRACKET,FOLLOW_KLEENEBRACKET_in_whereDecl354); 
                    KLEENEBRACKET42_tree = (Object)adaptor.create(KLEENEBRACKET42);
                    adaptor.addChild(root_0, KLEENEBRACKET42_tree);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(5);}

            dbg.location(116,45);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:45: ( COMMA NAME ( KLEENEBRACKET )? )*
            try { dbg.enterSubRule(7);

            loop7:
            do {
                int alt7=2;
                try { dbg.enterDecision(7);

                int LA7_0 = input.LA(1);

                if ( (LA7_0==COMMA) ) {
                    alt7=1;
                }


                } finally {dbg.exitDecision(7);}

                switch (alt7) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:46: COMMA NAME ( KLEENEBRACKET )?
            	    {
            	    dbg.location(116,46);
            	    COMMA43=(Token)match(input,COMMA,FOLLOW_COMMA_in_whereDecl357); 
            	    COMMA43_tree = (Object)adaptor.create(COMMA43);
            	    adaptor.addChild(root_0, COMMA43_tree);

            	    dbg.location(116,52);
            	    NAME44=(Token)match(input,NAME,FOLLOW_NAME_in_whereDecl359); 
            	    NAME44_tree = (Object)adaptor.create(NAME44);
            	    adaptor.addChild(root_0, NAME44_tree);

            	    dbg.location(116,57);
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:57: ( KLEENEBRACKET )?
            	    int alt6=2;
            	    try { dbg.enterSubRule(6);
            	    try { dbg.enterDecision(6);

            	    int LA6_0 = input.LA(1);

            	    if ( (LA6_0==KLEENEBRACKET) ) {
            	        alt6=1;
            	    }
            	    } finally {dbg.exitDecision(6);}

            	    switch (alt6) {
            	        case 1 :
            	            dbg.enterAlt(1);

            	            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:57: KLEENEBRACKET
            	            {
            	            dbg.location(116,57);
            	            KLEENEBRACKET45=(Token)match(input,KLEENEBRACKET,FOLLOW_KLEENEBRACKET_in_whereDecl361); 
            	            KLEENEBRACKET45_tree = (Object)adaptor.create(KLEENEBRACKET45);
            	            adaptor.addChild(root_0, KLEENEBRACKET45_tree);


            	            }
            	            break;

            	    }
            	    } finally {dbg.exitSubRule(6);}


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);
            } finally {dbg.exitSubRule(7);}


            }

            dbg.location(116,75);
            RBRACKET46=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_whereDecl367); 
            RBRACKET46_tree = (Object)adaptor.create(RBRACKET46);
            adaptor.addChild(root_0, RBRACKET46_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(116, 84);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "whereDecl");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "whereDecl"

    // Delegated rules


 

    public static final BitSet FOLLOW_patternPart_in_query70 = new BitSet(new long[]{0x0000000000000062L});
    public static final BitSet FOLLOW_wherePart_in_query73 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_withinPart_in_query78 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart92 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_INTEGER_in_withinPart94 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_TIMEUNIT_in_withinPart96 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart108 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_whereDecl_in_wherePart110 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart131 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEQ_in_patternDecl155 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_LBRACKET_in_patternDecl157 = new BitSet(new long[]{0x0000000000802000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl159 = new BitSet(new long[]{0x0000000000014000L});
    public static final BitSet FOLLOW_COMMA_in_patternDecl162 = new BitSet(new long[]{0x0000000000802000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl164 = new BitSet(new long[]{0x0000000000014000L});
    public static final BitSet FOLLOW_RBRACKET_in_patternDecl168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_singlePItem_in_pItem194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kleenePITem_in_pItem198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_notSinglePItem_in_pItem202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_notKleenePITem_in_pItem206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_notSinglePItem218 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_LBRACKET_in_notSinglePItem220 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_TYPE_in_notSinglePItem222 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_NAME_in_notSinglePItem224 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_RBRACKET_in_notSinglePItem226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPE_in_singlePItem252 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_NAME_in_singlePItem254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPE_in_kleenePITem278 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_PLUS_in_kleenePITem280 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_NAME_in_kleenePITem282 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_KLEENEBRACKET_in_kleenePITem284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_notKleenePITem309 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_LBRACKET_in_notKleenePITem311 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_TYPE_in_notKleenePITem313 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_PLUS_in_notKleenePITem315 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_NAME_in_notKleenePITem317 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_KLEENEBRACKET_in_notKleenePITem319 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_RBRACKET_in_notKleenePITem322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_METHOD_in_whereDecl347 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_LBRACKET_in_whereDecl349 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_NAME_in_whereDecl352 = new BitSet(new long[]{0x0000000000014800L});
    public static final BitSet FOLLOW_KLEENEBRACKET_in_whereDecl354 = new BitSet(new long[]{0x0000000000014000L});
    public static final BitSet FOLLOW_COMMA_in_whereDecl357 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_NAME_in_whereDecl359 = new BitSet(new long[]{0x0000000000014800L});
    public static final BitSet FOLLOW_KLEENEBRACKET_in_whereDecl361 = new BitSet(new long[]{0x0000000000014000L});
    public static final BitSet FOLLOW_RBRACKET_in_whereDecl367 = new BitSet(new long[]{0x0000000000000002L});

}