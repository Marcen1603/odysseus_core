// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2009-12-07 16:54:41
 
	package de.uniol.inf.is.odysseus.cep.sase; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class SaseParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "HAVING", "PATTERN", "WHERE", "WITHIN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "FIRST", "CURRENT", "PREVIOUS", "ALLTOPREVIOUS", "NAME", "LAST", "BBRACKETLEFT", "BBRACKETRIGHT", "TIMEUNIT", "SKIP_METHOD", "AGGREGATEOP", "PLUS", "MINUS", "POINT", "MULT", "COMPAREOP", "SINGLEEQUALS", "EQUALS", "NOT", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "STATE", "KTYPE", "TYPE", "WHERESTRAT", "WHEREEXPRESSION", "EXPRESSION", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "KMEMBER", "MEMBER", "COMPAREEXPRESSION", "IDEXPRESSION", "AGGREGATION"
    };
    public static final int WHERE=6;
    public static final int MEMBER=56;
    public static final int POINT=25;
    public static final int LETTER=38;
    public static final int ATTRIBUTE=52;
    public static final int FLOAT=35;
    public static final int EQUALS=29;
    public static final int NOT=30;
    public static final int AND=11;
    public static final int SPACE=41;
    public static final int EOF=-1;
    public static final int TYPE=48;
    public static final int LBRACKET=32;
    public static final int PATTERN=5;
    public static final int SINGLEEQUALS=28;
    public static final int COMPAREEXPRESSION=57;
    public static final int NAME=16;
    public static final int STRING_LITERAL=40;
    public static final int LEFTCURLY=9;
    public static final int COMMA=31;
    public static final int SEQ=8;
    public static final int PREVIOUS=14;
    public static final int KATTRIBUTE=53;
    public static final int RIGHTCURLY=10;
    public static final int PLUS=23;
    public static final int DIGIT=37;
    public static final int LAST=17;
    public static final int RBRACKET=33;
    public static final int EXPRESSION=51;
    public static final int BBRACKETRIGHT=19;
    public static final int INTEGER=34;
    public static final int STATE=46;
    public static final int IDEXPRESSION=58;
    public static final int KMEMBER=55;
    public static final int ALLTOPREVIOUS=15;
    public static final int TIMEUNIT=20;
    public static final int SKIP_METHOD=21;
    public static final int NUMBER=36;
    public static final int PARAMLIST=54;
    public static final int WHITESPACE=45;
    public static final int HAVING=4;
    public static final int MINUS=24;
    public static final int MULT=26;
    public static final int AGGREGATEOP=22;
    public static final int CURRENT=13;
    public static final int WHEREEXPRESSION=50;
    public static final int NEWLINE=44;
    public static final int NONCONTROL_CHAR=39;
    public static final int AGGREGATION=59;
    public static final int BBRACKETLEFT=18;
    public static final int WHERESTRAT=49;
    public static final int WITHIN=7;
    public static final int KTYPE=47;
    public static final int LOWER=42;
    public static final int COMPAREOP=27;
    public static final int UPPER=43;
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:30:1: query : patternPart ( wherePart )? ( withinPart )? ;
    public final SaseParser.query_return query() throws RecognitionException {
        SaseParser.query_return retval = new SaseParser.query_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.patternPart_return patternPart1 = null;

        SaseParser.wherePart_return wherePart2 = null;

        SaseParser.withinPart_return withinPart3 = null;



        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:30:7: ( patternPart ( wherePart )? ( withinPart )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:30:10: patternPart ( wherePart )? ( withinPart )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_patternPart_in_query104);
            patternPart1=patternPart();

            state._fsp--;

            adaptor.addChild(root_0, patternPart1.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:30:22: ( wherePart )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==WHERE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:30:23: wherePart
                    {
                    pushFollow(FOLLOW_wherePart_in_query107);
                    wherePart2=wherePart();

                    state._fsp--;

                    adaptor.addChild(root_0, wherePart2.getTree());

                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:30:35: ( withinPart )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==WITHIN) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:30:36: withinPart
                    {
                    pushFollow(FOLLOW_withinPart_in_query112);
                    withinPart3=withinPart();

                    state._fsp--;

                    adaptor.addChild(root_0, withinPart3.getTree());

                    }
                    break;

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
        return retval;
    }
    // $ANTLR end "query"

    public static class withinPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "withinPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:1: withinPart : WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:34:2: ( WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:34:4: WITHIN NUMBER TIMEUNIT
            {
            WITHIN4=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart127);  
            stream_WITHIN.add(WITHIN4);

            NUMBER5=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart129);  
            stream_NUMBER.add(NUMBER5);

            TIMEUNIT6=(Token)match(input,TIMEUNIT,FOLLOW_TIMEUNIT_in_withinPart131);  
            stream_TIMEUNIT.add(TIMEUNIT6);



            // AST REWRITE
            // elements: TIMEUNIT, WITHIN, NUMBER
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 34:27: -> ^( WITHIN NUMBER TIMEUNIT )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:34:30: ^( WITHIN NUMBER TIMEUNIT )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_WITHIN.nextNode(), root_1);

                adaptor.addChild(root_1, stream_NUMBER.nextNode());
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
        return retval;
    }
    // $ANTLR end "withinPart"

    public static class wherePart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "wherePart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:1: wherePart : ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:38:2: ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==WHERE) ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1==NAME||LA3_1==AGGREGATEOP||LA3_1==NUMBER||LA3_1==STRING_LITERAL) ) {
                    alt3=2;
                }
                else if ( (LA3_1==SKIP_METHOD) ) {
                    alt3=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:38:4: WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    WHERE7=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart153);  
                    stream_WHERE.add(WHERE7);

                    pushFollow(FOLLOW_wherePart1_in_wherePart155);
                    wherePart18=wherePart1();

                    state._fsp--;

                    stream_wherePart1.add(wherePart18.getTree());
                    LEFTCURLY9=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart157);  
                    stream_LEFTCURLY.add(LEFTCURLY9);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart159);
                    whereExpressions10=whereExpressions();

                    state._fsp--;

                    stream_whereExpressions.add(whereExpressions10.getTree());
                    RIGHTCURLY11=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart161);  
                    stream_RIGHTCURLY.add(RIGHTCURLY11);



                    // AST REWRITE
                    // elements: WHERE, whereExpressions, wherePart1
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 38:59: -> ^( WHERE wherePart1 whereExpressions )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:38:62: ^( WHERE wherePart1 whereExpressions )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_wherePart1.nextTree());
                        adaptor.addChild(root_1, stream_whereExpressions.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:39:4: WHERE whereExpressions
                    {
                    WHERE12=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart178);  
                    stream_WHERE.add(WHERE12);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart180);
                    whereExpressions13=whereExpressions();

                    state._fsp--;

                    stream_whereExpressions.add(whereExpressions13.getTree());


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
                    // 39:27: -> ^( WHERE whereExpressions )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:39:30: ^( WHERE whereExpressions )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_WHERE.nextNode(), root_1);

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
        return retval;
    }
    // $ANTLR end "wherePart"

    public static class patternPart_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternPart"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:42:1: patternPart : PATTERN patternDecl -> ^( PATTERN patternDecl ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:43:2: ( PATTERN patternDecl -> ^( PATTERN patternDecl ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:43:4: PATTERN patternDecl
            {
            PATTERN14=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart201);  
            stream_PATTERN.add(PATTERN14);

            pushFollow(FOLLOW_patternDecl_in_patternPart203);
            patternDecl15=patternDecl();

            state._fsp--;

            stream_patternDecl.add(patternDecl15.getTree());


            // AST REWRITE
            // elements: patternDecl, PATTERN
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 43:24: -> ^( PATTERN patternDecl )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:43:27: ^( PATTERN patternDecl )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_PATTERN.nextNode(), root_1);

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
        return retval;
    }
    // $ANTLR end "patternPart"

    public static class patternDecl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternDecl"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:1: patternDecl : SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:48:2: ( SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:48:4: SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET
            {
            SEQ16=(Token)match(input,SEQ,FOLLOW_SEQ_in_patternDecl225);  
            stream_SEQ.add(SEQ16);

            LBRACKET17=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_patternDecl227);  
            stream_LBRACKET.add(LBRACKET17);

            pushFollow(FOLLOW_pItem_in_patternDecl229);
            pItem18=pItem();

            state._fsp--;

            stream_pItem.add(pItem18.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:48:23: ( COMMA pItem )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==COMMA) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:48:24: COMMA pItem
            	    {
            	    COMMA19=(Token)match(input,COMMA,FOLLOW_COMMA_in_patternDecl232);  
            	    stream_COMMA.add(COMMA19);

            	    pushFollow(FOLLOW_pItem_in_patternDecl234);
            	    pItem20=pItem();

            	    state._fsp--;

            	    stream_pItem.add(pItem20.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            RBRACKET21=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_patternDecl238);  
            stream_RBRACKET.add(RBRACKET21);



            // AST REWRITE
            // elements: SEQ, pItem
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 48:47: -> ^( SEQ ( pItem )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:48:50: ^( SEQ ( pItem )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_SEQ.nextNode(), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:48:56: ( pItem )*
                while ( stream_pItem.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "patternDecl"

    public static class pItem_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pItem"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:1: pItem : ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:8: ( ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:10: ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )?
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:10: ( NOT )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==NOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:11: NOT
                    {
                    NOT22=(Token)match(input,NOT,FOLLOW_NOT_in_pItem263);  
                    stream_NOT.add(NOT22);


                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:17: ( LBRACKET )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==LBRACKET) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:17: LBRACKET
                    {
                    LBRACKET23=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_pItem267);  
                    stream_LBRACKET.add(LBRACKET23);


                    }
                    break;

            }

            pushFollow(FOLLOW_typeName_in_pItem273);
            type=typeName();

            state._fsp--;

            stream_typeName.add(type.getTree());
            pushFollow(FOLLOW_attributeName_in_pItem277);
            variable=attributeName();

            state._fsp--;

            stream_attributeName.add(variable.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:65: ( RBRACKET )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==RBRACKET) ) {
                int LA7_1 = input.LA(2);

                if ( (LA7_1==COMMA||LA7_1==RBRACKET) ) {
                    alt7=1;
                }
            }
            switch (alt7) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:65: RBRACKET
                    {
                    RBRACKET24=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_pItem279);  
                    stream_RBRACKET.add(RBRACKET24);


                    }
                    break;

            }



            // AST REWRITE
            // elements: type, variable, NOT
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
            // 52:75: -> ^( STATE $type $variable ( NOT )? )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:78: ^( STATE $type $variable ( NOT )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STATE, "STATE"), root_1);

                adaptor.addChild(root_1, stream_type.nextTree());
                adaptor.addChild(root_1, stream_variable.nextTree());
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:52:102: ( NOT )?
                if ( stream_NOT.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "pItem"

    public static class typeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "typeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:55:1: typeName : NAME (op= PLUS )? -> {$op != null}? ^( KTYPE NAME $op) -> ^( TYPE NAME ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:55:9: ( NAME (op= PLUS )? -> {$op != null}? ^( KTYPE NAME $op) -> ^( TYPE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:55:11: NAME (op= PLUS )?
            {
            NAME25=(Token)match(input,NAME,FOLLOW_NAME_in_typeName306);  
            stream_NAME.add(NAME25);

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:55:18: (op= PLUS )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==PLUS) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:55:18: op= PLUS
                    {
                    op=(Token)match(input,PLUS,FOLLOW_PLUS_in_typeName310);  
                    stream_PLUS.add(op);


                    }
                    break;

            }



            // AST REWRITE
            // elements: op, NAME, NAME
            // token labels: op
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 55:25: -> {$op != null}? ^( KTYPE NAME $op)
            if (op != null) {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:55:43: ^( KTYPE NAME $op)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KTYPE, "KTYPE"), root_1);

                adaptor.addChild(root_1, stream_NAME.nextNode());
                adaptor.addChild(root_1, stream_op.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 56:9: -> ^( TYPE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:56:12: ^( TYPE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TYPE, "TYPE"), root_1);

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
        return retval;
    }
    // $ANTLR end "typeName"

    public static class wherePart1_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "wherePart1"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:59:1: wherePart1 : SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:2: ( SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:4: SKIP_METHOD LBRACKET parameterList RBRACKET
            {
            SKIP_METHOD26=(Token)match(input,SKIP_METHOD,FOLLOW_SKIP_METHOD_in_wherePart1353);  
            stream_SKIP_METHOD.add(SKIP_METHOD26);

            LBRACKET27=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_wherePart1355);  
            stream_LBRACKET.add(LBRACKET27);

            pushFollow(FOLLOW_parameterList_in_wherePart1357);
            parameterList28=parameterList();

            state._fsp--;

            stream_parameterList.add(parameterList28.getTree());
            RBRACKET29=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_wherePart1359);  
            stream_RBRACKET.add(RBRACKET29);



            // AST REWRITE
            // elements: SKIP_METHOD, parameterList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 60:48: -> ^( WHERESTRAT SKIP_METHOD parameterList )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:60:51: ^( WHERESTRAT SKIP_METHOD parameterList )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHERESTRAT, "WHERESTRAT"), root_1);

                adaptor.addChild(root_1, stream_SKIP_METHOD.nextNode());
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
        return retval;
    }
    // $ANTLR end "wherePart1"

    public static class parameterList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parameterList"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:63:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:4: attributeName ( COMMA attributeName )*
            {
            pushFollow(FOLLOW_attributeName_in_parameterList382);
            attributeName30=attributeName();

            state._fsp--;

            stream_attributeName.add(attributeName30.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:17: ( COMMA attributeName )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==COMMA) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:18: COMMA attributeName
            	    {
            	    COMMA31=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList384);  
            	    stream_COMMA.add(COMMA31);

            	    pushFollow(FOLLOW_attributeName_in_parameterList386);
            	    attributeName32=attributeName();

            	    state._fsp--;

            	    stream_attributeName.add(attributeName32.getTree());

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
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 64:40: -> ^( PARAMLIST ( attributeName )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:43: ^( PARAMLIST ( attributeName )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PARAMLIST, "PARAMLIST"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:64:55: ( attributeName )*
                while ( stream_attributeName.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "parameterList"

    public static class attributeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "attributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:67:1: attributeName : ( kAttributeName | sAttributeName );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.kAttributeName_return kAttributeName33 = null;

        SaseParser.sAttributeName_return sAttributeName34 = null;



        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:68:2: ( kAttributeName | sAttributeName )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==NAME) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==BBRACKETLEFT) ) {
                    alt10=1;
                }
                else if ( (LA10_1==COMMA||LA10_1==RBRACKET) ) {
                    alt10=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:68:5: kAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_kAttributeName_in_attributeName410);
                    kAttributeName33=kAttributeName();

                    state._fsp--;

                    adaptor.addChild(root_0, kAttributeName33.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:68:20: sAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_sAttributeName_in_attributeName412);
                    sAttributeName34=sAttributeName();

                    state._fsp--;

                    adaptor.addChild(root_0, sAttributeName34.getTree());

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
        return retval;
    }
    // $ANTLR end "attributeName"

    public static class kAttributeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kAttributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:71:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:2: ( NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            NAME35=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName424);  
            stream_NAME.add(NAME35);

            BBRACKETLEFT36=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName427);  
            stream_BBRACKETLEFT.add(BBRACKETLEFT36);

            BBRACKETRIGHT37=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName429);  
            stream_BBRACKETRIGHT.add(BBRACKETRIGHT37);



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
            // 72:38: -> ^( KATTRIBUTE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:72:41: ^( KATTRIBUTE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KATTRIBUTE, "KATTRIBUTE"), root_1);

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
        return retval;
    }
    // $ANTLR end "kAttributeName"

    public static class sAttributeName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sAttributeName"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:76:1: sAttributeName : NAME -> ^( ATTRIBUTE NAME ) ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME38=null;

        Object NAME38_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:2: ( NAME -> ^( ATTRIBUTE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:4: NAME
            {
            NAME38=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName452);  
            stream_NAME.add(NAME38);



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
            // 77:9: -> ^( ATTRIBUTE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:12: ^( ATTRIBUTE NAME )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ATTRIBUTE, "ATTRIBUTE"), root_1);

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
        return retval;
    }
    // $ANTLR end "sAttributeName"

    public static class kAttributeUsage_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kAttributeUsage"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:1: kAttributeUsage : ( NAME CURRENT | NAME FIRST | NAME PREVIOUS | NAME LAST );
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:80:2: ( NAME CURRENT | NAME FIRST | NAME PREVIOUS | NAME LAST )
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
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:80:5: NAME CURRENT
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME39=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage471); 
                    NAME39_tree = (Object)adaptor.create(NAME39);
                    adaptor.addChild(root_0, NAME39_tree);

                    CURRENT40=(Token)match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage473); 
                    CURRENT40_tree = (Object)adaptor.create(CURRENT40);
                    adaptor.addChild(root_0, CURRENT40_tree);


                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:81:3: NAME FIRST
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME41=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage479); 
                    NAME41_tree = (Object)adaptor.create(NAME41);
                    adaptor.addChild(root_0, NAME41_tree);

                    FIRST42=(Token)match(input,FIRST,FOLLOW_FIRST_in_kAttributeUsage481); 
                    FIRST42_tree = (Object)adaptor.create(FIRST42);
                    adaptor.addChild(root_0, FIRST42_tree);


                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:82:3: NAME PREVIOUS
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME43=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage486); 
                    NAME43_tree = (Object)adaptor.create(NAME43);
                    adaptor.addChild(root_0, NAME43_tree);

                    PREVIOUS44=(Token)match(input,PREVIOUS,FOLLOW_PREVIOUS_in_kAttributeUsage488); 
                    PREVIOUS44_tree = (Object)adaptor.create(PREVIOUS44);
                    adaptor.addChild(root_0, PREVIOUS44_tree);


                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:3: NAME LAST
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME45=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage493); 
                    NAME45_tree = (Object)adaptor.create(NAME45);
                    adaptor.addChild(root_0, NAME45_tree);

                    LAST46=(Token)match(input,LAST,FOLLOW_LAST_in_kAttributeUsage495); 
                    LAST46_tree = (Object)adaptor.create(LAST46);
                    adaptor.addChild(root_0, LAST46_tree);


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
        return retval;
    }
    // $ANTLR end "kAttributeUsage"

    public static class whereExpressions_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "whereExpressions"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:86:1: whereExpressions : expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:2: ( expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:4: expression ( AND expression )*
            {
            pushFollow(FOLLOW_expression_in_whereExpressions507);
            expression47=expression();

            state._fsp--;

            stream_expression.add(expression47.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:15: ( AND expression )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==AND) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:16: AND expression
            	    {
            	    AND48=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions510);  
            	    stream_AND.add(AND48);

            	    pushFollow(FOLLOW_expression_in_whereExpressions512);
            	    expression49=expression();

            	    state._fsp--;

            	    stream_expression.add(expression49.getTree());

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
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 87:33: -> ^( WHEREEXPRESSION ( AND )? ( expression )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:36: ^( WHEREEXPRESSION ( AND )? ( expression )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:54: ( AND )?
                if ( stream_AND.hasNext() ) {
                    adaptor.addChild(root_1, stream_AND.nextNode());

                }
                stream_AND.reset();
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:59: ( expression )*
                while ( stream_expression.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "whereExpressions"

    public static class expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:90:1: expression : ( NAME -> ^( IDEXPRESSION NAME ) | f1= term SINGLEEQUALS f2= term -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= term COMPAREOP f2= term -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME50=null;
        Token SINGLEEQUALS51=null;
        Token COMPAREOP52=null;
        SaseParser.term_return f1 = null;

        SaseParser.term_return f2 = null;


        Object NAME50_tree=null;
        Object SINGLEEQUALS51_tree=null;
        Object COMPAREOP52_tree=null;
        RewriteRuleTokenStream stream_SINGLEEQUALS=new RewriteRuleTokenStream(adaptor,"token SINGLEEQUALS");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:2: ( NAME -> ^( IDEXPRESSION NAME ) | f1= term SINGLEEQUALS f2= term -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= term COMPAREOP f2= term -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) )
            int alt13=3;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:4: NAME
                    {
                    NAME50=(Token)match(input,NAME,FOLLOW_NAME_in_expression538);  
                    stream_NAME.add(NAME50);



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
                    // 91:9: -> ^( IDEXPRESSION NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:12: ^( IDEXPRESSION NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IDEXPRESSION, "IDEXPRESSION"), root_1);

                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:35: f1= term SINGLEEQUALS f2= term
                    {
                    pushFollow(FOLLOW_term_in_expression552);
                    f1=term();

                    state._fsp--;

                    stream_term.add(f1.getTree());
                    SINGLEEQUALS51=(Token)match(input,SINGLEEQUALS,FOLLOW_SINGLEEQUALS_in_expression554);  
                    stream_SINGLEEQUALS.add(SINGLEEQUALS51);

                    pushFollow(FOLLOW_term_in_expression558);
                    f2=term();

                    state._fsp--;

                    stream_term.add(f2.getTree());


                    // AST REWRITE
                    // elements: f1, f2
                    // token labels: 
                    // rule labels: retval, f1, f2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);
                    RewriteRuleSubtreeStream stream_f2=new RewriteRuleSubtreeStream(adaptor,"rule f2",f2!=null?f2.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 91:64: -> ^( COMPAREEXPRESSION $f1 EQUALS $f2)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:68: ^( COMPAREEXPRESSION $f1 EQUALS $f2)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(COMPAREEXPRESSION, "COMPAREEXPRESSION"), root_1);

                        adaptor.addChild(root_1, stream_f1.nextTree());
                        adaptor.addChild(root_1, (Object)adaptor.create(EQUALS, "EQUALS"));
                        adaptor.addChild(root_1, stream_f2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:106: f1= term COMPAREOP f2= term
                    {
                    pushFollow(FOLLOW_term_in_expression579);
                    f1=term();

                    state._fsp--;

                    stream_term.add(f1.getTree());
                    COMPAREOP52=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression581);  
                    stream_COMPAREOP.add(COMPAREOP52);

                    pushFollow(FOLLOW_term_in_expression585);
                    f2=term();

                    state._fsp--;

                    stream_term.add(f2.getTree());


                    // AST REWRITE
                    // elements: COMPAREOP, f1, f2
                    // token labels: 
                    // rule labels: retval, f1, f2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);
                    RewriteRuleSubtreeStream stream_f2=new RewriteRuleSubtreeStream(adaptor,"rule f2",f2!=null?f2.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 91:132: -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:135: ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(COMPAREEXPRESSION, "COMPAREEXPRESSION"), root_1);

                        adaptor.addChild(root_1, stream_f1.nextTree());
                        adaptor.addChild(root_1, stream_COMPAREOP.nextNode());
                        adaptor.addChild(root_1, stream_f2.nextTree());

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
        return retval;
    }
    // $ANTLR end "expression"

    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:1: term : ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) | value );
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:6: ( aggregation | kAttributeUsage POINT NAME -> ^( KMEMBER kAttributeUsage NAME ) | aName= NAME POINT member= NAME -> ^( MEMBER $aName $member) | value )
            int alt14=4;
            switch ( input.LA(1) ) {
            case AGGREGATEOP:
                {
                alt14=1;
                }
                break;
            case NAME:
                {
                int LA14_2 = input.LA(2);

                if ( ((LA14_2>=FIRST && LA14_2<=PREVIOUS)||LA14_2==LAST) ) {
                    alt14=2;
                }
                else if ( (LA14_2==POINT) ) {
                    alt14=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 2, input);

                    throw nvae;
                }
                }
                break;
            case NUMBER:
            case STRING_LITERAL:
                {
                alt14=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:8: aggregation
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregation_in_term609);
                    aggregation53=aggregation();

                    state._fsp--;

                    adaptor.addChild(root_0, aggregation53.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:3: kAttributeUsage POINT NAME
                    {
                    pushFollow(FOLLOW_kAttributeUsage_in_term615);
                    kAttributeUsage54=kAttributeUsage();

                    state._fsp--;

                    stream_kAttributeUsage.add(kAttributeUsage54.getTree());
                    POINT55=(Token)match(input,POINT,FOLLOW_POINT_in_term617);  
                    stream_POINT.add(POINT55);

                    NAME56=(Token)match(input,NAME,FOLLOW_NAME_in_term619);  
                    stream_NAME.add(NAME56);



                    // AST REWRITE
                    // elements: NAME, kAttributeUsage
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 95:30: -> ^( KMEMBER kAttributeUsage NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:95:33: ^( KMEMBER kAttributeUsage NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KMEMBER, "KMEMBER"), root_1);

                        adaptor.addChild(root_1, stream_kAttributeUsage.nextTree());
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:3: aName= NAME POINT member= NAME
                    {
                    aName=(Token)match(input,NAME,FOLLOW_NAME_in_term636);  
                    stream_NAME.add(aName);

                    POINT57=(Token)match(input,POINT,FOLLOW_POINT_in_term638);  
                    stream_POINT.add(POINT57);

                    member=(Token)match(input,NAME,FOLLOW_NAME_in_term642);  
                    stream_NAME.add(member);



                    // AST REWRITE
                    // elements: member, aName
                    // token labels: member, aName
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_member=new RewriteRuleTokenStream(adaptor,"token member",member);
                    RewriteRuleTokenStream stream_aName=new RewriteRuleTokenStream(adaptor,"token aName",aName);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 96:32: -> ^( MEMBER $aName $member)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:96:35: ^( MEMBER $aName $member)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MEMBER, "MEMBER"), root_1);

                        adaptor.addChild(root_1, stream_aName.nextNode());
                        adaptor.addChild(root_1, stream_member.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:97:3: value
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_value_in_term659);
                    value58=value();

                    state._fsp--;

                    adaptor.addChild(root_0, value58.getTree());

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
        return retval;
    }
    // $ANTLR end "term"

    public static class aggregation_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "aggregation"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:100:1: aggregation : AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:2: ( AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:4: AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT member= NAME RBRACKET
            {
            AGGREGATEOP59=(Token)match(input,AGGREGATEOP,FOLLOW_AGGREGATEOP_in_aggregation673);  
            stream_AGGREGATEOP.add(AGGREGATEOP59);

            LBRACKET60=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation675);  
            stream_LBRACKET.add(LBRACKET60);

            var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation679);  
            stream_NAME.add(var);

            ALLTOPREVIOUS61=(Token)match(input,ALLTOPREVIOUS,FOLLOW_ALLTOPREVIOUS_in_aggregation681);  
            stream_ALLTOPREVIOUS.add(ALLTOPREVIOUS61);

            POINT62=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation683);  
            stream_POINT.add(POINT62);

            member=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation687);  
            stream_NAME.add(member);

            RBRACKET63=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation689);  
            stream_RBRACKET.add(RBRACKET63);



            // AST REWRITE
            // elements: AGGREGATEOP, var, ALLTOPREVIOUS, member
            // token labels: member, var
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_member=new RewriteRuleTokenStream(adaptor,"token member",member);
            RewriteRuleTokenStream stream_var=new RewriteRuleTokenStream(adaptor,"token var",var);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 101:75: -> ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member)
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:78: ^( AGGREGATION AGGREGATEOP $var ALLTOPREVIOUS $member)
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
        return retval;
    }
    // $ANTLR end "aggregation"

    public static class value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set64=null;

        Object set64_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:104:8: ( NUMBER | STRING_LITERAL )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (Object)adaptor.nil();

            set64=(Token)input.LT(1);
            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set64));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
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
        return retval;
    }
    // $ANTLR end "value"

    // Delegated rules


    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA13_eotS =
        "\25\uffff";
    static final String DFA13_eofS =
        "\1\uffff\1\11\23\uffff";
    static final String DFA13_minS =
        "\1\20\1\7\1\40\1\33\4\31\1\20\1\uffff\1\20\2\uffff\1\20\1\33\1"+
        "\17\1\33\1\31\1\20\1\41\1\33";
    static final String DFA13_maxS =
        "\1\50\1\31\1\40\1\34\4\31\1\20\1\uffff\1\20\2\uffff\1\20\1\34\1"+
        "\17\1\34\1\31\1\20\1\41\1\34";
    static final String DFA13_acceptS =
        "\11\uffff\1\1\1\uffff\1\3\1\2\10\uffff";
    static final String DFA13_specialS =
        "\25\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\1\5\uffff\1\2\15\uffff\1\3\3\uffff\1\3",
            "\1\11\2\uffff\2\11\1\5\1\4\1\6\2\uffff\1\7\7\uffff\1\10",
            "\1\12",
            "\1\13\1\14",
            "\1\15",
            "\1\15",
            "\1\15",
            "\1\15",
            "\1\16",
            "",
            "\1\17",
            "",
            "",
            "\1\20",
            "\1\13\1\14",
            "\1\21",
            "\1\13\1\14",
            "\1\22",
            "\1\23",
            "\1\24",
            "\1\13\1\14"
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
            return "90:1: expression : ( NAME -> ^( IDEXPRESSION NAME ) | f1= term SINGLEEQUALS f2= term -> ^( COMPAREEXPRESSION $f1 EQUALS $f2) | f1= term COMPAREOP f2= term -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) );";
        }
    }
 

    public static final BitSet FOLLOW_patternPart_in_query104 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_wherePart_in_query107 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_withinPart_in_query112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart127 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart129 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_TIMEUNIT_in_withinPart131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart153 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_wherePart1_in_wherePart155 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart157 = new BitSet(new long[]{0x0000011000410000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart159 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart178 = new BitSet(new long[]{0x0000011000410000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart201 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEQ_in_patternDecl225 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_LBRACKET_in_patternDecl227 = new BitSet(new long[]{0x0000000140010000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl229 = new BitSet(new long[]{0x0000000280000000L});
    public static final BitSet FOLLOW_COMMA_in_patternDecl232 = new BitSet(new long[]{0x0000000140010000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl234 = new BitSet(new long[]{0x0000000280000000L});
    public static final BitSet FOLLOW_RBRACKET_in_patternDecl238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_pItem263 = new BitSet(new long[]{0x0000000140010000L});
    public static final BitSet FOLLOW_LBRACKET_in_pItem267 = new BitSet(new long[]{0x0000000140010000L});
    public static final BitSet FOLLOW_typeName_in_pItem273 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_attributeName_in_pItem277 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_RBRACKET_in_pItem279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeName306 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_PLUS_in_typeName310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_METHOD_in_wherePart1353 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_LBRACKET_in_wherePart1355 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_parameterList_in_wherePart1357 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RBRACKET_in_wherePart1359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList382 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList384 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList386 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName424 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName427 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName452 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage471 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage479 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_FIRST_in_kAttributeUsage481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage486 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_PREVIOUS_in_kAttributeUsage488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage493 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_LAST_in_kAttributeUsage495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions507 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_whereExpressions510 = new BitSet(new long[]{0x0000011000410000L});
    public static final BitSet FOLLOW_expression_in_whereExpressions512 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_NAME_in_expression538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_expression552 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_SINGLEEQUALS_in_expression554 = new BitSet(new long[]{0x0000011000410000L});
    public static final BitSet FOLLOW_term_in_expression558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_expression579 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression581 = new BitSet(new long[]{0x0000011000410000L});
    public static final BitSet FOLLOW_term_in_expression585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregation_in_term609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_term615 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_term617 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_term619 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_term636 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_term638 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_term642 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AGGREGATEOP_in_aggregation673 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation675 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_aggregation679 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_ALLTOPREVIOUS_in_aggregation681 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_POINT_in_aggregation683 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_NAME_in_aggregation687 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value0 = new BitSet(new long[]{0x0000000000000002L});

}