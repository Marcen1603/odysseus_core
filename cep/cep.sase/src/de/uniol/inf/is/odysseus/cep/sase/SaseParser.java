// $ANTLR 3.4 E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2012-01-27 10:01:57
 
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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALT", "AND", "ASSIGN", "AVG", "BBRACKETLEFT", "BBRACKETRIGHT", "COMMA", "COMPAREOP", "COUNT", "CREATE", "DAY", "DIGIT", "DIVISION", "EQUALS", "FLOAT", "HOUR", "INTEGER", "LBRACKET", "LEFTCURLY", "LEN", "LETTER", "LOWER", "MAX", "MILLISECOND", "MIN", "MINUS", "MINUTE", "MULT", "NAME", "NEWLINE", "NONCONTROL_CHAR", "NOTSIGN", "NUMBER", "PARTITION_CONTIGUITY", "PATTERN", "PLUS", "POINT", "RBRACKET", "RETURN", "RIGHTCURLY", "SECOND", "SEQ", "SINGLEEQUALS", "SKIP_TILL_ANY_MATCH", "SKIP_TILL_NEXT_MATCH", "SPACE", "STREAM", "STRICT_CONTIGUITY", "STRING_LITERAL", "SUM", "UPPER", "VIEW", "WEEK", "WHERE", "WHITESPACE", "WITHIN", "AGGREGATION", "ASSIGNMENT", "ATTRIBUTE", "COMPAREEXPRESSION", "CREATEVIEW", "CURRENT", "FIRST", "IDEXPRESSION", "KATTRIBUTE", "KMEMBER", "KSTATE", "KTYPE", "MATHEXPRESSION", "MEMBER", "NOT", "PARAMLIST", "PREV", "QUERY", "STATE", "TERM", "TYPE", "WHEREEXPRESSION"
    };

    public static final int EOF=-1;
    public static final int ALT=4;
    public static final int AND=5;
    public static final int ASSIGN=6;
    public static final int AVG=7;
    public static final int BBRACKETLEFT=8;
    public static final int BBRACKETRIGHT=9;
    public static final int COMMA=10;
    public static final int COMPAREOP=11;
    public static final int COUNT=12;
    public static final int CREATE=13;
    public static final int DAY=14;
    public static final int DIGIT=15;
    public static final int DIVISION=16;
    public static final int EQUALS=17;
    public static final int FLOAT=18;
    public static final int HOUR=19;
    public static final int INTEGER=20;
    public static final int LBRACKET=21;
    public static final int LEFTCURLY=22;
    public static final int LEN=23;
    public static final int LETTER=24;
    public static final int LOWER=25;
    public static final int MAX=26;
    public static final int MILLISECOND=27;
    public static final int MIN=28;
    public static final int MINUS=29;
    public static final int MINUTE=30;
    public static final int MULT=31;
    public static final int NAME=32;
    public static final int NEWLINE=33;
    public static final int NONCONTROL_CHAR=34;
    public static final int NOTSIGN=35;
    public static final int NUMBER=36;
    public static final int PARTITION_CONTIGUITY=37;
    public static final int PATTERN=38;
    public static final int PLUS=39;
    public static final int POINT=40;
    public static final int RBRACKET=41;
    public static final int RETURN=42;
    public static final int RIGHTCURLY=43;
    public static final int SECOND=44;
    public static final int SEQ=45;
    public static final int SINGLEEQUALS=46;
    public static final int SKIP_TILL_ANY_MATCH=47;
    public static final int SKIP_TILL_NEXT_MATCH=48;
    public static final int SPACE=49;
    public static final int STREAM=50;
    public static final int STRICT_CONTIGUITY=51;
    public static final int STRING_LITERAL=52;
    public static final int SUM=53;
    public static final int UPPER=54;
    public static final int VIEW=55;
    public static final int WEEK=56;
    public static final int WHERE=57;
    public static final int WHITESPACE=58;
    public static final int WITHIN=59;
    public static final int AGGREGATION=60;
    public static final int ASSIGNMENT=61;
    public static final int ATTRIBUTE=62;
    public static final int COMPAREEXPRESSION=63;
    public static final int CREATEVIEW=64;
    public static final int CURRENT=65;
    public static final int FIRST=66;
    public static final int IDEXPRESSION=67;
    public static final int KATTRIBUTE=68;
    public static final int KMEMBER=69;
    public static final int KSTATE=70;
    public static final int KTYPE=71;
    public static final int MATHEXPRESSION=72;
    public static final int MEMBER=73;
    public static final int NOT=74;
    public static final int PARAMLIST=75;
    public static final int PREV=76;
    public static final int QUERY=77;
    public static final int STATE=78;
    public static final int TERM=79;
    public static final int TYPE=80;
    public static final int WHEREEXPRESSION=81;

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
    public String getGrammarFileName() { return "E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g"; }


    public static class start_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "start"
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:59:1: start : ( createStmt | queryStmt );
    public final SaseParser.start_return start() throws RecognitionException {
        SaseParser.start_return retval = new SaseParser.start_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.createStmt_return createStmt1 =null;

        SaseParser.queryStmt_return queryStmt2 =null;



        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:59:8: ( createStmt | queryStmt )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:59:10: createStmt
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:4: queryStmt
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:63:1: createStmt : CREATE VIEW NAME queryStmt -> ^( CREATEVIEW NAME queryStmt ) ;
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
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:2: ( CREATE VIEW NAME queryStmt -> ^( CREATEVIEW NAME queryStmt ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:4: CREATE VIEW NAME queryStmt
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
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:34: ^( CREATEVIEW NAME queryStmt )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:1: queryStmt : patternPart ( wherePart )? ( withinPart )? ( returnPart )? -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? ) ;
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
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:10: ( patternPart ( wherePart )? ( withinPart )? ( returnPart )? -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:13: patternPart ( wherePart )? ( withinPart )? ( returnPart )?
            {
            pushFollow(FOLLOW_patternPart_in_queryStmt196);
            patternPart7=patternPart();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternPart.add(patternPart7.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:25: ( wherePart )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==WHERE) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:25: wherePart
                    {
                    pushFollow(FOLLOW_wherePart_in_queryStmt198);
                    wherePart8=wherePart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_wherePart.add(wherePart8.getTree());

                    }
                    break;

            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:36: ( withinPart )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==WITHIN) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:36: withinPart
                    {
                    pushFollow(FOLLOW_withinPart_in_queryStmt201);
                    withinPart9=withinPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_withinPart.add(withinPart9.getTree());

                    }
                    break;

            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:48: ( returnPart )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==RETURN) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:48: returnPart
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
            // elements: patternPart, returnPart, wherePart, withinPart
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 67:60: -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:63: ^( QUERY patternPart ( wherePart )? ( withinPart )? ( returnPart )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(QUERY, "QUERY")
                , root_1);

                adaptor.addChild(root_1, stream_patternPart.nextTree());

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:83: ( wherePart )?
                if ( stream_wherePart.hasNext() ) {
                    adaptor.addChild(root_1, stream_wherePart.nextTree());

                }
                stream_wherePart.reset();

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:94: ( withinPart )?
                if ( stream_withinPart.hasNext() ) {
                    adaptor.addChild(root_1, stream_withinPart.nextTree());

                }
                stream_withinPart.reset();

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:106: ( returnPart )?
                if ( stream_returnPart.hasNext() ) {
                    adaptor.addChild(root_1, stream_returnPart.nextTree());

                }
                stream_returnPart.reset();

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:71:1: withinPart : WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) ;
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
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:2: ( WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:4: WITHIN NUMBER ( timeunit )?
            {
            WITHIN11=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart237); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_WITHIN.add(WITHIN11);


            NUMBER12=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart239); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER12);


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:18: ( timeunit )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DAY||LA5_0==HOUR||LA5_0==MILLISECOND||LA5_0==MINUTE||LA5_0==SECOND||LA5_0==WEEK) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:18: timeunit
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
            // 72:28: -> ^( WITHIN NUMBER ( timeunit )? )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:31: ^( WITHIN NUMBER ( timeunit )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_WITHIN.nextNode()
                , root_1);

                adaptor.addChild(root_1, 
                stream_NUMBER.nextNode()
                );

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:47: ( timeunit )?
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:75:1: timeunit : ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND );
    public final SaseParser.timeunit_return timeunit() throws RecognitionException {
        SaseParser.timeunit_return retval = new SaseParser.timeunit_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set14=null;

        CommonTree set14_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:75:9: ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:1: wherePart : ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
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
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:2: ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WHERE) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==PARTITION_CONTIGUITY||(LA6_1 >= SKIP_TILL_ANY_MATCH && LA6_1 <= SKIP_TILL_NEXT_MATCH)||LA6_1==STRICT_CONTIGUITY) ) {
                    alt6=1;
                }
                else if ( ((LA6_1 >= AVG && LA6_1 <= BBRACKETLEFT)||LA6_1==COUNT||LA6_1==LBRACKET||LA6_1==MAX||LA6_1==MIN||LA6_1==NAME||LA6_1==NUMBER||(LA6_1 >= STRING_LITERAL && LA6_1 <= SUM)) ) {
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:4: WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY
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
                    // elements: WHERE, whereExpressions, skipPart
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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:60: ^( WHERE skipPart whereExpressions )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:4: WHERE whereExpressions
                    {
                    WHERE20=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart308); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE20);


                    pushFollow(FOLLOW_whereExpressions_in_wherePart310);
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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:30: ^( WHERE whereExpressions )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:1: skipPart : skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) ;
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
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:2: ( skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:4: skipMethod LBRACKET parameterList RBRACKET
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
            // 84:2: -> ^( skipMethod parameterList )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:5: ^( skipMethod parameterList )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:1: skipMethod : ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY );
    public final SaseParser.skipMethod_return skipMethod() throws RecognitionException {
        SaseParser.skipMethod_return retval = new SaseParser.skipMethod_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set26=null;

        CommonTree set26_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:11: ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:1: patternPart : PATTERN ( patternDecl )* -> ^( PATTERN ( patternDecl )* ) ;
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
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:2: ( PATTERN ( patternDecl )* -> ^( PATTERN ( patternDecl )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:4: PATTERN ( patternDecl )*
            {
            PATTERN27=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart384); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN27);


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:12: ( patternDecl )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==SEQ) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:12: patternDecl
            	    {
            	    pushFollow(FOLLOW_patternDecl_in_patternPart386);
            	    patternDecl28=patternDecl();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_patternDecl.add(patternDecl28.getTree());

            	    }
            	    break;

            	default :
            	    break loop7;
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
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:28: ^( PATTERN ( patternDecl )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_PATTERN.nextNode()
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:38: ( patternDecl )*
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:97:1: returnPart : RETURN attributeTerm ( COMMA attributeTerm )* -> ^( RETURN ( attributeTerm )* ) ;
    public final SaseParser.returnPart_return returnPart() throws RecognitionException {
        SaseParser.returnPart_return retval = new SaseParser.returnPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token RETURN29=null;
        Token COMMA31=null;
        SaseParser.attributeTerm_return attributeTerm30 =null;

        SaseParser.attributeTerm_return attributeTerm32 =null;


        CommonTree RETURN29_tree=null;
        CommonTree COMMA31_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RETURN=new RewriteRuleTokenStream(adaptor,"token RETURN");
        RewriteRuleSubtreeStream stream_attributeTerm=new RewriteRuleSubtreeStream(adaptor,"rule attributeTerm");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:2: ( RETURN attributeTerm ( COMMA attributeTerm )* -> ^( RETURN ( attributeTerm )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:5: RETURN attributeTerm ( COMMA attributeTerm )*
            {
            RETURN29=(Token)match(input,RETURN,FOLLOW_RETURN_in_returnPart409); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RETURN.add(RETURN29);


            pushFollow(FOLLOW_attributeTerm_in_returnPart411);
            attributeTerm30=attributeTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeTerm.add(attributeTerm30.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:26: ( COMMA attributeTerm )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==COMMA) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:27: COMMA attributeTerm
            	    {
            	    COMMA31=(Token)match(input,COMMA,FOLLOW_COMMA_in_returnPart414); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA31);


            	    pushFollow(FOLLOW_attributeTerm_in_returnPart416);
            	    attributeTerm32=attributeTerm();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeTerm.add(attributeTerm32.getTree());

            	    }
            	    break;

            	default :
            	    break loop8;
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
            // 98:49: -> ^( RETURN ( attributeTerm )* )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:52: ^( RETURN ( attributeTerm )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_RETURN.nextNode()
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:61: ( attributeTerm )*
                while ( stream_attributeTerm.hasNext() ) {
                    adaptor.addChild(root_1, stream_attributeTerm.nextTree());

                }
                stream_attributeTerm.reset();

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:1: patternDecl : seqPatternDecl ;
    public final SaseParser.patternDecl_return patternDecl() throws RecognitionException {
        SaseParser.patternDecl_return retval = new SaseParser.patternDecl_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.seqPatternDecl_return seqPatternDecl33 =null;



        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:2: ( seqPatternDecl )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:102:5: seqPatternDecl
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_seqPatternDecl_in_patternDecl440);
            seqPatternDecl33=seqPatternDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, seqPatternDecl33.getTree());

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:105:1: seqPatternDecl : SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) ;
    public final SaseParser.seqPatternDecl_return seqPatternDecl() throws RecognitionException {
        SaseParser.seqPatternDecl_return retval = new SaseParser.seqPatternDecl_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token SEQ34=null;
        Token LBRACKET35=null;
        Token COMMA37=null;
        Token RBRACKET39=null;
        SaseParser.stateDef_return stateDef36 =null;

        SaseParser.stateDef_return stateDef38 =null;


        CommonTree SEQ34_tree=null;
        CommonTree LBRACKET35_tree=null;
        CommonTree COMMA37_tree=null;
        CommonTree RBRACKET39_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SEQ=new RewriteRuleTokenStream(adaptor,"token SEQ");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_stateDef=new RewriteRuleSubtreeStream(adaptor,"rule stateDef");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:2: ( SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:4: SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET
            {
            SEQ34=(Token)match(input,SEQ,FOLLOW_SEQ_in_seqPatternDecl452); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEQ.add(SEQ34);


            LBRACKET35=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_seqPatternDecl454); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET35);


            pushFollow(FOLLOW_stateDef_in_seqPatternDecl456);
            stateDef36=stateDef();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_stateDef.add(stateDef36.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:26: ( COMMA stateDef )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==COMMA) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:27: COMMA stateDef
            	    {
            	    COMMA37=(Token)match(input,COMMA,FOLLOW_COMMA_in_seqPatternDecl459); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA37);


            	    pushFollow(FOLLOW_stateDef_in_seqPatternDecl461);
            	    stateDef38=stateDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_stateDef.add(stateDef38.getTree());

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            RBRACKET39=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_seqPatternDecl465); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET39);


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
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:56: ^( SEQ ( stateDef )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_SEQ.nextNode()
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:106:62: ( stateDef )*
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:1: stateDef : ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) );
    public final SaseParser.stateDef_return stateDef() throws RecognitionException {
        SaseParser.stateDef_return retval = new SaseParser.stateDef_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NOTSIGN40=null;
        Token NOTSIGN42=null;
        Token NOTSIGN44=null;
        Token LBRACKET45=null;
        Token RBRACKET47=null;
        Token NOTSIGN48=null;
        Token LBRACKET49=null;
        Token RBRACKET51=null;
        SaseParser.ktypeDefinition_return ktypeDefinition41 =null;

        SaseParser.typeDefinition_return typeDefinition43 =null;

        SaseParser.ktypeDefinition_return ktypeDefinition46 =null;

        SaseParser.typeDefinition_return typeDefinition50 =null;


        CommonTree NOTSIGN40_tree=null;
        CommonTree NOTSIGN42_tree=null;
        CommonTree NOTSIGN44_tree=null;
        CommonTree LBRACKET45_tree=null;
        CommonTree RBRACKET47_tree=null;
        CommonTree NOTSIGN48_tree=null;
        CommonTree LBRACKET49_tree=null;
        CommonTree RBRACKET51_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NOTSIGN=new RewriteRuleTokenStream(adaptor,"token NOTSIGN");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_typeDefinition=new RewriteRuleSubtreeStream(adaptor,"rule typeDefinition");
        RewriteRuleSubtreeStream stream_ktypeDefinition=new RewriteRuleSubtreeStream(adaptor,"rule ktypeDefinition");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:10: ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:12: ( NOTSIGN )? ktypeDefinition
                    {
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:12: ( NOTSIGN )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==NOTSIGN) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:13: NOTSIGN
                            {
                            NOTSIGN40=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef489); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN40);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef493);
                    ktypeDefinition41=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition41.getTree());

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
                    // 110:40: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:43: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(KSTATE, "KSTATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:110:68: ( NOTSIGN )?
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:5: ( NOTSIGN )? typeDefinition
                    {
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:5: ( NOTSIGN )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==NOTSIGN) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:6: NOTSIGN
                            {
                            NOTSIGN42=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef512); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN42);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_typeDefinition_in_stateDef516);
                    typeDefinition43=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition43.getTree());

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
                    // 111:32: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:35: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(STATE, "STATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:58: ( NOTSIGN )?
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:5: ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET
                    {
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:5: ( NOTSIGN )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==NOTSIGN) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:6: NOTSIGN
                            {
                            NOTSIGN44=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef537); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN44);


                            }
                            break;

                    }


                    LBRACKET45=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef541); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET45);


                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef543);
                    ktypeDefinition46=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition46.getTree());

                    RBRACKET47=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef545); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET47);


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
                    // 112:50: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:53: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(KSTATE, "KSTATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:78: ( NOTSIGN )?
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:5: ( NOTSIGN )? LBRACKET typeDefinition RBRACKET
                    {
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:5: ( NOTSIGN )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==NOTSIGN) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:6: NOTSIGN
                            {
                            NOTSIGN48=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef563); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN48);


                            }
                            break;

                    }


                    LBRACKET49=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef567); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET49);


                    pushFollow(FOLLOW_typeDefinition_in_stateDef569);
                    typeDefinition50=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition50.getTree());

                    RBRACKET51=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef571); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET51);


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
                    // 113:49: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:52: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(STATE, "STATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:113:75: ( NOTSIGN )?
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:1: typeDefinition : NAME sAttributeName ;
    public final SaseParser.typeDefinition_return typeDefinition() throws RecognitionException {
        SaseParser.typeDefinition_return retval = new SaseParser.typeDefinition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME52=null;
        SaseParser.sAttributeName_return sAttributeName53 =null;


        CommonTree NAME52_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:2: ( NAME sAttributeName )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:4: NAME sAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME52=(Token)match(input,NAME,FOLLOW_NAME_in_typeDefinition596); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME52_tree = 
            (CommonTree)adaptor.create(NAME52)
            ;
            adaptor.addChild(root_0, NAME52_tree);
            }

            pushFollow(FOLLOW_sAttributeName_in_typeDefinition598);
            sAttributeName53=sAttributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, sAttributeName53.getTree());

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:120:1: ktypeDefinition : NAME PLUS kAttributeName ;
    public final SaseParser.ktypeDefinition_return ktypeDefinition() throws RecognitionException {
        SaseParser.ktypeDefinition_return retval = new SaseParser.ktypeDefinition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME54=null;
        Token PLUS55=null;
        SaseParser.kAttributeName_return kAttributeName56 =null;


        CommonTree NAME54_tree=null;
        CommonTree PLUS55_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:121:2: ( NAME PLUS kAttributeName )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:121:4: NAME PLUS kAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME54=(Token)match(input,NAME,FOLLOW_NAME_in_ktypeDefinition609); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME54_tree = 
            (CommonTree)adaptor.create(NAME54)
            ;
            adaptor.addChild(root_0, NAME54_tree);
            }

            PLUS55=(Token)match(input,PLUS,FOLLOW_PLUS_in_ktypeDefinition611); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PLUS55_tree = 
            (CommonTree)adaptor.create(PLUS55)
            ;
            adaptor.addChild(root_0, PLUS55_tree);
            }

            pushFollow(FOLLOW_kAttributeName_in_ktypeDefinition613);
            kAttributeName56=kAttributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, kAttributeName56.getTree());

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:125:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
    public final SaseParser.parameterList_return parameterList() throws RecognitionException {
        SaseParser.parameterList_return retval = new SaseParser.parameterList_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token COMMA58=null;
        SaseParser.attributeName_return attributeName57 =null;

        SaseParser.attributeName_return attributeName59 =null;


        CommonTree COMMA58_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:4: attributeName ( COMMA attributeName )*
            {
            pushFollow(FOLLOW_attributeName_in_parameterList627);
            attributeName57=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(attributeName57.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:18: ( COMMA attributeName )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==COMMA) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:19: COMMA attributeName
            	    {
            	    COMMA58=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList630); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA58);


            	    pushFollow(FOLLOW_attributeName_in_parameterList632);
            	    attributeName59=attributeName();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeName.add(attributeName59.getTree());

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
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:44: ^( PARAMLIST ( attributeName )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(PARAMLIST, "PARAMLIST")
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:56: ( attributeName )*
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:129:1: attributeName : ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.kAttributeName_return kAttributeName60 =null;

        SaseParser.sAttributeName_return sAttributeName61 =null;


        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_kAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeName");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:130:2: ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:130:5: kAttributeName
                    {
                    pushFollow(FOLLOW_kAttributeName_in_attributeName656);
                    kAttributeName60=kAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeName.add(kAttributeName60.getTree());

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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:130:23: ^( KATTRIBUTE NAME )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:131:4: sAttributeName
                    {
                    pushFollow(FOLLOW_sAttributeName_in_attributeName670);
                    sAttributeName61=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sAttributeName.add(sAttributeName61.getTree());

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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:131:22: ^( ATTRIBUTE NAME )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:134:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT ;
    public final SaseParser.kAttributeName_return kAttributeName() throws RecognitionException {
        SaseParser.kAttributeName_return retval = new SaseParser.kAttributeName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME62=null;
        Token BBRACKETLEFT63=null;
        Token BBRACKETRIGHT64=null;

        CommonTree NAME62_tree=null;
        CommonTree BBRACKETLEFT63_tree=null;
        CommonTree BBRACKETRIGHT64_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:135:2: ( NAME BBRACKETLEFT BBRACKETRIGHT )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:135:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME62=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName692); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME62_tree = 
            (CommonTree)adaptor.create(NAME62)
            ;
            adaptor.addChild(root_0, NAME62_tree);
            }

            BBRACKETLEFT63=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName695); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT63_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT63)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT63_tree);
            }

            BBRACKETRIGHT64=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName697); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT64_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT64)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT64_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:138:1: sAttributeName : NAME ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME65=null;

        CommonTree NAME65_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:139:2: ( NAME )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:139:4: NAME
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME65=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName711); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME65_tree = 
            (CommonTree)adaptor.create(NAME65)
            ;
            adaptor.addChild(root_0, NAME65_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:142:1: kAttributeUsage : ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) );
    public final SaseParser.kAttributeUsage_return kAttributeUsage() throws RecognitionException {
        SaseParser.kAttributeUsage_return retval = new SaseParser.kAttributeUsage_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME66=null;
        Token NAME68=null;
        Token NAME70=null;
        Token NAME72=null;
        SaseParser.current_return current67 =null;

        SaseParser.first_return first69 =null;

        SaseParser.last_return last71 =null;

        SaseParser.len_return len73 =null;


        CommonTree NAME66_tree=null;
        CommonTree NAME68_tree=null;
        CommonTree NAME70_tree=null;
        CommonTree NAME72_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleSubtreeStream stream_last=new RewriteRuleSubtreeStream(adaptor,"rule last");
        RewriteRuleSubtreeStream stream_current=new RewriteRuleSubtreeStream(adaptor,"rule current");
        RewriteRuleSubtreeStream stream_len=new RewriteRuleSubtreeStream(adaptor,"rule len");
        RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:143:2: ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:143:4: NAME current
                    {
                    NAME66=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage724); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME66);


                    pushFollow(FOLLOW_current_in_kAttributeUsage727);
                    current67=current();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_current.add(current67.getTree());

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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:143:22: ^( NAME CURRENT )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:144:4: NAME first
                    {
                    NAME68=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage741); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME68);


                    pushFollow(FOLLOW_first_in_kAttributeUsage743);
                    first69=first();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_first.add(first69.getTree());

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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:144:18: ^( NAME FIRST )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:145:4: NAME last
                    {
                    NAME70=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage756); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME70);


                    pushFollow(FOLLOW_last_in_kAttributeUsage758);
                    last71=last();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_last.add(last71.getTree());

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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:145:17: ^( NAME PREV )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:146:4: NAME len
                    {
                    NAME72=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage771); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME72);


                    pushFollow(FOLLOW_len_in_kAttributeUsage773);
                    len73=len();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_len.add(len73.getTree());

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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:146:16: ^( NAME LEN )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:149:1: current : BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?;
    public final SaseParser.current_return current() throws RecognitionException {
        SaseParser.current_return retval = new SaseParser.current_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token name=null;
        Token BBRACKETLEFT74=null;
        Token BBRACKETRIGHT75=null;

        CommonTree name_tree=null;
        CommonTree BBRACKETLEFT74_tree=null;
        CommonTree BBRACKETRIGHT75_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:149:9: ( BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?)
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:2: BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT74=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_current794); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT74_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT74)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT74_tree);
            }

            name=(Token)match(input,NAME,FOLLOW_NAME_in_current798); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = 
            (CommonTree)adaptor.create(name)
            ;
            adaptor.addChild(root_0, name_tree);
            }

            BBRACKETRIGHT75=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_current800); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT75_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT75)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT75_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:1: first : BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?;
    public final SaseParser.first_return first() throws RecognitionException {
        SaseParser.first_return retval = new SaseParser.first_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token number=null;
        Token BBRACKETLEFT76=null;
        Token BBRACKETRIGHT77=null;

        CommonTree number_tree=null;
        CommonTree BBRACKETLEFT76_tree=null;
        CommonTree BBRACKETRIGHT77_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:153:7: ( BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?)
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:154:2: BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT76=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_first815); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT76_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT76)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT76_tree);
            }

            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_first819); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = 
            (CommonTree)adaptor.create(number)
            ;
            adaptor.addChild(root_0, number_tree);
            }

            BBRACKETRIGHT77=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_first821); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT77_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT77)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT77_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:1: last : BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?;
    public final SaseParser.last_return last() throws RecognitionException {
        SaseParser.last_return retval = new SaseParser.last_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token name=null;
        Token number=null;
        Token BBRACKETLEFT78=null;
        Token MINUS79=null;
        Token BBRACKETRIGHT80=null;

        CommonTree name_tree=null;
        CommonTree number_tree=null;
        CommonTree BBRACKETLEFT78_tree=null;
        CommonTree MINUS79_tree=null;
        CommonTree BBRACKETRIGHT80_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:157:6: ( BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?)
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:158:2: BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT78=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_last834); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT78_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT78)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT78_tree);
            }

            name=(Token)match(input,NAME,FOLLOW_NAME_in_last838); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = 
            (CommonTree)adaptor.create(name)
            ;
            adaptor.addChild(root_0, name_tree);
            }

            MINUS79=(Token)match(input,MINUS,FOLLOW_MINUS_in_last840); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            MINUS79_tree = 
            (CommonTree)adaptor.create(MINUS79)
            ;
            adaptor.addChild(root_0, MINUS79_tree);
            }

            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_last844); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = 
            (CommonTree)adaptor.create(number)
            ;
            adaptor.addChild(root_0, number_tree);
            }

            BBRACKETRIGHT80=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_last846); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT80_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT80)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT80_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:161:1: len : BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT ;
    public final SaseParser.len_return len() throws RecognitionException {
        SaseParser.len_return retval = new SaseParser.len_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token BBRACKETLEFT81=null;
        Token NAME82=null;
        Token POINT83=null;
        Token LEN84=null;
        Token BBRACKETRIGHT85=null;

        CommonTree BBRACKETLEFT81_tree=null;
        CommonTree NAME82_tree=null;
        CommonTree POINT83_tree=null;
        CommonTree LEN84_tree=null;
        CommonTree BBRACKETRIGHT85_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:161:5: ( BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:162:2: BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT81=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_len862); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT81_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT81)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT81_tree);
            }

            NAME82=(Token)match(input,NAME,FOLLOW_NAME_in_len864); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME82_tree = 
            (CommonTree)adaptor.create(NAME82)
            ;
            adaptor.addChild(root_0, NAME82_tree);
            }

            POINT83=(Token)match(input,POINT,FOLLOW_POINT_in_len866); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            POINT83_tree = 
            (CommonTree)adaptor.create(POINT83)
            ;
            adaptor.addChild(root_0, POINT83_tree);
            }

            LEN84=(Token)match(input,LEN,FOLLOW_LEN_in_len868); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEN84_tree = 
            (CommonTree)adaptor.create(LEN84)
            ;
            adaptor.addChild(root_0, LEN84_tree);
            }

            BBRACKETRIGHT85=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_len870); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT85_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT85)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT85_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:165:1: whereExpressions : expression ( ( AND | COMMA ) expression )* -> ^( WHEREEXPRESSION ( expression )* ) ;
    public final SaseParser.whereExpressions_return whereExpressions() throws RecognitionException {
        SaseParser.whereExpressions_return retval = new SaseParser.whereExpressions_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token AND87=null;
        Token COMMA88=null;
        SaseParser.expression_return expression86 =null;

        SaseParser.expression_return expression89 =null;


        CommonTree AND87_tree=null;
        CommonTree COMMA88_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:2: ( expression ( ( AND | COMMA ) expression )* -> ^( WHEREEXPRESSION ( expression )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:4: expression ( ( AND | COMMA ) expression )*
            {
            pushFollow(FOLLOW_expression_in_whereExpressions883);
            expression86=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression86.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:15: ( ( AND | COMMA ) expression )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==AND||LA19_0==COMMA) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:16: ( AND | COMMA ) expression
            	    {
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:16: ( AND | COMMA )
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
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:17: AND
            	            {
            	            AND87=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions887); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_AND.add(AND87);


            	            }
            	            break;
            	        case 2 :
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:21: COMMA
            	            {
            	            COMMA88=(Token)match(input,COMMA,FOLLOW_COMMA_in_whereExpressions889); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_COMMA.add(COMMA88);


            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_expression_in_whereExpressions892);
            	    expression89=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression89.getTree());

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
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:44: ^( WHEREEXPRESSION ( expression )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION")
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:166:62: ( expression )*
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:169:1: expression : ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) |f1= mathExpression (op= SINGLEEQUALS |op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) | sAttributeName ASSIGN mathExpression -> ^( ASSIGNMENT sAttributeName mathExpression ) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token op=null;
        Token BBRACKETLEFT90=null;
        Token NAME91=null;
        Token BBRACKETRIGHT92=null;
        Token ASSIGN94=null;
        SaseParser.mathExpression_return f1 =null;

        SaseParser.mathExpression_return f2 =null;

        SaseParser.sAttributeName_return sAttributeName93 =null;

        SaseParser.mathExpression_return mathExpression95 =null;


        CommonTree op_tree=null;
        CommonTree BBRACKETLEFT90_tree=null;
        CommonTree NAME91_tree=null;
        CommonTree BBRACKETRIGHT92_tree=null;
        CommonTree ASSIGN94_tree=null;
        RewriteRuleTokenStream stream_SINGLEEQUALS=new RewriteRuleTokenStream(adaptor,"token SINGLEEQUALS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");
        RewriteRuleTokenStream stream_ASSIGN=new RewriteRuleTokenStream(adaptor,"token ASSIGN");
        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_mathExpression=new RewriteRuleSubtreeStream(adaptor,"rule mathExpression");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:170:2: ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) |f1= mathExpression (op= SINGLEEQUALS |op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) | sAttributeName ASSIGN mathExpression -> ^( ASSIGNMENT sAttributeName mathExpression ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:170:4: BBRACKETLEFT NAME BBRACKETRIGHT
                    {
                    BBRACKETLEFT90=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_expression915); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT90);


                    NAME91=(Token)match(input,NAME,FOLLOW_NAME_in_expression917); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME91);


                    BBRACKETRIGHT92=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_expression919); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT92);


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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:170:39: ^( IDEXPRESSION NAME )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:3: f1= mathExpression (op= SINGLEEQUALS |op= COMPAREOP ) f2= mathExpression
                    {
                    pushFollow(FOLLOW_mathExpression_in_expression935);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());

                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:21: (op= SINGLEEQUALS |op= COMPAREOP )
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
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:22: op= SINGLEEQUALS
                            {
                            op=(Token)match(input,SINGLEEQUALS,FOLLOW_SINGLEEQUALS_in_expression940); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_SINGLEEQUALS.add(op);


                            }
                            break;
                        case 2 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:38: op= COMPAREOP
                            {
                            op=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression944); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COMPAREOP.add(op);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_mathExpression_in_expression949);
                    f2=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f2.getTree());

                    // AST REWRITE
                    // elements: op, f1, f2
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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:74: ^( COMPAREEXPRESSION $f1 $op $f2)
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:3: sAttributeName ASSIGN mathExpression
                    {
                    pushFollow(FOLLOW_sAttributeName_in_expression971);
                    sAttributeName93=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sAttributeName.add(sAttributeName93.getTree());

                    ASSIGN94=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_expression973); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ASSIGN.add(ASSIGN94);


                    pushFollow(FOLLOW_mathExpression_in_expression975);
                    mathExpression95=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(mathExpression95.getTree());

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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:43: ^( ASSIGNMENT sAttributeName mathExpression )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:1: mathExpression : mult ( ( PLUS ^| MINUS ^) mult )* ;
    public final SaseParser.mathExpression_return mathExpression() throws RecognitionException {
        SaseParser.mathExpression_return retval = new SaseParser.mathExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PLUS97=null;
        Token MINUS98=null;
        SaseParser.mult_return mult96 =null;

        SaseParser.mult_return mult99 =null;


        CommonTree PLUS97_tree=null;
        CommonTree MINUS98_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:15: ( mult ( ( PLUS ^| MINUS ^) mult )* )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:17: mult ( ( PLUS ^| MINUS ^) mult )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_mult_in_mathExpression995);
            mult96=mult();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mult96.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:22: ( ( PLUS ^| MINUS ^) mult )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==MINUS||LA23_0==PLUS) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:23: ( PLUS ^| MINUS ^) mult
            	    {
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:23: ( PLUS ^| MINUS ^)
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
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:24: PLUS ^
            	            {
            	            PLUS97=(Token)match(input,PLUS,FOLLOW_PLUS_in_mathExpression999); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            PLUS97_tree = 
            	            (CommonTree)adaptor.create(PLUS97)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(PLUS97_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:30: MINUS ^
            	            {
            	            MINUS98=(Token)match(input,MINUS,FOLLOW_MINUS_in_mathExpression1002); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MINUS98_tree = 
            	            (CommonTree)adaptor.create(MINUS98)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(MINUS98_tree, root_0);
            	            }

            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_mult_in_mathExpression1006);
            	    mult99=mult();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, mult99.getTree());

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:1: mult : term ( ( MULT ^| DIVISION ^) term )* ;
    public final SaseParser.mult_return mult() throws RecognitionException {
        SaseParser.mult_return retval = new SaseParser.mult_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token MULT101=null;
        Token DIVISION102=null;
        SaseParser.term_return term100 =null;

        SaseParser.term_return term103 =null;


        CommonTree MULT101_tree=null;
        CommonTree DIVISION102_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:7: ( term ( ( MULT ^| DIVISION ^) term )* )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:9: term ( ( MULT ^| DIVISION ^) term )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_term_in_mult1020);
            term100=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, term100.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:14: ( ( MULT ^| DIVISION ^) term )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==DIVISION||LA25_0==MULT) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:15: ( MULT ^| DIVISION ^) term
            	    {
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:15: ( MULT ^| DIVISION ^)
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
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:16: MULT ^
            	            {
            	            MULT101=(Token)match(input,MULT,FOLLOW_MULT_in_mult1024); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MULT101_tree = 
            	            (CommonTree)adaptor.create(MULT101)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(MULT101_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:22: DIVISION ^
            	            {
            	            DIVISION102=(Token)match(input,DIVISION,FOLLOW_DIVISION_in_mult1027); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            DIVISION102_tree = 
            	            (CommonTree)adaptor.create(DIVISION102)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(DIVISION102_tree, root_0);
            	            }

            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_term_in_mult1031);
            	    term103=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, term103.getTree());

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:182:1: term : ( attributeTerm | value | LBRACKET ! mathExpression RBRACKET !);
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LBRACKET106=null;
        Token RBRACKET108=null;
        SaseParser.attributeTerm_return attributeTerm104 =null;

        SaseParser.value_return value105 =null;

        SaseParser.mathExpression_return mathExpression107 =null;


        CommonTree LBRACKET106_tree=null;
        CommonTree RBRACKET108_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:182:5: ( attributeTerm | value | LBRACKET ! mathExpression RBRACKET !)
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:182:7: attributeTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_attributeTerm_in_term1044);
                    attributeTerm104=attributeTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attributeTerm104.getTree());

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:183:3: value
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_value_in_term1050);
                    value105=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value105.getTree());

                    }
                    break;
                case 3 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:3: LBRACKET ! mathExpression RBRACKET !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    LBRACKET106=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_term1055); if (state.failed) return retval;

                    pushFollow(FOLLOW_mathExpression_in_term1058);
                    mathExpression107=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mathExpression107.getTree());

                    RBRACKET108=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_term1060); if (state.failed) return retval;

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:187:1: attributeTerm : ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) |aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) );
    public final SaseParser.attributeTerm_return attributeTerm() throws RecognitionException {
        SaseParser.attributeTerm_return retval = new SaseParser.attributeTerm_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token aName=null;
        Token member=null;
        Token POINT111=null;
        Token NAME112=null;
        Token POINT113=null;
        SaseParser.aggregation_return aggregation109 =null;

        SaseParser.kAttributeUsage_return kAttributeUsage110 =null;


        CommonTree aName_tree=null;
        CommonTree member_tree=null;
        CommonTree POINT111_tree=null;
        CommonTree NAME112_tree=null;
        CommonTree POINT113_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_kAttributeUsage=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeUsage");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:187:14: ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) |aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:187:16: aggregation
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_aggregation_in_attributeTerm1072);
                    aggregation109=aggregation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregation109.getTree());

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:188:5: kAttributeUsage POINT NAME
                    {
                    pushFollow(FOLLOW_kAttributeUsage_in_attributeTerm1080);
                    kAttributeUsage110=kAttributeUsage();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeUsage.add(kAttributeUsage110.getTree());

                    POINT111=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1082); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT111);


                    NAME112=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1084); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME112);


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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:188:35: ^( KMEMBER kAttributeUsage NAME )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:189:5: aName= NAME POINT member= NAME
                    {
                    aName=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1103); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(aName);


                    POINT113=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1105); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT113);


                    member=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1109); if (state.failed) return retval; 
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

                    root_0 = (CommonTree)adaptor.nil();
                    // 189:34: -> ^( MEMBER $aName $member)
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:189:37: ^( MEMBER $aName $member)
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:192:1: aggregation : ( aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION aggop $var $member) | aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION aggop $var ( $member)? ) );
    public final SaseParser.aggregation_return aggregation() throws RecognitionException {
        SaseParser.aggregation_return retval = new SaseParser.aggregation_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token var=null;
        Token name=null;
        Token number=null;
        Token member=null;
        Token LBRACKET115=null;
        Token BBRACKETLEFT116=null;
        Token POINT117=null;
        Token POINT118=null;
        Token MINUS119=null;
        Token BBRACKETRIGHT120=null;
        Token POINT121=null;
        Token RBRACKET122=null;
        Token LBRACKET124=null;
        Token BBRACKETLEFT125=null;
        Token BBRACKETRIGHT126=null;
        Token POINT127=null;
        Token RBRACKET128=null;
        SaseParser.aggop_return aggop114 =null;

        SaseParser.aggop_return aggop123 =null;


        CommonTree var_tree=null;
        CommonTree name_tree=null;
        CommonTree number_tree=null;
        CommonTree member_tree=null;
        CommonTree LBRACKET115_tree=null;
        CommonTree BBRACKETLEFT116_tree=null;
        CommonTree POINT117_tree=null;
        CommonTree POINT118_tree=null;
        CommonTree MINUS119_tree=null;
        CommonTree BBRACKETRIGHT120_tree=null;
        CommonTree POINT121_tree=null;
        CommonTree RBRACKET122_tree=null;
        CommonTree LBRACKET124_tree=null;
        CommonTree BBRACKETLEFT125_tree=null;
        CommonTree BBRACKETRIGHT126_tree=null;
        CommonTree POINT127_tree=null;
        CommonTree RBRACKET128_tree=null;
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
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:193:2: ( aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION aggop $var $member) | aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION aggop $var ( $member)? ) )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:193:5: aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}?
                    {
                    pushFollow(FOLLOW_aggop_in_aggregation1134);
                    aggop114=aggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggop.add(aggop114.getTree());

                    LBRACKET115=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1136); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET115);


                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1140); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);


                    BBRACKETLEFT116=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1142); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT116);


                    POINT117=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1144); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT117);


                    POINT118=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1146); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT118);


                    name=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1150); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(name);


                    MINUS119=(Token)match(input,MINUS,FOLLOW_MINUS_in_aggregation1152); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MINUS.add(MINUS119);


                    number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_aggregation1156); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(number);


                    BBRACKETRIGHT120=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1158); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT120);


                    POINT121=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1160); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT121);


                    member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1164); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(member);


                    RBRACKET122=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1166); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET122);


                    if ( !((name.getText().equalsIgnoreCase("I") && Integer.parseInt(number.getText()) == 1)) ) {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        throw new FailedPredicateException(input, "aggregation", "$name.getText().equalsIgnoreCase(\"I\") && Integer.parseInt($number.getText()) == 1");
                    }

                    // AST REWRITE
                    // elements: var, aggop, member
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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:194:7: ^( AGGREGATION aggop $var $member)
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:195:5: aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET
                    {
                    pushFollow(FOLLOW_aggop_in_aggregation1192);
                    aggop123=aggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggop.add(aggop123.getTree());

                    LBRACKET124=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1194); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET124);


                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1198); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);


                    BBRACKETLEFT125=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1200); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT125);


                    BBRACKETRIGHT126=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1202); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT126);


                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:195:56: ( POINT member= NAME )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==POINT) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:195:57: POINT member= NAME
                            {
                            POINT127=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1205); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_POINT.add(POINT127);


                            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1209); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NAME.add(member);


                            }
                            break;

                    }


                    RBRACKET128=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1213); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET128);


                    // AST REWRITE
                    // elements: member, var, aggop
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
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:196:7: ^( AGGREGATION aggop $var ( $member)? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(AGGREGATION, "AGGREGATION")
                        , root_1);

                        adaptor.addChild(root_1, stream_aggop.nextTree());

                        adaptor.addChild(root_1, stream_var.nextNode());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:196:33: ( $member)?
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:199:1: aggop : ( MIN | MAX | SUM | COUNT | AVG );
    public final SaseParser.aggop_return aggop() throws RecognitionException {
        SaseParser.aggop_return retval = new SaseParser.aggop_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set129=null;

        CommonTree set129_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:200:2: ( MIN | MAX | SUM | COUNT | AVG )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set129=(Token)input.LT(1);

            if ( input.LA(1)==AVG||input.LA(1)==COUNT||input.LA(1)==MAX||input.LA(1)==MIN||input.LA(1)==SUM ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set129)
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:203:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set130=null;

        CommonTree set130_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:203:8: ( NUMBER | STRING_LITERAL )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set130=(Token)input.LT(1);

            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set130)
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
    public static final BitSet FOLLOW_CREATE_in_createStmt170 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_VIEW_in_createStmt172 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_createStmt174 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_queryStmt_in_createStmt176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_patternPart_in_queryStmt196 = new BitSet(new long[]{0x0A00040000000002L});
    public static final BitSet FOLLOW_wherePart_in_queryStmt198 = new BitSet(new long[]{0x0800040000000002L});
    public static final BitSet FOLLOW_withinPart_in_queryStmt201 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_returnPart_in_queryStmt204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart237 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart239 = new BitSet(new long[]{0x0100100048084002L});
    public static final BitSet FOLLOW_timeunit_in_withinPart241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart283 = new BitSet(new long[]{0x0009802000000000L});
    public static final BitSet FOLLOW_skipPart_in_wherePart285 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart287 = new BitSet(new long[]{0x0030001114201180L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart289 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart308 = new BitSet(new long[]{0x0030001114201180L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_skipMethod_in_skipPart329 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACKET_in_skipPart331 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parameterList_in_skipPart333 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_skipPart335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart384 = new BitSet(new long[]{0x0000200000000002L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart386 = new BitSet(new long[]{0x0000200000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnPart409 = new BitSet(new long[]{0x0020000114001080L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart411 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_COMMA_in_returnPart414 = new BitSet(new long[]{0x0020000114001080L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart416 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_seqPatternDecl_in_patternDecl440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEQ_in_seqPatternDecl452 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACKET_in_seqPatternDecl454 = new BitSet(new long[]{0x0000000900200000L});
    public static final BitSet FOLLOW_stateDef_in_seqPatternDecl456 = new BitSet(new long[]{0x0000020000000400L});
    public static final BitSet FOLLOW_COMMA_in_seqPatternDecl459 = new BitSet(new long[]{0x0000000900200000L});
    public static final BitSet FOLLOW_stateDef_in_seqPatternDecl461 = new BitSet(new long[]{0x0000020000000400L});
    public static final BitSet FOLLOW_RBRACKET_in_seqPatternDecl465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef489 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef512 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef537 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef541 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef543 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef545 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef563 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef567 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef569 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeDefinition596 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_sAttributeName_in_typeDefinition598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_ktypeDefinition609 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_PLUS_in_ktypeDefinition611 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_kAttributeName_in_ktypeDefinition613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList627 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_COMMA_in_parameterList630 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList632 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName692 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName695 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage724 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_current_in_kAttributeUsage727 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage741 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_first_in_kAttributeUsage743 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage756 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_last_in_kAttributeUsage758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage771 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_len_in_kAttributeUsage773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_current794 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_current798 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_current800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_first815 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_NUMBER_in_first819 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_first821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_last834 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_last838 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_MINUS_in_last840 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_NUMBER_in_last844 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_last846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_len862 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_len864 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_POINT_in_len866 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_LEN_in_len868 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_len870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions883 = new BitSet(new long[]{0x0000000000000422L});
    public static final BitSet FOLLOW_AND_in_whereExpressions887 = new BitSet(new long[]{0x0030001114201180L});
    public static final BitSet FOLLOW_COMMA_in_whereExpressions889 = new BitSet(new long[]{0x0030001114201180L});
    public static final BitSet FOLLOW_expression_in_whereExpressions892 = new BitSet(new long[]{0x0000000000000422L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_expression915 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_expression917 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_expression919 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression935 = new BitSet(new long[]{0x0000400000000800L});
    public static final BitSet FOLLOW_SINGLEEQUALS_in_expression940 = new BitSet(new long[]{0x0030001114201080L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression944 = new BitSet(new long[]{0x0030001114201080L});
    public static final BitSet FOLLOW_mathExpression_in_expression949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_expression971 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_ASSIGN_in_expression973 = new BitSet(new long[]{0x0030001114201080L});
    public static final BitSet FOLLOW_mathExpression_in_expression975 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mult_in_mathExpression995 = new BitSet(new long[]{0x0000008020000002L});
    public static final BitSet FOLLOW_PLUS_in_mathExpression999 = new BitSet(new long[]{0x0030001114201080L});
    public static final BitSet FOLLOW_MINUS_in_mathExpression1002 = new BitSet(new long[]{0x0030001114201080L});
    public static final BitSet FOLLOW_mult_in_mathExpression1006 = new BitSet(new long[]{0x0000008020000002L});
    public static final BitSet FOLLOW_term_in_mult1020 = new BitSet(new long[]{0x0000000080010002L});
    public static final BitSet FOLLOW_MULT_in_mult1024 = new BitSet(new long[]{0x0030001114201080L});
    public static final BitSet FOLLOW_DIVISION_in_mult1027 = new BitSet(new long[]{0x0030001114201080L});
    public static final BitSet FOLLOW_term_in_mult1031 = new BitSet(new long[]{0x0000000080010002L});
    public static final BitSet FOLLOW_attributeTerm_in_term1044 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term1050 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_term1055 = new BitSet(new long[]{0x0030001114201080L});
    public static final BitSet FOLLOW_mathExpression_in_term1058 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_term1060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregation_in_attributeTerm1072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_attributeTerm1080 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1082 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1103 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1105 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggop_in_aggregation1134 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1136 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1140 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1142 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1144 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1146 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1150 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_MINUS_in_aggregation1152 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_NUMBER_in_aggregation1156 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1158 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1160 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1164 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggop_in_aggregation1192 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1194 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1198 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1200 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1202 = new BitSet(new long[]{0x0000030000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1205 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1209 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1213 = new BitSet(new long[]{0x0000000000000002L});

}