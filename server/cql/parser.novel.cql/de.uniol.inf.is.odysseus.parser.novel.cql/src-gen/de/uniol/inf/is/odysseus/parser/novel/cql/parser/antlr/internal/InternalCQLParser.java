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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_FLOAT", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "';'", "'SELECT'", "'DISTINCT'", "'*'", "','", "'FROM'", "'WHERE'", "'GROUP'", "'BY'", "'HAVING'", "'('", "')'", "'['", "']'", "'AS'", "'.'", "'IN'", "'AVG'", "'MIN'", "'MAX'", "'COUNT'", "'SUM'", "'MEDIAN'", "'FIRST'", "'LAST'", "'DolToEur'", "'+'", "'-'", "'/'", "'WRAPPER'", "'PROTOCOL'", "'TRANSPORT'", "'DATAHANDLER'", "'OPTIONS'", "'STREAM'", "'SINK'", "'CHANNEL'", "':'", "'FILE'", "'VIEW'", "'TO'", "'DROP'", "'IF EXISTS'", "'UNBOUNDED'", "'SIZE'", "'ADVANCE'", "'TIME'", "'TUPLE'", "'PARTITION'", "'OR'", "'AND'", "'='", "'!='", "'>='", "'<='", "'<'", "'>'", "'NOT'", "'TRUE'", "'FALSE'", "'INTEGER'", "'DOUBLE'", "'LONG'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'", "'CREATE'", "'ATTACH'"
    };
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__59=59;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__55=55;
    public static final int T__12=12;
    public static final int T__56=56;
    public static final int T__13=13;
    public static final int T__57=57;
    public static final int T__14=14;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int RULE_ID=4;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=7;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int T__66=66;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__67=67;
    public static final int T__24=24;
    public static final int T__68=68;
    public static final int T__25=25;
    public static final int T__69=69;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__20=20;
    public static final int T__64=64;
    public static final int T__21=21;
    public static final int T__65=65;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int RULE_STRING=6;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__77=77;
    public static final int T__34=34;
    public static final int T__78=78;
    public static final int T__35=35;
    public static final int T__79=79;
    public static final int T__36=36;
    public static final int T__73=73;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__74=74;
    public static final int T__31=31;
    public static final int T__75=75;
    public static final int T__32=32;
    public static final int T__76=76;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int RULE_WS=10;
    public static final int RULE_ANY_OTHER=11;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int RULE_FLOAT=5;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;

    // delegates
    // delegators


        public InternalCQLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalCQLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalCQLParser.tokenNames; }
    public String getGrammarFileName() { return "InternalCQL.g"; }



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
    // InternalCQL.g:65:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalCQL.g:65:46: (iv_ruleModel= ruleModel EOF )
            // InternalCQL.g:66:2: iv_ruleModel= ruleModel EOF
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
    // InternalCQL.g:72:1: ruleModel returns [EObject current=null] : ( (lv_statements_0_0= ruleStatement ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_statements_0_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:78:2: ( ( (lv_statements_0_0= ruleStatement ) )* )
            // InternalCQL.g:79:2: ( (lv_statements_0_0= ruleStatement ) )*
            {
            // InternalCQL.g:79:2: ( (lv_statements_0_0= ruleStatement ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==13||LA1_0==46||LA1_0==51||LA1_0==53||(LA1_0>=80 && LA1_0<=81)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCQL.g:80:3: (lv_statements_0_0= ruleStatement )
            	    {
            	    // InternalCQL.g:80:3: (lv_statements_0_0= ruleStatement )
            	    // InternalCQL.g:81:4: lv_statements_0_0= ruleStatement
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
    // InternalCQL.g:101:1: entryRuleStatement returns [EObject current=null] : iv_ruleStatement= ruleStatement EOF ;
    public final EObject entryRuleStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStatement = null;


        try {
            // InternalCQL.g:101:50: (iv_ruleStatement= ruleStatement EOF )
            // InternalCQL.g:102:2: iv_ruleStatement= ruleStatement EOF
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
    // InternalCQL.g:108:1: ruleStatement returns [EObject current=null] : ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) otherlv_8= ';' ) ;
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
            // InternalCQL.g:114:2: ( ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) otherlv_8= ';' ) )
            // InternalCQL.g:115:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) otherlv_8= ';' )
            {
            // InternalCQL.g:115:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) otherlv_8= ';' )
            // InternalCQL.g:116:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) otherlv_8= ';'
            {
            // InternalCQL.g:116:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) )
            int alt2=8;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // InternalCQL.g:117:4: ( (lv_type_0_0= ruleSelect ) )
                    {
                    // InternalCQL.g:117:4: ( (lv_type_0_0= ruleSelect ) )
                    // InternalCQL.g:118:5: (lv_type_0_0= ruleSelect )
                    {
                    // InternalCQL.g:118:5: (lv_type_0_0= ruleSelect )
                    // InternalCQL.g:119:6: lv_type_0_0= ruleSelect
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
                    // InternalCQL.g:137:4: ( (lv_type_1_0= ruleStreamTo ) )
                    {
                    // InternalCQL.g:137:4: ( (lv_type_1_0= ruleStreamTo ) )
                    // InternalCQL.g:138:5: (lv_type_1_0= ruleStreamTo )
                    {
                    // InternalCQL.g:138:5: (lv_type_1_0= ruleStreamTo )
                    // InternalCQL.g:139:6: lv_type_1_0= ruleStreamTo
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
                    // InternalCQL.g:157:4: ( (lv_type_2_0= ruleDrop ) )
                    {
                    // InternalCQL.g:157:4: ( (lv_type_2_0= ruleDrop ) )
                    // InternalCQL.g:158:5: (lv_type_2_0= ruleDrop )
                    {
                    // InternalCQL.g:158:5: (lv_type_2_0= ruleDrop )
                    // InternalCQL.g:159:6: lv_type_2_0= ruleDrop
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
                    // InternalCQL.g:177:4: ( (lv_type_3_0= ruleCreateStream1 ) )
                    {
                    // InternalCQL.g:177:4: ( (lv_type_3_0= ruleCreateStream1 ) )
                    // InternalCQL.g:178:5: (lv_type_3_0= ruleCreateStream1 )
                    {
                    // InternalCQL.g:178:5: (lv_type_3_0= ruleCreateStream1 )
                    // InternalCQL.g:179:6: lv_type_3_0= ruleCreateStream1
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
                    // InternalCQL.g:197:4: ( (lv_type_4_0= ruleCreateSink1 ) )
                    {
                    // InternalCQL.g:197:4: ( (lv_type_4_0= ruleCreateSink1 ) )
                    // InternalCQL.g:198:5: (lv_type_4_0= ruleCreateSink1 )
                    {
                    // InternalCQL.g:198:5: (lv_type_4_0= ruleCreateSink1 )
                    // InternalCQL.g:199:6: lv_type_4_0= ruleCreateSink1
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
                    // InternalCQL.g:217:4: ( (lv_type_5_0= ruleCreateStreamChannel ) )
                    {
                    // InternalCQL.g:217:4: ( (lv_type_5_0= ruleCreateStreamChannel ) )
                    // InternalCQL.g:218:5: (lv_type_5_0= ruleCreateStreamChannel )
                    {
                    // InternalCQL.g:218:5: (lv_type_5_0= ruleCreateStreamChannel )
                    // InternalCQL.g:219:6: lv_type_5_0= ruleCreateStreamChannel
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
                    // InternalCQL.g:237:4: ( (lv_type_6_0= ruleCreateStreamFile ) )
                    {
                    // InternalCQL.g:237:4: ( (lv_type_6_0= ruleCreateStreamFile ) )
                    // InternalCQL.g:238:5: (lv_type_6_0= ruleCreateStreamFile )
                    {
                    // InternalCQL.g:238:5: (lv_type_6_0= ruleCreateStreamFile )
                    // InternalCQL.g:239:6: lv_type_6_0= ruleCreateStreamFile
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
                    // InternalCQL.g:257:4: ( (lv_type_7_0= ruleCreateView ) )
                    {
                    // InternalCQL.g:257:4: ( (lv_type_7_0= ruleCreateView ) )
                    // InternalCQL.g:258:5: (lv_type_7_0= ruleCreateView )
                    {
                    // InternalCQL.g:258:5: (lv_type_7_0= ruleCreateView )
                    // InternalCQL.g:259:6: lv_type_7_0= ruleCreateView
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

            otherlv_8=(Token)match(input,12,FOLLOW_2); 

            			newLeafNode(otherlv_8, grammarAccess.getStatementAccess().getSemicolonKeyword_1());
            		

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
    // InternalCQL.g:285:1: entryRuleSelect returns [EObject current=null] : iv_ruleSelect= ruleSelect EOF ;
    public final EObject entryRuleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect = null;


        try {
            // InternalCQL.g:285:47: (iv_ruleSelect= ruleSelect EOF )
            // InternalCQL.g:286:2: iv_ruleSelect= ruleSelect EOF
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
    // InternalCQL.g:292:1: ruleSelect returns [EObject current=null] : ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+ ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )* ) ) (otherlv_15= 'FROM' ( (lv_sources_16_0= ruleSource ) )+ (otherlv_17= ',' ( (lv_sources_18_0= ruleSource ) ) )* ) (otherlv_19= 'WHERE' ( (lv_predicates_20_0= ruleExpressionsModel ) ) )? (otherlv_21= 'GROUP' otherlv_22= 'BY' ( (lv_order_23_0= ruleAttribute ) )+ (otherlv_24= ',' ( (lv_order_25_0= ruleAttribute ) ) )* )? (otherlv_26= 'HAVING' ( (lv_having_27_0= ruleExpressionsModel ) ) )? ) ;
    public final EObject ruleSelect() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token lv_distinct_1_0=null;
        Token otherlv_2=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token otherlv_13=null;
        Token otherlv_15=null;
        Token otherlv_17=null;
        Token otherlv_19=null;
        Token otherlv_21=null;
        Token otherlv_22=null;
        Token otherlv_24=null;
        Token otherlv_26=null;
        EObject lv_attributes_3_0 = null;

        EObject lv_aggregations_4_0 = null;

        EObject lv_functions_5_0 = null;

        EObject lv_expressions_6_0 = null;

        EObject lv_attributes_8_0 = null;

        EObject lv_aggregations_10_0 = null;

        EObject lv_functions_12_0 = null;

        EObject lv_expressions_14_0 = null;

        EObject lv_sources_16_0 = null;

        EObject lv_sources_18_0 = null;

        EObject lv_predicates_20_0 = null;

        EObject lv_order_23_0 = null;

        EObject lv_order_25_0 = null;

        EObject lv_having_27_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:298:2: ( ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+ ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )* ) ) (otherlv_15= 'FROM' ( (lv_sources_16_0= ruleSource ) )+ (otherlv_17= ',' ( (lv_sources_18_0= ruleSource ) ) )* ) (otherlv_19= 'WHERE' ( (lv_predicates_20_0= ruleExpressionsModel ) ) )? (otherlv_21= 'GROUP' otherlv_22= 'BY' ( (lv_order_23_0= ruleAttribute ) )+ (otherlv_24= ',' ( (lv_order_25_0= ruleAttribute ) ) )* )? (otherlv_26= 'HAVING' ( (lv_having_27_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQL.g:299:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+ ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )* ) ) (otherlv_15= 'FROM' ( (lv_sources_16_0= ruleSource ) )+ (otherlv_17= ',' ( (lv_sources_18_0= ruleSource ) ) )* ) (otherlv_19= 'WHERE' ( (lv_predicates_20_0= ruleExpressionsModel ) ) )? (otherlv_21= 'GROUP' otherlv_22= 'BY' ( (lv_order_23_0= ruleAttribute ) )+ (otherlv_24= ',' ( (lv_order_25_0= ruleAttribute ) ) )* )? (otherlv_26= 'HAVING' ( (lv_having_27_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQL.g:299:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+ ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )* ) ) (otherlv_15= 'FROM' ( (lv_sources_16_0= ruleSource ) )+ (otherlv_17= ',' ( (lv_sources_18_0= ruleSource ) ) )* ) (otherlv_19= 'WHERE' ( (lv_predicates_20_0= ruleExpressionsModel ) ) )? (otherlv_21= 'GROUP' otherlv_22= 'BY' ( (lv_order_23_0= ruleAttribute ) )+ (otherlv_24= ',' ( (lv_order_25_0= ruleAttribute ) ) )* )? (otherlv_26= 'HAVING' ( (lv_having_27_0= ruleExpressionsModel ) ) )? )
            // InternalCQL.g:300:3: ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+ ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )* ) ) (otherlv_15= 'FROM' ( (lv_sources_16_0= ruleSource ) )+ (otherlv_17= ',' ( (lv_sources_18_0= ruleSource ) ) )* ) (otherlv_19= 'WHERE' ( (lv_predicates_20_0= ruleExpressionsModel ) ) )? (otherlv_21= 'GROUP' otherlv_22= 'BY' ( (lv_order_23_0= ruleAttribute ) )+ (otherlv_24= ',' ( (lv_order_25_0= ruleAttribute ) ) )* )? (otherlv_26= 'HAVING' ( (lv_having_27_0= ruleExpressionsModel ) ) )?
            {
            // InternalCQL.g:300:3: ( (lv_name_0_0= 'SELECT' ) )
            // InternalCQL.g:301:4: (lv_name_0_0= 'SELECT' )
            {
            // InternalCQL.g:301:4: (lv_name_0_0= 'SELECT' )
            // InternalCQL.g:302:5: lv_name_0_0= 'SELECT'
            {
            lv_name_0_0=(Token)match(input,13,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelectAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelectRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCQL.g:314:3: ( (lv_distinct_1_0= 'DISTINCT' ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==14) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalCQL.g:315:4: (lv_distinct_1_0= 'DISTINCT' )
                    {
                    // InternalCQL.g:315:4: (lv_distinct_1_0= 'DISTINCT' )
                    // InternalCQL.g:316:5: lv_distinct_1_0= 'DISTINCT'
                    {
                    lv_distinct_1_0=(Token)match(input,14,FOLLOW_5); 

                    					newLeafNode(lv_distinct_1_0, grammarAccess.getSelectAccess().getDistinctDISTINCTKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getSelectRule());
                    					}
                    					setWithLastConsumed(current, "distinct", lv_distinct_1_0, "DISTINCT");
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:328:3: (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+ ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )* ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==15) ) {
                alt6=1;
            }
            else if ( ((LA6_0>=RULE_ID && LA6_0<=RULE_FLOAT)||(LA6_0>=29 && LA6_0<=37)) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // InternalCQL.g:329:4: otherlv_2= '*'
                    {
                    otherlv_2=(Token)match(input,15,FOLLOW_6); 

                    				newLeafNode(otherlv_2, grammarAccess.getSelectAccess().getAsteriskKeyword_2_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQL.g:334:4: ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+ ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )* )
                    {
                    // InternalCQL.g:334:4: ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+ ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )* )
                    // InternalCQL.g:335:5: ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+ ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )*
                    {
                    // InternalCQL.g:335:5: ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+
                    int cnt4=0;
                    loop4:
                    do {
                        int alt4=5;
                        alt4 = dfa4.predict(input);
                        switch (alt4) {
                    	case 1 :
                    	    // InternalCQL.g:336:6: ( (lv_attributes_3_0= ruleAttribute ) )
                    	    {
                    	    // InternalCQL.g:336:6: ( (lv_attributes_3_0= ruleAttribute ) )
                    	    // InternalCQL.g:337:7: (lv_attributes_3_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:337:7: (lv_attributes_3_0= ruleAttribute )
                    	    // InternalCQL.g:338:8: lv_attributes_3_0= ruleAttribute
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
                    	    // InternalCQL.g:356:6: ( (lv_aggregations_4_0= ruleAggregation ) )
                    	    {
                    	    // InternalCQL.g:356:6: ( (lv_aggregations_4_0= ruleAggregation ) )
                    	    // InternalCQL.g:357:7: (lv_aggregations_4_0= ruleAggregation )
                    	    {
                    	    // InternalCQL.g:357:7: (lv_aggregations_4_0= ruleAggregation )
                    	    // InternalCQL.g:358:8: lv_aggregations_4_0= ruleAggregation
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
                    	    // InternalCQL.g:376:6: ( (lv_functions_5_0= ruleFunction ) )
                    	    {
                    	    // InternalCQL.g:376:6: ( (lv_functions_5_0= ruleFunction ) )
                    	    // InternalCQL.g:377:7: (lv_functions_5_0= ruleFunction )
                    	    {
                    	    // InternalCQL.g:377:7: (lv_functions_5_0= ruleFunction )
                    	    // InternalCQL.g:378:8: lv_functions_5_0= ruleFunction
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectAccess().getFunctionsFunctionParserRuleCall_2_1_0_2_0());
                    	    							
                    	    pushFollow(FOLLOW_7);
                    	    lv_functions_5_0=ruleFunction();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"functions",
                    	    									lv_functions_5_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Function");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 4 :
                    	    // InternalCQL.g:396:6: ( (lv_expressions_6_0= ruleFunctionExpression ) )
                    	    {
                    	    // InternalCQL.g:396:6: ( (lv_expressions_6_0= ruleFunctionExpression ) )
                    	    // InternalCQL.g:397:7: (lv_expressions_6_0= ruleFunctionExpression )
                    	    {
                    	    // InternalCQL.g:397:7: (lv_expressions_6_0= ruleFunctionExpression )
                    	    // InternalCQL.g:398:8: lv_expressions_6_0= ruleFunctionExpression
                    	    {

                    	    								newCompositeNode(grammarAccess.getSelectAccess().getExpressionsFunctionExpressionParserRuleCall_2_1_0_3_0());
                    	    							
                    	    pushFollow(FOLLOW_7);
                    	    lv_expressions_6_0=ruleFunctionExpression();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"expressions",
                    	    									lv_expressions_6_0,
                    	    									"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FunctionExpression");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt4 >= 1 ) break loop4;
                                EarlyExitException eee =
                                    new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);

                    // InternalCQL.g:416:5: ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )*
                    loop5:
                    do {
                        int alt5=5;
                        alt5 = dfa5.predict(input);
                        switch (alt5) {
                    	case 1 :
                    	    // InternalCQL.g:417:6: (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) )
                    	    {
                    	    // InternalCQL.g:417:6: (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) )
                    	    // InternalCQL.g:418:7: otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) )
                    	    {
                    	    otherlv_7=(Token)match(input,16,FOLLOW_8); 

                    	    							newLeafNode(otherlv_7, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_0_0());
                    	    						
                    	    // InternalCQL.g:422:7: ( (lv_attributes_8_0= ruleAttribute ) )
                    	    // InternalCQL.g:423:8: (lv_attributes_8_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:423:8: (lv_attributes_8_0= ruleAttribute )
                    	    // InternalCQL.g:424:9: lv_attributes_8_0= ruleAttribute
                    	    {

                    	    									newCompositeNode(grammarAccess.getSelectAccess().getAttributesAttributeParserRuleCall_2_1_1_0_1_0());
                    	    								
                    	    pushFollow(FOLLOW_9);
                    	    lv_attributes_8_0=ruleAttribute();

                    	    state._fsp--;


                    	    									if (current==null) {
                    	    										current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    									}
                    	    									add(
                    	    										current,
                    	    										"attributes",
                    	    										lv_attributes_8_0,
                    	    										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    									afterParserOrEnumRuleCall();
                    	    								

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQL.g:443:6: (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) )
                    	    {
                    	    // InternalCQL.g:443:6: (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) )
                    	    // InternalCQL.g:444:7: otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) )
                    	    {
                    	    otherlv_9=(Token)match(input,16,FOLLOW_10); 

                    	    							newLeafNode(otherlv_9, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_1_0());
                    	    						
                    	    // InternalCQL.g:448:7: ( (lv_aggregations_10_0= ruleAggregation ) )
                    	    // InternalCQL.g:449:8: (lv_aggregations_10_0= ruleAggregation )
                    	    {
                    	    // InternalCQL.g:449:8: (lv_aggregations_10_0= ruleAggregation )
                    	    // InternalCQL.g:450:9: lv_aggregations_10_0= ruleAggregation
                    	    {

                    	    									newCompositeNode(grammarAccess.getSelectAccess().getAggregationsAggregationParserRuleCall_2_1_1_1_1_0());
                    	    								
                    	    pushFollow(FOLLOW_9);
                    	    lv_aggregations_10_0=ruleAggregation();

                    	    state._fsp--;


                    	    									if (current==null) {
                    	    										current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    									}
                    	    									add(
                    	    										current,
                    	    										"aggregations",
                    	    										lv_aggregations_10_0,
                    	    										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Aggregation");
                    	    									afterParserOrEnumRuleCall();
                    	    								

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 3 :
                    	    // InternalCQL.g:469:6: (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) )
                    	    {
                    	    // InternalCQL.g:469:6: (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) )
                    	    // InternalCQL.g:470:7: otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) )
                    	    {
                    	    otherlv_11=(Token)match(input,16,FOLLOW_11); 

                    	    							newLeafNode(otherlv_11, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_2_0());
                    	    						
                    	    // InternalCQL.g:474:7: ( (lv_functions_12_0= ruleFunction ) )
                    	    // InternalCQL.g:475:8: (lv_functions_12_0= ruleFunction )
                    	    {
                    	    // InternalCQL.g:475:8: (lv_functions_12_0= ruleFunction )
                    	    // InternalCQL.g:476:9: lv_functions_12_0= ruleFunction
                    	    {

                    	    									newCompositeNode(grammarAccess.getSelectAccess().getFunctionsFunctionParserRuleCall_2_1_1_2_1_0());
                    	    								
                    	    pushFollow(FOLLOW_9);
                    	    lv_functions_12_0=ruleFunction();

                    	    state._fsp--;


                    	    									if (current==null) {
                    	    										current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    									}
                    	    									add(
                    	    										current,
                    	    										"functions",
                    	    										lv_functions_12_0,
                    	    										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Function");
                    	    									afterParserOrEnumRuleCall();
                    	    								

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 4 :
                    	    // InternalCQL.g:495:6: (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) )
                    	    {
                    	    // InternalCQL.g:495:6: (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) )
                    	    // InternalCQL.g:496:7: otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) )
                    	    {
                    	    otherlv_13=(Token)match(input,16,FOLLOW_5); 

                    	    							newLeafNode(otherlv_13, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_3_0());
                    	    						
                    	    // InternalCQL.g:500:7: ( (lv_expressions_14_0= ruleFunctionExpression ) )
                    	    // InternalCQL.g:501:8: (lv_expressions_14_0= ruleFunctionExpression )
                    	    {
                    	    // InternalCQL.g:501:8: (lv_expressions_14_0= ruleFunctionExpression )
                    	    // InternalCQL.g:502:9: lv_expressions_14_0= ruleFunctionExpression
                    	    {

                    	    									newCompositeNode(grammarAccess.getSelectAccess().getExpressionsFunctionExpressionParserRuleCall_2_1_1_3_1_0());
                    	    								
                    	    pushFollow(FOLLOW_9);
                    	    lv_expressions_14_0=ruleFunctionExpression();

                    	    state._fsp--;


                    	    									if (current==null) {
                    	    										current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    									}
                    	    									add(
                    	    										current,
                    	    										"expressions",
                    	    										lv_expressions_14_0,
                    	    										"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FunctionExpression");
                    	    									afterParserOrEnumRuleCall();
                    	    								

                    	    }


                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


                    }


                    }
                    break;

            }

            // InternalCQL.g:523:3: (otherlv_15= 'FROM' ( (lv_sources_16_0= ruleSource ) )+ (otherlv_17= ',' ( (lv_sources_18_0= ruleSource ) ) )* )
            // InternalCQL.g:524:4: otherlv_15= 'FROM' ( (lv_sources_16_0= ruleSource ) )+ (otherlv_17= ',' ( (lv_sources_18_0= ruleSource ) ) )*
            {
            otherlv_15=(Token)match(input,17,FOLLOW_12); 

            				newLeafNode(otherlv_15, grammarAccess.getSelectAccess().getFROMKeyword_3_0());
            			
            // InternalCQL.g:528:4: ( (lv_sources_16_0= ruleSource ) )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==RULE_ID||LA7_0==22) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalCQL.g:529:5: (lv_sources_16_0= ruleSource )
            	    {
            	    // InternalCQL.g:529:5: (lv_sources_16_0= ruleSource )
            	    // InternalCQL.g:530:6: lv_sources_16_0= ruleSource
            	    {

            	    						newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_13);
            	    lv_sources_16_0=ruleSource();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelectRule());
            	    						}
            	    						add(
            	    							current,
            	    							"sources",
            	    							lv_sources_16_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);

            // InternalCQL.g:547:4: (otherlv_17= ',' ( (lv_sources_18_0= ruleSource ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==16) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCQL.g:548:5: otherlv_17= ',' ( (lv_sources_18_0= ruleSource ) )
            	    {
            	    otherlv_17=(Token)match(input,16,FOLLOW_12); 

            	    					newLeafNode(otherlv_17, grammarAccess.getSelectAccess().getCommaKeyword_3_2_0());
            	    				
            	    // InternalCQL.g:552:5: ( (lv_sources_18_0= ruleSource ) )
            	    // InternalCQL.g:553:6: (lv_sources_18_0= ruleSource )
            	    {
            	    // InternalCQL.g:553:6: (lv_sources_18_0= ruleSource )
            	    // InternalCQL.g:554:7: lv_sources_18_0= ruleSource
            	    {

            	    							newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_2_1_0());
            	    						
            	    pushFollow(FOLLOW_14);
            	    lv_sources_18_0=ruleSource();

            	    state._fsp--;


            	    							if (current==null) {
            	    								current = createModelElementForParent(grammarAccess.getSelectRule());
            	    							}
            	    							add(
            	    								current,
            	    								"sources",
            	    								lv_sources_18_0,
            	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
            	    							afterParserOrEnumRuleCall();
            	    						

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }

            // InternalCQL.g:573:3: (otherlv_19= 'WHERE' ( (lv_predicates_20_0= ruleExpressionsModel ) ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==18) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalCQL.g:574:4: otherlv_19= 'WHERE' ( (lv_predicates_20_0= ruleExpressionsModel ) )
                    {
                    otherlv_19=(Token)match(input,18,FOLLOW_15); 

                    				newLeafNode(otherlv_19, grammarAccess.getSelectAccess().getWHEREKeyword_4_0());
                    			
                    // InternalCQL.g:578:4: ( (lv_predicates_20_0= ruleExpressionsModel ) )
                    // InternalCQL.g:579:5: (lv_predicates_20_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:579:5: (lv_predicates_20_0= ruleExpressionsModel )
                    // InternalCQL.g:580:6: lv_predicates_20_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getPredicatesExpressionsModelParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_16);
                    lv_predicates_20_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"predicates",
                    							lv_predicates_20_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:598:3: (otherlv_21= 'GROUP' otherlv_22= 'BY' ( (lv_order_23_0= ruleAttribute ) )+ (otherlv_24= ',' ( (lv_order_25_0= ruleAttribute ) ) )* )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==19) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalCQL.g:599:4: otherlv_21= 'GROUP' otherlv_22= 'BY' ( (lv_order_23_0= ruleAttribute ) )+ (otherlv_24= ',' ( (lv_order_25_0= ruleAttribute ) ) )*
                    {
                    otherlv_21=(Token)match(input,19,FOLLOW_17); 

                    				newLeafNode(otherlv_21, grammarAccess.getSelectAccess().getGROUPKeyword_5_0());
                    			
                    otherlv_22=(Token)match(input,20,FOLLOW_8); 

                    				newLeafNode(otherlv_22, grammarAccess.getSelectAccess().getBYKeyword_5_1());
                    			
                    // InternalCQL.g:607:4: ( (lv_order_23_0= ruleAttribute ) )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==RULE_ID) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // InternalCQL.g:608:5: (lv_order_23_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:608:5: (lv_order_23_0= ruleAttribute )
                    	    // InternalCQL.g:609:6: lv_order_23_0= ruleAttribute
                    	    {

                    	    						newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_2_0());
                    	    					
                    	    pushFollow(FOLLOW_18);
                    	    lv_order_23_0=ruleAttribute();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"order",
                    	    							lv_order_23_0,
                    	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt10 >= 1 ) break loop10;
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);

                    // InternalCQL.g:626:4: (otherlv_24= ',' ( (lv_order_25_0= ruleAttribute ) ) )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==16) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // InternalCQL.g:627:5: otherlv_24= ',' ( (lv_order_25_0= ruleAttribute ) )
                    	    {
                    	    otherlv_24=(Token)match(input,16,FOLLOW_8); 

                    	    					newLeafNode(otherlv_24, grammarAccess.getSelectAccess().getCommaKeyword_5_3_0());
                    	    				
                    	    // InternalCQL.g:631:5: ( (lv_order_25_0= ruleAttribute ) )
                    	    // InternalCQL.g:632:6: (lv_order_25_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:632:6: (lv_order_25_0= ruleAttribute )
                    	    // InternalCQL.g:633:7: lv_order_25_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_19);
                    	    lv_order_25_0=ruleAttribute();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"order",
                    	    								lv_order_25_0,
                    	    								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCQL.g:652:3: (otherlv_26= 'HAVING' ( (lv_having_27_0= ruleExpressionsModel ) ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==21) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQL.g:653:4: otherlv_26= 'HAVING' ( (lv_having_27_0= ruleExpressionsModel ) )
                    {
                    otherlv_26=(Token)match(input,21,FOLLOW_15); 

                    				newLeafNode(otherlv_26, grammarAccess.getSelectAccess().getHAVINGKeyword_6_0());
                    			
                    // InternalCQL.g:657:4: ( (lv_having_27_0= ruleExpressionsModel ) )
                    // InternalCQL.g:658:5: (lv_having_27_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:658:5: (lv_having_27_0= ruleExpressionsModel )
                    // InternalCQL.g:659:6: lv_having_27_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getHavingExpressionsModelParserRuleCall_6_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_having_27_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"having",
                    							lv_having_27_0,
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
    // InternalCQL.g:681:1: entryRuleNestedStatement returns [EObject current=null] : iv_ruleNestedStatement= ruleNestedStatement EOF ;
    public final EObject entryRuleNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedStatement = null;


        try {
            // InternalCQL.g:681:56: (iv_ruleNestedStatement= ruleNestedStatement EOF )
            // InternalCQL.g:682:2: iv_ruleNestedStatement= ruleNestedStatement EOF
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
    // InternalCQL.g:688:1: ruleNestedStatement returns [EObject current=null] : (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) ;
    public final EObject ruleNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:694:2: ( (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) )
            // InternalCQL.g:695:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            {
            // InternalCQL.g:695:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            // InternalCQL.g:696:3: otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')'
            {
            otherlv_0=(Token)match(input,22,FOLLOW_20); 

            			newLeafNode(otherlv_0, grammarAccess.getNestedStatementAccess().getLeftParenthesisKeyword_0());
            		

            			newCompositeNode(grammarAccess.getNestedStatementAccess().getSelectParserRuleCall_1());
            		
            pushFollow(FOLLOW_21);
            this_Select_1=ruleSelect();

            state._fsp--;


            			current = this_Select_1;
            			afterParserOrEnumRuleCall();
            		
            otherlv_2=(Token)match(input,23,FOLLOW_2); 

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
    // InternalCQL.g:716:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQL.g:716:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQL.g:717:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQL.g:723:1: ruleSource returns [EObject current=null] : ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) ) ;
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
            // InternalCQL.g:729:2: ( ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) ) )
            // InternalCQL.g:730:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) )
            {
            // InternalCQL.g:730:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==RULE_ID) ) {
                alt17=1;
            }
            else if ( (LA17_0==22) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // InternalCQL.g:731:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
                    {
                    // InternalCQL.g:731:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
                    // InternalCQL.g:732:4: ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
                    {
                    // InternalCQL.g:732:4: ( (lv_name_0_0= ruleSourceName ) )
                    // InternalCQL.g:733:5: (lv_name_0_0= ruleSourceName )
                    {
                    // InternalCQL.g:733:5: (lv_name_0_0= ruleSourceName )
                    // InternalCQL.g:734:6: lv_name_0_0= ruleSourceName
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNameSourceNameParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_22);
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

                    // InternalCQL.g:751:4: (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==24) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // InternalCQL.g:752:5: otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']'
                            {
                            otherlv_1=(Token)match(input,24,FOLLOW_23); 

                            					newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_1_0());
                            				
                            // InternalCQL.g:756:5: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                            int alt14=3;
                            int LA14_0 = input.LA(1);

                            if ( (LA14_0==55) ) {
                                alt14=1;
                            }
                            else if ( (LA14_0==56) ) {
                                int LA14_2 = input.LA(2);

                                if ( (LA14_2==RULE_INT) ) {
                                    int LA14_3 = input.LA(3);

                                    if ( (LA14_3==RULE_ID) ) {
                                        alt14=2;
                                    }
                                    else if ( (LA14_3==57||LA14_3==59) ) {
                                        alt14=3;
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("", 14, 3, input);

                                        throw nvae;
                                    }
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 14, 2, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 14, 0, input);

                                throw nvae;
                            }
                            switch (alt14) {
                                case 1 :
                                    // InternalCQL.g:757:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQL.g:757:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQL.g:758:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQL.g:758:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQL.g:759:8: lv_unbounded_2_0= ruleWindow_Unbounded
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getUnboundedWindow_UnboundedParserRuleCall_0_1_1_0_0());
                                    							
                                    pushFollow(FOLLOW_24);
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
                                    // InternalCQL.g:777:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQL.g:777:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQL.g:778:7: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQL.g:778:7: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQL.g:779:8: lv_time_3_0= ruleWindow_Timebased
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getTimeWindow_TimebasedParserRuleCall_0_1_1_1_0());
                                    							
                                    pushFollow(FOLLOW_24);
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
                                    // InternalCQL.g:797:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQL.g:797:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQL.g:798:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQL.g:798:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQL.g:799:8: lv_tuple_4_0= ruleWindow_Tuplebased
                                    {

                                    								newCompositeNode(grammarAccess.getSourceAccess().getTupleWindow_TuplebasedParserRuleCall_0_1_1_2_0());
                                    							
                                    pushFollow(FOLLOW_24);
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

                            otherlv_5=(Token)match(input,25,FOLLOW_25); 

                            					newLeafNode(otherlv_5, grammarAccess.getSourceAccess().getRightSquareBracketKeyword_0_1_2());
                            				

                            }
                            break;

                    }

                    // InternalCQL.g:822:4: (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==26) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // InternalCQL.g:823:5: otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) )
                            {
                            otherlv_6=(Token)match(input,26,FOLLOW_8); 

                            					newLeafNode(otherlv_6, grammarAccess.getSourceAccess().getASKeyword_0_2_0());
                            				
                            // InternalCQL.g:827:5: ( (lv_alias_7_0= ruleAlias ) )
                            // InternalCQL.g:828:6: (lv_alias_7_0= ruleAlias )
                            {
                            // InternalCQL.g:828:6: (lv_alias_7_0= ruleAlias )
                            // InternalCQL.g:829:7: lv_alias_7_0= ruleAlias
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
                    // InternalCQL.g:849:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) )
                    {
                    // InternalCQL.g:849:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) )
                    // InternalCQL.g:850:4: ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) )
                    {
                    // InternalCQL.g:850:4: ( (lv_nested_8_0= ruleNestedStatement ) )
                    // InternalCQL.g:851:5: (lv_nested_8_0= ruleNestedStatement )
                    {
                    // InternalCQL.g:851:5: (lv_nested_8_0= ruleNestedStatement )
                    // InternalCQL.g:852:6: lv_nested_8_0= ruleNestedStatement
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNestedNestedStatementParserRuleCall_1_0_0());
                    					
                    pushFollow(FOLLOW_26);
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

                    otherlv_9=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_9, grammarAccess.getSourceAccess().getASKeyword_1_1());
                    			
                    // InternalCQL.g:873:4: ( (lv_alias_10_0= ruleAlias ) )
                    // InternalCQL.g:874:5: (lv_alias_10_0= ruleAlias )
                    {
                    // InternalCQL.g:874:5: (lv_alias_10_0= ruleAlias )
                    // InternalCQL.g:875:6: lv_alias_10_0= ruleAlias
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
    // InternalCQL.g:897:1: entryRuleSourceName returns [String current=null] : iv_ruleSourceName= ruleSourceName EOF ;
    public final String entryRuleSourceName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSourceName = null;


        try {
            // InternalCQL.g:897:50: (iv_ruleSourceName= ruleSourceName EOF )
            // InternalCQL.g:898:2: iv_ruleSourceName= ruleSourceName EOF
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
    // InternalCQL.g:904:1: ruleSourceName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleSourceName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQL.g:910:2: (this_ID_0= RULE_ID )
            // InternalCQL.g:911:2: this_ID_0= RULE_ID
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
    // InternalCQL.g:921:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQL.g:921:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQL.g:922:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQL.g:928:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:934:2: ( ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:935:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:935:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQL.g:936:3: ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:936:3: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQL.g:937:4: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQL.g:937:4: (lv_name_0_0= ruleAttributeName )
            // InternalCQL.g:938:5: lv_name_0_0= ruleAttributeName
            {

            					newCompositeNode(grammarAccess.getAttributeAccess().getNameAttributeNameParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_25);
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

            // InternalCQL.g:955:3: (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==26) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // InternalCQL.g:956:4: otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:960:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQL.g:961:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQL.g:961:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQL.g:962:6: lv_alias_2_0= ruleAlias
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
    // InternalCQL.g:984:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQL.g:984:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQL.g:985:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
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
    // InternalCQL.g:991:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleAttributeName ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:997:2: ( ( (lv_name_0_0= ruleAttributeName ) ) )
            // InternalCQL.g:998:2: ( (lv_name_0_0= ruleAttributeName ) )
            {
            // InternalCQL.g:998:2: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQL.g:999:3: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQL.g:999:3: (lv_name_0_0= ruleAttributeName )
            // InternalCQL.g:1000:4: lv_name_0_0= ruleAttributeName
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
    // InternalCQL.g:1020:1: entryRuleAttributeName returns [String current=null] : iv_ruleAttributeName= ruleAttributeName EOF ;
    public final String entryRuleAttributeName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAttributeName = null;


        try {
            // InternalCQL.g:1020:53: (iv_ruleAttributeName= ruleAttributeName EOF )
            // InternalCQL.g:1021:2: iv_ruleAttributeName= ruleAttributeName EOF
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
    // InternalCQL.g:1027:1: ruleAttributeName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) ) ;
    public final AntlrDatatypeRuleToken ruleAttributeName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_SourceName_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:1033:2: ( (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) ) )
            // InternalCQL.g:1034:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) )
            {
            // InternalCQL.g:1034:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==RULE_ID) ) {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==27) ) {
                    alt19=2;
                }
                else if ( (LA19_1==EOF||(LA19_1>=RULE_ID && LA19_1<=RULE_FLOAT)||LA19_1==12||(LA19_1>=15 && LA19_1<=17)||LA19_1==19||LA19_1==21||LA19_1==23||(LA19_1>=25 && LA19_1<=26)||(LA19_1>=28 && LA19_1<=40)||(LA19_1>=61 && LA19_1<=68)||(LA19_1>=72 && LA19_1<=79)) ) {
                    alt19=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQL.g:1035:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1043:3: (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID )
                    {
                    // InternalCQL.g:1043:3: (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID )
                    // InternalCQL.g:1044:4: this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getAttributeNameAccess().getSourceNameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_27);
                    this_SourceName_1=ruleSourceName();

                    state._fsp--;


                    				current.merge(this_SourceName_1);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,27,FOLLOW_8); 

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
    // InternalCQL.g:1071:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQL.g:1071:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQL.g:1072:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
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
    // InternalCQL.g:1078:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1084:2: ( ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) )
            // InternalCQL.g:1085:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            {
            // InternalCQL.g:1085:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            // InternalCQL.g:1086:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) )
            {
            // InternalCQL.g:1086:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:1087:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:1087:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:1088:5: lv_value_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getAttributeWithNestedStatementAccess().getValueAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_28);
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

            otherlv_1=(Token)match(input,28,FOLLOW_12); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeWithNestedStatementAccess().getINKeyword_1());
            		
            // InternalCQL.g:1109:3: ( (lv_nested_2_0= ruleNestedStatement ) )
            // InternalCQL.g:1110:4: (lv_nested_2_0= ruleNestedStatement )
            {
            // InternalCQL.g:1110:4: (lv_nested_2_0= ruleNestedStatement )
            // InternalCQL.g:1111:5: lv_nested_2_0= ruleNestedStatement
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
    // InternalCQL.g:1132:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalCQL.g:1132:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalCQL.g:1133:2: iv_ruleAggregation= ruleAggregation EOF
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
    // InternalCQL.g:1139:1: ruleAggregation returns [EObject current=null] : ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) ;
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
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_attribute_2_0 = null;

        EObject lv_alias_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1145:2: ( ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:1146:2: ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:1146:2: ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            // InternalCQL.g:1147:3: ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:1147:3: ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) )
            // InternalCQL.g:1148:4: ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) )
            {
            // InternalCQL.g:1148:4: ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) )
            // InternalCQL.g:1149:5: (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' )
            {
            // InternalCQL.g:1149:5: (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' )
            int alt20=8;
            switch ( input.LA(1) ) {
            case 29:
                {
                alt20=1;
                }
                break;
            case 30:
                {
                alt20=2;
                }
                break;
            case 31:
                {
                alt20=3;
                }
                break;
            case 32:
                {
                alt20=4;
                }
                break;
            case 33:
                {
                alt20=5;
                }
                break;
            case 34:
                {
                alt20=6;
                }
                break;
            case 35:
                {
                alt20=7;
                }
                break;
            case 36:
                {
                alt20=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // InternalCQL.g:1150:6: lv_name_0_1= 'AVG'
                    {
                    lv_name_0_1=(Token)match(input,29,FOLLOW_29); 

                    						newLeafNode(lv_name_0_1, grammarAccess.getAggregationAccess().getNameAVGKeyword_0_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1161:6: lv_name_0_2= 'MIN'
                    {
                    lv_name_0_2=(Token)match(input,30,FOLLOW_29); 

                    						newLeafNode(lv_name_0_2, grammarAccess.getAggregationAccess().getNameMINKeyword_0_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:1172:6: lv_name_0_3= 'MAX'
                    {
                    lv_name_0_3=(Token)match(input,31,FOLLOW_29); 

                    						newLeafNode(lv_name_0_3, grammarAccess.getAggregationAccess().getNameMAXKeyword_0_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:1183:6: lv_name_0_4= 'COUNT'
                    {
                    lv_name_0_4=(Token)match(input,32,FOLLOW_29); 

                    						newLeafNode(lv_name_0_4, grammarAccess.getAggregationAccess().getNameCOUNTKeyword_0_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_4, null);
                    					

                    }
                    break;
                case 5 :
                    // InternalCQL.g:1194:6: lv_name_0_5= 'SUM'
                    {
                    lv_name_0_5=(Token)match(input,33,FOLLOW_29); 

                    						newLeafNode(lv_name_0_5, grammarAccess.getAggregationAccess().getNameSUMKeyword_0_0_4());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_5, null);
                    					

                    }
                    break;
                case 6 :
                    // InternalCQL.g:1205:6: lv_name_0_6= 'MEDIAN'
                    {
                    lv_name_0_6=(Token)match(input,34,FOLLOW_29); 

                    						newLeafNode(lv_name_0_6, grammarAccess.getAggregationAccess().getNameMEDIANKeyword_0_0_5());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_6, null);
                    					

                    }
                    break;
                case 7 :
                    // InternalCQL.g:1216:6: lv_name_0_7= 'FIRST'
                    {
                    lv_name_0_7=(Token)match(input,35,FOLLOW_29); 

                    						newLeafNode(lv_name_0_7, grammarAccess.getAggregationAccess().getNameFIRSTKeyword_0_0_6());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_7, null);
                    					

                    }
                    break;
                case 8 :
                    // InternalCQL.g:1227:6: lv_name_0_8= 'LAST'
                    {
                    lv_name_0_8=(Token)match(input,36,FOLLOW_29); 

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

            otherlv_1=(Token)match(input,22,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregationAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:1244:3: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:1245:4: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:1245:4: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:1246:5: lv_attribute_2_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getAggregationAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_21);
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

            otherlv_3=(Token)match(input,23,FOLLOW_25); 

            			newLeafNode(otherlv_3, grammarAccess.getAggregationAccess().getRightParenthesisKeyword_3());
            		
            // InternalCQL.g:1267:3: (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==26) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalCQL.g:1268:4: otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) )
                    {
                    otherlv_4=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getASKeyword_4_0());
                    			
                    // InternalCQL.g:1272:4: ( (lv_alias_5_0= ruleAlias ) )
                    // InternalCQL.g:1273:5: (lv_alias_5_0= ruleAlias )
                    {
                    // InternalCQL.g:1273:5: (lv_alias_5_0= ruleAlias )
                    // InternalCQL.g:1274:6: lv_alias_5_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getAggregationAccess().getAliasAliasParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_5_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAggregationRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_5_0,
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


    // $ANTLR start "entryRuleFunction"
    // InternalCQL.g:1296:1: entryRuleFunction returns [EObject current=null] : iv_ruleFunction= ruleFunction EOF ;
    public final EObject entryRuleFunction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunction = null;


        try {
            // InternalCQL.g:1296:49: (iv_ruleFunction= ruleFunction EOF )
            // InternalCQL.g:1297:2: iv_ruleFunction= ruleFunction EOF
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
    // InternalCQL.g:1303:1: ruleFunction returns [EObject current=null] : ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) ) ;
    public final EObject ruleFunction() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_attribute_2_0 = null;

        EObject lv_alias_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1309:2: ( ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) ) )
            // InternalCQL.g:1310:2: ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )
            {
            // InternalCQL.g:1310:2: ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )
            // InternalCQL.g:1311:3: ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) )
            {
            // InternalCQL.g:1311:3: ( (lv_name_0_0= 'DolToEur' ) )
            // InternalCQL.g:1312:4: (lv_name_0_0= 'DolToEur' )
            {
            // InternalCQL.g:1312:4: (lv_name_0_0= 'DolToEur' )
            // InternalCQL.g:1313:5: lv_name_0_0= 'DolToEur'
            {
            lv_name_0_0=(Token)match(input,37,FOLLOW_29); 

            					newLeafNode(lv_name_0_0, grammarAccess.getFunctionAccess().getNameDolToEurKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFunctionRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "DolToEur");
            				

            }


            }

            otherlv_1=(Token)match(input,22,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getFunctionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:1329:3: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:1330:4: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:1330:4: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:1331:5: lv_attribute_2_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getFunctionAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_21);
            lv_attribute_2_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getFunctionRule());
            					}
            					set(
            						current,
            						"attribute",
            						lv_attribute_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,23,FOLLOW_26); 

            			newLeafNode(otherlv_3, grammarAccess.getFunctionAccess().getRightParenthesisKeyword_3());
            		
            otherlv_4=(Token)match(input,26,FOLLOW_8); 

            			newLeafNode(otherlv_4, grammarAccess.getFunctionAccess().getASKeyword_4());
            		
            // InternalCQL.g:1356:3: ( (lv_alias_5_0= ruleAlias ) )
            // InternalCQL.g:1357:4: (lv_alias_5_0= ruleAlias )
            {
            // InternalCQL.g:1357:4: (lv_alias_5_0= ruleAlias )
            // InternalCQL.g:1358:5: lv_alias_5_0= ruleAlias
            {

            					newCompositeNode(grammarAccess.getFunctionAccess().getAliasAliasParserRuleCall_5_0());
            				
            pushFollow(FOLLOW_2);
            lv_alias_5_0=ruleAlias();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getFunctionRule());
            					}
            					set(
            						current,
            						"alias",
            						lv_alias_5_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Alias");
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
    // $ANTLR end "ruleFunction"


    // $ANTLR start "entryRuleFunctionWithoutAliasDefinition"
    // InternalCQL.g:1379:1: entryRuleFunctionWithoutAliasDefinition returns [EObject current=null] : iv_ruleFunctionWithoutAliasDefinition= ruleFunctionWithoutAliasDefinition EOF ;
    public final EObject entryRuleFunctionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunctionWithoutAliasDefinition = null;


        try {
            // InternalCQL.g:1379:71: (iv_ruleFunctionWithoutAliasDefinition= ruleFunctionWithoutAliasDefinition EOF )
            // InternalCQL.g:1380:2: iv_ruleFunctionWithoutAliasDefinition= ruleFunctionWithoutAliasDefinition EOF
            {
             newCompositeNode(grammarAccess.getFunctionWithoutAliasDefinitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFunctionWithoutAliasDefinition=ruleFunctionWithoutAliasDefinition();

            state._fsp--;

             current =iv_ruleFunctionWithoutAliasDefinition; 
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
    // $ANTLR end "entryRuleFunctionWithoutAliasDefinition"


    // $ANTLR start "ruleFunctionWithoutAliasDefinition"
    // InternalCQL.g:1386:1: ruleFunctionWithoutAliasDefinition returns [EObject current=null] : ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' ) ;
    public final EObject ruleFunctionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_attribute_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1392:2: ( ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' ) )
            // InternalCQL.g:1393:2: ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' )
            {
            // InternalCQL.g:1393:2: ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')' )
            // InternalCQL.g:1394:3: ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_3= ')'
            {
            // InternalCQL.g:1394:3: ( (lv_name_0_0= 'DolToEur' ) )
            // InternalCQL.g:1395:4: (lv_name_0_0= 'DolToEur' )
            {
            // InternalCQL.g:1395:4: (lv_name_0_0= 'DolToEur' )
            // InternalCQL.g:1396:5: lv_name_0_0= 'DolToEur'
            {
            lv_name_0_0=(Token)match(input,37,FOLLOW_29); 

            					newLeafNode(lv_name_0_0, grammarAccess.getFunctionWithoutAliasDefinitionAccess().getNameDolToEurKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFunctionWithoutAliasDefinitionRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "DolToEur");
            				

            }


            }

            otherlv_1=(Token)match(input,22,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getFunctionWithoutAliasDefinitionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:1412:3: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:1413:4: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:1413:4: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:1414:5: lv_attribute_2_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getFunctionWithoutAliasDefinitionAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_21);
            lv_attribute_2_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getFunctionWithoutAliasDefinitionRule());
            					}
            					set(
            						current,
            						"attribute",
            						lv_attribute_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,23,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getFunctionWithoutAliasDefinitionAccess().getRightParenthesisKeyword_3());
            		

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
    // $ANTLR end "ruleFunctionWithoutAliasDefinition"


    // $ANTLR start "entryRuleFunctionExpression"
    // InternalCQL.g:1439:1: entryRuleFunctionExpression returns [EObject current=null] : iv_ruleFunctionExpression= ruleFunctionExpression EOF ;
    public final EObject entryRuleFunctionExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunctionExpression = null;


        try {
            // InternalCQL.g:1439:59: (iv_ruleFunctionExpression= ruleFunctionExpression EOF )
            // InternalCQL.g:1440:2: iv_ruleFunctionExpression= ruleFunctionExpression EOF
            {
             newCompositeNode(grammarAccess.getFunctionExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFunctionExpression=ruleFunctionExpression();

            state._fsp--;

             current =iv_ruleFunctionExpression; 
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
    // $ANTLR end "entryRuleFunctionExpression"


    // $ANTLR start "ruleFunctionExpression"
    // InternalCQL.g:1446:1: ruleFunctionExpression returns [EObject current=null] : ( ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= RULE_FLOAT ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? ) ;
    public final EObject ruleFunctionExpression() throws RecognitionException {
        EObject current = null;

        Token lv_leftconstant_2_0=null;
        Token lv_operator_3_1=null;
        Token lv_operator_3_2=null;
        Token lv_operator_3_3=null;
        Token lv_operator_3_4=null;
        Token lv_rightconstant_6_0=null;
        Token otherlv_7=null;
        EObject lv_function_0_0 = null;

        EObject lv_leftattribute_1_0 = null;

        EObject lv_innerexpression_4_0 = null;

        EObject lv_rightattribute_5_0 = null;

        EObject lv_alias_8_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1452:2: ( ( ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= RULE_FLOAT ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:1453:2: ( ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= RULE_FLOAT ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:1453:2: ( ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= RULE_FLOAT ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? )
            // InternalCQL.g:1454:3: ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= RULE_FLOAT ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:1454:3: ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= RULE_FLOAT ) ) )
            int alt22=3;
            switch ( input.LA(1) ) {
            case 37:
                {
                alt22=1;
                }
                break;
            case RULE_ID:
                {
                alt22=2;
                }
                break;
            case RULE_FLOAT:
                {
                alt22=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }

            switch (alt22) {
                case 1 :
                    // InternalCQL.g:1455:4: ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1455:4: ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) )
                    // InternalCQL.g:1456:5: (lv_function_0_0= ruleFunctionWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1456:5: (lv_function_0_0= ruleFunctionWithoutAliasDefinition )
                    // InternalCQL.g:1457:6: lv_function_0_0= ruleFunctionWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getFunctionExpressionAccess().getFunctionFunctionWithoutAliasDefinitionParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_30);
                    lv_function_0_0=ruleFunctionWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionExpressionRule());
                    						}
                    						set(
                    							current,
                    							"function",
                    							lv_function_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FunctionWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1475:4: ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1475:4: ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1476:5: (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1476:5: (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1477:6: lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getFunctionExpressionAccess().getLeftattributeAttributeWithoutAliasDefinitionParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_30);
                    lv_leftattribute_1_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionExpressionRule());
                    						}
                    						set(
                    							current,
                    							"leftattribute",
                    							lv_leftattribute_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:1495:4: ( (lv_leftconstant_2_0= RULE_FLOAT ) )
                    {
                    // InternalCQL.g:1495:4: ( (lv_leftconstant_2_0= RULE_FLOAT ) )
                    // InternalCQL.g:1496:5: (lv_leftconstant_2_0= RULE_FLOAT )
                    {
                    // InternalCQL.g:1496:5: (lv_leftconstant_2_0= RULE_FLOAT )
                    // InternalCQL.g:1497:6: lv_leftconstant_2_0= RULE_FLOAT
                    {
                    lv_leftconstant_2_0=(Token)match(input,RULE_FLOAT,FOLLOW_30); 

                    						newLeafNode(lv_leftconstant_2_0, grammarAccess.getFunctionExpressionAccess().getLeftconstantFLOATTerminalRuleCall_0_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"leftconstant",
                    							lv_leftconstant_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FLOAT");
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1514:3: ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) )
            // InternalCQL.g:1515:4: ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) )
            {
            // InternalCQL.g:1515:4: ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) )
            // InternalCQL.g:1516:5: (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' )
            {
            // InternalCQL.g:1516:5: (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' )
            int alt23=4;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt23=1;
                }
                break;
            case 39:
                {
                alt23=2;
                }
                break;
            case 40:
                {
                alt23=3;
                }
                break;
            case 15:
                {
                alt23=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // InternalCQL.g:1517:6: lv_operator_3_1= '+'
                    {
                    lv_operator_3_1=(Token)match(input,38,FOLLOW_31); 

                    						newLeafNode(lv_operator_3_1, grammarAccess.getFunctionExpressionAccess().getOperatorPlusSignKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1528:6: lv_operator_3_2= '-'
                    {
                    lv_operator_3_2=(Token)match(input,39,FOLLOW_31); 

                    						newLeafNode(lv_operator_3_2, grammarAccess.getFunctionExpressionAccess().getOperatorHyphenMinusKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:1539:6: lv_operator_3_3= '/'
                    {
                    lv_operator_3_3=(Token)match(input,40,FOLLOW_31); 

                    						newLeafNode(lv_operator_3_3, grammarAccess.getFunctionExpressionAccess().getOperatorSolidusKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:1550:6: lv_operator_3_4= '*'
                    {
                    lv_operator_3_4=(Token)match(input,15,FOLLOW_31); 

                    						newLeafNode(lv_operator_3_4, grammarAccess.getFunctionExpressionAccess().getOperatorAsteriskKeyword_1_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_4, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:1563:3: ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )?
            int alt24=2;
            switch ( input.LA(1) ) {
                case 37:
                    {
                    alt24=1;
                    }
                    break;
                case RULE_ID:
                    {
                    int LA24_2 = input.LA(2);

                    if ( (LA24_2==27) ) {
                        int LA24_4 = input.LA(3);

                        if ( (LA24_4==RULE_ID) ) {
                            int LA24_6 = input.LA(4);

                            if ( (LA24_6==15||(LA24_6>=38 && LA24_6<=40)) ) {
                                alt24=1;
                            }
                        }
                    }
                    else if ( (LA24_2==15||(LA24_2>=38 && LA24_2<=40)) ) {
                        alt24=1;
                    }
                    }
                    break;
                case RULE_FLOAT:
                    {
                    int LA24_3 = input.LA(2);

                    if ( (LA24_3==15||(LA24_3>=38 && LA24_3<=40)) ) {
                        alt24=1;
                    }
                    }
                    break;
            }

            switch (alt24) {
                case 1 :
                    // InternalCQL.g:1564:4: (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1564:4: (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition )
                    // InternalCQL.g:1565:5: lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition
                    {

                    					newCompositeNode(grammarAccess.getFunctionExpressionAccess().getInnerexpressionFunctionExpressionWithoutAliasDefinitionParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_32);
                    lv_innerexpression_4_0=ruleFunctionExpressionWithoutAliasDefinition();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getFunctionExpressionRule());
                    					}
                    					set(
                    						current,
                    						"innerexpression",
                    						lv_innerexpression_4_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FunctionExpressionWithoutAliasDefinition");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:1582:3: ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= RULE_FLOAT ) ) )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==RULE_ID) ) {
                alt25=1;
            }
            else if ( (LA25_0==RULE_FLOAT) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // InternalCQL.g:1583:4: ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1583:4: ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1584:5: (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1584:5: (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1585:6: lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getFunctionExpressionAccess().getRightattributeAttributeWithoutAliasDefinitionParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_25);
                    lv_rightattribute_5_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionExpressionRule());
                    						}
                    						set(
                    							current,
                    							"rightattribute",
                    							lv_rightattribute_5_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1603:4: ( (lv_rightconstant_6_0= RULE_FLOAT ) )
                    {
                    // InternalCQL.g:1603:4: ( (lv_rightconstant_6_0= RULE_FLOAT ) )
                    // InternalCQL.g:1604:5: (lv_rightconstant_6_0= RULE_FLOAT )
                    {
                    // InternalCQL.g:1604:5: (lv_rightconstant_6_0= RULE_FLOAT )
                    // InternalCQL.g:1605:6: lv_rightconstant_6_0= RULE_FLOAT
                    {
                    lv_rightconstant_6_0=(Token)match(input,RULE_FLOAT,FOLLOW_25); 

                    						newLeafNode(lv_rightconstant_6_0, grammarAccess.getFunctionExpressionAccess().getRightconstantFLOATTerminalRuleCall_3_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"rightconstant",
                    							lv_rightconstant_6_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FLOAT");
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1622:3: (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==26) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // InternalCQL.g:1623:4: otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) )
                    {
                    otherlv_7=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_7, grammarAccess.getFunctionExpressionAccess().getASKeyword_4_0());
                    			
                    // InternalCQL.g:1627:4: ( (lv_alias_8_0= ruleAlias ) )
                    // InternalCQL.g:1628:5: (lv_alias_8_0= ruleAlias )
                    {
                    // InternalCQL.g:1628:5: (lv_alias_8_0= ruleAlias )
                    // InternalCQL.g:1629:6: lv_alias_8_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getFunctionExpressionAccess().getAliasAliasParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_8_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionExpressionRule());
                    						}
                    						set(
                    							current,
                    							"alias",
                    							lv_alias_8_0,
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
    // $ANTLR end "ruleFunctionExpression"


    // $ANTLR start "entryRuleFunctionExpressionWithoutAliasDefinition"
    // InternalCQL.g:1651:1: entryRuleFunctionExpressionWithoutAliasDefinition returns [EObject current=null] : iv_ruleFunctionExpressionWithoutAliasDefinition= ruleFunctionExpressionWithoutAliasDefinition EOF ;
    public final EObject entryRuleFunctionExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunctionExpressionWithoutAliasDefinition = null;


        try {
            // InternalCQL.g:1651:81: (iv_ruleFunctionExpressionWithoutAliasDefinition= ruleFunctionExpressionWithoutAliasDefinition EOF )
            // InternalCQL.g:1652:2: iv_ruleFunctionExpressionWithoutAliasDefinition= ruleFunctionExpressionWithoutAliasDefinition EOF
            {
             newCompositeNode(grammarAccess.getFunctionExpressionWithoutAliasDefinitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFunctionExpressionWithoutAliasDefinition=ruleFunctionExpressionWithoutAliasDefinition();

            state._fsp--;

             current =iv_ruleFunctionExpressionWithoutAliasDefinition; 
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
    // $ANTLR end "entryRuleFunctionExpressionWithoutAliasDefinition"


    // $ANTLR start "ruleFunctionExpressionWithoutAliasDefinition"
    // InternalCQL.g:1658:1: ruleFunctionExpressionWithoutAliasDefinition returns [EObject current=null] : ( ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )? ) ;
    public final EObject ruleFunctionExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        Token lv_rightconstant_2_0=null;
        Token lv_operator_3_1=null;
        Token lv_operator_3_2=null;
        Token lv_operator_3_3=null;
        Token lv_operator_3_4=null;
        EObject lv_function_0_0 = null;

        EObject lv_attribute_1_0 = null;

        EObject lv_innerexpression_4_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1664:2: ( ( ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )? ) )
            // InternalCQL.g:1665:2: ( ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )? )
            {
            // InternalCQL.g:1665:2: ( ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )? )
            // InternalCQL.g:1666:3: ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_2_0= RULE_FLOAT ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )?
            {
            // InternalCQL.g:1666:3: ( ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) ) | ( (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_2_0= RULE_FLOAT ) ) )
            int alt27=3;
            switch ( input.LA(1) ) {
            case 37:
                {
                alt27=1;
                }
                break;
            case RULE_ID:
                {
                alt27=2;
                }
                break;
            case RULE_FLOAT:
                {
                alt27=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // InternalCQL.g:1667:4: ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1667:4: ( (lv_function_0_0= ruleFunctionWithoutAliasDefinition ) )
                    // InternalCQL.g:1668:5: (lv_function_0_0= ruleFunctionWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1668:5: (lv_function_0_0= ruleFunctionWithoutAliasDefinition )
                    // InternalCQL.g:1669:6: lv_function_0_0= ruleFunctionWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getFunctionExpressionWithoutAliasDefinitionAccess().getFunctionFunctionWithoutAliasDefinitionParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_30);
                    lv_function_0_0=ruleFunctionWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionExpressionWithoutAliasDefinitionRule());
                    						}
                    						set(
                    							current,
                    							"function",
                    							lv_function_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FunctionWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1687:4: ( (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1687:4: ( (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1688:5: (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1688:5: (lv_attribute_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1689:6: lv_attribute_1_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getFunctionExpressionWithoutAliasDefinitionAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_30);
                    lv_attribute_1_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getFunctionExpressionWithoutAliasDefinitionRule());
                    						}
                    						set(
                    							current,
                    							"attribute",
                    							lv_attribute_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:1707:4: ( (lv_rightconstant_2_0= RULE_FLOAT ) )
                    {
                    // InternalCQL.g:1707:4: ( (lv_rightconstant_2_0= RULE_FLOAT ) )
                    // InternalCQL.g:1708:5: (lv_rightconstant_2_0= RULE_FLOAT )
                    {
                    // InternalCQL.g:1708:5: (lv_rightconstant_2_0= RULE_FLOAT )
                    // InternalCQL.g:1709:6: lv_rightconstant_2_0= RULE_FLOAT
                    {
                    lv_rightconstant_2_0=(Token)match(input,RULE_FLOAT,FOLLOW_30); 

                    						newLeafNode(lv_rightconstant_2_0, grammarAccess.getFunctionExpressionWithoutAliasDefinitionAccess().getRightconstantFLOATTerminalRuleCall_0_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionWithoutAliasDefinitionRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"rightconstant",
                    							lv_rightconstant_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FLOAT");
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1726:3: ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) )
            // InternalCQL.g:1727:4: ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) )
            {
            // InternalCQL.g:1727:4: ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) )
            // InternalCQL.g:1728:5: (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' )
            {
            // InternalCQL.g:1728:5: (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' )
            int alt28=4;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt28=1;
                }
                break;
            case 39:
                {
                alt28=2;
                }
                break;
            case 40:
                {
                alt28=3;
                }
                break;
            case 15:
                {
                alt28=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;
            }

            switch (alt28) {
                case 1 :
                    // InternalCQL.g:1729:6: lv_operator_3_1= '+'
                    {
                    lv_operator_3_1=(Token)match(input,38,FOLLOW_33); 

                    						newLeafNode(lv_operator_3_1, grammarAccess.getFunctionExpressionWithoutAliasDefinitionAccess().getOperatorPlusSignKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionWithoutAliasDefinitionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1740:6: lv_operator_3_2= '-'
                    {
                    lv_operator_3_2=(Token)match(input,39,FOLLOW_33); 

                    						newLeafNode(lv_operator_3_2, grammarAccess.getFunctionExpressionWithoutAliasDefinitionAccess().getOperatorHyphenMinusKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionWithoutAliasDefinitionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:1751:6: lv_operator_3_3= '/'
                    {
                    lv_operator_3_3=(Token)match(input,40,FOLLOW_33); 

                    						newLeafNode(lv_operator_3_3, grammarAccess.getFunctionExpressionWithoutAliasDefinitionAccess().getOperatorSolidusKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionWithoutAliasDefinitionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:1762:6: lv_operator_3_4= '*'
                    {
                    lv_operator_3_4=(Token)match(input,15,FOLLOW_33); 

                    						newLeafNode(lv_operator_3_4, grammarAccess.getFunctionExpressionWithoutAliasDefinitionAccess().getOperatorAsteriskKeyword_1_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getFunctionExpressionWithoutAliasDefinitionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_4, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:1775:3: ( (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition ) )?
            int alt29=2;
            switch ( input.LA(1) ) {
                case 37:
                    {
                    alt29=1;
                    }
                    break;
                case RULE_ID:
                    {
                    int LA29_2 = input.LA(2);

                    if ( (LA29_2==27) ) {
                        int LA29_5 = input.LA(3);

                        if ( (LA29_5==RULE_ID) ) {
                            int LA29_6 = input.LA(4);

                            if ( (LA29_6==15||(LA29_6>=38 && LA29_6<=40)) ) {
                                alt29=1;
                            }
                        }
                    }
                    else if ( (LA29_2==15||(LA29_2>=38 && LA29_2<=40)) ) {
                        alt29=1;
                    }
                    }
                    break;
                case RULE_FLOAT:
                    {
                    int LA29_3 = input.LA(2);

                    if ( (LA29_3==15||(LA29_3>=38 && LA29_3<=40)) ) {
                        alt29=1;
                    }
                    }
                    break;
            }

            switch (alt29) {
                case 1 :
                    // InternalCQL.g:1776:4: (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1776:4: (lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition )
                    // InternalCQL.g:1777:5: lv_innerexpression_4_0= ruleFunctionExpressionWithoutAliasDefinition
                    {

                    					newCompositeNode(grammarAccess.getFunctionExpressionWithoutAliasDefinitionAccess().getInnerexpressionFunctionExpressionWithoutAliasDefinitionParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_innerexpression_4_0=ruleFunctionExpressionWithoutAliasDefinition();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getFunctionExpressionWithoutAliasDefinitionRule());
                    					}
                    					set(
                    						current,
                    						"innerexpression",
                    						lv_innerexpression_4_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FunctionExpressionWithoutAliasDefinition");
                    					afterParserOrEnumRuleCall();
                    				

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
    // $ANTLR end "ruleFunctionExpressionWithoutAliasDefinition"


    // $ANTLR start "entryRuleAlias"
    // InternalCQL.g:1798:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQL.g:1798:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQL.g:1799:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQL.g:1805:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:1811:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:1812:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:1812:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:1813:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:1813:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:1814:4: lv_name_0_0= RULE_ID
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
    // InternalCQL.g:1833:1: entryRuleCreateParameters returns [EObject current=null] : iv_ruleCreateParameters= ruleCreateParameters EOF ;
    public final EObject entryRuleCreateParameters() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateParameters = null;


        try {
            // InternalCQL.g:1833:57: (iv_ruleCreateParameters= ruleCreateParameters EOF )
            // InternalCQL.g:1834:2: iv_ruleCreateParameters= ruleCreateParameters EOF
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
    // InternalCQL.g:1840:1: ruleCreateParameters returns [EObject current=null] : (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' ) ;
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
            // InternalCQL.g:1846:2: ( (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' ) )
            // InternalCQL.g:1847:2: (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' )
            {
            // InternalCQL.g:1847:2: (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' )
            // InternalCQL.g:1848:3: otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')'
            {
            otherlv_0=(Token)match(input,41,FOLLOW_34); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateParametersAccess().getWRAPPERKeyword_0());
            		
            // InternalCQL.g:1852:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQL.g:1853:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQL.g:1853:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQL.g:1854:5: lv_wrapper_1_0= RULE_STRING
            {
            lv_wrapper_1_0=(Token)match(input,RULE_STRING,FOLLOW_35); 

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

            otherlv_2=(Token)match(input,42,FOLLOW_34); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateParametersAccess().getPROTOCOLKeyword_2());
            		
            // InternalCQL.g:1874:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQL.g:1875:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQL.g:1875:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQL.g:1876:5: lv_protocol_3_0= RULE_STRING
            {
            lv_protocol_3_0=(Token)match(input,RULE_STRING,FOLLOW_36); 

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

            otherlv_4=(Token)match(input,43,FOLLOW_34); 

            			newLeafNode(otherlv_4, grammarAccess.getCreateParametersAccess().getTRANSPORTKeyword_4());
            		
            // InternalCQL.g:1896:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQL.g:1897:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQL.g:1897:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQL.g:1898:5: lv_transport_5_0= RULE_STRING
            {
            lv_transport_5_0=(Token)match(input,RULE_STRING,FOLLOW_37); 

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

            otherlv_6=(Token)match(input,44,FOLLOW_34); 

            			newLeafNode(otherlv_6, grammarAccess.getCreateParametersAccess().getDATAHANDLERKeyword_6());
            		
            // InternalCQL.g:1918:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQL.g:1919:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQL.g:1919:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQL.g:1920:5: lv_datahandler_7_0= RULE_STRING
            {
            lv_datahandler_7_0=(Token)match(input,RULE_STRING,FOLLOW_38); 

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

            otherlv_8=(Token)match(input,45,FOLLOW_29); 

            			newLeafNode(otherlv_8, grammarAccess.getCreateParametersAccess().getOPTIONSKeyword_8());
            		
            otherlv_9=(Token)match(input,22,FOLLOW_34); 

            			newLeafNode(otherlv_9, grammarAccess.getCreateParametersAccess().getLeftParenthesisKeyword_9());
            		
            // InternalCQL.g:1944:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+
            int cnt30=0;
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==RULE_STRING) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // InternalCQL.g:1945:4: ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) )
            	    {
            	    // InternalCQL.g:1945:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQL.g:1946:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:1946:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQL.g:1947:6: lv_keys_10_0= RULE_STRING
            	    {
            	    lv_keys_10_0=(Token)match(input,RULE_STRING,FOLLOW_34); 

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

            	    // InternalCQL.g:1963:4: ( (lv_values_11_0= RULE_STRING ) )
            	    // InternalCQL.g:1964:5: (lv_values_11_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:1964:5: (lv_values_11_0= RULE_STRING )
            	    // InternalCQL.g:1965:6: lv_values_11_0= RULE_STRING
            	    {
            	    lv_values_11_0=(Token)match(input,RULE_STRING,FOLLOW_39); 

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
            	    if ( cnt30 >= 1 ) break loop30;
                        EarlyExitException eee =
                            new EarlyExitException(30, input);
                        throw eee;
                }
                cnt30++;
            } while (true);

            // InternalCQL.g:1982:3: (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==16) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // InternalCQL.g:1983:4: otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) )
                    {
                    otherlv_12=(Token)match(input,16,FOLLOW_34); 

                    				newLeafNode(otherlv_12, grammarAccess.getCreateParametersAccess().getCommaKeyword_11_0());
                    			
                    // InternalCQL.g:1987:4: ( (lv_keys_13_0= RULE_STRING ) )
                    // InternalCQL.g:1988:5: (lv_keys_13_0= RULE_STRING )
                    {
                    // InternalCQL.g:1988:5: (lv_keys_13_0= RULE_STRING )
                    // InternalCQL.g:1989:6: lv_keys_13_0= RULE_STRING
                    {
                    lv_keys_13_0=(Token)match(input,RULE_STRING,FOLLOW_34); 

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

                    // InternalCQL.g:2005:4: ( (lv_values_14_0= RULE_STRING ) )
                    // InternalCQL.g:2006:5: (lv_values_14_0= RULE_STRING )
                    {
                    // InternalCQL.g:2006:5: (lv_values_14_0= RULE_STRING )
                    // InternalCQL.g:2007:6: lv_values_14_0= RULE_STRING
                    {
                    lv_values_14_0=(Token)match(input,RULE_STRING,FOLLOW_21); 

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

            otherlv_15=(Token)match(input,23,FOLLOW_2); 

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
    // InternalCQL.g:2032:1: entryRuleAttributeDefinition returns [EObject current=null] : iv_ruleAttributeDefinition= ruleAttributeDefinition EOF ;
    public final EObject entryRuleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeDefinition = null;


        try {
            // InternalCQL.g:2032:60: (iv_ruleAttributeDefinition= ruleAttributeDefinition EOF )
            // InternalCQL.g:2033:2: iv_ruleAttributeDefinition= ruleAttributeDefinition EOF
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
    // InternalCQL.g:2039:1: ruleAttributeDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' ) ;
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
            // InternalCQL.g:2045:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' ) )
            // InternalCQL.g:2046:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' )
            {
            // InternalCQL.g:2046:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' )
            // InternalCQL.g:2047:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')'
            {
            // InternalCQL.g:2047:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:2048:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:2048:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:2049:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_29); 

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

            otherlv_1=(Token)match(input,22,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeDefinitionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:2069:3: ( (lv_attributes_2_0= ruleAttribute ) )+
            int cnt32=0;
            loop32:
            do {
                int alt32=2;
                int LA32_0 = input.LA(1);

                if ( (LA32_0==RULE_ID) ) {
                    alt32=1;
                }


                switch (alt32) {
            	case 1 :
            	    // InternalCQL.g:2070:4: (lv_attributes_2_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2070:4: (lv_attributes_2_0= ruleAttribute )
            	    // InternalCQL.g:2071:5: lv_attributes_2_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getAttributesAttributeParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_40);
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
            	    if ( cnt32 >= 1 ) break loop32;
                        EarlyExitException eee =
                            new EarlyExitException(32, input);
                        throw eee;
                }
                cnt32++;
            } while (true);

            // InternalCQL.g:2088:3: ( (lv_datatypes_3_0= ruleDataType ) )+
            int cnt33=0;
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( ((LA33_0>=72 && LA33_0<=79)) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // InternalCQL.g:2089:4: (lv_datatypes_3_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2089:4: (lv_datatypes_3_0= ruleDataType )
            	    // InternalCQL.g:2090:5: lv_datatypes_3_0= ruleDataType
            	    {

            	    					newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getDatatypesDataTypeParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_41);
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
            	    if ( cnt33 >= 1 ) break loop33;
                        EarlyExitException eee =
                            new EarlyExitException(33, input);
                        throw eee;
                }
                cnt33++;
            } while (true);

            // InternalCQL.g:2107:3: (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==16) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // InternalCQL.g:2108:4: otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) )
            	    {
            	    otherlv_4=(Token)match(input,16,FOLLOW_8); 

            	    				newLeafNode(otherlv_4, grammarAccess.getAttributeDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQL.g:2112:4: ( (lv_attributes_5_0= ruleAttribute ) )
            	    // InternalCQL.g:2113:5: (lv_attributes_5_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2113:5: (lv_attributes_5_0= ruleAttribute )
            	    // InternalCQL.g:2114:6: lv_attributes_5_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getAttributesAttributeParserRuleCall_4_1_0());
            	    					
            	    pushFollow(FOLLOW_42);
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

            	    // InternalCQL.g:2131:4: ( (lv_datatypes_6_0= ruleDataType ) )
            	    // InternalCQL.g:2132:5: (lv_datatypes_6_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2132:5: (lv_datatypes_6_0= ruleDataType )
            	    // InternalCQL.g:2133:6: lv_datatypes_6_0= ruleDataType
            	    {

            	    						newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getDatatypesDataTypeParserRuleCall_4_2_0());
            	    					
            	    pushFollow(FOLLOW_43);
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
            	    break loop34;
                }
            } while (true);

            otherlv_7=(Token)match(input,23,FOLLOW_2); 

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
    // InternalCQL.g:2159:1: entryRuleCreateStream1 returns [EObject current=null] : iv_ruleCreateStream1= ruleCreateStream1 EOF ;
    public final EObject entryRuleCreateStream1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStream1 = null;


        try {
            // InternalCQL.g:2159:54: (iv_ruleCreateStream1= ruleCreateStream1 EOF )
            // InternalCQL.g:2160:2: iv_ruleCreateStream1= ruleCreateStream1 EOF
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
    // InternalCQL.g:2166:1: ruleCreateStream1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateStream1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2172:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQL.g:2173:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQL.g:2173:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQL.g:2174:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQL.g:2174:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2175:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2175:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2176:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_44);
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

            otherlv_1=(Token)match(input,46,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStream1Access().getSTREAMKeyword_1());
            		
            // InternalCQL.g:2197:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2198:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2198:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2199:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_45);
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

            // InternalCQL.g:2216:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQL.g:2217:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQL.g:2217:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQL.g:2218:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQL.g:2239:1: entryRuleCreateSink1 returns [EObject current=null] : iv_ruleCreateSink1= ruleCreateSink1 EOF ;
    public final EObject entryRuleCreateSink1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateSink1 = null;


        try {
            // InternalCQL.g:2239:52: (iv_ruleCreateSink1= ruleCreateSink1 EOF )
            // InternalCQL.g:2240:2: iv_ruleCreateSink1= ruleCreateSink1 EOF
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
    // InternalCQL.g:2246:1: ruleCreateSink1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateSink1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2252:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQL.g:2253:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQL.g:2253:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQL.g:2254:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQL.g:2254:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2255:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2255:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2256:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_46);
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

            otherlv_1=(Token)match(input,47,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateSink1Access().getSINKKeyword_1());
            		
            // InternalCQL.g:2277:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2278:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2278:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2279:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_45);
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

            // InternalCQL.g:2296:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQL.g:2297:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQL.g:2297:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQL.g:2298:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQL.g:2319:1: entryRuleCreateStreamChannel returns [EObject current=null] : iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF ;
    public final EObject entryRuleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamChannel = null;


        try {
            // InternalCQL.g:2319:60: (iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF )
            // InternalCQL.g:2320:2: iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF
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
    // InternalCQL.g:2326:1: ruleCreateStreamChannel returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) ) ;
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
            // InternalCQL.g:2332:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) ) )
            // InternalCQL.g:2333:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) )
            {
            // InternalCQL.g:2333:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) )
            // InternalCQL.g:2334:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) )
            {
            // InternalCQL.g:2334:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2335:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2335:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2336:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStreamChannelAccess().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_44);
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

            otherlv_1=(Token)match(input,46,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamChannelAccess().getSTREAMKeyword_1());
            		
            // InternalCQL.g:2357:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2358:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2358:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2359:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamChannelAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_47);
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

            otherlv_3=(Token)match(input,48,FOLLOW_8); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamChannelAccess().getCHANNELKeyword_3());
            		
            // InternalCQL.g:2380:3: ( (lv_host_4_0= RULE_ID ) )
            // InternalCQL.g:2381:4: (lv_host_4_0= RULE_ID )
            {
            // InternalCQL.g:2381:4: (lv_host_4_0= RULE_ID )
            // InternalCQL.g:2382:5: lv_host_4_0= RULE_ID
            {
            lv_host_4_0=(Token)match(input,RULE_ID,FOLLOW_48); 

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

            otherlv_5=(Token)match(input,49,FOLLOW_49); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamChannelAccess().getColonKeyword_5());
            		
            // InternalCQL.g:2402:3: ( (lv_port_6_0= RULE_INT ) )
            // InternalCQL.g:2403:4: (lv_port_6_0= RULE_INT )
            {
            // InternalCQL.g:2403:4: (lv_port_6_0= RULE_INT )
            // InternalCQL.g:2404:5: lv_port_6_0= RULE_INT
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
    // InternalCQL.g:2424:1: entryRuleCreateStreamFile returns [EObject current=null] : iv_ruleCreateStreamFile= ruleCreateStreamFile EOF ;
    public final EObject entryRuleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamFile = null;


        try {
            // InternalCQL.g:2424:57: (iv_ruleCreateStreamFile= ruleCreateStreamFile EOF )
            // InternalCQL.g:2425:2: iv_ruleCreateStreamFile= ruleCreateStreamFile EOF
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
    // InternalCQL.g:2431:1: ruleCreateStreamFile returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) ) ;
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
            // InternalCQL.g:2437:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) ) )
            // InternalCQL.g:2438:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) )
            {
            // InternalCQL.g:2438:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) )
            // InternalCQL.g:2439:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) )
            {
            // InternalCQL.g:2439:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2440:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2440:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2441:5: lv_keyword_0_0= ruleCreateKeyword
            {

            					newCompositeNode(grammarAccess.getCreateStreamFileAccess().getKeywordCreateKeywordEnumRuleCall_0_0());
            				
            pushFollow(FOLLOW_44);
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

            otherlv_1=(Token)match(input,46,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamFileAccess().getSTREAMKeyword_1());
            		
            // InternalCQL.g:2462:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2463:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2463:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2464:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamFileAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_50);
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

            otherlv_3=(Token)match(input,50,FOLLOW_34); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamFileAccess().getFILEKeyword_3());
            		
            // InternalCQL.g:2485:3: ( (lv_filename_4_0= RULE_STRING ) )
            // InternalCQL.g:2486:4: (lv_filename_4_0= RULE_STRING )
            {
            // InternalCQL.g:2486:4: (lv_filename_4_0= RULE_STRING )
            // InternalCQL.g:2487:5: lv_filename_4_0= RULE_STRING
            {
            lv_filename_4_0=(Token)match(input,RULE_STRING,FOLLOW_26); 

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

            otherlv_5=(Token)match(input,26,FOLLOW_8); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamFileAccess().getASKeyword_5());
            		
            // InternalCQL.g:2507:3: ( (lv_type_6_0= RULE_ID ) )
            // InternalCQL.g:2508:4: (lv_type_6_0= RULE_ID )
            {
            // InternalCQL.g:2508:4: (lv_type_6_0= RULE_ID )
            // InternalCQL.g:2509:5: lv_type_6_0= RULE_ID
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
    // InternalCQL.g:2529:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQL.g:2529:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQL.g:2530:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQL.g:2536:1: ruleCreateView returns [EObject current=null] : (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2542:2: ( (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) ) )
            // InternalCQL.g:2543:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) )
            {
            // InternalCQL.g:2543:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) )
            // InternalCQL.g:2544:3: otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) )
            {
            otherlv_0=(Token)match(input,51,FOLLOW_8); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateViewAccess().getVIEWKeyword_0());
            		
            // InternalCQL.g:2548:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:2549:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:2549:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:2550:5: lv_name_1_0= RULE_ID
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

            otherlv_2=(Token)match(input,17,FOLLOW_12); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateViewAccess().getFROMKeyword_2());
            		
            // InternalCQL.g:2570:3: ( (lv_select_3_0= ruleNestedStatement ) )
            // InternalCQL.g:2571:4: (lv_select_3_0= ruleNestedStatement )
            {
            // InternalCQL.g:2571:4: (lv_select_3_0= ruleNestedStatement )
            // InternalCQL.g:2572:5: lv_select_3_0= ruleNestedStatement
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
    // InternalCQL.g:2593:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQL.g:2593:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQL.g:2594:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQL.g:2600:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2606:2: ( (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQL.g:2607:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQL.g:2607:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:2608:3: otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,46,FOLLOW_51); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,52,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQL.g:2616:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQL.g:2617:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQL.g:2617:4: (lv_name_2_0= RULE_ID )
            // InternalCQL.g:2618:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_52); 

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

            // InternalCQL.g:2634:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==13) ) {
                alt35=1;
            }
            else if ( (LA35_0==RULE_ID) ) {
                alt35=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;
            }
            switch (alt35) {
                case 1 :
                    // InternalCQL.g:2635:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQL.g:2635:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQL.g:2636:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQL.g:2636:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQL.g:2637:6: lv_statement_3_0= ruleSelect
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
                    // InternalCQL.g:2655:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQL.g:2655:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQL.g:2656:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQL.g:2656:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQL.g:2657:6: lv_inputname_4_0= RULE_ID
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
    // InternalCQL.g:2678:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQL.g:2678:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQL.g:2679:2: iv_ruleDrop= ruleDrop EOF
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
    // InternalCQL.g:2685:1: ruleDrop returns [EObject current=null] : ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) ) ;
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
            // InternalCQL.g:2691:2: ( ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:2692:2: ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            {
            // InternalCQL.g:2692:2: ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            // InternalCQL.g:2693:3: ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) )
            {
            // InternalCQL.g:2693:3: ( (lv_keyword1_0_0= 'DROP' ) )
            // InternalCQL.g:2694:4: (lv_keyword1_0_0= 'DROP' )
            {
            // InternalCQL.g:2694:4: (lv_keyword1_0_0= 'DROP' )
            // InternalCQL.g:2695:5: lv_keyword1_0_0= 'DROP'
            {
            lv_keyword1_0_0=(Token)match(input,53,FOLLOW_53); 

            					newLeafNode(lv_keyword1_0_0, grammarAccess.getDropAccess().getKeyword1DROPKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(current, "keyword1", lv_keyword1_0_0, "DROP");
            				

            }


            }

            // InternalCQL.g:2707:3: ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) )
            // InternalCQL.g:2708:4: ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) )
            {
            // InternalCQL.g:2708:4: ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) )
            // InternalCQL.g:2709:5: (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' )
            {
            // InternalCQL.g:2709:5: (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==47) ) {
                alt36=1;
            }
            else if ( (LA36_0==46) ) {
                alt36=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // InternalCQL.g:2710:6: lv_keyword2_1_1= 'SINK'
                    {
                    lv_keyword2_1_1=(Token)match(input,47,FOLLOW_8); 

                    						newLeafNode(lv_keyword2_1_1, grammarAccess.getDropAccess().getKeyword2SINKKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "keyword2", lv_keyword2_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:2721:6: lv_keyword2_1_2= 'STREAM'
                    {
                    lv_keyword2_1_2=(Token)match(input,46,FOLLOW_8); 

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

            // InternalCQL.g:2734:3: ( (lv_value1_2_0= RULE_ID ) )
            // InternalCQL.g:2735:4: (lv_value1_2_0= RULE_ID )
            {
            // InternalCQL.g:2735:4: (lv_value1_2_0= RULE_ID )
            // InternalCQL.g:2736:5: lv_value1_2_0= RULE_ID
            {
            lv_value1_2_0=(Token)match(input,RULE_ID,FOLLOW_54); 

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

            // InternalCQL.g:2752:3: ( (lv_keyword3_3_0= 'IF EXISTS' ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==54) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalCQL.g:2753:4: (lv_keyword3_3_0= 'IF EXISTS' )
                    {
                    // InternalCQL.g:2753:4: (lv_keyword3_3_0= 'IF EXISTS' )
                    // InternalCQL.g:2754:5: lv_keyword3_3_0= 'IF EXISTS'
                    {
                    lv_keyword3_3_0=(Token)match(input,54,FOLLOW_8); 

                    					newLeafNode(lv_keyword3_3_0, grammarAccess.getDropAccess().getKeyword3IFEXISTSKeyword_3_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDropRule());
                    					}
                    					setWithLastConsumed(current, "keyword3", lv_keyword3_3_0, "IF EXISTS");
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:2766:3: ( (lv_value2_4_0= RULE_ID ) )
            // InternalCQL.g:2767:4: (lv_value2_4_0= RULE_ID )
            {
            // InternalCQL.g:2767:4: (lv_value2_4_0= RULE_ID )
            // InternalCQL.g:2768:5: lv_value2_4_0= RULE_ID
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
    // InternalCQL.g:2788:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQL.g:2788:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQL.g:2789:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQL.g:2795:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'UNBOUNDED' ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQL.g:2801:2: (kw= 'UNBOUNDED' )
            // InternalCQL.g:2802:2: kw= 'UNBOUNDED'
            {
            kw=(Token)match(input,55,FOLLOW_2); 

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
    // InternalCQL.g:2810:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQL.g:2810:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQL.g:2811:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQL.g:2817:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) ;
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
            // InternalCQL.g:2823:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) )
            // InternalCQL.g:2824:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            {
            // InternalCQL.g:2824:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            // InternalCQL.g:2825:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME'
            {
            otherlv_0=(Token)match(input,56,FOLLOW_49); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:2829:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:2830:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:2830:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:2831:5: lv_size_1_0= RULE_INT
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

            // InternalCQL.g:2847:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQL.g:2848:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQL.g:2848:4: (lv_unit_2_0= RULE_ID )
            // InternalCQL.g:2849:5: lv_unit_2_0= RULE_ID
            {
            lv_unit_2_0=(Token)match(input,RULE_ID,FOLLOW_55); 

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

            // InternalCQL.g:2865:3: (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==57) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // InternalCQL.g:2866:4: otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,57,FOLLOW_49); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQL.g:2870:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQL.g:2871:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQL.g:2871:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQL.g:2872:6: lv_advance_size_4_0= RULE_INT
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

                    // InternalCQL.g:2888:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQL.g:2889:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQL.g:2889:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQL.g:2890:6: lv_advance_unit_5_0= RULE_ID
                    {
                    lv_advance_unit_5_0=(Token)match(input,RULE_ID,FOLLOW_56); 

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

            otherlv_6=(Token)match(input,58,FOLLOW_2); 

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
    // InternalCQL.g:2915:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQL.g:2915:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQL.g:2916:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQL.g:2922:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQL.g:2928:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQL.g:2929:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQL.g:2929:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQL.g:2930:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,56,FOLLOW_49); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:2934:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:2935:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:2935:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:2936:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_57); 

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

            // InternalCQL.g:2952:3: (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==57) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // InternalCQL.g:2953:4: otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,57,FOLLOW_49); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQL.g:2957:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQL.g:2958:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQL.g:2958:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQL.g:2959:6: lv_advance_size_3_0= RULE_INT
                    {
                    lv_advance_size_3_0=(Token)match(input,RULE_INT,FOLLOW_58); 

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

            otherlv_4=(Token)match(input,59,FOLLOW_59); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQL.g:2980:3: (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==60) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalCQL.g:2981:4: otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,60,FOLLOW_17); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,20,FOLLOW_8); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQL.g:2989:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQL.g:2990:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQL.g:2990:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQL.g:2991:6: lv_partition_attribute_7_0= ruleAttribute
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
    // InternalCQL.g:3013:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQL.g:3013:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQL.g:3014:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQL.g:3020:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3026:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQL.g:3027:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQL.g:3027:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQL.g:3028:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQL.g:3028:3: ()
            // InternalCQL.g:3029:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQL.g:3035:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQL.g:3036:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQL.g:3036:4: (lv_elements_1_0= ruleExpression )
            // InternalCQL.g:3037:5: lv_elements_1_0= ruleExpression
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
    // InternalCQL.g:3058:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQL.g:3058:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQL.g:3059:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQL.g:3065:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3071:2: (this_Or_0= ruleOr )
            // InternalCQL.g:3072:2: this_Or_0= ruleOr
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
    // InternalCQL.g:3083:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQL.g:3083:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQL.g:3084:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQL.g:3090:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3096:2: ( (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQL.g:3097:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQL.g:3097:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQL.g:3098:3: this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_60);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3106:3: ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==61) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // InternalCQL.g:3107:4: () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQL.g:3107:4: ()
            	    // InternalCQL.g:3108:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,61,FOLLOW_15); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQL.g:3118:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQL.g:3119:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQL.g:3119:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQL.g:3120:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_60);
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
            	    break loop41;
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
    // InternalCQL.g:3142:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQL.g:3142:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQL.g:3143:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQL.g:3149:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3155:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQL.g:3156:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQL.g:3156:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQL.g:3157:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_61);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3165:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==62) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // InternalCQL.g:3166:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQL.g:3166:4: ()
            	    // InternalCQL.g:3167:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,62,FOLLOW_15); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQL.g:3177:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQL.g:3178:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQL.g:3178:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQL.g:3179:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_61);
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
    // $ANTLR end "ruleAnd"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQL.g:3201:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQL.g:3201:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQL.g:3202:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQL.g:3208:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3214:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQL.g:3215:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQL.g:3215:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQL.g:3216:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_62);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3224:3: ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( ((LA44_0>=63 && LA44_0<=64)) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // InternalCQL.g:3225:4: () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQL.g:3225:4: ()
            	    // InternalCQL.g:3226:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:3232:4: ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) )
            	    // InternalCQL.g:3233:5: ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) )
            	    {
            	    // InternalCQL.g:3233:5: ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) )
            	    // InternalCQL.g:3234:6: (lv_op_2_1= '=' | lv_op_2_2= '!=' )
            	    {
            	    // InternalCQL.g:3234:6: (lv_op_2_1= '=' | lv_op_2_2= '!=' )
            	    int alt43=2;
            	    int LA43_0 = input.LA(1);

            	    if ( (LA43_0==63) ) {
            	        alt43=1;
            	    }
            	    else if ( (LA43_0==64) ) {
            	        alt43=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 43, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt43) {
            	        case 1 :
            	            // InternalCQL.g:3235:7: lv_op_2_1= '='
            	            {
            	            lv_op_2_1=(Token)match(input,63,FOLLOW_15); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:3246:7: lv_op_2_2= '!='
            	            {
            	            lv_op_2_2=(Token)match(input,64,FOLLOW_15); 

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

            	    // InternalCQL.g:3259:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQL.g:3260:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQL.g:3260:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQL.g:3261:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_62);
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
            	    break loop44;
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
    // InternalCQL.g:3283:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQL.g:3283:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQL.g:3284:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQL.g:3290:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQL.g:3296:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQL.g:3297:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQL.g:3297:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQL.g:3298:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_63);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3306:3: ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( ((LA46_0>=65 && LA46_0<=68)) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // InternalCQL.g:3307:4: () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQL.g:3307:4: ()
            	    // InternalCQL.g:3308:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:3314:4: ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) )
            	    // InternalCQL.g:3315:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    {
            	    // InternalCQL.g:3315:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    // InternalCQL.g:3316:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    {
            	    // InternalCQL.g:3316:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    int alt45=4;
            	    switch ( input.LA(1) ) {
            	    case 65:
            	        {
            	        alt45=1;
            	        }
            	        break;
            	    case 66:
            	        {
            	        alt45=2;
            	        }
            	        break;
            	    case 67:
            	        {
            	        alt45=3;
            	        }
            	        break;
            	    case 68:
            	        {
            	        alt45=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 45, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt45) {
            	        case 1 :
            	            // InternalCQL.g:3317:7: lv_op_2_1= '>='
            	            {
            	            lv_op_2_1=(Token)match(input,65,FOLLOW_15); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:3328:7: lv_op_2_2= '<='
            	            {
            	            lv_op_2_2=(Token)match(input,66,FOLLOW_15); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQL.g:3339:7: lv_op_2_3= '<'
            	            {
            	            lv_op_2_3=(Token)match(input,67,FOLLOW_15); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQL.g:3350:7: lv_op_2_4= '>'
            	            {
            	            lv_op_2_4=(Token)match(input,68,FOLLOW_15); 

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

            	    // InternalCQL.g:3363:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQL.g:3364:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQL.g:3364:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQL.g:3365:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_63);
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
            	    break loop46;
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
    // InternalCQL.g:3387:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQL.g:3387:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQL.g:3388:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQL.g:3394:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3400:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQL.g:3401:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQL.g:3401:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQL.g:3402:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_64);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3410:3: ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( ((LA48_0>=38 && LA48_0<=39)) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // InternalCQL.g:3411:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQL.g:3411:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) )
            	    int alt47=2;
            	    int LA47_0 = input.LA(1);

            	    if ( (LA47_0==38) ) {
            	        alt47=1;
            	    }
            	    else if ( (LA47_0==39) ) {
            	        alt47=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 47, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt47) {
            	        case 1 :
            	            // InternalCQL.g:3412:5: ( () otherlv_2= '+' )
            	            {
            	            // InternalCQL.g:3412:5: ( () otherlv_2= '+' )
            	            // InternalCQL.g:3413:6: () otherlv_2= '+'
            	            {
            	            // InternalCQL.g:3413:6: ()
            	            // InternalCQL.g:3414:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,38,FOLLOW_15); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:3426:5: ( () otherlv_4= '-' )
            	            {
            	            // InternalCQL.g:3426:5: ( () otherlv_4= '-' )
            	            // InternalCQL.g:3427:6: () otherlv_4= '-'
            	            {
            	            // InternalCQL.g:3427:6: ()
            	            // InternalCQL.g:3428:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,39,FOLLOW_15); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQL.g:3440:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQL.g:3441:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQL.g:3441:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQL.g:3442:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_64);
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
            	    break loop48;
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
    // InternalCQL.g:3464:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQL.g:3464:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQL.g:3465:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQL.g:3471:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3477:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQL.g:3478:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQL.g:3478:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQL.g:3479:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_65);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3487:3: ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop50:
            do {
                int alt50=2;
                int LA50_0 = input.LA(1);

                if ( (LA50_0==15||LA50_0==40) ) {
                    alt50=1;
                }


                switch (alt50) {
            	case 1 :
            	    // InternalCQL.g:3488:4: () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQL.g:3488:4: ()
            	    // InternalCQL.g:3489:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:3495:4: ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) )
            	    // InternalCQL.g:3496:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    {
            	    // InternalCQL.g:3496:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    // InternalCQL.g:3497:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    {
            	    // InternalCQL.g:3497:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    int alt49=2;
            	    int LA49_0 = input.LA(1);

            	    if ( (LA49_0==15) ) {
            	        alt49=1;
            	    }
            	    else if ( (LA49_0==40) ) {
            	        alt49=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 49, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt49) {
            	        case 1 :
            	            // InternalCQL.g:3498:7: lv_op_2_1= '*'
            	            {
            	            lv_op_2_1=(Token)match(input,15,FOLLOW_15); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:3509:7: lv_op_2_2= '/'
            	            {
            	            lv_op_2_2=(Token)match(input,40,FOLLOW_15); 

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

            	    // InternalCQL.g:3522:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQL.g:3523:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQL.g:3523:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQL.g:3524:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_65);
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
    // $ANTLR end "ruleMulOrDiv"


    // $ANTLR start "entryRulePrimary"
    // InternalCQL.g:3546:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQL.g:3546:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQL.g:3547:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQL.g:3553:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQL.g:3559:2: ( ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQL.g:3560:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQL.g:3560:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt51=3;
            switch ( input.LA(1) ) {
            case 22:
                {
                alt51=1;
                }
                break;
            case 69:
                {
                alt51=2;
                }
                break;
            case RULE_ID:
            case RULE_FLOAT:
            case RULE_STRING:
            case RULE_INT:
            case 70:
            case 71:
                {
                alt51=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                throw nvae;
            }

            switch (alt51) {
                case 1 :
                    // InternalCQL.g:3561:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    {
                    // InternalCQL.g:3561:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    // InternalCQL.g:3562:4: () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')'
                    {
                    // InternalCQL.g:3562:4: ()
                    // InternalCQL.g:3563:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,22,FOLLOW_15); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQL.g:3573:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQL.g:3574:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQL.g:3574:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQL.g:3575:6: lv_inner_2_0= ruleExpression
                    {

                    						newCompositeNode(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_21);
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

                    otherlv_3=(Token)match(input,23,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:3598:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQL.g:3598:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQL.g:3599:4: () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQL.g:3599:4: ()
                    // InternalCQL.g:3600:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,69,FOLLOW_15); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQL.g:3610:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQL.g:3611:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQL.g:3611:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQL.g:3612:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQL.g:3631:3: this_Atomic_7= ruleAtomic
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
    // InternalCQL.g:3643:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQL.g:3643:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQL.g:3644:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQL.g:3650:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
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
            // InternalCQL.g:3656:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQL.g:3657:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQL.g:3657:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt54=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt54=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt54=2;
                }
                break;
            case RULE_STRING:
                {
                alt54=3;
                }
                break;
            case 70:
            case 71:
                {
                alt54=4;
                }
                break;
            case RULE_ID:
                {
                alt54=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }

            switch (alt54) {
                case 1 :
                    // InternalCQL.g:3658:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:3658:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:3659:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:3659:4: ()
                    // InternalCQL.g:3660:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:3666:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:3667:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:3667:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:3668:6: lv_value_1_0= RULE_INT
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
                    // InternalCQL.g:3686:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQL.g:3686:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQL.g:3687:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQL.g:3687:4: ()
                    // InternalCQL.g:3688:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:3694:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQL.g:3695:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQL.g:3695:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQL.g:3696:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQL.g:3714:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:3714:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:3715:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:3715:4: ()
                    // InternalCQL.g:3716:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:3722:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:3723:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:3723:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:3724:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQL.g:3742:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:3742:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:3743:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:3743:4: ()
                    // InternalCQL.g:3744:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:3750:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:3751:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:3751:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:3752:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:3752:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt52=2;
                    int LA52_0 = input.LA(1);

                    if ( (LA52_0==70) ) {
                        alt52=1;
                    }
                    else if ( (LA52_0==71) ) {
                        alt52=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 52, 0, input);

                        throw nvae;
                    }
                    switch (alt52) {
                        case 1 :
                            // InternalCQL.g:3753:7: lv_value_7_1= 'TRUE'
                            {
                            lv_value_7_1=(Token)match(input,70,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQL.g:3764:7: lv_value_7_2= 'FALSE'
                            {
                            lv_value_7_2=(Token)match(input,71,FOLLOW_2); 

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
                    // InternalCQL.g:3779:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQL.g:3779:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQL.g:3780:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQL.g:3780:4: ()
                    // InternalCQL.g:3781:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:3787:4: ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt53=2;
                    int LA53_0 = input.LA(1);

                    if ( (LA53_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
                        case 27:
                            {
                            int LA53_2 = input.LA(3);

                            if ( (LA53_2==RULE_ID) ) {
                                int LA53_5 = input.LA(4);

                                if ( (LA53_5==EOF||LA53_5==12||LA53_5==15||LA53_5==19||LA53_5==21||LA53_5==23||(LA53_5>=38 && LA53_5<=40)||(LA53_5>=61 && LA53_5<=68)) ) {
                                    alt53=1;
                                }
                                else if ( (LA53_5==28) ) {
                                    alt53=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 53, 5, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 53, 2, input);

                                throw nvae;
                            }
                            }
                            break;
                        case EOF:
                        case 12:
                        case 15:
                        case 19:
                        case 21:
                        case 23:
                        case 38:
                        case 39:
                        case 40:
                        case 61:
                        case 62:
                        case 63:
                        case 64:
                        case 65:
                        case 66:
                        case 67:
                        case 68:
                            {
                            alt53=1;
                            }
                            break;
                        case 28:
                            {
                            alt53=2;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 53, 1, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 53, 0, input);

                        throw nvae;
                    }
                    switch (alt53) {
                        case 1 :
                            // InternalCQL.g:3788:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            {
                            // InternalCQL.g:3788:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            // InternalCQL.g:3789:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            {
                            // InternalCQL.g:3789:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            // InternalCQL.g:3790:7: lv_value_9_0= ruleAttributeWithoutAliasDefinition
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
                            // InternalCQL.g:3808:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQL.g:3808:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQL.g:3809:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQL.g:3809:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQL.g:3810:7: lv_value_10_0= ruleAttributeWithNestedStatement
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


    // $ANTLR start "entryRuleDataType"
    // InternalCQL.g:3833:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQL.g:3833:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQL.g:3834:2: iv_ruleDataType= ruleDataType EOF
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
    // InternalCQL.g:3840:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) ) ;
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
            // InternalCQL.g:3846:2: ( ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) ) )
            // InternalCQL.g:3847:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) )
            {
            // InternalCQL.g:3847:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) )
            // InternalCQL.g:3848:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) )
            {
            // InternalCQL.g:3848:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) )
            // InternalCQL.g:3849:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' )
            {
            // InternalCQL.g:3849:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' )
            int alt55=8;
            switch ( input.LA(1) ) {
            case 72:
                {
                alt55=1;
                }
                break;
            case 73:
                {
                alt55=2;
                }
                break;
            case 74:
                {
                alt55=3;
                }
                break;
            case 75:
                {
                alt55=4;
                }
                break;
            case 76:
                {
                alt55=5;
                }
                break;
            case 77:
                {
                alt55=6;
                }
                break;
            case 78:
                {
                alt55=7;
                }
                break;
            case 79:
                {
                alt55=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 55, 0, input);

                throw nvae;
            }

            switch (alt55) {
                case 1 :
                    // InternalCQL.g:3850:5: lv_value_0_1= 'INTEGER'
                    {
                    lv_value_0_1=(Token)match(input,72,FOLLOW_2); 

                    					newLeafNode(lv_value_0_1, grammarAccess.getDataTypeAccess().getValueINTEGERKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_1, null);
                    				

                    }
                    break;
                case 2 :
                    // InternalCQL.g:3861:5: lv_value_0_2= 'DOUBLE'
                    {
                    lv_value_0_2=(Token)match(input,73,FOLLOW_2); 

                    					newLeafNode(lv_value_0_2, grammarAccess.getDataTypeAccess().getValueDOUBLEKeyword_0_1());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_2, null);
                    				

                    }
                    break;
                case 3 :
                    // InternalCQL.g:3872:5: lv_value_0_3= 'LONG'
                    {
                    lv_value_0_3=(Token)match(input,74,FOLLOW_2); 

                    					newLeafNode(lv_value_0_3, grammarAccess.getDataTypeAccess().getValueLONGKeyword_0_2());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_3, null);
                    				

                    }
                    break;
                case 4 :
                    // InternalCQL.g:3883:5: lv_value_0_4= 'FLOAT'
                    {
                    lv_value_0_4=(Token)match(input,75,FOLLOW_2); 

                    					newLeafNode(lv_value_0_4, grammarAccess.getDataTypeAccess().getValueFLOATKeyword_0_3());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_4, null);
                    				

                    }
                    break;
                case 5 :
                    // InternalCQL.g:3894:5: lv_value_0_5= 'STRING'
                    {
                    lv_value_0_5=(Token)match(input,76,FOLLOW_2); 

                    					newLeafNode(lv_value_0_5, grammarAccess.getDataTypeAccess().getValueSTRINGKeyword_0_4());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_5, null);
                    				

                    }
                    break;
                case 6 :
                    // InternalCQL.g:3905:5: lv_value_0_6= 'BOOLEAN'
                    {
                    lv_value_0_6=(Token)match(input,77,FOLLOW_2); 

                    					newLeafNode(lv_value_0_6, grammarAccess.getDataTypeAccess().getValueBOOLEANKeyword_0_5());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_6, null);
                    				

                    }
                    break;
                case 7 :
                    // InternalCQL.g:3916:5: lv_value_0_7= 'STARTTIMESTAMP'
                    {
                    lv_value_0_7=(Token)match(input,78,FOLLOW_2); 

                    					newLeafNode(lv_value_0_7, grammarAccess.getDataTypeAccess().getValueSTARTTIMESTAMPKeyword_0_6());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_7, null);
                    				

                    }
                    break;
                case 8 :
                    // InternalCQL.g:3927:5: lv_value_0_8= 'ENDTIMESTAMP'
                    {
                    lv_value_0_8=(Token)match(input,79,FOLLOW_2); 

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
    // InternalCQL.g:3943:1: ruleCreateKeyword returns [Enumerator current=null] : ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) ) ;
    public final Enumerator ruleCreateKeyword() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCQL.g:3949:2: ( ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) ) )
            // InternalCQL.g:3950:2: ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) )
            {
            // InternalCQL.g:3950:2: ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==80) ) {
                alt56=1;
            }
            else if ( (LA56_0==81) ) {
                alt56=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // InternalCQL.g:3951:3: (enumLiteral_0= 'CREATE' )
                    {
                    // InternalCQL.g:3951:3: (enumLiteral_0= 'CREATE' )
                    // InternalCQL.g:3952:4: enumLiteral_0= 'CREATE'
                    {
                    enumLiteral_0=(Token)match(input,80,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:3959:3: (enumLiteral_1= 'ATTACH' )
                    {
                    // InternalCQL.g:3959:3: (enumLiteral_1= 'ATTACH' )
                    // InternalCQL.g:3960:4: enumLiteral_1= 'ATTACH'
                    {
                    enumLiteral_1=(Token)match(input,81,FOLLOW_2); 

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
    protected DFA4 dfa4 = new DFA4(this);
    protected DFA5 dfa5 = new DFA5(this);
    static final String dfa_1s = "\52\uffff";
    static final String dfa_2s = "\1\15\3\uffff\2\56\1\uffff\1\4\1\uffff\1\26\3\4\10\20\3\4\1\51\1\4\1\32\3\uffff\1\4\10\20\1\4\1\110\1\32";
    static final String dfa_3s = "\1\121\3\uffff\2\57\1\uffff\1\4\1\uffff\1\26\1\4\1\117\1\4\10\117\1\4\1\117\1\4\1\62\2\117\3\uffff\1\4\10\27\1\4\2\117";
    static final String dfa_4s = "\1\uffff\1\1\1\2\1\3\2\uffff\1\10\1\uffff\1\5\22\uffff\1\7\1\4\1\6\14\uffff";
    static final String dfa_5s = "\52\uffff}>";
    static final String[] dfa_6s = {
            "\1\1\40\uffff\1\2\4\uffff\1\6\1\uffff\1\3\32\uffff\1\4\1\5",
            "",
            "",
            "",
            "\1\7\1\10",
            "\1\7\1\10",
            "",
            "\1\11",
            "",
            "\1\12",
            "\1\13",
            "\1\13\25\uffff\1\14\1\25\54\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\26",
            "\1\27\6\uffff\1\30\60\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\27\6\uffff\1\30\60\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\27\6\uffff\1\30\60\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\27\6\uffff\1\30\60\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\27\6\uffff\1\30\60\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\27\6\uffff\1\30\60\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\27\6\uffff\1\30\60\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\27\6\uffff\1\30\60\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\31",
            "\1\13\103\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\32",
            "\1\34\6\uffff\1\35\1\uffff\1\33",
            "\1\13\25\uffff\1\14\55\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\36\1\47\54\uffff\1\37\1\40\1\41\1\42\1\43\1\44\1\45\1\46",
            "",
            "",
            "",
            "\1\50",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\51",
            "\1\37\1\40\1\41\1\42\1\43\1\44\1\45\1\46",
            "\1\36\55\uffff\1\37\1\40\1\41\1\42\1\43\1\44\1\45\1\46"
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
            return "116:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) )";
        }
    }
    static final String dfa_7s = "\17\uffff";
    static final String dfa_8s = "\1\4\1\uffff\1\4\1\uffff\1\26\1\uffff\1\4\1\uffff\2\4\1\27\1\4\1\17\1\27\1\uffff";
    static final String dfa_9s = "\1\45\1\uffff\1\50\1\uffff\1\26\1\uffff\1\4\1\uffff\1\4\1\50\1\33\1\4\1\50\1\27\1\uffff";
    static final String dfa_10s = "\1\uffff\1\5\1\uffff\1\2\1\uffff\1\4\1\uffff\1\1\6\uffff\1\3";
    static final String dfa_11s = "\17\uffff}>";
    static final String[] dfa_12s = {
            "\1\2\1\5\12\uffff\2\1\13\uffff\10\3\1\4",
            "",
            "\2\7\11\uffff\1\5\2\7\10\uffff\1\7\1\6\1\uffff\11\7\3\5",
            "",
            "\1\10",
            "",
            "\1\11",
            "",
            "\1\12",
            "\2\7\11\uffff\1\5\2\7\10\uffff\1\7\2\uffff\11\7\3\5",
            "\1\14\3\uffff\1\13",
            "\1\15",
            "\1\5\12\uffff\1\16\13\uffff\3\5",
            "\1\14",
            ""
    };

    static final short[] dfa_7 = DFA.unpackEncodedString(dfa_7s);
    static final char[] dfa_8 = DFA.unpackEncodedStringToUnsignedChars(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final short[] dfa_10 = DFA.unpackEncodedString(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[][] dfa_12 = unpackEncodedStringArray(dfa_12s);

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = dfa_7;
            this.eof = dfa_7;
            this.min = dfa_8;
            this.max = dfa_9;
            this.accept = dfa_10;
            this.special = dfa_11;
            this.transition = dfa_12;
        }
        public String getDescription() {
            return "()+ loopback of 335:5: ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_functions_5_0= ruleFunction ) ) | ( (lv_expressions_6_0= ruleFunctionExpression ) ) )+";
        }
    }
    static final String dfa_13s = "\20\uffff";
    static final String dfa_14s = "\1\20\1\uffff\1\4\1\26\1\17\2\uffff\2\4\1\uffff\1\27\2\17\1\4\1\uffff\1\27";
    static final String dfa_15s = "\1\21\1\uffff\1\45\1\26\1\50\2\uffff\2\4\1\uffff\1\33\2\50\1\4\1\uffff\1\27";
    static final String dfa_16s = "\1\uffff\1\5\3\uffff\1\4\1\2\2\uffff\1\1\4\uffff\1\3\1\uffff";
    static final String dfa_17s = "\20\uffff}>";
    static final String[] dfa_18s = {
            "\1\2\1\1",
            "",
            "\1\4\1\5\27\uffff\10\6\1\3",
            "\1\7",
            "\1\5\2\11\10\uffff\1\11\1\10\12\uffff\3\5",
            "",
            "",
            "\1\12",
            "\1\13",
            "",
            "\1\14\3\uffff\1\15",
            "\1\5\2\11\10\uffff\1\11\13\uffff\3\5",
            "\1\5\12\uffff\1\16\13\uffff\3\5",
            "\1\17",
            "",
            "\1\14"
    };

    static final short[] dfa_13 = DFA.unpackEncodedString(dfa_13s);
    static final char[] dfa_14 = DFA.unpackEncodedStringToUnsignedChars(dfa_14s);
    static final char[] dfa_15 = DFA.unpackEncodedStringToUnsignedChars(dfa_15s);
    static final short[] dfa_16 = DFA.unpackEncodedString(dfa_16s);
    static final short[] dfa_17 = DFA.unpackEncodedString(dfa_17s);
    static final short[][] dfa_18 = unpackEncodedStringArray(dfa_18s);

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = dfa_13;
            this.eof = dfa_13;
            this.min = dfa_14;
            this.max = dfa_15;
            this.accept = dfa_16;
            this.special = dfa_17;
            this.transition = dfa_18;
        }
        public String getDescription() {
            return "()* loopback of 416:5: ( (otherlv_7= ',' ( (lv_attributes_8_0= ruleAttribute ) ) ) | (otherlv_9= ',' ( (lv_aggregations_10_0= ruleAggregation ) ) ) | (otherlv_11= ',' ( (lv_functions_12_0= ruleFunction ) ) ) | (otherlv_13= ',' ( (lv_expressions_14_0= ruleFunctionExpression ) ) ) )*";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0028400000002002L,0x0000000000030000L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000003FE000C030L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000003FE003C030L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000030000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000001FE0000000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000400010L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x00000000006D0012L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x00000000002D0002L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x00000000004000F0L,0x00000000000000E0L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000280002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000210012L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000210002L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000005000002L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0180000000000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x000001C000008000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000002000000030L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000002000000032L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000810040L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000000010L,0x000000000000FF00L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000000810000L,0x000000000000FF00L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000000000000L,0x000000000000FF00L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000000810000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000000000002010L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000C00000000000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0040000000000010L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0600000000000000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0A00000000000000L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x1000000000000002L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x4000000000000002L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x8000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000000000000002L,0x000000000000001EL});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x000000C000000002L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x0000010000008002L});

}