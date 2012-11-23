// $ANTLR 3.4 E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2012-11-23 14:50:13
 
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:1: start : ( createStmt | queryStmt );
    public final SaseParser.start_return start() throws RecognitionException {
        SaseParser.start_return retval = new SaseParser.start_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.createStmt_return createStmt1 =null;

        SaseParser.queryStmt_return queryStmt2 =null;



        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:8: ( createStmt | queryStmt )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:10: createStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_createStmt_in_start158);
                    createStmt1=createStmt();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, createStmt1.getTree());

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:4: queryStmt
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_queryStmt_in_start164);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:1: createStmt : CREATE VIEW NAME queryStmt -> ^( CREATEVIEW NAME queryStmt ) ;
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
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:2: ( CREATE VIEW NAME queryStmt -> ^( CREATEVIEW NAME queryStmt ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:4: CREATE VIEW NAME queryStmt
            {
            CREATE3=(Token)match(input,CREATE,FOLLOW_CREATE_in_createStmt174); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CREATE.add(CREATE3);


            VIEW4=(Token)match(input,VIEW,FOLLOW_VIEW_in_createStmt176); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_VIEW.add(VIEW4);


            NAME5=(Token)match(input,NAME,FOLLOW_NAME_in_createStmt178); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME5);


            pushFollow(FOLLOW_queryStmt_in_createStmt180);
            queryStmt6=queryStmt();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_queryStmt.add(queryStmt6.getTree());

            // AST REWRITE
            // elements: NAME, queryStmt
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 66:31: -> ^( CREATEVIEW NAME queryStmt )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:34: ^( CREATEVIEW NAME queryStmt )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:1: queryStmt : patternPart ( wherePart )? ( withinPart )? ( endsAtPart )? returnPart -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( endsAtPart )? returnPart ) ;
    public final SaseParser.queryStmt_return queryStmt() throws RecognitionException {
        SaseParser.queryStmt_return retval = new SaseParser.queryStmt_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.patternPart_return patternPart7 =null;

        SaseParser.wherePart_return wherePart8 =null;

        SaseParser.withinPart_return withinPart9 =null;

        SaseParser.endsAtPart_return endsAtPart10 =null;

        SaseParser.returnPart_return returnPart11 =null;


        RewriteRuleSubtreeStream stream_patternPart=new RewriteRuleSubtreeStream(adaptor,"rule patternPart");
        RewriteRuleSubtreeStream stream_returnPart=new RewriteRuleSubtreeStream(adaptor,"rule returnPart");
        RewriteRuleSubtreeStream stream_withinPart=new RewriteRuleSubtreeStream(adaptor,"rule withinPart");
        RewriteRuleSubtreeStream stream_wherePart=new RewriteRuleSubtreeStream(adaptor,"rule wherePart");
        RewriteRuleSubtreeStream stream_endsAtPart=new RewriteRuleSubtreeStream(adaptor,"rule endsAtPart");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:10: ( patternPart ( wherePart )? ( withinPart )? ( endsAtPart )? returnPart -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( endsAtPart )? returnPart ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:13: patternPart ( wherePart )? ( withinPart )? ( endsAtPart )? returnPart
            {
            pushFollow(FOLLOW_patternPart_in_queryStmt200);
            patternPart7=patternPart();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_patternPart.add(patternPart7.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:25: ( wherePart )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==WHERE) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:25: wherePart
                    {
                    pushFollow(FOLLOW_wherePart_in_queryStmt202);
                    wherePart8=wherePart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_wherePart.add(wherePart8.getTree());

                    }
                    break;

            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:36: ( withinPart )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==WITHIN) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:36: withinPart
                    {
                    pushFollow(FOLLOW_withinPart_in_queryStmt205);
                    withinPart9=withinPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_withinPart.add(withinPart9.getTree());

                    }
                    break;

            }


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:48: ( endsAtPart )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ENDS) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:48: endsAtPart
                    {
                    pushFollow(FOLLOW_endsAtPart_in_queryStmt208);
                    endsAtPart10=endsAtPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_endsAtPart.add(endsAtPart10.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_returnPart_in_queryStmt211);
            returnPart11=returnPart();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_returnPart.add(returnPart11.getTree());

            // AST REWRITE
            // elements: wherePart, returnPart, endsAtPart, withinPart, patternPart
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 69:71: -> ^( QUERY patternPart ( wherePart )? ( withinPart )? ( endsAtPart )? returnPart )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:74: ^( QUERY patternPart ( wherePart )? ( withinPart )? ( endsAtPart )? returnPart )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(QUERY, "QUERY")
                , root_1);

                adaptor.addChild(root_1, stream_patternPart.nextTree());

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:94: ( wherePart )?
                if ( stream_wherePart.hasNext() ) {
                    adaptor.addChild(root_1, stream_wherePart.nextTree());

                }
                stream_wherePart.reset();

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:105: ( withinPart )?
                if ( stream_withinPart.hasNext() ) {
                    adaptor.addChild(root_1, stream_withinPart.nextTree());

                }
                stream_withinPart.reset();

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:117: ( endsAtPart )?
                if ( stream_endsAtPart.hasNext() ) {
                    adaptor.addChild(root_1, stream_endsAtPart.nextTree());

                }
                stream_endsAtPart.reset();

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:73:1: withinPart : WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) ;
    public final SaseParser.withinPart_return withinPart() throws RecognitionException {
        SaseParser.withinPart_return retval = new SaseParser.withinPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token WITHIN12=null;
        Token NUMBER13=null;
        SaseParser.timeunit_return timeunit14 =null;


        CommonTree WITHIN12_tree=null;
        CommonTree NUMBER13_tree=null;
        RewriteRuleTokenStream stream_WITHIN=new RewriteRuleTokenStream(adaptor,"token WITHIN");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
        RewriteRuleSubtreeStream stream_timeunit=new RewriteRuleSubtreeStream(adaptor,"rule timeunit");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:2: ( WITHIN NUMBER ( timeunit )? -> ^( WITHIN NUMBER ( timeunit )? ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:4: WITHIN NUMBER ( timeunit )?
            {
            WITHIN12=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart245); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_WITHIN.add(WITHIN12);


            NUMBER13=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart247); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER13);


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:18: ( timeunit )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==DAY||LA5_0==HOUR||LA5_0==MILLISECOND||LA5_0==MINUTE||LA5_0==SECOND||LA5_0==WEEK) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:18: timeunit
                    {
                    pushFollow(FOLLOW_timeunit_in_withinPart249);
                    timeunit14=timeunit();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_timeunit.add(timeunit14.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: timeunit, NUMBER, WITHIN
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 74:28: -> ^( WITHIN NUMBER ( timeunit )? )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:31: ^( WITHIN NUMBER ( timeunit )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_WITHIN.nextNode()
                , root_1);

                adaptor.addChild(root_1, 
                stream_NUMBER.nextNode()
                );

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:47: ( timeunit )?
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


    public static class endsAtPart_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "endsAtPart"
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:1: endsAtPart : ENDS AT attributeTerm -> ^( ENDSAT attributeTerm ) ;
    public final SaseParser.endsAtPart_return endsAtPart() throws RecognitionException {
        SaseParser.endsAtPart_return retval = new SaseParser.endsAtPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ENDS15=null;
        Token AT16=null;
        SaseParser.attributeTerm_return attributeTerm17 =null;


        CommonTree ENDS15_tree=null;
        CommonTree AT16_tree=null;
        RewriteRuleTokenStream stream_AT=new RewriteRuleTokenStream(adaptor,"token AT");
        RewriteRuleTokenStream stream_ENDS=new RewriteRuleTokenStream(adaptor,"token ENDS");
        RewriteRuleSubtreeStream stream_attributeTerm=new RewriteRuleSubtreeStream(adaptor,"rule attributeTerm");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:3: ( ENDS AT attributeTerm -> ^( ENDSAT attributeTerm ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:5: ENDS AT attributeTerm
            {
            ENDS15=(Token)match(input,ENDS,FOLLOW_ENDS_in_endsAtPart274); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ENDS.add(ENDS15);


            AT16=(Token)match(input,AT,FOLLOW_AT_in_endsAtPart276); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_AT.add(AT16);


            pushFollow(FOLLOW_attributeTerm_in_endsAtPart278);
            attributeTerm17=attributeTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeTerm.add(attributeTerm17.getTree());

            // AST REWRITE
            // elements: attributeTerm
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 78:27: -> ^( ENDSAT attributeTerm )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:30: ^( ENDSAT attributeTerm )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ENDSAT, "ENDSAT")
                , root_1);

                adaptor.addChild(root_1, stream_attributeTerm.nextTree());

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
    // $ANTLR end "endsAtPart"


    public static class timeunit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "timeunit"
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:1: timeunit : ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND );
    public final SaseParser.timeunit_return timeunit() throws RecognitionException {
        SaseParser.timeunit_return retval = new SaseParser.timeunit_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set18=null;

        CommonTree set18_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:9: ( WEEK | DAY | HOUR | MINUTE | SECOND | MILLISECOND )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set18=(Token)input.LT(1);

            if ( input.LA(1)==DAY||input.LA(1)==HOUR||input.LA(1)==MILLISECOND||input.LA(1)==MINUTE||input.LA(1)==SECOND||input.LA(1)==WEEK ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set18)
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:1: wherePart : ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
    public final SaseParser.wherePart_return wherePart() throws RecognitionException {
        SaseParser.wherePart_return retval = new SaseParser.wherePart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token WHERE19=null;
        Token LEFTCURLY21=null;
        Token RIGHTCURLY23=null;
        Token WHERE24=null;
        SaseParser.skipPart_return skipPart20 =null;

        SaseParser.whereExpressions_return whereExpressions22 =null;

        SaseParser.whereExpressions_return whereExpressions25 =null;


        CommonTree WHERE19_tree=null;
        CommonTree LEFTCURLY21_tree=null;
        CommonTree RIGHTCURLY23_tree=null;
        CommonTree WHERE24_tree=null;
        RewriteRuleTokenStream stream_RIGHTCURLY=new RewriteRuleTokenStream(adaptor,"token RIGHTCURLY");
        RewriteRuleTokenStream stream_WHERE=new RewriteRuleTokenStream(adaptor,"token WHERE");
        RewriteRuleTokenStream stream_LEFTCURLY=new RewriteRuleTokenStream(adaptor,"token LEFTCURLY");
        RewriteRuleSubtreeStream stream_skipPart=new RewriteRuleSubtreeStream(adaptor,"rule skipPart");
        RewriteRuleSubtreeStream stream_whereExpressions=new RewriteRuleSubtreeStream(adaptor,"rule whereExpressions");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:2: ( WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE skipPart whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WHERE) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==PARTITION_CONTIGUITY||(LA6_1 >= SKIP_TILL_ANY_MATCH && LA6_1 <= SKIP_TILL_NEXT_MATCH)||LA6_1==STRICT_CONTIGUITY) ) {
                    alt6=1;
                }
                else if ( ((LA6_1 >= AVG && LA6_1 <= BBRACKETLEFT)||LA6_1==BOOLEAN||LA6_1==COUNT||LA6_1==LBRACKET||LA6_1==MAX||LA6_1==MIN||LA6_1==NAME||LA6_1==NUMBER||(LA6_1 >= STRING_LITERAL && LA6_1 <= SUM)) ) {
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:4: WHERE skipPart LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    WHERE19=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart317); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE19);


                    pushFollow(FOLLOW_skipPart_in_wherePart319);
                    skipPart20=skipPart();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_skipPart.add(skipPart20.getTree());

                    LEFTCURLY21=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart321); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFTCURLY.add(LEFTCURLY21);


                    pushFollow(FOLLOW_whereExpressions_in_wherePart323);
                    whereExpressions22=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions22.getTree());

                    RIGHTCURLY23=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart325); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHTCURLY.add(RIGHTCURLY23);


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
                    // 84:57: -> ^( WHERE skipPart whereExpressions )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:60: ^( WHERE skipPart whereExpressions )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:85:4: WHERE whereExpressions
                    {
                    WHERE24=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart342); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHERE.add(WHERE24);


                    pushFollow(FOLLOW_whereExpressions_in_wherePart344);
                    whereExpressions25=whereExpressions();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_whereExpressions.add(whereExpressions25.getTree());

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
                    // 85:27: -> ^( WHERE whereExpressions )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:85:30: ^( WHERE whereExpressions )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:1: skipPart : skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) ;
    public final SaseParser.skipPart_return skipPart() throws RecognitionException {
        SaseParser.skipPart_return retval = new SaseParser.skipPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LBRACKET27=null;
        Token RBRACKET29=null;
        SaseParser.skipMethod_return skipMethod26 =null;

        SaseParser.parameterList_return parameterList28 =null;


        CommonTree LBRACKET27_tree=null;
        CommonTree RBRACKET29_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_skipMethod=new RewriteRuleSubtreeStream(adaptor,"rule skipMethod");
        RewriteRuleSubtreeStream stream_parameterList=new RewriteRuleSubtreeStream(adaptor,"rule parameterList");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:2: ( skipMethod LBRACKET parameterList RBRACKET -> ^( skipMethod parameterList ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:89:4: skipMethod LBRACKET parameterList RBRACKET
            {
            pushFollow(FOLLOW_skipMethod_in_skipPart363);
            skipMethod26=skipMethod();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_skipMethod.add(skipMethod26.getTree());

            LBRACKET27=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_skipPart365); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET27);


            pushFollow(FOLLOW_parameterList_in_skipPart367);
            parameterList28=parameterList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_parameterList.add(parameterList28.getTree());

            RBRACKET29=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_skipPart369); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET29);


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
            // 90:2: -> ^( skipMethod parameterList )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:90:5: ^( skipMethod parameterList )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:1: skipMethod : ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY );
    public final SaseParser.skipMethod_return skipMethod() throws RecognitionException {
        SaseParser.skipMethod_return retval = new SaseParser.skipMethod_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set30=null;

        CommonTree set30_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:11: ( SKIP_TILL_NEXT_MATCH | SKIP_TILL_ANY_MATCH | STRICT_CONTIGUITY | PARTITION_CONTIGUITY )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set30=(Token)input.LT(1);

            if ( input.LA(1)==PARTITION_CONTIGUITY||(input.LA(1) >= SKIP_TILL_ANY_MATCH && input.LA(1) <= SKIP_TILL_NEXT_MATCH)||input.LA(1)==STRICT_CONTIGUITY ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set30)
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:99:1: patternPart : PATTERN ( patternDecl )* -> ^( PATTERN ( patternDecl )* ) ;
    public final SaseParser.patternPart_return patternPart() throws RecognitionException {
        SaseParser.patternPart_return retval = new SaseParser.patternPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PATTERN31=null;
        SaseParser.patternDecl_return patternDecl32 =null;


        CommonTree PATTERN31_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleSubtreeStream stream_patternDecl=new RewriteRuleSubtreeStream(adaptor,"rule patternDecl");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:2: ( PATTERN ( patternDecl )* -> ^( PATTERN ( patternDecl )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:4: PATTERN ( patternDecl )*
            {
            PATTERN31=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart418); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PATTERN.add(PATTERN31);


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:12: ( patternDecl )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==SEQ) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:12: patternDecl
            	    {
            	    pushFollow(FOLLOW_patternDecl_in_patternPart420);
            	    patternDecl32=patternDecl();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_patternDecl.add(patternDecl32.getTree());

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


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

            root_0 = (CommonTree)adaptor.nil();
            // 100:25: -> ^( PATTERN ( patternDecl )* )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:28: ^( PATTERN ( patternDecl )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_PATTERN.nextNode()
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:38: ( patternDecl )*
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:103:1: returnPart : RETURN attributeTerm ( COMMA attributeTerm )* ( AS NAME )? -> ^( RETURN ( attributeTerm )* ( NAME )? ) ;
    public final SaseParser.returnPart_return returnPart() throws RecognitionException {
        SaseParser.returnPart_return retval = new SaseParser.returnPart_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token RETURN33=null;
        Token COMMA35=null;
        Token AS37=null;
        Token NAME38=null;
        SaseParser.attributeTerm_return attributeTerm34 =null;

        SaseParser.attributeTerm_return attributeTerm36 =null;


        CommonTree RETURN33_tree=null;
        CommonTree COMMA35_tree=null;
        CommonTree AS37_tree=null;
        CommonTree NAME38_tree=null;
        RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_RETURN=new RewriteRuleTokenStream(adaptor,"token RETURN");
        RewriteRuleSubtreeStream stream_attributeTerm=new RewriteRuleSubtreeStream(adaptor,"rule attributeTerm");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:2: ( RETURN attributeTerm ( COMMA attributeTerm )* ( AS NAME )? -> ^( RETURN ( attributeTerm )* ( NAME )? ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:5: RETURN attributeTerm ( COMMA attributeTerm )* ( AS NAME )?
            {
            RETURN33=(Token)match(input,RETURN,FOLLOW_RETURN_in_returnPart443); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RETURN.add(RETURN33);


            pushFollow(FOLLOW_attributeTerm_in_returnPart445);
            attributeTerm34=attributeTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeTerm.add(attributeTerm34.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:26: ( COMMA attributeTerm )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==COMMA) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:27: COMMA attributeTerm
            	    {
            	    COMMA35=(Token)match(input,COMMA,FOLLOW_COMMA_in_returnPart448); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA35);


            	    pushFollow(FOLLOW_attributeTerm_in_returnPart450);
            	    attributeTerm36=attributeTerm();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeTerm.add(attributeTerm36.getTree());

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:49: ( AS NAME )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==AS) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:50: AS NAME
                    {
                    AS37=(Token)match(input,AS,FOLLOW_AS_in_returnPart455); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AS.add(AS37);


                    NAME38=(Token)match(input,NAME,FOLLOW_NAME_in_returnPart457); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME38);


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
            // 104:59: -> ^( RETURN ( attributeTerm )* ( NAME )? )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:62: ^( RETURN ( attributeTerm )* ( NAME )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_RETURN.nextNode()
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:71: ( attributeTerm )*
                while ( stream_attributeTerm.hasNext() ) {
                    adaptor.addChild(root_1, stream_attributeTerm.nextTree());

                }
                stream_attributeTerm.reset();

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:86: ( NAME )?
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:107:1: patternDecl : seqPatternDecl ;
    public final SaseParser.patternDecl_return patternDecl() throws RecognitionException {
        SaseParser.patternDecl_return retval = new SaseParser.patternDecl_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.seqPatternDecl_return seqPatternDecl39 =null;



        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:2: ( seqPatternDecl )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:108:5: seqPatternDecl
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_seqPatternDecl_in_patternDecl483);
            seqPatternDecl39=seqPatternDecl();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, seqPatternDecl39.getTree());

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:111:1: seqPatternDecl : SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) ;
    public final SaseParser.seqPatternDecl_return seqPatternDecl() throws RecognitionException {
        SaseParser.seqPatternDecl_return retval = new SaseParser.seqPatternDecl_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token SEQ40=null;
        Token LBRACKET41=null;
        Token COMMA43=null;
        Token RBRACKET45=null;
        SaseParser.stateDef_return stateDef42 =null;

        SaseParser.stateDef_return stateDef44 =null;


        CommonTree SEQ40_tree=null;
        CommonTree LBRACKET41_tree=null;
        CommonTree COMMA43_tree=null;
        CommonTree RBRACKET45_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SEQ=new RewriteRuleTokenStream(adaptor,"token SEQ");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_stateDef=new RewriteRuleSubtreeStream(adaptor,"rule stateDef");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:2: ( SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET -> ^( SEQ ( stateDef )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:4: SEQ LBRACKET stateDef ( COMMA stateDef )* RBRACKET
            {
            SEQ40=(Token)match(input,SEQ,FOLLOW_SEQ_in_seqPatternDecl495); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SEQ.add(SEQ40);


            LBRACKET41=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_seqPatternDecl497); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET41);


            pushFollow(FOLLOW_stateDef_in_seqPatternDecl499);
            stateDef42=stateDef();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_stateDef.add(stateDef42.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:26: ( COMMA stateDef )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==COMMA) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:27: COMMA stateDef
            	    {
            	    COMMA43=(Token)match(input,COMMA,FOLLOW_COMMA_in_seqPatternDecl502); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA43);


            	    pushFollow(FOLLOW_stateDef_in_seqPatternDecl504);
            	    stateDef44=stateDef();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_stateDef.add(stateDef44.getTree());

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            RBRACKET45=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_seqPatternDecl508); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET45);


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
            // 112:53: -> ^( SEQ ( stateDef )* )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:56: ^( SEQ ( stateDef )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_SEQ.nextNode()
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:112:62: ( stateDef )*
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:1: stateDef : ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) );
    public final SaseParser.stateDef_return stateDef() throws RecognitionException {
        SaseParser.stateDef_return retval = new SaseParser.stateDef_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NOTSIGN46=null;
        Token NOTSIGN48=null;
        Token NOTSIGN50=null;
        Token LBRACKET51=null;
        Token RBRACKET53=null;
        Token NOTSIGN54=null;
        Token LBRACKET55=null;
        Token RBRACKET57=null;
        SaseParser.ktypeDefinition_return ktypeDefinition47 =null;

        SaseParser.typeDefinition_return typeDefinition49 =null;

        SaseParser.ktypeDefinition_return ktypeDefinition52 =null;

        SaseParser.typeDefinition_return typeDefinition56 =null;


        CommonTree NOTSIGN46_tree=null;
        CommonTree NOTSIGN48_tree=null;
        CommonTree NOTSIGN50_tree=null;
        CommonTree LBRACKET51_tree=null;
        CommonTree RBRACKET53_tree=null;
        CommonTree NOTSIGN54_tree=null;
        CommonTree LBRACKET55_tree=null;
        CommonTree RBRACKET57_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NOTSIGN=new RewriteRuleTokenStream(adaptor,"token NOTSIGN");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_typeDefinition=new RewriteRuleSubtreeStream(adaptor,"rule typeDefinition");
        RewriteRuleSubtreeStream stream_ktypeDefinition=new RewriteRuleSubtreeStream(adaptor,"rule ktypeDefinition");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:10: ( ( NOTSIGN )? ktypeDefinition -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? typeDefinition -> ^( STATE typeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET -> ^( KSTATE ktypeDefinition ( NOTSIGN )? ) | ( NOTSIGN )? LBRACKET typeDefinition RBRACKET -> ^( STATE typeDefinition ( NOTSIGN )? ) )
            int alt15=4;
            switch ( input.LA(1) ) {
            case NOTSIGN:
                {
                int LA15_1 = input.LA(2);

                if ( (LA15_1==NAME) ) {
                    int LA15_2 = input.LA(3);

                    if ( (LA15_2==PLUS) ) {
                        alt15=1;
                    }
                    else if ( (LA15_2==NAME) ) {
                        alt15=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 2, input);

                        throw nvae;

                    }
                }
                else if ( (LA15_1==LBRACKET) ) {
                    int LA15_3 = input.LA(3);

                    if ( (LA15_3==NAME) ) {
                        int LA15_6 = input.LA(4);

                        if ( (LA15_6==PLUS) ) {
                            alt15=3;
                        }
                        else if ( (LA15_6==NAME) ) {
                            alt15=4;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 6, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 3, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;

                }
                }
                break;
            case NAME:
                {
                int LA15_2 = input.LA(2);

                if ( (LA15_2==PLUS) ) {
                    alt15=1;
                }
                else if ( (LA15_2==NAME) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 2, input);

                    throw nvae;

                }
                }
                break;
            case LBRACKET:
                {
                int LA15_3 = input.LA(2);

                if ( (LA15_3==NAME) ) {
                    int LA15_6 = input.LA(3);

                    if ( (LA15_6==PLUS) ) {
                        alt15=3;
                    }
                    else if ( (LA15_6==NAME) ) {
                        alt15=4;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 6, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 3, input);

                    throw nvae;

                }
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:12: ( NOTSIGN )? ktypeDefinition
                    {
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:12: ( NOTSIGN )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==NOTSIGN) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:13: NOTSIGN
                            {
                            NOTSIGN46=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef532); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN46);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef536);
                    ktypeDefinition47=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition47.getTree());

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
                    // 116:40: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:43: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(KSTATE, "KSTATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:116:68: ( NOTSIGN )?
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:5: ( NOTSIGN )? typeDefinition
                    {
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:5: ( NOTSIGN )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==NOTSIGN) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:6: NOTSIGN
                            {
                            NOTSIGN48=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef555); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN48);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_typeDefinition_in_stateDef559);
                    typeDefinition49=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition49.getTree());

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
                    // 117:32: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:35: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(STATE, "STATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:117:58: ( NOTSIGN )?
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:118:5: ( NOTSIGN )? LBRACKET ktypeDefinition RBRACKET
                    {
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:118:5: ( NOTSIGN )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==NOTSIGN) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:118:6: NOTSIGN
                            {
                            NOTSIGN50=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef580); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN50);


                            }
                            break;

                    }


                    LBRACKET51=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef584); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET51);


                    pushFollow(FOLLOW_ktypeDefinition_in_stateDef586);
                    ktypeDefinition52=ktypeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ktypeDefinition.add(ktypeDefinition52.getTree());

                    RBRACKET53=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef588); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET53);


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
                    // 118:50: -> ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:118:53: ^( KSTATE ktypeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(KSTATE, "KSTATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_ktypeDefinition.nextTree());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:118:78: ( NOTSIGN )?
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:119:5: ( NOTSIGN )? LBRACKET typeDefinition RBRACKET
                    {
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:119:5: ( NOTSIGN )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==NOTSIGN) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:119:6: NOTSIGN
                            {
                            NOTSIGN54=(Token)match(input,NOTSIGN,FOLLOW_NOTSIGN_in_stateDef606); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NOTSIGN.add(NOTSIGN54);


                            }
                            break;

                    }


                    LBRACKET55=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_stateDef610); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET55);


                    pushFollow(FOLLOW_typeDefinition_in_stateDef612);
                    typeDefinition56=typeDefinition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeDefinition.add(typeDefinition56.getTree());

                    RBRACKET57=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_stateDef614); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET57);


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
                    // 119:49: -> ^( STATE typeDefinition ( NOTSIGN )? )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:119:52: ^( STATE typeDefinition ( NOTSIGN )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(STATE, "STATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_typeDefinition.nextTree());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:119:75: ( NOTSIGN )?
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:122:1: typeDefinition : NAME sAttributeName ;
    public final SaseParser.typeDefinition_return typeDefinition() throws RecognitionException {
        SaseParser.typeDefinition_return retval = new SaseParser.typeDefinition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME58=null;
        SaseParser.sAttributeName_return sAttributeName59 =null;


        CommonTree NAME58_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:123:2: ( NAME sAttributeName )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:123:4: NAME sAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME58=(Token)match(input,NAME,FOLLOW_NAME_in_typeDefinition639); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME58_tree = 
            (CommonTree)adaptor.create(NAME58)
            ;
            adaptor.addChild(root_0, NAME58_tree);
            }

            pushFollow(FOLLOW_sAttributeName_in_typeDefinition641);
            sAttributeName59=sAttributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, sAttributeName59.getTree());

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:126:1: ktypeDefinition : NAME PLUS kAttributeName ;
    public final SaseParser.ktypeDefinition_return ktypeDefinition() throws RecognitionException {
        SaseParser.ktypeDefinition_return retval = new SaseParser.ktypeDefinition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME60=null;
        Token PLUS61=null;
        SaseParser.kAttributeName_return kAttributeName62 =null;


        CommonTree NAME60_tree=null;
        CommonTree PLUS61_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:127:2: ( NAME PLUS kAttributeName )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:127:4: NAME PLUS kAttributeName
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME60=(Token)match(input,NAME,FOLLOW_NAME_in_ktypeDefinition652); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME60_tree = 
            (CommonTree)adaptor.create(NAME60)
            ;
            adaptor.addChild(root_0, NAME60_tree);
            }

            PLUS61=(Token)match(input,PLUS,FOLLOW_PLUS_in_ktypeDefinition654); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PLUS61_tree = 
            (CommonTree)adaptor.create(PLUS61)
            ;
            adaptor.addChild(root_0, PLUS61_tree);
            }

            pushFollow(FOLLOW_kAttributeName_in_ktypeDefinition656);
            kAttributeName62=kAttributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, kAttributeName62.getTree());

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:131:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
    public final SaseParser.parameterList_return parameterList() throws RecognitionException {
        SaseParser.parameterList_return retval = new SaseParser.parameterList_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token COMMA64=null;
        SaseParser.attributeName_return attributeName63 =null;

        SaseParser.attributeName_return attributeName65 =null;


        CommonTree COMMA64_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:132:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:132:4: attributeName ( COMMA attributeName )*
            {
            pushFollow(FOLLOW_attributeName_in_parameterList670);
            attributeName63=attributeName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_attributeName.add(attributeName63.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:132:18: ( COMMA attributeName )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==COMMA) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:132:19: COMMA attributeName
            	    {
            	    COMMA64=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList673); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_COMMA.add(COMMA64);


            	    pushFollow(FOLLOW_attributeName_in_parameterList675);
            	    attributeName65=attributeName();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_attributeName.add(attributeName65.getTree());

            	    }
            	    break;

            	default :
            	    break loop16;
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
            // 132:41: -> ^( PARAMLIST ( attributeName )* )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:132:44: ^( PARAMLIST ( attributeName )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(PARAMLIST, "PARAMLIST")
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:132:56: ( attributeName )*
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:135:1: attributeName : ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        SaseParser.kAttributeName_return kAttributeName66 =null;

        SaseParser.sAttributeName_return sAttributeName67 =null;


        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_kAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeName");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:136:2: ( kAttributeName -> ^( KATTRIBUTE NAME ) | sAttributeName -> ^( ATTRIBUTE NAME ) )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==NAME) ) {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==BBRACKETLEFT) ) {
                    alt17=1;
                }
                else if ( (LA17_1==EOF||LA17_1==COMMA||LA17_1==RBRACKET) ) {
                    alt17=2;
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:136:5: kAttributeName
                    {
                    pushFollow(FOLLOW_kAttributeName_in_attributeName699);
                    kAttributeName66=kAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeName.add(kAttributeName66.getTree());

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
                    // 136:20: -> ^( KATTRIBUTE NAME )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:136:23: ^( KATTRIBUTE NAME )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:137:4: sAttributeName
                    {
                    pushFollow(FOLLOW_sAttributeName_in_attributeName713);
                    sAttributeName67=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sAttributeName.add(sAttributeName67.getTree());

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
                    // 137:19: -> ^( ATTRIBUTE NAME )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:137:22: ^( ATTRIBUTE NAME )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:140:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT ;
    public final SaseParser.kAttributeName_return kAttributeName() throws RecognitionException {
        SaseParser.kAttributeName_return retval = new SaseParser.kAttributeName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME68=null;
        Token BBRACKETLEFT69=null;
        Token BBRACKETRIGHT70=null;

        CommonTree NAME68_tree=null;
        CommonTree BBRACKETLEFT69_tree=null;
        CommonTree BBRACKETRIGHT70_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:141:2: ( NAME BBRACKETLEFT BBRACKETRIGHT )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:141:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME68=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName735); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME68_tree = 
            (CommonTree)adaptor.create(NAME68)
            ;
            adaptor.addChild(root_0, NAME68_tree);
            }

            BBRACKETLEFT69=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName738); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT69_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT69)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT69_tree);
            }

            BBRACKETRIGHT70=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName740); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT70_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT70)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT70_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:144:1: sAttributeName : NAME ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME71=null;

        CommonTree NAME71_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:145:2: ( NAME )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:145:4: NAME
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME71=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName751); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME71_tree = 
            (CommonTree)adaptor.create(NAME71)
            ;
            adaptor.addChild(root_0, NAME71_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:148:1: kAttributeUsage : ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) );
    public final SaseParser.kAttributeUsage_return kAttributeUsage() throws RecognitionException {
        SaseParser.kAttributeUsage_return retval = new SaseParser.kAttributeUsage_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME72=null;
        Token NAME74=null;
        Token NAME76=null;
        Token NAME78=null;
        SaseParser.current_return current73 =null;

        SaseParser.first_return first75 =null;

        SaseParser.last_return last77 =null;

        SaseParser.len_return len79 =null;


        CommonTree NAME72_tree=null;
        CommonTree NAME74_tree=null;
        CommonTree NAME76_tree=null;
        CommonTree NAME78_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleSubtreeStream stream_last=new RewriteRuleSubtreeStream(adaptor,"rule last");
        RewriteRuleSubtreeStream stream_current=new RewriteRuleSubtreeStream(adaptor,"rule current");
        RewriteRuleSubtreeStream stream_len=new RewriteRuleSubtreeStream(adaptor,"rule len");
        RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:149:2: ( NAME current -> ^( NAME CURRENT ) | NAME first -> ^( NAME FIRST ) | NAME last -> ^( NAME PREV ) | NAME len -> ^( NAME LEN ) )
            int alt18=4;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==NAME) ) {
                int LA18_1 = input.LA(2);

                if ( (LA18_1==BBRACKETLEFT) ) {
                    int LA18_2 = input.LA(3);

                    if ( (LA18_2==NAME) ) {
                        switch ( input.LA(4) ) {
                        case BBRACKETRIGHT:
                            {
                            alt18=1;
                            }
                            break;
                        case MINUS:
                            {
                            alt18=3;
                            }
                            break;
                        case POINT:
                            {
                            alt18=4;
                            }
                            break;
                        default:
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 18, 3, input);

                            throw nvae;

                        }

                    }
                    else if ( (LA18_2==NUMBER) ) {
                        alt18=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 2, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }
            switch (alt18) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:149:4: NAME current
                    {
                    NAME72=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage764); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME72);


                    pushFollow(FOLLOW_current_in_kAttributeUsage767);
                    current73=current();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_current.add(current73.getTree());

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
                    // 149:19: -> ^( NAME CURRENT )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:149:22: ^( NAME CURRENT )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:4: NAME first
                    {
                    NAME74=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage781); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME74);


                    pushFollow(FOLLOW_first_in_kAttributeUsage783);
                    first75=first();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_first.add(first75.getTree());

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
                    // 150:15: -> ^( NAME FIRST )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:150:18: ^( NAME FIRST )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:151:4: NAME last
                    {
                    NAME76=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage796); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME76);


                    pushFollow(FOLLOW_last_in_kAttributeUsage798);
                    last77=last();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_last.add(last77.getTree());

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
                    // 151:14: -> ^( NAME PREV )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:151:17: ^( NAME PREV )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:152:4: NAME len
                    {
                    NAME78=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage811); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME78);


                    pushFollow(FOLLOW_len_in_kAttributeUsage813);
                    len79=len();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_len.add(len79.getTree());

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
                    // 152:13: -> ^( NAME LEN )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:152:16: ^( NAME LEN )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:155:1: current : BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?;
    public final SaseParser.current_return current() throws RecognitionException {
        SaseParser.current_return retval = new SaseParser.current_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token name=null;
        Token BBRACKETLEFT80=null;
        Token BBRACKETRIGHT81=null;

        CommonTree name_tree=null;
        CommonTree BBRACKETLEFT80_tree=null;
        CommonTree BBRACKETRIGHT81_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:155:9: ( BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?)
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:156:2: BBRACKETLEFT name= NAME BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT80=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_current834); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT80_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT80)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT80_tree);
            }

            name=(Token)match(input,NAME,FOLLOW_NAME_in_current838); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = 
            (CommonTree)adaptor.create(name)
            ;
            adaptor.addChild(root_0, name_tree);
            }

            BBRACKETRIGHT81=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_current840); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT81_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT81)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT81_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:159:1: first : BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?;
    public final SaseParser.first_return first() throws RecognitionException {
        SaseParser.first_return retval = new SaseParser.first_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token number=null;
        Token BBRACKETLEFT82=null;
        Token BBRACKETRIGHT83=null;

        CommonTree number_tree=null;
        CommonTree BBRACKETLEFT82_tree=null;
        CommonTree BBRACKETRIGHT83_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:159:7: ( BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?)
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:160:2: BBRACKETLEFT number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT82=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_first855); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT82_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT82)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT82_tree);
            }

            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_first859); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = 
            (CommonTree)adaptor.create(number)
            ;
            adaptor.addChild(root_0, number_tree);
            }

            BBRACKETRIGHT83=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_first861); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT83_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT83)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT83_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:163:1: last : BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?;
    public final SaseParser.last_return last() throws RecognitionException {
        SaseParser.last_return retval = new SaseParser.last_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token name=null;
        Token number=null;
        Token BBRACKETLEFT84=null;
        Token MINUS85=null;
        Token BBRACKETRIGHT86=null;

        CommonTree name_tree=null;
        CommonTree number_tree=null;
        CommonTree BBRACKETLEFT84_tree=null;
        CommonTree MINUS85_tree=null;
        CommonTree BBRACKETRIGHT86_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:163:6: ( BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?)
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:164:2: BBRACKETLEFT name= NAME MINUS number= NUMBER BBRACKETRIGHT {...}?
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT84=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_last874); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT84_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT84)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT84_tree);
            }

            name=(Token)match(input,NAME,FOLLOW_NAME_in_last878); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            name_tree = 
            (CommonTree)adaptor.create(name)
            ;
            adaptor.addChild(root_0, name_tree);
            }

            MINUS85=(Token)match(input,MINUS,FOLLOW_MINUS_in_last880); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            MINUS85_tree = 
            (CommonTree)adaptor.create(MINUS85)
            ;
            adaptor.addChild(root_0, MINUS85_tree);
            }

            number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_last884); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            number_tree = 
            (CommonTree)adaptor.create(number)
            ;
            adaptor.addChild(root_0, number_tree);
            }

            BBRACKETRIGHT86=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_last886); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT86_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT86)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT86_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:167:1: len : BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT ;
    public final SaseParser.len_return len() throws RecognitionException {
        SaseParser.len_return retval = new SaseParser.len_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token BBRACKETLEFT87=null;
        Token NAME88=null;
        Token POINT89=null;
        Token LEN90=null;
        Token BBRACKETRIGHT91=null;

        CommonTree BBRACKETLEFT87_tree=null;
        CommonTree NAME88_tree=null;
        CommonTree POINT89_tree=null;
        CommonTree LEN90_tree=null;
        CommonTree BBRACKETRIGHT91_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:167:5: ( BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:168:2: BBRACKETLEFT NAME POINT LEN BBRACKETRIGHT
            {
            root_0 = (CommonTree)adaptor.nil();


            BBRACKETLEFT87=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_len902); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETLEFT87_tree = 
            (CommonTree)adaptor.create(BBRACKETLEFT87)
            ;
            adaptor.addChild(root_0, BBRACKETLEFT87_tree);
            }

            NAME88=(Token)match(input,NAME,FOLLOW_NAME_in_len904); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME88_tree = 
            (CommonTree)adaptor.create(NAME88)
            ;
            adaptor.addChild(root_0, NAME88_tree);
            }

            POINT89=(Token)match(input,POINT,FOLLOW_POINT_in_len906); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            POINT89_tree = 
            (CommonTree)adaptor.create(POINT89)
            ;
            adaptor.addChild(root_0, POINT89_tree);
            }

            LEN90=(Token)match(input,LEN,FOLLOW_LEN_in_len908); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEN90_tree = 
            (CommonTree)adaptor.create(LEN90)
            ;
            adaptor.addChild(root_0, LEN90_tree);
            }

            BBRACKETRIGHT91=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_len910); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BBRACKETRIGHT91_tree = 
            (CommonTree)adaptor.create(BBRACKETRIGHT91)
            ;
            adaptor.addChild(root_0, BBRACKETRIGHT91_tree);
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:171:1: whereExpressions : expression ( ( AND | COMMA ) expression )* -> ^( WHEREEXPRESSION ( expression )* ) ;
    public final SaseParser.whereExpressions_return whereExpressions() throws RecognitionException {
        SaseParser.whereExpressions_return retval = new SaseParser.whereExpressions_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token AND93=null;
        Token COMMA94=null;
        SaseParser.expression_return expression92 =null;

        SaseParser.expression_return expression95 =null;


        CommonTree AND93_tree=null;
        CommonTree COMMA94_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:2: ( expression ( ( AND | COMMA ) expression )* -> ^( WHEREEXPRESSION ( expression )* ) )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:4: expression ( ( AND | COMMA ) expression )*
            {
            pushFollow(FOLLOW_expression_in_whereExpressions923);
            expression92=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression92.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:15: ( ( AND | COMMA ) expression )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==AND||LA20_0==COMMA) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:16: ( AND | COMMA ) expression
            	    {
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:16: ( AND | COMMA )
            	    int alt19=2;
            	    int LA19_0 = input.LA(1);

            	    if ( (LA19_0==AND) ) {
            	        alt19=1;
            	    }
            	    else if ( (LA19_0==COMMA) ) {
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
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:17: AND
            	            {
            	            AND93=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions927); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_AND.add(AND93);


            	            }
            	            break;
            	        case 2 :
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:21: COMMA
            	            {
            	            COMMA94=(Token)match(input,COMMA,FOLLOW_COMMA_in_whereExpressions929); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_COMMA.add(COMMA94);


            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_expression_in_whereExpressions932);
            	    expression95=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_expression.add(expression95.getTree());

            	    }
            	    break;

            	default :
            	    break loop20;
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
            // 172:41: -> ^( WHEREEXPRESSION ( expression )* )
            {
                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:44: ^( WHEREEXPRESSION ( expression )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION")
                , root_1);

                // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:172:62: ( expression )*
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:175:1: expression : ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) |f1= mathExpression (op= SINGLEEQUALS |op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) | sAttributeName ASSIGN mathExpression -> ^( ASSIGNMENT sAttributeName mathExpression ) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token op=null;
        Token BBRACKETLEFT96=null;
        Token NAME97=null;
        Token BBRACKETRIGHT98=null;
        Token ASSIGN100=null;
        SaseParser.mathExpression_return f1 =null;

        SaseParser.mathExpression_return f2 =null;

        SaseParser.sAttributeName_return sAttributeName99 =null;

        SaseParser.mathExpression_return mathExpression101 =null;


        CommonTree op_tree=null;
        CommonTree BBRACKETLEFT96_tree=null;
        CommonTree NAME97_tree=null;
        CommonTree BBRACKETRIGHT98_tree=null;
        CommonTree ASSIGN100_tree=null;
        RewriteRuleTokenStream stream_SINGLEEQUALS=new RewriteRuleTokenStream(adaptor,"token SINGLEEQUALS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");
        RewriteRuleTokenStream stream_ASSIGN=new RewriteRuleTokenStream(adaptor,"token ASSIGN");
        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_mathExpression=new RewriteRuleSubtreeStream(adaptor,"rule mathExpression");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:176:2: ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) |f1= mathExpression (op= SINGLEEQUALS |op= COMPAREOP ) f2= mathExpression -> ^( COMPAREEXPRESSION $f1 $op $f2) | sAttributeName ASSIGN mathExpression -> ^( ASSIGNMENT sAttributeName mathExpression ) )
            int alt22=3;
            switch ( input.LA(1) ) {
            case BBRACKETLEFT:
                {
                alt22=1;
                }
                break;
            case AVG:
            case BOOLEAN:
            case COUNT:
            case LBRACKET:
            case MAX:
            case MIN:
            case NUMBER:
            case STRING_LITERAL:
            case SUM:
                {
                alt22=2;
                }
                break;
            case NAME:
                {
                int LA22_3 = input.LA(2);

                if ( (LA22_3==BBRACKETLEFT||LA22_3==POINT) ) {
                    alt22=2;
                }
                else if ( (LA22_3==ASSIGN) ) {
                    alt22=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 22, 3, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;

            }

            switch (alt22) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:176:4: BBRACKETLEFT NAME BBRACKETRIGHT
                    {
                    BBRACKETLEFT96=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_expression955); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT96);


                    NAME97=(Token)match(input,NAME,FOLLOW_NAME_in_expression957); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME97);


                    BBRACKETRIGHT98=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_expression959); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT98);


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
                    // 176:36: -> ^( IDEXPRESSION NAME )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:176:39: ^( IDEXPRESSION NAME )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:177:3: f1= mathExpression (op= SINGLEEQUALS |op= COMPAREOP ) f2= mathExpression
                    {
                    pushFollow(FOLLOW_mathExpression_in_expression975);
                    f1=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f1.getTree());

                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:177:21: (op= SINGLEEQUALS |op= COMPAREOP )
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==SINGLEEQUALS) ) {
                        alt21=1;
                    }
                    else if ( (LA21_0==COMPAREOP) ) {
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
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:177:22: op= SINGLEEQUALS
                            {
                            op=(Token)match(input,SINGLEEQUALS,FOLLOW_SINGLEEQUALS_in_expression980); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_SINGLEEQUALS.add(op);


                            }
                            break;
                        case 2 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:177:38: op= COMPAREOP
                            {
                            op=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression984); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COMPAREOP.add(op);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_mathExpression_in_expression989);
                    f2=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(f2.getTree());

                    // AST REWRITE
                    // elements: op, f2, f1
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
                    // 177:70: -> ^( COMPAREEXPRESSION $f1 $op $f2)
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:177:74: ^( COMPAREEXPRESSION $f1 $op $f2)
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:3: sAttributeName ASSIGN mathExpression
                    {
                    pushFollow(FOLLOW_sAttributeName_in_expression1011);
                    sAttributeName99=sAttributeName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sAttributeName.add(sAttributeName99.getTree());

                    ASSIGN100=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_expression1013); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ASSIGN.add(ASSIGN100);


                    pushFollow(FOLLOW_mathExpression_in_expression1015);
                    mathExpression101=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_mathExpression.add(mathExpression101.getTree());

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
                    // 178:40: -> ^( ASSIGNMENT sAttributeName mathExpression )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:178:43: ^( ASSIGNMENT sAttributeName mathExpression )
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:181:1: mathExpression : mult ( ( PLUS ^| MINUS ^) mult )* ;
    public final SaseParser.mathExpression_return mathExpression() throws RecognitionException {
        SaseParser.mathExpression_return retval = new SaseParser.mathExpression_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PLUS103=null;
        Token MINUS104=null;
        SaseParser.mult_return mult102 =null;

        SaseParser.mult_return mult105 =null;


        CommonTree PLUS103_tree=null;
        CommonTree MINUS104_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:181:15: ( mult ( ( PLUS ^| MINUS ^) mult )* )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:181:17: mult ( ( PLUS ^| MINUS ^) mult )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_mult_in_mathExpression1035);
            mult102=mult();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mult102.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:181:22: ( ( PLUS ^| MINUS ^) mult )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==MINUS||LA24_0==PLUS) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:181:23: ( PLUS ^| MINUS ^) mult
            	    {
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:181:23: ( PLUS ^| MINUS ^)
            	    int alt23=2;
            	    int LA23_0 = input.LA(1);

            	    if ( (LA23_0==PLUS) ) {
            	        alt23=1;
            	    }
            	    else if ( (LA23_0==MINUS) ) {
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
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:181:24: PLUS ^
            	            {
            	            PLUS103=(Token)match(input,PLUS,FOLLOW_PLUS_in_mathExpression1039); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            PLUS103_tree = 
            	            (CommonTree)adaptor.create(PLUS103)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(PLUS103_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:181:30: MINUS ^
            	            {
            	            MINUS104=(Token)match(input,MINUS,FOLLOW_MINUS_in_mathExpression1042); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MINUS104_tree = 
            	            (CommonTree)adaptor.create(MINUS104)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(MINUS104_tree, root_0);
            	            }

            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_mult_in_mathExpression1046);
            	    mult105=mult();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, mult105.getTree());

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:1: mult : term ( ( MULT ^| DIVISION ^) term )* ;
    public final SaseParser.mult_return mult() throws RecognitionException {
        SaseParser.mult_return retval = new SaseParser.mult_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token MULT107=null;
        Token DIVISION108=null;
        SaseParser.term_return term106 =null;

        SaseParser.term_return term109 =null;


        CommonTree MULT107_tree=null;
        CommonTree DIVISION108_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:7: ( term ( ( MULT ^| DIVISION ^) term )* )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:9: term ( ( MULT ^| DIVISION ^) term )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_term_in_mult1060);
            term106=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, term106.getTree());

            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:14: ( ( MULT ^| DIVISION ^) term )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==DIVISION||LA26_0==MULT) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:15: ( MULT ^| DIVISION ^) term
            	    {
            	    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:15: ( MULT ^| DIVISION ^)
            	    int alt25=2;
            	    int LA25_0 = input.LA(1);

            	    if ( (LA25_0==MULT) ) {
            	        alt25=1;
            	    }
            	    else if ( (LA25_0==DIVISION) ) {
            	        alt25=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 25, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt25) {
            	        case 1 :
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:16: MULT ^
            	            {
            	            MULT107=(Token)match(input,MULT,FOLLOW_MULT_in_mult1064); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            MULT107_tree = 
            	            (CommonTree)adaptor.create(MULT107)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(MULT107_tree, root_0);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:184:22: DIVISION ^
            	            {
            	            DIVISION108=(Token)match(input,DIVISION,FOLLOW_DIVISION_in_mult1067); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            DIVISION108_tree = 
            	            (CommonTree)adaptor.create(DIVISION108)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(DIVISION108_tree, root_0);
            	            }

            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_term_in_mult1071);
            	    term109=term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, term109.getTree());

            	    }
            	    break;

            	default :
            	    break loop26;
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:188:1: term : ( attributeTerm | value | LBRACKET ! mathExpression RBRACKET !);
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LBRACKET112=null;
        Token RBRACKET114=null;
        SaseParser.attributeTerm_return attributeTerm110 =null;

        SaseParser.value_return value111 =null;

        SaseParser.mathExpression_return mathExpression113 =null;


        CommonTree LBRACKET112_tree=null;
        CommonTree RBRACKET114_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:188:5: ( attributeTerm | value | LBRACKET ! mathExpression RBRACKET !)
            int alt27=3;
            switch ( input.LA(1) ) {
            case AVG:
            case COUNT:
            case MAX:
            case MIN:
            case NAME:
            case SUM:
                {
                alt27=1;
                }
                break;
            case BOOLEAN:
            case NUMBER:
            case STRING_LITERAL:
                {
                alt27=2;
                }
                break;
            case LBRACKET:
                {
                alt27=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;

            }

            switch (alt27) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:188:7: attributeTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_attributeTerm_in_term1084);
                    attributeTerm110=attributeTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, attributeTerm110.getTree());

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:189:3: value
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_value_in_term1090);
                    value111=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value111.getTree());

                    }
                    break;
                case 3 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:190:3: LBRACKET ! mathExpression RBRACKET !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    LBRACKET112=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_term1095); if (state.failed) return retval;

                    pushFollow(FOLLOW_mathExpression_in_term1098);
                    mathExpression113=mathExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, mathExpression113.getTree());

                    RBRACKET114=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_term1100); if (state.failed) return retval;

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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:193:1: attributeTerm : ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) |aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) );
    public final SaseParser.attributeTerm_return attributeTerm() throws RecognitionException {
        SaseParser.attributeTerm_return retval = new SaseParser.attributeTerm_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token aName=null;
        Token member=null;
        Token POINT117=null;
        Token NAME118=null;
        Token POINT119=null;
        SaseParser.aggregation_return aggregation115 =null;

        SaseParser.kAttributeUsage_return kAttributeUsage116 =null;


        CommonTree aName_tree=null;
        CommonTree member_tree=null;
        CommonTree POINT117_tree=null;
        CommonTree NAME118_tree=null;
        CommonTree POINT119_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_kAttributeUsage=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeUsage");
        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:193:14: ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) |aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) )
            int alt28=3;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==AVG||LA28_0==COUNT||LA28_0==MAX||LA28_0==MIN||LA28_0==SUM) ) {
                alt28=1;
            }
            else if ( (LA28_0==NAME) ) {
                int LA28_2 = input.LA(2);

                if ( (LA28_2==POINT) ) {
                    alt28=3;
                }
                else if ( (LA28_2==BBRACKETLEFT) ) {
                    alt28=2;
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
                    new NoViableAltException("", 28, 0, input);

                throw nvae;

            }
            switch (alt28) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:193:16: aggregation
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_aggregation_in_attributeTerm1112);
                    aggregation115=aggregation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregation115.getTree());

                    }
                    break;
                case 2 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:194:5: kAttributeUsage POINT NAME
                    {
                    pushFollow(FOLLOW_kAttributeUsage_in_attributeTerm1120);
                    kAttributeUsage116=kAttributeUsage();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_kAttributeUsage.add(kAttributeUsage116.getTree());

                    POINT117=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1122); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT117);


                    NAME118=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1124); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(NAME118);


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
                    // 194:32: -> ^( KMEMBER kAttributeUsage NAME )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:194:35: ^( KMEMBER kAttributeUsage NAME )
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:195:5: aName= NAME POINT member= NAME
                    {
                    aName=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1143); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(aName);


                    POINT119=(Token)match(input,POINT,FOLLOW_POINT_in_attributeTerm1145); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT119);


                    member=(Token)match(input,NAME,FOLLOW_NAME_in_attributeTerm1149); if (state.failed) return retval; 
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
                    // 195:34: -> ^( MEMBER $aName $member)
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:195:37: ^( MEMBER $aName $member)
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:198:1: aggregation : ( aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION aggop $var $member) | aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION aggop $var ( $member)? ) );
    public final SaseParser.aggregation_return aggregation() throws RecognitionException {
        SaseParser.aggregation_return retval = new SaseParser.aggregation_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token var=null;
        Token name=null;
        Token number=null;
        Token member=null;
        Token LBRACKET121=null;
        Token BBRACKETLEFT122=null;
        Token POINT123=null;
        Token POINT124=null;
        Token MINUS125=null;
        Token BBRACKETRIGHT126=null;
        Token POINT127=null;
        Token RBRACKET128=null;
        Token LBRACKET130=null;
        Token BBRACKETLEFT131=null;
        Token BBRACKETRIGHT132=null;
        Token POINT133=null;
        Token RBRACKET134=null;
        SaseParser.aggop_return aggop120 =null;

        SaseParser.aggop_return aggop129 =null;


        CommonTree var_tree=null;
        CommonTree name_tree=null;
        CommonTree number_tree=null;
        CommonTree member_tree=null;
        CommonTree LBRACKET121_tree=null;
        CommonTree BBRACKETLEFT122_tree=null;
        CommonTree POINT123_tree=null;
        CommonTree POINT124_tree=null;
        CommonTree MINUS125_tree=null;
        CommonTree BBRACKETRIGHT126_tree=null;
        CommonTree POINT127_tree=null;
        CommonTree RBRACKET128_tree=null;
        CommonTree LBRACKET130_tree=null;
        CommonTree BBRACKETLEFT131_tree=null;
        CommonTree BBRACKETRIGHT132_tree=null;
        CommonTree POINT133_tree=null;
        CommonTree RBRACKET134_tree=null;
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
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:199:2: ( aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}? -> ^( AGGREGATION aggop $var $member) | aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET -> ^( AGGREGATION aggop $var ( $member)? ) )
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==AVG||LA30_0==COUNT||LA30_0==MAX||LA30_0==MIN||LA30_0==SUM) ) {
                int LA30_1 = input.LA(2);

                if ( (LA30_1==LBRACKET) ) {
                    int LA30_2 = input.LA(3);

                    if ( (LA30_2==NAME) ) {
                        int LA30_3 = input.LA(4);

                        if ( (LA30_3==BBRACKETLEFT) ) {
                            int LA30_4 = input.LA(5);

                            if ( (LA30_4==POINT) ) {
                                alt30=1;
                            }
                            else if ( (LA30_4==BBRACKETRIGHT) ) {
                                alt30=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 30, 4, input);

                                throw nvae;

                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 30, 3, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 30, 2, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 30, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;

            }
            switch (alt30) {
                case 1 :
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:199:5: aggop LBRACKET var= NAME BBRACKETLEFT POINT POINT name= NAME MINUS number= NUMBER BBRACKETRIGHT POINT member= NAME RBRACKET {...}?
                    {
                    pushFollow(FOLLOW_aggop_in_aggregation1174);
                    aggop120=aggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggop.add(aggop120.getTree());

                    LBRACKET121=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1176); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET121);


                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1180); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);


                    BBRACKETLEFT122=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1182); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT122);


                    POINT123=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1184); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT123);


                    POINT124=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1186); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT124);


                    name=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1190); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(name);


                    MINUS125=(Token)match(input,MINUS,FOLLOW_MINUS_in_aggregation1192); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MINUS.add(MINUS125);


                    number=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_aggregation1196); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(number);


                    BBRACKETRIGHT126=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1198); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT126);


                    POINT127=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1200); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_POINT.add(POINT127);


                    member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1204); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(member);


                    RBRACKET128=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1206); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET128);


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
                    // 200:4: -> ^( AGGREGATION aggop $var $member)
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:200:7: ^( AGGREGATION aggop $var $member)
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
                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:201:5: aggop LBRACKET var= NAME BBRACKETLEFT BBRACKETRIGHT ( POINT member= NAME )? RBRACKET
                    {
                    pushFollow(FOLLOW_aggop_in_aggregation1232);
                    aggop129=aggop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_aggop.add(aggop129.getTree());

                    LBRACKET130=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation1234); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACKET.add(LBRACKET130);


                    var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1238); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NAME.add(var);


                    BBRACKETLEFT131=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_aggregation1240); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETLEFT.add(BBRACKETLEFT131);


                    BBRACKETRIGHT132=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_aggregation1242); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BBRACKETRIGHT.add(BBRACKETRIGHT132);


                    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:201:56: ( POINT member= NAME )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==POINT) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:201:57: POINT member= NAME
                            {
                            POINT133=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation1245); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_POINT.add(POINT133);


                            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation1249); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NAME.add(member);


                            }
                            break;

                    }


                    RBRACKET134=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation1253); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACKET.add(RBRACKET134);


                    // AST REWRITE
                    // elements: var, member, aggop
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
                    // 202:4: -> ^( AGGREGATION aggop $var ( $member)? )
                    {
                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:202:7: ^( AGGREGATION aggop $var ( $member)? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(AGGREGATION, "AGGREGATION")
                        , root_1);

                        adaptor.addChild(root_1, stream_aggop.nextTree());

                        adaptor.addChild(root_1, stream_var.nextNode());

                        // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:202:33: ( $member)?
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:205:1: aggop : ( MIN | MAX | SUM | COUNT | AVG );
    public final SaseParser.aggop_return aggop() throws RecognitionException {
        SaseParser.aggop_return retval = new SaseParser.aggop_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set135=null;

        CommonTree set135_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:206:2: ( MIN | MAX | SUM | COUNT | AVG )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set135=(Token)input.LT(1);

            if ( input.LA(1)==AVG||input.LA(1)==COUNT||input.LA(1)==MAX||input.LA(1)==MIN||input.LA(1)==SUM ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set135)
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
    // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:209:1: value : ( NUMBER | BOOLEAN | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set136=null;

        CommonTree set136_tree=null;

        try {
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:209:8: ( NUMBER | BOOLEAN | STRING_LITERAL )
            // E:\\odysseus\\cep\\cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set136=(Token)input.LT(1);

            if ( input.LA(1)==BOOLEAN||input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set136)
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


 

    public static final BitSet FOLLOW_createStmt_in_start158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_queryStmt_in_start164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_createStmt174 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_VIEW_in_createStmt176 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_createStmt178 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_queryStmt_in_createStmt180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_patternPart_in_queryStmt200 = new BitSet(new long[]{0x8000800000100000L,0x0000000000000002L});
    public static final BitSet FOLLOW_wherePart_in_queryStmt202 = new BitSet(new long[]{0x0000800000100000L,0x0000000000000002L});
    public static final BitSet FOLLOW_withinPart_in_queryStmt205 = new BitSet(new long[]{0x0000800000100000L});
    public static final BitSet FOLLOW_endsAtPart_in_queryStmt208 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_returnPart_in_queryStmt211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart245 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart247 = new BitSet(new long[]{0x4002000901020002L});
    public static final BitSet FOLLOW_timeunit_in_withinPart249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ENDS_in_endsAtPart274 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_AT_in_endsAtPart276 = new BitSet(new long[]{0x0400002280008200L});
    public static final BitSet FOLLOW_attributeTerm_in_endsAtPart278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart317 = new BitSet(new long[]{0x0130040000000000L});
    public static final BitSet FOLLOW_skipPart_in_wherePart319 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart321 = new BitSet(new long[]{0x0600022284009600L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart323 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart325 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart342 = new BitSet(new long[]{0x0600022284009600L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_skipMethod_in_skipPart363 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LBRACKET_in_skipPart365 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_parameterList_in_skipPart367 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_skipPart369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart418 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart420 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnPart443 = new BitSet(new long[]{0x0400002280008200L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart445 = new BitSet(new long[]{0x0000000000002042L});
    public static final BitSet FOLLOW_COMMA_in_returnPart448 = new BitSet(new long[]{0x0400002280008200L});
    public static final BitSet FOLLOW_attributeTerm_in_returnPart450 = new BitSet(new long[]{0x0000000000002042L});
    public static final BitSet FOLLOW_AS_in_returnPart455 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_returnPart457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_seqPatternDecl_in_patternDecl483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEQ_in_seqPatternDecl495 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LBRACKET_in_seqPatternDecl497 = new BitSet(new long[]{0x0000012004000000L});
    public static final BitSet FOLLOW_stateDef_in_seqPatternDecl499 = new BitSet(new long[]{0x0000400000002000L});
    public static final BitSet FOLLOW_COMMA_in_seqPatternDecl502 = new BitSet(new long[]{0x0000012004000000L});
    public static final BitSet FOLLOW_stateDef_in_seqPatternDecl504 = new BitSet(new long[]{0x0000400000002000L});
    public static final BitSet FOLLOW_RBRACKET_in_seqPatternDecl508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef532 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef555 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef580 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef584 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_ktypeDefinition_in_stateDef586 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTSIGN_in_stateDef606 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LBRACKET_in_stateDef610 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_typeDefinition_in_stateDef612 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_stateDef614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeDefinition639 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_sAttributeName_in_typeDefinition641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_ktypeDefinition652 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_PLUS_in_ktypeDefinition654 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_kAttributeName_in_ktypeDefinition656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList670 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList673 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList675 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName713 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName735 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName738 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage764 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_current_in_kAttributeUsage767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage781 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_first_in_kAttributeUsage783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage796 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_last_in_kAttributeUsage798 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage811 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_len_in_kAttributeUsage813 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_current834 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_current838 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_current840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_first855 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_NUMBER_in_first859 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_first861 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_last874 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_last878 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_MINUS_in_last880 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_NUMBER_in_last884 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_last886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_len902 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_len904 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_POINT_in_len906 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LEN_in_len908 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_len910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions923 = new BitSet(new long[]{0x0000000000002022L});
    public static final BitSet FOLLOW_AND_in_whereExpressions927 = new BitSet(new long[]{0x0600022284009600L});
    public static final BitSet FOLLOW_COMMA_in_whereExpressions929 = new BitSet(new long[]{0x0600022284009600L});
    public static final BitSet FOLLOW_expression_in_whereExpressions932 = new BitSet(new long[]{0x0000000000002022L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_expression955 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_expression957 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_expression959 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mathExpression_in_expression975 = new BitSet(new long[]{0x0008000000004000L});
    public static final BitSet FOLLOW_SINGLEEQUALS_in_expression980 = new BitSet(new long[]{0x0600022284009200L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression984 = new BitSet(new long[]{0x0600022284009200L});
    public static final BitSet FOLLOW_mathExpression_in_expression989 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_expression1011 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ASSIGN_in_expression1013 = new BitSet(new long[]{0x0600022284009200L});
    public static final BitSet FOLLOW_mathExpression_in_expression1015 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mult_in_mathExpression1035 = new BitSet(new long[]{0x0000100400000002L});
    public static final BitSet FOLLOW_PLUS_in_mathExpression1039 = new BitSet(new long[]{0x0600022284009200L});
    public static final BitSet FOLLOW_MINUS_in_mathExpression1042 = new BitSet(new long[]{0x0600022284009200L});
    public static final BitSet FOLLOW_mult_in_mathExpression1046 = new BitSet(new long[]{0x0000100400000002L});
    public static final BitSet FOLLOW_term_in_mult1060 = new BitSet(new long[]{0x0000001000080002L});
    public static final BitSet FOLLOW_MULT_in_mult1064 = new BitSet(new long[]{0x0600022284009200L});
    public static final BitSet FOLLOW_DIVISION_in_mult1067 = new BitSet(new long[]{0x0600022284009200L});
    public static final BitSet FOLLOW_term_in_mult1071 = new BitSet(new long[]{0x0000001000080002L});
    public static final BitSet FOLLOW_attributeTerm_in_term1084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term1090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_term1095 = new BitSet(new long[]{0x0600022284009200L});
    public static final BitSet FOLLOW_mathExpression_in_term1098 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_term1100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregation_in_attributeTerm1112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_attributeTerm1120 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1122 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1143 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_POINT_in_attributeTerm1145 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_attributeTerm1149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggop_in_aggregation1174 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1176 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1180 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1182 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1184 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1186 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1190 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_MINUS_in_aggregation1192 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_NUMBER_in_aggregation1196 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1198 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1200 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1204 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggop_in_aggregation1232 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation1234 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1238 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_aggregation1240 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_aggregation1242 = new BitSet(new long[]{0x0000600000000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation1245 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation1249 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation1253 = new BitSet(new long[]{0x0000000000000002L});

}