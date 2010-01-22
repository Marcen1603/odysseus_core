// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2010-01-22 09:17:49
 
	package de.uniol.inf.is.odysseus.cep.sase; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.antlr.runtime.debug.*;
import java.io.IOException;

import org.antlr.runtime.tree.*;

public class SaseParser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CREATE", "STREAM", "PATTERN", "WHERE", "WITHIN", "RETURN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "FIRST", "CURRENT", "PREVIOUS", "ALLTOPREVIOUS", "NAME", "LAST", "BBRACKETLEFT", "BBRACKETRIGHT", "TIMEUNIT", "SKIP_METHOD", "AGGREGATEOP", "PLUS", "MINUS", "POINT", "DIVISION", "MULT", "COMPAREOP", "SINGLEEQUALS", "EQUALS", "NOT", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "STATE", "KTYPE", "TYPE", "WHERESTRAT", "WHEREEXPRESSION", "MATHEXPRESSION", "TERM", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "KMEMBER", "MEMBER", "COMPAREEXPRESSION", "IDEXPRESSION", "AGGREGATION", "CREATESTREAM"
    };
    public static final int TERM=55;
    public static final int WHERE=7;
    public static final int MEMBER=60;
    public static final int POINT=27;
    public static final int LETTER=41;
    public static final int ATTRIBUTE=56;
    public static final int FLOAT=38;
    public static final int EQUALS=32;
    public static final int NOT=33;
    public static final int AND=13;
    public static final int MATHEXPRESSION=54;
    public static final int SPACE=44;
    public static final int EOF=-1;
    public static final int TYPE=51;
    public static final int LBRACKET=35;
    public static final int PATTERN=6;
    public static final int SINGLEEQUALS=31;
    public static final int COMPAREEXPRESSION=61;
    public static final int CREATE=4;
    public static final int NAME=18;
    public static final int STRING_LITERAL=43;
    public static final int LEFTCURLY=11;
    public static final int SEQ=10;
    public static final int COMMA=34;
    public static final int PREVIOUS=16;
    public static final int KATTRIBUTE=57;
    public static final int RETURN=9;
    public static final int RIGHTCURLY=12;
    public static final int DIVISION=28;
    public static final int PLUS=25;
    public static final int DIGIT=40;
    public static final int LAST=19;
    public static final int RBRACKET=36;
    public static final int STREAM=5;
    public static final int BBRACKETRIGHT=21;
    public static final int INTEGER=37;
    public static final int STATE=49;
    public static final int IDEXPRESSION=62;
    public static final int CREATESTREAM=64;
    public static final int KMEMBER=59;
    public static final int ALLTOPREVIOUS=17;
    public static final int TIMEUNIT=22;
    public static final int SKIP_METHOD=23;
    public static final int NUMBER=39;
    public static final int PARAMLIST=58;
    public static final int WHITESPACE=48;
    public static final int MINUS=26;
    public static final int MULT=29;
    public static final int AGGREGATEOP=24;
    public static final int CURRENT=15;
    public static final int WHEREEXPRESSION=53;
    public static final int NEWLINE=47;
    public static final int NONCONTROL_CHAR=42;
    public static final int AGGREGATION=63;
    public static final int BBRACKETLEFT=20;
    public static final int WHERESTRAT=52;
    public static final int WITHIN=8;
    public static final int KTYPE=50;
    public static final int LOWER=45;
    public static final int COMPAREOP=30;
    public static final int UPPER=46;
    public static final int FIRST=14;

    // delegates
    // delegators

    public static final String[] ruleNames = new String[] {
        "invalidRule", "synpred24_SaseParser", "synpred23_SaseParser", 
        "synpred12_SaseParser", "synpred9_SaseParser", "query", "synpred14_SaseParser", 
        "patternDecl", "synpred4_SaseParser", "parameterList", "sAttributeName", 
        "whereExpressions", "synpred8_SaseParser", "kAttributeName", "patternPart", 
        "synpred5_SaseParser", "pItem", "synpred16_SaseParser", "synpred10_SaseParser", 
        "createPart", "synpred17_SaseParser", "value", "wherePart", "synpred22_SaseParser", 
        "synpred11_SaseParser", "synpred7_SaseParser", "kAttributeUsage", 
        "typeName", "returnPart", "term", "synpred15_SaseParser", "mathExpression", 
        "synpred3_SaseParser", "withinPart", "synpred1_SaseParser", "expression", 
        "synpred21_SaseParser", "synpred18_SaseParser", "aggregation", "synpred19_SaseParser", 
        "synpred13_SaseParser", "wherePart1", "synpred2_SaseParser", "attributeName", 
        "synpred6_SaseParser", "synpred25_SaseParser", "synpred20_SaseParser"
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


    public static class query_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "query"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:1: query : ( createPart )? patternPart ( wherePart )? ( withinPart )? ( returnPart )? ;
    public final SaseParser.query_return query() throws RecognitionException {
        SaseParser.query_return retval = new SaseParser.query_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.createPart_return createPart1 = null;

        SaseParser.patternPart_return patternPart2 = null;

        SaseParser.wherePart_return wherePart3 = null;

        SaseParser.withinPart_return withinPart4 = null;

        SaseParser.returnPart_return returnPart5 = null;



        try { dbg.enterRule(getGrammarFileName(), "query");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(33, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:7: ( ( createPart )? patternPart ( wherePart )? ( withinPart )? ( returnPart )? )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:9: ( createPart )? patternPart ( wherePart )? ( withinPart )? ( returnPart )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(33,9);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:9: ( createPart )?
            int alt1=2;
            try { dbg.enterSubRule(1);
            try { dbg.enterDecision(1);

            int LA1_0 = input.LA(1);

            if ( (LA1_0==CREATE) ) {
                alt1=1;
            }
            } finally {dbg.exitDecision(1);}

            switch (alt1) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:10: createPart
                    {
                    dbg.location(33,10);
                    pushFollow(FOLLOW_createPart_in_query120);
                    createPart1=createPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, createPart1.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(1);}

            dbg.location(33,23);
            pushFollow(FOLLOW_patternPart_in_query124);
            patternPart2=patternPart();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, patternPart2.getTree());
            dbg.location(33,35);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:35: ( wherePart )?
            int alt2=2;
            try { dbg.enterSubRule(2);
            try { dbg.enterDecision(2);

            int LA2_0 = input.LA(1);

            if ( (LA2_0==WHERE) ) {
                alt2=1;
            }
            } finally {dbg.exitDecision(2);}

            switch (alt2) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:36: wherePart
                    {
                    dbg.location(33,36);
                    pushFollow(FOLLOW_wherePart_in_query127);
                    wherePart3=wherePart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, wherePart3.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(2);}

            dbg.location(33,48);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:48: ( withinPart )?
            int alt3=2;
            try { dbg.enterSubRule(3);
            try { dbg.enterDecision(3);

            int LA3_0 = input.LA(1);

            if ( (LA3_0==WITHIN) ) {
                alt3=1;
            }
            } finally {dbg.exitDecision(3);}

            switch (alt3) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:49: withinPart
                    {
                    dbg.location(33,49);
                    pushFollow(FOLLOW_withinPart_in_query132);
                    withinPart4=withinPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, withinPart4.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(3);}

            dbg.location(33,62);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:62: ( returnPart )?
            int alt4=2;
            try { dbg.enterSubRule(4);
            try { dbg.enterDecision(4);

            int LA4_0 = input.LA(1);

            if ( (LA4_0==RETURN) ) {
                alt4=1;
            }
            } finally {dbg.exitDecision(4);}

            switch (alt4) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:63: returnPart
                    {
                    dbg.location(33,63);
                    pushFollow(FOLLOW_returnPart_in_query137);
                    returnPart5=returnPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, returnPart5.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(4);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(34, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "query");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "query"

    public static class createPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "createPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:36:1: createPart : CREATE STREAM NAME -> ^( CREATESTREAM NAME ) ;
    public final SaseParser.createPart_return createPart() throws RecognitionException {
        SaseParser.createPart_return retval = new SaseParser.createPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token CREATE6=null;
        Token STREAM7=null;
        Token NAME8=null;

        Object CREATE6_tree=null;
        Object STREAM7_tree=null;
        Object NAME8_tree=null;
        RewriteRuleTokenStream stream_CREATE=new RewriteRuleTokenStream(adaptor,"token CREATE");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_STREAM=new RewriteRuleTokenStream(adaptor,"token STREAM");

        try { dbg.enterRule(getGrammarFileName(), "createPart");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(36, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:2: ( CREATE STREAM NAME -> ^( CREATESTREAM NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:4: CREATE STREAM NAME
            {
            dbg.location(37,4);
            CREATE6=(Token)match(input,CREATE,FOLLOW_CREATE_in_createPart151); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CREATE.add(CREATE6);

            dbg.location(37,11);
            STREAM7=(Token)match(input,STREAM,FOLLOW_STREAM_in_createPart153); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STREAM.add(STREAM7);

            dbg.location(37,18);
            NAME8=(Token)match(input,NAME,FOLLOW_NAME_in_createPart155); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME8);



            // AST REWRITE
            // elements: NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 37:23: -> ^( CREATESTREAM NAME )
            {
                dbg.location(37,26);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:26: ^( CREATESTREAM NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(37,28);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CREATESTREAM, "CREATESTREAM"), root_1);

                dbg.location(37,41);
                adaptor.addChild(root_1, stream_NAME.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(37, 47);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "createPart");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "createPart"

    public static class withinPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "withinPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:39:1: withinPart : WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) ;
    public final SaseParser.withinPart_return withinPart() throws RecognitionException {
        SaseParser.withinPart_return retval = new SaseParser.withinPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WITHIN9=null;
        Token NUMBER10=null;
        Token TIMEUNIT11=null;

        Object WITHIN9_tree=null;
        Object NUMBER10_tree=null;
        Object TIMEUNIT11_tree=null;
        RewriteRuleTokenStream stream_WITHIN=new RewriteRuleTokenStream(adaptor,"token WITHIN");
        RewriteRuleTokenStream stream_TIMEUNIT=new RewriteRuleTokenStream(adaptor,"token TIMEUNIT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try { dbg.enterRule(getGrammarFileName(), "withinPart");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(39, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:40:2: ( WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:40:4: WITHIN NUMBER TIMEUNIT
            {
            dbg.location(40,4);
            WITHIN9=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart175); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_WITHIN.add(WITHIN9);

            dbg.location(40,11);
            NUMBER10=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart177); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER10);

            dbg.location(40,18);
            TIMEUNIT11=(Token)match(input,TIMEUNIT,FOLLOW_TIMEUNIT_in_withinPart179); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TIMEUNIT.add(TIMEUNIT11);



            // AST REWRITE
            // elements: TIMEUNIT, NUMBER, WITHIN
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 40:27: -> ^( WITHIN NUMBER TIMEUNIT )
            {
                dbg.location(40,30);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:40:30: ^( WITHIN NUMBER TIMEUNIT )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(40,32);
                root_1 = (Object)adaptor.becomeRoot(stream_WITHIN.nextNode(), root_1);

                dbg.location(40,39);
                adaptor.addChild(root_1, stream_NUMBER.nextNode());
                dbg.location(40,46);
                adaptor.addChild(root_1, stream_TIMEUNIT.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(41, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:43:1: wherePart : ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
    public final SaseParser.wherePart_return wherePart() throws RecognitionException {
        SaseParser.wherePart_return retval = new SaseParser.wherePart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WHERE12=null;
        Token LEFTCURLY14=null;
        Token RIGHTCURLY16=null;
        Token WHERE17=null;
        SaseParser.wherePart1_return wherePart113 = null;

        SaseParser.whereExpressions_return whereExpressions15 = null;

        SaseParser.whereExpressions_return whereExpressions18 = null;


        Object WHERE12_tree=null;
        Object LEFTCURLY14_tree=null;
        Object RIGHTCURLY16_tree=null;
        Object WHERE17_tree=null;
        RewriteRuleTokenStream stream_RIGHTCURLY=new RewriteRuleTokenStream(adaptor,"token RIGHTCURLY");
        RewriteRuleTokenStream stream_WHERE=new RewriteRuleTokenStream(adaptor,"token WHERE");
        RewriteRuleTokenStream stream_LEFTCURLY=new RewriteRuleTokenStream(adaptor,"token LEFTCURLY");
        RewriteRuleSubtreeStream stream_wherePart1=new RewriteRuleSubtreeStream(adaptor,"rule wherePart1");
        RewriteRuleSubtreeStream stream_whereExpressions=new RewriteRuleSubtreeStream(adaptor,"rule whereExpressions");
        try { dbg.enterRule(getGrammarFileName(), "wherePart");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(43, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:44:2: ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt5=2;
            try { dbg.enterDecision(5);

            int LA5_0 = input.LA(1);

            if ( (LA5_0==WHERE) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==NAME||LA5_1==AGGREGATEOP||LA5_1==NUMBER||LA5_1==STRING_LITERAL) ) {
                    alt5=2;
                }
                else if ( (LA5_1==SKIP_METHOD) ) {
                    alt5=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(5);}

            switch (alt5) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:44:4: WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    dbg.location(44,4);
                    WHERE12=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart201); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE12);

                    dbg.location(44,10);
                    pushFollow(FOLLOW_wherePart1_in_wherePart203);
                    wherePart113=wherePart1();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_wherePart1.add(wherePart113.getTree());
                    dbg.location(44,21);
                    LEFTCURLY14=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart205); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFTCURLY.add(LEFTCURLY14);

                    dbg.location(44,31);
                    pushFollow(FOLLOW_whereExpressions_in_wherePart207);
                    whereExpressions15=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions15.getTree());
                    dbg.location(44,48);
                    RIGHTCURLY16=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart209); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHTCURLY.add(RIGHTCURLY16);



                    // AST REWRITE
                    // elements: whereExpressions, WHERE, wherePart1
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 44:59: -> ^( WHERE wherePart1 whereExpressions )
                    {
                        dbg.location(44,62);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:44:62: ^( WHERE wherePart1 whereExpressions )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(44,64);
                        root_1 = (Object)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

                        dbg.location(44,70);
                        adaptor.addChild(root_1, stream_wherePart1.nextTree());
                        dbg.location(44,81);
                        adaptor.addChild(root_1, stream_whereExpressions.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:4: WHERE whereExpressions
                    {
                    dbg.location(45,4);
                    WHERE17=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart226); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE17);

                    dbg.location(45,10);
                    pushFollow(FOLLOW_whereExpressions_in_wherePart228);
                    whereExpressions18=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions18.getTree());


                    // AST REWRITE
                    // elements: WHERE, whereExpressions
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 45:27: -> ^( WHERE whereExpressions )
                    {
                        dbg.location(45,30);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:30: ^( WHERE whereExpressions )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(45,32);
                        root_1 = (Object)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

                        dbg.location(45,38);
                        adaptor.addChild(root_1, stream_whereExpressions.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(46, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:48:1: patternPart : PATTERN patternDecl -> ^( PATTERN patternDecl ) ;
    public final SaseParser.patternPart_return patternPart() throws RecognitionException {
        SaseParser.patternPart_return retval = new SaseParser.patternPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token PATTERN19=null;
        SaseParser.patternDecl_return patternDecl20 = null;


        Object PATTERN19_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleSubtreeStream stream_patternDecl=new RewriteRuleSubtreeStream(adaptor,"rule patternDecl");
        try { dbg.enterRule(getGrammarFileName(), "patternPart");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(48, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:2: ( PATTERN patternDecl -> ^( PATTERN patternDecl ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:4: PATTERN patternDecl
            {
            dbg.location(49,4);
            PATTERN19=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart249); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN19);

            dbg.location(49,12);
            pushFollow(FOLLOW_patternDecl_in_patternPart251);
            patternDecl20=patternDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternDecl.add(patternDecl20.getTree());


            // AST REWRITE
            // elements: PATTERN, patternDecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 49:24: -> ^( PATTERN patternDecl )
            {
                dbg.location(49,27);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:27: ^( PATTERN patternDecl )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(49,29);
                root_1 = (Object)adaptor.becomeRoot(stream_PATTERN.nextNode(), root_1);

                dbg.location(49,37);
                adaptor.addChild(root_1, stream_patternDecl.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(50, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "patternPart");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "patternPart"

    public static class returnPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "returnPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:1: returnPart : RETURN term ( COMMA term )* -> ^( RETURN ( term )* ) ;
    public final SaseParser.returnPart_return returnPart() throws RecognitionException {
        SaseParser.returnPart_return retval = new SaseParser.returnPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token RETURN21=null;
        Token COMMA23=null;
        SaseParser.term_return term22 = null;

        SaseParser.term_return term24 = null;


        Object RETURN21_tree=null;
        Object COMMA23_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RETURN=new RewriteRuleTokenStream(adaptor,"token RETURN");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try { dbg.enterRule(getGrammarFileName(), "returnPart");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(52, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:2: ( RETURN term ( COMMA term )* -> ^( RETURN ( term )* ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:5: RETURN term ( COMMA term )*
            {
            dbg.location(53,5);
            RETURN21=(Token)match(input,RETURN,FOLLOW_RETURN_in_returnPart272); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RETURN.add(RETURN21);

            dbg.location(53,12);
            pushFollow(FOLLOW_term_in_returnPart274);
            term22=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_term.add(term22.getTree());
            dbg.location(53,17);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:17: ( COMMA term )*
            try { dbg.enterSubRule(6);

            loop6:
            do {
                int alt6=2;
                try { dbg.enterDecision(6);

                int LA6_0 = input.LA(1);

                if ( (LA6_0==COMMA) ) {
                    alt6=1;
                }


                } finally {dbg.exitDecision(6);}

                switch (alt6) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:18: COMMA term
            	    {
            	    dbg.location(53,18);
            	    COMMA23=(Token)match(input,COMMA,FOLLOW_COMMA_in_returnPart277); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA23);

            	    dbg.location(53,24);
            	    pushFollow(FOLLOW_term_in_returnPart279);
            	    term24=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_term.add(term24.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);
            } finally {dbg.exitSubRule(6);}



            // AST REWRITE
            // elements: RETURN, term
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 53:31: -> ^( RETURN ( term )* )
            {
                dbg.location(53,34);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:34: ^( RETURN ( term )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(53,36);
                root_1 = (Object)adaptor.becomeRoot(stream_RETURN.nextNode(), root_1);

                dbg.location(53,43);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:43: ( term )*
                while ( stream_term.hasNext() ) {
                    dbg.location(53,43);
                    adaptor.addChild(root_1, stream_term.nextTree());

                }
                stream_term.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(54, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "returnPart");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "returnPart"

    public static class patternDecl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternDecl"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:56:1: patternDecl : SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) ;
    public final SaseParser.patternDecl_return patternDecl() throws RecognitionException {
        SaseParser.patternDecl_return retval = new SaseParser.patternDecl_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEQ25=null;
        Token LBRACKET26=null;
        Token COMMA28=null;
        Token RBRACKET30=null;
        SaseParser.pItem_return pItem27 = null;

        SaseParser.pItem_return pItem29 = null;


        Object SEQ25_tree=null;
        Object LBRACKET26_tree=null;
        Object COMMA28_tree=null;
        Object RBRACKET30_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SEQ=new RewriteRuleTokenStream(adaptor,"token SEQ");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_pItem=new RewriteRuleSubtreeStream(adaptor,"rule pItem");
        try { dbg.enterRule(getGrammarFileName(), "patternDecl");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(56, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:2: ( SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:4: SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET
            {
            dbg.location(57,4);
            SEQ25=(Token)match(input,SEQ,FOLLOW_SEQ_in_patternDecl302); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEQ.add(SEQ25);

            dbg.location(57,8);
            LBRACKET26=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_patternDecl304); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET26);

            dbg.location(57,17);
            pushFollow(FOLLOW_pItem_in_patternDecl306);
            pItem27=pItem();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_pItem.add(pItem27.getTree());
            dbg.location(57,23);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:23: ( COMMA pItem )*
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

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:24: COMMA pItem
            	    {
            	    dbg.location(57,24);
            	    COMMA28=(Token)match(input,COMMA,FOLLOW_COMMA_in_patternDecl309); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA28);

            	    dbg.location(57,30);
            	    pushFollow(FOLLOW_pItem_in_patternDecl311);
            	    pItem29=pItem();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_pItem.add(pItem29.getTree());

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);
            } finally {dbg.exitSubRule(7);}

            dbg.location(57,38);
            RBRACKET30=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_patternDecl315); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET30);



            // AST REWRITE
            // elements: pItem, SEQ
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 57:47: -> ^( SEQ ( pItem )* )
            {
                dbg.location(57,50);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:50: ^( SEQ ( pItem )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(57,52);
                root_1 = (Object)adaptor.becomeRoot(stream_SEQ.nextNode(), root_1);

                dbg.location(57,56);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:56: ( pItem )*
                while ( stream_pItem.hasNext() ) {
                    dbg.location(57,56);
                    adaptor.addChild(root_1, stream_pItem.nextTree());

                }
                stream_pItem.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(58, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:1: pItem : ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) ;
    public final SaseParser.pItem_return pItem() throws RecognitionException {
        SaseParser.pItem_return retval = new SaseParser.pItem_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NOT31=null;
        Token LBRACKET32=null;
        Token RBRACKET33=null;
        SaseParser.typeName_return type = null;

        SaseParser.attributeName_return variable = null;


        Object NOT31_tree=null;
        Object LBRACKET32_tree=null;
        Object RBRACKET33_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_typeName=new RewriteRuleSubtreeStream(adaptor,"rule typeName");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try { dbg.enterRule(getGrammarFileName(), "pItem");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(61, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:8: ( ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:10: ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )?
            {
            dbg.location(61,10);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:10: ( NOT )?
            int alt8=2;
            try { dbg.enterSubRule(8);
            try { dbg.enterDecision(8);

            int LA8_0 = input.LA(1);

            if ( (LA8_0==NOT) ) {
                alt8=1;
            }
            } finally {dbg.exitDecision(8);}

            switch (alt8) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:11: NOT
                    {
                    dbg.location(61,11);
                    NOT31=(Token)match(input,NOT,FOLLOW_NOT_in_pItem340); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NOT.add(NOT31);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(8);}

            dbg.location(61,17);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:17: ( LBRACKET )?
            int alt9=2;
            try { dbg.enterSubRule(9);
            try { dbg.enterDecision(9);

            int LA9_0 = input.LA(1);

            if ( (LA9_0==LBRACKET) ) {
                alt9=1;
            }
            } finally {dbg.exitDecision(9);}

            switch (alt9) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: LBRACKET
                    {
                    dbg.location(61,17);
                    LBRACKET32=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_pItem344); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET32);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(9);}

            dbg.location(61,32);
            pushFollow(FOLLOW_typeName_in_pItem350);
            type=typeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_typeName.add(type.getTree());
            dbg.location(61,50);
            pushFollow(FOLLOW_attributeName_in_pItem354);
            variable=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(variable.getTree());
            dbg.location(61,65);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:65: ( RBRACKET )?
            int alt10=2;
            try { dbg.enterSubRule(10);
            try { dbg.enterDecision(10);

            int LA10_0 = input.LA(1);

            if ( (LA10_0==RBRACKET) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==COMMA||LA10_1==RBRACKET) ) {
                    alt10=1;
                }
                else if ( (LA10_1==EOF) ) {
                    alt10=1;
                }
            }
            } finally {dbg.exitDecision(10);}

            switch (alt10) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: RBRACKET
                    {
                    dbg.location(61,65);
                    RBRACKET33=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_pItem356); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET33);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(10);}



            // AST REWRITE
            // elements: variable, type, NOT
            // token labels: 
            // rule labels: retval, type, variable
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type",type!=null?type.tree:null);
            RewriteRuleSubtreeStream stream_variable=new RewriteRuleSubtreeStream(adaptor,"rule variable",variable!=null?variable.tree:null);

            root_0 = (Object)adaptor.nil();
            // 61:75: -> ^( STATE $type $variable ( NOT )? )
            {
                dbg.location(61,78);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:78: ^( STATE $type $variable ( NOT )? )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(61,80);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STATE, "STATE"), root_1);

                dbg.location(61,86);
                adaptor.addChild(root_1, stream_type.nextTree());
                dbg.location(61,92);
                adaptor.addChild(root_1, stream_variable.nextTree());
                dbg.location(61,102);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:102: ( NOT )?
                if ( stream_NOT.hasNext() ) {
                    dbg.location(61,102);
                    adaptor.addChild(root_1, stream_NOT.nextNode());

                }
                stream_NOT.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(62, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "pItem");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "pItem"

    public static class typeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:1: typeName : NAME (op= PLUS )? -> {$op != null}? ^( KTYPE NAME $op) -> ^( TYPE NAME ) ;
    public final SaseParser.typeName_return typeName() throws RecognitionException {
        SaseParser.typeName_return retval = new SaseParser.typeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;
        Token NAME34=null;

        Object op_tree=null;
        Object NAME34_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");

        try { dbg.enterRule(getGrammarFileName(), "typeName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(64, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:9: ( NAME (op= PLUS )? -> {$op != null}? ^( KTYPE NAME $op) -> ^( TYPE NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:11: NAME (op= PLUS )?
            {
            dbg.location(64,11);
            NAME34=(Token)match(input,NAME,FOLLOW_NAME_in_typeName383); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME34);

            dbg.location(64,18);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:18: (op= PLUS )?
            int alt11=2;
            try { dbg.enterSubRule(11);
            try { dbg.enterDecision(11);

            int LA11_0 = input.LA(1);

            if ( (LA11_0==PLUS) ) {
                alt11=1;
            }
            } finally {dbg.exitDecision(11);}

            switch (alt11) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: op= PLUS
                    {
                    dbg.location(64,18);
                    op=(Token)match(input,PLUS,FOLLOW_PLUS_in_typeName387); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PLUS.add(op);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(11);}



            // AST REWRITE
            // elements: op, NAME, NAME
            // token labels: op
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 64:25: -> {$op != null}? ^( KTYPE NAME $op)
            if (op != null) {
                dbg.location(64,43);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:43: ^( KTYPE NAME $op)
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(64,45);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KTYPE, "KTYPE"), root_1);

                dbg.location(64,51);
                adaptor.addChild(root_1, stream_NAME.nextNode());
                dbg.location(64,56);
                adaptor.addChild(root_1, stream_op.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 65:9: -> ^( TYPE NAME )
            {
                dbg.location(65,12);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:12: ^( TYPE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(65,14);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TYPE, "TYPE"), root_1);

                dbg.location(65,19);
                adaptor.addChild(root_1, stream_NAME.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(66, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "typeName");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "typeName"

    public static class wherePart1_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "wherePart1"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:68:1: wherePart1 : SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) ;
    public final SaseParser.wherePart1_return wherePart1() throws RecognitionException {
        SaseParser.wherePart1_return retval = new SaseParser.wherePart1_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SKIP_METHOD35=null;
        Token LBRACKET36=null;
        Token RBRACKET38=null;
        SaseParser.parameterList_return parameterList37 = null;


        Object SKIP_METHOD35_tree=null;
        Object LBRACKET36_tree=null;
        Object RBRACKET38_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SKIP_METHOD=new RewriteRuleTokenStream(adaptor,"token SKIP_METHOD");
        RewriteRuleSubtreeStream stream_parameterList=new RewriteRuleSubtreeStream(adaptor,"rule parameterList");
        try { dbg.enterRule(getGrammarFileName(), "wherePart1");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(68, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:2: ( SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:4: SKIP_METHOD LBRACKET parameterList RBRACKET
            {
            dbg.location(69,4);
            SKIP_METHOD35=(Token)match(input,SKIP_METHOD,FOLLOW_SKIP_METHOD_in_wherePart1430); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SKIP_METHOD.add(SKIP_METHOD35);

            dbg.location(69,16);
            LBRACKET36=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_wherePart1432); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET36);

            dbg.location(69,25);
            pushFollow(FOLLOW_parameterList_in_wherePart1434);
            parameterList37=parameterList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_parameterList.add(parameterList37.getTree());
            dbg.location(69,39);
            RBRACKET38=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_wherePart1436); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET38);



            // AST REWRITE
            // elements: parameterList, SKIP_METHOD
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 69:48: -> ^( WHERESTRAT SKIP_METHOD parameterList )
            {
                dbg.location(69,51);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:51: ^( WHERESTRAT SKIP_METHOD parameterList )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(69,53);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHERESTRAT, "WHERESTRAT"), root_1);

                dbg.location(69,64);
                adaptor.addChild(root_1, stream_SKIP_METHOD.nextNode());
                dbg.location(69,76);
                adaptor.addChild(root_1, stream_parameterList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(70, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "wherePart1");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "wherePart1"

    public static class parameterList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameterList"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
    public final SaseParser.parameterList_return parameterList() throws RecognitionException {
        SaseParser.parameterList_return retval = new SaseParser.parameterList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token COMMA40=null;
        SaseParser.attributeName_return attributeName39 = null;

        SaseParser.attributeName_return attributeName41 = null;


        Object COMMA40_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try { dbg.enterRule(getGrammarFileName(), "parameterList");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(72, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:4: attributeName ( COMMA attributeName )*
            {
            dbg.location(73,4);
            pushFollow(FOLLOW_attributeName_in_parameterList459);
            attributeName39=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(attributeName39.getTree());
            dbg.location(73,17);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:17: ( COMMA attributeName )*
            try { dbg.enterSubRule(12);

            loop12:
            do {
                int alt12=2;
                try { dbg.enterDecision(12);

                int LA12_0 = input.LA(1);

                if ( (LA12_0==COMMA) ) {
                    alt12=1;
                }


                } finally {dbg.exitDecision(12);}

                switch (alt12) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:18: COMMA attributeName
            	    {
            	    dbg.location(73,18);
            	    COMMA40=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList461); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA40);

            	    dbg.location(73,24);
            	    pushFollow(FOLLOW_attributeName_in_parameterList463);
            	    attributeName41=attributeName();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeName.add(attributeName41.getTree());

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);
            } finally {dbg.exitSubRule(12);}



            // AST REWRITE
            // elements: attributeName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 73:40: -> ^( PARAMLIST ( attributeName )* )
            {
                dbg.location(73,43);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:43: ^( PARAMLIST ( attributeName )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(73,45);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PARAMLIST, "PARAMLIST"), root_1);

                dbg.location(73,55);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:55: ( attributeName )*
                while ( stream_attributeName.hasNext() ) {
                    dbg.location(73,55);
                    adaptor.addChild(root_1, stream_attributeName.nextTree());

                }
                stream_attributeName.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(74, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "parameterList");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "parameterList"

    public static class attributeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "attributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:76:1: attributeName : ( kAttributeName | sAttributeName );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.kAttributeName_return kAttributeName42 = null;

        SaseParser.sAttributeName_return sAttributeName43 = null;



        try { dbg.enterRule(getGrammarFileName(), "attributeName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(76, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:2: ( kAttributeName | sAttributeName )
            int alt13=2;
            try { dbg.enterDecision(13);

            int LA13_0 = input.LA(1);

            if ( (LA13_0==NAME) ) {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==BBRACKETLEFT) ) {
                    alt13=1;
                }
                else if ( (LA13_1==EOF||LA13_1==COMMA||LA13_1==RBRACKET) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(13);}

            switch (alt13) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:5: kAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(77,5);
                    pushFollow(FOLLOW_kAttributeName_in_attributeName487);
                    kAttributeName42=kAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, kAttributeName42.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:20: sAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(77,20);
                    pushFollow(FOLLOW_sAttributeName_in_attributeName489);
                    sAttributeName43=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sAttributeName43.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(78, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "attributeName");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "attributeName"

    public static class kAttributeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kAttributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:80:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) ;
    public final SaseParser.kAttributeName_return kAttributeName() throws RecognitionException {
        SaseParser.kAttributeName_return retval = new SaseParser.kAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME44=null;
        Token BBRACKETLEFT45=null;
        Token BBRACKETRIGHT46=null;

        Object NAME44_tree=null;
        Object BBRACKETLEFT45_tree=null;
        Object BBRACKETRIGHT46_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");

        try { dbg.enterRule(getGrammarFileName(), "kAttributeName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(80, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:2: ( NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            dbg.location(81,4);
            NAME44=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName501); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME44);

            dbg.location(81,10);
            BBRACKETLEFT45=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName504); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT45);

            dbg.location(81,23);
            BBRACKETRIGHT46=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName506); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT46);



            // AST REWRITE
            // elements: NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 81:38: -> ^( KATTRIBUTE NAME )
            {
                dbg.location(81,41);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:41: ^( KATTRIBUTE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(81,43);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KATTRIBUTE, "KATTRIBUTE"), root_1);

                dbg.location(81,54);
                adaptor.addChild(root_1, stream_NAME.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(82, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "kAttributeName");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "kAttributeName"

    public static class sAttributeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sAttributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:85:1: sAttributeName : NAME -> ^( ATTRIBUTE NAME ) ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME47=null;

        Object NAME47_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");

        try { dbg.enterRule(getGrammarFileName(), "sAttributeName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(85, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:2: ( NAME -> ^( ATTRIBUTE NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:4: NAME
            {
            dbg.location(86,4);
            NAME47=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName529); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME47);



            // AST REWRITE
            // elements: NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 86:9: -> ^( ATTRIBUTE NAME )
            {
                dbg.location(86,12);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:12: ^( ATTRIBUTE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(86,14);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ATTRIBUTE, "ATTRIBUTE"), root_1);

                dbg.location(86,24);
                adaptor.addChild(root_1, stream_NAME.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
            dbg.exitRule(getGrammarFileName(), "sAttributeName");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "sAttributeName"

    public static class kAttributeUsage_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kAttributeUsage"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:1: kAttributeUsage : ( NAME CURRENT | NAME FIRST | NAME PREVIOUS | NAME LAST );
    public final SaseParser.kAttributeUsage_return kAttributeUsage() throws RecognitionException {
        SaseParser.kAttributeUsage_return retval = new SaseParser.kAttributeUsage_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME48=null;
        Token CURRENT49=null;
        Token NAME50=null;
        Token FIRST51=null;
        Token NAME52=null;
        Token PREVIOUS53=null;
        Token NAME54=null;
        Token LAST55=null;

        Object NAME48_tree=null;
        Object CURRENT49_tree=null;
        Object NAME50_tree=null;
        Object FIRST51_tree=null;
        Object NAME52_tree=null;
        Object PREVIOUS53_tree=null;
        Object NAME54_tree=null;
        Object LAST55_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "kAttributeUsage");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(88, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:2: ( NAME CURRENT | NAME FIRST | NAME PREVIOUS | NAME LAST )
            int alt14=4;
            try { dbg.enterDecision(14);

            int LA14_0 = input.LA(1);

            if ( (LA14_0==NAME) ) {
                switch ( input.LA(2) ) {
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
                case PREVIOUS:
                    {
                    alt14=3;
                    }
                    break;
                case LAST:
                    {
                    alt14=4;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }

            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(14);}

            switch (alt14) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:5: NAME CURRENT
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(89,5);
                    NAME48=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage548); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME48_tree = (Object)adaptor.create(NAME48);
                    adaptor.addChild(root_0, NAME48_tree);
                    }
                    dbg.location(89,10);
                    CURRENT49=(Token)match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage550); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    CURRENT49_tree = (Object)adaptor.create(CURRENT49);
                    adaptor.addChild(root_0, CURRENT49_tree);
                    }

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:90:3: NAME FIRST
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(90,3);
                    NAME50=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage556); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME50_tree = (Object)adaptor.create(NAME50);
                    adaptor.addChild(root_0, NAME50_tree);
                    }
                    dbg.location(90,8);
                    FIRST51=(Token)match(input,FIRST,FOLLOW_FIRST_in_kAttributeUsage558); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FIRST51_tree = (Object)adaptor.create(FIRST51);
                    adaptor.addChild(root_0, FIRST51_tree);
                    }

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:3: NAME PREVIOUS
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(91,3);
                    NAME52=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage563); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME52_tree = (Object)adaptor.create(NAME52);
                    adaptor.addChild(root_0, NAME52_tree);
                    }
                    dbg.location(91,8);
                    PREVIOUS53=(Token)match(input,PREVIOUS,FOLLOW_PREVIOUS_in_kAttributeUsage565); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PREVIOUS53_tree = (Object)adaptor.create(PREVIOUS53);
                    adaptor.addChild(root_0, PREVIOUS53_tree);
                    }

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:3: NAME LAST
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(92,3);
                    NAME54=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage570); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME54_tree = (Object)adaptor.create(NAME54);
                    adaptor.addChild(root_0, NAME54_tree);
                    }
                    dbg.location(92,8);
                    LAST55=(Token)match(input,LAST,FOLLOW_LAST_in_kAttributeUsage572); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LAST55_tree = (Object)adaptor.create(LAST55);
                    adaptor.addChild(root_0, LAST55_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(93, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "kAttributeUsage");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "kAttributeUsage"

    public static class whereExpressions_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "whereExpressions"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:1: whereExpressions : expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) ;
    public final SaseParser.whereExpressions_return whereExpressions() throws RecognitionException {
        SaseParser.whereExpressions_return retval = new SaseParser.whereExpressions_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AND57=null;
        SaseParser.expression_return expression56 = null;

        SaseParser.expression_return expression58 = null;


        Object AND57_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try { dbg.enterRule(getGrammarFileName(), "whereExpressions");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(95, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:2: ( expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:4: expression ( AND expression )*
            {
            dbg.location(96,4);
            pushFollow(FOLLOW_expression_in_whereExpressions584);
            expression56=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression56.getTree());
            dbg.location(96,15);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:15: ( AND expression )*
            try { dbg.enterSubRule(15);

            loop15:
            do {
                int alt15=2;
                try { dbg.enterDecision(15);

                int LA15_0 = input.LA(1);

                if ( (LA15_0==AND) ) {
                    alt15=1;
                }


                } finally {dbg.exitDecision(15);}

                switch (alt15) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:16: AND expression
            	    {
            	    dbg.location(96,16);
            	    AND57=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions587); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_AND.add(AND57);

            	    dbg.location(96,20);
            	    pushFollow(FOLLOW_expression_in_whereExpressions589);
            	    expression58=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression58.getTree());

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);
            } finally {dbg.exitSubRule(15);}



            // AST REWRITE
            // elements: expression, AND
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 96:33: -> ^( WHEREEXPRESSION ( AND )? ( expression )* )
            {
                dbg.location(96,36);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:36: ^( WHEREEXPRESSION ( AND )? ( expression )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(96,38);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION"), root_1);

                dbg.location(96,54);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:54: ( AND )?
                if ( stream_AND.hasNext() ) {
                    dbg.location(96,54);
                    adaptor.addChild(root_1, stream_AND.nextNode());

                }
                stream_AND.reset();
                dbg.location(96,59);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:59: ( expression )*
                while ( stream_expression.hasNext() ) {
                    dbg.location(96,59);
                    adaptor.addChild(root_1, stream_expression.nextTree());

                }
                stream_expression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(97, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "whereExpressions");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "whereExpressions"

    public static class expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:1: expression : ( NAME -> ^( IDEXPRESSION NAME ) | f1= mathExpression SINGLEEQUALS f2= mathExpression -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= mathExpression COMPAREOP f2= mathExpression -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME59=null;
        Token SINGLEEQUALS60=null;
        Token COMPAREOP61=null;
        SaseParser.mathExpression_return f1 = null;

        SaseParser.mathExpression_return f2 = null;


        Object NAME59_tree=null;
        Object SINGLEEQUALS60_tree=null;
        Object COMPAREOP61_tree=null;
        RewriteRuleTokenStream stream_SINGLEEQUALS=new RewriteRuleTokenStream(adaptor,"token SINGLEEQUALS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleSubtreeStream stream_mathExpression=new RewriteRuleSubtreeStream(adaptor,"rule mathExpression");
        try { dbg.enterRule(getGrammarFileName(), "expression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(99, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:2: ( NAME -> ^( IDEXPRESSION NAME ) | f1= mathExpression SINGLEEQUALS f2= mathExpression -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= mathExpression COMPAREOP f2= mathExpression -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) )
            int alt16=3;
            try { dbg.enterDecision(16);

            try {
                isCyclicDecision = true;
                alt16 = dfa16.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(16);}

            switch (alt16) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:4: NAME
                    {
                    dbg.location(100,4);
                    NAME59=(Token)match(input,NAME,FOLLOW_NAME_in_expression615); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME59);



                    // AST REWRITE
                    // elements: NAME
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 100:9: -> ^( IDEXPRESSION NAME )
                    {
                        dbg.location(100,12);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:12: ^( IDEXPRESSION NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(100,14);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IDEXPRESSION, "IDEXPRESSION"), root_1);

                        dbg.location(100,27);
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:35: f1= mathExpression SINGLEEQUALS f2= mathExpression
                    {
                    dbg.location(100,37);
                    pushFollow(FOLLOW_mathExpression_in_expression629);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());
                    dbg.location(100,53);
                    SINGLEEQUALS60=(Token)match(input,SINGLEEQUALS,FOLLOW_SINGLEEQUALS_in_expression631); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SINGLEEQUALS.add(SINGLEEQUALS60);

                    dbg.location(100,68);
                    pushFollow(FOLLOW_mathExpression_in_expression635);
                    f2=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f2.getTree());


                    // AST REWRITE
                    // elements: f1, f2
                    // token labels: 
                    // rule labels: retval, f1, f2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);
                    RewriteRuleSubtreeStream stream_f2=new RewriteRuleSubtreeStream(adaptor,"rule f2",f2!=null?f2.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 100:84: -> ^( COMPAREEXPRESSION $f1 EQUALS $f2)
                    {
                        dbg.location(100,88);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:88: ^( COMPAREEXPRESSION $f1 EQUALS $f2)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(100,90);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(COMPAREEXPRESSION, "COMPAREEXPRESSION"), root_1);

                        dbg.location(100,108);
                        adaptor.addChild(root_1, stream_f1.nextTree());
                        dbg.location(100,112);
                        adaptor.addChild(root_1, (Object)adaptor.create(EQUALS, "EQUALS"));
                        dbg.location(100,119);
                        adaptor.addChild(root_1, stream_f2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:126: f1= mathExpression COMPAREOP f2= mathExpression
                    {
                    dbg.location(100,128);
                    pushFollow(FOLLOW_mathExpression_in_expression656);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());
                    dbg.location(100,144);
                    COMPAREOP61=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression658); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COMPAREOP.add(COMPAREOP61);

                    dbg.location(100,156);
                    pushFollow(FOLLOW_mathExpression_in_expression662);
                    f2=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f2.getTree());


                    // AST REWRITE
                    // elements: COMPAREOP, f1, f2
                    // token labels: 
                    // rule labels: retval, f1, f2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);
                    RewriteRuleSubtreeStream stream_f2=new RewriteRuleSubtreeStream(adaptor,"rule f2",f2!=null?f2.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 100:172: -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
                    {
                        dbg.location(100,175);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:175: ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(100,177);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(COMPAREEXPRESSION, "COMPAREEXPRESSION"), root_1);

                        dbg.location(100,195);
                        adaptor.addChild(root_1, stream_f1.nextTree());
                        dbg.location(100,199);
                        adaptor.addChild(root_1, stream_COMPAREOP.nextNode());
                        dbg.location(100,209);
                        adaptor.addChild(root_1, stream_f2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(101, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "expression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "expression"

    public static class mathExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mathExpression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:1: mathExpression : left= term (op= ( MULT | DIVISION ) right= term )* ;
    public final SaseParser.mathExpression_return mathExpression() throws RecognitionException {
        SaseParser.mathExpression_return retval = new SaseParser.mathExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;
        SaseParser.term_return left = null;

        SaseParser.term_return right = null;


        Object op_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "mathExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(103, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:15: (left= term (op= ( MULT | DIVISION ) right= term )* )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:17: left= term (op= ( MULT | DIVISION ) right= term )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(103,21);
            pushFollow(FOLLOW_term_in_mathExpression688);
            left=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, left.getTree());
            dbg.location(103,27);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:27: (op= ( MULT | DIVISION ) right= term )*
            try { dbg.enterSubRule(17);

            loop17:
            do {
                int alt17=2;
                try { dbg.enterDecision(17);

                int LA17_0 = input.LA(1);

                if ( ((LA17_0>=DIVISION && LA17_0<=MULT)) ) {
                    alt17=1;
                }


                } finally {dbg.exitDecision(17);}

                switch (alt17) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:28: op= ( MULT | DIVISION ) right= term
            	    {
            	    dbg.location(103,30);
            	    op=(Token)input.LT(1);
            	    if ( (input.LA(1)>=DIVISION && input.LA(1)<=MULT) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(op));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(103,52);
            	    pushFollow(FOLLOW_term_in_mathExpression701);
            	    right=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, right.getTree());

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);
            } finally {dbg.exitSubRule(17);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
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
            dbg.exitRule(getGrammarFileName(), "mathExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "mathExpression"

    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:107:1: term : ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) | value );
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token aName=null;
        Token member=null;
        Token POINT64=null;
        Token NAME65=null;
        Token POINT66=null;
        SaseParser.aggregation_return aggregation62 = null;

        SaseParser.kAttributeUsage_return kAttributeUsage63 = null;

        SaseParser.value_return value67 = null;


        Object aName_tree=null;
        Object member_tree=null;
        Object POINT64_tree=null;
        Object NAME65_tree=null;
        Object POINT66_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_kAttributeUsage=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeUsage");
        try { dbg.enterRule(getGrammarFileName(), "term");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(107, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:107:6: ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) | value )
            int alt18=4;
            try { dbg.enterDecision(18);

            switch ( input.LA(1) ) {
            case AGGREGATEOP:
                {
                alt18=1;
                }
                break;
            case NAME:
                {
                int LA18_2 = input.LA(2);

                if ( ((LA18_2>=FIRST && LA18_2<=PREVIOUS)||LA18_2==LAST) ) {
                    alt18=2;
                }
                else if ( (LA18_2==POINT) ) {
                    alt18=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 2, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                }
                break;
            case NUMBER:
            case STRING_LITERAL:
                {
                alt18=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(18);}

            switch (alt18) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:107:8: aggregation
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(107,8);
                    pushFollow(FOLLOW_aggregation_in_term723);
                    aggregation62=aggregation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregation62.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:3: kAttributeUsage POINT NAME
                    {
                    dbg.location(108,3);
                    pushFollow(FOLLOW_kAttributeUsage_in_term729);
                    kAttributeUsage63=kAttributeUsage();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeUsage.add(kAttributeUsage63.getTree());
                    dbg.location(108,19);
                    POINT64=(Token)match(input,POINT,FOLLOW_POINT_in_term731); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT64);

                    dbg.location(108,25);
                    NAME65=(Token)match(input,NAME,FOLLOW_NAME_in_term733); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME65);



                    // AST REWRITE
                    // elements: kAttributeUsage, NAME
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 108:30: -> ^( KMEMBER kAttributeUsage NAME )
                    {
                        dbg.location(108,33);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:33: ^( KMEMBER kAttributeUsage NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(108,35);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KMEMBER, "KMEMBER"), root_1);

                        dbg.location(108,43);
                        adaptor.addChild(root_1, stream_kAttributeUsage.nextTree());
                        dbg.location(108,59);
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:109:3: aName= NAME POINT member= NAME
                    {
                    dbg.location(109,8);
                    aName=(Token)match(input,NAME,FOLLOW_NAME_in_term750); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(aName);

                    dbg.location(109,14);
                    POINT66=(Token)match(input,POINT,FOLLOW_POINT_in_term752); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT66);

                    dbg.location(109,26);
                    member=(Token)match(input,NAME,FOLLOW_NAME_in_term756); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(member);



                    // AST REWRITE
                    // elements: member, aName
                    // token labels: member, aName
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_member=new RewriteRuleTokenStream(adaptor,"token member",member);
                    RewriteRuleTokenStream stream_aName=new RewriteRuleTokenStream(adaptor,"token aName",aName);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 109:32: -> ^( MEMBER $aName $member)
                    {
                        dbg.location(109,35);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:109:35: ^( MEMBER $aName $member)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(109,37);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MEMBER, "MEMBER"), root_1);

                        dbg.location(109,44);
                        adaptor.addChild(root_1, stream_aName.nextNode());
                        dbg.location(109,51);
                        adaptor.addChild(root_1, stream_member.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:3: value
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(110,3);
                    pushFollow(FOLLOW_value_in_term773);
                    value67=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value67.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(111, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "term");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "term"

    public static class aggregation_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "aggregation"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:114:1: aggregation : AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member) ;
    public final SaseParser.aggregation_return aggregation() throws RecognitionException {
        SaseParser.aggregation_return retval = new SaseParser.aggregation_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token var=null;
        Token member=null;
        Token AGGREGATEOP68=null;
        Token LBRACKET69=null;
        Token ALLTOPREVIOUS70=null;
        Token POINT71=null;
        Token RBRACKET72=null;

        Object var_tree=null;
        Object member_tree=null;
        Object AGGREGATEOP68_tree=null;
        Object LBRACKET69_tree=null;
        Object ALLTOPREVIOUS70_tree=null;
        Object POINT71_tree=null;
        Object RBRACKET72_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_AGGREGATEOP=new RewriteRuleTokenStream(adaptor,"token AGGREGATEOP");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleTokenStream stream_ALLTOPREVIOUS=new RewriteRuleTokenStream(adaptor,"token ALLTOPREVIOUS");

        try { dbg.enterRule(getGrammarFileName(), "aggregation");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(114, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:115:2: ( AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:115:4: AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET
            {
            dbg.location(115,4);
            AGGREGATEOP68=(Token)match(input,AGGREGATEOP,FOLLOW_AGGREGATEOP_in_aggregation787); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_AGGREGATEOP.add(AGGREGATEOP68);

            dbg.location(115,16);
            LBRACKET69=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation789); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET69);

            dbg.location(115,28);
            var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation793); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(var);

            dbg.location(115,34);
            ALLTOPREVIOUS70=(Token)match(input,ALLTOPREVIOUS,FOLLOW_ALLTOPREVIOUS_in_aggregation795); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ALLTOPREVIOUS.add(ALLTOPREVIOUS70);

            dbg.location(115,48);
            POINT71=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation797); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_POINT.add(POINT71);

            dbg.location(115,60);
            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation801); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(member);

            dbg.location(115,66);
            RBRACKET72=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation803); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET72);



            // AST REWRITE
            // elements: AGGREGATEOP, var, member, ALLTOPREVIOUS
            // token labels: member, var
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_member=new RewriteRuleTokenStream(adaptor,"token member",member);
            RewriteRuleTokenStream stream_var=new RewriteRuleTokenStream(adaptor,"token var",var);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 115:75: -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member)
            {
                dbg.location(115,78);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:115:78: ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member)
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(115,80);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(AGGREGATION, "AGGREGATION"), root_1);

                dbg.location(115,92);
                adaptor.addChild(root_1, stream_AGGREGATEOP.nextNode());
                dbg.location(115,104);
                adaptor.addChild(root_1, stream_var.nextNode());
                dbg.location(115,109);
                adaptor.addChild(root_1, stream_ALLTOPREVIOUS.nextNode());
                dbg.location(115,123);
                adaptor.addChild(root_1, stream_member.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(116, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "aggregation");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "aggregation"

    public static class value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:118:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set73=null;

        Object set73_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "value");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(118, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:118:8: ( NUMBER | STRING_LITERAL )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(118,8);
            set73=(Token)input.LT(1);
            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set73));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(118, 34);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "value");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "value"

    // Delegated rules


    protected DFA16 dfa16 = new DFA16(this);
    static final String DFA16_eotS =
        "\47\uffff";
    static final String DFA16_eofS =
        "\1\uffff\1\11\45\uffff";
    static final String DFA16_minS =
        "\1\22\1\10\1\43\1\34\4\33\1\22\1\uffff\2\22\2\uffff\1\22\1\34\1"+
        "\21\1\43\1\16\2\34\1\33\1\22\4\33\2\22\1\21\1\22\1\34\1\44\1\33"+
        "\2\34\1\22\1\44\1\34";
    static final String DFA16_maxS =
        "\1\53\1\33\1\43\1\37\4\33\1\22\1\uffff\1\22\1\53\2\uffff\1\22\1"+
        "\37\1\21\1\43\1\33\2\37\1\33\1\22\4\33\2\22\1\21\1\22\1\37\1\44"+
        "\1\33\2\37\1\22\1\44\1\37";
    static final String DFA16_acceptS =
        "\11\uffff\1\1\2\uffff\1\3\1\2\31\uffff";
    static final String DFA16_specialS =
        "\47\uffff}>";
    static final String[] DFA16_transitionS = {
            "\1\1\5\uffff\1\2\16\uffff\1\3\3\uffff\1\3",
            "\2\11\2\uffff\2\11\1\5\1\4\1\6\2\uffff\1\7\7\uffff\1\10",
            "\1\12",
            "\2\13\1\14\1\15",
            "\1\16",
            "\1\16",
            "\1\16",
            "\1\16",
            "\1\17",
            "",
            "\1\20",
            "\1\22\5\uffff\1\21\16\uffff\1\23\3\uffff\1\23",
            "",
            "",
            "\1\24",
            "\2\13\1\14\1\15",
            "\1\25",
            "\1\26",
            "\1\30\1\27\1\31\2\uffff\1\32\7\uffff\1\33",
            "\2\13\1\14\1\15",
            "\2\13\1\14\1\15",
            "\1\34",
            "\1\35",
            "\1\36",
            "\1\36",
            "\1\36",
            "\1\36",
            "\1\37",
            "\1\40",
            "\1\41",
            "\1\42",
            "\2\13\1\14\1\15",
            "\1\43",
            "\1\44",
            "\2\13\1\14\1\15",
            "\2\13\1\14\1\15",
            "\1\45",
            "\1\46",
            "\2\13\1\14\1\15"
    };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "99:1: expression : ( NAME -> ^( IDEXPRESSION NAME ) | f1= mathExpression SINGLEEQUALS f2= mathExpression -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= mathExpression COMPAREOP f2= mathExpression -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
    }
 

    public static final BitSet FOLLOW_createPart_in_query120 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_patternPart_in_query124 = new BitSet(new long[]{0x0000000000000382L});
    public static final BitSet FOLLOW_wherePart_in_query127 = new BitSet(new long[]{0x0000000000000302L});
    public static final BitSet FOLLOW_withinPart_in_query132 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_returnPart_in_query137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_createPart151 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STREAM_in_createPart153 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NAME_in_createPart155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart175 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart177 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_TIMEUNIT_in_withinPart179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart201 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_wherePart1_in_wherePart203 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart205 = new BitSet(new long[]{0x0000088001040000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart207 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart226 = new BitSet(new long[]{0x0000088001040000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart228 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart249 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnPart272 = new BitSet(new long[]{0x0000088001040000L});
    public static final BitSet FOLLOW_term_in_returnPart274 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_COMMA_in_returnPart277 = new BitSet(new long[]{0x0000088001040000L});
    public static final BitSet FOLLOW_term_in_returnPart279 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_SEQ_in_patternDecl302 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_LBRACKET_in_patternDecl304 = new BitSet(new long[]{0x0000000A00040000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl306 = new BitSet(new long[]{0x0000001400000000L});
    public static final BitSet FOLLOW_COMMA_in_patternDecl309 = new BitSet(new long[]{0x0000000A00040000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl311 = new BitSet(new long[]{0x0000001400000000L});
    public static final BitSet FOLLOW_RBRACKET_in_patternDecl315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_pItem340 = new BitSet(new long[]{0x0000000A00040000L});
    public static final BitSet FOLLOW_LBRACKET_in_pItem344 = new BitSet(new long[]{0x0000000A00040000L});
    public static final BitSet FOLLOW_typeName_in_pItem350 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_attributeName_in_pItem354 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_RBRACKET_in_pItem356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeName383 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_PLUS_in_typeName387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_METHOD_in_wherePart1430 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_LBRACKET_in_wherePart1432 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_parameterList_in_wherePart1434 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_wherePart1436 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList459 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList461 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList463 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName501 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName504 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage548 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage556 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_FIRST_in_kAttributeUsage558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage563 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_PREVIOUS_in_kAttributeUsage565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage570 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LAST_in_kAttributeUsage572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions584 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_AND_in_whereExpressions587 = new BitSet(new long[]{0x0000088001040000L});
    public static final BitSet FOLLOW_expression_in_whereExpressions589 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_NAME_in_expression615 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression629 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_SINGLEEQUALS_in_expression631 = new BitSet(new long[]{0x0000088001040000L});
    public static final BitSet FOLLOW_mathExpression_in_expression635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression656 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression658 = new BitSet(new long[]{0x0000088001040000L});
    public static final BitSet FOLLOW_mathExpression_in_expression662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_mathExpression688 = new BitSet(new long[]{0x0000000030000002L});
    public static final BitSet FOLLOW_set_in_mathExpression693 = new BitSet(new long[]{0x0000088001040000L});
    public static final BitSet FOLLOW_term_in_mathExpression701 = new BitSet(new long[]{0x0000000030000002L});
    public static final BitSet FOLLOW_aggregation_in_term723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_term729 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_POINT_in_term731 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NAME_in_term733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_term750 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_POINT_in_term752 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NAME_in_term756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AGGREGATEOP_in_aggregation787 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation789 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NAME_in_aggregation793 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ALLTOPREVIOUS_in_aggregation795 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation797 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NAME_in_aggregation801 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value0 = new BitSet(new long[]{0x0000000000000002L});

}