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
    // InternalCQL.g:108:1: ruleStatement returns [EObject current=null] : ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )? ) ;
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
            // InternalCQL.g:114:2: ( ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )? ) )
            // InternalCQL.g:115:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )? )
            {
            // InternalCQL.g:115:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )? )
            // InternalCQL.g:116:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleStreamTo ) ) | ( (lv_type_2_0= ruleDrop ) ) | ( (lv_type_3_0= ruleCreateStream1 ) ) | ( (lv_type_4_0= ruleCreateSink1 ) ) | ( (lv_type_5_0= ruleCreateStreamChannel ) ) | ( (lv_type_6_0= ruleCreateStreamFile ) ) | ( (lv_type_7_0= ruleCreateView ) ) ) (otherlv_8= ';' )?
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

            // InternalCQL.g:277:3: (otherlv_8= ';' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==12) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalCQL.g:278:4: otherlv_8= ';'
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
    // InternalCQL.g:287:1: entryRuleSelect returns [EObject current=null] : iv_ruleSelect= ruleSelect EOF ;
    public final EObject entryRuleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect = null;


        try {
            // InternalCQL.g:287:47: (iv_ruleSelect= ruleSelect EOF )
            // InternalCQL.g:288:2: iv_ruleSelect= ruleSelect EOF
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
    // InternalCQL.g:294:1: ruleSelect returns [EObject current=null] : ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )? ) ;
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
            // InternalCQL.g:300:2: ( ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQL.g:301:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQL.g:301:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )? )
            // InternalCQL.g:302:3: ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) ) (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* ) (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )? (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )? (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )?
            {
            // InternalCQL.g:302:3: ( (lv_name_0_0= 'SELECT' ) )
            // InternalCQL.g:303:4: (lv_name_0_0= 'SELECT' )
            {
            // InternalCQL.g:303:4: (lv_name_0_0= 'SELECT' )
            // InternalCQL.g:304:5: lv_name_0_0= 'SELECT'
            {
            lv_name_0_0=(Token)match(input,13,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelectAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelectRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCQL.g:316:3: ( (lv_distinct_1_0= 'DISTINCT' ) )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==14) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalCQL.g:317:4: (lv_distinct_1_0= 'DISTINCT' )
                    {
                    // InternalCQL.g:317:4: (lv_distinct_1_0= 'DISTINCT' )
                    // InternalCQL.g:318:5: lv_distinct_1_0= 'DISTINCT'
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

            // InternalCQL.g:330:3: (otherlv_2= '*' | ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==15) ) {
                alt7=1;
            }
            else if ( ((LA7_0>=RULE_ID && LA7_0<=RULE_FLOAT)||(LA7_0>=29 && LA7_0<=37)||(LA7_0>=70 && LA7_0<=71)) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // InternalCQL.g:331:4: otherlv_2= '*'
                    {
                    otherlv_2=(Token)match(input,15,FOLLOW_6); 

                    				newLeafNode(otherlv_2, grammarAccess.getSelectAccess().getAsteriskKeyword_2_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQL.g:336:4: ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* )
                    {
                    // InternalCQL.g:336:4: ( ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )* )
                    // InternalCQL.g:337:5: ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+ ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )*
                    {
                    // InternalCQL.g:337:5: ( ( (lv_attributes_3_0= ruleAttribute ) ) | ( (lv_aggregations_4_0= ruleAggregation ) ) | ( (lv_expressions_5_0= ruleSelectExpression ) ) )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=4;
                        switch ( input.LA(1) ) {
                        case RULE_ID:
                            {
                            switch ( input.LA(2) ) {
                            case 15:
                            case 38:
                            case 39:
                            case 40:
                                {
                                alt5=3;
                                }
                                break;
                            case 27:
                                {
                                int LA5_5 = input.LA(3);

                                if ( (LA5_5==RULE_ID) ) {
                                    int LA5_7 = input.LA(4);

                                    if ( (LA5_7==15||(LA5_7>=38 && LA5_7<=40)) ) {
                                        alt5=3;
                                    }
                                    else if ( ((LA5_7>=RULE_ID && LA5_7<=RULE_FLOAT)||(LA5_7>=16 && LA5_7<=17)||LA5_7==26||(LA5_7>=29 && LA5_7<=37)||(LA5_7>=70 && LA5_7<=71)) ) {
                                        alt5=1;
                                    }


                                }


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
                                alt5=1;
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

                        }

                        switch (alt5) {
                    	case 1 :
                    	    // InternalCQL.g:338:6: ( (lv_attributes_3_0= ruleAttribute ) )
                    	    {
                    	    // InternalCQL.g:338:6: ( (lv_attributes_3_0= ruleAttribute ) )
                    	    // InternalCQL.g:339:7: (lv_attributes_3_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:339:7: (lv_attributes_3_0= ruleAttribute )
                    	    // InternalCQL.g:340:8: lv_attributes_3_0= ruleAttribute
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
                    	    // InternalCQL.g:358:6: ( (lv_aggregations_4_0= ruleAggregation ) )
                    	    {
                    	    // InternalCQL.g:358:6: ( (lv_aggregations_4_0= ruleAggregation ) )
                    	    // InternalCQL.g:359:7: (lv_aggregations_4_0= ruleAggregation )
                    	    {
                    	    // InternalCQL.g:359:7: (lv_aggregations_4_0= ruleAggregation )
                    	    // InternalCQL.g:360:8: lv_aggregations_4_0= ruleAggregation
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
                    	    // InternalCQL.g:378:6: ( (lv_expressions_5_0= ruleSelectExpression ) )
                    	    {
                    	    // InternalCQL.g:378:6: ( (lv_expressions_5_0= ruleSelectExpression ) )
                    	    // InternalCQL.g:379:7: (lv_expressions_5_0= ruleSelectExpression )
                    	    {
                    	    // InternalCQL.g:379:7: (lv_expressions_5_0= ruleSelectExpression )
                    	    // InternalCQL.g:380:8: lv_expressions_5_0= ruleSelectExpression
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

                    // InternalCQL.g:398:5: ( (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) | (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) | (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) ) )*
                    loop6:
                    do {
                        int alt6=4;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==16) ) {
                            switch ( input.LA(2) ) {
                            case RULE_STRING:
                            case RULE_INT:
                            case RULE_FLOAT:
                            case 37:
                            case 70:
                            case 71:
                                {
                                alt6=3;
                                }
                                break;
                            case RULE_ID:
                                {
                                switch ( input.LA(3) ) {
                                case 15:
                                case 38:
                                case 39:
                                case 40:
                                    {
                                    alt6=3;
                                    }
                                    break;
                                case 27:
                                    {
                                    int LA6_6 = input.LA(4);

                                    if ( (LA6_6==RULE_ID) ) {
                                        int LA6_8 = input.LA(5);

                                        if ( ((LA6_8>=16 && LA6_8<=17)||LA6_8==26) ) {
                                            alt6=1;
                                        }
                                        else if ( (LA6_8==15||(LA6_8>=38 && LA6_8<=40)) ) {
                                            alt6=3;
                                        }


                                    }


                                    }
                                    break;
                                case 16:
                                case 17:
                                case 26:
                                    {
                                    alt6=1;
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
                                alt6=2;
                                }
                                break;

                            }

                        }


                        switch (alt6) {
                    	case 1 :
                    	    // InternalCQL.g:399:6: (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    {
                    	    // InternalCQL.g:399:6: (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    // InternalCQL.g:400:7: otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) )
                    	    {
                    	    otherlv_6=(Token)match(input,16,FOLLOW_8); 

                    	    							newLeafNode(otherlv_6, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_0_0());
                    	    						
                    	    // InternalCQL.g:404:7: ( (lv_attributes_7_0= ruleAttribute ) )
                    	    // InternalCQL.g:405:8: (lv_attributes_7_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:405:8: (lv_attributes_7_0= ruleAttribute )
                    	    // InternalCQL.g:406:9: lv_attributes_7_0= ruleAttribute
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
                    	    // InternalCQL.g:425:6: (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    {
                    	    // InternalCQL.g:425:6: (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    // InternalCQL.g:426:7: otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    {
                    	    otherlv_8=(Token)match(input,16,FOLLOW_10); 

                    	    							newLeafNode(otherlv_8, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_1_0());
                    	    						
                    	    // InternalCQL.g:430:7: ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    // InternalCQL.g:431:8: (lv_aggregations_9_0= ruleAggregation )
                    	    {
                    	    // InternalCQL.g:431:8: (lv_aggregations_9_0= ruleAggregation )
                    	    // InternalCQL.g:432:9: lv_aggregations_9_0= ruleAggregation
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
                    	    // InternalCQL.g:451:6: (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) )
                    	    {
                    	    // InternalCQL.g:451:6: (otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) ) )
                    	    // InternalCQL.g:452:7: otherlv_10= ',' ( (lv_expressions_11_0= ruleSelectExpression ) )
                    	    {
                    	    otherlv_10=(Token)match(input,16,FOLLOW_5); 

                    	    							newLeafNode(otherlv_10, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_2_0());
                    	    						
                    	    // InternalCQL.g:456:7: ( (lv_expressions_11_0= ruleSelectExpression ) )
                    	    // InternalCQL.g:457:8: (lv_expressions_11_0= ruleSelectExpression )
                    	    {
                    	    // InternalCQL.g:457:8: (lv_expressions_11_0= ruleSelectExpression )
                    	    // InternalCQL.g:458:9: lv_expressions_11_0= ruleSelectExpression
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

            // InternalCQL.g:479:3: (otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )* )
            // InternalCQL.g:480:4: otherlv_12= 'FROM' ( (lv_sources_13_0= ruleSource ) )+ (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )*
            {
            otherlv_12=(Token)match(input,17,FOLLOW_11); 

            				newLeafNode(otherlv_12, grammarAccess.getSelectAccess().getFROMKeyword_3_0());
            			
            // InternalCQL.g:484:4: ( (lv_sources_13_0= ruleSource ) )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==RULE_ID||LA8_0==22) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCQL.g:485:5: (lv_sources_13_0= ruleSource )
            	    {
            	    // InternalCQL.g:485:5: (lv_sources_13_0= ruleSource )
            	    // InternalCQL.g:486:6: lv_sources_13_0= ruleSource
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

            // InternalCQL.g:503:4: (otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==16) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalCQL.g:504:5: otherlv_14= ',' ( (lv_sources_15_0= ruleSource ) )
            	    {
            	    otherlv_14=(Token)match(input,16,FOLLOW_11); 

            	    					newLeafNode(otherlv_14, grammarAccess.getSelectAccess().getCommaKeyword_3_2_0());
            	    				
            	    // InternalCQL.g:508:5: ( (lv_sources_15_0= ruleSource ) )
            	    // InternalCQL.g:509:6: (lv_sources_15_0= ruleSource )
            	    {
            	    // InternalCQL.g:509:6: (lv_sources_15_0= ruleSource )
            	    // InternalCQL.g:510:7: lv_sources_15_0= ruleSource
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

            // InternalCQL.g:529:3: (otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==18) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQL.g:530:4: otherlv_16= 'WHERE' ( (lv_predicates_17_0= ruleExpressionsModel ) )
                    {
                    otherlv_16=(Token)match(input,18,FOLLOW_14); 

                    				newLeafNode(otherlv_16, grammarAccess.getSelectAccess().getWHEREKeyword_4_0());
                    			
                    // InternalCQL.g:534:4: ( (lv_predicates_17_0= ruleExpressionsModel ) )
                    // InternalCQL.g:535:5: (lv_predicates_17_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:535:5: (lv_predicates_17_0= ruleExpressionsModel )
                    // InternalCQL.g:536:6: lv_predicates_17_0= ruleExpressionsModel
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

            // InternalCQL.g:554:3: (otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )* )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==19) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQL.g:555:4: otherlv_18= 'GROUP' otherlv_19= 'BY' ( (lv_order_20_0= ruleAttribute ) )+ (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )*
                    {
                    otherlv_18=(Token)match(input,19,FOLLOW_16); 

                    				newLeafNode(otherlv_18, grammarAccess.getSelectAccess().getGROUPKeyword_5_0());
                    			
                    otherlv_19=(Token)match(input,20,FOLLOW_8); 

                    				newLeafNode(otherlv_19, grammarAccess.getSelectAccess().getBYKeyword_5_1());
                    			
                    // InternalCQL.g:563:4: ( (lv_order_20_0= ruleAttribute ) )+
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
                    	    // InternalCQL.g:564:5: (lv_order_20_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:564:5: (lv_order_20_0= ruleAttribute )
                    	    // InternalCQL.g:565:6: lv_order_20_0= ruleAttribute
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

                    // InternalCQL.g:582:4: (otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==16) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // InternalCQL.g:583:5: otherlv_21= ',' ( (lv_order_22_0= ruleAttribute ) )
                    	    {
                    	    otherlv_21=(Token)match(input,16,FOLLOW_8); 

                    	    					newLeafNode(otherlv_21, grammarAccess.getSelectAccess().getCommaKeyword_5_3_0());
                    	    				
                    	    // InternalCQL.g:587:5: ( (lv_order_22_0= ruleAttribute ) )
                    	    // InternalCQL.g:588:6: (lv_order_22_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:588:6: (lv_order_22_0= ruleAttribute )
                    	    // InternalCQL.g:589:7: lv_order_22_0= ruleAttribute
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

            // InternalCQL.g:608:3: (otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==21) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // InternalCQL.g:609:4: otherlv_23= 'HAVING' ( (lv_having_24_0= ruleExpressionsModel ) )
                    {
                    otherlv_23=(Token)match(input,21,FOLLOW_14); 

                    				newLeafNode(otherlv_23, grammarAccess.getSelectAccess().getHAVINGKeyword_6_0());
                    			
                    // InternalCQL.g:613:4: ( (lv_having_24_0= ruleExpressionsModel ) )
                    // InternalCQL.g:614:5: (lv_having_24_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:614:5: (lv_having_24_0= ruleExpressionsModel )
                    // InternalCQL.g:615:6: lv_having_24_0= ruleExpressionsModel
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
    // InternalCQL.g:637:1: entryRuleNestedStatement returns [EObject current=null] : iv_ruleNestedStatement= ruleNestedStatement EOF ;
    public final EObject entryRuleNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedStatement = null;


        try {
            // InternalCQL.g:637:56: (iv_ruleNestedStatement= ruleNestedStatement EOF )
            // InternalCQL.g:638:2: iv_ruleNestedStatement= ruleNestedStatement EOF
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
    // InternalCQL.g:644:1: ruleNestedStatement returns [EObject current=null] : (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) ;
    public final EObject ruleNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:650:2: ( (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) )
            // InternalCQL.g:651:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            {
            // InternalCQL.g:651:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            // InternalCQL.g:652:3: otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')'
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
    // InternalCQL.g:672:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQL.g:672:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQL.g:673:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQL.g:679:1: ruleSource returns [EObject current=null] : ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) ) ;
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
            // InternalCQL.g:685:2: ( ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) ) )
            // InternalCQL.g:686:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) )
            {
            // InternalCQL.g:686:2: ( ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) | ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) ) )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==RULE_ID) ) {
                alt18=1;
            }
            else if ( (LA18_0==22) ) {
                alt18=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }
            switch (alt18) {
                case 1 :
                    // InternalCQL.g:687:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
                    {
                    // InternalCQL.g:687:3: ( ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
                    // InternalCQL.g:688:4: ( (lv_name_0_0= ruleSourceName ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
                    {
                    // InternalCQL.g:688:4: ( (lv_name_0_0= ruleSourceName ) )
                    // InternalCQL.g:689:5: (lv_name_0_0= ruleSourceName )
                    {
                    // InternalCQL.g:689:5: (lv_name_0_0= ruleSourceName )
                    // InternalCQL.g:690:6: lv_name_0_0= ruleSourceName
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

                    // InternalCQL.g:707:4: (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==24) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // InternalCQL.g:708:5: otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']'
                            {
                            otherlv_1=(Token)match(input,24,FOLLOW_22); 

                            					newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_1_0());
                            				
                            // InternalCQL.g:712:5: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                            int alt15=3;
                            int LA15_0 = input.LA(1);

                            if ( (LA15_0==55) ) {
                                alt15=1;
                            }
                            else if ( (LA15_0==56) ) {
                                int LA15_2 = input.LA(2);

                                if ( (LA15_2==RULE_INT) ) {
                                    int LA15_3 = input.LA(3);

                                    if ( (LA15_3==RULE_ID) ) {
                                        alt15=2;
                                    }
                                    else if ( (LA15_3==57||LA15_3==59) ) {
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
                                    // InternalCQL.g:713:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQL.g:713:6: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQL.g:714:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQL.g:714:7: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQL.g:715:8: lv_unbounded_2_0= ruleWindow_Unbounded
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
                                    // InternalCQL.g:733:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQL.g:733:6: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQL.g:734:7: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQL.g:734:7: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQL.g:735:8: lv_time_3_0= ruleWindow_Timebased
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
                                    // InternalCQL.g:753:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQL.g:753:6: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQL.g:754:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQL.g:754:7: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQL.g:755:8: lv_tuple_4_0= ruleWindow_Tuplebased
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

                    // InternalCQL.g:778:4: (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==26) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // InternalCQL.g:779:5: otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) )
                            {
                            otherlv_6=(Token)match(input,26,FOLLOW_8); 

                            					newLeafNode(otherlv_6, grammarAccess.getSourceAccess().getASKeyword_0_2_0());
                            				
                            // InternalCQL.g:783:5: ( (lv_alias_7_0= ruleAlias ) )
                            // InternalCQL.g:784:6: (lv_alias_7_0= ruleAlias )
                            {
                            // InternalCQL.g:784:6: (lv_alias_7_0= ruleAlias )
                            // InternalCQL.g:785:7: lv_alias_7_0= ruleAlias
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
                    // InternalCQL.g:805:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) )
                    {
                    // InternalCQL.g:805:3: ( ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) ) )
                    // InternalCQL.g:806:4: ( (lv_nested_8_0= ruleNestedStatement ) ) otherlv_9= 'AS' ( (lv_alias_10_0= ruleAlias ) )
                    {
                    // InternalCQL.g:806:4: ( (lv_nested_8_0= ruleNestedStatement ) )
                    // InternalCQL.g:807:5: (lv_nested_8_0= ruleNestedStatement )
                    {
                    // InternalCQL.g:807:5: (lv_nested_8_0= ruleNestedStatement )
                    // InternalCQL.g:808:6: lv_nested_8_0= ruleNestedStatement
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
                    			
                    // InternalCQL.g:829:4: ( (lv_alias_10_0= ruleAlias ) )
                    // InternalCQL.g:830:5: (lv_alias_10_0= ruleAlias )
                    {
                    // InternalCQL.g:830:5: (lv_alias_10_0= ruleAlias )
                    // InternalCQL.g:831:6: lv_alias_10_0= ruleAlias
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
    // InternalCQL.g:853:1: entryRuleSourceName returns [String current=null] : iv_ruleSourceName= ruleSourceName EOF ;
    public final String entryRuleSourceName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSourceName = null;


        try {
            // InternalCQL.g:853:50: (iv_ruleSourceName= ruleSourceName EOF )
            // InternalCQL.g:854:2: iv_ruleSourceName= ruleSourceName EOF
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
    // InternalCQL.g:860:1: ruleSourceName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleSourceName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalCQL.g:866:2: (this_ID_0= RULE_ID )
            // InternalCQL.g:867:2: this_ID_0= RULE_ID
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
    // InternalCQL.g:877:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQL.g:877:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQL.g:878:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQL.g:884:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;

        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:890:2: ( ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:891:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:891:2: ( ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQL.g:892:3: ( (lv_name_0_0= ruleAttributeName ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:892:3: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQL.g:893:4: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQL.g:893:4: (lv_name_0_0= ruleAttributeName )
            // InternalCQL.g:894:5: lv_name_0_0= ruleAttributeName
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

            // InternalCQL.g:911:3: (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==26) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQL.g:912:4: otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:916:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQL.g:917:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQL.g:917:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQL.g:918:6: lv_alias_2_0= ruleAlias
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
    // InternalCQL.g:940:1: entryRuleAttributeWithoutAliasDefinition returns [EObject current=null] : iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF ;
    public final EObject entryRuleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAliasDefinition = null;


        try {
            // InternalCQL.g:940:72: (iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF )
            // InternalCQL.g:941:2: iv_ruleAttributeWithoutAliasDefinition= ruleAttributeWithoutAliasDefinition EOF
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
    // InternalCQL.g:947:1: ruleAttributeWithoutAliasDefinition returns [EObject current=null] : ( (lv_name_0_0= ruleAttributeName ) ) ;
    public final EObject ruleAttributeWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:953:2: ( ( (lv_name_0_0= ruleAttributeName ) ) )
            // InternalCQL.g:954:2: ( (lv_name_0_0= ruleAttributeName ) )
            {
            // InternalCQL.g:954:2: ( (lv_name_0_0= ruleAttributeName ) )
            // InternalCQL.g:955:3: (lv_name_0_0= ruleAttributeName )
            {
            // InternalCQL.g:955:3: (lv_name_0_0= ruleAttributeName )
            // InternalCQL.g:956:4: lv_name_0_0= ruleAttributeName
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
    // InternalCQL.g:976:1: entryRuleAttributeName returns [String current=null] : iv_ruleAttributeName= ruleAttributeName EOF ;
    public final String entryRuleAttributeName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAttributeName = null;


        try {
            // InternalCQL.g:976:53: (iv_ruleAttributeName= ruleAttributeName EOF )
            // InternalCQL.g:977:2: iv_ruleAttributeName= ruleAttributeName EOF
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
    // InternalCQL.g:983:1: ruleAttributeName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) ) ;
    public final AntlrDatatypeRuleToken ruleAttributeName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_3=null;
        AntlrDatatypeRuleToken this_SourceName_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:989:2: ( (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) ) )
            // InternalCQL.g:990:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) )
            {
            // InternalCQL.g:990:2: (this_ID_0= RULE_ID | (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID ) )
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==RULE_ID) ) {
                int LA20_1 = input.LA(2);

                if ( (LA20_1==EOF||(LA20_1>=RULE_ID && LA20_1<=RULE_FLOAT)||(LA20_1>=12 && LA20_1<=13)||(LA20_1>=15 && LA20_1<=17)||LA20_1==19||LA20_1==21||LA20_1==23||(LA20_1>=25 && LA20_1<=26)||(LA20_1>=28 && LA20_1<=40)||LA20_1==46||LA20_1==51||LA20_1==53||(LA20_1>=61 && LA20_1<=68)||(LA20_1>=70 && LA20_1<=81)) ) {
                    alt20=1;
                }
                else if ( (LA20_1==27) ) {
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
                    // InternalCQL.g:991:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getAttributeNameAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCQL.g:999:3: (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID )
                    {
                    // InternalCQL.g:999:3: (this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID )
                    // InternalCQL.g:1000:4: this_SourceName_1= ruleSourceName kw= '.' this_ID_3= RULE_ID
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
    // InternalCQL.g:1027:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQL.g:1027:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQL.g:1028:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
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
    // InternalCQL.g:1034:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1040:2: ( ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) )
            // InternalCQL.g:1041:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            {
            // InternalCQL.g:1041:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            // InternalCQL.g:1042:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) )
            {
            // InternalCQL.g:1042:3: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:1043:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:1043:4: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:1044:5: lv_value_0_0= ruleAttributeWithoutAliasDefinition
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
            		
            // InternalCQL.g:1065:3: ( (lv_nested_2_0= ruleNestedStatement ) )
            // InternalCQL.g:1066:4: (lv_nested_2_0= ruleNestedStatement )
            {
            // InternalCQL.g:1066:4: (lv_nested_2_0= ruleNestedStatement )
            // InternalCQL.g:1067:5: lv_nested_2_0= ruleNestedStatement
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
    // InternalCQL.g:1088:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalCQL.g:1088:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalCQL.g:1089:2: iv_ruleAggregation= ruleAggregation EOF
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
    // InternalCQL.g:1095:1: ruleAggregation returns [EObject current=null] : ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )? ) ;
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
            // InternalCQL.g:1101:2: ( ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:1102:2: ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:1102:2: ( ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )? )
            // InternalCQL.g:1103:3: ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) ) otherlv_1= '(' ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) ) otherlv_4= ')' (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:1103:3: ( ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) ) )
            // InternalCQL.g:1104:4: ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) )
            {
            // InternalCQL.g:1104:4: ( (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' ) )
            // InternalCQL.g:1105:5: (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' )
            {
            // InternalCQL.g:1105:5: (lv_name_0_1= 'AVG' | lv_name_0_2= 'MIN' | lv_name_0_3= 'MAX' | lv_name_0_4= 'COUNT' | lv_name_0_5= 'SUM' | lv_name_0_6= 'MEDIAN' | lv_name_0_7= 'FIRST' | lv_name_0_8= 'LAST' )
            int alt21=8;
            switch ( input.LA(1) ) {
            case 29:
                {
                alt21=1;
                }
                break;
            case 30:
                {
                alt21=2;
                }
                break;
            case 31:
                {
                alt21=3;
                }
                break;
            case 32:
                {
                alt21=4;
                }
                break;
            case 33:
                {
                alt21=5;
                }
                break;
            case 34:
                {
                alt21=6;
                }
                break;
            case 35:
                {
                alt21=7;
                }
                break;
            case 36:
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
                    // InternalCQL.g:1106:6: lv_name_0_1= 'AVG'
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
                    // InternalCQL.g:1117:6: lv_name_0_2= 'MIN'
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
                    // InternalCQL.g:1128:6: lv_name_0_3= 'MAX'
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
                    // InternalCQL.g:1139:6: lv_name_0_4= 'COUNT'
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
                    // InternalCQL.g:1150:6: lv_name_0_5= 'SUM'
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
                    // InternalCQL.g:1161:6: lv_name_0_6= 'MEDIAN'
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
                    // InternalCQL.g:1172:6: lv_name_0_7= 'FIRST'
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
                    // InternalCQL.g:1183:6: lv_name_0_8= 'LAST'
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

            otherlv_1=(Token)match(input,22,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregationAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:1200:3: ( ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case RULE_ID:
                case RULE_STRING:
                case RULE_INT:
                case RULE_FLOAT:
                case 15:
                case 37:
                case 38:
                case 39:
                case 40:
                case 70:
                case 71:
                    {
                    alt22=2;
                    }
                    break;
                case 23:
                    {
                    alt22=1;
                    }
                    break;
                case 27:
                    {
                    int LA22_4 = input.LA(3);

                    if ( (LA22_4==RULE_ID) ) {
                        int LA22_5 = input.LA(4);

                        if ( ((LA22_5>=RULE_ID && LA22_5<=RULE_FLOAT)||LA22_5==15||(LA22_5>=37 && LA22_5<=40)||(LA22_5>=70 && LA22_5<=71)) ) {
                            alt22=2;
                        }
                        else if ( (LA22_5==23) ) {
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
            else if ( ((LA22_0>=RULE_STRING && LA22_0<=RULE_FLOAT)||(LA22_0>=70 && LA22_0<=71)) ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // InternalCQL.g:1201:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1201:4: ( (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1202:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1202:5: (lv_attribute_2_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1203:6: lv_attribute_2_0= ruleAttributeWithoutAliasDefinition
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
                    // InternalCQL.g:1221:4: ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1221:4: ( (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
                    // InternalCQL.g:1222:5: (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1222:5: (lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition )
                    // InternalCQL.g:1223:6: lv_expression_3_0= ruleSelectExpressionWithoutAliasDefinition
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

            otherlv_4=(Token)match(input,23,FOLLOW_24); 

            			newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getRightParenthesisKeyword_3());
            		
            // InternalCQL.g:1245:3: (otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) ) )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==26) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalCQL.g:1246:4: otherlv_5= 'AS' ( (lv_alias_6_0= ruleAlias ) )
                    {
                    otherlv_5=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_5, grammarAccess.getAggregationAccess().getASKeyword_4_0());
                    			
                    // InternalCQL.g:1250:4: ( (lv_alias_6_0= ruleAlias ) )
                    // InternalCQL.g:1251:5: (lv_alias_6_0= ruleAlias )
                    {
                    // InternalCQL.g:1251:5: (lv_alias_6_0= ruleAlias )
                    // InternalCQL.g:1252:6: lv_alias_6_0= ruleAlias
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
    // InternalCQL.g:1274:1: entryRuleExpressionComponentConstantOrAttribute returns [EObject current=null] : iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF ;
    public final EObject entryRuleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentConstantOrAttribute = null;


        try {
            // InternalCQL.g:1274:79: (iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF )
            // InternalCQL.g:1275:2: iv_ruleExpressionComponentConstantOrAttribute= ruleExpressionComponentConstantOrAttribute EOF
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
    // InternalCQL.g:1281:1: ruleExpressionComponentConstantOrAttribute returns [EObject current=null] : ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) ;
    public final EObject ruleExpressionComponentConstantOrAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;

        EObject lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1287:2: ( ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) ) )
            // InternalCQL.g:1288:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            {
            // InternalCQL.g:1288:2: ( ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) ) | ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) ) )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( ((LA24_0>=RULE_STRING && LA24_0<=RULE_FLOAT)||(LA24_0>=70 && LA24_0<=71)) ) {
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
                    // InternalCQL.g:1289:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQL.g:1289:3: ( (lv_value_0_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQL.g:1290:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQL.g:1290:4: (lv_value_0_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQL.g:1291:5: lv_value_0_0= ruleAtomicWithoutAttributeRef
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
                    // InternalCQL.g:1309:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    {
                    // InternalCQL.g:1309:3: ( (lv_value_1_0= ruleAttributeWithoutAliasDefinition ) )
                    // InternalCQL.g:1310:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    {
                    // InternalCQL.g:1310:4: (lv_value_1_0= ruleAttributeWithoutAliasDefinition )
                    // InternalCQL.g:1311:5: lv_value_1_0= ruleAttributeWithoutAliasDefinition
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
    // InternalCQL.g:1332:1: entryRuleExpressionComponentMapperOrConstant returns [EObject current=null] : iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF ;
    public final EObject entryRuleExpressionComponentMapperOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentMapperOrConstant = null;


        try {
            // InternalCQL.g:1332:76: (iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF )
            // InternalCQL.g:1333:2: iv_ruleExpressionComponentMapperOrConstant= ruleExpressionComponentMapperOrConstant EOF
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
    // InternalCQL.g:1339:1: ruleExpressionComponentMapperOrConstant returns [EObject current=null] : ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) ;
    public final EObject ruleExpressionComponentMapperOrConstant() throws RecognitionException {
        EObject current = null;

        EObject this_Mapper_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1345:2: ( ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) ) )
            // InternalCQL.g:1346:2: ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            {
            // InternalCQL.g:1346:2: ( (this_Mapper_0= ruleMapper () ) | ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) ) )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==37) ) {
                alt25=1;
            }
            else if ( ((LA25_0>=RULE_STRING && LA25_0<=RULE_FLOAT)||(LA25_0>=70 && LA25_0<=71)) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // InternalCQL.g:1347:3: (this_Mapper_0= ruleMapper () )
                    {
                    // InternalCQL.g:1347:3: (this_Mapper_0= ruleMapper () )
                    // InternalCQL.g:1348:4: this_Mapper_0= ruleMapper ()
                    {

                    				newCompositeNode(grammarAccess.getExpressionComponentMapperOrConstantAccess().getMapperParserRuleCall_0_0());
                    			
                    pushFollow(FOLLOW_2);
                    this_Mapper_0=ruleMapper();

                    state._fsp--;


                    				current = this_Mapper_0;
                    				afterParserOrEnumRuleCall();
                    			
                    // InternalCQL.g:1356:4: ()
                    // InternalCQL.g:1357:5: 
                    {

                    					current = forceCreateModelElementAndSet(
                    						grammarAccess.getExpressionComponentMapperOrConstantAccess().getExpressionComponentValueAction_0_1(),
                    						current);
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1365:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    {
                    // InternalCQL.g:1365:3: ( (lv_value_2_0= ruleAtomicWithoutAttributeRef ) )
                    // InternalCQL.g:1366:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    {
                    // InternalCQL.g:1366:4: (lv_value_2_0= ruleAtomicWithoutAttributeRef )
                    // InternalCQL.g:1367:5: lv_value_2_0= ruleAtomicWithoutAttributeRef
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
    // InternalCQL.g:1388:1: entryRuleExpressionComponentOnlyAttribute returns [EObject current=null] : iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF ;
    public final EObject entryRuleExpressionComponentOnlyAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlyAttribute = null;


        try {
            // InternalCQL.g:1388:73: (iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF )
            // InternalCQL.g:1389:2: iv_ruleExpressionComponentOnlyAttribute= ruleExpressionComponentOnlyAttribute EOF
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
    // InternalCQL.g:1395:1: ruleExpressionComponentOnlyAttribute returns [EObject current=null] : ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) ;
    public final EObject ruleExpressionComponentOnlyAttribute() throws RecognitionException {
        EObject current = null;

        EObject lv_value_0_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1401:2: ( ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) ) )
            // InternalCQL.g:1402:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            {
            // InternalCQL.g:1402:2: ( (lv_value_0_0= ruleAttributeWithoutAliasDefinition ) )
            // InternalCQL.g:1403:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            {
            // InternalCQL.g:1403:3: (lv_value_0_0= ruleAttributeWithoutAliasDefinition )
            // InternalCQL.g:1404:4: lv_value_0_0= ruleAttributeWithoutAliasDefinition
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
    // InternalCQL.g:1424:1: entryRuleExpressionComponentOnlymapper returns [EObject current=null] : iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF ;
    public final EObject entryRuleExpressionComponentOnlymapper() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionComponentOnlymapper = null;


        try {
            // InternalCQL.g:1424:70: (iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF )
            // InternalCQL.g:1425:2: iv_ruleExpressionComponentOnlymapper= ruleExpressionComponentOnlymapper EOF
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
    // InternalCQL.g:1431:1: ruleExpressionComponentOnlymapper returns [EObject current=null] : (this_Mapper_0= ruleMapper () ) ;
    public final EObject ruleExpressionComponentOnlymapper() throws RecognitionException {
        EObject current = null;

        EObject this_Mapper_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1437:2: ( (this_Mapper_0= ruleMapper () ) )
            // InternalCQL.g:1438:2: (this_Mapper_0= ruleMapper () )
            {
            // InternalCQL.g:1438:2: (this_Mapper_0= ruleMapper () )
            // InternalCQL.g:1439:3: this_Mapper_0= ruleMapper ()
            {

            			newCompositeNode(grammarAccess.getExpressionComponentOnlymapperAccess().getMapperParserRuleCall_0());
            		
            pushFollow(FOLLOW_2);
            this_Mapper_0=ruleMapper();

            state._fsp--;


            			current = this_Mapper_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:1447:3: ()
            // InternalCQL.g:1448:4: 
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
    // InternalCQL.g:1458:1: entryRuleMapper returns [EObject current=null] : iv_ruleMapper= ruleMapper EOF ;
    public final EObject entryRuleMapper() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapper = null;


        try {
            // InternalCQL.g:1458:47: (iv_ruleMapper= ruleMapper EOF )
            // InternalCQL.g:1459:2: iv_ruleMapper= ruleMapper EOF
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
    // InternalCQL.g:1465:1: ruleMapper returns [EObject current=null] : ( () ( (lv_name_1_0= 'DolToEur' ) ) otherlv_2= '(' ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= ')' ) ;
    public final EObject ruleMapper() throws RecognitionException {
        EObject current = null;

        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_value_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1471:2: ( ( () ( (lv_name_1_0= 'DolToEur' ) ) otherlv_2= '(' ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= ')' ) )
            // InternalCQL.g:1472:2: ( () ( (lv_name_1_0= 'DolToEur' ) ) otherlv_2= '(' ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= ')' )
            {
            // InternalCQL.g:1472:2: ( () ( (lv_name_1_0= 'DolToEur' ) ) otherlv_2= '(' ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= ')' )
            // InternalCQL.g:1473:3: () ( (lv_name_1_0= 'DolToEur' ) ) otherlv_2= '(' ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) ) otherlv_4= ')'
            {
            // InternalCQL.g:1473:3: ()
            // InternalCQL.g:1474:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getMapperAccess().getMapperAction_0(),
            					current);
            			

            }

            // InternalCQL.g:1480:3: ( (lv_name_1_0= 'DolToEur' ) )
            // InternalCQL.g:1481:4: (lv_name_1_0= 'DolToEur' )
            {
            // InternalCQL.g:1481:4: (lv_name_1_0= 'DolToEur' )
            // InternalCQL.g:1482:5: lv_name_1_0= 'DolToEur'
            {
            lv_name_1_0=(Token)match(input,37,FOLLOW_28); 

            					newLeafNode(lv_name_1_0, grammarAccess.getMapperAccess().getNameDolToEurKeyword_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getMapperRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_1_0, "DolToEur");
            				

            }


            }

            otherlv_2=(Token)match(input,22,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getMapperAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQL.g:1498:3: ( (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition ) )
            // InternalCQL.g:1499:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            {
            // InternalCQL.g:1499:4: (lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition )
            // InternalCQL.g:1500:5: lv_value_3_0= ruleSelectExpressionWithoutAliasDefinition
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

            otherlv_4=(Token)match(input,23,FOLLOW_2); 

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
    // InternalCQL.g:1525:1: entryRuleSelectExpression returns [EObject current=null] : iv_ruleSelectExpression= ruleSelectExpression EOF ;
    public final EObject entryRuleSelectExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpression = null;


        try {
            // InternalCQL.g:1525:57: (iv_ruleSelectExpression= ruleSelectExpression EOF )
            // InternalCQL.g:1526:2: iv_ruleSelectExpression= ruleSelectExpression EOF
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
    // InternalCQL.g:1532:1: ruleSelectExpression returns [EObject current=null] : ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) ;
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
            // InternalCQL.g:1538:2: ( ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:1539:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:1539:2: ( ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )? )
            // InternalCQL.g:1540:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:1540:3: ( ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( ((LA32_0>=RULE_STRING && LA32_0<=RULE_FLOAT)||LA32_0==37||(LA32_0>=70 && LA32_0<=71)) ) {
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
                    // InternalCQL.g:1541:4: ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    {
                    // InternalCQL.g:1541:4: ( ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    // InternalCQL.g:1542:5: ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    {
                    // InternalCQL.g:1542:5: ( (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant ) )
                    // InternalCQL.g:1543:6: (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant )
                    {
                    // InternalCQL.g:1543:6: (lv_expressions_0_0= ruleExpressionComponentMapperOrConstant )
                    // InternalCQL.g:1544:7: lv_expressions_0_0= ruleExpressionComponentMapperOrConstant
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

                    // InternalCQL.g:1561:5: ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    loop28:
                    do {
                        int alt28=2;
                        int LA28_0 = input.LA(1);

                        if ( (LA28_0==15||(LA28_0>=38 && LA28_0<=40)) ) {
                            alt28=1;
                        }


                        switch (alt28) {
                    	case 1 :
                    	    // InternalCQL.g:1562:6: ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQL.g:1562:6: ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) )
                    	    // InternalCQL.g:1563:7: ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) )
                    	    {
                    	    // InternalCQL.g:1563:7: ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) )
                    	    // InternalCQL.g:1564:8: (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' )
                    	    {
                    	    // InternalCQL.g:1564:8: (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' )
                    	    int alt26=4;
                    	    switch ( input.LA(1) ) {
                    	    case 38:
                    	        {
                    	        alt26=1;
                    	        }
                    	        break;
                    	    case 39:
                    	        {
                    	        alt26=2;
                    	        }
                    	        break;
                    	    case 15:
                    	        {
                    	        alt26=3;
                    	        }
                    	        break;
                    	    case 40:
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
                    	            // InternalCQL.g:1565:9: lv_operators_1_1= '+'
                    	            {
                    	            lv_operators_1_1=(Token)match(input,38,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_1, grammarAccess.getSelectExpressionAccess().getOperatorsPlusSignKeyword_0_0_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQL.g:1576:9: lv_operators_1_2= '-'
                    	            {
                    	            lv_operators_1_2=(Token)match(input,39,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_2, grammarAccess.getSelectExpressionAccess().getOperatorsHyphenMinusKeyword_0_0_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQL.g:1587:9: lv_operators_1_3= '*'
                    	            {
                    	            lv_operators_1_3=(Token)match(input,15,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_1_3, grammarAccess.getSelectExpressionAccess().getOperatorsAsteriskKeyword_0_0_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_1_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQL.g:1598:9: lv_operators_1_4= '/'
                    	            {
                    	            lv_operators_1_4=(Token)match(input,40,FOLLOW_5); 

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

                    	    // InternalCQL.g:1611:6: ( ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQL.g:1612:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQL.g:1612:7: ( (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQL.g:1613:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQL.g:1613:8: (lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_2_2= ruleExpressionComponentOnlymapper )
                    	    int alt27=2;
                    	    int LA27_0 = input.LA(1);

                    	    if ( ((LA27_0>=RULE_ID && LA27_0<=RULE_FLOAT)||(LA27_0>=70 && LA27_0<=71)) ) {
                    	        alt27=1;
                    	    }
                    	    else if ( (LA27_0==37) ) {
                    	        alt27=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 27, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt27) {
                    	        case 1 :
                    	            // InternalCQL.g:1614:9: lv_expressions_2_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQL.g:1630:9: lv_expressions_2_2= ruleExpressionComponentOnlymapper
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
                    // InternalCQL.g:1651:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    {
                    // InternalCQL.g:1651:4: ( ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    // InternalCQL.g:1652:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    {
                    // InternalCQL.g:1652:5: ( (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute ) )
                    // InternalCQL.g:1653:6: (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute )
                    {
                    // InternalCQL.g:1653:6: (lv_expressions_3_0= ruleExpressionComponentOnlyAttribute )
                    // InternalCQL.g:1654:7: lv_expressions_3_0= ruleExpressionComponentOnlyAttribute
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

                    // InternalCQL.g:1671:5: ( ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    int cnt31=0;
                    loop31:
                    do {
                        int alt31=2;
                        int LA31_0 = input.LA(1);

                        if ( (LA31_0==15||(LA31_0>=38 && LA31_0<=40)) ) {
                            alt31=1;
                        }


                        switch (alt31) {
                    	case 1 :
                    	    // InternalCQL.g:1672:6: ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) ) ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQL.g:1672:6: ( ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) ) )
                    	    // InternalCQL.g:1673:7: ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) )
                    	    {
                    	    // InternalCQL.g:1673:7: ( (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' ) )
                    	    // InternalCQL.g:1674:8: (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' )
                    	    {
                    	    // InternalCQL.g:1674:8: (lv_operators_4_1= '+' | lv_operators_4_2= '-' | lv_operators_4_3= '*' | lv_operators_4_4= '/' )
                    	    int alt29=4;
                    	    switch ( input.LA(1) ) {
                    	    case 38:
                    	        {
                    	        alt29=1;
                    	        }
                    	        break;
                    	    case 39:
                    	        {
                    	        alt29=2;
                    	        }
                    	        break;
                    	    case 15:
                    	        {
                    	        alt29=3;
                    	        }
                    	        break;
                    	    case 40:
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
                    	            // InternalCQL.g:1675:9: lv_operators_4_1= '+'
                    	            {
                    	            lv_operators_4_1=(Token)match(input,38,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_1, grammarAccess.getSelectExpressionAccess().getOperatorsPlusSignKeyword_0_1_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQL.g:1686:9: lv_operators_4_2= '-'
                    	            {
                    	            lv_operators_4_2=(Token)match(input,39,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_2, grammarAccess.getSelectExpressionAccess().getOperatorsHyphenMinusKeyword_0_1_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQL.g:1697:9: lv_operators_4_3= '*'
                    	            {
                    	            lv_operators_4_3=(Token)match(input,15,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_4_3, grammarAccess.getSelectExpressionAccess().getOperatorsAsteriskKeyword_0_1_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_4_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQL.g:1708:9: lv_operators_4_4= '/'
                    	            {
                    	            lv_operators_4_4=(Token)match(input,40,FOLLOW_5); 

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

                    	    // InternalCQL.g:1721:6: ( ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQL.g:1722:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQL.g:1722:7: ( (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQL.g:1723:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQL.g:1723:8: (lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_5_2= ruleExpressionComponentOnlymapper )
                    	    int alt30=2;
                    	    int LA30_0 = input.LA(1);

                    	    if ( ((LA30_0>=RULE_ID && LA30_0<=RULE_FLOAT)||(LA30_0>=70 && LA30_0<=71)) ) {
                    	        alt30=1;
                    	    }
                    	    else if ( (LA30_0==37) ) {
                    	        alt30=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 30, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt30) {
                    	        case 1 :
                    	            // InternalCQL.g:1724:9: lv_expressions_5_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQL.g:1740:9: lv_expressions_5_2= ruleExpressionComponentOnlymapper
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

            // InternalCQL.g:1761:3: (otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) ) )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==26) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // InternalCQL.g:1762:4: otherlv_6= 'AS' ( (lv_alias_7_0= ruleAlias ) )
                    {
                    otherlv_6=(Token)match(input,26,FOLLOW_8); 

                    				newLeafNode(otherlv_6, grammarAccess.getSelectExpressionAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:1766:4: ( (lv_alias_7_0= ruleAlias ) )
                    // InternalCQL.g:1767:5: (lv_alias_7_0= ruleAlias )
                    {
                    // InternalCQL.g:1767:5: (lv_alias_7_0= ruleAlias )
                    // InternalCQL.g:1768:6: lv_alias_7_0= ruleAlias
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
    // InternalCQL.g:1790:1: entryRuleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF ;
    public final EObject entryRuleSelectExpressionWithoutAliasDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithoutAliasDefinition = null;


        try {
            // InternalCQL.g:1790:79: (iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF )
            // InternalCQL.g:1791:2: iv_ruleSelectExpressionWithoutAliasDefinition= ruleSelectExpressionWithoutAliasDefinition EOF
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
    // InternalCQL.g:1797:1: ruleSelectExpressionWithoutAliasDefinition returns [EObject current=null] : (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) ) ;
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
            // InternalCQL.g:1803:2: ( (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) ) )
            // InternalCQL.g:1804:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) )
            {
            // InternalCQL.g:1804:2: (this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) ) )
            // InternalCQL.g:1805:3: this_SelectExpressionWithOnlyAttributeOrConstant_0= ruleSelectExpressionWithOnlyAttributeOrConstant ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            {

            			newCompositeNode(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getSelectExpressionWithOnlyAttributeOrConstantParserRuleCall_0());
            		
            pushFollow(FOLLOW_5);
            this_SelectExpressionWithOnlyAttributeOrConstant_0=ruleSelectExpressionWithOnlyAttributeOrConstant();

            state._fsp--;


            			current = this_SelectExpressionWithOnlyAttributeOrConstant_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:1813:3: ( ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* ) | ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ ) )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( ((LA40_0>=RULE_STRING && LA40_0<=RULE_FLOAT)||LA40_0==37||(LA40_0>=70 && LA40_0<=71)) ) {
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
                    // InternalCQL.g:1814:4: ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    {
                    // InternalCQL.g:1814:4: ( ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )* )
                    // InternalCQL.g:1815:5: ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) ) ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    {
                    // InternalCQL.g:1815:5: ( (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant ) )
                    // InternalCQL.g:1816:6: (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant )
                    {
                    // InternalCQL.g:1816:6: (lv_expressions_1_0= ruleExpressionComponentMapperOrConstant )
                    // InternalCQL.g:1817:7: lv_expressions_1_0= ruleExpressionComponentMapperOrConstant
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

                    // InternalCQL.g:1834:5: ( ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) ) )*
                    loop36:
                    do {
                        int alt36=2;
                        int LA36_0 = input.LA(1);

                        if ( (LA36_0==15||(LA36_0>=38 && LA36_0<=40)) ) {
                            alt36=1;
                        }


                        switch (alt36) {
                    	case 1 :
                    	    // InternalCQL.g:1835:6: ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) ) ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQL.g:1835:6: ( ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) ) )
                    	    // InternalCQL.g:1836:7: ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) )
                    	    {
                    	    // InternalCQL.g:1836:7: ( (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' ) )
                    	    // InternalCQL.g:1837:8: (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' )
                    	    {
                    	    // InternalCQL.g:1837:8: (lv_operators_2_1= '+' | lv_operators_2_2= '-' | lv_operators_2_3= '*' | lv_operators_2_4= '/' )
                    	    int alt34=4;
                    	    switch ( input.LA(1) ) {
                    	    case 38:
                    	        {
                    	        alt34=1;
                    	        }
                    	        break;
                    	    case 39:
                    	        {
                    	        alt34=2;
                    	        }
                    	        break;
                    	    case 15:
                    	        {
                    	        alt34=3;
                    	        }
                    	        break;
                    	    case 40:
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
                    	            // InternalCQL.g:1838:9: lv_operators_2_1= '+'
                    	            {
                    	            lv_operators_2_1=(Token)match(input,38,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_1, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsPlusSignKeyword_1_0_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQL.g:1849:9: lv_operators_2_2= '-'
                    	            {
                    	            lv_operators_2_2=(Token)match(input,39,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_2, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsHyphenMinusKeyword_1_0_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQL.g:1860:9: lv_operators_2_3= '*'
                    	            {
                    	            lv_operators_2_3=(Token)match(input,15,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_2_3, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAsteriskKeyword_1_0_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_2_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQL.g:1871:9: lv_operators_2_4= '/'
                    	            {
                    	            lv_operators_2_4=(Token)match(input,40,FOLLOW_5); 

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

                    	    // InternalCQL.g:1884:6: ( ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQL.g:1885:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQL.g:1885:7: ( (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQL.g:1886:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQL.g:1886:8: (lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_3_2= ruleExpressionComponentOnlymapper )
                    	    int alt35=2;
                    	    int LA35_0 = input.LA(1);

                    	    if ( ((LA35_0>=RULE_ID && LA35_0<=RULE_FLOAT)||(LA35_0>=70 && LA35_0<=71)) ) {
                    	        alt35=1;
                    	    }
                    	    else if ( (LA35_0==37) ) {
                    	        alt35=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 35, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt35) {
                    	        case 1 :
                    	            // InternalCQL.g:1887:9: lv_expressions_3_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQL.g:1903:9: lv_expressions_3_2= ruleExpressionComponentOnlymapper
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
                    // InternalCQL.g:1924:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    {
                    // InternalCQL.g:1924:4: ( ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+ )
                    // InternalCQL.g:1925:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) ) ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    {
                    // InternalCQL.g:1925:5: ( (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute ) )
                    // InternalCQL.g:1926:6: (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute )
                    {
                    // InternalCQL.g:1926:6: (lv_expressions_4_0= ruleExpressionComponentOnlyAttribute )
                    // InternalCQL.g:1927:7: lv_expressions_4_0= ruleExpressionComponentOnlyAttribute
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

                    // InternalCQL.g:1944:5: ( ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) ) )+
                    int cnt39=0;
                    loop39:
                    do {
                        int alt39=2;
                        int LA39_0 = input.LA(1);

                        if ( (LA39_0==15||(LA39_0>=38 && LA39_0<=40)) ) {
                            alt39=1;
                        }


                        switch (alt39) {
                    	case 1 :
                    	    // InternalCQL.g:1945:6: ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) ) ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) )
                    	    {
                    	    // InternalCQL.g:1945:6: ( ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) ) )
                    	    // InternalCQL.g:1946:7: ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) )
                    	    {
                    	    // InternalCQL.g:1946:7: ( (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' ) )
                    	    // InternalCQL.g:1947:8: (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' )
                    	    {
                    	    // InternalCQL.g:1947:8: (lv_operators_5_1= '+' | lv_operators_5_2= '-' | lv_operators_5_3= '*' | lv_operators_5_4= '/' )
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
                    	    case 15:
                    	        {
                    	        alt37=3;
                    	        }
                    	        break;
                    	    case 40:
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
                    	            // InternalCQL.g:1948:9: lv_operators_5_1= '+'
                    	            {
                    	            lv_operators_5_1=(Token)match(input,38,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_1, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsPlusSignKeyword_1_1_1_0_0_0());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_1, null);
                    	            								

                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalCQL.g:1959:9: lv_operators_5_2= '-'
                    	            {
                    	            lv_operators_5_2=(Token)match(input,39,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_2, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsHyphenMinusKeyword_1_1_1_0_0_1());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_2, null);
                    	            								

                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalCQL.g:1970:9: lv_operators_5_3= '*'
                    	            {
                    	            lv_operators_5_3=(Token)match(input,15,FOLLOW_5); 

                    	            									newLeafNode(lv_operators_5_3, grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAsteriskKeyword_1_1_1_0_0_2());
                    	            								

                    	            									if (current==null) {
                    	            										current = createModelElement(grammarAccess.getSelectExpressionWithoutAliasDefinitionRule());
                    	            									}
                    	            									addWithLastConsumed(current, "operators", lv_operators_5_3, null);
                    	            								

                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalCQL.g:1981:9: lv_operators_5_4= '/'
                    	            {
                    	            lv_operators_5_4=(Token)match(input,40,FOLLOW_5); 

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

                    	    // InternalCQL.g:1994:6: ( ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) ) )
                    	    // InternalCQL.g:1995:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) )
                    	    {
                    	    // InternalCQL.g:1995:7: ( (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper ) )
                    	    // InternalCQL.g:1996:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper )
                    	    {
                    	    // InternalCQL.g:1996:8: (lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute | lv_expressions_6_2= ruleExpressionComponentOnlymapper )
                    	    int alt38=2;
                    	    int LA38_0 = input.LA(1);

                    	    if ( ((LA38_0>=RULE_ID && LA38_0<=RULE_FLOAT)||(LA38_0>=70 && LA38_0<=71)) ) {
                    	        alt38=1;
                    	    }
                    	    else if ( (LA38_0==37) ) {
                    	        alt38=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 38, 0, input);

                    	        throw nvae;
                    	    }
                    	    switch (alt38) {
                    	        case 1 :
                    	            // InternalCQL.g:1997:9: lv_expressions_6_1= ruleExpressionComponentConstantOrAttribute
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
                    	            // InternalCQL.g:2013:9: lv_expressions_6_2= ruleExpressionComponentOnlymapper
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
    // InternalCQL.g:2038:1: entryRuleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF ;
    public final EObject entryRuleSelectExpressionWithOnlyAttributeOrConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectExpressionWithOnlyAttributeOrConstant = null;


        try {
            // InternalCQL.g:2038:84: (iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF )
            // InternalCQL.g:2039:2: iv_ruleSelectExpressionWithOnlyAttributeOrConstant= ruleSelectExpressionWithOnlyAttributeOrConstant EOF
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
    // InternalCQL.g:2045:1: ruleSelectExpressionWithOnlyAttributeOrConstant returns [EObject current=null] : ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) ;
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
            // InternalCQL.g:2051:2: ( ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* ) )
            // InternalCQL.g:2052:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            {
            // InternalCQL.g:2052:2: ( ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )* )
            // InternalCQL.g:2053:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) ) ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            {
            // InternalCQL.g:2053:3: ( (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute ) )
            // InternalCQL.g:2054:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            {
            // InternalCQL.g:2054:4: (lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute )
            // InternalCQL.g:2055:5: lv_expressions_0_0= ruleExpressionComponentConstantOrAttribute
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

            // InternalCQL.g:2072:3: ( ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) ) )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==15||(LA42_0>=38 && LA42_0<=40)) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // InternalCQL.g:2073:4: ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) ) ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    {
            	    // InternalCQL.g:2073:4: ( ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) ) )
            	    // InternalCQL.g:2074:5: ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) )
            	    {
            	    // InternalCQL.g:2074:5: ( (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' ) )
            	    // InternalCQL.g:2075:6: (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' )
            	    {
            	    // InternalCQL.g:2075:6: (lv_operators_1_1= '+' | lv_operators_1_2= '-' | lv_operators_1_3= '*' | lv_operators_1_4= '/' )
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
            	    case 15:
            	        {
            	        alt41=3;
            	        }
            	        break;
            	    case 40:
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
            	            // InternalCQL.g:2076:7: lv_operators_1_1= '+'
            	            {
            	            lv_operators_1_1=(Token)match(input,38,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_1, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsPlusSignKeyword_1_0_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_1, null);
            	            						

            	            }
            	            break;
            	        case 2 :
            	            // InternalCQL.g:2087:7: lv_operators_1_2= '-'
            	            {
            	            lv_operators_1_2=(Token)match(input,39,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_2, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsHyphenMinusKeyword_1_0_0_1());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_2, null);
            	            						

            	            }
            	            break;
            	        case 3 :
            	            // InternalCQL.g:2098:7: lv_operators_1_3= '*'
            	            {
            	            lv_operators_1_3=(Token)match(input,15,FOLLOW_5); 

            	            							newLeafNode(lv_operators_1_3, grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsAsteriskKeyword_1_0_0_2());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantRule());
            	            							}
            	            							addWithLastConsumed(current, "operators", lv_operators_1_3, null);
            	            						

            	            }
            	            break;
            	        case 4 :
            	            // InternalCQL.g:2109:7: lv_operators_1_4= '/'
            	            {
            	            lv_operators_1_4=(Token)match(input,40,FOLLOW_5); 

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

            	    // InternalCQL.g:2122:4: ( (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute ) )
            	    // InternalCQL.g:2123:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    {
            	    // InternalCQL.g:2123:5: (lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute )
            	    // InternalCQL.g:2124:6: lv_expressions_2_0= ruleExpressionComponentConstantOrAttribute
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
    // InternalCQL.g:2146:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQL.g:2146:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQL.g:2147:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQL.g:2153:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:2159:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:2160:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:2160:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:2161:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:2161:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:2162:4: lv_name_0_0= RULE_ID
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
    // InternalCQL.g:2181:1: entryRuleCreateParameters returns [EObject current=null] : iv_ruleCreateParameters= ruleCreateParameters EOF ;
    public final EObject entryRuleCreateParameters() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateParameters = null;


        try {
            // InternalCQL.g:2181:57: (iv_ruleCreateParameters= ruleCreateParameters EOF )
            // InternalCQL.g:2182:2: iv_ruleCreateParameters= ruleCreateParameters EOF
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
    // InternalCQL.g:2188:1: ruleCreateParameters returns [EObject current=null] : (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' ) ;
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
            // InternalCQL.g:2194:2: ( (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' ) )
            // InternalCQL.g:2195:2: (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' )
            {
            // InternalCQL.g:2195:2: (otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')' )
            // InternalCQL.g:2196:3: otherlv_0= 'WRAPPER' ( (lv_wrapper_1_0= RULE_STRING ) ) otherlv_2= 'PROTOCOL' ( (lv_protocol_3_0= RULE_STRING ) ) otherlv_4= 'TRANSPORT' ( (lv_transport_5_0= RULE_STRING ) ) otherlv_6= 'DATAHANDLER' ( (lv_datahandler_7_0= RULE_STRING ) ) otherlv_8= 'OPTIONS' otherlv_9= '(' ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+ (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )? otherlv_15= ')'
            {
            otherlv_0=(Token)match(input,41,FOLLOW_32); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateParametersAccess().getWRAPPERKeyword_0());
            		
            // InternalCQL.g:2200:3: ( (lv_wrapper_1_0= RULE_STRING ) )
            // InternalCQL.g:2201:4: (lv_wrapper_1_0= RULE_STRING )
            {
            // InternalCQL.g:2201:4: (lv_wrapper_1_0= RULE_STRING )
            // InternalCQL.g:2202:5: lv_wrapper_1_0= RULE_STRING
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

            otherlv_2=(Token)match(input,42,FOLLOW_32); 

            			newLeafNode(otherlv_2, grammarAccess.getCreateParametersAccess().getPROTOCOLKeyword_2());
            		
            // InternalCQL.g:2222:3: ( (lv_protocol_3_0= RULE_STRING ) )
            // InternalCQL.g:2223:4: (lv_protocol_3_0= RULE_STRING )
            {
            // InternalCQL.g:2223:4: (lv_protocol_3_0= RULE_STRING )
            // InternalCQL.g:2224:5: lv_protocol_3_0= RULE_STRING
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

            otherlv_4=(Token)match(input,43,FOLLOW_32); 

            			newLeafNode(otherlv_4, grammarAccess.getCreateParametersAccess().getTRANSPORTKeyword_4());
            		
            // InternalCQL.g:2244:3: ( (lv_transport_5_0= RULE_STRING ) )
            // InternalCQL.g:2245:4: (lv_transport_5_0= RULE_STRING )
            {
            // InternalCQL.g:2245:4: (lv_transport_5_0= RULE_STRING )
            // InternalCQL.g:2246:5: lv_transport_5_0= RULE_STRING
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

            otherlv_6=(Token)match(input,44,FOLLOW_32); 

            			newLeafNode(otherlv_6, grammarAccess.getCreateParametersAccess().getDATAHANDLERKeyword_6());
            		
            // InternalCQL.g:2266:3: ( (lv_datahandler_7_0= RULE_STRING ) )
            // InternalCQL.g:2267:4: (lv_datahandler_7_0= RULE_STRING )
            {
            // InternalCQL.g:2267:4: (lv_datahandler_7_0= RULE_STRING )
            // InternalCQL.g:2268:5: lv_datahandler_7_0= RULE_STRING
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

            otherlv_8=(Token)match(input,45,FOLLOW_28); 

            			newLeafNode(otherlv_8, grammarAccess.getCreateParametersAccess().getOPTIONSKeyword_8());
            		
            otherlv_9=(Token)match(input,22,FOLLOW_32); 

            			newLeafNode(otherlv_9, grammarAccess.getCreateParametersAccess().getLeftParenthesisKeyword_9());
            		
            // InternalCQL.g:2292:3: ( ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) ) )+
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
            	    // InternalCQL.g:2293:4: ( (lv_keys_10_0= RULE_STRING ) ) ( (lv_values_11_0= RULE_STRING ) )
            	    {
            	    // InternalCQL.g:2293:4: ( (lv_keys_10_0= RULE_STRING ) )
            	    // InternalCQL.g:2294:5: (lv_keys_10_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:2294:5: (lv_keys_10_0= RULE_STRING )
            	    // InternalCQL.g:2295:6: lv_keys_10_0= RULE_STRING
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

            	    // InternalCQL.g:2311:4: ( (lv_values_11_0= RULE_STRING ) )
            	    // InternalCQL.g:2312:5: (lv_values_11_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:2312:5: (lv_values_11_0= RULE_STRING )
            	    // InternalCQL.g:2313:6: lv_values_11_0= RULE_STRING
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

            // InternalCQL.g:2330:3: (otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) ) )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==16) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // InternalCQL.g:2331:4: otherlv_12= ',' ( (lv_keys_13_0= RULE_STRING ) ) ( (lv_values_14_0= RULE_STRING ) )
                    {
                    otherlv_12=(Token)match(input,16,FOLLOW_32); 

                    				newLeafNode(otherlv_12, grammarAccess.getCreateParametersAccess().getCommaKeyword_11_0());
                    			
                    // InternalCQL.g:2335:4: ( (lv_keys_13_0= RULE_STRING ) )
                    // InternalCQL.g:2336:5: (lv_keys_13_0= RULE_STRING )
                    {
                    // InternalCQL.g:2336:5: (lv_keys_13_0= RULE_STRING )
                    // InternalCQL.g:2337:6: lv_keys_13_0= RULE_STRING
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

                    // InternalCQL.g:2353:4: ( (lv_values_14_0= RULE_STRING ) )
                    // InternalCQL.g:2354:5: (lv_values_14_0= RULE_STRING )
                    {
                    // InternalCQL.g:2354:5: (lv_values_14_0= RULE_STRING )
                    // InternalCQL.g:2355:6: lv_values_14_0= RULE_STRING
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
    // InternalCQL.g:2380:1: entryRuleAttributeDefinition returns [EObject current=null] : iv_ruleAttributeDefinition= ruleAttributeDefinition EOF ;
    public final EObject entryRuleAttributeDefinition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeDefinition = null;


        try {
            // InternalCQL.g:2380:60: (iv_ruleAttributeDefinition= ruleAttributeDefinition EOF )
            // InternalCQL.g:2381:2: iv_ruleAttributeDefinition= ruleAttributeDefinition EOF
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
    // InternalCQL.g:2387:1: ruleAttributeDefinition returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' ) ;
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
            // InternalCQL.g:2393:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' ) )
            // InternalCQL.g:2394:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' )
            {
            // InternalCQL.g:2394:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')' )
            // InternalCQL.g:2395:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attributes_2_0= ruleAttribute ) )+ ( (lv_datatypes_3_0= ruleDataType ) )+ (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )* otherlv_7= ')'
            {
            // InternalCQL.g:2395:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:2396:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:2396:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:2397:5: lv_name_0_0= RULE_ID
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
            		
            // InternalCQL.g:2417:3: ( (lv_attributes_2_0= ruleAttribute ) )+
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
            	    // InternalCQL.g:2418:4: (lv_attributes_2_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2418:4: (lv_attributes_2_0= ruleAttribute )
            	    // InternalCQL.g:2419:5: lv_attributes_2_0= ruleAttribute
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

            // InternalCQL.g:2436:3: ( (lv_datatypes_3_0= ruleDataType ) )+
            int cnt46=0;
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( ((LA46_0>=72 && LA46_0<=79)) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // InternalCQL.g:2437:4: (lv_datatypes_3_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2437:4: (lv_datatypes_3_0= ruleDataType )
            	    // InternalCQL.g:2438:5: lv_datatypes_3_0= ruleDataType
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

            // InternalCQL.g:2455:3: (otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) ) )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==16) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // InternalCQL.g:2456:4: otherlv_4= ',' ( (lv_attributes_5_0= ruleAttribute ) ) ( (lv_datatypes_6_0= ruleDataType ) )
            	    {
            	    otherlv_4=(Token)match(input,16,FOLLOW_8); 

            	    				newLeafNode(otherlv_4, grammarAccess.getAttributeDefinitionAccess().getCommaKeyword_4_0());
            	    			
            	    // InternalCQL.g:2460:4: ( (lv_attributes_5_0= ruleAttribute ) )
            	    // InternalCQL.g:2461:5: (lv_attributes_5_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:2461:5: (lv_attributes_5_0= ruleAttribute )
            	    // InternalCQL.g:2462:6: lv_attributes_5_0= ruleAttribute
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

            	    // InternalCQL.g:2479:4: ( (lv_datatypes_6_0= ruleDataType ) )
            	    // InternalCQL.g:2480:5: (lv_datatypes_6_0= ruleDataType )
            	    {
            	    // InternalCQL.g:2480:5: (lv_datatypes_6_0= ruleDataType )
            	    // InternalCQL.g:2481:6: lv_datatypes_6_0= ruleDataType
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
    // InternalCQL.g:2507:1: entryRuleCreateStream1 returns [EObject current=null] : iv_ruleCreateStream1= ruleCreateStream1 EOF ;
    public final EObject entryRuleCreateStream1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStream1 = null;


        try {
            // InternalCQL.g:2507:54: (iv_ruleCreateStream1= ruleCreateStream1 EOF )
            // InternalCQL.g:2508:2: iv_ruleCreateStream1= ruleCreateStream1 EOF
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
    // InternalCQL.g:2514:1: ruleCreateStream1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateStream1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2520:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQL.g:2521:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQL.g:2521:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQL.g:2522:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQL.g:2522:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2523:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2523:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2524:5: lv_keyword_0_0= ruleCreateKeyword
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

            otherlv_1=(Token)match(input,46,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStream1Access().getSTREAMKeyword_1());
            		
            // InternalCQL.g:2545:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2546:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2546:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2547:5: lv_attributes_2_0= ruleAttributeDefinition
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

            // InternalCQL.g:2564:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQL.g:2565:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQL.g:2565:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQL.g:2566:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQL.g:2587:1: entryRuleCreateSink1 returns [EObject current=null] : iv_ruleCreateSink1= ruleCreateSink1 EOF ;
    public final EObject entryRuleCreateSink1() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateSink1 = null;


        try {
            // InternalCQL.g:2587:52: (iv_ruleCreateSink1= ruleCreateSink1 EOF )
            // InternalCQL.g:2588:2: iv_ruleCreateSink1= ruleCreateSink1 EOF
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
    // InternalCQL.g:2594:1: ruleCreateSink1 returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) ;
    public final EObject ruleCreateSink1() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Enumerator lv_keyword_0_0 = null;

        EObject lv_attributes_2_0 = null;

        EObject lv_pars_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2600:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) ) )
            // InternalCQL.g:2601:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            {
            // InternalCQL.g:2601:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) ) )
            // InternalCQL.g:2602:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'SINK' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) ( (lv_pars_3_0= ruleCreateParameters ) )
            {
            // InternalCQL.g:2602:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2603:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2603:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2604:5: lv_keyword_0_0= ruleCreateKeyword
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

            otherlv_1=(Token)match(input,47,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateSink1Access().getSINKKeyword_1());
            		
            // InternalCQL.g:2625:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2626:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2626:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2627:5: lv_attributes_2_0= ruleAttributeDefinition
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

            // InternalCQL.g:2644:3: ( (lv_pars_3_0= ruleCreateParameters ) )
            // InternalCQL.g:2645:4: (lv_pars_3_0= ruleCreateParameters )
            {
            // InternalCQL.g:2645:4: (lv_pars_3_0= ruleCreateParameters )
            // InternalCQL.g:2646:5: lv_pars_3_0= ruleCreateParameters
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
    // InternalCQL.g:2667:1: entryRuleCreateStreamChannel returns [EObject current=null] : iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF ;
    public final EObject entryRuleCreateStreamChannel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamChannel = null;


        try {
            // InternalCQL.g:2667:60: (iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF )
            // InternalCQL.g:2668:2: iv_ruleCreateStreamChannel= ruleCreateStreamChannel EOF
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
    // InternalCQL.g:2674:1: ruleCreateStreamChannel returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) ) ;
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
            // InternalCQL.g:2680:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) ) )
            // InternalCQL.g:2681:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) )
            {
            // InternalCQL.g:2681:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) ) )
            // InternalCQL.g:2682:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'CHANNEL' ( (lv_host_4_0= RULE_ID ) ) otherlv_5= ':' ( (lv_port_6_0= RULE_INT ) )
            {
            // InternalCQL.g:2682:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2683:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2683:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2684:5: lv_keyword_0_0= ruleCreateKeyword
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

            otherlv_1=(Token)match(input,46,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamChannelAccess().getSTREAMKeyword_1());
            		
            // InternalCQL.g:2705:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2706:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2706:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2707:5: lv_attributes_2_0= ruleAttributeDefinition
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

            otherlv_3=(Token)match(input,48,FOLLOW_8); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamChannelAccess().getCHANNELKeyword_3());
            		
            // InternalCQL.g:2728:3: ( (lv_host_4_0= RULE_ID ) )
            // InternalCQL.g:2729:4: (lv_host_4_0= RULE_ID )
            {
            // InternalCQL.g:2729:4: (lv_host_4_0= RULE_ID )
            // InternalCQL.g:2730:5: lv_host_4_0= RULE_ID
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

            otherlv_5=(Token)match(input,49,FOLLOW_47); 

            			newLeafNode(otherlv_5, grammarAccess.getCreateStreamChannelAccess().getColonKeyword_5());
            		
            // InternalCQL.g:2750:3: ( (lv_port_6_0= RULE_INT ) )
            // InternalCQL.g:2751:4: (lv_port_6_0= RULE_INT )
            {
            // InternalCQL.g:2751:4: (lv_port_6_0= RULE_INT )
            // InternalCQL.g:2752:5: lv_port_6_0= RULE_INT
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
    // InternalCQL.g:2772:1: entryRuleCreateStreamFile returns [EObject current=null] : iv_ruleCreateStreamFile= ruleCreateStreamFile EOF ;
    public final EObject entryRuleCreateStreamFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateStreamFile = null;


        try {
            // InternalCQL.g:2772:57: (iv_ruleCreateStreamFile= ruleCreateStreamFile EOF )
            // InternalCQL.g:2773:2: iv_ruleCreateStreamFile= ruleCreateStreamFile EOF
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
    // InternalCQL.g:2779:1: ruleCreateStreamFile returns [EObject current=null] : ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) ) ;
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
            // InternalCQL.g:2785:2: ( ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) ) )
            // InternalCQL.g:2786:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) )
            {
            // InternalCQL.g:2786:2: ( ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) ) )
            // InternalCQL.g:2787:3: ( (lv_keyword_0_0= ruleCreateKeyword ) ) otherlv_1= 'STREAM' ( (lv_attributes_2_0= ruleAttributeDefinition ) ) otherlv_3= 'FILE' ( (lv_filename_4_0= RULE_STRING ) ) otherlv_5= 'AS' ( (lv_type_6_0= RULE_ID ) )
            {
            // InternalCQL.g:2787:3: ( (lv_keyword_0_0= ruleCreateKeyword ) )
            // InternalCQL.g:2788:4: (lv_keyword_0_0= ruleCreateKeyword )
            {
            // InternalCQL.g:2788:4: (lv_keyword_0_0= ruleCreateKeyword )
            // InternalCQL.g:2789:5: lv_keyword_0_0= ruleCreateKeyword
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

            otherlv_1=(Token)match(input,46,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getCreateStreamFileAccess().getSTREAMKeyword_1());
            		
            // InternalCQL.g:2810:3: ( (lv_attributes_2_0= ruleAttributeDefinition ) )
            // InternalCQL.g:2811:4: (lv_attributes_2_0= ruleAttributeDefinition )
            {
            // InternalCQL.g:2811:4: (lv_attributes_2_0= ruleAttributeDefinition )
            // InternalCQL.g:2812:5: lv_attributes_2_0= ruleAttributeDefinition
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

            otherlv_3=(Token)match(input,50,FOLLOW_32); 

            			newLeafNode(otherlv_3, grammarAccess.getCreateStreamFileAccess().getFILEKeyword_3());
            		
            // InternalCQL.g:2833:3: ( (lv_filename_4_0= RULE_STRING ) )
            // InternalCQL.g:2834:4: (lv_filename_4_0= RULE_STRING )
            {
            // InternalCQL.g:2834:4: (lv_filename_4_0= RULE_STRING )
            // InternalCQL.g:2835:5: lv_filename_4_0= RULE_STRING
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
            		
            // InternalCQL.g:2855:3: ( (lv_type_6_0= RULE_ID ) )
            // InternalCQL.g:2856:4: (lv_type_6_0= RULE_ID )
            {
            // InternalCQL.g:2856:4: (lv_type_6_0= RULE_ID )
            // InternalCQL.g:2857:5: lv_type_6_0= RULE_ID
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
    // InternalCQL.g:2877:1: entryRuleCreateView returns [EObject current=null] : iv_ruleCreateView= ruleCreateView EOF ;
    public final EObject entryRuleCreateView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreateView = null;


        try {
            // InternalCQL.g:2877:51: (iv_ruleCreateView= ruleCreateView EOF )
            // InternalCQL.g:2878:2: iv_ruleCreateView= ruleCreateView EOF
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
    // InternalCQL.g:2884:1: ruleCreateView returns [EObject current=null] : (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleCreateView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_select_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2890:2: ( (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) ) )
            // InternalCQL.g:2891:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) )
            {
            // InternalCQL.g:2891:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) ) )
            // InternalCQL.g:2892:3: otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' ( (lv_select_3_0= ruleNestedStatement ) )
            {
            otherlv_0=(Token)match(input,51,FOLLOW_8); 

            			newLeafNode(otherlv_0, grammarAccess.getCreateViewAccess().getVIEWKeyword_0());
            		
            // InternalCQL.g:2896:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:2897:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:2897:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:2898:5: lv_name_1_0= RULE_ID
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
            		
            // InternalCQL.g:2918:3: ( (lv_select_3_0= ruleNestedStatement ) )
            // InternalCQL.g:2919:4: (lv_select_3_0= ruleNestedStatement )
            {
            // InternalCQL.g:2919:4: (lv_select_3_0= ruleNestedStatement )
            // InternalCQL.g:2920:5: lv_select_3_0= ruleNestedStatement
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
    // InternalCQL.g:2941:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQL.g:2941:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQL.g:2942:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQL.g:2948:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2954:2: ( (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQL.g:2955:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQL.g:2955:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:2956:3: otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,46,FOLLOW_49); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,52,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQL.g:2964:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQL.g:2965:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQL.g:2965:4: (lv_name_2_0= RULE_ID )
            // InternalCQL.g:2966:5: lv_name_2_0= RULE_ID
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

            // InternalCQL.g:2982:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==13) ) {
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
                    // InternalCQL.g:2983:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQL.g:2983:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQL.g:2984:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQL.g:2984:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQL.g:2985:6: lv_statement_3_0= ruleSelect
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
                    // InternalCQL.g:3003:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQL.g:3003:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQL.g:3004:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQL.g:3004:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQL.g:3005:6: lv_inputname_4_0= RULE_ID
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
    // InternalCQL.g:3026:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQL.g:3026:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQL.g:3027:2: iv_ruleDrop= ruleDrop EOF
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
    // InternalCQL.g:3033:1: ruleDrop returns [EObject current=null] : ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) ) ;
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
            // InternalCQL.g:3039:2: ( ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:3040:2: ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            {
            // InternalCQL.g:3040:2: ( ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) ) )
            // InternalCQL.g:3041:3: ( (lv_keyword1_0_0= 'DROP' ) ) ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) ) ( (lv_value1_2_0= RULE_ID ) ) ( (lv_keyword3_3_0= 'IF EXISTS' ) )? ( (lv_value2_4_0= RULE_ID ) )
            {
            // InternalCQL.g:3041:3: ( (lv_keyword1_0_0= 'DROP' ) )
            // InternalCQL.g:3042:4: (lv_keyword1_0_0= 'DROP' )
            {
            // InternalCQL.g:3042:4: (lv_keyword1_0_0= 'DROP' )
            // InternalCQL.g:3043:5: lv_keyword1_0_0= 'DROP'
            {
            lv_keyword1_0_0=(Token)match(input,53,FOLLOW_51); 

            					newLeafNode(lv_keyword1_0_0, grammarAccess.getDropAccess().getKeyword1DROPKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDropRule());
            					}
            					setWithLastConsumed(current, "keyword1", lv_keyword1_0_0, "DROP");
            				

            }


            }

            // InternalCQL.g:3055:3: ( ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) ) )
            // InternalCQL.g:3056:4: ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) )
            {
            // InternalCQL.g:3056:4: ( (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' ) )
            // InternalCQL.g:3057:5: (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' )
            {
            // InternalCQL.g:3057:5: (lv_keyword2_1_1= 'SINK' | lv_keyword2_1_2= 'STREAM' )
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==47) ) {
                alt49=1;
            }
            else if ( (LA49_0==46) ) {
                alt49=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;
            }
            switch (alt49) {
                case 1 :
                    // InternalCQL.g:3058:6: lv_keyword2_1_1= 'SINK'
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
                    // InternalCQL.g:3069:6: lv_keyword2_1_2= 'STREAM'
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

            // InternalCQL.g:3082:3: ( (lv_value1_2_0= RULE_ID ) )
            // InternalCQL.g:3083:4: (lv_value1_2_0= RULE_ID )
            {
            // InternalCQL.g:3083:4: (lv_value1_2_0= RULE_ID )
            // InternalCQL.g:3084:5: lv_value1_2_0= RULE_ID
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

            // InternalCQL.g:3100:3: ( (lv_keyword3_3_0= 'IF EXISTS' ) )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==54) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // InternalCQL.g:3101:4: (lv_keyword3_3_0= 'IF EXISTS' )
                    {
                    // InternalCQL.g:3101:4: (lv_keyword3_3_0= 'IF EXISTS' )
                    // InternalCQL.g:3102:5: lv_keyword3_3_0= 'IF EXISTS'
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

            // InternalCQL.g:3114:3: ( (lv_value2_4_0= RULE_ID ) )
            // InternalCQL.g:3115:4: (lv_value2_4_0= RULE_ID )
            {
            // InternalCQL.g:3115:4: (lv_value2_4_0= RULE_ID )
            // InternalCQL.g:3116:5: lv_value2_4_0= RULE_ID
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
    // InternalCQL.g:3136:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQL.g:3136:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQL.g:3137:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQL.g:3143:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'UNBOUNDED' ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQL.g:3149:2: (kw= 'UNBOUNDED' )
            // InternalCQL.g:3150:2: kw= 'UNBOUNDED'
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
    // InternalCQL.g:3158:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQL.g:3158:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQL.g:3159:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQL.g:3165:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) ;
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
            // InternalCQL.g:3171:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) )
            // InternalCQL.g:3172:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            {
            // InternalCQL.g:3172:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            // InternalCQL.g:3173:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME'
            {
            otherlv_0=(Token)match(input,56,FOLLOW_47); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:3177:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:3178:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:3178:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:3179:5: lv_size_1_0= RULE_INT
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

            // InternalCQL.g:3195:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQL.g:3196:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQL.g:3196:4: (lv_unit_2_0= RULE_ID )
            // InternalCQL.g:3197:5: lv_unit_2_0= RULE_ID
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

            // InternalCQL.g:3213:3: (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==57) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // InternalCQL.g:3214:4: otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,57,FOLLOW_47); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQL.g:3218:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQL.g:3219:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQL.g:3219:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQL.g:3220:6: lv_advance_size_4_0= RULE_INT
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

                    // InternalCQL.g:3236:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQL.g:3237:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQL.g:3237:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQL.g:3238:6: lv_advance_unit_5_0= RULE_ID
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
    // InternalCQL.g:3263:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQL.g:3263:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQL.g:3264:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQL.g:3270:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQL.g:3276:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQL.g:3277:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQL.g:3277:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQL.g:3278:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,56,FOLLOW_47); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:3282:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:3283:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:3283:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:3284:5: lv_size_1_0= RULE_INT
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

            // InternalCQL.g:3300:3: (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==57) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // InternalCQL.g:3301:4: otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,57,FOLLOW_47); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQL.g:3305:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQL.g:3306:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQL.g:3306:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQL.g:3307:6: lv_advance_size_3_0= RULE_INT
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

            otherlv_4=(Token)match(input,59,FOLLOW_57); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQL.g:3328:3: (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==60) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // InternalCQL.g:3329:4: otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,60,FOLLOW_16); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,20,FOLLOW_8); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQL.g:3337:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQL.g:3338:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQL.g:3338:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQL.g:3339:6: lv_partition_attribute_7_0= ruleAttribute
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
    // InternalCQL.g:3361:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQL.g:3361:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQL.g:3362:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQL.g:3368:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3374:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQL.g:3375:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQL.g:3375:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQL.g:3376:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQL.g:3376:3: ()
            // InternalCQL.g:3377:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQL.g:3383:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQL.g:3384:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQL.g:3384:4: (lv_elements_1_0= ruleExpression )
            // InternalCQL.g:3385:5: lv_elements_1_0= ruleExpression
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
    // InternalCQL.g:3406:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQL.g:3406:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQL.g:3407:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQL.g:3413:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3419:2: (this_Or_0= ruleOr )
            // InternalCQL.g:3420:2: this_Or_0= ruleOr
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
    // InternalCQL.g:3431:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQL.g:3431:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQL.g:3432:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQL.g:3438:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3444:2: ( (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQL.g:3445:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQL.g:3445:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQL.g:3446:3: this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_58);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3454:3: ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            loop54:
            do {
                int alt54=2;
                int LA54_0 = input.LA(1);

                if ( (LA54_0==61) ) {
                    alt54=1;
                }


                switch (alt54) {
            	case 1 :
            	    // InternalCQL.g:3455:4: () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQL.g:3455:4: ()
            	    // InternalCQL.g:3456:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,61,FOLLOW_14); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQL.g:3466:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQL.g:3467:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQL.g:3467:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQL.g:3468:6: lv_right_3_0= ruleAnd
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
    // InternalCQL.g:3490:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQL.g:3490:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQL.g:3491:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQL.g:3497:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3503:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQL.g:3504:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQL.g:3504:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQL.g:3505:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_59);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3513:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop55:
            do {
                int alt55=2;
                int LA55_0 = input.LA(1);

                if ( (LA55_0==62) ) {
                    alt55=1;
                }


                switch (alt55) {
            	case 1 :
            	    // InternalCQL.g:3514:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQL.g:3514:4: ()
            	    // InternalCQL.g:3515:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,62,FOLLOW_14); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQL.g:3525:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQL.g:3526:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQL.g:3526:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQL.g:3527:6: lv_right_3_0= ruleEqualitiy
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
    // InternalCQL.g:3549:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQL.g:3549:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQL.g:3550:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQL.g:3556:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3562:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQL.g:3563:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQL.g:3563:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQL.g:3564:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_60);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3572:3: ( () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( ((LA57_0>=63 && LA57_0<=64)) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // InternalCQL.g:3573:4: () ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQL.g:3573:4: ()
            	    // InternalCQL.g:3574:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:3580:4: ( ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) ) )
            	    // InternalCQL.g:3581:5: ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) )
            	    {
            	    // InternalCQL.g:3581:5: ( (lv_op_2_1= '=' | lv_op_2_2= '!=' ) )
            	    // InternalCQL.g:3582:6: (lv_op_2_1= '=' | lv_op_2_2= '!=' )
            	    {
            	    // InternalCQL.g:3582:6: (lv_op_2_1= '=' | lv_op_2_2= '!=' )
            	    int alt56=2;
            	    int LA56_0 = input.LA(1);

            	    if ( (LA56_0==63) ) {
            	        alt56=1;
            	    }
            	    else if ( (LA56_0==64) ) {
            	        alt56=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 56, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt56) {
            	        case 1 :
            	            // InternalCQL.g:3583:7: lv_op_2_1= '='
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
            	            // InternalCQL.g:3594:7: lv_op_2_2= '!='
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

            	    // InternalCQL.g:3607:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQL.g:3608:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQL.g:3608:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQL.g:3609:6: lv_right_3_0= ruleComparison
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
    // InternalCQL.g:3631:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQL.g:3631:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQL.g:3632:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQL.g:3638:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQL.g:3644:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQL.g:3645:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQL.g:3645:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQL.g:3646:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_61);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3654:3: ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( ((LA59_0>=65 && LA59_0<=68)) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // InternalCQL.g:3655:4: () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQL.g:3655:4: ()
            	    // InternalCQL.g:3656:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:3662:4: ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) )
            	    // InternalCQL.g:3663:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    {
            	    // InternalCQL.g:3663:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    // InternalCQL.g:3664:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    {
            	    // InternalCQL.g:3664:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    int alt58=4;
            	    switch ( input.LA(1) ) {
            	    case 65:
            	        {
            	        alt58=1;
            	        }
            	        break;
            	    case 66:
            	        {
            	        alt58=2;
            	        }
            	        break;
            	    case 67:
            	        {
            	        alt58=3;
            	        }
            	        break;
            	    case 68:
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
            	            // InternalCQL.g:3665:7: lv_op_2_1= '>='
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
            	            // InternalCQL.g:3676:7: lv_op_2_2= '<='
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
            	            // InternalCQL.g:3687:7: lv_op_2_3= '<'
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
            	            // InternalCQL.g:3698:7: lv_op_2_4= '>'
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

            	    // InternalCQL.g:3711:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQL.g:3712:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQL.g:3712:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQL.g:3713:6: lv_right_3_0= rulePlusOrMinus
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
    // InternalCQL.g:3735:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQL.g:3735:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQL.g:3736:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQL.g:3742:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3748:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQL.g:3749:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQL.g:3749:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQL.g:3750:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_62);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3758:3: ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( ((LA61_0>=38 && LA61_0<=39)) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // InternalCQL.g:3759:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQL.g:3759:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) )
            	    int alt60=2;
            	    int LA60_0 = input.LA(1);

            	    if ( (LA60_0==38) ) {
            	        alt60=1;
            	    }
            	    else if ( (LA60_0==39) ) {
            	        alt60=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 60, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt60) {
            	        case 1 :
            	            // InternalCQL.g:3760:5: ( () otherlv_2= '+' )
            	            {
            	            // InternalCQL.g:3760:5: ( () otherlv_2= '+' )
            	            // InternalCQL.g:3761:6: () otherlv_2= '+'
            	            {
            	            // InternalCQL.g:3761:6: ()
            	            // InternalCQL.g:3762:7: 
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
            	            // InternalCQL.g:3774:5: ( () otherlv_4= '-' )
            	            {
            	            // InternalCQL.g:3774:5: ( () otherlv_4= '-' )
            	            // InternalCQL.g:3775:6: () otherlv_4= '-'
            	            {
            	            // InternalCQL.g:3775:6: ()
            	            // InternalCQL.g:3776:7: 
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

            	    // InternalCQL.g:3788:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQL.g:3789:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQL.g:3789:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQL.g:3790:6: lv_right_5_0= ruleMulOrDiv
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
    // InternalCQL.g:3812:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQL.g:3812:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQL.g:3813:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQL.g:3819:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:3825:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQL.g:3826:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQL.g:3826:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQL.g:3827:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_63);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:3835:3: ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop63:
            do {
                int alt63=2;
                int LA63_0 = input.LA(1);

                if ( (LA63_0==15||LA63_0==40) ) {
                    alt63=1;
                }


                switch (alt63) {
            	case 1 :
            	    // InternalCQL.g:3836:4: () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQL.g:3836:4: ()
            	    // InternalCQL.g:3837:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:3843:4: ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) )
            	    // InternalCQL.g:3844:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    {
            	    // InternalCQL.g:3844:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    // InternalCQL.g:3845:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    {
            	    // InternalCQL.g:3845:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    int alt62=2;
            	    int LA62_0 = input.LA(1);

            	    if ( (LA62_0==15) ) {
            	        alt62=1;
            	    }
            	    else if ( (LA62_0==40) ) {
            	        alt62=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 62, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt62) {
            	        case 1 :
            	            // InternalCQL.g:3846:7: lv_op_2_1= '*'
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
            	            // InternalCQL.g:3857:7: lv_op_2_2= '/'
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

            	    // InternalCQL.g:3870:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQL.g:3871:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQL.g:3871:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQL.g:3872:6: lv_right_3_0= rulePrimary
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
    // InternalCQL.g:3894:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQL.g:3894:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQL.g:3895:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQL.g:3901:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQL.g:3907:2: ( ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQL.g:3908:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQL.g:3908:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt64=3;
            switch ( input.LA(1) ) {
            case 22:
                {
                alt64=1;
                }
                break;
            case 69:
                {
                alt64=2;
                }
                break;
            case RULE_ID:
            case RULE_STRING:
            case RULE_INT:
            case RULE_FLOAT:
            case 70:
            case 71:
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
                    // InternalCQL.g:3909:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    {
                    // InternalCQL.g:3909:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    // InternalCQL.g:3910:4: () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')'
                    {
                    // InternalCQL.g:3910:4: ()
                    // InternalCQL.g:3911:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,22,FOLLOW_14); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQL.g:3921:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQL.g:3922:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQL.g:3922:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQL.g:3923:6: lv_inner_2_0= ruleExpression
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
                    // InternalCQL.g:3946:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQL.g:3946:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQL.g:3947:4: () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQL.g:3947:4: ()
                    // InternalCQL.g:3948:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,69,FOLLOW_14); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQL.g:3958:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQL.g:3959:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQL.g:3959:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQL.g:3960:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQL.g:3979:3: this_Atomic_7= ruleAtomic
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
    // InternalCQL.g:3991:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQL.g:3991:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQL.g:3992:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQL.g:3998:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
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
            // InternalCQL.g:4004:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQL.g:4005:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQL.g:4005:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
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
            case 70:
            case 71:
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
                    // InternalCQL.g:4006:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:4006:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:4007:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:4007:4: ()
                    // InternalCQL.g:4008:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4014:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:4015:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:4015:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:4016:6: lv_value_1_0= RULE_INT
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
                    // InternalCQL.g:4034:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQL.g:4034:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQL.g:4035:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQL.g:4035:4: ()
                    // InternalCQL.g:4036:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4042:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQL.g:4043:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQL.g:4043:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQL.g:4044:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQL.g:4062:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:4062:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:4063:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:4063:4: ()
                    // InternalCQL.g:4064:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4070:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:4071:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:4071:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:4072:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQL.g:4090:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:4090:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:4091:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:4091:4: ()
                    // InternalCQL.g:4092:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4098:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:4099:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:4099:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:4100:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:4100:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt65=2;
                    int LA65_0 = input.LA(1);

                    if ( (LA65_0==70) ) {
                        alt65=1;
                    }
                    else if ( (LA65_0==71) ) {
                        alt65=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 65, 0, input);

                        throw nvae;
                    }
                    switch (alt65) {
                        case 1 :
                            // InternalCQL.g:4101:7: lv_value_7_1= 'TRUE'
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
                            // InternalCQL.g:4112:7: lv_value_7_2= 'FALSE'
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
                    // InternalCQL.g:4127:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQL.g:4127:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQL.g:4128:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQL.g:4128:4: ()
                    // InternalCQL.g:4129:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4135:4: ( ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt66=2;
                    int LA66_0 = input.LA(1);

                    if ( (LA66_0==RULE_ID) ) {
                        switch ( input.LA(2) ) {
                        case 27:
                            {
                            int LA66_2 = input.LA(3);

                            if ( (LA66_2==RULE_ID) ) {
                                int LA66_5 = input.LA(4);

                                if ( (LA66_5==EOF||(LA66_5>=12 && LA66_5<=13)||LA66_5==15||LA66_5==19||LA66_5==21||LA66_5==23||(LA66_5>=38 && LA66_5<=40)||LA66_5==46||LA66_5==51||LA66_5==53||(LA66_5>=61 && LA66_5<=68)||(LA66_5>=80 && LA66_5<=81)) ) {
                                    alt66=1;
                                }
                                else if ( (LA66_5==28) ) {
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
                        case 12:
                        case 13:
                        case 15:
                        case 19:
                        case 21:
                        case 23:
                        case 38:
                        case 39:
                        case 40:
                        case 46:
                        case 51:
                        case 53:
                        case 61:
                        case 62:
                        case 63:
                        case 64:
                        case 65:
                        case 66:
                        case 67:
                        case 68:
                        case 80:
                        case 81:
                            {
                            alt66=1;
                            }
                            break;
                        case 28:
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
                            // InternalCQL.g:4136:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            {
                            // InternalCQL.g:4136:5: ( (lv_value_9_0= ruleAttributeWithoutAliasDefinition ) )
                            // InternalCQL.g:4137:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            {
                            // InternalCQL.g:4137:6: (lv_value_9_0= ruleAttributeWithoutAliasDefinition )
                            // InternalCQL.g:4138:7: lv_value_9_0= ruleAttributeWithoutAliasDefinition
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
                            // InternalCQL.g:4156:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQL.g:4156:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQL.g:4157:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQL.g:4157:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQL.g:4158:7: lv_value_10_0= ruleAttributeWithNestedStatement
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
    // InternalCQL.g:4181:1: entryRuleAtomicWithoutAttributeRef returns [EObject current=null] : iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF ;
    public final EObject entryRuleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicWithoutAttributeRef = null;


        try {
            // InternalCQL.g:4181:66: (iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF )
            // InternalCQL.g:4182:2: iv_ruleAtomicWithoutAttributeRef= ruleAtomicWithoutAttributeRef EOF
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
    // InternalCQL.g:4188:1: ruleAtomicWithoutAttributeRef returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) ) ;
    public final EObject ruleAtomicWithoutAttributeRef() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;
        Token lv_value_3_0=null;
        Token lv_value_5_0=null;
        Token lv_value_7_1=null;
        Token lv_value_7_2=null;


        	enterRule();

        try {
            // InternalCQL.g:4194:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) ) )
            // InternalCQL.g:4195:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) )
            {
            // InternalCQL.g:4195:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) )
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
            case 70:
            case 71:
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
                    // InternalCQL.g:4196:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:4196:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:4197:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:4197:4: ()
                    // InternalCQL.g:4198:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4204:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:4205:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:4205:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:4206:6: lv_value_1_0= RULE_INT
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
                    // InternalCQL.g:4224:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    {
                    // InternalCQL.g:4224:3: ( () ( (lv_value_3_0= RULE_FLOAT ) ) )
                    // InternalCQL.g:4225:4: () ( (lv_value_3_0= RULE_FLOAT ) )
                    {
                    // InternalCQL.g:4225:4: ()
                    // InternalCQL.g:4226:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4232:4: ( (lv_value_3_0= RULE_FLOAT ) )
                    // InternalCQL.g:4233:5: (lv_value_3_0= RULE_FLOAT )
                    {
                    // InternalCQL.g:4233:5: (lv_value_3_0= RULE_FLOAT )
                    // InternalCQL.g:4234:6: lv_value_3_0= RULE_FLOAT
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
                    // InternalCQL.g:4252:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:4252:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:4253:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:4253:4: ()
                    // InternalCQL.g:4254:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4260:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:4261:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:4261:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:4262:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQL.g:4280:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:4280:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:4281:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:4281:4: ()
                    // InternalCQL.g:4282:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicWithoutAttributeRefAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:4288:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:4289:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:4289:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:4290:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:4290:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt68=2;
                    int LA68_0 = input.LA(1);

                    if ( (LA68_0==70) ) {
                        alt68=1;
                    }
                    else if ( (LA68_0==71) ) {
                        alt68=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 68, 0, input);

                        throw nvae;
                    }
                    switch (alt68) {
                        case 1 :
                            // InternalCQL.g:4291:7: lv_value_7_1= 'TRUE'
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
                            // InternalCQL.g:4302:7: lv_value_7_2= 'FALSE'
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


    // $ANTLR start "entryRuleDataType"
    // InternalCQL.g:4320:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQL.g:4320:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQL.g:4321:2: iv_ruleDataType= ruleDataType EOF
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
    // InternalCQL.g:4327:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) ) ;
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
            // InternalCQL.g:4333:2: ( ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) ) )
            // InternalCQL.g:4334:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) )
            {
            // InternalCQL.g:4334:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) ) )
            // InternalCQL.g:4335:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) )
            {
            // InternalCQL.g:4335:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' ) )
            // InternalCQL.g:4336:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' )
            {
            // InternalCQL.g:4336:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'LONG' | lv_value_0_4= 'FLOAT' | lv_value_0_5= 'STRING' | lv_value_0_6= 'BOOLEAN' | lv_value_0_7= 'STARTTIMESTAMP' | lv_value_0_8= 'ENDTIMESTAMP' )
            int alt70=8;
            switch ( input.LA(1) ) {
            case 72:
                {
                alt70=1;
                }
                break;
            case 73:
                {
                alt70=2;
                }
                break;
            case 74:
                {
                alt70=3;
                }
                break;
            case 75:
                {
                alt70=4;
                }
                break;
            case 76:
                {
                alt70=5;
                }
                break;
            case 77:
                {
                alt70=6;
                }
                break;
            case 78:
                {
                alt70=7;
                }
                break;
            case 79:
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
                    // InternalCQL.g:4337:5: lv_value_0_1= 'INTEGER'
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
                    // InternalCQL.g:4348:5: lv_value_0_2= 'DOUBLE'
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
                    // InternalCQL.g:4359:5: lv_value_0_3= 'LONG'
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
                    // InternalCQL.g:4370:5: lv_value_0_4= 'FLOAT'
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
                    // InternalCQL.g:4381:5: lv_value_0_5= 'STRING'
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
                    // InternalCQL.g:4392:5: lv_value_0_6= 'BOOLEAN'
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
                    // InternalCQL.g:4403:5: lv_value_0_7= 'STARTTIMESTAMP'
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
                    // InternalCQL.g:4414:5: lv_value_0_8= 'ENDTIMESTAMP'
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
    // InternalCQL.g:4430:1: ruleCreateKeyword returns [Enumerator current=null] : ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) ) ;
    public final Enumerator ruleCreateKeyword() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCQL.g:4436:2: ( ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) ) )
            // InternalCQL.g:4437:2: ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) )
            {
            // InternalCQL.g:4437:2: ( (enumLiteral_0= 'CREATE' ) | (enumLiteral_1= 'ATTACH' ) )
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==80) ) {
                alt71=1;
            }
            else if ( (LA71_0==81) ) {
                alt71=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }
            switch (alt71) {
                case 1 :
                    // InternalCQL.g:4438:3: (enumLiteral_0= 'CREATE' )
                    {
                    // InternalCQL.g:4438:3: (enumLiteral_0= 'CREATE' )
                    // InternalCQL.g:4439:4: enumLiteral_0= 'CREATE'
                    {
                    enumLiteral_0=(Token)match(input,80,FOLLOW_2); 

                    				current = grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getCreateKeywordAccess().getCREATEEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:4446:3: (enumLiteral_1= 'ATTACH' )
                    {
                    // InternalCQL.g:4446:3: (enumLiteral_1= 'ATTACH' )
                    // InternalCQL.g:4447:4: enumLiteral_1= 'ATTACH'
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
    static final String dfa_1s = "\52\uffff";
    static final String dfa_2s = "\1\15\3\uffff\2\56\1\uffff\1\4\1\uffff\1\26\3\4\10\20\3\4\1\51\1\4\1\32\3\uffff\2\4\10\20\1\32\1\110";
    static final String dfa_3s = "\1\121\3\uffff\2\57\1\uffff\1\4\1\uffff\1\26\1\4\1\117\1\4\10\117\1\4\1\117\1\4\1\62\2\117\3\uffff\2\4\10\27\2\117";
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
            "\1\35\6\uffff\1\33\1\uffff\1\34",
            "\1\13\25\uffff\1\14\55\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24",
            "\1\37\1\36\54\uffff\1\40\1\41\1\42\1\43\1\44\1\45\1\46\1\47",
            "",
            "",
            "",
            "\1\50",
            "\1\51",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
            "\1\27\6\uffff\1\30",
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
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0028400000002002L,0x0000000000030000L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000003FE000C0F0L,0x00000000000000C0L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000003FE003C0F0L,0x00000000000000C0L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000030000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000001FE0000000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000400010L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x00000000006D0012L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x00000000002D0002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000003FE040C0F0L,0x00000000000000E0L});
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
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x000001C004008002L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x000001C000008000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x000001C000008002L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000000810020L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000000000000010L,0x000000000000FF00L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000810000L,0x000000000000FF00L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000000000L,0x000000000000FF00L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000000810000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000000002010L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000C00000000000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0040000000000010L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0600000000000000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0A00000000000000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x1000000000000002L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x4000000000000002L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x8000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0000000000000002L,0x000000000000001EL});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x000000C000000002L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000010000008002L});

}