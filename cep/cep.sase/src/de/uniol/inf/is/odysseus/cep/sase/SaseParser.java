// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2010-10-25 09:18:27
 
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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CREATE", "STREAM", "VIEW", "PATTERN", "WHERE", "WITHIN", "RETURN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "LEN", "BBRACKETLEFT", "BBRACKETRIGHT", "WEEK", "DAY", "HOUR", "MINUTE", "SECOND", "MILLISECOND", "SKIP_TILL_NEXT_MATCH", "SKIP_TILL_ANY_MATCH", "STRICT_CONTIGUITY", "PARTITION_CONTIGUITY", "AVG", "MIN", "MAX", "SUM", "COUNT", "PLUS", "MINUS", "POINT", "DIVISION", "MULT", "COMPAREOP", "SINGLEEQUALS", "EQUALS", "ASSIGN", "NOTSIGN", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NAME", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "KSTATE", "STATE", "KTYPE", "TYPE", "WHEREEXPRESSION", "MATHEXPRESSION", "TERM", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "KMEMBER", "MEMBER", "COMPAREEXPRESSION", "ASSIGNMENT", "IDEXPRESSION", "AGGREGATION", "CREATEVIEW", "QUERY", "NOT", "PREV", "FIRST", "CURRENT", "KOMMA"
    };
    public static final int TERM=65;
    public static final int WEEK=18;
    public static final int WHERE=8;
    public static final int MEMBER=70;
    public static final int MILLISECOND=23;
    public static final int KSTATE=59;
    public static final int POINT=35;
    public static final int LETTER=50;
    public static final int ATTRIBUTE=66;
    public static final int MAX=30;
    public static final int EQUALS=40;
    public static final int COUNT=32;
    public static final int DAY=19;
    public static final int FLOAT=47;
    public static final int NOT=77;
    public static final int SUM=31;
    public static final int AND=14;
    public static final int MATHEXPRESSION=64;
    public static final int SPACE=54;
    public static final int EOF=-1;
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
    public static final int MINUS=34;
    public static final int MULT=37;
    public static final int HOUR=20;
    public static final int CURRENT=80;
    public static final int PREV=78;
    public static final int SKIP_TILL_NEXT_MATCH=24;
    public static final int WHEREEXPRESSION=63;
    public static final int NEWLINE=57;
    public static final int NONCONTROL_CHAR=52;
    public static final int AGGREGATION=74;
    public static final int BBRACKETLEFT=16;
    public static final int KOMMA=81;
    public static final int PARTITION_CONTIGUITY=27;
    public static final int ASSIGN=41;
    public static final int WITHIN=9;
    public static final int SKIP_TILL_ANY_MATCH=25;
    public static final int QUERY=76;
    public static final int LEN=15;
    public static final int ASSIGNMENT=72;
    public static final int KTYPE=61;
    public static final int LOWER=55;
    public static final int MINUTE=21;
    public static final int CREATEVIEW=75;
    public static final int COMPAREOP=38;
    public static final int STRICT_CONTIGUITY=26;
    public static final int UPPER=56;
    public static final int FIRST=79;

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
    public String getGrammarFileName() { return "C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g"; }


    public static class start_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "start"
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:1: start : ( createStmt | queryStmt );
    public final SaseParser.start_return start() throws RecognitionException {
        SaseParser.start_return retval = new SaseParser.start_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SaseParser.createStmt_return createStmt1 = null;

        SaseParser.queryStmt_return queryStmt2 = null;



        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:8: ( createStmt | queryStmt )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:45:10: createStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_createStmt_in_start154);
                    createStmt1=createStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, createStmt1.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:46:4: queryStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_queryStmt_in_start160);
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:49:1: createStmt : CREATE VIEW NAME queryStmt -> ^( CREATEVIEW NAME queryStmt ) ;
    public final SaseParser.createStmt_return createStmt() throws RecognitionException {
        SaseParser.createStmt_return retval = new SaseParser.createStmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token CREATE3=null;
        Token VIEW4=null;
        Token NAME5=null;
        SaseParser.queryStmt_return queryStmt6 = null;


        CommonTree CREATE3_tree=null;
        CommonTree VIEW4_tree=null;
        CommonTree NAME5_tree=null;
        RewriteRuleTokenStream stream_CREATE=new RewriteRuleTokenStream(adaptor,"token CREATE");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_VIEW=new RewriteRuleTokenStream(adaptor,"token VIEW");
        RewriteRuleSubtreeStream stream_queryStmt=new RewriteRuleSubtreeStream(adaptor,"rule queryStmt");
        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:50:2: ( CREATE VIEW NAME queryStmt -> ^( CREATEVIEW NAME queryStmt ) )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:50:4: CREATE VIEW NAME queryStmt
            {
            CREATE3=(Token)match(input,CREATE,FOLLOW_CREATE_in_createStmt170); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CREATE.add(CREATE3);

            VIEW4=(Token)match(input,VIEW,FOLLOW_VIEW_in_createStmt172); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_VIEW.add(VIEW4);

            NAME5=(Token)match(input,NAME,FOLLOW_NAME_in_createStmt174); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME5);

            pushFollow(FOLLOW_queryStmt_in_createStmt176);
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
            // 50:31: -> ^( CREATEVIEW NAME queryStmt )
            {
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:50:34: ^( CREATEVIEW NAME queryStmt )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CREATEVIEW, "CREATEVIEW"), root_1);

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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:1: queryStmt : patternPart ( wherePart )? ( withinPart )? ( returnPart )? -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? ) ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:10: ( patternPart ( wherePart )? ( withinPart )? ( returnPart )? -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? ) )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:13: patternPart ( wherePart )? ( withinPart )? ( returnPart )?
            {
            pushFollow(FOLLOW_patternPart_in_queryStmt196);
            patternPart7=patternPart();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternPart.add(patternPart7.getTree());
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:25: ( wherePart )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==WHERE) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: wherePart
                    {
                    pushFollow(FOLLOW_wherePart_in_queryStmt198);
                    wherePart8=wherePart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_wherePart.add(wherePart8.getTree());

                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:36: ( withinPart )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==WITHIN) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: withinPart
                    {
                    pushFollow(FOLLOW_withinPart_in_queryStmt201);
                    withinPart9=withinPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_withinPart.add(withinPart9.getTree());

                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:48: ( returnPart )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==RETURN) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: returnPart
                    {
                    pushFollow(FOLLOW_returnPart_in_queryStmt204);
                    returnPart10=returnPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_returnPart.add(returnPart10.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: returnPart, wherePart, patternPart, withinPart
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 53:60: -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? )
            {
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:63: ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUERY, "QUERY"), root_1);

                adaptor.addChild(root_1, stream_patternPart.nextTree());
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:83: ( wherePart )?
                if ( stream_wherePart.hasNext() ) {
                    adaptor.addChild(root_1, stream_wherePart.nextTree());

                }
                stream_wherePart.reset();
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:94: ( withinPart )?
                if ( stream_withinPart.hasNext() ) {
                    adaptor.addChild(root_1, stream_withinPart.nextTree());

                }
                stream_withinPart.reset();
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:53:106: ( returnPart )?
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:57:1: withinPart : WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:2: ( WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:4: WITHIN NUMBER ( timeunit )?
            {
            WITHIN11=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart237); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_WITHIN.add(WITHIN11);

            NUMBER12=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart239); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER12);

            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:18: ( timeunit )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( ((LA5_0>=WEEK && LA5_0<=MILLISECOND)) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:0:0: timeunit
                    {
                    pushFollow(FOLLOW_timeunit_in_withinPart241);
                    timeunit13=timeunit();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_timeunit.add(timeunit13.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: timeunit, WITHIN, NUMBER
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 58:28: -> ^( WITHIN NUMBER ( timeunit )? )
            {
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:31: ^( WITHIN NUMBER ( timeunit )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_WITHIN.nextNode(), root_1);

                adaptor.addChild(root_1, stream_NUMBER.nextNode());
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:47: ( timeunit )?
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:1: timeunit : ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND );
    public final SaseParser.timeunit_return timeunit() throws RecognitionException {
        SaseParser.timeunit_return retval = new SaseParser.timeunit_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set14=null;

        CommonTree set14_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:9: ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:63:1: wherePart : ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:2: ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:4: WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    WHERE15=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart283); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE15);

                    pushFollow(FOLLOW_skipPart_in_wherePart285);
                    skipPart16=skipPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_skipPart.add(skipPart16.getTree());
                    LEFTCURLY17=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart287); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFTCURLY.add(LEFTCURLY17);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart289);
                    whereExpressions18=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions18.getTree());
                    RIGHTCURLY19=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart291); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHTCURLY.add(RIGHTCURLY19);



                    // AST REWRITE
                    // elements: whereExpressions, WHERE, skipPart
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 64:57: -> ^( WHERE skipPart whereExpressions )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:60: ^( WHERE skipPart whereExpressions )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:4: WHERE whereExpressions
                    {
                    WHERE20=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart308); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE20);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart310);
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
                    // 65:27: -> ^( WHERE whereExpressions )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:30: ^( WHERE whereExpressions )
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:68:1: skipPart : skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:2: ( skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:4: skipMethod LBRACKET parameterList RBRACKET
            {
            pushFollow(FOLLOW_skipMethod_in_skipPart329);
            skipMethod22=skipMethod();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_skipMethod.add(skipMethod22.getTree());
            LBRACKET23=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_skipPart331); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET23);

            pushFollow(FOLLOW_parameterList_in_skipPart333);
            parameterList24=parameterList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_parameterList.add(parameterList24.getTree());
            RBRACKET25=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_skipPart335); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET25);



            // AST REWRITE
            // elements: skipMethod, parameterList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 70:2: -> ^( skipMethod parameterList )
            {
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:5: ^( skipMethod parameterList )
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:1: skipMethod : ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY );
    public final SaseParser.skipMethod_return skipMethod() throws RecognitionException {
        SaseParser.skipMethod_return retval = new SaseParser.skipMethod_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set26=null;

        CommonTree set26_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:11: ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:1: patternPart : PATTERN patternDecl -> ^( patternDecl ) ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:80:2: ( PATTERN patternDecl -> ^( patternDecl ) )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:80:4: PATTERN patternDecl
            {
            PATTERN27=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart384); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN27);

            pushFollow(FOLLOW_patternDecl_in_patternPart386);
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
            // 80:24: -> ^( patternDecl )
            {
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:80:27: ^( patternDecl )
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:1: returnPart : RETURN attributeTerm ( COMMA attributeTerm )* -> ^( RETURN ( attributeTerm )* ) ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:2: ( RETURN attributeTerm ( COMMA attributeTerm )* -> ^( RETURN ( attributeTerm )* ) )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:5: RETURN attributeTerm ( COMMA attributeTerm )*
            {
            RETURN29=(Token)match(input,RETURN,FOLLOW_RETURN_in_returnPart405); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RETURN.add(RETURN29);

            pushFollow(FOLLOW_attributeTerm_in_returnPart407);
            attributeTerm30=attributeTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeTerm.add(attributeTerm30.getTree());
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:26: ( COMMA attributeTerm )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==COMMA) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:27: COMMA attributeTerm
            	    {
            	    COMMA31=(Token)match(input,COMMA,FOLLOW_COMMA_in_returnPart410); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA31);

            	    pushFollow(FOLLOW_attributeTerm_in_returnPart412);
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
            // elements: attributeTerm, RETURN
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 84:49: -> ^( RETURN ( attributeTerm )* )
            {
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:52: ^( RETURN ( attributeTerm )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_RETURN.nextNode(), root_1);

                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:61: ( attributeTerm )*
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:1: patternDecl : SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:2: ( SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:4: SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET
            {
            SEQ33=(Token)match(input,SEQ,FOLLOW_SEQ_in_patternDecl435); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEQ.add(SEQ33);

            LBRACKET34=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_patternDecl437); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET34);

            pushFollow(FOLLOW_stateDef_in_patternDecl439);
            stateDef35=stateDef();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_stateDef.add(stateDef35.getTree());
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:26: ( COMMA stateDef )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==COMMA) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:27: COMMA stateDef
            	    {
            	    COMMA36=(Token)match(input,COMMA,FOLLOW_COMMA_in_patternDecl442); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA36);

            	    pushFollow(FOLLOW_stateDef_in_patternDecl444);
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

            RBRACKET38=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_patternDecl448); if (state.failed) return retval; 
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
            // 88:53: -> ^( SEQ ( stateDef )* )
            {
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:56: ^( SEQ ( stateDef )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_SEQ.nextNode(), root_1);

                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:62: ( stateDef )*
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:1: stateDef : ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) );
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:10: ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:12: ( NOTSIGN )? ktypeDefinition
                    {
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:12: ( NOTSIGN )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==NOTSIGN) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:13: NOTSIGN
                            {
                            NOTSIGN39=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef472); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN39);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef476);
                    ktypeDefinition40=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition40.getTree());


                    // AST REWRITE
                    // elements: NOTSIGN, ktypeDefinition
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 92:40: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:43: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(KSTATE, "KSTATE"), root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:68: ( NOTSIGN )?
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:5: ( NOTSIGN )? typeDefinition
                    {
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:5: ( NOTSIGN )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==NOTSIGN) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:6: NOTSIGN
                            {
                            NOTSIGN41=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef495); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN41);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_typeDefinition_in_stateDef499);
                    typeDefinition42=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition42.getTree());


                    // AST REWRITE
                    // elements: typeDefinition, NOTSIGN
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 93:32: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:35: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STATE, "STATE"), root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:58: ( NOTSIGN )?
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:5: ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET
                    {
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:5: ( NOTSIGN )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==NOTSIGN) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:6: NOTSIGN
                            {
                            NOTSIGN43=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef520); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN43);


                            }
                            break;

                    }

                    LBRACKET44=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef524); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET44);

                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef526);
                    ktypeDefinition45=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition45.getTree());
                    RBRACKET46=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef528); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET46);



                    // AST REWRITE
                    // elements: NOTSIGN, ktypeDefinition
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 94:50: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:53: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(KSTATE, "KSTATE"), root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:78: ( NOTSIGN )?
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:5: ( NOTSIGN )? LBRACKET typeDefinition RBRACKET
                    {
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:5: ( NOTSIGN )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==NOTSIGN) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:6: NOTSIGN
                            {
                            NOTSIGN47=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef546); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN47);


                            }
                            break;

                    }

                    LBRACKET48=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef550); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET48);

                    pushFollow(FOLLOW_typeDefinition_in_stateDef552);
                    typeDefinition49=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition49.getTree());
                    RBRACKET50=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef554); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET50);



                    // AST REWRITE
                    // elements: typeDefinition, NOTSIGN
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 95:49: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:52: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STATE, "STATE"), root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:75: ( NOTSIGN )?
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:1: typeDefinition : NAME sAttributeName ;
    public final SaseParser.typeDefinition_return typeDefinition() throws RecognitionException {
        SaseParser.typeDefinition_return retval = new SaseParser.typeDefinition_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NAME51=null;
        SaseParser.sAttributeName_return sAttributeName52 = null;


        CommonTree NAME51_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:2: ( NAME sAttributeName )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:4: NAME sAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();

            NAME51=(Token)match(input,NAME,FOLLOW_NAME_in_typeDefinition579); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME51_tree = (CommonTree)adaptor.create(NAME51);
            adaptor.addChild(root_0, NAME51_tree);
            }
            pushFollow(FOLLOW_sAttributeName_in_typeDefinition581);
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:1: ktypeDefinition : NAME PLUS kAttributeName ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:2: ( NAME PLUS kAttributeName )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:4: NAME PLUS kAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();

            NAME53=(Token)match(input,NAME,FOLLOW_NAME_in_ktypeDefinition592); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME53_tree = (CommonTree)adaptor.create(NAME53);
            adaptor.addChild(root_0, NAME53_tree);
            }
            PLUS54=(Token)match(input,PLUS,FOLLOW_PLUS_in_ktypeDefinition594); if (state.failed) return retval;
            pushFollow(FOLLOW_kAttributeName_in_ktypeDefinition597);
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:107:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:4: attributeName ( COMMA attributeName )*
            {
            pushFollow(FOLLOW_attributeName_in_parameterList611);
            attributeName56=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(attributeName56.getTree());
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:18: ( COMMA attributeName )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==COMMA) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:19: COMMA attributeName
            	    {
            	    COMMA57=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList614); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA57);

            	    pushFollow(FOLLOW_attributeName_in_parameterList616);
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
            // 108:41: -> ^( PARAMLIST ( attributeName )* )
            {
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:44: ^( PARAMLIST ( attributeName )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PARAMLIST, "PARAMLIST"), root_1);

                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:56: ( attributeName )*
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:1: attributeName : ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SaseParser.kAttributeName_return kAttributeName59 = null;

        SaseParser.sAttributeName_return sAttributeName60 = null;


        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_kAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeName");
        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:2: ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:5: kAttributeName
                    {
                    pushFollow(FOLLOW_kAttributeName_in_attributeName640);
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
                    // 112:20: -> ^( KATTRIBUTE NAME )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:23: ^( KATTRIBUTE NAME )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:4: sAttributeName
                    {
                    pushFollow(FOLLOW_sAttributeName_in_attributeName654);
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
                    // 113:19: -> ^( ATTRIBUTE NAME )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:22: ^( ATTRIBUTE NAME )
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:2: ( NAME BBRACKETLEFT BBRACKETRIGHT )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();

            NAME61=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName676); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME61_tree = (CommonTree)adaptor.create(NAME61);
            adaptor.addChild(root_0, NAME61_tree);
            }
            BBRACKETLEFT62=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName679); if (state.failed) return retval;
            BBRACKETRIGHT63=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName682); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:120:1: sAttributeName : NAME ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NAME64=null;

        CommonTree NAME64_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:121:2: ( NAME )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:121:4: NAME
            {
            root_0 = (CommonTree)adaptor.nil();

            NAME64=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName697); if (state.failed) return retval;
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:124:1: kAttributeUsage : ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) );
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:125:2: ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:125:4: NAME current
                    {
                    NAME65=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage710); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME65);

                    pushFollow(FOLLOW_current_in_kAttributeUsage713);
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
                    // 125:19: -> ^( NAME CURRENT )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:125:22: ^( NAME CURRENT )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:4: NAME first
                    {
                    NAME67=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage727); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME67);

                    pushFollow(FOLLOW_first_in_kAttributeUsage729);
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
                    // 126:15: -> ^( NAME FIRST )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:18: ^( NAME FIRST )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:127:4: NAME last
                    {
                    NAME69=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage742); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME69);

                    pushFollow(FOLLOW_last_in_kAttributeUsage744);
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
                    // 127:14: -> ^( NAME PREV )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:127:17: ^( NAME PREV )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:128:4: NAME len
                    {
                    NAME71=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage757); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME71);

                    pushFollow(FOLLOW_len_in_kAttributeUsage759);
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
                    // 128:13: -> ^( NAME LEN )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:128:16: ^( NAME LEN )
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:131:1: current : BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:131:9: ( BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?)
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:132:2: BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();

            BBRACKETLEFT73=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_current780); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT73_tree = (CommonTree)adaptor.create(BBRACKETLEFT73);
            adaptor.addChild(root_0, BBRACKETLEFT73_tree);
            }
            name=(Token)match(input,NAME,FOLLOW_NAME_in_current784); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = (CommonTree)adaptor.create(name);
            adaptor.addChild(root_0, name_tree);
            }
            BBRACKETRIGHT74=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_current786); if (state.failed) return retval;
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:135:1: first : BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:135:7: ( BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?)
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:136:2: BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();

            BBRACKETLEFT75=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_first801); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT75_tree = (CommonTree)adaptor.create(BBRACKETLEFT75);
            adaptor.addChild(root_0, BBRACKETLEFT75_tree);
            }
            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_first805); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = (CommonTree)adaptor.create(number);
            adaptor.addChild(root_0, number_tree);
            }
            BBRACKETRIGHT76=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_first807); if (state.failed) return retval;
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:139:1: last : BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:139:6: ( BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?)
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:140:2: BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();

            BBRACKETLEFT77=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_last820); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT77_tree = (CommonTree)adaptor.create(BBRACKETLEFT77);
            adaptor.addChild(root_0, BBRACKETLEFT77_tree);
            }
            name=(Token)match(input,NAME,FOLLOW_NAME_in_last824); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = (CommonTree)adaptor.create(name);
            adaptor.addChild(root_0, name_tree);
            }
            MINUS78=(Token)match(input,MINUS,FOLLOW_MINUS_in_last826); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            MINUS78_tree = (CommonTree)adaptor.create(MINUS78);
            adaptor.addChild(root_0, MINUS78_tree);
            }
            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_last830); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = (CommonTree)adaptor.create(number);
            adaptor.addChild(root_0, number_tree);
            }
            BBRACKETRIGHT79=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_last832); if (state.failed) return retval;
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:143:1: len : BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT ;
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
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:143:5: ( BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:144:2: BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();

            BBRACKETLEFT80=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_len848); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT80_tree = (CommonTree)adaptor.create(BBRACKETLEFT80);
            adaptor.addChild(root_0, BBRACKETLEFT80_tree);
            }
            NAME81=(Token)match(input,NAME,FOLLOW_NAME_in_len850); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME81_tree = (CommonTree)adaptor.create(NAME81);
            adaptor.addChild(root_0, NAME81_tree);
            }
            POINT82=(Token)match(input,POINT,FOLLOW_POINT_in_len852); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            POINT82_tree = (CommonTree)adaptor.create(POINT82);
            adaptor.addChild(root_0, POINT82_tree);
            }
            LEN83=(Token)match(input,LEN,FOLLOW_LEN_in_len854); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEN83_tree = (CommonTree)adaptor.create(LEN83);
            adaptor.addChild(root_0, LEN83_tree);
            }
            BBRACKETRIGHT84=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_len856); if (state.failed) return retval;
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:147:1: whereExpressions : expression ( ( AND | KOMMA ) expression )* -> ^( WHEREEXPRESSION ( expression )* ) ;
    public final SaseParser.whereExpressions_return whereExpressions() throws RecognitionException {
        SaseParser.whereExpressions_return retval = new SaseParser.whereExpressions_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token AND86=null;
        Token KOMMA87=null;
        SaseParser.expression_return expression85 = null;

        SaseParser.expression_return expression88 = null;


        CommonTree AND86_tree=null;
        CommonTree KOMMA87_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_KOMMA=new RewriteRuleTokenStream(adaptor,"token KOMMA");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:2: ( expression ( ( AND | KOMMA ) expression )* -> ^( WHEREEXPRESSION ( expression )* ) )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:4: expression ( ( AND | KOMMA ) expression )*
            {
            pushFollow(FOLLOW_expression_in_whereExpressions869);
            expression85=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression85.getTree());
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:15: ( ( AND | KOMMA ) expression )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==AND||LA18_0==KOMMA) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:16: ( AND | KOMMA ) expression
            	    {
            	    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:16: ( AND | KOMMA )
            	    int alt17=2;
            	    int LA17_0 = input.LA(1);

            	    if ( (LA17_0==AND) ) {
            	        alt17=1;
            	    }
            	    else if ( (LA17_0==KOMMA) ) {
            	        alt17=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 17, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt17) {
            	        case 1 :
            	            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:17: AND
            	            {
            	            AND86=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions873); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_AND.add(AND86);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:21: KOMMA
            	            {
            	            KOMMA87=(Token)match(input,KOMMA,FOLLOW_KOMMA_in_whereExpressions875); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_KOMMA.add(KOMMA87);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_whereExpressions878);
            	    expression88=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression88.getTree());

            	    }
            	    break;

            	default :
            	    break loop18;
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
            // 148:41: -> ^( WHEREEXPRESSION ( expression )* )
            {
                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:44: ^( WHEREEXPRESSION ( expression )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION"), root_1);

                // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:62: ( expression )*
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:151:1: expression : ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) | f1= mathExpression (op= SINGLEEQUALS | op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) | sAttributeName ASSIGN mathExpression -> ^( ASSIGNMENT sAttributeName mathExpression ) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token op=null;
        Token BBRACKETLEFT89=null;
        Token NAME90=null;
        Token BBRACKETRIGHT91=null;
        Token ASSIGN93=null;
        SaseParser.mathExpression_return f1 = null;

        SaseParser.mathExpression_return f2 = null;

        SaseParser.sAttributeName_return sAttributeName92 = null;

        SaseParser.mathExpression_return mathExpression94 = null;


        CommonTree op_tree=null;
        CommonTree BBRACKETLEFT89_tree=null;
        CommonTree NAME90_tree=null;
        CommonTree BBRACKETRIGHT91_tree=null;
        CommonTree ASSIGN93_tree=null;
        RewriteRuleTokenStream stream_SINGLEEQUALS=new RewriteRuleTokenStream(adaptor,"token SINGLEEQUALS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");
        RewriteRuleTokenStream stream_ASSIGN=new RewriteRuleTokenStream(adaptor,"token ASSIGN");
        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_mathExpression=new RewriteRuleSubtreeStream(adaptor,"rule mathExpression");
        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:152:2: ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) | f1= mathExpression (op= SINGLEEQUALS | op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) | sAttributeName ASSIGN mathExpression -> ^( ASSIGNMENT sAttributeName mathExpression ) )
            int alt20=3;
            switch ( input.LA(1) ) {
            case BBRACKETLEFT:
                {
                alt20=1;
                }
                break;
            case AVG:
            case MIN:
            case MAX:
            case SUM:
            case COUNT:
            case LBRACKET:
            case NUMBER:
            case STRING_LITERAL:
                {
                alt20=2;
                }
                break;
            case NAME:
                {
                int LA20_3 = input.LA(2);

                if ( (LA20_3==BBRACKETLEFT||LA20_3==POINT) ) {
                    alt20=2;
                }
                else if ( (LA20_3==ASSIGN) ) {
                    alt20=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:152:4: BBRACKETLEFT NAME BBRACKETRIGHT
                    {
                    BBRACKETLEFT89=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_expression901); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT89);

                    NAME90=(Token)match(input,NAME,FOLLOW_NAME_in_expression903); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME90);

                    BBRACKETRIGHT91=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_expression905); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT91);



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
                    // 152:36: -> ^( IDEXPRESSION NAME )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:152:39: ^( IDEXPRESSION NAME )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:3: f1= mathExpression (op= SINGLEEQUALS | op= COMPAREOP ) f2= mathExpression
                    {
                    pushFollow(FOLLOW_mathExpression_in_expression921);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:21: (op= SINGLEEQUALS | op= COMPAREOP )
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==SINGLEEQUALS) ) {
                        alt19=1;
                    }
                    else if ( (LA19_0==COMPAREOP) ) {
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
                            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:22: op= SINGLEEQUALS
                            {
                            op=(Token)match(input,SINGLEEQUALS,FOLLOW_SINGLEEQUALS_in_expression926); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_SINGLEEQUALS.add(op);


                            }
                            break;
                        case 2 :
                            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:38: op= COMPAREOP
                            {
                            op=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression930); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COMPAREOP.add(op);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_mathExpression_in_expression935);
                    f2=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f2.getTree());


                    // AST REWRITE
                    // elements: f1, op, f2
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
                    // 153:70: -> ^( COMPAREEXPRESSION $f1 $op $f2)
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:74: ^( COMPAREEXPRESSION $f1 $op $f2)
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
                case 3 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:154:3: sAttributeName ASSIGN mathExpression
                    {
                    pushFollow(FOLLOW_sAttributeName_in_expression957);
                    sAttributeName92=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sAttributeName.add(sAttributeName92.getTree());
                    ASSIGN93=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_expression959); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ASSIGN.add(ASSIGN93);

                    pushFollow(FOLLOW_mathExpression_in_expression961);
                    mathExpression94=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(mathExpression94.getTree());


                    // AST REWRITE
                    // elements: mathExpression, sAttributeName
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 154:40: -> ^( ASSIGNMENT sAttributeName mathExpression )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:154:43: ^( ASSIGNMENT sAttributeName mathExpression )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ASSIGNMENT, "ASSIGNMENT"), root_1);

                        adaptor.addChild(root_1, stream_sAttributeName.nextTree());
                        adaptor.addChild(root_1, stream_mathExpression.nextTree());

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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:1: mathExpression : mult ( ( PLUS | MINUS ) mult )* ;
    public final SaseParser.mathExpression_return mathExpression() throws RecognitionException {
        SaseParser.mathExpression_return retval = new SaseParser.mathExpression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token PLUS96=null;
        Token MINUS97=null;
        SaseParser.mult_return mult95 = null;

        SaseParser.mult_return mult98 = null;


        CommonTree PLUS96_tree=null;
        CommonTree MINUS97_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:15: ( mult ( ( PLUS | MINUS ) mult )* )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:17: mult ( ( PLUS | MINUS ) mult )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_mult_in_mathExpression981);
            mult95=mult();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mult95.getTree());
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:22: ( ( PLUS | MINUS ) mult )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( ((LA22_0>=PLUS && LA22_0<=MINUS)) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:23: ( PLUS | MINUS ) mult
            	    {
            	    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:23: ( PLUS | MINUS )
            	    int alt21=2;
            	    int LA21_0 = input.LA(1);

            	    if ( (LA21_0==PLUS) ) {
            	        alt21=1;
            	    }
            	    else if ( (LA21_0==MINUS) ) {
            	        alt21=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 21, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt21) {
            	        case 1 :
            	            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:24: PLUS
            	            {
            	            PLUS96=(Token)match(input,PLUS,FOLLOW_PLUS_in_mathExpression985); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            PLUS96_tree = (CommonTree)adaptor.create(PLUS96);
            	            root_0 = (CommonTree)adaptor.becomeRoot(PLUS96_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:30: MINUS
            	            {
            	            MINUS97=(Token)match(input,MINUS,FOLLOW_MINUS_in_mathExpression988); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MINUS97_tree = (CommonTree)adaptor.create(MINUS97);
            	            root_0 = (CommonTree)adaptor.becomeRoot(MINUS97_tree, root_0);
            	            }

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_mult_in_mathExpression992);
            	    mult98=mult();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, mult98.getTree());

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:160:1: mult : term ( ( MULT | DIVISION ) term )* ;
    public final SaseParser.mult_return mult() throws RecognitionException {
        SaseParser.mult_return retval = new SaseParser.mult_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token MULT100=null;
        Token DIVISION101=null;
        SaseParser.term_return term99 = null;

        SaseParser.term_return term102 = null;


        CommonTree MULT100_tree=null;
        CommonTree DIVISION101_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:160:7: ( term ( ( MULT | DIVISION ) term )* )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:160:9: term ( ( MULT | DIVISION ) term )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_term_in_mult1006);
            term99=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, term99.getTree());
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:160:14: ( ( MULT | DIVISION ) term )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( ((LA24_0>=DIVISION && LA24_0<=MULT)) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:160:15: ( MULT | DIVISION ) term
            	    {
            	    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:160:15: ( MULT | DIVISION )
            	    int alt23=2;
            	    int LA23_0 = input.LA(1);

            	    if ( (LA23_0==MULT) ) {
            	        alt23=1;
            	    }
            	    else if ( (LA23_0==DIVISION) ) {
            	        alt23=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 23, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt23) {
            	        case 1 :
            	            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:160:16: MULT
            	            {
            	            MULT100=(Token)match(input,MULT,FOLLOW_MULT_in_mult1010); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MULT100_tree = (CommonTree)adaptor.create(MULT100);
            	            root_0 = (CommonTree)adaptor.becomeRoot(MULT100_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:160:22: DIVISION
            	            {
            	            DIVISION101=(Token)match(input,DIVISION,FOLLOW_DIVISION_in_mult1013); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            DIVISION101_tree = (CommonTree)adaptor.create(DIVISION101);
            	            root_0 = (CommonTree)adaptor.becomeRoot(DIVISION101_tree, root_0);
            	            }

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_term_in_mult1017);
            	    term102=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, term102.getTree());

            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:164:1: term : ( attributeTerm | value | LBRACKET mathExpression RBRACKET );
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACKET105=null;
        Token RBRACKET107=null;
        SaseParser.attributeTerm_return attributeTerm103 = null;

        SaseParser.value_return value104 = null;

        SaseParser.mathExpression_return mathExpression106 = null;


        CommonTree LBRACKET105_tree=null;
        CommonTree RBRACKET107_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:164:5: ( attributeTerm | value | LBRACKET mathExpression RBRACKET )
            int alt25=3;
            switch ( input.LA(1) ) {
            case AVG:
            case MIN:
            case MAX:
            case SUM:
            case COUNT:
            case NAME:
                {
                alt25=1;
                }
                break;
            case NUMBER:
            case STRING_LITERAL:
                {
                alt25=2;
                }
                break;
            case LBRACKET:
                {
                alt25=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }

            switch (alt25) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:164:7: attributeTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_attributeTerm_in_term1030);
                    attributeTerm103=attributeTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attributeTerm103.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:165:3: value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_value_in_term1036);
                    value104=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value104.getTree());

                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:3: LBRACKET mathExpression RBRACKET
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LBRACKET105=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_term1041); if (state.failed) return retval;
                    pushFollow(FOLLOW_mathExpression_in_term1044);
                    mathExpression106=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mathExpression106.getTree());
                    RBRACKET107=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_term1046); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:169:1: attributeTerm : ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) );
    public final SaseParser.attributeTerm_return attributeTerm() throws RecognitionException {
        SaseParser.attributeTerm_return retval = new SaseParser.attributeTerm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token aName=null;
        Token member=null;
        Token POINT110=null;
        Token NAME111=null;
        Token POINT112=null;
        SaseParser.aggregation_return aggregation108 = null;

        SaseParser.kAttributeUsage_return kAttributeUsage109 = null;


        CommonTree aName_tree=null;
        CommonTree member_tree=null;
        CommonTree POINT110_tree=null;
        CommonTree NAME111_tree=null;
        CommonTree POINT112_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_kAttributeUsage=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeUsage");
        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:169:14: ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) )
            int alt26=3;
            int LA26_0 = input.LA(1);

            if ( ((LA26_0>=AVG && LA26_0<=COUNT)) ) {
                alt26=1;
            }
            else if ( (LA26_0==NAME) ) {
                int LA26_2 = input.LA(2);

                if ( (LA26_2==POINT) ) {
                    alt26=3;
                }
                else if ( (LA26_2==BBRACKETLEFT) ) {
                    alt26=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 26, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:169:16: aggregation
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_aggregation_in_attributeTerm1058);
                    aggregation108=aggregation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregation108.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:170:5: kAttributeUsage POINT NAME
                    {
                    pushFollow(FOLLOW_kAttributeUsage_in_attributeTerm1066);
                    kAttributeUsage109=kAttributeUsage();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeUsage.add(kAttributeUsage109.getTree());
                    POINT110=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1068); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT110);

                    NAME111=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1070); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME111);



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
                    // 170:32: -> ^( KMEMBER kAttributeUsage NAME )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:170:35: ^( KMEMBER kAttributeUsage NAME )
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
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:5: aName= NAME POINT member= NAME
                    {
                    aName=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1089); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(aName);

                    POINT112=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1091); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT112);

                    member=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1095); if (state.failed) return retval; 
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
                    // 171:34: -> ^( MEMBER $aName $member)
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:37: ^( MEMBER $aName $member)
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

        catch(RecognitionException e){
          throw e;
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
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:174:1: aggregation : ( aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION aggop $var $member) | aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION aggop $var ( $member)? ) );
    public final SaseParser.aggregation_return aggregation() throws RecognitionException {
        SaseParser.aggregation_return retval = new SaseParser.aggregation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token var=null;
        Token name=null;
        Token number=null;
        Token member=null;
        Token LBRACKET114=null;
        Token BBRACKETLEFT115=null;
        Token POINT116=null;
        Token POINT117=null;
        Token MINUS118=null;
        Token BBRACKETRIGHT119=null;
        Token POINT120=null;
        Token RBRACKET121=null;
        Token LBRACKET123=null;
        Token BBRACKETLEFT124=null;
        Token BBRACKETRIGHT125=null;
        Token POINT126=null;
        Token RBRACKET127=null;
        SaseParser.aggop_return aggop113 = null;

        SaseParser.aggop_return aggop122 = null;


        CommonTree var_tree=null;
        CommonTree name_tree=null;
        CommonTree number_tree=null;
        CommonTree member_tree=null;
        CommonTree LBRACKET114_tree=null;
        CommonTree BBRACKETLEFT115_tree=null;
        CommonTree POINT116_tree=null;
        CommonTree POINT117_tree=null;
        CommonTree MINUS118_tree=null;
        CommonTree BBRACKETRIGHT119_tree=null;
        CommonTree POINT120_tree=null;
        CommonTree RBRACKET121_tree=null;
        CommonTree LBRACKET123_tree=null;
        CommonTree BBRACKETLEFT124_tree=null;
        CommonTree BBRACKETRIGHT125_tree=null;
        CommonTree POINT126_tree=null;
        CommonTree RBRACKET127_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
        RewriteRuleSubtreeStream stream_aggop=new RewriteRuleSubtreeStream(adaptor,"rule aggop");
        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:2: ( aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION aggop $var $member) | aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION aggop $var ( $member)? ) )
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( ((LA28_0>=AVG && LA28_0<=COUNT)) ) {
                int LA28_1 = input.LA(2);

                if ( (LA28_1==LBRACKET) ) {
                    int LA28_2 = input.LA(3);

                    if ( (LA28_2==NAME) ) {
                        int LA28_3 = input.LA(4);

                        if ( (LA28_3==BBRACKETLEFT) ) {
                            int LA28_4 = input.LA(5);

                            if ( (LA28_4==POINT) ) {
                                alt28=1;
                            }
                            else if ( (LA28_4==BBRACKETRIGHT) ) {
                                alt28=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 28, 4, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 28, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 28, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 28, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;
            }
            switch (alt28) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:5: aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}?
                    {
                    pushFollow(FOLLOW_aggop_in_aggregation1120);
                    aggop113=aggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggop.add(aggop113.getTree());
                    LBRACKET114=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1122); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET114);

                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1126); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);

                    BBRACKETLEFT115=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1128); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT115);

                    POINT116=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1130); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT116);

                    POINT117=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1132); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT117);

                    name=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1136); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(name);

                    MINUS118=(Token)match(input,MINUS,FOLLOW_MINUS_in_aggregation1138); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MINUS.add(MINUS118);

                    number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_aggregation1142); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(number);

                    BBRACKETRIGHT119=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1144); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT119);

                    POINT120=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1146); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT120);

                    member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1150); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(member);

                    RBRACKET121=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1152); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET121);

                    if ( !((name.getText().equalsIgnoreCase("I") && Integer.parseInt(number.getText()) == 1)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "aggregation", "$name.getText().equalsIgnoreCase(\"I\") && Integer.parseInt($number.getText()) == 1");
                    }


                    // AST REWRITE
                    // elements: aggop, member, var
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
                    // 176:4: -> ^( AGGREGATION aggop $var $member)
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:176:7: ^( AGGREGATION aggop $var $member)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AGGREGATION, "AGGREGATION"), root_1);

                        adaptor.addChild(root_1, stream_aggop.nextTree());
                        adaptor.addChild(root_1, stream_var.nextNode());
                        adaptor.addChild(root_1, stream_member.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:177:5: aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET
                    {
                    pushFollow(FOLLOW_aggop_in_aggregation1178);
                    aggop122=aggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggop.add(aggop122.getTree());
                    LBRACKET123=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1180); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET123);

                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1184); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);

                    BBRACKETLEFT124=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1186); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT124);

                    BBRACKETRIGHT125=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1188); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT125);

                    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:177:56: ( POINT member= NAME )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==POINT) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:177:57: POINT member= NAME
                            {
                            POINT126=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1191); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_POINT.add(POINT126);

                            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1195); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NAME.add(member);


                            }
                            break;

                    }

                    RBRACKET127=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1199); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET127);



                    // AST REWRITE
                    // elements: aggop, member, var
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
                    // 178:4: -> ^( AGGREGATION aggop $var ( $member)? )
                    {
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:7: ^( AGGREGATION aggop $var ( $member)? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AGGREGATION, "AGGREGATION"), root_1);

                        adaptor.addChild(root_1, stream_aggop.nextTree());
                        adaptor.addChild(root_1, stream_var.nextNode());
                        // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:32: ( $member)?
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

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "aggregation"

    public static class aggop_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "aggop"
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:181:1: aggop : ( MIN | MAX | SUM | COUNT | AVG );
    public final SaseParser.aggop_return aggop() throws RecognitionException {
        SaseParser.aggop_return retval = new SaseParser.aggop_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set128=null;

        CommonTree set128_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:182:2: ( MIN | MAX | SUM | COUNT | AVG )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set128=(Token)input.LT(1);
            if ( (input.LA(1)>=AVG && input.LA(1)<=COUNT) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set128));
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

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "aggop"

    public static class value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:185:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set129=null;

        CommonTree set129_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:185:8: ( NUMBER | STRING_LITERAL )
            // C:\\development\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set129=(Token)input.LT(1);
            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set129));
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

        catch(RecognitionException e){
          throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "value"

    // Delegated rules


 

    public static final BitSet FOLLOW_createStmt_in_start154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_queryStmt_in_start160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_createStmt170 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_VIEW_in_createStmt172 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_createStmt174 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_queryStmt_in_createStmt176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_patternPart_in_queryStmt196 = new BitSet(new long[]{0x0000000000000702L});
    public static final BitSet FOLLOW_wherePart_in_queryStmt198 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_withinPart_in_queryStmt201 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_returnPart_in_queryStmt204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart237 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart239 = new BitSet(new long[]{0x0000000000FC0002L});
    public static final BitSet FOLLOW_timeunit_in_withinPart241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_timeunit0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart283 = new BitSet(new long[]{0x000000000F000000L});
    public static final BitSet FOLLOW_skipPart_in_wherePart285 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart287 = new BitSet(new long[]{0x00291001F0010000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart289 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart308 = new BitSet(new long[]{0x00291001F0010000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_skipMethod_in_skipPart329 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_skipPart331 = new BitSet(new long[]{0x00291001F0010000L});
    public static final BitSet FOLLOW_parameterList_in_skipPart333 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_skipPart335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_skipMethod0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart384 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnPart405 = new BitSet(new long[]{0x00080001F0000000L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart407 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_COMMA_in_returnPart410 = new BitSet(new long[]{0x00080001F0000000L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart412 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_SEQ_in_patternDecl435 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_patternDecl437 = new BitSet(new long[]{0x0008140000000000L});
    public static final BitSet FOLLOW_stateDef_in_patternDecl439 = new BitSet(new long[]{0x0000280000000000L});
    public static final BitSet FOLLOW_COMMA_in_patternDecl442 = new BitSet(new long[]{0x0008140000000000L});
    public static final BitSet FOLLOW_stateDef_in_patternDecl444 = new BitSet(new long[]{0x0000280000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_patternDecl448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef472 = new BitSet(new long[]{0x0008040000000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef495 = new BitSet(new long[]{0x0008040000000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef499 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef520 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef524 = new BitSet(new long[]{0x0008040000000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef526 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef546 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef550 = new BitSet(new long[]{0x0008040000000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef552 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeDefinition579 = new BitSet(new long[]{0x00291001F0010000L});
    public static final BitSet FOLLOW_sAttributeName_in_typeDefinition581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_ktypeDefinition592 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_PLUS_in_ktypeDefinition594 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_kAttributeName_in_ktypeDefinition597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList611 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList614 = new BitSet(new long[]{0x00291001F0010000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList616 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName676 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName679 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage710 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_current_in_kAttributeUsage713 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage727 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_first_in_kAttributeUsage729 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage742 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_last_in_kAttributeUsage744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage757 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_len_in_kAttributeUsage759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_current780 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_current784 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_current786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_first801 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_NUMBER_in_first805 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_first807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_last820 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_last824 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_MINUS_in_last826 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_NUMBER_in_last830 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_last832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_len848 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_len850 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_POINT_in_len852 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_LEN_in_len854 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_len856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions869 = new BitSet(new long[]{0x0000000000004002L,0x0000000000020000L});
    public static final BitSet FOLLOW_AND_in_whereExpressions873 = new BitSet(new long[]{0x00291001F0010000L});
    public static final BitSet FOLLOW_KOMMA_in_whereExpressions875 = new BitSet(new long[]{0x00291001F0010000L});
    public static final BitSet FOLLOW_expression_in_whereExpressions878 = new BitSet(new long[]{0x0000000000004002L,0x0000000000020000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_expression901 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_expression903 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_expression905 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression921 = new BitSet(new long[]{0x000000C000000000L});
    public static final BitSet FOLLOW_SINGLEEQUALS_in_expression926 = new BitSet(new long[]{0x00291001F0000000L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression930 = new BitSet(new long[]{0x00291001F0000000L});
    public static final BitSet FOLLOW_mathExpression_in_expression935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_expression957 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_ASSIGN_in_expression959 = new BitSet(new long[]{0x00291001F0000000L});
    public static final BitSet FOLLOW_mathExpression_in_expression961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mult_in_mathExpression981 = new BitSet(new long[]{0x0000000600000002L});
    public static final BitSet FOLLOW_PLUS_in_mathExpression985 = new BitSet(new long[]{0x00291001F0000000L});
    public static final BitSet FOLLOW_MINUS_in_mathExpression988 = new BitSet(new long[]{0x00291001F0000000L});
    public static final BitSet FOLLOW_mult_in_mathExpression992 = new BitSet(new long[]{0x0000000600000002L});
    public static final BitSet FOLLOW_term_in_mult1006 = new BitSet(new long[]{0x0000003000000002L});
    public static final BitSet FOLLOW_MULT_in_mult1010 = new BitSet(new long[]{0x00291001F0000000L});
    public static final BitSet FOLLOW_DIVISION_in_mult1013 = new BitSet(new long[]{0x00291001F0000000L});
    public static final BitSet FOLLOW_term_in_mult1017 = new BitSet(new long[]{0x0000003000000002L});
    public static final BitSet FOLLOW_attributeTerm_in_term1030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term1036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_term1041 = new BitSet(new long[]{0x00291001F0000000L});
    public static final BitSet FOLLOW_mathExpression_in_term1044 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_term1046 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregation_in_attributeTerm1058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_attributeTerm1066 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1068 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1070 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1089 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1091 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggop_in_aggregation1120 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1122 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1126 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1128 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1130 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1132 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1136 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_MINUS_in_aggregation1138 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_NUMBER_in_aggregation1142 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1144 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1146 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1150 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggop_in_aggregation1178 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1180 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1184 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1186 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1188 = new BitSet(new long[]{0x0000200800000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1191 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1195 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_aggop0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value0 = new BitSet(new long[]{0x0000000000000002L});

}