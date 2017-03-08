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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "STARTTIMESTAMP", "ENDTIMESTAMP", "DATAHANDLER", "PARTITION", "TRANSPORT", "UNBOUNDED", "DISTINCT", "DolToEur", "PROTOCOL", "ADVANCE", "BOOLEAN", "CHANNEL", "INTEGER", "OPTIONS", "WRAPPER", "ATTACH", "CREATE", "DOUBLE", "EXISTS", "HAVING", "MEDIAN", "SELECT", "STREAM", "STRING", "COUNT", "FALSE", "FIRST", "FLOAT", "GROUP", "TUPLE", "WHERE", "DROP", "FILE", "FROM", "LAST", "LONG", "SINK", "SIZE", "TIME", "TRUE", "VIEW", "AND", "AVG", "MAX", "MIN", "NOT", "SUM", "ExclamationMarkEqualsSign", "LessThanSignEqualsSign", "GreaterThanSignEqualsSign", "AS", "BY", "IF", "IN", "OR", "TO", "LeftParenthesis", "RightParenthesis", "Asterisk", "PlusSign", "Comma", "HyphenMinus", "FullStop", "Solidus", "Colon", "Semicolon", "LessThanSign", "EqualsSign", "GreaterThanSign", "LeftSquareBracket", "RightSquareBracket", "RULE_ID", "RULE_INT", "RULE_FLOAT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER"
    };
    public static final int CREATE=20;
    public static final int FROM=37;
    public static final int VIEW=44;
    public static final int LessThanSign=70;
    public static final int LeftParenthesis=60;
    public static final int UNBOUNDED=9;
    public static final int WRAPPER=18;
    public static final int PARTITION=7;
    public static final int IF=56;
    public static final int RightSquareBracket=74;
    public static final int INTEGER=16;
    public static final int GreaterThanSign=72;
    public static final int RULE_ID=75;
    public static final int IN=57;
    public static final int DISTINCT=10;
    public static final int SIZE=41;
    public static final int PROTOCOL=12;
    public static final int RightParenthesis=61;
    public static final int TRUE=43;
    public static final int SUM=50;
    public static final int OPTIONS=17;
    public static final int WHERE=34;
    public static final int BOOLEAN=14;
    public static final int GreaterThanSignEqualsSign=53;
    public static final int AVG=46;
    public static final int NOT=49;
    public static final int AS=54;
    public static final int MIN=48;
    public static final int CHANNEL=15;
    public static final int LAST=38;
    public static final int SINK=40;
    public static final int PlusSign=63;
    public static final int RULE_INT=76;
    public static final int AND=45;
    public static final int RULE_ML_COMMENT=79;
    public static final int COUNT=28;
    public static final int LeftSquareBracket=73;
    public static final int ADVANCE=13;
    public static final int HAVING=23;
    public static final int FLOAT=31;
    public static final int MAX=47;
    public static final int STARTTIMESTAMP=4;
    public static final int RULE_STRING=78;
    public static final int DROP=35;
    public static final int RULE_SL_COMMENT=80;
    public static final int GROUP=32;
    public static final int Comma=64;
    public static final int EqualsSign=71;
    public static final int TRANSPORT=8;
    public static final int HyphenMinus=65;
    public static final int BY=55;
    public static final int DOUBLE=21;
    public static final int MEDIAN=24;
    public static final int LessThanSignEqualsSign=52;
    public static final int Solidus=67;
    public static final int Colon=68;
    public static final int DATAHANDLER=6;
    public static final int FILE=36;
    public static final int EOF=-1;
    public static final int Asterisk=62;
    public static final int LONG=39;
    public static final int FullStop=66;
    public static final int OR=58;
    public static final int DolToEur=11;
    public static final int EXISTS=22;
    public static final int RULE_WS=81;
    public static final int ENDTIMESTAMP=5;
    public static final int STREAM=26;
    public static final int TIME=42;
    public static final int RULE_ANY_OTHER=82;
    public static final int FIRST=30;
    public static final int SELECT=25;
    public static final int TUPLE=33;
    public static final int Semicolon=69;
    public static final int ATTACH=19;
    public static final int STRING=27;
    public static final int RULE_FLOAT=77;
    public static final int FALSE=29;
    public static final int ExclamationMarkEqualsSign=51;
    public static final int TO=59;

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

                        if ( (LA5_0==DolToEur||LA5_0==MEDIAN||(LA5_0>=COUNT && LA5_0<=FIRST)||LA5_0==LAST||LA5_0==TRUE||(LA5_0>=AVG && LA5_0<=MIN)||LA5_0==SUM||(LA5_0>=RULE_ID && LA5_0<=RULE_STRING)) ) {
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
    // InternalCQLParser.g:576:1: ruleArgument returns [EObject current=null] : ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_aggregation_1_0= ruleAggregation ) ) | ( (lv_expression_2_0= ruleSelectExpression ) ) ) ;
    public final EObject ruleArgument() throws RecognitionException {
        EObject current = null;

        EObject lv_attribute_0_0 = null;

        EObject lv_aggregation_1_0 = null;

        EObject lv_expression_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:582:2: ( ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_aggregation_1_0= ruleAggregation ) ) | ( (lv_expression_2_0= ruleSelectExpression ) ) ) )
            // InternalCQLParser.g:583:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_aggregation_1_0= ruleAggregation ) ) | ( (lv_expression_2_0= ruleSelectExpression ) ) )
            {
            // InternalCQLParser.g:583:2: ( ( (lv_attribute_0_0= ruleAttribute ) ) | ( (lv_aggregation_1_0= ruleAggregation ) ) | ( (lv_expression_2_0= ruleSelectExpression ) ) )
            int alt15=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                switch ( input.LA(2) ) {
                case FullStop:
                    {
                    int LA15_4 = input.LA(3);

                    if ( (LA15_4==RULE_ID) ) {
                        int LA15_6 = input.LA(4);

                        if ( ((LA15_6>=Asterisk && LA15_6<=PlusSign)||LA15_6==HyphenMinus||LA15_6==Solidus) ) {
                            alt15=3;
                        }
                        else if ( (LA15_6==EOF||LA15_6==DolToEur||LA15_6==MEDIAN||(LA15_6>=COUNT && LA15_6<=FIRST)||(LA15_6>=FROM && LA15_6<=LAST)||LA15_6==TRUE||(LA15_6>=AVG && LA15_6<=MIN)||LA15_6==SUM||LA15_6==AS||LA15_6==Comma||(LA15_6>=RULE_ID && LA15_6<=RULE_STRING)) ) {
                            alt15=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 6, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA15_4==Asterisk) ) {
                        int LA15_7 = input.LA(4);

                        if ( ((LA15_7>=Asterisk && LA15_7<=PlusSign)||LA15_7==HyphenMinus||LA15_7==Solidus) ) {
                            alt15=3;
                        }
                        else if ( (LA15_7==EOF||LA15_7==DolToEur||LA15_7==MEDIAN||(LA15_7>=COUNT && LA15_7<=FIRST)||(LA15_7>=FROM && LA15_7<=LAST)||LA15_7==TRUE||(LA15_7>=AVG && LA15_7<=MIN)||LA15_7==SUM||LA15_7==AS||LA15_7==Comma||(LA15_7>=RULE_ID && LA15_7<=RULE_STRING)) ) {
                            alt15=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 7, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case Asterisk:
                case PlusSign:
                case HyphenMinus:
                case Solidus:
                    {
                    alt15=3;
                    }
                    break;
                case EOF:
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
                    alt15=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
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
                alt15=2;
                }
                break;
            case DolToEur:
            case FALSE:
            case TRUE:
            case RULE_INT:
            case RULE_FLOAT:
            case RULE_STRING:
                {
                alt15=3;
                }
                break;
            default:
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
                    // InternalCQLParser.g:604:3: ( (lv_aggregation_1_0= ruleAggregation ) )
                    {
                    // InternalCQLParser.g:604:3: ( (lv_aggregation_1_0= ruleAggregation ) )
                    // InternalCQLParser.g:605:4: (lv_aggregation_1_0= ruleAggregation )
                    {
                    // InternalCQLParser.g:605:4: (lv_aggregation_1_0= ruleAggregation )
                    // InternalCQLParser.g:606:5: lv_aggregation_1_0= ruleAggregation
                    {

                    					newCompositeNode(grammarAccess.getArgumentAccess().getAggregationAggregationParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_aggregation_1_0=ruleAggregation();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getArgumentRule());
                    					}
                    					set(
                    						current,
                    						"aggregation",
                    						lv_aggregation_1_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Aggregation");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQLParser.g:624:3: ( (lv_expression_2_0= ruleSelectExpression ) )
                    {
                    // InternalCQLParser.g:624:3: ( (lv_expression_2_0= ruleSelectExpression ) )
                    // InternalCQLParser.g:625:4: (lv_expression_2_0= ruleSelectExpression )
                    {
                    // InternalCQLParser.g:625:4: (lv_expression_2_0= ruleSelectExpression )
                    // InternalCQLParser.g:626:5: lv_expression_2_0= ruleSelectExpression
                    {

                    					newCompositeNode(grammarAccess.getArgumentAccess().getExpressionSelectExpressionParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_expression_2_0=ruleSelectExpression();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getArgumentRule());
                    					}
                    					set(
                    						current,
                    						"expression",
                    						lv_expression_2_0,
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
    // InternalCQLParser.g:647:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQLParser.g:647:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQLParser.g:648:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQLParser.g:654:1: ruleSource returns [EObject current=null] : ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) ) ;
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
            // InternalCQLParser.g:660:2: ( ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) ) )
            // InternalCQLParser.g:661:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) )
            {
            // InternalCQLParser.g:661:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) ) )
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
                    // InternalCQLParser.g:662:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
                    {
                    // InternalCQLParser.g:662:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
                    // InternalCQLParser.g:663:4: ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )? (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
                    {
                    // InternalCQLParser.g:663:4: ( (lv_name_0_0= ruleSourceName ) )
                    // InternalCQLParser.g:664:5: (lv_name_0_0= ruleSourceName )
                    {
                    // InternalCQLParser.g:664:5: (lv_name_0_0= ruleSourceName )
                    // InternalCQLParser.g:665:6: lv_name_0_0= ruleSourceName
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

                    // InternalCQLParser.g:682:4: (otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==LeftSquareBracket) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // InternalCQLParser.g:683:5: otherlv_1= LeftSquareBracket ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= RightSquareBracket
                            {
                            otherlv_1=(Token)match(input,LeftSquareBracket,FOLLOW_21); 

                            					newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_1_0());
                            				
                            // InternalCQLParser.g:687:5: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
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
                                    // InternalCQLParser.g:688:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQLParser.g:688:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQLParser.g:689:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQLParser.g:689:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQLParser.g:690:8: lv_unbounded_2_0= ruleWindow_Unbounded
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
                                    // InternalCQLParser.g:708:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQLParser.g:708:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQLParser.g:709:7: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQLParser.g:709:7: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQLParser.g:710:8: lv_time_3_0= ruleWindow_Timebased
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
                                    // InternalCQLParser.g:728:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQLParser.g:728:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQLParser.g:729:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQLParser.g:729:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQLParser.g:730:8: lv_tuple_4_0= ruleWindow_Tuplebased
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

                    // InternalCQLParser.g:753:4: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==AS) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // InternalCQLParser.g:754:5: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                            {
                            otherlv_6=(Token)match(input,AS,FOLLOW_15); 

                            					newLeafNode(otherlv_6, grammarAccess.getSourceAccess().getASKeyword_0_2_0());
                            				
                            // InternalCQLParser.g:758:5: ( (lv_alias_7_0= ruleAlias ) )
                            // InternalCQLParser.g:759:6: (lv_alias_7_0= ruleAlias )
                            {
                            // InternalCQLParser.g:759:6: (lv_alias_7_0= ruleAlias )
                            // InternalCQLParser.g:760:7: lv_alias_7_0= ruleAlias
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
                    // InternalCQLParser.g:780:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) )
                    {
                    // InternalCQLParser.g:780:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) ) )
                    // InternalCQLParser.g:781:4: ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= AS ( (lv_alias_10_0= ruleAlias ) )
                    {
                    // InternalCQLParser.g:781:4: ( (lv_nested_8_0= ruleNestedStatement ) )
                    // InternalCQLParser.g:782:5: (lv_nested_8_0= ruleNestedStatement )
                    {
                    // InternalCQLParser.g:782:5: (lv_nested_8_0= ruleNestedStatement )
                    // InternalCQLParser.g:783:6: lv_nested_8_0= ruleNestedStatement
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
                    			
                    // InternalCQLParser.g:804:4: ( (lv_alias_10_0= ruleAlias ) )
                    // InternalCQLParser.g:805:5: (lv_alias_10_0= ruleAlias )
                    {
                    // InternalCQLParser.g:805:5: (lv_alias_10_0= ruleAlias )
                    // InternalCQLParser.g:806:6: lv_alias_10_0= ruleAlias
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
    // InternalCQLParser.g:828:1: entryRuleSourceName returns [String current=null] : iv_ruleSourceName= ruleSourceName EOF ;
    public final String entryRuleSourceName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSourceName = null;


        try {
            // InternalCQLParser.g:828:50: (iv_ruleSourceName= ruleSourceName EOF )
            // InternalCQLParser.g:829:2: iv_ruleSourceName= ruleSourceName EOF
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
    // InternalCQLParser.g:835:1: ruleSourceName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleSourceName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:841:2: (this_ID_0= RULE_ID )
            // InternalCQLParser.g:842:2: this_ID_0= RULE_ID
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
    // InternalCQLParser.g:852:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQLParser.g:852:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQLParser.g:853:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQLParser.g:859:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:865:2: ( ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:866:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:866:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:867:3: ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:867:3: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQLParser.g:868:4: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQLParser.g:868:4: (lv_name_0_0= ruleAttributeName )
            // InternalCQLParser.g:869:5: lv_name_0_0= ruleAttributeName
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

            // InternalCQLParser.g:886:3: (otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==AS) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQLParser.g:887:4: otherlv_1= AS ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:891:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQLParser.g:892:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQLParser.g:892:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQLParser.g:893:6: lv_alias_2_0= ruleAlias
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
    // InternalCQLParser.g:915:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:915:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQLParser.g:916:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
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
    // InternalCQLParser.g:922:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleAttributeName ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:928:2: ( ( (lv_name_0_0= ruleAttributeName ) ) )
            // InternalCQLParser.g:929:2: ( (lv_name_0_0= ruleAttributeName ) )
            {
            // InternalCQLParser.g:929:2: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQLParser.g:930:3: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQLParser.g:930:3: (lv_name_0_0= ruleAttributeName )
            // InternalCQLParser.g:931:4: lv_name_0_0= ruleAttributeName
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
    // InternalCQLParser.g:951:1: entryRuleAttributeName returns [String current=null] : iv_ruleAttributeName= ruleAttributeName EOF ;
    public final String entryRuleAttributeName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAttributeName = null;


        try {
            // InternalCQLParser.g:951:53: (iv_ruleAttributeName= ruleAttributeName EOF )
            // InternalCQLParser.g:952:2: iv_ruleAttributeName= ruleAttributeName EOF
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
    // InternalCQLParser.g:958:1: ruleAttributeName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) ) ;
    public final AntlrDatatypeRuleToken ruleAttributeName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_SourceName_1 = null;

        AntlrDatatypeRuleToken this_SourceName_4 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:964:2: ( (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) ) )
            // InternalCQLParser.g:965:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) )
            {
            // InternalCQLParser.g:965:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID ) | (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk ) )
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
                else if ( (LA21_1==EOF||(LA21_1>=STARTTIMESTAMP && LA21_1<=ENDTIMESTAMP)||LA21_1==DolToEur||LA21_1==BOOLEAN||LA21_1==INTEGER||(LA21_1>=ATTACH && LA21_1<=DOUBLE)||(LA21_1>=HAVING && LA21_1<=GROUP)||LA21_1==DROP||(LA21_1>=FROM && LA21_1<=LONG)||(LA21_1>=TRUE && LA21_1<=MIN)||(LA21_1>=SUM && LA21_1<=AS)||(LA21_1>=IN && LA21_1<=OR)||(LA21_1>=RightParenthesis && LA21_1<=HyphenMinus)||LA21_1==Solidus||(LA21_1>=Semicolon && LA21_1<=GreaterThanSign)||(LA21_1>=RightSquareBracket && LA21_1<=RULE_STRING)) ) {
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
                    // InternalCQLParser.g:966:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:974:3: (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID )
                    {
                    // InternalCQLParser.g:974:3: (this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID )
                    // InternalCQLParser.g:975:4: this_SourceName_1= ruleSourceName kw= FullStop this_ID_3= RULE_ID
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
                    // InternalCQLParser.g:999:3: (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk )
                    {
                    // InternalCQLParser.g:999:3: (this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk )
                    // InternalCQLParser.g:1000:4: this_SourceName_4= ruleSourceName kw= FullStop kw= Asterisk
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
    // InternalCQLParser.g:1025:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQLParser.g:1025:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQLParser.g:1026:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
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
    // InternalCQLParser.g:1032:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1038:2: ( ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) ) )
            // InternalCQLParser.g:1039:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) )
            {
            // InternalCQLParser.g:1039:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) ) )
            // InternalCQLParser.g:1040:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= IN ( (lv_nested_2_0= ruleNestedStatement ) )
            {
            // InternalCQLParser.g:1040:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1041:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1041:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1042:5: lv_value_0_0= ruleAttributeWithoutAliasDefinition
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
            		
            // InternalCQLParser.g:1063:3: ( (lv_nested_2_0= ruleNestedStatement ) )
            // InternalCQLParser.g:1064:4: (lv_nested_2_0= ruleNestedStatement )
            {
            // InternalCQLParser.g:1064:4: (lv_nested_2_0= ruleNestedStatement )
            // InternalCQLParser.g:1065:5: lv_nested_2_0= ruleNestedStatement
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
    // InternalCQLParser.g:1086:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalCQLParser.g:1086:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalCQLParser.g:1087:2: iv_ruleAggregation= ruleAggregation EOF
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
    // InternalCQLParser.g:1093:1: ruleAggregation returns [EObject current=null] : ( ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )? ) ;
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
            // InternalCQLParser.g:1099:2: ( ( ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1100:2: ( ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1100:2: ( ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1101:3: ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) ) otherlv_1= LeftParenthesis ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= RightParenthesis (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1101:3: ( ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) ) )
            // InternalCQLParser.g:1102:4: ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) )
            {
            // InternalCQLParser.g:1102:4: ( (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST ) )
            // InternalCQLParser.g:1103:5: (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST )
            {
            // InternalCQLParser.g:1103:5: (lv_name_0_1= AVG | lv_name_0_2= MIN | lv_name_0_3= MAX | lv_name_0_4= COUNT | lv_name_0_5= SUM | lv_name_0_6= MEDIAN | lv_name_0_7= FIRST | lv_name_0_8= LAST )
            int alt22=8;
            switch ( input.LA(1) ) {
            case AVG:
                {
                alt22=1;
                }
                break;
            case MIN:
                {
                alt22=2;
                }
                break;
            case MAX:
                {
                alt22=3;
                }
                break;
            case COUNT:
                {
                alt22=4;
                }
                break;
            case SUM:
                {
                alt22=5;
                }
                break;
            case MEDIAN:
                {
                alt22=6;
                }
                break;
            case FIRST:
                {
                alt22=7;
                }
                break;
            case LAST:
                {
                alt22=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }

            switch (alt22) {
                case 1 :
                    // InternalCQLParser.g:1104:6: lv_name_0_1= AVG
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
                    // InternalCQLParser.g:1115:6: lv_name_0_2= MIN
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
                    // InternalCQLParser.g:1126:6: lv_name_0_3= MAX
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
                    // InternalCQLParser.g:1137:6: lv_name_0_4= COUNT
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
                    // InternalCQLParser.g:1148:6: lv_name_0_5= SUM
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
                    // InternalCQLParser.g:1159:6: lv_name_0_6= MEDIAN
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
                    // InternalCQLParser.g:1170:6: lv_name_0_7= FIRST
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
                    // InternalCQLParser.g:1181:6: lv_name_0_8= LAST
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
            		
            // InternalCQLParser.g:1198:3: ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case FullStop:
                    {
                    int LA23_3 = input.LA(3);

                    if ( (LA23_3==RULE_ID) ) {
                        int LA23_5 = input.LA(4);

                        if ( (LA23_5==DolToEur||LA23_5==FALSE||LA23_5==TRUE||(LA23_5>=Asterisk && LA23_5<=PlusSign)||LA23_5==HyphenMinus||LA23_5==Solidus||(LA23_5>=RULE_ID && LA23_5<=RULE_STRING)) ) {
                            alt23=2;
                        }
                        else if ( (LA23_5==RightParenthesis) ) {
                            alt23=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 23, 5, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA23_3==Asterisk) ) {
                        int LA23_6 = input.LA(4);

                        if ( (LA23_6==RightParenthesis) ) {
                            alt23=1;
                        }
                        else if ( (LA23_6==DolToEur||LA23_6==FALSE||LA23_6==TRUE||(LA23_6>=Asterisk && LA23_6<=PlusSign)||LA23_6==HyphenMinus||LA23_6==Solidus||(LA23_6>=RULE_ID && LA23_6<=RULE_STRING)) ) {
                            alt23=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 23, 6, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 23, 3, input);

                        throw nvae;
                    }
                    }
                    break;
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
                    alt23=2;
                    }
                    break;
                case RightParenthesis:
                    {
                    alt23=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 23, 1, input);

                    throw nvae;
                }

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
                    // InternalCQLParser.g:1199:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:1199:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQLParser.g:1200:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:1200:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQLParser.g:1201:6: lv_attribute_2_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getAggregationAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_2_0_0());
                    					
                    pushFollow(FOLLOW_19);
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
                    // InternalCQLParser.g:1219:4: ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:1219:4: ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
                    // InternalCQLParser.g:1220:5: (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:1220:5: (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition )
                    // InternalCQLParser.g:1221:6: lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getAggregationAccess().getExpressionSelectExpressionWithoutAliasDefinitionParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_19);
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

            otherlv_4=(Token)match(input,RightParenthesis,FOLLOW_23); 

            			newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getRightParenthesisKeyword_3());
            		
            // InternalCQLParser.g:1243:3: (otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) ) )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==AS) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalCQLParser.g:1244:4: otherlv_5= AS ( (lv_alias_6_0= ruleAlias ) )
                    {
                    otherlv_5=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_5, grammarAccess.getAggregationAccess().getASKeyword_4_0());
                    			
                    // InternalCQLParser.g:1248:4: ( (lv_alias_6_0= ruleAlias ) )
                    // InternalCQLParser.g:1249:5: (lv_alias_6_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1249:5: (lv_alias_6_0= ruleAlias )
                    // InternalCQLParser.g:1250:6: lv_alias_6_0= ruleAlias
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
    // InternalCQLParser.g:1272:1: entryRuleExpressionComponentConstantOrAttribute returns [EObject current=null] : iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF ;
    public final EObject entryRuleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentConstantOrAttribute = null;


        try {
            // InternalCQLParser.g:1272:79: (iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF )
            // InternalCQLParser.g:1273:2: iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF
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
    // InternalCQLParser.g:1279:1: ruleExpressionComponentConstantOrAttribute returns [EObject current=null] : ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) ;
    public final EObject ruleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1285:2: ( ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) )
            // InternalCQLParser.g:1286:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            {
            // InternalCQLParser.g:1286:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==FALSE||LA25_0==TRUE||(LA25_0>=RULE_INT && LA25_0<=RULE_STRING)) ) {
                alt25=1;
            }
            else if ( (LA25_0==RULE_ID) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // InternalCQLParser.g:1287:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1287:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1288:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1288:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1289:5: lv_value_0_0= ruleAtomicWithoutAttributeRef
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
                    // InternalCQLParser.g:1307:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQLParser.g:1307:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQLParser.g:1308:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQLParser.g:1308:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQLParser.g:1309:5: lv_value_1_0= ruleAttributeWithoutAliasDefinition
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
    // InternalCQLParser.g:1330:1: entryRuleExpressionComponentMapperOrConstant returns [EObject current=null] : iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF ;
    public final EObject entryRuleExpressionComponentMapperOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentMapperOrConstant = null;


        try {
            // InternalCQLParser.g:1330:76: (iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF )
            // InternalCQLParser.g:1331:2: iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF
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
    // InternalCQLParser.g:1337:1: ruleExpressionComponentMapperOrConstant returns [EObject current=null] : ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) ;
    public final EObject ruleExpressionComponentMapperOrConstant() throws RecognitionException {
        EObject current = null;

        EObject this_Mapper_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1343:2: ( ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQLParser.g:1344:2: ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQLParser.g:1344:2: ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==DolToEur) ) {
                alt26=1;
            }
            else if ( (LA26_0==FALSE||LA26_0==TRUE||(LA26_0>=RULE_INT && LA26_0<=RULE_STRING)) ) {
                alt26=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // InternalCQLParser.g:1345:3: (this_Mapper_0= ruleMapper () )
                    {
                    // InternalCQLParser.g:1345:3: (this_Mapper_0= ruleMapper () )
                    // InternalCQLParser.g:1346:4: this_Mapper_0= ruleMapper ()
                    {

                    				newCompositeNode(grammarAccess.getExpressionComponentMapperOrConstantAccess().getMapperParserRuleCall_0_0());
                    			
                    pushFollow(FOLLOW_2);
                    this_Mapper_0=ruleMapper();

                    state._fsp--;


                    				current = this_Mapper_0;
                    				afterParserOrEnumRuleCall();
                    			
                    // InternalCQLParser.g:1354:4: ()
                    // InternalCQLParser.g:1355:5: 
                    {

                    					current = forceCreateModelElementAndSet(
                    						grammarAccess.getExpressionComponentMapperOrConstantAccess().getExpressionComponentValueAction_0_1(),
                    						current);
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1363:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQLParser.g:1363:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQLParser.g:1364:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQLParser.g:1364:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQLParser.g:1365:5: lv_value_2_0= ruleAtomicWithoutAttributeRef
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
    // InternalCQLParser.g:1386:1: entryRuleExpressionComponentOnlyAttribute returns [EObject current=null] : iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF ;
    public final EObject entryRuleExpressionComponentOnlyAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlyAttribute = null;


        try {
            // InternalCQLParser.g:1386:73: (iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF )
            // InternalCQLParser.g:1387:2: iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF
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
    // InternalCQLParser.g:1393:1: ruleExpressionComponentOnlyAttribute returns [EObject current=null] : ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) ;
    public final EObject ruleExpressionComponentOnlyAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1399:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) )
            // InternalCQLParser.g:1400:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            {
            // InternalCQLParser.g:1400:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQLParser.g:1401:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1401:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQLParser.g:1402:4: lv_value_0_0= ruleAttributeWithoutAliasDefinition
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
    // InternalCQLParser.g:1422:1: entryRuleExpressionComponentOnlymapper returns [EObject current=null] : iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF ;
    public final EObject entryRuleExpressionComponentOnlymapper() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlymapper = null;


        try {
            // InternalCQLParser.g:1422:70: (iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF )
            // InternalCQLParser.g:1423:2: iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF
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
    // InternalCQLParser.g:1429:1: ruleExpressionComponentOnlymapper returns [EObject current=null] : (this_Mapper_0= ruleMapper () ) ;
    public final EObject ruleExpressionComponentOnlymapper() throws RecognitionException {
        EObject current = null;

        EObject this_Mapper_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1435:2: ( (this_Mapper_0= ruleMapper () ) )
            // InternalCQLParser.g:1436:2: (this_Mapper_0= ruleMapper () )
            {
            // InternalCQLParser.g:1436:2: (this_Mapper_0= ruleMapper () )
            // InternalCQLParser.g:1437:3: this_Mapper_0= ruleMapper ()
            {

            			newCompositeNode(grammarAccess.getExpressionComponentOnlymapperAccess().getMapperParserRuleCall_0());
            		
            pushFollow(FOLLOW_2);
            this_Mapper_0=ruleMapper();

            state._fsp--;


            			current = this_Mapper_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1445:3: ()
            // InternalCQLParser.g:1446:4: 
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
    // InternalCQLParser.g:1456:1: entryRuleMapper returns [EObject current=null] : iv_ruleMapper= ruleMapper EOF ;
    public final EObject entryRuleMapper() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapper = null;


        try {
            // InternalCQLParser.g:1456:47: (iv_ruleMapper= ruleMapper EOF )
            // InternalCQLParser.g:1457:2: iv_ruleMapper= ruleMapper EOF
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
    // InternalCQLParser.g:1463:1: ruleMapper returns [EObject current=null] : ( () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) ;
    public final EObject ruleMapper() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_value_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:1469:2: ( ( () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis ) )
            // InternalCQLParser.g:1470:2: ( () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            {
            // InternalCQLParser.g:1470:2: ( () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis )
            // InternalCQLParser.g:1471:3: () ( (lv_name_1_0= DolToEur ) ) otherlv_2= LeftParenthesis ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= RightParenthesis
            {
            // InternalCQLParser.g:1471:3: ()
            // InternalCQLParser.g:1472:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getMapperAccess().getMapperAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:1478:3: ( (lv_name_1_0= DolToEur ) )
            // InternalCQLParser.g:1479:4: (lv_name_1_0= DolToEur )
            {
            // InternalCQLParser.g:1479:4: (lv_name_1_0= DolToEur )
            // InternalCQLParser.g:1480:5: lv_name_1_0= DolToEur
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
            		
            // InternalCQLParser.g:1496:3: ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
            // InternalCQLParser.g:1497:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            {
            // InternalCQLParser.g:1497:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            // InternalCQLParser.g:1498:5: lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getMapperAccess().getValueSelectExpressionWithoutAliasDefinitionParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_19);
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
    // InternalCQLParser.g:1523:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQLParser.g:1523:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQLParser.g:1524:2: iv_ruleSelectExpression= ruleSelectExpression EOF
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
    // InternalCQLParser.g:1530:1: ruleSelectExpression returns [EObject current=null] : ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
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
            // InternalCQLParser.g:1536:2: ( ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQLParser.g:1537:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQLParser.g:1537:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQLParser.g:1538:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQLParser.g:1538:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==DolToEur||LA33_0==FALSE||LA33_0==TRUE||(LA33_0>=RULE_INT && LA33_0<=RULE_STRING)) ) {
                alt33=1;
            }
            else if ( (LA33_0==RULE_ID) ) {
                alt33=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;
            }
            switch (alt33) {
                case 1 :
                    // InternalCQLParser.g:1539:4: ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    {
                    // InternalCQLParser.g:1539:4: ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    // InternalCQLParser.g:1540:5: ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    {
                    // InternalCQLParser.g:1540:5: ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) )
                    // InternalCQLParser.g:1541:6: (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant )
                    {
                    // InternalCQLParser.g:1541:6: (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant )
                    // InternalCQLParser.g:1542:7: lv_expressions_0_0= ruleExpressionComponentMapperOrConstant
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

                    // InternalCQLParser.g:1559:5: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    loop29:
                    do {
                        int alt29=2;
                        int LA29_0 = input.LA(1);

                        if ( ((LA29_0>=Asterisk && LA29_0<=PlusSign)||LA29_0==HyphenMinus||LA29_0==Solidus) ) {
                            alt29=1;
                        }


                        switch (alt29) {
                    	case 1 :
                    	    // InternalCQLParser.g:1560:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1560:6: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1561:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1561:7: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
                    	    // InternalCQLParser.g:1562:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1562:8: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
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
                    	            // InternalCQLParser.g:1563:9: lv_operators_1_1= PlusSign
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
                    	            // InternalCQLParser.g:1574:9: lv_operators_1_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1585:9: lv_operators_1_3= Asterisk
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
                    	            // InternalCQLParser.g:1596:9: lv_operators_1_4= Solidus
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

                    	    // InternalCQLParser.g:1609:6: ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1610:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1610:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1611:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1611:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper )
                    	    int alt28=2;
                    	    int LA28_0 = input.LA(1);

                    	    if ( (LA28_0==FALSE||LA28_0==TRUE||(LA28_0>=RULE_ID && LA28_0<=RULE_STRING)) ) {
                    	        alt28=1;
                    	    }
                    	    else if ( (LA28_0==DolToEur) ) {
                    	        alt28=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 28, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt28) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1612:9: lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQLParser.g:1628:9: lv_expressions_2_2= ruleExpressionComponentOnlymapper
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
                    	    break loop29;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1649:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1649:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    // InternalCQLParser.g:1650:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    {
                    // InternalCQLParser.g:1650:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) )
                    // InternalCQLParser.g:1651:6: (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute )
                    {
                    // InternalCQLParser.g:1651:6: (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute )
                    // InternalCQLParser.g:1652:7: lv_expressions_3_0= ruleExpressionComponentOnlyAttribute
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

                    // InternalCQLParser.g:1669:5: ( ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    int cnt32=0;
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( ((LA32_0>=Asterisk && LA32_0<=PlusSign)||LA32_0==HyphenMinus||LA32_0==Solidus) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // InternalCQLParser.g:1670:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1670:6: ( ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1671:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1671:7: ( (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus ) )
                    	    // InternalCQLParser.g:1672:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1672:8: (lv_operators_4_1= PlusSign | lv_operators_4_2= HyphenMinus | lv_operators_4_3= Asterisk | lv_operators_4_4= Solidus )
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
                    	            // InternalCQLParser.g:1673:9: lv_operators_4_1= PlusSign
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
                    	            // InternalCQLParser.g:1684:9: lv_operators_4_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1695:9: lv_operators_4_3= Asterisk
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
                    	            // InternalCQLParser.g:1706:9: lv_operators_4_4= Solidus
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

                    	    // InternalCQLParser.g:1719:6: ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1720:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1720:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1721:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1721:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper )
                    	    int alt31=2;
                    	    int LA31_0 = input.LA(1);

                    	    if ( (LA31_0==FALSE||LA31_0==TRUE||(LA31_0>=RULE_ID && LA31_0<=RULE_STRING)) ) {
                    	        alt31=1;
                    	    }
                    	    else if ( (LA31_0==DolToEur) ) {
                    	        alt31=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 31, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt31) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1722:9: lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQLParser.g:1738:9: lv_expressions_5_2= ruleExpressionComponentOnlymapper
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
                    	    if ( cnt32 >= 1 ) break loop32;
                                EarlyExitException eee =
                                    new EarlyExitException(32, input);
                                throw eee;
                        }
                        cnt32++;
                    } while (true);


                    }


                    }
                    break;

            }

            // InternalCQLParser.g:1759:3: (otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==AS) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // InternalCQLParser.g:1760:4: otherlv_6= AS ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,AS,FOLLOW_15); 

                    				newLeafNode(otherlv_6, grammarAccess.getSelectExpressionAccess().getASKeyword_1_0());
                    			
                    // InternalCQLParser.g:1764:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQLParser.g:1765:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQLParser.g:1765:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQLParser.g:1766:6: lv_alias_7_0= ruleAlias
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
    // InternalCQLParser.g:1788:1: entryRuleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF ;
    public final EObject entryRuleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithoutAliasDefinition = null;


        try {
            // InternalCQLParser.g:1788:79: (iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF )
            // InternalCQLParser.g:1789:2: iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF
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
    // InternalCQLParser.g:1795:1: ruleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) ) ;
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
            // InternalCQLParser.g:1801:2: ( (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) ) )
            // InternalCQLParser.g:1802:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) )
            {
            // InternalCQLParser.g:1802:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) )
            // InternalCQLParser.g:1803:3: this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            {

            			newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getSelectExpressionWithOnlyAttributeOrConstantParserRuleCall_0());
            		
            pushFollow(FOLLOW_5);
            this_SelectExpressionWithOnlyAttributeOrConstant_0=ruleSelectExpressionWithOnlyAttributeOrConstant();

            state._fsp--;


            			current = this_SelectExpressionWithOnlyAttributeOrConstant_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:1811:3: ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==DolToEur||LA41_0==FALSE||LA41_0==TRUE||(LA41_0>=RULE_INT && LA41_0<=RULE_STRING)) ) {
                alt41=1;
            }
            else if ( (LA41_0==RULE_ID) ) {
                alt41=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;
            }
            switch (alt41) {
                case 1 :
                    // InternalCQLParser.g:1812:4: ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    {
                    // InternalCQLParser.g:1812:4: ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    // InternalCQLParser.g:1813:5: ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    {
                    // InternalCQLParser.g:1813:5: ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) )
                    // InternalCQLParser.g:1814:6: (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant )
                    {
                    // InternalCQLParser.g:1814:6: (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant )
                    // InternalCQLParser.g:1815:7: lv_expressions_1_0= ruleExpressionComponentMapperOrConstant
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

                    // InternalCQLParser.g:1832:5: ( ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    loop37:
                    do {
                        int alt37=2;
                        int LA37_0 = input.LA(1);

                        if ( ((LA37_0>=Asterisk && LA37_0<=PlusSign)||LA37_0==HyphenMinus||LA37_0==Solidus) ) {
                            alt37=1;
                        }


                        switch (alt37) {
                    	case 1 :
                    	    // InternalCQLParser.g:1833:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1833:6: ( ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1834:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1834:7: ( (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus ) )
                    	    // InternalCQLParser.g:1835:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1835:8: (lv_operators_2_1= PlusSign | lv_operators_2_2= HyphenMinus | lv_operators_2_3= Asterisk | lv_operators_2_4= Solidus )
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
                    	            // InternalCQLParser.g:1836:9: lv_operators_2_1= PlusSign
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
                    	            // InternalCQLParser.g:1847:9: lv_operators_2_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1858:9: lv_operators_2_3= Asterisk
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
                    	            // InternalCQLParser.g:1869:9: lv_operators_2_4= Solidus
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

                    	    // InternalCQLParser.g:1882:6: ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1883:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1883:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1884:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1884:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper )
                    	    int alt36=2;
                    	    int LA36_0 = input.LA(1);

                    	    if ( (LA36_0==FALSE||LA36_0==TRUE||(LA36_0>=RULE_ID && LA36_0<=RULE_STRING)) ) {
                    	        alt36=1;
                    	    }
                    	    else if ( (LA36_0==DolToEur) ) {
                    	        alt36=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 36, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt36) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1885:9: lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQLParser.g:1901:9: lv_expressions_3_2= ruleExpressionComponentOnlymapper
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
                    	    break loop37;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:1922:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    {
                    // InternalCQLParser.g:1922:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    // InternalCQLParser.g:1923:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    {
                    // InternalCQLParser.g:1923:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) )
                    // InternalCQLParser.g:1924:6: (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute )
                    {
                    // InternalCQLParser.g:1924:6: (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute )
                    // InternalCQLParser.g:1925:7: lv_expressions_4_0= ruleExpressionComponentOnlyAttribute
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

                    // InternalCQLParser.g:1942:5: ( ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    int cnt40=0;
                    loop40:
                    do {
                        int alt40=2;
                        int LA40_0 = input.LA(1);

                        if ( ((LA40_0>=Asterisk && LA40_0<=PlusSign)||LA40_0==HyphenMinus||LA40_0==Solidus) ) {
                            alt40=1;
                        }


                        switch (alt40) {
                    	case 1 :
                    	    // InternalCQLParser.g:1943:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQLParser.g:1943:6: ( ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) ) )
                    	    // InternalCQLParser.g:1944:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    {
                    	    // InternalCQLParser.g:1944:7: ( (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus ) )
                    	    // InternalCQLParser.g:1945:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
                    	    {
                    	    // InternalCQLParser.g:1945:8: (lv_operators_5_1= PlusSign | lv_operators_5_2= HyphenMinus | lv_operators_5_3= Asterisk | lv_operators_5_4= Solidus )
                    	    int alt38=4;
                    	    switch ( input.LA(1) ) {
                    	    case PlusSign:
                    	        {
                    	        alt38=1;
                    	        }
                    	        break;
                    	    case HyphenMinus:
                    	        {
                    	        alt38=2;
                    	        }
                    	        break;
                    	    case Asterisk:
                    	        {
                    	        alt38=3;
                    	        }
                    	        break;
                    	    case Solidus:
                    	        {
                    	        alt38=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 38, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt38) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1946:9: lv_operators_5_1= PlusSign
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
                    	            // InternalCQLParser.g:1957:9: lv_operators_5_2= HyphenMinus
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
                    	            // InternalCQLParser.g:1968:9: lv_operators_5_3= Asterisk
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
                    	            // InternalCQLParser.g:1979:9: lv_operators_5_4= Solidus
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

                    	    // InternalCQLParser.g:1992:6: ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQLParser.g:1993:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQLParser.g:1993:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQLParser.g:1994:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQLParser.g:1994:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper )
                    	    int alt39=2;
                    	    int LA39_0 = input.LA(1);

                    	    if ( (LA39_0==FALSE||LA39_0==TRUE||(LA39_0>=RULE_ID && LA39_0<=RULE_STRING)) ) {
                    	        alt39=1;
                    	    }
                    	    else if ( (LA39_0==DolToEur) ) {
                    	        alt39=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 39, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt39) {
                    	        case 1 :
                    	            // InternalCQLParser.g:1995:9: lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQLParser.g:2011:9: lv_expressions_6_2= ruleExpressionComponentOnlymapper
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
                    	    if ( cnt40 >= 1 ) break loop40;
                                EarlyExitException eee =
                                    new EarlyExitException(40, input);
                                throw eee;
                        }
                        cnt40++;
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
    // InternalCQLParser.g:2036:1: entryRuleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF ;
    public final EObject entryRuleSelectExpressionWithOnlyAttributeOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithOnlyAttributeOrConstant = null;


        try {
            // InternalCQLParser.g:2036:84: (iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF )
            // InternalCQLParser.g:2037:2: iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF
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
    // InternalCQLParser.g:2043:1: ruleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) ;
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
            // InternalCQLParser.g:2049:2: ( ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) )
            // InternalCQLParser.g:2050:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            {
            // InternalCQLParser.g:2050:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            // InternalCQLParser.g:2051:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            {
            // InternalCQLParser.g:2051:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) )
            // InternalCQLParser.g:2052:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            {
            // InternalCQLParser.g:2052:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            // InternalCQLParser.g:2053:5: lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute
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

            // InternalCQLParser.g:2070:3: ( ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( ((LA43_0>=Asterisk && LA43_0<=PlusSign)||LA43_0==HyphenMinus||LA43_0==Solidus) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalCQLParser.g:2071:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    {
            	    // InternalCQLParser.g:2071:4: ( ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) ) )
            	    // InternalCQLParser.g:2072:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    {
            	    // InternalCQLParser.g:2072:5: ( (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus ) )
            	    // InternalCQLParser.g:2073:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    {
            	    // InternalCQLParser.g:2073:6: (lv_operators_1_1= PlusSign | lv_operators_1_2= HyphenMinus | lv_operators_1_3= Asterisk | lv_operators_1_4= Solidus )
            	    int alt42=4;
            	    switch ( input.LA(1) ) {
            	    case PlusSign:
            	        {
            	        alt42=1;
            	        }
            	        break;
            	    case HyphenMinus:
            	        {
            	        alt42=2;
            	        }
            	        break;
            	    case Asterisk:
            	        {
            	        alt42=3;
            	        }
            	        break;
            	    case Solidus:
            	        {
            	        alt42=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 42, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt42) {
            	        case 1 :
            	            // InternalCQLParser.g:2074:7: lv_operators_1_1= PlusSign
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
            	            // InternalCQLParser.g:2085:7: lv_operators_1_2= HyphenMinus
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
            	            // InternalCQLParser.g:2096:7: lv_operators_1_3= Asterisk
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
            	            // InternalCQLParser.g:2107:7: lv_operators_1_4= Solidus
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

            	    // InternalCQLParser.g:2120:4: ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    // InternalCQLParser.g:2121:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    {
            	    // InternalCQLParser.g:2121:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    // InternalCQLParser.g:2122:6: lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute
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
            	    break loop43;
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
    // InternalCQLParser.g:2144:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQLParser.g:2144:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQLParser.g:2145:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQLParser.g:2151:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQLParser.g:2157:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQLParser.g:2158:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2158:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2159:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2159:3: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2160:4: lv_name_0_0= RULE_ID
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
    // InternalCQLParser.g:2179:1: entryRuleCreateParameters returns [EObject current=null] : iv_ruleCreateParameters= ruleCreateParameters EOF ;
    public final EObject entryRuleCreateParameters() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateParameters = null;


        try {
            // InternalCQLParser.g:2179:57: (iv_ruleCreateParameters= ruleCreateParameters EOF )
            // InternalCQLParser.g:2180:2: iv_ruleCreateParameters= ruleCreateParameters EOF
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
    // InternalCQLParser.g:2186:1: ruleCreateParameters returns [EObject current=null] : (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) ;
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
            // InternalCQLParser.g:2192:2: ( (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis ) )
            // InternalCQLParser.g:2193:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            {
            // InternalCQLParser.g:2193:2: (otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis )
            // InternalCQLParser.g:2194:3: otherlv_0= WRAPPER ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= PROTOCOL ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= TRANSPORT ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= DATAHANDLER ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= OPTIONS otherlv_9= LeftParenthesis ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= RightParenthesis
            {
            otherlv_0=(Token)match(input,WRAPPER,FOLLOW_32); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateParametersAccess().getWRAPPERKeyword_0());
            		
            // InternalCQLParser.g:2198:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQLParser.g:2199:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQLParser.g:2199:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQLParser.g:2200:5: lv_wrapper_1_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2220:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQLParser.g:2221:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQLParser.g:2221:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQLParser.g:2222:5: lv_protocol_3_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2242:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQLParser.g:2243:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQLParser.g:2243:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQLParser.g:2244:5: lv_transport_5_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2264:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQLParser.g:2265:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQLParser.g:2265:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQLParser.g:2266:5: lv_datahandler_7_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2290:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+
            int cnt44=0;
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==RULE_STRING) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // InternalCQLParser.g:2291:4: ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) )
            	    {
            	    // InternalCQLParser.g:2291:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2292:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2292:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQLParser.g:2293:6: lv_keys_10_0= RULE_STRING
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

            	    // InternalCQLParser.g:2309:4: ( (lv_values_11_0= RULE_STRING ) )
            	    // InternalCQLParser.g:2310:5: (lv_values_11_0= RULE_STRING )
            	    {
            	    // InternalCQLParser.g:2310:5: (lv_values_11_0= RULE_STRING )
            	    // InternalCQLParser.g:2311:6: lv_values_11_0= RULE_STRING
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
            	    if ( cnt44 >= 1 ) break loop44;
                        EarlyExitException eee =
                            new EarlyExitException(44, input);
                        throw eee;
                }
                cnt44++;
            } while (true);

            // InternalCQLParser.g:2328:3: (otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==Comma) ) {
                alt45=1;
            }
            switch (alt45) {
                case 1 :
                    // InternalCQLParser.g:2329:4: otherlv_12= Comma ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) )
                    {
                    otherlv_12=(Token)match(input,Comma,FOLLOW_32); 

                    				newLeafNode(otherlv_12, grammarAccess.getCreateParametersAccess().getCommaKeyword_11_0());
                    			
                    // InternalCQLParser.g:2333:4: ( (lv_keys_13_0= RULE_STRING ) )
                    // InternalCQLParser.g:2334:5: (lv_keys_13_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2334:5: (lv_keys_13_0= RULE_STRING )
                    // InternalCQLParser.g:2335:6: lv_keys_13_0= RULE_STRING
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

                    // InternalCQLParser.g:2351:4: ( (lv_values_14_0= RULE_STRING ) )
                    // InternalCQLParser.g:2352:5: (lv_values_14_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:2352:5: (lv_values_14_0= RULE_STRING )
                    // InternalCQLParser.g:2353:6: lv_values_14_0= RULE_STRING
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
    // InternalCQLParser.g:2378:1: entryRuleAttributeDefinition returns [EObject current=null] : iv_ruleAttributeDefinition= ruleAttributeDefinition EOF ;
    public final EObject entryRuleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeDefinition = null;


        try {
            // InternalCQLParser.g:2378:60: (iv_ruleAttributeDefinition= ruleAttributeDefinition EOF )
            // InternalCQLParser.g:2379:2: iv_ruleAttributeDefinition= ruleAttributeDefinition EOF
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
    // InternalCQLParser.g:2385:1: ruleAttributeDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis ) ;
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
            // InternalCQLParser.g:2391:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis ) )
            // InternalCQLParser.g:2392:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis )
            {
            // InternalCQLParser.g:2392:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis )
            // InternalCQLParser.g:2393:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= LeftParenthesis ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= RightParenthesis
            {
            // InternalCQLParser.g:2393:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQLParser.g:2394:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQLParser.g:2394:4: (lv_name_0_0= RULE_ID )
            // InternalCQLParser.g:2395:5: lv_name_0_0= RULE_ID
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
            		
            // InternalCQLParser.g:2415:3: ( (lv_attributes_2_0= ruleAttribute ) )+
            int cnt46=0;
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( (LA46_0==RULE_ID) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // InternalCQLParser.g:2416:4: (lv_attributes_2_0= ruleAttribute )
            	    {
            	    // InternalCQLParser.g:2416:4: (lv_attributes_2_0= ruleAttribute )
            	    // InternalCQLParser.g:2417:5: lv_attributes_2_0= ruleAttribute
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
            	    if ( cnt46 >= 1 ) break loop46;
                        EarlyExitException eee =
                            new EarlyExitException(46, input);
                        throw eee;
                }
                cnt46++;
            } while (true);

            // InternalCQLParser.g:2434:3: ( (lv_datatypes_3_0= ruleDataType ) )+
            int cnt47=0;
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( ((LA47_0>=STARTTIMESTAMP && LA47_0<=ENDTIMESTAMP)||LA47_0==BOOLEAN||LA47_0==INTEGER||LA47_0==DOUBLE||LA47_0==STRING||LA47_0==FLOAT||LA47_0==LONG) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // InternalCQLParser.g:2435:4: (lv_datatypes_3_0= ruleDataType )
            	    {
            	    // InternalCQLParser.g:2435:4: (lv_datatypes_3_0= ruleDataType )
            	    // InternalCQLParser.g:2436:5: lv_datatypes_3_0= ruleDataType
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
            	    if ( cnt47 >= 1 ) break loop47;
                        EarlyExitException eee =
                            new EarlyExitException(47, input);
                        throw eee;
                }
                cnt47++;
            } while (true);

            // InternalCQLParser.g:2453:3: (otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==Comma) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // InternalCQLParser.g:2454:4: otherlv_4= Comma ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) )
            	    {
            	    otherlv_4=(Token)match(input,Comma,FOLLOW_15); 

            	    				newLeafNode(otherlv_4, grammarAccess.getAttributeDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQLParser.g:2458:4: ( (lv_attributes_5_0= ruleAttribute ) )
            	    // InternalCQLParser.g:2459:5: (lv_attributes_5_0= ruleAttribute )
            	    {
            	    // InternalCQLParser.g:2459:5: (lv_attributes_5_0= ruleAttribute )
            	    // InternalCQLParser.g:2460:6: lv_attributes_5_0= ruleAttribute
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

            	    // InternalCQLParser.g:2477:4: ( (lv_datatypes_6_0= ruleDataType ) )
            	    // InternalCQLParser.g:2478:5: (lv_datatypes_6_0= ruleDataType )
            	    {
            	    // InternalCQLParser.g:2478:5: (lv_datatypes_6_0= ruleDataType )
            	    // InternalCQLParser.g:2479:6: lv_datatypes_6_0= ruleDataType
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
            	    break loop48;
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
    // InternalCQLParser.g:2505:1: entryRuleCreateStream1 returns [EObject current=null] : iv_ruleCreateStream1= ruleCreateStream1 EOF ;
    public final EObject entryRuleCreateStream1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStream1 = null;


        try {
            // InternalCQLParser.g:2505:54: (iv_ruleCreateStream1= ruleCreateStream1 EOF )
            // InternalCQLParser.g:2506:2: iv_ruleCreateStream1= ruleCreateStream1 EOF
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
    // InternalCQLParser.g:2512:1: ruleCreateStream1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateStream1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2518:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2519:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2519:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2520:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2520:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2521:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2521:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2522:5: lv_keyword_0_0= ruleCreateKeyword
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
            		
            // InternalCQLParser.g:2543:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2544:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2544:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2545:5: lv_attributes_2_0= ruleAttributeDefinition
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

            // InternalCQLParser.g:2562:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2563:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2563:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2564:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQLParser.g:2585:1: entryRuleCreateSink1 returns [EObject current=null] : iv_ruleCreateSink1= ruleCreateSink1 EOF ;
    public final EObject entryRuleCreateSink1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateSink1 = null;


        try {
            // InternalCQLParser.g:2585:52: (iv_ruleCreateSink1= ruleCreateSink1 EOF )
            // InternalCQLParser.g:2586:2: iv_ruleCreateSink1= ruleCreateSink1 EOF
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
    // InternalCQLParser.g:2592:1: ruleCreateSink1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateSink1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2598:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQLParser.g:2599:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQLParser.g:2599:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQLParser.g:2600:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= SINK ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQLParser.g:2600:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2601:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2601:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2602:5: lv_keyword_0_0= ruleCreateKeyword
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
            		
            // InternalCQLParser.g:2623:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2624:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2624:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2625:5: lv_attributes_2_0= ruleAttributeDefinition
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

            // InternalCQLParser.g:2642:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQLParser.g:2643:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQLParser.g:2643:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQLParser.g:2644:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQLParser.g:2665:1: entryRuleCreateStreamChannel returns [EObject current=null] : iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF ;
    public final EObject entryRuleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamChannel = null;


        try {
            // InternalCQLParser.g:2665:60: (iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF )
            // InternalCQLParser.g:2666:2: iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF
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
    // InternalCQLParser.g:2672:1: ruleCreateStreamChannel returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) ;
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
            // InternalCQLParser.g:2678:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) ) )
            // InternalCQLParser.g:2679:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            {
            // InternalCQLParser.g:2679:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) ) )
            // InternalCQLParser.g:2680:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= CHANNEL ( (lv_host_4_0= RULE_ID ) ) otherlv_5= Colon ( (lv_port_6_0= RULE_INT ) )
            {
            // InternalCQLParser.g:2680:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2681:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2681:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2682:5: lv_keyword_0_0= ruleCreateKeyword
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
            		
            // InternalCQLParser.g:2703:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2704:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2704:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2705:5: lv_attributes_2_0= ruleAttributeDefinition
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
            		
            // InternalCQLParser.g:2726:3: ( (lv_host_4_0= RULE_ID ) )
            // InternalCQLParser.g:2727:4: (lv_host_4_0= RULE_ID )
            {
            // InternalCQLParser.g:2727:4: (lv_host_4_0= RULE_ID )
            // InternalCQLParser.g:2728:5: lv_host_4_0= RULE_ID
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
            		
            // InternalCQLParser.g:2748:3: ( (lv_port_6_0= RULE_INT ) )
            // InternalCQLParser.g:2749:4: (lv_port_6_0= RULE_INT )
            {
            // InternalCQLParser.g:2749:4: (lv_port_6_0= RULE_INT )
            // InternalCQLParser.g:2750:5: lv_port_6_0= RULE_INT
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
    // InternalCQLParser.g:2770:1: entryRuleCreateStreamFile returns [EObject current=null] : iv_ruleCreateStreamFile= ruleCreateStreamFile EOF ;
    public final EObject entryRuleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamFile = null;


        try {
            // InternalCQLParser.g:2770:57: (iv_ruleCreateStreamFile= ruleCreateStreamFile EOF )
            // InternalCQLParser.g:2771:2: iv_ruleCreateStreamFile= ruleCreateStreamFile EOF
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
    // InternalCQLParser.g:2777:1: ruleCreateStreamFile returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) ;
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
            // InternalCQLParser.g:2783:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2784:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            {
            // InternalCQLParser.g:2784:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) ) )
            // InternalCQLParser.g:2785:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= STREAM ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= FILE ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= AS ( (lv_type_6_0= RULE_ID ) )
            {
            // InternalCQLParser.g:2785:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQLParser.g:2786:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQLParser.g:2786:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQLParser.g:2787:5: lv_keyword_0_0= ruleCreateKeyword
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
            		
            // InternalCQLParser.g:2808:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQLParser.g:2809:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQLParser.g:2809:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQLParser.g:2810:5: lv_attributes_2_0= ruleAttributeDefinition
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
            		
            // InternalCQLParser.g:2831:3: ( (lv_filename_4_0= RULE_STRING ) )
            // InternalCQLParser.g:2832:4: (lv_filename_4_0= RULE_STRING )
            {
            // InternalCQLParser.g:2832:4: (lv_filename_4_0= RULE_STRING )
            // InternalCQLParser.g:2833:5: lv_filename_4_0= RULE_STRING
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
            		
            // InternalCQLParser.g:2853:3: ( (lv_type_6_0= RULE_ID ) )
            // InternalCQLParser.g:2854:4: (lv_type_6_0= RULE_ID )
            {
            // InternalCQLParser.g:2854:4: (lv_type_6_0= RULE_ID )
            // InternalCQLParser.g:2855:5: lv_type_6_0= RULE_ID
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
    // InternalCQLParser.g:2875:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQLParser.g:2875:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQLParser.g:2876:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQLParser.g:2882:1: ruleCreateView returns [EObject current=null] : (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2888:2: ( (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) ) )
            // InternalCQLParser.g:2889:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            {
            // InternalCQLParser.g:2889:2: (otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) ) )
            // InternalCQLParser.g:2890:3: otherlv_0= VIEW ( (lv_name_1_0= RULE_ID ) ) otherlv_2= FROM ( (lv_select_3_0= ruleNestedStatement ) )
            {
            otherlv_0=(Token)match(input,VIEW,FOLLOW_15); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateViewAccess().getVIEWKeyword_0());
            		
            // InternalCQLParser.g:2894:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQLParser.g:2895:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQLParser.g:2895:4: (lv_name_1_0= RULE_ID )
            // InternalCQLParser.g:2896:5: lv_name_1_0= RULE_ID
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
            		
            // InternalCQLParser.g:2916:3: ( (lv_select_3_0= ruleNestedStatement ) )
            // InternalCQLParser.g:2917:4: (lv_select_3_0= ruleNestedStatement )
            {
            // InternalCQLParser.g:2917:4: (lv_select_3_0= ruleNestedStatement )
            // InternalCQLParser.g:2918:5: lv_select_3_0= ruleNestedStatement
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
    // InternalCQLParser.g:2939:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQLParser.g:2939:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQLParser.g:2940:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQLParser.g:2946:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:2952:2: ( (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQLParser.g:2953:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQLParser.g:2953:2: (otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQLParser.g:2954:3: otherlv_0= STREAM otherlv_1= TO ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,STREAM,FOLLOW_49); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,TO,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQLParser.g:2962:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQLParser.g:2963:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQLParser.g:2963:4: (lv_name_2_0= RULE_ID )
            // InternalCQLParser.g:2964:5: lv_name_2_0= RULE_ID
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

            // InternalCQLParser.g:2980:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==SELECT) ) {
                alt49=1;
            }
            else if ( (LA49_0==RULE_ID) ) {
                alt49=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;
            }
            switch (alt49) {
                case 1 :
                    // InternalCQLParser.g:2981:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQLParser.g:2981:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQLParser.g:2982:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQLParser.g:2982:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQLParser.g:2983:6: lv_statement_3_0= ruleSelect
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
                    // InternalCQLParser.g:3001:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQLParser.g:3001:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQLParser.g:3002:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3002:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQLParser.g:3003:6: lv_inputname_4_0= RULE_ID
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
    // InternalCQLParser.g:3024:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQLParser.g:3024:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQLParser.g:3025:2: iv_ruleDrop= ruleDrop EOF
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
    // InternalCQLParser.g:3031:1: ruleDrop returns [EObject current=null] : ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? ) ;
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
            // InternalCQLParser.g:3037:2: ( ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? ) )
            // InternalCQLParser.g:3038:2: ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? )
            {
            // InternalCQLParser.g:3038:2: ( ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )? )
            // InternalCQLParser.g:3039:3: ( (lv_keyword1_0_0= DROP ) ) ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )?
            {
            // InternalCQLParser.g:3039:3: ( (lv_keyword1_0_0= DROP ) )
            // InternalCQLParser.g:3040:4: (lv_keyword1_0_0= DROP )
            {
            // InternalCQLParser.g:3040:4: (lv_keyword1_0_0= DROP )
            // InternalCQLParser.g:3041:5: lv_keyword1_0_0= DROP
            {
            lv_keyword1_0_0=(Token)match(input,DROP,FOLLOW_51); 

            					newLeafNode(lv_keyword1_0_0, grammarAccess.getDropAccess().getKeyword1DROPKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(current, "keyword1", lv_keyword1_0_0, "DROP");
            				

            }


            }

            // InternalCQLParser.g:3053:3: ( ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) ) )
            // InternalCQLParser.g:3054:4: ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) )
            {
            // InternalCQLParser.g:3054:4: ( (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM ) )
            // InternalCQLParser.g:3055:5: (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM )
            {
            // InternalCQLParser.g:3055:5: (lv_keyword2_1_1= SINK | lv_keyword2_1_2= STREAM )
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==SINK) ) {
                alt50=1;
            }
            else if ( (LA50_0==STREAM) ) {
                alt50=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }
            switch (alt50) {
                case 1 :
                    // InternalCQLParser.g:3056:6: lv_keyword2_1_1= SINK
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
                    // InternalCQLParser.g:3067:6: lv_keyword2_1_2= STREAM
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

            // InternalCQLParser.g:3080:3: ( (lv_value1_2_0= RULE_ID ) )
            // InternalCQLParser.g:3081:4: (lv_value1_2_0= RULE_ID )
            {
            // InternalCQLParser.g:3081:4: (lv_value1_2_0= RULE_ID )
            // InternalCQLParser.g:3082:5: lv_value1_2_0= RULE_ID
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

            // InternalCQLParser.g:3098:3: ( ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==IF) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // InternalCQLParser.g:3099:4: ( (lv_keyword3_3_0= IF ) ) otherlv_4= EXISTS
                    {
                    // InternalCQLParser.g:3099:4: ( (lv_keyword3_3_0= IF ) )
                    // InternalCQLParser.g:3100:5: (lv_keyword3_3_0= IF )
                    {
                    // InternalCQLParser.g:3100:5: (lv_keyword3_3_0= IF )
                    // InternalCQLParser.g:3101:6: lv_keyword3_3_0= IF
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
    // InternalCQLParser.g:3122:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQLParser.g:3122:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQLParser.g:3123:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQLParser.g:3129:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= UNBOUNDED ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQLParser.g:3135:2: (kw= UNBOUNDED )
            // InternalCQLParser.g:3136:2: kw= UNBOUNDED
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
    // InternalCQLParser.g:3144:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQLParser.g:3144:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQLParser.g:3145:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQLParser.g:3151:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) ;
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
            // InternalCQLParser.g:3157:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME ) )
            // InternalCQLParser.g:3158:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            {
            // InternalCQLParser.g:3158:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME )
            // InternalCQLParser.g:3159:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= TIME
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_47); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:3163:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:3164:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:3164:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:3165:5: lv_size_1_0= RULE_INT
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

            // InternalCQLParser.g:3181:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQLParser.g:3182:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQLParser.g:3182:4: (lv_unit_2_0= RULE_ID )
            // InternalCQLParser.g:3183:5: lv_unit_2_0= RULE_ID
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

            // InternalCQLParser.g:3199:3: (otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==ADVANCE) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // InternalCQLParser.g:3200:4: otherlv_3= ADVANCE ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,ADVANCE,FOLLOW_47); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQLParser.g:3204:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQLParser.g:3205:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3205:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQLParser.g:3206:6: lv_advance_size_4_0= RULE_INT
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

                    // InternalCQLParser.g:3222:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQLParser.g:3223:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQLParser.g:3223:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQLParser.g:3224:6: lv_advance_unit_5_0= RULE_ID
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
    // InternalCQLParser.g:3249:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQLParser.g:3249:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQLParser.g:3250:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQLParser.g:3256:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQLParser.g:3262:2: ( (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQLParser.g:3263:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQLParser.g:3263:2: (otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQLParser.g:3264:3: otherlv_0= SIZE ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= TUPLE (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,SIZE,FOLLOW_47); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQLParser.g:3268:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQLParser.g:3269:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQLParser.g:3269:4: (lv_size_1_0= RULE_INT )
            // InternalCQLParser.g:3270:5: lv_size_1_0= RULE_INT
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

            // InternalCQLParser.g:3286:3: (otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==ADVANCE) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // InternalCQLParser.g:3287:4: otherlv_2= ADVANCE ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,ADVANCE,FOLLOW_47); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQLParser.g:3291:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQLParser.g:3292:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQLParser.g:3292:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQLParser.g:3293:6: lv_advance_size_3_0= RULE_INT
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
            		
            // InternalCQLParser.g:3314:3: (otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==PARTITION) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // InternalCQLParser.g:3315:4: otherlv_5= PARTITION otherlv_6= BY ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,PARTITION,FOLLOW_14); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,BY,FOLLOW_15); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQLParser.g:3323:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQLParser.g:3324:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQLParser.g:3324:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQLParser.g:3325:6: lv_partition_attribute_7_0= ruleAttribute
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
    // InternalCQLParser.g:3347:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQLParser.g:3347:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQLParser.g:3348:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQLParser.g:3354:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3360:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQLParser.g:3361:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQLParser.g:3361:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQLParser.g:3362:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQLParser.g:3362:3: ()
            // InternalCQLParser.g:3363:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQLParser.g:3369:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQLParser.g:3370:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQLParser.g:3370:4: (lv_elements_1_0= ruleExpression )
            // InternalCQLParser.g:3371:5: lv_elements_1_0= ruleExpression
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
    // InternalCQLParser.g:3392:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQLParser.g:3392:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQLParser.g:3393:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQLParser.g:3399:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3405:2: (this_Or_0= ruleOr )
            // InternalCQLParser.g:3406:2: this_Or_0= ruleOr
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
    // InternalCQLParser.g:3417:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQLParser.g:3417:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQLParser.g:3418:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQLParser.g:3424:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3430:2: ( (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQLParser.g:3431:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQLParser.g:3431:2: (this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQLParser.g:3432:3: this_And_0= ruleAnd ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_59);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3440:3: ( () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) ) )*
            loop55:
            do {
                int alt55=2;
                int LA55_0 = input.LA(1);

                if ( (LA55_0==OR) ) {
                    alt55=1;
                }


                switch (alt55) {
            	case 1 :
            	    // InternalCQLParser.g:3441:4: () otherlv_2= OR ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQLParser.g:3441:4: ()
            	    // InternalCQLParser.g:3442:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,OR,FOLLOW_12); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:3452:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQLParser.g:3453:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQLParser.g:3453:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQLParser.g:3454:6: lv_right_3_0= ruleAnd
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
    // $ANTLR end "ruleOr"


    // $ANTLR start "entryRuleAnd"
    // InternalCQLParser.g:3476:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQLParser.g:3476:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQLParser.g:3477:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQLParser.g:3483:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3489:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQLParser.g:3490:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQLParser.g:3490:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQLParser.g:3491:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_60);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3499:3: ( () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop56:
            do {
                int alt56=2;
                int LA56_0 = input.LA(1);

                if ( (LA56_0==AND) ) {
                    alt56=1;
                }


                switch (alt56) {
            	case 1 :
            	    // InternalCQLParser.g:3500:4: () otherlv_2= AND ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQLParser.g:3500:4: ()
            	    // InternalCQLParser.g:3501:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,AND,FOLLOW_12); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQLParser.g:3511:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQLParser.g:3512:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQLParser.g:3512:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQLParser.g:3513:6: lv_right_3_0= ruleEqualitiy
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
            	    break loop56;
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
    // InternalCQLParser.g:3535:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQLParser.g:3535:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQLParser.g:3536:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQLParser.g:3542:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3548:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQLParser.g:3549:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQLParser.g:3549:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQLParser.g:3550:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_61);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3558:3: ( () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop58:
            do {
                int alt58=2;
                int LA58_0 = input.LA(1);

                if ( (LA58_0==ExclamationMarkEqualsSign||LA58_0==EqualsSign) ) {
                    alt58=1;
                }


                switch (alt58) {
            	case 1 :
            	    // InternalCQLParser.g:3559:4: () ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQLParser.g:3559:4: ()
            	    // InternalCQLParser.g:3560:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3566:4: ( ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) ) )
            	    // InternalCQLParser.g:3567:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    {
            	    // InternalCQLParser.g:3567:5: ( (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign ) )
            	    // InternalCQLParser.g:3568:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    {
            	    // InternalCQLParser.g:3568:6: (lv_op_2_1= EqualsSign | lv_op_2_2= ExclamationMarkEqualsSign )
            	    int alt57=2;
            	    int LA57_0 = input.LA(1);

            	    if ( (LA57_0==EqualsSign) ) {
            	        alt57=1;
            	    }
            	    else if ( (LA57_0==ExclamationMarkEqualsSign) ) {
            	        alt57=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 57, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt57) {
            	        case 1 :
            	            // InternalCQLParser.g:3569:7: lv_op_2_1= EqualsSign
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
            	            // InternalCQLParser.g:3580:7: lv_op_2_2= ExclamationMarkEqualsSign
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

            	    // InternalCQLParser.g:3593:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQLParser.g:3594:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQLParser.g:3594:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQLParser.g:3595:6: lv_right_3_0= ruleComparison
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
            	    break loop58;
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
    // InternalCQLParser.g:3617:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQLParser.g:3617:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQLParser.g:3618:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQLParser.g:3624:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQLParser.g:3630:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQLParser.g:3631:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQLParser.g:3631:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQLParser.g:3632:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_62);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3640:3: ( () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop60:
            do {
                int alt60=2;
                int LA60_0 = input.LA(1);

                if ( ((LA60_0>=LessThanSignEqualsSign && LA60_0<=GreaterThanSignEqualsSign)||LA60_0==LessThanSign||LA60_0==GreaterThanSign) ) {
                    alt60=1;
                }


                switch (alt60) {
            	case 1 :
            	    // InternalCQLParser.g:3641:4: () ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQLParser.g:3641:4: ()
            	    // InternalCQLParser.g:3642:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3648:4: ( ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) ) )
            	    // InternalCQLParser.g:3649:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    {
            	    // InternalCQLParser.g:3649:5: ( (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign ) )
            	    // InternalCQLParser.g:3650:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    {
            	    // InternalCQLParser.g:3650:6: (lv_op_2_1= GreaterThanSignEqualsSign | lv_op_2_2= LessThanSignEqualsSign | lv_op_2_3= LessThanSign | lv_op_2_4= GreaterThanSign )
            	    int alt59=4;
            	    switch ( input.LA(1) ) {
            	    case GreaterThanSignEqualsSign:
            	        {
            	        alt59=1;
            	        }
            	        break;
            	    case LessThanSignEqualsSign:
            	        {
            	        alt59=2;
            	        }
            	        break;
            	    case LessThanSign:
            	        {
            	        alt59=3;
            	        }
            	        break;
            	    case GreaterThanSign:
            	        {
            	        alt59=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 59, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt59) {
            	        case 1 :
            	            // InternalCQLParser.g:3651:7: lv_op_2_1= GreaterThanSignEqualsSign
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
            	            // InternalCQLParser.g:3662:7: lv_op_2_2= LessThanSignEqualsSign
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
            	            // InternalCQLParser.g:3673:7: lv_op_2_3= LessThanSign
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
            	            // InternalCQLParser.g:3684:7: lv_op_2_4= GreaterThanSign
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

            	    // InternalCQLParser.g:3697:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQLParser.g:3698:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQLParser.g:3698:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQLParser.g:3699:6: lv_right_3_0= rulePlusOrMinus
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
            	    break loop60;
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
    // InternalCQLParser.g:3721:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQLParser.g:3721:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQLParser.g:3722:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQLParser.g:3728:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3734:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQLParser.g:3735:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQLParser.g:3735:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQLParser.g:3736:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_63);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3744:3: ( ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop62:
            do {
                int alt62=2;
                int LA62_0 = input.LA(1);

                if ( (LA62_0==PlusSign||LA62_0==HyphenMinus) ) {
                    alt62=1;
                }


                switch (alt62) {
            	case 1 :
            	    // InternalCQLParser.g:3745:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQLParser.g:3745:4: ( ( () otherlv_2= PlusSign ) | ( () otherlv_4= HyphenMinus ) )
            	    int alt61=2;
            	    int LA61_0 = input.LA(1);

            	    if ( (LA61_0==PlusSign) ) {
            	        alt61=1;
            	    }
            	    else if ( (LA61_0==HyphenMinus) ) {
            	        alt61=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 61, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt61) {
            	        case 1 :
            	            // InternalCQLParser.g:3746:5: ( () otherlv_2= PlusSign )
            	            {
            	            // InternalCQLParser.g:3746:5: ( () otherlv_2= PlusSign )
            	            // InternalCQLParser.g:3747:6: () otherlv_2= PlusSign
            	            {
            	            // InternalCQLParser.g:3747:6: ()
            	            // InternalCQLParser.g:3748:7: 
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
            	            // InternalCQLParser.g:3760:5: ( () otherlv_4= HyphenMinus )
            	            {
            	            // InternalCQLParser.g:3760:5: ( () otherlv_4= HyphenMinus )
            	            // InternalCQLParser.g:3761:6: () otherlv_4= HyphenMinus
            	            {
            	            // InternalCQLParser.g:3761:6: ()
            	            // InternalCQLParser.g:3762:7: 
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

            	    // InternalCQLParser.g:3774:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQLParser.g:3775:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQLParser.g:3775:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQLParser.g:3776:6: lv_right_5_0= ruleMulOrDiv
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
            	    break loop62;
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
    // InternalCQLParser.g:3798:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQLParser.g:3798:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQLParser.g:3799:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQLParser.g:3805:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQLParser.g:3811:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQLParser.g:3812:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQLParser.g:3812:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQLParser.g:3813:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_64);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQLParser.g:3821:3: ( () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop64:
            do {
                int alt64=2;
                int LA64_0 = input.LA(1);

                if ( (LA64_0==Asterisk||LA64_0==Solidus) ) {
                    alt64=1;
                }


                switch (alt64) {
            	case 1 :
            	    // InternalCQLParser.g:3822:4: () ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQLParser.g:3822:4: ()
            	    // InternalCQLParser.g:3823:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQLParser.g:3829:4: ( ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) ) )
            	    // InternalCQLParser.g:3830:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    {
            	    // InternalCQLParser.g:3830:5: ( (lv_op_2_1= Asterisk | lv_op_2_2= Solidus ) )
            	    // InternalCQLParser.g:3831:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    {
            	    // InternalCQLParser.g:3831:6: (lv_op_2_1= Asterisk | lv_op_2_2= Solidus )
            	    int alt63=2;
            	    int LA63_0 = input.LA(1);

            	    if ( (LA63_0==Asterisk) ) {
            	        alt63=1;
            	    }
            	    else if ( (LA63_0==Solidus) ) {
            	        alt63=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 63, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt63) {
            	        case 1 :
            	            // InternalCQLParser.g:3832:7: lv_op_2_1= Asterisk
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
            	            // InternalCQLParser.g:3843:7: lv_op_2_2= Solidus
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

            	    // InternalCQLParser.g:3856:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQLParser.g:3857:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQLParser.g:3857:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQLParser.g:3858:6: lv_right_3_0= rulePrimary
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
            	    break loop64;
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
    // InternalCQLParser.g:3880:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQLParser.g:3880:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQLParser.g:3881:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQLParser.g:3887:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQLParser.g:3893:2: ( ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQLParser.g:3894:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQLParser.g:3894:2: ( ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis ) | ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt65=3;
            switch ( input.LA(1) ) {
            case LeftParenthesis:
                {
                alt65=1;
                }
                break;
            case NOT:
                {
                alt65=2;
                }
                break;
            case FALSE:
            case TRUE:
            case RULE_ID:
            case RULE_INT:
            case RULE_FLOAT:
            case RULE_STRING:
                {
                alt65=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 65, 0, input);

                throw nvae;
            }

            switch (alt65) {
                case 1 :
                    // InternalCQLParser.g:3895:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    {
                    // InternalCQLParser.g:3895:3: ( () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis )
                    // InternalCQLParser.g:3896:4: () otherlv_1= LeftParenthesis ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= RightParenthesis
                    {
                    // InternalCQLParser.g:3896:4: ()
                    // InternalCQLParser.g:3897:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,LeftParenthesis,FOLLOW_12); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQLParser.g:3907:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQLParser.g:3908:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQLParser.g:3908:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQLParser.g:3909:6: lv_inner_2_0= ruleExpression
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
                    // InternalCQLParser.g:3932:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQLParser.g:3932:3: ( () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQLParser.g:3933:4: () otherlv_5= NOT ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQLParser.g:3933:4: ()
                    // InternalCQLParser.g:3934:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,NOT,FOLLOW_12); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQLParser.g:3944:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQLParser.g:3945:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQLParser.g:3945:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQLParser.g:3946:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQLParser.g:3965:3: this_Atomic_7= ruleAtomic
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
    // InternalCQLParser.g:3977:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQLParser.g:3977:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQLParser.g:3978:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQLParser.g:3984:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
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
            // InternalCQLParser.g:3990:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQLParser.g:3991:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQLParser.g:3991:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt68=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt68=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt68=2;
                }
                break;
            case RULE_STRING:
                {
                alt68=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt68=4;
                }
                break;
            case RULE_ID:
                {
                alt68=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                throw nvae;
            }

            switch (alt68) {
                case 1 :
                    // InternalCQLParser.g:3992:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:3992:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:3993:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:3993:4: ()
                    // InternalCQLParser.g:3994:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4000:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:4001:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4001:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:4002:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:4020:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:4020:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:4021:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:4021:4: ()
                    // InternalCQLParser.g:4022:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4028:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:4029:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:4029:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:4030:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:4048:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:4048:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:4049:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:4049:4: ()
                    // InternalCQLParser.g:4050:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4056:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:4057:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4057:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:4058:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:4076:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:4076:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:4077:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:4077:4: ()
                    // InternalCQLParser.g:4078:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4084:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:4085:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:4085:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:4086:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:4086:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
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
                            // InternalCQLParser.g:4087:7: lv_value_7_1= TRUE
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
                            // InternalCQLParser.g:4098:7: lv_value_7_2= FALSE
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
                    // InternalCQLParser.g:4113:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQLParser.g:4113:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQLParser.g:4114:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQLParser.g:4114:4: ()
                    // InternalCQLParser.g:4115:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4121:4: ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt67=2;
                    int LA67_0 = input.LA(1);

                    if ( (LA67_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
                        case FullStop:
                            {
                            int LA67_2 = input.LA(3);

                            if ( (LA67_2==Asterisk) ) {
                                int LA67_5 = input.LA(4);

                                if ( (LA67_5==IN) ) {
                                    alt67=2;
                                }
                                else if ( (LA67_5==EOF||(LA67_5>=ATTACH && LA67_5<=CREATE)||LA67_5==HAVING||(LA67_5>=SELECT && LA67_5<=STREAM)||LA67_5==GROUP||LA67_5==DROP||(LA67_5>=VIEW && LA67_5<=AND)||(LA67_5>=ExclamationMarkEqualsSign && LA67_5<=GreaterThanSignEqualsSign)||LA67_5==OR||(LA67_5>=RightParenthesis && LA67_5<=PlusSign)||LA67_5==HyphenMinus||LA67_5==Solidus||(LA67_5>=Semicolon && LA67_5<=GreaterThanSign)) ) {
                                    alt67=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 67, 5, input);

                                    throw nvae;
                                }
                            }
                            else if ( (LA67_2==RULE_ID) ) {
                                int LA67_6 = input.LA(4);

                                if ( (LA67_6==IN) ) {
                                    alt67=2;
                                }
                                else if ( (LA67_6==EOF||(LA67_6>=ATTACH && LA67_6<=CREATE)||LA67_6==HAVING||(LA67_6>=SELECT && LA67_6<=STREAM)||LA67_6==GROUP||LA67_6==DROP||(LA67_6>=VIEW && LA67_6<=AND)||(LA67_6>=ExclamationMarkEqualsSign && LA67_6<=GreaterThanSignEqualsSign)||LA67_6==OR||(LA67_6>=RightParenthesis && LA67_6<=PlusSign)||LA67_6==HyphenMinus||LA67_6==Solidus||(LA67_6>=Semicolon && LA67_6<=GreaterThanSign)) ) {
                                    alt67=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 67, 6, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 67, 2, input);

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
                            alt67=1;
                            }
                            break;
                        case IN:
                            {
                            alt67=2;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 67, 1, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 67, 0, input);

                        throw nvae;
                    }
                    switch (alt67) {
                        case 1 :
                            // InternalCQLParser.g:4122:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            {
                            // InternalCQLParser.g:4122:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            // InternalCQLParser.g:4123:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            {
                            // InternalCQLParser.g:4123:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            // InternalCQLParser.g:4124:7: lv_value_9_0= ruleAttributeWithoutAliasDefinition
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
                            // InternalCQLParser.g:4142:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQLParser.g:4142:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQLParser.g:4143:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQLParser.g:4143:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQLParser.g:4144:7: lv_value_10_0= ruleAttributeWithNestedStatement
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
    // InternalCQLParser.g:4167:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQLParser.g:4167:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQLParser.g:4168:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
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
    // InternalCQLParser.g:4174:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) ;
    public final EObject ruleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4180:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) ) )
            // InternalCQLParser.g:4181:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            {
            // InternalCQLParser.g:4181:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) ) )
            int alt70=4;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt70=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt70=2;
                }
                break;
            case RULE_STRING:
                {
                alt70=3;
                }
                break;
            case FALSE:
            case TRUE:
                {
                alt70=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 70, 0, input);

                throw nvae;
            }

            switch (alt70) {
                case 1 :
                    // InternalCQLParser.g:4182:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQLParser.g:4182:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQLParser.g:4183:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQLParser.g:4183:4: ()
                    // InternalCQLParser.g:4184:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4190:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQLParser.g:4191:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQLParser.g:4191:5: (lv_value_1_0= RULE_INT )
                    // InternalCQLParser.g:4192:6: lv_value_1_0= RULE_INT
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
                    // InternalCQLParser.g:4210:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQLParser.g:4210:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQLParser.g:4211:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQLParser.g:4211:4: ()
                    // InternalCQLParser.g:4212:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4218:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQLParser.g:4219:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQLParser.g:4219:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQLParser.g:4220:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQLParser.g:4238:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQLParser.g:4238:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQLParser.g:4239:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQLParser.g:4239:4: ()
                    // InternalCQLParser.g:4240:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4246:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQLParser.g:4247:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQLParser.g:4247:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQLParser.g:4248:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQLParser.g:4266:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    {
                    // InternalCQLParser.g:4266:3: ( () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) ) )
                    // InternalCQLParser.g:4267:4: () ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    {
                    // InternalCQLParser.g:4267:4: ()
                    // InternalCQLParser.g:4268:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQLParser.g:4274:4: ( ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) ) )
                    // InternalCQLParser.g:4275:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    {
                    // InternalCQLParser.g:4275:5: ( (lv_value_7_1= TRUE | lv_value_7_2= FALSE ) )
                    // InternalCQLParser.g:4276:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    {
                    // InternalCQLParser.g:4276:6: (lv_value_7_1= TRUE | lv_value_7_2= FALSE )
                    int alt69=2;
                    int LA69_0 = input.LA(1);

                    if ( (LA69_0==TRUE) ) {
                        alt69=1;
                    }
                    else if ( (LA69_0==FALSE) ) {
                        alt69=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 69, 0, input);

                        throw nvae;
                    }
                    switch (alt69) {
                        case 1 :
                            // InternalCQLParser.g:4277:7: lv_value_7_1= TRUE
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
                            // InternalCQLParser.g:4288:7: lv_value_7_2= FALSE
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
    // InternalCQLParser.g:4306:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQLParser.g:4306:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQLParser.g:4307:2: iv_ruleDataType= ruleDataType EOF
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
    // InternalCQLParser.g:4313:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) ) ;
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
            // InternalCQLParser.g:4319:2: ( ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) ) )
            // InternalCQLParser.g:4320:2: ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) )
            {
            // InternalCQLParser.g:4320:2: ( ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) ) )
            // InternalCQLParser.g:4321:3: ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) )
            {
            // InternalCQLParser.g:4321:3: ( (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP ) )
            // InternalCQLParser.g:4322:4: (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP )
            {
            // InternalCQLParser.g:4322:4: (lv_value_0_1= INTEGER | lv_value_0_2= DOUBLE | lv_value_0_3= LONG | lv_value_0_4= FLOAT | lv_value_0_5= STRING | lv_value_0_6= BOOLEAN | lv_value_0_7= STARTTIMESTAMP | lv_value_0_8= ENDTIMESTAMP )
            int alt71=8;
            switch ( input.LA(1) ) {
            case INTEGER:
                {
                alt71=1;
                }
                break;
            case DOUBLE:
                {
                alt71=2;
                }
                break;
            case LONG:
                {
                alt71=3;
                }
                break;
            case FLOAT:
                {
                alt71=4;
                }
                break;
            case STRING:
                {
                alt71=5;
                }
                break;
            case BOOLEAN:
                {
                alt71=6;
                }
                break;
            case STARTTIMESTAMP:
                {
                alt71=7;
                }
                break;
            case ENDTIMESTAMP:
                {
                alt71=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }

            switch (alt71) {
                case 1 :
                    // InternalCQLParser.g:4323:5: lv_value_0_1= INTEGER
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
                    // InternalCQLParser.g:4334:5: lv_value_0_2= DOUBLE
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
                    // InternalCQLParser.g:4345:5: lv_value_0_3= LONG
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
                    // InternalCQLParser.g:4356:5: lv_value_0_4= FLOAT
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
                    // InternalCQLParser.g:4367:5: lv_value_0_5= STRING
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
                    // InternalCQLParser.g:4378:5: lv_value_0_6= BOOLEAN
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
                    // InternalCQLParser.g:4389:5: lv_value_0_7= STARTTIMESTAMP
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
                    // InternalCQLParser.g:4400:5: lv_value_0_8= ENDTIMESTAMP
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
    // InternalCQLParser.g:4416:1: ruleCreateKeyword returns [Enumerator current=null] : ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) ;
    public final Enumerator ruleCreateKeyword() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCQLParser.g:4422:2: ( ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) ) )
            // InternalCQLParser.g:4423:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            {
            // InternalCQLParser.g:4423:2: ( (enumLiteral_0= CREATE ) | (enumLiteral_1= ATTACH ) )
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==CREATE) ) {
                alt72=1;
            }
            else if ( (LA72_0==ATTACH) ) {
                alt72=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 72, 0, input);

                throw nvae;
            }
            switch (alt72) {
                case 1 :
                    // InternalCQLParser.g:4424:3: (enumLiteral_0= CREATE )
                    {
                    // InternalCQLParser.g:4424:3: (enumLiteral_0= CREATE )
                    // InternalCQLParser.g:4425:4: enumLiteral_0= CREATE
                    {
                    enumLiteral_0=(Token)match(input,CREATE,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQLParser.g:4432:3: (enumLiteral_1= ATTACH )
                    {
                    // InternalCQLParser.g:4432:3: (enumLiteral_1= ATTACH )
                    // InternalCQLParser.g:4433:4: enumLiteral_1= ATTACH
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
    static final String dfa_2s = "\1\23\3\uffff\2\32\2\uffff\1\113\1\74\1\113\1\4\1\76\1\113\13\4\1\113\1\17\1\4\3\uffff\1\113\10\75\1\76\3\4";
    static final String dfa_3s = "\1\54\3\uffff\2\50\2\uffff\1\113\1\74\4\113\10\100\4\113\1\44\1\102\3\uffff\1\113\10\100\1\113\1\47\2\66";
    static final String dfa_4s = "\1\uffff\1\1\1\2\1\3\2\uffff\1\10\1\5\24\uffff\1\7\1\4\1\6\15\uffff";
    static final String dfa_5s = "\54\uffff}>";
    static final String[] dfa_6s = {
            "\1\5\1\4\4\uffff\1\1\1\2\10\uffff\1\3\10\uffff\1\6",
            "",
            "",
            "",
            "\1\10\15\uffff\1\7",
            "\1\10\15\uffff\1\7",
            "",
            "",
            "\1\11",
            "\1\12",
            "\1\13",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\16\uffff\1\15\13\uffff\1\14\10\uffff\1\13",
            "\1\27\14\uffff\1\26",
            "\1\30",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\25\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\25\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\25\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\25\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\25\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\25\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\25\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\25\uffff\1\32\2\uffff\1\31",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\16\uffff\1\15\24\uffff\1\13",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\16\uffff\1\15\24\uffff\1\13",
            "\1\24\1\25\10\uffff\1\23\1\uffff\1\16\4\uffff\1\17\5\uffff\1\22\3\uffff\1\21\7\uffff\1\20\43\uffff\1\13",
            "\1\33",
            "\1\36\2\uffff\1\35\21\uffff\1\34",
            "\1\46\1\47\10\uffff\1\45\1\uffff\1\40\4\uffff\1\41\5\uffff\1\44\3\uffff\1\43\7\uffff\1\42\16\uffff\1\37\13\uffff\1\50",
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
            "\1\46\1\47\10\uffff\1\45\1\uffff\1\40\4\uffff\1\41\5\uffff\1\44\3\uffff\1\43\7\uffff\1\42",
            "\1\46\1\47\10\uffff\1\45\1\uffff\1\40\4\uffff\1\41\5\uffff\1\44\3\uffff\1\43\7\uffff\1\42\16\uffff\1\37",
            "\1\46\1\47\10\uffff\1\45\1\uffff\1\40\4\uffff\1\41\5\uffff\1\44\3\uffff\1\43\7\uffff\1\42\16\uffff\1\37"
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
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000100806180002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x4005C84071000C00L,0x0000000000007800L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x4005C86071000C00L,0x0000000000007801L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x1000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x1000000500800002L,0x0000000000000801L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000500800002L,0x0000000000000001L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x5007C84071000C00L,0x0000000000007800L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000100800002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000800002L,0x0000000000000801L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000800002L,0x0000000000000001L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0040000000000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000020000000200L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0xC040000000000002L,0x000000000000000AL});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0xC000000000000000L,0x000000000000000AL});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0xC000000000000002L,0x000000000000000AL});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x2000000000000000L,0x0000000000004001L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000008088214030L,0x0000000000000800L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x2000008088214030L,0x0000000000000001L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000008088214030L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x2000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000002000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000010004000000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000040000002000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000000200002000L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000200000000002L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0008000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x0030000000000002L,0x0000000000000140L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x8000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x4000000000000002L,0x0000000000000008L});

}