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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_FLOAT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "';'", "'SELECT'", "'DISTINCT'", "'*'", "','", "'FROM'", "'WHERE'", "'GROUP'", "'BY'", "'HAVING'", "'('", "')'", "'['", "']'", "'AS'", "'.'", "'IN'", "'AVG'", "'MIN'", "'MAX'", "'COUNT'", "'SUM'", "'MEDIAN'", "'FIRST'", "'LAST'", "'DolToEur'", "'+'", "'-'", "'/'", "'WRAPPER'", "'PROTOCOL'", "'TRANSPORT'", "'DATAHANDLER'", "'OPTIONS'", "'STREAM'", "'SINK'", "'CHANNEL'", "':'", "'FILE'", "'VIEW'", "'TO'", "'DROP'", "'IF EXISTS'", "'UNBOUNDED'", "'SIZE'", "'ADVANCE'", "'TIME'", "'TUPLE'", "'PARTITION'", "'OR'", "'AND'", "'='", "'!='", "'>='", "'<='", "'<'", "'>'", "'NOT'", "'TRUE'", "'FALSE'", "'INTEGER'", "'DOUBLE'", "'LONG'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'", "'CREATE'", "'ATTACH'"
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
    public static final int RULE_INT=6;
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
    public static final int RULE_STRING=5;
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
    public static final int RULE_FLOAT=7;
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
    // InternalCQL.g:292:1: ruleSelect returns [EObject current=null] : ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )? ) ;
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
            // InternalCQL.g:298:2: ( ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQL.g:299:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQL.g:299:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )? )
            // InternalCQL.g:300:3: ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )?
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

            // InternalCQL.g:328:3: (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==15) ) {
                alt6=1;
            }
            else if ( ((LA6_0>=RULE_ID && LA6_0<=RULE_FLOAT)||(LA6_0>=29 && LA6_0<=37)||(LA6_0>=70 && LA6_0<=71)) ) {
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
                    // InternalCQL.g:334:4: ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* )
                    {
                    // InternalCQL.g:334:4: ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* )
                    // InternalCQL.g:335:5: ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )*
                    {
                    // InternalCQL.g:335:5: ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+
                    int cnt4=0;
                    loop4:
                    do {
                        int alt4=4;
                        switch ( input.LA(1) ) {
                        case RULE_ID:
                            {
                            switch ( input.LA(2) ) {
                            case 27:
                                {
                                int LA4_5 = input.LA(3);

                                if ( (LA4_5==RULE_ID) ) {
                                    int LA4_7 = input.LA(4);

                                    if ( ((LA4_7>=RULE_ID && LA4_7<=RULE_FLOAT)||(LA4_7>=16 && LA4_7<=17)||LA4_7==26||(LA4_7>=29 && LA4_7<=37)||(LA4_7>=70 && LA4_7<=71)) ) {
                                        alt4=1;
                                    }
                                    else if ( (LA4_7==15||(LA4_7>=38 && LA4_7<=40)) ) {
                                        alt4=3;
                                    }


                                }


                                }
                                break;
                            case 15:
                            case 38:
                            case 39:
                            case 40:
                                {
                                alt4=3;
                                }
                                break;
                            case RULE_ID:
                            case RULE_STRING:
                            case RULE_INT:
                            case RULE_FLOAT:
                            case 16:
                            case 17:
                            case 26:
                            case 29:
                            case 30:
                            case 31:
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 70:
                            case 71:
                                {
                                alt4=1;
                                }
                                break;

                            }

                            }
                            break;
                        case 29:
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                            {
                            alt4=2;
                            }
                            break;
                        case RULE_STRING:
                        case RULE_INT:
                        case RULE_FLOAT:
                        case 37:
                        case 70:
                        case 71:
                            {
                            alt4=3;
                            }
                            break;

                        }

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
                    	    // InternalCQL.g:376:6: ( (lv_expressions_5_0= ruleSelectExpression ) )
                    	    {
                    	    // InternalCQL.g:376:6: ( (lv_expressions_5_0= ruleSelectExpression ) )
                    	    // InternalCQL.g:377:7: (lv_expressions_5_0= ruleSelectExpression )
                    	    {
                    	    // InternalCQL.g:377:7: (lv_expressions_5_0= ruleSelectExpression )
                    	    // InternalCQL.g:378:8: lv_expressions_5_0= ruleSelectExpression
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
                    	    if ( cnt4 >= 1 ) break loop4;
                                EarlyExitException eee =
                                    new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);

                    // InternalCQL.g:396:5: ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )*
                    loop5:
                    do {
                        int alt5=4;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==16) ) {
                            switch ( input.LA(2) ) {
                            case RULE_STRING:
                            case RULE_INT:
                            case RULE_FLOAT:
                            case 37:
                            case 70:
                            case 71:
                                {
                                alt5=3;
                                }
                                break;
                            case RULE_ID:
                                {
                                switch ( input.LA(3) ) {
                                case 16:
                                case 17:
                                case 26:
                                    {
                                    alt5=1;
                                    }
                                    break;
                                case 27:
                                    {
                                    int LA5_7 = input.LA(4);

                                    if ( (LA5_7==RULE_ID) ) {
                                        int LA5_8 = input.LA(5);

                                        if ( ((LA5_8>=16 && LA5_8<=17)||LA5_8==26) ) {
                                            alt5=1;
                                        }
                                        else if ( (LA5_8==15||(LA5_8>=38 && LA5_8<=40)) ) {
                                            alt5=3;
                                        }


                                    }


                                    }
                                    break;
                                case 15:
                                case 38:
                                case 39:
                                case 40:
                                    {
                                    alt5=3;
                                    }
                                    break;

                                }

                                }
                                break;
                            case 29:
                            case 30:
                            case 31:
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                                {
                                alt5=2;
                                }
                                break;

                            }

                        }


                        switch (alt5) {
                    	case 1 :
                    	    // InternalCQL.g:397:6: (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    {
                    	    // InternalCQL.g:397:6: (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    // InternalCQL.g:398:7: otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) )
                    	    {
                    	    otherlv_6=(Token)match(input,16,FOLLOW_8); 

                    	    							newLeafNode(otherlv_6, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_0_0());
                    	    						
                    	    // InternalCQL.g:402:7: ( (lv_attributes_7_0= ruleAttribute ) )
                    	    // InternalCQL.g:403:8: (lv_attributes_7_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:403:8: (lv_attributes_7_0= ruleAttribute )
                    	    // InternalCQL.g:404:9: lv_attributes_7_0= ruleAttribute
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
                    	    // InternalCQL.g:423:6: (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    {
                    	    // InternalCQL.g:423:6: (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    // InternalCQL.g:424:7: otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    {
                    	    otherlv_8=(Token)match(input,16,FOLLOW_10); 

                    	    							newLeafNode(otherlv_8, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_1_0());
                    	    						
                    	    // InternalCQL.g:428:7: ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    // InternalCQL.g:429:8: (lv_aggregations_9_0= ruleAggregation )
                    	    {
                    	    // InternalCQL.g:429:8: (lv_aggregations_9_0= ruleAggregation )
                    	    // InternalCQL.g:430:9: lv_aggregations_9_0= ruleAggregation
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
                    	    // InternalCQL.g:449:6: (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) )
                    	    {
                    	    // InternalCQL.g:449:6: (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) )
                    	    // InternalCQL.g:450:7: otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) )
                    	    {
                    	    otherlv_10=(Token)match(input,16,FOLLOW_5); 

                    	    							newLeafNode(otherlv_10, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_2_0());
                    	    						
                    	    // InternalCQL.g:454:7: ( (lv_expressions_11_0= ruleSelectExpression ) )
                    	    // InternalCQL.g:455:8: (lv_expressions_11_0= ruleSelectExpression )
                    	    {
                    	    // InternalCQL.g:455:8: (lv_expressions_11_0= ruleSelectExpression )
                    	    // InternalCQL.g:456:9: lv_expressions_11_0= ruleSelectExpression
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
                    	    break loop5;
                        }
                    } while (true);


                    }


                    }
                    break;

            }

            // InternalCQL.g:477:3: (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* )
            // InternalCQL.g:478:4: otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )*
            {
            otherlv_12=(Token)match(input,17,FOLLOW_11); 

            				newLeafNode(otherlv_12, grammarAccess.getSelectAccess().getFROMKeyword_3_0());
            			
            // InternalCQL.g:482:4: ( (lv_sources_13_0= ruleSource ) )+
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
            	    // InternalCQL.g:483:5: (lv_sources_13_0= ruleSource )
            	    {
            	    // InternalCQL.g:483:5: (lv_sources_13_0= ruleSource )
            	    // InternalCQL.g:484:6: lv_sources_13_0= ruleSource
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
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);

            // InternalCQL.g:501:4: (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==16) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCQL.g:502:5: otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) )
            	    {
            	    otherlv_14=(Token)match(input,16,FOLLOW_11); 

            	    					newLeafNode(otherlv_14, grammarAccess.getSelectAccess().getCommaKeyword_3_2_0());
            	    				
            	    // InternalCQL.g:506:5: ( (lv_sources_15_0= ruleSource ) )
            	    // InternalCQL.g:507:6: (lv_sources_15_0= ruleSource )
            	    {
            	    // InternalCQL.g:507:6: (lv_sources_15_0= ruleSource )
            	    // InternalCQL.g:508:7: lv_sources_15_0= ruleSource
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
            	    break loop8;
                }
            } while (true);


            }

            // InternalCQL.g:527:3: (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==18) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalCQL.g:528:4: otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) )
                    {
                    otherlv_16=(Token)match(input,18,FOLLOW_14); 

                    				newLeafNode(otherlv_16, grammarAccess.getSelectAccess().getWHEREKeyword_4_0());
                    			
                    // InternalCQL.g:532:4: ( (lv_predicates_17_0= ruleExpressionsModel ) )
                    // InternalCQL.g:533:5: (lv_predicates_17_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:533:5: (lv_predicates_17_0= ruleExpressionsModel )
                    // InternalCQL.g:534:6: lv_predicates_17_0= ruleExpressionsModel
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

            // InternalCQL.g:552:3: (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==19) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalCQL.g:553:4: otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )*
                    {
                    otherlv_18=(Token)match(input,19,FOLLOW_16); 

                    				newLeafNode(otherlv_18, grammarAccess.getSelectAccess().getGROUPKeyword_5_0());
                    			
                    otherlv_19=(Token)match(input,20,FOLLOW_8); 

                    				newLeafNode(otherlv_19, grammarAccess.getSelectAccess().getBYKeyword_5_1());
                    			
                    // InternalCQL.g:561:4: ( (lv_order_20_0= ruleAttribute ) )+
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
                    	    // InternalCQL.g:562:5: (lv_order_20_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:562:5: (lv_order_20_0= ruleAttribute )
                    	    // InternalCQL.g:563:6: lv_order_20_0= ruleAttribute
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
                    	    if ( cnt10 >= 1 ) break loop10;
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);

                    // InternalCQL.g:580:4: (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==16) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // InternalCQL.g:581:5: otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) )
                    	    {
                    	    otherlv_21=(Token)match(input,16,FOLLOW_8); 

                    	    					newLeafNode(otherlv_21, grammarAccess.getSelectAccess().getCommaKeyword_5_3_0());
                    	    				
                    	    // InternalCQL.g:585:5: ( (lv_order_22_0= ruleAttribute ) )
                    	    // InternalCQL.g:586:6: (lv_order_22_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:586:6: (lv_order_22_0= ruleAttribute )
                    	    // InternalCQL.g:587:7: lv_order_22_0= ruleAttribute
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
                    	    break loop11;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCQL.g:606:3: (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==21) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQL.g:607:4: otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) )
                    {
                    otherlv_23=(Token)match(input,21,FOLLOW_14); 

                    				newLeafNode(otherlv_23, grammarAccess.getSelectAccess().getHAVINGKeyword_6_0());
                    			
                    // InternalCQL.g:611:4: ( (lv_having_24_0= ruleExpressionsModel ) )
                    // InternalCQL.g:612:5: (lv_having_24_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:612:5: (lv_having_24_0= ruleExpressionsModel )
                    // InternalCQL.g:613:6: lv_having_24_0= ruleExpressionsModel
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
    // InternalCQL.g:635:1: entryRuleNestedStatement returns [EObject current=null] : iv_ruleNestedStatement= ruleNestedStatement EOF ;
    public final EObject entryRuleNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedStatement = null;


        try {
            // InternalCQL.g:635:56: (iv_ruleNestedStatement= ruleNestedStatement EOF )
            // InternalCQL.g:636:2: iv_ruleNestedStatement= ruleNestedStatement EOF
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
    // InternalCQL.g:642:1: ruleNestedStatement returns [EObject current=null] : (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) ;
    public final EObject ruleNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:648:2: ( (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) )
            // InternalCQL.g:649:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            {
            // InternalCQL.g:649:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            // InternalCQL.g:650:3: otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')'
            {
            otherlv_0=(Token)match(input,22,FOLLOW_19); 

            			newLeafNode(otherlv_0, grammarAccess.getNestedStatementAccess().getLeftParenthesisKeyword_0());
            		

            			newCompositeNode(grammarAccess.getNestedStatementAccess().getSelectParserRuleCall_1());
            		
            pushFollow(FOLLOW_20);
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
    // InternalCQL.g:670:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQL.g:670:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQL.g:671:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQL.g:677:1: ruleSource returns [EObject current=null] : ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) ) ;
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
            // InternalCQL.g:683:2: ( ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) ) )
            // InternalCQL.g:684:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) )
            {
            // InternalCQL.g:684:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) )
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
                    // InternalCQL.g:685:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
                    {
                    // InternalCQL.g:685:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
                    // InternalCQL.g:686:4: ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
                    {
                    // InternalCQL.g:686:4: ( (lv_name_0_0= ruleSourceName ) )
                    // InternalCQL.g:687:5: (lv_name_0_0= ruleSourceName )
                    {
                    // InternalCQL.g:687:5: (lv_name_0_0= ruleSourceName )
                    // InternalCQL.g:688:6: lv_name_0_0= ruleSourceName
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

                    // InternalCQL.g:705:4: (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==24) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // InternalCQL.g:706:5: otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']'
                            {
                            otherlv_1=(Token)match(input,24,FOLLOW_22); 

                            					newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_1_0());
                            				
                            // InternalCQL.g:710:5: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                            int alt14=3;
                            int LA14_0 = input.LA(1);

                            if ( (LA14_0==55) ) {
                                alt14=1;
                            }
                            else if ( (LA14_0==56) ) {
                                int LA14_2 = input.LA(2);

                                if ( (LA14_2==RULE_INT) ) {
                                    int LA14_3 = input.LA(3);

                                    if ( (LA14_3==57||LA14_3==59) ) {
                                        alt14=3;
                                    }
                                    else if ( (LA14_3==RULE_ID) ) {
                                        alt14=2;
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
                                    // InternalCQL.g:711:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQL.g:711:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQL.g:712:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQL.g:712:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQL.g:713:8: lv_unbounded_2_0= ruleWindow_Unbounded
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
                                    // InternalCQL.g:731:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQL.g:731:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQL.g:732:7: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQL.g:732:7: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQL.g:733:8: lv_time_3_0= ruleWindow_Timebased
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
                                    // InternalCQL.g:751:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQL.g:751:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQL.g:752:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQL.g:752:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQL.g:753:8: lv_tuple_4_0= ruleWindow_Tuplebased
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

                            otherlv_5=(Token)match(input,25,FOLLOW_24); 

                            					newLeafNode(otherlv_5, grammarAccess.getSourceAccess().getRightSquareBracketKeyword_0_1_2());
                            				

                            }
                            break;

                    }

                    // InternalCQL.g:776:4: (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==26) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // InternalCQL.g:777:5: otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) )
                            {
                            otherlv_6=(Token)match(input,26,FOLLOW_8); 

                            					newLeafNode(otherlv_6, grammarAccess.getSourceAccess().getASKeyword_0_2_0());
                            				
                            // InternalCQL.g:781:5: ( (lv_alias_7_0= ruleAlias ) )
                            // InternalCQL.g:782:6: (lv_alias_7_0= ruleAlias )
                            {
                            // InternalCQL.g:782:6: (lv_alias_7_0= ruleAlias )
                            // InternalCQL.g:783:7: lv_alias_7_0= ruleAlias
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
                    // InternalCQL.g:803:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) )
                    {
                    // InternalCQL.g:803:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) )
                    // InternalCQL.g:804:4: ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) )
                    {
                    // InternalCQL.g:804:4: ( (lv_nested_8_0= ruleNestedStatement ) )
                    // InternalCQL.g:805:5: (lv_nested_8_0= ruleNestedStatement )
                    {
                    // InternalCQL.g:805:5: (lv_nested_8_0= ruleNestedStatement )
                    // InternalCQL.g:806:6: lv_nested_8_0= ruleNestedStatement
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

                    otherlv_9=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_9, grammarAccess.getSourceAccess().getASKeyword_1_1());
                    			
                    // InternalCQL.g:827:4: ( (lv_alias_10_0= ruleAlias ) )
                    // InternalCQL.g:828:5: (lv_alias_10_0= ruleAlias )
                    {
                    // InternalCQL.g:828:5: (lv_alias_10_0= ruleAlias )
                    // InternalCQL.g:829:6: lv_alias_10_0= ruleAlias
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
    // InternalCQL.g:851:1: entryRuleSourceName returns [String current=null] : iv_ruleSourceName= ruleSourceName EOF ;
    public final String entryRuleSourceName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSourceName = null;


        try {
            // InternalCQL.g:851:50: (iv_ruleSourceName= ruleSourceName EOF )
            // InternalCQL.g:852:2: iv_ruleSourceName= ruleSourceName EOF
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
    // InternalCQL.g:858:1: ruleSourceName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleSourceName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQL.g:864:2: (this_ID_0= RULE_ID )
            // InternalCQL.g:865:2: this_ID_0= RULE_ID
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
    // InternalCQL.g:875:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQL.g:875:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQL.g:876:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQL.g:882:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:888:2: ( ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:889:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:889:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQL.g:890:3: ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:890:3: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQL.g:891:4: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQL.g:891:4: (lv_name_0_0= ruleAttributeName )
            // InternalCQL.g:892:5: lv_name_0_0= ruleAttributeName
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

            // InternalCQL.g:909:3: (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==26) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // InternalCQL.g:910:4: otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:914:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQL.g:915:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQL.g:915:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQL.g:916:6: lv_alias_2_0= ruleAlias
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
    // InternalCQL.g:938:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQL.g:938:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQL.g:939:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
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
    // InternalCQL.g:945:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleAttributeName ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:951:2: ( ( (lv_name_0_0= ruleAttributeName ) ) )
            // InternalCQL.g:952:2: ( (lv_name_0_0= ruleAttributeName ) )
            {
            // InternalCQL.g:952:2: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQL.g:953:3: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQL.g:953:3: (lv_name_0_0= ruleAttributeName )
            // InternalCQL.g:954:4: lv_name_0_0= ruleAttributeName
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
    // InternalCQL.g:974:1: entryRuleAttributeName returns [String current=null] : iv_ruleAttributeName= ruleAttributeName EOF ;
    public final String entryRuleAttributeName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAttributeName = null;


        try {
            // InternalCQL.g:974:53: (iv_ruleAttributeName= ruleAttributeName EOF )
            // InternalCQL.g:975:2: iv_ruleAttributeName= ruleAttributeName EOF
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
    // InternalCQL.g:981:1: ruleAttributeName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) ) ;
    public final AntlrDatatypeRuleToken ruleAttributeName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_SourceName_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:987:2: ( (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) ) )
            // InternalCQL.g:988:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) )
            {
            // InternalCQL.g:988:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==RULE_ID) ) {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==27) ) {
                    alt19=2;
                }
                else if ( (LA19_1==EOF||(LA19_1>=RULE_ID && LA19_1<=RULE_FLOAT)||LA19_1==12||(LA19_1>=15 && LA19_1<=17)||LA19_1==19||LA19_1==21||LA19_1==23||(LA19_1>=25 && LA19_1<=26)||(LA19_1>=28 && LA19_1<=40)||(LA19_1>=61 && LA19_1<=68)||(LA19_1>=70 && LA19_1<=79)) ) {
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
                    // InternalCQL.g:989:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQL.g:997:3: (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID )
                    {
                    // InternalCQL.g:997:3: (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID )
                    // InternalCQL.g:998:4: this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getAttributeNameAccess().getSourceNameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_26);
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
    // InternalCQL.g:1025:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQL.g:1025:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQL.g:1026:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
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
    // InternalCQL.g:1032:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1038:2: ( ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) )
            // InternalCQL.g:1039:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            {
            // InternalCQL.g:1039:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            // InternalCQL.g:1040:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) )
            {
            // InternalCQL.g:1040:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:1041:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:1041:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:1042:5: lv_value_0_0= ruleAttributeWithoutAliasDefinition
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

            otherlv_1=(Token)match(input,28,FOLLOW_11); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeWithNestedStatementAccess().getINKeyword_1());
            		
            // InternalCQL.g:1063:3: ( (lv_nested_2_0= ruleNestedStatement ) )
            // InternalCQL.g:1064:4: (lv_nested_2_0= ruleNestedStatement )
            {
            // InternalCQL.g:1064:4: (lv_nested_2_0= ruleNestedStatement )
            // InternalCQL.g:1065:5: lv_nested_2_0= ruleNestedStatement
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
    // InternalCQL.g:1086:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalCQL.g:1086:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalCQL.g:1087:2: iv_ruleAggregation= ruleAggregation EOF
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
    // InternalCQL.g:1093:1: ruleAggregation returns [EObject current=null] : ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )? ) ;
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
            // InternalCQL.g:1099:2: ( ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:1100:2: ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:1100:2: ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )? )
            // InternalCQL.g:1101:3: ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:1101:3: ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) )
            // InternalCQL.g:1102:4: ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) )
            {
            // InternalCQL.g:1102:4: ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) )
            // InternalCQL.g:1103:5: (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' )
            {
            // InternalCQL.g:1103:5: (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' )
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
                    // InternalCQL.g:1104:6: lv_name_0_1= 'AVG'
                    {
                    lv_name_0_1=(Token)match(input,29,FOLLOW_28); 

                    						newLeafNode(lv_name_0_1, grammarAccess.getAggregationAccess().getNameAVGKeyword_0_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1115:6: lv_name_0_2= 'MIN'
                    {
                    lv_name_0_2=(Token)match(input,30,FOLLOW_28); 

                    						newLeafNode(lv_name_0_2, grammarAccess.getAggregationAccess().getNameMINKeyword_0_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:1126:6: lv_name_0_3= 'MAX'
                    {
                    lv_name_0_3=(Token)match(input,31,FOLLOW_28); 

                    						newLeafNode(lv_name_0_3, grammarAccess.getAggregationAccess().getNameMAXKeyword_0_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:1137:6: lv_name_0_4= 'COUNT'
                    {
                    lv_name_0_4=(Token)match(input,32,FOLLOW_28); 

                    						newLeafNode(lv_name_0_4, grammarAccess.getAggregationAccess().getNameCOUNTKeyword_0_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_4, null);
                    					

                    }
                    break;
                case 5 :
                    // InternalCQL.g:1148:6: lv_name_0_5= 'SUM'
                    {
                    lv_name_0_5=(Token)match(input,33,FOLLOW_28); 

                    						newLeafNode(lv_name_0_5, grammarAccess.getAggregationAccess().getNameSUMKeyword_0_0_4());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_5, null);
                    					

                    }
                    break;
                case 6 :
                    // InternalCQL.g:1159:6: lv_name_0_6= 'MEDIAN'
                    {
                    lv_name_0_6=(Token)match(input,34,FOLLOW_28); 

                    						newLeafNode(lv_name_0_6, grammarAccess.getAggregationAccess().getNameMEDIANKeyword_0_0_5());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_6, null);
                    					

                    }
                    break;
                case 7 :
                    // InternalCQL.g:1170:6: lv_name_0_7= 'FIRST'
                    {
                    lv_name_0_7=(Token)match(input,35,FOLLOW_28); 

                    						newLeafNode(lv_name_0_7, grammarAccess.getAggregationAccess().getNameFIRSTKeyword_0_0_6());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_7, null);
                    					

                    }
                    break;
                case 8 :
                    // InternalCQL.g:1181:6: lv_name_0_8= 'LAST'
                    {
                    lv_name_0_8=(Token)match(input,36,FOLLOW_28); 

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
            		
            // InternalCQL.g:1198:3: ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute ) ) )
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case 15:
                case 38:
                case 39:
                case 40:
                    {
                    alt21=2;
                    }
                    break;
                case 27:
                    {
                    int LA21_3 = input.LA(3);

                    if ( (LA21_3==RULE_ID) ) {
                        int LA21_5 = input.LA(4);

                        if ( (LA21_5==15||(LA21_5>=38 && LA21_5<=40)) ) {
                            alt21=2;
                        }
                        else if ( (LA21_5==23) ) {
                            alt21=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 21, 5, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 3, input);

                        throw nvae;
                    }
                    }
                    break;
                case 23:
                    {
                    alt21=1;
                    }
                    break;
                default:
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
                    // InternalCQL.g:1199:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1199:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1200:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1200:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1201:6: lv_attribute_2_0= ruleAttributeWithoutAliasDefinition
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
                    // InternalCQL.g:1219:4: ( (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute ) )
                    {
                    // InternalCQL.g:1219:4: ( (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute ) )
                    // InternalCQL.g:1220:5: (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute )
                    {
                    // InternalCQL.g:1220:5: (lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute )
                    // InternalCQL.g:1221:6: lv_expression_3_0= ruleSelectExpressionOnlyWithAttribute
                    {

                    						newCompositeNode(grammarAccess.getAggregationAccess().getExpressionSelectExpressionOnlyWithAttributeParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_20);
                    lv_expression_3_0=ruleSelectExpressionOnlyWithAttribute();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAggregationRule());
                    						}
                    						set(
                    							current,
                    							"expression",
                    							lv_expression_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionOnlyWithAttribute");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,23,FOLLOW_24); 

            			newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getRightParenthesisKeyword_3());
            		
            // InternalCQL.g:1243:3: (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==26) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalCQL.g:1244:4: otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) )
                    {
                    otherlv_5=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_5, grammarAccess.getAggregationAccess().getASKeyword_4_0());
                    			
                    // InternalCQL.g:1248:4: ( (lv_alias_6_0= ruleAlias ) )
                    // InternalCQL.g:1249:5: (lv_alias_6_0= ruleAlias )
                    {
                    // InternalCQL.g:1249:5: (lv_alias_6_0= ruleAlias )
                    // InternalCQL.g:1250:6: lv_alias_6_0= ruleAlias
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


    // $ANTLR start "entryRuleMapper"
    // InternalCQL.g:1272:1: entryRuleMapper returns [EObject current=null] : iv_ruleMapper= ruleMapper EOF ;
    public final EObject entryRuleMapper() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapper = null;


        try {
            // InternalCQL.g:1272:47: (iv_ruleMapper= ruleMapper EOF )
            // InternalCQL.g:1273:2: iv_ruleMapper= ruleMapper EOF
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
    // InternalCQL.g:1279:1: ruleMapper returns [EObject current=null] : ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant ) ) ) otherlv_4= ')' ) ;
    public final EObject ruleMapper() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_4=null;
        EObject lv_attribute_2_0 = null;

        EObject lv_innerexpression_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1285:2: ( ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant ) ) ) otherlv_4= ')' ) )
            // InternalCQL.g:1286:2: ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant ) ) ) otherlv_4= ')' )
            {
            // InternalCQL.g:1286:2: ( ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant ) ) ) otherlv_4= ')' )
            // InternalCQL.g:1287:3: ( (lv_name_0_0= 'DolToEur' ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant ) ) ) otherlv_4= ')'
            {
            // InternalCQL.g:1287:3: ( (lv_name_0_0= 'DolToEur' ) )
            // InternalCQL.g:1288:4: (lv_name_0_0= 'DolToEur' )
            {
            // InternalCQL.g:1288:4: (lv_name_0_0= 'DolToEur' )
            // InternalCQL.g:1289:5: lv_name_0_0= 'DolToEur'
            {
            lv_name_0_0=(Token)match(input,37,FOLLOW_28); 

            					newLeafNode(lv_name_0_0, grammarAccess.getMapperAccess().getNameDolToEurKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getMapperRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "DolToEur");
            				

            }


            }

            otherlv_1=(Token)match(input,22,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getMapperAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:1305:3: ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant ) ) )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case 27:
                    {
                    int LA23_3 = input.LA(3);

                    if ( (LA23_3==RULE_ID) ) {
                        int LA23_5 = input.LA(4);

                        if ( (LA23_5==23) ) {
                            alt23=1;
                        }
                        else if ( (LA23_5==15||(LA23_5>=38 && LA23_5<=40)) ) {
                            alt23=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 23, 5, input);

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
                case 23:
                    {
                    alt23=1;
                    }
                    break;
                case 15:
                case 38:
                case 39:
                case 40:
                    {
                    alt23=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 23, 1, input);

                    throw nvae;
                }

            }
            else if ( ((LA23_0>=RULE_STRING && LA23_0<=RULE_FLOAT)||(LA23_0>=70 && LA23_0<=71)) ) {
                alt23=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }
            switch (alt23) {
                case 1 :
                    // InternalCQL.g:1306:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1306:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1307:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1307:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1308:6: lv_attribute_2_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getMapperAccess().getAttributeAttributeWithoutAliasDefinitionParserRuleCall_2_0_0());
                    					
                    pushFollow(FOLLOW_20);
                    lv_attribute_2_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getMapperRule());
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
                    // InternalCQL.g:1326:4: ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant ) )
                    {
                    // InternalCQL.g:1326:4: ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant ) )
                    // InternalCQL.g:1327:5: (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant )
                    {
                    // InternalCQL.g:1327:5: (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant )
                    // InternalCQL.g:1328:6: lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant
                    {

                    						newCompositeNode(grammarAccess.getMapperAccess().getInnerexpressionSelectExpressionOnlyWithAttributeAndConstantParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_20);
                    lv_innerexpression_3_0=ruleSelectExpressionOnlyWithAttributeAndConstant();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getMapperRule());
                    						}
                    						set(
                    							current,
                    							"innerexpression",
                    							lv_innerexpression_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionOnlyWithAttributeAndConstant");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,23,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getMapperAccess().getRightParenthesisKeyword_3());
            		

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
    // InternalCQL.g:1354:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQL.g:1354:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQL.g:1355:2: iv_ruleSelectExpression= ruleSelectExpression EOF
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
    // InternalCQL.g:1361:1: ruleSelectExpression returns [EObject current=null] : ( ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSelectExpression() throws RecognitionException {
        EObject current = null;

        Token lv_operator_3_1=null;
        Token lv_operator_3_2=null;
        Token lv_operator_3_3=null;
        Token lv_operator_3_4=null;
        Token otherlv_7=null;
        EObject lv_leftmapper_0_0 = null;

        EObject lv_leftattribute_1_0 = null;

        EObject lv_leftconstant_2_0 = null;

        EObject lv_innerexpression_4_0 = null;

        EObject lv_rightattribute_5_0 = null;

        EObject lv_rightconstant_6_0 = null;

        EObject lv_alias_8_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1367:2: ( ( ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:1368:2: ( ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:1368:2: ( ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? )
            // InternalCQL.g:1369:3: ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )? ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:1369:3: ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt24=3;
            switch ( input.LA(1) ) {
            case 37:
                {
                alt24=1;
                }
                break;
            case RULE_ID:
                {
                alt24=2;
                }
                break;
            case RULE_STRING:
            case RULE_INT:
            case RULE_FLOAT:
            case 70:
            case 71:
                {
                alt24=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }

            switch (alt24) {
                case 1 :
                    // InternalCQL.g:1370:4: ( (lv_leftmapper_0_0= ruleMapper ) )
                    {
                    // InternalCQL.g:1370:4: ( (lv_leftmapper_0_0= ruleMapper ) )
                    // InternalCQL.g:1371:5: (lv_leftmapper_0_0= ruleMapper )
                    {
                    // InternalCQL.g:1371:5: (lv_leftmapper_0_0= ruleMapper )
                    // InternalCQL.g:1372:6: lv_leftmapper_0_0= ruleMapper
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionAccess().getLeftmapperMapperParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftmapper_0_0=ruleMapper();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    						}
                    						set(
                    							current,
                    							"leftmapper",
                    							lv_leftmapper_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Mapper");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1390:4: ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1390:4: ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1391:5: (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1391:5: (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1392:6: lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionAccess().getLeftattributeAttributeWithoutAliasDefinitionParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftattribute_1_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
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
                    // InternalCQL.g:1410:4: ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQL.g:1410:4: ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQL.g:1411:5: (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQL.g:1411:5: (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQL.g:1412:6: lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionAccess().getLeftconstantAtomicWithoutAttributeRefParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftconstant_2_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    						}
                    						set(
                    							current,
                    							"leftconstant",
                    							lv_leftconstant_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1430:3: ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) )
            // InternalCQL.g:1431:4: ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) )
            {
            // InternalCQL.g:1431:4: ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) )
            // InternalCQL.g:1432:5: (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' )
            {
            // InternalCQL.g:1432:5: (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' )
            int alt25=4;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt25=1;
                }
                break;
            case 39:
                {
                alt25=2;
                }
                break;
            case 40:
                {
                alt25=3;
                }
                break;
            case 15:
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
                    // InternalCQL.g:1433:6: lv_operator_3_1= '+'
                    {
                    lv_operator_3_1=(Token)match(input,38,FOLLOW_5); 

                    						newLeafNode(lv_operator_3_1, grammarAccess.getSelectExpressionAccess().getOperatorPlusSignKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1444:6: lv_operator_3_2= '-'
                    {
                    lv_operator_3_2=(Token)match(input,39,FOLLOW_5); 

                    						newLeafNode(lv_operator_3_2, grammarAccess.getSelectExpressionAccess().getOperatorHyphenMinusKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:1455:6: lv_operator_3_3= '/'
                    {
                    lv_operator_3_3=(Token)match(input,40,FOLLOW_5); 

                    						newLeafNode(lv_operator_3_3, grammarAccess.getSelectExpressionAccess().getOperatorSolidusKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:1466:6: lv_operator_3_4= '*'
                    {
                    lv_operator_3_4=(Token)match(input,15,FOLLOW_5); 

                    						newLeafNode(lv_operator_3_4, grammarAccess.getSelectExpressionAccess().getOperatorAsteriskKeyword_1_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_4, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:1479:3: ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )?
            int alt26=2;
            alt26 = dfa26.predict(input);
            switch (alt26) {
                case 1 :
                    // InternalCQL.g:1480:4: (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1480:4: (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition )
                    // InternalCQL.g:1481:5: lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition
                    {

                    					newCompositeNode(grammarAccess.getSelectExpressionAccess().getInnerexpressionSelectExpressionWithoutAliasDefinitionParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_5);
                    lv_innerexpression_4_0=ruleSelectExpressionWithoutAliasDefinition();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    					}
                    					set(
                    						current,
                    						"innerexpression",
                    						lv_innerexpression_4_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionWithoutAliasDefinition");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:1498:3: ( ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==RULE_ID) ) {
                alt27=1;
            }
            else if ( ((LA27_0>=RULE_STRING && LA27_0<=RULE_FLOAT)||(LA27_0>=70 && LA27_0<=71)) ) {
                alt27=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // InternalCQL.g:1499:4: ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1499:4: ( (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1500:5: (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1500:5: (lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1501:6: lv_rightattribute_5_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionAccess().getRightattributeAttributeWithoutAliasDefinitionParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_24);
                    lv_rightattribute_5_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
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
                    // InternalCQL.g:1519:4: ( (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQL.g:1519:4: ( (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQL.g:1520:5: (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQL.g:1520:5: (lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQL.g:1521:6: lv_rightconstant_6_0= ruleAtomicWithoutAttributeRef
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionAccess().getRightconstantAtomicWithoutAttributeRefParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_24);
                    lv_rightconstant_6_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
                    						}
                    						set(
                    							current,
                    							"rightconstant",
                    							lv_rightconstant_6_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1539:3: (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==26) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // InternalCQL.g:1540:4: otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) )
                    {
                    otherlv_7=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_7, grammarAccess.getSelectExpressionAccess().getASKeyword_4_0());
                    			
                    // InternalCQL.g:1544:4: ( (lv_alias_8_0= ruleAlias ) )
                    // InternalCQL.g:1545:5: (lv_alias_8_0= ruleAlias )
                    {
                    // InternalCQL.g:1545:5: (lv_alias_8_0= ruleAlias )
                    // InternalCQL.g:1546:6: lv_alias_8_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionAccess().getAliasAliasParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_8_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionRule());
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
    // $ANTLR end "ruleSelectExpression"


    // $ANTLR start "entryRuleSelectExpressionWithoutAliasDefinition"
    // InternalCQL.g:1568:1: entryRuleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF ;
    public final EObject entryRuleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithoutAliasDefinition = null;


        try {
            // InternalCQL.g:1568:79: (iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF )
            // InternalCQL.g:1569:2: iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF
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
    // InternalCQL.g:1575:1: ruleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : ( ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )? ) ;
    public final EObject ruleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        Token lv_operator_3_1=null;
        Token lv_operator_3_2=null;
        Token lv_operator_3_3=null;
        Token lv_operator_3_4=null;
        EObject lv_leftmapper_0_0 = null;

        EObject lv_leftattribute_1_0 = null;

        EObject lv_leftconstant_2_0 = null;

        EObject lv_innerexpression_4_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1581:2: ( ( ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )? ) )
            // InternalCQL.g:1582:2: ( ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )? )
            {
            // InternalCQL.g:1582:2: ( ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )? )
            // InternalCQL.g:1583:3: ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) ) ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )?
            {
            // InternalCQL.g:1583:3: ( ( (lv_leftmapper_0_0= ruleMapper ) ) | ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt29=3;
            switch ( input.LA(1) ) {
            case 37:
                {
                alt29=1;
                }
                break;
            case RULE_ID:
                {
                alt29=2;
                }
                break;
            case RULE_STRING:
            case RULE_INT:
            case RULE_FLOAT:
            case 70:
            case 71:
                {
                alt29=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }

            switch (alt29) {
                case 1 :
                    // InternalCQL.g:1584:4: ( (lv_leftmapper_0_0= ruleMapper ) )
                    {
                    // InternalCQL.g:1584:4: ( (lv_leftmapper_0_0= ruleMapper ) )
                    // InternalCQL.g:1585:5: (lv_leftmapper_0_0= ruleMapper )
                    {
                    // InternalCQL.g:1585:5: (lv_leftmapper_0_0= ruleMapper )
                    // InternalCQL.g:1586:6: lv_leftmapper_0_0= ruleMapper
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getLeftmapperMapperParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftmapper_0_0=ruleMapper();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    						}
                    						set(
                    							current,
                    							"leftmapper",
                    							lv_leftmapper_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Mapper");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1604:4: ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1604:4: ( (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1605:5: (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1605:5: (lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1606:6: lv_leftattribute_1_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getLeftattributeAttributeWithoutAliasDefinitionParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftattribute_1_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
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
                    // InternalCQL.g:1624:4: ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQL.g:1624:4: ( (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQL.g:1625:5: (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQL.g:1625:5: (lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQL.g:1626:6: lv_leftconstant_2_0= ruleAtomicWithoutAttributeRef
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getLeftconstantAtomicWithoutAttributeRefParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftconstant_2_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    						}
                    						set(
                    							current,
                    							"leftconstant",
                    							lv_leftconstant_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1644:3: ( ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) ) )
            // InternalCQL.g:1645:4: ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) )
            {
            // InternalCQL.g:1645:4: ( (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' ) )
            // InternalCQL.g:1646:5: (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' )
            {
            // InternalCQL.g:1646:5: (lv_operator_3_1= '+' | lv_operator_3_2= '-' | lv_operator_3_3= '/' | lv_operator_3_4= '*' )
            int alt30=4;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt30=1;
                }
                break;
            case 39:
                {
                alt30=2;
                }
                break;
            case 40:
                {
                alt30=3;
                }
                break;
            case 15:
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
                    // InternalCQL.g:1647:6: lv_operator_3_1= '+'
                    {
                    lv_operator_3_1=(Token)match(input,38,FOLLOW_30); 

                    						newLeafNode(lv_operator_3_1, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorPlusSignKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1658:6: lv_operator_3_2= '-'
                    {
                    lv_operator_3_2=(Token)match(input,39,FOLLOW_30); 

                    						newLeafNode(lv_operator_3_2, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorHyphenMinusKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:1669:6: lv_operator_3_3= '/'
                    {
                    lv_operator_3_3=(Token)match(input,40,FOLLOW_30); 

                    						newLeafNode(lv_operator_3_3, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorSolidusKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:1680:6: lv_operator_3_4= '*'
                    {
                    lv_operator_3_4=(Token)match(input,15,FOLLOW_30); 

                    						newLeafNode(lv_operator_3_4, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorAsteriskKeyword_1_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_3_4, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:1693:3: ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )?
            int alt31=2;
            alt31 = dfa31.predict(input);
            switch (alt31) {
                case 1 :
                    // InternalCQL.g:1694:4: (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1694:4: (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition )
                    // InternalCQL.g:1695:5: lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition
                    {

                    					newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getInnerexpressionSelectExpressionWithoutAliasDefinitionParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_innerexpression_4_0=ruleSelectExpressionWithoutAliasDefinition();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    					}
                    					set(
                    						current,
                    						"innerexpression",
                    						lv_innerexpression_4_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionWithoutAliasDefinition");
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
    // $ANTLR end "ruleSelectExpressionWithoutAliasDefinition"


    // $ANTLR start "entryRuleSelectExpressionOnlyWithAttributeAndConstant"
    // InternalCQL.g:1716:1: entryRuleSelectExpressionOnlyWithAttributeAndConstant returns [EObject current=null] : iv_ruleSelectExpressionOnlyWithAttributeAndConstant= ruleSelectExpressionOnlyWithAttributeAndConstant EOF ;
    public final EObject entryRuleSelectExpressionOnlyWithAttributeAndConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionOnlyWithAttributeAndConstant = null;


        try {
            // InternalCQL.g:1716:85: (iv_ruleSelectExpressionOnlyWithAttributeAndConstant= ruleSelectExpressionOnlyWithAttributeAndConstant EOF )
            // InternalCQL.g:1717:2: iv_ruleSelectExpressionOnlyWithAttributeAndConstant= ruleSelectExpressionOnlyWithAttributeAndConstant EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionOnlyWithAttributeAndConstant=ruleSelectExpressionOnlyWithAttributeAndConstant();

            state._fsp--;

             current =iv_ruleSelectExpressionOnlyWithAttributeAndConstant; 
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
    // $ANTLR end "entryRuleSelectExpressionOnlyWithAttributeAndConstant"


    // $ANTLR start "ruleSelectExpressionOnlyWithAttributeAndConstant"
    // InternalCQL.g:1723:1: ruleSelectExpressionOnlyWithAttributeAndConstant returns [EObject current=null] : ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )? ( ( (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef ) ) ) ) ;
    public final EObject ruleSelectExpressionOnlyWithAttributeAndConstant() throws RecognitionException {
        EObject current = null;

        Token lv_operator_2_1=null;
        Token lv_operator_2_2=null;
        Token lv_operator_2_3=null;
        Token lv_operator_2_4=null;
        EObject lv_leftattribute_0_0 = null;

        EObject lv_leftconstant_1_0 = null;

        EObject lv_innerexpression_3_0 = null;

        EObject lv_rightattribute_4_0 = null;

        EObject lv_rightconstant_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1729:2: ( ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )? ( ( (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef ) ) ) ) )
            // InternalCQL.g:1730:2: ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )? ( ( (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef ) ) ) )
            {
            // InternalCQL.g:1730:2: ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )? ( ( (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQL.g:1731:3: ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )? ( ( (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQL.g:1731:3: ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==RULE_ID) ) {
                alt32=1;
            }
            else if ( ((LA32_0>=RULE_STRING && LA32_0<=RULE_FLOAT)||(LA32_0>=70 && LA32_0<=71)) ) {
                alt32=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // InternalCQL.g:1732:4: ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1732:4: ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1733:5: (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1733:5: (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1734:6: lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantAccess().getLeftattributeAttributeWithoutAliasDefinitionParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftattribute_0_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule());
                    						}
                    						set(
                    							current,
                    							"leftattribute",
                    							lv_leftattribute_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1752:4: ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQL.g:1752:4: ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQL.g:1753:5: (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQL.g:1753:5: (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQL.g:1754:6: lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantAccess().getLeftconstantAtomicWithoutAttributeRefParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftconstant_1_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule());
                    						}
                    						set(
                    							current,
                    							"leftconstant",
                    							lv_leftconstant_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1772:3: ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) )
            // InternalCQL.g:1773:4: ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) )
            {
            // InternalCQL.g:1773:4: ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) )
            // InternalCQL.g:1774:5: (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' )
            {
            // InternalCQL.g:1774:5: (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' )
            int alt33=4;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt33=1;
                }
                break;
            case 39:
                {
                alt33=2;
                }
                break;
            case 40:
                {
                alt33=3;
                }
                break;
            case 15:
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
                    // InternalCQL.g:1775:6: lv_operator_2_1= '+'
                    {
                    lv_operator_2_1=(Token)match(input,38,FOLLOW_5); 

                    						newLeafNode(lv_operator_2_1, grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantAccess().getOperatorPlusSignKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_2_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1786:6: lv_operator_2_2= '-'
                    {
                    lv_operator_2_2=(Token)match(input,39,FOLLOW_5); 

                    						newLeafNode(lv_operator_2_2, grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantAccess().getOperatorHyphenMinusKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_2_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:1797:6: lv_operator_2_3= '/'
                    {
                    lv_operator_2_3=(Token)match(input,40,FOLLOW_5); 

                    						newLeafNode(lv_operator_2_3, grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantAccess().getOperatorSolidusKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_2_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:1808:6: lv_operator_2_4= '*'
                    {
                    lv_operator_2_4=(Token)match(input,15,FOLLOW_5); 

                    						newLeafNode(lv_operator_2_4, grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantAccess().getOperatorAsteriskKeyword_1_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_2_4, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:1821:3: ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )?
            int alt34=2;
            alt34 = dfa34.predict(input);
            switch (alt34) {
                case 1 :
                    // InternalCQL.g:1822:4: (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 )
                    {
                    // InternalCQL.g:1822:4: (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 )
                    // InternalCQL.g:1823:5: lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2
                    {

                    					newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantAccess().getInnerexpressionSelectExpressionOnlyWithAttributeAndConstant2ParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_5);
                    lv_innerexpression_3_0=ruleSelectExpressionOnlyWithAttributeAndConstant2();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule());
                    					}
                    					set(
                    						current,
                    						"innerexpression",
                    						lv_innerexpression_3_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionOnlyWithAttributeAndConstant2");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:1840:3: ( ( (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==RULE_ID) ) {
                alt35=1;
            }
            else if ( ((LA35_0>=RULE_STRING && LA35_0<=RULE_FLOAT)||(LA35_0>=70 && LA35_0<=71)) ) {
                alt35=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;
            }
            switch (alt35) {
                case 1 :
                    // InternalCQL.g:1841:4: ( (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1841:4: ( (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1842:5: (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1842:5: (lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1843:6: lv_rightattribute_4_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantAccess().getRightattributeAttributeWithoutAliasDefinitionParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_rightattribute_4_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule());
                    						}
                    						set(
                    							current,
                    							"rightattribute",
                    							lv_rightattribute_4_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1861:4: ( (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQL.g:1861:4: ( (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQL.g:1862:5: (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQL.g:1862:5: (lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQL.g:1863:6: lv_rightconstant_5_0= ruleAtomicWithoutAttributeRef
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantAccess().getRightconstantAtomicWithoutAttributeRefParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_rightconstant_5_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstantRule());
                    						}
                    						set(
                    							current,
                    							"rightconstant",
                    							lv_rightconstant_5_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
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
    // $ANTLR end "ruleSelectExpressionOnlyWithAttributeAndConstant"


    // $ANTLR start "entryRuleSelectExpressionOnlyWithAttributeAndConstant2"
    // InternalCQL.g:1885:1: entryRuleSelectExpressionOnlyWithAttributeAndConstant2 returns [EObject current=null] : iv_ruleSelectExpressionOnlyWithAttributeAndConstant2= ruleSelectExpressionOnlyWithAttributeAndConstant2 EOF ;
    public final EObject entryRuleSelectExpressionOnlyWithAttributeAndConstant2() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionOnlyWithAttributeAndConstant2 = null;


        try {
            // InternalCQL.g:1885:86: (iv_ruleSelectExpressionOnlyWithAttributeAndConstant2= ruleSelectExpressionOnlyWithAttributeAndConstant2 EOF )
            // InternalCQL.g:1886:2: iv_ruleSelectExpressionOnlyWithAttributeAndConstant2= ruleSelectExpressionOnlyWithAttributeAndConstant2 EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Rule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionOnlyWithAttributeAndConstant2=ruleSelectExpressionOnlyWithAttributeAndConstant2();

            state._fsp--;

             current =iv_ruleSelectExpressionOnlyWithAttributeAndConstant2; 
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
    // $ANTLR end "entryRuleSelectExpressionOnlyWithAttributeAndConstant2"


    // $ANTLR start "ruleSelectExpressionOnlyWithAttributeAndConstant2"
    // InternalCQL.g:1892:1: ruleSelectExpressionOnlyWithAttributeAndConstant2 returns [EObject current=null] : ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )? ) ;
    public final EObject ruleSelectExpressionOnlyWithAttributeAndConstant2() throws RecognitionException {
        EObject current = null;

        Token lv_operator_2_1=null;
        Token lv_operator_2_2=null;
        Token lv_operator_2_3=null;
        Token lv_operator_2_4=null;
        EObject lv_leftattribute_0_0 = null;

        EObject lv_leftconstant_1_0 = null;

        EObject lv_innerexpression_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1898:2: ( ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )? ) )
            // InternalCQL.g:1899:2: ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )? )
            {
            // InternalCQL.g:1899:2: ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )? )
            // InternalCQL.g:1900:3: ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) ) ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) ) ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )?
            {
            // InternalCQL.g:1900:3: ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==RULE_ID) ) {
                alt36=1;
            }
            else if ( ((LA36_0>=RULE_STRING && LA36_0<=RULE_FLOAT)||(LA36_0>=70 && LA36_0<=71)) ) {
                alt36=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // InternalCQL.g:1901:4: ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1901:4: ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1902:5: (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1902:5: (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1903:6: lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Access().getLeftattributeAttributeWithoutAliasDefinitionParserRuleCall_0_0_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftattribute_0_0=ruleAttributeWithoutAliasDefinition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Rule());
                    						}
                    						set(
                    							current,
                    							"leftattribute",
                    							lv_leftattribute_0_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1921:4: ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQL.g:1921:4: ( (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQL.g:1922:5: (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQL.g:1922:5: (lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQL.g:1923:6: lv_leftconstant_1_0= ruleAtomicWithoutAttributeRef
                    {

                    						newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Access().getLeftconstantAtomicWithoutAttributeRefParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_leftconstant_1_0=ruleAtomicWithoutAttributeRef();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Rule());
                    						}
                    						set(
                    							current,
                    							"leftconstant",
                    							lv_leftconstant_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithoutAttributeRef");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1941:3: ( ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) ) )
            // InternalCQL.g:1942:4: ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) )
            {
            // InternalCQL.g:1942:4: ( (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' ) )
            // InternalCQL.g:1943:5: (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' )
            {
            // InternalCQL.g:1943:5: (lv_operator_2_1= '+' | lv_operator_2_2= '-' | lv_operator_2_3= '/' | lv_operator_2_4= '*' )
            int alt37=4;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt37=1;
                }
                break;
            case 39:
                {
                alt37=2;
                }
                break;
            case 40:
                {
                alt37=3;
                }
                break;
            case 15:
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
                    // InternalCQL.g:1944:6: lv_operator_2_1= '+'
                    {
                    lv_operator_2_1=(Token)match(input,38,FOLLOW_30); 

                    						newLeafNode(lv_operator_2_1, grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Access().getOperatorPlusSignKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_2_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1955:6: lv_operator_2_2= '-'
                    {
                    lv_operator_2_2=(Token)match(input,39,FOLLOW_30); 

                    						newLeafNode(lv_operator_2_2, grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Access().getOperatorHyphenMinusKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_2_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:1966:6: lv_operator_2_3= '/'
                    {
                    lv_operator_2_3=(Token)match(input,40,FOLLOW_30); 

                    						newLeafNode(lv_operator_2_3, grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Access().getOperatorSolidusKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_2_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:1977:6: lv_operator_2_4= '*'
                    {
                    lv_operator_2_4=(Token)match(input,15,FOLLOW_30); 

                    						newLeafNode(lv_operator_2_4, grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Access().getOperatorAsteriskKeyword_1_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_2_4, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:1990:3: ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )?
            int alt38=2;
            alt38 = dfa38.predict(input);
            switch (alt38) {
                case 1 :
                    // InternalCQL.g:1991:4: (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 )
                    {
                    // InternalCQL.g:1991:4: (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 )
                    // InternalCQL.g:1992:5: lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2
                    {

                    					newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Access().getInnerexpressionSelectExpressionOnlyWithAttributeAndConstant2ParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_innerexpression_3_0=ruleSelectExpressionOnlyWithAttributeAndConstant2();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeAndConstant2Rule());
                    					}
                    					set(
                    						current,
                    						"innerexpression",
                    						lv_innerexpression_3_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionOnlyWithAttributeAndConstant2");
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
    // $ANTLR end "ruleSelectExpressionOnlyWithAttributeAndConstant2"


    // $ANTLR start "entryRuleSelectExpressionOnlyWithStringConstant2"
    // InternalCQL.g:2013:1: entryRuleSelectExpressionOnlyWithStringConstant2 returns [EObject current=null] : iv_ruleSelectExpressionOnlyWithStringConstant2= ruleSelectExpressionOnlyWithStringConstant2 EOF ;
    public final EObject entryRuleSelectExpressionOnlyWithStringConstant2() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionOnlyWithStringConstant2 = null;


        try {
            // InternalCQL.g:2013:80: (iv_ruleSelectExpressionOnlyWithStringConstant2= ruleSelectExpressionOnlyWithStringConstant2 EOF )
            // InternalCQL.g:2014:2: iv_ruleSelectExpressionOnlyWithStringConstant2= ruleSelectExpressionOnlyWithStringConstant2 EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionOnlyWithStringConstant2Rule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionOnlyWithStringConstant2=ruleSelectExpressionOnlyWithStringConstant2();

            state._fsp--;

             current =iv_ruleSelectExpressionOnlyWithStringConstant2; 
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
    // $ANTLR end "entryRuleSelectExpressionOnlyWithStringConstant2"


    // $ANTLR start "ruleSelectExpressionOnlyWithStringConstant2"
    // InternalCQL.g:2020:1: ruleSelectExpressionOnlyWithStringConstant2 returns [EObject current=null] : ( ( (lv_leftconstant_0_0= ruleAtomicWithOnlyStringConstant ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithStringConstant2 ) )? ) ;
    public final EObject ruleSelectExpressionOnlyWithStringConstant2() throws RecognitionException {
        EObject current = null;

        Token lv_operator_1_1=null;
        Token lv_operator_1_2=null;
        EObject lv_leftconstant_0_0 = null;

        EObject lv_innerexpression_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2026:2: ( ( ( (lv_leftconstant_0_0= ruleAtomicWithOnlyStringConstant ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithStringConstant2 ) )? ) )
            // InternalCQL.g:2027:2: ( ( (lv_leftconstant_0_0= ruleAtomicWithOnlyStringConstant ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithStringConstant2 ) )? )
            {
            // InternalCQL.g:2027:2: ( ( (lv_leftconstant_0_0= ruleAtomicWithOnlyStringConstant ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithStringConstant2 ) )? )
            // InternalCQL.g:2028:3: ( (lv_leftconstant_0_0= ruleAtomicWithOnlyStringConstant ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithStringConstant2 ) )?
            {
            // InternalCQL.g:2028:3: ( (lv_leftconstant_0_0= ruleAtomicWithOnlyStringConstant ) )
            // InternalCQL.g:2029:4: (lv_leftconstant_0_0= ruleAtomicWithOnlyStringConstant )
            {
            // InternalCQL.g:2029:4: (lv_leftconstant_0_0= ruleAtomicWithOnlyStringConstant )
            // InternalCQL.g:2030:5: lv_leftconstant_0_0= ruleAtomicWithOnlyStringConstant
            {

            					newCompositeNode(grammarAccess.getSelectExpressionOnlyWithStringConstant2Access().getLeftconstantAtomicWithOnlyStringConstantParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_31);
            lv_leftconstant_0_0=ruleAtomicWithOnlyStringConstant();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithStringConstant2Rule());
            					}
            					set(
            						current,
            						"leftconstant",
            						lv_leftconstant_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AtomicWithOnlyStringConstant");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQL.g:2047:3: ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' ) ) )
            // InternalCQL.g:2048:4: ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' ) )
            {
            // InternalCQL.g:2048:4: ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' ) )
            // InternalCQL.g:2049:5: (lv_operator_1_1= '+' | lv_operator_1_2= '-' )
            {
            // InternalCQL.g:2049:5: (lv_operator_1_1= '+' | lv_operator_1_2= '-' )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==38) ) {
                alt39=1;
            }
            else if ( (LA39_0==39) ) {
                alt39=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // InternalCQL.g:2050:6: lv_operator_1_1= '+'
                    {
                    lv_operator_1_1=(Token)match(input,38,FOLLOW_32); 

                    						newLeafNode(lv_operator_1_1, grammarAccess.getSelectExpressionOnlyWithStringConstant2Access().getOperatorPlusSignKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithStringConstant2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:2061:6: lv_operator_1_2= '-'
                    {
                    lv_operator_1_2=(Token)match(input,39,FOLLOW_32); 

                    						newLeafNode(lv_operator_1_2, grammarAccess.getSelectExpressionOnlyWithStringConstant2Access().getOperatorHyphenMinusKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithStringConstant2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_2, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:2074:3: ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithStringConstant2 ) )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==RULE_STRING) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalCQL.g:2075:4: (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithStringConstant2 )
                    {
                    // InternalCQL.g:2075:4: (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithStringConstant2 )
                    // InternalCQL.g:2076:5: lv_innerexpression_2_0= ruleSelectExpressionOnlyWithStringConstant2
                    {

                    					newCompositeNode(grammarAccess.getSelectExpressionOnlyWithStringConstant2Access().getInnerexpressionSelectExpressionOnlyWithStringConstant2ParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_innerexpression_2_0=ruleSelectExpressionOnlyWithStringConstant2();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithStringConstant2Rule());
                    					}
                    					set(
                    						current,
                    						"innerexpression",
                    						lv_innerexpression_2_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionOnlyWithStringConstant2");
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
    // $ANTLR end "ruleSelectExpressionOnlyWithStringConstant2"


    // $ANTLR start "entryRuleSelectExpressionOnlyWithAttribute"
    // InternalCQL.g:2097:1: entryRuleSelectExpressionOnlyWithAttribute returns [EObject current=null] : iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF ;
    public final EObject entryRuleSelectExpressionOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionOnlyWithAttribute = null;


        try {
            // InternalCQL.g:2097:74: (iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF )
            // InternalCQL.g:2098:2: iv_ruleSelectExpressionOnlyWithAttribute= ruleSelectExpressionOnlyWithAttribute EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionOnlyWithAttribute=ruleSelectExpressionOnlyWithAttribute();

            state._fsp--;

             current =iv_ruleSelectExpressionOnlyWithAttribute; 
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
    // $ANTLR end "entryRuleSelectExpressionOnlyWithAttribute"


    // $ANTLR start "ruleSelectExpressionOnlyWithAttribute"
    // InternalCQL.g:2104:1: ruleSelectExpressionOnlyWithAttribute returns [EObject current=null] : ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )? ( (lv_rightatttriute_3_0= ruleAttributeWithoutAliasDefinition ) ) ) ;
    public final EObject ruleSelectExpressionOnlyWithAttribute() throws RecognitionException {
        EObject current = null;

        Token lv_operator_1_1=null;
        Token lv_operator_1_2=null;
        Token lv_operator_1_3=null;
        Token lv_operator_1_4=null;
        EObject lv_leftattribute_0_0 = null;

        EObject lv_innerexpression_2_0 = null;

        EObject lv_rightatttriute_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2110:2: ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )? ( (lv_rightatttriute_3_0= ruleAttributeWithoutAliasDefinition ) ) ) )
            // InternalCQL.g:2111:2: ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )? ( (lv_rightatttriute_3_0= ruleAttributeWithoutAliasDefinition ) ) )
            {
            // InternalCQL.g:2111:2: ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )? ( (lv_rightatttriute_3_0= ruleAttributeWithoutAliasDefinition ) ) )
            // InternalCQL.g:2112:3: ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )? ( (lv_rightatttriute_3_0= ruleAttributeWithoutAliasDefinition ) )
            {
            // InternalCQL.g:2112:3: ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:2113:4: (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:2113:4: (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:2114:5: lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAccess().getLeftattributeAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_29);
            lv_leftattribute_0_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeRule());
            					}
            					set(
            						current,
            						"leftattribute",
            						lv_leftattribute_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQL.g:2131:3: ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) )
            // InternalCQL.g:2132:4: ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) )
            {
            // InternalCQL.g:2132:4: ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) )
            // InternalCQL.g:2133:5: (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' )
            {
            // InternalCQL.g:2133:5: (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' )
            int alt41=4;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt41=1;
                }
                break;
            case 39:
                {
                alt41=2;
                }
                break;
            case 40:
                {
                alt41=3;
                }
                break;
            case 15:
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
                    // InternalCQL.g:2134:6: lv_operator_1_1= '+'
                    {
                    lv_operator_1_1=(Token)match(input,38,FOLLOW_8); 

                    						newLeafNode(lv_operator_1_1, grammarAccess.getSelectExpressionOnlyWithAttributeAccess().getOperatorPlusSignKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:2145:6: lv_operator_1_2= '-'
                    {
                    lv_operator_1_2=(Token)match(input,39,FOLLOW_8); 

                    						newLeafNode(lv_operator_1_2, grammarAccess.getSelectExpressionOnlyWithAttributeAccess().getOperatorHyphenMinusKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:2156:6: lv_operator_1_3= '/'
                    {
                    lv_operator_1_3=(Token)match(input,40,FOLLOW_8); 

                    						newLeafNode(lv_operator_1_3, grammarAccess.getSelectExpressionOnlyWithAttributeAccess().getOperatorSolidusKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:2167:6: lv_operator_1_4= '*'
                    {
                    lv_operator_1_4=(Token)match(input,15,FOLLOW_8); 

                    						newLeafNode(lv_operator_1_4, grammarAccess.getSelectExpressionOnlyWithAttributeAccess().getOperatorAsteriskKeyword_1_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttributeRule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_4, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:2180:3: ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==RULE_ID) ) {
                int LA42_1 = input.LA(2);

                if ( (LA42_1==27) ) {
                    int LA42_3 = input.LA(3);

                    if ( (LA42_3==RULE_ID) ) {
                        int LA42_5 = input.LA(4);

                        if ( (LA42_5==15||(LA42_5>=38 && LA42_5<=40)) ) {
                            alt42=1;
                        }
                    }
                }
                else if ( (LA42_1==15||(LA42_1>=38 && LA42_1<=40)) ) {
                    alt42=1;
                }
            }
            switch (alt42) {
                case 1 :
                    // InternalCQL.g:2181:4: (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 )
                    {
                    // InternalCQL.g:2181:4: (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 )
                    // InternalCQL.g:2182:5: lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2
                    {

                    					newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAccess().getInnerexpressionSelectExpressionOnlyWithAttribute2ParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_8);
                    lv_innerexpression_2_0=ruleSelectExpressionOnlyWithAttribute2();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeRule());
                    					}
                    					set(
                    						current,
                    						"innerexpression",
                    						lv_innerexpression_2_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionOnlyWithAttribute2");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:2199:3: ( (lv_rightatttriute_3_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:2200:4: (lv_rightatttriute_3_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:2200:4: (lv_rightatttriute_3_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:2201:5: lv_rightatttriute_3_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttributeAccess().getRightatttriuteAttributeWithoutAliasDefinitionParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_rightatttriute_3_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttributeRule());
            					}
            					set(
            						current,
            						"rightatttriute",
            						lv_rightatttriute_3_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
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
    // $ANTLR end "ruleSelectExpressionOnlyWithAttribute"


    // $ANTLR start "entryRuleSelectExpressionOnlyWithAttribute2"
    // InternalCQL.g:2222:1: entryRuleSelectExpressionOnlyWithAttribute2 returns [EObject current=null] : iv_ruleSelectExpressionOnlyWithAttribute2= ruleSelectExpressionOnlyWithAttribute2 EOF ;
    public final EObject entryRuleSelectExpressionOnlyWithAttribute2() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionOnlyWithAttribute2 = null;


        try {
            // InternalCQL.g:2222:75: (iv_ruleSelectExpressionOnlyWithAttribute2= ruleSelectExpressionOnlyWithAttribute2 EOF )
            // InternalCQL.g:2223:2: iv_ruleSelectExpressionOnlyWithAttribute2= ruleSelectExpressionOnlyWithAttribute2 EOF
            {
             newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttribute2Rule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectExpressionOnlyWithAttribute2=ruleSelectExpressionOnlyWithAttribute2();

            state._fsp--;

             current =iv_ruleSelectExpressionOnlyWithAttribute2; 
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
    // $ANTLR end "entryRuleSelectExpressionOnlyWithAttribute2"


    // $ANTLR start "ruleSelectExpressionOnlyWithAttribute2"
    // InternalCQL.g:2229:1: ruleSelectExpressionOnlyWithAttribute2 returns [EObject current=null] : ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )? ) ;
    public final EObject ruleSelectExpressionOnlyWithAttribute2() throws RecognitionException {
        EObject current = null;

        Token lv_operator_1_1=null;
        Token lv_operator_1_2=null;
        Token lv_operator_1_3=null;
        Token lv_operator_1_4=null;
        EObject lv_leftattribute_0_0 = null;

        EObject lv_innerexpression_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2235:2: ( ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )? ) )
            // InternalCQL.g:2236:2: ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )? )
            {
            // InternalCQL.g:2236:2: ( ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )? )
            // InternalCQL.g:2237:3: ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) ) ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) ) ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )?
            {
            // InternalCQL.g:2237:3: ( (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:2238:4: (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:2238:4: (lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:2239:5: lv_leftattribute_0_0= ruleAttributeWithoutAliasDefinition
            {

            					newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttribute2Access().getLeftattributeAttributeWithoutAliasDefinitionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_29);
            lv_leftattribute_0_0=ruleAttributeWithoutAliasDefinition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttribute2Rule());
            					}
            					set(
            						current,
            						"leftattribute",
            						lv_leftattribute_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAliasDefinition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQL.g:2256:3: ( ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) ) )
            // InternalCQL.g:2257:4: ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) )
            {
            // InternalCQL.g:2257:4: ( (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' ) )
            // InternalCQL.g:2258:5: (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' )
            {
            // InternalCQL.g:2258:5: (lv_operator_1_1= '+' | lv_operator_1_2= '-' | lv_operator_1_3= '/' | lv_operator_1_4= '*' )
            int alt43=4;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt43=1;
                }
                break;
            case 39:
                {
                alt43=2;
                }
                break;
            case 40:
                {
                alt43=3;
                }
                break;
            case 15:
                {
                alt43=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 43, 0, input);

                throw nvae;
            }

            switch (alt43) {
                case 1 :
                    // InternalCQL.g:2259:6: lv_operator_1_1= '+'
                    {
                    lv_operator_1_1=(Token)match(input,38,FOLLOW_33); 

                    						newLeafNode(lv_operator_1_1, grammarAccess.getSelectExpressionOnlyWithAttribute2Access().getOperatorPlusSignKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttribute2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:2270:6: lv_operator_1_2= '-'
                    {
                    lv_operator_1_2=(Token)match(input,39,FOLLOW_33); 

                    						newLeafNode(lv_operator_1_2, grammarAccess.getSelectExpressionOnlyWithAttribute2Access().getOperatorHyphenMinusKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttribute2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalCQL.g:2281:6: lv_operator_1_3= '/'
                    {
                    lv_operator_1_3=(Token)match(input,40,FOLLOW_33); 

                    						newLeafNode(lv_operator_1_3, grammarAccess.getSelectExpressionOnlyWithAttribute2Access().getOperatorSolidusKeyword_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttribute2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_3, null);
                    					

                    }
                    break;
                case 4 :
                    // InternalCQL.g:2292:6: lv_operator_1_4= '*'
                    {
                    lv_operator_1_4=(Token)match(input,15,FOLLOW_33); 

                    						newLeafNode(lv_operator_1_4, grammarAccess.getSelectExpressionOnlyWithAttribute2Access().getOperatorAsteriskKeyword_1_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectExpressionOnlyWithAttribute2Rule());
                    						}
                    						setWithLastConsumed(current, "operator", lv_operator_1_4, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:2305:3: ( (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 ) )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==RULE_ID) ) {
                int LA44_1 = input.LA(2);

                if ( (LA44_1==27) ) {
                    int LA44_3 = input.LA(3);

                    if ( (LA44_3==RULE_ID) ) {
                        int LA44_5 = input.LA(4);

                        if ( (LA44_5==15||(LA44_5>=38 && LA44_5<=40)) ) {
                            alt44=1;
                        }
                    }
                }
                else if ( (LA44_1==15||(LA44_1>=38 && LA44_1<=40)) ) {
                    alt44=1;
                }
            }
            switch (alt44) {
                case 1 :
                    // InternalCQL.g:2306:4: (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 )
                    {
                    // InternalCQL.g:2306:4: (lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2 )
                    // InternalCQL.g:2307:5: lv_innerexpression_2_0= ruleSelectExpressionOnlyWithAttribute2
                    {

                    					newCompositeNode(grammarAccess.getSelectExpressionOnlyWithAttribute2Access().getInnerexpressionSelectExpressionOnlyWithAttribute2ParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_innerexpression_2_0=ruleSelectExpressionOnlyWithAttribute2();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectExpressionOnlyWithAttribute2Rule());
                    					}
                    					set(
                    						current,
                    						"innerexpression",
                    						lv_innerexpression_2_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.SelectExpressionOnlyWithAttribute2");
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
    // $ANTLR end "ruleSelectExpressionOnlyWithAttribute2"


    // $ANTLR start "entryRuleAlias"
    // InternalCQL.g:2328:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQL.g:2328:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQL.g:2329:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQL.g:2335:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:2341:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:2342:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:2342:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:2343:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:2343:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:2344:4: lv_name_0_0= RULE_ID
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
    // InternalCQL.g:2363:1: entryRuleCreateParameters returns [EObject current=null] : iv_ruleCreateParameters= ruleCreateParameters EOF ;
    public final EObject entryRuleCreateParameters() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateParameters = null;


        try {
            // InternalCQL.g:2363:57: (iv_ruleCreateParameters= ruleCreateParameters EOF )
            // InternalCQL.g:2364:2: iv_ruleCreateParameters= ruleCreateParameters EOF
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
    // InternalCQL.g:2370:1: ruleCreateParameters returns [EObject current=null] : (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' ) ;
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
            // InternalCQL.g:2376:2: ( (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' ) )
            // InternalCQL.g:2377:2: (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' )
            {
            // InternalCQL.g:2377:2: (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' )
            // InternalCQL.g:2378:3: otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')'
            {
            otherlv_0=(Token)match(input,41,FOLLOW_34); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateParametersAccess().getWRAPPERKeyword_0());
            		
            // InternalCQL.g:2382:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQL.g:2383:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQL.g:2383:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQL.g:2384:5: lv_wrapper_1_0= RULE_STRING
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
            		
            // InternalCQL.g:2404:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQL.g:2405:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQL.g:2405:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQL.g:2406:5: lv_protocol_3_0= RULE_STRING
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
            		
            // InternalCQL.g:2426:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQL.g:2427:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQL.g:2427:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQL.g:2428:5: lv_transport_5_0= RULE_STRING
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
            		
            // InternalCQL.g:2448:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQL.g:2449:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQL.g:2449:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQL.g:2450:5: lv_datahandler_7_0= RULE_STRING
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

            otherlv_8=(Token)match(input,45,FOLLOW_28); 

            			newLeafNode(otherlv_8, grammarAccess.getCreateParametersAccess().getOPTIONSKeyword_8());
            		
            otherlv_9=(Token)match(input,22,FOLLOW_34); 

            			newLeafNode(otherlv_9, grammarAccess.getCreateParametersAccess().getLeftParenthesisKeyword_9());
            		
            // InternalCQL.g:2474:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+
            int cnt45=0;
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==RULE_STRING) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // InternalCQL.g:2475:4: ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) )
            	    {
            	    // InternalCQL.g:2475:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQL.g:2476:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:2476:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQL.g:2477:6: lv_keys_10_0= RULE_STRING
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

            	    // InternalCQL.g:2493:4: ( (lv_values_11_0= RULE_STRING ) )
            	    // InternalCQL.g:2494:5: (lv_values_11_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:2494:5: (lv_values_11_0= RULE_STRING )
            	    // InternalCQL.g:2495:6: lv_values_11_0= RULE_STRING
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
            	    if ( cnt45 >= 1 ) break loop45;
                        EarlyExitException eee =
                            new EarlyExitException(45, input);
                        throw eee;
                }
                cnt45++;
            } while (true);

            // InternalCQL.g:2512:3: (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==16) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // InternalCQL.g:2513:4: otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) )
                    {
                    otherlv_12=(Token)match(input,16,FOLLOW_34); 

                    				newLeafNode(otherlv_12, grammarAccess.getCreateParametersAccess().getCommaKeyword_11_0());
                    			
                    // InternalCQL.g:2517:4: ( (lv_keys_13_0= RULE_STRING ) )
                    // InternalCQL.g:2518:5: (lv_keys_13_0= RULE_STRING )
                    {
                    // InternalCQL.g:2518:5: (lv_keys_13_0= RULE_STRING )
                    // InternalCQL.g:2519:6: lv_keys_13_0= RULE_STRING
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

                    // InternalCQL.g:2535:4: ( (lv_values_14_0= RULE_STRING ) )
                    // InternalCQL.g:2536:5: (lv_values_14_0= RULE_STRING )
                    {
                    // InternalCQL.g:2536:5: (lv_values_14_0= RULE_STRING )
                    // InternalCQL.g:2537:6: lv_values_14_0= RULE_STRING
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
    // InternalCQL.g:2562:1: entryRuleAttributeDefinition returns [EObject current=null] : iv_ruleAttributeDefinition= ruleAttributeDefinition EOF ;
    public final EObject entryRuleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeDefinition = null;


        try {
            // InternalCQL.g:2562:60: (iv_ruleAttributeDefinition= ruleAttributeDefinition EOF )
            // InternalCQL.g:2563:2: iv_ruleAttributeDefinition= ruleAttributeDefinition EOF
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
    // InternalCQL.g:2569:1: ruleAttributeDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' ) ;
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
            // InternalCQL.g:2575:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' ) )
            // InternalCQL.g:2576:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' )
            {
            // InternalCQL.g:2576:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' )
            // InternalCQL.g:2577:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')'
            {
            // InternalCQL.g:2577:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:2578:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:2578:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:2579:5: lv_name_0_0= RULE_ID
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

            otherlv_1=(Token)match(input,22,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeDefinitionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:2599:3: ( (lv_attributes_2_0= ruleAttribute ) )+
            int cnt47=0;
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==RULE_ID) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // InternalCQL.g:2600:4: (lv_attributes_2_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2600:4: (lv_attributes_2_0= ruleAttribute )
            	    // InternalCQL.g:2601:5: lv_attributes_2_0= ruleAttribute
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
            	    if ( cnt47 >= 1 ) break loop47;
                        EarlyExitException eee =
                            new EarlyExitException(47, input);
                        throw eee;
                }
                cnt47++;
            } while (true);

            // InternalCQL.g:2618:3: ( (lv_datatypes_3_0= ruleDataType ) )+
            int cnt48=0;
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( ((LA48_0>=72 && LA48_0<=79)) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // InternalCQL.g:2619:4: (lv_datatypes_3_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2619:4: (lv_datatypes_3_0= ruleDataType )
            	    // InternalCQL.g:2620:5: lv_datatypes_3_0= ruleDataType
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
            	    if ( cnt48 >= 1 ) break loop48;
                        EarlyExitException eee =
                            new EarlyExitException(48, input);
                        throw eee;
                }
                cnt48++;
            } while (true);

            // InternalCQL.g:2637:3: (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )*
            loop49:
            do {
                int alt49=2;
                int LA49_0 = input.LA(1);

                if ( (LA49_0==16) ) {
                    alt49=1;
                }


                switch (alt49) {
            	case 1 :
            	    // InternalCQL.g:2638:4: otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) )
            	    {
            	    otherlv_4=(Token)match(input,16,FOLLOW_8); 

            	    				newLeafNode(otherlv_4, grammarAccess.getAttributeDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQL.g:2642:4: ( (lv_attributes_5_0= ruleAttribute ) )
            	    // InternalCQL.g:2643:5: (lv_attributes_5_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2643:5: (lv_attributes_5_0= ruleAttribute )
            	    // InternalCQL.g:2644:6: lv_attributes_5_0= ruleAttribute
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

            	    // InternalCQL.g:2661:4: ( (lv_datatypes_6_0= ruleDataType ) )
            	    // InternalCQL.g:2662:5: (lv_datatypes_6_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2662:5: (lv_datatypes_6_0= ruleDataType )
            	    // InternalCQL.g:2663:6: lv_datatypes_6_0= ruleDataType
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
            	    break loop49;
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
    // InternalCQL.g:2689:1: entryRuleCreateStream1 returns [EObject current=null] : iv_ruleCreateStream1= ruleCreateStream1 EOF ;
    public final EObject entryRuleCreateStream1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStream1 = null;


        try {
            // InternalCQL.g:2689:54: (iv_ruleCreateStream1= ruleCreateStream1 EOF )
            // InternalCQL.g:2690:2: iv_ruleCreateStream1= ruleCreateStream1 EOF
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
    // InternalCQL.g:2696:1: ruleCreateStream1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateStream1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2702:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQL.g:2703:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQL.g:2703:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQL.g:2704:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQL.g:2704:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2705:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2705:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2706:5: lv_keyword_0_0= ruleCreateKeyword
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
            		
            // InternalCQL.g:2727:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2728:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2728:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2729:5: lv_attributes_2_0= ruleAttributeDefinition
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

            // InternalCQL.g:2746:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQL.g:2747:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQL.g:2747:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQL.g:2748:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQL.g:2769:1: entryRuleCreateSink1 returns [EObject current=null] : iv_ruleCreateSink1= ruleCreateSink1 EOF ;
    public final EObject entryRuleCreateSink1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateSink1 = null;


        try {
            // InternalCQL.g:2769:52: (iv_ruleCreateSink1= ruleCreateSink1 EOF )
            // InternalCQL.g:2770:2: iv_ruleCreateSink1= ruleCreateSink1 EOF
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
    // InternalCQL.g:2776:1: ruleCreateSink1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateSink1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2782:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQL.g:2783:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQL.g:2783:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQL.g:2784:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQL.g:2784:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2785:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2785:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2786:5: lv_keyword_0_0= ruleCreateKeyword
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
            		
            // InternalCQL.g:2807:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2808:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2808:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2809:5: lv_attributes_2_0= ruleAttributeDefinition
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

            // InternalCQL.g:2826:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQL.g:2827:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQL.g:2827:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQL.g:2828:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQL.g:2849:1: entryRuleCreateStreamChannel returns [EObject current=null] : iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF ;
    public final EObject entryRuleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamChannel = null;


        try {
            // InternalCQL.g:2849:60: (iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF )
            // InternalCQL.g:2850:2: iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF
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
    // InternalCQL.g:2856:1: ruleCreateStreamChannel returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) ) ;
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
            // InternalCQL.g:2862:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) ) )
            // InternalCQL.g:2863:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) )
            {
            // InternalCQL.g:2863:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) )
            // InternalCQL.g:2864:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) )
            {
            // InternalCQL.g:2864:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2865:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2865:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2866:5: lv_keyword_0_0= ruleCreateKeyword
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
            		
            // InternalCQL.g:2887:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2888:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2888:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2889:5: lv_attributes_2_0= ruleAttributeDefinition
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
            		
            // InternalCQL.g:2910:3: ( (lv_host_4_0= RULE_ID ) )
            // InternalCQL.g:2911:4: (lv_host_4_0= RULE_ID )
            {
            // InternalCQL.g:2911:4: (lv_host_4_0= RULE_ID )
            // InternalCQL.g:2912:5: lv_host_4_0= RULE_ID
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
            		
            // InternalCQL.g:2932:3: ( (lv_port_6_0= RULE_INT ) )
            // InternalCQL.g:2933:4: (lv_port_6_0= RULE_INT )
            {
            // InternalCQL.g:2933:4: (lv_port_6_0= RULE_INT )
            // InternalCQL.g:2934:5: lv_port_6_0= RULE_INT
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
    // InternalCQL.g:2954:1: entryRuleCreateStreamFile returns [EObject current=null] : iv_ruleCreateStreamFile= ruleCreateStreamFile EOF ;
    public final EObject entryRuleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamFile = null;


        try {
            // InternalCQL.g:2954:57: (iv_ruleCreateStreamFile= ruleCreateStreamFile EOF )
            // InternalCQL.g:2955:2: iv_ruleCreateStreamFile= ruleCreateStreamFile EOF
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
    // InternalCQL.g:2961:1: ruleCreateStreamFile returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) ) ;
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
            // InternalCQL.g:2967:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) ) )
            // InternalCQL.g:2968:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) )
            {
            // InternalCQL.g:2968:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) )
            // InternalCQL.g:2969:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) )
            {
            // InternalCQL.g:2969:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2970:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2970:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2971:5: lv_keyword_0_0= ruleCreateKeyword
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
            		
            // InternalCQL.g:2992:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2993:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2993:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2994:5: lv_attributes_2_0= ruleAttributeDefinition
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
            		
            // InternalCQL.g:3015:3: ( (lv_filename_4_0= RULE_STRING ) )
            // InternalCQL.g:3016:4: (lv_filename_4_0= RULE_STRING )
            {
            // InternalCQL.g:3016:4: (lv_filename_4_0= RULE_STRING )
            // InternalCQL.g:3017:5: lv_filename_4_0= RULE_STRING
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

            otherlv_5=(Token)match(input,26,FOLLOW_8); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamFileAccess().getASKeyword_5());
            		
            // InternalCQL.g:3037:3: ( (lv_type_6_0= RULE_ID ) )
            // InternalCQL.g:3038:4: (lv_type_6_0= RULE_ID )
            {
            // InternalCQL.g:3038:4: (lv_type_6_0= RULE_ID )
            // InternalCQL.g:3039:5: lv_type_6_0= RULE_ID
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
    // InternalCQL.g:3059:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQL.g:3059:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQL.g:3060:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQL.g:3066:1: ruleCreateView returns [EObject current=null] : (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3072:2: ( (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) ) )
            // InternalCQL.g:3073:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) )
            {
            // InternalCQL.g:3073:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) )
            // InternalCQL.g:3074:3: otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) )
            {
            otherlv_0=(Token)match(input,51,FOLLOW_8); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateViewAccess().getVIEWKeyword_0());
            		
            // InternalCQL.g:3078:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:3079:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:3079:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:3080:5: lv_name_1_0= RULE_ID
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

            otherlv_2=(Token)match(input,17,FOLLOW_11); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateViewAccess().getFROMKeyword_2());
            		
            // InternalCQL.g:3100:3: ( (lv_select_3_0= ruleNestedStatement ) )
            // InternalCQL.g:3101:4: (lv_select_3_0= ruleNestedStatement )
            {
            // InternalCQL.g:3101:4: (lv_select_3_0= ruleNestedStatement )
            // InternalCQL.g:3102:5: lv_select_3_0= ruleNestedStatement
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
    // InternalCQL.g:3123:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQL.g:3123:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQL.g:3124:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQL.g:3130:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3136:2: ( (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQL.g:3137:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQL.g:3137:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:3138:3: otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,46,FOLLOW_51); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,52,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQL.g:3146:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQL.g:3147:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQL.g:3147:4: (lv_name_2_0= RULE_ID )
            // InternalCQL.g:3148:5: lv_name_2_0= RULE_ID
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

            // InternalCQL.g:3164:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==13) ) {
                alt50=1;
            }
            else if ( (LA50_0==RULE_ID) ) {
                alt50=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }
            switch (alt50) {
                case 1 :
                    // InternalCQL.g:3165:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQL.g:3165:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQL.g:3166:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQL.g:3166:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQL.g:3167:6: lv_statement_3_0= ruleSelect
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
                    // InternalCQL.g:3185:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQL.g:3185:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQL.g:3186:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQL.g:3186:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQL.g:3187:6: lv_inputname_4_0= RULE_ID
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
    // InternalCQL.g:3208:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQL.g:3208:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQL.g:3209:2: iv_ruleDrop= ruleDrop EOF
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
    // InternalCQL.g:3215:1: ruleDrop returns [EObject current=null] : ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) ) ;
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
            // InternalCQL.g:3221:2: ( ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:3222:2: ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            {
            // InternalCQL.g:3222:2: ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            // InternalCQL.g:3223:3: ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) )
            {
            // InternalCQL.g:3223:3: ( (lv_keyword1_0_0= 'DROP' ) )
            // InternalCQL.g:3224:4: (lv_keyword1_0_0= 'DROP' )
            {
            // InternalCQL.g:3224:4: (lv_keyword1_0_0= 'DROP' )
            // InternalCQL.g:3225:5: lv_keyword1_0_0= 'DROP'
            {
            lv_keyword1_0_0=(Token)match(input,53,FOLLOW_53); 

            					newLeafNode(lv_keyword1_0_0, grammarAccess.getDropAccess().getKeyword1DROPKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(current, "keyword1", lv_keyword1_0_0, "DROP");
            				

            }


            }

            // InternalCQL.g:3237:3: ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) )
            // InternalCQL.g:3238:4: ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) )
            {
            // InternalCQL.g:3238:4: ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) )
            // InternalCQL.g:3239:5: (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' )
            {
            // InternalCQL.g:3239:5: (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' )
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==47) ) {
                alt51=1;
            }
            else if ( (LA51_0==46) ) {
                alt51=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                throw nvae;
            }
            switch (alt51) {
                case 1 :
                    // InternalCQL.g:3240:6: lv_keyword2_1_1= 'SINK'
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
                    // InternalCQL.g:3251:6: lv_keyword2_1_2= 'STREAM'
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

            // InternalCQL.g:3264:3: ( (lv_value1_2_0= RULE_ID ) )
            // InternalCQL.g:3265:4: (lv_value1_2_0= RULE_ID )
            {
            // InternalCQL.g:3265:4: (lv_value1_2_0= RULE_ID )
            // InternalCQL.g:3266:5: lv_value1_2_0= RULE_ID
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

            // InternalCQL.g:3282:3: ( (lv_keyword3_3_0= 'IF EXISTS' ) )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==54) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // InternalCQL.g:3283:4: (lv_keyword3_3_0= 'IF EXISTS' )
                    {
                    // InternalCQL.g:3283:4: (lv_keyword3_3_0= 'IF EXISTS' )
                    // InternalCQL.g:3284:5: lv_keyword3_3_0= 'IF EXISTS'
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

            // InternalCQL.g:3296:3: ( (lv_value2_4_0= RULE_ID ) )
            // InternalCQL.g:3297:4: (lv_value2_4_0= RULE_ID )
            {
            // InternalCQL.g:3297:4: (lv_value2_4_0= RULE_ID )
            // InternalCQL.g:3298:5: lv_value2_4_0= RULE_ID
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
    // InternalCQL.g:3318:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQL.g:3318:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQL.g:3319:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQL.g:3325:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'UNBOUNDED' ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQL.g:3331:2: (kw= 'UNBOUNDED' )
            // InternalCQL.g:3332:2: kw= 'UNBOUNDED'
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
    // InternalCQL.g:3340:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQL.g:3340:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQL.g:3341:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQL.g:3347:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) ;
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
            // InternalCQL.g:3353:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) )
            // InternalCQL.g:3354:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            {
            // InternalCQL.g:3354:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            // InternalCQL.g:3355:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME'
            {
            otherlv_0=(Token)match(input,56,FOLLOW_49); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:3359:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:3360:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:3360:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:3361:5: lv_size_1_0= RULE_INT
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

            // InternalCQL.g:3377:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQL.g:3378:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQL.g:3378:4: (lv_unit_2_0= RULE_ID )
            // InternalCQL.g:3379:5: lv_unit_2_0= RULE_ID
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

            // InternalCQL.g:3395:3: (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==57) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // InternalCQL.g:3396:4: otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,57,FOLLOW_49); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQL.g:3400:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQL.g:3401:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQL.g:3401:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQL.g:3402:6: lv_advance_size_4_0= RULE_INT
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

                    // InternalCQL.g:3418:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQL.g:3419:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQL.g:3419:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQL.g:3420:6: lv_advance_unit_5_0= RULE_ID
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
    // InternalCQL.g:3445:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQL.g:3445:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQL.g:3446:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQL.g:3452:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQL.g:3458:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQL.g:3459:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQL.g:3459:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQL.g:3460:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,56,FOLLOW_49); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:3464:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:3465:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:3465:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:3466:5: lv_size_1_0= RULE_INT
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

            // InternalCQL.g:3482:3: (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==57) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // InternalCQL.g:3483:4: otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,57,FOLLOW_49); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQL.g:3487:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQL.g:3488:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQL.g:3488:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQL.g:3489:6: lv_advance_size_3_0= RULE_INT
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
            		
            // InternalCQL.g:3510:3: (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==60) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // InternalCQL.g:3511:4: otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,60,FOLLOW_16); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,20,FOLLOW_8); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQL.g:3519:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQL.g:3520:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQL.g:3520:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQL.g:3521:6: lv_partition_attribute_7_0= ruleAttribute
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
    // InternalCQL.g:3543:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQL.g:3543:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQL.g:3544:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQL.g:3550:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3556:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQL.g:3557:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQL.g:3557:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQL.g:3558:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQL.g:3558:3: ()
            // InternalCQL.g:3559:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQL.g:3565:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQL.g:3566:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQL.g:3566:4: (lv_elements_1_0= ruleExpression )
            // InternalCQL.g:3567:5: lv_elements_1_0= ruleExpression
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
    // InternalCQL.g:3588:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQL.g:3588:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQL.g:3589:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQL.g:3595:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3601:2: (this_Or_0= ruleOr )
            // InternalCQL.g:3602:2: this_Or_0= ruleOr
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
    // InternalCQL.g:3613:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQL.g:3613:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQL.g:3614:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQL.g:3620:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3626:2: ( (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQL.g:3627:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQL.g:3627:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQL.g:3628:3: this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_60);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3636:3: ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            loop56:
            do {
                int alt56=2;
                int LA56_0 = input.LA(1);

                if ( (LA56_0==61) ) {
                    alt56=1;
                }


                switch (alt56) {
            	case 1 :
            	    // InternalCQL.g:3637:4: () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQL.g:3637:4: ()
            	    // InternalCQL.g:3638:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,61,FOLLOW_14); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQL.g:3648:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQL.g:3649:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQL.g:3649:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQL.g:3650:6: lv_right_3_0= ruleAnd
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
    // $ANTLR end "ruleOr"


    // $ANTLR start "entryRuleAnd"
    // InternalCQL.g:3672:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQL.g:3672:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQL.g:3673:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQL.g:3679:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3685:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQL.g:3686:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQL.g:3686:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQL.g:3687:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_61);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3695:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==62) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // InternalCQL.g:3696:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQL.g:3696:4: ()
            	    // InternalCQL.g:3697:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,62,FOLLOW_14); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQL.g:3707:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQL.g:3708:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQL.g:3708:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQL.g:3709:6: lv_right_3_0= ruleEqualitiy
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
    // $ANTLR end "ruleAnd"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQL.g:3731:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQL.g:3731:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQL.g:3732:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQL.g:3738:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3744:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQL.g:3745:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQL.g:3745:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQL.g:3746:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_62);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3754:3: ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( ((LA59_0>=63 && LA59_0<=64)) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // InternalCQL.g:3755:4: () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQL.g:3755:4: ()
            	    // InternalCQL.g:3756:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:3762:4: ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) )
            	    // InternalCQL.g:3763:5: ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) )
            	    {
            	    // InternalCQL.g:3763:5: ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) )
            	    // InternalCQL.g:3764:6: (lv_op_2_1= '=' | lv_op_2_2= '!=' )
            	    {
            	    // InternalCQL.g:3764:6: (lv_op_2_1= '=' | lv_op_2_2= '!=' )
            	    int alt58=2;
            	    int LA58_0 = input.LA(1);

            	    if ( (LA58_0==63) ) {
            	        alt58=1;
            	    }
            	    else if ( (LA58_0==64) ) {
            	        alt58=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 58, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt58) {
            	        case 1 :
            	            // InternalCQL.g:3765:7: lv_op_2_1= '='
            	            {
            	            lv_op_2_1=(Token)match(input,63,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:3776:7: lv_op_2_2= '!='
            	            {
            	            lv_op_2_2=(Token)match(input,64,FOLLOW_14); 

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

            	    // InternalCQL.g:3789:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQL.g:3790:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQL.g:3790:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQL.g:3791:6: lv_right_3_0= ruleComparison
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
    // $ANTLR end "ruleEqualitiy"


    // $ANTLR start "entryRuleComparison"
    // InternalCQL.g:3813:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQL.g:3813:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQL.g:3814:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQL.g:3820:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQL.g:3826:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQL.g:3827:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQL.g:3827:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQL.g:3828:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_63);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3836:3: ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( ((LA61_0>=65 && LA61_0<=68)) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // InternalCQL.g:3837:4: () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQL.g:3837:4: ()
            	    // InternalCQL.g:3838:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:3844:4: ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) )
            	    // InternalCQL.g:3845:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    {
            	    // InternalCQL.g:3845:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    // InternalCQL.g:3846:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    {
            	    // InternalCQL.g:3846:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    int alt60=4;
            	    switch ( input.LA(1) ) {
            	    case 65:
            	        {
            	        alt60=1;
            	        }
            	        break;
            	    case 66:
            	        {
            	        alt60=2;
            	        }
            	        break;
            	    case 67:
            	        {
            	        alt60=3;
            	        }
            	        break;
            	    case 68:
            	        {
            	        alt60=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 60, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt60) {
            	        case 1 :
            	            // InternalCQL.g:3847:7: lv_op_2_1= '>='
            	            {
            	            lv_op_2_1=(Token)match(input,65,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:3858:7: lv_op_2_2= '<='
            	            {
            	            lv_op_2_2=(Token)match(input,66,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQL.g:3869:7: lv_op_2_3= '<'
            	            {
            	            lv_op_2_3=(Token)match(input,67,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQL.g:3880:7: lv_op_2_4= '>'
            	            {
            	            lv_op_2_4=(Token)match(input,68,FOLLOW_14); 

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

            	    // InternalCQL.g:3893:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQL.g:3894:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQL.g:3894:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQL.g:3895:6: lv_right_3_0= rulePlusOrMinus
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
    // $ANTLR end "ruleComparison"


    // $ANTLR start "entryRulePlusOrMinus"
    // InternalCQL.g:3917:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQL.g:3917:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQL.g:3918:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQL.g:3924:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3930:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQL.g:3931:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQL.g:3931:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQL.g:3932:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_64);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3940:3: ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop63:
            do {
                int alt63=2;
                int LA63_0 = input.LA(1);

                if ( ((LA63_0>=38 && LA63_0<=39)) ) {
                    alt63=1;
                }


                switch (alt63) {
            	case 1 :
            	    // InternalCQL.g:3941:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQL.g:3941:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) )
            	    int alt62=2;
            	    int LA62_0 = input.LA(1);

            	    if ( (LA62_0==38) ) {
            	        alt62=1;
            	    }
            	    else if ( (LA62_0==39) ) {
            	        alt62=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 62, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt62) {
            	        case 1 :
            	            // InternalCQL.g:3942:5: ( () otherlv_2= '+' )
            	            {
            	            // InternalCQL.g:3942:5: ( () otherlv_2= '+' )
            	            // InternalCQL.g:3943:6: () otherlv_2= '+'
            	            {
            	            // InternalCQL.g:3943:6: ()
            	            // InternalCQL.g:3944:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,38,FOLLOW_14); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:3956:5: ( () otherlv_4= '-' )
            	            {
            	            // InternalCQL.g:3956:5: ( () otherlv_4= '-' )
            	            // InternalCQL.g:3957:6: () otherlv_4= '-'
            	            {
            	            // InternalCQL.g:3957:6: ()
            	            // InternalCQL.g:3958:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,39,FOLLOW_14); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQL.g:3970:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQL.g:3971:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQL.g:3971:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQL.g:3972:6: lv_right_5_0= ruleMulOrDiv
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
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCQL.g:3994:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQL.g:3994:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQL.g:3995:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQL.g:4001:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:4007:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQL.g:4008:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQL.g:4008:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQL.g:4009:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_65);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:4017:3: ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop65:
            do {
                int alt65=2;
                int LA65_0 = input.LA(1);

                if ( (LA65_0==15||LA65_0==40) ) {
                    alt65=1;
                }


                switch (alt65) {
            	case 1 :
            	    // InternalCQL.g:4018:4: () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQL.g:4018:4: ()
            	    // InternalCQL.g:4019:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:4025:4: ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) )
            	    // InternalCQL.g:4026:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    {
            	    // InternalCQL.g:4026:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    // InternalCQL.g:4027:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    {
            	    // InternalCQL.g:4027:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    int alt64=2;
            	    int LA64_0 = input.LA(1);

            	    if ( (LA64_0==15) ) {
            	        alt64=1;
            	    }
            	    else if ( (LA64_0==40) ) {
            	        alt64=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 64, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt64) {
            	        case 1 :
            	            // InternalCQL.g:4028:7: lv_op_2_1= '*'
            	            {
            	            lv_op_2_1=(Token)match(input,15,FOLLOW_14); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:4039:7: lv_op_2_2= '/'
            	            {
            	            lv_op_2_2=(Token)match(input,40,FOLLOW_14); 

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

            	    // InternalCQL.g:4052:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQL.g:4053:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQL.g:4053:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQL.g:4054:6: lv_right_3_0= rulePrimary
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
            	    break loop65;
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
    // InternalCQL.g:4076:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQL.g:4076:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQL.g:4077:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQL.g:4083:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQL.g:4089:2: ( ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQL.g:4090:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQL.g:4090:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt66=3;
            switch ( input.LA(1) ) {
            case 22:
                {
                alt66=1;
                }
                break;
            case 69:
                {
                alt66=2;
                }
                break;
            case RULE_ID:
            case RULE_STRING:
            case RULE_INT:
            case RULE_FLOAT:
            case 70:
            case 71:
                {
                alt66=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 66, 0, input);

                throw nvae;
            }

            switch (alt66) {
                case 1 :
                    // InternalCQL.g:4091:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    {
                    // InternalCQL.g:4091:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    // InternalCQL.g:4092:4: () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')'
                    {
                    // InternalCQL.g:4092:4: ()
                    // InternalCQL.g:4093:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,22,FOLLOW_14); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQL.g:4103:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQL.g:4104:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQL.g:4104:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQL.g:4105:6: lv_inner_2_0= ruleExpression
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

                    otherlv_3=(Token)match(input,23,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:4128:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQL.g:4128:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQL.g:4129:4: () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQL.g:4129:4: ()
                    // InternalCQL.g:4130:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,69,FOLLOW_14); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQL.g:4140:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQL.g:4141:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQL.g:4141:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQL.g:4142:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQL.g:4161:3: this_Atomic_7= ruleAtomic
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
    // InternalCQL.g:4173:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQL.g:4173:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQL.g:4174:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQL.g:4180:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
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
            // InternalCQL.g:4186:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQL.g:4187:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQL.g:4187:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt69=5;
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
            case 70:
            case 71:
                {
                alt69=4;
                }
                break;
            case RULE_ID:
                {
                alt69=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 69, 0, input);

                throw nvae;
            }

            switch (alt69) {
                case 1 :
                    // InternalCQL.g:4188:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:4188:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:4189:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:4189:4: ()
                    // InternalCQL.g:4190:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4196:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:4197:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:4197:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:4198:6: lv_value_1_0= RULE_INT
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
                    // InternalCQL.g:4216:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQL.g:4216:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQL.g:4217:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQL.g:4217:4: ()
                    // InternalCQL.g:4218:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4224:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQL.g:4225:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQL.g:4225:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQL.g:4226:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQL.g:4244:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:4244:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:4245:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:4245:4: ()
                    // InternalCQL.g:4246:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4252:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:4253:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:4253:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:4254:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQL.g:4272:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:4272:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:4273:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:4273:4: ()
                    // InternalCQL.g:4274:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4280:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:4281:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:4281:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:4282:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:4282:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt67=2;
                    int LA67_0 = input.LA(1);

                    if ( (LA67_0==70) ) {
                        alt67=1;
                    }
                    else if ( (LA67_0==71) ) {
                        alt67=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 67, 0, input);

                        throw nvae;
                    }
                    switch (alt67) {
                        case 1 :
                            // InternalCQL.g:4283:7: lv_value_7_1= 'TRUE'
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
                            // InternalCQL.g:4294:7: lv_value_7_2= 'FALSE'
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
                    // InternalCQL.g:4309:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQL.g:4309:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQL.g:4310:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQL.g:4310:4: ()
                    // InternalCQL.g:4311:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4317:4: ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt68=2;
                    int LA68_0 = input.LA(1);

                    if ( (LA68_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
                        case 27:
                            {
                            int LA68_2 = input.LA(3);

                            if ( (LA68_2==RULE_ID) ) {
                                int LA68_5 = input.LA(4);

                                if ( (LA68_5==28) ) {
                                    alt68=2;
                                }
                                else if ( (LA68_5==EOF||LA68_5==12||LA68_5==15||LA68_5==19||LA68_5==21||LA68_5==23||(LA68_5>=38 && LA68_5<=40)||(LA68_5>=61 && LA68_5<=68)) ) {
                                    alt68=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 68, 5, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 68, 2, input);

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
                            alt68=1;
                            }
                            break;
                        case 28:
                            {
                            alt68=2;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 68, 1, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 68, 0, input);

                        throw nvae;
                    }
                    switch (alt68) {
                        case 1 :
                            // InternalCQL.g:4318:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            {
                            // InternalCQL.g:4318:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            // InternalCQL.g:4319:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            {
                            // InternalCQL.g:4319:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            // InternalCQL.g:4320:7: lv_value_9_0= ruleAttributeWithoutAliasDefinition
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
                            // InternalCQL.g:4338:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQL.g:4338:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQL.g:4339:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQL.g:4339:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQL.g:4340:7: lv_value_10_0= ruleAttributeWithNestedStatement
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
    // InternalCQL.g:4363:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQL.g:4363:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQL.g:4364:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
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
    // InternalCQL.g:4370:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) ) ;
    public final EObject ruleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;


        	enterRule();

        try {
            // InternalCQL.g:4376:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) ) )
            // InternalCQL.g:4377:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) )
            {
            // InternalCQL.g:4377:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) )
            int alt71=4;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt71=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt71=2;
                }
                break;
            case RULE_STRING:
                {
                alt71=3;
                }
                break;
            case 70:
            case 71:
                {
                alt71=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }

            switch (alt71) {
                case 1 :
                    // InternalCQL.g:4378:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:4378:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:4379:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:4379:4: ()
                    // InternalCQL.g:4380:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4386:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:4387:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:4387:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:4388:6: lv_value_1_0= RULE_INT
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
                    // InternalCQL.g:4406:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQL.g:4406:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQL.g:4407:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQL.g:4407:4: ()
                    // InternalCQL.g:4408:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4414:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQL.g:4415:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQL.g:4415:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQL.g:4416:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQL.g:4434:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:4434:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:4435:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:4435:4: ()
                    // InternalCQL.g:4436:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4442:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:4443:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:4443:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:4444:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQL.g:4462:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:4462:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:4463:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:4463:4: ()
                    // InternalCQL.g:4464:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4470:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:4471:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:4471:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:4472:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:4472:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt70=2;
                    int LA70_0 = input.LA(1);

                    if ( (LA70_0==70) ) {
                        alt70=1;
                    }
                    else if ( (LA70_0==71) ) {
                        alt70=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 70, 0, input);

                        throw nvae;
                    }
                    switch (alt70) {
                        case 1 :
                            // InternalCQL.g:4473:7: lv_value_7_1= 'TRUE'
                            {
                            lv_value_7_1=(Token)match(input,70,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicWithoutAttributeRefAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicWithoutAttributeRefRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQL.g:4484:7: lv_value_7_2= 'FALSE'
                            {
                            lv_value_7_2=(Token)match(input,71,FOLLOW_2); 

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


    // $ANTLR start "entryRuleAtomicWithOnlyStringConstant"
    // InternalCQL.g:4502:1: entryRuleAtomicWithOnlyStringConstant returns [EObject current=null] : iv_ruleAtomicWithOnlyStringConstant= ruleAtomicWithOnlyStringConstant EOF ;
    public final EObject entryRuleAtomicWithOnlyStringConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithOnlyStringConstant = null;


        try {
            // InternalCQL.g:4502:69: (iv_ruleAtomicWithOnlyStringConstant= ruleAtomicWithOnlyStringConstant EOF )
            // InternalCQL.g:4503:2: iv_ruleAtomicWithOnlyStringConstant= ruleAtomicWithOnlyStringConstant EOF
            {
             newCompositeNode(grammarAccess.getAtomicWithOnlyStringConstantRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAtomicWithOnlyStringConstant=ruleAtomicWithOnlyStringConstant();

            state._fsp--;

             current =iv_ruleAtomicWithOnlyStringConstant; 
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
    // $ANTLR end "entryRuleAtomicWithOnlyStringConstant"


    // $ANTLR start "ruleAtomicWithOnlyStringConstant"
    // InternalCQL.g:4509:1: ruleAtomicWithOnlyStringConstant returns [EObject current=null] : ( () ( (lv_value_1_0= RULE_STRING ) ) ) ;
    public final EObject ruleAtomicWithOnlyStringConstant() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;


        	enterRule();

        try {
            // InternalCQL.g:4515:2: ( ( () ( (lv_value_1_0= RULE_STRING ) ) ) )
            // InternalCQL.g:4516:2: ( () ( (lv_value_1_0= RULE_STRING ) ) )
            {
            // InternalCQL.g:4516:2: ( () ( (lv_value_1_0= RULE_STRING ) ) )
            // InternalCQL.g:4517:3: () ( (lv_value_1_0= RULE_STRING ) )
            {
            // InternalCQL.g:4517:3: ()
            // InternalCQL.g:4518:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getAtomicWithOnlyStringConstantAccess().getStringConstantAction_0(),
            					current);
            			

            }

            // InternalCQL.g:4524:3: ( (lv_value_1_0= RULE_STRING ) )
            // InternalCQL.g:4525:4: (lv_value_1_0= RULE_STRING )
            {
            // InternalCQL.g:4525:4: (lv_value_1_0= RULE_STRING )
            // InternalCQL.g:4526:5: lv_value_1_0= RULE_STRING
            {
            lv_value_1_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            					newLeafNode(lv_value_1_0, grammarAccess.getAtomicWithOnlyStringConstantAccess().getValueSTRINGTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAtomicWithOnlyStringConstantRule());
            					}
            					setWithLastConsumed(
            						current,
            						"value",
            						lv_value_1_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

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
    // $ANTLR end "ruleAtomicWithOnlyStringConstant"


    // $ANTLR start "entryRuleDataType"
    // InternalCQL.g:4546:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQL.g:4546:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQL.g:4547:2: iv_ruleDataType= ruleDataType EOF
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
    // InternalCQL.g:4553:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) ) ;
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
            // InternalCQL.g:4559:2: ( ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) ) )
            // InternalCQL.g:4560:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) )
            {
            // InternalCQL.g:4560:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) )
            // InternalCQL.g:4561:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) )
            {
            // InternalCQL.g:4561:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) )
            // InternalCQL.g:4562:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' )
            {
            // InternalCQL.g:4562:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' )
            int alt72=8;
            switch ( input.LA(1) ) {
            case 72:
                {
                alt72=1;
                }
                break;
            case 73:
                {
                alt72=2;
                }
                break;
            case 74:
                {
                alt72=3;
                }
                break;
            case 75:
                {
                alt72=4;
                }
                break;
            case 76:
                {
                alt72=5;
                }
                break;
            case 77:
                {
                alt72=6;
                }
                break;
            case 78:
                {
                alt72=7;
                }
                break;
            case 79:
                {
                alt72=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 72, 0, input);

                throw nvae;
            }

            switch (alt72) {
                case 1 :
                    // InternalCQL.g:4563:5: lv_value_0_1= 'INTEGER'
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
                    // InternalCQL.g:4574:5: lv_value_0_2= 'DOUBLE'
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
                    // InternalCQL.g:4585:5: lv_value_0_3= 'LONG'
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
                    // InternalCQL.g:4596:5: lv_value_0_4= 'FLOAT'
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
                    // InternalCQL.g:4607:5: lv_value_0_5= 'STRING'
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
                    // InternalCQL.g:4618:5: lv_value_0_6= 'BOOLEAN'
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
                    // InternalCQL.g:4629:5: lv_value_0_7= 'STARTTIMESTAMP'
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
                    // InternalCQL.g:4640:5: lv_value_0_8= 'ENDTIMESTAMP'
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
    // InternalCQL.g:4656:1: ruleCreateKeyword returns [Enumerator current=null] : ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) ) ;
    public final Enumerator ruleCreateKeyword() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCQL.g:4662:2: ( ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) ) )
            // InternalCQL.g:4663:2: ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) )
            {
            // InternalCQL.g:4663:2: ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) )
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==80) ) {
                alt73=1;
            }
            else if ( (LA73_0==81) ) {
                alt73=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;
            }
            switch (alt73) {
                case 1 :
                    // InternalCQL.g:4664:3: (enumLiteral_0= 'CREATE' )
                    {
                    // InternalCQL.g:4664:3: (enumLiteral_0= 'CREATE' )
                    // InternalCQL.g:4665:4: enumLiteral_0= 'CREATE'
                    {
                    enumLiteral_0=(Token)match(input,80,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:4672:3: (enumLiteral_1= 'ATTACH' )
                    {
                    // InternalCQL.g:4672:3: (enumLiteral_1= 'ATTACH' )
                    // InternalCQL.g:4673:4: enumLiteral_1= 'ATTACH'
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
    protected DFA26 dfa26 = new DFA26(this);
    protected DFA31 dfa31 = new DFA31(this);
    protected DFA34 dfa34 = new DFA34(this);
    protected DFA38 dfa38 = new DFA38(this);
    static final String dfa_1s = "\52\uffff";
    static final String dfa_2s = "\1\15\3\uffff\2\56\1\uffff\1\4\1\uffff\1\26\4\4\10\20\3\4\1\51\1\32\3\uffff\2\4\10\20\1\32\1\110";
    static final String dfa_3s = "\1\121\3\uffff\2\57\1\uffff\1\4\1\uffff\1\26\1\4\1\117\2\4\12\117\1\4\1\62\1\117\3\uffff\2\4\10\27\2\117";
    static final String dfa_4s = "\1\uffff\1\1\1\2\1\3\2\uffff\1\10\1\uffff\1\5\22\uffff\1\6\1\7\1\4\14\uffff";
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
            "\1\13\25\uffff\1\15\1\14\54\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\26",
            "\1\27",
            "\1\30\6\uffff\1\31\60\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\30\6\uffff\1\31\60\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\30\6\uffff\1\31\60\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\30\6\uffff\1\31\60\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\30\6\uffff\1\31\60\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\30\6\uffff\1\31\60\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\30\6\uffff\1\31\60\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\30\6\uffff\1\31\60\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\13\25\uffff\1\15\55\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\13\103\uffff\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25",
            "\1\32",
            "\1\35\6\uffff\1\33\1\uffff\1\34",
            "\1\37\1\36\54\uffff\1\40\1\41\1\42\1\43\1\44\1\45\1\46\1\47",
            "",
            "",
            "",
            "\1\50",
            "\1\51",
            "\1\30\6\uffff\1\31",
            "\1\30\6\uffff\1\31",
            "\1\30\6\uffff\1\31",
            "\1\30\6\uffff\1\31",
            "\1\30\6\uffff\1\31",
            "\1\30\6\uffff\1\31",
            "\1\30\6\uffff\1\31",
            "\1\30\6\uffff\1\31",
            "\1\37\55\uffff\1\40\1\41\1\42\1\43\1\44\1\45\1\46\1\47",
            "\1\40\1\41\1\42\1\43\1\44\1\45\1\46\1\47"
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
    static final String dfa_7s = "\13\uffff";
    static final String dfa_8s = "\2\uffff\6\10\2\uffff\1\10";
    static final String dfa_9s = "\1\4\1\uffff\6\4\1\uffff\2\4";
    static final String dfa_10s = "\1\107\1\uffff\6\107\1\uffff\1\4\1\107";
    static final String dfa_11s = "\1\uffff\1\1\6\uffff\1\2\2\uffff";
    static final String dfa_12s = "\13\uffff}>";
    static final String[] dfa_13s = {
            "\1\2\1\5\1\3\1\4\35\uffff\1\1\40\uffff\1\6\1\7",
            "",
            "\4\10\7\uffff\1\1\2\10\10\uffff\1\10\1\11\1\uffff\11\10\3\1\35\uffff\2\10",
            "\4\10\7\uffff\1\1\2\10\10\uffff\1\10\2\uffff\11\10\3\1\35\uffff\2\10",
            "\4\10\7\uffff\1\1\2\10\10\uffff\1\10\2\uffff\11\10\3\1\35\uffff\2\10",
            "\4\10\7\uffff\1\1\2\10\10\uffff\1\10\2\uffff\11\10\3\1\35\uffff\2\10",
            "\4\10\7\uffff\1\1\2\10\10\uffff\1\10\2\uffff\11\10\3\1\35\uffff\2\10",
            "\4\10\7\uffff\1\1\2\10\10\uffff\1\10\2\uffff\11\10\3\1\35\uffff\2\10",
            "",
            "\1\12",
            "\4\10\7\uffff\1\1\2\10\10\uffff\1\10\2\uffff\11\10\3\1\35\uffff\2\10"
    };

    static final short[] dfa_7 = DFA.unpackEncodedString(dfa_7s);
    static final short[] dfa_8 = DFA.unpackEncodedString(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final char[] dfa_10 = DFA.unpackEncodedStringToUnsignedChars(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[] dfa_12 = DFA.unpackEncodedString(dfa_12s);
    static final short[][] dfa_13 = unpackEncodedStringArray(dfa_13s);

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = dfa_7;
            this.eof = dfa_8;
            this.min = dfa_9;
            this.max = dfa_10;
            this.accept = dfa_11;
            this.special = dfa_12;
            this.transition = dfa_13;
        }
        public String getDescription() {
            return "1479:3: ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )?";
        }
    }
    static final String dfa_14s = "\1\10\1\uffff\6\10\2\uffff\1\10";
    static final short[] dfa_14 = DFA.unpackEncodedString(dfa_14s);

    class DFA31 extends DFA {

        public DFA31(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 31;
            this.eot = dfa_7;
            this.eof = dfa_14;
            this.min = dfa_9;
            this.max = dfa_10;
            this.accept = dfa_11;
            this.special = dfa_12;
            this.transition = dfa_13;
        }
        public String getDescription() {
            return "1693:3: ( (lv_innerexpression_4_0= ruleSelectExpressionWithoutAliasDefinition ) )?";
        }
    }
    static final String dfa_15s = "\1\uffff\6\11\3\uffff\1\11";
    static final String dfa_16s = "\1\4\6\17\1\uffff\1\4\1\uffff\1\17";
    static final String dfa_17s = "\1\107\6\50\1\uffff\1\4\1\uffff\1\50";
    static final String dfa_18s = "\7\uffff\1\1\1\uffff\1\2\1\uffff";
    static final String[] dfa_19s = {
            "\1\1\1\4\1\2\1\3\76\uffff\1\5\1\6",
            "\1\7\7\uffff\1\11\3\uffff\1\10\12\uffff\3\7",
            "\1\7\7\uffff\1\11\16\uffff\3\7",
            "\1\7\7\uffff\1\11\16\uffff\3\7",
            "\1\7\7\uffff\1\11\16\uffff\3\7",
            "\1\7\7\uffff\1\11\16\uffff\3\7",
            "\1\7\7\uffff\1\11\16\uffff\3\7",
            "",
            "\1\12",
            "",
            "\1\7\7\uffff\1\11\16\uffff\3\7"
    };
    static final short[] dfa_15 = DFA.unpackEncodedString(dfa_15s);
    static final char[] dfa_16 = DFA.unpackEncodedStringToUnsignedChars(dfa_16s);
    static final char[] dfa_17 = DFA.unpackEncodedStringToUnsignedChars(dfa_17s);
    static final short[] dfa_18 = DFA.unpackEncodedString(dfa_18s);
    static final short[][] dfa_19 = unpackEncodedStringArray(dfa_19s);

    class DFA34 extends DFA {

        public DFA34(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 34;
            this.eot = dfa_7;
            this.eof = dfa_15;
            this.min = dfa_16;
            this.max = dfa_17;
            this.accept = dfa_18;
            this.special = dfa_12;
            this.transition = dfa_19;
        }
        public String getDescription() {
            return "1821:3: ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )?";
        }
    }
    static final String dfa_20s = "\7\7\3\uffff\1\7";
    static final String dfa_21s = "\7\uffff\1\2\1\uffff\1\1\1\uffff";
    static final String[] dfa_22s = {
            "\1\1\1\4\1\2\1\3\76\uffff\1\5\1\6",
            "\1\11\7\uffff\1\7\3\uffff\1\10\12\uffff\3\11",
            "\1\11\7\uffff\1\7\16\uffff\3\11",
            "\1\11\7\uffff\1\7\16\uffff\3\11",
            "\1\11\7\uffff\1\7\16\uffff\3\11",
            "\1\11\7\uffff\1\7\16\uffff\3\11",
            "\1\11\7\uffff\1\7\16\uffff\3\11",
            "",
            "\1\12",
            "",
            "\1\11\7\uffff\1\7\16\uffff\3\11"
    };
    static final short[] dfa_20 = DFA.unpackEncodedString(dfa_20s);
    static final short[] dfa_21 = DFA.unpackEncodedString(dfa_21s);
    static final short[][] dfa_22 = unpackEncodedStringArray(dfa_22s);

    class DFA38 extends DFA {

        public DFA38(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 38;
            this.eot = dfa_7;
            this.eof = dfa_20;
            this.min = dfa_16;
            this.max = dfa_17;
            this.accept = dfa_21;
            this.special = dfa_12;
            this.transition = dfa_22;
        }
        public String getDescription() {
            return "1990:3: ( (lv_innerexpression_3_0= ruleSelectExpressionOnlyWithAttributeAndConstant2 ) )?";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0028400000002002L,0x0000000000030000L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000003FE000C0F0L,0x00000000000000C0L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000003FE003C0F0L,0x00000000000000C0L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000030000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000001FE0000000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000400010L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x00000000006D0012L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x00000000002D0002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x00000000004000F0L,0x00000000000000E0L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000280002L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000210012L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000210002L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000005000002L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0180000000000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x000001C000008000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000003FE000C0F2L,0x00000000000000C0L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x000000C000000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000810020L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000000010L,0x000000000000FF00L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000000810000L,0x000000000000FF00L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000000000000L,0x000000000000FF00L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000000810000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000000000000040L});
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