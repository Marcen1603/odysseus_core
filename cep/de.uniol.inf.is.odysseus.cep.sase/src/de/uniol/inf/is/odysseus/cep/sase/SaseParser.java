// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2009-12-11 11:21:43
 
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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PATTERN", "WHERE", "WITHIN", "RETURN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "FIRST", "CURRENT", "PREVIOUS", "ALLTOPREVIOUS", "NAME", "LAST", "BBRACKETLEFT", "BBRACKETRIGHT", "TIMEUNIT", "SKIP_METHOD", "AGGREGATEOP", "PLUS", "MINUS", "POINT", "DIVISION", "MULT", "COMPAREOP", "SINGLEEQUALS", "EQUALS", "NOT", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "STATE", "KTYPE", "TYPE", "WHERESTRAT", "WHEREEXPRESSION", "MATHEXPRESSION", "TERM", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "KMEMBER", "MEMBER", "COMPAREEXPRESSION", "IDEXPRESSION", "AGGREGATION"
    };
    public static final int TERM=53;
    public static final int WHERE=5;
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
    public static final int PATTERN=4;
    public static final int SINGLEEQUALS=29;
    public static final int COMPAREEXPRESSION=59;
    public static final int NAME=16;
    public static final int STRING_LITERAL=41;
    public static final int LEFTCURLY=9;
    public static final int COMMA=32;
    public static final int SEQ=8;
    public static final int PREVIOUS=14;
    public static final int KATTRIBUTE=55;
    public static final int RETURN=7;
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
    public static final int WITHIN=6;
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:1: query : patternPart ( wherePart )? ( withinPart )? ( returnPart )? ;
    public final SaseParser.query_return query() throws RecognitionException {
        SaseParser.query_return retval = new SaseParser.query_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.patternPart_return patternPart1 = null;

        SaseParser.wherePart_return wherePart2 = null;

        SaseParser.withinPart_return withinPart3 = null;

        SaseParser.returnPart_return returnPart4 = null;



        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:7: ( patternPart ( wherePart )? ( withinPart )? ( returnPart )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:10: patternPart ( wherePart )? ( withinPart )? ( returnPart )?
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

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:49: ( returnPart )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==RETURN) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:50: returnPart
                    {
                    pushFollow(FOLLOW_returnPart_in_query129);
                    returnPart4=returnPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, returnPart4.getTree());

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

        Token WITHIN5=null;
        Token NUMBER6=null;
        Token TIMEUNIT7=null;

        Object WITHIN5_tree=null;
        Object NUMBER6_tree=null;
        Object TIMEUNIT7_tree=null;
        RewriteRuleTokenStream stream_WITHIN=new RewriteRuleTokenStream(adaptor,"token WITHIN");
        RewriteRuleTokenStream stream_TIMEUNIT=new RewriteRuleTokenStream(adaptor,"token TIMEUNIT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:36:2: ( WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:36:4: WITHIN NUMBER TIMEUNIT
            {
            WITHIN5=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart144); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_WITHIN.add(WITHIN5);

            NUMBER6=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart146); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER6);

            TIMEUNIT7=(Token)match(input,TIMEUNIT,FOLLOW_TIMEUNIT_in_withinPart148); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TIMEUNIT.add(TIMEUNIT7);



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

        Token WHERE8=null;
        Token LEFTCURLY10=null;
        Token RIGHTCURLY12=null;
        Token WHERE13=null;
        SaseParser.wherePart1_return wherePart19 = null;

        SaseParser.whereExpressions_return whereExpressions11 = null;

        SaseParser.whereExpressions_return whereExpressions14 = null;


        Object WHERE8_tree=null;
        Object LEFTCURLY10_tree=null;
        Object RIGHTCURLY12_tree=null;
        Object WHERE13_tree=null;
        RewriteRuleTokenStream stream_RIGHTCURLY=new RewriteRuleTokenStream(adaptor,"token RIGHTCURLY");
        RewriteRuleTokenStream stream_WHERE=new RewriteRuleTokenStream(adaptor,"token WHERE");
        RewriteRuleTokenStream stream_LEFTCURLY=new RewriteRuleTokenStream(adaptor,"token LEFTCURLY");
        RewriteRuleSubtreeStream stream_wherePart1=new RewriteRuleSubtreeStream(adaptor,"rule wherePart1");
        RewriteRuleSubtreeStream stream_whereExpressions=new RewriteRuleSubtreeStream(adaptor,"rule whereExpressions");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:40:2: ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==WHERE) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==SKIP_METHOD) ) {
                    alt4=1;
                }
                else if ( (LA4_1==NAME||LA4_1==AGGREGATEOP||LA4_1==NUMBER||LA4_1==STRING_LITERAL) ) {
                    alt4=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:40:4: WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    WHERE8=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart170); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE8);

                    pushFollow(FOLLOW_wherePart1_in_wherePart172);
                    wherePart19=wherePart1();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_wherePart1.add(wherePart19.getTree());
                    LEFTCURLY10=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart174); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFTCURLY.add(LEFTCURLY10);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart176);
                    whereExpressions11=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions11.getTree());
                    RIGHTCURLY12=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart178); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHTCURLY.add(RIGHTCURLY12);



                    // AST REWRITE
                    // elements: whereExpressions, wherePart1, WHERE
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
                    WHERE13=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart195); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE13);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart197);
                    whereExpressions14=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions14.getTree());


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

        Token PATTERN15=null;
        SaseParser.patternDecl_return patternDecl16 = null;


        Object PATTERN15_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleSubtreeStream stream_patternDecl=new RewriteRuleSubtreeStream(adaptor,"rule patternDecl");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:2: ( PATTERN patternDecl -> ^( PATTERN patternDecl ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:4: PATTERN patternDecl
            {
            PATTERN15=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart218); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN15);

            pushFollow(FOLLOW_patternDecl_in_patternPart220);
            patternDecl16=patternDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternDecl.add(patternDecl16.getTree());


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

    public static class returnPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "returnPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:48:1: returnPart : RETURN term ( COMMA term )* -> ^( RETURN ( term )* ) ;
    public final SaseParser.returnPart_return returnPart() throws RecognitionException {
        SaseParser.returnPart_return retval = new SaseParser.returnPart_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token RETURN17=null;
        Token COMMA19=null;
        SaseParser.term_return term18 = null;

        SaseParser.term_return term20 = null;


        Object RETURN17_tree=null;
        Object COMMA19_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RETURN=new RewriteRuleTokenStream(adaptor,"token RETURN");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:2: ( RETURN term ( COMMA term )* -> ^( RETURN ( term )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:5: RETURN term ( COMMA term )*
            {
            RETURN17=(Token)match(input,RETURN,FOLLOW_RETURN_in_returnPart241); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RETURN.add(RETURN17);

            pushFollow(FOLLOW_term_in_returnPart243);
            term18=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_term.add(term18.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:17: ( COMMA term )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==COMMA) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:18: COMMA term
            	    {
            	    COMMA19=(Token)match(input,COMMA,FOLLOW_COMMA_in_returnPart246); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA19);

            	    pushFollow(FOLLOW_term_in_returnPart248);
            	    term20=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_term.add(term20.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);



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
            // 49:31: -> ^( RETURN ( term )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:34: ^( RETURN ( term )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_RETURN.nextNode(), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:43: ( term )*
                while ( stream_term.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "returnPart"

    public static class patternDecl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternDecl"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:1: patternDecl : SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) ;
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
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:2: ( SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:4: SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET
            {
            SEQ21=(Token)match(input,SEQ,FOLLOW_SEQ_in_patternDecl271); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEQ.add(SEQ21);

            LBRACKET22=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_patternDecl273); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET22);

            pushFollow(FOLLOW_pItem_in_patternDecl275);
            pItem23=pItem();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_pItem.add(pItem23.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:23: ( COMMA pItem )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==COMMA) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:24: COMMA pItem
            	    {
            	    COMMA24=(Token)match(input,COMMA,FOLLOW_COMMA_in_patternDecl278); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA24);

            	    pushFollow(FOLLOW_pItem_in_patternDecl280);
            	    pItem25=pItem();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_pItem.add(pItem25.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            RBRACKET26=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_patternDecl284); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET26);



            // AST REWRITE
            // elements: SEQ, pItem
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 53:47: -> ^( SEQ ( pItem )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:50: ^( SEQ ( pItem )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_SEQ.nextNode(), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:56: ( pItem )*
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:1: pItem : ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) ;
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
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:8: ( ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:10: ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )?
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:10: ( NOT )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==NOT) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:11: NOT
                    {
                    NOT27=(Token)match(input,NOT,FOLLOW_NOT_in_pItem309); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NOT.add(NOT27);


                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:17: ( LBRACKET )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==LBRACKET) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: LBRACKET
                    {
                    LBRACKET28=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_pItem313); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET28);


                    }
                    break;

            }

            pushFollow(FOLLOW_typeName_in_pItem319);
            type=typeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_typeName.add(type.getTree());
            pushFollow(FOLLOW_attributeName_in_pItem323);
            variable=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(variable.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:65: ( RBRACKET )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==RBRACKET) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==COMMA||LA9_1==RBRACKET) ) {
                    alt9=1;
                }
                else if ( (LA9_1==EOF) ) {
                    alt9=1;
                }
            }
            switch (alt9) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: RBRACKET
                    {
                    RBRACKET29=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_pItem325); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET29);


                    }
                    break;

            }



            // AST REWRITE
            // elements: NOT, variable, type
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
            // 57:75: -> ^( STATE $type $variable ( NOT )? )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:78: ^( STATE $type $variable ( NOT )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STATE, "STATE"), root_1);

                adaptor.addChild(root_1, stream_type.nextTree());
                adaptor.addChild(root_1, stream_variable.nextTree());
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:102: ( NOT )?
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:1: typeName : NAME (op= PLUS )? -> {$op != null}? ^( KTYPE NAME $op) -> ^( TYPE NAME ) ;
    public final SaseParser.typeName_return typeName() throws RecognitionException {
        SaseParser.typeName_return retval = new SaseParser.typeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;
        Token NAME30=null;

        Object op_tree=null;
        Object NAME30_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:9: ( NAME (op= PLUS )? -> {$op != null}? ^( KTYPE NAME $op) -> ^( TYPE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:11: NAME (op= PLUS )?
            {
            NAME30=(Token)match(input,NAME,FOLLOW_NAME_in_typeName352); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME30);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:18: (op= PLUS )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==PLUS) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: op= PLUS
                    {
                    op=(Token)match(input,PLUS,FOLLOW_PLUS_in_typeName356); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PLUS.add(op);


                    }
                    break;

            }



            // AST REWRITE
            // elements: NAME, NAME, op
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
            // 60:25: -> {$op != null}? ^( KTYPE NAME $op)
            if (op != null) {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:43: ^( KTYPE NAME $op)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KTYPE, "KTYPE"), root_1);

                adaptor.addChild(root_1, stream_NAME.nextNode());
                adaptor.addChild(root_1, stream_op.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 61:9: -> ^( TYPE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:12: ^( TYPE NAME )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:1: wherePart1 : SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) ;
    public final SaseParser.wherePart1_return wherePart1() throws RecognitionException {
        SaseParser.wherePart1_return retval = new SaseParser.wherePart1_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SKIP_METHOD31=null;
        Token LBRACKET32=null;
        Token RBRACKET34=null;
        SaseParser.parameterList_return parameterList33 = null;


        Object SKIP_METHOD31_tree=null;
        Object LBRACKET32_tree=null;
        Object RBRACKET34_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SKIP_METHOD=new RewriteRuleTokenStream(adaptor,"token SKIP_METHOD");
        RewriteRuleSubtreeStream stream_parameterList=new RewriteRuleSubtreeStream(adaptor,"rule parameterList");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:2: ( SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:4: SKIP_METHOD LBRACKET parameterList RBRACKET
            {
            SKIP_METHOD31=(Token)match(input,SKIP_METHOD,FOLLOW_SKIP_METHOD_in_wherePart1399); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SKIP_METHOD.add(SKIP_METHOD31);

            LBRACKET32=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_wherePart1401); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET32);

            pushFollow(FOLLOW_parameterList_in_wherePart1403);
            parameterList33=parameterList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_parameterList.add(parameterList33.getTree());
            RBRACKET34=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_wherePart1405); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET34);



            // AST REWRITE
            // elements: SKIP_METHOD, parameterList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 65:48: -> ^( WHERESTRAT SKIP_METHOD parameterList )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:51: ^( WHERESTRAT SKIP_METHOD parameterList )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:68:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
    public final SaseParser.parameterList_return parameterList() throws RecognitionException {
        SaseParser.parameterList_return retval = new SaseParser.parameterList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token COMMA36=null;
        SaseParser.attributeName_return attributeName35 = null;

        SaseParser.attributeName_return attributeName37 = null;


        Object COMMA36_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:4: attributeName ( COMMA attributeName )*
            {
            pushFollow(FOLLOW_attributeName_in_parameterList428);
            attributeName35=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(attributeName35.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:17: ( COMMA attributeName )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==COMMA) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:18: COMMA attributeName
            	    {
            	    COMMA36=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList430); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA36);

            	    pushFollow(FOLLOW_attributeName_in_parameterList432);
            	    attributeName37=attributeName();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeName.add(attributeName37.getTree());

            	    }
            	    break;

            	default :
            	    break loop11;
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
            // 69:40: -> ^( PARAMLIST ( attributeName )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:43: ^( PARAMLIST ( attributeName )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PARAMLIST, "PARAMLIST"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:55: ( attributeName )*
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:1: attributeName : ( kAttributeName | sAttributeName );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.kAttributeName_return kAttributeName38 = null;

        SaseParser.sAttributeName_return sAttributeName39 = null;



        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:2: ( kAttributeName | sAttributeName )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==NAME) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==BBRACKETLEFT) ) {
                    alt12=1;
                }
                else if ( (LA12_1==EOF||LA12_1==COMMA||LA12_1==RBRACKET) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:5: kAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_kAttributeName_in_attributeName456);
                    kAttributeName38=kAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, kAttributeName38.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:20: sAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_sAttributeName_in_attributeName458);
                    sAttributeName39=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sAttributeName39.getTree());

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:76:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) ;
    public final SaseParser.kAttributeName_return kAttributeName() throws RecognitionException {
        SaseParser.kAttributeName_return retval = new SaseParser.kAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME40=null;
        Token BBRACKETLEFT41=null;
        Token BBRACKETRIGHT42=null;

        Object NAME40_tree=null;
        Object BBRACKETLEFT41_tree=null;
        Object BBRACKETRIGHT42_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:2: ( NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            NAME40=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName470); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME40);

            BBRACKETLEFT41=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName473); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT41);

            BBRACKETRIGHT42=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName475); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT42);



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
            // 77:38: -> ^( KATTRIBUTE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:41: ^( KATTRIBUTE NAME )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:1: sAttributeName : NAME -> ^( ATTRIBUTE NAME ) ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME43=null;

        Object NAME43_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:2: ( NAME -> ^( ATTRIBUTE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:4: NAME
            {
            NAME43=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName498); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME43);



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
            // 82:9: -> ^( ATTRIBUTE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:12: ^( ATTRIBUTE NAME )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:1: kAttributeUsage : ( NAME CURRENT | NAME FIRST | NAME PREVIOUS | NAME LAST );
    public final SaseParser.kAttributeUsage_return kAttributeUsage() throws RecognitionException {
        SaseParser.kAttributeUsage_return retval = new SaseParser.kAttributeUsage_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME44=null;
        Token CURRENT45=null;
        Token NAME46=null;
        Token FIRST47=null;
        Token NAME48=null;
        Token PREVIOUS49=null;
        Token NAME50=null;
        Token LAST51=null;

        Object NAME44_tree=null;
        Object CURRENT45_tree=null;
        Object NAME46_tree=null;
        Object FIRST47_tree=null;
        Object NAME48_tree=null;
        Object PREVIOUS49_tree=null;
        Object NAME50_tree=null;
        Object LAST51_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:85:2: ( NAME CURRENT | NAME FIRST | NAME PREVIOUS | NAME LAST )
            int alt13=4;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==NAME) ) {
                switch ( input.LA(2) ) {
                case CURRENT:
                    {
                    alt13=1;
                    }
                    break;
                case FIRST:
                    {
                    alt13=2;
                    }
                    break;
                case PREVIOUS:
                    {
                    alt13=3;
                    }
                    break;
                case LAST:
                    {
                    alt13=4;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;
                }

            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:85:5: NAME CURRENT
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME44=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage517); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME44_tree = (Object)adaptor.create(NAME44);
                    adaptor.addChild(root_0, NAME44_tree);
                    }
                    CURRENT45=(Token)match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage519); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    CURRENT45_tree = (Object)adaptor.create(CURRENT45);
                    adaptor.addChild(root_0, CURRENT45_tree);
                    }

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:3: NAME FIRST
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME46=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage525); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME46_tree = (Object)adaptor.create(NAME46);
                    adaptor.addChild(root_0, NAME46_tree);
                    }
                    FIRST47=(Token)match(input,FIRST,FOLLOW_FIRST_in_kAttributeUsage527); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FIRST47_tree = (Object)adaptor.create(FIRST47);
                    adaptor.addChild(root_0, FIRST47_tree);
                    }

                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:3: NAME PREVIOUS
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME48=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage532); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME48_tree = (Object)adaptor.create(NAME48);
                    adaptor.addChild(root_0, NAME48_tree);
                    }
                    PREVIOUS49=(Token)match(input,PREVIOUS,FOLLOW_PREVIOUS_in_kAttributeUsage534); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PREVIOUS49_tree = (Object)adaptor.create(PREVIOUS49);
                    adaptor.addChild(root_0, PREVIOUS49_tree);
                    }

                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:3: NAME LAST
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME50=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage539); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME50_tree = (Object)adaptor.create(NAME50);
                    adaptor.addChild(root_0, NAME50_tree);
                    }
                    LAST51=(Token)match(input,LAST,FOLLOW_LAST_in_kAttributeUsage541); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LAST51_tree = (Object)adaptor.create(LAST51);
                    adaptor.addChild(root_0, LAST51_tree);
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:1: whereExpressions : expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) ;
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
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:2: ( expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:4: expression ( AND expression )*
            {
            pushFollow(FOLLOW_expression_in_whereExpressions553);
            expression52=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression52.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:15: ( AND expression )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==AND) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:16: AND expression
            	    {
            	    AND53=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions556); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_AND.add(AND53);

            	    pushFollow(FOLLOW_expression_in_whereExpressions558);
            	    expression54=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression54.getTree());

            	    }
            	    break;

            	default :
            	    break loop14;
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
            // 92:33: -> ^( WHEREEXPRESSION ( AND )? ( expression )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:36: ^( WHEREEXPRESSION ( AND )? ( expression )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:54: ( AND )?
                if ( stream_AND.hasNext() ) {
                    adaptor.addChild(root_1, stream_AND.nextNode());

                }
                stream_AND.reset();
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:59: ( expression )*
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:1: expression : ( NAME -> ^( IDEXPRESSION NAME ) | f1= mathExpression SINGLEEQUALS f2= mathExpression -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= mathExpression COMPAREOP f2= mathExpression -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME55=null;
        Token SINGLEEQUALS56=null;
        Token COMPAREOP57=null;
        SaseParser.mathExpression_return f1 = null;

        SaseParser.mathExpression_return f2 = null;


        Object NAME55_tree=null;
        Object SINGLEEQUALS56_tree=null;
        Object COMPAREOP57_tree=null;
        RewriteRuleTokenStream stream_SINGLEEQUALS=new RewriteRuleTokenStream(adaptor,"token SINGLEEQUALS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleSubtreeStream stream_mathExpression=new RewriteRuleSubtreeStream(adaptor,"rule mathExpression");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:2: ( NAME -> ^( IDEXPRESSION NAME ) | f1= mathExpression SINGLEEQUALS f2= mathExpression -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= mathExpression COMPAREOP f2= mathExpression -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) )
            int alt15=3;
            alt15 = dfa15.predict(input);
            switch (alt15) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:4: NAME
                    {
                    NAME55=(Token)match(input,NAME,FOLLOW_NAME_in_expression584); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME55);



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
                    // 96:9: -> ^( IDEXPRESSION NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:12: ^( IDEXPRESSION NAME )
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:35: f1= mathExpression SINGLEEQUALS f2= mathExpression
                    {
                    pushFollow(FOLLOW_mathExpression_in_expression598);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());
                    SINGLEEQUALS56=(Token)match(input,SINGLEEQUALS,FOLLOW_SINGLEEQUALS_in_expression600); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SINGLEEQUALS.add(SINGLEEQUALS56);

                    pushFollow(FOLLOW_mathExpression_in_expression604);
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
                    // 96:84: -> ^( COMPAREEXPRESSION $f1 EQUALS $f2)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:88: ^( COMPAREEXPRESSION $f1 EQUALS $f2)
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:126: f1= mathExpression COMPAREOP f2= mathExpression
                    {
                    pushFollow(FOLLOW_mathExpression_in_expression625);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());
                    COMPAREOP57=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression627); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COMPAREOP.add(COMPAREOP57);

                    pushFollow(FOLLOW_mathExpression_in_expression631);
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
                    // 96:172: -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:175: ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:1: mathExpression : left= term (op= ( MULT | DIVISION ) right= term )* ;
    public final SaseParser.mathExpression_return mathExpression() throws RecognitionException {
        SaseParser.mathExpression_return retval = new SaseParser.mathExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;
        SaseParser.term_return left = null;

        SaseParser.term_return right = null;


        Object op_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:15: (left= term (op= ( MULT | DIVISION ) right= term )* )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:17: left= term (op= ( MULT | DIVISION ) right= term )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_term_in_mathExpression657);
            left=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, left.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:27: (op= ( MULT | DIVISION ) right= term )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( ((LA16_0>=DIVISION && LA16_0<=MULT)) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:28: op= ( MULT | DIVISION ) right= term
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

            	    pushFollow(FOLLOW_term_in_mathExpression670);
            	    right=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, right.getTree());

            	    }
            	    break;

            	default :
            	    break loop16;
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:1: term : ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) | value );
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token aName=null;
        Token member=null;
        Token POINT60=null;
        Token NAME61=null;
        Token POINT62=null;
        SaseParser.aggregation_return aggregation58 = null;

        SaseParser.kAttributeUsage_return kAttributeUsage59 = null;

        SaseParser.value_return value63 = null;


        Object aName_tree=null;
        Object member_tree=null;
        Object POINT60_tree=null;
        Object NAME61_tree=null;
        Object POINT62_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_kAttributeUsage=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeUsage");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:6: ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) | value )
            int alt17=4;
            switch ( input.LA(1) ) {
            case AGGREGATEOP:
                {
                alt17=1;
                }
                break;
            case NAME:
                {
                int LA17_2 = input.LA(2);

                if ( ((LA17_2>=FIRST && LA17_2<=PREVIOUS)||LA17_2==LAST) ) {
                    alt17=2;
                }
                else if ( (LA17_2==POINT) ) {
                    alt17=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 2, input);

                    throw nvae;
                }
                }
                break;
            case NUMBER:
            case STRING_LITERAL:
                {
                alt17=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:8: aggregation
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregation_in_term692);
                    aggregation58=aggregation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregation58.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:3: kAttributeUsage POINT NAME
                    {
                    pushFollow(FOLLOW_kAttributeUsage_in_term698);
                    kAttributeUsage59=kAttributeUsage();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeUsage.add(kAttributeUsage59.getTree());
                    POINT60=(Token)match(input,POINT,FOLLOW_POINT_in_term700); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT60);

                    NAME61=(Token)match(input,NAME,FOLLOW_NAME_in_term702); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME61);



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
                    // 104:30: -> ^( KMEMBER kAttributeUsage NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:33: ^( KMEMBER kAttributeUsage NAME )
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:105:3: aName= NAME POINT member= NAME
                    {
                    aName=(Token)match(input,NAME,FOLLOW_NAME_in_term719); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(aName);

                    POINT62=(Token)match(input,POINT,FOLLOW_POINT_in_term721); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT62);

                    member=(Token)match(input,NAME,FOLLOW_NAME_in_term725); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(member);



                    // AST REWRITE
                    // elements: aName, member
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
                    // 105:32: -> ^( MEMBER $aName $member)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:105:35: ^( MEMBER $aName $member)
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:3: value
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_value_in_term742);
                    value63=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value63.getTree());

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:1: aggregation : AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member) ;
    public final SaseParser.aggregation_return aggregation() throws RecognitionException {
        SaseParser.aggregation_return retval = new SaseParser.aggregation_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token var=null;
        Token member=null;
        Token AGGREGATEOP64=null;
        Token LBRACKET65=null;
        Token ALLTOPREVIOUS66=null;
        Token POINT67=null;
        Token RBRACKET68=null;

        Object var_tree=null;
        Object member_tree=null;
        Object AGGREGATEOP64_tree=null;
        Object LBRACKET65_tree=null;
        Object ALLTOPREVIOUS66_tree=null;
        Object POINT67_tree=null;
        Object RBRACKET68_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_AGGREGATEOP=new RewriteRuleTokenStream(adaptor,"token AGGREGATEOP");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleTokenStream stream_ALLTOPREVIOUS=new RewriteRuleTokenStream(adaptor,"token ALLTOPREVIOUS");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:2: ( AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:4: AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET
            {
            AGGREGATEOP64=(Token)match(input,AGGREGATEOP,FOLLOW_AGGREGATEOP_in_aggregation756); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_AGGREGATEOP.add(AGGREGATEOP64);

            LBRACKET65=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation758); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET65);

            var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation762); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(var);

            ALLTOPREVIOUS66=(Token)match(input,ALLTOPREVIOUS,FOLLOW_ALLTOPREVIOUS_in_aggregation764); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ALLTOPREVIOUS.add(ALLTOPREVIOUS66);

            POINT67=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation766); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_POINT.add(POINT67);

            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation770); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(member);

            RBRACKET68=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation772); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET68);



            // AST REWRITE
            // elements: var, ALLTOPREVIOUS, AGGREGATEOP, member
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
            // 111:75: -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member)
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:78: ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member)
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:114:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set69=null;

        Object set69_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:114:8: ( NUMBER | STRING_LITERAL )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (Object)adaptor.nil();

            set69=(Token)input.LT(1);
            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set69));
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


    protected DFA15 dfa15 = new DFA15(this);
    static final String DFA15_eotS =
        "\47\uffff";
    static final String DFA15_eofS =
        "\1\uffff\1\11\45\uffff";
    static final String DFA15_minS =
        "\1\20\1\6\1\41\1\32\4\31\1\20\1\uffff\2\20\2\uffff\1\20\1\32\1"+
        "\17\1\41\1\14\2\32\1\31\1\20\4\31\2\20\1\17\1\20\1\32\1\42\1\31"+
        "\2\32\1\20\1\42\1\32";
    static final String DFA15_maxS =
        "\1\51\1\31\1\41\1\35\4\31\1\20\1\uffff\1\20\1\51\2\uffff\1\20\1"+
        "\35\1\17\1\41\1\31\2\35\1\31\1\20\4\31\2\20\1\17\1\20\1\35\1\42"+
        "\1\31\2\35\1\20\1\42\1\35";
    static final String DFA15_acceptS =
        "\11\uffff\1\1\2\uffff\1\2\1\3\31\uffff";
    static final String DFA15_specialS =
        "\47\uffff}>";
    static final String[] DFA15_transitionS = {
            "\1\1\5\uffff\1\2\16\uffff\1\3\3\uffff\1\3",
            "\2\11\2\uffff\2\11\1\5\1\4\1\6\2\uffff\1\7\7\uffff\1\10",
            "\1\12",
            "\2\13\1\15\1\14",
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
            "\2\13\1\15\1\14",
            "\1\25",
            "\1\26",
            "\1\30\1\27\1\31\2\uffff\1\32\7\uffff\1\33",
            "\2\13\1\15\1\14",
            "\2\13\1\15\1\14",
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
            "\2\13\1\15\1\14",
            "\1\43",
            "\1\44",
            "\2\13\1\15\1\14",
            "\2\13\1\15\1\14",
            "\1\45",
            "\1\46",
            "\2\13\1\15\1\14"
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
            return "95:1: expression : ( NAME -> ^( IDEXPRESSION NAME ) | f1= mathExpression SINGLEEQUALS f2= mathExpression -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= mathExpression COMPAREOP f2= mathExpression -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) );";
        }
    }
 

    public static final BitSet FOLLOW_patternPart_in_query116 = new BitSet(new long[]{0x00000000000000E2L});
    public static final BitSet FOLLOW_wherePart_in_query119 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_withinPart_in_query124 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_returnPart_in_query129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart144 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart146 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_TIMEUNIT_in_withinPart148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart170 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_wherePart1_in_wherePart172 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart174 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart176 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart195 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart218 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnPart241 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_term_in_returnPart243 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_COMMA_in_returnPart246 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_term_in_returnPart248 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_SEQ_in_patternDecl271 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_LBRACKET_in_patternDecl273 = new BitSet(new long[]{0x0000000280010000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl275 = new BitSet(new long[]{0x0000000500000000L});
    public static final BitSet FOLLOW_COMMA_in_patternDecl278 = new BitSet(new long[]{0x0000000280010000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl280 = new BitSet(new long[]{0x0000000500000000L});
    public static final BitSet FOLLOW_RBRACKET_in_patternDecl284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_pItem309 = new BitSet(new long[]{0x0000000280010000L});
    public static final BitSet FOLLOW_LBRACKET_in_pItem313 = new BitSet(new long[]{0x0000000280010000L});
    public static final BitSet FOLLOW_typeName_in_pItem319 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_attributeName_in_pItem323 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_RBRACKET_in_pItem325 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeName352 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_PLUS_in_typeName356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_METHOD_in_wherePart1399 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_LBRACKET_in_wherePart1401 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_parameterList_in_wherePart1403 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RBRACKET_in_wherePart1405 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList428 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList430 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList432 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName470 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName473 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName475 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage517 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage525 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_FIRST_in_kAttributeUsage527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage532 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_PREVIOUS_in_kAttributeUsage534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage539 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_LAST_in_kAttributeUsage541 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions553 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_whereExpressions556 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_expression_in_whereExpressions558 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_NAME_in_expression584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression598 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_SINGLEEQUALS_in_expression600 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_mathExpression_in_expression604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression625 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression627 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_mathExpression_in_expression631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_mathExpression657 = new BitSet(new long[]{0x000000000C000002L});
    public static final BitSet FOLLOW_set_in_mathExpression662 = new BitSet(new long[]{0x0000022000410000L});
    public static final BitSet FOLLOW_term_in_mathExpression670 = new BitSet(new long[]{0x000000000C000002L});
    public static final BitSet FOLLOW_aggregation_in_term692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_term698 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_term700 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_term702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_term719 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_term721 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_term725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AGGREGATEOP_in_aggregation756 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation758 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_aggregation762 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_ALLTOPREVIOUS_in_aggregation764 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation766 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_aggregation770 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value0 = new BitSet(new long[]{0x0000000000000002L});

}