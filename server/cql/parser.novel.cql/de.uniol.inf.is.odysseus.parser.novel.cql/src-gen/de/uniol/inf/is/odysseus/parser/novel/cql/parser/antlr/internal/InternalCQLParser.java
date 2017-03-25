package de.uniol.inf.is.odysseus.parser.novel.cql.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.uniol.inf.is.odysseus.parser.novel.cql.services.CQLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCQLParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "INTERSECTION", "DATAHANDLER", "DIFFERENCE", "PARTITION", "TRANSPORT", "UNBOUNDED", "DISTINCT", "PROTOCOL", "ADVANCE", "CHANNEL", "OPTIONS", "WRAPPER", "ATTACH", "CREATE", "EXISTS", "HAVING", "SELECT", "STREAM", "FALSE", "GROUP", "TUPLE", "UNION", "WHERE", "DROP", "FILE", "FROM", "SINK", "SIZE", "TIME", "TRUE", "VIEW", "AND", "NOT", "ExclamationMarkEqualsSign", "LessThanSignEqualsSign", "GreaterThanSignEqualsSign", "AS", "BY", "IF", "IN", "OR", "TO", "LeftParenthesis", "RightParenthesis", "Asterisk", "PlusSign", "Comma", "HyphenMinus", "FullStop", "Solidus", "Colon", "Semicolon", "LessThanSign", "EqualsSign", "GreaterThanSign", "LeftSquareBracket", "RightSquareBracket", "RULE_ID", "RULE_INT", "RULE_FLOAT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER"
    };
    public static final int CREATE=17;
    public static final int FROM=29;
    public static final int VIEW=34;
    public static final int LessThanSign=56;
    public static final int LeftParenthesis=46;
    public static final int UNBOUNDED=9;
    public static final int WRAPPER=15;
    public static final int PARTITION=7;
    public static final int IF=42;
    public static final int RightSquareBracket=60;
    public static final int GreaterThanSign=58;
    public static final int RULE_ID=61;
    public static final int IN=43;
    public static final int DISTINCT=10;
    public static final int SIZE=31;
    public static final int PROTOCOL=11;
    public static final int RightParenthesis=47;
    public static final int TRUE=33;
    public static final int OPTIONS=14;
    public static final int WHERE=26;
    public static final int GreaterThanSignEqualsSign=39;
    public static final int NOT=36;
    public static final int AS=40;
    public static final int INTERSECTION=4;
    public static final int CHANNEL=13;
    public static final int SINK=30;
    public static final int PlusSign=49;
    public static final int RULE_INT=62;
    public static final int AND=35;
    public static final int RULE_ML_COMMENT=65;
    public static final int LeftSquareBracket=59;
    public static final int ADVANCE=12;
    public static final int HAVING=19;
    public static final int RULE_STRING=64;
    public static final int DROP=27;
    public static final int RULE_SL_COMMENT=66;
    public static final int GROUP=23;
    public static final int Comma=50;
    public static final int EqualsSign=57;
    public static final int TRANSPORT=8;
    public static final int HyphenMinus=51;
    public static final int DIFFERENCE=6;
    public static final int BY=41;
    public static final int LessThanSignEqualsSign=38;
    public static final int Solidus=53;
    public static final int Colon=54;
    public static final int DATAHANDLER=5;
    public static final int FILE=28;
    public static final int EOF=-1;
    public static final int Asterisk=48;
    public static final int FullStop=52;
    public static final int OR=44;
    public static final int EXISTS=18;
    public static final int RULE_WS=67;
    public static final int STREAM=21;
    public static final int TIME=32;
    public static final int RULE_ANY_OTHER=68;
    public static final int SELECT=20;
    public static final int TUPLE=24;
    public static final int Semicolon=55;
    public static final int ATTACH=16;
    public static final int RULE_FLOAT=63;
    public static final int FALSE=22;
    public static final int ExclamationMarkEqualsSign=37;
    public static final int TO=45;
    public static final int UNION=25;

    // delegates
    // delegators


        public InternalCQLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalCQLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalCQLParser.tokenNames; }
    public String getGrammarFileName() { return "InternalCQLParser.g"; }



     	private CQLGrammarAccess grammarAccess;

        public InternalCQLParser(TokenStream input, CQLGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "Model";
       	}

       	@Override
       	protected CQLGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleModel"
    // InternalCQLParser.g:58:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalCQLParser.g:58:46: (iv_ruleModel= ruleModel EOF )
            // InternalCQLParser.g:59:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalCQLParser.g:65:1: ruleModel returns [EObject current=null] : ( (lv_statements_0_0= ruleStatement ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_statements_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:71:2: ( ( (lv_statements_0_0= ruleStatement ) )* )
            // InternalCQLParser.g:72:2: ( (lv_statements_0_0= ruleStatement ) )*
            {
            // InternalCQLParser.g:72:2: ( (lv_statements_0_0= ruleStatement ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=ATTACH && LA1_0<=CREATE)||(LA1_0>=SELECT && LA1_0<=STREAM)||LA1_0==DROP||LA1_0==VIEW) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCQLParser.g:73:3: (lv_statements_0_0= ruleStatement )
            	    {
            	    // InternalCQLParser.g:73:3: (lv_statements_0_0= ruleStatement )
            	    // InternalCQLParser.g:74:4: lv_statements_0_0= ruleStatement
            	    {

            	    				newCompositeNode(grammarAccess.getModelAccess().getStatementsStatementParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_statements_0_0=ruleStatement();

            	    state._fsp--;


            	    				if (current==null) {
            	    					current = createModelElementForParent(grammarAccess.getModelRule());
            	    				}
            	    				add(
            	    					current,
            	    					"statements",
            	    					lv_statements_0_0,
            	    					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Statement");
            	    				afterParserOrEnumRuleCall();
            	    			

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleStatement"
    // InternalCQLParser.g:94:1: entryRuleStatement returns [EObject current=null] : iv_ruleStatement= ruleStatement EOF ;
    public final EObject entryRuleStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStatement = null;


        try {
            // InternalCQLParser.g:94:50: (iv_ruleStatement= ruleStatement EOF )
            // InternalCQLParser.g:95:2: iv_ruleStatement= ruleStatement EOF
            {
             newCompositeNode(grammarAccess.getStatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleStatement=ruleStatement();

            state._fsp--;

             current =iv_ruleStatement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStatement"


    // $ANTLR start "ruleStatement"
    // InternalCQLParser.g:101:1: ruleStatement returns [EObject current=null] : ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= Semicolon )? ) ;
    public final EObject ruleStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_8=null;
        EObject lv_type_0_0 = null;

        EObject lv_type_1_0 = null;

        EObject lv_type_2_0 = null;

        EObject lv_type_3_0 = null;

        EObject lv_type_4_0 = null;

        EObject lv_type_5_0 = null;

        EObject lv_type_6_0 = null;

        EObject lv_type_7_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:107:2: ( ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= Semicolon )? ) )
            // InternalCQLParser.g:108:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= Semicolon )? )
            {
            // InternalCQLParser.g:108:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= Semicolon )? )
            // InternalCQLParser.g:109:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= Semicolon )?
            {
            // InternalCQLParser.g:109:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) )
            int alt2=8;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // InternalCQLParser.g:110:4: ( (lv_type_0_0= ruleSelect ) )
                    {
                    // InternalCQLParser.g:110:4: ( (lv_type_0_0= ruleSelect ) )
                    // InternalCQLParser.g:111:5: (lv_type_0_0= ruleSelect )
                    {
                    // InternalCQLParser.g:111:5: (lv_type_0_0= ruleSelect )
                    // InternalCQLParser.g:112:6: lv_type_0_0= ruleSelect
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeSelectParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_0_0=ruleSelect();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:130:4: ( (lv_type_1_0= ruleStreamTo ) )
                    {
                    // InternalCQLParser.g:130:4: ( (lv_type_1_0= ruleStreamTo ) )
                    // InternalCQLParser.g:131:5: (lv_type_1_0= ruleStreamTo )
                    {
                    // InternalCQLParser.g:131:5: (lv_type_1_0= ruleStreamTo )
                    // InternalCQLParser.g:132:6: lv_type_1_0= ruleStreamTo
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeStreamToParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_1_0=ruleStreamTo();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.StreamTo");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:150:4: ( (lv_type_2_0= ruleDrop ) )
                    {
                    // InternalCQLParser.g:150:4: ( (lv_type_2_0= ruleDrop ) )
                    // InternalCQLParser.g:151:5: (lv_type_2_0= ruleDrop )
                    {
                    // InternalCQLParser.g:151:5: (lv_type_2_0= ruleDrop )
                    // InternalCQLParser.g:152:6: lv_type_2_0= ruleDrop
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeDropParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_2_0=ruleDrop();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Drop");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:170:4: ( (lv_type_3_0= ruleCreateStream1 ) )
                    {
                    // InternalCQLParser.g:170:4: ( (lv_type_3_0= ruleCreateStream1 ) )
                    // InternalCQLParser.g:171:5: (lv_type_3_0= ruleCreateStream1 )
                    {
                    // InternalCQLParser.g:171:5: (lv_type_3_0= ruleCreateStream1 )
                    // InternalCQLParser.g:172:6: lv_type_3_0= ruleCreateStream1
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateStream1ParserRuleCall_0_3_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_3_0=ruleCreateStream1();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateStream1");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:190:4: ( (lv_type_4_0= ruleCreateSink1 ) )
                    {
                    // InternalCQLParser.g:190:4: ( (lv_type_4_0= ruleCreateSink1 ) )
                    // InternalCQLParser.g:191:5: (lv_type_4_0= ruleCreateSink1 )
                    {
                    // InternalCQLParser.g:191:5: (lv_type_4_0= ruleCreateSink1 )
                    // InternalCQLParser.g:192:6: lv_type_4_0= ruleCreateSink1
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateSink1ParserRuleCall_0_4_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_4_0=ruleCreateSink1();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_4_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateSink1");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:210:4: ( (lv_type_5_0= ruleCreateStreamChannel ) )
                    {
                    // InternalCQLParser.g:210:4: ( (lv_type_5_0= ruleCreateStreamChannel ) )
                    // InternalCQLParser.g:211:5: (lv_type_5_0= ruleCreateStreamChannel )
                    {
                    // InternalCQLParser.g:211:5: (lv_type_5_0= ruleCreateStreamChannel )
                    // InternalCQLParser.g:212:6: lv_type_5_0= ruleCreateStreamChannel
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateStreamChannelParserRuleCall_0_5_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_5_0=ruleCreateStreamChannel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_5_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateStreamChannel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 7 :
                    // InternalCQLParser.g:230:4: ( (lv_type_6_0= ruleCreateStreamFile ) )
                    {
                    // InternalCQLParser.g:230:4: ( (lv_type_6_0= ruleCreateStreamFile ) )
                    // InternalCQLParser.g:231:5: (lv_type_6_0= ruleCreateStreamFile )
                    {
                    // InternalCQLParser.g:231:5: (lv_type_6_0= ruleCreateStreamFile )
                    // InternalCQLParser.g:232:6: lv_type_6_0= ruleCreateStreamFile
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateStreamFileParserRuleCall_0_6_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_6_0=ruleCreateStreamFile();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_6_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateStreamFile");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 8 :
                    // InternalCQLParser.g:250:4: ( (lv_type_7_0= ruleCreateView ) )
                    {
                    // InternalCQLParser.g:250:4: ( (lv_type_7_0= ruleCreateView ) )
                    // InternalCQLParser.g:251:5: (lv_type_7_0= ruleCreateView )
                    {
                    // InternalCQLParser.g:251:5: (lv_type_7_0= ruleCreateView )
                    // InternalCQLParser.g:252:6: lv_type_7_0= ruleCreateView
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateViewParserRuleCall_0_7_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_7_0=ruleCreateView();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_7_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateView");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:270:3: (otherlv_8= Semicolon )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==Semicolon) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalCQLParser.g:271:4: otherlv_8= Semicolon
                    {
                    otherlv_8=(Token)match(input,Semicolon,FOLLOW_2); 

                    				newLeafNode(otherlv_8, grammarAccess.getStatementAccess().getSemicolonKeyword_1());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStatement"


    // $ANTLR start "entryRuleSelect"
    // InternalCQLParser.g:280:1: entryRuleSelect returns [EObject current=null] : iv_ruleSelect= ruleSelect EOF ;
    public final EObject entryRuleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect = null;


        try {
            // InternalCQLParser.g:280:47: (iv_ruleSelect= ruleSelect EOF )
            // InternalCQLParser.g:281:2: iv_ruleSelect= ruleSelect EOF
            {
             newCompositeNode(grammarAccess.getSelectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelect=ruleSelect();

            state._fsp--;

             current =iv_ruleSelect; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSelect"


    // $ANTLR start "ruleSelect"
    // InternalCQLParser.g:287:1: ruleSelect returns [EObject current=null] : ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )? ) ;
    public final EObject ruleSelect() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token lv_distinct_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_12=null;
        Token otherlv_13=null;
        Token otherlv_15=null;
        Token otherlv_17=null;
        EObject lv_arguments_3_0 = null;

        EObject lv_arguments_5_0 = null;

        EObject lv_sources_7_0 = null;

        EObject lv_sources_9_0 = null;

        EObject lv_predicates_11_0 = null;

        EObject lv_order_14_0 = null;

        EObject lv_order_16_0 = null;

        EObject lv_having_18_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:293:2: ( ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQLParser.g:294:2: ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQLParser.g:294:2: ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )? )
            // InternalCQLParser.g:295:3: ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) ) (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* ) (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )? (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )? (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )?
            {
            // InternalCQLParser.g:295:3: ( (lv_name_0_0= SELECT ) )
            // InternalCQLParser.g:296:4: (lv_name_0_0= SELECT )
            {
            // InternalCQLParser.g:296:4: (lv_name_0_0= SELECT )
            // InternalCQLParser.g:297:5: lv_name_0_0= SELECT
            {
            lv_name_0_0=(Token)match(input,SELECT,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelectAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelectRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCQLParser.g:309:3: ( (lv_distinct_1_0= DISTINCT ) )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==DISTINCT) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalCQLParser.g:310:4: (lv_distinct_1_0= DISTINCT )
                    {
                    // InternalCQLParser.g:310:4: (lv_distinct_1_0= DISTINCT )
                    // InternalCQLParser.g:311:5: lv_distinct_1_0= DISTINCT
                    {
                    lv_distinct_1_0=(Token)match(input,DISTINCT,FOLLOW_5); 

                    					newLeafNode(lv_distinct_1_0, grammarAccess.getSelectAccess().getDistinctDISTINCTKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getSelectRule());
                    					}
                    					setWithLastConsumed(current, "distinct", lv_distinct_1_0, "DISTINCT");
                    				

                    }


                    }
                    break;

            }

            // InternalCQLParser.g:323:3: (otherlv_2= Asterisk | ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==Asterisk) ) {
                alt7=1;
            }
            else if ( (LA7_0==FALSE||LA7_0==TRUE||(LA7_0>=RULE_ID && LA7_0<=RULE_STRING)) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // InternalCQLParser.g:324:4: otherlv_2= Asterisk
                    {
                    otherlv_2=(Token)match(input,Asterisk,FOLLOW_6); 

                    				newLeafNode(otherlv_2, grammarAccess.getSelectAccess().getAsteriskKeyword_2_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:329:4: ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* )
                    {
                    // InternalCQLParser.g:329:4: ( ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )* )
                    // InternalCQLParser.g:330:5: ( (lv_arguments_3_0= ruleArgument ) )+ (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )*
                    {
                    // InternalCQLParser.g:330:5: ( (lv_arguments_3_0= ruleArgument ) )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==FALSE||LA5_0==TRUE||(LA5_0>=RULE_ID && LA5_0<=RULE_STRING)) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // InternalCQLParser.g:331:6: (lv_arguments_3_0= ruleArgument )
                    	    {
                    	    // InternalCQLParser.g:331:6: (lv_arguments_3_0= ruleArgument )
                    	    // InternalCQLParser.g:332:7: lv_arguments_3_0= ruleArgument
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getArgumentsArgumentParserRuleCall_2_1_0_0());
                    	    						
                    	    pushFollow(FOLLOW_7);
                    	    lv_arguments_3_0=ruleArgument();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"arguments",
                    	    								lv_arguments_3_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Argument");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);

                    // InternalCQLParser.g:349:5: (otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==Comma) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // InternalCQLParser.g:350:6: otherlv_4= Comma ( (lv_arguments_5_0= ruleArgument ) )
                    	    {
                    	    otherlv_4=(Token)match(input,Comma,FOLLOW_5); 

                    	    						newLeafNode(otherlv_4, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_0());
                    	    					
                    	    // InternalCQLParser.g:354:6: ( (lv_arguments_5_0= ruleArgument ) )
                    	    // InternalCQLParser.g:355:7: (lv_arguments_5_0= ruleArgument )
                    	    {
                    	    // InternalCQLParser.g:355:7: (lv_arguments_5_0= ruleArgument )
                    	    // InternalCQLParser.g:356:8: lv_arguments_5_0= ruleArgument
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectAccess().getArgumentsArgumentParserRuleCall_2_1_1_1_0());
                    	    							
                    	    pushFollow(FOLLOW_8);
                    	    lv_arguments_5_0=ruleArgument();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"arguments",
                    	    									lv_arguments_5_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Argument");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:376:3: (otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )* )
            // InternalCQLParser.g:377:4: otherlv_6= FROM ( (lv_sources_7_0= ruleSource ) )+ (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )*
            {
            otherlv_6=(Token)match(input,FROM,FOLLOW_9); 

            				newLeafNode(otherlv_6, grammarAccess.getSelectAccess().getFROMKeyword_3_0());
            			
            // InternalCQLParser.g:381:4: ( (lv_sources_7_0= ruleSource ) )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==LeftParenthesis||LA8_0==RULE_ID) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCQLParser.g:382:5: (lv_sources_7_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:382:5: (lv_sources_7_0= ruleSource )
            	    // InternalCQLParser.g:383:6: lv_sources_7_0= ruleSource
            	    {

            	    						newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_10);
            	    lv_sources_7_0=ruleSource();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelectRule());
            	    						}
            	    						add(
            	    							current,
            	    							"sources",
            	    							lv_sources_7_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);

            // InternalCQLParser.g:400:4: (otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==Comma) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalCQLParser.g:401:5: otherlv_8= Comma ( (lv_sources_9_0= ruleSource ) )
            	    {
            	    otherlv_8=(Token)match(input,Comma,FOLLOW_9); 

            	    					newLeafNode(otherlv_8, grammarAccess.getSelectAccess().getCommaKeyword_3_2_0());
            	    				
            	    // InternalCQLParser.g:405:5: ( (lv_sources_9_0= ruleSource ) )
            	    // InternalCQLParser.g:406:6: (lv_sources_9_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:406:6: (lv_sources_9_0= ruleSource )
            	    // InternalCQLParser.g:407:7: lv_sources_9_0= ruleSource
            	    {

            	    							newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_2_1_0());
            	    						
            	    pushFollow(FOLLOW_11);
            	    lv_sources_9_0=ruleSource();

            	    state._fsp--;


            	    							if (current==null) {
            	    								current = createModelElementForParent(grammarAccess.getSelectRule());
            	    							}
            	    							add(
            	    								current,
            	    								"sources",
            	    								lv_sources_9_0,
            	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
            	    							afterParserOrEnumRuleCall();
            	    						

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }

            // InternalCQLParser.g:426:3: (otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==WHERE) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQLParser.g:427:4: otherlv_10= WHERE ( (lv_predicates_11_0= ruleExpressionsModel ) )
                    {
                    otherlv_10=(Token)match(input,WHERE,FOLLOW_12); 

                    				newLeafNode(otherlv_10, grammarAccess.getSelectAccess().getWHEREKeyword_4_0());
                    			
                    // InternalCQLParser.g:431:4: ( (lv_predicates_11_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:432:5: (lv_predicates_11_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:432:5: (lv_predicates_11_0= ruleExpressionsModel )
                    // InternalCQLParser.g:433:6: lv_predicates_11_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getPredicatesExpressionsModelParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_13);
                    lv_predicates_11_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"predicates",
                    							lv_predicates_11_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:451:3: (otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )* )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==GROUP) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQLParser.g:452:4: otherlv_12= GROUP otherlv_13= BY ( (lv_order_14_0= ruleAttribute ) )+ (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )*
                    {
                    otherlv_12=(Token)match(input,GROUP,FOLLOW_14); 

                    				newLeafNode(otherlv_12, grammarAccess.getSelectAccess().getGROUPKeyword_5_0());
                    			
                    otherlv_13=(Token)match(input,BY,FOLLOW_15); 

                    				newLeafNode(otherlv_13, grammarAccess.getSelectAccess().getBYKeyword_5_1());
                    			
                    // InternalCQLParser.g:460:4: ( (lv_order_14_0= ruleAttribute ) )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==RULE_ID) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // InternalCQLParser.g:461:5: (lv_order_14_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:461:5: (lv_order_14_0= ruleAttribute )
                    	    // InternalCQLParser.g:462:6: lv_order_14_0= ruleAttribute
                    	    {

                    	    						newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_2_0());
                    	    					
                    	    pushFollow(FOLLOW_16);
                    	    lv_order_14_0=ruleAttribute();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"order",
                    	    							lv_order_14_0,
                    	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);

                    // InternalCQLParser.g:479:4: (otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==Comma) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // InternalCQLParser.g:480:5: otherlv_15= Comma ( (lv_order_16_0= ruleAttribute ) )
                    	    {
                    	    otherlv_15=(Token)match(input,Comma,FOLLOW_15); 

                    	    					newLeafNode(otherlv_15, grammarAccess.getSelectAccess().getCommaKeyword_5_3_0());
                    	    				
                    	    // InternalCQLParser.g:484:5: ( (lv_order_16_0= ruleAttribute ) )
                    	    // InternalCQLParser.g:485:6: (lv_order_16_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:485:6: (lv_order_16_0= ruleAttribute )
                    	    // InternalCQLParser.g:486:7: lv_order_16_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_17);
                    	    lv_order_16_0=ruleAttribute();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"order",
                    	    								lv_order_16_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCQLParser.g:505:3: (otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==HAVING) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // InternalCQLParser.g:506:4: otherlv_17= HAVING ( (lv_having_18_0= ruleExpressionsModel ) )
                    {
                    otherlv_17=(Token)match(input,HAVING,FOLLOW_12); 

                    				newLeafNode(otherlv_17, grammarAccess.getSelectAccess().getHAVINGKeyword_6_0());
                    			
                    // InternalCQLParser.g:510:4: ( (lv_having_18_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:511:5: (lv_having_18_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:511:5: (lv_having_18_0= ruleExpressionsModel )
                    // InternalCQLParser.g:512:6: lv_having_18_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getHavingExpressionsModelParserRuleCall_6_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_having_18_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"having",
                    							lv_having_18_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSelect"


    // $ANTLR start "entryRuleNestedStatement"
    // InternalCQLParser.g:534:1: entryRuleNestedStatement returns [EObject current=null] : iv_ruleNestedStatement= ruleNestedStatement EOF ;
    public final EObject entryRuleNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedStatement = null;


        try {
            // InternalCQLParser.g:534:56: (iv_ruleNestedStatement= ruleNestedStatement EOF )
            // InternalCQLParser.g:535:2: iv_ruleNestedStatement= ruleNestedStatement EOF
            {
             newCompositeNode(grammarAccess.getNestedStatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNestedStatement=ruleNestedStatement();

            state._fsp--;

             current =iv_ruleNestedStatement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNestedStatement"


    // $ANTLR start "ruleNestedStatement"
    // InternalCQLParser.g:541:1: ruleNestedStatement returns [EObject current=null] : (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis ) ;
    public final EObject ruleNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_1 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:547:2: ( (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis ) )
            // InternalCQLParser.g:548:2: (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis )
            {
            // InternalCQLParser.g:548:2: (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis )
            // InternalCQLParser.g:549:3: otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis
            {
            otherlv_0=(Token)match(input,LeftParenthesis,FOLLOW_18); 

            			newLeafNode(otherlv_0, grammarAccess.getNestedStatementAccess().getLeftParenthesisKeyword_0());
            		

            			newCompositeNode(grammarAccess.getNestedStatementAccess().getSelectParserRuleCall_1());
            		
            pushFollow(FOLLOW_19);
            this_Select_1=ruleSelect();

            state._fsp--;


            			current = this_Select_1;
            			afterParserOrEnumRuleCall();
            		
            otherlv_2=(Token)match(input,RightParenthesis,FOLLOW_2); 

            			newLeafNode(otherlv_2, grammarAccess.getNestedStatementAccess().getRightParenthesisKeyword_2());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNestedStatement"


    // $ANTLR start "entryRuleArgument"
    // InternalCQLParser.g:569:1: entryRuleArgument returns [EObject current=null] : iv_ruleArgument= ruleArgument EOF ;
    public final EObject entryRuleArgument() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleArgument = null;


        try {
            // InternalCQLParser.g:569:49: (iv_ruleArgument= ruleArgument EOF )
            // InternalCQLParser.g:570:2: iv_ruleArgument= ruleArgument EOF
            {
             newCompositeNode(grammarAccess.getArgumentRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleArgument=ruleArgument();

            state._fsp--;

             current =iv_ruleArgument; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleArgument"


    // $ANTLR start "ruleArgument"
    // InternalCQLParser.g:576:1: ruleArgument returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) ;
    public final EObject ruleArgument() throws RecognitionException {
        EObject current = null;

        EObject lv_attribute_0_0 = null;

        EObject lv_expression_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:582:2: ( ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) ) )
            // InternalCQLParser.g:583:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            {
            // InternalCQLParser.g:583:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_expression_1_0= ruleSelectExpression ) ) )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case LeftParenthesis:
                case Asterisk:
                case PlusSign:
                case HyphenMinus:
                case Solidus:
                    {
                    alt15=2;
                    }
                    break;
                case FullStop:
                    {
                    int LA15_3 = input.LA(3);

                    if ( (LA15_3==Asterisk) ) {
                        int LA15_5 = input.LA(4);

                        if ( (LA15_5==EOF||LA15_5==FALSE||LA15_5==FROM||LA15_5==TRUE||LA15_5==AS||LA15_5==Comma||(LA15_5>=RULE_ID && LA15_5<=RULE_STRING)) ) {
                            alt15=1;
                        }
                        else if ( ((LA15_5>=Asterisk && LA15_5<=PlusSign)||LA15_5==HyphenMinus||LA15_5==Solidus) ) {
                            alt15=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 5, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA15_3==RULE_ID) ) {
                        int LA15_6 = input.LA(4);

                        if ( (LA15_6==EOF||LA15_6==FALSE||LA15_6==FROM||LA15_6==TRUE||LA15_6==AS||LA15_6==Comma||(LA15_6>=RULE_ID && LA15_6<=RULE_STRING)) ) {
                            alt15=1;
                        }
                        else if ( ((LA15_6>=Asterisk && LA15_6<=PlusSign)||LA15_6==HyphenMinus||LA15_6==Solidus) ) {
                            alt15=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 6, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 3, input);

                        throw nvae;
                    }
                    }
                    break;
                case EOF:
                case FALSE:
                case FROM:
                case TRUE:
                case AS:
                case Comma:
                case RULE_ID:
                case RULE_INT:
                case RULE_FLOAT:
                case RULE_STRING:
                    {
                    alt15=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA15_0==FALSE||LA15_0==TRUE||(LA15_0>=RULE_INT && LA15_0<=RULE_STRING)) ) {
                alt15=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // InternalCQLParser.g:584:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    {
                    // InternalCQLParser.g:584:3: ( (lv_attribute_0_0= ruleAttribute ) )
                    // InternalCQLParser.g:585:4: (lv_attribute_0_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:585:4: (lv_attribute_0_0= ruleAttribute )
                    // InternalCQLParser.g:586:5: lv_attribute_0_0= ruleAttribute
                    {

                    					newCompositeNode(grammarAccess.getArgumentAccess().getAttributeAttributeParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_attribute_0_0=ruleAttribute();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getArgumentRule());
                    					}
                    					set(
                    						current,
                    						"attribute",
                    						lv_attribute_0_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:604:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    {
                    // InternalCQLParser.g:604:3: ( (lv_expression_1_0= ruleSelectExpression ) )
                    // InternalCQLParser.g:605:4: (lv_expression_1_0= ruleSelectExpression )
                    {
                    // InternalCQLParser.g:605:4: (lv_expression_1_0= ruleSelectExpression )
                    // InternalCQLParser.g:606:5: lv_expression_1_0= ruleSelectExpression
                    {

                    					newCompositeNode(grammarAccess.getArgumentAccess().getExpressionSelectExpressionParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_expression_1_0=ruleSelectExpression();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getArgumentRule());
                    					}
                    					set(
                    						current,
                    						"expression",
                    						lv_expression_1_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpression");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleArgument"


    // $ANTLR start "entryRuleSource"
    // InternalCQLParser.g:627:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQLParser.g:627:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQLParser.g:628:2: iv_ruleSource= ruleSource EOF
            {
             newCompositeNode(grammarAccess.getSourceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSource=ruleSource();

            state._fsp--;

             current =iv_ruleSource; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSource"


    // $ANTLR start "ruleSource"
    // InternalCQLParser.g:634:1: ruleSource returns [EObject current=null] : ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_9=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        AntlrDatatypeRuleToken lv_unbounded_2_0 = null;

        EObject lv_time_3_0 = null;

        EObject lv_tuple_4_0 = null;

        EObject lv_alias_7_0 = null;

        EObject lv_nested_8_0 = null;

        EObject lv_alias_10_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:640:2: ( ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) ) )
            // InternalCQLParser.g:641:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) )
            {
            // InternalCQLParser.g:641:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==RULE_ID) ) {
                alt19=1;
            }
            else if ( (LA19_0==LeftParenthesis) ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQLParser.g:642:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
                    {
                    // InternalCQLParser.g:642:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
                    // InternalCQLParser.g:643:4: ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
                    {
                    // InternalCQLParser.g:643:4: ( (lv_name_0_0= ruleSourceName ) )
                    // InternalCQLParser.g:644:5: (lv_name_0_0= ruleSourceName )
                    {
                    // InternalCQLParser.g:644:5: (lv_name_0_0= ruleSourceName )
                    // InternalCQLParser.g:645:6: lv_name_0_0= ruleSourceName
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNameSourceNameParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_20);
                    lv_name_0_0=ruleSourceName();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"name",
                    							lv_name_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SourceName");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCQLParser.g:662:4: (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==LeftSquareBracket) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // InternalCQLParser.g:663:5: otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket
                            {
                            otherlv_1=(Token)match(input,LeftSquareBracket,FOLLOW_21); 

                            					newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_1_0());
                            				
                            // InternalCQLParser.g:667:5: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                            int alt16=3;
                            int LA16_0 = input.LA(1);

                            if ( (LA16_0==UNBOUNDED) ) {
                                alt16=1;
                            }
                            else if ( (LA16_0==SIZE) ) {
                                int LA16_2 = input.LA(2);

                                if ( (LA16_2==RULE_INT) ) {
                                    int LA16_3 = input.LA(3);

                                    if ( (LA16_3==RULE_ID) ) {
                                        alt16=2;
                                    }
                                    else if ( (LA16_3==ADVANCE||LA16_3==TUPLE) ) {
                                        alt16=3;
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("", 16, 3, input);

                                        throw nvae;
                                    }
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 16, 2, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 16, 0, input);

                                throw nvae;
                            }
                            switch (alt16) {
                                case 1 :
                                    // InternalCQLParser.g:668:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQLParser.g:668:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQLParser.g:669:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQLParser.g:669:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQLParser.g:670:8: lv_unbounded_2_0= ruleWindow_Unbounded
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getUnboundedWindow_UnboundedParserRuleCall_0_1_1_0_0());
                                    							
                                    pushFollow(FOLLOW_22);
                                    lv_unbounded_2_0=ruleWindow_Unbounded();

                                    state._fsp--;


                                    								if (current==null) {
                                    									current = createModelElementForParent(grammarAccess.getSourceRule());
                                    								}
                                    								set(
                                    									current,
                                    									"unbounded",
                                    									lv_unbounded_2_0,
                                    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Window_Unbounded");
                                    								afterParserOrEnumRuleCall();
                                    							

                                    }


                                    }


                                    }
                                    break;
                                case 2 :
                                    // InternalCQLParser.g:688:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQLParser.g:688:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQLParser.g:689:7: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQLParser.g:689:7: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQLParser.g:690:8: lv_time_3_0= ruleWindow_Timebased
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getTimeWindow_TimebasedParserRuleCall_0_1_1_1_0());
                                    							
                                    pushFollow(FOLLOW_22);
                                    lv_time_3_0=ruleWindow_Timebased();

                                    state._fsp--;


                                    								if (current==null) {
                                    									current = createModelElementForParent(grammarAccess.getSourceRule());
                                    								}
                                    								set(
                                    									current,
                                    									"time",
                                    									lv_time_3_0,
                                    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Window_Timebased");
                                    								afterParserOrEnumRuleCall();
                                    							

                                    }


                                    }


                                    }
                                    break;
                                case 3 :
                                    // InternalCQLParser.g:708:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQLParser.g:708:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQLParser.g:709:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQLParser.g:709:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQLParser.g:710:8: lv_tuple_4_0= ruleWindow_Tuplebased
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getTupleWindow_TuplebasedParserRuleCall_0_1_1_2_0());
                                    							
                                    pushFollow(FOLLOW_22);
                                    lv_tuple_4_0=ruleWindow_Tuplebased();

                                    state._fsp--;


                                    								if (current==null) {
                                    									current = createModelElementForParent(grammarAccess.getSourceRule());
                                    								}
                                    								set(
                                    									current,
                                    									"tuple",
                                    									lv_tuple_4_0,
                                    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Window_Tuplebased");
                                    								afterParserOrEnumRuleCall();
                                    							

                                    }


                                    }


                                    }
                                    break;

                            }

                            otherlv_5=(Token)match(input,RightSquareBracket,FOLLOW_23); 

                            					newLeafNode(otherlv_5, grammarAccess.getSourceAccess().getRightSquareBracketKeyword_0_1_2());
                            				

                            }
                            break;

                    }

                    // InternalCQLParser.g:733:4: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==AS) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // InternalCQLParser.g:734:5: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                            {
                            otherlv_6=(Token)match(input,AS,FOLLOW_15); 

                            					newLeafNode(otherlv_6, grammarAccess.getSourceAccess().getASKeyword_0_2_0());
                            				
                            // InternalCQLParser.g:738:5: ( (lv_alias_7_0= ruleAlias ) )
                            // InternalCQLParser.g:739:6: (lv_alias_7_0= ruleAlias )
                            {
                            // InternalCQLParser.g:739:6: (lv_alias_7_0= ruleAlias )
                            // InternalCQLParser.g:740:7: lv_alias_7_0= ruleAlias
                            {

                            							newCompositeNode(grammarAccess.getSourceAccess().getAliasAliasParserRuleCall_0_2_1_0());
                            						
                            pushFollow(FOLLOW_2);
                            lv_alias_7_0=ruleAlias();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getSourceRule());
                            							}
                            							set(
                            								current,
                            								"alias",
                            								lv_alias_7_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
                            							afterParserOrEnumRuleCall();
                            						

                            }


                            }


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:760:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) )
                    {
                    // InternalCQLParser.g:760:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) )
                    // InternalCQLParser.g:761:4: ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) )
                    {
                    // InternalCQLParser.g:761:4: ( (lv_nested_8_0= ruleNestedStatement ) )
                    // InternalCQLParser.g:762:5: (lv_nested_8_0= ruleNestedStatement )
                    {
                    // InternalCQLParser.g:762:5: (lv_nested_8_0= ruleNestedStatement )
                    // InternalCQLParser.g:763:6: lv_nested_8_0= ruleNestedStatement
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNestedNestedStatementParserRuleCall_1_0_0());
                    					
                    pushFollow(FOLLOW_24);
                    lv_nested_8_0=ruleNestedStatement();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"nested",
                    							lv_nested_8_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.NestedStatement");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_9=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_9, grammarAccess.getSourceAccess().getASKeyword_1_1());
                    			
                    // InternalCQLParser.g:784:4: ( (lv_alias_10_0= ruleAlias ) )
                    // InternalCQLParser.g:785:5: (lv_alias_10_0= ruleAlias )
                    {
                    // InternalCQLParser.g:785:5: (lv_alias_10_0= ruleAlias )
                    // InternalCQLParser.g:786:6: lv_alias_10_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getAliasAliasParserRuleCall_1_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_10_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_10_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSource"


    // $ANTLR start "entryRuleSourceName"
    // InternalCQLParser.g:808:1: entryRuleSourceName returns [String current=null] : iv_ruleSourceName= ruleSourceName EOF ;
    public final String entryRuleSourceName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSourceName = null;


        try {
            // InternalCQLParser.g:808:50: (iv_ruleSourceName= ruleSourceName EOF )
            // InternalCQLParser.g:809:2: iv_ruleSourceName= ruleSourceName EOF
            {
             newCompositeNode(grammarAccess.getSourceNameRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSourceName=ruleSourceName();

            state._fsp--;

             current =iv_ruleSourceName.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSourceName"


    // $ANTLR start "ruleSourceName"
    // InternalCQLParser.g:815:1: ruleSourceName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleSourceName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:821:2: (this_ID_0= RULE_ID )
            // InternalCQLParser.g:822:2: this_ID_0= RULE_ID
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            		current.merge(this_ID_0);
            	

            		newLeafNode(this_ID_0, grammarAccess.getSourceNameAccess().getIDTerminalRuleCall());
            	

            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSourceName"


    // $ANTLR start "entryRuleAttribute"
    // InternalCQLParser.g:832:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQLParser.g:832:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQLParser.g:833:2: iv_ruleAttribute= ruleAttribute EOF
            {
             newCompositeNode(grammarAccess.getAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttribute=ruleAttribute();

            state._fsp--;

             current =iv_ruleAttribute; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttribute"


    // $ANTLR start "ruleAttribute"
    // InternalCQLParser.g:839:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:845:2: ( ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:846:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:846:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:847:3: ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:847:3: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQLParser.g:848:4: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQLParser.g:848:4: (lv_name_0_0= ruleAttributeName )
            // InternalCQLParser.g:849:5: lv_name_0_0= ruleAttributeName
            {

            					newCompositeNode(grammarAccess.getAttributeAccess().getNameAttributeNameParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_23);
            lv_name_0_0=ruleAttributeName();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeName");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:866:3: (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==AS) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQLParser.g:867:4: otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:871:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQLParser.g:872:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQLParser.g:872:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQLParser.g:873:6: lv_alias_2_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getAttributeAccess().getAliasAliasParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_2_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAttributeRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttribute"


    // $ANTLR start "entryRuleAttributeWithoutAliasDefinition"
    // InternalCQLParser.g:895:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:895:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQLParser.g:896:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
            {
             newCompositeNode(grammarAccess.getAttributeWithoutAliasDefinitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeWithoutAliasDefinition=ruleAttributeWithoutAliasDefinition();

            state._fsp--;

             current =iv_ruleAttributeWithoutAliasDefinition; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttributeWithoutAliasDefinition"


    // $ANTLR start "ruleAttributeWithoutAliasDefinition"
    // InternalCQLParser.g:902:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleAttributeName ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:908:2: ( ( (lv_name_0_0= ruleAttributeName ) ) )
            // InternalCQLParser.g:909:2: ( (lv_name_0_0= ruleAttributeName ) )
            {
            // InternalCQLParser.g:909:2: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQLParser.g:910:3: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQLParser.g:910:3: (lv_name_0_0= ruleAttributeName )
            // InternalCQLParser.g:911:4: lv_name_0_0= ruleAttributeName
            {

            				newCompositeNode(grammarAccess.getAttributeWithoutAliasDefinitionAccess().getNameAttributeNameParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_name_0_0=ruleAttributeName();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getAttributeWithoutAliasDefinitionRule());
            				}
            				set(
            					current,
            					"name",
            					lv_name_0_0,
            					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeName");
            				afterParserOrEnumRuleCall();
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttributeWithoutAliasDefinition"


    // $ANTLR start "entryRuleAttributeName"
    // InternalCQLParser.g:931:1: entryRuleAttributeName returns [String current=null] : iv_ruleAttributeName= ruleAttributeName EOF ;
    public final String entryRuleAttributeName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAttributeName = null;


        try {
            // InternalCQLParser.g:931:53: (iv_ruleAttributeName= ruleAttributeName EOF )
            // InternalCQLParser.g:932:2: iv_ruleAttributeName= ruleAttributeName EOF
            {
             newCompositeNode(grammarAccess.getAttributeNameRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeName=ruleAttributeName();

            state._fsp--;

             current =iv_ruleAttributeName.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttributeName"


    // $ANTLR start "ruleAttributeName"
    // InternalCQLParser.g:938:1: ruleAttributeName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) ) ;
    public final AntlrDatatypeRuleToken ruleAttributeName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_SourceName_1 = null;

        AntlrDatatypeRuleToken this_SourceName_4 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:944:2: ( (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) ) )
            // InternalCQLParser.g:945:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) )
            {
            // InternalCQLParser.g:945:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) )
            int alt21=3;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==RULE_ID) ) {
                int LA21_1 = input.LA(2);

                if ( (LA21_1==EOF||(LA21_1>=ATTACH && LA21_1<=CREATE)||(LA21_1>=HAVING && LA21_1<=GROUP)||LA21_1==DROP||LA21_1==FROM||(LA21_1>=TRUE && LA21_1<=AND)||(LA21_1>=ExclamationMarkEqualsSign && LA21_1<=AS)||(LA21_1>=IN && LA21_1<=OR)||(LA21_1>=RightParenthesis && LA21_1<=HyphenMinus)||LA21_1==Solidus||(LA21_1>=Semicolon && LA21_1<=GreaterThanSign)||(LA21_1>=RightSquareBracket && LA21_1<=RULE_STRING)) ) {
                    alt21=1;
                }
                else if ( (LA21_1==FullStop) ) {
                    int LA21_3 = input.LA(3);

                    if ( (LA21_3==RULE_ID) ) {
                        alt21=2;
                    }
                    else if ( (LA21_3==Asterisk) ) {
                        alt21=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 3, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }
            switch (alt21) {
                case 1 :
                    // InternalCQLParser.g:946:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:954:3: (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID )
                    {
                    // InternalCQLParser.g:954:3: (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID )
                    // InternalCQLParser.g:955:4: this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getAttributeNameAccess().getSourceNameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_25);
                    this_SourceName_1=ruleSourceName();

                    state._fsp--;


                    				current.merge(this_SourceName_1);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_15); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getAttributeNameAccess().getFullStopKeyword_1_1());
                    			
                    this_ID_3=(Token)match(input,RULE_ID,FOLLOW_2); 

                    				current.merge(this_ID_3);
                    			

                    				newLeafNode(this_ID_3, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_1_2());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:979:3: (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk )
                    {
                    // InternalCQLParser.g:979:3: (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk )
                    // InternalCQLParser.g:980:4: this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk
                    {

                    				newCompositeNode(grammarAccess.getAttributeNameAccess().getSourceNameParserRuleCall_2_0());
                    			
                    pushFollow(FOLLOW_25);
                    this_SourceName_4=ruleSourceName();

                    state._fsp--;


                    				current.merge(this_SourceName_4);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_26); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getAttributeNameAccess().getFullStopKeyword_2_1());
                    			
                    kw=(Token)match(input,Asterisk,FOLLOW_2); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getAttributeNameAccess().getAsteriskKeyword_2_2());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttributeName"


    // $ANTLR start "entryRuleAttributeWithNestedStatement"
    // InternalCQLParser.g:1005:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQLParser.g:1005:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQLParser.g:1006:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
            {
             newCompositeNode(grammarAccess.getAttributeWithNestedStatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeWithNestedStatement=ruleAttributeWithNestedStatement();

            state._fsp--;

             current =iv_ruleAttributeWithNestedStatement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttributeWithNestedStatement"


    // $ANTLR start "ruleAttributeWithNestedStatement"
    // InternalCQLParser.g:1012:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1018:2: ( ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) ) )
            // InternalCQLParser.g:1019:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) )
            {
            // InternalCQLParser.g:1019:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) )
            // InternalCQLParser.g:1020:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) )
            {
            // InternalCQLParser.g:1020:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1021:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1021:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1022:5: lv_value_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getAttributeWithNestedStatementAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_27);
            lv_value_0_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeWithNestedStatementRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,IN,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeWithNestedStatementAccess().getINKeyword_1());
            		
            // InternalCQLParser.g:1043:3: ( (lv_nested_2_0= ruleNestedStatement ) )
            // InternalCQLParser.g:1044:4: (lv_nested_2_0= ruleNestedStatement )
            {
            // InternalCQLParser.g:1044:4: (lv_nested_2_0= ruleNestedStatement )
            // InternalCQLParser.g:1045:5: lv_nested_2_0= ruleNestedStatement
            {

            					newCompositeNode(grammarAccess.getAttributeWithNestedStatementAccess().getNestedNestedStatementParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_nested_2_0=ruleNestedStatement();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeWithNestedStatementRule());
            					}
            					set(
            						current,
            						"nested",
            						lv_nested_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.NestedStatement");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttributeWithNestedStatement"


    // $ANTLR start "entryRuleSelectExpression"
    // InternalCQLParser.g:1066:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQLParser.g:1066:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQLParser.g:1067:2: iv_ruleSelectExpression= ruleSelectExpression EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpression=ruleSelectExpression();

            state._fsp--;

             current =iv_ruleSelectExpression; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSelectExpression"


    // $ANTLR start "ruleSelectExpression"
    // InternalCQLParser.g:1073:1: ruleSelectExpression returns [EObject current=null] : ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSelectExpression() throws RecognitionException {
        EObject current = null;

        Token lv_operators_1_1=null;
        Token lv_operators_1_2=null;
        Token lv_operators_1_3=null;
        Token lv_operators_1_4=null;
        Token lv_operators_4_1=null;
        Token lv_operators_4_2=null;
        Token lv_operators_4_3=null;
        Token lv_operators_4_4=null;
        Token otherlv_6=null;
        EObject lv_expressions_0_0 = null;

        EObject lv_expressions_2_1 = null;

        EObject lv_expressions_2_2 = null;

        EObject lv_expressions_3_0 = null;

        EObject lv_expressions_5_1 = null;

        EObject lv_expressions_5_2 = null;

        EObject lv_alias_7_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1079:2: ( ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1080:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1080:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1081:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1081:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) )
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==RULE_ID) ) {
                int LA28_1 = input.LA(2);

                if ( ((LA28_1>=Asterisk && LA28_1<=PlusSign)||(LA28_1>=HyphenMinus && LA28_1<=Solidus)) ) {
                    alt28=2;
                }
                else if ( (LA28_1==LeftParenthesis) ) {
                    alt28=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 28, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA28_0==FALSE||LA28_0==TRUE||(LA28_0>=RULE_INT && LA28_0<=RULE_STRING)) ) {
                alt28=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;
            }
            switch (alt28) {
                case 1 :
                    // InternalCQLParser.g:1082:4: ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* )
                    {
                    // InternalCQLParser.g:1082:4: ( ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* )
                    // InternalCQLParser.g:1083:5: ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )*
                    {
                    // InternalCQLParser.g:1083:5: ( (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant ) )
                    // InternalCQLParser.g:1084:6: (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant )
                    {
                    // InternalCQLParser.g:1084:6: (lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant )
                    // InternalCQLParser.g:1085:7: lv_expressions_0_0= ruleExpressionComponentFunctionOrConstant
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentFunctionOrConstantParserRuleCall_0_0_0_0());
                    						
                    pushFollow(FOLLOW_28);
                    lv_expressions_0_0=ruleExpressionComponentFunctionOrConstant();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_0_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentFunctionOrConstant");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1102:5: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) ) )*
                    loop24:
                    do {
                        int alt24=2;
                        int LA24_0 = input.LA(1);

                        if ( ((LA24_0>=Asterisk && LA24_0<=PlusSign)||LA24_0==HyphenMinus||LA24_0==Solidus) ) {
                            alt24=1;
                        }


                        switch (alt24) {
                    	case 1 :
                    	    // InternalCQLParser.g:1103:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    {
                    	    // InternalCQLParser.g:1103:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1104:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1104:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    // InternalCQLParser.g:1105:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1105:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
                    	    int alt22=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt22=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt22=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt22=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt22=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 22, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt22) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1106:9: lv_operators_1_1= PlusSign
                    	            {
                    	            lv_operators_1_1=(Token)match(input,PlusSign,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_1, grammarAccess.getSelectExpressionAccess().getOperatorsPlusSignKeyword_0_0_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1117:9: lv_operators_1_2= HyphenMinus
                    	            {
                    	            lv_operators_1_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_2, grammarAccess.getSelectExpressionAccess().getOperatorsHyphenMinusKeyword_0_0_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQLParser.g:1128:9: lv_operators_1_3= Asterisk
                    	            {
                    	            lv_operators_1_3=(Token)match(input,Asterisk,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_3, grammarAccess.getSelectExpressionAccess().getOperatorsAsteriskKeyword_0_0_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQLParser.g:1139:9: lv_operators_1_4= Solidus
                    	            {
                    	            lv_operators_1_4=(Token)match(input,Solidus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_4, grammarAccess.getSelectExpressionAccess().getOperatorsSolidusKeyword_0_0_1_0_0_3());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_4, null);
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }

                    	    // InternalCQLParser.g:1152:6: ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    // InternalCQLParser.g:1153:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    {
                    	    // InternalCQLParser.g:1153:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    // InternalCQLParser.g:1154:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction )
                    	    {
                    	    // InternalCQLParser.g:1154:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction )
                    	    int alt23=2;
                    	    int LA23_0 = input.LA(1);

                    	    if ( (LA23_0==FALSE||LA23_0==TRUE||(LA23_0>=RULE_INT && LA23_0<=RULE_STRING)) ) {
                    	        alt23=1;
                    	    }
                    	    else if ( (LA23_0==RULE_ID) ) {
                    	        int LA23_2 = input.LA(2);

                    	        if ( (LA23_2==EOF||LA23_2==FALSE||LA23_2==FROM||LA23_2==TRUE||LA23_2==AS||(LA23_2>=Asterisk && LA23_2<=Solidus)||(LA23_2>=RULE_ID && LA23_2<=RULE_STRING)) ) {
                    	            alt23=1;
                    	        }
                    	        else if ( (LA23_2==LeftParenthesis) ) {
                    	            alt23=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 23, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 23, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt23) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1155:9: lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_0_0_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_28);
                    	            lv_expressions_2_1=ruleExpressionComponentConstantOrAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_2_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1171:9: lv_expressions_2_2= ruleExpressionComponentOnlyWithFunction
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentOnlyWithFunctionParserRuleCall_0_0_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_28);
                    	            lv_expressions_2_2=ruleExpressionComponentOnlyWithFunction();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_2_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithFunction");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop24;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1192:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1192:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ )
                    // InternalCQLParser.g:1193:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+
                    {
                    // InternalCQLParser.g:1193:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute ) )
                    // InternalCQLParser.g:1194:6: (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute )
                    {
                    // InternalCQLParser.g:1194:6: (lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute )
                    // InternalCQLParser.g:1195:7: lv_expressions_3_0= ruleExpressionComponentOnlyWithAttribute
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentOnlyWithAttributeParserRuleCall_0_1_0_0());
                    						
                    pushFollow(FOLLOW_29);
                    lv_expressions_3_0=ruleExpressionComponentOnlyWithAttribute();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_3_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithAttribute");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1212:5: ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+
                    int cnt27=0;
                    loop27:
                    do {
                        int alt27=2;
                        int LA27_0 = input.LA(1);

                        if ( ((LA27_0>=Asterisk && LA27_0<=PlusSign)||LA27_0==HyphenMinus||LA27_0==Solidus) ) {
                            alt27=1;
                        }


                        switch (alt27) {
                    	case 1 :
                    	    // InternalCQLParser.g:1213:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    {
                    	    // InternalCQLParser.g:1213:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1214:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1214:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    // InternalCQLParser.g:1215:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1215:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
                    	    int alt25=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt25=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt25=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt25=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt25=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 25, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt25) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1216:9: lv_operators_4_1= PlusSign
                    	            {
                    	            lv_operators_4_1=(Token)match(input,PlusSign,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_1, grammarAccess.getSelectExpressionAccess().getOperatorsPlusSignKeyword_0_1_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1227:9: lv_operators_4_2= HyphenMinus
                    	            {
                    	            lv_operators_4_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_2, grammarAccess.getSelectExpressionAccess().getOperatorsHyphenMinusKeyword_0_1_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQLParser.g:1238:9: lv_operators_4_3= Asterisk
                    	            {
                    	            lv_operators_4_3=(Token)match(input,Asterisk,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_3, grammarAccess.getSelectExpressionAccess().getOperatorsAsteriskKeyword_0_1_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQLParser.g:1249:9: lv_operators_4_4= Solidus
                    	            {
                    	            lv_operators_4_4=(Token)match(input,Solidus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_4, grammarAccess.getSelectExpressionAccess().getOperatorsSolidusKeyword_0_1_1_0_0_3());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_4, null);
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }

                    	    // InternalCQLParser.g:1262:6: ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    // InternalCQLParser.g:1263:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    {
                    	    // InternalCQLParser.g:1263:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    // InternalCQLParser.g:1264:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction )
                    	    {
                    	    // InternalCQLParser.g:1264:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction )
                    	    int alt26=2;
                    	    int LA26_0 = input.LA(1);

                    	    if ( (LA26_0==FALSE||LA26_0==TRUE||(LA26_0>=RULE_INT && LA26_0<=RULE_STRING)) ) {
                    	        alt26=1;
                    	    }
                    	    else if ( (LA26_0==RULE_ID) ) {
                    	        int LA26_2 = input.LA(2);

                    	        if ( (LA26_2==EOF||LA26_2==FALSE||LA26_2==FROM||LA26_2==TRUE||LA26_2==AS||(LA26_2>=Asterisk && LA26_2<=Solidus)||(LA26_2>=RULE_ID && LA26_2<=RULE_STRING)) ) {
                    	            alt26=1;
                    	        }
                    	        else if ( (LA26_2==LeftParenthesis) ) {
                    	            alt26=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 26, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 26, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt26) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1265:9: lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_0_1_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_28);
                    	            lv_expressions_5_1=ruleExpressionComponentConstantOrAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_5_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1281:9: lv_expressions_5_2= ruleExpressionComponentOnlyWithFunction
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentOnlyWithFunctionParserRuleCall_0_1_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_28);
                    	            lv_expressions_5_2=ruleExpressionComponentOnlyWithFunction();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_5_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithFunction");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt27 >= 1 ) break loop27;
                                EarlyExitException eee =
                                    new EarlyExitException(27, input);
                                throw eee;
                        }
                        cnt27++;
                    } while (true);


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:1302:3: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==AS) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // InternalCQLParser.g:1303:4: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_6, grammarAccess.getSelectExpressionAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:1307:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQLParser.g:1308:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1308:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQLParser.g:1309:6: lv_alias_7_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionAccess().getAliasAliasParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_7_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_7_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSelectExpression"


    // $ANTLR start "entryRuleSelectExpressionWithoutAliasDefinition"
    // InternalCQLParser.g:1331:1: entryRuleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF ;
    public final EObject entryRuleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:1331:79: (iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF )
            // InternalCQLParser.g:1332:2: iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionWithoutAliasDefinition=ruleSelectExpressionWithoutAliasDefinition();

            state._fsp--;

             current =iv_ruleSelectExpressionWithoutAliasDefinition; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSelectExpressionWithoutAliasDefinition"


    // $ANTLR start "ruleSelectExpressionWithoutAliasDefinition"
    // InternalCQLParser.g:1338:1: ruleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) ) ;
    public final EObject ruleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        Token lv_operators_2_1=null;
        Token lv_operators_2_2=null;
        Token lv_operators_2_3=null;
        Token lv_operators_2_4=null;
        Token lv_operators_5_1=null;
        Token lv_operators_5_2=null;
        Token lv_operators_5_3=null;
        Token lv_operators_5_4=null;
        EObject this_SelectExpressionWithOnlyAttributeOrConstant_0 = null;

        EObject lv_expressions_1_0 = null;

        EObject lv_expressions_3_1 = null;

        EObject lv_expressions_3_2 = null;

        EObject lv_expressions_4_0 = null;

        EObject lv_expressions_6_1 = null;

        EObject lv_expressions_6_2 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1344:2: ( (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) ) )
            // InternalCQLParser.g:1345:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) )
            {
            // InternalCQLParser.g:1345:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) ) )
            // InternalCQLParser.g:1346:3: this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) )
            {

            			newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getSelectExpressionWithOnlyAttributeOrConstantParserRuleCall_0());
            		
            pushFollow(FOLLOW_5);
            this_SelectExpressionWithOnlyAttributeOrConstant_0=ruleSelectExpressionWithOnlyAttributeOrConstant();

            state._fsp--;


            			current = this_SelectExpressionWithOnlyAttributeOrConstant_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1354:3: ( ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ ) )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==RULE_ID) ) {
                int LA36_1 = input.LA(2);

                if ( ((LA36_1>=Asterisk && LA36_1<=PlusSign)||(LA36_1>=HyphenMinus && LA36_1<=Solidus)) ) {
                    alt36=2;
                }
                else if ( (LA36_1==LeftParenthesis) ) {
                    alt36=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 36, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA36_0==FALSE||LA36_0==TRUE||(LA36_0>=RULE_INT && LA36_0<=RULE_STRING)) ) {
                alt36=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // InternalCQLParser.g:1355:4: ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* )
                    {
                    // InternalCQLParser.g:1355:4: ( ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )* )
                    // InternalCQLParser.g:1356:5: ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )*
                    {
                    // InternalCQLParser.g:1356:5: ( (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant ) )
                    // InternalCQLParser.g:1357:6: (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant )
                    {
                    // InternalCQLParser.g:1357:6: (lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant )
                    // InternalCQLParser.g:1358:7: lv_expressions_1_0= ruleExpressionComponentFunctionOrConstant
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentFunctionOrConstantParserRuleCall_1_0_0_0());
                    						
                    pushFollow(FOLLOW_30);
                    lv_expressions_1_0=ruleExpressionComponentFunctionOrConstant();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_1_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentFunctionOrConstant");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1375:5: ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) ) )*
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( ((LA32_0>=Asterisk && LA32_0<=PlusSign)||LA32_0==HyphenMinus||LA32_0==Solidus) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // InternalCQLParser.g:1376:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    {
                    	    // InternalCQLParser.g:1376:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1377:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1377:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    // InternalCQLParser.g:1378:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1378:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
                    	    int alt30=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt30=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt30=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt30=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt30=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 30, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt30) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1379:9: lv_operators_2_1= PlusSign
                    	            {
                    	            lv_operators_2_1=(Token)match(input,PlusSign,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_1, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsPlusSignKeyword_1_0_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1390:9: lv_operators_2_2= HyphenMinus
                    	            {
                    	            lv_operators_2_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_2, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsHyphenMinusKeyword_1_0_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQLParser.g:1401:9: lv_operators_2_3= Asterisk
                    	            {
                    	            lv_operators_2_3=(Token)match(input,Asterisk,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_3, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAsteriskKeyword_1_0_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQLParser.g:1412:9: lv_operators_2_4= Solidus
                    	            {
                    	            lv_operators_2_4=(Token)match(input,Solidus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_4, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsSolidusKeyword_1_0_1_0_0_3());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_4, null);
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }

                    	    // InternalCQLParser.g:1425:6: ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    // InternalCQLParser.g:1426:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    {
                    	    // InternalCQLParser.g:1426:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    // InternalCQLParser.g:1427:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction )
                    	    {
                    	    // InternalCQLParser.g:1427:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction )
                    	    int alt31=2;
                    	    int LA31_0 = input.LA(1);

                    	    if ( (LA31_0==FALSE||LA31_0==TRUE||(LA31_0>=RULE_INT && LA31_0<=RULE_STRING)) ) {
                    	        alt31=1;
                    	    }
                    	    else if ( (LA31_0==RULE_ID) ) {
                    	        int LA31_2 = input.LA(2);

                    	        if ( (LA31_2==EOF||(LA31_2>=RightParenthesis && LA31_2<=PlusSign)||(LA31_2>=HyphenMinus && LA31_2<=Solidus)) ) {
                    	            alt31=1;
                    	        }
                    	        else if ( (LA31_2==LeftParenthesis) ) {
                    	            alt31=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 31, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 31, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt31) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1428:9: lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_1_0_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_30);
                    	            lv_expressions_3_1=ruleExpressionComponentConstantOrAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_3_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1444:9: lv_expressions_3_2= ruleExpressionComponentOnlyWithFunction
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentOnlyWithFunctionParserRuleCall_1_0_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_30);
                    	            lv_expressions_3_2=ruleExpressionComponentOnlyWithFunction();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_3_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithFunction");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop32;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1465:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1465:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+ )
                    // InternalCQLParser.g:1466:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+
                    {
                    // InternalCQLParser.g:1466:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute ) )
                    // InternalCQLParser.g:1467:6: (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute )
                    {
                    // InternalCQLParser.g:1467:6: (lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute )
                    // InternalCQLParser.g:1468:7: lv_expressions_4_0= ruleExpressionComponentOnlyWithAttribute
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentOnlyWithAttributeParserRuleCall_1_1_0_0());
                    						
                    pushFollow(FOLLOW_29);
                    lv_expressions_4_0=ruleExpressionComponentOnlyWithAttribute();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_4_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithAttribute");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1485:5: ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) ) )+
                    int cnt35=0;
                    loop35:
                    do {
                        int alt35=2;
                        int LA35_0 = input.LA(1);

                        if ( ((LA35_0>=Asterisk && LA35_0<=PlusSign)||LA35_0==HyphenMinus||LA35_0==Solidus) ) {
                            alt35=1;
                        }


                        switch (alt35) {
                    	case 1 :
                    	    // InternalCQLParser.g:1486:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    {
                    	    // InternalCQLParser.g:1486:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1487:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1487:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    // InternalCQLParser.g:1488:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1488:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
                    	    int alt33=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt33=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt33=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt33=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt33=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 33, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt33) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1489:9: lv_operators_5_1= PlusSign
                    	            {
                    	            lv_operators_5_1=(Token)match(input,PlusSign,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_1, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsPlusSignKeyword_1_1_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1500:9: lv_operators_5_2= HyphenMinus
                    	            {
                    	            lv_operators_5_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_2, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsHyphenMinusKeyword_1_1_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQLParser.g:1511:9: lv_operators_5_3= Asterisk
                    	            {
                    	            lv_operators_5_3=(Token)match(input,Asterisk,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_3, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAsteriskKeyword_1_1_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQLParser.g:1522:9: lv_operators_5_4= Solidus
                    	            {
                    	            lv_operators_5_4=(Token)match(input,Solidus,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_4, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsSolidusKeyword_1_1_1_0_0_3());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_4, null);
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }

                    	    // InternalCQLParser.g:1535:6: ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) ) )
                    	    // InternalCQLParser.g:1536:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    {
                    	    // InternalCQLParser.g:1536:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction ) )
                    	    // InternalCQLParser.g:1537:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction )
                    	    {
                    	    // InternalCQLParser.g:1537:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction )
                    	    int alt34=2;
                    	    int LA34_0 = input.LA(1);

                    	    if ( (LA34_0==FALSE||LA34_0==TRUE||(LA34_0>=RULE_INT && LA34_0<=RULE_STRING)) ) {
                    	        alt34=1;
                    	    }
                    	    else if ( (LA34_0==RULE_ID) ) {
                    	        int LA34_2 = input.LA(2);

                    	        if ( (LA34_2==EOF||(LA34_2>=RightParenthesis && LA34_2<=PlusSign)||(LA34_2>=HyphenMinus && LA34_2<=Solidus)) ) {
                    	            alt34=1;
                    	        }
                    	        else if ( (LA34_2==LeftParenthesis) ) {
                    	            alt34=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 34, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 34, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt34) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1538:9: lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_1_1_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_30);
                    	            lv_expressions_6_1=ruleExpressionComponentConstantOrAttribute();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_6_1,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQLParser.g:1554:9: lv_expressions_6_2= ruleExpressionComponentOnlyWithFunction
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentOnlyWithFunctionParserRuleCall_1_1_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_30);
                    	            lv_expressions_6_2=ruleExpressionComponentOnlyWithFunction();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_6_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyWithFunction");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt35 >= 1 ) break loop35;
                                EarlyExitException eee =
                                    new EarlyExitException(35, input);
                                throw eee;
                        }
                        cnt35++;
                    } while (true);


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSelectExpressionWithoutAliasDefinition"


    // $ANTLR start "entryRuleSelectExpressionWithOnlyAttributeOrConstant"
    // InternalCQLParser.g:1579:1: entryRuleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF ;
    public final EObject entryRuleSelectExpressionWithOnlyAttributeOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithOnlyAttributeOrConstant = null;


        try {
            // InternalCQLParser.g:1579:84: (iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF )
            // InternalCQLParser.g:1580:2: iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionWithOnlyAttributeOrConstant=ruleSelectExpressionWithOnlyAttributeOrConstant();

            state._fsp--;

             current =iv_ruleSelectExpressionWithOnlyAttributeOrConstant; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSelectExpressionWithOnlyAttributeOrConstant"


    // $ANTLR start "ruleSelectExpressionWithOnlyAttributeOrConstant"
    // InternalCQLParser.g:1586:1: ruleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) ;
    public final EObject ruleSelectExpressionWithOnlyAttributeOrConstant() throws RecognitionException {
        EObject current = null;

        Token lv_operators_1_1=null;
        Token lv_operators_1_2=null;
        Token lv_operators_1_3=null;
        Token lv_operators_1_4=null;
        EObject lv_expressions_0_0 = null;

        EObject lv_expressions_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1592:2: ( ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) )
            // InternalCQLParser.g:1593:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            {
            // InternalCQLParser.g:1593:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            // InternalCQLParser.g:1594:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            {
            // InternalCQLParser.g:1594:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) )
            // InternalCQLParser.g:1595:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            {
            // InternalCQLParser.g:1595:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            // InternalCQLParser.g:1596:5: lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute
            {

            					newCompositeNode(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_30);
            lv_expressions_0_0=ruleExpressionComponentConstantOrAttribute();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            					}
            					add(
            						current,
            						"expressions",
            						lv_expressions_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:1613:3: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            loop38:
            do {
                int alt38=2;
                int LA38_0 = input.LA(1);

                if ( ((LA38_0>=Asterisk && LA38_0<=PlusSign)||LA38_0==HyphenMinus||LA38_0==Solidus) ) {
                    alt38=1;
                }


                switch (alt38) {
            	case 1 :
            	    // InternalCQLParser.g:1614:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    {
            	    // InternalCQLParser.g:1614:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
            	    // InternalCQLParser.g:1615:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    {
            	    // InternalCQLParser.g:1615:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    // InternalCQLParser.g:1616:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    {
            	    // InternalCQLParser.g:1616:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    int alt37=4;
            	    switch ( input.LA(1) ) {
            	    case PlusSign:
            	        {
            	        alt37=1;
            	        }
            	        break;
            	    case HyphenMinus:
            	        {
            	        alt37=2;
            	        }
            	        break;
            	    case Asterisk:
            	        {
            	        alt37=3;
            	        }
            	        break;
            	    case Solidus:
            	        {
            	        alt37=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 37, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt37) {
            	        case 1 :
            	            // InternalCQLParser.g:1617:7: lv_operators_1_1= PlusSign
            	            {
            	            lv_operators_1_1=(Token)match(input,PlusSign,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_1, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsPlusSignKeyword_1_0_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:1628:7: lv_operators_1_2= HyphenMinus
            	            {
            	            lv_operators_1_2=(Token)match(input,HyphenMinus,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_2, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsHyphenMinusKeyword_1_0_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQLParser.g:1639:7: lv_operators_1_3= Asterisk
            	            {
            	            lv_operators_1_3=(Token)match(input,Asterisk,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_3, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsAsteriskKeyword_1_0_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQLParser.g:1650:7: lv_operators_1_4= Solidus
            	            {
            	            lv_operators_1_4=(Token)match(input,Solidus,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_4, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsSolidusKeyword_1_0_0_3());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_4, null);
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQLParser.g:1663:4: ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    // InternalCQLParser.g:1664:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    {
            	    // InternalCQLParser.g:1664:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    // InternalCQLParser.g:1665:6: lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_30);
            	    lv_expressions_2_0=ruleExpressionComponentConstantOrAttribute();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	    						}
            	    						add(
            	    							current,
            	    							"expressions",
            	    							lv_expressions_2_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentConstantOrAttribute");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop38;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSelectExpressionWithOnlyAttributeOrConstant"


    // $ANTLR start "entryRuleFunction"
    // InternalCQLParser.g:1687:1: entryRuleFunction returns [EObject current=null] : iv_ruleFunction= ruleFunction EOF ;
    public final EObject entryRuleFunction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunction = null;


        try {
            // InternalCQLParser.g:1687:49: (iv_ruleFunction= ruleFunction EOF )
            // InternalCQLParser.g:1688:2: iv_ruleFunction= ruleFunction EOF
            {
             newCompositeNode(grammarAccess.getFunctionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFunction=ruleFunction();

            state._fsp--;

             current =iv_ruleFunction; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFunction"


    // $ANTLR start "ruleFunction"
    // InternalCQLParser.g:1694:1: ruleFunction returns [EObject current=null] : ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) ;
    public final EObject ruleFunction() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_value_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1700:2: ( ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) )
            // InternalCQLParser.g:1701:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            {
            // InternalCQLParser.g:1701:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            // InternalCQLParser.g:1702:3: () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis
            {
            // InternalCQLParser.g:1702:3: ()
            // InternalCQLParser.g:1703:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getFunctionAccess().getFunctionAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1709:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:1710:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:1710:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:1711:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_31); 

            					newLeafNode(lv_name_1_0, grammarAccess.getFunctionAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFunctionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,LeftParenthesis,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getFunctionAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQLParser.g:1731:3: ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
            // InternalCQLParser.g:1732:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1732:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            // InternalCQLParser.g:1733:5: lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getFunctionAccess().getValueSelectExpressionWithoutAliasDefinitionParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_19);
            lv_value_3_0=ruleSelectExpressionWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getFunctionRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_4=(Token)match(input,RightParenthesis,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getFunctionAccess().getRightParenthesisKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFunction"


    // $ANTLR start "entryRuleExpressionComponentConstantOrAttribute"
    // InternalCQLParser.g:1758:1: entryRuleExpressionComponentConstantOrAttribute returns [EObject current=null] : iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF ;
    public final EObject entryRuleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentConstantOrAttribute = null;


        try {
            // InternalCQLParser.g:1758:79: (iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF )
            // InternalCQLParser.g:1759:2: iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentConstantOrAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentConstantOrAttribute=ruleExpressionComponentConstantOrAttribute();

            state._fsp--;

             current =iv_ruleExpressionComponentConstantOrAttribute; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpressionComponentConstantOrAttribute"


    // $ANTLR start "ruleExpressionComponentConstantOrAttribute"
    // InternalCQLParser.g:1765:1: ruleExpressionComponentConstantOrAttribute returns [EObject current=null] : ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) ;
    public final EObject ruleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1771:2: ( ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) )
            // InternalCQLParser.g:1772:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            {
            // InternalCQLParser.g:1772:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==FALSE||LA39_0==TRUE||(LA39_0>=RULE_INT && LA39_0<=RULE_STRING)) ) {
                alt39=1;
            }
            else if ( (LA39_0==RULE_ID) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // InternalCQLParser.g:1773:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1773:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1774:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1774:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1775:5: lv_value_0_0= ruleAtomicWithoutAttributeRef
                    {

                    					newCompositeNode(grammarAccess.getExpressionComponentConstantOrAttributeAccess().getValueAtomicWithoutAttributeRefParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_value_0_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getExpressionComponentConstantOrAttributeRule());
                    					}
                    					set(
                    						current,
                    						"value",
                    						lv_value_0_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1793:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:1793:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQLParser.g:1794:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:1794:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQLParser.g:1795:5: lv_value_1_0= ruleAttributeWithoutAliasDefinition
                    {

                    					newCompositeNode(grammarAccess.getExpressionComponentConstantOrAttributeAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_value_1_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getExpressionComponentConstantOrAttributeRule());
                    					}
                    					set(
                    						current,
                    						"value",
                    						lv_value_1_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpressionComponentConstantOrAttribute"


    // $ANTLR start "entryRuleExpressionComponentFunctionOrConstant"
    // InternalCQLParser.g:1816:1: entryRuleExpressionComponentFunctionOrConstant returns [EObject current=null] : iv_ruleExpressionComponentFunctionOrConstant= ruleExpressionComponentFunctionOrConstant EOF ;
    public final EObject entryRuleExpressionComponentFunctionOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentFunctionOrConstant = null;


        try {
            // InternalCQLParser.g:1816:78: (iv_ruleExpressionComponentFunctionOrConstant= ruleExpressionComponentFunctionOrConstant EOF )
            // InternalCQLParser.g:1817:2: iv_ruleExpressionComponentFunctionOrConstant= ruleExpressionComponentFunctionOrConstant EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentFunctionOrConstantRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentFunctionOrConstant=ruleExpressionComponentFunctionOrConstant();

            state._fsp--;

             current =iv_ruleExpressionComponentFunctionOrConstant; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpressionComponentFunctionOrConstant"


    // $ANTLR start "ruleExpressionComponentFunctionOrConstant"
    // InternalCQLParser.g:1823:1: ruleExpressionComponentFunctionOrConstant returns [EObject current=null] : ( (this_Function_0= ruleFunction () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) ;
    public final EObject ruleExpressionComponentFunctionOrConstant() throws RecognitionException {
        EObject current = null;

        EObject this_Function_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1829:2: ( ( (this_Function_0= ruleFunction () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQLParser.g:1830:2: ( (this_Function_0= ruleFunction () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQLParser.g:1830:2: ( (this_Function_0= ruleFunction () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==RULE_ID) ) {
                alt40=1;
            }
            else if ( (LA40_0==FALSE||LA40_0==TRUE||(LA40_0>=RULE_INT && LA40_0<=RULE_STRING)) ) {
                alt40=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // InternalCQLParser.g:1831:3: (this_Function_0= ruleFunction () )
                    {
                    // InternalCQLParser.g:1831:3: (this_Function_0= ruleFunction () )
                    // InternalCQLParser.g:1832:4: this_Function_0= ruleFunction ()
                    {

                    				newCompositeNode(grammarAccess.getExpressionComponentFunctionOrConstantAccess().getFunctionParserRuleCall_0_0());
                    			
                    pushFollow(FOLLOW_2);
                    this_Function_0=ruleFunction();

                    state._fsp--;


                    				current = this_Function_0;
                    				afterParserOrEnumRuleCall();
                    			
                    // InternalCQLParser.g:1840:4: ()
                    // InternalCQLParser.g:1841:5: 
                    {

                    					current = forceCreateModelElementAndSet(
                    						grammarAccess.getExpressionComponentFunctionOrConstantAccess().getExpressionComponentValueAction_0_1(),
                    						current);
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1849:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1849:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1850:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1850:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1851:5: lv_value_2_0= ruleAtomicWithoutAttributeRef
                    {

                    					newCompositeNode(grammarAccess.getExpressionComponentFunctionOrConstantAccess().getValueAtomicWithoutAttributeRefParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_value_2_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getExpressionComponentFunctionOrConstantRule());
                    					}
                    					set(
                    						current,
                    						"value",
                    						lv_value_2_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpressionComponentFunctionOrConstant"


    // $ANTLR start "entryRuleExpressionComponentOnlyWithAttribute"
    // InternalCQLParser.g:1872:1: entryRuleExpressionComponentOnlyWithAttribute returns [EObject current=null] : iv_ruleExpressionComponentOnlyWithAttribute= ruleExpressionComponentOnlyWithAttribute EOF ;
    public final EObject entryRuleExpressionComponentOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlyWithAttribute = null;


        try {
            // InternalCQLParser.g:1872:77: (iv_ruleExpressionComponentOnlyWithAttribute= ruleExpressionComponentOnlyWithAttribute EOF )
            // InternalCQLParser.g:1873:2: iv_ruleExpressionComponentOnlyWithAttribute= ruleExpressionComponentOnlyWithAttribute EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentOnlyWithAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentOnlyWithAttribute=ruleExpressionComponentOnlyWithAttribute();

            state._fsp--;

             current =iv_ruleExpressionComponentOnlyWithAttribute; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpressionComponentOnlyWithAttribute"


    // $ANTLR start "ruleExpressionComponentOnlyWithAttribute"
    // InternalCQLParser.g:1879:1: ruleExpressionComponentOnlyWithAttribute returns [EObject current=null] : ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) ;
    public final EObject ruleExpressionComponentOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1885:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) )
            // InternalCQLParser.g:1886:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            {
            // InternalCQLParser.g:1886:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1887:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1887:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1888:4: lv_value_0_0= ruleAttributeWithoutAliasDefinition
            {

            				newCompositeNode(grammarAccess.getExpressionComponentOnlyWithAttributeAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_value_0_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getExpressionComponentOnlyWithAttributeRule());
            				}
            				set(
            					current,
            					"value",
            					lv_value_0_0,
            					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
            				afterParserOrEnumRuleCall();
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpressionComponentOnlyWithAttribute"


    // $ANTLR start "entryRuleExpressionComponentOnlyWithFunction"
    // InternalCQLParser.g:1908:1: entryRuleExpressionComponentOnlyWithFunction returns [EObject current=null] : iv_ruleExpressionComponentOnlyWithFunction= ruleExpressionComponentOnlyWithFunction EOF ;
    public final EObject entryRuleExpressionComponentOnlyWithFunction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlyWithFunction = null;


        try {
            // InternalCQLParser.g:1908:76: (iv_ruleExpressionComponentOnlyWithFunction= ruleExpressionComponentOnlyWithFunction EOF )
            // InternalCQLParser.g:1909:2: iv_ruleExpressionComponentOnlyWithFunction= ruleExpressionComponentOnlyWithFunction EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentOnlyWithFunctionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentOnlyWithFunction=ruleExpressionComponentOnlyWithFunction();

            state._fsp--;

             current =iv_ruleExpressionComponentOnlyWithFunction; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpressionComponentOnlyWithFunction"


    // $ANTLR start "ruleExpressionComponentOnlyWithFunction"
    // InternalCQLParser.g:1915:1: ruleExpressionComponentOnlyWithFunction returns [EObject current=null] : (this_Function_0= ruleFunction () ) ;
    public final EObject ruleExpressionComponentOnlyWithFunction() throws RecognitionException {
        EObject current = null;

        EObject this_Function_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1921:2: ( (this_Function_0= ruleFunction () ) )
            // InternalCQLParser.g:1922:2: (this_Function_0= ruleFunction () )
            {
            // InternalCQLParser.g:1922:2: (this_Function_0= ruleFunction () )
            // InternalCQLParser.g:1923:3: this_Function_0= ruleFunction ()
            {

            			newCompositeNode(grammarAccess.getExpressionComponentOnlyWithFunctionAccess().getFunctionParserRuleCall_0());
            		
            pushFollow(FOLLOW_2);
            this_Function_0=ruleFunction();

            state._fsp--;


            			current = this_Function_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1931:3: ()
            // InternalCQLParser.g:1932:4: 
            {

            				current = forceCreateModelElementAndSet(
            					grammarAccess.getExpressionComponentOnlyWithFunctionAccess().getExpressionComponentValueAction_1(),
            					current);
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpressionComponentOnlyWithFunction"


    // $ANTLR start "entryRuleAlias"
    // InternalCQLParser.g:1942:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQLParser.g:1942:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQLParser.g:1943:2: iv_ruleAlias= ruleAlias EOF
            {
             newCompositeNode(grammarAccess.getAliasRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAlias=ruleAlias();

            state._fsp--;

             current =iv_ruleAlias; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAlias"


    // $ANTLR start "ruleAlias"
    // InternalCQLParser.g:1949:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1955:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQLParser.g:1956:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQLParser.g:1956:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:1957:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:1957:3: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:1958:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getAliasAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getAliasRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAlias"


    // $ANTLR start "entryRuleCreateParameters"
    // InternalCQLParser.g:1977:1: entryRuleCreateParameters returns [EObject current=null] : iv_ruleCreateParameters= ruleCreateParameters EOF ;
    public final EObject entryRuleCreateParameters() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateParameters = null;


        try {
            // InternalCQLParser.g:1977:57: (iv_ruleCreateParameters= ruleCreateParameters EOF )
            // InternalCQLParser.g:1978:2: iv_ruleCreateParameters= ruleCreateParameters EOF
            {
             newCompositeNode(grammarAccess.getCreateParametersRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateParameters=ruleCreateParameters();

            state._fsp--;

             current =iv_ruleCreateParameters; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCreateParameters"


    // $ANTLR start "ruleCreateParameters"
    // InternalCQLParser.g:1984:1: ruleCreateParameters returns [EObject current=null] : (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) ;
    public final EObject ruleCreateParameters() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_wrapper_1_0=null;
        Token otherlv_2=null;
        Token lv_protocol_3_0=null;
        Token otherlv_4=null;
        Token lv_transport_5_0=null;
        Token otherlv_6=null;
        Token lv_datahandler_7_0=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token lv_keys_10_0=null;
        Token lv_values_11_0=null;
        Token otherlv_12=null;
        Token lv_keys_13_0=null;
        Token lv_values_14_0=null;
        Token otherlv_15=null;


        	enterRule();

        try {
            // InternalCQLParser.g:1990:2: ( (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) )
            // InternalCQLParser.g:1991:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            {
            // InternalCQLParser.g:1991:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            // InternalCQLParser.g:1992:3: otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis
            {
            otherlv_0=(Token)match(input,WRAPPER,FOLLOW_32); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateParametersAccess().getWRAPPERKeyword_0());
            		
            // InternalCQLParser.g:1996:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQLParser.g:1997:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQLParser.g:1997:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQLParser.g:1998:5: lv_wrapper_1_0= RULE_STRING
            {
            lv_wrapper_1_0=(Token)match(input,RULE_STRING,FOLLOW_33); 

            					newLeafNode(lv_wrapper_1_0, grammarAccess.getCreateParametersAccess().getWrapperSTRINGTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateParametersRule());
            					}
            					setWithLastConsumed(
            						current,
            						"wrapper",
            						lv_wrapper_1_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_2=(Token)match(input,PROTOCOL,FOLLOW_32); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateParametersAccess().getPROTOCOLKeyword_2());
            		
            // InternalCQLParser.g:2018:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQLParser.g:2019:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQLParser.g:2019:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQLParser.g:2020:5: lv_protocol_3_0= RULE_STRING
            {
            lv_protocol_3_0=(Token)match(input,RULE_STRING,FOLLOW_34); 

            					newLeafNode(lv_protocol_3_0, grammarAccess.getCreateParametersAccess().getProtocolSTRINGTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateParametersRule());
            					}
            					setWithLastConsumed(
            						current,
            						"protocol",
            						lv_protocol_3_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_4=(Token)match(input,TRANSPORT,FOLLOW_32); 

            			newLeafNode(otherlv_4, grammarAccess.getCreateParametersAccess().getTRANSPORTKeyword_4());
            		
            // InternalCQLParser.g:2040:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQLParser.g:2041:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQLParser.g:2041:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQLParser.g:2042:5: lv_transport_5_0= RULE_STRING
            {
            lv_transport_5_0=(Token)match(input,RULE_STRING,FOLLOW_35); 

            					newLeafNode(lv_transport_5_0, grammarAccess.getCreateParametersAccess().getTransportSTRINGTerminalRuleCall_5_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateParametersRule());
            					}
            					setWithLastConsumed(
            						current,
            						"transport",
            						lv_transport_5_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_6=(Token)match(input,DATAHANDLER,FOLLOW_32); 

            			newLeafNode(otherlv_6, grammarAccess.getCreateParametersAccess().getDATAHANDLERKeyword_6());
            		
            // InternalCQLParser.g:2062:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQLParser.g:2063:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQLParser.g:2063:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQLParser.g:2064:5: lv_datahandler_7_0= RULE_STRING
            {
            lv_datahandler_7_0=(Token)match(input,RULE_STRING,FOLLOW_36); 

            					newLeafNode(lv_datahandler_7_0, grammarAccess.getCreateParametersAccess().getDatahandlerSTRINGTerminalRuleCall_7_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateParametersRule());
            					}
            					setWithLastConsumed(
            						current,
            						"datahandler",
            						lv_datahandler_7_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_8=(Token)match(input,OPTIONS,FOLLOW_31); 

            			newLeafNode(otherlv_8, grammarAccess.getCreateParametersAccess().getOPTIONSKeyword_8());
            		
            otherlv_9=(Token)match(input,LeftParenthesis,FOLLOW_32); 

            			newLeafNode(otherlv_9, grammarAccess.getCreateParametersAccess().getLeftParenthesisKeyword_9());
            		
            // InternalCQLParser.g:2088:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+
            int cnt41=0;
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==RULE_STRING) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // InternalCQLParser.g:2089:4: ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) )
            	    {
            	    // InternalCQLParser.g:2089:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2090:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2090:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQLParser.g:2091:6: lv_keys_10_0= RULE_STRING
            	    {
            	    lv_keys_10_0=(Token)match(input,RULE_STRING,FOLLOW_32); 

            	    						newLeafNode(lv_keys_10_0, grammarAccess.getCreateParametersAccess().getKeysSTRINGTerminalRuleCall_10_0_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getCreateParametersRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"keys",
            	    							lv_keys_10_0,
            	    							"org.eclipse.xtext.common.Terminals.STRING");
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:2107:4: ( (lv_values_11_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2108:5: (lv_values_11_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2108:5: (lv_values_11_0= RULE_STRING )
            	    // InternalCQLParser.g:2109:6: lv_values_11_0= RULE_STRING
            	    {
            	    lv_values_11_0=(Token)match(input,RULE_STRING,FOLLOW_37); 

            	    						newLeafNode(lv_values_11_0, grammarAccess.getCreateParametersAccess().getValuesSTRINGTerminalRuleCall_10_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getCreateParametersRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"values",
            	    							lv_values_11_0,
            	    							"org.eclipse.xtext.common.Terminals.STRING");
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt41 >= 1 ) break loop41;
                        EarlyExitException eee =
                            new EarlyExitException(41, input);
                        throw eee;
                }
                cnt41++;
            } while (true);

            // InternalCQLParser.g:2126:3: (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==Comma) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // InternalCQLParser.g:2127:4: otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) )
                    {
                    otherlv_12=(Token)match(input,Comma,FOLLOW_32); 

                    				newLeafNode(otherlv_12, grammarAccess.getCreateParametersAccess().getCommaKeyword_11_0());
                    			
                    // InternalCQLParser.g:2131:4: ( (lv_keys_13_0= RULE_STRING ) )
                    // InternalCQLParser.g:2132:5: (lv_keys_13_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2132:5: (lv_keys_13_0= RULE_STRING )
                    // InternalCQLParser.g:2133:6: lv_keys_13_0= RULE_STRING
                    {
                    lv_keys_13_0=(Token)match(input,RULE_STRING,FOLLOW_32); 

                    						newLeafNode(lv_keys_13_0, grammarAccess.getCreateParametersAccess().getKeysSTRINGTerminalRuleCall_11_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateParametersRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"keys",
                    							lv_keys_13_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }

                    // InternalCQLParser.g:2149:4: ( (lv_values_14_0= RULE_STRING ) )
                    // InternalCQLParser.g:2150:5: (lv_values_14_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2150:5: (lv_values_14_0= RULE_STRING )
                    // InternalCQLParser.g:2151:6: lv_values_14_0= RULE_STRING
                    {
                    lv_values_14_0=(Token)match(input,RULE_STRING,FOLLOW_19); 

                    						newLeafNode(lv_values_14_0, grammarAccess.getCreateParametersAccess().getValuesSTRINGTerminalRuleCall_11_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateParametersRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"values",
                    							lv_values_14_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_15=(Token)match(input,RightParenthesis,FOLLOW_2); 

            			newLeafNode(otherlv_15, grammarAccess.getCreateParametersAccess().getRightParenthesisKeyword_12());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCreateParameters"


    // $ANTLR start "entryRuleAttributeDefinition"
    // InternalCQLParser.g:2176:1: entryRuleAttributeDefinition returns [EObject current=null] : iv_ruleAttributeDefinition= ruleAttributeDefinition EOF ;
    public final EObject entryRuleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeDefinition = null;


        try {
            // InternalCQLParser.g:2176:60: (iv_ruleAttributeDefinition= ruleAttributeDefinition EOF )
            // InternalCQLParser.g:2177:2: iv_ruleAttributeDefinition= ruleAttributeDefinition EOF
            {
             newCompositeNode(grammarAccess.getAttributeDefinitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeDefinition=ruleAttributeDefinition();

            state._fsp--;

             current =iv_ruleAttributeDefinition; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttributeDefinition"


    // $ANTLR start "ruleAttributeDefinition"
    // InternalCQLParser.g:2183:1: ruleAttributeDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) ;
    public final EObject ruleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token lv_arguments_2_0=null;
        Token lv_arguments_3_0=null;
        Token otherlv_4=null;
        Token lv_arguments_5_0=null;
        Token lv_arguments_6_0=null;
        Token otherlv_7=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2189:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis ) )
            // InternalCQLParser.g:2190:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            {
            // InternalCQLParser.g:2190:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis )
            // InternalCQLParser.g:2191:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_arguments_2_0= RULE_ID ) ) ( (lv_arguments_3_0= RULE_ID ) ) (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )* otherlv_7= RightParenthesis
            {
            // InternalCQLParser.g:2191:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2192:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2192:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2193:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_31); 

            					newLeafNode(lv_name_0_0, grammarAccess.getAttributeDefinitionAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeDefinitionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQLParser.g:2213:3: ( (lv_arguments_2_0= RULE_ID ) )
            // InternalCQLParser.g:2214:4: (lv_arguments_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2214:4: (lv_arguments_2_0= RULE_ID )
            // InternalCQLParser.g:2215:5: lv_arguments_2_0= RULE_ID
            {
            lv_arguments_2_0=(Token)match(input,RULE_ID,FOLLOW_15); 

            					newLeafNode(lv_arguments_2_0, grammarAccess.getAttributeDefinitionAccess().getArgumentsIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            					}
            					addWithLastConsumed(
            						current,
            						"arguments",
            						lv_arguments_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2231:3: ( (lv_arguments_3_0= RULE_ID ) )
            // InternalCQLParser.g:2232:4: (lv_arguments_3_0= RULE_ID )
            {
            // InternalCQLParser.g:2232:4: (lv_arguments_3_0= RULE_ID )
            // InternalCQLParser.g:2233:5: lv_arguments_3_0= RULE_ID
            {
            lv_arguments_3_0=(Token)match(input,RULE_ID,FOLLOW_38); 

            					newLeafNode(lv_arguments_3_0, grammarAccess.getAttributeDefinitionAccess().getArgumentsIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            					}
            					addWithLastConsumed(
            						current,
            						"arguments",
            						lv_arguments_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2249:3: (otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) ) )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==Comma) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalCQLParser.g:2250:4: otherlv_4= Comma ( (lv_arguments_5_0= RULE_ID ) ) ( (lv_arguments_6_0= RULE_ID ) )
            	    {
            	    otherlv_4=(Token)match(input,Comma,FOLLOW_15); 

            	    				newLeafNode(otherlv_4, grammarAccess.getAttributeDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQLParser.g:2254:4: ( (lv_arguments_5_0= RULE_ID ) )
            	    // InternalCQLParser.g:2255:5: (lv_arguments_5_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2255:5: (lv_arguments_5_0= RULE_ID )
            	    // InternalCQLParser.g:2256:6: lv_arguments_5_0= RULE_ID
            	    {
            	    lv_arguments_5_0=(Token)match(input,RULE_ID,FOLLOW_15); 

            	    						newLeafNode(lv_arguments_5_0, grammarAccess.getAttributeDefinitionAccess().getArgumentsIDTerminalRuleCall_4_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"arguments",
            	    							lv_arguments_5_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:2272:4: ( (lv_arguments_6_0= RULE_ID ) )
            	    // InternalCQLParser.g:2273:5: (lv_arguments_6_0= RULE_ID )
            	    {
            	    // InternalCQLParser.g:2273:5: (lv_arguments_6_0= RULE_ID )
            	    // InternalCQLParser.g:2274:6: lv_arguments_6_0= RULE_ID
            	    {
            	    lv_arguments_6_0=(Token)match(input,RULE_ID,FOLLOW_38); 

            	    						newLeafNode(lv_arguments_6_0, grammarAccess.getAttributeDefinitionAccess().getArgumentsIDTerminalRuleCall_4_2_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAttributeDefinitionRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"arguments",
            	    							lv_arguments_6_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);

            otherlv_7=(Token)match(input,RightParenthesis,FOLLOW_2); 

            			newLeafNode(otherlv_7, grammarAccess.getAttributeDefinitionAccess().getRightParenthesisKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttributeDefinition"


    // $ANTLR start "entryRuleCreateStream1"
    // InternalCQLParser.g:2299:1: entryRuleCreateStream1 returns [EObject current=null] : iv_ruleCreateStream1= ruleCreateStream1 EOF ;
    public final EObject entryRuleCreateStream1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStream1 = null;


        try {
            // InternalCQLParser.g:2299:54: (iv_ruleCreateStream1= ruleCreateStream1 EOF )
            // InternalCQLParser.g:2300:2: iv_ruleCreateStream1= ruleCreateStream1 EOF
            {
             newCompositeNode(grammarAccess.getCreateStream1Rule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateStream1=ruleCreateStream1();

            state._fsp--;

             current =iv_ruleCreateStream1; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCreateStream1"


    // $ANTLR start "ruleCreateStream1"
    // InternalCQLParser.g:2306:1: ruleCreateStream1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateStream1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2312:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2313:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2313:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2314:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2314:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2315:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2315:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2316:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_39);
            lv_keyword_0_0=ruleCreateKeyword();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStream1Rule());
            					}
            					set(
            						current,
            						"keyword",
            						lv_keyword_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateKeyword");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,STREAM,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStream1Access().getSTREAMKeyword_1());
            		
            // InternalCQLParser.g:2337:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2338:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2338:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2339:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_40);
            lv_attributes_2_0=ruleAttributeDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStream1Rule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:2356:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2357:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2357:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2358:5: lv_pars_3_0= ruleCreateParameters
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getParsCreateParametersParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_pars_3_0=ruleCreateParameters();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStream1Rule());
            					}
            					set(
            						current,
            						"pars",
            						lv_pars_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateParameters");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCreateStream1"


    // $ANTLR start "entryRuleCreateSink1"
    // InternalCQLParser.g:2379:1: entryRuleCreateSink1 returns [EObject current=null] : iv_ruleCreateSink1= ruleCreateSink1 EOF ;
    public final EObject entryRuleCreateSink1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateSink1 = null;


        try {
            // InternalCQLParser.g:2379:52: (iv_ruleCreateSink1= ruleCreateSink1 EOF )
            // InternalCQLParser.g:2380:2: iv_ruleCreateSink1= ruleCreateSink1 EOF
            {
             newCompositeNode(grammarAccess.getCreateSink1Rule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateSink1=ruleCreateSink1();

            state._fsp--;

             current =iv_ruleCreateSink1; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCreateSink1"


    // $ANTLR start "ruleCreateSink1"
    // InternalCQLParser.g:2386:1: ruleCreateSink1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateSink1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2392:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2393:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2393:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2394:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2394:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2395:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2395:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2396:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_41);
            lv_keyword_0_0=ruleCreateKeyword();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateSink1Rule());
            					}
            					set(
            						current,
            						"keyword",
            						lv_keyword_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateKeyword");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,SINK,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateSink1Access().getSINKKeyword_1());
            		
            // InternalCQLParser.g:2417:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2418:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2418:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2419:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_40);
            lv_attributes_2_0=ruleAttributeDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateSink1Rule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQLParser.g:2436:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2437:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2437:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2438:5: lv_pars_3_0= ruleCreateParameters
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getParsCreateParametersParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_pars_3_0=ruleCreateParameters();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateSink1Rule());
            					}
            					set(
            						current,
            						"pars",
            						lv_pars_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateParameters");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCreateSink1"


    // $ANTLR start "entryRuleCreateStreamChannel"
    // InternalCQLParser.g:2459:1: entryRuleCreateStreamChannel returns [EObject current=null] : iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF ;
    public final EObject entryRuleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamChannel = null;


        try {
            // InternalCQLParser.g:2459:60: (iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF )
            // InternalCQLParser.g:2460:2: iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF
            {
             newCompositeNode(grammarAccess.getCreateStreamChannelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateStreamChannel=ruleCreateStreamChannel();

            state._fsp--;

             current =iv_ruleCreateStreamChannel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCreateStreamChannel"


    // $ANTLR start "ruleCreateStreamChannel"
    // InternalCQLParser.g:2466:1: ruleCreateStreamChannel returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) ;
    public final EObject ruleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token lv_host_4_0=null;
        Token otherlv_5=null;
        Token lv_port_6_0=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2472:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) )
            // InternalCQLParser.g:2473:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            {
            // InternalCQLParser.g:2473:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            // InternalCQLParser.g:2474:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) )
            {
            // InternalCQLParser.g:2474:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2475:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2475:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2476:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStreamChannelAccess().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_39);
            lv_keyword_0_0=ruleCreateKeyword();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStreamChannelRule());
            					}
            					set(
            						current,
            						"keyword",
            						lv_keyword_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateKeyword");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,STREAM,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamChannelAccess().getSTREAMKeyword_1());
            		
            // InternalCQLParser.g:2497:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2498:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2498:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2499:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamChannelAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_42);
            lv_attributes_2_0=ruleAttributeDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStreamChannelRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,CHANNEL,FOLLOW_15); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamChannelAccess().getCHANNELKeyword_3());
            		
            // InternalCQLParser.g:2520:3: ( (lv_host_4_0= RULE_ID ) )
            // InternalCQLParser.g:2521:4: (lv_host_4_0= RULE_ID )
            {
            // InternalCQLParser.g:2521:4: (lv_host_4_0= RULE_ID )
            // InternalCQLParser.g:2522:5: lv_host_4_0= RULE_ID
            {
            lv_host_4_0=(Token)match(input,RULE_ID,FOLLOW_43); 

            					newLeafNode(lv_host_4_0, grammarAccess.getCreateStreamChannelAccess().getHostIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateStreamChannelRule());
            					}
            					setWithLastConsumed(
            						current,
            						"host",
            						lv_host_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_5=(Token)match(input,Colon,FOLLOW_44); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamChannelAccess().getColonKeyword_5());
            		
            // InternalCQLParser.g:2542:3: ( (lv_port_6_0= RULE_INT ) )
            // InternalCQLParser.g:2543:4: (lv_port_6_0= RULE_INT )
            {
            // InternalCQLParser.g:2543:4: (lv_port_6_0= RULE_INT )
            // InternalCQLParser.g:2544:5: lv_port_6_0= RULE_INT
            {
            lv_port_6_0=(Token)match(input,RULE_INT,FOLLOW_2); 

            					newLeafNode(lv_port_6_0, grammarAccess.getCreateStreamChannelAccess().getPortINTTerminalRuleCall_6_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateStreamChannelRule());
            					}
            					setWithLastConsumed(
            						current,
            						"port",
            						lv_port_6_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCreateStreamChannel"


    // $ANTLR start "entryRuleCreateStreamFile"
    // InternalCQLParser.g:2564:1: entryRuleCreateStreamFile returns [EObject current=null] : iv_ruleCreateStreamFile= ruleCreateStreamFile EOF ;
    public final EObject entryRuleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamFile = null;


        try {
            // InternalCQLParser.g:2564:57: (iv_ruleCreateStreamFile= ruleCreateStreamFile EOF )
            // InternalCQLParser.g:2565:2: iv_ruleCreateStreamFile= ruleCreateStreamFile EOF
            {
             newCompositeNode(grammarAccess.getCreateStreamFileRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateStreamFile=ruleCreateStreamFile();

            state._fsp--;

             current =iv_ruleCreateStreamFile; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCreateStreamFile"


    // $ANTLR start "ruleCreateStreamFile"
    // InternalCQLParser.g:2571:1: ruleCreateStreamFile returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) ;
    public final EObject ruleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token lv_filename_4_0=null;
        Token otherlv_5=null;
        Token lv_type_6_0=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2577:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2578:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:2578:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            // InternalCQLParser.g:2579:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2579:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2580:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2580:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2581:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStreamFileAccess().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_39);
            lv_keyword_0_0=ruleCreateKeyword();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStreamFileRule());
            					}
            					set(
            						current,
            						"keyword",
            						lv_keyword_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.CreateKeyword");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,STREAM,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamFileAccess().getSTREAMKeyword_1());
            		
            // InternalCQLParser.g:2602:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2603:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2603:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2604:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamFileAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_45);
            lv_attributes_2_0=ruleAttributeDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateStreamFileRule());
            					}
            					set(
            						current,
            						"attributes",
            						lv_attributes_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,FILE,FOLLOW_32); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamFileAccess().getFILEKeyword_3());
            		
            // InternalCQLParser.g:2625:3: ( (lv_filename_4_0= RULE_STRING ) )
            // InternalCQLParser.g:2626:4: (lv_filename_4_0= RULE_STRING )
            {
            // InternalCQLParser.g:2626:4: (lv_filename_4_0= RULE_STRING )
            // InternalCQLParser.g:2627:5: lv_filename_4_0= RULE_STRING
            {
            lv_filename_4_0=(Token)match(input,RULE_STRING,FOLLOW_24); 

            					newLeafNode(lv_filename_4_0, grammarAccess.getCreateStreamFileAccess().getFilenameSTRINGTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateStreamFileRule());
            					}
            					setWithLastConsumed(
            						current,
            						"filename",
            						lv_filename_4_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_5=(Token)match(input,AS,FOLLOW_15); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamFileAccess().getASKeyword_5());
            		
            // InternalCQLParser.g:2647:3: ( (lv_type_6_0= RULE_ID ) )
            // InternalCQLParser.g:2648:4: (lv_type_6_0= RULE_ID )
            {
            // InternalCQLParser.g:2648:4: (lv_type_6_0= RULE_ID )
            // InternalCQLParser.g:2649:5: lv_type_6_0= RULE_ID
            {
            lv_type_6_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_type_6_0, grammarAccess.getCreateStreamFileAccess().getTypeIDTerminalRuleCall_6_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateStreamFileRule());
            					}
            					setWithLastConsumed(
            						current,
            						"type",
            						lv_type_6_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCreateStreamFile"


    // $ANTLR start "entryRuleCreateView"
    // InternalCQLParser.g:2669:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQLParser.g:2669:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQLParser.g:2670:2: iv_ruleCreateView= ruleCreateView EOF
            {
             newCompositeNode(grammarAccess.getCreateViewRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreateView=ruleCreateView();

            state._fsp--;

             current =iv_ruleCreateView; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCreateView"


    // $ANTLR start "ruleCreateView"
    // InternalCQLParser.g:2676:1: ruleCreateView returns [EObject current=null] : (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2682:2: ( (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) )
            // InternalCQLParser.g:2683:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            {
            // InternalCQLParser.g:2683:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            // InternalCQLParser.g:2684:3: otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) )
            {
            otherlv_0=(Token)match(input,VIEW,FOLLOW_15); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateViewAccess().getVIEWKeyword_0());
            		
            // InternalCQLParser.g:2688:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:2689:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:2689:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:2690:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_1_0, grammarAccess.getCreateViewAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCreateViewRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,FROM,FOLLOW_9); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateViewAccess().getFROMKeyword_2());
            		
            // InternalCQLParser.g:2710:3: ( (lv_select_3_0= ruleNestedStatement ) )
            // InternalCQLParser.g:2711:4: (lv_select_3_0= ruleNestedStatement )
            {
            // InternalCQLParser.g:2711:4: (lv_select_3_0= ruleNestedStatement )
            // InternalCQLParser.g:2712:5: lv_select_3_0= ruleNestedStatement
            {

            					newCompositeNode(grammarAccess.getCreateViewAccess().getSelectNestedStatementParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_select_3_0=ruleNestedStatement();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCreateViewRule());
            					}
            					set(
            						current,
            						"select",
            						lv_select_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.NestedStatement");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCreateView"


    // $ANTLR start "entryRuleStreamTo"
    // InternalCQLParser.g:2733:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQLParser.g:2733:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQLParser.g:2734:2: iv_ruleStreamTo= ruleStreamTo EOF
            {
             newCompositeNode(grammarAccess.getStreamToRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleStreamTo=ruleStreamTo();

            state._fsp--;

             current =iv_ruleStreamTo; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStreamTo"


    // $ANTLR start "ruleStreamTo"
    // InternalCQLParser.g:2740:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2746:2: ( (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:2747:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:2747:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2748:3: otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,STREAM,FOLLOW_46); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,TO,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQLParser.g:2756:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQLParser.g:2757:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2757:4: (lv_name_2_0= RULE_ID )
            // InternalCQLParser.g:2758:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_47); 

            					newLeafNode(lv_name_2_0, grammarAccess.getStreamToAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getStreamToRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2774:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==SELECT) ) {
                alt44=1;
            }
            else if ( (LA44_0==RULE_ID) ) {
                alt44=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 44, 0, input);

                throw nvae;
            }
            switch (alt44) {
                case 1 :
                    // InternalCQLParser.g:2775:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQLParser.g:2775:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQLParser.g:2776:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQLParser.g:2776:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQLParser.g:2777:6: lv_statement_3_0= ruleSelect
                    {

                    						newCompositeNode(grammarAccess.getStreamToAccess().getStatementSelectParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_statement_3_0=ruleSelect();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStreamToRule());
                    						}
                    						set(
                    							current,
                    							"statement",
                    							lv_statement_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2795:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:2795:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQLParser.g:2796:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQLParser.g:2796:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQLParser.g:2797:6: lv_inputname_4_0= RULE_ID
                    {
                    lv_inputname_4_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(lv_inputname_4_0, grammarAccess.getStreamToAccess().getInputnameIDTerminalRuleCall_3_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getStreamToRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"inputname",
                    							lv_inputname_4_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStreamTo"


    // $ANTLR start "entryRuleDrop"
    // InternalCQLParser.g:2818:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQLParser.g:2818:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQLParser.g:2819:2: iv_ruleDrop= ruleDrop EOF
            {
             newCompositeNode(grammarAccess.getDropRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDrop=ruleDrop();

            state._fsp--;

             current =iv_ruleDrop; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDrop"


    // $ANTLR start "ruleDrop"
    // InternalCQLParser.g:2825:1: ruleDrop returns [EObject current=null] : ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? ) ;
    public final EObject ruleDrop() throws RecognitionException {
        EObject current = null;

        Token lv_keyword1_0_0=null;
        Token lv_keyword2_1_1=null;
        Token lv_keyword2_1_2=null;
        Token lv_value1_2_0=null;
        Token lv_keyword3_3_0=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2831:2: ( ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? ) )
            // InternalCQLParser.g:2832:2: ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? )
            {
            // InternalCQLParser.g:2832:2: ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? )
            // InternalCQLParser.g:2833:3: ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )?
            {
            // InternalCQLParser.g:2833:3: ( (lv_keyword1_0_0= DROP ) )
            // InternalCQLParser.g:2834:4: (lv_keyword1_0_0= DROP )
            {
            // InternalCQLParser.g:2834:4: (lv_keyword1_0_0= DROP )
            // InternalCQLParser.g:2835:5: lv_keyword1_0_0= DROP
            {
            lv_keyword1_0_0=(Token)match(input,DROP,FOLLOW_48); 

            					newLeafNode(lv_keyword1_0_0, grammarAccess.getDropAccess().getKeyword1DROPKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(current, "keyword1", lv_keyword1_0_0, "DROP");
            				

            }


            }

            // InternalCQLParser.g:2847:3: ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) )
            // InternalCQLParser.g:2848:4: ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) )
            {
            // InternalCQLParser.g:2848:4: ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) )
            // InternalCQLParser.g:2849:5: (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM )
            {
            // InternalCQLParser.g:2849:5: (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM )
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==SINK) ) {
                alt45=1;
            }
            else if ( (LA45_0==STREAM) ) {
                alt45=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;
            }
            switch (alt45) {
                case 1 :
                    // InternalCQLParser.g:2850:6: lv_keyword2_1_1= SINK
                    {
                    lv_keyword2_1_1=(Token)match(input,SINK,FOLLOW_15); 

                    						newLeafNode(lv_keyword2_1_1, grammarAccess.getDropAccess().getKeyword2SINKKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "keyword2", lv_keyword2_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:2861:6: lv_keyword2_1_2= STREAM
                    {
                    lv_keyword2_1_2=(Token)match(input,STREAM,FOLLOW_15); 

                    						newLeafNode(lv_keyword2_1_2, grammarAccess.getDropAccess().getKeyword2STREAMKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "keyword2", lv_keyword2_1_2, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQLParser.g:2874:3: ( (lv_value1_2_0= RULE_ID ) )
            // InternalCQLParser.g:2875:4: (lv_value1_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2875:4: (lv_value1_2_0= RULE_ID )
            // InternalCQLParser.g:2876:5: lv_value1_2_0= RULE_ID
            {
            lv_value1_2_0=(Token)match(input,RULE_ID,FOLLOW_49); 

            					newLeafNode(lv_value1_2_0, grammarAccess.getDropAccess().getValue1IDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(
            						current,
            						"value1",
            						lv_value1_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2892:3: ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==IF) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // InternalCQLParser.g:2893:4: ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS
                    {
                    // InternalCQLParser.g:2893:4: ( (lv_keyword3_3_0= IF ) )
                    // InternalCQLParser.g:2894:5: (lv_keyword3_3_0= IF )
                    {
                    // InternalCQLParser.g:2894:5: (lv_keyword3_3_0= IF )
                    // InternalCQLParser.g:2895:6: lv_keyword3_3_0= IF
                    {
                    lv_keyword3_3_0=(Token)match(input,IF,FOLLOW_50); 

                    						newLeafNode(lv_keyword3_3_0, grammarAccess.getDropAccess().getKeyword3IFKeyword_3_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "keyword3", lv_keyword3_3_0, "IF");
                    					

                    }


                    }

                    otherlv_4=(Token)match(input,EXISTS,FOLLOW_2); 

                    				newLeafNode(otherlv_4, grammarAccess.getDropAccess().getEXISTSKeyword_3_1());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDrop"


    // $ANTLR start "entryRuleWindow_Unbounded"
    // InternalCQLParser.g:2916:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQLParser.g:2916:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQLParser.g:2917:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
            {
             newCompositeNode(grammarAccess.getWindow_UnboundedRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWindow_Unbounded=ruleWindow_Unbounded();

            state._fsp--;

             current =iv_ruleWindow_Unbounded.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWindow_Unbounded"


    // $ANTLR start "ruleWindow_Unbounded"
    // InternalCQLParser.g:2923:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= UNBOUNDED ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2929:2: (kw= UNBOUNDED )
            // InternalCQLParser.g:2930:2: kw= UNBOUNDED
            {
            kw=(Token)match(input,UNBOUNDED,FOLLOW_2); 

            		current.merge(kw);
            		newLeafNode(kw, grammarAccess.getWindow_UnboundedAccess().getUNBOUNDEDKeyword());
            	

            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWindow_Unbounded"


    // $ANTLR start "entryRuleWindow_Timebased"
    // InternalCQLParser.g:2938:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQLParser.g:2938:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQLParser.g:2939:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
            {
             newCompositeNode(grammarAccess.getWindow_TimebasedRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWindow_Timebased=ruleWindow_Timebased();

            state._fsp--;

             current =iv_ruleWindow_Timebased; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWindow_Timebased"


    // $ANTLR start "ruleWindow_Timebased"
    // InternalCQLParser.g:2945:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) ;
    public final EObject ruleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_size_1_0=null;
        Token lv_unit_2_0=null;
        Token otherlv_3=null;
        Token lv_advance_size_4_0=null;
        Token lv_advance_unit_5_0=null;
        Token otherlv_6=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2951:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) )
            // InternalCQLParser.g:2952:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            {
            // InternalCQLParser.g:2952:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            // InternalCQLParser.g:2953:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_44); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:2957:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:2958:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:2958:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:2959:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_15); 

            					newLeafNode(lv_size_1_0, grammarAccess.getWindow_TimebasedAccess().getSizeINTTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getWindow_TimebasedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"size",
            						lv_size_1_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            // InternalCQLParser.g:2975:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQLParser.g:2976:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2976:4: (lv_unit_2_0= RULE_ID )
            // InternalCQLParser.g:2977:5: lv_unit_2_0= RULE_ID
            {
            lv_unit_2_0=(Token)match(input,RULE_ID,FOLLOW_51); 

            					newLeafNode(lv_unit_2_0, grammarAccess.getWindow_TimebasedAccess().getUnitIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getWindow_TimebasedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"unit",
            						lv_unit_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            // InternalCQLParser.g:2993:3: (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==ADVANCE) ) {
                alt47=1;
            }
            switch (alt47) {
                case 1 :
                    // InternalCQLParser.g:2994:4: otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,ADVANCE,FOLLOW_44); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQLParser.g:2998:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQLParser.g:2999:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQLParser.g:2999:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQLParser.g:3000:6: lv_advance_size_4_0= RULE_INT
                    {
                    lv_advance_size_4_0=(Token)match(input,RULE_INT,FOLLOW_15); 

                    						newLeafNode(lv_advance_size_4_0, grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeINTTerminalRuleCall_3_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getWindow_TimebasedRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_size",
                    							lv_advance_size_4_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }

                    // InternalCQLParser.g:3016:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQLParser.g:3017:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3017:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQLParser.g:3018:6: lv_advance_unit_5_0= RULE_ID
                    {
                    lv_advance_unit_5_0=(Token)match(input,RULE_ID,FOLLOW_52); 

                    						newLeafNode(lv_advance_unit_5_0, grammarAccess.getWindow_TimebasedAccess().getAdvance_unitIDTerminalRuleCall_3_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getWindow_TimebasedRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_unit",
                    							lv_advance_unit_5_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,TIME,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getWindow_TimebasedAccess().getTIMEKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWindow_Timebased"


    // $ANTLR start "entryRuleWindow_Tuplebased"
    // InternalCQLParser.g:3043:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQLParser.g:3043:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQLParser.g:3044:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
            {
             newCompositeNode(grammarAccess.getWindow_TuplebasedRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWindow_Tuplebased=ruleWindow_Tuplebased();

            state._fsp--;

             current =iv_ruleWindow_Tuplebased; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWindow_Tuplebased"


    // $ANTLR start "ruleWindow_Tuplebased"
    // InternalCQLParser.g:3050:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
    public final EObject ruleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_size_1_0=null;
        Token otherlv_2=null;
        Token lv_advance_size_3_0=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        EObject lv_partition_attribute_7_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3056:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQLParser.g:3057:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQLParser.g:3057:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQLParser.g:3058:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_44); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:3062:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:3063:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:3063:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:3064:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_53); 

            					newLeafNode(lv_size_1_0, grammarAccess.getWindow_TuplebasedAccess().getSizeINTTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getWindow_TuplebasedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"size",
            						lv_size_1_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            // InternalCQLParser.g:3080:3: (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==ADVANCE) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // InternalCQLParser.g:3081:4: otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,ADVANCE,FOLLOW_44); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQLParser.g:3085:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQLParser.g:3086:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3086:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQLParser.g:3087:6: lv_advance_size_3_0= RULE_INT
                    {
                    lv_advance_size_3_0=(Token)match(input,RULE_INT,FOLLOW_54); 

                    						newLeafNode(lv_advance_size_3_0, grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeINTTerminalRuleCall_2_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getWindow_TuplebasedRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_size",
                    							lv_advance_size_3_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,TUPLE,FOLLOW_55); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQLParser.g:3108:3: (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==PARTITION) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalCQLParser.g:3109:4: otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,PARTITION,FOLLOW_14); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,BY,FOLLOW_15); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQLParser.g:3117:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQLParser.g:3118:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:3118:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQLParser.g:3119:6: lv_partition_attribute_7_0= ruleAttribute
                    {

                    						newCompositeNode(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAttributeParserRuleCall_4_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_partition_attribute_7_0=ruleAttribute();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getWindow_TuplebasedRule());
                    						}
                    						set(
                    							current,
                    							"partition_attribute",
                    							lv_partition_attribute_7_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWindow_Tuplebased"


    // $ANTLR start "entryRuleExpressionsModel"
    // InternalCQLParser.g:3141:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQLParser.g:3141:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQLParser.g:3142:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
            {
             newCompositeNode(grammarAccess.getExpressionsModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionsModel=ruleExpressionsModel();

            state._fsp--;

             current =iv_ruleExpressionsModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpressionsModel"


    // $ANTLR start "ruleExpressionsModel"
    // InternalCQLParser.g:3148:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3154:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQLParser.g:3155:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQLParser.g:3155:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQLParser.g:3156:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQLParser.g:3156:3: ()
            // InternalCQLParser.g:3157:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:3163:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQLParser.g:3164:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQLParser.g:3164:4: (lv_elements_1_0= ruleExpression )
            // InternalCQLParser.g:3165:5: lv_elements_1_0= ruleExpression
            {

            					newCompositeNode(grammarAccess.getExpressionsModelAccess().getElementsExpressionParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_elements_1_0=ruleExpression();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getExpressionsModelRule());
            					}
            					add(
            						current,
            						"elements",
            						lv_elements_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Expression");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpressionsModel"


    // $ANTLR start "entryRuleExpression"
    // InternalCQLParser.g:3186:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQLParser.g:3186:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQLParser.g:3187:2: iv_ruleExpression= ruleExpression EOF
            {
             newCompositeNode(grammarAccess.getExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpression=ruleExpression();

            state._fsp--;

             current =iv_ruleExpression; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpression"


    // $ANTLR start "ruleExpression"
    // InternalCQLParser.g:3193:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3199:2: (this_Or_0= ruleOr )
            // InternalCQLParser.g:3200:2: this_Or_0= ruleOr
            {

            		newCompositeNode(grammarAccess.getExpressionAccess().getOrParserRuleCall());
            	
            pushFollow(FOLLOW_2);
            this_Or_0=ruleOr();

            state._fsp--;


            		current = this_Or_0;
            		afterParserOrEnumRuleCall();
            	

            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpression"


    // $ANTLR start "entryRuleOr"
    // InternalCQLParser.g:3211:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQLParser.g:3211:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQLParser.g:3212:2: iv_ruleOr= ruleOr EOF
            {
             newCompositeNode(grammarAccess.getOrRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOr=ruleOr();

            state._fsp--;

             current =iv_ruleOr; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOr"


    // $ANTLR start "ruleOr"
    // InternalCQLParser.g:3218:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3224:2: ( (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQLParser.g:3225:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQLParser.g:3225:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQLParser.g:3226:3: this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_56);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3234:3: ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            loop50:
            do {
                int alt50=2;
                int LA50_0 = input.LA(1);

                if ( (LA50_0==OR) ) {
                    alt50=1;
                }


                switch (alt50) {
            	case 1 :
            	    // InternalCQLParser.g:3235:4: () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQLParser.g:3235:4: ()
            	    // InternalCQLParser.g:3236:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,OR,FOLLOW_12); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:3246:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQLParser.g:3247:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQLParser.g:3247:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQLParser.g:3248:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_56);
            	    lv_right_3_0=ruleAnd();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getOrRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.And");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop50;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOr"


    // $ANTLR start "entryRuleAnd"
    // InternalCQLParser.g:3270:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQLParser.g:3270:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQLParser.g:3271:2: iv_ruleAnd= ruleAnd EOF
            {
             newCompositeNode(grammarAccess.getAndRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAnd=ruleAnd();

            state._fsp--;

             current =iv_ruleAnd; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAnd"


    // $ANTLR start "ruleAnd"
    // InternalCQLParser.g:3277:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3283:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQLParser.g:3284:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQLParser.g:3284:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQLParser.g:3285:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_57);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3293:3: ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==AND) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // InternalCQLParser.g:3294:4: () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQLParser.g:3294:4: ()
            	    // InternalCQLParser.g:3295:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,AND,FOLLOW_12); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:3305:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQLParser.g:3306:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQLParser.g:3306:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQLParser.g:3307:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_57);
            	    lv_right_3_0=ruleEqualitiy();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAndRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Equalitiy");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAnd"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQLParser.g:3329:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQLParser.g:3329:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQLParser.g:3330:2: iv_ruleEqualitiy= ruleEqualitiy EOF
            {
             newCompositeNode(grammarAccess.getEqualitiyRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEqualitiy=ruleEqualitiy();

            state._fsp--;

             current =iv_ruleEqualitiy; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEqualitiy"


    // $ANTLR start "ruleEqualitiy"
    // InternalCQLParser.g:3336:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3342:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQLParser.g:3343:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQLParser.g:3343:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQLParser.g:3344:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_58);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3352:3: ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop53:
            do {
                int alt53=2;
                int LA53_0 = input.LA(1);

                if ( (LA53_0==ExclamationMarkEqualsSign||LA53_0==EqualsSign) ) {
                    alt53=1;
                }


                switch (alt53) {
            	case 1 :
            	    // InternalCQLParser.g:3353:4: () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQLParser.g:3353:4: ()
            	    // InternalCQLParser.g:3354:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3360:4: ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) )
            	    // InternalCQLParser.g:3361:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    {
            	    // InternalCQLParser.g:3361:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    // InternalCQLParser.g:3362:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    {
            	    // InternalCQLParser.g:3362:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    int alt52=2;
            	    int LA52_0 = input.LA(1);

            	    if ( (LA52_0==EqualsSign) ) {
            	        alt52=1;
            	    }
            	    else if ( (LA52_0==ExclamationMarkEqualsSign) ) {
            	        alt52=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 52, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt52) {
            	        case 1 :
            	            // InternalCQLParser.g:3363:7: lv_op_2_1= EqualsSign
            	            {
            	            lv_op_2_1=(Token)match(input,EqualsSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:3374:7: lv_op_2_2= ExclamationMarkEqualsSign
            	            {
            	            lv_op_2_2=(Token)match(input,ExclamationMarkEqualsSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getEqualitiyAccess().getOpExclamationMarkEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQLParser.g:3387:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQLParser.g:3388:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQLParser.g:3388:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQLParser.g:3389:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_58);
            	    lv_right_3_0=ruleComparison();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getEqualitiyRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Comparison");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop53;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEqualitiy"


    // $ANTLR start "entryRuleComparison"
    // InternalCQLParser.g:3411:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQLParser.g:3411:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQLParser.g:3412:2: iv_ruleComparison= ruleComparison EOF
            {
             newCompositeNode(grammarAccess.getComparisonRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleComparison=ruleComparison();

            state._fsp--;

             current =iv_ruleComparison; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleComparison"


    // $ANTLR start "ruleComparison"
    // InternalCQLParser.g:3418:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
    public final EObject ruleComparison() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        Token lv_op_2_3=null;
        Token lv_op_2_4=null;
        EObject this_PlusOrMinus_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3424:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQLParser.g:3425:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQLParser.g:3425:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQLParser.g:3426:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_59);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3434:3: ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop55:
            do {
                int alt55=2;
                int LA55_0 = input.LA(1);

                if ( ((LA55_0>=LessThanSignEqualsSign && LA55_0<=GreaterThanSignEqualsSign)||LA55_0==LessThanSign||LA55_0==GreaterThanSign) ) {
                    alt55=1;
                }


                switch (alt55) {
            	case 1 :
            	    // InternalCQLParser.g:3435:4: () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQLParser.g:3435:4: ()
            	    // InternalCQLParser.g:3436:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3442:4: ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) )
            	    // InternalCQLParser.g:3443:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    {
            	    // InternalCQLParser.g:3443:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    // InternalCQLParser.g:3444:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    {
            	    // InternalCQLParser.g:3444:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    int alt54=4;
            	    switch ( input.LA(1) ) {
            	    case GreaterThanSignEqualsSign:
            	        {
            	        alt54=1;
            	        }
            	        break;
            	    case LessThanSignEqualsSign:
            	        {
            	        alt54=2;
            	        }
            	        break;
            	    case LessThanSign:
            	        {
            	        alt54=3;
            	        }
            	        break;
            	    case GreaterThanSign:
            	        {
            	        alt54=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 54, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt54) {
            	        case 1 :
            	            // InternalCQLParser.g:3445:7: lv_op_2_1= GreaterThanSignEqualsSign
            	            {
            	            lv_op_2_1=(Token)match(input,GreaterThanSignEqualsSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:3456:7: lv_op_2_2= LessThanSignEqualsSign
            	            {
            	            lv_op_2_2=(Token)match(input,LessThanSignEqualsSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQLParser.g:3467:7: lv_op_2_3= LessThanSign
            	            {
            	            lv_op_2_3=(Token)match(input,LessThanSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQLParser.g:3478:7: lv_op_2_4= GreaterThanSign
            	            {
            	            lv_op_2_4=(Token)match(input,GreaterThanSign,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_4, grammarAccess.getComparisonAccess().getOpGreaterThanSignKeyword_1_1_0_3());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_4, null);
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQLParser.g:3491:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQLParser.g:3492:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQLParser.g:3492:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQLParser.g:3493:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_59);
            	    lv_right_3_0=rulePlusOrMinus();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getComparisonRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.PlusOrMinus");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop55;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleComparison"


    // $ANTLR start "entryRulePlusOrMinus"
    // InternalCQLParser.g:3515:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQLParser.g:3515:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQLParser.g:3516:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
            {
             newCompositeNode(grammarAccess.getPlusOrMinusRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePlusOrMinus=rulePlusOrMinus();

            state._fsp--;

             current =iv_rulePlusOrMinus; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePlusOrMinus"


    // $ANTLR start "rulePlusOrMinus"
    // InternalCQLParser.g:3522:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3528:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQLParser.g:3529:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQLParser.g:3529:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQLParser.g:3530:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_60);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3538:3: ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==PlusSign||LA57_0==HyphenMinus) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // InternalCQLParser.g:3539:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQLParser.g:3539:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) )
            	    int alt56=2;
            	    int LA56_0 = input.LA(1);

            	    if ( (LA56_0==PlusSign) ) {
            	        alt56=1;
            	    }
            	    else if ( (LA56_0==HyphenMinus) ) {
            	        alt56=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 56, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt56) {
            	        case 1 :
            	            // InternalCQLParser.g:3540:5: ( () otherlv_2= PlusSign )
            	            {
            	            // InternalCQLParser.g:3540:5: ( () otherlv_2= PlusSign )
            	            // InternalCQLParser.g:3541:6: () otherlv_2= PlusSign
            	            {
            	            // InternalCQLParser.g:3541:6: ()
            	            // InternalCQLParser.g:3542:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,PlusSign,FOLLOW_12); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:3554:5: ( () otherlv_4= HyphenMinus )
            	            {
            	            // InternalCQLParser.g:3554:5: ( () otherlv_4= HyphenMinus )
            	            // InternalCQLParser.g:3555:6: () otherlv_4= HyphenMinus
            	            {
            	            // InternalCQLParser.g:3555:6: ()
            	            // InternalCQLParser.g:3556:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,HyphenMinus,FOLLOW_12); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQLParser.g:3568:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQLParser.g:3569:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQLParser.g:3569:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQLParser.g:3570:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_60);
            	    lv_right_5_0=ruleMulOrDiv();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getPlusOrMinusRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_5_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.MulOrDiv");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCQLParser.g:3592:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQLParser.g:3592:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQLParser.g:3593:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
            {
             newCompositeNode(grammarAccess.getMulOrDivRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMulOrDiv=ruleMulOrDiv();

            state._fsp--;

             current =iv_ruleMulOrDiv; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMulOrDiv"


    // $ANTLR start "ruleMulOrDiv"
    // InternalCQLParser.g:3599:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3605:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQLParser.g:3606:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQLParser.g:3606:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQLParser.g:3607:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_61);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3615:3: ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==Asterisk||LA59_0==Solidus) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // InternalCQLParser.g:3616:4: () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQLParser.g:3616:4: ()
            	    // InternalCQLParser.g:3617:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3623:4: ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) )
            	    // InternalCQLParser.g:3624:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    {
            	    // InternalCQLParser.g:3624:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    // InternalCQLParser.g:3625:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    {
            	    // InternalCQLParser.g:3625:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    int alt58=2;
            	    int LA58_0 = input.LA(1);

            	    if ( (LA58_0==Asterisk) ) {
            	        alt58=1;
            	    }
            	    else if ( (LA58_0==Solidus) ) {
            	        alt58=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 58, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt58) {
            	        case 1 :
            	            // InternalCQLParser.g:3626:7: lv_op_2_1= Asterisk
            	            {
            	            lv_op_2_1=(Token)match(input,Asterisk,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:3637:7: lv_op_2_2= Solidus
            	            {
            	            lv_op_2_2=(Token)match(input,Solidus,FOLLOW_12); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getMulOrDivAccess().getOpSolidusKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;

            	    }


            	    }


            	    }

            	    // InternalCQLParser.g:3650:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQLParser.g:3651:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQLParser.g:3651:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQLParser.g:3652:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_61);
            	    lv_right_3_0=rulePrimary();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getMulOrDivRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Primary");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMulOrDiv"


    // $ANTLR start "entryRulePrimary"
    // InternalCQLParser.g:3674:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQLParser.g:3674:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQLParser.g:3675:2: iv_rulePrimary= rulePrimary EOF
            {
             newCompositeNode(grammarAccess.getPrimaryRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePrimary=rulePrimary();

            state._fsp--;

             current =iv_rulePrimary; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePrimary"


    // $ANTLR start "rulePrimary"
    // InternalCQLParser.g:3681:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
    public final EObject rulePrimary() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_inner_2_0 = null;

        EObject lv_expression_6_0 = null;

        EObject this_Atomic_7 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3687:2: ( ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQLParser.g:3688:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQLParser.g:3688:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt60=3;
            switch ( input.LA(1) ) {
            case LeftParenthesis:
                {
                alt60=1;
                }
                break;
            case NOT:
                {
                alt60=2;
                }
                break;
            case FALSE:
            case TRUE:
            case RULE_ID:
            case RULE_INT:
            case RULE_FLOAT:
            case RULE_STRING:
                {
                alt60=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 60, 0, input);

                throw nvae;
            }

            switch (alt60) {
                case 1 :
                    // InternalCQLParser.g:3689:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    {
                    // InternalCQLParser.g:3689:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    // InternalCQLParser.g:3690:4: () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis
                    {
                    // InternalCQLParser.g:3690:4: ()
                    // InternalCQLParser.g:3691:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_12); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQLParser.g:3701:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQLParser.g:3702:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQLParser.g:3702:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQLParser.g:3703:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_19);
                    lv_inner_2_0=ruleExpression();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getPrimaryRule());
                    						}
                    						set(
                    							current,
                    							"inner",
                    							lv_inner_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Expression");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_3=(Token)match(input,RightParenthesis,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3726:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQLParser.g:3726:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQLParser.g:3727:4: () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQLParser.g:3727:4: ()
                    // InternalCQLParser.g:3728:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,NOT,FOLLOW_12); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQLParser.g:3738:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQLParser.g:3739:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQLParser.g:3739:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQLParser.g:3740:6: lv_expression_6_0= rulePrimary
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getExpressionPrimaryParserRuleCall_1_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_expression_6_0=rulePrimary();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getPrimaryRule());
                    						}
                    						set(
                    							current,
                    							"expression",
                    							lv_expression_6_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Primary");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:3759:3: this_Atomic_7= ruleAtomic
                    {

                    			newCompositeNode(grammarAccess.getPrimaryAccess().getAtomicParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Atomic_7=ruleAtomic();

                    state._fsp--;


                    			current = this_Atomic_7;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePrimary"


    // $ANTLR start "entryRuleAtomic"
    // InternalCQLParser.g:3771:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQLParser.g:3771:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQLParser.g:3772:2: iv_ruleAtomic= ruleAtomic EOF
            {
             newCompositeNode(grammarAccess.getAtomicRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAtomic=ruleAtomic();

            state._fsp--;

             current =iv_ruleAtomic; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAtomic"


    // $ANTLR start "ruleAtomic"
    // InternalCQLParser.g:3778:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
    public final EObject ruleAtomic() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;
        EObject lv_value_9_0 = null;

        EObject lv_value_10_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3784:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQLParser.g:3785:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQLParser.g:3785:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt63=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt63=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt63=2;
                }
                break;
            case RULE_STRING:
                {
                alt63=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt63=4;
                }
                break;
            case RULE_ID:
                {
                alt63=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 63, 0, input);

                throw nvae;
            }

            switch (alt63) {
                case 1 :
                    // InternalCQLParser.g:3786:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:3786:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:3787:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:3787:4: ()
                    // InternalCQLParser.g:3788:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3794:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:3795:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3795:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:3796:6: lv_value_1_0= RULE_INT
                    {
                    lv_value_1_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    						newLeafNode(lv_value_1_0, grammarAccess.getAtomicAccess().getValueINTTerminalRuleCall_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_1_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3814:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:3814:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:3815:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:3815:4: ()
                    // InternalCQLParser.g:3816:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3822:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:3823:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:3823:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:3824:6: lv_value_3_0= RULE_FLOAT
                    {
                    lv_value_3_0=(Token)match(input,RULE_FLOAT,FOLLOW_2); 

                    						newLeafNode(lv_value_3_0, grammarAccess.getAtomicAccess().getValueFLOATTerminalRuleCall_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FLOAT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:3842:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:3842:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:3843:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:3843:4: ()
                    // InternalCQLParser.g:3844:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3850:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:3851:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:3851:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:3852:6: lv_value_5_0= RULE_STRING
                    {
                    lv_value_5_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    						newLeafNode(lv_value_5_0, grammarAccess.getAtomicAccess().getValueSTRINGTerminalRuleCall_2_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_5_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:3870:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:3870:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:3871:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:3871:4: ()
                    // InternalCQLParser.g:3872:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3878:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:3879:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:3879:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:3880:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:3880:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    int alt61=2;
                    int LA61_0 = input.LA(1);

                    if ( (LA61_0==TRUE) ) {
                        alt61=1;
                    }
                    else if ( (LA61_0==FALSE) ) {
                        alt61=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 61, 0, input);

                        throw nvae;
                    }
                    switch (alt61) {
                        case 1 :
                            // InternalCQLParser.g:3881:7: lv_value_7_1= TRUE
                            {
                            lv_value_7_1=(Token)match(input,TRUE,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:3892:7: lv_value_7_2= FALSE
                            {
                            lv_value_7_2=(Token)match(input,FALSE,FOLLOW_2); 

                            							newLeafNode(lv_value_7_2, grammarAccess.getAtomicAccess().getValueFALSEKeyword_3_1_0_1());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_2, null);
                            						

                            }
                            break;

                    }


                    }


                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:3907:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQLParser.g:3907:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQLParser.g:3908:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQLParser.g:3908:4: ()
                    // InternalCQLParser.g:3909:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3915:4: ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt62=2;
                    int LA62_0 = input.LA(1);

                    if ( (LA62_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
                        case FullStop:
                            {
                            int LA62_2 = input.LA(3);

                            if ( (LA62_2==Asterisk) ) {
                                int LA62_5 = input.LA(4);

                                if ( (LA62_5==IN) ) {
                                    alt62=2;
                                }
                                else if ( (LA62_5==EOF||(LA62_5>=ATTACH && LA62_5<=CREATE)||(LA62_5>=HAVING && LA62_5<=STREAM)||LA62_5==GROUP||LA62_5==DROP||(LA62_5>=VIEW && LA62_5<=AND)||(LA62_5>=ExclamationMarkEqualsSign && LA62_5<=GreaterThanSignEqualsSign)||LA62_5==OR||(LA62_5>=RightParenthesis && LA62_5<=PlusSign)||LA62_5==HyphenMinus||LA62_5==Solidus||(LA62_5>=Semicolon && LA62_5<=GreaterThanSign)) ) {
                                    alt62=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 62, 5, input);

                                    throw nvae;
                                }
                            }
                            else if ( (LA62_2==RULE_ID) ) {
                                int LA62_6 = input.LA(4);

                                if ( (LA62_6==IN) ) {
                                    alt62=2;
                                }
                                else if ( (LA62_6==EOF||(LA62_6>=ATTACH && LA62_6<=CREATE)||(LA62_6>=HAVING && LA62_6<=STREAM)||LA62_6==GROUP||LA62_6==DROP||(LA62_6>=VIEW && LA62_6<=AND)||(LA62_6>=ExclamationMarkEqualsSign && LA62_6<=GreaterThanSignEqualsSign)||LA62_6==OR||(LA62_6>=RightParenthesis && LA62_6<=PlusSign)||LA62_6==HyphenMinus||LA62_6==Solidus||(LA62_6>=Semicolon && LA62_6<=GreaterThanSign)) ) {
                                    alt62=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 62, 6, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 62, 2, input);

                                throw nvae;
                            }
                            }
                            break;
                        case EOF:
                        case ATTACH:
                        case CREATE:
                        case HAVING:
                        case SELECT:
                        case STREAM:
                        case GROUP:
                        case DROP:
                        case VIEW:
                        case AND:
                        case ExclamationMarkEqualsSign:
                        case LessThanSignEqualsSign:
                        case GreaterThanSignEqualsSign:
                        case OR:
                        case RightParenthesis:
                        case Asterisk:
                        case PlusSign:
                        case HyphenMinus:
                        case Solidus:
                        case Semicolon:
                        case LessThanSign:
                        case EqualsSign:
                        case GreaterThanSign:
                            {
                            alt62=1;
                            }
                            break;
                        case IN:
                            {
                            alt62=2;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 62, 1, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 62, 0, input);

                        throw nvae;
                    }
                    switch (alt62) {
                        case 1 :
                            // InternalCQLParser.g:3916:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            {
                            // InternalCQLParser.g:3916:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            // InternalCQLParser.g:3917:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            {
                            // InternalCQLParser.g:3917:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            // InternalCQLParser.g:3918:7: lv_value_9_0= ruleAttributeWithoutAliasDefinition
                            {

                            							newCompositeNode(grammarAccess.getAtomicAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_4_1_0_0());
                            						
                            pushFollow(FOLLOW_2);
                            lv_value_9_0=ruleAttributeWithoutAliasDefinition();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getAtomicRule());
                            							}
                            							set(
                            								current,
                            								"value",
                            								lv_value_9_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                            							afterParserOrEnumRuleCall();
                            						

                            }


                            }


                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:3936:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQLParser.g:3936:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQLParser.g:3937:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQLParser.g:3937:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQLParser.g:3938:7: lv_value_10_0= ruleAttributeWithNestedStatement
                            {

                            							newCompositeNode(grammarAccess.getAtomicAccess().getValueAttributeWithNestedStatementParserRuleCall_4_1_1_0());
                            						
                            pushFollow(FOLLOW_2);
                            lv_value_10_0=ruleAttributeWithNestedStatement();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getAtomicRule());
                            							}
                            							set(
                            								current,
                            								"value",
                            								lv_value_10_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithNestedStatement");
                            							afterParserOrEnumRuleCall();
                            						

                            }


                            }


                            }
                            break;

                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAtomic"


    // $ANTLR start "entryRuleAtomicWithoutAttributeRef"
    // InternalCQLParser.g:3961:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQLParser.g:3961:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQLParser.g:3962:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
            {
             newCompositeNode(grammarAccess.getAtomicWithoutAttributeRefRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAtomicWithoutAttributeRef=ruleAtomicWithoutAttributeRef();

            state._fsp--;

             current =iv_ruleAtomicWithoutAttributeRef; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAtomicWithoutAttributeRef"


    // $ANTLR start "ruleAtomicWithoutAttributeRef"
    // InternalCQLParser.g:3968:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) ;
    public final EObject ruleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3974:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) )
            // InternalCQLParser.g:3975:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            {
            // InternalCQLParser.g:3975:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            int alt65=4;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt65=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt65=2;
                }
                break;
            case RULE_STRING:
                {
                alt65=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt65=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 65, 0, input);

                throw nvae;
            }

            switch (alt65) {
                case 1 :
                    // InternalCQLParser.g:3976:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:3976:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:3977:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:3977:4: ()
                    // InternalCQLParser.g:3978:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3984:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:3985:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3985:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:3986:6: lv_value_1_0= RULE_INT
                    {
                    lv_value_1_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    						newLeafNode(lv_value_1_0, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueINTTerminalRuleCall_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_1_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4004:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:4004:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:4005:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:4005:4: ()
                    // InternalCQLParser.g:4006:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4012:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:4013:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:4013:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:4014:6: lv_value_3_0= RULE_FLOAT
                    {
                    lv_value_3_0=(Token)match(input,RULE_FLOAT,FOLLOW_2); 

                    						newLeafNode(lv_value_3_0, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueFLOATTerminalRuleCall_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FLOAT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:4032:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:4032:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:4033:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:4033:4: ()
                    // InternalCQLParser.g:4034:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4040:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:4041:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4041:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:4042:6: lv_value_5_0= RULE_STRING
                    {
                    lv_value_5_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    						newLeafNode(lv_value_5_0, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueSTRINGTerminalRuleCall_2_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_5_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:4060:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:4060:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:4061:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:4061:4: ()
                    // InternalCQLParser.g:4062:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4068:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:4069:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:4069:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:4070:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:4070:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    int alt64=2;
                    int LA64_0 = input.LA(1);

                    if ( (LA64_0==TRUE) ) {
                        alt64=1;
                    }
                    else if ( (LA64_0==FALSE) ) {
                        alt64=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 64, 0, input);

                        throw nvae;
                    }
                    switch (alt64) {
                        case 1 :
                            // InternalCQLParser.g:4071:7: lv_value_7_1= TRUE
                            {
                            lv_value_7_1=(Token)match(input,TRUE,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQLParser.g:4082:7: lv_value_7_2= FALSE
                            {
                            lv_value_7_2=(Token)match(input,FALSE,FOLLOW_2); 

                            							newLeafNode(lv_value_7_2, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueFALSEKeyword_3_1_0_1());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_2, null);
                            						

                            }
                            break;

                    }


                    }


                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAtomicWithoutAttributeRef"


    // $ANTLR start "ruleCreateKeyword"
    // InternalCQLParser.g:4100:1: ruleCreateKeyword returns [Enumerator current=null] : ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) ;
    public final Enumerator ruleCreateKeyword() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4106:2: ( ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) )
            // InternalCQLParser.g:4107:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            {
            // InternalCQLParser.g:4107:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==CREATE) ) {
                alt66=1;
            }
            else if ( (LA66_0==ATTACH) ) {
                alt66=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 66, 0, input);

                throw nvae;
            }
            switch (alt66) {
                case 1 :
                    // InternalCQLParser.g:4108:3: (enumLiteral_0= CREATE )
                    {
                    // InternalCQLParser.g:4108:3: (enumLiteral_0= CREATE )
                    // InternalCQLParser.g:4109:4: enumLiteral_0= CREATE
                    {
                    enumLiteral_0=(Token)match(input,CREATE,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4116:3: (enumLiteral_1= ATTACH )
                    {
                    // InternalCQLParser.g:4116:3: (enumLiteral_1= ATTACH )
                    // InternalCQLParser.g:4117:4: enumLiteral_1= ATTACH
                    {
                    enumLiteral_1=(Token)match(input,ATTACH,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getATTACHEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getCreateKeywordAccess().getATTACHEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCreateKeyword"

    // Delegated rules


    protected DFA2 dfa2 = new DFA2(this);
    static final String dfa_1s = "\24\uffff";
    static final String dfa_2s = "\1\20\3\uffff\2\25\1\uffff\1\75\1\uffff\1\56\2\75\1\57\1\75\1\15\1\75\3\uffff\1\57";
    static final String dfa_3s = "\1\42\3\uffff\2\36\1\uffff\1\75\1\uffff\1\56\2\75\1\62\1\75\1\34\1\75\3\uffff\1\62";
    static final String dfa_4s = "\1\uffff\1\1\1\2\1\3\2\uffff\1\10\1\uffff\1\5\7\uffff\1\7\1\4\1\6\1\uffff";
    static final String dfa_5s = "\24\uffff}>";
    static final String[] dfa_6s = {
            "\1\5\1\4\2\uffff\1\1\1\2\5\uffff\1\3\6\uffff\1\6",
            "",
            "",
            "",
            "\1\7\10\uffff\1\10",
            "\1\7\10\uffff\1\10",
            "",
            "\1\11",
            "",
            "\1\12",
            "\1\13",
            "\1\14",
            "\1\16\2\uffff\1\15",
            "\1\17",
            "\1\22\1\uffff\1\21\14\uffff\1\20",
            "\1\23",
            "",
            "",
            "",
            "\1\16\2\uffff\1\15"
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final char[] dfa_2 = DFA.unpackEncodedStringToUnsignedChars(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final short[] dfa_4 = DFA.unpackEncodedString(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[][] dfa_6 = unpackEncodedStringArray(dfa_6s);

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_2;
            this.max = dfa_3;
            this.accept = dfa_4;
            this.special = dfa_5;
            this.transition = dfa_6;
        }
        public String getDescription() {
            return "109:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000408330002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0xE001000200400400L,0x0000000000000001L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0xE005000220400400L,0x0000000000000001L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0004000020000000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x2000400000000000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x2004400004880002L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0004000004880002L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0xE001401200400400L,0x0000000000000001L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000880002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x2004000000080002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0004000000080002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0800010000000002L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000080000200L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000010000000002L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x002B010000000002L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x002B000000000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x002B000000000002L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0004800000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0004800000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x2000000000100000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000000040200000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000000100001000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000000001001000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000100000000002L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0200002000000002L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x050000C000000002L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x000A000000000002L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0021000000000002L});

}