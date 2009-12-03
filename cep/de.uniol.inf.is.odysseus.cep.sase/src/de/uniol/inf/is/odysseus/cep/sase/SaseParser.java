// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g 2009-12-03 10:25:47
 
	package de.uniol.inf.is.odysseus.cep.sase; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class SaseParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "HAVING", "PATTERN", "WHERE", "WITHIN", "SEQ", "LEFTCURLY", "RIGHTCURLY", "AND", "FIRST", "CURRENT", "PREVIOUS", "ALLTOPREVIOUS", "TIMEUNIT", "SKIP_METHOD", "AGGREGATEOP", "BBRACKETLEFT", "BBRACKETRIGHT", "PLUS", "MINUS", "POINT", "MULT", "COMPAREOP", "NOT", "COMMA", "LBRACKET", "RBRACKET", "INTEGER", "FLOAT", "NUMBER", "DIGIT", "LETTER", "NAME", "NONCONTROL_CHAR", "STRING_LITERAL", "SPACE", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "STATE", "KTYPE", "TYPE", "WHERESTRAT", "WHEREEXPRESSION", "EXPRESSION", "ATTRIBUTE", "KATTRIBUTE", "PARAMLIST", "MEMBERACCESS", "COMPAREEXPRESSION", "IDEXPRESSION", "AGGREGATION"
    };
    public static final int WHERE=6;
    public static final int POINT=23;
    public static final int LETTER=34;
    public static final int ATTRIBUTE=49;
    public static final int FLOAT=31;
    public static final int NOT=26;
    public static final int AND=11;
    public static final int SPACE=38;
    public static final int EOF=-1;
    public static final int TYPE=45;
    public static final int LBRACKET=28;
    public static final int PATTERN=5;
    public static final int COMPAREEXPRESSION=53;
    public static final int NAME=35;
    public static final int STRING_LITERAL=37;
    public static final int LEFTCURLY=9;
    public static final int COMMA=27;
    public static final int SEQ=8;
    public static final int PREVIOUS=14;
    public static final int KATTRIBUTE=50;
    public static final int RIGHTCURLY=10;
    public static final int PLUS=21;
    public static final int DIGIT=33;
    public static final int RBRACKET=29;
    public static final int EXPRESSION=48;
    public static final int BBRACKETRIGHT=20;
    public static final int INTEGER=30;
    public static final int STATE=43;
    public static final int IDEXPRESSION=54;
    public static final int TIMEUNIT=16;
    public static final int ALLTOPREVIOUS=15;
    public static final int SKIP_METHOD=17;
    public static final int NUMBER=32;
    public static final int PARAMLIST=51;
    public static final int WHITESPACE=42;
    public static final int HAVING=4;
    public static final int MINUS=22;
    public static final int MULT=24;
    public static final int AGGREGATEOP=18;
    public static final int CURRENT=13;
    public static final int WHEREEXPRESSION=47;
    public static final int NEWLINE=41;
    public static final int NONCONTROL_CHAR=36;
    public static final int AGGREGATION=55;
    public static final int BBRACKETLEFT=19;
    public static final int WHERESTRAT=46;
    public static final int WITHIN=7;
    public static final int MEMBERACCESS=52;
    public static final int KTYPE=44;
    public static final int LOWER=39;
    public static final int COMPAREOP=25;
    public static final int UPPER=40;
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:1: query : patternPart ( wherePart )? ( withinPart )? ;
    public final SaseParser.query_return query() throws RecognitionException {
        SaseParser.query_return retval = new SaseParser.query_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.patternPart_return patternPart1 = null;

        SaseParser.wherePart_return wherePart2 = null;

        SaseParser.withinPart_return withinPart3 = null;



        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:7: ( patternPart ( wherePart )? ( withinPart )? )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:10: patternPart ( wherePart )? ( withinPart )?
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_patternPart_in_query100);
            patternPart1=patternPart();

            state._fsp--;

            adaptor.addChild(root_0, patternPart1.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:22: ( wherePart )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==WHERE) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:23: wherePart
                    {
                    pushFollow(FOLLOW_wherePart_in_query103);
                    wherePart2=wherePart();

                    state._fsp--;

                    adaptor.addChild(root_0, wherePart2.getTree());

                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:35: ( withinPart )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==WITHIN) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:29:36: withinPart
                    {
                    pushFollow(FOLLOW_withinPart_in_query108);
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:32:1: withinPart : WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:2: ( WITHIN NUMBER TIMEUNIT -> ^( WITHIN NUMBER TIMEUNIT ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:4: WITHIN NUMBER TIMEUNIT
            {
            WITHIN4=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_withinPart123);  
            stream_WITHIN.add(WITHIN4);

            NUMBER5=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_withinPart125);  
            stream_NUMBER.add(NUMBER5);

            TIMEUNIT6=(Token)match(input,TIMEUNIT,FOLLOW_TIMEUNIT_in_withinPart127);  
            stream_TIMEUNIT.add(TIMEUNIT6);



            // AST REWRITE
            // elements: WITHIN, NUMBER, TIMEUNIT
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
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:33:30: ^( WITHIN NUMBER TIMEUNIT )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:36:1: wherePart : ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) );
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:2: ( WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY -> ^( WHERE wherePart1 whereExpressions ) | WHERE whereExpressions -> ^( WHERE whereExpressions ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==WHERE) ) {
                int LA3_1 = input.LA(2);

                if ( ((LA3_1>=AGGREGATEOP && LA3_1<=BBRACKETLEFT)||LA3_1==NUMBER||LA3_1==NAME||LA3_1==STRING_LITERAL) ) {
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:4: WHERE wherePart1 LEFTCURLY whereExpressions RIGHTCURLY
                    {
                    WHERE7=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart149);  
                    stream_WHERE.add(WHERE7);

                    pushFollow(FOLLOW_wherePart1_in_wherePart151);
                    wherePart18=wherePart1();

                    state._fsp--;

                    stream_wherePart1.add(wherePart18.getTree());
                    LEFTCURLY9=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_wherePart153);  
                    stream_LEFTCURLY.add(LEFTCURLY9);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart155);
                    whereExpressions10=whereExpressions();

                    state._fsp--;

                    stream_whereExpressions.add(whereExpressions10.getTree());
                    RIGHTCURLY11=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_wherePart157);  
                    stream_RIGHTCURLY.add(RIGHTCURLY11);



                    // AST REWRITE
                    // elements: whereExpressions, WHERE, wherePart1
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
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:37:62: ^( WHERE wherePart1 whereExpressions )
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:38:4: WHERE whereExpressions
                    {
                    WHERE12=(Token)match(input,WHERE,FOLLOW_WHERE_in_wherePart174);  
                    stream_WHERE.add(WHERE12);

                    pushFollow(FOLLOW_whereExpressions_in_wherePart176);
                    whereExpressions13=whereExpressions();

                    state._fsp--;

                    stream_whereExpressions.add(whereExpressions13.getTree());


                    // AST REWRITE
                    // elements: WHERE, whereExpressions
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
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:38:30: ^( WHERE whereExpressions )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:41:1: patternPart : PATTERN patternDecl -> ^( PATTERN patternDecl ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:42:2: ( PATTERN patternDecl -> ^( PATTERN patternDecl ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:42:4: PATTERN patternDecl
            {
            PATTERN14=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_patternPart197);  
            stream_PATTERN.add(PATTERN14);

            pushFollow(FOLLOW_patternDecl_in_patternPart199);
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
            // 42:24: -> ^( PATTERN patternDecl )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:42:27: ^( PATTERN patternDecl )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:46:1: patternDecl : SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:2: ( SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET -> ^( SEQ ( pItem )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:4: SEQ LBRACKET pItem ( COMMA pItem )* RBRACKET
            {
            SEQ16=(Token)match(input,SEQ,FOLLOW_SEQ_in_patternDecl221);  
            stream_SEQ.add(SEQ16);

            LBRACKET17=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_patternDecl223);  
            stream_LBRACKET.add(LBRACKET17);

            pushFollow(FOLLOW_pItem_in_patternDecl225);
            pItem18=pItem();

            state._fsp--;

            stream_pItem.add(pItem18.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:23: ( COMMA pItem )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==COMMA) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:24: COMMA pItem
            	    {
            	    COMMA19=(Token)match(input,COMMA,FOLLOW_COMMA_in_patternDecl228);  
            	    stream_COMMA.add(COMMA19);

            	    pushFollow(FOLLOW_pItem_in_patternDecl230);
            	    pItem20=pItem();

            	    state._fsp--;

            	    stream_pItem.add(pItem20.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            RBRACKET21=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_patternDecl234);  
            stream_RBRACKET.add(RBRACKET21);



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
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:50: ^( SEQ ( pItem )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_SEQ.nextNode(), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:47:56: ( pItem )*
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:1: pItem : ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) ;
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
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:8: ( ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )? -> ^( STATE $type $variable ( NOT )? ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:10: ( NOT )? ( LBRACKET )? type= typeName variable= attributeName ( RBRACKET )?
            {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:10: ( NOT )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==NOT) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:11: NOT
                    {
                    NOT22=(Token)match(input,NOT,FOLLOW_NOT_in_pItem259);  
                    stream_NOT.add(NOT22);


                    }
                    break;

            }

            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:17: ( LBRACKET )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==LBRACKET) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:17: LBRACKET
                    {
                    LBRACKET23=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_pItem263);  
                    stream_LBRACKET.add(LBRACKET23);


                    }
                    break;

            }

            pushFollow(FOLLOW_typeName_in_pItem269);
            type=typeName();

            state._fsp--;

            stream_typeName.add(type.getTree());
            pushFollow(FOLLOW_attributeName_in_pItem273);
            variable=attributeName();

            state._fsp--;

            stream_attributeName.add(variable.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:65: ( RBRACKET )?
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:65: RBRACKET
                    {
                    RBRACKET24=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_pItem275);  
                    stream_RBRACKET.add(RBRACKET24);


                    }
                    break;

            }



            // AST REWRITE
            // elements: variable, NOT, type
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
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:78: ^( STATE $type $variable ( NOT )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STATE, "STATE"), root_1);

                adaptor.addChild(root_1, stream_type.nextTree());
                adaptor.addChild(root_1, stream_variable.nextTree());
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:51:102: ( NOT )?
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:1: typeName : ( NAME op= PLUS -> ^( KTYPE NAME $op) | NAME -> ^( TYPE NAME ) );
    public final SaseParser.typeName_return typeName() throws RecognitionException {
        SaseParser.typeName_return retval = new SaseParser.typeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;
        Token NAME25=null;
        Token NAME26=null;

        Object op_tree=null;
        Object NAME25_tree=null;
        Object NAME26_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:9: ( NAME op= PLUS -> ^( KTYPE NAME $op) | NAME -> ^( TYPE NAME ) )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==NAME) ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==PLUS) ) {
                    alt8=1;
                }
                else if ( (LA8_1==NAME) ) {
                    alt8=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:11: NAME op= PLUS
                    {
                    NAME25=(Token)match(input,NAME,FOLLOW_NAME_in_typeName302);  
                    stream_NAME.add(NAME25);

                    op=(Token)match(input,PLUS,FOLLOW_PLUS_in_typeName306);  
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
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:27: ^( KTYPE NAME $op)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KTYPE, "KTYPE"), root_1);

                        adaptor.addChild(root_1, stream_NAME.nextNode());
                        adaptor.addChild(root_1, stream_op.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:47: NAME
                    {
                    NAME26=(Token)match(input,NAME,FOLLOW_NAME_in_typeName321);  
                    stream_NAME.add(NAME26);



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
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:54:55: ^( TYPE NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TYPE, "TYPE"), root_1);

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

        Token SKIP_METHOD27=null;
        Token LBRACKET28=null;
        Token RBRACKET30=null;
        SaseParser.parameterList_return parameterList29 = null;


        Object SKIP_METHOD27_tree=null;
        Object LBRACKET28_tree=null;
        Object RBRACKET30_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SKIP_METHOD=new RewriteRuleTokenStream(adaptor,"token SKIP_METHOD");
        RewriteRuleSubtreeStream stream_parameterList=new RewriteRuleSubtreeStream(adaptor,"rule parameterList");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:2: ( SKIP_METHOD LBRACKET parameterList RBRACKET -> ^( WHERESTRAT SKIP_METHOD parameterList ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:4: SKIP_METHOD LBRACKET parameterList RBRACKET
            {
            SKIP_METHOD27=(Token)match(input,SKIP_METHOD,FOLLOW_SKIP_METHOD_in_wherePart1341);  
            stream_SKIP_METHOD.add(SKIP_METHOD27);

            LBRACKET28=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_wherePart1343);  
            stream_LBRACKET.add(LBRACKET28);

            pushFollow(FOLLOW_parameterList_in_wherePart1345);
            parameterList29=parameterList();

            state._fsp--;

            stream_parameterList.add(parameterList29.getTree());
            RBRACKET30=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_wherePart1347);  
            stream_RBRACKET.add(RBRACKET30);



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
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:58:51: ^( WHERESTRAT SKIP_METHOD parameterList )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:61:1: parameterList : attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) ;
    public final SaseParser.parameterList_return parameterList() throws RecognitionException {
        SaseParser.parameterList_return retval = new SaseParser.parameterList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token COMMA32=null;
        SaseParser.attributeName_return attributeName31 = null;

        SaseParser.attributeName_return attributeName33 = null;


        Object COMMA32_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_attributeName=new RewriteRuleSubtreeStream(adaptor,"rule attributeName");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:2: ( attributeName ( COMMA attributeName )* -> ^( PARAMLIST ( attributeName )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:4: attributeName ( COMMA attributeName )*
            {
            pushFollow(FOLLOW_attributeName_in_parameterList370);
            attributeName31=attributeName();

            state._fsp--;

            stream_attributeName.add(attributeName31.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:17: ( COMMA attributeName )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==COMMA) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:18: COMMA attributeName
            	    {
            	    COMMA32=(Token)match(input,COMMA,FOLLOW_COMMA_in_parameterList372);  
            	    stream_COMMA.add(COMMA32);

            	    pushFollow(FOLLOW_attributeName_in_parameterList374);
            	    attributeName33=attributeName();

            	    state._fsp--;

            	    stream_attributeName.add(attributeName33.getTree());

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
            // 62:40: -> ^( PARAMLIST ( attributeName )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:43: ^( PARAMLIST ( attributeName )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PARAMLIST, "PARAMLIST"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:62:55: ( attributeName )*
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:65:1: attributeName : ( kAttributeName | sAttributeName );
    public final SaseParser.attributeName_return attributeName() throws RecognitionException {
        SaseParser.attributeName_return retval = new SaseParser.attributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        SaseParser.kAttributeName_return kAttributeName34 = null;

        SaseParser.sAttributeName_return sAttributeName35 = null;



        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:2: ( kAttributeName | sAttributeName )
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:5: kAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_kAttributeName_in_attributeName398);
                    kAttributeName34=kAttributeName();

                    state._fsp--;

                    adaptor.addChild(root_0, kAttributeName34.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:66:20: sAttributeName
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_sAttributeName_in_attributeName400);
                    sAttributeName35=sAttributeName();

                    state._fsp--;

                    adaptor.addChild(root_0, sAttributeName35.getTree());

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:69:1: kAttributeName : NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) ;
    public final SaseParser.kAttributeName_return kAttributeName() throws RecognitionException {
        SaseParser.kAttributeName_return retval = new SaseParser.kAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME36=null;
        Token BBRACKETLEFT37=null;
        Token BBRACKETRIGHT38=null;

        Object NAME36_tree=null;
        Object BBRACKETLEFT37_tree=null;
        Object BBRACKETRIGHT38_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:2: ( NAME BBRACKETLEFT BBRACKETRIGHT -> ^( KATTRIBUTE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:4: NAME BBRACKETLEFT BBRACKETRIGHT
            {
            NAME36=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeName412);  
            stream_NAME.add(NAME36);

            BBRACKETLEFT37=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_kAttributeName415);  
            stream_BBRACKETLEFT.add(BBRACKETLEFT37);

            BBRACKETRIGHT38=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_kAttributeName417);  
            stream_BBRACKETRIGHT.add(BBRACKETRIGHT38);



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
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:70:41: ^( KATTRIBUTE NAME )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:74:1: sAttributeName : NAME -> ^( ATTRIBUTE NAME ) ;
    public final SaseParser.sAttributeName_return sAttributeName() throws RecognitionException {
        SaseParser.sAttributeName_return retval = new SaseParser.sAttributeName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME39=null;

        Object NAME39_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:75:2: ( NAME -> ^( ATTRIBUTE NAME ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:75:4: NAME
            {
            NAME39=(Token)match(input,NAME,FOLLOW_NAME_in_sAttributeName440);  
            stream_NAME.add(NAME39);



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
            // 75:9: -> ^( ATTRIBUTE NAME )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:75:12: ^( ATTRIBUTE NAME )
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:77:1: kAttributeUsage : ( NAME CURRENT -> ^( KATTRIBUTE NAME CURRENT ) | NAME FIRST -> ^( KATTRIBUTE NAME FIRST ) | NAME PREVIOUS -> ^( KATTRIBUTE NAME PREVIOUS ) );
    public final SaseParser.kAttributeUsage_return kAttributeUsage() throws RecognitionException {
        SaseParser.kAttributeUsage_return retval = new SaseParser.kAttributeUsage_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME40=null;
        Token CURRENT41=null;
        Token NAME42=null;
        Token FIRST43=null;
        Token NAME44=null;
        Token PREVIOUS45=null;

        Object NAME40_tree=null;
        Object CURRENT41_tree=null;
        Object NAME42_tree=null;
        Object FIRST43_tree=null;
        Object NAME44_tree=null;
        Object PREVIOUS45_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_PREVIOUS=new RewriteRuleTokenStream(adaptor,"token PREVIOUS");
        RewriteRuleTokenStream stream_CURRENT=new RewriteRuleTokenStream(adaptor,"token CURRENT");
        RewriteRuleTokenStream stream_FIRST=new RewriteRuleTokenStream(adaptor,"token FIRST");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:2: ( NAME CURRENT -> ^( KATTRIBUTE NAME CURRENT ) | NAME FIRST -> ^( KATTRIBUTE NAME FIRST ) | NAME PREVIOUS -> ^( KATTRIBUTE NAME PREVIOUS ) )
            int alt11=3;
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:5: NAME CURRENT
                    {
                    NAME40=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage459);  
                    stream_NAME.add(NAME40);

                    CURRENT41=(Token)match(input,CURRENT,FOLLOW_CURRENT_in_kAttributeUsage461);  
                    stream_CURRENT.add(CURRENT41);



                    // AST REWRITE
                    // elements: NAME, CURRENT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 78:18: -> ^( KATTRIBUTE NAME CURRENT )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:78:21: ^( KATTRIBUTE NAME CURRENT )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KATTRIBUTE, "KATTRIBUTE"), root_1);

                        adaptor.addChild(root_1, stream_NAME.nextNode());
                        adaptor.addChild(root_1, stream_CURRENT.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:3: NAME FIRST
                    {
                    NAME42=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage476);  
                    stream_NAME.add(NAME42);

                    FIRST43=(Token)match(input,FIRST,FOLLOW_FIRST_in_kAttributeUsage478);  
                    stream_FIRST.add(FIRST43);



                    // AST REWRITE
                    // elements: NAME, FIRST
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 79:14: -> ^( KATTRIBUTE NAME FIRST )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:79:17: ^( KATTRIBUTE NAME FIRST )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KATTRIBUTE, "KATTRIBUTE"), root_1);

                        adaptor.addChild(root_1, stream_NAME.nextNode());
                        adaptor.addChild(root_1, stream_FIRST.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:80:3: NAME PREVIOUS
                    {
                    NAME44=(Token)match(input,NAME,FOLLOW_NAME_in_kAttributeUsage493);  
                    stream_NAME.add(NAME44);

                    PREVIOUS45=(Token)match(input,PREVIOUS,FOLLOW_PREVIOUS_in_kAttributeUsage495);  
                    stream_PREVIOUS.add(PREVIOUS45);



                    // AST REWRITE
                    // elements: NAME, PREVIOUS
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 80:17: -> ^( KATTRIBUTE NAME PREVIOUS )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:80:20: ^( KATTRIBUTE NAME PREVIOUS )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(KATTRIBUTE, "KATTRIBUTE"), root_1);

                        adaptor.addChild(root_1, stream_NAME.nextNode());
                        adaptor.addChild(root_1, stream_PREVIOUS.nextNode());

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
    // $ANTLR end "kAttributeUsage"

    public static class whereExpressions_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "whereExpressions"
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:83:1: whereExpressions : expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) ;
    public final SaseParser.whereExpressions_return whereExpressions() throws RecognitionException {
        SaseParser.whereExpressions_return retval = new SaseParser.whereExpressions_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AND47=null;
        SaseParser.expression_return expression46 = null;

        SaseParser.expression_return expression48 = null;


        Object AND47_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:2: ( expression ( AND expression )* -> ^( WHEREEXPRESSION ( AND )? ( expression )* ) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:4: expression ( AND expression )*
            {
            pushFollow(FOLLOW_expression_in_whereExpressions517);
            expression46=expression();

            state._fsp--;

            stream_expression.add(expression46.getTree());
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:15: ( AND expression )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==AND) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:16: AND expression
            	    {
            	    AND47=(Token)match(input,AND,FOLLOW_AND_in_whereExpressions520);  
            	    stream_AND.add(AND47);

            	    pushFollow(FOLLOW_expression_in_whereExpressions522);
            	    expression48=expression();

            	    state._fsp--;

            	    stream_expression.add(expression48.getTree());

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
            // 84:33: -> ^( WHEREEXPRESSION ( AND )? ( expression )* )
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:36: ^( WHEREEXPRESSION ( AND )? ( expression )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHEREEXPRESSION, "WHEREEXPRESSION"), root_1);

                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:54: ( AND )?
                if ( stream_AND.hasNext() ) {
                    adaptor.addChild(root_1, stream_AND.nextNode());

                }
                stream_AND.reset();
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:84:59: ( expression )*
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:87:1: expression : ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) | f1= term COMPAREOP f2= term -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) );
    public final SaseParser.expression_return expression() throws RecognitionException {
        SaseParser.expression_return retval = new SaseParser.expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token BBRACKETLEFT49=null;
        Token NAME50=null;
        Token BBRACKETRIGHT51=null;
        Token COMPAREOP52=null;
        SaseParser.term_return f1 = null;

        SaseParser.term_return f2 = null;


        Object BBRACKETLEFT49_tree=null;
        Object NAME50_tree=null;
        Object BBRACKETRIGHT51_tree=null;
        Object COMPAREOP52_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_BBRACKETLEFT=new RewriteRuleTokenStream(adaptor,"token BBRACKETLEFT");
        RewriteRuleTokenStream stream_COMPAREOP=new RewriteRuleTokenStream(adaptor,"token COMPAREOP");
        RewriteRuleTokenStream stream_BBRACKETRIGHT=new RewriteRuleTokenStream(adaptor,"token BBRACKETRIGHT");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:2: ( BBRACKETLEFT NAME BBRACKETRIGHT -> ^( IDEXPRESSION NAME ) | f1= term COMPAREOP f2= term -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2) )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==BBRACKETLEFT) ) {
                alt13=1;
            }
            else if ( (LA13_0==AGGREGATEOP||LA13_0==NUMBER||LA13_0==NAME||LA13_0==STRING_LITERAL) ) {
                alt13=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:5: BBRACKETLEFT NAME BBRACKETRIGHT
                    {
                    BBRACKETLEFT49=(Token)match(input,BBRACKETLEFT,FOLLOW_BBRACKETLEFT_in_expression549);  
                    stream_BBRACKETLEFT.add(BBRACKETLEFT49);

                    NAME50=(Token)match(input,NAME,FOLLOW_NAME_in_expression551);  
                    stream_NAME.add(NAME50);

                    BBRACKETRIGHT51=(Token)match(input,BBRACKETRIGHT,FOLLOW_BBRACKETRIGHT_in_expression553);  
                    stream_BBRACKETRIGHT.add(BBRACKETRIGHT51);



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
                    // 88:37: -> ^( IDEXPRESSION NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:40: ^( IDEXPRESSION NAME )
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:63: f1= term COMPAREOP f2= term
                    {
                    pushFollow(FOLLOW_term_in_expression567);
                    f1=term();

                    state._fsp--;

                    stream_term.add(f1.getTree());
                    COMPAREOP52=(Token)match(input,COMPAREOP,FOLLOW_COMPAREOP_in_expression569);  
                    stream_COMPAREOP.add(COMPAREOP52);

                    pushFollow(FOLLOW_term_in_expression573);
                    f2=term();

                    state._fsp--;

                    stream_term.add(f2.getTree());


                    // AST REWRITE
                    // elements: f2, COMPAREOP, f1
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
                    // 88:89: -> ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:88:92: ^( COMPAREEXPRESSION $f1 COMPAREOP $f2)
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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:1: term : ( aggregation | kAttributeUsage POINT NAME -> ^( MEMBERACCESS kAttributeUsage NAME ) | sAttributeName POINT NAME -> ^( MEMBERACCESS sAttributeName NAME ) | value );
    public final SaseParser.term_return term() throws RecognitionException {
        SaseParser.term_return retval = new SaseParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token POINT55=null;
        Token NAME56=null;
        Token POINT58=null;
        Token NAME59=null;
        SaseParser.aggregation_return aggregation53 = null;

        SaseParser.kAttributeUsage_return kAttributeUsage54 = null;

        SaseParser.sAttributeName_return sAttributeName57 = null;

        SaseParser.value_return value60 = null;


        Object POINT55_tree=null;
        Object NAME56_tree=null;
        Object POINT58_tree=null;
        Object NAME59_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_sAttributeName=new RewriteRuleSubtreeStream(adaptor,"rule sAttributeName");
        RewriteRuleSubtreeStream stream_kAttributeUsage=new RewriteRuleSubtreeStream(adaptor,"rule kAttributeUsage");
        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:6: ( aggregation | kAttributeUsage POINT NAME -> ^( MEMBERACCESS kAttributeUsage NAME ) | sAttributeName POINT NAME -> ^( MEMBERACCESS sAttributeName NAME ) | value )
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

                if ( ((LA14_2>=FIRST && LA14_2<=PREVIOUS)) ) {
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
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:91:8: aggregation
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_aggregation_in_term597);
                    aggregation53=aggregation();

                    state._fsp--;

                    adaptor.addChild(root_0, aggregation53.getTree());

                    }
                    break;
                case 2 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:3: kAttributeUsage POINT NAME
                    {
                    pushFollow(FOLLOW_kAttributeUsage_in_term603);
                    kAttributeUsage54=kAttributeUsage();

                    state._fsp--;

                    stream_kAttributeUsage.add(kAttributeUsage54.getTree());
                    POINT55=(Token)match(input,POINT,FOLLOW_POINT_in_term605);  
                    stream_POINT.add(POINT55);

                    NAME56=(Token)match(input,NAME,FOLLOW_NAME_in_term607);  
                    stream_NAME.add(NAME56);



                    // AST REWRITE
                    // elements: kAttributeUsage, NAME
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 92:29: -> ^( MEMBERACCESS kAttributeUsage NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:92:32: ^( MEMBERACCESS kAttributeUsage NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MEMBERACCESS, "MEMBERACCESS"), root_1);

                        adaptor.addChild(root_1, stream_kAttributeUsage.nextTree());
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:3: sAttributeName POINT NAME
                    {
                    pushFollow(FOLLOW_sAttributeName_in_term621);
                    sAttributeName57=sAttributeName();

                    state._fsp--;

                    stream_sAttributeName.add(sAttributeName57.getTree());
                    POINT58=(Token)match(input,POINT,FOLLOW_POINT_in_term623);  
                    stream_POINT.add(POINT58);

                    NAME59=(Token)match(input,NAME,FOLLOW_NAME_in_term625);  
                    stream_NAME.add(NAME59);



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
                    // 93:29: -> ^( MEMBERACCESS sAttributeName NAME )
                    {
                        // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:93:32: ^( MEMBERACCESS sAttributeName NAME )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MEMBERACCESS, "MEMBERACCESS"), root_1);

                        adaptor.addChild(root_1, stream_sAttributeName.nextTree());
                        adaptor.addChild(root_1, stream_NAME.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:94:3: value
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_value_in_term640);
                    value60=value();

                    state._fsp--;

                    adaptor.addChild(root_0, value60.getTree());

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:97:1: aggregation : AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT attr= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var $attr) ;
    public final SaseParser.aggregation_return aggregation() throws RecognitionException {
        SaseParser.aggregation_return retval = new SaseParser.aggregation_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token var=null;
        Token attr=null;
        Token AGGREGATEOP61=null;
        Token LBRACKET62=null;
        Token ALLTOPREVIOUS63=null;
        Token POINT64=null;
        Token RBRACKET65=null;

        Object var_tree=null;
        Object attr_tree=null;
        Object AGGREGATEOP61_tree=null;
        Object LBRACKET62_tree=null;
        Object ALLTOPREVIOUS63_tree=null;
        Object POINT64_tree=null;
        Object RBRACKET65_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_AGGREGATEOP=new RewriteRuleTokenStream(adaptor,"token AGGREGATEOP");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleTokenStream stream_ALLTOPREVIOUS=new RewriteRuleTokenStream(adaptor,"token ALLTOPREVIOUS");

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:2: ( AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT attr= NAME RBRACKET -> ^( AGGREGATION AGGREGATEOP $var $attr) )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:4: AGGREGATEOP LBRACKET var= NAME ALLTOPREVIOUS POINT attr= NAME RBRACKET
            {
            AGGREGATEOP61=(Token)match(input,AGGREGATEOP,FOLLOW_AGGREGATEOP_in_aggregation654);  
            stream_AGGREGATEOP.add(AGGREGATEOP61);

            LBRACKET62=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_aggregation656);  
            stream_LBRACKET.add(LBRACKET62);

            var=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation660);  
            stream_NAME.add(var);

            ALLTOPREVIOUS63=(Token)match(input,ALLTOPREVIOUS,FOLLOW_ALLTOPREVIOUS_in_aggregation662);  
            stream_ALLTOPREVIOUS.add(ALLTOPREVIOUS63);

            POINT64=(Token)match(input,POINT,FOLLOW_POINT_in_aggregation664);  
            stream_POINT.add(POINT64);

            attr=(Token)match(input,NAME,FOLLOW_NAME_in_aggregation668);  
            stream_NAME.add(attr);

            RBRACKET65=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_aggregation670);  
            stream_RBRACKET.add(RBRACKET65);



            // AST REWRITE
            // elements: attr, AGGREGATEOP, var
            // token labels: var, attr
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_var=new RewriteRuleTokenStream(adaptor,"token var",var);
            RewriteRuleTokenStream stream_attr=new RewriteRuleTokenStream(adaptor,"token attr",attr);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 98:73: -> ^( AGGREGATION AGGREGATEOP $var $attr)
            {
                // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:98:76: ^( AGGREGATION AGGREGATEOP $var $attr)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(AGGREGATION, "AGGREGATION"), root_1);

                adaptor.addChild(root_1, stream_AGGREGATEOP.nextNode());
                adaptor.addChild(root_1, stream_var.nextNode());
                adaptor.addChild(root_1, stream_attr.nextNode());

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
    // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:1: value : ( NUMBER | STRING_LITERAL );
    public final SaseParser.value_return value() throws RecognitionException {
        SaseParser.value_return retval = new SaseParser.value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set66=null;

        Object set66_tree=null;

        try {
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:101:8: ( NUMBER | STRING_LITERAL )
            // C:\\development\\odysseus\\cep\\de.uniol.inf.is.odysseus.cep.sase\\src\\de\\uniol\\inf\\is\\odysseus\\cep\\sase\\SaseParser.g:
            {
            root_0 = (Object)adaptor.nil();

            set66=(Token)input.LT(1);
            if ( input.LA(1)==NUMBER||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set66));
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


 

    public static final BitSet FOLLOW_patternPart_in_query100 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_wherePart_in_query103 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_withinPart_in_query108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITHIN_in_withinPart123 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_NUMBER_in_withinPart125 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_TIMEUNIT_in_withinPart127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart149 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_wherePart1_in_wherePart151 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LEFTCURLY_in_wherePart153 = new BitSet(new long[]{0x00000029000C0000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart155 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_wherePart157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_wherePart174 = new BitSet(new long[]{0x00000029000C0000L});
    public static final BitSet FOLLOW_whereExpressions_in_wherePart176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATTERN_in_patternPart197 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_patternDecl_in_patternPart199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEQ_in_patternDecl221 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LBRACKET_in_patternDecl223 = new BitSet(new long[]{0x0000000814000000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl225 = new BitSet(new long[]{0x0000000028000000L});
    public static final BitSet FOLLOW_COMMA_in_patternDecl228 = new BitSet(new long[]{0x0000000814000000L});
    public static final BitSet FOLLOW_pItem_in_patternDecl230 = new BitSet(new long[]{0x0000000028000000L});
    public static final BitSet FOLLOW_RBRACKET_in_patternDecl234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_pItem259 = new BitSet(new long[]{0x0000000814000000L});
    public static final BitSet FOLLOW_LBRACKET_in_pItem263 = new BitSet(new long[]{0x0000000814000000L});
    public static final BitSet FOLLOW_typeName_in_pItem269 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_attributeName_in_pItem273 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_RBRACKET_in_pItem275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeName302 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_PLUS_in_typeName306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_typeName321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_METHOD_in_wherePart1341 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LBRACKET_in_wherePart1343 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_parameterList_in_wherePart1345 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_RBRACKET_in_wherePart1347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attributeName_in_parameterList370 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_COMMA_in_parameterList372 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_attributeName_in_parameterList374 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_kAttributeName_in_attributeName398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_attributeName400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeName412 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_kAttributeName415 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_kAttributeName417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sAttributeName440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage459 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_CURRENT_in_kAttributeUsage461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage476 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_FIRST_in_kAttributeUsage478 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_kAttributeUsage493 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_PREVIOUS_in_kAttributeUsage495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_whereExpressions517 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_AND_in_whereExpressions520 = new BitSet(new long[]{0x00000029000C0000L});
    public static final BitSet FOLLOW_expression_in_whereExpressions522 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_BBRACKETLEFT_in_expression549 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_NAME_in_expression551 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_BBRACKETRIGHT_in_expression553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_expression567 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_COMPAREOP_in_expression569 = new BitSet(new long[]{0x00000029000C0000L});
    public static final BitSet FOLLOW_term_in_expression573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregation_in_term597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_kAttributeUsage_in_term603 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_POINT_in_term605 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_NAME_in_term607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sAttributeName_in_term621 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_POINT_in_term623 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_NAME_in_term625 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_term640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AGGREGATEOP_in_aggregation654 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LBRACKET_in_aggregation656 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation660 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_ALLTOPREVIOUS_in_aggregation662 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_POINT_in_aggregation664 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_NAME_in_aggregation668 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_RBRACKET_in_aggregation670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value0 = new BitSet(new long[]{0x0000000000000002L});

}