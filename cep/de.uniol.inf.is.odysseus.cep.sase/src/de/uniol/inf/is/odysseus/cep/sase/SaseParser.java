// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2009-12-10 09:23:27
 
	package de.uniol.inf.is.odysseus.cep.sase; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class SaseParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "HAVING", "PATTERN", "WHERE", "WITHIN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "FIRST", "CURRENT", "PREVIOUS", "ALLTOPREVIOUS", "NAME", "LAST", "BBRACKETLEFT", "BBRACKETRIGHT", "TIMEUNIT", "SKIP_METHOD", "AGGREGATEOP", "PLUS", "MINUS", "POINT", "DIVISION", "MULT", "COMPAREOP", "SINGLEEQUALS", "EQUALS", "NOT", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "STATE", "KTYPE", "TYPE", "WHERESTRAT", "WHEREEXPRESSION", "MATHEXPRESSION", "TERM", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "KMEMBER", "MEMBER", "COMPAREEXPRESSION", "IDEXPRESSION", "AGGREGATION"
    };
    public static final int TERM=53;
    public static final int WHERE=6;
    public static final int MEMBER=58;
    public static final int POINT=25;
    public static final int LETTER=39;
    public static final int ATTRIBUTE=54;
    public static final int FLOAT=36;
    public static final int EQUALS=30;
    public static final int NOT=31;
    public static final int AND=11;
    public static final int MATHEXPRESSION=52;
    public static final int SPACE=42;
    public static final int EOF=-1;
    public static final int TYPE=49;
    public static final int LBRACKET=33;
    public static final int PATTERN=5;
    public static final int SINGLEEQUALS=29;
    public static final int COMPAREEXPRESSION=59;
    public static final int NAME=16;
    public static final int STRING_LITERAL=41;
    public static final int LEFTCURLY=9;
    public static final int COMMA=32;
    public static final int SEQ=8;
    public static final int PREVIOUS=14;
    public static final int KATTRIBUTE=55;
    public static final int RIGHTCURLY=10;
    public static final int DIVISION=26;
    public static final int PLUS=23;
    public static final int DIGIT=38;
    public static final int LAST=17;
    public static final int RBRACKET=34;
    public static final int BBRACKETRIGHT=19;
    public static final int INTEGER=35;
    public static final int STATE=47;
    public static final int IDEXPRESSION=60;
    public static final int KMEMBER=57;
    public static final int ALLTOPREVIOUS=15;
    public static final int TIMEUNIT=20;
    public static final int SKIP_METHOD=21;
    public static final int NUMBER=37;
    public static final int PARAMLIST=56;
    public static final int WHITESPACE=46;
    public static final int HAVING=4;
    public static final int MINUS=24;
    public static final int MULT=27;
    public static final int AGGREGATEOP=22;
    public static final int CURRENT=13;
    public static final int WHEREEXPRESSION=51;
    public static final int NEWLINE=45;
    public static final int NONCONTROL_CHAR=40;
    public static final int AGGREGATION=61;
    public static final int BBRACKETLEFT=18;
    public static final int WHERESTRAT=50;
    public static final int WITHIN=7;
    public static final int KTYPE=48;
    public static final int LOWER=43;
    public static final int COMPAREOP=28;
    public static final int UPPER=44;
    public static final int FIRST=12;

    // delegates
    // delegators


        public SaseParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public SaseParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:1: query : patternPart ( wherePart )? ( withinPart )? ;
    public final SaseParser.query_return query() throws RecognitionException {
        SaseParser.query_return retval = new SaseParser.query_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.patternPart_return patternPart1 = null;

        SaseParser.wherePart_return wherePart2 = null;

        SaseParser.withinPart_return withinPart3 = null;



        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:7: ( patternPart ( wherePart )? ( withinPart )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:10: patternPart ( wherePart )? ( withinPart )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_patternPart_in_query116);
            patternPart1=patternPart();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, patternPart1.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:22: ( wherePart )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==WHERE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:23: wherePart
                    {
                    pushFollow(FOLLOW_wherePart_in_query119);
                    wherePart2=wherePart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, wherePart2.getTree());

                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:35: ( withinPart )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==WITHIN) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:36: withinPart
                    {
                    pushFollow(FOLLOW_withinPart_in_query124);
                    withinPart3=withinPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, withinPart3.getTree());

                    }
                    break;

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
        return retval;
    }
    // $ANTLR end "query"

    public static class withinPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "withinPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:35:1: withinPart : WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) ;
    public final SaseParser.withinPart_return withinPart() throws RecognitionException {
        SaseParser.withinPart_return retval = new SaseParser.withinPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WITHIN4=null;
        Token NUMBER5=null;
        Token TIMEUNIT6=null;

        Object WITHIN4_tree=null;
        Object NUMBER5_tree=null;
        Object TIMEUNIT6_tree=null;
        RewriteRuleTokenStream stream_WITHIN=new RewriteRuleTokenStream(adaptor,"token WITHIN");
        RewriteRuleTokenStream stream_TIMEUNIT=new RewriteRuleTokenStream(adaptor,"token TIMEUNIT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:36:2: ( WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:36:4: WITHIN NUMBER TIMEUNIT
            {
            WITHIN4=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart139); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_WITHIN.add(WITHIN4);

            NUMBER5=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart141); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER5);

            TIMEUNIT6=(Token)match(input,TIMEUNIT,FOLLOW_TIMEUNIT_in_withinPart143); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TIMEUNIT.add(TIMEUNIT6);



            // AST REWRITE
            // elements: TIMEUNIT, WITHIN, NUMBER
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 36:27: -> ^( WITHIN NUMBER TIMEUNIT )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:36:30: ^( WITHIN NUMBER TIMEUNIT )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_WITHIN.nextNode(), root_1);

                adaptor.addChild(root_1, stream_NUMBER.nextNode());
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
        return retval;
    }
    // $ANTLR end "withinPart"

    public static class wherePart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "wherePart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:39:1: wherePart : ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
    public final SaseParser.wherePart_return wherePart() throws RecognitionException {
        SaseParser.wherePart_return retval = new SaseParser.wherePart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WHERE7=null;
        Token LEFTCURLY9=null;
        Token RIGHTCURLY11=null;
        Token WHERE12=null;
        SaseParser.wherePart1_return wherePart18 = null;

        SaseParser.whereExpressions_return whereExpressions10 = null;

        SaseParser.whereExpressions_return whereExpressions13 = null;


        Object WHERE7_tree=null;
        Object LEFTCURLY9_tree=null;
        Object RIGHTCURLY11_tree=null;
        Object WHERE12_tree=null;
        RewriteRuleTokenStream stream_RIGHTCURLY=new RewriteRuleTokenStream(adaptor,"token RIGHTCURLY");
        RewriteRuleTokenStream stream_WHERE=new RewriteRuleTokenStream(adaptor,"token WHERE");
        RewriteRuleTokenStream stream_LEFTCURLY=new RewriteRuleTokenStream(adaptor,"token LEFTCURLY");
        RewriteRuleSubtreeStream stream_wherePart1=new RewriteRuleSubtreeStream(adaptor,"rule wherePart1");
        RewriteRuleSubtreeStream stream_whereExpressions=new RewriteRuleSubtreeStream(adaptor,"rule whereExpressions");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:40:2: ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==WHERE) ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1==SKIP_METHOD) ) {
                    alt3=1;
                }
                else if ( (LA3_1==NAME||LA3_1==AGGREGATEOP||LA3_1==NUMBER||LA3_1==STRING_LITERAL) ) {
                    alt3=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:40:4: WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    WHERE7=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart165); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE7);

                    pushFollow(FOLLOW_wherePart1_in_wherePart167);
                    wherePart18=wherePart1();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_wherePart1.add(wherePart18.getTree());
                    LEFTCURLY9=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart169); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFTCURLY.add(LEFTCURLY9);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart171);
                    whereExpressions10=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions10.getTree());
                    RIGHTCURLY11=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart173); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHTCURLY.add(RIGHTCURLY11);



                    // AST REWRITE
                    // elements: WHERE, whereExpressions, wherePart1
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 40:59: -> ^( WHERE wherePart1 whereExpressions )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:40:62: ^( WHERE wherePart1 whereExpressions )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_wherePart1.nextTree());
                        adaptor.addChild(root_1, stream_whereExpressions.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:41:4: WHERE whereExpressions
                    {
                    WHERE12=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart190); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE12);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart192);
                    whereExpressions13=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions13.getTree());


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
                    // 41:27: -> ^( WHERE whereExpressions )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:41:30: ^( WHERE whereExpressions )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

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
        return retval;
    }
    // $ANTLR end "wherePart"

    public static class patternPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:44:1: patternPart : PATTERN patternDecl -> ^( PATTERN patternDecl ) ;
    public final SaseParser.patternPart_return patternPart() throws RecognitionException {
        SaseParser.patternPart_return retval = new SaseParser.patternPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token PATTERN14=null;
        SaseParser.patternDecl_return patternDecl15 = null;


        Object PATTERN14_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleSubtreeStream stream_patternDecl=new RewriteRuleSubtreeStream(adaptor,"rule patternDecl");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:2: ( PATTERN patternDecl -> ^( PATTERN patternDecl ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:4: PATTERN patternDecl
            {
            PATTERN14=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart213); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN14);

            pushFollow(FOLLOW_patternDecl_in_patternPart215);
            patternDecl15=patternDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternDecl.add(patternDecl15.getTree());


            // AST REWRITE
            // elements: patternDecl, PATTERN
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 45:24: -> ^( PATTERN patternDecl )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:27: ^( PATTERN patternDecl )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_PATTERN.nextNode(), root_1);

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
        return retval;
    }
    // $ANTLR end "patternPart"

    public static class patternDecl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternDecl"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:1: patternDecl : SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) ;
    public final SaseParser.patternDecl_return patternDecl() throws RecognitionException {
        SaseParser.patternDecl_return retval = new SaseParser.patternDecl_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEQ16=null;
        Token LBRACKET17=null;
        Token COMMA19=null;
        Token RBRACKET21=null;
        SaseParser.pItem_return pItem18 = null;

        SaseParser.pItem_return pItem20 = null;


        Object SEQ16_tree=null;
        Object LBRACKET17_tree=null;
        Object COMMA19_tree=null;
        Object RBRACKET21_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SEQ=new RewriteRuleTokenStream(adaptor,"token SEQ");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_pItem=new RewriteRuleSubtreeStream(adaptor,"rule pItem");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:50:2: ( SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:50:4: SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET
            {
            SEQ16=(Token)match(input,SEQ,FOLLOW_SEQ_in_patternDecl237); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEQ.add(SEQ16);

            LBRACKET17=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_patternDecl239); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET17);

            pushFollow(FOLLOW_pItem_in_patternDecl241);
            pItem18=pItem();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_pItem.add(pItem18.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:50:23: ( COMMA pItem )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==COMMA) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:50:24: COMMA pItem
            	    {
            	    COMMA19=(Token)match(input,COMMA,FOLLOW_COMMA_in_patternDecl244); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA19);

            	    pushFollow(FOLLOW_pItem_in_patternDecl246);
            	    pItem20=pItem();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_pItem.add(pItem20.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            RBRACKET21=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_patternDecl250); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET21);



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
            // 50:47: -> ^( SEQ ( pItem )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:50:50: ^( SEQ ( pItem )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_SEQ.nextNode(), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:50:56: ( pItem )*
                while ( stream_pItem.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "patternDecl"

    public static class pItem_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pItem"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:1: pItem : ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) ;
    public final SaseParser.pItem_return pItem() throws RecognitionException {
        SaseParser.pItem_return retval = new SaseParser.pItem_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NOT22=null;
        Token LBRACKET23=null;
        Token RBRACKET24=null;
        SaseParser.typeName_return type = null;

        SaseParser.attributeName_return variable = null;


        Object NOT22_tree=null;
        Object LBRACKET23_tree=null;
        Object RBRACKET24_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_typeName=new RewriteRuleSubtreeStream(adaptor,"rule typeName");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:8: ( ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:10: ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )?
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:10: ( NOT )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==NOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:11: NOT
                    {
                    NOT22=(Token)match(input,NOT,FOLLOW_NOT_in_pItem275); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NOT.add(NOT22);


                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:17: ( LBRACKET )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==LBRACKET) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: LBRACKET
                    {
                    LBRACKET23=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_pItem279); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET23);


                    }
                    break;

            }

            pushFollow(FOLLOW_typeName_in_pItem285);
            type=typeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_typeName.add(type.getTree());
            pushFollow(FOLLOW_attributeName_in_pItem289);
            variable=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(variable.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:65: ( RBRACKET )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==RBRACKET) ) {
                int LA7_1 = input.LA(2);

                if ( (LA7_1==COMMA||LA7_1==RBRACKET) ) {
                    alt7=1;
                }
                else if ( (LA7_1==EOF) ) {
                    alt7=1;
                }
            }
            switch (alt7) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: RBRACKET
                    {
                    RBRACKET24=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_pItem291); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET24);


                    }
                    break;

            }



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
            // 54:75: -> ^( STATE $type $variable ( NOT )? )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:78: ^( STATE $type $variable ( NOT )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STATE, "STATE"), root_1);

                adaptor.addChild(root_1, stream_type.nextTree());
                adaptor.addChild(root_1, stream_variable.nextTree());
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:102: ( NOT )?
                if ( stream_NOT.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "pItem"

    public static class typeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:1: typeName : NAME (op= PLUS )? -> {$op != null}? ^( KTYPE NAME $op) -> ^( TYPE NAME ) ;
    public final SaseParser.typeName_return typeName() throws RecognitionException {
        SaseParser.typeName_return retval = new SaseParser.typeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;
        Token NAME25=null;

        Object op_tree=null;
        Object NAME25_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:9: ( NAME (op= PLUS )? -> {$op != null}? ^( KTYPE NAME $op) -> ^( TYPE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:11: NAME (op= PLUS )?
            {
            NAME25=(Token)match(input,NAME,FOLLOW_NAME_in_typeName318); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME25);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:18: (op= PLUS )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==PLUS) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: op= PLUS
                    {
                    op=(Token)match(input,PLUS,FOLLOW_PLUS_in_typeName322); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PLUS.add(op);


                    }
                    break;

            }



            // AST REWRITE
            // elements: NAME, op, NAME
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
            // 57:25: -> {$op != null}? ^( KTYPE NAME $op)
            if (op != null) {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:43: ^( KTYPE NAME $op)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KTYPE, "KTYPE"), root_1);

                adaptor.addChild(root_1, stream_NAME.nextNode());
                adaptor.addChild(root_1, stream_op.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 58:9: -> ^( TYPE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:12: ^( TYPE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TYPE, "TYPE"), root_1);

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
        return retval;
    }
    // $ANTLR end "typeName"

    public static class wherePart1_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "wherePart1"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:1: wherePart1 : SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) ;
    public final SaseParser.wherePart1_return wherePart1() throws RecognitionException {
        SaseParser.wherePart1_return retval = new SaseParser.wherePart1_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SKIP_METHOD26=null;
        Token LBRACKET27=null;
        Token RBRACKET29=null;
        SaseParser.parameterList_return parameterList28 = null;


        Object SKIP_METHOD26_tree=null;
        Object LBRACKET27_tree=null;
        Object RBRACKET29_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SKIP_METHOD=new RewriteRuleTokenStream(adaptor,"token SKIP_METHOD");
        RewriteRuleSubtreeStream stream_parameterList=new RewriteRuleSubtreeStream(adaptor,"rule parameterList");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:2: ( SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:4: SKIP_METHOD LBRACKET parameterList RBRACKET
            {
            SKIP_METHOD26=(Token)match(input,SKIP_METHOD,FOLLOW_SKIP_METHOD_in_wherePart1365); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SKIP_METHOD.add(SKIP_METHOD26);

            LBRACKET27=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_wherePart1367); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET27);

            pushFollow(FOLLOW_parameterList_in_wherePart1369);
            parameterList28=parameterList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_parameterList.add(parameterList28.getTree());
            RBRACKET29=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_wherePart1371); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET29);



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
            // 62:48: -> ^( WHERESTRAT SKIP_METHOD parameterList )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:51: ^( WHERESTRAT SKIP_METHOD parameterList )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHERESTRAT, "WHERESTRAT"), root_1);

                adaptor.addChild(root_1, stream_SKIP_METHOD.nextNode());
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
        return retval;
    }
    // $ANTLR end "wherePart1"

    public static class parameterList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameterList"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
    public final SaseParser.parameterList_return parameterList() throws RecognitionException {
        SaseParser.parameterList_return retval = new SaseParser.parameterList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token COMMA31=null;
        SaseParser.attributeName_return attributeName30 = null;

        SaseParser.attributeName_return attributeName32 = null;


        Object COMMA31_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:4: attributeName ( COMMA attributeName )*
            {
            pushFollow(FOLLOW_attributeName_in_parameterList394);
            attributeName30=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(attributeName30.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:17: ( COMMA attributeName )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==COMMA) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:18: COMMA attributeName
            	    {
            	    COMMA31=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList396); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA31);

            	    pushFollow(FOLLOW_attributeName_in_parameterList398);
            	    attributeName32=attributeName();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeName.add(attributeName32.getTree());

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);



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
            // 66:40: -> ^( PARAMLIST ( attributeName )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:43: ^( PARAMLIST ( attributeName )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PARAMLIST, "PARAMLIST"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:55: ( attributeName )*
                while ( stream_attributeName.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "parameterList"

    public static class attributeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "attributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:1: attributeName : ( kAttributeName | sAttributeName );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.kAttributeName_return kAttributeName33 = null;

        SaseParser.sAttributeName_return sAttributeName34 = null;



        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:2: ( kAttributeName | sAttributeName )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==NAME) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==BBRACKETLEFT) ) {
                    alt10=1;
                }
                else if ( (LA10_1==EOF||LA10_1==COMMA||LA10_1==RBRACKET) ) {
                    alt10=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:5: kAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_kAttributeName_in_attributeName422);
                    kAttributeName33=kAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, kAttributeName33.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:20: sAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_sAttributeName_in_attributeName424);
                    sAttributeName34=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sAttributeName34.getTree());

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
        return retval;
    }
    // $ANTLR end "attributeName"

    public static class kAttributeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kAttributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) ;
    public final SaseParser.kAttributeName_return kAttributeName() throws RecognitionException {
        SaseParser.kAttributeName_return retval = new SaseParser.kAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME35=null;
        Token BBRACKETLEFT36=null;
        Token BBRACKETRIGHT37=null;

        Object NAME35_tree=null;
        Object BBRACKETLEFT36_tree=null;
        Object BBRACKETRIGHT37_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:2: ( NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            NAME35=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName436); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME35);

            BBRACKETLEFT36=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName439); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT36);

            BBRACKETRIGHT37=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName441); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT37);



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
            // 74:38: -> ^( KATTRIBUTE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:41: ^( KATTRIBUTE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KATTRIBUTE, "KATTRIBUTE"), root_1);

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
        return retval;
    }
    // $ANTLR end "kAttributeName"

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

        Token NAME38=null;

        Object NAME38_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:2: ( NAME -> ^( ATTRIBUTE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:4: NAME
            {
            NAME38=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName464); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME38);



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
            // 79:9: -> ^( ATTRIBUTE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:12: ^( ATTRIBUTE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ATTRIBUTE, "ATTRIBUTE"), root_1);

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
        return retval;
    }
    // $ANTLR end "sAttributeName"

    public static class kAttributeUsage_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kAttributeUsage"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:1: kAttributeUsage : ( NAME CURRENT | NAME FIRST | NAME PREVIOUS | NAME LAST );
    public final SaseParser.kAttributeUsage_return kAttributeUsage() throws RecognitionException {
        SaseParser.kAttributeUsage_return retval = new SaseParser.kAttributeUsage_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME39=null;
        Token CURRENT40=null;
        Token NAME41=null;
        Token FIRST42=null;
        Token NAME43=null;
        Token PREVIOUS44=null;
        Token NAME45=null;
        Token LAST46=null;

        Object NAME39_tree=null;
        Object CURRENT40_tree=null;
        Object NAME41_tree=null;
        Object FIRST42_tree=null;
        Object NAME43_tree=null;
        Object PREVIOUS44_tree=null;
        Object NAME45_tree=null;
        Object LAST46_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:2: ( NAME CURRENT | NAME FIRST | NAME PREVIOUS | NAME LAST )
            int alt11=4;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==NAME) ) {
                switch ( input.LA(2) ) {
                case CURRENT:
                    {
                    alt11=1;
                    }
                    break;
                case FIRST:
                    {
                    alt11=2;
                    }
                    break;
                case PREVIOUS:
                    {
                    alt11=3;
                    }
                    break;
                case LAST:
                    {
                    alt11=4;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;
                }

            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:5: NAME CURRENT
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME39=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage483); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME39_tree = (Object)adaptor.create(NAME39);
                    adaptor.addChild(root_0, NAME39_tree);
                    }
                    CURRENT40=(Token)match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage485); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    CURRENT40_tree = (Object)adaptor.create(CURRENT40);
                    adaptor.addChild(root_0, CURRENT40_tree);
                    }

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:3: NAME FIRST
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME41=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage491); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME41_tree = (Object)adaptor.create(NAME41);
                    adaptor.addChild(root_0, NAME41_tree);
                    }
                    FIRST42=(Token)match(input,FIRST,FOLLOW_FIRST_in_kAttributeUsage493); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FIRST42_tree = (Object)adaptor.create(FIRST42);
                    adaptor.addChild(root_0, FIRST42_tree);
                    }

                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:3: NAME PREVIOUS
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME43=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage498); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME43_tree = (Object)adaptor.create(NAME43);
                    adaptor.addChild(root_0, NAME43_tree);
                    }
                    PREVIOUS44=(Token)match(input,PREVIOUS,FOLLOW_PREVIOUS_in_kAttributeUsage500); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PREVIOUS44_tree = (Object)adaptor.create(PREVIOUS44);
                    adaptor.addChild(root_0, PREVIOUS44_tree);
                    }

                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:85:3: NAME LAST
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME45=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage505); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME45_tree = (Object)adaptor.create(NAME45);
                    adaptor.addChild(root_0, NAME45_tree);
                    }
                    LAST46=(Token)match(input,LAST,FOLLOW_LAST_in_kAttributeUsage507); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LAST46_tree = (Object)adaptor.create(LAST46);
                    adaptor.addChild(root_0, LAST46_tree);
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
        return retval;
    }
    // $ANTLR end "kAttributeUsage"

    public static class whereExpressions_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "whereExpressions"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:1: whereExpressions : expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) ;
    public final SaseParser.whereExpressions_return whereExpressions() throws RecognitionException {
        SaseParser.whereExpressions_return retval = new SaseParser.whereExpressions_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AND48=null;
        SaseParser.expression_return expression47 = null;

        SaseParser.expression_return expression49 = null;


        Object AND48_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:2: ( expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:4: expression ( AND expression )*
            {
            pushFollow(FOLLOW_expression_in_whereExpressions519);
            expression47=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression47.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:15: ( AND expression )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==AND) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:16: AND expression
            	    {
            	    AND48=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions522); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_AND.add(AND48);

            	    pushFollow(FOLLOW_expression_in_whereExpressions524);
            	    expression49=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression49.getTree());

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);



            // AST REWRITE
            // elements: AND, expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 89:33: -> ^( WHEREEXPRESSION ( AND )? ( expression )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:36: ^( WHEREEXPRESSION ( AND )? ( expression )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:54: ( AND )?
                if ( stream_AND.hasNext() ) {
                    adaptor.addChild(root_1, stream_AND.nextNode());

                }
                stream_AND.reset();
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:59: ( expression )*
                while ( stream_expression.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "whereExpressions"

    public static class expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:1: expression : ( NAME -> ^( IDEXPRESSION NAME ) | f1= mathExpression SINGLEEQUALS f2= mathExpression -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= mathExpression COMPAREOP f2= mathExpression -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME50=null;
        Token SINGLEEQUALS51=null;
        Token COMPAREOP52=null;
        SaseParser.mathExpression_return f1 = null;

        SaseParser.mathExpression_return f2 = null;


        Object NAME50_tree=null;
        Object SINGLEEQUALS51_tree=null;
        Object COMPAREOP52_tree=null;
        RewriteRuleTokenStream stream_SINGLEEQUALS=new RewriteRuleTokenStream(adaptor,"token SINGLEEQUALS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleSubtreeStream stream_mathExpression=new RewriteRuleSubtreeStream(adaptor,"rule mathExpression");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:2: ( NAME -> ^( IDEXPRESSION NAME ) | f1= mathExpression SINGLEEQUALS f2= mathExpression -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= mathExpression COMPAREOP f2= mathExpression -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) )
            int alt13=3;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:4: NAME
                    {
                    NAME50=(Token)match(input,NAME,FOLLOW_NAME_in_expression550); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME50);



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
                    // 93:9: -> ^( IDEXPRESSION NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:12: ^( IDEXPRESSION NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IDEXPRESSION, "IDEXPRESSION"), root_1);

                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:35: f1= mathExpression SINGLEEQUALS f2= mathExpression
                    {
                    pushFollow(FOLLOW_mathExpression_in_expression564);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());
                    SINGLEEQUALS51=(Token)match(input,SINGLEEQUALS,FOLLOW_SINGLEEQUALS_in_expression566); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SINGLEEQUALS.add(SINGLEEQUALS51);

                    pushFollow(FOLLOW_mathExpression_in_expression570);
                    f2=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f2.getTree());


                    // AST REWRITE
                    // elements: f2, f1
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
                    // 93:84: -> ^( COMPAREEXPRESSION $f1 EQUALS $f2)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:88: ^( COMPAREEXPRESSION $f1 EQUALS $f2)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(COMPAREEXPRESSION, "COMPAREEXPRESSION"), root_1);

                        adaptor.addChild(root_1, stream_f1.nextTree());
                        adaptor.addChild(root_1, (Object)adaptor.create(EQUALS, "EQUALS"));
                        adaptor.addChild(root_1, stream_f2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:126: f1= mathExpression COMPAREOP f2= mathExpression
                    {
                    pushFollow(FOLLOW_mathExpression_in_expression591);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());
                    COMPAREOP52=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression593); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COMPAREOP.add(COMPAREOP52);

                    pushFollow(FOLLOW_mathExpression_in_expression597);
                    f2=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f2.getTree());


                    // AST REWRITE
                    // elements: f1, f2, COMPAREOP
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
                    // 93:172: -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:175: ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(COMPAREEXPRESSION, "COMPAREEXPRESSION"), root_1);

                        adaptor.addChild(root_1, stream_f1.nextTree());
                        adaptor.addChild(root_1, stream_COMPAREOP.nextNode());
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
        return retval;
    }
    // $ANTLR end "expression"

    public static class mathExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mathExpression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:1: mathExpression : left= term (op= ( MULT | DIVISION ) right= term )* ;
    public final SaseParser.mathExpression_return mathExpression() throws RecognitionException {
        SaseParser.mathExpression_return retval = new SaseParser.mathExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;
        SaseParser.term_return left = null;

        SaseParser.term_return right = null;


        Object op_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:15: (left= term (op= ( MULT | DIVISION ) right= term )* )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:17: left= term (op= ( MULT | DIVISION ) right= term )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_term_in_mathExpression623);
            left=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, left.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:27: (op= ( MULT | DIVISION ) right= term )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>=DIVISION && LA14_0<=MULT)) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:28: op= ( MULT | DIVISION ) right= term
            	    {
            	    op=(Token)input.LT(1);
            	    if ( (input.LA(1)>=DIVISION && input.LA(1)<=MULT) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(op));
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_term_in_mathExpression636);
            	    right=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, right.getTree());

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


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
        return retval;
    }
    // $ANTLR end "mathExpression"

    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:1: term : ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) | value );
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token aName=null;
        Token member=null;
        Token POINT55=null;
        Token NAME56=null;
        Token POINT57=null;
        SaseParser.aggregation_return aggregation53 = null;

        SaseParser.kAttributeUsage_return kAttributeUsage54 = null;

        SaseParser.value_return value58 = null;


        Object aName_tree=null;
        Object member_tree=null;
        Object POINT55_tree=null;
        Object NAME56_tree=null;
        Object POINT57_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_kAttributeUsage=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeUsage");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:6: ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) | value )
            int alt15=4;
            switch ( input.LA(1) ) {
            case AGGREGATEOP:
                {
                alt15=1;
                }
                break;
            case NAME:
                {
                int LA15_2 = input.LA(2);

                if ( ((LA15_2>=FIRST && LA15_2<=PREVIOUS)||LA15_2==LAST) ) {
                    alt15=2;
                }
                else if ( (LA15_2==POINT) ) {
                    alt15=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 2, input);

                    throw nvae;
                }
                }
                break;
            case NUMBER:
            case STRING_LITERAL:
                {
                alt15=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }

            switch (alt15) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:8: aggregation
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregation_in_term658);
                    aggregation53=aggregation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregation53.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:3: kAttributeUsage POINT NAME
                    {
                    pushFollow(FOLLOW_kAttributeUsage_in_term664);
                    kAttributeUsage54=kAttributeUsage();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeUsage.add(kAttributeUsage54.getTree());
                    POINT55=(Token)match(input,POINT,FOLLOW_POINT_in_term666); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT55);

                    NAME56=(Token)match(input,NAME,FOLLOW_NAME_in_term668); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME56);



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
                    // 101:30: -> ^( KMEMBER kAttributeUsage NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:33: ^( KMEMBER kAttributeUsage NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KMEMBER, "KMEMBER"), root_1);

                        adaptor.addChild(root_1, stream_kAttributeUsage.nextTree());
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:3: aName= NAME POINT member= NAME
                    {
                    aName=(Token)match(input,NAME,FOLLOW_NAME_in_term685); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(aName);

                    POINT57=(Token)match(input,POINT,FOLLOW_POINT_in_term687); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT57);

                    member=(Token)match(input,NAME,FOLLOW_NAME_in_term691); if (state.failed) return retval; 
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
                    // 102:32: -> ^( MEMBER $aName $member)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:35: ^( MEMBER $aName $member)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MEMBER, "MEMBER"), root_1);

                        adaptor.addChild(root_1, stream_aName.nextNode());
                        adaptor.addChild(root_1, stream_member.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:3: value
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_value_in_term708);
                    value58=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value58.getTree());

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
        return retval;
    }
    // $ANTLR end "term"

    public static class aggregation_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "aggregation"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:107:1: aggregation : AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member) ;
    public final SaseParser.aggregation_return aggregation() throws RecognitionException {
        SaseParser.aggregation_return retval = new SaseParser.aggregation_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token var=null;
        Token member=null;
        Token AGGREGATEOP59=null;
        Token LBRACKET60=null;
        Token ALLTOPREVIOUS61=null;
        Token POINT62=null;
        Token RBRACKET63=null;

        Object var_tree=null;
        Object member_tree=null;
        Object AGGREGATEOP59_tree=null;
        Object LBRACKET60_tree=null;
        Object ALLTOPREVIOUS61_tree=null;
        Object POINT62_tree=null;
        Object RBRACKET63_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_AGGREGATEOP=new RewriteRuleTokenStream(adaptor,"token AGGREGATEOP");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleTokenStream stream_ALLTOPREVIOUS=new RewriteRuleTokenStream(adaptor,"token ALLTOPREVIOUS");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:2: ( AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:4: AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET
            {
            AGGREGATEOP59=(Token)match(input,AGGREGATEOP,FOLLOW_AGGREGATEOP_in_aggregation722); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_AGGREGATEOP.add(AGGREGATEOP59);

            LBRACKET60=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation724); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET60);

            var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation728); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(var);

            ALLTOPREVIOUS61=(Token)match(input,ALLTOPREVIOUS,FOLLOW_ALLTOPREVIOUS_in_aggregation730); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ALLTOPREVIOUS.add(ALLTOPREVIOUS61);

            POINT62=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation732); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_POINT.add(POINT62);

            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation736); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(member);

            RBRACKET63=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation738); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET63);



            // AST REWRITE
            // elements: AGGREGATEOP, ALLTOPREVIOUS, var, member
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
            // 108:75: -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member)
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:78: ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(AGGREGATION, "AGGREGATION"), root_1);

                adaptor.addChild(root_1, stream_AGGREGATEOP.nextNode());
                adaptor.addChild(root_1, stream_var.nextNode());
                adaptor.addChild(root_1, stream_ALLTOPREVIOUS.nextNode());
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
        return retval;
    }
    // $ANTLR end "aggregation"

    public static class value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set64=null;

        Object set64_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:8: ( NUMBER | STRING_LITERAL )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (Object)adaptor.nil();

            set64=(Token)input.LT(1);
            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set64));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
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
        return retval;
    }
    // $ANTLR end "value"

    // Delegated rules


    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA13_eotS =
        "\47\uffff";
    static final String DFA13_eofS =
        "\1\uffff\1\11\45\uffff";
    static final String DFA13_minS =
        "\1\20\1\7\1\41\1\32\4\31\1\20\1\uffff\2\20\2\uffff\1\20\1\32\1"+
        "\17\1\41\1\14\2\32\1\31\1\20\4\31\2\20\1\17\1\20\1\32\1\42\1\31"+
        "\2\32\1\20\1\42\1\32";
    static final String DFA13_maxS =
        "\1\51\1\31\1\41\1\35\4\31\1\20\1\uffff\1\20\1\51\2\uffff\1\20\1"+
        "\35\1\17\1\41\1\31\2\35\1\31\1\20\4\31\2\20\1\17\1\20\1\35\1\42"+
        "\1\31\2\35\1\20\1\42\1\35";
    static final String DFA13_acceptS =
        "\11\uffff\1\1\2\uffff\1\3\1\2\31\uffff";
    static final String DFA13_specialS =
        "\47\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\1\5\uffff\1\2\16\uffff\1\3\3\uffff\1\3",
            "\1\11\2\uffff\2\11\1\5\1\4\1\6\2\uffff\1\7\7\uffff\1\10",
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

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "92:1: expression : ( NAME -> ^( IDEXPRESSION NAME ) | f1= mathExpression SINGLEEQUALS f2= mathExpression -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= mathExpression COMPAREOP f2= mathExpression -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) );";
        }
    }
 

    public static final BitSet FOLLOW_patternPart_in_query116 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_wherePart_in_query119 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_withinPart_in_query124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart139 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart141 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_TIMEUNIT_in_withinPart143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart165 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_wherePart1_in_wherePart167 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart169 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart171 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart190 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart213 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEQ_in_patternDecl237 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_LBRACKET_in_patternDecl239 = new BitSet(new long[]{0x0000000280010000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl241 = new BitSet(new long[]{0x0000000500000000L});
    public static final BitSet FOLLOW_COMMA_in_patternDecl244 = new BitSet(new long[]{0x0000000280010000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl246 = new BitSet(new long[]{0x0000000500000000L});
    public static final BitSet FOLLOW_RBRACKET_in_patternDecl250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_pItem275 = new BitSet(new long[]{0x0000000280010000L});
    public static final BitSet FOLLOW_LBRACKET_in_pItem279 = new BitSet(new long[]{0x0000000280010000L});
    public static final BitSet FOLLOW_typeName_in_pItem285 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_attributeName_in_pItem289 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_RBRACKET_in_pItem291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeName318 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_PLUS_in_typeName322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_METHOD_in_wherePart1365 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_LBRACKET_in_wherePart1367 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_parameterList_in_wherePart1369 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RBRACKET_in_wherePart1371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList394 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList396 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList398 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName436 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName439 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage483 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage491 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_FIRST_in_kAttributeUsage493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage498 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_PREVIOUS_in_kAttributeUsage500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage505 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_LAST_in_kAttributeUsage507 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions519 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_whereExpressions522 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_expression_in_whereExpressions524 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_NAME_in_expression550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression564 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_SINGLEEQUALS_in_expression566 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_mathExpression_in_expression570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression591 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression593 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_mathExpression_in_expression597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_mathExpression623 = new BitSet(new long[]{0x000000000C000002L});
    public static final BitSet FOLLOW_set_in_mathExpression628 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_term_in_mathExpression636 = new BitSet(new long[]{0x000000000C000002L});
    public static final BitSet FOLLOW_aggregation_in_term658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_term664 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_term666 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_term668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_term685 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_term687 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_term691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AGGREGATEOP_in_aggregation722 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation724 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_aggregation728 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_ALLTOPREVIOUS_in_aggregation730 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation732 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_aggregation736 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value0 = new BitSet(new long[]{0x0000000000000002L});

}