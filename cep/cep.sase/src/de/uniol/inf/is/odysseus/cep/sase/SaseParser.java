// $ANTLR 3.4 C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2012-03-16 08:58:05
 
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


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class SaseParser extends Parser {
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
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

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
    public String getGrammarFileName() { return "C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g"; }


    public static class start_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "start"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:59:1: start : ( createStmt | queryStmt );
    public final SaseParser.start_return start() throws RecognitionException {
        SaseParser.start_return retval = new SaseParser.start_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.createStmt_return createStmt1 =null;

        SaseParser.queryStmt_return queryStmt2 =null;



        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:59:8: ( createStmt | queryStmt )
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:59:10: createStmt
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:4: queryStmt
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "start"


    public static class createStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "createStmt"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:63:1: createStmt : CREATE VIEW NAME queryStmt -> ^( CREATEVIEW NAME queryStmt ) ;
    public final SaseParser.createStmt_return createStmt() throws RecognitionException {
        SaseParser.createStmt_return retval = new SaseParser.createStmt_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token CREATE3=null;
        Token VIEW4=null;
        Token NAME5=null;
        SaseParser.queryStmt_return queryStmt6 =null;


        CommonTree CREATE3_tree=null;
        CommonTree VIEW4_tree=null;
        CommonTree NAME5_tree=null;
        RewriteRuleTokenStream stream_CREATE=new RewriteRuleTokenStream(adaptor,"token CREATE");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_VIEW=new RewriteRuleTokenStream(adaptor,"token VIEW");
        RewriteRuleSubtreeStream stream_queryStmt=new RewriteRuleSubtreeStream(adaptor,"rule queryStmt");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:2: ( CREATE VIEW NAME queryStmt -> ^( CREATEVIEW NAME queryStmt ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:4: CREATE VIEW NAME queryStmt
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
            // 64:31: -> ^( CREATEVIEW NAME queryStmt )
            {
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:34: ^( CREATEVIEW NAME queryStmt )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(CREATEVIEW, "CREATEVIEW")
                , root_1);

                adaptor.addChild(root_1, 
                stream_NAME.nextNode()
                );

                adaptor.addChild(root_1, stream_queryStmt.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "createStmt"


    public static class queryStmt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "queryStmt"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:1: queryStmt : patternPart ( wherePart )? ( withinPart )? returnPart -> ^( QUERY patternPart ( wherePart )? ( withinPart )? returnPart ) ;
    public final SaseParser.queryStmt_return queryStmt() throws RecognitionException {
        SaseParser.queryStmt_return retval = new SaseParser.queryStmt_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.patternPart_return patternPart7 =null;

        SaseParser.wherePart_return wherePart8 =null;

        SaseParser.withinPart_return withinPart9 =null;

        SaseParser.returnPart_return returnPart10 =null;


        RewriteRuleSubtreeStream stream_patternPart=new RewriteRuleSubtreeStream(adaptor,"rule patternPart");
        RewriteRuleSubtreeStream stream_returnPart=new RewriteRuleSubtreeStream(adaptor,"rule returnPart");
        RewriteRuleSubtreeStream stream_withinPart=new RewriteRuleSubtreeStream(adaptor,"rule withinPart");
        RewriteRuleSubtreeStream stream_wherePart=new RewriteRuleSubtreeStream(adaptor,"rule wherePart");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:10: ( patternPart ( wherePart )? ( withinPart )? returnPart -> ^( QUERY patternPart ( wherePart )? ( withinPart )? returnPart ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:13: patternPart ( wherePart )? ( withinPart )? returnPart
            {
            pushFollow(FOLLOW_patternPart_in_queryStmt196);
            patternPart7=patternPart();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternPart.add(patternPart7.getTree());

            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:25: ( wherePart )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==WHERE) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:25: wherePart
                    {
                    pushFollow(FOLLOW_wherePart_in_queryStmt198);
                    wherePart8=wherePart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_wherePart.add(wherePart8.getTree());

                    }
                    break;

            }


            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:36: ( withinPart )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==WITHIN) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:36: withinPart
                    {
                    pushFollow(FOLLOW_withinPart_in_queryStmt201);
                    withinPart9=withinPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_withinPart.add(withinPart9.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_returnPart_in_queryStmt204);
            returnPart10=returnPart();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_returnPart.add(returnPart10.getTree());

            // AST REWRITE
            // elements: patternPart, withinPart, wherePart, returnPart
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 67:59: -> ^( QUERY patternPart ( wherePart )? ( withinPart )? returnPart )
            {
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:62: ^( QUERY patternPart ( wherePart )? ( withinPart )? returnPart )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(QUERY, "QUERY")
                , root_1);

                adaptor.addChild(root_1, stream_patternPart.nextTree());

                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:82: ( wherePart )?
                if ( stream_wherePart.hasNext() ) {
                    adaptor.addChild(root_1, stream_wherePart.nextTree());

                }
                stream_wherePart.reset();

                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:93: ( withinPart )?
                if ( stream_withinPart.hasNext() ) {
                    adaptor.addChild(root_1, stream_withinPart.nextTree());

                }
                stream_withinPart.reset();

                adaptor.addChild(root_1, stream_returnPart.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "queryStmt"


    public static class withinPart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "withinPart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:71:1: withinPart : WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) ;
    public final SaseParser.withinPart_return withinPart() throws RecognitionException {
        SaseParser.withinPart_return retval = new SaseParser.withinPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token WITHIN11=null;
        Token NUMBER12=null;
        SaseParser.timeunit_return timeunit13 =null;


        CommonTree WITHIN11_tree=null;
        CommonTree NUMBER12_tree=null;
        RewriteRuleTokenStream stream_WITHIN=new RewriteRuleTokenStream(adaptor,"token WITHIN");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
        RewriteRuleSubtreeStream stream_timeunit=new RewriteRuleSubtreeStream(adaptor,"rule timeunit");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:2: ( WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:4: WITHIN NUMBER ( timeunit )?
            {
            WITHIN11=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart235); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_WITHIN.add(WITHIN11);


            NUMBER12=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart237); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER12);


            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:18: ( timeunit )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==DAY||LA4_0==HOUR||LA4_0==MILLISECOND||LA4_0==MINUTE||LA4_0==SECOND||LA4_0==WEEK) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:18: timeunit
                    {
                    pushFollow(FOLLOW_timeunit_in_withinPart239);
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
            // 72:28: -> ^( WITHIN NUMBER ( timeunit )? )
            {
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:31: ^( WITHIN NUMBER ( timeunit )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_WITHIN.nextNode()
                , root_1);

                adaptor.addChild(root_1, 
                stream_NUMBER.nextNode()
                );

                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:47: ( timeunit )?
                if ( stream_timeunit.hasNext() ) {
                    adaptor.addChild(root_1, stream_timeunit.nextTree());

                }
                stream_timeunit.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "withinPart"


    public static class timeunit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "timeunit"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:75:1: timeunit : ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND );
    public final SaseParser.timeunit_return timeunit() throws RecognitionException {
        SaseParser.timeunit_return retval = new SaseParser.timeunit_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set14=null;

        CommonTree set14_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:75:9: ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set14=(Token)input.LT(1);

            if ( input.LA(1)==DAY||input.LA(1)==HOUR||input.LA(1)==MILLISECOND||input.LA(1)==MINUTE||input.LA(1)==SECOND||input.LA(1)==WEEK ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set14)
                );
                state.errorRecovery=false;
                state.failed=false;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "timeunit"


    public static class wherePart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "wherePart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:1: wherePart : ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
    public final SaseParser.wherePart_return wherePart() throws RecognitionException {
        SaseParser.wherePart_return retval = new SaseParser.wherePart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token WHERE15=null;
        Token LEFTCURLY17=null;
        Token RIGHTCURLY19=null;
        Token WHERE20=null;
        SaseParser.skipPart_return skipPart16 =null;

        SaseParser.whereExpressions_return whereExpressions18 =null;

        SaseParser.whereExpressions_return whereExpressions21 =null;


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
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:2: ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==WHERE) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==PARTITION_CONTIGUITY||(LA5_1 >= SKIP_TILL_ANY_MATCH && LA5_1 <= SKIP_TILL_NEXT_MATCH)||LA5_1==STRICT_CONTIGUITY) ) {
                    alt5=1;
                }
                else if ( ((LA5_1 >= AVG && LA5_1 <= BBRACKETLEFT)||LA5_1==COUNT||LA5_1==LBRACKET||LA5_1==MAX||LA5_1==MIN||LA5_1==NAME||LA5_1==NUMBER||(LA5_1 >= STRING_LITERAL && LA5_1 <= SUM)) ) {
                    alt5=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:4: WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    WHERE15=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart281); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE15);


                    pushFollow(FOLLOW_skipPart_in_wherePart283);
                    skipPart16=skipPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_skipPart.add(skipPart16.getTree());

                    LEFTCURLY17=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart285); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFTCURLY.add(LEFTCURLY17);


                    pushFollow(FOLLOW_whereExpressions_in_wherePart287);
                    whereExpressions18=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions18.getTree());

                    RIGHTCURLY19=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart289); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHTCURLY.add(RIGHTCURLY19);


                    // AST REWRITE
                    // elements: WHERE, skipPart, whereExpressions
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 78:57: -> ^( WHERE skipPart whereExpressions )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:60: ^( WHERE skipPart whereExpressions )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_WHERE.nextNode()
                        , root_1);

                        adaptor.addChild(root_1, stream_skipPart.nextTree());

                        adaptor.addChild(root_1, stream_whereExpressions.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:4: WHERE whereExpressions
                    {
                    WHERE20=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart306); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE20);


                    pushFollow(FOLLOW_whereExpressions_in_wherePart308);
                    whereExpressions21=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions21.getTree());

                    // AST REWRITE
                    // elements: whereExpressions, WHERE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 79:27: -> ^( WHERE whereExpressions )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:30: ^( WHERE whereExpressions )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_WHERE.nextNode()
                        , root_1);

                        adaptor.addChild(root_1, stream_whereExpressions.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "wherePart"


    public static class skipPart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "skipPart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:1: skipPart : skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) ;
    public final SaseParser.skipPart_return skipPart() throws RecognitionException {
        SaseParser.skipPart_return retval = new SaseParser.skipPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LBRACKET23=null;
        Token RBRACKET25=null;
        SaseParser.skipMethod_return skipMethod22 =null;

        SaseParser.parameterList_return parameterList24 =null;


        CommonTree LBRACKET23_tree=null;
        CommonTree RBRACKET25_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_skipMethod=new RewriteRuleSubtreeStream(adaptor,"rule skipMethod");
        RewriteRuleSubtreeStream stream_parameterList=new RewriteRuleSubtreeStream(adaptor,"rule parameterList");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:2: ( skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:4: skipMethod LBRACKET parameterList RBRACKET
            {
            pushFollow(FOLLOW_skipMethod_in_skipPart327);
            skipMethod22=skipMethod();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_skipMethod.add(skipMethod22.getTree());

            LBRACKET23=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_skipPart329); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET23);


            pushFollow(FOLLOW_parameterList_in_skipPart331);
            parameterList24=parameterList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_parameterList.add(parameterList24.getTree());

            RBRACKET25=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_skipPart333); if (state.failed) return retval; 
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
            // 84:2: -> ^( skipMethod parameterList )
            {
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:5: ^( skipMethod parameterList )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_skipMethod.nextNode(), root_1);

                adaptor.addChild(root_1, stream_parameterList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "skipPart"


    public static class skipMethod_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "skipMethod"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:1: skipMethod : ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY );
    public final SaseParser.skipMethod_return skipMethod() throws RecognitionException {
        SaseParser.skipMethod_return retval = new SaseParser.skipMethod_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set26=null;

        CommonTree set26_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:11: ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set26=(Token)input.LT(1);

            if ( input.LA(1)==PARTITION_CONTIGUITY||(input.LA(1) >= SKIP_TILL_ANY_MATCH && input.LA(1) <= SKIP_TILL_NEXT_MATCH)||input.LA(1)==STRICT_CONTIGUITY ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set26)
                );
                state.errorRecovery=false;
                state.failed=false;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "skipMethod"


    public static class patternPart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "patternPart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:1: patternPart : PATTERN ( patternDecl )* -> ^( PATTERN ( patternDecl )* ) ;
    public final SaseParser.patternPart_return patternPart() throws RecognitionException {
        SaseParser.patternPart_return retval = new SaseParser.patternPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PATTERN27=null;
        SaseParser.patternDecl_return patternDecl28 =null;


        CommonTree PATTERN27_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleSubtreeStream stream_patternDecl=new RewriteRuleSubtreeStream(adaptor,"rule patternDecl");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:2: ( PATTERN ( patternDecl )* -> ^( PATTERN ( patternDecl )* ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:4: PATTERN ( patternDecl )*
            {
            PATTERN27=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart382); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN27);


            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:12: ( patternDecl )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==SEQ) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:12: patternDecl
            	    {
            	    pushFollow(FOLLOW_patternDecl_in_patternPart384);
            	    patternDecl28=patternDecl();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_patternDecl.add(patternDecl28.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


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

            root_0 = (CommonTree)adaptor.nil();
            // 94:25: -> ^( PATTERN ( patternDecl )* )
            {
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:28: ^( PATTERN ( patternDecl )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_PATTERN.nextNode()
                , root_1);

                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:38: ( patternDecl )*
                while ( stream_patternDecl.hasNext() ) {
                    adaptor.addChild(root_1, stream_patternDecl.nextTree());

                }
                stream_patternDecl.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "patternPart"


    public static class returnPart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "returnPart"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:97:1: returnPart : RETURN attributeTerm ( COMMA attributeTerm )* ( AS NAME )? -> ^( RETURN ( attributeTerm )* ( NAME )? ) ;
    public final SaseParser.returnPart_return returnPart() throws RecognitionException {
        SaseParser.returnPart_return retval = new SaseParser.returnPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token RETURN29=null;
        Token COMMA31=null;
        Token AS33=null;
        Token NAME34=null;
        SaseParser.attributeTerm_return attributeTerm30 =null;

        SaseParser.attributeTerm_return attributeTerm32 =null;


        CommonTree RETURN29_tree=null;
        CommonTree COMMA31_tree=null;
        CommonTree AS33_tree=null;
        CommonTree NAME34_tree=null;
        RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RETURN=new RewriteRuleTokenStream(adaptor,"token RETURN");
        RewriteRuleSubtreeStream stream_attributeTerm=new RewriteRuleSubtreeStream(adaptor,"rule attributeTerm");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:2: ( RETURN attributeTerm ( COMMA attributeTerm )* ( AS NAME )? -> ^( RETURN ( attributeTerm )* ( NAME )? ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:5: RETURN attributeTerm ( COMMA attributeTerm )* ( AS NAME )?
            {
            RETURN29=(Token)match(input,RETURN,FOLLOW_RETURN_in_returnPart407); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RETURN.add(RETURN29);


            pushFollow(FOLLOW_attributeTerm_in_returnPart409);
            attributeTerm30=attributeTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeTerm.add(attributeTerm30.getTree());

            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:26: ( COMMA attributeTerm )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==COMMA) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:27: COMMA attributeTerm
            	    {
            	    COMMA31=(Token)match(input,COMMA,FOLLOW_COMMA_in_returnPart412); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA31);


            	    pushFollow(FOLLOW_attributeTerm_in_returnPart414);
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


            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:49: ( AS NAME )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==AS) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:50: AS NAME
                    {
                    AS33=(Token)match(input,AS,FOLLOW_AS_in_returnPart419); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AS.add(AS33);


                    NAME34=(Token)match(input,NAME,FOLLOW_NAME_in_returnPart421); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME34);


                    }
                    break;

            }


            // AST REWRITE
            // elements: NAME, attributeTerm, RETURN
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 98:59: -> ^( RETURN ( attributeTerm )* ( NAME )? )
            {
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:62: ^( RETURN ( attributeTerm )* ( NAME )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_RETURN.nextNode()
                , root_1);

                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:71: ( attributeTerm )*
                while ( stream_attributeTerm.hasNext() ) {
                    adaptor.addChild(root_1, stream_attributeTerm.nextTree());

                }
                stream_attributeTerm.reset();

                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:86: ( NAME )?
                if ( stream_NAME.hasNext() ) {
                    adaptor.addChild(root_1, 
                    stream_NAME.nextNode()
                    );

                }
                stream_NAME.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "returnPart"


    public static class patternDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "patternDecl"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:1: patternDecl : seqPatternDecl ;
    public final SaseParser.patternDecl_return patternDecl() throws RecognitionException {
        SaseParser.patternDecl_return retval = new SaseParser.patternDecl_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.seqPatternDecl_return seqPatternDecl35 =null;



        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:2: ( seqPatternDecl )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:5: seqPatternDecl
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_seqPatternDecl_in_patternDecl447);
            seqPatternDecl35=seqPatternDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, seqPatternDecl35.getTree());

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "patternDecl"


    public static class seqPatternDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "seqPatternDecl"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:105:1: seqPatternDecl : SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) ;
    public final SaseParser.seqPatternDecl_return seqPatternDecl() throws RecognitionException {
        SaseParser.seqPatternDecl_return retval = new SaseParser.seqPatternDecl_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token SEQ36=null;
        Token LBRACKET37=null;
        Token COMMA39=null;
        Token RBRACKET41=null;
        SaseParser.stateDef_return stateDef38 =null;

        SaseParser.stateDef_return stateDef40 =null;


        CommonTree SEQ36_tree=null;
        CommonTree LBRACKET37_tree=null;
        CommonTree COMMA39_tree=null;
        CommonTree RBRACKET41_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SEQ=new RewriteRuleTokenStream(adaptor,"token SEQ");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_stateDef=new RewriteRuleSubtreeStream(adaptor,"rule stateDef");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:2: ( SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:4: SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET
            {
            SEQ36=(Token)match(input,SEQ,FOLLOW_SEQ_in_seqPatternDecl459); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEQ.add(SEQ36);


            LBRACKET37=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_seqPatternDecl461); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET37);


            pushFollow(FOLLOW_stateDef_in_seqPatternDecl463);
            stateDef38=stateDef();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_stateDef.add(stateDef38.getTree());

            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:26: ( COMMA stateDef )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==COMMA) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:27: COMMA stateDef
            	    {
            	    COMMA39=(Token)match(input,COMMA,FOLLOW_COMMA_in_seqPatternDecl466); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA39);


            	    pushFollow(FOLLOW_stateDef_in_seqPatternDecl468);
            	    stateDef40=stateDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_stateDef.add(stateDef40.getTree());

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            RBRACKET41=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_seqPatternDecl472); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET41);


            // AST REWRITE
            // elements: stateDef, SEQ
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 106:53: -> ^( SEQ ( stateDef )* )
            {
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:56: ^( SEQ ( stateDef )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_SEQ.nextNode()
                , root_1);

                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:62: ( stateDef )*
                while ( stream_stateDef.hasNext() ) {
                    adaptor.addChild(root_1, stream_stateDef.nextTree());

                }
                stream_stateDef.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "seqPatternDecl"


    public static class stateDef_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "stateDef"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:1: stateDef : ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) );
    public final SaseParser.stateDef_return stateDef() throws RecognitionException {
        SaseParser.stateDef_return retval = new SaseParser.stateDef_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NOTSIGN42=null;
        Token NOTSIGN44=null;
        Token NOTSIGN46=null;
        Token LBRACKET47=null;
        Token RBRACKET49=null;
        Token NOTSIGN50=null;
        Token LBRACKET51=null;
        Token RBRACKET53=null;
        SaseParser.ktypeDefinition_return ktypeDefinition43 =null;

        SaseParser.typeDefinition_return typeDefinition45 =null;

        SaseParser.ktypeDefinition_return ktypeDefinition48 =null;

        SaseParser.typeDefinition_return typeDefinition52 =null;


        CommonTree NOTSIGN42_tree=null;
        CommonTree NOTSIGN44_tree=null;
        CommonTree NOTSIGN46_tree=null;
        CommonTree LBRACKET47_tree=null;
        CommonTree RBRACKET49_tree=null;
        CommonTree NOTSIGN50_tree=null;
        CommonTree LBRACKET51_tree=null;
        CommonTree RBRACKET53_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NOTSIGN=new RewriteRuleTokenStream(adaptor,"token NOTSIGN");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_typeDefinition=new RewriteRuleSubtreeStream(adaptor,"rule typeDefinition");
        RewriteRuleSubtreeStream stream_ktypeDefinition=new RewriteRuleSubtreeStream(adaptor,"rule ktypeDefinition");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:10: ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) )
            int alt14=4;
            switch ( input.LA(1) ) {
            case NOTSIGN:
                {
                int LA14_1 = input.LA(2);

                if ( (LA14_1==NAME) ) {
                    int LA14_2 = input.LA(3);

                    if ( (LA14_2==PLUS) ) {
                        alt14=1;
                    }
                    else if ( (LA14_2==NAME) ) {
                        alt14=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 2, input);

                        throw nvae;

                    }
                }
                else if ( (LA14_1==LBRACKET) ) {
                    int LA14_3 = input.LA(3);

                    if ( (LA14_3==NAME) ) {
                        int LA14_6 = input.LA(4);

                        if ( (LA14_6==PLUS) ) {
                            alt14=3;
                        }
                        else if ( (LA14_6==NAME) ) {
                            alt14=4;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 14, 6, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 3, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;

                }
                }
                break;
            case NAME:
                {
                int LA14_2 = input.LA(2);

                if ( (LA14_2==PLUS) ) {
                    alt14=1;
                }
                else if ( (LA14_2==NAME) ) {
                    alt14=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 2, input);

                    throw nvae;

                }
                }
                break;
            case LBRACKET:
                {
                int LA14_3 = input.LA(2);

                if ( (LA14_3==NAME) ) {
                    int LA14_6 = input.LA(3);

                    if ( (LA14_6==PLUS) ) {
                        alt14=3;
                    }
                    else if ( (LA14_6==NAME) ) {
                        alt14=4;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 6, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 3, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }

            switch (alt14) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:12: ( NOTSIGN )? ktypeDefinition
                    {
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:12: ( NOTSIGN )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==NOTSIGN) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:13: NOTSIGN
                            {
                            NOTSIGN42=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef496); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN42);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef500);
                    ktypeDefinition43=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition43.getTree());

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
                    // 110:40: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:43: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(KSTATE, "KSTATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());

                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:68: ( NOTSIGN )?
                        if ( stream_NOTSIGN.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_NOTSIGN.nextNode()
                            );

                        }
                        stream_NOTSIGN.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:5: ( NOTSIGN )? typeDefinition
                    {
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:5: ( NOTSIGN )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==NOTSIGN) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:6: NOTSIGN
                            {
                            NOTSIGN44=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef519); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN44);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_typeDefinition_in_stateDef523);
                    typeDefinition45=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition45.getTree());

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
                    // 111:32: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:35: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(STATE, "STATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());

                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:58: ( NOTSIGN )?
                        if ( stream_NOTSIGN.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_NOTSIGN.nextNode()
                            );

                        }
                        stream_NOTSIGN.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:5: ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET
                    {
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:5: ( NOTSIGN )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==NOTSIGN) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:6: NOTSIGN
                            {
                            NOTSIGN46=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef544); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN46);


                            }
                            break;

                    }


                    LBRACKET47=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef548); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET47);


                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef550);
                    ktypeDefinition48=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition48.getTree());

                    RBRACKET49=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef552); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET49);


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
                    // 112:50: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:53: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(KSTATE, "KSTATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());

                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:78: ( NOTSIGN )?
                        if ( stream_NOTSIGN.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_NOTSIGN.nextNode()
                            );

                        }
                        stream_NOTSIGN.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:5: ( NOTSIGN )? LBRACKET typeDefinition RBRACKET
                    {
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:5: ( NOTSIGN )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==NOTSIGN) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:6: NOTSIGN
                            {
                            NOTSIGN50=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef570); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN50);


                            }
                            break;

                    }


                    LBRACKET51=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef574); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET51);


                    pushFollow(FOLLOW_typeDefinition_in_stateDef576);
                    typeDefinition52=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition52.getTree());

                    RBRACKET53=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef578); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET53);


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
                    // 113:49: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:52: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(STATE, "STATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());

                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:75: ( NOTSIGN )?
                        if ( stream_NOTSIGN.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_NOTSIGN.nextNode()
                            );

                        }
                        stream_NOTSIGN.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "stateDef"


    public static class typeDefinition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "typeDefinition"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:1: typeDefinition : NAME sAttributeName ;
    public final SaseParser.typeDefinition_return typeDefinition() throws RecognitionException {
        SaseParser.typeDefinition_return retval = new SaseParser.typeDefinition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME54=null;
        SaseParser.sAttributeName_return sAttributeName55 =null;


        CommonTree NAME54_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:2: ( NAME sAttributeName )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:4: NAME sAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME54=(Token)match(input,NAME,FOLLOW_NAME_in_typeDefinition603); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME54_tree = 
            (CommonTree)adaptor.create(NAME54)
            ;
            adaptor.addChild(root_0, NAME54_tree);
            }

            pushFollow(FOLLOW_sAttributeName_in_typeDefinition605);
            sAttributeName55=sAttributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, sAttributeName55.getTree());

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "typeDefinition"


    public static class ktypeDefinition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "ktypeDefinition"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:120:1: ktypeDefinition : NAME PLUS kAttributeName ;
    public final SaseParser.ktypeDefinition_return ktypeDefinition() throws RecognitionException {
        SaseParser.ktypeDefinition_return retval = new SaseParser.ktypeDefinition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME56=null;
        Token PLUS57=null;
        SaseParser.kAttributeName_return kAttributeName58 =null;


        CommonTree NAME56_tree=null;
        CommonTree PLUS57_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:121:2: ( NAME PLUS kAttributeName )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:121:4: NAME PLUS kAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME56=(Token)match(input,NAME,FOLLOW_NAME_in_ktypeDefinition616); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME56_tree = 
            (CommonTree)adaptor.create(NAME56)
            ;
            adaptor.addChild(root_0, NAME56_tree);
            }

            PLUS57=(Token)match(input,PLUS,FOLLOW_PLUS_in_ktypeDefinition618); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PLUS57_tree = 
            (CommonTree)adaptor.create(PLUS57)
            ;
            adaptor.addChild(root_0, PLUS57_tree);
            }

            pushFollow(FOLLOW_kAttributeName_in_ktypeDefinition620);
            kAttributeName58=kAttributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, kAttributeName58.getTree());

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ktypeDefinition"


    public static class parameterList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "parameterList"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:125:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
    public final SaseParser.parameterList_return parameterList() throws RecognitionException {
        SaseParser.parameterList_return retval = new SaseParser.parameterList_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token COMMA60=null;
        SaseParser.attributeName_return attributeName59 =null;

        SaseParser.attributeName_return attributeName61 =null;


        CommonTree COMMA60_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:4: attributeName ( COMMA attributeName )*
            {
            pushFollow(FOLLOW_attributeName_in_parameterList634);
            attributeName59=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(attributeName59.getTree());

            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:18: ( COMMA attributeName )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==COMMA) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:19: COMMA attributeName
            	    {
            	    COMMA60=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList637); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA60);


            	    pushFollow(FOLLOW_attributeName_in_parameterList639);
            	    attributeName61=attributeName();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeName.add(attributeName61.getTree());

            	    }
            	    break;

            	default :
            	    break loop15;
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
            // 126:41: -> ^( PARAMLIST ( attributeName )* )
            {
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:44: ^( PARAMLIST ( attributeName )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(PARAMLIST, "PARAMLIST")
                , root_1);

                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:56: ( attributeName )*
                while ( stream_attributeName.hasNext() ) {
                    adaptor.addChild(root_1, stream_attributeName.nextTree());

                }
                stream_attributeName.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "parameterList"


    public static class attributeName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attributeName"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:129:1: attributeName : ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.kAttributeName_return kAttributeName62 =null;

        SaseParser.sAttributeName_return sAttributeName63 =null;


        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_kAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeName");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:130:2: ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==NAME) ) {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==BBRACKETLEFT) ) {
                    alt16=1;
                }
                else if ( (LA16_1==EOF||LA16_1==COMMA||LA16_1==RBRACKET) ) {
                    alt16=2;
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
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:130:5: kAttributeName
                    {
                    pushFollow(FOLLOW_kAttributeName_in_attributeName663);
                    kAttributeName62=kAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeName.add(kAttributeName62.getTree());

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
                    // 130:20: -> ^( KATTRIBUTE NAME )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:130:23: ^( KATTRIBUTE NAME )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(KATTRIBUTE, "KATTRIBUTE")
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(NAME, "NAME")
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:131:4: sAttributeName
                    {
                    pushFollow(FOLLOW_sAttributeName_in_attributeName677);
                    sAttributeName63=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sAttributeName.add(sAttributeName63.getTree());

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
                    // 131:19: -> ^( ATTRIBUTE NAME )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:131:22: ^( ATTRIBUTE NAME )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(ATTRIBUTE, "ATTRIBUTE")
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(NAME, "NAME")
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attributeName"


    public static class kAttributeName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "kAttributeName"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:134:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT ;
    public final SaseParser.kAttributeName_return kAttributeName() throws RecognitionException {
        SaseParser.kAttributeName_return retval = new SaseParser.kAttributeName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME64=null;
        Token BBRACKETLEFT65=null;
        Token BBRACKETRIGHT66=null;

        CommonTree NAME64_tree=null;
        CommonTree BBRACKETLEFT65_tree=null;
        CommonTree BBRACKETRIGHT66_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:135:2: ( NAME BBRACKETLEFT BBRACKETRIGHT )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:135:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME64=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName699); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME64_tree = 
            (CommonTree)adaptor.create(NAME64)
            ;
            adaptor.addChild(root_0, NAME64_tree);
            }

            BBRACKETLEFT65=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName702); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT65_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT65)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT65_tree);
            }

            BBRACKETRIGHT66=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName704); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT66_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT66)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT66_tree);
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "kAttributeName"


    public static class sAttributeName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "sAttributeName"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:138:1: sAttributeName : NAME ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME67=null;

        CommonTree NAME67_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:139:2: ( NAME )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:139:4: NAME
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME67=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName718); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME67_tree = 
            (CommonTree)adaptor.create(NAME67)
            ;
            adaptor.addChild(root_0, NAME67_tree);
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "sAttributeName"


    public static class kAttributeUsage_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "kAttributeUsage"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:142:1: kAttributeUsage : ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) );
    public final SaseParser.kAttributeUsage_return kAttributeUsage() throws RecognitionException {
        SaseParser.kAttributeUsage_return retval = new SaseParser.kAttributeUsage_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME68=null;
        Token NAME70=null;
        Token NAME72=null;
        Token NAME74=null;
        SaseParser.current_return current69 =null;

        SaseParser.first_return first71 =null;

        SaseParser.last_return last73 =null;

        SaseParser.len_return len75 =null;


        CommonTree NAME68_tree=null;
        CommonTree NAME70_tree=null;
        CommonTree NAME72_tree=null;
        CommonTree NAME74_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleSubtreeStream stream_last=new RewriteRuleSubtreeStream(adaptor,"rule last");
        RewriteRuleSubtreeStream stream_current=new RewriteRuleSubtreeStream(adaptor,"rule current");
        RewriteRuleSubtreeStream stream_len=new RewriteRuleSubtreeStream(adaptor,"rule len");
        RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:143:2: ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) )
            int alt17=4;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==NAME) ) {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==BBRACKETLEFT) ) {
                    int LA17_2 = input.LA(3);

                    if ( (LA17_2==NAME) ) {
                        switch ( input.LA(4) ) {
                        case BBRACKETRIGHT:
                            {
                            alt17=1;
                            }
                            break;
                        case MINUS:
                            {
                            alt17=3;
                            }
                            break;
                        case POINT:
                            {
                            alt17=4;
                            }
                            break;
                        default:
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 17, 3, input);

                            throw nvae;

                        }

                    }
                    else if ( (LA17_2==NUMBER) ) {
                        alt17=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 2, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }
            switch (alt17) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:143:4: NAME current
                    {
                    NAME68=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage731); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME68);


                    pushFollow(FOLLOW_current_in_kAttributeUsage734);
                    current69=current();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_current.add(current69.getTree());

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
                    // 143:19: -> ^( NAME CURRENT )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:143:22: ^( NAME CURRENT )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_NAME.nextNode()
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(CURRENT, "CURRENT")
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:144:4: NAME first
                    {
                    NAME70=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage748); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME70);


                    pushFollow(FOLLOW_first_in_kAttributeUsage750);
                    first71=first();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_first.add(first71.getTree());

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
                    // 144:15: -> ^( NAME FIRST )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:144:18: ^( NAME FIRST )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_NAME.nextNode()
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(FIRST, "FIRST")
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:145:4: NAME last
                    {
                    NAME72=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage763); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME72);


                    pushFollow(FOLLOW_last_in_kAttributeUsage765);
                    last73=last();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_last.add(last73.getTree());

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
                    // 145:14: -> ^( NAME PREV )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:145:17: ^( NAME PREV )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_NAME.nextNode()
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(PREV, "PREV")
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:146:4: NAME len
                    {
                    NAME74=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage778); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME74);


                    pushFollow(FOLLOW_len_in_kAttributeUsage780);
                    len75=len();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_len.add(len75.getTree());

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
                    // 146:13: -> ^( NAME LEN )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:146:16: ^( NAME LEN )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_NAME.nextNode()
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(LEN, "LEN")
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "kAttributeUsage"


    public static class current_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "current"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:149:1: current : BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?;
    public final SaseParser.current_return current() throws RecognitionException {
        SaseParser.current_return retval = new SaseParser.current_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token name=null;
        Token BBRACKETLEFT76=null;
        Token BBRACKETRIGHT77=null;

        CommonTree name_tree=null;
        CommonTree BBRACKETLEFT76_tree=null;
        CommonTree BBRACKETRIGHT77_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:149:9: ( BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?)
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:2: BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT76=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_current801); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT76_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT76)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT76_tree);
            }

            name=(Token)match(input,NAME,FOLLOW_NAME_in_current805); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = 
            (CommonTree)adaptor.create(name)
            ;
            adaptor.addChild(root_0, name_tree);
            }

            BBRACKETRIGHT77=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_current807); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT77_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT77)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT77_tree);
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "current"


    public static class first_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "first"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:1: first : BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?;
    public final SaseParser.first_return first() throws RecognitionException {
        SaseParser.first_return retval = new SaseParser.first_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token number=null;
        Token BBRACKETLEFT78=null;
        Token BBRACKETRIGHT79=null;

        CommonTree number_tree=null;
        CommonTree BBRACKETLEFT78_tree=null;
        CommonTree BBRACKETRIGHT79_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:7: ( BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?)
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:154:2: BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT78=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_first822); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT78_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT78)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT78_tree);
            }

            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_first826); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = 
            (CommonTree)adaptor.create(number)
            ;
            adaptor.addChild(root_0, number_tree);
            }

            BBRACKETRIGHT79=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_first828); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT79_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT79)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT79_tree);
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "first"


    public static class last_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "last"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:1: last : BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?;
    public final SaseParser.last_return last() throws RecognitionException {
        SaseParser.last_return retval = new SaseParser.last_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token name=null;
        Token number=null;
        Token BBRACKETLEFT80=null;
        Token MINUS81=null;
        Token BBRACKETRIGHT82=null;

        CommonTree name_tree=null;
        CommonTree number_tree=null;
        CommonTree BBRACKETLEFT80_tree=null;
        CommonTree MINUS81_tree=null;
        CommonTree BBRACKETRIGHT82_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:6: ( BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?)
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:158:2: BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT80=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_last841); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT80_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT80)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT80_tree);
            }

            name=(Token)match(input,NAME,FOLLOW_NAME_in_last845); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = 
            (CommonTree)adaptor.create(name)
            ;
            adaptor.addChild(root_0, name_tree);
            }

            MINUS81=(Token)match(input,MINUS,FOLLOW_MINUS_in_last847); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            MINUS81_tree = 
            (CommonTree)adaptor.create(MINUS81)
            ;
            adaptor.addChild(root_0, MINUS81_tree);
            }

            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_last851); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = 
            (CommonTree)adaptor.create(number)
            ;
            adaptor.addChild(root_0, number_tree);
            }

            BBRACKETRIGHT82=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_last853); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT82_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT82)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT82_tree);
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "last"


    public static class len_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "len"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:161:1: len : BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT ;
    public final SaseParser.len_return len() throws RecognitionException {
        SaseParser.len_return retval = new SaseParser.len_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token BBRACKETLEFT83=null;
        Token NAME84=null;
        Token POINT85=null;
        Token LEN86=null;
        Token BBRACKETRIGHT87=null;

        CommonTree BBRACKETLEFT83_tree=null;
        CommonTree NAME84_tree=null;
        CommonTree POINT85_tree=null;
        CommonTree LEN86_tree=null;
        CommonTree BBRACKETRIGHT87_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:161:5: ( BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:162:2: BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT83=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_len869); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT83_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT83)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT83_tree);
            }

            NAME84=(Token)match(input,NAME,FOLLOW_NAME_in_len871); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME84_tree = 
            (CommonTree)adaptor.create(NAME84)
            ;
            adaptor.addChild(root_0, NAME84_tree);
            }

            POINT85=(Token)match(input,POINT,FOLLOW_POINT_in_len873); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            POINT85_tree = 
            (CommonTree)adaptor.create(POINT85)
            ;
            adaptor.addChild(root_0, POINT85_tree);
            }

            LEN86=(Token)match(input,LEN,FOLLOW_LEN_in_len875); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEN86_tree = 
            (CommonTree)adaptor.create(LEN86)
            ;
            adaptor.addChild(root_0, LEN86_tree);
            }

            BBRACKETRIGHT87=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_len877); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT87_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT87)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT87_tree);
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "len"


    public static class whereExpressions_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "whereExpressions"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:165:1: whereExpressions : expression ( ( AND | COMMA ) expression )* -> ^( WHEREEXPRESSION ( expression )* ) ;
    public final SaseParser.whereExpressions_return whereExpressions() throws RecognitionException {
        SaseParser.whereExpressions_return retval = new SaseParser.whereExpressions_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token AND89=null;
        Token COMMA90=null;
        SaseParser.expression_return expression88 =null;

        SaseParser.expression_return expression91 =null;


        CommonTree AND89_tree=null;
        CommonTree COMMA90_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:2: ( expression ( ( AND | COMMA ) expression )* -> ^( WHEREEXPRESSION ( expression )* ) )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:4: expression ( ( AND | COMMA ) expression )*
            {
            pushFollow(FOLLOW_expression_in_whereExpressions890);
            expression88=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression88.getTree());

            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:15: ( ( AND | COMMA ) expression )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==AND||LA19_0==COMMA) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:16: ( AND | COMMA ) expression
            	    {
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:16: ( AND | COMMA )
            	    int alt18=2;
            	    int LA18_0 = input.LA(1);

            	    if ( (LA18_0==AND) ) {
            	        alt18=1;
            	    }
            	    else if ( (LA18_0==COMMA) ) {
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
            	            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:17: AND
            	            {
            	            AND89=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions894); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_AND.add(AND89);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:21: COMMA
            	            {
            	            COMMA90=(Token)match(input,COMMA,FOLLOW_COMMA_in_whereExpressions896); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_COMMA.add(COMMA90);


            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_expression_in_whereExpressions899);
            	    expression91=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression91.getTree());

            	    }
            	    break;

            	default :
            	    break loop19;
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
            // 166:41: -> ^( WHEREEXPRESSION ( expression )* )
            {
                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:44: ^( WHEREEXPRESSION ( expression )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION")
                , root_1);

                // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:62: ( expression )*
                while ( stream_expression.hasNext() ) {
                    adaptor.addChild(root_1, stream_expression.nextTree());

                }
                stream_expression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "whereExpressions"


    public static class expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "expression"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:169:1: expression : ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) |f1= mathExpression (op= SINGLEEQUALS |op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) | sAttributeName ASSIGN mathExpression -> ^( ASSIGNMENT sAttributeName mathExpression ) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token op=null;
        Token BBRACKETLEFT92=null;
        Token NAME93=null;
        Token BBRACKETRIGHT94=null;
        Token ASSIGN96=null;
        SaseParser.mathExpression_return f1 =null;

        SaseParser.mathExpression_return f2 =null;

        SaseParser.sAttributeName_return sAttributeName95 =null;

        SaseParser.mathExpression_return mathExpression97 =null;


        CommonTree op_tree=null;
        CommonTree BBRACKETLEFT92_tree=null;
        CommonTree NAME93_tree=null;
        CommonTree BBRACKETRIGHT94_tree=null;
        CommonTree ASSIGN96_tree=null;
        RewriteRuleTokenStream stream_SINGLEEQUALS=new RewriteRuleTokenStream(adaptor,"token SINGLEEQUALS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");
        RewriteRuleTokenStream stream_ASSIGN=new RewriteRuleTokenStream(adaptor,"token ASSIGN");
        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_mathExpression=new RewriteRuleSubtreeStream(adaptor,"rule mathExpression");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:170:2: ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) |f1= mathExpression (op= SINGLEEQUALS |op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) | sAttributeName ASSIGN mathExpression -> ^( ASSIGNMENT sAttributeName mathExpression ) )
            int alt21=3;
            switch ( input.LA(1) ) {
            case BBRACKETLEFT:
                {
                alt21=1;
                }
                break;
            case AVG:
            case COUNT:
            case LBRACKET:
            case MAX:
            case MIN:
            case NUMBER:
            case STRING_LITERAL:
            case SUM:
                {
                alt21=2;
                }
                break;
            case NAME:
                {
                int LA21_3 = input.LA(2);

                if ( (LA21_3==BBRACKETLEFT||LA21_3==POINT) ) {
                    alt21=2;
                }
                else if ( (LA21_3==ASSIGN) ) {
                    alt21=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 3, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }

            switch (alt21) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:170:4: BBRACKETLEFT NAME BBRACKETRIGHT
                    {
                    BBRACKETLEFT92=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_expression922); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT92);


                    NAME93=(Token)match(input,NAME,FOLLOW_NAME_in_expression924); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME93);


                    BBRACKETRIGHT94=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_expression926); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT94);


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
                    // 170:36: -> ^( IDEXPRESSION NAME )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:170:39: ^( IDEXPRESSION NAME )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(IDEXPRESSION, "IDEXPRESSION")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_NAME.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:3: f1= mathExpression (op= SINGLEEQUALS |op= COMPAREOP ) f2= mathExpression
                    {
                    pushFollow(FOLLOW_mathExpression_in_expression942);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());

                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:21: (op= SINGLEEQUALS |op= COMPAREOP )
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==SINGLEEQUALS) ) {
                        alt20=1;
                    }
                    else if ( (LA20_0==COMPAREOP) ) {
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
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:22: op= SINGLEEQUALS
                            {
                            op=(Token)match(input,SINGLEEQUALS,FOLLOW_SINGLEEQUALS_in_expression947); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_SINGLEEQUALS.add(op);


                            }
                            break;
                        case 2 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:38: op= COMPAREOP
                            {
                            op=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression951); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COMPAREOP.add(op);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_mathExpression_in_expression956);
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
                    // 171:70: -> ^( COMPAREEXPRESSION $f1 $op $f2)
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:74: ^( COMPAREEXPRESSION $f1 $op $f2)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(COMPAREEXPRESSION, "COMPAREEXPRESSION")
                        , root_1);

                        adaptor.addChild(root_1, stream_f1.nextTree());

                        adaptor.addChild(root_1, stream_op.nextNode());

                        adaptor.addChild(root_1, stream_f2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:3: sAttributeName ASSIGN mathExpression
                    {
                    pushFollow(FOLLOW_sAttributeName_in_expression978);
                    sAttributeName95=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sAttributeName.add(sAttributeName95.getTree());

                    ASSIGN96=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_expression980); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ASSIGN.add(ASSIGN96);


                    pushFollow(FOLLOW_mathExpression_in_expression982);
                    mathExpression97=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(mathExpression97.getTree());

                    // AST REWRITE
                    // elements: sAttributeName, mathExpression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 172:40: -> ^( ASSIGNMENT sAttributeName mathExpression )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:43: ^( ASSIGNMENT sAttributeName mathExpression )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(ASSIGNMENT, "ASSIGNMENT")
                        , root_1);

                        adaptor.addChild(root_1, stream_sAttributeName.nextTree());

                        adaptor.addChild(root_1, stream_mathExpression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "expression"


    public static class mathExpression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mathExpression"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:1: mathExpression : mult ( ( PLUS ^| MINUS ^) mult )* ;
    public final SaseParser.mathExpression_return mathExpression() throws RecognitionException {
        SaseParser.mathExpression_return retval = new SaseParser.mathExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PLUS99=null;
        Token MINUS100=null;
        SaseParser.mult_return mult98 =null;

        SaseParser.mult_return mult101 =null;


        CommonTree PLUS99_tree=null;
        CommonTree MINUS100_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:15: ( mult ( ( PLUS ^| MINUS ^) mult )* )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:17: mult ( ( PLUS ^| MINUS ^) mult )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_mult_in_mathExpression1002);
            mult98=mult();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mult98.getTree());

            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:22: ( ( PLUS ^| MINUS ^) mult )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==MINUS||LA23_0==PLUS) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:23: ( PLUS ^| MINUS ^) mult
            	    {
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:23: ( PLUS ^| MINUS ^)
            	    int alt22=2;
            	    int LA22_0 = input.LA(1);

            	    if ( (LA22_0==PLUS) ) {
            	        alt22=1;
            	    }
            	    else if ( (LA22_0==MINUS) ) {
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
            	            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:24: PLUS ^
            	            {
            	            PLUS99=(Token)match(input,PLUS,FOLLOW_PLUS_in_mathExpression1006); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            PLUS99_tree = 
            	            (CommonTree)adaptor.create(PLUS99)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(PLUS99_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:30: MINUS ^
            	            {
            	            MINUS100=(Token)match(input,MINUS,FOLLOW_MINUS_in_mathExpression1009); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MINUS100_tree = 
            	            (CommonTree)adaptor.create(MINUS100)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(MINUS100_tree, root_0);
            	            }

            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_mult_in_mathExpression1013);
            	    mult101=mult();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, mult101.getTree());

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

        catch(RecognitionException e){
          throw e;
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "mathExpression"


    public static class mult_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mult"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:1: mult : term ( ( MULT ^| DIVISION ^) term )* ;
    public final SaseParser.mult_return mult() throws RecognitionException {
        SaseParser.mult_return retval = new SaseParser.mult_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token MULT103=null;
        Token DIVISION104=null;
        SaseParser.term_return term102 =null;

        SaseParser.term_return term105 =null;


        CommonTree MULT103_tree=null;
        CommonTree DIVISION104_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:7: ( term ( ( MULT ^| DIVISION ^) term )* )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:9: term ( ( MULT ^| DIVISION ^) term )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_term_in_mult1027);
            term102=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, term102.getTree());

            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:14: ( ( MULT ^| DIVISION ^) term )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==DIVISION||LA25_0==MULT) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:15: ( MULT ^| DIVISION ^) term
            	    {
            	    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:15: ( MULT ^| DIVISION ^)
            	    int alt24=2;
            	    int LA24_0 = input.LA(1);

            	    if ( (LA24_0==MULT) ) {
            	        alt24=1;
            	    }
            	    else if ( (LA24_0==DIVISION) ) {
            	        alt24=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 24, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt24) {
            	        case 1 :
            	            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:16: MULT ^
            	            {
            	            MULT103=(Token)match(input,MULT,FOLLOW_MULT_in_mult1031); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MULT103_tree = 
            	            (CommonTree)adaptor.create(MULT103)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(MULT103_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:22: DIVISION ^
            	            {
            	            DIVISION104=(Token)match(input,DIVISION,FOLLOW_DIVISION_in_mult1034); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            DIVISION104_tree = 
            	            (CommonTree)adaptor.create(DIVISION104)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(DIVISION104_tree, root_0);
            	            }

            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_term_in_mult1038);
            	    term105=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, term105.getTree());

            	    }
            	    break;

            	default :
            	    break loop25;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "mult"


    public static class term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "term"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:182:1: term : ( attributeTerm | value | LBRACKET ! mathExpression RBRACKET !);
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LBRACKET108=null;
        Token RBRACKET110=null;
        SaseParser.attributeTerm_return attributeTerm106 =null;

        SaseParser.value_return value107 =null;

        SaseParser.mathExpression_return mathExpression109 =null;


        CommonTree LBRACKET108_tree=null;
        CommonTree RBRACKET110_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:182:5: ( attributeTerm | value | LBRACKET ! mathExpression RBRACKET !)
            int alt26=3;
            switch ( input.LA(1) ) {
            case AVG:
            case COUNT:
            case MAX:
            case MIN:
            case NAME:
            case SUM:
                {
                alt26=1;
                }
                break;
            case NUMBER:
            case STRING_LITERAL:
                {
                alt26=2;
                }
                break;
            case LBRACKET:
                {
                alt26=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;

            }

            switch (alt26) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:182:7: attributeTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_attributeTerm_in_term1051);
                    attributeTerm106=attributeTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attributeTerm106.getTree());

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:183:3: value
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_value_in_term1057);
                    value107=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value107.getTree());

                    }
                    break;
                case 3 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:3: LBRACKET ! mathExpression RBRACKET !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    LBRACKET108=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_term1062); if (state.failed) return retval;

                    pushFollow(FOLLOW_mathExpression_in_term1065);
                    mathExpression109=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mathExpression109.getTree());

                    RBRACKET110=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_term1067); if (state.failed) return retval;

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "term"


    public static class attributeTerm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attributeTerm"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:187:1: attributeTerm : ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) |aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) );
    public final SaseParser.attributeTerm_return attributeTerm() throws RecognitionException {
        SaseParser.attributeTerm_return retval = new SaseParser.attributeTerm_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token aName=null;
        Token member=null;
        Token POINT113=null;
        Token NAME114=null;
        Token POINT115=null;
        SaseParser.aggregation_return aggregation111 =null;

        SaseParser.kAttributeUsage_return kAttributeUsage112 =null;


        CommonTree aName_tree=null;
        CommonTree member_tree=null;
        CommonTree POINT113_tree=null;
        CommonTree NAME114_tree=null;
        CommonTree POINT115_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_kAttributeUsage=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeUsage");
        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:187:14: ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) |aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) )
            int alt27=3;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==AVG||LA27_0==COUNT||LA27_0==MAX||LA27_0==MIN||LA27_0==SUM) ) {
                alt27=1;
            }
            else if ( (LA27_0==NAME) ) {
                int LA27_2 = input.LA(2);

                if ( (LA27_2==POINT) ) {
                    alt27=3;
                }
                else if ( (LA27_2==BBRACKETLEFT) ) {
                    alt27=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 27, 2, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;

            }
            switch (alt27) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:187:16: aggregation
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_aggregation_in_attributeTerm1079);
                    aggregation111=aggregation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregation111.getTree());

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:188:5: kAttributeUsage POINT NAME
                    {
                    pushFollow(FOLLOW_kAttributeUsage_in_attributeTerm1087);
                    kAttributeUsage112=kAttributeUsage();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeUsage.add(kAttributeUsage112.getTree());

                    POINT113=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1089); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT113);


                    NAME114=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1091); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME114);


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
                    // 188:32: -> ^( KMEMBER kAttributeUsage NAME )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:188:35: ^( KMEMBER kAttributeUsage NAME )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(KMEMBER, "KMEMBER")
                        , root_1);

                        adaptor.addChild(root_1, stream_kAttributeUsage.nextTree());

                        adaptor.addChild(root_1, 
                        stream_NAME.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:189:5: aName= NAME POINT member= NAME
                    {
                    aName=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1110); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(aName);


                    POINT115=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1112); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT115);


                    member=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1116); if (state.failed) return retval; 
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
                    // 189:34: -> ^( MEMBER $aName $member)
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:189:37: ^( MEMBER $aName $member)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(MEMBER, "MEMBER")
                        , root_1);

                        adaptor.addChild(root_1, stream_aName.nextNode());

                        adaptor.addChild(root_1, stream_member.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attributeTerm"


    public static class aggregation_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "aggregation"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:192:1: aggregation : ( aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION aggop $var $member) | aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION aggop $var ( $member)? ) );
    public final SaseParser.aggregation_return aggregation() throws RecognitionException {
        SaseParser.aggregation_return retval = new SaseParser.aggregation_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token var=null;
        Token name=null;
        Token number=null;
        Token member=null;
        Token LBRACKET117=null;
        Token BBRACKETLEFT118=null;
        Token POINT119=null;
        Token POINT120=null;
        Token MINUS121=null;
        Token BBRACKETRIGHT122=null;
        Token POINT123=null;
        Token RBRACKET124=null;
        Token LBRACKET126=null;
        Token BBRACKETLEFT127=null;
        Token BBRACKETRIGHT128=null;
        Token POINT129=null;
        Token RBRACKET130=null;
        SaseParser.aggop_return aggop116 =null;

        SaseParser.aggop_return aggop125 =null;


        CommonTree var_tree=null;
        CommonTree name_tree=null;
        CommonTree number_tree=null;
        CommonTree member_tree=null;
        CommonTree LBRACKET117_tree=null;
        CommonTree BBRACKETLEFT118_tree=null;
        CommonTree POINT119_tree=null;
        CommonTree POINT120_tree=null;
        CommonTree MINUS121_tree=null;
        CommonTree BBRACKETRIGHT122_tree=null;
        CommonTree POINT123_tree=null;
        CommonTree RBRACKET124_tree=null;
        CommonTree LBRACKET126_tree=null;
        CommonTree BBRACKETLEFT127_tree=null;
        CommonTree BBRACKETRIGHT128_tree=null;
        CommonTree POINT129_tree=null;
        CommonTree RBRACKET130_tree=null;
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
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:193:2: ( aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION aggop $var $member) | aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION aggop $var ( $member)? ) )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==AVG||LA29_0==COUNT||LA29_0==MAX||LA29_0==MIN||LA29_0==SUM) ) {
                int LA29_1 = input.LA(2);

                if ( (LA29_1==LBRACKET) ) {
                    int LA29_2 = input.LA(3);

                    if ( (LA29_2==NAME) ) {
                        int LA29_3 = input.LA(4);

                        if ( (LA29_3==BBRACKETLEFT) ) {
                            int LA29_4 = input.LA(5);

                            if ( (LA29_4==POINT) ) {
                                alt29=1;
                            }
                            else if ( (LA29_4==BBRACKETRIGHT) ) {
                                alt29=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 29, 4, input);

                                throw nvae;

                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 29, 3, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 29, 2, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 29, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;

            }
            switch (alt29) {
                case 1 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:193:5: aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}?
                    {
                    pushFollow(FOLLOW_aggop_in_aggregation1141);
                    aggop116=aggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggop.add(aggop116.getTree());

                    LBRACKET117=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1143); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET117);


                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1147); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);


                    BBRACKETLEFT118=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1149); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT118);


                    POINT119=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1151); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT119);


                    POINT120=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1153); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT120);


                    name=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1157); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(name);


                    MINUS121=(Token)match(input,MINUS,FOLLOW_MINUS_in_aggregation1159); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MINUS.add(MINUS121);


                    number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_aggregation1163); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(number);


                    BBRACKETRIGHT122=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1165); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT122);


                    POINT123=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1167); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT123);


                    member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1171); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(member);


                    RBRACKET124=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1173); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET124);


                    if ( !((name.getText().equalsIgnoreCase("I") && Integer.parseInt(number.getText()) == 1)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "aggregation", "$name.getText().equalsIgnoreCase(\"I\") && Integer.parseInt($number.getText()) == 1");
                    }

                    // AST REWRITE
                    // elements: member, aggop, var
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
                    // 194:4: -> ^( AGGREGATION aggop $var $member)
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:194:7: ^( AGGREGATION aggop $var $member)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(AGGREGATION, "AGGREGATION")
                        , root_1);

                        adaptor.addChild(root_1, stream_aggop.nextTree());

                        adaptor.addChild(root_1, stream_var.nextNode());

                        adaptor.addChild(root_1, stream_member.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:195:5: aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET
                    {
                    pushFollow(FOLLOW_aggop_in_aggregation1199);
                    aggop125=aggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggop.add(aggop125.getTree());

                    LBRACKET126=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1201); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET126);


                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1205); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);


                    BBRACKETLEFT127=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1207); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT127);


                    BBRACKETRIGHT128=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1209); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT128);


                    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:195:56: ( POINT member= NAME )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==POINT) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:195:57: POINT member= NAME
                            {
                            POINT129=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1212); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_POINT.add(POINT129);


                            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1216); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NAME.add(member);


                            }
                            break;

                    }


                    RBRACKET130=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1220); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET130);


                    // AST REWRITE
                    // elements: aggop, var, member
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
                    // 196:4: -> ^( AGGREGATION aggop $var ( $member)? )
                    {
                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:196:7: ^( AGGREGATION aggop $var ( $member)? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(AGGREGATION, "AGGREGATION")
                        , root_1);

                        adaptor.addChild(root_1, stream_aggop.nextTree());

                        adaptor.addChild(root_1, stream_var.nextNode());

                        // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:196:33: ( $member)?
                        if ( stream_member.hasNext() ) {
                            adaptor.addChild(root_1, stream_member.nextNode());

                        }
                        stream_member.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "aggregation"


    public static class aggop_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "aggop"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:199:1: aggop : ( MIN | MAX | SUM | COUNT | AVG );
    public final SaseParser.aggop_return aggop() throws RecognitionException {
        SaseParser.aggop_return retval = new SaseParser.aggop_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set131=null;

        CommonTree set131_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:200:2: ( MIN | MAX | SUM | COUNT | AVG )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set131=(Token)input.LT(1);

            if ( input.LA(1)==AVG||input.LA(1)==COUNT||input.LA(1)==MAX||input.LA(1)==MIN||input.LA(1)==SUM ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set131)
                );
                state.errorRecovery=false;
                state.failed=false;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "aggop"


    public static class value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "value"
    // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:203:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set132=null;

        CommonTree set132_tree=null;

        try {
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:203:8: ( NUMBER | STRING_LITERAL )
            // C:\\Development\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set132=(Token)input.LT(1);

            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set132)
                );
                state.errorRecovery=false;
                state.failed=false;
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
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "value"

    // Delegated rules


 

    public static final BitSet FOLLOW_createStmt_in_start154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_queryStmt_in_start160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_createStmt170 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_VIEW_in_createStmt172 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_createStmt174 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_queryStmt_in_createStmt176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_patternPart_in_queryStmt196 = new BitSet(new long[]{0x1400080000000000L});
    public static final BitSet FOLLOW_wherePart_in_queryStmt198 = new BitSet(new long[]{0x1000080000000000L});
    public static final BitSet FOLLOW_withinPart_in_queryStmt201 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_returnPart_in_queryStmt204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart235 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart237 = new BitSet(new long[]{0x0200200090108002L});
    public static final BitSet FOLLOW_timeunit_in_withinPart239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart281 = new BitSet(new long[]{0x0013004000000000L});
    public static final BitSet FOLLOW_skipPart_in_wherePart283 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart285 = new BitSet(new long[]{0x0060002228402300L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart287 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart306 = new BitSet(new long[]{0x0060002228402300L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_skipMethod_in_skipPart327 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LBRACKET_in_skipPart329 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_parameterList_in_skipPart331 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_skipPart333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart382 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart384 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnPart407 = new BitSet(new long[]{0x0040000228002100L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart409 = new BitSet(new long[]{0x0000000000000842L});
    public static final BitSet FOLLOW_COMMA_in_returnPart412 = new BitSet(new long[]{0x0040000228002100L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart414 = new BitSet(new long[]{0x0000000000000842L});
    public static final BitSet FOLLOW_AS_in_returnPart419 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_returnPart421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_seqPatternDecl_in_patternDecl447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEQ_in_seqPatternDecl459 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LBRACKET_in_seqPatternDecl461 = new BitSet(new long[]{0x0000001200400000L});
    public static final BitSet FOLLOW_stateDef_in_seqPatternDecl463 = new BitSet(new long[]{0x0000040000000800L});
    public static final BitSet FOLLOW_COMMA_in_seqPatternDecl466 = new BitSet(new long[]{0x0000001200400000L});
    public static final BitSet FOLLOW_stateDef_in_seqPatternDecl468 = new BitSet(new long[]{0x0000040000000800L});
    public static final BitSet FOLLOW_RBRACKET_in_seqPatternDecl472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef496 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef519 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef544 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef548 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef550 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef570 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef574 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef576 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeDefinition603 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_sAttributeName_in_typeDefinition605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_ktypeDefinition616 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_PLUS_in_ktypeDefinition618 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_kAttributeName_in_ktypeDefinition620 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList634 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_COMMA_in_parameterList637 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList639 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName699 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName702 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage731 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_current_in_kAttributeUsage734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage748 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_first_in_kAttributeUsage750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage763 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_last_in_kAttributeUsage765 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage778 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_len_in_kAttributeUsage780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_current801 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_current805 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_current807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_first822 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NUMBER_in_first826 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_first828 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_last841 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_last845 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_MINUS_in_last847 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NUMBER_in_last851 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_last853 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_len869 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_len871 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_POINT_in_len873 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_LEN_in_len875 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_len877 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions890 = new BitSet(new long[]{0x0000000000000822L});
    public static final BitSet FOLLOW_AND_in_whereExpressions894 = new BitSet(new long[]{0x0060002228402300L});
    public static final BitSet FOLLOW_COMMA_in_whereExpressions896 = new BitSet(new long[]{0x0060002228402300L});
    public static final BitSet FOLLOW_expression_in_whereExpressions899 = new BitSet(new long[]{0x0000000000000822L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_expression922 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_expression924 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_expression926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression942 = new BitSet(new long[]{0x0000800000001000L});
    public static final BitSet FOLLOW_SINGLEEQUALS_in_expression947 = new BitSet(new long[]{0x0060002228402100L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression951 = new BitSet(new long[]{0x0060002228402100L});
    public static final BitSet FOLLOW_mathExpression_in_expression956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_expression978 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ASSIGN_in_expression980 = new BitSet(new long[]{0x0060002228402100L});
    public static final BitSet FOLLOW_mathExpression_in_expression982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mult_in_mathExpression1002 = new BitSet(new long[]{0x0000010040000002L});
    public static final BitSet FOLLOW_PLUS_in_mathExpression1006 = new BitSet(new long[]{0x0060002228402100L});
    public static final BitSet FOLLOW_MINUS_in_mathExpression1009 = new BitSet(new long[]{0x0060002228402100L});
    public static final BitSet FOLLOW_mult_in_mathExpression1013 = new BitSet(new long[]{0x0000010040000002L});
    public static final BitSet FOLLOW_term_in_mult1027 = new BitSet(new long[]{0x0000000100020002L});
    public static final BitSet FOLLOW_MULT_in_mult1031 = new BitSet(new long[]{0x0060002228402100L});
    public static final BitSet FOLLOW_DIVISION_in_mult1034 = new BitSet(new long[]{0x0060002228402100L});
    public static final BitSet FOLLOW_term_in_mult1038 = new BitSet(new long[]{0x0000000100020002L});
    public static final BitSet FOLLOW_attributeTerm_in_term1051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term1057 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_term1062 = new BitSet(new long[]{0x0060002228402100L});
    public static final BitSet FOLLOW_mathExpression_in_term1065 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_term1067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregation_in_attributeTerm1079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_attributeTerm1087 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1089 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1110 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1112 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggop_in_aggregation1141 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1143 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1147 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1149 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1151 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1153 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1157 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_MINUS_in_aggregation1159 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NUMBER_in_aggregation1163 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1165 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1167 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1171 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggop_in_aggregation1199 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1201 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1205 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1207 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1209 = new BitSet(new long[]{0x0000060000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1212 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1216 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1220 = new BitSet(new long[]{0x0000000000000002L});

}