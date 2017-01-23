package de.uniol.inf.is.odysseus.parser.novel.cql.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_FLOAT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "';'", "'SELECT'", "'DISTINCT'", "'*'", "','", "'FROM'", "'WHERE'", "'GROUP'", "'BY'", "'HAVING'", "'('", "')'", "'['", "']'", "'AS'", "'.'", "'IN'", "'WRAPPER'", "'PROTOCOL'", "'TRANSPORT'", "'DATAHANDLER'", "'OPTIONS'", "'CREATE'", "'STREAM'", "'SINK'", "'CHANNEL'", "':'", "'FILE'", "'VIEW'", "'TO'", "'DROP'", "'IF EXISTS'", "'UNBOUNDED'", "'SIZE'", "'ADVANCE'", "'TIME'", "'TUPLE'", "'PARTITION'", "'OR'", "'AND'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'+'", "'-'", "'/'", "'NOT'", "'TRUE'", "'FALSE'", "'INTEGER'", "'DOUBLE'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'"
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
    public static final int RULE_STRING=5;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
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
    // InternalCQL.g:64:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalCQL.g:64:46: (iv_ruleModel= ruleModel EOF )
            // InternalCQL.g:65:2: iv_ruleModel= ruleModel EOF
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
    // InternalCQL.g:71:1: ruleModel returns [EObject current=null] : ( (lv_statements_0_0= ruleStatement ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_statements_0_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:77:2: ( ( (lv_statements_0_0= ruleStatement ) )* )
            // InternalCQL.g:78:2: ( (lv_statements_0_0= ruleStatement ) )*
            {
            // InternalCQL.g:78:2: ( (lv_statements_0_0= ruleStatement ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==13||(LA1_0>=34 && LA1_0<=35)||LA1_0==40||LA1_0==42) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCQL.g:79:3: (lv_statements_0_0= ruleStatement )
            	    {
            	    // InternalCQL.g:79:3: (lv_statements_0_0= ruleStatement )
            	    // InternalCQL.g:80:4: lv_statements_0_0= ruleStatement
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
    // InternalCQL.g:100:1: entryRuleStatement returns [EObject current=null] : iv_ruleStatement= ruleStatement EOF ;
    public final EObject entryRuleStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStatement = null;


        try {
            // InternalCQL.g:100:50: (iv_ruleStatement= ruleStatement EOF )
            // InternalCQL.g:101:2: iv_ruleStatement= ruleStatement EOF
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
    // InternalCQL.g:107:1: ruleStatement returns [EObject current=null] : ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )? ) ;
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
            // InternalCQL.g:113:2: ( ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )? ) )
            // InternalCQL.g:114:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )? )
            {
            // InternalCQL.g:114:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )? )
            // InternalCQL.g:115:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )?
            {
            // InternalCQL.g:115:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) )
            int alt2=8;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // InternalCQL.g:116:4: ( (lv_type_0_0= ruleSelect ) )
                    {
                    // InternalCQL.g:116:4: ( (lv_type_0_0= ruleSelect ) )
                    // InternalCQL.g:117:5: (lv_type_0_0= ruleSelect )
                    {
                    // InternalCQL.g:117:5: (lv_type_0_0= ruleSelect )
                    // InternalCQL.g:118:6: lv_type_0_0= ruleSelect
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
                    // InternalCQL.g:136:4: ( (lv_type_1_0= ruleStreamTo ) )
                    {
                    // InternalCQL.g:136:4: ( (lv_type_1_0= ruleStreamTo ) )
                    // InternalCQL.g:137:5: (lv_type_1_0= ruleStreamTo )
                    {
                    // InternalCQL.g:137:5: (lv_type_1_0= ruleStreamTo )
                    // InternalCQL.g:138:6: lv_type_1_0= ruleStreamTo
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
                    // InternalCQL.g:156:4: ( (lv_type_2_0= ruleDrop ) )
                    {
                    // InternalCQL.g:156:4: ( (lv_type_2_0= ruleDrop ) )
                    // InternalCQL.g:157:5: (lv_type_2_0= ruleDrop )
                    {
                    // InternalCQL.g:157:5: (lv_type_2_0= ruleDrop )
                    // InternalCQL.g:158:6: lv_type_2_0= ruleDrop
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
                    // InternalCQL.g:176:4: ( (lv_type_3_0= ruleCreateStream1 ) )
                    {
                    // InternalCQL.g:176:4: ( (lv_type_3_0= ruleCreateStream1 ) )
                    // InternalCQL.g:177:5: (lv_type_3_0= ruleCreateStream1 )
                    {
                    // InternalCQL.g:177:5: (lv_type_3_0= ruleCreateStream1 )
                    // InternalCQL.g:178:6: lv_type_3_0= ruleCreateStream1
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
                    // InternalCQL.g:196:4: ( (lv_type_4_0= ruleCreateSink1 ) )
                    {
                    // InternalCQL.g:196:4: ( (lv_type_4_0= ruleCreateSink1 ) )
                    // InternalCQL.g:197:5: (lv_type_4_0= ruleCreateSink1 )
                    {
                    // InternalCQL.g:197:5: (lv_type_4_0= ruleCreateSink1 )
                    // InternalCQL.g:198:6: lv_type_4_0= ruleCreateSink1
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
                    // InternalCQL.g:216:4: ( (lv_type_5_0= ruleCreateStreamChannel ) )
                    {
                    // InternalCQL.g:216:4: ( (lv_type_5_0= ruleCreateStreamChannel ) )
                    // InternalCQL.g:217:5: (lv_type_5_0= ruleCreateStreamChannel )
                    {
                    // InternalCQL.g:217:5: (lv_type_5_0= ruleCreateStreamChannel )
                    // InternalCQL.g:218:6: lv_type_5_0= ruleCreateStreamChannel
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
                    // InternalCQL.g:236:4: ( (lv_type_6_0= ruleCreateStreamFile ) )
                    {
                    // InternalCQL.g:236:4: ( (lv_type_6_0= ruleCreateStreamFile ) )
                    // InternalCQL.g:237:5: (lv_type_6_0= ruleCreateStreamFile )
                    {
                    // InternalCQL.g:237:5: (lv_type_6_0= ruleCreateStreamFile )
                    // InternalCQL.g:238:6: lv_type_6_0= ruleCreateStreamFile
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
                    // InternalCQL.g:256:4: ( (lv_type_7_0= ruleCreateView ) )
                    {
                    // InternalCQL.g:256:4: ( (lv_type_7_0= ruleCreateView ) )
                    // InternalCQL.g:257:5: (lv_type_7_0= ruleCreateView )
                    {
                    // InternalCQL.g:257:5: (lv_type_7_0= ruleCreateView )
                    // InternalCQL.g:258:6: lv_type_7_0= ruleCreateView
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

            // InternalCQL.g:276:3: (otherlv_8= ';' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==12) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalCQL.g:277:4: otherlv_8= ';'
                    {
                    otherlv_8=(Token)match(input,12,FOLLOW_2); 

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
    // InternalCQL.g:286:1: entryRuleSelect returns [EObject current=null] : iv_ruleSelect= ruleSelect EOF ;
    public final EObject entryRuleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect = null;


        try {
            // InternalCQL.g:286:47: (iv_ruleSelect= ruleSelect EOF )
            // InternalCQL.g:287:2: iv_ruleSelect= ruleSelect EOF
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
    // InternalCQL.g:293:1: ruleSelect returns [EObject current=null] : ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? ) ;
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
        Token otherlv_17=null;
        Token otherlv_19=null;
        Token otherlv_21=null;
        EObject lv_attributes_4_0 = null;

        EObject lv_aggregations_5_0 = null;

        EObject lv_attributes_7_0 = null;

        EObject lv_aggregations_9_0 = null;

        EObject lv_sources_11_0 = null;

        EObject lv_sources_13_0 = null;

        EObject lv_predicates_15_0 = null;

        EObject lv_order_18_0 = null;

        EObject lv_order_20_0 = null;

        EObject lv_having_22_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:299:2: ( ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQL.g:300:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQL.g:300:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? )
            // InternalCQL.g:301:3: ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )?
            {
            // InternalCQL.g:301:3: ( (lv_name_0_0= 'SELECT' ) )
            // InternalCQL.g:302:4: (lv_name_0_0= 'SELECT' )
            {
            // InternalCQL.g:302:4: (lv_name_0_0= 'SELECT' )
            // InternalCQL.g:303:5: lv_name_0_0= 'SELECT'
            {
            lv_name_0_0=(Token)match(input,13,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelectAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelectRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCQL.g:315:3: ( (lv_distinct_1_0= 'DISTINCT' ) )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==14) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalCQL.g:316:4: (lv_distinct_1_0= 'DISTINCT' )
                    {
                    // InternalCQL.g:316:4: (lv_distinct_1_0= 'DISTINCT' )
                    // InternalCQL.g:317:5: lv_distinct_1_0= 'DISTINCT'
                    {
                    lv_distinct_1_0=(Token)match(input,14,FOLLOW_6); 

                    					newLeafNode(lv_distinct_1_0, grammarAccess.getSelectAccess().getDistinctDISTINCTKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getSelectRule());
                    					}
                    					setWithLastConsumed(current, "distinct", lv_distinct_1_0, "DISTINCT");
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:329:3: (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==15) ) {
                alt10=1;
            }
            else if ( (LA10_0==RULE_ID||LA10_0==16) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQL.g:330:4: otherlv_2= '*'
                    {
                    otherlv_2=(Token)match(input,15,FOLLOW_7); 

                    				newLeafNode(otherlv_2, grammarAccess.getSelectAccess().getAsteriskKeyword_2_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQL.g:335:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) )
                    {
                    // InternalCQL.g:335:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) )
                    // InternalCQL.g:336:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) )
                    {
                    // InternalCQL.g:336:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) )
                    // InternalCQL.g:337:6: ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?)
                    {
                     
                    					  getUnorderedGroupHelper().enter(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    					
                    // InternalCQL.g:340:6: ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?)
                    // InternalCQL.g:341:7: ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?
                    {
                    // InternalCQL.g:341:7: ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=4;
                        int LA9_0 = input.LA(1);

                        if ( LA9_0 == RULE_ID && getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0) ) {
                            alt9=1;
                        }
                        else if ( LA9_0 == 16 && ( getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) || getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) ) {
                            int LA9_3 = input.LA(2);

                            if ( LA9_3 == RULE_ID && ( getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) || getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) ) {
                                int LA9_4 = input.LA(3);

                                if ( ( LA9_4 == RULE_ID || LA9_4 >= 16 && LA9_4 <= 17 || LA9_4 >= 26 && LA9_4 <= 27 ) && getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) {
                                    alt9=2;
                                }
                                else if ( LA9_4 == 22 && getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) ) {
                                    alt9=3;
                                }


                            }


                        }


                        switch (alt9) {
                    	case 1 :
                    	    // InternalCQL.g:342:5: ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:342:5: ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) )
                    	    // InternalCQL.g:343:6: {...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0)");
                    	    }
                    	    // InternalCQL.g:343:106: ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ )
                    	    // InternalCQL.g:344:7: ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0);
                    	    						
                    	    // InternalCQL.g:347:10: ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+
                    	    int cnt6=0;
                    	    loop6:
                    	    do {
                    	        int alt6=2;
                    	        int LA6_0 = input.LA(1);

                    	        if ( (LA6_0==RULE_ID) ) {
                    	            int LA6_2 = input.LA(2);

                    	            if ( ((true)) ) {
                    	                alt6=1;
                    	            }


                    	        }


                    	        switch (alt6) {
                    	    	case 1 :
                    	    	    // InternalCQL.g:347:11: {...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect", "true");
                    	    	    }
                    	    	    // InternalCQL.g:347:20: ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) )
                    	    	    int alt5=2;
                    	    	    int LA5_0 = input.LA(1);

                    	    	    if ( (LA5_0==RULE_ID) ) {
                    	    	        int LA5_1 = input.LA(2);

                    	    	        if ( (LA5_1==22) ) {
                    	    	            alt5=2;
                    	    	        }
                    	    	        else if ( (LA5_1==RULE_ID||(LA5_1>=16 && LA5_1<=17)||(LA5_1>=26 && LA5_1<=27)) ) {
                    	    	            alt5=1;
                    	    	        }
                    	    	        else {
                    	    	            NoViableAltException nvae =
                    	    	                new NoViableAltException("", 5, 1, input);

                    	    	            throw nvae;
                    	    	        }
                    	    	    }
                    	    	    else {
                    	    	        NoViableAltException nvae =
                    	    	            new NoViableAltException("", 5, 0, input);

                    	    	        throw nvae;
                    	    	    }
                    	    	    switch (alt5) {
                    	    	        case 1 :
                    	    	            // InternalCQL.g:347:21: ( (lv_attributes_4_0= ruleAttribute ) )
                    	    	            {
                    	    	            // InternalCQL.g:347:21: ( (lv_attributes_4_0= ruleAttribute ) )
                    	    	            // InternalCQL.g:348:11: (lv_attributes_4_0= ruleAttribute )
                    	    	            {
                    	    	            // InternalCQL.g:348:11: (lv_attributes_4_0= ruleAttribute )
                    	    	            // InternalCQL.g:349:12: lv_attributes_4_0= ruleAttribute
                    	    	            {

                    	    	            												newCompositeNode(grammarAccess.getSelectAccess().getAttributesAttributeParserRuleCall_2_1_0_0_0());
                    	    	            											
                    	    	            pushFollow(FOLLOW_8);
                    	    	            lv_attributes_4_0=ruleAttribute();

                    	    	            state._fsp--;


                    	    	            												if (current==null) {
                    	    	            													current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    	            												}
                    	    	            												add(
                    	    	            													current,
                    	    	            													"attributes",
                    	    	            													lv_attributes_4_0,
                    	    	            													"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    	            												afterParserOrEnumRuleCall();
                    	    	            											

                    	    	            }


                    	    	            }


                    	    	            }
                    	    	            break;
                    	    	        case 2 :
                    	    	            // InternalCQL.g:367:10: ( (lv_aggregations_5_0= ruleAggregation ) )
                    	    	            {
                    	    	            // InternalCQL.g:367:10: ( (lv_aggregations_5_0= ruleAggregation ) )
                    	    	            // InternalCQL.g:368:11: (lv_aggregations_5_0= ruleAggregation )
                    	    	            {
                    	    	            // InternalCQL.g:368:11: (lv_aggregations_5_0= ruleAggregation )
                    	    	            // InternalCQL.g:369:12: lv_aggregations_5_0= ruleAggregation
                    	    	            {

                    	    	            												newCompositeNode(grammarAccess.getSelectAccess().getAggregationsAggregationParserRuleCall_2_1_0_1_0());
                    	    	            											
                    	    	            pushFollow(FOLLOW_8);
                    	    	            lv_aggregations_5_0=ruleAggregation();

                    	    	            state._fsp--;


                    	    	            												if (current==null) {
                    	    	            													current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    	            												}
                    	    	            												add(
                    	    	            													current,
                    	    	            													"aggregations",
                    	    	            													lv_aggregations_5_0,
                    	    	            													"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Aggregation");
                    	    	            												afterParserOrEnumRuleCall();
                    	    	            											

                    	    	            }


                    	    	            }


                    	    	            }
                    	    	            break;

                    	    	    }


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    if ( cnt6 >= 1 ) break loop6;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(6, input);
                    	                throw eee;
                    	        }
                    	        cnt6++;
                    	    } while (true);

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCQL.g:392:5: ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:392:5: ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) )
                    	    // InternalCQL.g:393:6: {...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1)");
                    	    }
                    	    // InternalCQL.g:393:106: ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ )
                    	    // InternalCQL.g:394:7: ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1);
                    	    						
                    	    // InternalCQL.g:397:10: ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+
                    	    int cnt7=0;
                    	    loop7:
                    	    do {
                    	        int alt7=2;
                    	        int LA7_0 = input.LA(1);

                    	        if ( (LA7_0==16) ) {
                    	            int LA7_2 = input.LA(2);

                    	            if ( ((true)) ) {
                    	                alt7=1;
                    	            }


                    	        }


                    	        switch (alt7) {
                    	    	case 1 :
                    	    	    // InternalCQL.g:397:11: {...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect", "true");
                    	    	    }
                    	    	    // InternalCQL.g:397:20: (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    	    // InternalCQL.g:397:21: otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) )
                    	    	    {
                    	    	    otherlv_6=(Token)match(input,16,FOLLOW_9); 

                    	    	    										newLeafNode(otherlv_6, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_0());
                    	    	    									
                    	    	    // InternalCQL.g:401:10: ( (lv_attributes_7_0= ruleAttribute ) )
                    	    	    // InternalCQL.g:402:11: (lv_attributes_7_0= ruleAttribute )
                    	    	    {
                    	    	    // InternalCQL.g:402:11: (lv_attributes_7_0= ruleAttribute )
                    	    	    // InternalCQL.g:403:12: lv_attributes_7_0= ruleAttribute
                    	    	    {

                    	    	    												newCompositeNode(grammarAccess.getSelectAccess().getAttributesAttributeParserRuleCall_2_1_1_1_0());
                    	    	    											
                    	    	    pushFollow(FOLLOW_8);
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

                    	    	default :
                    	    	    if ( cnt7 >= 1 ) break loop7;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(7, input);
                    	                throw eee;
                    	        }
                    	        cnt7++;
                    	    } while (true);

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 3 :
                    	    // InternalCQL.g:426:5: ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:426:5: ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) )
                    	    // InternalCQL.g:427:6: {...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2)");
                    	    }
                    	    // InternalCQL.g:427:106: ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ )
                    	    // InternalCQL.g:428:7: ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2);
                    	    						
                    	    // InternalCQL.g:431:10: ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+
                    	    int cnt8=0;
                    	    loop8:
                    	    do {
                    	        int alt8=2;
                    	        int LA8_0 = input.LA(1);

                    	        if ( (LA8_0==16) ) {
                    	            int LA8_2 = input.LA(2);

                    	            if ( ((true)) ) {
                    	                alt8=1;
                    	            }


                    	        }


                    	        switch (alt8) {
                    	    	case 1 :
                    	    	    // InternalCQL.g:431:11: {...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect", "true");
                    	    	    }
                    	    	    // InternalCQL.g:431:20: (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    	    // InternalCQL.g:431:21: otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    	    {
                    	    	    otherlv_8=(Token)match(input,16,FOLLOW_9); 

                    	    	    										newLeafNode(otherlv_8, grammarAccess.getSelectAccess().getCommaKeyword_2_1_2_0());
                    	    	    									
                    	    	    // InternalCQL.g:435:10: ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    	    // InternalCQL.g:436:11: (lv_aggregations_9_0= ruleAggregation )
                    	    	    {
                    	    	    // InternalCQL.g:436:11: (lv_aggregations_9_0= ruleAggregation )
                    	    	    // InternalCQL.g:437:12: lv_aggregations_9_0= ruleAggregation
                    	    	    {

                    	    	    												newCompositeNode(grammarAccess.getSelectAccess().getAggregationsAggregationParserRuleCall_2_1_2_1_0());
                    	    	    											
                    	    	    pushFollow(FOLLOW_8);
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

                    	    	default :
                    	    	    if ( cnt8 >= 1 ) break loop8;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(8, input);
                    	                throw eee;
                    	        }
                    	        cnt8++;
                    	    } while (true);

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt9 >= 1 ) break loop9;
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);

                    if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getSelectAccess().getUnorderedGroup_2_1()) ) {
                        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canLeave(grammarAccess.getSelectAccess().getUnorderedGroup_2_1())");
                    }

                    }


                    }

                     
                    					  getUnorderedGroupHelper().leave(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    					

                    }


                    }
                    break;

            }

            // InternalCQL.g:469:3: (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* )
            // InternalCQL.g:470:4: otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )*
            {
            otherlv_10=(Token)match(input,17,FOLLOW_10); 

            				newLeafNode(otherlv_10, grammarAccess.getSelectAccess().getFROMKeyword_3_0());
            			
            // InternalCQL.g:474:4: ( (lv_sources_11_0= ruleSource ) )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==RULE_ID||LA11_0==22) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalCQL.g:475:5: (lv_sources_11_0= ruleSource )
            	    {
            	    // InternalCQL.g:475:5: (lv_sources_11_0= ruleSource )
            	    // InternalCQL.g:476:6: lv_sources_11_0= ruleSource
            	    {

            	    						newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_11);
            	    lv_sources_11_0=ruleSource();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getSelectRule());
            	    						}
            	    						add(
            	    							current,
            	    							"sources",
            	    							lv_sources_11_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Source");
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

            // InternalCQL.g:493:4: (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==16) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalCQL.g:494:5: otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) )
            	    {
            	    otherlv_12=(Token)match(input,16,FOLLOW_10); 

            	    					newLeafNode(otherlv_12, grammarAccess.getSelectAccess().getCommaKeyword_3_2_0());
            	    				
            	    // InternalCQL.g:498:5: ( (lv_sources_13_0= ruleSource ) )
            	    // InternalCQL.g:499:6: (lv_sources_13_0= ruleSource )
            	    {
            	    // InternalCQL.g:499:6: (lv_sources_13_0= ruleSource )
            	    // InternalCQL.g:500:7: lv_sources_13_0= ruleSource
            	    {

            	    							newCompositeNode(grammarAccess.getSelectAccess().getSourcesSourceParserRuleCall_3_2_1_0());
            	    						
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


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            }

            // InternalCQL.g:519:3: (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==18) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQL.g:520:4: otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) )
                    {
                    otherlv_14=(Token)match(input,18,FOLLOW_13); 

                    				newLeafNode(otherlv_14, grammarAccess.getSelectAccess().getWHEREKeyword_4_0());
                    			
                    // InternalCQL.g:524:4: ( (lv_predicates_15_0= ruleExpressionsModel ) )
                    // InternalCQL.g:525:5: (lv_predicates_15_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:525:5: (lv_predicates_15_0= ruleExpressionsModel )
                    // InternalCQL.g:526:6: lv_predicates_15_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getPredicatesExpressionsModelParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_14);
                    lv_predicates_15_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"predicates",
                    							lv_predicates_15_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ExpressionsModel");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:544:3: (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==19) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalCQL.g:545:4: otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )*
                    {
                    otherlv_16=(Token)match(input,19,FOLLOW_15); 

                    				newLeafNode(otherlv_16, grammarAccess.getSelectAccess().getGROUPKeyword_5_0());
                    			
                    otherlv_17=(Token)match(input,20,FOLLOW_9); 

                    				newLeafNode(otherlv_17, grammarAccess.getSelectAccess().getBYKeyword_5_1());
                    			
                    // InternalCQL.g:553:4: ( (lv_order_18_0= ruleAttribute ) )+
                    int cnt14=0;
                    loop14:
                    do {
                        int alt14=2;
                        int LA14_0 = input.LA(1);

                        if ( (LA14_0==RULE_ID) ) {
                            alt14=1;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // InternalCQL.g:554:5: (lv_order_18_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:554:5: (lv_order_18_0= ruleAttribute )
                    	    // InternalCQL.g:555:6: lv_order_18_0= ruleAttribute
                    	    {

                    	    						newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_2_0());
                    	    					
                    	    pushFollow(FOLLOW_16);
                    	    lv_order_18_0=ruleAttribute();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"order",
                    	    							lv_order_18_0,
                    	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt14 >= 1 ) break loop14;
                                EarlyExitException eee =
                                    new EarlyExitException(14, input);
                                throw eee;
                        }
                        cnt14++;
                    } while (true);

                    // InternalCQL.g:572:4: (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==16) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // InternalCQL.g:573:5: otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) )
                    	    {
                    	    otherlv_19=(Token)match(input,16,FOLLOW_9); 

                    	    					newLeafNode(otherlv_19, grammarAccess.getSelectAccess().getCommaKeyword_5_3_0());
                    	    				
                    	    // InternalCQL.g:577:5: ( (lv_order_20_0= ruleAttribute ) )
                    	    // InternalCQL.g:578:6: (lv_order_20_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:578:6: (lv_order_20_0= ruleAttribute )
                    	    // InternalCQL.g:579:7: lv_order_20_0= ruleAttribute
                    	    {

                    	    							newCompositeNode(grammarAccess.getSelectAccess().getOrderAttributeParserRuleCall_5_3_1_0());
                    	    						
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


                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCQL.g:598:3: (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==21) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // InternalCQL.g:599:4: otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) )
                    {
                    otherlv_21=(Token)match(input,21,FOLLOW_13); 

                    				newLeafNode(otherlv_21, grammarAccess.getSelectAccess().getHAVINGKeyword_6_0());
                    			
                    // InternalCQL.g:603:4: ( (lv_having_22_0= ruleExpressionsModel ) )
                    // InternalCQL.g:604:5: (lv_having_22_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:604:5: (lv_having_22_0= ruleExpressionsModel )
                    // InternalCQL.g:605:6: lv_having_22_0= ruleExpressionsModel
                    {

                    						newCompositeNode(grammarAccess.getSelectAccess().getHavingExpressionsModelParserRuleCall_6_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_having_22_0=ruleExpressionsModel();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSelectRule());
                    						}
                    						set(
                    							current,
                    							"having",
                    							lv_having_22_0,
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
    // InternalCQL.g:627:1: entryRuleNestedStatement returns [EObject current=null] : iv_ruleNestedStatement= ruleNestedStatement EOF ;
    public final EObject entryRuleNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedStatement = null;


        try {
            // InternalCQL.g:627:56: (iv_ruleNestedStatement= ruleNestedStatement EOF )
            // InternalCQL.g:628:2: iv_ruleNestedStatement= ruleNestedStatement EOF
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
    // InternalCQL.g:634:1: ruleNestedStatement returns [EObject current=null] : (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) ;
    public final EObject ruleNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:640:2: ( (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) )
            // InternalCQL.g:641:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            {
            // InternalCQL.g:641:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            // InternalCQL.g:642:3: otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')'
            {
            otherlv_0=(Token)match(input,22,FOLLOW_18); 

            			newLeafNode(otherlv_0, grammarAccess.getNestedStatementAccess().getLeftParenthesisKeyword_0());
            		

            			newCompositeNode(grammarAccess.getNestedStatementAccess().getSelectParserRuleCall_1());
            		
            pushFollow(FOLLOW_19);
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
    // InternalCQL.g:662:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQL.g:662:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQL.g:663:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQL.g:669:1: ruleSource returns [EObject current=null] : ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) ) ;
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
            // InternalCQL.g:675:2: ( ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) ) )
            // InternalCQL.g:676:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) )
            {
            // InternalCQL.g:676:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) )
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==RULE_ID) ) {
                alt21=1;
            }
            else if ( (LA21_0==22) ) {
                alt21=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }
            switch (alt21) {
                case 1 :
                    // InternalCQL.g:677:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
                    {
                    // InternalCQL.g:677:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
                    // InternalCQL.g:678:4: ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
                    {
                    // InternalCQL.g:678:4: ( (lv_name_0_0= ruleSourceName ) )
                    // InternalCQL.g:679:5: (lv_name_0_0= ruleSourceName )
                    {
                    // InternalCQL.g:679:5: (lv_name_0_0= ruleSourceName )
                    // InternalCQL.g:680:6: lv_name_0_0= ruleSourceName
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

                    // InternalCQL.g:697:4: (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==24) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // InternalCQL.g:698:5: otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']'
                            {
                            otherlv_1=(Token)match(input,24,FOLLOW_21); 

                            					newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_1_0());
                            				
                            // InternalCQL.g:702:5: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                            int alt18=3;
                            int LA18_0 = input.LA(1);

                            if ( (LA18_0==44) ) {
                                alt18=1;
                            }
                            else if ( (LA18_0==45) ) {
                                int LA18_2 = input.LA(2);

                                if ( (LA18_2==RULE_INT) ) {
                                    int LA18_3 = input.LA(3);

                                    if ( (LA18_3==46||LA18_3==48) ) {
                                        alt18=3;
                                    }
                                    else if ( (LA18_3==RULE_ID) ) {
                                        alt18=2;
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("", 18, 3, input);

                                        throw nvae;
                                    }
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 18, 2, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 18, 0, input);

                                throw nvae;
                            }
                            switch (alt18) {
                                case 1 :
                                    // InternalCQL.g:703:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQL.g:703:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQL.g:704:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQL.g:704:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQL.g:705:8: lv_unbounded_2_0= ruleWindow_Unbounded
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
                                    // InternalCQL.g:723:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQL.g:723:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQL.g:724:7: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQL.g:724:7: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQL.g:725:8: lv_time_3_0= ruleWindow_Timebased
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
                                    // InternalCQL.g:743:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQL.g:743:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQL.g:744:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQL.g:744:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQL.g:745:8: lv_tuple_4_0= ruleWindow_Tuplebased
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

                            otherlv_5=(Token)match(input,25,FOLLOW_23); 

                            					newLeafNode(otherlv_5, grammarAccess.getSourceAccess().getRightSquareBracketKeyword_0_1_2());
                            				

                            }
                            break;

                    }

                    // InternalCQL.g:768:4: (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==26) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // InternalCQL.g:769:5: otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) )
                            {
                            otherlv_6=(Token)match(input,26,FOLLOW_9); 

                            					newLeafNode(otherlv_6, grammarAccess.getSourceAccess().getASKeyword_0_2_0());
                            				
                            // InternalCQL.g:773:5: ( (lv_alias_7_0= ruleAlias ) )
                            // InternalCQL.g:774:6: (lv_alias_7_0= ruleAlias )
                            {
                            // InternalCQL.g:774:6: (lv_alias_7_0= ruleAlias )
                            // InternalCQL.g:775:7: lv_alias_7_0= ruleAlias
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
                    // InternalCQL.g:795:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) )
                    {
                    // InternalCQL.g:795:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) )
                    // InternalCQL.g:796:4: ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) )
                    {
                    // InternalCQL.g:796:4: ( (lv_nested_8_0= ruleNestedStatement ) )
                    // InternalCQL.g:797:5: (lv_nested_8_0= ruleNestedStatement )
                    {
                    // InternalCQL.g:797:5: (lv_nested_8_0= ruleNestedStatement )
                    // InternalCQL.g:798:6: lv_nested_8_0= ruleNestedStatement
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

                    otherlv_9=(Token)match(input,26,FOLLOW_9); 

                    				newLeafNode(otherlv_9, grammarAccess.getSourceAccess().getASKeyword_1_1());
                    			
                    // InternalCQL.g:819:4: ( (lv_alias_10_0= ruleAlias ) )
                    // InternalCQL.g:820:5: (lv_alias_10_0= ruleAlias )
                    {
                    // InternalCQL.g:820:5: (lv_alias_10_0= ruleAlias )
                    // InternalCQL.g:821:6: lv_alias_10_0= ruleAlias
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
    // InternalCQL.g:843:1: entryRuleSourceName returns [String current=null] : iv_ruleSourceName= ruleSourceName EOF ;
    public final String entryRuleSourceName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSourceName = null;


        try {
            // InternalCQL.g:843:50: (iv_ruleSourceName= ruleSourceName EOF )
            // InternalCQL.g:844:2: iv_ruleSourceName= ruleSourceName EOF
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
    // InternalCQL.g:850:1: ruleSourceName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleSourceName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQL.g:856:2: (this_ID_0= RULE_ID )
            // InternalCQL.g:857:2: this_ID_0= RULE_ID
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
    // InternalCQL.g:867:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQL.g:867:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQL.g:868:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQL.g:874:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:880:2: ( ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:881:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:881:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQL.g:882:3: ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:882:3: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQL.g:883:4: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQL.g:883:4: (lv_name_0_0= ruleAttributeName )
            // InternalCQL.g:884:5: lv_name_0_0= ruleAttributeName
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

            // InternalCQL.g:901:3: (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==26) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalCQL.g:902:4: otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,26,FOLLOW_9); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:906:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQL.g:907:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQL.g:907:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQL.g:908:6: lv_alias_2_0= ruleAlias
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


    // $ANTLR start "entryRuleAttributeWithoutAlias"
    // InternalCQL.g:930:1: entryRuleAttributeWithoutAlias returns [EObject current=null] : iv_ruleAttributeWithoutAlias= ruleAttributeWithoutAlias EOF ;
    public final EObject entryRuleAttributeWithoutAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAlias = null;


        try {
            // InternalCQL.g:930:62: (iv_ruleAttributeWithoutAlias= ruleAttributeWithoutAlias EOF )
            // InternalCQL.g:931:2: iv_ruleAttributeWithoutAlias= ruleAttributeWithoutAlias EOF
            {
             newCompositeNode(grammarAccess.getAttributeWithoutAliasRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttributeWithoutAlias=ruleAttributeWithoutAlias();

            state._fsp--;

             current =iv_ruleAttributeWithoutAlias; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttributeWithoutAlias"


    // $ANTLR start "ruleAttributeWithoutAlias"
    // InternalCQL.g:937:1: ruleAttributeWithoutAlias returns [EObject current=null] : ( (lv_name_0_0= ruleAttributeName ) ) ;
    public final EObject ruleAttributeWithoutAlias() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:943:2: ( ( (lv_name_0_0= ruleAttributeName ) ) )
            // InternalCQL.g:944:2: ( (lv_name_0_0= ruleAttributeName ) )
            {
            // InternalCQL.g:944:2: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQL.g:945:3: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQL.g:945:3: (lv_name_0_0= ruleAttributeName )
            // InternalCQL.g:946:4: lv_name_0_0= ruleAttributeName
            {

            				newCompositeNode(grammarAccess.getAttributeWithoutAliasAccess().getNameAttributeNameParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_name_0_0=ruleAttributeName();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getAttributeWithoutAliasRule());
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
    // $ANTLR end "ruleAttributeWithoutAlias"


    // $ANTLR start "entryRuleAttributeName"
    // InternalCQL.g:966:1: entryRuleAttributeName returns [String current=null] : iv_ruleAttributeName= ruleAttributeName EOF ;
    public final String entryRuleAttributeName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAttributeName = null;


        try {
            // InternalCQL.g:966:53: (iv_ruleAttributeName= ruleAttributeName EOF )
            // InternalCQL.g:967:2: iv_ruleAttributeName= ruleAttributeName EOF
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
    // InternalCQL.g:973:1: ruleAttributeName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) ) ;
    public final AntlrDatatypeRuleToken ruleAttributeName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_SourceName_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:979:2: ( (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) ) )
            // InternalCQL.g:980:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) )
            {
            // InternalCQL.g:980:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==RULE_ID) ) {
                int LA23_1 = input.LA(2);

                if ( (LA23_1==27) ) {
                    alt23=2;
                }
                else if ( (LA23_1==EOF||LA23_1==RULE_ID||(LA23_1>=12 && LA23_1<=13)||(LA23_1>=15 && LA23_1<=17)||LA23_1==19||LA23_1==21||LA23_1==23||(LA23_1>=25 && LA23_1<=26)||LA23_1==28||(LA23_1>=34 && LA23_1<=35)||LA23_1==40||LA23_1==42||(LA23_1>=50 && LA23_1<=60)||(LA23_1>=64 && LA23_1<=70)) ) {
                    alt23=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 23, 1, input);

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
                    // InternalCQL.g:981:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQL.g:989:3: (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID )
                    {
                    // InternalCQL.g:989:3: (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID )
                    // InternalCQL.g:990:4: this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID
                    {

                    				newCompositeNode(grammarAccess.getAttributeNameAccess().getSourceNameParserRuleCall_1_0());
                    			
                    pushFollow(FOLLOW_25);
                    this_SourceName_1=ruleSourceName();

                    state._fsp--;


                    				current.merge(this_SourceName_1);
                    			

                    				afterParserOrEnumRuleCall();
                    			
                    kw=(Token)match(input,27,FOLLOW_9); 

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
    // InternalCQL.g:1017:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQL.g:1017:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQL.g:1018:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
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
    // InternalCQL.g:1024:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttributeWithoutAlias ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1030:2: ( ( ( (lv_value_0_0= ruleAttributeWithoutAlias ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) )
            // InternalCQL.g:1031:2: ( ( (lv_value_0_0= ruleAttributeWithoutAlias ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            {
            // InternalCQL.g:1031:2: ( ( (lv_value_0_0= ruleAttributeWithoutAlias ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            // InternalCQL.g:1032:3: ( (lv_value_0_0= ruleAttributeWithoutAlias ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) )
            {
            // InternalCQL.g:1032:3: ( (lv_value_0_0= ruleAttributeWithoutAlias ) )
            // InternalCQL.g:1033:4: (lv_value_0_0= ruleAttributeWithoutAlias )
            {
            // InternalCQL.g:1033:4: (lv_value_0_0= ruleAttributeWithoutAlias )
            // InternalCQL.g:1034:5: lv_value_0_0= ruleAttributeWithoutAlias
            {

            					newCompositeNode(grammarAccess.getAttributeWithNestedStatementAccess().getValueAttributeWithoutAliasParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_26);
            lv_value_0_0=ruleAttributeWithoutAlias();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeWithNestedStatementRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAlias");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,28,FOLLOW_10); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeWithNestedStatementAccess().getINKeyword_1());
            		
            // InternalCQL.g:1055:3: ( (lv_nested_2_0= ruleNestedStatement ) )
            // InternalCQL.g:1056:4: (lv_nested_2_0= ruleNestedStatement )
            {
            // InternalCQL.g:1056:4: (lv_nested_2_0= ruleNestedStatement )
            // InternalCQL.g:1057:5: lv_nested_2_0= ruleNestedStatement
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
    // InternalCQL.g:1078:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalCQL.g:1078:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalCQL.g:1079:2: iv_ruleAggregation= ruleAggregation EOF
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
    // InternalCQL.g:1085:1: ruleAggregation returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAggregation() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_attribute_2_0 = null;

        EObject lv_alias_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1091:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:1092:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:1092:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            // InternalCQL.g:1093:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:1093:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:1094:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:1094:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:1095:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_27); 

            					newLeafNode(lv_name_0_0, grammarAccess.getAggregationAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAggregationRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,22,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregationAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:1115:3: ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) )
            // InternalCQL.g:1116:4: (lv_attribute_2_0= ruleAttributeWithoutAlias )
            {
            // InternalCQL.g:1116:4: (lv_attribute_2_0= ruleAttributeWithoutAlias )
            // InternalCQL.g:1117:5: lv_attribute_2_0= ruleAttributeWithoutAlias
            {

            					newCompositeNode(grammarAccess.getAggregationAccess().getAttributeAttributeWithoutAliasParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_19);
            lv_attribute_2_0=ruleAttributeWithoutAlias();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAggregationRule());
            					}
            					set(
            						current,
            						"attribute",
            						lv_attribute_2_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAlias");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,23,FOLLOW_23); 

            			newLeafNode(otherlv_3, grammarAccess.getAggregationAccess().getRightParenthesisKeyword_3());
            		
            // InternalCQL.g:1138:3: (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==26) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalCQL.g:1139:4: otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) )
                    {
                    otherlv_4=(Token)match(input,26,FOLLOW_9); 

                    				newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getASKeyword_4_0());
                    			
                    // InternalCQL.g:1143:4: ( (lv_alias_5_0= ruleAlias ) )
                    // InternalCQL.g:1144:5: (lv_alias_5_0= ruleAlias )
                    {
                    // InternalCQL.g:1144:5: (lv_alias_5_0= ruleAlias )
                    // InternalCQL.g:1145:6: lv_alias_5_0= ruleAlias
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


    // $ANTLR start "entryRuleAlias"
    // InternalCQL.g:1167:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQL.g:1167:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQL.g:1168:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQL.g:1174:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:1180:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:1181:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:1181:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:1182:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:1182:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:1183:4: lv_name_0_0= RULE_ID
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
    // InternalCQL.g:1202:1: entryRuleCreateParameters returns [EObject current=null] : iv_ruleCreateParameters= ruleCreateParameters EOF ;
    public final EObject entryRuleCreateParameters() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateParameters = null;


        try {
            // InternalCQL.g:1202:57: (iv_ruleCreateParameters= ruleCreateParameters EOF )
            // InternalCQL.g:1203:2: iv_ruleCreateParameters= ruleCreateParameters EOF
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
    // InternalCQL.g:1209:1: ruleCreateParameters returns [EObject current=null] : (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' ) ;
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
            // InternalCQL.g:1215:2: ( (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' ) )
            // InternalCQL.g:1216:2: (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' )
            {
            // InternalCQL.g:1216:2: (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' )
            // InternalCQL.g:1217:3: otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_28); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateParametersAccess().getWRAPPERKeyword_0());
            		
            // InternalCQL.g:1221:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQL.g:1222:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQL.g:1222:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQL.g:1223:5: lv_wrapper_1_0= RULE_STRING
            {
            lv_wrapper_1_0=(Token)match(input,RULE_STRING,FOLLOW_29); 

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

            otherlv_2=(Token)match(input,30,FOLLOW_28); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateParametersAccess().getPROTOCOLKeyword_2());
            		
            // InternalCQL.g:1243:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQL.g:1244:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQL.g:1244:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQL.g:1245:5: lv_protocol_3_0= RULE_STRING
            {
            lv_protocol_3_0=(Token)match(input,RULE_STRING,FOLLOW_30); 

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

            otherlv_4=(Token)match(input,31,FOLLOW_28); 

            			newLeafNode(otherlv_4, grammarAccess.getCreateParametersAccess().getTRANSPORTKeyword_4());
            		
            // InternalCQL.g:1265:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQL.g:1266:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQL.g:1266:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQL.g:1267:5: lv_transport_5_0= RULE_STRING
            {
            lv_transport_5_0=(Token)match(input,RULE_STRING,FOLLOW_31); 

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

            otherlv_6=(Token)match(input,32,FOLLOW_28); 

            			newLeafNode(otherlv_6, grammarAccess.getCreateParametersAccess().getDATAHANDLERKeyword_6());
            		
            // InternalCQL.g:1287:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQL.g:1288:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQL.g:1288:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQL.g:1289:5: lv_datahandler_7_0= RULE_STRING
            {
            lv_datahandler_7_0=(Token)match(input,RULE_STRING,FOLLOW_32); 

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

            otherlv_8=(Token)match(input,33,FOLLOW_27); 

            			newLeafNode(otherlv_8, grammarAccess.getCreateParametersAccess().getOPTIONSKeyword_8());
            		
            otherlv_9=(Token)match(input,22,FOLLOW_28); 

            			newLeafNode(otherlv_9, grammarAccess.getCreateParametersAccess().getLeftParenthesisKeyword_9());
            		
            // InternalCQL.g:1313:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+
            int cnt25=0;
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==RULE_STRING) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // InternalCQL.g:1314:4: ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) )
            	    {
            	    // InternalCQL.g:1314:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQL.g:1315:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:1315:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQL.g:1316:6: lv_keys_10_0= RULE_STRING
            	    {
            	    lv_keys_10_0=(Token)match(input,RULE_STRING,FOLLOW_28); 

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

            	    // InternalCQL.g:1332:4: ( (lv_values_11_0= RULE_STRING ) )
            	    // InternalCQL.g:1333:5: (lv_values_11_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:1333:5: (lv_values_11_0= RULE_STRING )
            	    // InternalCQL.g:1334:6: lv_values_11_0= RULE_STRING
            	    {
            	    lv_values_11_0=(Token)match(input,RULE_STRING,FOLLOW_33); 

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
            	    if ( cnt25 >= 1 ) break loop25;
                        EarlyExitException eee =
                            new EarlyExitException(25, input);
                        throw eee;
                }
                cnt25++;
            } while (true);

            // InternalCQL.g:1351:3: (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==16) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // InternalCQL.g:1352:4: otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) )
                    {
                    otherlv_12=(Token)match(input,16,FOLLOW_28); 

                    				newLeafNode(otherlv_12, grammarAccess.getCreateParametersAccess().getCommaKeyword_11_0());
                    			
                    // InternalCQL.g:1356:4: ( (lv_keys_13_0= RULE_STRING ) )
                    // InternalCQL.g:1357:5: (lv_keys_13_0= RULE_STRING )
                    {
                    // InternalCQL.g:1357:5: (lv_keys_13_0= RULE_STRING )
                    // InternalCQL.g:1358:6: lv_keys_13_0= RULE_STRING
                    {
                    lv_keys_13_0=(Token)match(input,RULE_STRING,FOLLOW_28); 

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

                    // InternalCQL.g:1374:4: ( (lv_values_14_0= RULE_STRING ) )
                    // InternalCQL.g:1375:5: (lv_values_14_0= RULE_STRING )
                    {
                    // InternalCQL.g:1375:5: (lv_values_14_0= RULE_STRING )
                    // InternalCQL.g:1376:6: lv_values_14_0= RULE_STRING
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
    // InternalCQL.g:1401:1: entryRuleAttributeDefinition returns [EObject current=null] : iv_ruleAttributeDefinition= ruleAttributeDefinition EOF ;
    public final EObject entryRuleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeDefinition = null;


        try {
            // InternalCQL.g:1401:60: (iv_ruleAttributeDefinition= ruleAttributeDefinition EOF )
            // InternalCQL.g:1402:2: iv_ruleAttributeDefinition= ruleAttributeDefinition EOF
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
    // InternalCQL.g:1408:1: ruleAttributeDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' ) ;
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
            // InternalCQL.g:1414:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' ) )
            // InternalCQL.g:1415:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' )
            {
            // InternalCQL.g:1415:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' )
            // InternalCQL.g:1416:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')'
            {
            // InternalCQL.g:1416:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:1417:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:1417:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:1418:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_27); 

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

            otherlv_1=(Token)match(input,22,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeDefinitionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:1438:3: ( (lv_attributes_2_0= ruleAttribute ) )+
            int cnt27=0;
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==RULE_ID) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // InternalCQL.g:1439:4: (lv_attributes_2_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1439:4: (lv_attributes_2_0= ruleAttribute )
            	    // InternalCQL.g:1440:5: lv_attributes_2_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getAttributesAttributeParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_34);
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
            	    if ( cnt27 >= 1 ) break loop27;
                        EarlyExitException eee =
                            new EarlyExitException(27, input);
                        throw eee;
                }
                cnt27++;
            } while (true);

            // InternalCQL.g:1457:3: ( (lv_datatypes_3_0= ruleDataType ) )+
            int cnt28=0;
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( ((LA28_0>=64 && LA28_0<=70)) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // InternalCQL.g:1458:4: (lv_datatypes_3_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1458:4: (lv_datatypes_3_0= ruleDataType )
            	    // InternalCQL.g:1459:5: lv_datatypes_3_0= ruleDataType
            	    {

            	    					newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getDatatypesDataTypeParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_35);
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
            	    if ( cnt28 >= 1 ) break loop28;
                        EarlyExitException eee =
                            new EarlyExitException(28, input);
                        throw eee;
                }
                cnt28++;
            } while (true);

            // InternalCQL.g:1476:3: (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( (LA29_0==16) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // InternalCQL.g:1477:4: otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) )
            	    {
            	    otherlv_4=(Token)match(input,16,FOLLOW_9); 

            	    				newLeafNode(otherlv_4, grammarAccess.getAttributeDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQL.g:1481:4: ( (lv_attributes_5_0= ruleAttribute ) )
            	    // InternalCQL.g:1482:5: (lv_attributes_5_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1482:5: (lv_attributes_5_0= ruleAttribute )
            	    // InternalCQL.g:1483:6: lv_attributes_5_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getAttributesAttributeParserRuleCall_4_1_0());
            	    					
            	    pushFollow(FOLLOW_36);
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

            	    // InternalCQL.g:1500:4: ( (lv_datatypes_6_0= ruleDataType ) )
            	    // InternalCQL.g:1501:5: (lv_datatypes_6_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1501:5: (lv_datatypes_6_0= ruleDataType )
            	    // InternalCQL.g:1502:6: lv_datatypes_6_0= ruleDataType
            	    {

            	    						newCompositeNode(grammarAccess.getAttributeDefinitionAccess().getDatatypesDataTypeParserRuleCall_4_2_0());
            	    					
            	    pushFollow(FOLLOW_37);
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
            	    break loop29;
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
    // InternalCQL.g:1528:1: entryRuleCreateStream1 returns [EObject current=null] : iv_ruleCreateStream1= ruleCreateStream1 EOF ;
    public final EObject entryRuleCreateStream1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStream1 = null;


        try {
            // InternalCQL.g:1528:54: (iv_ruleCreateStream1= ruleCreateStream1 EOF )
            // InternalCQL.g:1529:2: iv_ruleCreateStream1= ruleCreateStream1 EOF
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
    // InternalCQL.g:1535:1: ruleCreateStream1 returns [EObject current=null] : (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateStream1() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1541:2: ( (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQL.g:1542:2: (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQL.g:1542:2: (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQL.g:1543:3: otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            otherlv_0=(Token)match(input,34,FOLLOW_38); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateStream1Access().getCREATEKeyword_0());
            		
            otherlv_1=(Token)match(input,35,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStream1Access().getSTREAMKeyword_1());
            		
            // InternalCQL.g:1551:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:1552:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:1552:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:1553:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStream1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_39);
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

            // InternalCQL.g:1570:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQL.g:1571:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQL.g:1571:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQL.g:1572:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQL.g:1593:1: entryRuleCreateSink1 returns [EObject current=null] : iv_ruleCreateSink1= ruleCreateSink1 EOF ;
    public final EObject entryRuleCreateSink1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateSink1 = null;


        try {
            // InternalCQL.g:1593:52: (iv_ruleCreateSink1= ruleCreateSink1 EOF )
            // InternalCQL.g:1594:2: iv_ruleCreateSink1= ruleCreateSink1 EOF
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
    // InternalCQL.g:1600:1: ruleCreateSink1 returns [EObject current=null] : (otherlv_0= 'CREATE' otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateSink1() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1606:2: ( (otherlv_0= 'CREATE' otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQL.g:1607:2: (otherlv_0= 'CREATE' otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQL.g:1607:2: (otherlv_0= 'CREATE' otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQL.g:1608:3: otherlv_0= 'CREATE' otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            otherlv_0=(Token)match(input,34,FOLLOW_40); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateSink1Access().getCREATEKeyword_0());
            		
            otherlv_1=(Token)match(input,36,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateSink1Access().getSINKKeyword_1());
            		
            // InternalCQL.g:1616:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:1617:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:1617:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:1618:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateSink1Access().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_39);
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

            // InternalCQL.g:1635:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQL.g:1636:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQL.g:1636:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQL.g:1637:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQL.g:1658:1: entryRuleCreateStreamChannel returns [EObject current=null] : iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF ;
    public final EObject entryRuleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamChannel = null;


        try {
            // InternalCQL.g:1658:60: (iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF )
            // InternalCQL.g:1659:2: iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF
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
    // InternalCQL.g:1665:1: ruleCreateStreamChannel returns [EObject current=null] : (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) ) ;
    public final EObject ruleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token lv_host_4_0=null;
        Token otherlv_5=null;
        Token lv_port_6_0=null;
        EObject lv_attributes_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1671:2: ( (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) ) )
            // InternalCQL.g:1672:2: (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) )
            {
            // InternalCQL.g:1672:2: (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) )
            // InternalCQL.g:1673:3: otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) )
            {
            otherlv_0=(Token)match(input,34,FOLLOW_38); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateStreamChannelAccess().getCREATEKeyword_0());
            		
            otherlv_1=(Token)match(input,35,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamChannelAccess().getSTREAMKeyword_1());
            		
            // InternalCQL.g:1681:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:1682:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:1682:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:1683:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamChannelAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_41);
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

            otherlv_3=(Token)match(input,37,FOLLOW_9); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamChannelAccess().getCHANNELKeyword_3());
            		
            // InternalCQL.g:1704:3: ( (lv_host_4_0= RULE_ID ) )
            // InternalCQL.g:1705:4: (lv_host_4_0= RULE_ID )
            {
            // InternalCQL.g:1705:4: (lv_host_4_0= RULE_ID )
            // InternalCQL.g:1706:5: lv_host_4_0= RULE_ID
            {
            lv_host_4_0=(Token)match(input,RULE_ID,FOLLOW_42); 

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

            otherlv_5=(Token)match(input,38,FOLLOW_43); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamChannelAccess().getColonKeyword_5());
            		
            // InternalCQL.g:1726:3: ( (lv_port_6_0= RULE_INT ) )
            // InternalCQL.g:1727:4: (lv_port_6_0= RULE_INT )
            {
            // InternalCQL.g:1727:4: (lv_port_6_0= RULE_INT )
            // InternalCQL.g:1728:5: lv_port_6_0= RULE_INT
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
    // InternalCQL.g:1748:1: entryRuleCreateStreamFile returns [EObject current=null] : iv_ruleCreateStreamFile= ruleCreateStreamFile EOF ;
    public final EObject entryRuleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamFile = null;


        try {
            // InternalCQL.g:1748:57: (iv_ruleCreateStreamFile= ruleCreateStreamFile EOF )
            // InternalCQL.g:1749:2: iv_ruleCreateStreamFile= ruleCreateStreamFile EOF
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
    // InternalCQL.g:1755:1: ruleCreateStreamFile returns [EObject current=null] : (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) ) ;
    public final EObject ruleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token lv_filename_4_0=null;
        Token otherlv_5=null;
        Token lv_type_6_0=null;
        EObject lv_attributes_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1761:2: ( (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) ) )
            // InternalCQL.g:1762:2: (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) )
            {
            // InternalCQL.g:1762:2: (otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) )
            // InternalCQL.g:1763:3: otherlv_0= 'CREATE' otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,34,FOLLOW_38); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateStreamFileAccess().getCREATEKeyword_0());
            		
            otherlv_1=(Token)match(input,35,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamFileAccess().getSTREAMKeyword_1());
            		
            // InternalCQL.g:1771:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:1772:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:1772:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:1773:5: lv_attributes_2_0= ruleAttributeDefinition
            {

            					newCompositeNode(grammarAccess.getCreateStreamFileAccess().getAttributesAttributeDefinitionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_44);
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

            otherlv_3=(Token)match(input,39,FOLLOW_28); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamFileAccess().getFILEKeyword_3());
            		
            // InternalCQL.g:1794:3: ( (lv_filename_4_0= RULE_STRING ) )
            // InternalCQL.g:1795:4: (lv_filename_4_0= RULE_STRING )
            {
            // InternalCQL.g:1795:4: (lv_filename_4_0= RULE_STRING )
            // InternalCQL.g:1796:5: lv_filename_4_0= RULE_STRING
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

            otherlv_5=(Token)match(input,26,FOLLOW_9); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamFileAccess().getASKeyword_5());
            		
            // InternalCQL.g:1816:3: ( (lv_type_6_0= RULE_ID ) )
            // InternalCQL.g:1817:4: (lv_type_6_0= RULE_ID )
            {
            // InternalCQL.g:1817:4: (lv_type_6_0= RULE_ID )
            // InternalCQL.g:1818:5: lv_type_6_0= RULE_ID
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
    // InternalCQL.g:1838:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQL.g:1838:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQL.g:1839:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQL.g:1845:1: ruleCreateView returns [EObject current=null] : (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1851:2: ( (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) ) )
            // InternalCQL.g:1852:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) )
            {
            // InternalCQL.g:1852:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) )
            // InternalCQL.g:1853:3: otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) )
            {
            otherlv_0=(Token)match(input,40,FOLLOW_9); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateViewAccess().getVIEWKeyword_0());
            		
            // InternalCQL.g:1857:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:1858:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:1858:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:1859:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_7); 

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

            otherlv_2=(Token)match(input,17,FOLLOW_10); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateViewAccess().getFROMKeyword_2());
            		
            // InternalCQL.g:1879:3: ( (lv_select_3_0= ruleNestedStatement ) )
            // InternalCQL.g:1880:4: (lv_select_3_0= ruleNestedStatement )
            {
            // InternalCQL.g:1880:4: (lv_select_3_0= ruleNestedStatement )
            // InternalCQL.g:1881:5: lv_select_3_0= ruleNestedStatement
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
    // InternalCQL.g:1902:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQL.g:1902:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQL.g:1903:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQL.g:1909:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1915:2: ( (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQL.g:1916:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQL.g:1916:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:1917:3: otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,35,FOLLOW_45); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,41,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQL.g:1925:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQL.g:1926:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQL.g:1926:4: (lv_name_2_0= RULE_ID )
            // InternalCQL.g:1927:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_46); 

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

            // InternalCQL.g:1943:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==13) ) {
                alt30=1;
            }
            else if ( (LA30_0==RULE_ID) ) {
                alt30=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;
            }
            switch (alt30) {
                case 1 :
                    // InternalCQL.g:1944:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQL.g:1944:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQL.g:1945:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQL.g:1945:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQL.g:1946:6: lv_statement_3_0= ruleSelect
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
                    // InternalCQL.g:1964:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQL.g:1964:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQL.g:1965:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQL.g:1965:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQL.g:1966:6: lv_inputname_4_0= RULE_ID
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
    // InternalCQL.g:1987:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQL.g:1987:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQL.g:1988:2: iv_ruleDrop= ruleDrop EOF
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
    // InternalCQL.g:1994:1: ruleDrop returns [EObject current=null] : ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) ) ;
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
            // InternalCQL.g:2000:2: ( ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:2001:2: ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            {
            // InternalCQL.g:2001:2: ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            // InternalCQL.g:2002:3: ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) )
            {
            // InternalCQL.g:2002:3: ( (lv_keyword1_0_0= 'DROP' ) )
            // InternalCQL.g:2003:4: (lv_keyword1_0_0= 'DROP' )
            {
            // InternalCQL.g:2003:4: (lv_keyword1_0_0= 'DROP' )
            // InternalCQL.g:2004:5: lv_keyword1_0_0= 'DROP'
            {
            lv_keyword1_0_0=(Token)match(input,42,FOLLOW_47); 

            					newLeafNode(lv_keyword1_0_0, grammarAccess.getDropAccess().getKeyword1DROPKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(current, "keyword1", lv_keyword1_0_0, "DROP");
            				

            }


            }

            // InternalCQL.g:2016:3: ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) )
            // InternalCQL.g:2017:4: ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) )
            {
            // InternalCQL.g:2017:4: ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) )
            // InternalCQL.g:2018:5: (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' )
            {
            // InternalCQL.g:2018:5: (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' )
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==36) ) {
                alt31=1;
            }
            else if ( (LA31_0==35) ) {
                alt31=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;
            }
            switch (alt31) {
                case 1 :
                    // InternalCQL.g:2019:6: lv_keyword2_1_1= 'SINK'
                    {
                    lv_keyword2_1_1=(Token)match(input,36,FOLLOW_9); 

                    						newLeafNode(lv_keyword2_1_1, grammarAccess.getDropAccess().getKeyword2SINKKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "keyword2", lv_keyword2_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:2030:6: lv_keyword2_1_2= 'STREAM'
                    {
                    lv_keyword2_1_2=(Token)match(input,35,FOLLOW_9); 

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

            // InternalCQL.g:2043:3: ( (lv_value1_2_0= RULE_ID ) )
            // InternalCQL.g:2044:4: (lv_value1_2_0= RULE_ID )
            {
            // InternalCQL.g:2044:4: (lv_value1_2_0= RULE_ID )
            // InternalCQL.g:2045:5: lv_value1_2_0= RULE_ID
            {
            lv_value1_2_0=(Token)match(input,RULE_ID,FOLLOW_48); 

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

            // InternalCQL.g:2061:3: ( (lv_keyword3_3_0= 'IF EXISTS' ) )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==43) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // InternalCQL.g:2062:4: (lv_keyword3_3_0= 'IF EXISTS' )
                    {
                    // InternalCQL.g:2062:4: (lv_keyword3_3_0= 'IF EXISTS' )
                    // InternalCQL.g:2063:5: lv_keyword3_3_0= 'IF EXISTS'
                    {
                    lv_keyword3_3_0=(Token)match(input,43,FOLLOW_9); 

                    					newLeafNode(lv_keyword3_3_0, grammarAccess.getDropAccess().getKeyword3IFEXISTSKeyword_3_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDropRule());
                    					}
                    					setWithLastConsumed(current, "keyword3", lv_keyword3_3_0, "IF EXISTS");
                    				

                    }


                    }
                    break;

            }

            // InternalCQL.g:2075:3: ( (lv_value2_4_0= RULE_ID ) )
            // InternalCQL.g:2076:4: (lv_value2_4_0= RULE_ID )
            {
            // InternalCQL.g:2076:4: (lv_value2_4_0= RULE_ID )
            // InternalCQL.g:2077:5: lv_value2_4_0= RULE_ID
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
    // InternalCQL.g:2097:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQL.g:2097:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQL.g:2098:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQL.g:2104:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'UNBOUNDED' ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQL.g:2110:2: (kw= 'UNBOUNDED' )
            // InternalCQL.g:2111:2: kw= 'UNBOUNDED'
            {
            kw=(Token)match(input,44,FOLLOW_2); 

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
    // InternalCQL.g:2119:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQL.g:2119:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQL.g:2120:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQL.g:2126:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) ;
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
            // InternalCQL.g:2132:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) )
            // InternalCQL.g:2133:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            {
            // InternalCQL.g:2133:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            // InternalCQL.g:2134:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME'
            {
            otherlv_0=(Token)match(input,45,FOLLOW_43); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:2138:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:2139:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:2139:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:2140:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_9); 

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

            // InternalCQL.g:2156:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQL.g:2157:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQL.g:2157:4: (lv_unit_2_0= RULE_ID )
            // InternalCQL.g:2158:5: lv_unit_2_0= RULE_ID
            {
            lv_unit_2_0=(Token)match(input,RULE_ID,FOLLOW_49); 

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

            // InternalCQL.g:2174:3: (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==46) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // InternalCQL.g:2175:4: otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,46,FOLLOW_43); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQL.g:2179:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQL.g:2180:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQL.g:2180:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQL.g:2181:6: lv_advance_size_4_0= RULE_INT
                    {
                    lv_advance_size_4_0=(Token)match(input,RULE_INT,FOLLOW_9); 

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

                    // InternalCQL.g:2197:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQL.g:2198:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQL.g:2198:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQL.g:2199:6: lv_advance_unit_5_0= RULE_ID
                    {
                    lv_advance_unit_5_0=(Token)match(input,RULE_ID,FOLLOW_50); 

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

            otherlv_6=(Token)match(input,47,FOLLOW_2); 

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
    // InternalCQL.g:2224:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQL.g:2224:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQL.g:2225:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQL.g:2231:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQL.g:2237:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQL.g:2238:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQL.g:2238:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQL.g:2239:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,45,FOLLOW_43); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:2243:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:2244:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:2244:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:2245:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_51); 

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

            // InternalCQL.g:2261:3: (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==46) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // InternalCQL.g:2262:4: otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,46,FOLLOW_43); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQL.g:2266:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQL.g:2267:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQL.g:2267:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQL.g:2268:6: lv_advance_size_3_0= RULE_INT
                    {
                    lv_advance_size_3_0=(Token)match(input,RULE_INT,FOLLOW_52); 

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

            otherlv_4=(Token)match(input,48,FOLLOW_53); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQL.g:2289:3: (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==49) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // InternalCQL.g:2290:4: otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,49,FOLLOW_15); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,20,FOLLOW_9); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQL.g:2298:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQL.g:2299:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQL.g:2299:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQL.g:2300:6: lv_partition_attribute_7_0= ruleAttribute
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
    // InternalCQL.g:2322:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQL.g:2322:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQL.g:2323:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQL.g:2329:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2335:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQL.g:2336:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQL.g:2336:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQL.g:2337:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQL.g:2337:3: ()
            // InternalCQL.g:2338:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQL.g:2344:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQL.g:2345:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQL.g:2345:4: (lv_elements_1_0= ruleExpression )
            // InternalCQL.g:2346:5: lv_elements_1_0= ruleExpression
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
    // InternalCQL.g:2367:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQL.g:2367:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQL.g:2368:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQL.g:2374:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2380:2: (this_Or_0= ruleOr )
            // InternalCQL.g:2381:2: this_Or_0= ruleOr
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
    // InternalCQL.g:2392:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQL.g:2392:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQL.g:2393:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQL.g:2399:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2405:2: ( (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQL.g:2406:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQL.g:2406:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQL.g:2407:3: this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_54);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2415:3: ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==50) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // InternalCQL.g:2416:4: () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQL.g:2416:4: ()
            	    // InternalCQL.g:2417:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,50,FOLLOW_13); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQL.g:2427:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQL.g:2428:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQL.g:2428:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQL.g:2429:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_54);
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
            	    break loop36;
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
    // InternalCQL.g:2451:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQL.g:2451:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQL.g:2452:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQL.g:2458:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2464:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQL.g:2465:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQL.g:2465:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQL.g:2466:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_55);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2474:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop37:
            do {
                int alt37=2;
                int LA37_0 = input.LA(1);

                if ( (LA37_0==51) ) {
                    alt37=1;
                }


                switch (alt37) {
            	case 1 :
            	    // InternalCQL.g:2475:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQL.g:2475:4: ()
            	    // InternalCQL.g:2476:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,51,FOLLOW_13); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQL.g:2486:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQL.g:2487:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQL.g:2487:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQL.g:2488:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_55);
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
            	    break loop37;
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
    // InternalCQL.g:2510:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQL.g:2510:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQL.g:2511:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQL.g:2517:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2523:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQL.g:2524:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQL.g:2524:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQL.g:2525:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_56);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2533:3: ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop39:
            do {
                int alt39=2;
                int LA39_0 = input.LA(1);

                if ( ((LA39_0>=52 && LA39_0<=53)) ) {
                    alt39=1;
                }


                switch (alt39) {
            	case 1 :
            	    // InternalCQL.g:2534:4: () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQL.g:2534:4: ()
            	    // InternalCQL.g:2535:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:2541:4: ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) )
            	    // InternalCQL.g:2542:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    {
            	    // InternalCQL.g:2542:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    // InternalCQL.g:2543:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    {
            	    // InternalCQL.g:2543:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    int alt38=2;
            	    int LA38_0 = input.LA(1);

            	    if ( (LA38_0==52) ) {
            	        alt38=1;
            	    }
            	    else if ( (LA38_0==53) ) {
            	        alt38=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 38, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt38) {
            	        case 1 :
            	            // InternalCQL.g:2544:7: lv_op_2_1= '=='
            	            {
            	            lv_op_2_1=(Token)match(input,52,FOLLOW_13); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getEqualitiyRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:2555:7: lv_op_2_2= '!='
            	            {
            	            lv_op_2_2=(Token)match(input,53,FOLLOW_13); 

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

            	    // InternalCQL.g:2568:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQL.g:2569:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQL.g:2569:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQL.g:2570:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_56);
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
            	    break loop39;
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
    // InternalCQL.g:2592:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQL.g:2592:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQL.g:2593:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQL.g:2599:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQL.g:2605:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQL.g:2606:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQL.g:2606:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQL.g:2607:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_57);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2615:3: ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( ((LA41_0>=54 && LA41_0<=57)) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // InternalCQL.g:2616:4: () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQL.g:2616:4: ()
            	    // InternalCQL.g:2617:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:2623:4: ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) )
            	    // InternalCQL.g:2624:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    {
            	    // InternalCQL.g:2624:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    // InternalCQL.g:2625:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    {
            	    // InternalCQL.g:2625:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    int alt40=4;
            	    switch ( input.LA(1) ) {
            	    case 54:
            	        {
            	        alt40=1;
            	        }
            	        break;
            	    case 55:
            	        {
            	        alt40=2;
            	        }
            	        break;
            	    case 56:
            	        {
            	        alt40=3;
            	        }
            	        break;
            	    case 57:
            	        {
            	        alt40=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 40, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt40) {
            	        case 1 :
            	            // InternalCQL.g:2626:7: lv_op_2_1= '>='
            	            {
            	            lv_op_2_1=(Token)match(input,54,FOLLOW_13); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:2637:7: lv_op_2_2= '<='
            	            {
            	            lv_op_2_2=(Token)match(input,55,FOLLOW_13); 

            	            							newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQL.g:2648:7: lv_op_2_3= '<'
            	            {
            	            lv_op_2_3=(Token)match(input,56,FOLLOW_13); 

            	            							newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getComparisonRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQL.g:2659:7: lv_op_2_4= '>'
            	            {
            	            lv_op_2_4=(Token)match(input,57,FOLLOW_13); 

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

            	    // InternalCQL.g:2672:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQL.g:2673:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQL.g:2673:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQL.g:2674:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_57);
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
    // $ANTLR end "ruleComparison"


    // $ANTLR start "entryRulePlusOrMinus"
    // InternalCQL.g:2696:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQL.g:2696:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQL.g:2697:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQL.g:2703:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2709:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQL.g:2710:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQL.g:2710:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQL.g:2711:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_58);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2719:3: ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( ((LA43_0>=58 && LA43_0<=59)) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalCQL.g:2720:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQL.g:2720:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) )
            	    int alt42=2;
            	    int LA42_0 = input.LA(1);

            	    if ( (LA42_0==58) ) {
            	        alt42=1;
            	    }
            	    else if ( (LA42_0==59) ) {
            	        alt42=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 42, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt42) {
            	        case 1 :
            	            // InternalCQL.g:2721:5: ( () otherlv_2= '+' )
            	            {
            	            // InternalCQL.g:2721:5: ( () otherlv_2= '+' )
            	            // InternalCQL.g:2722:6: () otherlv_2= '+'
            	            {
            	            // InternalCQL.g:2722:6: ()
            	            // InternalCQL.g:2723:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_2=(Token)match(input,58,FOLLOW_13); 

            	            						newLeafNode(otherlv_2, grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1());
            	            					

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:2735:5: ( () otherlv_4= '-' )
            	            {
            	            // InternalCQL.g:2735:5: ( () otherlv_4= '-' )
            	            // InternalCQL.g:2736:6: () otherlv_4= '-'
            	            {
            	            // InternalCQL.g:2736:6: ()
            	            // InternalCQL.g:2737:7: 
            	            {

            	            							current = forceCreateModelElementAndSet(
            	            								grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(),
            	            								current);
            	            						

            	            }

            	            otherlv_4=(Token)match(input,59,FOLLOW_13); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1());
            	            					

            	            }


            	            }
            	            break;

            	    }

            	    // InternalCQL.g:2749:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQL.g:2750:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQL.g:2750:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQL.g:2751:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_58);
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
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCQL.g:2773:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQL.g:2773:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQL.g:2774:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQL.g:2780:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2786:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQL.g:2787:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQL.g:2787:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQL.g:2788:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_59);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2796:3: ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop45:
            do {
                int alt45=2;
                int LA45_0 = input.LA(1);

                if ( (LA45_0==15||LA45_0==60) ) {
                    alt45=1;
                }


                switch (alt45) {
            	case 1 :
            	    // InternalCQL.g:2797:4: () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQL.g:2797:4: ()
            	    // InternalCQL.g:2798:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:2804:4: ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) )
            	    // InternalCQL.g:2805:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    {
            	    // InternalCQL.g:2805:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    // InternalCQL.g:2806:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    {
            	    // InternalCQL.g:2806:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    int alt44=2;
            	    int LA44_0 = input.LA(1);

            	    if ( (LA44_0==15) ) {
            	        alt44=1;
            	    }
            	    else if ( (LA44_0==60) ) {
            	        alt44=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 44, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt44) {
            	        case 1 :
            	            // InternalCQL.g:2807:7: lv_op_2_1= '*'
            	            {
            	            lv_op_2_1=(Token)match(input,15,FOLLOW_13); 

            	            							newLeafNode(lv_op_2_1, grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getMulOrDivRule());
            	            							}
            	            							setWithLastConsumed(current, "op", lv_op_2_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:2818:7: lv_op_2_2= '/'
            	            {
            	            lv_op_2_2=(Token)match(input,60,FOLLOW_13); 

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

            	    // InternalCQL.g:2831:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQL.g:2832:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQL.g:2832:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQL.g:2833:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_59);
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
            	    break loop45;
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
    // InternalCQL.g:2855:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQL.g:2855:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQL.g:2856:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQL.g:2862:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQL.g:2868:2: ( ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQL.g:2869:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQL.g:2869:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt46=3;
            switch ( input.LA(1) ) {
            case 22:
                {
                alt46=1;
                }
                break;
            case 61:
                {
                alt46=2;
                }
                break;
            case RULE_ID:
            case RULE_STRING:
            case RULE_INT:
            case RULE_FLOAT:
            case 62:
            case 63:
                {
                alt46=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 46, 0, input);

                throw nvae;
            }

            switch (alt46) {
                case 1 :
                    // InternalCQL.g:2870:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    {
                    // InternalCQL.g:2870:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    // InternalCQL.g:2871:4: () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')'
                    {
                    // InternalCQL.g:2871:4: ()
                    // InternalCQL.g:2872:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,22,FOLLOW_13); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQL.g:2882:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQL.g:2883:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQL.g:2883:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQL.g:2884:6: lv_inner_2_0= ruleExpression
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

                    otherlv_3=(Token)match(input,23,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:2907:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQL.g:2907:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQL.g:2908:4: () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQL.g:2908:4: ()
                    // InternalCQL.g:2909:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,61,FOLLOW_13); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQL.g:2919:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQL.g:2920:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQL.g:2920:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQL.g:2921:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQL.g:2940:3: this_Atomic_7= ruleAtomic
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
    // InternalCQL.g:2952:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQL.g:2952:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQL.g:2953:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQL.g:2959:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
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
            // InternalCQL.g:2965:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQL.g:2966:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQL.g:2966:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt49=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt49=1;
                }
                break;
            case RULE_FLOAT:
                {
                alt49=2;
                }
                break;
            case RULE_STRING:
                {
                alt49=3;
                }
                break;
            case 62:
            case 63:
                {
                alt49=4;
                }
                break;
            case RULE_ID:
                {
                alt49=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;
            }

            switch (alt49) {
                case 1 :
                    // InternalCQL.g:2967:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:2967:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:2968:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:2968:4: ()
                    // InternalCQL.g:2969:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2975:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:2976:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:2976:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:2977:6: lv_value_1_0= RULE_INT
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
                    // InternalCQL.g:2995:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQL.g:2995:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQL.g:2996:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQL.g:2996:4: ()
                    // InternalCQL.g:2997:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:3003:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQL.g:3004:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQL.g:3004:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQL.g:3005:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQL.g:3023:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:3023:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:3024:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:3024:4: ()
                    // InternalCQL.g:3025:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:3031:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:3032:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:3032:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:3033:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQL.g:3051:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:3051:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:3052:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:3052:4: ()
                    // InternalCQL.g:3053:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:3059:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:3060:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:3060:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:3061:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:3061:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt47=2;
                    int LA47_0 = input.LA(1);

                    if ( (LA47_0==62) ) {
                        alt47=1;
                    }
                    else if ( (LA47_0==63) ) {
                        alt47=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 47, 0, input);

                        throw nvae;
                    }
                    switch (alt47) {
                        case 1 :
                            // InternalCQL.g:3062:7: lv_value_7_1= 'TRUE'
                            {
                            lv_value_7_1=(Token)match(input,62,FOLLOW_2); 

                            							newLeafNode(lv_value_7_1, grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getAtomicRule());
                            							}
                            							setWithLastConsumed(current, "value", lv_value_7_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalCQL.g:3073:7: lv_value_7_2= 'FALSE'
                            {
                            lv_value_7_2=(Token)match(input,63,FOLLOW_2); 

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
                    // InternalCQL.g:3088:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQL.g:3088:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQL.g:3089:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQL.g:3089:4: ()
                    // InternalCQL.g:3090:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:3096:4: ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt48=2;
                    int LA48_0 = input.LA(1);

                    if ( (LA48_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
                        case 27:
                            {
                            int LA48_2 = input.LA(3);

                            if ( (LA48_2==RULE_ID) ) {
                                int LA48_5 = input.LA(4);

                                if ( (LA48_5==28) ) {
                                    alt48=2;
                                }
                                else if ( (LA48_5==EOF||(LA48_5>=12 && LA48_5<=13)||LA48_5==15||LA48_5==19||LA48_5==21||LA48_5==23||(LA48_5>=34 && LA48_5<=35)||LA48_5==40||LA48_5==42||(LA48_5>=50 && LA48_5<=60)) ) {
                                    alt48=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 48, 5, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 48, 2, input);

                                throw nvae;
                            }
                            }
                            break;
                        case 28:
                            {
                            alt48=2;
                            }
                            break;
                        case EOF:
                        case 12:
                        case 13:
                        case 15:
                        case 19:
                        case 21:
                        case 23:
                        case 34:
                        case 35:
                        case 40:
                        case 42:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                            {
                            alt48=1;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 48, 1, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 48, 0, input);

                        throw nvae;
                    }
                    switch (alt48) {
                        case 1 :
                            // InternalCQL.g:3097:5: ( (lv_value_9_0= ruleAttributeWithoutAlias ) )
                            {
                            // InternalCQL.g:3097:5: ( (lv_value_9_0= ruleAttributeWithoutAlias ) )
                            // InternalCQL.g:3098:6: (lv_value_9_0= ruleAttributeWithoutAlias )
                            {
                            // InternalCQL.g:3098:6: (lv_value_9_0= ruleAttributeWithoutAlias )
                            // InternalCQL.g:3099:7: lv_value_9_0= ruleAttributeWithoutAlias
                            {

                            							newCompositeNode(grammarAccess.getAtomicAccess().getValueAttributeWithoutAliasParserRuleCall_4_1_0_0());
                            						
                            pushFollow(FOLLOW_2);
                            lv_value_9_0=ruleAttributeWithoutAlias();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getAtomicRule());
                            							}
                            							set(
                            								current,
                            								"value",
                            								lv_value_9_0,
                            								"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AttributeWithoutAlias");
                            							afterParserOrEnumRuleCall();
                            						

                            }


                            }


                            }
                            break;
                        case 2 :
                            // InternalCQL.g:3117:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQL.g:3117:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQL.g:3118:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQL.g:3118:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQL.g:3119:7: lv_value_10_0= ruleAttributeWithNestedStatement
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
    // InternalCQL.g:3142:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQL.g:3142:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQL.g:3143:2: iv_ruleDataType= ruleDataType EOF
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
    // InternalCQL.g:3149:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) ) ;
    public final EObject ruleDataType() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_1=null;
        Token lv_value_0_2=null;
        Token lv_value_0_3=null;
        Token lv_value_0_4=null;
        Token lv_value_0_5=null;
        Token lv_value_0_6=null;
        Token lv_value_0_7=null;


        	enterRule();

        try {
            // InternalCQL.g:3155:2: ( ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) ) )
            // InternalCQL.g:3156:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) )
            {
            // InternalCQL.g:3156:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) )
            // InternalCQL.g:3157:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) )
            {
            // InternalCQL.g:3157:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) )
            // InternalCQL.g:3158:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' )
            {
            // InternalCQL.g:3158:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' )
            int alt50=7;
            switch ( input.LA(1) ) {
            case 64:
                {
                alt50=1;
                }
                break;
            case 65:
                {
                alt50=2;
                }
                break;
            case 66:
                {
                alt50=3;
                }
                break;
            case 67:
                {
                alt50=4;
                }
                break;
            case 68:
                {
                alt50=5;
                }
                break;
            case 69:
                {
                alt50=6;
                }
                break;
            case 70:
                {
                alt50=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }

            switch (alt50) {
                case 1 :
                    // InternalCQL.g:3159:5: lv_value_0_1= 'INTEGER'
                    {
                    lv_value_0_1=(Token)match(input,64,FOLLOW_2); 

                    					newLeafNode(lv_value_0_1, grammarAccess.getDataTypeAccess().getValueINTEGERKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_1, null);
                    				

                    }
                    break;
                case 2 :
                    // InternalCQL.g:3170:5: lv_value_0_2= 'DOUBLE'
                    {
                    lv_value_0_2=(Token)match(input,65,FOLLOW_2); 

                    					newLeafNode(lv_value_0_2, grammarAccess.getDataTypeAccess().getValueDOUBLEKeyword_0_1());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_2, null);
                    				

                    }
                    break;
                case 3 :
                    // InternalCQL.g:3181:5: lv_value_0_3= 'FLOAT'
                    {
                    lv_value_0_3=(Token)match(input,66,FOLLOW_2); 

                    					newLeafNode(lv_value_0_3, grammarAccess.getDataTypeAccess().getValueFLOATKeyword_0_2());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_3, null);
                    				

                    }
                    break;
                case 4 :
                    // InternalCQL.g:3192:5: lv_value_0_4= 'STRING'
                    {
                    lv_value_0_4=(Token)match(input,67,FOLLOW_2); 

                    					newLeafNode(lv_value_0_4, grammarAccess.getDataTypeAccess().getValueSTRINGKeyword_0_3());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_4, null);
                    				

                    }
                    break;
                case 5 :
                    // InternalCQL.g:3203:5: lv_value_0_5= 'BOOLEAN'
                    {
                    lv_value_0_5=(Token)match(input,68,FOLLOW_2); 

                    					newLeafNode(lv_value_0_5, grammarAccess.getDataTypeAccess().getValueBOOLEANKeyword_0_4());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_5, null);
                    				

                    }
                    break;
                case 6 :
                    // InternalCQL.g:3214:5: lv_value_0_6= 'STARTTIMESTAMP'
                    {
                    lv_value_0_6=(Token)match(input,69,FOLLOW_2); 

                    					newLeafNode(lv_value_0_6, grammarAccess.getDataTypeAccess().getValueSTARTTIMESTAMPKeyword_0_5());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_6, null);
                    				

                    }
                    break;
                case 7 :
                    // InternalCQL.g:3225:5: lv_value_0_7= 'ENDTIMESTAMP'
                    {
                    lv_value_0_7=(Token)match(input,70,FOLLOW_2); 

                    					newLeafNode(lv_value_0_7, grammarAccess.getDataTypeAccess().getValueENDTIMESTAMPKeyword_0_6());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getDataTypeRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_7, null);
                    				

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

    // Delegated rules


    protected DFA2 dfa2 = new DFA2(this);
    static final String dfa_1s = "\47\uffff";
    static final String dfa_2s = "\1\15\3\uffff\1\43\1\uffff\1\4\1\uffff\1\26\4\4\7\20\3\4\1\35\1\32\3\uffff\1\4\7\20\1\4\1\100\1\32";
    static final String dfa_3s = "\1\52\3\uffff\1\44\1\uffff\1\4\1\uffff\1\26\1\4\1\106\2\4\11\106\1\4\1\47\1\106\3\uffff\1\4\7\27\1\4\2\106";
    static final String dfa_4s = "\1\uffff\1\1\1\2\1\3\1\uffff\1\10\1\uffff\1\5\21\uffff\1\7\1\6\1\4\13\uffff";
    static final String dfa_5s = "\47\uffff}>";
    static final String[] dfa_6s = {
            "\1\1\24\uffff\1\4\1\2\4\uffff\1\5\1\uffff\1\3",
            "",
            "",
            "",
            "\1\6\1\7",
            "",
            "\1\10",
            "",
            "\1\11",
            "\1\12",
            "\1\12\25\uffff\1\14\1\13\44\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\24",
            "\1\25",
            "\1\26\6\uffff\1\27\50\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\26\6\uffff\1\27\50\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\26\6\uffff\1\27\50\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\26\6\uffff\1\27\50\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\26\6\uffff\1\27\50\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\26\6\uffff\1\27\50\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\26\6\uffff\1\27\50\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\12\25\uffff\1\14\45\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\12\73\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23",
            "\1\30",
            "\1\33\7\uffff\1\32\1\uffff\1\31",
            "\1\34\1\44\44\uffff\1\35\1\36\1\37\1\40\1\41\1\42\1\43",
            "",
            "",
            "",
            "\1\45",
            "\1\26\6\uffff\1\27",
            "\1\26\6\uffff\1\27",
            "\1\26\6\uffff\1\27",
            "\1\26\6\uffff\1\27",
            "\1\26\6\uffff\1\27",
            "\1\26\6\uffff\1\27",
            "\1\26\6\uffff\1\27",
            "\1\46",
            "\1\35\1\36\1\37\1\40\1\41\1\42\1\43",
            "\1\34\45\uffff\1\35\1\36\1\37\1\40\1\41\1\42\1\43"
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
            return "115:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000050C00002002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x000000000001C010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000018010L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000030010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000400010L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x00000000006D0012L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x00000000002D0002L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0xE0000000004000F0L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000280002L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000210012L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000210002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000005000002L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000300000000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000810020L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000010L,0x000000000000007FL});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000810000L,0x000000000000007FL});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007FL});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000000810000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000000000002010L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000001800000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000080000000010L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000C00000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0001400000000000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0030000000000002L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x03C0000000000002L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0C00000000000002L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x1000000000008002L});

}
