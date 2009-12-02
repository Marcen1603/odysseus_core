// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2009-12-02 12:38:05
 
	package de.uniol.inf.is.odysseus.cep.sase; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.debug.*;
import java.io.IOException;

import org.antlr.runtime.tree.*;

public class SaseParser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "FROM", "HAVING", "PATTERN", "WHERE", "WITHIN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "BBRACKETLEFT", "BBRACKETRIGHT", "FIRST", "CURRENT", "PREVIOUS", "ALLTOPREVIOUS", "NAME", "LAST", "TIMEUNIT", "SKIP_METHOD", "PLUS", "MINUS", "POINT", "MULT", "COMPAREOP", "AGGREGATEOP", "NOT", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "STATE", "KTYPE", "TYPE", "WHERESTRAT", "WHEREEXPRESSION", "EXPRESSION", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "MEMBERACCESS"
    };
    public static final int WHERE=7;
    public static final int POINT=25;
    public static final int LETTER=37;
    public static final int ATTRIBUTE=51;
    public static final int FLOAT=34;
    public static final int NOT=29;
    public static final int AND=12;
    public static final int SPACE=40;
    public static final int EOF=-1;
    public static final int TYPE=47;
    public static final int LBRACKET=31;
    public static final int PATTERN=6;
    public static final int NAME=19;
    public static final int STRING_LITERAL=39;
    public static final int LEFTCURLY=10;
    public static final int COMMA=30;
    public static final int SEQ=9;
    public static final int PREVIOUS=17;
    public static final int KATTRIBUTE=52;
    public static final int RIGHTCURLY=11;
    public static final int PLUS=23;
    public static final int DIGIT=36;
    public static final int LAST=20;
    public static final int RBRACKET=32;
    public static final int EXPRESSION=50;
    public static final int BBRACKETRIGHT=14;
    public static final int INTEGER=33;
    public static final int STATE=45;
    public static final int ALLTOPREVIOUS=18;
    public static final int TIMEUNIT=21;
    public static final int SKIP_METHOD=22;
    public static final int NUMBER=35;
    public static final int PARAMLIST=53;
    public static final int WHITESPACE=44;
    public static final int HAVING=5;
    public static final int MINUS=24;
    public static final int MULT=26;
    public static final int AGGREGATEOP=28;
    public static final int CURRENT=16;
    public static final int WHEREEXPRESSION=49;
    public static final int NEWLINE=43;
    public static final int NONCONTROL_CHAR=38;
    public static final int BBRACKETLEFT=13;
    public static final int WHERESTRAT=48;
    public static final int WITHIN=8;
    public static final int MEMBERACCESS=54;
    public static final int KTYPE=46;
    public static final int LOWER=41;
    public static final int FROM=4;
    public static final int COMPAREOP=27;
    public static final int UPPER=42;
    public static final int FIRST=15;

    // delegates
    // delegators

    public static final String[] ruleNames = new String[] {
        "invalidRule", "expression", "kAttributeName", "fromPart", "parameterList", 
        "withinPart", "typeName", "patternDecl", "pItem", "kAttributeUsage", 
        "term", "sAttributeName", "wherePart", "value", "attributeName", 
        "wherePart1", "whereExpressions", "query", "patternPart"
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:26:1: query : ( fromPart )? patternPart ( wherePart )? ( withinPart )? ;
    public final SaseParser.query_return query() throws RecognitionException {
        SaseParser.query_return retval = new SaseParser.query_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.fromPart_return fromPart1 = null;

        SaseParser.patternPart_return patternPart2 = null;

        SaseParser.wherePart_return wherePart3 = null;

        SaseParser.withinPart_return withinPart4 = null;



        try { dbg.enterRule(getGrammarFileName(), "query");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(26, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:26:7: ( ( fromPart )? patternPart ( wherePart )? ( withinPart )? )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:26:10: ( fromPart )? patternPart ( wherePart )? ( withinPart )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(26,10);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:26:10: ( fromPart )?
            int alt1=2;
            try { dbg.enterSubRule(1);
            try { dbg.enterDecision(1);

            int LA1_0 = input.LA(1);

            if ( (LA1_0==FROM) ) {
                alt1=1;
            }
            } finally {dbg.exitDecision(1);}

            switch (alt1) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:26:11: fromPart
                    {
                    dbg.location(26,11);
                    pushFollow(FOLLOW_fromPart_in_query89);
                    fromPart1=fromPart();

                    state._fsp--;

                    adaptor.addChild(root_0, fromPart1.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(1);}

            dbg.location(26,22);
            pushFollow(FOLLOW_patternPart_in_query93);
            patternPart2=patternPart();

            state._fsp--;

            adaptor.addChild(root_0, patternPart2.getTree());
            dbg.location(26,34);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:26:34: ( wherePart )?
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

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:26:35: wherePart
                    {
                    dbg.location(26,35);
                    pushFollow(FOLLOW_wherePart_in_query96);
                    wherePart3=wherePart();

                    state._fsp--;

                    adaptor.addChild(root_0, wherePart3.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(2);}

            dbg.location(26,47);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:26:47: ( withinPart )?
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

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:26:48: withinPart
                    {
                    dbg.location(26,48);
                    pushFollow(FOLLOW_withinPart_in_query101);
                    withinPart4=withinPart();

                    state._fsp--;

                    adaptor.addChild(root_0, withinPart4.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(3);}


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
        dbg.location(27, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "query");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "query"

    public static class fromPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fromPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:1: fromPart : FROM NAME ( COMMA NAME )* ;
    public final SaseParser.fromPart_return fromPart() throws RecognitionException {
        SaseParser.fromPart_return retval = new SaseParser.fromPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token FROM5=null;
        Token NAME6=null;
        Token COMMA7=null;
        Token NAME8=null;

        Object FROM5_tree=null;
        Object NAME6_tree=null;
        Object COMMA7_tree=null;
        Object NAME8_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "fromPart");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(29, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:9: ( FROM NAME ( COMMA NAME )* )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:11: FROM NAME ( COMMA NAME )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(29,11);
            FROM5=(Token)match(input,FROM,FOLLOW_FROM_in_fromPart113); 
            FROM5_tree = (Object)adaptor.create(FROM5);
            adaptor.addChild(root_0, FROM5_tree);

            dbg.location(29,16);
            NAME6=(Token)match(input,NAME,FOLLOW_NAME_in_fromPart115); 
            NAME6_tree = (Object)adaptor.create(NAME6);
            adaptor.addChild(root_0, NAME6_tree);

            dbg.location(29,21);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:21: ( COMMA NAME )*
            try { dbg.enterSubRule(4);

            loop4:
            do {
                int alt4=2;
                try { dbg.enterDecision(4);

                int LA4_0 = input.LA(1);

                if ( (LA4_0==COMMA) ) {
                    alt4=1;
                }


                } finally {dbg.exitDecision(4);}

                switch (alt4) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:22: COMMA NAME
            	    {
            	    dbg.location(29,22);
            	    COMMA7=(Token)match(input,COMMA,FOLLOW_COMMA_in_fromPart118); 
            	    COMMA7_tree = (Object)adaptor.create(COMMA7);
            	    adaptor.addChild(root_0, COMMA7_tree);

            	    dbg.location(29,28);
            	    NAME8=(Token)match(input,NAME,FOLLOW_NAME_in_fromPart120); 
            	    NAME8_tree = (Object)adaptor.create(NAME8);
            	    adaptor.addChild(root_0, NAME8_tree);


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);
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
        dbg.location(30, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "fromPart");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "fromPart"

    public static class withinPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "withinPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:1: withinPart : WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) ;
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
        dbg.location(32, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:2: ( WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:4: WITHIN NUMBER TIMEUNIT
            {
            dbg.location(33,4);
            WITHIN9=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart135);  
            stream_WITHIN.add(WITHIN9);

            dbg.location(33,11);
            NUMBER10=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart137);  
            stream_NUMBER.add(NUMBER10);

            dbg.location(33,18);
            TIMEUNIT11=(Token)match(input,TIMEUNIT,FOLLOW_TIMEUNIT_in_withinPart139);  
            stream_TIMEUNIT.add(TIMEUNIT11);



            // AST REWRITE
            // elements: NUMBER, WITHIN, TIMEUNIT
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 33:27: -> ^( WITHIN NUMBER TIMEUNIT )
            {
                dbg.location(33,30);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:30: ^( WITHIN NUMBER TIMEUNIT )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(33,32);
                root_1 = (Object)adaptor.becomeRoot(stream_WITHIN.nextNode(), root_1);

                dbg.location(33,39);
                adaptor.addChild(root_1, stream_NUMBER.nextNode());
                dbg.location(33,46);
                adaptor.addChild(root_1, stream_TIMEUNIT.nextNode());

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
        dbg.location(34, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:36:1: wherePart : ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
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
        dbg.location(36, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:2: ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt5=2;
            try { dbg.enterDecision(5);

            int LA5_0 = input.LA(1);

            if ( (LA5_0==WHERE) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==NAME) ) {
                    alt5=2;
                }
                else if ( (LA5_1==SKIP_METHOD) ) {
                    alt5=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(5);}

            switch (alt5) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:4: WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    dbg.location(37,4);
                    WHERE12=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart161);  
                    stream_WHERE.add(WHERE12);

                    dbg.location(37,10);
                    pushFollow(FOLLOW_wherePart1_in_wherePart163);
                    wherePart113=wherePart1();

                    state._fsp--;

                    stream_wherePart1.add(wherePart113.getTree());
                    dbg.location(37,21);
                    LEFTCURLY14=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart165);  
                    stream_LEFTCURLY.add(LEFTCURLY14);

                    dbg.location(37,31);
                    pushFollow(FOLLOW_whereExpressions_in_wherePart167);
                    whereExpressions15=whereExpressions();

                    state._fsp--;

                    stream_whereExpressions.add(whereExpressions15.getTree());
                    dbg.location(37,48);
                    RIGHTCURLY16=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart169);  
                    stream_RIGHTCURLY.add(RIGHTCURLY16);



                    // AST REWRITE
                    // elements: wherePart1, WHERE, whereExpressions
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 37:59: -> ^( WHERE wherePart1 whereExpressions )
                    {
                        dbg.location(37,62);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:62: ^( WHERE wherePart1 whereExpressions )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(37,64);
                        root_1 = (Object)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

                        dbg.location(37,70);
                        adaptor.addChild(root_1, stream_wherePart1.nextTree());
                        dbg.location(37,81);
                        adaptor.addChild(root_1, stream_whereExpressions.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:38:4: WHERE whereExpressions
                    {
                    dbg.location(38,4);
                    WHERE17=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart186);  
                    stream_WHERE.add(WHERE17);

                    dbg.location(38,10);
                    pushFollow(FOLLOW_whereExpressions_in_wherePart188);
                    whereExpressions18=whereExpressions();

                    state._fsp--;

                    stream_whereExpressions.add(whereExpressions18.getTree());


                    // AST REWRITE
                    // elements: whereExpressions, WHERE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 38:27: -> ^( WHERE whereExpressions )
                    {
                        dbg.location(38,30);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:38:30: ^( WHERE whereExpressions )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(38,32);
                        root_1 = (Object)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

                        dbg.location(38,38);
                        adaptor.addChild(root_1, stream_whereExpressions.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

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
        dbg.location(39, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:41:1: patternPart : PATTERN patternDecl -> ^( PATTERN patternDecl ) ;
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
        dbg.location(41, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:42:2: ( PATTERN patternDecl -> ^( PATTERN patternDecl ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:42:4: PATTERN patternDecl
            {
            dbg.location(42,4);
            PATTERN19=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart209);  
            stream_PATTERN.add(PATTERN19);

            dbg.location(42,12);
            pushFollow(FOLLOW_patternDecl_in_patternPart211);
            patternDecl20=patternDecl();

            state._fsp--;

            stream_patternDecl.add(patternDecl20.getTree());


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
            // 42:24: -> ^( PATTERN patternDecl )
            {
                dbg.location(42,27);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:42:27: ^( PATTERN patternDecl )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(42,29);
                root_1 = (Object)adaptor.becomeRoot(stream_PATTERN.nextNode(), root_1);

                dbg.location(42,37);
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
        dbg.location(43, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:46:1: patternDecl : SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) ;
    public final SaseParser.patternDecl_return patternDecl() throws RecognitionException {
        SaseParser.patternDecl_return retval = new SaseParser.patternDecl_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEQ21=null;
        Token LBRACKET22=null;
        Token COMMA24=null;
        Token RBRACKET26=null;
        SaseParser.pItem_return pItem23 = null;

        SaseParser.pItem_return pItem25 = null;


        Object SEQ21_tree=null;
        Object LBRACKET22_tree=null;
        Object COMMA24_tree=null;
        Object RBRACKET26_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SEQ=new RewriteRuleTokenStream(adaptor,"token SEQ");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_pItem=new RewriteRuleSubtreeStream(adaptor,"rule pItem");
        try { dbg.enterRule(getGrammarFileName(), "patternDecl");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(46, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:2: ( SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:4: SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET
            {
            dbg.location(47,4);
            SEQ21=(Token)match(input,SEQ,FOLLOW_SEQ_in_patternDecl233);  
            stream_SEQ.add(SEQ21);

            dbg.location(47,8);
            LBRACKET22=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_patternDecl235);  
            stream_LBRACKET.add(LBRACKET22);

            dbg.location(47,17);
            pushFollow(FOLLOW_pItem_in_patternDecl237);
            pItem23=pItem();

            state._fsp--;

            stream_pItem.add(pItem23.getTree());
            dbg.location(47,23);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:23: ( COMMA pItem )*
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

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:24: COMMA pItem
            	    {
            	    dbg.location(47,24);
            	    COMMA24=(Token)match(input,COMMA,FOLLOW_COMMA_in_patternDecl240);  
            	    stream_COMMA.add(COMMA24);

            	    dbg.location(47,30);
            	    pushFollow(FOLLOW_pItem_in_patternDecl242);
            	    pItem25=pItem();

            	    state._fsp--;

            	    stream_pItem.add(pItem25.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);
            } finally {dbg.exitSubRule(6);}

            dbg.location(47,38);
            RBRACKET26=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_patternDecl246);  
            stream_RBRACKET.add(RBRACKET26);



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
            // 47:47: -> ^( SEQ ( pItem )* )
            {
                dbg.location(47,50);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:50: ^( SEQ ( pItem )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(47,52);
                root_1 = (Object)adaptor.becomeRoot(stream_SEQ.nextNode(), root_1);

                dbg.location(47,56);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:56: ( pItem )*
                while ( stream_pItem.hasNext() ) {
                    dbg.location(47,56);
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
        dbg.location(48, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:1: pItem : ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) ;
    public final SaseParser.pItem_return pItem() throws RecognitionException {
        SaseParser.pItem_return retval = new SaseParser.pItem_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NOT27=null;
        Token LBRACKET28=null;
        Token RBRACKET29=null;
        SaseParser.typeName_return type = null;

        SaseParser.attributeName_return variable = null;


        Object NOT27_tree=null;
        Object LBRACKET28_tree=null;
        Object RBRACKET29_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_typeName=new RewriteRuleSubtreeStream(adaptor,"rule typeName");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try { dbg.enterRule(getGrammarFileName(), "pItem");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(51, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:8: ( ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:10: ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )?
            {
            dbg.location(51,10);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:10: ( NOT )?
            int alt7=2;
            try { dbg.enterSubRule(7);
            try { dbg.enterDecision(7);

            int LA7_0 = input.LA(1);

            if ( (LA7_0==NOT) ) {
                alt7=1;
            }
            } finally {dbg.exitDecision(7);}

            switch (alt7) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:11: NOT
                    {
                    dbg.location(51,11);
                    NOT27=(Token)match(input,NOT,FOLLOW_NOT_in_pItem271);  
                    stream_NOT.add(NOT27);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(7);}

            dbg.location(51,17);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:17: ( LBRACKET )?
            int alt8=2;
            try { dbg.enterSubRule(8);
            try { dbg.enterDecision(8);

            int LA8_0 = input.LA(1);

            if ( (LA8_0==LBRACKET) ) {
                alt8=1;
            }
            } finally {dbg.exitDecision(8);}

            switch (alt8) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:17: LBRACKET
                    {
                    dbg.location(51,17);
                    LBRACKET28=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_pItem275);  
                    stream_LBRACKET.add(LBRACKET28);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(8);}

            dbg.location(51,32);
            pushFollow(FOLLOW_typeName_in_pItem281);
            type=typeName();

            state._fsp--;

            stream_typeName.add(type.getTree());
            dbg.location(51,50);
            pushFollow(FOLLOW_attributeName_in_pItem285);
            variable=attributeName();

            state._fsp--;

            stream_attributeName.add(variable.getTree());
            dbg.location(51,65);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:65: ( RBRACKET )?
            int alt9=2;
            try { dbg.enterSubRule(9);
            try { dbg.enterDecision(9);

            int LA9_0 = input.LA(1);

            if ( (LA9_0==RBRACKET) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==COMMA||LA9_1==RBRACKET) ) {
                    alt9=1;
                }
            }
            } finally {dbg.exitDecision(9);}

            switch (alt9) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:65: RBRACKET
                    {
                    dbg.location(51,65);
                    RBRACKET29=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_pItem287);  
                    stream_RBRACKET.add(RBRACKET29);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(9);}



            // AST REWRITE
            // elements: NOT, variable, type
            // token labels: 
            // rule labels: retval, type, variable
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type",type!=null?type.tree:null);
            RewriteRuleSubtreeStream stream_variable=new RewriteRuleSubtreeStream(adaptor,"rule variable",variable!=null?variable.tree:null);

            root_0 = (Object)adaptor.nil();
            // 51:75: -> ^( STATE $type $variable ( NOT )? )
            {
                dbg.location(51,78);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:78: ^( STATE $type $variable ( NOT )? )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(51,80);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STATE, "STATE"), root_1);

                dbg.location(51,86);
                adaptor.addChild(root_1, stream_type.nextTree());
                dbg.location(51,92);
                adaptor.addChild(root_1, stream_variable.nextTree());
                dbg.location(51,102);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:102: ( NOT )?
                if ( stream_NOT.hasNext() ) {
                    dbg.location(51,102);
                    adaptor.addChild(root_1, stream_NOT.nextNode());

                }
                stream_NOT.reset();

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
        dbg.location(52, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:1: typeName : ( NAME op= PLUS -> ^( KTYPE NAME $op) | NAME -> ^( TYPE NAME ) );
    public final SaseParser.typeName_return typeName() throws RecognitionException {
        SaseParser.typeName_return retval = new SaseParser.typeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;
        Token NAME30=null;
        Token NAME31=null;

        Object op_tree=null;
        Object NAME30_tree=null;
        Object NAME31_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");

        try { dbg.enterRule(getGrammarFileName(), "typeName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(54, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:9: ( NAME op= PLUS -> ^( KTYPE NAME $op) | NAME -> ^( TYPE NAME ) )
            int alt10=2;
            try { dbg.enterDecision(10);

            int LA10_0 = input.LA(1);

            if ( (LA10_0==NAME) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==PLUS) ) {
                    alt10=1;
                }
                else if ( (LA10_1==NAME) ) {
                    alt10=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(10);}

            switch (alt10) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:11: NAME op= PLUS
                    {
                    dbg.location(54,11);
                    NAME30=(Token)match(input,NAME,FOLLOW_NAME_in_typeName314);  
                    stream_NAME.add(NAME30);

                    dbg.location(54,18);
                    op=(Token)match(input,PLUS,FOLLOW_PLUS_in_typeName318);  
                    stream_PLUS.add(op);



                    // AST REWRITE
                    // elements: NAME, op
                    // token labels: op
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 54:24: -> ^( KTYPE NAME $op)
                    {
                        dbg.location(54,27);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:27: ^( KTYPE NAME $op)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(54,29);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KTYPE, "KTYPE"), root_1);

                        dbg.location(54,35);
                        adaptor.addChild(root_1, stream_NAME.nextNode());
                        dbg.location(54,40);
                        adaptor.addChild(root_1, stream_op.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:47: NAME
                    {
                    dbg.location(54,47);
                    NAME31=(Token)match(input,NAME,FOLLOW_NAME_in_typeName333);  
                    stream_NAME.add(NAME31);



                    // AST REWRITE
                    // elements: NAME
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 54:52: -> ^( TYPE NAME )
                    {
                        dbg.location(54,55);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:55: ^( TYPE NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(54,57);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TYPE, "TYPE"), root_1);

                        dbg.location(54,62);
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

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
        dbg.location(55, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:1: wherePart1 : SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) ;
    public final SaseParser.wherePart1_return wherePart1() throws RecognitionException {
        SaseParser.wherePart1_return retval = new SaseParser.wherePart1_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SKIP_METHOD32=null;
        Token LBRACKET33=null;
        Token RBRACKET35=null;
        SaseParser.parameterList_return parameterList34 = null;


        Object SKIP_METHOD32_tree=null;
        Object LBRACKET33_tree=null;
        Object RBRACKET35_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SKIP_METHOD=new RewriteRuleTokenStream(adaptor,"token SKIP_METHOD");
        RewriteRuleSubtreeStream stream_parameterList=new RewriteRuleSubtreeStream(adaptor,"rule parameterList");
        try { dbg.enterRule(getGrammarFileName(), "wherePart1");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(57, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:2: ( SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:4: SKIP_METHOD LBRACKET parameterList RBRACKET
            {
            dbg.location(58,4);
            SKIP_METHOD32=(Token)match(input,SKIP_METHOD,FOLLOW_SKIP_METHOD_in_wherePart1353);  
            stream_SKIP_METHOD.add(SKIP_METHOD32);

            dbg.location(58,16);
            LBRACKET33=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_wherePart1355);  
            stream_LBRACKET.add(LBRACKET33);

            dbg.location(58,25);
            pushFollow(FOLLOW_parameterList_in_wherePart1357);
            parameterList34=parameterList();

            state._fsp--;

            stream_parameterList.add(parameterList34.getTree());
            dbg.location(58,39);
            RBRACKET35=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_wherePart1359);  
            stream_RBRACKET.add(RBRACKET35);



            // AST REWRITE
            // elements: parameterList, SKIP_METHOD
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 58:48: -> ^( WHERESTRAT SKIP_METHOD parameterList )
            {
                dbg.location(58,51);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:51: ^( WHERESTRAT SKIP_METHOD parameterList )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(58,53);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHERESTRAT, "WHERESTRAT"), root_1);

                dbg.location(58,64);
                adaptor.addChild(root_1, stream_SKIP_METHOD.nextNode());
                dbg.location(58,76);
                adaptor.addChild(root_1, stream_parameterList.nextTree());

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
        dbg.location(59, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
    public final SaseParser.parameterList_return parameterList() throws RecognitionException {
        SaseParser.parameterList_return retval = new SaseParser.parameterList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token COMMA37=null;
        SaseParser.attributeName_return attributeName36 = null;

        SaseParser.attributeName_return attributeName38 = null;


        Object COMMA37_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try { dbg.enterRule(getGrammarFileName(), "parameterList");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(61, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:4: attributeName ( COMMA attributeName )*
            {
            dbg.location(62,4);
            pushFollow(FOLLOW_attributeName_in_parameterList382);
            attributeName36=attributeName();

            state._fsp--;

            stream_attributeName.add(attributeName36.getTree());
            dbg.location(62,17);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:17: ( COMMA attributeName )*
            try { dbg.enterSubRule(11);

            loop11:
            do {
                int alt11=2;
                try { dbg.enterDecision(11);

                int LA11_0 = input.LA(1);

                if ( (LA11_0==COMMA) ) {
                    alt11=1;
                }


                } finally {dbg.exitDecision(11);}

                switch (alt11) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:18: COMMA attributeName
            	    {
            	    dbg.location(62,18);
            	    COMMA37=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList384);  
            	    stream_COMMA.add(COMMA37);

            	    dbg.location(62,24);
            	    pushFollow(FOLLOW_attributeName_in_parameterList386);
            	    attributeName38=attributeName();

            	    state._fsp--;

            	    stream_attributeName.add(attributeName38.getTree());

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);
            } finally {dbg.exitSubRule(11);}



            // AST REWRITE
            // elements: attributeName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 62:40: -> ^( PARAMLIST ( attributeName )* )
            {
                dbg.location(62,43);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:43: ^( PARAMLIST ( attributeName )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(62,45);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PARAMLIST, "PARAMLIST"), root_1);

                dbg.location(62,55);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:55: ( attributeName )*
                while ( stream_attributeName.hasNext() ) {
                    dbg.location(62,55);
                    adaptor.addChild(root_1, stream_attributeName.nextTree());

                }
                stream_attributeName.reset();

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
        dbg.location(63, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:1: attributeName : ( kAttributeName | sAttributeName );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.kAttributeName_return kAttributeName39 = null;

        SaseParser.sAttributeName_return sAttributeName40 = null;



        try { dbg.enterRule(getGrammarFileName(), "attributeName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(65, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:2: ( kAttributeName | sAttributeName )
            int alt12=2;
            try { dbg.enterDecision(12);

            int LA12_0 = input.LA(1);

            if ( (LA12_0==NAME) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==BBRACKETLEFT) ) {
                    alt12=1;
                }
                else if ( (LA12_1==COMMA||LA12_1==RBRACKET) ) {
                    alt12=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(12);}

            switch (alt12) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:5: kAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(66,5);
                    pushFollow(FOLLOW_kAttributeName_in_attributeName410);
                    kAttributeName39=kAttributeName();

                    state._fsp--;

                    adaptor.addChild(root_0, kAttributeName39.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:20: sAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(66,20);
                    pushFollow(FOLLOW_sAttributeName_in_attributeName412);
                    sAttributeName40=sAttributeName();

                    state._fsp--;

                    adaptor.addChild(root_0, sAttributeName40.getTree());

                    }
                    break;

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
        dbg.location(67, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) ;
    public final SaseParser.kAttributeName_return kAttributeName() throws RecognitionException {
        SaseParser.kAttributeName_return retval = new SaseParser.kAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME41=null;
        Token BBRACKETLEFT42=null;
        Token BBRACKETRIGHT43=null;

        Object NAME41_tree=null;
        Object BBRACKETLEFT42_tree=null;
        Object BBRACKETRIGHT43_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");

        try { dbg.enterRule(getGrammarFileName(), "kAttributeName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(69, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:2: ( NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            dbg.location(70,4);
            NAME41=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName424);  
            stream_NAME.add(NAME41);

            dbg.location(70,10);
            BBRACKETLEFT42=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName427);  
            stream_BBRACKETLEFT.add(BBRACKETLEFT42);

            dbg.location(70,23);
            BBRACKETRIGHT43=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName429);  
            stream_BBRACKETRIGHT.add(BBRACKETRIGHT43);



            // AST REWRITE
            // elements: NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 70:38: -> ^( KATTRIBUTE NAME )
            {
                dbg.location(70,41);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:41: ^( KATTRIBUTE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(70,43);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KATTRIBUTE, "KATTRIBUTE"), root_1);

                dbg.location(70,54);
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
        dbg.location(71, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "kAttributeName");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "kAttributeName"

    public static class kAttributeUsage_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kAttributeUsage"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:1: kAttributeUsage : ( NAME BBRACKETLEFT BBRACKETRIGHT | NAME CURRENT | NAME PREVIOUS );
    public final SaseParser.kAttributeUsage_return kAttributeUsage() throws RecognitionException {
        SaseParser.kAttributeUsage_return retval = new SaseParser.kAttributeUsage_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME44=null;
        Token BBRACKETLEFT45=null;
        Token BBRACKETRIGHT46=null;
        Token NAME47=null;
        Token CURRENT48=null;
        Token NAME49=null;
        Token PREVIOUS50=null;

        Object NAME44_tree=null;
        Object BBRACKETLEFT45_tree=null;
        Object BBRACKETRIGHT46_tree=null;
        Object NAME47_tree=null;
        Object CURRENT48_tree=null;
        Object NAME49_tree=null;
        Object PREVIOUS50_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "kAttributeUsage");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(73, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:2: ( NAME BBRACKETLEFT BBRACKETRIGHT | NAME CURRENT | NAME PREVIOUS )
            int alt13=3;
            try { dbg.enterDecision(13);

            int LA13_0 = input.LA(1);

            if ( (LA13_0==NAME) ) {
                switch ( input.LA(2) ) {
                case BBRACKETLEFT:
                    {
                    alt13=1;
                    }
                    break;
                case CURRENT:
                    {
                    alt13=2;
                    }
                    break;
                case PREVIOUS:
                    {
                    alt13=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(13);}

            switch (alt13) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:5: NAME BBRACKETLEFT BBRACKETRIGHT
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(74,5);
                    NAME44=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage452); 
                    NAME44_tree = (Object)adaptor.create(NAME44);
                    adaptor.addChild(root_0, NAME44_tree);

                    dbg.location(74,10);
                    BBRACKETLEFT45=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeUsage454); 
                    BBRACKETLEFT45_tree = (Object)adaptor.create(BBRACKETLEFT45);
                    adaptor.addChild(root_0, BBRACKETLEFT45_tree);

                    dbg.location(74,23);
                    BBRACKETRIGHT46=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeUsage456); 
                    BBRACKETRIGHT46_tree = (Object)adaptor.create(BBRACKETRIGHT46);
                    adaptor.addChild(root_0, BBRACKETRIGHT46_tree);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:75:3: NAME CURRENT
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(75,3);
                    NAME47=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage461); 
                    NAME47_tree = (Object)adaptor.create(NAME47);
                    adaptor.addChild(root_0, NAME47_tree);

                    dbg.location(75,8);
                    CURRENT48=(Token)match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage463); 
                    CURRENT48_tree = (Object)adaptor.create(CURRENT48);
                    adaptor.addChild(root_0, CURRENT48_tree);


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:76:3: NAME PREVIOUS
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(76,3);
                    NAME49=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage468); 
                    NAME49_tree = (Object)adaptor.create(NAME49);
                    adaptor.addChild(root_0, NAME49_tree);

                    dbg.location(76,8);
                    PREVIOUS50=(Token)match(input,PREVIOUS,FOLLOW_PREVIOUS_in_kAttributeUsage470); 
                    PREVIOUS50_tree = (Object)adaptor.create(PREVIOUS50);
                    adaptor.addChild(root_0, PREVIOUS50_tree);


                    }
                    break;

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
        dbg.location(77, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "kAttributeUsage");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "kAttributeUsage"

    public static class sAttributeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sAttributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:1: sAttributeName : NAME -> ^( ATTRIBUTE NAME ) ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME51=null;

        Object NAME51_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");

        try { dbg.enterRule(getGrammarFileName(), "sAttributeName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(78, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:2: ( NAME -> ^( ATTRIBUTE NAME ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:4: NAME
            {
            dbg.location(79,4);
            NAME51=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName480);  
            stream_NAME.add(NAME51);



            // AST REWRITE
            // elements: NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 79:9: -> ^( ATTRIBUTE NAME )
            {
                dbg.location(79,12);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:12: ^( ATTRIBUTE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(79,14);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ATTRIBUTE, "ATTRIBUTE"), root_1);

                dbg.location(79,24);
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
        dbg.location(80, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "sAttributeName");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "sAttributeName"

    public static class whereExpressions_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "whereExpressions"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:1: whereExpressions : expression ( AND expression )* -> ^( WHEREEXPRESSION AND ( expression )* ) ;
    public final SaseParser.whereExpressions_return whereExpressions() throws RecognitionException {
        SaseParser.whereExpressions_return retval = new SaseParser.whereExpressions_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AND53=null;
        SaseParser.expression_return expression52 = null;

        SaseParser.expression_return expression54 = null;


        Object AND53_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try { dbg.enterRule(getGrammarFileName(), "whereExpressions");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(82, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:2: ( expression ( AND expression )* -> ^( WHEREEXPRESSION AND ( expression )* ) )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:4: expression ( AND expression )*
            {
            dbg.location(83,4);
            pushFollow(FOLLOW_expression_in_whereExpressions500);
            expression52=expression();

            state._fsp--;

            stream_expression.add(expression52.getTree());
            dbg.location(83,15);
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:15: ( AND expression )*
            try { dbg.enterSubRule(14);

            loop14:
            do {
                int alt14=2;
                try { dbg.enterDecision(14);

                int LA14_0 = input.LA(1);

                if ( (LA14_0==AND) ) {
                    alt14=1;
                }


                } finally {dbg.exitDecision(14);}

                switch (alt14) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:16: AND expression
            	    {
            	    dbg.location(83,16);
            	    AND53=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions503);  
            	    stream_AND.add(AND53);

            	    dbg.location(83,20);
            	    pushFollow(FOLLOW_expression_in_whereExpressions505);
            	    expression54=expression();

            	    state._fsp--;

            	    stream_expression.add(expression54.getTree());

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);
            } finally {dbg.exitSubRule(14);}



            // AST REWRITE
            // elements: expression, AND
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 83:33: -> ^( WHEREEXPRESSION AND ( expression )* )
            {
                dbg.location(83,36);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:36: ^( WHEREEXPRESSION AND ( expression )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(83,38);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION"), root_1);

                dbg.location(83,54);
                adaptor.addChild(root_1, stream_AND.nextNode());
                dbg.location(83,58);
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:58: ( expression )*
                while ( stream_expression.hasNext() ) {
                    dbg.location(83,58);
                    adaptor.addChild(root_1, stream_expression.nextTree());

                }
                stream_expression.reset();

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
        dbg.location(84, 2);

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:1: expression : ( term COMPAREOP term -> ^( EXPRESSION term COMPAREOP term ) | term COMPAREOP value -> ^( EXPRESSION term COMPAREOP value ) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token COMPAREOP56=null;
        Token COMPAREOP59=null;
        SaseParser.term_return term55 = null;

        SaseParser.term_return term57 = null;

        SaseParser.term_return term58 = null;

        SaseParser.value_return value60 = null;


        Object COMPAREOP56_tree=null;
        Object COMPAREOP59_tree=null;
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try { dbg.enterRule(getGrammarFileName(), "expression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(86, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:2: ( term COMPAREOP term -> ^( EXPRESSION term COMPAREOP term ) | term COMPAREOP value -> ^( EXPRESSION term COMPAREOP value ) )
            int alt15=2;
            try { dbg.enterDecision(15);

            try {
                isCyclicDecision = true;
                alt15 = dfa15.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(15);}

            switch (alt15) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:4: term COMPAREOP term
                    {
                    dbg.location(87,4);
                    pushFollow(FOLLOW_term_in_expression530);
                    term55=term();

                    state._fsp--;

                    stream_term.add(term55.getTree());
                    dbg.location(87,9);
                    COMPAREOP56=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression532);  
                    stream_COMPAREOP.add(COMPAREOP56);

                    dbg.location(87,19);
                    pushFollow(FOLLOW_term_in_expression534);
                    term57=term();

                    state._fsp--;

                    stream_term.add(term57.getTree());


                    // AST REWRITE
                    // elements: term, COMPAREOP, term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 87:24: -> ^( EXPRESSION term COMPAREOP term )
                    {
                        dbg.location(87,27);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:27: ^( EXPRESSION term COMPAREOP term )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(87,29);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPRESSION, "EXPRESSION"), root_1);

                        dbg.location(87,40);
                        adaptor.addChild(root_1, stream_term.nextTree());
                        dbg.location(87,45);
                        adaptor.addChild(root_1, stream_COMPAREOP.nextNode());
                        dbg.location(87,55);
                        adaptor.addChild(root_1, stream_term.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:63: term COMPAREOP value
                    {
                    dbg.location(87,63);
                    pushFollow(FOLLOW_term_in_expression550);
                    term58=term();

                    state._fsp--;

                    stream_term.add(term58.getTree());
                    dbg.location(87,68);
                    COMPAREOP59=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression552);  
                    stream_COMPAREOP.add(COMPAREOP59);

                    dbg.location(87,78);
                    pushFollow(FOLLOW_value_in_expression554);
                    value60=value();

                    state._fsp--;

                    stream_value.add(value60.getTree());


                    // AST REWRITE
                    // elements: term, value, COMPAREOP
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 87:84: -> ^( EXPRESSION term COMPAREOP value )
                    {
                        dbg.location(87,87);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:87: ^( EXPRESSION term COMPAREOP value )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(87,89);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPRESSION, "EXPRESSION"), root_1);

                        dbg.location(87,100);
                        adaptor.addChild(root_1, stream_term.nextTree());
                        dbg.location(87,105);
                        adaptor.addChild(root_1, stream_COMPAREOP.nextNode());
                        dbg.location(87,115);
                        adaptor.addChild(root_1, stream_value.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

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
        dbg.location(88, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "expression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "expression"

    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:90:1: term : ( sAttributeName POINT NAME -> ^( MEMBERACCESS sAttributeName NAME ) | kAttributeName POINT NAME -> ^( MEMBERACCESS kAttributeName NAME ) );
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token POINT62=null;
        Token NAME63=null;
        Token POINT65=null;
        Token NAME66=null;
        SaseParser.sAttributeName_return sAttributeName61 = null;

        SaseParser.kAttributeName_return kAttributeName64 = null;


        Object POINT62_tree=null;
        Object NAME63_tree=null;
        Object POINT65_tree=null;
        Object NAME66_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_kAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeName");
        try { dbg.enterRule(getGrammarFileName(), "term");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(90, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:90:6: ( sAttributeName POINT NAME -> ^( MEMBERACCESS sAttributeName NAME ) | kAttributeName POINT NAME -> ^( MEMBERACCESS kAttributeName NAME ) )
            int alt16=2;
            try { dbg.enterDecision(16);

            int LA16_0 = input.LA(1);

            if ( (LA16_0==NAME) ) {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==BBRACKETLEFT) ) {
                    alt16=2;
                }
                else if ( (LA16_1==POINT) ) {
                    alt16=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(16);}

            switch (alt16) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:90:8: sAttributeName POINT NAME
                    {
                    dbg.location(90,8);
                    pushFollow(FOLLOW_sAttributeName_in_term576);
                    sAttributeName61=sAttributeName();

                    state._fsp--;

                    stream_sAttributeName.add(sAttributeName61.getTree());
                    dbg.location(90,23);
                    POINT62=(Token)match(input,POINT,FOLLOW_POINT_in_term578);  
                    stream_POINT.add(POINT62);

                    dbg.location(90,29);
                    NAME63=(Token)match(input,NAME,FOLLOW_NAME_in_term580);  
                    stream_NAME.add(NAME63);



                    // AST REWRITE
                    // elements: NAME, sAttributeName
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 90:34: -> ^( MEMBERACCESS sAttributeName NAME )
                    {
                        dbg.location(90,37);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:90:37: ^( MEMBERACCESS sAttributeName NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(90,39);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MEMBERACCESS, "MEMBERACCESS"), root_1);

                        dbg.location(90,52);
                        adaptor.addChild(root_1, stream_sAttributeName.nextTree());
                        dbg.location(90,67);
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:3: kAttributeName POINT NAME
                    {
                    dbg.location(91,3);
                    pushFollow(FOLLOW_kAttributeName_in_term595);
                    kAttributeName64=kAttributeName();

                    state._fsp--;

                    stream_kAttributeName.add(kAttributeName64.getTree());
                    dbg.location(91,18);
                    POINT65=(Token)match(input,POINT,FOLLOW_POINT_in_term597);  
                    stream_POINT.add(POINT65);

                    dbg.location(91,24);
                    NAME66=(Token)match(input,NAME,FOLLOW_NAME_in_term599);  
                    stream_NAME.add(NAME66);



                    // AST REWRITE
                    // elements: kAttributeName, NAME
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 91:28: -> ^( MEMBERACCESS kAttributeName NAME )
                    {
                        dbg.location(91,31);
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:31: ^( MEMBERACCESS kAttributeName NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(91,33);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MEMBERACCESS, "MEMBERACCESS"), root_1);

                        dbg.location(91,46);
                        adaptor.addChild(root_1, stream_kAttributeName.nextTree());
                        dbg.location(91,61);
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

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
            dbg.exitRule(getGrammarFileName(), "term");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "term"

    public static class value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set67=null;

        Object set67_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "value");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(94, 1);

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:8: ( NUMBER | STRING_LITERAL )
            dbg.enterAlt(1);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(94,8);
            set67=(Token)input.LT(1);
            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set67));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


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
        dbg.location(94, 34);

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


    protected DFA15 dfa15 = new DFA15(this);
    static final String DFA15_eotS =
        "\13\uffff";
    static final String DFA15_eofS =
        "\13\uffff";
    static final String DFA15_minS =
        "\1\23\1\15\1\16\1\23\1\31\1\33\2\23\1\33\2\uffff";
    static final String DFA15_maxS =
        "\1\23\1\31\1\16\1\23\1\31\1\33\1\23\1\47\1\33\2\uffff";
    static final String DFA15_acceptS =
        "\11\uffff\1\1\1\2";
    static final String DFA15_specialS =
        "\13\uffff}>";
    static final String[] DFA15_transitionS = {
            "\1\1",
            "\1\2\13\uffff\1\3",
            "\1\4",
            "\1\5",
            "\1\6",
            "\1\7",
            "\1\10",
            "\1\11\17\uffff\1\12\3\uffff\1\12",
            "\1\7",
            "",
            ""
    };

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "86:1: expression : ( term COMPAREOP term -> ^( EXPRESSION term COMPAREOP term ) | term COMPAREOP value -> ^( EXPRESSION term COMPAREOP value ) );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
    }
 

    public static final BitSet FOLLOW_fromPart_in_query89 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_patternPart_in_query93 = new BitSet(new long[]{0x0000000000000182L});
    public static final BitSet FOLLOW_wherePart_in_query96 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_withinPart_in_query101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FROM_in_fromPart113 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_NAME_in_fromPart115 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_COMMA_in_fromPart118 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_NAME_in_fromPart120 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart135 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart137 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_TIMEUNIT_in_withinPart139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart161 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_wherePart1_in_wherePart163 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart165 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart167 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart186 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart209 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEQ_in_patternDecl233 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LBRACKET_in_patternDecl235 = new BitSet(new long[]{0x00000000A0080000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl237 = new BitSet(new long[]{0x0000000140000000L});
    public static final BitSet FOLLOW_COMMA_in_patternDecl240 = new BitSet(new long[]{0x00000000A0080000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl242 = new BitSet(new long[]{0x0000000140000000L});
    public static final BitSet FOLLOW_RBRACKET_in_patternDecl246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_pItem271 = new BitSet(new long[]{0x00000000A0080000L});
    public static final BitSet FOLLOW_LBRACKET_in_pItem275 = new BitSet(new long[]{0x00000000A0080000L});
    public static final BitSet FOLLOW_typeName_in_pItem281 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_attributeName_in_pItem285 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_RBRACKET_in_pItem287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeName314 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_PLUS_in_typeName318 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeName333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_METHOD_in_wherePart1353 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LBRACKET_in_wherePart1355 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_parameterList_in_wherePart1357 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RBRACKET_in_wherePart1359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList382 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList384 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList386 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName424 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName427 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage452 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeUsage454 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeUsage456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage461 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage468 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_PREVIOUS_in_kAttributeUsage470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions500 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_AND_in_whereExpressions503 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_expression_in_whereExpressions505 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_term_in_expression530 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression532 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_term_in_expression534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_expression550 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression552 = new BitSet(new long[]{0x0000008800000000L});
    public static final BitSet FOLLOW_value_in_expression554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_term576 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_term578 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_NAME_in_term580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeName_in_term595 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_term597 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_NAME_in_term599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value0 = new BitSet(new long[]{0x0000000000000002L});

}