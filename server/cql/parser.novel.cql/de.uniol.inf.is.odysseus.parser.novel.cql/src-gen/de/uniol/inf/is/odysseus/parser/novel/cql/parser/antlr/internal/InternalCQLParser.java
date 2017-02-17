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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "STARTTIMESTAMP", "ENDTIMESTAMP", "DATAHANDLER", "IFEXISTS", "PARTITION", "TRANSPORT", "UNBOUNDED", "DISTINCT", "DolToEur", "PROTOCOL", "ADVANCE", "BOOLEAN", "CHANNEL", "INTEGER", "OPTIONS", "WRAPPER", "ATTACH", "CREATE", "DOUBLE", "HAVING", "MEDIAN", "SELECT", "STREAM", "STRING", "COUNT", "FALSE", "FIRST", "FLOAT", "GROUP", "TUPLE", "WHERE", "DROP", "FILE", "FROM", "LAST", "LONG", "SINK", "SIZE", "TIME", "TRUE", "VIEW", "AND", "AVG", "MAX", "MIN", "NOT", "SUM", "ExclamationMarkEqualsSign", "LessThanSignEqualsSign", "GreaterThanSignEqualsSign", "AS", "BY", "IN", "OR", "TO", "LeftParenthesis", "RightParenthesis", "Asterisk", "PlusSign", "Comma", "HyphenMinus", "FullStop", "Solidus", "Colon", "Semicolon", "LessThanSign", "EqualsSign", "GreaterThanSign", "LeftSquareBracket", "RightSquareBracket", "RULE_ID", "RULE_INT", "RULE_FLOAT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER"
    };
    public static final int CREATE=21;
    public static final int FROM=37;
    public static final int VIEW=44;
    public static final int LessThanSign=69;
    public static final int LeftParenthesis=59;
    public static final int UNBOUNDED=10;
    public static final int WRAPPER=19;
    public static final int PARTITION=8;
    public static final int RightSquareBracket=73;
    public static final int INTEGER=17;
    public static final int GreaterThanSign=71;
    public static final int RULE_ID=74;
    public static final int IN=56;
    public static final int DISTINCT=11;
    public static final int SIZE=41;
    public static final int PROTOCOL=13;
    public static final int RightParenthesis=60;
    public static final int TRUE=43;
    public static final int SUM=50;
    public static final int OPTIONS=18;
    public static final int WHERE=34;
    public static final int BOOLEAN=15;
    public static final int GreaterThanSignEqualsSign=53;
    public static final int AVG=46;
    public static final int NOT=49;
    public static final int AS=54;
    public static final int MIN=48;
    public static final int CHANNEL=16;
    public static final int LAST=38;
    public static final int SINK=40;
    public static final int PlusSign=62;
    public static final int RULE_INT=75;
    public static final int AND=45;
    public static final int RULE_ML_COMMENT=78;
    public static final int COUNT=28;
    public static final int LeftSquareBracket=72;
    public static final int ADVANCE=14;
    public static final int HAVING=23;
    public static final int FLOAT=31;
    public static final int MAX=47;
    public static final int STARTTIMESTAMP=4;
    public static final int RULE_STRING=77;
    public static final int DROP=35;
    public static final int RULE_SL_COMMENT=79;
    public static final int GROUP=32;
    public static final int Comma=63;
    public static final int EqualsSign=70;
    public static final int TRANSPORT=9;
    public static final int HyphenMinus=64;
    public static final int BY=55;
    public static final int IFEXISTS=7;
    public static final int DOUBLE=22;
    public static final int MEDIAN=24;
    public static final int LessThanSignEqualsSign=52;
    public static final int Solidus=66;
    public static final int Colon=67;
    public static final int DATAHANDLER=6;
    public static final int FILE=36;
    public static final int EOF=-1;
    public static final int Asterisk=61;
    public static final int LONG=39;
    public static final int FullStop=65;
    public static final int OR=57;
    public static final int DolToEur=12;
    public static final int RULE_WS=80;
    public static final int ENDTIMESTAMP=5;
    public static final int STREAM=26;
    public static final int TIME=42;
    public static final int RULE_ANY_OTHER=81;
    public static final int FIRST=30;
    public static final int SELECT=25;
    public static final int TUPLE=33;
    public static final int Semicolon=68;
    public static final int ATTACH=20;
    public static final int STRING=27;
    public static final int RULE_FLOAT=76;
    public static final int FALSE=29;
    public static final int ExclamationMarkEqualsSign=51;
    public static final int TO=58;

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
    // InternalCQLParser.g:287:1: ruleSelect returns [EObject current=null] : ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= FROM ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= Comma ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= WHERE ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= GROUP otherlv_19= BY ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= Comma ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= HAVING ( (lv_having_24_0= ruleExpressionsModel ) ) )? ) ;
    public final EObject ruleSelect() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token lv_distinct_1_0=null;
        Token otherlv_2=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_12=null;
        Token otherlv_14=null;
        Token otherlv_16=null;
        Token otherlv_18=null;
        Token otherlv_19=null;
        Token otherlv_21=null;
        Token otherlv_23=null;
        EObject lv_attributes_3_0 = null;

        EObject lv_aggregations_4_0 = null;

        EObject lv_expressions_5_0 = null;

        EObject lv_attributes_7_0 = null;

        EObject lv_aggregations_9_0 = null;

        EObject lv_expressions_11_0 = null;

        EObject lv_sources_13_0 = null;

        EObject lv_sources_15_0 = null;

        EObject lv_predicates_17_0 = null;

        EObject lv_order_20_0 = null;

        EObject lv_order_22_0 = null;

        EObject lv_having_24_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:293:2: ( ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= FROM ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= Comma ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= WHERE ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= GROUP otherlv_19= BY ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= Comma ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= HAVING ( (lv_having_24_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQLParser.g:294:2: ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= FROM ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= Comma ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= WHERE ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= GROUP otherlv_19= BY ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= Comma ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= HAVING ( (lv_having_24_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQLParser.g:294:2: ( ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= FROM ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= Comma ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= WHERE ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= GROUP otherlv_19= BY ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= Comma ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= HAVING ( (lv_having_24_0= ruleExpressionsModel ) ) )? )
            // InternalCQLParser.g:295:3: ( (lv_name_0_0= SELECT ) ) ( (lv_distinct_1_0= DISTINCT ) )? (otherlv_2= Asterisk | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= FROM ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= Comma ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= WHERE ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= GROUP otherlv_19= BY ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= Comma ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= HAVING ( (lv_having_24_0= ruleExpressionsModel ) ) )?
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

            // InternalCQLParser.g:323:3: (otherlv_2= Asterisk | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==Asterisk) ) {
                alt7=1;
            }
            else if ( (LA7_0==DolToEur||LA7_0==MEDIAN||(LA7_0>=COUNT && LA7_0<=FIRST)||LA7_0==LAST||LA7_0==TRUE||(LA7_0>=AVG && LA7_0<=MIN)||LA7_0==SUM||(LA7_0>=RULE_ID && LA7_0<=RULE_STRING)) ) {
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
                    // InternalCQLParser.g:329:4: ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* )
                    {
                    // InternalCQLParser.g:329:4: ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* )
                    // InternalCQLParser.g:330:5: ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )*
                    {
                    // InternalCQLParser.g:330:5: ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=4;
                        switch ( input.LA(1) ) {
                        case RULE_ID:
                            {
                            switch ( input.LA(2) ) {
                            case Asterisk:
                            case PlusSign:
                            case HyphenMinus:
                            case Solidus:
                                {
                                alt5=3;
                                }
                                break;
                            case FullStop:
                                {
                                int LA5_5 = input.LA(3);

                                if ( (LA5_5==RULE_ID) ) {
                                    int LA5_7 = input.LA(4);

                                    if ( ((LA5_7>=Asterisk && LA5_7<=PlusSign)||LA5_7==HyphenMinus||LA5_7==Solidus) ) {
                                        alt5=3;
                                    }
                                    else if ( (LA5_7==DolToEur||LA5_7==MEDIAN||(LA5_7>=COUNT && LA5_7<=FIRST)||(LA5_7>=FROM && LA5_7<=LAST)||LA5_7==TRUE||(LA5_7>=AVG && LA5_7<=MIN)||LA5_7==SUM||LA5_7==AS||LA5_7==Comma||(LA5_7>=RULE_ID && LA5_7<=RULE_STRING)) ) {
                                        alt5=1;
                                    }


                                }


                                }
                                break;
                            case DolToEur:
                            case MEDIAN:
                            case COUNT:
                            case FALSE:
                            case FIRST:
                            case FROM:
                            case LAST:
                            case TRUE:
                            case AVG:
                            case MAX:
                            case MIN:
                            case SUM:
                            case AS:
                            case Comma:
                            case RULE_ID:
                            case RULE_INT:
                            case RULE_FLOAT:
                            case RULE_STRING:
                                {
                                alt5=1;
                                }
                                break;

                            }

                            }
                            break;
                        case MEDIAN:
                        case COUNT:
                        case FIRST:
                        case LAST:
                        case AVG:
                        case MAX:
                        case MIN:
                        case SUM:
                            {
                            alt5=2;
                            }
                            break;
                        case DolToEur:
                        case FALSE:
                        case TRUE:
                        case RULE_INT:
                        case RULE_FLOAT:
                        case RULE_STRING:
                            {
                            alt5=3;
                            }
                            break;

                        }

                        switch (alt5) {
                    	case 1 :
                    	    // InternalCQLParser.g:331:6: ( (lv_attributes_3_0= ruleAttribute ) )
                    	    {
                    	    // InternalCQLParser.g:331:6: ( (lv_attributes_3_0= ruleAttribute ) )
                    	    // InternalCQLParser.g:332:7: (lv_attributes_3_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:332:7: (lv_attributes_3_0= ruleAttribute )
                    	    // InternalCQLParser.g:333:8: lv_attributes_3_0= ruleAttribute
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectAccess().getAttributesAttributeParserRuleCall_2_1_0_0_0());
                    	    							
                    	    pushFollow(FOLLOW_7);
                    	    lv_attributes_3_0=ruleAttribute();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"attributes",
                    	    									lv_attributes_3_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQLParser.g:351:6: ( (lv_aggregations_4_0= ruleAggregation ) )
                    	    {
                    	    // InternalCQLParser.g:351:6: ( (lv_aggregations_4_0= ruleAggregation ) )
                    	    // InternalCQLParser.g:352:7: (lv_aggregations_4_0= ruleAggregation )
                    	    {
                    	    // InternalCQLParser.g:352:7: (lv_aggregations_4_0= ruleAggregation )
                    	    // InternalCQLParser.g:353:8: lv_aggregations_4_0= ruleAggregation
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectAccess().getAggregationsAggregationParserRuleCall_2_1_0_1_0());
                    	    							
                    	    pushFollow(FOLLOW_7);
                    	    lv_aggregations_4_0=ruleAggregation();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"aggregations",
                    	    									lv_aggregations_4_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Aggregation");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 3 :
                    	    // InternalCQLParser.g:371:6: ( (lv_expressions_5_0= ruleSelectExpression ) )
                    	    {
                    	    // InternalCQLParser.g:371:6: ( (lv_expressions_5_0= ruleSelectExpression ) )
                    	    // InternalCQLParser.g:372:7: (lv_expressions_5_0= ruleSelectExpression )
                    	    {
                    	    // InternalCQLParser.g:372:7: (lv_expressions_5_0= ruleSelectExpression )
                    	    // InternalCQLParser.g:373:8: lv_expressions_5_0= ruleSelectExpression
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectAccess().getExpressionsSelectExpressionParserRuleCall_2_1_0_2_0());
                    	    							
                    	    pushFollow(FOLLOW_7);
                    	    lv_expressions_5_0=ruleSelectExpression();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"expressions",
                    	    									lv_expressions_5_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpression");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


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

                    // InternalCQLParser.g:391:5: ( (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )*
                    loop6:
                    do {
                        int alt6=4;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==Comma) ) {
                            switch ( input.LA(2) ) {
                            case DolToEur:
                            case FALSE:
                            case TRUE:
                            case RULE_INT:
                            case RULE_FLOAT:
                            case RULE_STRING:
                                {
                                alt6=3;
                                }
                                break;
                            case RULE_ID:
                                {
                                switch ( input.LA(3) ) {
                                case Asterisk:
                                case PlusSign:
                                case HyphenMinus:
                                case Solidus:
                                    {
                                    alt6=3;
                                    }
                                    break;
                                case FullStop:
                                    {
                                    int LA6_6 = input.LA(4);

                                    if ( (LA6_6==RULE_ID) ) {
                                        int LA6_8 = input.LA(5);

                                        if ( (LA6_8==FROM||LA6_8==AS||LA6_8==Comma) ) {
                                            alt6=1;
                                        }
                                        else if ( ((LA6_8>=Asterisk && LA6_8<=PlusSign)||LA6_8==HyphenMinus||LA6_8==Solidus) ) {
                                            alt6=3;
                                        }


                                    }


                                    }
                                    break;
                                case FROM:
                                case AS:
                                case Comma:
                                    {
                                    alt6=1;
                                    }
                                    break;

                                }

                                }
                                break;
                            case MEDIAN:
                            case COUNT:
                            case FIRST:
                            case LAST:
                            case AVG:
                            case MAX:
                            case MIN:
                            case SUM:
                                {
                                alt6=2;
                                }
                                break;

                            }

                        }


                        switch (alt6) {
                    	case 1 :
                    	    // InternalCQLParser.g:392:6: (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    {
                    	    // InternalCQLParser.g:392:6: (otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    // InternalCQLParser.g:393:7: otherlv_6= Comma ( (lv_attributes_7_0= ruleAttribute ) )
                    	    {
                    	    otherlv_6=(Token)match(input,Comma,FOLLOW_8); 

                    	    							newLeafNode(otherlv_6, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_0_0());
                    	    						
                    	    // InternalCQLParser.g:397:7: ( (lv_attributes_7_0= ruleAttribute ) )
                    	    // InternalCQLParser.g:398:8: (lv_attributes_7_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:398:8: (lv_attributes_7_0= ruleAttribute )
                    	    // InternalCQLParser.g:399:9: lv_attributes_7_0= ruleAttribute
                    	    {

                    	    									newCompositeNode(grammarAccess.getSelectAccess().getAttributesAttributeParserRuleCall_2_1_1_0_1_0());
                    	    								
                    	    pushFollow(FOLLOW_9);
                    	    lv_attributes_7_0=ruleAttribute();

                    	    state._fsp--;


                    	    									if (current==null) {
                    	    										current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    									}
                    	    									add(
                    	    										current,
                    	    										"attributes",
                    	    										lv_attributes_7_0,
                    	    										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    									afterParserOrEnumRuleCall();
                    	    								

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQLParser.g:418:6: (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    {
                    	    // InternalCQLParser.g:418:6: (otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    // InternalCQLParser.g:419:7: otherlv_8= Comma ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    {
                    	    otherlv_8=(Token)match(input,Comma,FOLLOW_10); 

                    	    							newLeafNode(otherlv_8, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_1_0());
                    	    						
                    	    // InternalCQLParser.g:423:7: ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    // InternalCQLParser.g:424:8: (lv_aggregations_9_0= ruleAggregation )
                    	    {
                    	    // InternalCQLParser.g:424:8: (lv_aggregations_9_0= ruleAggregation )
                    	    // InternalCQLParser.g:425:9: lv_aggregations_9_0= ruleAggregation
                    	    {

                    	    									newCompositeNode(grammarAccess.getSelectAccess().getAggregationsAggregationParserRuleCall_2_1_1_1_1_0());
                    	    								
                    	    pushFollow(FOLLOW_9);
                    	    lv_aggregations_9_0=ruleAggregation();

                    	    state._fsp--;


                    	    									if (current==null) {
                    	    										current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    									}
                    	    									add(
                    	    										current,
                    	    										"aggregations",
                    	    										lv_aggregations_9_0,
                    	    										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Aggregation");
                    	    									afterParserOrEnumRuleCall();
                    	    								

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 3 :
                    	    // InternalCQLParser.g:444:6: (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) )
                    	    {
                    	    // InternalCQLParser.g:444:6: (otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) ) )
                    	    // InternalCQLParser.g:445:7: otherlv_10= Comma ( (lv_expressions_11_0= ruleSelectExpression ) )
                    	    {
                    	    otherlv_10=(Token)match(input,Comma,FOLLOW_5); 

                    	    							newLeafNode(otherlv_10, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_2_0());
                    	    						
                    	    // InternalCQLParser.g:449:7: ( (lv_expressions_11_0= ruleSelectExpression ) )
                    	    // InternalCQLParser.g:450:8: (lv_expressions_11_0= ruleSelectExpression )
                    	    {
                    	    // InternalCQLParser.g:450:8: (lv_expressions_11_0= ruleSelectExpression )
                    	    // InternalCQLParser.g:451:9: lv_expressions_11_0= ruleSelectExpression
                    	    {

                    	    									newCompositeNode(grammarAccess.getSelectAccess().getExpressionsSelectExpressionParserRuleCall_2_1_1_2_1_0());
                    	    								
                    	    pushFollow(FOLLOW_9);
                    	    lv_expressions_11_0=ruleSelectExpression();

                    	    state._fsp--;


                    	    									if (current==null) {
                    	    										current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    									}
                    	    									add(
                    	    										current,
                    	    										"expressions",
                    	    										lv_expressions_11_0,
                    	    										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpression");
                    	    									afterParserOrEnumRuleCall();
                    	    								

                    	    }


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

            // InternalCQLParser.g:472:3: (otherlv_12= FROM ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= Comma ( (lv_sources_15_0= ruleSource ) ) )* )
            // InternalCQLParser.g:473:4: otherlv_12= FROM ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= Comma ( (lv_sources_15_0= ruleSource ) ) )*
            {
            otherlv_12=(Token)match(input,FROM,FOLLOW_11); 

            				newLeafNode(otherlv_12, grammarAccess.getSelectAccess().getFROMKeyword_3_0());
            			
            // InternalCQLParser.g:477:4: ( (lv_sources_13_0= ruleSource ) )+
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
            	    // InternalCQLParser.g:478:5: (lv_sources_13_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:478:5: (lv_sources_13_0= ruleSource )
            	    // InternalCQLParser.g:479:6: lv_sources_13_0= ruleSource
            	    {

            	    						newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_12);
            	    lv_sources_13_0=ruleSource();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelectRule());
            	    						}
            	    						add(
            	    							current,
            	    							"sources",
            	    							lv_sources_13_0,
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

            // InternalCQLParser.g:496:4: (otherlv_14= Comma ( (lv_sources_15_0= ruleSource ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==Comma) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalCQLParser.g:497:5: otherlv_14= Comma ( (lv_sources_15_0= ruleSource ) )
            	    {
            	    otherlv_14=(Token)match(input,Comma,FOLLOW_11); 

            	    					newLeafNode(otherlv_14, grammarAccess.getSelectAccess().getCommaKeyword_3_2_0());
            	    				
            	    // InternalCQLParser.g:501:5: ( (lv_sources_15_0= ruleSource ) )
            	    // InternalCQLParser.g:502:6: (lv_sources_15_0= ruleSource )
            	    {
            	    // InternalCQLParser.g:502:6: (lv_sources_15_0= ruleSource )
            	    // InternalCQLParser.g:503:7: lv_sources_15_0= ruleSource
            	    {

            	    							newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_2_1_0());
            	    						
            	    pushFollow(FOLLOW_13);
            	    lv_sources_15_0=ruleSource();

            	    state._fsp--;


            	    							if (current==null) {
            	    								current = createModelElementForParent(grammarAccess.getSelectRule());
            	    							}
            	    							add(
            	    								current,
            	    								"sources",
            	    								lv_sources_15_0,
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

            // InternalCQLParser.g:522:3: (otherlv_16= WHERE ( (lv_predicates_17_0= ruleExpressionsModel ) ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==WHERE) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQLParser.g:523:4: otherlv_16= WHERE ( (lv_predicates_17_0= ruleExpressionsModel ) )
                    {
                    otherlv_16=(Token)match(input,WHERE,FOLLOW_14); 

                    				newLeafNode(otherlv_16, grammarAccess.getSelectAccess().getWHEREKeyword_4_0());
                    			
                    // InternalCQLParser.g:527:4: ( (lv_predicates_17_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:528:5: (lv_predicates_17_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:528:5: (lv_predicates_17_0= ruleExpressionsModel )
                    // InternalCQLParser.g:529:6: lv_predicates_17_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getPredicatesExpressionsModelParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_15);
                    lv_predicates_17_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"predicates",
                    							lv_predicates_17_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:547:3: (otherlv_18= GROUP otherlv_19= BY ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= Comma ( (lv_order_22_0= ruleAttribute ) ) )* )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==GROUP) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQLParser.g:548:4: otherlv_18= GROUP otherlv_19= BY ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= Comma ( (lv_order_22_0= ruleAttribute ) ) )*
                    {
                    otherlv_18=(Token)match(input,GROUP,FOLLOW_16); 

                    				newLeafNode(otherlv_18, grammarAccess.getSelectAccess().getGROUPKeyword_5_0());
                    			
                    otherlv_19=(Token)match(input,BY,FOLLOW_8); 

                    				newLeafNode(otherlv_19, grammarAccess.getSelectAccess().getBYKeyword_5_1());
                    			
                    // InternalCQLParser.g:556:4: ( (lv_order_20_0= ruleAttribute ) )+
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
                    	    // InternalCQLParser.g:557:5: (lv_order_20_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:557:5: (lv_order_20_0= ruleAttribute )
                    	    // InternalCQLParser.g:558:6: lv_order_20_0= ruleAttribute
                    	    {

                    	    						newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_2_0());
                    	    					
                    	    pushFollow(FOLLOW_17);
                    	    lv_order_20_0=ruleAttribute();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"order",
                    	    							lv_order_20_0,
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

                    // InternalCQLParser.g:575:4: (otherlv_21= Comma ( (lv_order_22_0= ruleAttribute ) ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==Comma) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // InternalCQLParser.g:576:5: otherlv_21= Comma ( (lv_order_22_0= ruleAttribute ) )
                    	    {
                    	    otherlv_21=(Token)match(input,Comma,FOLLOW_8); 

                    	    					newLeafNode(otherlv_21, grammarAccess.getSelectAccess().getCommaKeyword_5_3_0());
                    	    				
                    	    // InternalCQLParser.g:580:5: ( (lv_order_22_0= ruleAttribute ) )
                    	    // InternalCQLParser.g:581:6: (lv_order_22_0= ruleAttribute )
                    	    {
                    	    // InternalCQLParser.g:581:6: (lv_order_22_0= ruleAttribute )
                    	    // InternalCQLParser.g:582:7: lv_order_22_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_18);
                    	    lv_order_22_0=ruleAttribute();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"order",
                    	    								lv_order_22_0,
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

            // InternalCQLParser.g:601:3: (otherlv_23= HAVING ( (lv_having_24_0= ruleExpressionsModel ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==HAVING) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // InternalCQLParser.g:602:4: otherlv_23= HAVING ( (lv_having_24_0= ruleExpressionsModel ) )
                    {
                    otherlv_23=(Token)match(input,HAVING,FOLLOW_14); 

                    				newLeafNode(otherlv_23, grammarAccess.getSelectAccess().getHAVINGKeyword_6_0());
                    			
                    // InternalCQLParser.g:606:4: ( (lv_having_24_0= ruleExpressionsModel ) )
                    // InternalCQLParser.g:607:5: (lv_having_24_0= ruleExpressionsModel )
                    {
                    // InternalCQLParser.g:607:5: (lv_having_24_0= ruleExpressionsModel )
                    // InternalCQLParser.g:608:6: lv_having_24_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getHavingExpressionsModelParserRuleCall_6_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_having_24_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"having",
                    							lv_having_24_0,
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
    // InternalCQLParser.g:630:1: entryRuleNestedStatement returns [EObject current=null] : iv_ruleNestedStatement= ruleNestedStatement EOF ;
    public final EObject entryRuleNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedStatement = null;


        try {
            // InternalCQLParser.g:630:56: (iv_ruleNestedStatement= ruleNestedStatement EOF )
            // InternalCQLParser.g:631:2: iv_ruleNestedStatement= ruleNestedStatement EOF
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
    // InternalCQLParser.g:637:1: ruleNestedStatement returns [EObject current=null] : (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis ) ;
    public final EObject ruleNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_1 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:643:2: ( (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis ) )
            // InternalCQLParser.g:644:2: (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis )
            {
            // InternalCQLParser.g:644:2: (otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis )
            // InternalCQLParser.g:645:3: otherlv_0= LeftParenthesis this_Select_1= ruleSelect otherlv_2= RightParenthesis
            {
            otherlv_0=(Token)match(input,LeftParenthesis,FOLLOW_19); 

            			newLeafNode(otherlv_0, grammarAccess.getNestedStatementAccess().getLeftParenthesisKeyword_0());
            		

            			newCompositeNode(grammarAccess.getNestedStatementAccess().getSelectParserRuleCall_1());
            		
            pushFollow(FOLLOW_20);
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


    // $ANTLR start "entryRuleSource"
    // InternalCQLParser.g:665:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQLParser.g:665:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQLParser.g:666:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQLParser.g:672:1: ruleSource returns [EObject current=null] : ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) ) ;
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
            // InternalCQLParser.g:678:2: ( ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) ) )
            // InternalCQLParser.g:679:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) )
            {
            // InternalCQLParser.g:679:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==RULE_ID) ) {
                alt18=1;
            }
            else if ( (LA18_0==LeftParenthesis) ) {
                alt18=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }
            switch (alt18) {
                case 1 :
                    // InternalCQLParser.g:680:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
                    {
                    // InternalCQLParser.g:680:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
                    // InternalCQLParser.g:681:4: ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
                    {
                    // InternalCQLParser.g:681:4: ( (lv_name_0_0= ruleSourceName ) )
                    // InternalCQLParser.g:682:5: (lv_name_0_0= ruleSourceName )
                    {
                    // InternalCQLParser.g:682:5: (lv_name_0_0= ruleSourceName )
                    // InternalCQLParser.g:683:6: lv_name_0_0= ruleSourceName
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNameSourceNameParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_21);
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

                    // InternalCQLParser.g:700:4: (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==LeftSquareBracket) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // InternalCQLParser.g:701:5: otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket
                            {
                            otherlv_1=(Token)match(input,LeftSquareBracket,FOLLOW_22); 

                            					newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_1_0());
                            				
                            // InternalCQLParser.g:705:5: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                            int alt15=3;
                            int LA15_0 = input.LA(1);

                            if ( (LA15_0==UNBOUNDED) ) {
                                alt15=1;
                            }
                            else if ( (LA15_0==SIZE) ) {
                                int LA15_2 = input.LA(2);

                                if ( (LA15_2==RULE_INT) ) {
                                    int LA15_3 = input.LA(3);

                                    if ( (LA15_3==RULE_ID) ) {
                                        alt15=2;
                                    }
                                    else if ( (LA15_3==ADVANCE||LA15_3==TUPLE) ) {
                                        alt15=3;
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("", 15, 3, input);

                                        throw nvae;
                                    }
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 15, 2, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 15, 0, input);

                                throw nvae;
                            }
                            switch (alt15) {
                                case 1 :
                                    // InternalCQLParser.g:706:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQLParser.g:706:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQLParser.g:707:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQLParser.g:707:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQLParser.g:708:8: lv_unbounded_2_0= ruleWindow_Unbounded
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getUnboundedWindow_UnboundedParserRuleCall_0_1_1_0_0());
                                    							
                                    pushFollow(FOLLOW_23);
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
                                    // InternalCQLParser.g:726:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQLParser.g:726:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQLParser.g:727:7: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQLParser.g:727:7: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQLParser.g:728:8: lv_time_3_0= ruleWindow_Timebased
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getTimeWindow_TimebasedParserRuleCall_0_1_1_1_0());
                                    							
                                    pushFollow(FOLLOW_23);
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
                                    // InternalCQLParser.g:746:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQLParser.g:746:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQLParser.g:747:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQLParser.g:747:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQLParser.g:748:8: lv_tuple_4_0= ruleWindow_Tuplebased
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getTupleWindow_TuplebasedParserRuleCall_0_1_1_2_0());
                                    							
                                    pushFollow(FOLLOW_23);
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

                            otherlv_5=(Token)match(input,RightSquareBracket,FOLLOW_24); 

                            					newLeafNode(otherlv_5, grammarAccess.getSourceAccess().getRightSquareBracketKeyword_0_1_2());
                            				

                            }
                            break;

                    }

                    // InternalCQLParser.g:771:4: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==AS) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // InternalCQLParser.g:772:5: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                            {
                            otherlv_6=(Token)match(input,AS,FOLLOW_8); 

                            					newLeafNode(otherlv_6, grammarAccess.getSourceAccess().getASKeyword_0_2_0());
                            				
                            // InternalCQLParser.g:776:5: ( (lv_alias_7_0= ruleAlias ) )
                            // InternalCQLParser.g:777:6: (lv_alias_7_0= ruleAlias )
                            {
                            // InternalCQLParser.g:777:6: (lv_alias_7_0= ruleAlias )
                            // InternalCQLParser.g:778:7: lv_alias_7_0= ruleAlias
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
                    // InternalCQLParser.g:798:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) )
                    {
                    // InternalCQLParser.g:798:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) )
                    // InternalCQLParser.g:799:4: ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) )
                    {
                    // InternalCQLParser.g:799:4: ( (lv_nested_8_0= ruleNestedStatement ) )
                    // InternalCQLParser.g:800:5: (lv_nested_8_0= ruleNestedStatement )
                    {
                    // InternalCQLParser.g:800:5: (lv_nested_8_0= ruleNestedStatement )
                    // InternalCQLParser.g:801:6: lv_nested_8_0= ruleNestedStatement
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNestedNestedStatementParserRuleCall_1_0_0());
                    					
                    pushFollow(FOLLOW_25);
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

                    otherlv_9=(Token)match(input,AS,FOLLOW_8); 

                    				newLeafNode(otherlv_9, grammarAccess.getSourceAccess().getASKeyword_1_1());
                    			
                    // InternalCQLParser.g:822:4: ( (lv_alias_10_0= ruleAlias ) )
                    // InternalCQLParser.g:823:5: (lv_alias_10_0= ruleAlias )
                    {
                    // InternalCQLParser.g:823:5: (lv_alias_10_0= ruleAlias )
                    // InternalCQLParser.g:824:6: lv_alias_10_0= ruleAlias
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
    // InternalCQLParser.g:846:1: entryRuleSourceName returns [String current=null] : iv_ruleSourceName= ruleSourceName EOF ;
    public final String entryRuleSourceName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSourceName = null;


        try {
            // InternalCQLParser.g:846:50: (iv_ruleSourceName= ruleSourceName EOF )
            // InternalCQLParser.g:847:2: iv_ruleSourceName= ruleSourceName EOF
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
    // InternalCQLParser.g:853:1: ruleSourceName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleSourceName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:859:2: (this_ID_0= RULE_ID )
            // InternalCQLParser.g:860:2: this_ID_0= RULE_ID
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
    // InternalCQLParser.g:870:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQLParser.g:870:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQLParser.g:871:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQLParser.g:877:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:883:2: ( ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:884:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:884:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:885:3: ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:885:3: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQLParser.g:886:4: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQLParser.g:886:4: (lv_name_0_0= ruleAttributeName )
            // InternalCQLParser.g:887:5: lv_name_0_0= ruleAttributeName
            {

            					newCompositeNode(grammarAccess.getAttributeAccess().getNameAttributeNameParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_24);
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

            // InternalCQLParser.g:904:3: (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==AS) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQLParser.g:905:4: otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,AS,FOLLOW_8); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:909:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQLParser.g:910:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQLParser.g:910:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQLParser.g:911:6: lv_alias_2_0= ruleAlias
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
    // InternalCQLParser.g:933:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:933:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQLParser.g:934:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
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
    // InternalCQLParser.g:940:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleAttributeName ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:946:2: ( ( (lv_name_0_0= ruleAttributeName ) ) )
            // InternalCQLParser.g:947:2: ( (lv_name_0_0= ruleAttributeName ) )
            {
            // InternalCQLParser.g:947:2: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQLParser.g:948:3: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQLParser.g:948:3: (lv_name_0_0= ruleAttributeName )
            // InternalCQLParser.g:949:4: lv_name_0_0= ruleAttributeName
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
    // InternalCQLParser.g:969:1: entryRuleAttributeName returns [String current=null] : iv_ruleAttributeName= ruleAttributeName EOF ;
    public final String entryRuleAttributeName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAttributeName = null;


        try {
            // InternalCQLParser.g:969:53: (iv_ruleAttributeName= ruleAttributeName EOF )
            // InternalCQLParser.g:970:2: iv_ruleAttributeName= ruleAttributeName EOF
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
    // InternalCQLParser.g:976:1: ruleAttributeName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) ) ;
    public final AntlrDatatypeRuleToken ruleAttributeName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_SourceName_1 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:982:2: ( (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) ) )
            // InternalCQLParser.g:983:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) )
            {
            // InternalCQLParser.g:983:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) )
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==RULE_ID) ) {
                int LA20_1 = input.LA(2);

                if ( (LA20_1==EOF||(LA20_1>=STARTTIMESTAMP && LA20_1<=ENDTIMESTAMP)||LA20_1==DolToEur||LA20_1==BOOLEAN||LA20_1==INTEGER||(LA20_1>=ATTACH && LA20_1<=GROUP)||LA20_1==DROP||(LA20_1>=FROM && LA20_1<=LONG)||(LA20_1>=TRUE && LA20_1<=MIN)||(LA20_1>=SUM && LA20_1<=AS)||(LA20_1>=IN && LA20_1<=OR)||(LA20_1>=RightParenthesis && LA20_1<=HyphenMinus)||LA20_1==Solidus||(LA20_1>=Semicolon && LA20_1<=GreaterThanSign)||(LA20_1>=RightSquareBracket && LA20_1<=RULE_STRING)) ) {
                    alt20=1;
                }
                else if ( (LA20_1==FullStop) ) {
                    alt20=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQLParser.g:984:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:992:3: (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID )
                    {
                    // InternalCQLParser.g:992:3: (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID )
                    // InternalCQLParser.g:993:4: this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getAttributeNameAccess().getSourceNameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_26);
                    this_SourceName_1=ruleSourceName();

                    state._fsp--;


                    				current.merge(this_SourceName_1);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,FullStop,FOLLOW_8); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getAttributeNameAccess().getFullStopKeyword_1_1());
                    			
                    this_ID_3=(Token)match(input,RULE_ID,FOLLOW_2); 

                    				current.merge(this_ID_3);
                    			

                    				newLeafNode(this_ID_3, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_1_2());
                    			

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
    // InternalCQLParser.g:1020:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQLParser.g:1020:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQLParser.g:1021:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
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
    // InternalCQLParser.g:1027:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1033:2: ( ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) ) )
            // InternalCQLParser.g:1034:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) )
            {
            // InternalCQLParser.g:1034:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) )
            // InternalCQLParser.g:1035:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) )
            {
            // InternalCQLParser.g:1035:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1036:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1036:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1037:5: lv_value_0_0= ruleAttributeWithoutAliasDefinition
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

            otherlv_1=(Token)match(input,IN,FOLLOW_11); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeWithNestedStatementAccess().getINKeyword_1());
            		
            // InternalCQLParser.g:1058:3: ( (lv_nested_2_0= ruleNestedStatement ) )
            // InternalCQLParser.g:1059:4: (lv_nested_2_0= ruleNestedStatement )
            {
            // InternalCQLParser.g:1059:4: (lv_nested_2_0= ruleNestedStatement )
            // InternalCQLParser.g:1060:5: lv_nested_2_0= ruleNestedStatement
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


    // $ANTLR start "entryRuleAggregation"
    // InternalCQLParser.g:1081:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalCQLParser.g:1081:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalCQLParser.g:1082:2: iv_ruleAggregation= ruleAggregation EOF
            {
             newCompositeNode(grammarAccess.getAggregationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAggregation=ruleAggregation();

            state._fsp--;

             current =iv_ruleAggregation; 
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
    // $ANTLR end "entryRuleAggregation"


    // $ANTLR start "ruleAggregation"
    // InternalCQLParser.g:1088:1: ruleAggregation returns [EObject current=null] : ( ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAggregation() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_1=null;
        Token lv_name_0_2=null;
        Token lv_name_0_3=null;
        Token lv_name_0_4=null;
        Token lv_name_0_5=null;
        Token lv_name_0_6=null;
        Token lv_name_0_7=null;
        Token lv_name_0_8=null;
        Token otherlv_1=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        EObject lv_attribute_2_0 = null;

        EObject lv_expression_3_0 = null;

        EObject lv_alias_6_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1094:2: ( ( ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1095:2: ( ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1095:2: ( ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1096:3: ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1096:3: ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) )
            // InternalCQLParser.g:1097:4: ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) )
            {
            // InternalCQLParser.g:1097:4: ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) )
            // InternalCQLParser.g:1098:5: (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST )
            {
            // InternalCQLParser.g:1098:5: (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST )
            int alt21=8;
            switch ( input.LA(1) ) {
            case AVG:
                {
                alt21=1;
                }
                break;
            case MIN:
                {
                alt21=2;
                }
                break;
            case MAX:
                {
                alt21=3;
                }
                break;
            case COUNT:
                {
                alt21=4;
                }
                break;
            case SUM:
                {
                alt21=5;
                }
                break;
            case MEDIAN:
                {
                alt21=6;
                }
                break;
            case FIRST:
                {
                alt21=7;
                }
                break;
            case LAST:
                {
                alt21=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // InternalCQLParser.g:1099:6: lv_name_0_1= AVG
                    {
                    lv_name_0_1=(Token)match(input,AVG,FOLLOW_28); 

                    						newLeafNode(lv_name_0_1, grammarAccess.getAggregationAccess().getNameAVGKeyword_0_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1110:6: lv_name_0_2= MIN
                    {
                    lv_name_0_2=(Token)match(input,MIN,FOLLOW_28); 

                    						newLeafNode(lv_name_0_2, grammarAccess.getAggregationAccess().getNameMINKeyword_0_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:1121:6: lv_name_0_3= MAX
                    {
                    lv_name_0_3=(Token)match(input,MAX,FOLLOW_28); 

                    						newLeafNode(lv_name_0_3, grammarAccess.getAggregationAccess().getNameMAXKeyword_0_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQLParser.g:1132:6: lv_name_0_4= COUNT
                    {
                    lv_name_0_4=(Token)match(input,COUNT,FOLLOW_28); 

                    						newLeafNode(lv_name_0_4, grammarAccess.getAggregationAccess().getNameCOUNTKeyword_0_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_4, null);
                    					

                    }
                    break;
                case 5 :
                    // InternalCQLParser.g:1143:6: lv_name_0_5= SUM
                    {
                    lv_name_0_5=(Token)match(input,SUM,FOLLOW_28); 

                    						newLeafNode(lv_name_0_5, grammarAccess.getAggregationAccess().getNameSUMKeyword_0_0_4());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_5, null);
                    					

                    }
                    break;
                case 6 :
                    // InternalCQLParser.g:1154:6: lv_name_0_6= MEDIAN
                    {
                    lv_name_0_6=(Token)match(input,MEDIAN,FOLLOW_28); 

                    						newLeafNode(lv_name_0_6, grammarAccess.getAggregationAccess().getNameMEDIANKeyword_0_0_5());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_6, null);
                    					

                    }
                    break;
                case 7 :
                    // InternalCQLParser.g:1165:6: lv_name_0_7= FIRST
                    {
                    lv_name_0_7=(Token)match(input,FIRST,FOLLOW_28); 

                    						newLeafNode(lv_name_0_7, grammarAccess.getAggregationAccess().getNameFIRSTKeyword_0_0_6());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_7, null);
                    					

                    }
                    break;
                case 8 :
                    // InternalCQLParser.g:1176:6: lv_name_0_8= LAST
                    {
                    lv_name_0_8=(Token)match(input,LAST,FOLLOW_28); 

                    						newLeafNode(lv_name_0_8, grammarAccess.getAggregationAccess().getNameLASTKeyword_0_0_7());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_8, null);
                    					

                    }
                    break;

            }


            }


            }

            otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregationAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQLParser.g:1193:3: ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case DolToEur:
                case FALSE:
                case TRUE:
                case Asterisk:
                case PlusSign:
                case HyphenMinus:
                case Solidus:
                case RULE_ID:
                case RULE_INT:
                case RULE_FLOAT:
                case RULE_STRING:
                    {
                    alt22=2;
                    }
                    break;
                case RightParenthesis:
                    {
                    alt22=1;
                    }
                    break;
                case FullStop:
                    {
                    int LA22_4 = input.LA(3);

                    if ( (LA22_4==RULE_ID) ) {
                        int LA22_5 = input.LA(4);

                        if ( (LA22_5==DolToEur||LA22_5==FALSE||LA22_5==TRUE||(LA22_5>=Asterisk && LA22_5<=PlusSign)||LA22_5==HyphenMinus||LA22_5==Solidus||(LA22_5>=RULE_ID && LA22_5<=RULE_STRING)) ) {
                            alt22=2;
                        }
                        else if ( (LA22_5==RightParenthesis) ) {
                            alt22=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 22, 5, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 22, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 22, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA22_0==FALSE||LA22_0==TRUE||(LA22_0>=RULE_INT && LA22_0<=RULE_STRING)) ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // InternalCQLParser.g:1194:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:1194:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQLParser.g:1195:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:1195:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQLParser.g:1196:6: lv_attribute_2_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getAggregationAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_2_0_0());
                    					
                    pushFollow(FOLLOW_20);
                    lv_attribute_2_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAggregationRule());
                    						}
                    						set(
                    							current,
                    							"attribute",
                    							lv_attribute_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1214:4: ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:1214:4: ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
                    // InternalCQLParser.g:1215:5: (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:1215:5: (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition )
                    // InternalCQLParser.g:1216:6: lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getAggregationAccess().getExpressionSelectExpressionWithoutAliasDefinitionParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_20);
                    lv_expression_3_0=ruleSelectExpressionWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAggregationRule());
                    						}
                    						set(
                    							current,
                    							"expression",
                    							lv_expression_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,RightParenthesis,FOLLOW_24); 

            			newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getRightParenthesisKeyword_3());
            		
            // InternalCQLParser.g:1238:3: (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==AS) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalCQLParser.g:1239:4: otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) )
                    {
                    otherlv_5=(Token)match(input,AS,FOLLOW_8); 

                    				newLeafNode(otherlv_5, grammarAccess.getAggregationAccess().getASKeyword_4_0());
                    			
                    // InternalCQLParser.g:1243:4: ( (lv_alias_6_0= ruleAlias ) )
                    // InternalCQLParser.g:1244:5: (lv_alias_6_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1244:5: (lv_alias_6_0= ruleAlias )
                    // InternalCQLParser.g:1245:6: lv_alias_6_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getAggregationAccess().getAliasAliasParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_6_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAggregationRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_6_0,
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
    // $ANTLR end "ruleAggregation"


    // $ANTLR start "entryRuleExpressionComponentConstantOrAttribute"
    // InternalCQLParser.g:1267:1: entryRuleExpressionComponentConstantOrAttribute returns [EObject current=null] : iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF ;
    public final EObject entryRuleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentConstantOrAttribute = null;


        try {
            // InternalCQLParser.g:1267:79: (iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF )
            // InternalCQLParser.g:1268:2: iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF
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
    // InternalCQLParser.g:1274:1: ruleExpressionComponentConstantOrAttribute returns [EObject current=null] : ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) ;
    public final EObject ruleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1280:2: ( ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) )
            // InternalCQLParser.g:1281:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            {
            // InternalCQLParser.g:1281:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==FALSE||LA24_0==TRUE||(LA24_0>=RULE_INT && LA24_0<=RULE_STRING)) ) {
                alt24=1;
            }
            else if ( (LA24_0==RULE_ID) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // InternalCQLParser.g:1282:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1282:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1283:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1283:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1284:5: lv_value_0_0= ruleAtomicWithoutAttributeRef
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
                    // InternalCQLParser.g:1302:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:1302:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQLParser.g:1303:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:1303:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQLParser.g:1304:5: lv_value_1_0= ruleAttributeWithoutAliasDefinition
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
    // InternalCQLParser.g:1325:1: entryRuleExpressionComponentMapperOrConstant returns [EObject current=null] : iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF ;
    public final EObject entryRuleExpressionComponentMapperOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentMapperOrConstant = null;


        try {
            // InternalCQLParser.g:1325:76: (iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF )
            // InternalCQLParser.g:1326:2: iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF
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
    // InternalCQLParser.g:1332:1: ruleExpressionComponentMapperOrConstant returns [EObject current=null] : ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) ;
    public final EObject ruleExpressionComponentMapperOrConstant() throws RecognitionException {
        EObject current = null;

        EObject this_Mapper_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1338:2: ( ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQLParser.g:1339:2: ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQLParser.g:1339:2: ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==DolToEur) ) {
                alt25=1;
            }
            else if ( (LA25_0==FALSE||LA25_0==TRUE||(LA25_0>=RULE_INT && LA25_0<=RULE_STRING)) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // InternalCQLParser.g:1340:3: (this_Mapper_0= ruleMapper () )
                    {
                    // InternalCQLParser.g:1340:3: (this_Mapper_0= ruleMapper () )
                    // InternalCQLParser.g:1341:4: this_Mapper_0= ruleMapper ()
                    {

                    				newCompositeNode(grammarAccess.getExpressionComponentMapperOrConstantAccess().getMapperParserRuleCall_0_0());
                    			
                    pushFollow(FOLLOW_2);
                    this_Mapper_0=ruleMapper();

                    state._fsp--;


                    				current = this_Mapper_0;
                    				afterParserOrEnumRuleCall();
                    			
                    // InternalCQLParser.g:1349:4: ()
                    // InternalCQLParser.g:1350:5: 
                    {

                    					current = forceCreateModelElementAndSet(
                    						grammarAccess.getExpressionComponentMapperOrConstantAccess().getExpressionComponentValueAction_0_1(),
                    						current);
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1358:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1358:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1359:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1359:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1360:5: lv_value_2_0= ruleAtomicWithoutAttributeRef
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
    // InternalCQLParser.g:1381:1: entryRuleExpressionComponentOnlyAttribute returns [EObject current=null] : iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF ;
    public final EObject entryRuleExpressionComponentOnlyAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlyAttribute = null;


        try {
            // InternalCQLParser.g:1381:73: (iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF )
            // InternalCQLParser.g:1382:2: iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF
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
    // InternalCQLParser.g:1388:1: ruleExpressionComponentOnlyAttribute returns [EObject current=null] : ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) ;
    public final EObject ruleExpressionComponentOnlyAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1394:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) )
            // InternalCQLParser.g:1395:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            {
            // InternalCQLParser.g:1395:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1396:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1396:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1397:4: lv_value_0_0= ruleAttributeWithoutAliasDefinition
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
    // InternalCQLParser.g:1417:1: entryRuleExpressionComponentOnlymapper returns [EObject current=null] : iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF ;
    public final EObject entryRuleExpressionComponentOnlymapper() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlymapper = null;


        try {
            // InternalCQLParser.g:1417:70: (iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF )
            // InternalCQLParser.g:1418:2: iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF
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
    // InternalCQLParser.g:1424:1: ruleExpressionComponentOnlymapper returns [EObject current=null] : (this_Mapper_0= ruleMapper () ) ;
    public final EObject ruleExpressionComponentOnlymapper() throws RecognitionException {
        EObject current = null;

        EObject this_Mapper_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1430:2: ( (this_Mapper_0= ruleMapper () ) )
            // InternalCQLParser.g:1431:2: (this_Mapper_0= ruleMapper () )
            {
            // InternalCQLParser.g:1431:2: (this_Mapper_0= ruleMapper () )
            // InternalCQLParser.g:1432:3: this_Mapper_0= ruleMapper ()
            {

            			newCompositeNode(grammarAccess.getExpressionComponentOnlymapperAccess().getMapperParserRuleCall_0());
            		
            pushFollow(FOLLOW_2);
            this_Mapper_0=ruleMapper();

            state._fsp--;


            			current = this_Mapper_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1440:3: ()
            // InternalCQLParser.g:1441:4: 
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


    // $ANTLR start "entryRuleMapper"
    // InternalCQLParser.g:1451:1: entryRuleMapper returns [EObject current=null] : iv_ruleMapper= ruleMapper EOF ;
    public final EObject entryRuleMapper() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapper = null;


        try {
            // InternalCQLParser.g:1451:47: (iv_ruleMapper= ruleMapper EOF )
            // InternalCQLParser.g:1452:2: iv_ruleMapper= ruleMapper EOF
            {
             newCompositeNode(grammarAccess.getMapperRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMapper=ruleMapper();

            state._fsp--;

             current =iv_ruleMapper; 
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
    // $ANTLR end "entryRuleMapper"


    // $ANTLR start "ruleMapper"
    // InternalCQLParser.g:1458:1: ruleMapper returns [EObject current=null] : ( () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) ;
    public final EObject ruleMapper() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_value_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1464:2: ( ( () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) )
            // InternalCQLParser.g:1465:2: ( () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            {
            // InternalCQLParser.g:1465:2: ( () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            // InternalCQLParser.g:1466:3: () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis
            {
            // InternalCQLParser.g:1466:3: ()
            // InternalCQLParser.g:1467:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getMapperAccess().getMapperAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1473:3: ( (lv_name_1_0= DolToEur ) )
            // InternalCQLParser.g:1474:4: (lv_name_1_0= DolToEur )
            {
            // InternalCQLParser.g:1474:4: (lv_name_1_0= DolToEur )
            // InternalCQLParser.g:1475:5: lv_name_1_0= DolToEur
            {
            lv_name_1_0=(Token)match(input,DolToEur,FOLLOW_28); 

            					newLeafNode(lv_name_1_0, grammarAccess.getMapperAccess().getNameDolToEurKeyword_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getMapperRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_1_0, "DolToEur");
            				

            }


            }

            otherlv_2=(Token)match(input,LeftParenthesis,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getMapperAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQLParser.g:1491:3: ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
            // InternalCQLParser.g:1492:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1492:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            // InternalCQLParser.g:1493:5: lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getMapperAccess().getValueSelectExpressionWithoutAliasDefinitionParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_20);
            lv_value_3_0=ruleSelectExpressionWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getMapperRule());
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

            			newLeafNode(otherlv_4, grammarAccess.getMapperAccess().getRightParenthesisKeyword_4());
            		

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
    // $ANTLR end "ruleMapper"


    // $ANTLR start "entryRuleSelectExpression"
    // InternalCQLParser.g:1518:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQLParser.g:1518:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQLParser.g:1519:2: iv_ruleSelectExpression= ruleSelectExpression EOF
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
    // InternalCQLParser.g:1525:1: ruleSelectExpression returns [EObject current=null] : ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
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
            // InternalCQLParser.g:1531:2: ( ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1532:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1532:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1533:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1533:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==DolToEur||LA32_0==FALSE||LA32_0==TRUE||(LA32_0>=RULE_INT && LA32_0<=RULE_STRING)) ) {
                alt32=1;
            }
            else if ( (LA32_0==RULE_ID) ) {
                alt32=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // InternalCQLParser.g:1534:4: ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    {
                    // InternalCQLParser.g:1534:4: ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    // InternalCQLParser.g:1535:5: ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    {
                    // InternalCQLParser.g:1535:5: ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) )
                    // InternalCQLParser.g:1536:6: (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant )
                    {
                    // InternalCQLParser.g:1536:6: (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant )
                    // InternalCQLParser.g:1537:7: lv_expressions_0_0= ruleExpressionComponentMapperOrConstant
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

                    // InternalCQLParser.g:1554:5: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    loop28:
                    do {
                        int alt28=2;
                        int LA28_0 = input.LA(1);

                        if ( ((LA28_0>=Asterisk && LA28_0<=PlusSign)||LA28_0==HyphenMinus||LA28_0==Solidus) ) {
                            alt28=1;
                        }


                        switch (alt28) {
                    	case 1 :
                    	    // InternalCQLParser.g:1555:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1555:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1556:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1556:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    // InternalCQLParser.g:1557:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1557:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
                    	    int alt26=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt26=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt26=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt26=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt26=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 26, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt26) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1558:9: lv_operators_1_1= PlusSign
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
                    	            // InternalCQLParser.g:1569:9: lv_operators_1_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1580:9: lv_operators_1_3= Asterisk
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
                    	            // InternalCQLParser.g:1591:9: lv_operators_1_4= Solidus
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

                    	    // InternalCQLParser.g:1604:6: ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1605:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1605:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1606:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1606:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper )
                    	    int alt27=2;
                    	    int LA27_0 = input.LA(1);

                    	    if ( (LA27_0==FALSE||LA27_0==TRUE||(LA27_0>=RULE_ID && LA27_0<=RULE_STRING)) ) {
                    	        alt27=1;
                    	    }
                    	    else if ( (LA27_0==DolToEur) ) {
                    	        alt27=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 27, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt27) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1607:9: lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQLParser.g:1623:9: lv_expressions_2_2= ruleExpressionComponentOnlymapper
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
                    	    break loop28;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1644:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1644:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    // InternalCQLParser.g:1645:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    {
                    // InternalCQLParser.g:1645:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) )
                    // InternalCQLParser.g:1646:6: (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute )
                    {
                    // InternalCQLParser.g:1646:6: (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute )
                    // InternalCQLParser.g:1647:7: lv_expressions_3_0= ruleExpressionComponentOnlyAttribute
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

                    // InternalCQLParser.g:1664:5: ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    int cnt31=0;
                    loop31:
                    do {
                        int alt31=2;
                        int LA31_0 = input.LA(1);

                        if ( ((LA31_0>=Asterisk && LA31_0<=PlusSign)||LA31_0==HyphenMinus||LA31_0==Solidus) ) {
                            alt31=1;
                        }


                        switch (alt31) {
                    	case 1 :
                    	    // InternalCQLParser.g:1665:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1665:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1666:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1666:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    // InternalCQLParser.g:1667:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1667:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
                    	    int alt29=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt29=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt29=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt29=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt29=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 29, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt29) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1668:9: lv_operators_4_1= PlusSign
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
                    	            // InternalCQLParser.g:1679:9: lv_operators_4_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1690:9: lv_operators_4_3= Asterisk
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
                    	            // InternalCQLParser.g:1701:9: lv_operators_4_4= Solidus
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

                    	    // InternalCQLParser.g:1714:6: ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1715:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1715:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1716:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1716:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper )
                    	    int alt30=2;
                    	    int LA30_0 = input.LA(1);

                    	    if ( (LA30_0==FALSE||LA30_0==TRUE||(LA30_0>=RULE_ID && LA30_0<=RULE_STRING)) ) {
                    	        alt30=1;
                    	    }
                    	    else if ( (LA30_0==DolToEur) ) {
                    	        alt30=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 30, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt30) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1717:9: lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQLParser.g:1733:9: lv_expressions_5_2= ruleExpressionComponentOnlymapper
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
                    	    if ( cnt31 >= 1 ) break loop31;
                                EarlyExitException eee =
                                    new EarlyExitException(31, input);
                                throw eee;
                        }
                        cnt31++;
                    } while (true);


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:1754:3: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==AS) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // InternalCQLParser.g:1755:4: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,AS,FOLLOW_8); 

                    				newLeafNode(otherlv_6, grammarAccess.getSelectExpressionAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:1759:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQLParser.g:1760:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1760:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQLParser.g:1761:6: lv_alias_7_0= ruleAlias
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
    // InternalCQLParser.g:1783:1: entryRuleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF ;
    public final EObject entryRuleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:1783:79: (iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF )
            // InternalCQLParser.g:1784:2: iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF
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
    // InternalCQLParser.g:1790:1: ruleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) ) ;
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
            // InternalCQLParser.g:1796:2: ( (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) ) )
            // InternalCQLParser.g:1797:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) )
            {
            // InternalCQLParser.g:1797:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) )
            // InternalCQLParser.g:1798:3: this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            {

            			newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getSelectExpressionWithOnlyAttributeOrConstantParserRuleCall_0());
            		
            pushFollow(FOLLOW_5);
            this_SelectExpressionWithOnlyAttributeOrConstant_0=ruleSelectExpressionWithOnlyAttributeOrConstant();

            state._fsp--;


            			current = this_SelectExpressionWithOnlyAttributeOrConstant_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1806:3: ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==DolToEur||LA40_0==FALSE||LA40_0==TRUE||(LA40_0>=RULE_INT && LA40_0<=RULE_STRING)) ) {
                alt40=1;
            }
            else if ( (LA40_0==RULE_ID) ) {
                alt40=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // InternalCQLParser.g:1807:4: ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    {
                    // InternalCQLParser.g:1807:4: ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    // InternalCQLParser.g:1808:5: ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    {
                    // InternalCQLParser.g:1808:5: ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) )
                    // InternalCQLParser.g:1809:6: (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant )
                    {
                    // InternalCQLParser.g:1809:6: (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant )
                    // InternalCQLParser.g:1810:7: lv_expressions_1_0= ruleExpressionComponentMapperOrConstant
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

                    // InternalCQLParser.g:1827:5: ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    loop36:
                    do {
                        int alt36=2;
                        int LA36_0 = input.LA(1);

                        if ( ((LA36_0>=Asterisk && LA36_0<=PlusSign)||LA36_0==HyphenMinus||LA36_0==Solidus) ) {
                            alt36=1;
                        }


                        switch (alt36) {
                    	case 1 :
                    	    // InternalCQLParser.g:1828:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1828:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1829:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1829:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    // InternalCQLParser.g:1830:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1830:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
                    	    int alt34=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt34=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt34=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt34=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt34=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 34, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt34) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1831:9: lv_operators_2_1= PlusSign
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
                    	            // InternalCQLParser.g:1842:9: lv_operators_2_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1853:9: lv_operators_2_3= Asterisk
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
                    	            // InternalCQLParser.g:1864:9: lv_operators_2_4= Solidus
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

                    	    // InternalCQLParser.g:1877:6: ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1878:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1878:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1879:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1879:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper )
                    	    int alt35=2;
                    	    int LA35_0 = input.LA(1);

                    	    if ( (LA35_0==FALSE||LA35_0==TRUE||(LA35_0>=RULE_ID && LA35_0<=RULE_STRING)) ) {
                    	        alt35=1;
                    	    }
                    	    else if ( (LA35_0==DolToEur) ) {
                    	        alt35=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 35, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt35) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1880:9: lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQLParser.g:1896:9: lv_expressions_3_2= ruleExpressionComponentOnlymapper
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
                    	    break loop36;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1917:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1917:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    // InternalCQLParser.g:1918:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    {
                    // InternalCQLParser.g:1918:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) )
                    // InternalCQLParser.g:1919:6: (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute )
                    {
                    // InternalCQLParser.g:1919:6: (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute )
                    // InternalCQLParser.g:1920:7: lv_expressions_4_0= ruleExpressionComponentOnlyAttribute
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

                    // InternalCQLParser.g:1937:5: ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    int cnt39=0;
                    loop39:
                    do {
                        int alt39=2;
                        int LA39_0 = input.LA(1);

                        if ( ((LA39_0>=Asterisk && LA39_0<=PlusSign)||LA39_0==HyphenMinus||LA39_0==Solidus) ) {
                            alt39=1;
                        }


                        switch (alt39) {
                    	case 1 :
                    	    // InternalCQLParser.g:1938:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1938:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1939:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1939:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    // InternalCQLParser.g:1940:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1940:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
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
                    	            // InternalCQLParser.g:1941:9: lv_operators_5_1= PlusSign
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
                    	            // InternalCQLParser.g:1952:9: lv_operators_5_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1963:9: lv_operators_5_3= Asterisk
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
                    	            // InternalCQLParser.g:1974:9: lv_operators_5_4= Solidus
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

                    	    // InternalCQLParser.g:1987:6: ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1988:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1988:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1989:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1989:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper )
                    	    int alt38=2;
                    	    int LA38_0 = input.LA(1);

                    	    if ( (LA38_0==FALSE||LA38_0==TRUE||(LA38_0>=RULE_ID && LA38_0<=RULE_STRING)) ) {
                    	        alt38=1;
                    	    }
                    	    else if ( (LA38_0==DolToEur) ) {
                    	        alt38=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 38, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt38) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1990:9: lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQLParser.g:2006:9: lv_expressions_6_2= ruleExpressionComponentOnlymapper
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
                    	    if ( cnt39 >= 1 ) break loop39;
                                EarlyExitException eee =
                                    new EarlyExitException(39, input);
                                throw eee;
                        }
                        cnt39++;
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
    // InternalCQLParser.g:2031:1: entryRuleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF ;
    public final EObject entryRuleSelectExpressionWithOnlyAttributeOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithOnlyAttributeOrConstant = null;


        try {
            // InternalCQLParser.g:2031:84: (iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF )
            // InternalCQLParser.g:2032:2: iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF
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
    // InternalCQLParser.g:2038:1: ruleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) ;
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
            // InternalCQLParser.g:2044:2: ( ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) )
            // InternalCQLParser.g:2045:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            {
            // InternalCQLParser.g:2045:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            // InternalCQLParser.g:2046:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            {
            // InternalCQLParser.g:2046:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) )
            // InternalCQLParser.g:2047:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            {
            // InternalCQLParser.g:2047:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            // InternalCQLParser.g:2048:5: lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute
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

            // InternalCQLParser.g:2065:3: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( ((LA42_0>=Asterisk && LA42_0<=PlusSign)||LA42_0==HyphenMinus||LA42_0==Solidus) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // InternalCQLParser.g:2066:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    {
            	    // InternalCQLParser.g:2066:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
            	    // InternalCQLParser.g:2067:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    {
            	    // InternalCQLParser.g:2067:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    // InternalCQLParser.g:2068:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    {
            	    // InternalCQLParser.g:2068:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    int alt41=4;
            	    switch ( input.LA(1) ) {
            	    case PlusSign:
            	        {
            	        alt41=1;
            	        }
            	        break;
            	    case HyphenMinus:
            	        {
            	        alt41=2;
            	        }
            	        break;
            	    case Asterisk:
            	        {
            	        alt41=3;
            	        }
            	        break;
            	    case Solidus:
            	        {
            	        alt41=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 41, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt41) {
            	        case 1 :
            	            // InternalCQLParser.g:2069:7: lv_operators_1_1= PlusSign
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
            	            // InternalCQLParser.g:2080:7: lv_operators_1_2= HyphenMinus
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
            	            // InternalCQLParser.g:2091:7: lv_operators_1_3= Asterisk
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
            	            // InternalCQLParser.g:2102:7: lv_operators_1_4= Solidus
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

            	    // InternalCQLParser.g:2115:4: ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    // InternalCQLParser.g:2116:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    {
            	    // InternalCQLParser.g:2116:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    // InternalCQLParser.g:2117:6: lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute
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
            	    break loop42;
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
    // InternalCQLParser.g:2139:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQLParser.g:2139:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQLParser.g:2140:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQLParser.g:2146:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2152:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQLParser.g:2153:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2153:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2154:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2154:3: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2155:4: lv_name_0_0= RULE_ID
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
    // InternalCQLParser.g:2174:1: entryRuleCreateParameters returns [EObject current=null] : iv_ruleCreateParameters= ruleCreateParameters EOF ;
    public final EObject entryRuleCreateParameters() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateParameters = null;


        try {
            // InternalCQLParser.g:2174:57: (iv_ruleCreateParameters= ruleCreateParameters EOF )
            // InternalCQLParser.g:2175:2: iv_ruleCreateParameters= ruleCreateParameters EOF
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
    // InternalCQLParser.g:2181:1: ruleCreateParameters returns [EObject current=null] : (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) ;
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
            // InternalCQLParser.g:2187:2: ( (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) )
            // InternalCQLParser.g:2188:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            {
            // InternalCQLParser.g:2188:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            // InternalCQLParser.g:2189:3: otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis
            {
            otherlv_0=(Token)match(input,WRAPPER,FOLLOW_32); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateParametersAccess().getWRAPPERKeyword_0());
            		
            // InternalCQLParser.g:2193:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQLParser.g:2194:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQLParser.g:2194:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQLParser.g:2195:5: lv_wrapper_1_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2215:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQLParser.g:2216:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQLParser.g:2216:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQLParser.g:2217:5: lv_protocol_3_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2237:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQLParser.g:2238:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQLParser.g:2238:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQLParser.g:2239:5: lv_transport_5_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2259:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQLParser.g:2260:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQLParser.g:2260:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQLParser.g:2261:5: lv_datahandler_7_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2285:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+
            int cnt43=0;
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==RULE_STRING) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalCQLParser.g:2286:4: ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) )
            	    {
            	    // InternalCQLParser.g:2286:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2287:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2287:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQLParser.g:2288:6: lv_keys_10_0= RULE_STRING
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

            	    // InternalCQLParser.g:2304:4: ( (lv_values_11_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2305:5: (lv_values_11_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2305:5: (lv_values_11_0= RULE_STRING )
            	    // InternalCQLParser.g:2306:6: lv_values_11_0= RULE_STRING
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
            	    if ( cnt43 >= 1 ) break loop43;
                        EarlyExitException eee =
                            new EarlyExitException(43, input);
                        throw eee;
                }
                cnt43++;
            } while (true);

            // InternalCQLParser.g:2323:3: (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==Comma) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // InternalCQLParser.g:2324:4: otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) )
                    {
                    otherlv_12=(Token)match(input,Comma,FOLLOW_32); 

                    				newLeafNode(otherlv_12, grammarAccess.getCreateParametersAccess().getCommaKeyword_11_0());
                    			
                    // InternalCQLParser.g:2328:4: ( (lv_keys_13_0= RULE_STRING ) )
                    // InternalCQLParser.g:2329:5: (lv_keys_13_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2329:5: (lv_keys_13_0= RULE_STRING )
                    // InternalCQLParser.g:2330:6: lv_keys_13_0= RULE_STRING
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

                    // InternalCQLParser.g:2346:4: ( (lv_values_14_0= RULE_STRING ) )
                    // InternalCQLParser.g:2347:5: (lv_values_14_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2347:5: (lv_values_14_0= RULE_STRING )
                    // InternalCQLParser.g:2348:6: lv_values_14_0= RULE_STRING
                    {
                    lv_values_14_0=(Token)match(input,RULE_STRING,FOLLOW_20); 

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
    // InternalCQLParser.g:2373:1: entryRuleAttributeDefinition returns [EObject current=null] : iv_ruleAttributeDefinition= ruleAttributeDefinition EOF ;
    public final EObject entryRuleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeDefinition = null;


        try {
            // InternalCQLParser.g:2373:60: (iv_ruleAttributeDefinition= ruleAttributeDefinition EOF )
            // InternalCQLParser.g:2374:2: iv_ruleAttributeDefinition= ruleAttributeDefinition EOF
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
    // InternalCQLParser.g:2380:1: ruleAttributeDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis ) ;
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
            // InternalCQLParser.g:2386:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis ) )
            // InternalCQLParser.g:2387:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis )
            {
            // InternalCQLParser.g:2387:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis )
            // InternalCQLParser.g:2388:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis
            {
            // InternalCQLParser.g:2388:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2389:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2389:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2390:5: lv_name_0_0= RULE_ID
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

            otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeDefinitionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQLParser.g:2410:3: ( (lv_attributes_2_0= ruleAttribute ) )+
            int cnt45=0;
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==RULE_ID) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // InternalCQLParser.g:2411:4: (lv_attributes_2_0= ruleAttribute )
            	    {
            	    // InternalCQLParser.g:2411:4: (lv_attributes_2_0= ruleAttribute )
            	    // InternalCQLParser.g:2412:5: lv_attributes_2_0= ruleAttribute
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
            	    if ( cnt45 >= 1 ) break loop45;
                        EarlyExitException eee =
                            new EarlyExitException(45, input);
                        throw eee;
                }
                cnt45++;
            } while (true);

            // InternalCQLParser.g:2429:3: ( (lv_datatypes_3_0= ruleDataType ) )+
            int cnt46=0;
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( ((LA46_0>=STARTTIMESTAMP && LA46_0<=ENDTIMESTAMP)||LA46_0==BOOLEAN||LA46_0==INTEGER||LA46_0==DOUBLE||LA46_0==STRING||LA46_0==FLOAT||LA46_0==LONG) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // InternalCQLParser.g:2430:4: (lv_datatypes_3_0= ruleDataType )
            	    {
            	    // InternalCQLParser.g:2430:4: (lv_datatypes_3_0= ruleDataType )
            	    // InternalCQLParser.g:2431:5: lv_datatypes_3_0= ruleDataType
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
            	    if ( cnt46 >= 1 ) break loop46;
                        EarlyExitException eee =
                            new EarlyExitException(46, input);
                        throw eee;
                }
                cnt46++;
            } while (true);

            // InternalCQLParser.g:2448:3: (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==Comma) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // InternalCQLParser.g:2449:4: otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) )
            	    {
            	    otherlv_4=(Token)match(input,Comma,FOLLOW_8); 

            	    				newLeafNode(otherlv_4, grammarAccess.getAttributeDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQLParser.g:2453:4: ( (lv_attributes_5_0= ruleAttribute ) )
            	    // InternalCQLParser.g:2454:5: (lv_attributes_5_0= ruleAttribute )
            	    {
            	    // InternalCQLParser.g:2454:5: (lv_attributes_5_0= ruleAttribute )
            	    // InternalCQLParser.g:2455:6: lv_attributes_5_0= ruleAttribute
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

            	    // InternalCQLParser.g:2472:4: ( (lv_datatypes_6_0= ruleDataType ) )
            	    // InternalCQLParser.g:2473:5: (lv_datatypes_6_0= ruleDataType )
            	    {
            	    // InternalCQLParser.g:2473:5: (lv_datatypes_6_0= ruleDataType )
            	    // InternalCQLParser.g:2474:6: lv_datatypes_6_0= ruleDataType
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
            	    break loop47;
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
    // InternalCQLParser.g:2500:1: entryRuleCreateStream1 returns [EObject current=null] : iv_ruleCreateStream1= ruleCreateStream1 EOF ;
    public final EObject entryRuleCreateStream1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStream1 = null;


        try {
            // InternalCQLParser.g:2500:54: (iv_ruleCreateStream1= ruleCreateStream1 EOF )
            // InternalCQLParser.g:2501:2: iv_ruleCreateStream1= ruleCreateStream1 EOF
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
    // InternalCQLParser.g:2507:1: ruleCreateStream1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateStream1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2513:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2514:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2514:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2515:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2515:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2516:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2516:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2517:5: lv_keyword_0_0= ruleCreateKeyword
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

            otherlv_1=(Token)match(input,STREAM,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStream1Access().getSTREAMKeyword_1());
            		
            // InternalCQLParser.g:2538:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2539:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2539:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2540:5: lv_attributes_2_0= ruleAttributeDefinition
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

            // InternalCQLParser.g:2557:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2558:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2558:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2559:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQLParser.g:2580:1: entryRuleCreateSink1 returns [EObject current=null] : iv_ruleCreateSink1= ruleCreateSink1 EOF ;
    public final EObject entryRuleCreateSink1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateSink1 = null;


        try {
            // InternalCQLParser.g:2580:52: (iv_ruleCreateSink1= ruleCreateSink1 EOF )
            // InternalCQLParser.g:2581:2: iv_ruleCreateSink1= ruleCreateSink1 EOF
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
    // InternalCQLParser.g:2587:1: ruleCreateSink1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateSink1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2593:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2594:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2594:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2595:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2595:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2596:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2596:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2597:5: lv_keyword_0_0= ruleCreateKeyword
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

            otherlv_1=(Token)match(input,SINK,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateSink1Access().getSINKKeyword_1());
            		
            // InternalCQLParser.g:2618:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2619:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2619:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2620:5: lv_attributes_2_0= ruleAttributeDefinition
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

            // InternalCQLParser.g:2637:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2638:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2638:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2639:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQLParser.g:2660:1: entryRuleCreateStreamChannel returns [EObject current=null] : iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF ;
    public final EObject entryRuleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamChannel = null;


        try {
            // InternalCQLParser.g:2660:60: (iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF )
            // InternalCQLParser.g:2661:2: iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF
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
    // InternalCQLParser.g:2667:1: ruleCreateStreamChannel returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) ;
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
            // InternalCQLParser.g:2673:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) )
            // InternalCQLParser.g:2674:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            {
            // InternalCQLParser.g:2674:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            // InternalCQLParser.g:2675:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) )
            {
            // InternalCQLParser.g:2675:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2676:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2676:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2677:5: lv_keyword_0_0= ruleCreateKeyword
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

            otherlv_1=(Token)match(input,STREAM,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamChannelAccess().getSTREAMKeyword_1());
            		
            // InternalCQLParser.g:2698:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2699:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2699:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2700:5: lv_attributes_2_0= ruleAttributeDefinition
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

            otherlv_3=(Token)match(input,CHANNEL,FOLLOW_8); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamChannelAccess().getCHANNELKeyword_3());
            		
            // InternalCQLParser.g:2721:3: ( (lv_host_4_0= RULE_ID ) )
            // InternalCQLParser.g:2722:4: (lv_host_4_0= RULE_ID )
            {
            // InternalCQLParser.g:2722:4: (lv_host_4_0= RULE_ID )
            // InternalCQLParser.g:2723:5: lv_host_4_0= RULE_ID
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
            		
            // InternalCQLParser.g:2743:3: ( (lv_port_6_0= RULE_INT ) )
            // InternalCQLParser.g:2744:4: (lv_port_6_0= RULE_INT )
            {
            // InternalCQLParser.g:2744:4: (lv_port_6_0= RULE_INT )
            // InternalCQLParser.g:2745:5: lv_port_6_0= RULE_INT
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
    // InternalCQLParser.g:2765:1: entryRuleCreateStreamFile returns [EObject current=null] : iv_ruleCreateStreamFile= ruleCreateStreamFile EOF ;
    public final EObject entryRuleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamFile = null;


        try {
            // InternalCQLParser.g:2765:57: (iv_ruleCreateStreamFile= ruleCreateStreamFile EOF )
            // InternalCQLParser.g:2766:2: iv_ruleCreateStreamFile= ruleCreateStreamFile EOF
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
    // InternalCQLParser.g:2772:1: ruleCreateStreamFile returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) ;
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
            // InternalCQLParser.g:2778:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2779:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:2779:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            // InternalCQLParser.g:2780:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2780:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2781:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2781:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2782:5: lv_keyword_0_0= ruleCreateKeyword
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

            otherlv_1=(Token)match(input,STREAM,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamFileAccess().getSTREAMKeyword_1());
            		
            // InternalCQLParser.g:2803:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2804:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2804:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2805:5: lv_attributes_2_0= ruleAttributeDefinition
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
            		
            // InternalCQLParser.g:2826:3: ( (lv_filename_4_0= RULE_STRING ) )
            // InternalCQLParser.g:2827:4: (lv_filename_4_0= RULE_STRING )
            {
            // InternalCQLParser.g:2827:4: (lv_filename_4_0= RULE_STRING )
            // InternalCQLParser.g:2828:5: lv_filename_4_0= RULE_STRING
            {
            lv_filename_4_0=(Token)match(input,RULE_STRING,FOLLOW_25); 

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

            otherlv_5=(Token)match(input,AS,FOLLOW_8); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamFileAccess().getASKeyword_5());
            		
            // InternalCQLParser.g:2848:3: ( (lv_type_6_0= RULE_ID ) )
            // InternalCQLParser.g:2849:4: (lv_type_6_0= RULE_ID )
            {
            // InternalCQLParser.g:2849:4: (lv_type_6_0= RULE_ID )
            // InternalCQLParser.g:2850:5: lv_type_6_0= RULE_ID
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
    // InternalCQLParser.g:2870:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQLParser.g:2870:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQLParser.g:2871:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQLParser.g:2877:1: ruleCreateView returns [EObject current=null] : (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2883:2: ( (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) )
            // InternalCQLParser.g:2884:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            {
            // InternalCQLParser.g:2884:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            // InternalCQLParser.g:2885:3: otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) )
            {
            otherlv_0=(Token)match(input,VIEW,FOLLOW_8); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateViewAccess().getVIEWKeyword_0());
            		
            // InternalCQLParser.g:2889:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:2890:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:2890:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:2891:5: lv_name_1_0= RULE_ID
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

            otherlv_2=(Token)match(input,FROM,FOLLOW_11); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateViewAccess().getFROMKeyword_2());
            		
            // InternalCQLParser.g:2911:3: ( (lv_select_3_0= ruleNestedStatement ) )
            // InternalCQLParser.g:2912:4: (lv_select_3_0= ruleNestedStatement )
            {
            // InternalCQLParser.g:2912:4: (lv_select_3_0= ruleNestedStatement )
            // InternalCQLParser.g:2913:5: lv_select_3_0= ruleNestedStatement
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
    // InternalCQLParser.g:2934:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQLParser.g:2934:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQLParser.g:2935:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQLParser.g:2941:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2947:2: ( (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:2948:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:2948:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2949:3: otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,STREAM,FOLLOW_49); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,TO,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQLParser.g:2957:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQLParser.g:2958:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2958:4: (lv_name_2_0= RULE_ID )
            // InternalCQLParser.g:2959:5: lv_name_2_0= RULE_ID
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

            // InternalCQLParser.g:2975:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==SELECT) ) {
                alt48=1;
            }
            else if ( (LA48_0==RULE_ID) ) {
                alt48=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 48, 0, input);

                throw nvae;
            }
            switch (alt48) {
                case 1 :
                    // InternalCQLParser.g:2976:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQLParser.g:2976:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQLParser.g:2977:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQLParser.g:2977:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQLParser.g:2978:6: lv_statement_3_0= ruleSelect
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
                    // InternalCQLParser.g:2996:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:2996:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQLParser.g:2997:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQLParser.g:2997:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQLParser.g:2998:6: lv_inputname_4_0= RULE_ID
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
    // InternalCQLParser.g:3019:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQLParser.g:3019:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQLParser.g:3020:2: iv_ruleDrop= ruleDrop EOF
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
    // InternalCQLParser.g:3026:1: ruleDrop returns [EObject current=null] : ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= IFEXISTS ) )? ( (lv_value2_4_0= RULE_ID ) ) ) ;
    public final EObject ruleDrop() throws RecognitionException {
        EObject current = null;

        Token lv_keyword1_0_0=null;
        Token lv_keyword2_1_1=null;
        Token lv_keyword2_1_2=null;
        Token lv_value1_2_0=null;
        Token lv_keyword3_3_0=null;
        Token lv_value2_4_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3032:2: ( ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= IFEXISTS ) )? ( (lv_value2_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:3033:2: ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= IFEXISTS ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:3033:2: ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= IFEXISTS ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            // InternalCQLParser.g:3034:3: ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= IFEXISTS ) )? ( (lv_value2_4_0= RULE_ID ) )
            {
            // InternalCQLParser.g:3034:3: ( (lv_keyword1_0_0= DROP ) )
            // InternalCQLParser.g:3035:4: (lv_keyword1_0_0= DROP )
            {
            // InternalCQLParser.g:3035:4: (lv_keyword1_0_0= DROP )
            // InternalCQLParser.g:3036:5: lv_keyword1_0_0= DROP
            {
            lv_keyword1_0_0=(Token)match(input,DROP,FOLLOW_51); 

            					newLeafNode(lv_keyword1_0_0, grammarAccess.getDropAccess().getKeyword1DROPKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(current, "keyword1", lv_keyword1_0_0, "DROP");
            				

            }


            }

            // InternalCQLParser.g:3048:3: ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) )
            // InternalCQLParser.g:3049:4: ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) )
            {
            // InternalCQLParser.g:3049:4: ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) )
            // InternalCQLParser.g:3050:5: (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM )
            {
            // InternalCQLParser.g:3050:5: (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM )
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==SINK) ) {
                alt49=1;
            }
            else if ( (LA49_0==STREAM) ) {
                alt49=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;
            }
            switch (alt49) {
                case 1 :
                    // InternalCQLParser.g:3051:6: lv_keyword2_1_1= SINK
                    {
                    lv_keyword2_1_1=(Token)match(input,SINK,FOLLOW_8); 

                    						newLeafNode(lv_keyword2_1_1, grammarAccess.getDropAccess().getKeyword2SINKKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "keyword2", lv_keyword2_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:3062:6: lv_keyword2_1_2= STREAM
                    {
                    lv_keyword2_1_2=(Token)match(input,STREAM,FOLLOW_8); 

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

            // InternalCQLParser.g:3075:3: ( (lv_value1_2_0= RULE_ID ) )
            // InternalCQLParser.g:3076:4: (lv_value1_2_0= RULE_ID )
            {
            // InternalCQLParser.g:3076:4: (lv_value1_2_0= RULE_ID )
            // InternalCQLParser.g:3077:5: lv_value1_2_0= RULE_ID
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

            // InternalCQLParser.g:3093:3: ( (lv_keyword3_3_0= IFEXISTS ) )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==IFEXISTS) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // InternalCQLParser.g:3094:4: (lv_keyword3_3_0= IFEXISTS )
                    {
                    // InternalCQLParser.g:3094:4: (lv_keyword3_3_0= IFEXISTS )
                    // InternalCQLParser.g:3095:5: lv_keyword3_3_0= IFEXISTS
                    {
                    lv_keyword3_3_0=(Token)match(input,IFEXISTS,FOLLOW_8); 

                    					newLeafNode(lv_keyword3_3_0, grammarAccess.getDropAccess().getKeyword3IFEXISTSKeyword_3_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDropRule());
                    					}
                    					setWithLastConsumed(current, "keyword3", lv_keyword3_3_0, "IF EXISTS");
                    				

                    }


                    }
                    break;

            }

            // InternalCQLParser.g:3107:3: ( (lv_value2_4_0= RULE_ID ) )
            // InternalCQLParser.g:3108:4: (lv_value2_4_0= RULE_ID )
            {
            // InternalCQLParser.g:3108:4: (lv_value2_4_0= RULE_ID )
            // InternalCQLParser.g:3109:5: lv_value2_4_0= RULE_ID
            {
            lv_value2_4_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_value2_4_0, grammarAccess.getDropAccess().getValue2IDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(
            						current,
            						"value2",
            						lv_value2_4_0,
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
    // $ANTLR end "ruleDrop"


    // $ANTLR start "entryRuleWindow_Unbounded"
    // InternalCQLParser.g:3129:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQLParser.g:3129:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQLParser.g:3130:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQLParser.g:3136:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= UNBOUNDED ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3142:2: (kw= UNBOUNDED )
            // InternalCQLParser.g:3143:2: kw= UNBOUNDED
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
    // InternalCQLParser.g:3151:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQLParser.g:3151:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQLParser.g:3152:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQLParser.g:3158:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) ;
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
            // InternalCQLParser.g:3164:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) )
            // InternalCQLParser.g:3165:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            {
            // InternalCQLParser.g:3165:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            // InternalCQLParser.g:3166:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_47); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:3170:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:3171:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:3171:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:3172:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_8); 

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

            // InternalCQLParser.g:3188:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQLParser.g:3189:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQLParser.g:3189:4: (lv_unit_2_0= RULE_ID )
            // InternalCQLParser.g:3190:5: lv_unit_2_0= RULE_ID
            {
            lv_unit_2_0=(Token)match(input,RULE_ID,FOLLOW_53); 

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

            // InternalCQLParser.g:3206:3: (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==ADVANCE) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // InternalCQLParser.g:3207:4: otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,ADVANCE,FOLLOW_47); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQLParser.g:3211:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQLParser.g:3212:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3212:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQLParser.g:3213:6: lv_advance_size_4_0= RULE_INT
                    {
                    lv_advance_size_4_0=(Token)match(input,RULE_INT,FOLLOW_8); 

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

                    // InternalCQLParser.g:3229:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQLParser.g:3230:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3230:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQLParser.g:3231:6: lv_advance_unit_5_0= RULE_ID
                    {
                    lv_advance_unit_5_0=(Token)match(input,RULE_ID,FOLLOW_54); 

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
    // InternalCQLParser.g:3256:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQLParser.g:3256:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQLParser.g:3257:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQLParser.g:3263:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQLParser.g:3269:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQLParser.g:3270:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQLParser.g:3270:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQLParser.g:3271:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_47); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:3275:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:3276:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:3276:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:3277:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_55); 

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

            // InternalCQLParser.g:3293:3: (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==ADVANCE) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // InternalCQLParser.g:3294:4: otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,ADVANCE,FOLLOW_47); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQLParser.g:3298:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQLParser.g:3299:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3299:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQLParser.g:3300:6: lv_advance_size_3_0= RULE_INT
                    {
                    lv_advance_size_3_0=(Token)match(input,RULE_INT,FOLLOW_56); 

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

            otherlv_4=(Token)match(input,TUPLE,FOLLOW_57); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQLParser.g:3321:3: (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==PARTITION) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // InternalCQLParser.g:3322:4: otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,PARTITION,FOLLOW_16); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,BY,FOLLOW_8); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQLParser.g:3330:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQLParser.g:3331:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:3331:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQLParser.g:3332:6: lv_partition_attribute_7_0= ruleAttribute
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
    // InternalCQLParser.g:3354:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQLParser.g:3354:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQLParser.g:3355:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQLParser.g:3361:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3367:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQLParser.g:3368:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQLParser.g:3368:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQLParser.g:3369:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQLParser.g:3369:3: ()
            // InternalCQLParser.g:3370:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:3376:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQLParser.g:3377:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQLParser.g:3377:4: (lv_elements_1_0= ruleExpression )
            // InternalCQLParser.g:3378:5: lv_elements_1_0= ruleExpression
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
    // InternalCQLParser.g:3399:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQLParser.g:3399:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQLParser.g:3400:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQLParser.g:3406:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3412:2: (this_Or_0= ruleOr )
            // InternalCQLParser.g:3413:2: this_Or_0= ruleOr
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
    // InternalCQLParser.g:3424:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQLParser.g:3424:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQLParser.g:3425:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQLParser.g:3431:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3437:2: ( (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQLParser.g:3438:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQLParser.g:3438:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQLParser.g:3439:3: this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_58);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3447:3: ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            loop54:
            do {
                int alt54=2;
                int LA54_0 = input.LA(1);

                if ( (LA54_0==OR) ) {
                    alt54=1;
                }


                switch (alt54) {
            	case 1 :
            	    // InternalCQLParser.g:3448:4: () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQLParser.g:3448:4: ()
            	    // InternalCQLParser.g:3449:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,OR,FOLLOW_14); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:3459:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQLParser.g:3460:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQLParser.g:3460:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQLParser.g:3461:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_58);
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
            	    break loop54;
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
    // InternalCQLParser.g:3483:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQLParser.g:3483:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQLParser.g:3484:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQLParser.g:3490:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3496:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQLParser.g:3497:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQLParser.g:3497:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQLParser.g:3498:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_59);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3506:3: ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop55:
            do {
                int alt55=2;
                int LA55_0 = input.LA(1);

                if ( (LA55_0==AND) ) {
                    alt55=1;
                }


                switch (alt55) {
            	case 1 :
            	    // InternalCQLParser.g:3507:4: () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQLParser.g:3507:4: ()
            	    // InternalCQLParser.g:3508:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,AND,FOLLOW_14); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:3518:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQLParser.g:3519:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQLParser.g:3519:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQLParser.g:3520:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_59);
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
    // $ANTLR end "ruleAnd"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQLParser.g:3542:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQLParser.g:3542:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQLParser.g:3543:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQLParser.g:3549:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3555:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQLParser.g:3556:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQLParser.g:3556:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQLParser.g:3557:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_60);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3565:3: ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==ExclamationMarkEqualsSign||LA57_0==EqualsSign) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // InternalCQLParser.g:3566:4: () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQLParser.g:3566:4: ()
            	    // InternalCQLParser.g:3567:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3573:4: ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) )
            	    // InternalCQLParser.g:3574:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    {
            	    // InternalCQLParser.g:3574:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    // InternalCQLParser.g:3575:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    {
            	    // InternalCQLParser.g:3575:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    int alt56=2;
            	    int LA56_0 = input.LA(1);

            	    if ( (LA56_0==EqualsSign) ) {
            	        alt56=1;
            	    }
            	    else if ( (LA56_0==ExclamationMarkEqualsSign) ) {
            	        alt56=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 56, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt56) {
            	        case 1 :
            	            // InternalCQLParser.g:3576:7: lv_op_2_1= EqualsSign
            	            {
            	            lv_op_2_1=(Token)match(input,EqualsSign,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:3587:7: lv_op_2_2= ExclamationMarkEqualsSign
            	            {
            	            lv_op_2_2=(Token)match(input,ExclamationMarkEqualsSign,FOLLOW_14); 

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

            	    // InternalCQLParser.g:3600:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQLParser.g:3601:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQLParser.g:3601:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQLParser.g:3602:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_60);
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
    // $ANTLR end "ruleEqualitiy"


    // $ANTLR start "entryRuleComparison"
    // InternalCQLParser.g:3624:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQLParser.g:3624:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQLParser.g:3625:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQLParser.g:3631:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQLParser.g:3637:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQLParser.g:3638:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQLParser.g:3638:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQLParser.g:3639:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_61);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3647:3: ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( ((LA59_0>=LessThanSignEqualsSign && LA59_0<=GreaterThanSignEqualsSign)||LA59_0==LessThanSign||LA59_0==GreaterThanSign) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // InternalCQLParser.g:3648:4: () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQLParser.g:3648:4: ()
            	    // InternalCQLParser.g:3649:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3655:4: ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) )
            	    // InternalCQLParser.g:3656:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    {
            	    // InternalCQLParser.g:3656:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    // InternalCQLParser.g:3657:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    {
            	    // InternalCQLParser.g:3657:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    int alt58=4;
            	    switch ( input.LA(1) ) {
            	    case GreaterThanSignEqualsSign:
            	        {
            	        alt58=1;
            	        }
            	        break;
            	    case LessThanSignEqualsSign:
            	        {
            	        alt58=2;
            	        }
            	        break;
            	    case LessThanSign:
            	        {
            	        alt58=3;
            	        }
            	        break;
            	    case GreaterThanSign:
            	        {
            	        alt58=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 58, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt58) {
            	        case 1 :
            	            // InternalCQLParser.g:3658:7: lv_op_2_1= GreaterThanSignEqualsSign
            	            {
            	            lv_op_2_1=(Token)match(input,GreaterThanSignEqualsSign,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:3669:7: lv_op_2_2= LessThanSignEqualsSign
            	            {
            	            lv_op_2_2=(Token)match(input,LessThanSignEqualsSign,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQLParser.g:3680:7: lv_op_2_3= LessThanSign
            	            {
            	            lv_op_2_3=(Token)match(input,LessThanSign,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQLParser.g:3691:7: lv_op_2_4= GreaterThanSign
            	            {
            	            lv_op_2_4=(Token)match(input,GreaterThanSign,FOLLOW_14); 

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

            	    // InternalCQLParser.g:3704:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQLParser.g:3705:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQLParser.g:3705:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQLParser.g:3706:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_61);
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
    // $ANTLR end "ruleComparison"


    // $ANTLR start "entryRulePlusOrMinus"
    // InternalCQLParser.g:3728:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQLParser.g:3728:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQLParser.g:3729:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQLParser.g:3735:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3741:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQLParser.g:3742:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQLParser.g:3742:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQLParser.g:3743:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_62);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3751:3: ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( (LA61_0==PlusSign||LA61_0==HyphenMinus) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // InternalCQLParser.g:3752:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQLParser.g:3752:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) )
            	    int alt60=2;
            	    int LA60_0 = input.LA(1);

            	    if ( (LA60_0==PlusSign) ) {
            	        alt60=1;
            	    }
            	    else if ( (LA60_0==HyphenMinus) ) {
            	        alt60=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 60, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt60) {
            	        case 1 :
            	            // InternalCQLParser.g:3753:5: ( () otherlv_2= PlusSign )
            	            {
            	            // InternalCQLParser.g:3753:5: ( () otherlv_2= PlusSign )
            	            // InternalCQLParser.g:3754:6: () otherlv_2= PlusSign
            	            {
            	            // InternalCQLParser.g:3754:6: ()
            	            // InternalCQLParser.g:3755:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,PlusSign,FOLLOW_14); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:3767:5: ( () otherlv_4= HyphenMinus )
            	            {
            	            // InternalCQLParser.g:3767:5: ( () otherlv_4= HyphenMinus )
            	            // InternalCQLParser.g:3768:6: () otherlv_4= HyphenMinus
            	            {
            	            // InternalCQLParser.g:3768:6: ()
            	            // InternalCQLParser.g:3769:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,HyphenMinus,FOLLOW_14); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQLParser.g:3781:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQLParser.g:3782:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQLParser.g:3782:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQLParser.g:3783:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_62);
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
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCQLParser.g:3805:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQLParser.g:3805:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQLParser.g:3806:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQLParser.g:3812:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3818:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQLParser.g:3819:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQLParser.g:3819:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQLParser.g:3820:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_63);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3828:3: ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop63:
            do {
                int alt63=2;
                int LA63_0 = input.LA(1);

                if ( (LA63_0==Asterisk||LA63_0==Solidus) ) {
                    alt63=1;
                }


                switch (alt63) {
            	case 1 :
            	    // InternalCQLParser.g:3829:4: () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQLParser.g:3829:4: ()
            	    // InternalCQLParser.g:3830:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3836:4: ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) )
            	    // InternalCQLParser.g:3837:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    {
            	    // InternalCQLParser.g:3837:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    // InternalCQLParser.g:3838:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    {
            	    // InternalCQLParser.g:3838:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    int alt62=2;
            	    int LA62_0 = input.LA(1);

            	    if ( (LA62_0==Asterisk) ) {
            	        alt62=1;
            	    }
            	    else if ( (LA62_0==Solidus) ) {
            	        alt62=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 62, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt62) {
            	        case 1 :
            	            // InternalCQLParser.g:3839:7: lv_op_2_1= Asterisk
            	            {
            	            lv_op_2_1=(Token)match(input,Asterisk,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQLParser.g:3850:7: lv_op_2_2= Solidus
            	            {
            	            lv_op_2_2=(Token)match(input,Solidus,FOLLOW_14); 

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

            	    // InternalCQLParser.g:3863:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQLParser.g:3864:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQLParser.g:3864:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQLParser.g:3865:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_63);
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
            	    break loop63;
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
    // InternalCQLParser.g:3887:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQLParser.g:3887:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQLParser.g:3888:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQLParser.g:3894:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQLParser.g:3900:2: ( ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQLParser.g:3901:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQLParser.g:3901:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt64=3;
            switch ( input.LA(1) ) {
            case LeftParenthesis:
                {
                alt64=1;
                }
                break;
            case NOT:
                {
                alt64=2;
                }
                break;
            case FALSE:
            case TRUE:
            case RULE_ID:
            case RULE_INT:
            case RULE_FLOAT:
            case RULE_STRING:
                {
                alt64=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 64, 0, input);

                throw nvae;
            }

            switch (alt64) {
                case 1 :
                    // InternalCQLParser.g:3902:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    {
                    // InternalCQLParser.g:3902:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    // InternalCQLParser.g:3903:4: () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis
                    {
                    // InternalCQLParser.g:3903:4: ()
                    // InternalCQLParser.g:3904:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_14); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQLParser.g:3914:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQLParser.g:3915:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQLParser.g:3915:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQLParser.g:3916:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_20);
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
                    // InternalCQLParser.g:3939:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQLParser.g:3939:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQLParser.g:3940:4: () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQLParser.g:3940:4: ()
                    // InternalCQLParser.g:3941:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,NOT,FOLLOW_14); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQLParser.g:3951:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQLParser.g:3952:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQLParser.g:3952:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQLParser.g:3953:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQLParser.g:3972:3: this_Atomic_7= ruleAtomic
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
    // InternalCQLParser.g:3984:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQLParser.g:3984:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQLParser.g:3985:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQLParser.g:3991:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
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
            // InternalCQLParser.g:3997:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQLParser.g:3998:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQLParser.g:3998:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt67=5;
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
            case RULE_ID:
                {
                alt67=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 67, 0, input);

                throw nvae;
            }

            switch (alt67) {
                case 1 :
                    // InternalCQLParser.g:3999:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:3999:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:4000:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:4000:4: ()
                    // InternalCQLParser.g:4001:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4007:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:4008:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4008:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:4009:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:4027:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:4027:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:4028:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:4028:4: ()
                    // InternalCQLParser.g:4029:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4035:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:4036:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:4036:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:4037:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:4055:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:4055:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:4056:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:4056:4: ()
                    // InternalCQLParser.g:4057:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4063:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:4064:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4064:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:4065:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:4083:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:4083:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:4084:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:4084:4: ()
                    // InternalCQLParser.g:4085:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4091:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:4092:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:4092:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:4093:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:4093:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    int alt65=2;
                    int LA65_0 = input.LA(1);

                    if ( (LA65_0==TRUE) ) {
                        alt65=1;
                    }
                    else if ( (LA65_0==FALSE) ) {
                        alt65=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 65, 0, input);

                        throw nvae;
                    }
                    switch (alt65) {
                        case 1 :
                            // InternalCQLParser.g:4094:7: lv_value_7_1= TRUE
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
                            // InternalCQLParser.g:4105:7: lv_value_7_2= FALSE
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
                    // InternalCQLParser.g:4120:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQLParser.g:4120:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQLParser.g:4121:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQLParser.g:4121:4: ()
                    // InternalCQLParser.g:4122:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4128:4: ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt66=2;
                    int LA66_0 = input.LA(1);

                    if ( (LA66_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
                        case FullStop:
                            {
                            int LA66_2 = input.LA(3);

                            if ( (LA66_2==RULE_ID) ) {
                                int LA66_5 = input.LA(4);

                                if ( (LA66_5==EOF||(LA66_5>=ATTACH && LA66_5<=CREATE)||LA66_5==HAVING||(LA66_5>=SELECT && LA66_5<=STREAM)||LA66_5==GROUP||LA66_5==DROP||(LA66_5>=VIEW && LA66_5<=AND)||(LA66_5>=ExclamationMarkEqualsSign && LA66_5<=GreaterThanSignEqualsSign)||LA66_5==OR||(LA66_5>=RightParenthesis && LA66_5<=PlusSign)||LA66_5==HyphenMinus||LA66_5==Solidus||(LA66_5>=Semicolon && LA66_5<=GreaterThanSign)) ) {
                                    alt66=1;
                                }
                                else if ( (LA66_5==IN) ) {
                                    alt66=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 66, 5, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 66, 2, input);

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
                            alt66=1;
                            }
                            break;
                        case IN:
                            {
                            alt66=2;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 66, 1, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 66, 0, input);

                        throw nvae;
                    }
                    switch (alt66) {
                        case 1 :
                            // InternalCQLParser.g:4129:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            {
                            // InternalCQLParser.g:4129:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            // InternalCQLParser.g:4130:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            {
                            // InternalCQLParser.g:4130:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            // InternalCQLParser.g:4131:7: lv_value_9_0= ruleAttributeWithoutAliasDefinition
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
                            // InternalCQLParser.g:4149:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQLParser.g:4149:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQLParser.g:4150:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQLParser.g:4150:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQLParser.g:4151:7: lv_value_10_0= ruleAttributeWithNestedStatement
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
    // InternalCQLParser.g:4174:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQLParser.g:4174:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQLParser.g:4175:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
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
    // InternalCQLParser.g:4181:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) ;
    public final EObject ruleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4187:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) )
            // InternalCQLParser.g:4188:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            {
            // InternalCQLParser.g:4188:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            int alt69=4;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt69=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt69=2;
                }
                break;
            case RULE_STRING:
                {
                alt69=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt69=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 69, 0, input);

                throw nvae;
            }

            switch (alt69) {
                case 1 :
                    // InternalCQLParser.g:4189:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:4189:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:4190:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:4190:4: ()
                    // InternalCQLParser.g:4191:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4197:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:4198:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4198:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:4199:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:4217:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:4217:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:4218:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:4218:4: ()
                    // InternalCQLParser.g:4219:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4225:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:4226:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:4226:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:4227:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:4245:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:4245:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:4246:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:4246:4: ()
                    // InternalCQLParser.g:4247:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4253:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:4254:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4254:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:4255:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:4273:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:4273:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:4274:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:4274:4: ()
                    // InternalCQLParser.g:4275:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4281:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:4282:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:4282:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:4283:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:4283:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    int alt68=2;
                    int LA68_0 = input.LA(1);

                    if ( (LA68_0==TRUE) ) {
                        alt68=1;
                    }
                    else if ( (LA68_0==FALSE) ) {
                        alt68=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 68, 0, input);

                        throw nvae;
                    }
                    switch (alt68) {
                        case 1 :
                            // InternalCQLParser.g:4284:7: lv_value_7_1= TRUE
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
                            // InternalCQLParser.g:4295:7: lv_value_7_2= FALSE
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
    // InternalCQLParser.g:4313:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQLParser.g:4313:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQLParser.g:4314:2: iv_ruleDataType= ruleDataType EOF
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
    // InternalCQLParser.g:4320:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) ) ;
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
            // InternalCQLParser.g:4326:2: ( ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) ) )
            // InternalCQLParser.g:4327:2: ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) )
            {
            // InternalCQLParser.g:4327:2: ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) )
            // InternalCQLParser.g:4328:3: ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) )
            {
            // InternalCQLParser.g:4328:3: ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) )
            // InternalCQLParser.g:4329:4: (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP )
            {
            // InternalCQLParser.g:4329:4: (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP )
            int alt70=8;
            switch ( input.LA(1) ) {
            case INTEGER:
                {
                alt70=1;
                }
                break;
            case DOUBLE:
                {
                alt70=2;
                }
                break;
            case LONG:
                {
                alt70=3;
                }
                break;
            case FLOAT:
                {
                alt70=4;
                }
                break;
            case STRING:
                {
                alt70=5;
                }
                break;
            case BOOLEAN:
                {
                alt70=6;
                }
                break;
            case STARTTIMESTAMP:
                {
                alt70=7;
                }
                break;
            case ENDTIMESTAMP:
                {
                alt70=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 70, 0, input);

                throw nvae;
            }

            switch (alt70) {
                case 1 :
                    // InternalCQLParser.g:4330:5: lv_value_0_1= INTEGER
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
                    // InternalCQLParser.g:4341:5: lv_value_0_2= DOUBLE
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
                    // InternalCQLParser.g:4352:5: lv_value_0_3= LONG
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
                    // InternalCQLParser.g:4363:5: lv_value_0_4= FLOAT
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
                    // InternalCQLParser.g:4374:5: lv_value_0_5= STRING
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
                    // InternalCQLParser.g:4385:5: lv_value_0_6= BOOLEAN
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
                    // InternalCQLParser.g:4396:5: lv_value_0_7= STARTTIMESTAMP
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
                    // InternalCQLParser.g:4407:5: lv_value_0_8= ENDTIMESTAMP
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
    // InternalCQLParser.g:4423:1: ruleCreateKeyword returns [Enumerator current=null] : ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) ;
    public final Enumerator ruleCreateKeyword() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4429:2: ( ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) )
            // InternalCQLParser.g:4430:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            {
            // InternalCQLParser.g:4430:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==CREATE) ) {
                alt71=1;
            }
            else if ( (LA71_0==ATTACH) ) {
                alt71=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }
            switch (alt71) {
                case 1 :
                    // InternalCQLParser.g:4431:3: (enumLiteral_0= CREATE )
                    {
                    // InternalCQLParser.g:4431:3: (enumLiteral_0= CREATE )
                    // InternalCQLParser.g:4432:4: enumLiteral_0= CREATE
                    {
                    enumLiteral_0=(Token)match(input,CREATE,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4439:3: (enumLiteral_1= ATTACH )
                    {
                    // InternalCQLParser.g:4439:3: (enumLiteral_1= ATTACH )
                    // InternalCQLParser.g:4440:4: enumLiteral_1= ATTACH
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
    static final String dfa_1s = "\52\uffff";
    static final String dfa_2s = "\1\24\3\uffff\2\32\1\uffff\1\112\1\uffff\1\73\1\112\1\4\1\112\10\4\1\112\1\4\1\112\1\20\2\4\3\uffff\2\112\10\74\2\4";
    static final String dfa_3s = "\1\54\3\uffff\2\50\1\uffff\1\112\1\uffff\1\73\3\112\10\77\3\112\1\44\1\112\1\101\3\uffff\2\112\10\77\1\66\1\47";
    static final String dfa_4s = "\1\uffff\1\1\1\2\1\3\2\uffff\1\10\1\uffff\1\5\22\uffff\1\6\1\7\1\4\14\uffff";
    static final String dfa_5s = "\52\uffff}>";
    static final String[] dfa_6s = {
            "\1\5\1\4\3\uffff\1\1\1\2\10\uffff\1\3\10\uffff\1\6",
            "",
            "",
            "",
            "\1\7\15\uffff\1\10",
            "\1\7\15\uffff\1\10",
            "",
            "\1\11",
            "",
            "\1\12",
            "\1\13",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\16\uffff\1\14\12\uffff\1\25\10\uffff\1\13",
            "\1\26",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\24\uffff\1\30\2\uffff\1\27",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\24\uffff\1\30\2\uffff\1\27",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\24\uffff\1\30\2\uffff\1\27",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\24\uffff\1\30\2\uffff\1\27",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\24\uffff\1\30\2\uffff\1\27",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\24\uffff\1\30\2\uffff\1\27",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\24\uffff\1\30\2\uffff\1\27",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\24\uffff\1\30\2\uffff\1\27",
            "\1\31",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\42\uffff\1\13",
            "\1\32",
            "\1\33\2\uffff\1\35\20\uffff\1\34",
            "\1\23\1\24\11\uffff\1\22\1\uffff\1\15\4\uffff\1\16\4\uffff\1\21\3\uffff\1\20\7\uffff\1\17\16\uffff\1\14\23\uffff\1\13",
            "\1\46\1\47\11\uffff\1\45\1\uffff\1\40\4\uffff\1\41\4\uffff\1\44\3\uffff\1\43\7\uffff\1\42\16\uffff\1\37\12\uffff\1\36",
            "",
            "",
            "",
            "\1\50",
            "\1\51",
            "\1\30\2\uffff\1\27",
            "\1\30\2\uffff\1\27",
            "\1\30\2\uffff\1\27",
            "\1\30\2\uffff\1\27",
            "\1\30\2\uffff\1\27",
            "\1\30\2\uffff\1\27",
            "\1\30\2\uffff\1\27",
            "\1\30\2\uffff\1\27",
            "\1\46\1\47\11\uffff\1\45\1\uffff\1\40\4\uffff\1\41\4\uffff\1\44\3\uffff\1\43\7\uffff\1\42\16\uffff\1\37",
            "\1\46\1\47\11\uffff\1\45\1\uffff\1\40\4\uffff\1\41\4\uffff\1\44\3\uffff\1\43\7\uffff\1\42"
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
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000100806300002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x2005C84071001800L,0x0000000000003C00L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0xA005C86071001800L,0x0000000000003C00L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x8000002000000000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0005C04051000000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0800000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x8800000500800002L,0x0000000000000400L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x8000000500800002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x2807C84071001800L,0x0000000000003C00L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000100800002L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x8000000000800002L,0x0000000000000400L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x8000000000800002L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0040000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000020000000400L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x6040000000000002L,0x0000000000000005L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x6000000000000000L,0x0000000000000005L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x6000000000000002L,0x0000000000000005L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x9000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000008088428030L,0x0000000000000400L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x9000008088428030L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000008088428030L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x9000000000000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000002000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000010004000000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000000000000080L,0x0000000000000400L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000040000004000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000000200004000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0000200000000002L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0008000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0030000000000002L,0x00000000000000A0L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x4000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x2000000000000002L,0x0000000000000004L});

}