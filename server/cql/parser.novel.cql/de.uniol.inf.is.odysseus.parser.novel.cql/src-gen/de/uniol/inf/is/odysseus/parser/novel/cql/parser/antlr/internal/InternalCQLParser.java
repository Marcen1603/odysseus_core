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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "STARTTIMESTAMP", "ENDTIMESTAMP", "DATAHANDLER", "PARTITION", "TRANSPORT", "UNBOUNDED", "DISTINCT", "PROTOCOL", "ADVANCE", "BOOLEAN", "CHANNEL", "INTEGER", "OPTIONS", "WRAPPER", "ATTACH", "CREATE", "DOUBLE", "EXISTS", "HAVING", "SELECT", "STREAM", "STRING", "FALSE", "FLOAT", "GROUP", "TUPLE", "WHERE", "DROP", "FILE", "FROM", "LONG", "SINK", "SIZE", "TIME", "TRUE", "VIEW", "AND", "NOT", "ExclamationMarkEqualsSign", "LessThanSignEqualsSign", "GreaterThanSignEqualsSign", "AS", "BY", "IF", "IN", "OR", "TO", "LeftParenthesis", "RightParenthesis", "Asterisk", "PlusSign", "Comma", "HyphenMinus", "FullStop", "Solidus", "Colon", "Semicolon", "LessThanSign", "EqualsSign", "GreaterThanSign", "LeftSquareBracket", "RightSquareBracket", "RULE_ID", "RULE_INT", "RULE_FLOAT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER"
    };
    public static final int CREATE=19;
    public static final int FROM=33;
    public static final int VIEW=39;
    public static final int LessThanSign=61;
    public static final int LeftParenthesis=51;
    public static final int UNBOUNDED=9;
    public static final int WRAPPER=17;
    public static final int PARTITION=7;
    public static final int IF=47;
    public static final int RightSquareBracket=65;
    public static final int INTEGER=15;
    public static final int GreaterThanSign=63;
    public static final int RULE_ID=66;
    public static final int IN=48;
    public static final int DISTINCT=10;
    public static final int SIZE=36;
    public static final int PROTOCOL=11;
    public static final int RightParenthesis=52;
    public static final int TRUE=38;
    public static final int OPTIONS=16;
    public static final int WHERE=30;
    public static final int BOOLEAN=13;
    public static final int GreaterThanSignEqualsSign=44;
    public static final int NOT=41;
    public static final int AS=45;
    public static final int CHANNEL=14;
    public static final int SINK=35;
    public static final int PlusSign=54;
    public static final int RULE_INT=67;
    public static final int AND=40;
    public static final int RULE_ML_COMMENT=70;
    public static final int LeftSquareBracket=64;
    public static final int ADVANCE=12;
    public static final int HAVING=22;
    public static final int FLOAT=27;
    public static final int STARTTIMESTAMP=4;
    public static final int RULE_STRING=69;
    public static final int DROP=31;
    public static final int RULE_SL_COMMENT=71;
    public static final int GROUP=28;
    public static final int Comma=55;
    public static final int EqualsSign=62;
    public static final int TRANSPORT=8;
    public static final int HyphenMinus=56;
    public static final int BY=46;
    public static final int DOUBLE=20;
    public static final int LessThanSignEqualsSign=43;
    public static final int Solidus=58;
    public static final int Colon=59;
    public static final int DATAHANDLER=6;
    public static final int FILE=32;
    public static final int EOF=-1;
    public static final int Asterisk=53;
    public static final int LONG=34;
    public static final int FullStop=57;
    public static final int OR=49;
    public static final int EXISTS=21;
    public static final int RULE_WS=72;
    public static final int ENDTIMESTAMP=5;
    public static final int STREAM=24;
    public static final int TIME=37;
    public static final int RULE_ANY_OTHER=73;
    public static final int SELECT=23;
    public static final int TUPLE=29;
    public static final int Semicolon=60;
    public static final int ATTACH=18;
    public static final int STRING=25;
    public static final int RULE_FLOAT=68;
    public static final int FALSE=26;
    public static final int ExclamationMarkEqualsSign=42;
    public static final int TO=50;

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
                case FullStop:
                    {
                    int LA15_3 = input.LA(3);

                    if ( (LA15_3==RULE_ID) ) {
                        int LA15_5 = input.LA(4);

                        if ( ((LA15_5>=Asterisk && LA15_5<=PlusSign)||LA15_5==HyphenMinus||LA15_5==Solidus) ) {
                            alt15=2;
                        }
                        else if ( (LA15_5==EOF||LA15_5==FALSE||LA15_5==FROM||LA15_5==TRUE||LA15_5==AS||LA15_5==Comma||(LA15_5>=RULE_ID && LA15_5<=RULE_STRING)) ) {
                            alt15=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 5, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA15_3==Asterisk) ) {
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
                case LeftParenthesis:
                case Asterisk:
                case PlusSign:
                case HyphenMinus:
                case Solidus:
                    {
                    alt15=2;
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

                if ( (LA21_1==FullStop) ) {
                    int LA21_2 = input.LA(3);

                    if ( (LA21_2==RULE_ID) ) {
                        alt21=2;
                    }
                    else if ( (LA21_2==Asterisk) ) {
                        alt21=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA21_1==EOF||(LA21_1>=STARTTIMESTAMP && LA21_1<=ENDTIMESTAMP)||LA21_1==BOOLEAN||LA21_1==INTEGER||(LA21_1>=ATTACH && LA21_1<=DOUBLE)||(LA21_1>=HAVING && LA21_1<=GROUP)||LA21_1==DROP||(LA21_1>=FROM && LA21_1<=LONG)||(LA21_1>=TRUE && LA21_1<=AND)||(LA21_1>=ExclamationMarkEqualsSign && LA21_1<=AS)||(LA21_1>=IN && LA21_1<=OR)||(LA21_1>=RightParenthesis && LA21_1<=HyphenMinus)||LA21_1==Solidus||(LA21_1>=Semicolon && LA21_1<=GreaterThanSign)||(LA21_1>=RightSquareBracket && LA21_1<=RULE_STRING)) ) {
                    alt21=1;
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


    // $ANTLR start "entryRuleFunctionWithoutAlias"
    // InternalCQLParser.g:1066:1: entryRuleFunctionWithoutAlias returns [EObject current=null] : iv_ruleFunctionWithoutAlias= ruleFunctionWithoutAlias EOF ;
    public final EObject entryRuleFunctionWithoutAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunctionWithoutAlias = null;


        try {
            // InternalCQLParser.g:1066:61: (iv_ruleFunctionWithoutAlias= ruleFunctionWithoutAlias EOF )
            // InternalCQLParser.g:1067:2: iv_ruleFunctionWithoutAlias= ruleFunctionWithoutAlias EOF
            {
             newCompositeNode(grammarAccess.getFunctionWithoutAliasRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFunctionWithoutAlias=ruleFunctionWithoutAlias();

            state._fsp--;

             current =iv_ruleFunctionWithoutAlias; 
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
    // $ANTLR end "entryRuleFunctionWithoutAlias"


    // $ANTLR start "ruleFunctionWithoutAlias"
    // InternalCQLParser.g:1073:1: ruleFunctionWithoutAlias returns [EObject current=null] : ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) ;
    public final EObject ruleFunctionWithoutAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_value_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1079:2: ( ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) )
            // InternalCQLParser.g:1080:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            {
            // InternalCQLParser.g:1080:2: ( () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            // InternalCQLParser.g:1081:3: () ( (lv_name_1_0= RULE_ID ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis
            {
            // InternalCQLParser.g:1081:3: ()
            // InternalCQLParser.g:1082:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getFunctionWithoutAliasAccess().getFunctionWithoutAliasAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1088:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:1089:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:1089:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:1090:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_28); 

            					newLeafNode(lv_name_1_0, grammarAccess.getFunctionWithoutAliasAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFunctionWithoutAliasRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,LeftParenthesis,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getFunctionWithoutAliasAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQLParser.g:1110:3: ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
            // InternalCQLParser.g:1111:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1111:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            // InternalCQLParser.g:1112:5: lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getFunctionWithoutAliasAccess().getValueSelectExpressionWithoutAliasDefinitionParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_19);
            lv_value_3_0=ruleSelectExpressionWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getFunctionWithoutAliasRule());
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

            			newLeafNode(otherlv_4, grammarAccess.getFunctionWithoutAliasAccess().getRightParenthesisKeyword_4());
            		

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
    // $ANTLR end "ruleFunctionWithoutAlias"


    // $ANTLR start "entryRuleExpressionComponentConstantOrAttribute"
    // InternalCQLParser.g:1137:1: entryRuleExpressionComponentConstantOrAttribute returns [EObject current=null] : iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF ;
    public final EObject entryRuleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentConstantOrAttribute = null;


        try {
            // InternalCQLParser.g:1137:79: (iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF )
            // InternalCQLParser.g:1138:2: iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF
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
    // InternalCQLParser.g:1144:1: ruleExpressionComponentConstantOrAttribute returns [EObject current=null] : ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) ;
    public final EObject ruleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1150:2: ( ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) )
            // InternalCQLParser.g:1151:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            {
            // InternalCQLParser.g:1151:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==FALSE||LA22_0==TRUE||(LA22_0>=RULE_INT && LA22_0<=RULE_STRING)) ) {
                alt22=1;
            }
            else if ( (LA22_0==RULE_ID) ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // InternalCQLParser.g:1152:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1152:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1153:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1153:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1154:5: lv_value_0_0= ruleAtomicWithoutAttributeRef
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
                    // InternalCQLParser.g:1172:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:1172:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQLParser.g:1173:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:1173:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQLParser.g:1174:5: lv_value_1_0= ruleAttributeWithoutAliasDefinition
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


    // $ANTLR start "entryRuleExpressionComponentMapperOrConstant"
    // InternalCQLParser.g:1195:1: entryRuleExpressionComponentMapperOrConstant returns [EObject current=null] : iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF ;
    public final EObject entryRuleExpressionComponentMapperOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentMapperOrConstant = null;


        try {
            // InternalCQLParser.g:1195:76: (iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF )
            // InternalCQLParser.g:1196:2: iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentMapperOrConstantRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentMapperOrConstant=ruleExpressionComponentMapperOrConstant();

            state._fsp--;

             current =iv_ruleExpressionComponentMapperOrConstant; 
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
    // $ANTLR end "entryRuleExpressionComponentMapperOrConstant"


    // $ANTLR start "ruleExpressionComponentMapperOrConstant"
    // InternalCQLParser.g:1202:1: ruleExpressionComponentMapperOrConstant returns [EObject current=null] : ( (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) ;
    public final EObject ruleExpressionComponentMapperOrConstant() throws RecognitionException {
        EObject current = null;

        EObject this_FunctionWithoutAlias_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1208:2: ( ( (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQLParser.g:1209:2: ( (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQLParser.g:1209:2: ( (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==RULE_ID) ) {
                alt23=1;
            }
            else if ( (LA23_0==FALSE||LA23_0==TRUE||(LA23_0>=RULE_INT && LA23_0<=RULE_STRING)) ) {
                alt23=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }
            switch (alt23) {
                case 1 :
                    // InternalCQLParser.g:1210:3: (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () )
                    {
                    // InternalCQLParser.g:1210:3: (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () )
                    // InternalCQLParser.g:1211:4: this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias ()
                    {

                    				newCompositeNode(grammarAccess.getExpressionComponentMapperOrConstantAccess().getFunctionWithoutAliasParserRuleCall_0_0());
                    			
                    pushFollow(FOLLOW_2);
                    this_FunctionWithoutAlias_0=ruleFunctionWithoutAlias();

                    state._fsp--;


                    				current = this_FunctionWithoutAlias_0;
                    				afterParserOrEnumRuleCall();
                    			
                    // InternalCQLParser.g:1219:4: ()
                    // InternalCQLParser.g:1220:5: 
                    {

                    					current = forceCreateModelElementAndSet(
                    						grammarAccess.getExpressionComponentMapperOrConstantAccess().getExpressionComponentValueAction_0_1(),
                    						current);
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1228:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1228:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1229:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1229:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1230:5: lv_value_2_0= ruleAtomicWithoutAttributeRef
                    {

                    					newCompositeNode(grammarAccess.getExpressionComponentMapperOrConstantAccess().getValueAtomicWithoutAttributeRefParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_value_2_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getExpressionComponentMapperOrConstantRule());
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
    // $ANTLR end "ruleExpressionComponentMapperOrConstant"


    // $ANTLR start "entryRuleExpressionComponentOnlyAttribute"
    // InternalCQLParser.g:1251:1: entryRuleExpressionComponentOnlyAttribute returns [EObject current=null] : iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF ;
    public final EObject entryRuleExpressionComponentOnlyAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlyAttribute = null;


        try {
            // InternalCQLParser.g:1251:73: (iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF )
            // InternalCQLParser.g:1252:2: iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentOnlyAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentOnlyAttribute=ruleExpressionComponentOnlyAttribute();

            state._fsp--;

             current =iv_ruleExpressionComponentOnlyAttribute; 
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
    // $ANTLR end "entryRuleExpressionComponentOnlyAttribute"


    // $ANTLR start "ruleExpressionComponentOnlyAttribute"
    // InternalCQLParser.g:1258:1: ruleExpressionComponentOnlyAttribute returns [EObject current=null] : ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) ;
    public final EObject ruleExpressionComponentOnlyAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1264:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) )
            // InternalCQLParser.g:1265:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            {
            // InternalCQLParser.g:1265:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1266:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1266:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1267:4: lv_value_0_0= ruleAttributeWithoutAliasDefinition
            {

            				newCompositeNode(grammarAccess.getExpressionComponentOnlyAttributeAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_value_0_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getExpressionComponentOnlyAttributeRule());
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
    // $ANTLR end "ruleExpressionComponentOnlyAttribute"


    // $ANTLR start "entryRuleExpressionComponentOnlymapper"
    // InternalCQLParser.g:1287:1: entryRuleExpressionComponentOnlymapper returns [EObject current=null] : iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF ;
    public final EObject entryRuleExpressionComponentOnlymapper() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlymapper = null;


        try {
            // InternalCQLParser.g:1287:70: (iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF )
            // InternalCQLParser.g:1288:2: iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF
            {
             newCompositeNode(grammarAccess.getExpressionComponentOnlymapperRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpressionComponentOnlymapper=ruleExpressionComponentOnlymapper();

            state._fsp--;

             current =iv_ruleExpressionComponentOnlymapper; 
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
    // $ANTLR end "entryRuleExpressionComponentOnlymapper"


    // $ANTLR start "ruleExpressionComponentOnlymapper"
    // InternalCQLParser.g:1294:1: ruleExpressionComponentOnlymapper returns [EObject current=null] : (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () ) ;
    public final EObject ruleExpressionComponentOnlymapper() throws RecognitionException {
        EObject current = null;

        EObject this_FunctionWithoutAlias_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1300:2: ( (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () ) )
            // InternalCQLParser.g:1301:2: (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () )
            {
            // InternalCQLParser.g:1301:2: (this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias () )
            // InternalCQLParser.g:1302:3: this_FunctionWithoutAlias_0= ruleFunctionWithoutAlias ()
            {

            			newCompositeNode(grammarAccess.getExpressionComponentOnlymapperAccess().getFunctionWithoutAliasParserRuleCall_0());
            		
            pushFollow(FOLLOW_2);
            this_FunctionWithoutAlias_0=ruleFunctionWithoutAlias();

            state._fsp--;


            			current = this_FunctionWithoutAlias_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1310:3: ()
            // InternalCQLParser.g:1311:4: 
            {

            				current = forceCreateModelElementAndSet(
            					grammarAccess.getExpressionComponentOnlymapperAccess().getExpressionComponentValueAction_1(),
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
    // $ANTLR end "ruleExpressionComponentOnlymapper"


    // $ANTLR start "entryRuleSelectExpression"
    // InternalCQLParser.g:1321:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQLParser.g:1321:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQLParser.g:1322:2: iv_ruleSelectExpression= ruleSelectExpression EOF
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
    // InternalCQLParser.g:1328:1: ruleSelectExpression returns [EObject current=null] : ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
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
            // InternalCQLParser.g:1334:2: ( ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1335:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1335:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1336:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1336:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==RULE_ID) ) {
                int LA30_1 = input.LA(2);

                if ( (LA30_1==LeftParenthesis) ) {
                    alt30=1;
                }
                else if ( ((LA30_1>=Asterisk && LA30_1<=PlusSign)||(LA30_1>=HyphenMinus && LA30_1<=Solidus)) ) {
                    alt30=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 30, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA30_0==FALSE||LA30_0==TRUE||(LA30_0>=RULE_INT && LA30_0<=RULE_STRING)) ) {
                alt30=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // InternalCQLParser.g:1337:4: ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    {
                    // InternalCQLParser.g:1337:4: ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    // InternalCQLParser.g:1338:5: ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    {
                    // InternalCQLParser.g:1338:5: ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) )
                    // InternalCQLParser.g:1339:6: (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant )
                    {
                    // InternalCQLParser.g:1339:6: (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant )
                    // InternalCQLParser.g:1340:7: lv_expressions_0_0= ruleExpressionComponentMapperOrConstant
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentMapperOrConstantParserRuleCall_0_0_0_0());
                    						
                    pushFollow(FOLLOW_29);
                    lv_expressions_0_0=ruleExpressionComponentMapperOrConstant();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_0_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentMapperOrConstant");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1357:5: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    loop26:
                    do {
                        int alt26=2;
                        int LA26_0 = input.LA(1);

                        if ( ((LA26_0>=Asterisk && LA26_0<=PlusSign)||LA26_0==HyphenMinus||LA26_0==Solidus) ) {
                            alt26=1;
                        }


                        switch (alt26) {
                    	case 1 :
                    	    // InternalCQLParser.g:1358:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1358:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1359:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1359:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    // InternalCQLParser.g:1360:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1360:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
                    	    int alt24=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt24=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt24=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt24=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt24=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 24, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt24) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1361:9: lv_operators_1_1= PlusSign
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
                    	            // InternalCQLParser.g:1372:9: lv_operators_1_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1383:9: lv_operators_1_3= Asterisk
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
                    	            // InternalCQLParser.g:1394:9: lv_operators_1_4= Solidus
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

                    	    // InternalCQLParser.g:1407:6: ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1408:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1408:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1409:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1409:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper )
                    	    int alt25=2;
                    	    int LA25_0 = input.LA(1);

                    	    if ( (LA25_0==FALSE||LA25_0==TRUE||(LA25_0>=RULE_INT && LA25_0<=RULE_STRING)) ) {
                    	        alt25=1;
                    	    }
                    	    else if ( (LA25_0==RULE_ID) ) {
                    	        int LA25_2 = input.LA(2);

                    	        if ( (LA25_2==EOF||LA25_2==FALSE||LA25_2==FROM||LA25_2==TRUE||LA25_2==AS||(LA25_2>=Asterisk && LA25_2<=Solidus)||(LA25_2>=RULE_ID && LA25_2<=RULE_STRING)) ) {
                    	            alt25=1;
                    	        }
                    	        else if ( (LA25_2==LeftParenthesis) ) {
                    	            alt25=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 25, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 25, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt25) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1410:9: lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_0_0_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_29);
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
                    	            // InternalCQLParser.g:1426:9: lv_expressions_2_2= ruleExpressionComponentOnlymapper
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentOnlymapperParserRuleCall_0_0_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_29);
                    	            lv_expressions_2_2=ruleExpressionComponentOnlymapper();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_2_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlymapper");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop26;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1447:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1447:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    // InternalCQLParser.g:1448:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    {
                    // InternalCQLParser.g:1448:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) )
                    // InternalCQLParser.g:1449:6: (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute )
                    {
                    // InternalCQLParser.g:1449:6: (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute )
                    // InternalCQLParser.g:1450:7: lv_expressions_3_0= ruleExpressionComponentOnlyAttribute
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentOnlyAttributeParserRuleCall_0_1_0_0());
                    						
                    pushFollow(FOLLOW_30);
                    lv_expressions_3_0=ruleExpressionComponentOnlyAttribute();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_3_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyAttribute");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1467:5: ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    int cnt29=0;
                    loop29:
                    do {
                        int alt29=2;
                        int LA29_0 = input.LA(1);

                        if ( ((LA29_0>=Asterisk && LA29_0<=PlusSign)||LA29_0==HyphenMinus||LA29_0==Solidus) ) {
                            alt29=1;
                        }


                        switch (alt29) {
                    	case 1 :
                    	    // InternalCQLParser.g:1468:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1468:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1469:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1469:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    // InternalCQLParser.g:1470:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1470:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
                    	    int alt27=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt27=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt27=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt27=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt27=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 27, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt27) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1471:9: lv_operators_4_1= PlusSign
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
                    	            // InternalCQLParser.g:1482:9: lv_operators_4_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1493:9: lv_operators_4_3= Asterisk
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
                    	            // InternalCQLParser.g:1504:9: lv_operators_4_4= Solidus
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

                    	    // InternalCQLParser.g:1517:6: ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1518:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1518:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1519:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1519:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper )
                    	    int alt28=2;
                    	    int LA28_0 = input.LA(1);

                    	    if ( (LA28_0==FALSE||LA28_0==TRUE||(LA28_0>=RULE_INT && LA28_0<=RULE_STRING)) ) {
                    	        alt28=1;
                    	    }
                    	    else if ( (LA28_0==RULE_ID) ) {
                    	        int LA28_2 = input.LA(2);

                    	        if ( (LA28_2==EOF||LA28_2==FALSE||LA28_2==FROM||LA28_2==TRUE||LA28_2==AS||(LA28_2>=Asterisk && LA28_2<=Solidus)||(LA28_2>=RULE_ID && LA28_2<=RULE_STRING)) ) {
                    	            alt28=1;
                    	        }
                    	        else if ( (LA28_2==LeftParenthesis) ) {
                    	            alt28=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 28, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 28, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt28) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1520:9: lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_0_1_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_29);
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
                    	            // InternalCQLParser.g:1536:9: lv_expressions_5_2= ruleExpressionComponentOnlymapper
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionAccess().getExpressionsExpressionComponentOnlymapperParserRuleCall_0_1_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_29);
                    	            lv_expressions_5_2=ruleExpressionComponentOnlymapper();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_5_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlymapper");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt29 >= 1 ) break loop29;
                                EarlyExitException eee =
                                    new EarlyExitException(29, input);
                                throw eee;
                        }
                        cnt29++;
                    } while (true);


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:1557:3: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==AS) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // InternalCQLParser.g:1558:4: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_6, grammarAccess.getSelectExpressionAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:1562:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQLParser.g:1563:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1563:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQLParser.g:1564:6: lv_alias_7_0= ruleAlias
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
    // InternalCQLParser.g:1586:1: entryRuleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF ;
    public final EObject entryRuleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:1586:79: (iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF )
            // InternalCQLParser.g:1587:2: iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF
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
    // InternalCQLParser.g:1593:1: ruleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) ) ;
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
            // InternalCQLParser.g:1599:2: ( (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) ) )
            // InternalCQLParser.g:1600:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) )
            {
            // InternalCQLParser.g:1600:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) )
            // InternalCQLParser.g:1601:3: this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            {

            			newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getSelectExpressionWithOnlyAttributeOrConstantParserRuleCall_0());
            		
            pushFollow(FOLLOW_5);
            this_SelectExpressionWithOnlyAttributeOrConstant_0=ruleSelectExpressionWithOnlyAttributeOrConstant();

            state._fsp--;


            			current = this_SelectExpressionWithOnlyAttributeOrConstant_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1609:3: ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==RULE_ID) ) {
                int LA38_1 = input.LA(2);

                if ( (LA38_1==LeftParenthesis) ) {
                    alt38=1;
                }
                else if ( ((LA38_1>=Asterisk && LA38_1<=PlusSign)||(LA38_1>=HyphenMinus && LA38_1<=Solidus)) ) {
                    alt38=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 38, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA38_0==FALSE||LA38_0==TRUE||(LA38_0>=RULE_INT && LA38_0<=RULE_STRING)) ) {
                alt38=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // InternalCQLParser.g:1610:4: ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    {
                    // InternalCQLParser.g:1610:4: ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    // InternalCQLParser.g:1611:5: ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    {
                    // InternalCQLParser.g:1611:5: ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) )
                    // InternalCQLParser.g:1612:6: (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant )
                    {
                    // InternalCQLParser.g:1612:6: (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant )
                    // InternalCQLParser.g:1613:7: lv_expressions_1_0= ruleExpressionComponentMapperOrConstant
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentMapperOrConstantParserRuleCall_1_0_0_0());
                    						
                    pushFollow(FOLLOW_31);
                    lv_expressions_1_0=ruleExpressionComponentMapperOrConstant();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_1_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentMapperOrConstant");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1630:5: ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    loop34:
                    do {
                        int alt34=2;
                        int LA34_0 = input.LA(1);

                        if ( ((LA34_0>=Asterisk && LA34_0<=PlusSign)||LA34_0==HyphenMinus||LA34_0==Solidus) ) {
                            alt34=1;
                        }


                        switch (alt34) {
                    	case 1 :
                    	    // InternalCQLParser.g:1631:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1631:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1632:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1632:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    // InternalCQLParser.g:1633:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1633:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
                    	    int alt32=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt32=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt32=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt32=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt32=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 32, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt32) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1634:9: lv_operators_2_1= PlusSign
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
                    	            // InternalCQLParser.g:1645:9: lv_operators_2_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1656:9: lv_operators_2_3= Asterisk
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
                    	            // InternalCQLParser.g:1667:9: lv_operators_2_4= Solidus
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

                    	    // InternalCQLParser.g:1680:6: ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1681:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1681:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1682:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1682:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper )
                    	    int alt33=2;
                    	    int LA33_0 = input.LA(1);

                    	    if ( (LA33_0==FALSE||LA33_0==TRUE||(LA33_0>=RULE_INT && LA33_0<=RULE_STRING)) ) {
                    	        alt33=1;
                    	    }
                    	    else if ( (LA33_0==RULE_ID) ) {
                    	        int LA33_2 = input.LA(2);

                    	        if ( (LA33_2==EOF||(LA33_2>=RightParenthesis && LA33_2<=PlusSign)||(LA33_2>=HyphenMinus && LA33_2<=Solidus)) ) {
                    	            alt33=1;
                    	        }
                    	        else if ( (LA33_2==LeftParenthesis) ) {
                    	            alt33=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 33, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 33, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt33) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1683:9: lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_1_0_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_31);
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
                    	            // InternalCQLParser.g:1699:9: lv_expressions_3_2= ruleExpressionComponentOnlymapper
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentOnlymapperParserRuleCall_1_0_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_31);
                    	            lv_expressions_3_2=ruleExpressionComponentOnlymapper();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_3_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlymapper");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop34;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1720:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1720:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    // InternalCQLParser.g:1721:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    {
                    // InternalCQLParser.g:1721:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) )
                    // InternalCQLParser.g:1722:6: (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute )
                    {
                    // InternalCQLParser.g:1722:6: (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute )
                    // InternalCQLParser.g:1723:7: lv_expressions_4_0= ruleExpressionComponentOnlyAttribute
                    {

                    							newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentOnlyAttributeParserRuleCall_1_1_0_0());
                    						
                    pushFollow(FOLLOW_30);
                    lv_expressions_4_0=ruleExpressionComponentOnlyAttribute();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    							}
                    							add(
                    								current,
                    								"expressions",
                    								lv_expressions_4_0,
                    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlyAttribute");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCQLParser.g:1740:5: ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    int cnt37=0;
                    loop37:
                    do {
                        int alt37=2;
                        int LA37_0 = input.LA(1);

                        if ( ((LA37_0>=Asterisk && LA37_0<=PlusSign)||LA37_0==HyphenMinus||LA37_0==Solidus) ) {
                            alt37=1;
                        }


                        switch (alt37) {
                    	case 1 :
                    	    // InternalCQLParser.g:1741:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1741:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1742:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1742:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    // InternalCQLParser.g:1743:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1743:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
                    	    int alt35=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt35=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt35=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt35=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt35=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 35, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt35) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1744:9: lv_operators_5_1= PlusSign
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
                    	            // InternalCQLParser.g:1755:9: lv_operators_5_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1766:9: lv_operators_5_3= Asterisk
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
                    	            // InternalCQLParser.g:1777:9: lv_operators_5_4= Solidus
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

                    	    // InternalCQLParser.g:1790:6: ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1791:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1791:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1792:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1792:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper )
                    	    int alt36=2;
                    	    int LA36_0 = input.LA(1);

                    	    if ( (LA36_0==FALSE||LA36_0==TRUE||(LA36_0>=RULE_INT && LA36_0<=RULE_STRING)) ) {
                    	        alt36=1;
                    	    }
                    	    else if ( (LA36_0==RULE_ID) ) {
                    	        int LA36_2 = input.LA(2);

                    	        if ( (LA36_2==EOF||(LA36_2>=RightParenthesis && LA36_2<=PlusSign)||(LA36_2>=HyphenMinus && LA36_2<=Solidus)) ) {
                    	            alt36=1;
                    	        }
                    	        else if ( (LA36_2==LeftParenthesis) ) {
                    	            alt36=2;
                    	        }
                    	        else {
                    	            NoViableAltException nvae =
                    	                new NoViableAltException("", 36, 2, input);

                    	            throw nvae;
                    	        }
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 36, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt36) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1793:9: lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_1_1_1_1_0_0());
                    	            								
                    	            pushFollow(FOLLOW_31);
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
                    	            // InternalCQLParser.g:1809:9: lv_expressions_6_2= ruleExpressionComponentOnlymapper
                    	            {

                    	            									newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsExpressionComponentOnlymapperParserRuleCall_1_1_1_1_0_1());
                    	            								
                    	            pushFollow(FOLLOW_31);
                    	            lv_expressions_6_2=ruleExpressionComponentOnlymapper();

                    	            state._fsp--;


                    	            									if (current==null) {
                    	            										current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									add(
                    	            										current,
                    	            										"expressions",
                    	            										lv_expressions_6_2,
                    	            										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionComponentOnlymapper");
                    	            									afterParserOrEnumRuleCall();
                    	            								

                    	            }
                    	            break;

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt37 >= 1 ) break loop37;
                                EarlyExitException eee =
                                    new EarlyExitException(37, input);
                                throw eee;
                        }
                        cnt37++;
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
    // InternalCQLParser.g:1834:1: entryRuleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF ;
    public final EObject entryRuleSelectExpressionWithOnlyAttributeOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithOnlyAttributeOrConstant = null;


        try {
            // InternalCQLParser.g:1834:84: (iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF )
            // InternalCQLParser.g:1835:2: iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF
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
    // InternalCQLParser.g:1841:1: ruleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) ;
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
            // InternalCQLParser.g:1847:2: ( ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) )
            // InternalCQLParser.g:1848:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            {
            // InternalCQLParser.g:1848:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            // InternalCQLParser.g:1849:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            {
            // InternalCQLParser.g:1849:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) )
            // InternalCQLParser.g:1850:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            {
            // InternalCQLParser.g:1850:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            // InternalCQLParser.g:1851:5: lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute
            {

            					newCompositeNode(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_31);
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

            // InternalCQLParser.g:1868:3: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            loop40:
            do {
                int alt40=2;
                int LA40_0 = input.LA(1);

                if ( ((LA40_0>=Asterisk && LA40_0<=PlusSign)||LA40_0==HyphenMinus||LA40_0==Solidus) ) {
                    alt40=1;
                }


                switch (alt40) {
            	case 1 :
            	    // InternalCQLParser.g:1869:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    {
            	    // InternalCQLParser.g:1869:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
            	    // InternalCQLParser.g:1870:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    {
            	    // InternalCQLParser.g:1870:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    // InternalCQLParser.g:1871:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    {
            	    // InternalCQLParser.g:1871:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    int alt39=4;
            	    switch ( input.LA(1) ) {
            	    case PlusSign:
            	        {
            	        alt39=1;
            	        }
            	        break;
            	    case HyphenMinus:
            	        {
            	        alt39=2;
            	        }
            	        break;
            	    case Asterisk:
            	        {
            	        alt39=3;
            	        }
            	        break;
            	    case Solidus:
            	        {
            	        alt39=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 39, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt39) {
            	        case 1 :
            	            // InternalCQLParser.g:1872:7: lv_operators_1_1= PlusSign
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
            	            // InternalCQLParser.g:1883:7: lv_operators_1_2= HyphenMinus
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
            	            // InternalCQLParser.g:1894:7: lv_operators_1_3= Asterisk
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
            	            // InternalCQLParser.g:1905:7: lv_operators_1_4= Solidus
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

            	    // InternalCQLParser.g:1918:4: ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    // InternalCQLParser.g:1919:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    {
            	    // InternalCQLParser.g:1919:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    // InternalCQLParser.g:1920:6: lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getExpressionsExpressionComponentConstantOrAttributeParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_31);
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
            	    break loop40;
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

            otherlv_8=(Token)match(input,OPTIONS,FOLLOW_28); 

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
    // InternalCQLParser.g:2183:1: ruleAttributeDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis ) ;
    public final EObject ruleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_4=null;
        Token otherlv_7=null;
        EObject lv_attributes_2_0 = null;

        EObject lv_datatypes_3_0 = null;

        EObject lv_attributes_5_0 = null;

        EObject lv_datatypes_6_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2189:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis ) )
            // InternalCQLParser.g:2190:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis )
            {
            // InternalCQLParser.g:2190:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis )
            // InternalCQLParser.g:2191:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis
            {
            // InternalCQLParser.g:2191:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2192:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2192:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2193:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_28); 

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
            		
            // InternalCQLParser.g:2213:3: ( (lv_attributes_2_0= ruleAttribute ) )+
            int cnt43=0;
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==RULE_ID) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalCQLParser.g:2214:4: (lv_attributes_2_0= ruleAttribute )
            	    {
            	    // InternalCQLParser.g:2214:4: (lv_attributes_2_0= ruleAttribute )
            	    // InternalCQLParser.g:2215:5: lv_attributes_2_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getAttributesAttributeParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_38);
            	    lv_attributes_2_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAttributeDefinitionRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_2_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt43 >= 1 ) break loop43;
                        EarlyExitException eee =
                            new EarlyExitException(43, input);
                        throw eee;
                }
                cnt43++;
            } while (true);

            // InternalCQLParser.g:2232:3: ( (lv_datatypes_3_0= ruleDataType ) )+
            int cnt44=0;
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( ((LA44_0>=STARTTIMESTAMP && LA44_0<=ENDTIMESTAMP)||LA44_0==BOOLEAN||LA44_0==INTEGER||LA44_0==DOUBLE||LA44_0==STRING||LA44_0==FLOAT||LA44_0==LONG) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // InternalCQLParser.g:2233:4: (lv_datatypes_3_0= ruleDataType )
            	    {
            	    // InternalCQLParser.g:2233:4: (lv_datatypes_3_0= ruleDataType )
            	    // InternalCQLParser.g:2234:5: lv_datatypes_3_0= ruleDataType
            	    {

            	    					newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getDatatypesDataTypeParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_39);
            	    lv_datatypes_3_0=ruleDataType();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAttributeDefinitionRule());
            	    					}
            	    					add(
            	    						current,
            	    						"datatypes",
            	    						lv_datatypes_3_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DataType");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt44 >= 1 ) break loop44;
                        EarlyExitException eee =
                            new EarlyExitException(44, input);
                        throw eee;
                }
                cnt44++;
            } while (true);

            // InternalCQLParser.g:2251:3: (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==Comma) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // InternalCQLParser.g:2252:4: otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) )
            	    {
            	    otherlv_4=(Token)match(input,Comma,FOLLOW_15); 

            	    				newLeafNode(otherlv_4, grammarAccess.getAttributeDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQLParser.g:2256:4: ( (lv_attributes_5_0= ruleAttribute ) )
            	    // InternalCQLParser.g:2257:5: (lv_attributes_5_0= ruleAttribute )
            	    {
            	    // InternalCQLParser.g:2257:5: (lv_attributes_5_0= ruleAttribute )
            	    // InternalCQLParser.g:2258:6: lv_attributes_5_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getAttributesAttributeParserRuleCall_4_1_0());
            	    					
            	    pushFollow(FOLLOW_40);
            	    lv_attributes_5_0=ruleAttribute();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAttributeDefinitionRule());
            	    						}
            	    						add(
            	    							current,
            	    							"attributes",
            	    							lv_attributes_5_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCQLParser.g:2275:4: ( (lv_datatypes_6_0= ruleDataType ) )
            	    // InternalCQLParser.g:2276:5: (lv_datatypes_6_0= ruleDataType )
            	    {
            	    // InternalCQLParser.g:2276:5: (lv_datatypes_6_0= ruleDataType )
            	    // InternalCQLParser.g:2277:6: lv_datatypes_6_0= ruleDataType
            	    {

            	    						newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getDatatypesDataTypeParserRuleCall_4_2_0());
            	    					
            	    pushFollow(FOLLOW_41);
            	    lv_datatypes_6_0=ruleDataType();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAttributeDefinitionRule());
            	    						}
            	    						add(
            	    							current,
            	    							"datatypes",
            	    							lv_datatypes_6_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DataType");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop45;
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
    // InternalCQLParser.g:2303:1: entryRuleCreateStream1 returns [EObject current=null] : iv_ruleCreateStream1= ruleCreateStream1 EOF ;
    public final EObject entryRuleCreateStream1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStream1 = null;


        try {
            // InternalCQLParser.g:2303:54: (iv_ruleCreateStream1= ruleCreateStream1 EOF )
            // InternalCQLParser.g:2304:2: iv_ruleCreateStream1= ruleCreateStream1 EOF
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
    // InternalCQLParser.g:2310:1: ruleCreateStream1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateStream1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2316:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2317:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2317:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2318:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2318:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2319:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2319:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2320:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_42);
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
            		
            // InternalCQLParser.g:2341:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2342:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2342:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2343:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_43);
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

            // InternalCQLParser.g:2360:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2361:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2361:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2362:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQLParser.g:2383:1: entryRuleCreateSink1 returns [EObject current=null] : iv_ruleCreateSink1= ruleCreateSink1 EOF ;
    public final EObject entryRuleCreateSink1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateSink1 = null;


        try {
            // InternalCQLParser.g:2383:52: (iv_ruleCreateSink1= ruleCreateSink1 EOF )
            // InternalCQLParser.g:2384:2: iv_ruleCreateSink1= ruleCreateSink1 EOF
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
    // InternalCQLParser.g:2390:1: ruleCreateSink1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateSink1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2396:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2397:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2397:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2398:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2398:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2399:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2399:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2400:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_44);
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
            		
            // InternalCQLParser.g:2421:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2422:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2422:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2423:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_43);
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

            // InternalCQLParser.g:2440:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2441:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2441:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2442:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQLParser.g:2463:1: entryRuleCreateStreamChannel returns [EObject current=null] : iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF ;
    public final EObject entryRuleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamChannel = null;


        try {
            // InternalCQLParser.g:2463:60: (iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF )
            // InternalCQLParser.g:2464:2: iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF
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
    // InternalCQLParser.g:2470:1: ruleCreateStreamChannel returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) ;
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
            // InternalCQLParser.g:2476:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) )
            // InternalCQLParser.g:2477:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            {
            // InternalCQLParser.g:2477:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            // InternalCQLParser.g:2478:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) )
            {
            // InternalCQLParser.g:2478:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2479:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2479:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2480:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStreamChannelAccess().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_42);
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
            		
            // InternalCQLParser.g:2501:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2502:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2502:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2503:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamChannelAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_45);
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
            		
            // InternalCQLParser.g:2524:3: ( (lv_host_4_0= RULE_ID ) )
            // InternalCQLParser.g:2525:4: (lv_host_4_0= RULE_ID )
            {
            // InternalCQLParser.g:2525:4: (lv_host_4_0= RULE_ID )
            // InternalCQLParser.g:2526:5: lv_host_4_0= RULE_ID
            {
            lv_host_4_0=(Token)match(input,RULE_ID,FOLLOW_46); 

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

            otherlv_5=(Token)match(input,Colon,FOLLOW_47); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamChannelAccess().getColonKeyword_5());
            		
            // InternalCQLParser.g:2546:3: ( (lv_port_6_0= RULE_INT ) )
            // InternalCQLParser.g:2547:4: (lv_port_6_0= RULE_INT )
            {
            // InternalCQLParser.g:2547:4: (lv_port_6_0= RULE_INT )
            // InternalCQLParser.g:2548:5: lv_port_6_0= RULE_INT
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
    // InternalCQLParser.g:2568:1: entryRuleCreateStreamFile returns [EObject current=null] : iv_ruleCreateStreamFile= ruleCreateStreamFile EOF ;
    public final EObject entryRuleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamFile = null;


        try {
            // InternalCQLParser.g:2568:57: (iv_ruleCreateStreamFile= ruleCreateStreamFile EOF )
            // InternalCQLParser.g:2569:2: iv_ruleCreateStreamFile= ruleCreateStreamFile EOF
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
    // InternalCQLParser.g:2575:1: ruleCreateStreamFile returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) ;
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
            // InternalCQLParser.g:2581:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2582:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:2582:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            // InternalCQLParser.g:2583:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2583:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2584:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2584:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2585:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStreamFileAccess().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_42);
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
            		
            // InternalCQLParser.g:2606:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2607:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2607:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2608:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamFileAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_48);
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
            		
            // InternalCQLParser.g:2629:3: ( (lv_filename_4_0= RULE_STRING ) )
            // InternalCQLParser.g:2630:4: (lv_filename_4_0= RULE_STRING )
            {
            // InternalCQLParser.g:2630:4: (lv_filename_4_0= RULE_STRING )
            // InternalCQLParser.g:2631:5: lv_filename_4_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2651:3: ( (lv_type_6_0= RULE_ID ) )
            // InternalCQLParser.g:2652:4: (lv_type_6_0= RULE_ID )
            {
            // InternalCQLParser.g:2652:4: (lv_type_6_0= RULE_ID )
            // InternalCQLParser.g:2653:5: lv_type_6_0= RULE_ID
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
    // InternalCQLParser.g:2673:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQLParser.g:2673:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQLParser.g:2674:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQLParser.g:2680:1: ruleCreateView returns [EObject current=null] : (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2686:2: ( (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) )
            // InternalCQLParser.g:2687:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            {
            // InternalCQLParser.g:2687:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            // InternalCQLParser.g:2688:3: otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) )
            {
            otherlv_0=(Token)match(input,VIEW,FOLLOW_15); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateViewAccess().getVIEWKeyword_0());
            		
            // InternalCQLParser.g:2692:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:2693:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:2693:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:2694:5: lv_name_1_0= RULE_ID
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
            		
            // InternalCQLParser.g:2714:3: ( (lv_select_3_0= ruleNestedStatement ) )
            // InternalCQLParser.g:2715:4: (lv_select_3_0= ruleNestedStatement )
            {
            // InternalCQLParser.g:2715:4: (lv_select_3_0= ruleNestedStatement )
            // InternalCQLParser.g:2716:5: lv_select_3_0= ruleNestedStatement
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
    // InternalCQLParser.g:2737:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQLParser.g:2737:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQLParser.g:2738:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQLParser.g:2744:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2750:2: ( (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:2751:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:2751:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2752:3: otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,STREAM,FOLLOW_49); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,TO,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQLParser.g:2760:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQLParser.g:2761:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2761:4: (lv_name_2_0= RULE_ID )
            // InternalCQLParser.g:2762:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_50); 

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

            // InternalCQLParser.g:2778:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==SELECT) ) {
                alt46=1;
            }
            else if ( (LA46_0==RULE_ID) ) {
                alt46=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 46, 0, input);

                throw nvae;
            }
            switch (alt46) {
                case 1 :
                    // InternalCQLParser.g:2779:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQLParser.g:2779:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQLParser.g:2780:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQLParser.g:2780:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQLParser.g:2781:6: lv_statement_3_0= ruleSelect
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
                    // InternalCQLParser.g:2799:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:2799:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQLParser.g:2800:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQLParser.g:2800:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQLParser.g:2801:6: lv_inputname_4_0= RULE_ID
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
    // InternalCQLParser.g:2822:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQLParser.g:2822:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQLParser.g:2823:2: iv_ruleDrop= ruleDrop EOF
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
    // InternalCQLParser.g:2829:1: ruleDrop returns [EObject current=null] : ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? ) ;
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
            // InternalCQLParser.g:2835:2: ( ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? ) )
            // InternalCQLParser.g:2836:2: ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? )
            {
            // InternalCQLParser.g:2836:2: ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? )
            // InternalCQLParser.g:2837:3: ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )?
            {
            // InternalCQLParser.g:2837:3: ( (lv_keyword1_0_0= DROP ) )
            // InternalCQLParser.g:2838:4: (lv_keyword1_0_0= DROP )
            {
            // InternalCQLParser.g:2838:4: (lv_keyword1_0_0= DROP )
            // InternalCQLParser.g:2839:5: lv_keyword1_0_0= DROP
            {
            lv_keyword1_0_0=(Token)match(input,DROP,FOLLOW_51); 

            					newLeafNode(lv_keyword1_0_0, grammarAccess.getDropAccess().getKeyword1DROPKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(current, "keyword1", lv_keyword1_0_0, "DROP");
            				

            }


            }

            // InternalCQLParser.g:2851:3: ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) )
            // InternalCQLParser.g:2852:4: ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) )
            {
            // InternalCQLParser.g:2852:4: ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) )
            // InternalCQLParser.g:2853:5: (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM )
            {
            // InternalCQLParser.g:2853:5: (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM )
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==SINK) ) {
                alt47=1;
            }
            else if ( (LA47_0==STREAM) ) {
                alt47=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;
            }
            switch (alt47) {
                case 1 :
                    // InternalCQLParser.g:2854:6: lv_keyword2_1_1= SINK
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
                    // InternalCQLParser.g:2865:6: lv_keyword2_1_2= STREAM
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

            // InternalCQLParser.g:2878:3: ( (lv_value1_2_0= RULE_ID ) )
            // InternalCQLParser.g:2879:4: (lv_value1_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2879:4: (lv_value1_2_0= RULE_ID )
            // InternalCQLParser.g:2880:5: lv_value1_2_0= RULE_ID
            {
            lv_value1_2_0=(Token)match(input,RULE_ID,FOLLOW_52); 

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

            // InternalCQLParser.g:2896:3: ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==IF) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // InternalCQLParser.g:2897:4: ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS
                    {
                    // InternalCQLParser.g:2897:4: ( (lv_keyword3_3_0= IF ) )
                    // InternalCQLParser.g:2898:5: (lv_keyword3_3_0= IF )
                    {
                    // InternalCQLParser.g:2898:5: (lv_keyword3_3_0= IF )
                    // InternalCQLParser.g:2899:6: lv_keyword3_3_0= IF
                    {
                    lv_keyword3_3_0=(Token)match(input,IF,FOLLOW_53); 

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
    // InternalCQLParser.g:2920:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQLParser.g:2920:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQLParser.g:2921:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQLParser.g:2927:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= UNBOUNDED ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2933:2: (kw= UNBOUNDED )
            // InternalCQLParser.g:2934:2: kw= UNBOUNDED
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
    // InternalCQLParser.g:2942:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQLParser.g:2942:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQLParser.g:2943:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQLParser.g:2949:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) ;
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
            // InternalCQLParser.g:2955:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) )
            // InternalCQLParser.g:2956:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            {
            // InternalCQLParser.g:2956:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            // InternalCQLParser.g:2957:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_47); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:2961:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:2962:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:2962:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:2963:5: lv_size_1_0= RULE_INT
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

            // InternalCQLParser.g:2979:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQLParser.g:2980:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2980:4: (lv_unit_2_0= RULE_ID )
            // InternalCQLParser.g:2981:5: lv_unit_2_0= RULE_ID
            {
            lv_unit_2_0=(Token)match(input,RULE_ID,FOLLOW_54); 

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

            // InternalCQLParser.g:2997:3: (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==ADVANCE) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalCQLParser.g:2998:4: otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,ADVANCE,FOLLOW_47); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQLParser.g:3002:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQLParser.g:3003:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3003:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQLParser.g:3004:6: lv_advance_size_4_0= RULE_INT
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

                    // InternalCQLParser.g:3020:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQLParser.g:3021:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3021:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQLParser.g:3022:6: lv_advance_unit_5_0= RULE_ID
                    {
                    lv_advance_unit_5_0=(Token)match(input,RULE_ID,FOLLOW_55); 

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
    // InternalCQLParser.g:3047:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQLParser.g:3047:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQLParser.g:3048:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQLParser.g:3054:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQLParser.g:3060:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQLParser.g:3061:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQLParser.g:3061:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQLParser.g:3062:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_47); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:3066:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:3067:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:3067:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:3068:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_56); 

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

            // InternalCQLParser.g:3084:3: (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==ADVANCE) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // InternalCQLParser.g:3085:4: otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,ADVANCE,FOLLOW_47); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQLParser.g:3089:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQLParser.g:3090:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3090:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQLParser.g:3091:6: lv_advance_size_3_0= RULE_INT
                    {
                    lv_advance_size_3_0=(Token)match(input,RULE_INT,FOLLOW_57); 

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

            otherlv_4=(Token)match(input,TUPLE,FOLLOW_58); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQLParser.g:3112:3: (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==PARTITION) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // InternalCQLParser.g:3113:4: otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,PARTITION,FOLLOW_14); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,BY,FOLLOW_15); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQLParser.g:3121:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQLParser.g:3122:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:3122:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQLParser.g:3123:6: lv_partition_attribute_7_0= ruleAttribute
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
    // InternalCQLParser.g:3145:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQLParser.g:3145:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQLParser.g:3146:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQLParser.g:3152:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3158:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQLParser.g:3159:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQLParser.g:3159:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQLParser.g:3160:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQLParser.g:3160:3: ()
            // InternalCQLParser.g:3161:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:3167:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQLParser.g:3168:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQLParser.g:3168:4: (lv_elements_1_0= ruleExpression )
            // InternalCQLParser.g:3169:5: lv_elements_1_0= ruleExpression
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
    // InternalCQLParser.g:3190:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQLParser.g:3190:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQLParser.g:3191:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQLParser.g:3197:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3203:2: (this_Or_0= ruleOr )
            // InternalCQLParser.g:3204:2: this_Or_0= ruleOr
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
    // InternalCQLParser.g:3215:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQLParser.g:3215:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQLParser.g:3216:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQLParser.g:3222:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3228:2: ( (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQLParser.g:3229:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQLParser.g:3229:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQLParser.g:3230:3: this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_59);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3238:3: ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==OR) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // InternalCQLParser.g:3239:4: () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQLParser.g:3239:4: ()
            	    // InternalCQLParser.g:3240:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,OR,FOLLOW_12); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:3250:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQLParser.g:3251:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQLParser.g:3251:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQLParser.g:3252:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_59);
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
            	    break loop52;
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
    // InternalCQLParser.g:3274:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQLParser.g:3274:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQLParser.g:3275:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQLParser.g:3281:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3287:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQLParser.g:3288:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQLParser.g:3288:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQLParser.g:3289:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_60);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3297:3: ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop53:
            do {
                int alt53=2;
                int LA53_0 = input.LA(1);

                if ( (LA53_0==AND) ) {
                    alt53=1;
                }


                switch (alt53) {
            	case 1 :
            	    // InternalCQLParser.g:3298:4: () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQLParser.g:3298:4: ()
            	    // InternalCQLParser.g:3299:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,AND,FOLLOW_12); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:3309:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQLParser.g:3310:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQLParser.g:3310:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQLParser.g:3311:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_60);
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
    // $ANTLR end "ruleAnd"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQLParser.g:3333:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQLParser.g:3333:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQLParser.g:3334:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQLParser.g:3340:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3346:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQLParser.g:3347:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQLParser.g:3347:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQLParser.g:3348:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_61);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3356:3: ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop55:
            do {
                int alt55=2;
                int LA55_0 = input.LA(1);

                if ( (LA55_0==ExclamationMarkEqualsSign||LA55_0==EqualsSign) ) {
                    alt55=1;
                }


                switch (alt55) {
            	case 1 :
            	    // InternalCQLParser.g:3357:4: () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQLParser.g:3357:4: ()
            	    // InternalCQLParser.g:3358:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3364:4: ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) )
            	    // InternalCQLParser.g:3365:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    {
            	    // InternalCQLParser.g:3365:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    // InternalCQLParser.g:3366:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    {
            	    // InternalCQLParser.g:3366:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    int alt54=2;
            	    int LA54_0 = input.LA(1);

            	    if ( (LA54_0==EqualsSign) ) {
            	        alt54=1;
            	    }
            	    else if ( (LA54_0==ExclamationMarkEqualsSign) ) {
            	        alt54=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 54, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt54) {
            	        case 1 :
            	            // InternalCQLParser.g:3367:7: lv_op_2_1= EqualsSign
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
            	            // InternalCQLParser.g:3378:7: lv_op_2_2= ExclamationMarkEqualsSign
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

            	    // InternalCQLParser.g:3391:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQLParser.g:3392:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQLParser.g:3392:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQLParser.g:3393:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_61);
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
    // $ANTLR end "ruleEqualitiy"


    // $ANTLR start "entryRuleComparison"
    // InternalCQLParser.g:3415:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQLParser.g:3415:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQLParser.g:3416:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQLParser.g:3422:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQLParser.g:3428:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQLParser.g:3429:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQLParser.g:3429:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQLParser.g:3430:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_62);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3438:3: ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( ((LA57_0>=LessThanSignEqualsSign && LA57_0<=GreaterThanSignEqualsSign)||LA57_0==LessThanSign||LA57_0==GreaterThanSign) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // InternalCQLParser.g:3439:4: () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQLParser.g:3439:4: ()
            	    // InternalCQLParser.g:3440:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3446:4: ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) )
            	    // InternalCQLParser.g:3447:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    {
            	    // InternalCQLParser.g:3447:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    // InternalCQLParser.g:3448:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    {
            	    // InternalCQLParser.g:3448:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    int alt56=4;
            	    switch ( input.LA(1) ) {
            	    case GreaterThanSignEqualsSign:
            	        {
            	        alt56=1;
            	        }
            	        break;
            	    case LessThanSignEqualsSign:
            	        {
            	        alt56=2;
            	        }
            	        break;
            	    case LessThanSign:
            	        {
            	        alt56=3;
            	        }
            	        break;
            	    case GreaterThanSign:
            	        {
            	        alt56=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 56, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt56) {
            	        case 1 :
            	            // InternalCQLParser.g:3449:7: lv_op_2_1= GreaterThanSignEqualsSign
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
            	            // InternalCQLParser.g:3460:7: lv_op_2_2= LessThanSignEqualsSign
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
            	            // InternalCQLParser.g:3471:7: lv_op_2_3= LessThanSign
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
            	            // InternalCQLParser.g:3482:7: lv_op_2_4= GreaterThanSign
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

            	    // InternalCQLParser.g:3495:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQLParser.g:3496:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQLParser.g:3496:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQLParser.g:3497:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_62);
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
    // $ANTLR end "ruleComparison"


    // $ANTLR start "entryRulePlusOrMinus"
    // InternalCQLParser.g:3519:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQLParser.g:3519:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQLParser.g:3520:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQLParser.g:3526:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3532:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQLParser.g:3533:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQLParser.g:3533:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQLParser.g:3534:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_63);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3542:3: ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==PlusSign||LA59_0==HyphenMinus) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // InternalCQLParser.g:3543:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQLParser.g:3543:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) )
            	    int alt58=2;
            	    int LA58_0 = input.LA(1);

            	    if ( (LA58_0==PlusSign) ) {
            	        alt58=1;
            	    }
            	    else if ( (LA58_0==HyphenMinus) ) {
            	        alt58=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 58, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt58) {
            	        case 1 :
            	            // InternalCQLParser.g:3544:5: ( () otherlv_2= PlusSign )
            	            {
            	            // InternalCQLParser.g:3544:5: ( () otherlv_2= PlusSign )
            	            // InternalCQLParser.g:3545:6: () otherlv_2= PlusSign
            	            {
            	            // InternalCQLParser.g:3545:6: ()
            	            // InternalCQLParser.g:3546:7: 
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
            	            // InternalCQLParser.g:3558:5: ( () otherlv_4= HyphenMinus )
            	            {
            	            // InternalCQLParser.g:3558:5: ( () otherlv_4= HyphenMinus )
            	            // InternalCQLParser.g:3559:6: () otherlv_4= HyphenMinus
            	            {
            	            // InternalCQLParser.g:3559:6: ()
            	            // InternalCQLParser.g:3560:7: 
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

            	    // InternalCQLParser.g:3572:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQLParser.g:3573:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQLParser.g:3573:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQLParser.g:3574:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_63);
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
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCQLParser.g:3596:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQLParser.g:3596:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQLParser.g:3597:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQLParser.g:3603:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3609:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQLParser.g:3610:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQLParser.g:3610:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQLParser.g:3611:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_64);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3619:3: ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( (LA61_0==Asterisk||LA61_0==Solidus) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // InternalCQLParser.g:3620:4: () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQLParser.g:3620:4: ()
            	    // InternalCQLParser.g:3621:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3627:4: ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) )
            	    // InternalCQLParser.g:3628:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    {
            	    // InternalCQLParser.g:3628:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    // InternalCQLParser.g:3629:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    {
            	    // InternalCQLParser.g:3629:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    int alt60=2;
            	    int LA60_0 = input.LA(1);

            	    if ( (LA60_0==Asterisk) ) {
            	        alt60=1;
            	    }
            	    else if ( (LA60_0==Solidus) ) {
            	        alt60=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 60, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt60) {
            	        case 1 :
            	            // InternalCQLParser.g:3630:7: lv_op_2_1= Asterisk
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
            	            // InternalCQLParser.g:3641:7: lv_op_2_2= Solidus
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

            	    // InternalCQLParser.g:3654:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQLParser.g:3655:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQLParser.g:3655:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQLParser.g:3656:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_64);
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
            	    break loop61;
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
    // InternalCQLParser.g:3678:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQLParser.g:3678:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQLParser.g:3679:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQLParser.g:3685:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQLParser.g:3691:2: ( ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQLParser.g:3692:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQLParser.g:3692:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt62=3;
            switch ( input.LA(1) ) {
            case LeftParenthesis:
                {
                alt62=1;
                }
                break;
            case NOT:
                {
                alt62=2;
                }
                break;
            case FALSE:
            case TRUE:
            case RULE_ID:
            case RULE_INT:
            case RULE_FLOAT:
            case RULE_STRING:
                {
                alt62=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 62, 0, input);

                throw nvae;
            }

            switch (alt62) {
                case 1 :
                    // InternalCQLParser.g:3693:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    {
                    // InternalCQLParser.g:3693:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    // InternalCQLParser.g:3694:4: () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis
                    {
                    // InternalCQLParser.g:3694:4: ()
                    // InternalCQLParser.g:3695:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_12); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQLParser.g:3705:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQLParser.g:3706:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQLParser.g:3706:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQLParser.g:3707:6: lv_inner_2_0= ruleExpression
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
                    // InternalCQLParser.g:3730:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQLParser.g:3730:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQLParser.g:3731:4: () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQLParser.g:3731:4: ()
                    // InternalCQLParser.g:3732:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,NOT,FOLLOW_12); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQLParser.g:3742:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQLParser.g:3743:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQLParser.g:3743:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQLParser.g:3744:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQLParser.g:3763:3: this_Atomic_7= ruleAtomic
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
    // InternalCQLParser.g:3775:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQLParser.g:3775:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQLParser.g:3776:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQLParser.g:3782:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
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
            // InternalCQLParser.g:3788:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQLParser.g:3789:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQLParser.g:3789:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt65=5;
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
            case RULE_ID:
                {
                alt65=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 65, 0, input);

                throw nvae;
            }

            switch (alt65) {
                case 1 :
                    // InternalCQLParser.g:3790:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:3790:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:3791:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:3791:4: ()
                    // InternalCQLParser.g:3792:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3798:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:3799:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3799:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:3800:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:3818:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:3818:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:3819:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:3819:4: ()
                    // InternalCQLParser.g:3820:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3826:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:3827:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:3827:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:3828:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:3846:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:3846:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:3847:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:3847:4: ()
                    // InternalCQLParser.g:3848:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3854:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:3855:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:3855:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:3856:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:3874:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:3874:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:3875:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:3875:4: ()
                    // InternalCQLParser.g:3876:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3882:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:3883:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:3883:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:3884:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:3884:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    int alt63=2;
                    int LA63_0 = input.LA(1);

                    if ( (LA63_0==TRUE) ) {
                        alt63=1;
                    }
                    else if ( (LA63_0==FALSE) ) {
                        alt63=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 63, 0, input);

                        throw nvae;
                    }
                    switch (alt63) {
                        case 1 :
                            // InternalCQLParser.g:3885:7: lv_value_7_1= TRUE
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
                            // InternalCQLParser.g:3896:7: lv_value_7_2= FALSE
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
                    // InternalCQLParser.g:3911:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQLParser.g:3911:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQLParser.g:3912:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQLParser.g:3912:4: ()
                    // InternalCQLParser.g:3913:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3919:4: ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt64=2;
                    int LA64_0 = input.LA(1);

                    if ( (LA64_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
                        case FullStop:
                            {
                            int LA64_2 = input.LA(3);

                            if ( (LA64_2==RULE_ID) ) {
                                int LA64_5 = input.LA(4);

                                if ( (LA64_5==IN) ) {
                                    alt64=2;
                                }
                                else if ( (LA64_5==EOF||(LA64_5>=ATTACH && LA64_5<=CREATE)||(LA64_5>=HAVING && LA64_5<=STREAM)||LA64_5==GROUP||LA64_5==DROP||(LA64_5>=VIEW && LA64_5<=AND)||(LA64_5>=ExclamationMarkEqualsSign && LA64_5<=GreaterThanSignEqualsSign)||LA64_5==OR||(LA64_5>=RightParenthesis && LA64_5<=PlusSign)||LA64_5==HyphenMinus||LA64_5==Solidus||(LA64_5>=Semicolon && LA64_5<=GreaterThanSign)) ) {
                                    alt64=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 64, 5, input);

                                    throw nvae;
                                }
                            }
                            else if ( (LA64_2==Asterisk) ) {
                                int LA64_6 = input.LA(4);

                                if ( (LA64_6==EOF||(LA64_6>=ATTACH && LA64_6<=CREATE)||(LA64_6>=HAVING && LA64_6<=STREAM)||LA64_6==GROUP||LA64_6==DROP||(LA64_6>=VIEW && LA64_6<=AND)||(LA64_6>=ExclamationMarkEqualsSign && LA64_6<=GreaterThanSignEqualsSign)||LA64_6==OR||(LA64_6>=RightParenthesis && LA64_6<=PlusSign)||LA64_6==HyphenMinus||LA64_6==Solidus||(LA64_6>=Semicolon && LA64_6<=GreaterThanSign)) ) {
                                    alt64=1;
                                }
                                else if ( (LA64_6==IN) ) {
                                    alt64=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 64, 6, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 64, 2, input);

                                throw nvae;
                            }
                            }
                            break;
                        case IN:
                            {
                            alt64=2;
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
                            alt64=1;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 64, 1, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 64, 0, input);

                        throw nvae;
                    }
                    switch (alt64) {
                        case 1 :
                            // InternalCQLParser.g:3920:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            {
                            // InternalCQLParser.g:3920:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            // InternalCQLParser.g:3921:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            {
                            // InternalCQLParser.g:3921:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            // InternalCQLParser.g:3922:7: lv_value_9_0= ruleAttributeWithoutAliasDefinition
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
                            // InternalCQLParser.g:3940:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQLParser.g:3940:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQLParser.g:3941:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQLParser.g:3941:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQLParser.g:3942:7: lv_value_10_0= ruleAttributeWithNestedStatement
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
    // InternalCQLParser.g:3965:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQLParser.g:3965:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQLParser.g:3966:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
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
    // InternalCQLParser.g:3972:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) ;
    public final EObject ruleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3978:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) )
            // InternalCQLParser.g:3979:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            {
            // InternalCQLParser.g:3979:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            int alt67=4;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt67=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt67=2;
                }
                break;
            case RULE_STRING:
                {
                alt67=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt67=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 67, 0, input);

                throw nvae;
            }

            switch (alt67) {
                case 1 :
                    // InternalCQLParser.g:3980:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:3980:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:3981:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:3981:4: ()
                    // InternalCQLParser.g:3982:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:3988:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:3989:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3989:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:3990:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:4008:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:4008:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:4009:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:4009:4: ()
                    // InternalCQLParser.g:4010:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4016:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:4017:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:4017:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:4018:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:4036:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:4036:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:4037:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:4037:4: ()
                    // InternalCQLParser.g:4038:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4044:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:4045:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4045:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:4046:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:4064:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:4064:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:4065:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:4065:4: ()
                    // InternalCQLParser.g:4066:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4072:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:4073:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:4073:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:4074:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:4074:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    int alt66=2;
                    int LA66_0 = input.LA(1);

                    if ( (LA66_0==TRUE) ) {
                        alt66=1;
                    }
                    else if ( (LA66_0==FALSE) ) {
                        alt66=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 66, 0, input);

                        throw nvae;
                    }
                    switch (alt66) {
                        case 1 :
                            // InternalCQLParser.g:4075:7: lv_value_7_1= TRUE
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
                            // InternalCQLParser.g:4086:7: lv_value_7_2= FALSE
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


    // $ANTLR start "entryRuleDataType"
    // InternalCQLParser.g:4104:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQLParser.g:4104:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQLParser.g:4105:2: iv_ruleDataType= ruleDataType EOF
            {
             newCompositeNode(grammarAccess.getDataTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDataType=ruleDataType();

            state._fsp--;

             current =iv_ruleDataType; 
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
    // $ANTLR end "entryRuleDataType"


    // $ANTLR start "ruleDataType"
    // InternalCQLParser.g:4111:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) ) ;
    public final EObject ruleDataType() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_1=null;
        Token lv_value_0_2=null;
        Token lv_value_0_3=null;
        Token lv_value_0_4=null;
        Token lv_value_0_5=null;
        Token lv_value_0_6=null;
        Token lv_value_0_7=null;
        Token lv_value_0_8=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4117:2: ( ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) ) )
            // InternalCQLParser.g:4118:2: ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) )
            {
            // InternalCQLParser.g:4118:2: ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) )
            // InternalCQLParser.g:4119:3: ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) )
            {
            // InternalCQLParser.g:4119:3: ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) )
            // InternalCQLParser.g:4120:4: (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP )
            {
            // InternalCQLParser.g:4120:4: (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP )
            int alt68=8;
            switch ( input.LA(1) ) {
            case INTEGER:
                {
                alt68=1;
                }
                break;
            case DOUBLE:
                {
                alt68=2;
                }
                break;
            case LONG:
                {
                alt68=3;
                }
                break;
            case FLOAT:
                {
                alt68=4;
                }
                break;
            case STRING:
                {
                alt68=5;
                }
                break;
            case BOOLEAN:
                {
                alt68=6;
                }
                break;
            case STARTTIMESTAMP:
                {
                alt68=7;
                }
                break;
            case ENDTIMESTAMP:
                {
                alt68=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                throw nvae;
            }

            switch (alt68) {
                case 1 :
                    // InternalCQLParser.g:4121:5: lv_value_0_1= INTEGER
                    {
                    lv_value_0_1=(Token)match(input,INTEGER,FOLLOW_2); 

                    					newLeafNode(lv_value_0_1, grammarAccess.getDataTypeAccess().getValueINTEGERKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_1, null);
                    				

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4132:5: lv_value_0_2= DOUBLE
                    {
                    lv_value_0_2=(Token)match(input,DOUBLE,FOLLOW_2); 

                    					newLeafNode(lv_value_0_2, grammarAccess.getDataTypeAccess().getValueDOUBLEKeyword_0_1());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_2, null);
                    				

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:4143:5: lv_value_0_3= LONG
                    {
                    lv_value_0_3=(Token)match(input,LONG,FOLLOW_2); 

                    					newLeafNode(lv_value_0_3, grammarAccess.getDataTypeAccess().getValueLONGKeyword_0_2());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_3, null);
                    				

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:4154:5: lv_value_0_4= FLOAT
                    {
                    lv_value_0_4=(Token)match(input,FLOAT,FOLLOW_2); 

                    					newLeafNode(lv_value_0_4, grammarAccess.getDataTypeAccess().getValueFLOATKeyword_0_3());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_4, null);
                    				

                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:4165:5: lv_value_0_5= STRING
                    {
                    lv_value_0_5=(Token)match(input,STRING,FOLLOW_2); 

                    					newLeafNode(lv_value_0_5, grammarAccess.getDataTypeAccess().getValueSTRINGKeyword_0_4());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_5, null);
                    				

                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:4176:5: lv_value_0_6= BOOLEAN
                    {
                    lv_value_0_6=(Token)match(input,BOOLEAN,FOLLOW_2); 

                    					newLeafNode(lv_value_0_6, grammarAccess.getDataTypeAccess().getValueBOOLEANKeyword_0_5());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_6, null);
                    				

                    }
                    break;
                case 7 :
                    // InternalCQLParser.g:4187:5: lv_value_0_7= STARTTIMESTAMP
                    {
                    lv_value_0_7=(Token)match(input,STARTTIMESTAMP,FOLLOW_2); 

                    					newLeafNode(lv_value_0_7, grammarAccess.getDataTypeAccess().getValueSTARTTIMESTAMPKeyword_0_6());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_7, null);
                    				

                    }
                    break;
                case 8 :
                    // InternalCQLParser.g:4198:5: lv_value_0_8= ENDTIMESTAMP
                    {
                    lv_value_0_8=(Token)match(input,ENDTIMESTAMP,FOLLOW_2); 

                    					newLeafNode(lv_value_0_8, grammarAccess.getDataTypeAccess().getValueENDTIMESTAMPKeyword_0_7());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_8, null);
                    				

                    }
                    break;

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
    // $ANTLR end "ruleDataType"


    // $ANTLR start "ruleCreateKeyword"
    // InternalCQLParser.g:4214:1: ruleCreateKeyword returns [Enumerator current=null] : ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) ;
    public final Enumerator ruleCreateKeyword() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4220:2: ( ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) )
            // InternalCQLParser.g:4221:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            {
            // InternalCQLParser.g:4221:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==CREATE) ) {
                alt69=1;
            }
            else if ( (LA69_0==ATTACH) ) {
                alt69=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 69, 0, input);

                throw nvae;
            }
            switch (alt69) {
                case 1 :
                    // InternalCQLParser.g:4222:3: (enumLiteral_0= CREATE )
                    {
                    // InternalCQLParser.g:4222:3: (enumLiteral_0= CREATE )
                    // InternalCQLParser.g:4223:4: enumLiteral_0= CREATE
                    {
                    enumLiteral_0=(Token)match(input,CREATE,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4230:3: (enumLiteral_1= ATTACH )
                    {
                    // InternalCQLParser.g:4230:3: (enumLiteral_1= ATTACH )
                    // InternalCQLParser.g:4231:4: enumLiteral_1= ATTACH
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
    static final String dfa_1s = "\54\uffff";
    static final String dfa_2s = "\1\22\3\uffff\2\30\1\uffff\1\102\1\uffff\1\63\1\102\1\4\1\65\1\102\13\4\1\102\1\16\1\4\3\uffff\1\102\10\64\1\65\3\4";
    static final String dfa_3s = "\1\47\3\uffff\2\43\1\uffff\1\102\1\uffff\1\63\4\102\10\67\4\102\1\40\1\71\3\uffff\1\102\10\67\1\102\1\42\2\55";
    static final String dfa_4s = "\1\uffff\1\1\1\2\1\3\2\uffff\1\10\1\uffff\1\5\23\uffff\1\4\1\6\1\7\15\uffff";
    static final String dfa_5s = "\54\uffff}>";
    static final String[] dfa_6s = {
            "\1\5\1\4\3\uffff\1\1\1\2\6\uffff\1\3\7\uffff\1\6",
            "",
            "",
            "",
            "\1\7\12\uffff\1\10",
            "\1\7\12\uffff\1\10",
            "",
            "\1\11",
            "",
            "\1\12",
            "\1\13",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\12\uffff\1\15\13\uffff\1\14\10\uffff\1\13",
            "\1\27\14\uffff\1\26",
            "\1\30",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\21\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\21\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\21\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\21\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\21\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\21\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\21\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\21\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\12\uffff\1\15\24\uffff\1\13",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\12\uffff\1\15\24\uffff\1\13",
            "\1\24\1\25\7\uffff\1\23\1\uffff\1\16\4\uffff\1\17\4\uffff\1\22\1\uffff\1\21\6\uffff\1\20\37\uffff\1\13",
            "\1\33",
            "\1\35\2\uffff\1\34\16\uffff\1\36",
            "\1\46\1\47\7\uffff\1\45\1\uffff\1\40\4\uffff\1\41\4\uffff\1\44\1\uffff\1\43\6\uffff\1\42\12\uffff\1\37\13\uffff\1\50",
            "",
            "",
            "",
            "\1\51",
            "\1\32\2\uffff\1\31",
            "\1\32\2\uffff\1\31",
            "\1\32\2\uffff\1\31",
            "\1\32\2\uffff\1\31",
            "\1\32\2\uffff\1\31",
            "\1\32\2\uffff\1\31",
            "\1\32\2\uffff\1\31",
            "\1\32\2\uffff\1\31",
            "\1\52\14\uffff\1\53",
            "\1\46\1\47\7\uffff\1\45\1\uffff\1\40\4\uffff\1\41\4\uffff\1\44\1\uffff\1\43\6\uffff\1\42",
            "\1\46\1\47\7\uffff\1\45\1\uffff\1\40\4\uffff\1\41\4\uffff\1\44\1\uffff\1\43\6\uffff\1\42\12\uffff\1\37",
            "\1\46\1\47\7\uffff\1\45\1\uffff\1\40\4\uffff\1\41\4\uffff\1\44\1\uffff\1\43\6\uffff\1\42\12\uffff\1\37"
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
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x00000080818C0002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x1000000000000002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0020004004000400L,0x000000000000003CL});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x00A0004204000400L,0x000000000000003CL});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0080000200000000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0008000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0088000050400002L,0x0000000000000004L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0080000050400002L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0028024004000400L,0x000000000000003CL});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000010400002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0080000000400002L,0x0000000000000004L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0080000000400002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000200000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000001000000200L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000200000000002L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0560200000000002L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0560000000000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0560000000000002L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0090000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x000000040A10A030L,0x0000000000000004L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x009000040A10A030L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x000000040A10A030L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0090000000000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000000800000L,0x0000000000000004L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000000801000000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000002000001000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000000020001000L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000010000000002L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x4000040000000002L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0xA000180000000002L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0140000000000002L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0420000000000002L});

}