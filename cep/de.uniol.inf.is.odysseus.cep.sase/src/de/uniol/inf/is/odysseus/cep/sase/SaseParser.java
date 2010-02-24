// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2010-02-24 13:04:02
 
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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CREATE", "STREAM", "PATTERN", "WHERE", "WITHIN", "RETURN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "LEN", "BBRACKETLEFT", "BBRACKETRIGHT", "WEEK", "DAY", "HOUR", "MINUTE", "SECOND", "MILLISECOND", "SKIP_TILL_NEXT_MATCH", "SKIP_TILL_ANY_MATCH", "STRICT_CONTIGUITY", "PARTITION_CONTIGUITY", "AVG", "MIN", "MAX", "SUM", "COUNT", "PLUS", "MINUS", "POINT", "DIVISION", "MULT", "COMPAREOP", "SINGLEEQUALS", "EQUALS", "NOTSIGN", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NAME", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "KSTATE", "STATE", "KTYPE", "TYPE", "WHEREEXPRESSION", "MATHEXPRESSION", "TERM", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "KMEMBER", "MEMBER", "COMPAREEXPRESSION", "IDEXPRESSION", "AGGREGATION", "CREATESTREAM", "QUERY", "NOT", "PREV", "FIRST", "CURRENT"
    };
    public static final int TERM=63;
    public static final int WEEK=17;
    public static final int WHERE=7;
    public static final int MEMBER=68;
    public static final int MILLISECOND=22;
    public static final int KSTATE=57;
    public static final int POINT=34;
    public static final int LETTER=48;
    public static final int ATTRIBUTE=64;
    public static final int MAX=29;
    public static final int EQUALS=39;
    public static final int COUNT=31;
    public static final int DAY=18;
    public static final int FLOAT=45;
    public static final int NOT=74;
    public static final int SUM=30;
    public static final int AND=13;
    public static final int MATHEXPRESSION=62;
    public static final int SPACE=52;
    public static final int EOF=-1;
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
    public static final int MINUS=33;
    public static final int MULT=36;
    public static final int HOUR=19;
    public static final int CURRENT=77;
    public static final int PREV=75;
    public static final int SKIP_TILL_NEXT_MATCH=23;
    public static final int WHEREEXPRESSION=61;
    public static final int NEWLINE=55;
    public static final int NONCONTROL_CHAR=50;
    public static final int AGGREGATION=71;
    public static final int BBRACKETLEFT=15;
    public static final int PARTITION_CONTIGUITY=26;
    public static final int WITHIN=8;
    public static final int SKIP_TILL_ANY_MATCH=24;
    public static final int QUERY=73;
    public static final int LEN=14;
    public static final int KTYPE=59;
    public static final int LOWER=53;
    public static final int MINUTE=20;
    public static final int COMPAREOP=37;
    public static final int STRICT_CONTIGUITY=25;
    public static final int UPPER=54;
    public static final int FIRST=76;

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


    public static class start_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "start"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:39:1: start : ( createStmt | queryStmt );
    public final SaseParser.start_return start() throws RecognitionException {
        SaseParser.start_return retval = new SaseParser.start_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SaseParser.createStmt_return createStmt1 = null;

        SaseParser.queryStmt_return queryStmt2 = null;



        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:39:8: ( createStmt | queryStmt )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==CREATE) ) {
                alt1=1;
            }
            else if ( (LA1_0==PATTERN) ) {
                alt1=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:39:10: createStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_createStmt_in_start146);
                    createStmt1=createStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, createStmt1.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:40:4: queryStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_queryStmt_in_start152);
                    queryStmt2=queryStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, queryStmt2.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "start"

    public static class createStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "createStmt"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:43:1: createStmt : CREATE STREAM NAME queryStmt -> ^( CREATESTREAM NAME queryStmt ) ;
    public final SaseParser.createStmt_return createStmt() throws RecognitionException {
        SaseParser.createStmt_return retval = new SaseParser.createStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token CREATE3=null;
        Token STREAM4=null;
        Token NAME5=null;
        SaseParser.queryStmt_return queryStmt6 = null;


        CommonTree CREATE3_tree=null;
        CommonTree STREAM4_tree=null;
        CommonTree NAME5_tree=null;
        RewriteRuleTokenStream stream_CREATE=new RewriteRuleTokenStream(adaptor,"token CREATE");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_STREAM=new RewriteRuleTokenStream(adaptor,"token STREAM");
        RewriteRuleSubtreeStream stream_queryStmt=new RewriteRuleSubtreeStream(adaptor,"rule queryStmt");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:44:2: ( CREATE STREAM NAME queryStmt -> ^( CREATESTREAM NAME queryStmt ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:44:4: CREATE STREAM NAME queryStmt
            {
            CREATE3=(Token)match(input,CREATE,FOLLOW_CREATE_in_createStmt162); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CREATE.add(CREATE3);

            STREAM4=(Token)match(input,STREAM,FOLLOW_STREAM_in_createStmt164); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STREAM.add(STREAM4);

            NAME5=(Token)match(input,NAME,FOLLOW_NAME_in_createStmt166); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME5);

            pushFollow(FOLLOW_queryStmt_in_createStmt168);
            queryStmt6=queryStmt();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_queryStmt.add(queryStmt6.getTree());


            // AST REWRITE
            // elements: queryStmt, NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 44:33: -> ^( CREATESTREAM NAME queryStmt )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:44:36: ^( CREATESTREAM NAME queryStmt )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CREATESTREAM, "CREATESTREAM"), root_1);

                adaptor.addChild(root_1, stream_NAME.nextNode());
                adaptor.addChild(root_1, stream_queryStmt.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "createStmt"

    public static class queryStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "queryStmt"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:1: queryStmt : patternPart ( wherePart )? ( withinPart )? ( returnPart )? -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? ) ;
    public final SaseParser.queryStmt_return queryStmt() throws RecognitionException {
        SaseParser.queryStmt_return retval = new SaseParser.queryStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SaseParser.patternPart_return patternPart7 = null;

        SaseParser.wherePart_return wherePart8 = null;

        SaseParser.withinPart_return withinPart9 = null;

        SaseParser.returnPart_return returnPart10 = null;


        RewriteRuleSubtreeStream stream_patternPart=new RewriteRuleSubtreeStream(adaptor,"rule patternPart");
        RewriteRuleSubtreeStream stream_returnPart=new RewriteRuleSubtreeStream(adaptor,"rule returnPart");
        RewriteRuleSubtreeStream stream_withinPart=new RewriteRuleSubtreeStream(adaptor,"rule withinPart");
        RewriteRuleSubtreeStream stream_wherePart=new RewriteRuleSubtreeStream(adaptor,"rule wherePart");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:10: ( patternPart ( wherePart )? ( withinPart )? ( returnPart )? -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:13: patternPart ( wherePart )? ( withinPart )? ( returnPart )?
            {
            pushFollow(FOLLOW_patternPart_in_queryStmt188);
            patternPart7=patternPart();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternPart.add(patternPart7.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:25: ( wherePart )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==WHERE) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: wherePart
                    {
                    pushFollow(FOLLOW_wherePart_in_queryStmt190);
                    wherePart8=wherePart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_wherePart.add(wherePart8.getTree());

                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:36: ( withinPart )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==WITHIN) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: withinPart
                    {
                    pushFollow(FOLLOW_withinPart_in_queryStmt193);
                    withinPart9=withinPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_withinPart.add(withinPart9.getTree());

                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:48: ( returnPart )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==RETURN) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: returnPart
                    {
                    pushFollow(FOLLOW_returnPart_in_queryStmt196);
                    returnPart10=returnPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_returnPart.add(returnPart10.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: withinPart, wherePart, returnPart, patternPart
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 47:60: -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:63: ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUERY, "QUERY"), root_1);

                adaptor.addChild(root_1, stream_patternPart.nextTree());
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:83: ( wherePart )?
                if ( stream_wherePart.hasNext() ) {
                    adaptor.addChild(root_1, stream_wherePart.nextTree());

                }
                stream_wherePart.reset();
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:94: ( withinPart )?
                if ( stream_withinPart.hasNext() ) {
                    adaptor.addChild(root_1, stream_withinPart.nextTree());

                }
                stream_withinPart.reset();
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:106: ( returnPart )?
                if ( stream_returnPart.hasNext() ) {
                    adaptor.addChild(root_1, stream_returnPart.nextTree());

                }
                stream_returnPart.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "queryStmt"

    public static class withinPart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "withinPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:1: withinPart : WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) ;
    public final SaseParser.withinPart_return withinPart() throws RecognitionException {
        SaseParser.withinPart_return retval = new SaseParser.withinPart_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token WITHIN11=null;
        Token NUMBER12=null;
        SaseParser.timeunit_return timeunit13 = null;


        CommonTree WITHIN11_tree=null;
        CommonTree NUMBER12_tree=null;
        RewriteRuleTokenStream stream_WITHIN=new RewriteRuleTokenStream(adaptor,"token WITHIN");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
        RewriteRuleSubtreeStream stream_timeunit=new RewriteRuleSubtreeStream(adaptor,"rule timeunit");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:2: ( WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:4: WITHIN NUMBER ( timeunit )?
            {
            WITHIN11=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart229); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_WITHIN.add(WITHIN11);

            NUMBER12=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart231); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER12);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:18: ( timeunit )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( ((LA5_0>=WEEK && LA5_0<=MILLISECOND)) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: timeunit
                    {
                    pushFollow(FOLLOW_timeunit_in_withinPart233);
                    timeunit13=timeunit();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_timeunit.add(timeunit13.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: NUMBER, timeunit, WITHIN
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 52:28: -> ^( WITHIN NUMBER ( timeunit )? )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:31: ^( WITHIN NUMBER ( timeunit )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_WITHIN.nextNode(), root_1);

                adaptor.addChild(root_1, stream_NUMBER.nextNode());
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:47: ( timeunit )?
                if ( stream_timeunit.hasNext() ) {
                    adaptor.addChild(root_1, stream_timeunit.nextTree());

                }
                stream_timeunit.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "withinPart"

    public static class timeunit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "timeunit"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:55:1: timeunit : ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND );
    public final SaseParser.timeunit_return timeunit() throws RecognitionException {
        SaseParser.timeunit_return retval = new SaseParser.timeunit_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set14=null;

        CommonTree set14_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:55:9: ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set14=(Token)input.LT(1);
            if ( (input.LA(1)>=WEEK && input.LA(1)<=MILLISECOND) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set14));
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

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "timeunit"

    public static class wherePart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "wherePart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:1: wherePart : ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
    public final SaseParser.wherePart_return wherePart() throws RecognitionException {
        SaseParser.wherePart_return retval = new SaseParser.wherePart_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token WHERE15=null;
        Token LEFTCURLY17=null;
        Token RIGHTCURLY19=null;
        Token WHERE20=null;
        SaseParser.skipPart_return skipPart16 = null;

        SaseParser.whereExpressions_return whereExpressions18 = null;

        SaseParser.whereExpressions_return whereExpressions21 = null;


        CommonTree WHERE15_tree=null;
        CommonTree LEFTCURLY17_tree=null;
        CommonTree RIGHTCURLY19_tree=null;
        CommonTree WHERE20_tree=null;
        RewriteRuleTokenStream stream_RIGHTCURLY=new RewriteRuleTokenStream(adaptor,"token RIGHTCURLY");
        RewriteRuleTokenStream stream_WHERE=new RewriteRuleTokenStream(adaptor,"token WHERE");
        RewriteRuleTokenStream stream_LEFTCURLY=new RewriteRuleTokenStream(adaptor,"token LEFTCURLY");
        RewriteRuleSubtreeStream stream_skipPart=new RewriteRuleSubtreeStream(adaptor,"rule skipPart");
        RewriteRuleSubtreeStream stream_whereExpressions=new RewriteRuleSubtreeStream(adaptor,"rule whereExpressions");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:2: ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WHERE) ) {
                int LA6_1 = input.LA(2);

                if ( ((LA6_1>=SKIP_TILL_NEXT_MATCH && LA6_1<=PARTITION_CONTIGUITY)) ) {
                    alt6=1;
                }
                else if ( (LA6_1==BBRACKETLEFT||(LA6_1>=AVG && LA6_1<=COUNT)||LA6_1==LBRACKET||LA6_1==NUMBER||LA6_1==NAME||LA6_1==STRING_LITERAL) ) {
                    alt6=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:4: WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    WHERE15=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart275); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE15);

                    pushFollow(FOLLOW_skipPart_in_wherePart277);
                    skipPart16=skipPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_skipPart.add(skipPart16.getTree());
                    LEFTCURLY17=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart279); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFTCURLY.add(LEFTCURLY17);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart281);
                    whereExpressions18=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions18.getTree());
                    RIGHTCURLY19=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart283); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHTCURLY.add(RIGHTCURLY19);



                    // AST REWRITE
                    // elements: whereExpressions, skipPart, WHERE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 58:57: -> ^( WHERE skipPart whereExpressions )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:60: ^( WHERE skipPart whereExpressions )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_skipPart.nextTree());
                        adaptor.addChild(root_1, stream_whereExpressions.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:59:4: WHERE whereExpressions
                    {
                    WHERE20=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart300); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE20);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart302);
                    whereExpressions21=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions21.getTree());


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

                    root_0 = (CommonTree)adaptor.nil();
                    // 59:27: -> ^( WHERE whereExpressions )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:59:30: ^( WHERE whereExpressions )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

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

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "wherePart"

    public static class skipPart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "skipPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:1: skipPart : skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) ;
    public final SaseParser.skipPart_return skipPart() throws RecognitionException {
        SaseParser.skipPart_return retval = new SaseParser.skipPart_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACKET23=null;
        Token RBRACKET25=null;
        SaseParser.skipMethod_return skipMethod22 = null;

        SaseParser.parameterList_return parameterList24 = null;


        CommonTree LBRACKET23_tree=null;
        CommonTree RBRACKET25_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_skipMethod=new RewriteRuleSubtreeStream(adaptor,"rule skipMethod");
        RewriteRuleSubtreeStream stream_parameterList=new RewriteRuleSubtreeStream(adaptor,"rule parameterList");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:63:2: ( skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:63:4: skipMethod LBRACKET parameterList RBRACKET
            {
            pushFollow(FOLLOW_skipMethod_in_skipPart321);
            skipMethod22=skipMethod();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_skipMethod.add(skipMethod22.getTree());
            LBRACKET23=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_skipPart323); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET23);

            pushFollow(FOLLOW_parameterList_in_skipPart325);
            parameterList24=parameterList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_parameterList.add(parameterList24.getTree());
            RBRACKET25=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_skipPart327); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET25);



            // AST REWRITE
            // elements: parameterList, skipMethod
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 64:2: -> ^( skipMethod parameterList )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:5: ^( skipMethod parameterList )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_skipMethod.nextNode(), root_1);

                adaptor.addChild(root_1, stream_parameterList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "skipPart"

    public static class skipMethod_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "skipMethod"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:1: skipMethod : ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY );
    public final SaseParser.skipMethod_return skipMethod() throws RecognitionException {
        SaseParser.skipMethod_return retval = new SaseParser.skipMethod_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set26=null;

        CommonTree set26_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:11: ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set26=(Token)input.LT(1);
            if ( (input.LA(1)>=SKIP_TILL_NEXT_MATCH && input.LA(1)<=PARTITION_CONTIGUITY) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set26));
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

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "skipMethod"

    public static class patternPart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:1: patternPart : PATTERN patternDecl -> ^( patternDecl ) ;
    public final SaseParser.patternPart_return patternPart() throws RecognitionException {
        SaseParser.patternPart_return retval = new SaseParser.patternPart_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token PATTERN27=null;
        SaseParser.patternDecl_return patternDecl28 = null;


        CommonTree PATTERN27_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleSubtreeStream stream_patternDecl=new RewriteRuleSubtreeStream(adaptor,"rule patternDecl");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:2: ( PATTERN patternDecl -> ^( patternDecl ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:4: PATTERN patternDecl
            {
            PATTERN27=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart376); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN27);

            pushFollow(FOLLOW_patternDecl_in_patternPart378);
            patternDecl28=patternDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternDecl.add(patternDecl28.getTree());


            // AST REWRITE
            // elements: patternDecl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 74:24: -> ^( patternDecl )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:27: ^( patternDecl )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_patternDecl.nextNode(), root_1);

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "patternPart"

    public static class returnPart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "returnPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:1: returnPart : RETURN attributeTerm ( COMMA attributeTerm )* -> ^( RETURN ( attributeTerm )* ) ;
    public final SaseParser.returnPart_return returnPart() throws RecognitionException {
        SaseParser.returnPart_return retval = new SaseParser.returnPart_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token RETURN29=null;
        Token COMMA31=null;
        SaseParser.attributeTerm_return attributeTerm30 = null;

        SaseParser.attributeTerm_return attributeTerm32 = null;


        CommonTree RETURN29_tree=null;
        CommonTree COMMA31_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RETURN=new RewriteRuleTokenStream(adaptor,"token RETURN");
        RewriteRuleSubtreeStream stream_attributeTerm=new RewriteRuleSubtreeStream(adaptor,"rule attributeTerm");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:2: ( RETURN attributeTerm ( COMMA attributeTerm )* -> ^( RETURN ( attributeTerm )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:5: RETURN attributeTerm ( COMMA attributeTerm )*
            {
            RETURN29=(Token)match(input,RETURN,FOLLOW_RETURN_in_returnPart397); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RETURN.add(RETURN29);

            pushFollow(FOLLOW_attributeTerm_in_returnPart399);
            attributeTerm30=attributeTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeTerm.add(attributeTerm30.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:26: ( COMMA attributeTerm )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==COMMA) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:27: COMMA attributeTerm
            	    {
            	    COMMA31=(Token)match(input,COMMA,FOLLOW_COMMA_in_returnPart402); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA31);

            	    pushFollow(FOLLOW_attributeTerm_in_returnPart404);
            	    attributeTerm32=attributeTerm();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeTerm.add(attributeTerm32.getTree());

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);



            // AST REWRITE
            // elements: RETURN, attributeTerm
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 78:49: -> ^( RETURN ( attributeTerm )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:52: ^( RETURN ( attributeTerm )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_RETURN.nextNode(), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:61: ( attributeTerm )*
                while ( stream_attributeTerm.hasNext() ) {
                    adaptor.addChild(root_1, stream_attributeTerm.nextTree());

                }
                stream_attributeTerm.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "returnPart"

    public static class patternDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternDecl"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:1: patternDecl : SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) ;
    public final SaseParser.patternDecl_return patternDecl() throws RecognitionException {
        SaseParser.patternDecl_return retval = new SaseParser.patternDecl_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token SEQ33=null;
        Token LBRACKET34=null;
        Token COMMA36=null;
        Token RBRACKET38=null;
        SaseParser.stateDef_return stateDef35 = null;

        SaseParser.stateDef_return stateDef37 = null;


        CommonTree SEQ33_tree=null;
        CommonTree LBRACKET34_tree=null;
        CommonTree COMMA36_tree=null;
        CommonTree RBRACKET38_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SEQ=new RewriteRuleTokenStream(adaptor,"token SEQ");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_stateDef=new RewriteRuleSubtreeStream(adaptor,"rule stateDef");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:2: ( SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:4: SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET
            {
            SEQ33=(Token)match(input,SEQ,FOLLOW_SEQ_in_patternDecl427); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEQ.add(SEQ33);

            LBRACKET34=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_patternDecl429); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET34);

            pushFollow(FOLLOW_stateDef_in_patternDecl431);
            stateDef35=stateDef();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_stateDef.add(stateDef35.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:26: ( COMMA stateDef )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==COMMA) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:27: COMMA stateDef
            	    {
            	    COMMA36=(Token)match(input,COMMA,FOLLOW_COMMA_in_patternDecl434); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA36);

            	    pushFollow(FOLLOW_stateDef_in_patternDecl436);
            	    stateDef37=stateDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_stateDef.add(stateDef37.getTree());

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            RBRACKET38=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_patternDecl440); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET38);



            // AST REWRITE
            // elements: SEQ, stateDef
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 82:53: -> ^( SEQ ( stateDef )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:56: ^( SEQ ( stateDef )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_SEQ.nextNode(), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:62: ( stateDef )*
                while ( stream_stateDef.hasNext() ) {
                    adaptor.addChild(root_1, stream_stateDef.nextTree());

                }
                stream_stateDef.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "patternDecl"

    public static class stateDef_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "stateDef"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:1: stateDef : ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) );
    public final SaseParser.stateDef_return stateDef() throws RecognitionException {
        SaseParser.stateDef_return retval = new SaseParser.stateDef_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NOTSIGN39=null;
        Token NOTSIGN41=null;
        Token NOTSIGN43=null;
        Token LBRACKET44=null;
        Token RBRACKET46=null;
        Token NOTSIGN47=null;
        Token LBRACKET48=null;
        Token RBRACKET50=null;
        SaseParser.ktypeDefinition_return ktypeDefinition40 = null;

        SaseParser.typeDefinition_return typeDefinition42 = null;

        SaseParser.ktypeDefinition_return ktypeDefinition45 = null;

        SaseParser.typeDefinition_return typeDefinition49 = null;


        CommonTree NOTSIGN39_tree=null;
        CommonTree NOTSIGN41_tree=null;
        CommonTree NOTSIGN43_tree=null;
        CommonTree LBRACKET44_tree=null;
        CommonTree RBRACKET46_tree=null;
        CommonTree NOTSIGN47_tree=null;
        CommonTree LBRACKET48_tree=null;
        CommonTree RBRACKET50_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NOTSIGN=new RewriteRuleTokenStream(adaptor,"token NOTSIGN");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_typeDefinition=new RewriteRuleSubtreeStream(adaptor,"rule typeDefinition");
        RewriteRuleSubtreeStream stream_ktypeDefinition=new RewriteRuleSubtreeStream(adaptor,"rule ktypeDefinition");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:10: ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) )
            int alt13=4;
            switch ( input.LA(1) ) {
            case NOTSIGN:
                {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==NAME) ) {
                    int LA13_2 = input.LA(3);

                    if ( (LA13_2==PLUS) ) {
                        alt13=1;
                    }
                    else if ( (LA13_2==NAME) ) {
                        alt13=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 13, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA13_1==LBRACKET) ) {
                    int LA13_3 = input.LA(3);

                    if ( (LA13_3==NAME) ) {
                        int LA13_6 = input.LA(4);

                        if ( (LA13_6==PLUS) ) {
                            alt13=3;
                        }
                        else if ( (LA13_6==NAME) ) {
                            alt13=4;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 13, 6, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 13, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;
                }
                }
                break;
            case NAME:
                {
                int LA13_2 = input.LA(2);

                if ( (LA13_2==PLUS) ) {
                    alt13=1;
                }
                else if ( (LA13_2==NAME) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 2, input);

                    throw nvae;
                }
                }
                break;
            case LBRACKET:
                {
                int LA13_3 = input.LA(2);

                if ( (LA13_3==NAME) ) {
                    int LA13_6 = input.LA(3);

                    if ( (LA13_6==PLUS) ) {
                        alt13=3;
                    }
                    else if ( (LA13_6==NAME) ) {
                        alt13=4;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 13, 6, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:12: ( NOTSIGN )? ktypeDefinition
                    {
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:12: ( NOTSIGN )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==NOTSIGN) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:13: NOTSIGN
                            {
                            NOTSIGN39=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef464); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN39);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef468);
                    ktypeDefinition40=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition40.getTree());


                    // AST REWRITE
                    // elements: ktypeDefinition, NOTSIGN
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 86:40: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:43: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(KSTATE, "KSTATE"), root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:68: ( NOTSIGN )?
                        if ( stream_NOTSIGN.hasNext() ) {
                            adaptor.addChild(root_1, stream_NOTSIGN.nextNode());

                        }
                        stream_NOTSIGN.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:5: ( NOTSIGN )? typeDefinition
                    {
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:5: ( NOTSIGN )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==NOTSIGN) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:6: NOTSIGN
                            {
                            NOTSIGN41=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef487); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN41);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_typeDefinition_in_stateDef491);
                    typeDefinition42=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition42.getTree());


                    // AST REWRITE
                    // elements: NOTSIGN, typeDefinition
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 87:32: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:35: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STATE, "STATE"), root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:58: ( NOTSIGN )?
                        if ( stream_NOTSIGN.hasNext() ) {
                            adaptor.addChild(root_1, stream_NOTSIGN.nextNode());

                        }
                        stream_NOTSIGN.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:5: ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET
                    {
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:5: ( NOTSIGN )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==NOTSIGN) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:6: NOTSIGN
                            {
                            NOTSIGN43=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef512); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN43);


                            }
                            break;

                    }

                    LBRACKET44=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef516); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET44);

                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef518);
                    ktypeDefinition45=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition45.getTree());
                    RBRACKET46=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef520); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET46);



                    // AST REWRITE
                    // elements: ktypeDefinition, NOTSIGN
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 88:50: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:53: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(KSTATE, "KSTATE"), root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:78: ( NOTSIGN )?
                        if ( stream_NOTSIGN.hasNext() ) {
                            adaptor.addChild(root_1, stream_NOTSIGN.nextNode());

                        }
                        stream_NOTSIGN.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:5: ( NOTSIGN )? LBRACKET typeDefinition RBRACKET
                    {
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:5: ( NOTSIGN )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==NOTSIGN) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:6: NOTSIGN
                            {
                            NOTSIGN47=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef538); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN47);


                            }
                            break;

                    }

                    LBRACKET48=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef542); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET48);

                    pushFollow(FOLLOW_typeDefinition_in_stateDef544);
                    typeDefinition49=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition49.getTree());
                    RBRACKET50=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef546); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET50);



                    // AST REWRITE
                    // elements: NOTSIGN, typeDefinition
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 89:49: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:52: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STATE, "STATE"), root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:75: ( NOTSIGN )?
                        if ( stream_NOTSIGN.hasNext() ) {
                            adaptor.addChild(root_1, stream_NOTSIGN.nextNode());

                        }
                        stream_NOTSIGN.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "stateDef"

    public static class typeDefinition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeDefinition"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:1: typeDefinition : NAME sAttributeName ;
    public final SaseParser.typeDefinition_return typeDefinition() throws RecognitionException {
        SaseParser.typeDefinition_return retval = new SaseParser.typeDefinition_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NAME51=null;
        SaseParser.sAttributeName_return sAttributeName52 = null;


        CommonTree NAME51_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:2: ( NAME sAttributeName )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:4: NAME sAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();

            NAME51=(Token)match(input,NAME,FOLLOW_NAME_in_typeDefinition571); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME51_tree = (CommonTree)adaptor.create(NAME51);
            adaptor.addChild(root_0, NAME51_tree);
            }
            pushFollow(FOLLOW_sAttributeName_in_typeDefinition573);
            sAttributeName52=sAttributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, sAttributeName52.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "typeDefinition"

    public static class ktypeDefinition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ktypeDefinition"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:1: ktypeDefinition : NAME PLUS kAttributeName ;
    public final SaseParser.ktypeDefinition_return ktypeDefinition() throws RecognitionException {
        SaseParser.ktypeDefinition_return retval = new SaseParser.ktypeDefinition_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NAME53=null;
        Token PLUS54=null;
        SaseParser.kAttributeName_return kAttributeName55 = null;


        CommonTree NAME53_tree=null;
        CommonTree PLUS54_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:97:2: ( NAME PLUS kAttributeName )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:97:4: NAME PLUS kAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();

            NAME53=(Token)match(input,NAME,FOLLOW_NAME_in_ktypeDefinition584); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME53_tree = (CommonTree)adaptor.create(NAME53);
            adaptor.addChild(root_0, NAME53_tree);
            }
            PLUS54=(Token)match(input,PLUS,FOLLOW_PLUS_in_ktypeDefinition586); if (state.failed) return retval;
            pushFollow(FOLLOW_kAttributeName_in_ktypeDefinition589);
            kAttributeName55=kAttributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, kAttributeName55.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ktypeDefinition"

    public static class parameterList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameterList"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
    public final SaseParser.parameterList_return parameterList() throws RecognitionException {
        SaseParser.parameterList_return retval = new SaseParser.parameterList_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COMMA57=null;
        SaseParser.attributeName_return attributeName56 = null;

        SaseParser.attributeName_return attributeName58 = null;


        CommonTree COMMA57_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:4: attributeName ( COMMA attributeName )*
            {
            pushFollow(FOLLOW_attributeName_in_parameterList603);
            attributeName56=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(attributeName56.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:18: ( COMMA attributeName )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==COMMA) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:19: COMMA attributeName
            	    {
            	    COMMA57=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList606); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA57);

            	    pushFollow(FOLLOW_attributeName_in_parameterList608);
            	    attributeName58=attributeName();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeName.add(attributeName58.getTree());

            	    }
            	    break;

            	default :
            	    break loop14;
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

            root_0 = (CommonTree)adaptor.nil();
            // 102:41: -> ^( PARAMLIST ( attributeName )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:44: ^( PARAMLIST ( attributeName )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PARAMLIST, "PARAMLIST"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:56: ( attributeName )*
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

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "parameterList"

    public static class attributeName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "attributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:105:1: attributeName : ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SaseParser.kAttributeName_return kAttributeName59 = null;

        SaseParser.sAttributeName_return sAttributeName60 = null;


        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_kAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeName");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:2: ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==NAME) ) {
                int LA15_1 = input.LA(2);

                if ( (LA15_1==BBRACKETLEFT) ) {
                    alt15=1;
                }
                else if ( (LA15_1==EOF||LA15_1==COMMA||LA15_1==RBRACKET) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:5: kAttributeName
                    {
                    pushFollow(FOLLOW_kAttributeName_in_attributeName632);
                    kAttributeName59=kAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeName.add(kAttributeName59.getTree());


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 106:20: -> ^( KATTRIBUTE NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:23: ^( KATTRIBUTE NAME )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(KATTRIBUTE, "KATTRIBUTE"), root_1);

                        adaptor.addChild(root_1, (CommonTree)adaptor.create(NAME, "NAME"));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:107:4: sAttributeName
                    {
                    pushFollow(FOLLOW_sAttributeName_in_attributeName646);
                    sAttributeName60=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sAttributeName.add(sAttributeName60.getTree());


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 107:19: -> ^( ATTRIBUTE NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:107:22: ^( ATTRIBUTE NAME )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ATTRIBUTE, "ATTRIBUTE"), root_1);

                        adaptor.addChild(root_1, (CommonTree)adaptor.create(NAME, "NAME"));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "attributeName"

    public static class kAttributeName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kAttributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT ;
    public final SaseParser.kAttributeName_return kAttributeName() throws RecognitionException {
        SaseParser.kAttributeName_return retval = new SaseParser.kAttributeName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NAME61=null;
        Token BBRACKETLEFT62=null;
        Token BBRACKETRIGHT63=null;

        CommonTree NAME61_tree=null;
        CommonTree BBRACKETLEFT62_tree=null;
        CommonTree BBRACKETRIGHT63_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:2: ( NAME BBRACKETLEFT BBRACKETRIGHT )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();

            NAME61=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName668); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME61_tree = (CommonTree)adaptor.create(NAME61);
            adaptor.addChild(root_0, NAME61_tree);
            }
            BBRACKETLEFT62=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName671); if (state.failed) return retval;
            BBRACKETRIGHT63=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName674); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "kAttributeName"

    public static class sAttributeName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sAttributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:114:1: sAttributeName : NAME ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NAME64=null;

        CommonTree NAME64_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:115:2: ( NAME )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:115:4: NAME
            {
            root_0 = (CommonTree)adaptor.nil();

            NAME64=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName689); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME64_tree = (CommonTree)adaptor.create(NAME64);
            adaptor.addChild(root_0, NAME64_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sAttributeName"

    public static class kAttributeUsage_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kAttributeUsage"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:118:1: kAttributeUsage : ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) );
    public final SaseParser.kAttributeUsage_return kAttributeUsage() throws RecognitionException {
        SaseParser.kAttributeUsage_return retval = new SaseParser.kAttributeUsage_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NAME65=null;
        Token NAME67=null;
        Token NAME69=null;
        Token NAME71=null;
        SaseParser.current_return current66 = null;

        SaseParser.first_return first68 = null;

        SaseParser.last_return last70 = null;

        SaseParser.len_return len72 = null;


        CommonTree NAME65_tree=null;
        CommonTree NAME67_tree=null;
        CommonTree NAME69_tree=null;
        CommonTree NAME71_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleSubtreeStream stream_last=new RewriteRuleSubtreeStream(adaptor,"rule last");
        RewriteRuleSubtreeStream stream_current=new RewriteRuleSubtreeStream(adaptor,"rule current");
        RewriteRuleSubtreeStream stream_len=new RewriteRuleSubtreeStream(adaptor,"rule len");
        RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:119:2: ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) )
            int alt16=4;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==NAME) ) {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==BBRACKETLEFT) ) {
                    int LA16_2 = input.LA(3);

                    if ( (LA16_2==NAME) ) {
                        switch ( input.LA(4) ) {
                        case BBRACKETRIGHT:
                            {
                            alt16=1;
                            }
                            break;
                        case MINUS:
                            {
                            alt16=3;
                            }
                            break;
                        case POINT:
                            {
                            alt16=4;
                            }
                            break;
                        default:
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 3, input);

                            throw nvae;
                        }

                    }
                    else if ( (LA16_2==NUMBER) ) {
                        alt16=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:119:4: NAME current
                    {
                    NAME65=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage702); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME65);

                    pushFollow(FOLLOW_current_in_kAttributeUsage705);
                    current66=current();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_current.add(current66.getTree());


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

                    root_0 = (CommonTree)adaptor.nil();
                    // 119:19: -> ^( NAME CURRENT )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:119:22: ^( NAME CURRENT )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NAME.nextNode(), root_1);

                        adaptor.addChild(root_1, (CommonTree)adaptor.create(CURRENT, "CURRENT"));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:120:4: NAME first
                    {
                    NAME67=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage719); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME67);

                    pushFollow(FOLLOW_first_in_kAttributeUsage721);
                    first68=first();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_first.add(first68.getTree());


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

                    root_0 = (CommonTree)adaptor.nil();
                    // 120:15: -> ^( NAME FIRST )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:120:18: ^( NAME FIRST )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NAME.nextNode(), root_1);

                        adaptor.addChild(root_1, (CommonTree)adaptor.create(FIRST, "FIRST"));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:121:4: NAME last
                    {
                    NAME69=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage734); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME69);

                    pushFollow(FOLLOW_last_in_kAttributeUsage736);
                    last70=last();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_last.add(last70.getTree());


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

                    root_0 = (CommonTree)adaptor.nil();
                    // 121:14: -> ^( NAME PREV )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:121:17: ^( NAME PREV )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NAME.nextNode(), root_1);

                        adaptor.addChild(root_1, (CommonTree)adaptor.create(PREV, "PREV"));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:122:4: NAME len
                    {
                    NAME71=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage749); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME71);

                    pushFollow(FOLLOW_len_in_kAttributeUsage751);
                    len72=len();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_len.add(len72.getTree());


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

                    root_0 = (CommonTree)adaptor.nil();
                    // 122:13: -> ^( NAME LEN )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:122:16: ^( NAME LEN )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NAME.nextNode(), root_1);

                        adaptor.addChild(root_1, (CommonTree)adaptor.create(LEN, "LEN"));

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "kAttributeUsage"

    public static class current_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "current"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:125:1: current : BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?;
    public final SaseParser.current_return current() throws RecognitionException {
        SaseParser.current_return retval = new SaseParser.current_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token name=null;
        Token BBRACKETLEFT73=null;
        Token BBRACKETRIGHT74=null;

        CommonTree name_tree=null;
        CommonTree BBRACKETLEFT73_tree=null;
        CommonTree BBRACKETRIGHT74_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:125:9: ( BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?)
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:2: BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();

            BBRACKETLEFT73=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_current772); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT73_tree = (CommonTree)adaptor.create(BBRACKETLEFT73);
            adaptor.addChild(root_0, BBRACKETLEFT73_tree);
            }
            name=(Token)match(input,NAME,FOLLOW_NAME_in_current776); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = (CommonTree)adaptor.create(name);
            adaptor.addChild(root_0, name_tree);
            }
            BBRACKETRIGHT74=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_current778); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT74_tree = (CommonTree)adaptor.create(BBRACKETRIGHT74);
            adaptor.addChild(root_0, BBRACKETRIGHT74_tree);
            }
            if ( !((name.getText().equalsIgnoreCase("I"))) ) {
                if (state.backtracking>0) {state.failed=true; return retval;}
                throw new FailedPredicateException(input, "current", "$name.getText().equalsIgnoreCase(\"I\")");
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "current"

    public static class first_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "first"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:129:1: first : BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?;
    public final SaseParser.first_return first() throws RecognitionException {
        SaseParser.first_return retval = new SaseParser.first_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token number=null;
        Token BBRACKETLEFT75=null;
        Token BBRACKETRIGHT76=null;

        CommonTree number_tree=null;
        CommonTree BBRACKETLEFT75_tree=null;
        CommonTree BBRACKETRIGHT76_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:129:7: ( BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?)
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:130:2: BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();

            BBRACKETLEFT75=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_first793); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT75_tree = (CommonTree)adaptor.create(BBRACKETLEFT75);
            adaptor.addChild(root_0, BBRACKETLEFT75_tree);
            }
            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_first797); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = (CommonTree)adaptor.create(number);
            adaptor.addChild(root_0, number_tree);
            }
            BBRACKETRIGHT76=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_first799); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT76_tree = (CommonTree)adaptor.create(BBRACKETRIGHT76);
            adaptor.addChild(root_0, BBRACKETRIGHT76_tree);
            }
            if ( !((Integer.parseInt(number.getText()) == 1)) ) {
                if (state.backtracking>0) {state.failed=true; return retval;}
                throw new FailedPredicateException(input, "first", "Integer.parseInt($number.getText()) == 1");
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "first"

    public static class last_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "last"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:133:1: last : BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?;
    public final SaseParser.last_return last() throws RecognitionException {
        SaseParser.last_return retval = new SaseParser.last_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token name=null;
        Token number=null;
        Token BBRACKETLEFT77=null;
        Token MINUS78=null;
        Token BBRACKETRIGHT79=null;

        CommonTree name_tree=null;
        CommonTree number_tree=null;
        CommonTree BBRACKETLEFT77_tree=null;
        CommonTree MINUS78_tree=null;
        CommonTree BBRACKETRIGHT79_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:133:6: ( BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?)
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:134:2: BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();

            BBRACKETLEFT77=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_last812); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT77_tree = (CommonTree)adaptor.create(BBRACKETLEFT77);
            adaptor.addChild(root_0, BBRACKETLEFT77_tree);
            }
            name=(Token)match(input,NAME,FOLLOW_NAME_in_last816); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = (CommonTree)adaptor.create(name);
            adaptor.addChild(root_0, name_tree);
            }
            MINUS78=(Token)match(input,MINUS,FOLLOW_MINUS_in_last818); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            MINUS78_tree = (CommonTree)adaptor.create(MINUS78);
            adaptor.addChild(root_0, MINUS78_tree);
            }
            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_last822); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = (CommonTree)adaptor.create(number);
            adaptor.addChild(root_0, number_tree);
            }
            BBRACKETRIGHT79=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_last824); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT79_tree = (CommonTree)adaptor.create(BBRACKETRIGHT79);
            adaptor.addChild(root_0, BBRACKETRIGHT79_tree);
            }
            if ( !((name.getText().equalsIgnoreCase("I") && Integer.parseInt(number.getText()) == 1)) ) {
                if (state.backtracking>0) {state.failed=true; return retval;}
                throw new FailedPredicateException(input, "last", "$name.getText().equalsIgnoreCase(\"I\") && Integer.parseInt($number.getText()) == 1");
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "last"

    public static class len_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "len"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:137:1: len : BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT ;
    public final SaseParser.len_return len() throws RecognitionException {
        SaseParser.len_return retval = new SaseParser.len_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token BBRACKETLEFT80=null;
        Token NAME81=null;
        Token POINT82=null;
        Token LEN83=null;
        Token BBRACKETRIGHT84=null;

        CommonTree BBRACKETLEFT80_tree=null;
        CommonTree NAME81_tree=null;
        CommonTree POINT82_tree=null;
        CommonTree LEN83_tree=null;
        CommonTree BBRACKETRIGHT84_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:137:5: ( BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:138:2: BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();

            BBRACKETLEFT80=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_len840); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT80_tree = (CommonTree)adaptor.create(BBRACKETLEFT80);
            adaptor.addChild(root_0, BBRACKETLEFT80_tree);
            }
            NAME81=(Token)match(input,NAME,FOLLOW_NAME_in_len842); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME81_tree = (CommonTree)adaptor.create(NAME81);
            adaptor.addChild(root_0, NAME81_tree);
            }
            POINT82=(Token)match(input,POINT,FOLLOW_POINT_in_len844); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            POINT82_tree = (CommonTree)adaptor.create(POINT82);
            adaptor.addChild(root_0, POINT82_tree);
            }
            LEN83=(Token)match(input,LEN,FOLLOW_LEN_in_len846); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEN83_tree = (CommonTree)adaptor.create(LEN83);
            adaptor.addChild(root_0, LEN83_tree);
            }
            BBRACKETRIGHT84=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_len848); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT84_tree = (CommonTree)adaptor.create(BBRACKETRIGHT84);
            adaptor.addChild(root_0, BBRACKETRIGHT84_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "len"

    public static class whereExpressions_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "whereExpressions"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:141:1: whereExpressions : expression ( AND expression )* -> ^( WHEREEXPRESSION ( expression )* ) ;
    public final SaseParser.whereExpressions_return whereExpressions() throws RecognitionException {
        SaseParser.whereExpressions_return retval = new SaseParser.whereExpressions_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token AND86=null;
        SaseParser.expression_return expression85 = null;

        SaseParser.expression_return expression87 = null;


        CommonTree AND86_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:142:2: ( expression ( AND expression )* -> ^( WHEREEXPRESSION ( expression )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:142:4: expression ( AND expression )*
            {
            pushFollow(FOLLOW_expression_in_whereExpressions861);
            expression85=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression85.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:142:15: ( AND expression )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==AND) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:142:16: AND expression
            	    {
            	    AND86=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions864); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_AND.add(AND86);

            	    pushFollow(FOLLOW_expression_in_whereExpressions866);
            	    expression87=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression87.getTree());

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);



            // AST REWRITE
            // elements: expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 142:33: -> ^( WHEREEXPRESSION ( expression )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:142:36: ^( WHEREEXPRESSION ( expression )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:142:54: ( expression )*
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

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "whereExpressions"

    public static class expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:145:1: expression : ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) | f1= mathExpression (op= SINGLEEQUALS | op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token op=null;
        Token BBRACKETLEFT88=null;
        Token NAME89=null;
        Token BBRACKETRIGHT90=null;
        SaseParser.mathExpression_return f1 = null;

        SaseParser.mathExpression_return f2 = null;


        CommonTree op_tree=null;
        CommonTree BBRACKETLEFT88_tree=null;
        CommonTree NAME89_tree=null;
        CommonTree BBRACKETRIGHT90_tree=null;
        RewriteRuleTokenStream stream_SINGLEEQUALS=new RewriteRuleTokenStream(adaptor,"token SINGLEEQUALS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");
        RewriteRuleSubtreeStream stream_mathExpression=new RewriteRuleSubtreeStream(adaptor,"rule mathExpression");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:146:2: ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) | f1= mathExpression (op= SINGLEEQUALS | op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==BBRACKETLEFT) ) {
                alt19=1;
            }
            else if ( ((LA19_0>=AVG && LA19_0<=COUNT)||LA19_0==LBRACKET||LA19_0==NUMBER||LA19_0==NAME||LA19_0==STRING_LITERAL) ) {
                alt19=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:146:4: BBRACKETLEFT NAME BBRACKETRIGHT
                    {
                    BBRACKETLEFT88=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_expression889); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT88);

                    NAME89=(Token)match(input,NAME,FOLLOW_NAME_in_expression891); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME89);

                    BBRACKETRIGHT90=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_expression893); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT90);



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

                    root_0 = (CommonTree)adaptor.nil();
                    // 146:36: -> ^( IDEXPRESSION NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:146:39: ^( IDEXPRESSION NAME )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IDEXPRESSION, "IDEXPRESSION"), root_1);

                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:147:3: f1= mathExpression (op= SINGLEEQUALS | op= COMPAREOP ) f2= mathExpression
                    {
                    pushFollow(FOLLOW_mathExpression_in_expression909);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:147:21: (op= SINGLEEQUALS | op= COMPAREOP )
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==SINGLEEQUALS) ) {
                        alt18=1;
                    }
                    else if ( (LA18_0==COMPAREOP) ) {
                        alt18=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 0, input);

                        throw nvae;
                    }
                    switch (alt18) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:147:22: op= SINGLEEQUALS
                            {
                            op=(Token)match(input,SINGLEEQUALS,FOLLOW_SINGLEEQUALS_in_expression914); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_SINGLEEQUALS.add(op);


                            }
                            break;
                        case 2 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:147:38: op= COMPAREOP
                            {
                            op=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression918); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COMPAREOP.add(op);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_mathExpression_in_expression923);
                    f2=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f2.getTree());


                    // AST REWRITE
                    // elements: f1, f2, op
                    // token labels: op
                    // rule labels: retval, f1, f2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);
                    RewriteRuleSubtreeStream stream_f2=new RewriteRuleSubtreeStream(adaptor,"rule f2",f2!=null?f2.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 147:70: -> ^( COMPAREEXPRESSION $f1 $op $f2)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:147:74: ^( COMPAREEXPRESSION $f1 $op $f2)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COMPAREEXPRESSION, "COMPAREEXPRESSION"), root_1);

                        adaptor.addChild(root_1, stream_f1.nextTree());
                        adaptor.addChild(root_1, stream_op.nextNode());
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

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class mathExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mathExpression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:1: mathExpression : mult ( ( PLUS | MINUS ) mult )* ;
    public final SaseParser.mathExpression_return mathExpression() throws RecognitionException {
        SaseParser.mathExpression_return retval = new SaseParser.mathExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token PLUS92=null;
        Token MINUS93=null;
        SaseParser.mult_return mult91 = null;

        SaseParser.mult_return mult94 = null;


        CommonTree PLUS92_tree=null;
        CommonTree MINUS93_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:15: ( mult ( ( PLUS | MINUS ) mult )* )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:17: mult ( ( PLUS | MINUS ) mult )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_mult_in_mathExpression949);
            mult91=mult();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mult91.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:22: ( ( PLUS | MINUS ) mult )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( ((LA21_0>=PLUS && LA21_0<=MINUS)) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:23: ( PLUS | MINUS ) mult
            	    {
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:23: ( PLUS | MINUS )
            	    int alt20=2;
            	    int LA20_0 = input.LA(1);

            	    if ( (LA20_0==PLUS) ) {
            	        alt20=1;
            	    }
            	    else if ( (LA20_0==MINUS) ) {
            	        alt20=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 20, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt20) {
            	        case 1 :
            	            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:24: PLUS
            	            {
            	            PLUS92=(Token)match(input,PLUS,FOLLOW_PLUS_in_mathExpression953); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            PLUS92_tree = (CommonTree)adaptor.create(PLUS92);
            	            root_0 = (CommonTree)adaptor.becomeRoot(PLUS92_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:30: MINUS
            	            {
            	            MINUS93=(Token)match(input,MINUS,FOLLOW_MINUS_in_mathExpression956); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MINUS93_tree = (CommonTree)adaptor.create(MINUS93);
            	            root_0 = (CommonTree)adaptor.becomeRoot(MINUS93_tree, root_0);
            	            }

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_mult_in_mathExpression960);
            	    mult94=mult();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, mult94.getTree());

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "mathExpression"

    public static class mult_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "mult"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:1: mult : term ( ( MULT | DIVISION ) term )* ;
    public final SaseParser.mult_return mult() throws RecognitionException {
        SaseParser.mult_return retval = new SaseParser.mult_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token MULT96=null;
        Token DIVISION97=null;
        SaseParser.term_return term95 = null;

        SaseParser.term_return term98 = null;


        CommonTree MULT96_tree=null;
        CommonTree DIVISION97_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:7: ( term ( ( MULT | DIVISION ) term )* )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:9: term ( ( MULT | DIVISION ) term )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_term_in_mult974);
            term95=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, term95.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:14: ( ( MULT | DIVISION ) term )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( ((LA23_0>=DIVISION && LA23_0<=MULT)) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:15: ( MULT | DIVISION ) term
            	    {
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:15: ( MULT | DIVISION )
            	    int alt22=2;
            	    int LA22_0 = input.LA(1);

            	    if ( (LA22_0==MULT) ) {
            	        alt22=1;
            	    }
            	    else if ( (LA22_0==DIVISION) ) {
            	        alt22=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 22, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt22) {
            	        case 1 :
            	            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:16: MULT
            	            {
            	            MULT96=(Token)match(input,MULT,FOLLOW_MULT_in_mult978); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MULT96_tree = (CommonTree)adaptor.create(MULT96);
            	            root_0 = (CommonTree)adaptor.becomeRoot(MULT96_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:22: DIVISION
            	            {
            	            DIVISION97=(Token)match(input,DIVISION,FOLLOW_DIVISION_in_mult981); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            DIVISION97_tree = (CommonTree)adaptor.create(DIVISION97);
            	            root_0 = (CommonTree)adaptor.becomeRoot(DIVISION97_tree, root_0);
            	            }

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_term_in_mult985);
            	    term98=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, term98.getTree());

            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "mult"

    public static class term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:1: term : ( attributeTerm | value | LBRACKET mathExpression RBRACKET );
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACKET101=null;
        Token RBRACKET103=null;
        SaseParser.attributeTerm_return attributeTerm99 = null;

        SaseParser.value_return value100 = null;

        SaseParser.mathExpression_return mathExpression102 = null;


        CommonTree LBRACKET101_tree=null;
        CommonTree RBRACKET103_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:5: ( attributeTerm | value | LBRACKET mathExpression RBRACKET )
            int alt24=3;
            switch ( input.LA(1) ) {
            case AVG:
            case MIN:
            case MAX:
            case SUM:
            case COUNT:
            case NAME:
                {
                alt24=1;
                }
                break;
            case NUMBER:
            case STRING_LITERAL:
                {
                alt24=2;
                }
                break;
            case LBRACKET:
                {
                alt24=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }

            switch (alt24) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:7: attributeTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_attributeTerm_in_term998);
                    attributeTerm99=attributeTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attributeTerm99.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:158:3: value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_value_in_term1004);
                    value100=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value100.getTree());

                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:159:3: LBRACKET mathExpression RBRACKET
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LBRACKET101=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_term1009); if (state.failed) return retval;
                    pushFollow(FOLLOW_mathExpression_in_term1012);
                    mathExpression102=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mathExpression102.getTree());
                    RBRACKET103=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_term1014); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "term"

    public static class attributeTerm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "attributeTerm"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:162:1: attributeTerm : ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) );
    public final SaseParser.attributeTerm_return attributeTerm() throws RecognitionException {
        SaseParser.attributeTerm_return retval = new SaseParser.attributeTerm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token aName=null;
        Token member=null;
        Token POINT106=null;
        Token NAME107=null;
        Token POINT108=null;
        SaseParser.aggregation_return aggregation104 = null;

        SaseParser.kAttributeUsage_return kAttributeUsage105 = null;


        CommonTree aName_tree=null;
        CommonTree member_tree=null;
        CommonTree POINT106_tree=null;
        CommonTree NAME107_tree=null;
        CommonTree POINT108_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_kAttributeUsage=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeUsage");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:162:14: ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) )
            int alt25=3;
            int LA25_0 = input.LA(1);

            if ( ((LA25_0>=AVG && LA25_0<=COUNT)) ) {
                alt25=1;
            }
            else if ( (LA25_0==NAME) ) {
                int LA25_2 = input.LA(2);

                if ( (LA25_2==POINT) ) {
                    alt25=3;
                }
                else if ( (LA25_2==BBRACKETLEFT) ) {
                    alt25=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 25, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:162:16: aggregation
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_aggregation_in_attributeTerm1026);
                    aggregation104=aggregation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregation104.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:163:5: kAttributeUsage POINT NAME
                    {
                    pushFollow(FOLLOW_kAttributeUsage_in_attributeTerm1034);
                    kAttributeUsage105=kAttributeUsage();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeUsage.add(kAttributeUsage105.getTree());
                    POINT106=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1036); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT106);

                    NAME107=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1038); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME107);



                    // AST REWRITE
                    // elements: NAME, kAttributeUsage
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 163:32: -> ^( KMEMBER kAttributeUsage NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:163:35: ^( KMEMBER kAttributeUsage NAME )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(KMEMBER, "KMEMBER"), root_1);

                        adaptor.addChild(root_1, stream_kAttributeUsage.nextTree());
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:164:5: aName= NAME POINT member= NAME
                    {
                    aName=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1057); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(aName);

                    POINT108=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1059); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT108);

                    member=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1063); if (state.failed) return retval; 
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

                    root_0 = (CommonTree)adaptor.nil();
                    // 164:34: -> ^( MEMBER $aName $member)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:164:37: ^( MEMBER $aName $member)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(MEMBER, "MEMBER"), root_1);

                        adaptor.addChild(root_1, stream_aName.nextNode());
                        adaptor.addChild(root_1, stream_member.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "attributeTerm"

    public static class aggregation_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "aggregation"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:167:1: aggregation : ( saveaggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION saveaggop $var $member) | AVG LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( DIVISION ^( AGGREGATION SUM $var $member) ^( AGGREGATION COUNT $var $member) ) | saveaggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION saveaggop $var ( $member)? ) | AVG LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( DIVISION ^( AGGREGATION SUM $var ( $member)? ) ^( AGGREGATION COUNT $var ( $member)? ) ) );
    public final SaseParser.aggregation_return aggregation() throws RecognitionException {
        SaseParser.aggregation_return retval = new SaseParser.aggregation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token var=null;
        Token name=null;
        Token number=null;
        Token member=null;
        Token LBRACKET110=null;
        Token BBRACKETLEFT111=null;
        Token POINT112=null;
        Token POINT113=null;
        Token MINUS114=null;
        Token BBRACKETRIGHT115=null;
        Token POINT116=null;
        Token RBRACKET117=null;
        Token AVG118=null;
        Token LBRACKET119=null;
        Token BBRACKETLEFT120=null;
        Token POINT121=null;
        Token POINT122=null;
        Token MINUS123=null;
        Token BBRACKETRIGHT124=null;
        Token POINT125=null;
        Token RBRACKET126=null;
        Token LBRACKET128=null;
        Token BBRACKETLEFT129=null;
        Token BBRACKETRIGHT130=null;
        Token POINT131=null;
        Token RBRACKET132=null;
        Token AVG133=null;
        Token LBRACKET134=null;
        Token BBRACKETLEFT135=null;
        Token BBRACKETRIGHT136=null;
        Token POINT137=null;
        Token RBRACKET138=null;
        SaseParser.saveaggop_return saveaggop109 = null;

        SaseParser.saveaggop_return saveaggop127 = null;


        CommonTree var_tree=null;
        CommonTree name_tree=null;
        CommonTree number_tree=null;
        CommonTree member_tree=null;
        CommonTree LBRACKET110_tree=null;
        CommonTree BBRACKETLEFT111_tree=null;
        CommonTree POINT112_tree=null;
        CommonTree POINT113_tree=null;
        CommonTree MINUS114_tree=null;
        CommonTree BBRACKETRIGHT115_tree=null;
        CommonTree POINT116_tree=null;
        CommonTree RBRACKET117_tree=null;
        CommonTree AVG118_tree=null;
        CommonTree LBRACKET119_tree=null;
        CommonTree BBRACKETLEFT120_tree=null;
        CommonTree POINT121_tree=null;
        CommonTree POINT122_tree=null;
        CommonTree MINUS123_tree=null;
        CommonTree BBRACKETRIGHT124_tree=null;
        CommonTree POINT125_tree=null;
        CommonTree RBRACKET126_tree=null;
        CommonTree LBRACKET128_tree=null;
        CommonTree BBRACKETLEFT129_tree=null;
        CommonTree BBRACKETRIGHT130_tree=null;
        CommonTree POINT131_tree=null;
        CommonTree RBRACKET132_tree=null;
        CommonTree AVG133_tree=null;
        CommonTree LBRACKET134_tree=null;
        CommonTree BBRACKETLEFT135_tree=null;
        CommonTree BBRACKETRIGHT136_tree=null;
        CommonTree POINT137_tree=null;
        CommonTree RBRACKET138_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleTokenStream stream_AVG=new RewriteRuleTokenStream(adaptor,"token AVG");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
        RewriteRuleSubtreeStream stream_saveaggop=new RewriteRuleSubtreeStream(adaptor,"rule saveaggop");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:168:2: ( saveaggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION saveaggop $var $member) | AVG LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( DIVISION ^( AGGREGATION SUM $var $member) ^( AGGREGATION COUNT $var $member) ) | saveaggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION saveaggop $var ( $member)? ) | AVG LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( DIVISION ^( AGGREGATION SUM $var ( $member)? ) ^( AGGREGATION COUNT $var ( $member)? ) ) )
            int alt28=4;
            alt28 = dfa28.predict(input);
            switch (alt28) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:168:5: saveaggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}?
                    {
                    pushFollow(FOLLOW_saveaggop_in_aggregation1088);
                    saveaggop109=saveaggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_saveaggop.add(saveaggop109.getTree());
                    LBRACKET110=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1090); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET110);

                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1094); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);

                    BBRACKETLEFT111=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1096); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT111);

                    POINT112=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1098); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT112);

                    POINT113=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1100); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT113);

                    name=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1104); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(name);

                    MINUS114=(Token)match(input,MINUS,FOLLOW_MINUS_in_aggregation1106); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MINUS.add(MINUS114);

                    number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_aggregation1110); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(number);

                    BBRACKETRIGHT115=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1112); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT115);

                    POINT116=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1114); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT116);

                    member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1118); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(member);

                    RBRACKET117=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1120); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET117);

                    if ( !((name.getText().equalsIgnoreCase("I") && Integer.parseInt(number.getText()) == 1)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "aggregation", "$name.getText().equalsIgnoreCase(\"I\") && Integer.parseInt($number.getText()) == 1");
                    }


                    // AST REWRITE
                    // elements: member, saveaggop, var
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

                    root_0 = (CommonTree)adaptor.nil();
                    // 169:4: -> ^( AGGREGATION saveaggop $var $member)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:169:7: ^( AGGREGATION saveaggop $var $member)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AGGREGATION, "AGGREGATION"), root_1);

                        adaptor.addChild(root_1, stream_saveaggop.nextTree());
                        adaptor.addChild(root_1, stream_var.nextNode());
                        adaptor.addChild(root_1, stream_member.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:170:6: AVG LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}?
                    {
                    AVG118=(Token)match(input,AVG,FOLLOW_AVG_in_aggregation1147); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AVG.add(AVG118);

                    LBRACKET119=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1149); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET119);

                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1153); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);

                    BBRACKETLEFT120=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1155); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT120);

                    POINT121=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1157); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT121);

                    POINT122=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1159); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT122);

                    name=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1163); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(name);

                    MINUS123=(Token)match(input,MINUS,FOLLOW_MINUS_in_aggregation1165); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MINUS.add(MINUS123);

                    number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_aggregation1169); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(number);

                    BBRACKETRIGHT124=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1171); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT124);

                    POINT125=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1173); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT125);

                    member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1177); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(member);

                    RBRACKET126=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1179); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET126);

                    if ( !((name.getText().equalsIgnoreCase("I") && Integer.parseInt(number.getText()) == 1)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "aggregation", "$name.getText().equalsIgnoreCase(\"I\") && Integer.parseInt($number.getText()) == 1");
                    }


                    // AST REWRITE
                    // elements: var, member, var, member
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

                    root_0 = (CommonTree)adaptor.nil();
                    // 171:4: -> ^( DIVISION ^( AGGREGATION SUM $var $member) ^( AGGREGATION COUNT $var $member) )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:7: ^( DIVISION ^( AGGREGATION SUM $var $member) ^( AGGREGATION COUNT $var $member) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(DIVISION, "DIVISION"), root_1);

                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:18: ^( AGGREGATION SUM $var $member)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AGGREGATION, "AGGREGATION"), root_2);

                        adaptor.addChild(root_2, (CommonTree)adaptor.create(SUM, "SUM"));
                        adaptor.addChild(root_2, stream_var.nextNode());
                        adaptor.addChild(root_2, stream_member.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:50: ^( AGGREGATION COUNT $var $member)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AGGREGATION, "AGGREGATION"), root_2);

                        adaptor.addChild(root_2, (CommonTree)adaptor.create(COUNT, "COUNT"));
                        adaptor.addChild(root_2, stream_var.nextNode());
                        adaptor.addChild(root_2, stream_member.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:5: saveaggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET
                    {
                    pushFollow(FOLLOW_saveaggop_in_aggregation1224);
                    saveaggop127=saveaggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_saveaggop.add(saveaggop127.getTree());
                    LBRACKET128=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1226); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET128);

                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1230); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);

                    BBRACKETLEFT129=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1232); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT129);

                    BBRACKETRIGHT130=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1234); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT130);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:60: ( POINT member= NAME )?
                    int alt26=2;
                    int LA26_0 = input.LA(1);

                    if ( (LA26_0==POINT) ) {
                        alt26=1;
                    }
                    switch (alt26) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:61: POINT member= NAME
                            {
                            POINT131=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1237); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_POINT.add(POINT131);

                            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1241); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NAME.add(member);


                            }
                            break;

                    }

                    RBRACKET132=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1245); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET132);



                    // AST REWRITE
                    // elements: saveaggop, member, var
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

                    root_0 = (CommonTree)adaptor.nil();
                    // 173:4: -> ^( AGGREGATION saveaggop $var ( $member)? )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:173:7: ^( AGGREGATION saveaggop $var ( $member)? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AGGREGATION, "AGGREGATION"), root_1);

                        adaptor.addChild(root_1, stream_saveaggop.nextTree());
                        adaptor.addChild(root_1, stream_var.nextNode());
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:173:36: ( $member)?
                        if ( stream_member.hasNext() ) {
                            adaptor.addChild(root_1, stream_member.nextNode());

                        }
                        stream_member.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:174:6: AVG LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET
                    {
                    AVG133=(Token)match(input,AVG,FOLLOW_AVG_in_aggregation1271); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AVG.add(AVG133);

                    LBRACKET134=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1273); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET134);

                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1277); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);

                    BBRACKETLEFT135=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1279); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT135);

                    BBRACKETRIGHT136=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1281); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT136);

                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:174:55: ( POINT member= NAME )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==POINT) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:174:56: POINT member= NAME
                            {
                            POINT137=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1284); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_POINT.add(POINT137);

                            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1288); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NAME.add(member);


                            }
                            break;

                    }

                    RBRACKET138=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1292); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET138);



                    // AST REWRITE
                    // elements: var, member, member, var
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

                    root_0 = (CommonTree)adaptor.nil();
                    // 175:4: -> ^( DIVISION ^( AGGREGATION SUM $var ( $member)? ) ^( AGGREGATION COUNT $var ( $member)? ) )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:7: ^( DIVISION ^( AGGREGATION SUM $var ( $member)? ) ^( AGGREGATION COUNT $var ( $member)? ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(DIVISION, "DIVISION"), root_1);

                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:18: ^( AGGREGATION SUM $var ( $member)? )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AGGREGATION, "AGGREGATION"), root_2);

                        adaptor.addChild(root_2, (CommonTree)adaptor.create(SUM, "SUM"));
                        adaptor.addChild(root_2, stream_var.nextNode());
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:41: ( $member)?
                        if ( stream_member.hasNext() ) {
                            adaptor.addChild(root_2, stream_member.nextNode());

                        }
                        stream_member.reset();

                        adaptor.addChild(root_1, root_2);
                        }
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:51: ^( AGGREGATION COUNT $var ( $member)? )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AGGREGATION, "AGGREGATION"), root_2);

                        adaptor.addChild(root_2, (CommonTree)adaptor.create(COUNT, "COUNT"));
                        adaptor.addChild(root_2, stream_var.nextNode());
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:76: ( $member)?
                        if ( stream_member.hasNext() ) {
                            adaptor.addChild(root_2, stream_member.nextNode());

                        }
                        stream_member.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "aggregation"

    public static class saveaggop_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "saveaggop"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:1: saveaggop : ( MIN | MAX | SUM | COUNT );
    public final SaseParser.saveaggop_return saveaggop() throws RecognitionException {
        SaseParser.saveaggop_return retval = new SaseParser.saveaggop_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set139=null;

        CommonTree set139_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:179:2: ( MIN | MAX | SUM | COUNT )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set139=(Token)input.LT(1);
            if ( (input.LA(1)>=MIN && input.LA(1)<=COUNT) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set139));
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

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "saveaggop"

    public static class value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:182:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set140=null;

        CommonTree set140_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:182:8: ( NUMBER | STRING_LITERAL )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set140=(Token)input.LT(1);
            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set140));
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

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "value"

    // Delegated rules


    protected DFA28 dfa28 = new DFA28(this);
    static final String DFA28_eotS =
        "\15\uffff";
    static final String DFA28_eofS =
        "\15\uffff";
    static final String DFA28_minS =
        "\1\33\2\52\2\61\2\17\2\20\4\uffff";
    static final String DFA28_maxS =
        "\1\37\2\52\2\61\2\17\2\42\4\uffff";
    static final String DFA28_acceptS =
        "\11\uffff\1\3\1\1\1\2\1\4";
    static final String DFA28_specialS =
        "\15\uffff}>";
    static final String[] DFA28_transitionS = {
            "\1\2\4\1",
            "\1\3",
            "\1\4",
            "\1\5",
            "\1\6",
            "\1\7",
            "\1\10",
            "\1\11\21\uffff\1\12",
            "\1\14\21\uffff\1\13",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA28_eot = DFA.unpackEncodedString(DFA28_eotS);
    static final short[] DFA28_eof = DFA.unpackEncodedString(DFA28_eofS);
    static final char[] DFA28_min = DFA.unpackEncodedStringToUnsignedChars(DFA28_minS);
    static final char[] DFA28_max = DFA.unpackEncodedStringToUnsignedChars(DFA28_maxS);
    static final short[] DFA28_accept = DFA.unpackEncodedString(DFA28_acceptS);
    static final short[] DFA28_special = DFA.unpackEncodedString(DFA28_specialS);
    static final short[][] DFA28_transition;

    static {
        int numStates = DFA28_transitionS.length;
        DFA28_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA28_transition[i] = DFA.unpackEncodedString(DFA28_transitionS[i]);
        }
    }

    class DFA28 extends DFA {

        public DFA28(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 28;
            this.eot = DFA28_eot;
            this.eof = DFA28_eof;
            this.min = DFA28_min;
            this.max = DFA28_max;
            this.accept = DFA28_accept;
            this.special = DFA28_special;
            this.transition = DFA28_transition;
        }
        public String getDescription() {
            return "167:1: aggregation : ( saveaggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION saveaggop $var $member) | AVG LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( DIVISION ^( AGGREGATION SUM $var $member) ^( AGGREGATION COUNT $var $member) ) | saveaggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION saveaggop $var ( $member)? ) | AVG LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( DIVISION ^( AGGREGATION SUM $var ( $member)? ) ^( AGGREGATION COUNT $var ( $member)? ) ) );";
        }
    }
 

    public static final BitSet FOLLOW_createStmt_in_start146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_queryStmt_in_start152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_createStmt162 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STREAM_in_createStmt164 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_createStmt166 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_queryStmt_in_createStmt168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_patternPart_in_queryStmt188 = new BitSet(new long[]{0x0000000000000382L});
    public static final BitSet FOLLOW_wherePart_in_queryStmt190 = new BitSet(new long[]{0x0000000000000302L});
    public static final BitSet FOLLOW_withinPart_in_queryStmt193 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_returnPart_in_queryStmt196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart229 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart231 = new BitSet(new long[]{0x00000000007E0002L});
    public static final BitSet FOLLOW_timeunit_in_withinPart233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_timeunit0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart275 = new BitSet(new long[]{0x0000000007800000L});
    public static final BitSet FOLLOW_skipPart_in_wherePart277 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart279 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart281 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart300 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_skipMethod_in_skipPart321 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_skipPart323 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_parameterList_in_skipPart325 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_skipPart327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_skipMethod0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart376 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnPart397 = new BitSet(new long[]{0x00020000F8000000L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart399 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_COMMA_in_returnPart402 = new BitSet(new long[]{0x00020000F8000000L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart404 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_SEQ_in_patternDecl427 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_patternDecl429 = new BitSet(new long[]{0x0002050000000000L});
    public static final BitSet FOLLOW_stateDef_in_patternDecl431 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_COMMA_in_patternDecl434 = new BitSet(new long[]{0x0002050000000000L});
    public static final BitSet FOLLOW_stateDef_in_patternDecl436 = new BitSet(new long[]{0x00000A0000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_patternDecl440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef464 = new BitSet(new long[]{0x0002010000000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef487 = new BitSet(new long[]{0x0002010000000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef512 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef516 = new BitSet(new long[]{0x0002010000000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef518 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef538 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef542 = new BitSet(new long[]{0x0002010000000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef544 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeDefinition571 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_sAttributeName_in_typeDefinition573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_ktypeDefinition584 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_PLUS_in_ktypeDefinition586 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_kAttributeName_in_ktypeDefinition589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList603 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList606 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList608 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName668 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName671 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage702 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_current_in_kAttributeUsage705 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage719 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_first_in_kAttributeUsage721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage734 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_last_in_kAttributeUsage736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage749 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_len_in_kAttributeUsage751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_current772 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_current776 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_current778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_first793 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NUMBER_in_first797 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_first799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_last812 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_last816 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_MINUS_in_last818 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NUMBER_in_last822 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_last824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_len840 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_len842 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_POINT_in_len844 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_LEN_in_len846 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_len848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions861 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_AND_in_whereExpressions864 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_expression_in_whereExpressions866 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_expression889 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_expression891 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_expression893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression909 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_SINGLEEQUALS_in_expression914 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression918 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_mathExpression_in_expression923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mult_in_mathExpression949 = new BitSet(new long[]{0x0000000300000002L});
    public static final BitSet FOLLOW_PLUS_in_mathExpression953 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_MINUS_in_mathExpression956 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_mult_in_mathExpression960 = new BitSet(new long[]{0x0000000300000002L});
    public static final BitSet FOLLOW_term_in_mult974 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_MULT_in_mult978 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_DIVISION_in_mult981 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_term_in_mult985 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_attributeTerm_in_term998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term1004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_term1009 = new BitSet(new long[]{0x000A4400F8008000L});
    public static final BitSet FOLLOW_mathExpression_in_term1012 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_term1014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregation_in_attributeTerm1026 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_attributeTerm1034 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1036 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1038 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1057 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1059 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_saveaggop_in_aggregation1088 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1090 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1094 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1096 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1098 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1100 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1104 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_MINUS_in_aggregation1106 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NUMBER_in_aggregation1110 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1112 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1114 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1118 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AVG_in_aggregation1147 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1149 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1153 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1155 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1157 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1159 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1163 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_MINUS_in_aggregation1165 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NUMBER_in_aggregation1169 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1171 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1173 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1177 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_saveaggop_in_aggregation1224 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1226 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1230 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1232 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1234 = new BitSet(new long[]{0x0000080400000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1237 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1241 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AVG_in_aggregation1271 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1273 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1277 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1279 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1281 = new BitSet(new long[]{0x0000080400000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1284 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1288 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_saveaggop0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value0 = new BitSet(new long[]{0x0000000000000002L});

}