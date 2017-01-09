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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_FLOAT_NUMBER", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "';'", "'SELECT'", "'DISTINCT'", "'*'", "','", "'FROM'", "'WHERE'", "'GROUP'", "'BY'", "'HAVING'", "'('", "')'", "'['", "']'", "'AS'", "'IN'", "'CREATE'", "'ATTACH'", "'STREAM'", "'SINK'", "'WRAPPER'", "'PROTOCOL'", "'TRANSPORT'", "'DATAHANDLER'", "'OPTIONS'", "'CHANNEL'", "':'", "'VIEW'", "'TO'", "'DROP'", "'IF'", "'EXISTS'", "'UNBOUNDED'", "'SIZE'", "'ADVANCE'", "'TIME'", "'TUPLE'", "'PARTITION'", "'OR'", "'AND'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'+'", "'-'", "'/'", "'NOT'", "'TRUE'", "'FALSE'", "'INTEGER'", "'DOUBLE'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'"
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
    public static final int RULE_FLOAT_NUMBER=7;
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

                if ( (LA1_0==13||(LA1_0>=28 && LA1_0<=30)||LA1_0==41) ) {
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
    // InternalCQL.g:107:1: ruleStatement returns [EObject current=null] : ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? ) ;
    public final EObject ruleStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_4=null;
        EObject lv_type_0_0 = null;

        EObject lv_type_1_0 = null;

        EObject lv_type_2_0 = null;

        EObject lv_type_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:113:2: ( ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? ) )
            // InternalCQL.g:114:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? )
            {
            // InternalCQL.g:114:2: ( ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )? )
            // InternalCQL.g:115:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) ) (otherlv_4= ';' )?
            {
            // InternalCQL.g:115:3: ( ( (lv_type_0_0= ruleSelect ) ) | ( (lv_type_1_0= ruleCreate ) ) | ( (lv_type_2_0= ruleStreamTo ) ) | ( (lv_type_3_0= ruleDrop ) ) )
            int alt2=4;
            switch ( input.LA(1) ) {
            case 13:
                {
                alt2=1;
                }
                break;
            case 28:
            case 29:
                {
                alt2=2;
                }
                break;
            case 30:
                {
                alt2=3;
                }
                break;
            case 41:
                {
                alt2=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

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
                    // InternalCQL.g:136:4: ( (lv_type_1_0= ruleCreate ) )
                    {
                    // InternalCQL.g:136:4: ( (lv_type_1_0= ruleCreate ) )
                    // InternalCQL.g:137:5: (lv_type_1_0= ruleCreate )
                    {
                    // InternalCQL.g:137:5: (lv_type_1_0= ruleCreate )
                    // InternalCQL.g:138:6: lv_type_1_0= ruleCreate
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeCreateParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_1_0=ruleCreate();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Create");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:156:4: ( (lv_type_2_0= ruleStreamTo ) )
                    {
                    // InternalCQL.g:156:4: ( (lv_type_2_0= ruleStreamTo ) )
                    // InternalCQL.g:157:5: (lv_type_2_0= ruleStreamTo )
                    {
                    // InternalCQL.g:157:5: (lv_type_2_0= ruleStreamTo )
                    // InternalCQL.g:158:6: lv_type_2_0= ruleStreamTo
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeStreamToParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_2_0=ruleStreamTo();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.StreamTo");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:176:4: ( (lv_type_3_0= ruleDrop ) )
                    {
                    // InternalCQL.g:176:4: ( (lv_type_3_0= ruleDrop ) )
                    // InternalCQL.g:177:5: (lv_type_3_0= ruleDrop )
                    {
                    // InternalCQL.g:177:5: (lv_type_3_0= ruleDrop )
                    // InternalCQL.g:178:6: lv_type_3_0= ruleDrop
                    {

                    						newCompositeNode(grammarAccess.getStatementAccess().getTypeDropParserRuleCall_0_3_0());
                    					
                    pushFollow(FOLLOW_4);
                    lv_type_3_0=ruleDrop();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getStatementRule());
                    						}
                    						set(
                    							current,
                    							"type",
                    							lv_type_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Drop");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:196:3: (otherlv_4= ';' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==12) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalCQL.g:197:4: otherlv_4= ';'
                    {
                    otherlv_4=(Token)match(input,12,FOLLOW_2); 

                    				newLeafNode(otherlv_4, grammarAccess.getStatementAccess().getSemicolonKeyword_1());
                    			

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
    // InternalCQL.g:206:1: entryRuleSelect returns [EObject current=null] : iv_ruleSelect= ruleSelect EOF ;
    public final EObject entryRuleSelect() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelect = null;


        try {
            // InternalCQL.g:206:47: (iv_ruleSelect= ruleSelect EOF )
            // InternalCQL.g:207:2: iv_ruleSelect= ruleSelect EOF
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
    // InternalCQL.g:213:1: ruleSelect returns [EObject current=null] : ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? ) ;
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
            // InternalCQL.g:219:2: ( ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? ) )
            // InternalCQL.g:220:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? )
            {
            // InternalCQL.g:220:2: ( ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )? )
            // InternalCQL.g:221:3: ( (lv_name_0_0= 'SELECT' ) ) ( (lv_distinct_1_0= 'DISTINCT' ) )? (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) ) (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* ) (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )? (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )? (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )?
            {
            // InternalCQL.g:221:3: ( (lv_name_0_0= 'SELECT' ) )
            // InternalCQL.g:222:4: (lv_name_0_0= 'SELECT' )
            {
            // InternalCQL.g:222:4: (lv_name_0_0= 'SELECT' )
            // InternalCQL.g:223:5: lv_name_0_0= 'SELECT'
            {
            lv_name_0_0=(Token)match(input,13,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getSelectAccess().getNameSELECTKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSelectRule());
            					}
            					setWithLastConsumed(current, "name", lv_name_0_0, "SELECT");
            				

            }


            }

            // InternalCQL.g:235:3: ( (lv_distinct_1_0= 'DISTINCT' ) )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==14) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalCQL.g:236:4: (lv_distinct_1_0= 'DISTINCT' )
                    {
                    // InternalCQL.g:236:4: (lv_distinct_1_0= 'DISTINCT' )
                    // InternalCQL.g:237:5: lv_distinct_1_0= 'DISTINCT'
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

            // InternalCQL.g:249:3: (otherlv_2= '*' | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) ) )
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
                    // InternalCQL.g:250:4: otherlv_2= '*'
                    {
                    otherlv_2=(Token)match(input,15,FOLLOW_7); 

                    				newLeafNode(otherlv_2, grammarAccess.getSelectAccess().getAsteriskKeyword_2_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCQL.g:255:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) )
                    {
                    // InternalCQL.g:255:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) ) )
                    // InternalCQL.g:256:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) )
                    {
                    // InternalCQL.g:256:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?) )
                    // InternalCQL.g:257:6: ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?)
                    {
                     
                    					  getUnorderedGroupHelper().enter(grammarAccess.getSelectAccess().getUnorderedGroup_2_1());
                    					
                    // InternalCQL.g:260:6: ( ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?)
                    // InternalCQL.g:261:7: ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+ {...}?
                    {
                    // InternalCQL.g:261:7: ( ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) ) | ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) ) )+
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

                                if ( LA9_4 == 22 && getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) ) {
                                    alt9=3;
                                }
                                else if ( ( LA9_4 == RULE_ID || LA9_4 >= 16 && LA9_4 <= 17 || LA9_4 == 26 ) && getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) {
                                    alt9=2;
                                }


                            }


                        }


                        switch (alt9) {
                    	case 1 :
                    	    // InternalCQL.g:262:5: ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:262:5: ({...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ ) )
                    	    // InternalCQL.g:263:6: {...}? => ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0)");
                    	    }
                    	    // InternalCQL.g:263:106: ( ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+ )
                    	    // InternalCQL.g:264:7: ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 0);
                    	    						
                    	    // InternalCQL.g:267:10: ({...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) ) )+
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
                    	    	    // InternalCQL.g:267:11: {...}? => ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect", "true");
                    	    	    }
                    	    	    // InternalCQL.g:267:20: ( ( (lv_attributes_4_0= ruleAttribute ) ) | ( (lv_aggregations_5_0= ruleAggregation ) ) )
                    	    	    int alt5=2;
                    	    	    int LA5_0 = input.LA(1);

                    	    	    if ( (LA5_0==RULE_ID) ) {
                    	    	        int LA5_1 = input.LA(2);

                    	    	        if ( (LA5_1==RULE_ID||(LA5_1>=16 && LA5_1<=17)||LA5_1==26) ) {
                    	    	            alt5=1;
                    	    	        }
                    	    	        else if ( (LA5_1==22) ) {
                    	    	            alt5=2;
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
                    	    	            // InternalCQL.g:267:21: ( (lv_attributes_4_0= ruleAttribute ) )
                    	    	            {
                    	    	            // InternalCQL.g:267:21: ( (lv_attributes_4_0= ruleAttribute ) )
                    	    	            // InternalCQL.g:268:11: (lv_attributes_4_0= ruleAttribute )
                    	    	            {
                    	    	            // InternalCQL.g:268:11: (lv_attributes_4_0= ruleAttribute )
                    	    	            // InternalCQL.g:269:12: lv_attributes_4_0= ruleAttribute
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
                    	    	            // InternalCQL.g:287:10: ( (lv_aggregations_5_0= ruleAggregation ) )
                    	    	            {
                    	    	            // InternalCQL.g:287:10: ( (lv_aggregations_5_0= ruleAggregation ) )
                    	    	            // InternalCQL.g:288:11: (lv_aggregations_5_0= ruleAggregation )
                    	    	            {
                    	    	            // InternalCQL.g:288:11: (lv_aggregations_5_0= ruleAggregation )
                    	    	            // InternalCQL.g:289:12: lv_aggregations_5_0= ruleAggregation
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
                    	    // InternalCQL.g:312:5: ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:312:5: ({...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ ) )
                    	    // InternalCQL.g:313:6: {...}? => ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1)");
                    	    }
                    	    // InternalCQL.g:313:106: ( ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+ )
                    	    // InternalCQL.g:314:7: ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 1);
                    	    						
                    	    // InternalCQL.g:317:10: ({...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) ) )+
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
                    	    	    // InternalCQL.g:317:11: {...}? => (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect", "true");
                    	    	    }
                    	    	    // InternalCQL.g:317:20: (otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) ) )
                    	    	    // InternalCQL.g:317:21: otherlv_6= ',' ( (lv_attributes_7_0= ruleAttribute ) )
                    	    	    {
                    	    	    otherlv_6=(Token)match(input,16,FOLLOW_9); 

                    	    	    										newLeafNode(otherlv_6, grammarAccess.getSelectAccess().getCommaKeyword_2_1_1_0());
                    	    	    									
                    	    	    // InternalCQL.g:321:10: ( (lv_attributes_7_0= ruleAttribute ) )
                    	    	    // InternalCQL.g:322:11: (lv_attributes_7_0= ruleAttribute )
                    	    	    {
                    	    	    // InternalCQL.g:322:11: (lv_attributes_7_0= ruleAttribute )
                    	    	    // InternalCQL.g:323:12: lv_attributes_7_0= ruleAttribute
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
                    	    // InternalCQL.g:346:5: ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) )
                    	    {
                    	    // InternalCQL.g:346:5: ({...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ ) )
                    	    // InternalCQL.g:347:6: {...}? => ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2) ) {
                    	        throw new FailedPredicateException(input, "ruleSelect", "getUnorderedGroupHelper().canSelect(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2)");
                    	    }
                    	    // InternalCQL.g:347:106: ( ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+ )
                    	    // InternalCQL.g:348:7: ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getSelectAccess().getUnorderedGroup_2_1(), 2);
                    	    						
                    	    // InternalCQL.g:351:10: ({...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) ) )+
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
                    	    	    // InternalCQL.g:351:11: {...}? => (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    	    {
                    	    	    if ( !((true)) ) {
                    	    	        throw new FailedPredicateException(input, "ruleSelect", "true");
                    	    	    }
                    	    	    // InternalCQL.g:351:20: (otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) ) )
                    	    	    // InternalCQL.g:351:21: otherlv_8= ',' ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    	    {
                    	    	    otherlv_8=(Token)match(input,16,FOLLOW_9); 

                    	    	    										newLeafNode(otherlv_8, grammarAccess.getSelectAccess().getCommaKeyword_2_1_2_0());
                    	    	    									
                    	    	    // InternalCQL.g:355:10: ( (lv_aggregations_9_0= ruleAggregation ) )
                    	    	    // InternalCQL.g:356:11: (lv_aggregations_9_0= ruleAggregation )
                    	    	    {
                    	    	    // InternalCQL.g:356:11: (lv_aggregations_9_0= ruleAggregation )
                    	    	    // InternalCQL.g:357:12: lv_aggregations_9_0= ruleAggregation
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

            // InternalCQL.g:389:3: (otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )* )
            // InternalCQL.g:390:4: otherlv_10= 'FROM' ( (lv_sources_11_0= ruleSource ) )+ (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )*
            {
            otherlv_10=(Token)match(input,17,FOLLOW_10); 

            				newLeafNode(otherlv_10, grammarAccess.getSelectAccess().getFROMKeyword_3_0());
            			
            // InternalCQL.g:394:4: ( (lv_sources_11_0= ruleSource ) )+
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
            	    // InternalCQL.g:395:5: (lv_sources_11_0= ruleSource )
            	    {
            	    // InternalCQL.g:395:5: (lv_sources_11_0= ruleSource )
            	    // InternalCQL.g:396:6: lv_sources_11_0= ruleSource
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

            // InternalCQL.g:413:4: (otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) ) )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==16) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalCQL.g:414:5: otherlv_12= ',' ( (lv_sources_13_0= ruleSource ) )
            	    {
            	    otherlv_12=(Token)match(input,16,FOLLOW_10); 

            	    					newLeafNode(otherlv_12, grammarAccess.getSelectAccess().getCommaKeyword_3_2_0());
            	    				
            	    // InternalCQL.g:418:5: ( (lv_sources_13_0= ruleSource ) )
            	    // InternalCQL.g:419:6: (lv_sources_13_0= ruleSource )
            	    {
            	    // InternalCQL.g:419:6: (lv_sources_13_0= ruleSource )
            	    // InternalCQL.g:420:7: lv_sources_13_0= ruleSource
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

            // InternalCQL.g:439:3: (otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==18) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCQL.g:440:4: otherlv_14= 'WHERE' ( (lv_predicates_15_0= ruleExpressionsModel ) )
                    {
                    otherlv_14=(Token)match(input,18,FOLLOW_13); 

                    				newLeafNode(otherlv_14, grammarAccess.getSelectAccess().getWHEREKeyword_4_0());
                    			
                    // InternalCQL.g:444:4: ( (lv_predicates_15_0= ruleExpressionsModel ) )
                    // InternalCQL.g:445:5: (lv_predicates_15_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:445:5: (lv_predicates_15_0= ruleExpressionsModel )
                    // InternalCQL.g:446:6: lv_predicates_15_0= ruleExpressionsModel
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

            // InternalCQL.g:464:3: (otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==19) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalCQL.g:465:4: otherlv_16= 'GROUP' otherlv_17= 'BY' ( (lv_order_18_0= ruleAttribute ) )+ (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )*
                    {
                    otherlv_16=(Token)match(input,19,FOLLOW_15); 

                    				newLeafNode(otherlv_16, grammarAccess.getSelectAccess().getGROUPKeyword_5_0());
                    			
                    otherlv_17=(Token)match(input,20,FOLLOW_9); 

                    				newLeafNode(otherlv_17, grammarAccess.getSelectAccess().getBYKeyword_5_1());
                    			
                    // InternalCQL.g:473:4: ( (lv_order_18_0= ruleAttribute ) )+
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
                    	    // InternalCQL.g:474:5: (lv_order_18_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:474:5: (lv_order_18_0= ruleAttribute )
                    	    // InternalCQL.g:475:6: lv_order_18_0= ruleAttribute
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

                    // InternalCQL.g:492:4: (otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) ) )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==16) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // InternalCQL.g:493:5: otherlv_19= ',' ( (lv_order_20_0= ruleAttribute ) )
                    	    {
                    	    otherlv_19=(Token)match(input,16,FOLLOW_9); 

                    	    					newLeafNode(otherlv_19, grammarAccess.getSelectAccess().getCommaKeyword_5_3_0());
                    	    				
                    	    // InternalCQL.g:497:5: ( (lv_order_20_0= ruleAttribute ) )
                    	    // InternalCQL.g:498:6: (lv_order_20_0= ruleAttribute )
                    	    {
                    	    // InternalCQL.g:498:6: (lv_order_20_0= ruleAttribute )
                    	    // InternalCQL.g:499:7: lv_order_20_0= ruleAttribute
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

            // InternalCQL.g:518:3: (otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) ) )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==21) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // InternalCQL.g:519:4: otherlv_21= 'HAVING' ( (lv_having_22_0= ruleExpressionsModel ) )
                    {
                    otherlv_21=(Token)match(input,21,FOLLOW_13); 

                    				newLeafNode(otherlv_21, grammarAccess.getSelectAccess().getHAVINGKeyword_6_0());
                    			
                    // InternalCQL.g:523:4: ( (lv_having_22_0= ruleExpressionsModel ) )
                    // InternalCQL.g:524:5: (lv_having_22_0= ruleExpressionsModel )
                    {
                    // InternalCQL.g:524:5: (lv_having_22_0= ruleExpressionsModel )
                    // InternalCQL.g:525:6: lv_having_22_0= ruleExpressionsModel
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
    // InternalCQL.g:547:1: entryRuleNestedStatement returns [EObject current=null] : iv_ruleNestedStatement= ruleNestedStatement EOF ;
    public final EObject entryRuleNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNestedStatement = null;


        try {
            // InternalCQL.g:547:56: (iv_ruleNestedStatement= ruleNestedStatement EOF )
            // InternalCQL.g:548:2: iv_ruleNestedStatement= ruleNestedStatement EOF
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
    // InternalCQL.g:554:1: ruleNestedStatement returns [EObject current=null] : (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) ;
    public final EObject ruleNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_Select_1 = null;



        	enterRule();

        try {
            // InternalCQL.g:560:2: ( (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' ) )
            // InternalCQL.g:561:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            {
            // InternalCQL.g:561:2: (otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')' )
            // InternalCQL.g:562:3: otherlv_0= '(' this_Select_1= ruleSelect otherlv_2= ')'
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
    // InternalCQL.g:582:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalCQL.g:582:47: (iv_ruleSource= ruleSource EOF )
            // InternalCQL.g:583:2: iv_ruleSource= ruleSource EOF
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
    // InternalCQL.g:589:1: ruleSource returns [EObject current=null] : ( ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        AntlrDatatypeRuleToken lv_unbounded_2_0 = null;

        EObject lv_time_3_0 = null;

        EObject lv_tuple_4_0 = null;

        EObject lv_nested_6_0 = null;

        EObject lv_alias_8_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:595:2: ( ( ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:596:2: ( ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:596:2: ( ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )? )
            // InternalCQL.g:597:3: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) ) (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:597:3: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? ) | ( (lv_nested_6_0= ruleNestedStatement ) ) )
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==RULE_ID) ) {
                alt20=1;
            }
            else if ( (LA20_0==22) ) {
                alt20=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQL.g:598:4: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? )
                    {
                    // InternalCQL.g:598:4: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )? )
                    // InternalCQL.g:599:5: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )?
                    {
                    // InternalCQL.g:599:5: ( (lv_name_0_0= RULE_ID ) )
                    // InternalCQL.g:600:6: (lv_name_0_0= RULE_ID )
                    {
                    // InternalCQL.g:600:6: (lv_name_0_0= RULE_ID )
                    // InternalCQL.g:601:7: lv_name_0_0= RULE_ID
                    {
                    lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_20); 

                    							newLeafNode(lv_name_0_0, grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0_0_0_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getSourceRule());
                    							}
                    							setWithLastConsumed(
                    								current,
                    								"name",
                    								lv_name_0_0,
                    								"org.eclipse.xtext.common.Terminals.ID");
                    						

                    }


                    }

                    // InternalCQL.g:617:5: (otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']' )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==24) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // InternalCQL.g:618:6: otherlv_1= '[' ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) ) otherlv_5= ']'
                            {
                            otherlv_1=(Token)match(input,24,FOLLOW_21); 

                            						newLeafNode(otherlv_1, grammarAccess.getSourceAccess().getLeftSquareBracketKeyword_0_0_1_0());
                            					
                            // InternalCQL.g:622:6: ( ( (lv_unbounded_2_0= ruleWindow_Unbounded ) ) | ( (lv_time_3_0= ruleWindow_Timebased ) ) | ( (lv_tuple_4_0= ruleWindow_Tuplebased ) ) )
                            int alt18=3;
                            int LA18_0 = input.LA(1);

                            if ( (LA18_0==44) ) {
                                alt18=1;
                            }
                            else if ( (LA18_0==45) ) {
                                int LA18_2 = input.LA(2);

                                if ( (LA18_2==RULE_INT) ) {
                                    int LA18_3 = input.LA(3);

                                    if ( (LA18_3==RULE_ID) ) {
                                        alt18=2;
                                    }
                                    else if ( (LA18_3==46||LA18_3==48) ) {
                                        alt18=3;
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
                                    // InternalCQL.g:623:7: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    {
                                    // InternalCQL.g:623:7: ( (lv_unbounded_2_0= ruleWindow_Unbounded ) )
                                    // InternalCQL.g:624:8: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    {
                                    // InternalCQL.g:624:8: (lv_unbounded_2_0= ruleWindow_Unbounded )
                                    // InternalCQL.g:625:9: lv_unbounded_2_0= ruleWindow_Unbounded
                                    {

                                    									newCompositeNode(grammarAccess.getSourceAccess().getUnboundedWindow_UnboundedParserRuleCall_0_0_1_1_0_0());
                                    								
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
                                    // InternalCQL.g:643:7: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    {
                                    // InternalCQL.g:643:7: ( (lv_time_3_0= ruleWindow_Timebased ) )
                                    // InternalCQL.g:644:8: (lv_time_3_0= ruleWindow_Timebased )
                                    {
                                    // InternalCQL.g:644:8: (lv_time_3_0= ruleWindow_Timebased )
                                    // InternalCQL.g:645:9: lv_time_3_0= ruleWindow_Timebased
                                    {

                                    									newCompositeNode(grammarAccess.getSourceAccess().getTimeWindow_TimebasedParserRuleCall_0_0_1_1_1_0());
                                    								
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
                                    // InternalCQL.g:663:7: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    {
                                    // InternalCQL.g:663:7: ( (lv_tuple_4_0= ruleWindow_Tuplebased ) )
                                    // InternalCQL.g:664:8: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    {
                                    // InternalCQL.g:664:8: (lv_tuple_4_0= ruleWindow_Tuplebased )
                                    // InternalCQL.g:665:9: lv_tuple_4_0= ruleWindow_Tuplebased
                                    {

                                    									newCompositeNode(grammarAccess.getSourceAccess().getTupleWindow_TuplebasedParserRuleCall_0_0_1_1_2_0());
                                    								
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

                            						newLeafNode(otherlv_5, grammarAccess.getSourceAccess().getRightSquareBracketKeyword_0_0_1_2());
                            					

                            }
                            break;

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:690:4: ( (lv_nested_6_0= ruleNestedStatement ) )
                    {
                    // InternalCQL.g:690:4: ( (lv_nested_6_0= ruleNestedStatement ) )
                    // InternalCQL.g:691:5: (lv_nested_6_0= ruleNestedStatement )
                    {
                    // InternalCQL.g:691:5: (lv_nested_6_0= ruleNestedStatement )
                    // InternalCQL.g:692:6: lv_nested_6_0= ruleNestedStatement
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getNestedNestedStatementParserRuleCall_0_1_0());
                    					
                    pushFollow(FOLLOW_23);
                    lv_nested_6_0=ruleNestedStatement();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
                    						}
                    						set(
                    							current,
                    							"nested",
                    							lv_nested_6_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.NestedStatement");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:710:3: (otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) ) )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==26) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalCQL.g:711:4: otherlv_7= 'AS' ( (lv_alias_8_0= ruleAlias ) )
                    {
                    otherlv_7=(Token)match(input,26,FOLLOW_9); 

                    				newLeafNode(otherlv_7, grammarAccess.getSourceAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:715:4: ( (lv_alias_8_0= ruleAlias ) )
                    // InternalCQL.g:716:5: (lv_alias_8_0= ruleAlias )
                    {
                    // InternalCQL.g:716:5: (lv_alias_8_0= ruleAlias )
                    // InternalCQL.g:717:6: lv_alias_8_0= ruleAlias
                    {

                    						newCompositeNode(grammarAccess.getSourceAccess().getAliasAliasParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_alias_8_0=ruleAlias();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSourceRule());
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
    // $ANTLR end "ruleSource"


    // $ANTLR start "entryRuleAttribute"
    // InternalCQL.g:739:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCQL.g:739:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCQL.g:740:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCQL.g:746:1: ruleAttribute returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_alias_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:752:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:753:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:753:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )? )
            // InternalCQL.g:754:3: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:754:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:755:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:755:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:756:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_23); 

            					newLeafNode(lv_name_0_0, grammarAccess.getAttributeAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalCQL.g:772:3: (otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) ) )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==26) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalCQL.g:773:4: otherlv_1= 'AS' ( (lv_alias_2_0= ruleAlias ) )
                    {
                    otherlv_1=(Token)match(input,26,FOLLOW_9); 

                    				newLeafNode(otherlv_1, grammarAccess.getAttributeAccess().getASKeyword_1_0());
                    			
                    // InternalCQL.g:777:4: ( (lv_alias_2_0= ruleAlias ) )
                    // InternalCQL.g:778:5: (lv_alias_2_0= ruleAlias )
                    {
                    // InternalCQL.g:778:5: (lv_alias_2_0= ruleAlias )
                    // InternalCQL.g:779:6: lv_alias_2_0= ruleAlias
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
    // InternalCQL.g:801:1: entryRuleAttributeWithoutAlias returns [EObject current=null] : iv_ruleAttributeWithoutAlias= ruleAttributeWithoutAlias EOF ;
    public final EObject entryRuleAttributeWithoutAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithoutAlias = null;


        try {
            // InternalCQL.g:801:62: (iv_ruleAttributeWithoutAlias= ruleAttributeWithoutAlias EOF )
            // InternalCQL.g:802:2: iv_ruleAttributeWithoutAlias= ruleAttributeWithoutAlias EOF
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
    // InternalCQL.g:808:1: ruleAttributeWithoutAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAttributeWithoutAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:814:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:815:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:815:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:816:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:816:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:817:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getAttributeWithoutAliasAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getAttributeWithoutAliasRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

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


    // $ANTLR start "entryRuleAttributeWithNestedStatement"
    // InternalCQL.g:836:1: entryRuleAttributeWithNestedStatement returns [EObject current=null] : iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF ;
    public final EObject entryRuleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttributeWithNestedStatement = null;


        try {
            // InternalCQL.g:836:69: (iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF )
            // InternalCQL.g:837:2: iv_ruleAttributeWithNestedStatement= ruleAttributeWithNestedStatement EOF
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
    // InternalCQL.g:843:1: ruleAttributeWithNestedStatement returns [EObject current=null] : ( ( (lv_value_0_0= ruleAttribute ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) ;
    public final EObject ruleAttributeWithNestedStatement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_value_0_0 = null;

        EObject lv_nested_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:849:2: ( ( ( (lv_value_0_0= ruleAttribute ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) ) )
            // InternalCQL.g:850:2: ( ( (lv_value_0_0= ruleAttribute ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            {
            // InternalCQL.g:850:2: ( ( (lv_value_0_0= ruleAttribute ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) ) )
            // InternalCQL.g:851:3: ( (lv_value_0_0= ruleAttribute ) ) otherlv_1= 'IN' ( (lv_nested_2_0= ruleNestedStatement ) )
            {
            // InternalCQL.g:851:3: ( (lv_value_0_0= ruleAttribute ) )
            // InternalCQL.g:852:4: (lv_value_0_0= ruleAttribute )
            {
            // InternalCQL.g:852:4: (lv_value_0_0= ruleAttribute )
            // InternalCQL.g:853:5: lv_value_0_0= ruleAttribute
            {

            					newCompositeNode(grammarAccess.getAttributeWithNestedStatementAccess().getValueAttributeParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_24);
            lv_value_0_0=ruleAttribute();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAttributeWithNestedStatementRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_0_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,27,FOLLOW_10); 

            			newLeafNode(otherlv_1, grammarAccess.getAttributeWithNestedStatementAccess().getINKeyword_1());
            		
            // InternalCQL.g:874:3: ( (lv_nested_2_0= ruleNestedStatement ) )
            // InternalCQL.g:875:4: (lv_nested_2_0= ruleNestedStatement )
            {
            // InternalCQL.g:875:4: (lv_nested_2_0= ruleNestedStatement )
            // InternalCQL.g:876:5: lv_nested_2_0= ruleNestedStatement
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
    // InternalCQL.g:897:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalCQL.g:897:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalCQL.g:898:2: iv_ruleAggregation= ruleAggregation EOF
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
    // InternalCQL.g:904:1: ruleAggregation returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) ;
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
            // InternalCQL.g:910:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? ) )
            // InternalCQL.g:911:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            {
            // InternalCQL.g:911:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )? )
            // InternalCQL.g:912:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) ) otherlv_3= ')' (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            {
            // InternalCQL.g:912:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:913:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:913:4: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:914:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_25); 

            					newLeafNode(lv_name_0_0, grammarAccess.getAggregationAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAggregationRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,22,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregationAccess().getLeftParenthesisKeyword_1());
            		
            // InternalCQL.g:934:3: ( (lv_attribute_2_0= ruleAttributeWithoutAlias ) )
            // InternalCQL.g:935:4: (lv_attribute_2_0= ruleAttributeWithoutAlias )
            {
            // InternalCQL.g:935:4: (lv_attribute_2_0= ruleAttributeWithoutAlias )
            // InternalCQL.g:936:5: lv_attribute_2_0= ruleAttributeWithoutAlias
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
            		
            // InternalCQL.g:957:3: (otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) ) )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==26) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalCQL.g:958:4: otherlv_4= 'AS' ( (lv_alias_5_0= ruleAlias ) )
                    {
                    otherlv_4=(Token)match(input,26,FOLLOW_9); 

                    				newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getASKeyword_4_0());
                    			
                    // InternalCQL.g:962:4: ( (lv_alias_5_0= ruleAlias ) )
                    // InternalCQL.g:963:5: (lv_alias_5_0= ruleAlias )
                    {
                    // InternalCQL.g:963:5: (lv_alias_5_0= ruleAlias )
                    // InternalCQL.g:964:6: lv_alias_5_0= ruleAlias
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
    // InternalCQL.g:986:1: entryRuleAlias returns [EObject current=null] : iv_ruleAlias= ruleAlias EOF ;
    public final EObject entryRuleAlias() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAlias = null;


        try {
            // InternalCQL.g:986:46: (iv_ruleAlias= ruleAlias EOF )
            // InternalCQL.g:987:2: iv_ruleAlias= ruleAlias EOF
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
    // InternalCQL.g:993:1: ruleAlias returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAlias() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalCQL.g:999:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalCQL.g:1000:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalCQL.g:1000:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalCQL.g:1001:3: (lv_name_0_0= RULE_ID )
            {
            // InternalCQL.g:1001:3: (lv_name_0_0= RULE_ID )
            // InternalCQL.g:1002:4: lv_name_0_0= RULE_ID
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
            					"org.eclipse.xtext.common.Terminals.ID");
            			

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


    // $ANTLR start "entryRuleCreate"
    // InternalCQL.g:1021:1: entryRuleCreate returns [EObject current=null] : iv_ruleCreate= ruleCreate EOF ;
    public final EObject entryRuleCreate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreate = null;


        try {
            // InternalCQL.g:1021:47: (iv_ruleCreate= ruleCreate EOF )
            // InternalCQL.g:1022:2: iv_ruleCreate= ruleCreate EOF
            {
             newCompositeNode(grammarAccess.getCreateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCreate=ruleCreate();

            state._fsp--;

             current =iv_ruleCreate; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCreate"


    // $ANTLR start "ruleCreate"
    // InternalCQL.g:1028:1: ruleCreate returns [EObject current=null] : ( ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) ) ) ;
    public final EObject ruleCreate() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_1=null;
        Token lv_name_0_2=null;
        EObject lv_channelformat_1_0 = null;

        EObject lv_accessframework_2_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1034:2: ( ( ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) ) ) )
            // InternalCQL.g:1035:2: ( ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) ) )
            {
            // InternalCQL.g:1035:2: ( ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) ) )
            // InternalCQL.g:1036:3: ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) ) ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) )
            {
            // InternalCQL.g:1036:3: ( ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) ) )
            // InternalCQL.g:1037:4: ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) )
            {
            // InternalCQL.g:1037:4: ( (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' ) )
            // InternalCQL.g:1038:5: (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' )
            {
            // InternalCQL.g:1038:5: (lv_name_0_1= 'CREATE' | lv_name_0_2= 'ATTACH' )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==28) ) {
                alt24=1;
            }
            else if ( (LA24_0==29) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // InternalCQL.g:1039:6: lv_name_0_1= 'CREATE'
                    {
                    lv_name_0_1=(Token)match(input,28,FOLLOW_26); 

                    						newLeafNode(lv_name_0_1, grammarAccess.getCreateAccess().getNameCREATEKeyword_0_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1050:6: lv_name_0_2= 'ATTACH'
                    {
                    lv_name_0_2=(Token)match(input,29,FOLLOW_26); 

                    						newLeafNode(lv_name_0_2, grammarAccess.getCreateAccess().getNameATTACHKeyword_0_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCreateRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_0_2, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:1063:3: ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) )
            int alt25=2;
            alt25 = dfa25.predict(input);
            switch (alt25) {
                case 1 :
                    // InternalCQL.g:1064:4: ( (lv_channelformat_1_0= ruleChannelFormat ) )
                    {
                    // InternalCQL.g:1064:4: ( (lv_channelformat_1_0= ruleChannelFormat ) )
                    // InternalCQL.g:1065:5: (lv_channelformat_1_0= ruleChannelFormat )
                    {
                    // InternalCQL.g:1065:5: (lv_channelformat_1_0= ruleChannelFormat )
                    // InternalCQL.g:1066:6: lv_channelformat_1_0= ruleChannelFormat
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getChannelformatChannelFormatParserRuleCall_1_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_channelformat_1_0=ruleChannelFormat();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"channelformat",
                    							lv_channelformat_1_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ChannelFormat");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1084:4: ( (lv_accessframework_2_0= ruleAccessFramework ) )
                    {
                    // InternalCQL.g:1084:4: ( (lv_accessframework_2_0= ruleAccessFramework ) )
                    // InternalCQL.g:1085:5: (lv_accessframework_2_0= ruleAccessFramework )
                    {
                    // InternalCQL.g:1085:5: (lv_accessframework_2_0= ruleAccessFramework )
                    // InternalCQL.g:1086:6: lv_accessframework_2_0= ruleAccessFramework
                    {

                    						newCompositeNode(grammarAccess.getCreateAccess().getAccessframeworkAccessFrameworkParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_accessframework_2_0=ruleAccessFramework();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCreateRule());
                    						}
                    						set(
                    							current,
                    							"accessframework",
                    							lv_accessframework_2_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.AccessFramework");
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
    // $ANTLR end "ruleCreate"


    // $ANTLR start "entryRuleAccessFramework"
    // InternalCQL.g:1108:1: entryRuleAccessFramework returns [EObject current=null] : iv_ruleAccessFramework= ruleAccessFramework EOF ;
    public final EObject entryRuleAccessFramework() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAccessFramework = null;


        try {
            // InternalCQL.g:1108:56: (iv_ruleAccessFramework= ruleAccessFramework EOF )
            // InternalCQL.g:1109:2: iv_ruleAccessFramework= ruleAccessFramework EOF
            {
             newCompositeNode(grammarAccess.getAccessFrameworkRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAccessFramework=ruleAccessFramework();

            state._fsp--;

             current =iv_ruleAccessFramework; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAccessFramework"


    // $ANTLR start "ruleAccessFramework"
    // InternalCQL.g:1115:1: ruleAccessFramework returns [EObject current=null] : ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' ) ;
    public final EObject ruleAccessFramework() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_1=null;
        Token lv_type_0_2=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_5=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token lv_wrapper_10_0=null;
        Token otherlv_11=null;
        Token lv_protocol_12_0=null;
        Token otherlv_13=null;
        Token lv_transport_14_0=null;
        Token otherlv_15=null;
        Token lv_datahandler_16_0=null;
        Token otherlv_17=null;
        Token otherlv_18=null;
        Token lv_keys_19_0=null;
        Token lv_values_20_0=null;
        Token otherlv_21=null;
        Token lv_keys_22_0=null;
        Token lv_values_23_0=null;
        Token otherlv_24=null;
        EObject lv_attributes_3_0 = null;

        EObject lv_datatypes_4_0 = null;

        EObject lv_attributes_6_0 = null;

        EObject lv_datatypes_7_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1121:2: ( ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' ) )
            // InternalCQL.g:1122:2: ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' )
            {
            // InternalCQL.g:1122:2: ( ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')' )
            // InternalCQL.g:1123:3: ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) ) ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'WRAPPER' ( (lv_wrapper_10_0= RULE_STRING ) ) otherlv_11= 'PROTOCOL' ( (lv_protocol_12_0= RULE_STRING ) ) otherlv_13= 'TRANSPORT' ( (lv_transport_14_0= RULE_STRING ) ) otherlv_15= 'DATAHANDLER' ( (lv_datahandler_16_0= RULE_STRING ) ) otherlv_17= 'OPTIONS' otherlv_18= '(' ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+ (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )? otherlv_24= ')'
            {
            // InternalCQL.g:1123:3: ( ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) ) )
            // InternalCQL.g:1124:4: ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) )
            {
            // InternalCQL.g:1124:4: ( (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' ) )
            // InternalCQL.g:1125:5: (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' )
            {
            // InternalCQL.g:1125:5: (lv_type_0_1= 'STREAM' | lv_type_0_2= 'SINK' )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==30) ) {
                alt26=1;
            }
            else if ( (LA26_0==31) ) {
                alt26=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // InternalCQL.g:1126:6: lv_type_0_1= 'STREAM'
                    {
                    lv_type_0_1=(Token)match(input,30,FOLLOW_9); 

                    						newLeafNode(lv_type_0_1, grammarAccess.getAccessFrameworkAccess().getTypeSTREAMKeyword_0_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_0_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCQL.g:1137:6: lv_type_0_2= 'SINK'
                    {
                    lv_type_0_2=(Token)match(input,31,FOLLOW_9); 

                    						newLeafNode(lv_type_0_2, grammarAccess.getAccessFrameworkAccess().getTypeSINKKeyword_0_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						setWithLastConsumed(current, "type", lv_type_0_2, null);
                    					

                    }
                    break;

            }


            }


            }

            // InternalCQL.g:1150:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:1151:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:1151:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:1152:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_25); 

            					newLeafNode(lv_name_1_0, grammarAccess.getAccessFrameworkAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,22,FOLLOW_9); 

            			newLeafNode(otherlv_2, grammarAccess.getAccessFrameworkAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQL.g:1172:3: ( (lv_attributes_3_0= ruleAttribute ) )+
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
            	    // InternalCQL.g:1173:4: (lv_attributes_3_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1173:4: (lv_attributes_3_0= ruleAttribute )
            	    // InternalCQL.g:1174:5: lv_attributes_3_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAccessFrameworkAccess().getAttributesAttributeParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_27);
            	    lv_attributes_3_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAccessFrameworkRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_3_0,
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

            // InternalCQL.g:1191:3: ( (lv_datatypes_4_0= ruleDataType ) )+
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
            	    // InternalCQL.g:1192:4: (lv_datatypes_4_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1192:4: (lv_datatypes_4_0= ruleDataType )
            	    // InternalCQL.g:1193:5: lv_datatypes_4_0= ruleDataType
            	    {

            	    					newCompositeNode(grammarAccess.getAccessFrameworkAccess().getDatatypesDataTypeParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_28);
            	    lv_datatypes_4_0=ruleDataType();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAccessFrameworkRule());
            	    					}
            	    					add(
            	    						current,
            	    						"datatypes",
            	    						lv_datatypes_4_0,
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

            // InternalCQL.g:1210:3: (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( (LA29_0==16) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // InternalCQL.g:1211:4: otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) )
            	    {
            	    otherlv_5=(Token)match(input,16,FOLLOW_9); 

            	    				newLeafNode(otherlv_5, grammarAccess.getAccessFrameworkAccess().getCommaKeyword_5_0());
            	    			
            	    // InternalCQL.g:1215:4: ( (lv_attributes_6_0= ruleAttribute ) )
            	    // InternalCQL.g:1216:5: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1216:5: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCQL.g:1217:6: lv_attributes_6_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getAccessFrameworkAccess().getAttributesAttributeParserRuleCall_5_1_0());
            	    					
            	    pushFollow(FOLLOW_29);
            	    lv_attributes_6_0=ruleAttribute();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAccessFrameworkRule());
            	    						}
            	    						add(
            	    							current,
            	    							"attributes",
            	    							lv_attributes_6_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCQL.g:1234:4: ( (lv_datatypes_7_0= ruleDataType ) )
            	    // InternalCQL.g:1235:5: (lv_datatypes_7_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1235:5: (lv_datatypes_7_0= ruleDataType )
            	    // InternalCQL.g:1236:6: lv_datatypes_7_0= ruleDataType
            	    {

            	    						newCompositeNode(grammarAccess.getAccessFrameworkAccess().getDatatypesDataTypeParserRuleCall_5_2_0());
            	    					
            	    pushFollow(FOLLOW_30);
            	    lv_datatypes_7_0=ruleDataType();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAccessFrameworkRule());
            	    						}
            	    						add(
            	    							current,
            	    							"datatypes",
            	    							lv_datatypes_7_0,
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

            otherlv_8=(Token)match(input,23,FOLLOW_31); 

            			newLeafNode(otherlv_8, grammarAccess.getAccessFrameworkAccess().getRightParenthesisKeyword_6());
            		
            otherlv_9=(Token)match(input,32,FOLLOW_32); 

            			newLeafNode(otherlv_9, grammarAccess.getAccessFrameworkAccess().getWRAPPERKeyword_7());
            		
            // InternalCQL.g:1262:3: ( (lv_wrapper_10_0= RULE_STRING ) )
            // InternalCQL.g:1263:4: (lv_wrapper_10_0= RULE_STRING )
            {
            // InternalCQL.g:1263:4: (lv_wrapper_10_0= RULE_STRING )
            // InternalCQL.g:1264:5: lv_wrapper_10_0= RULE_STRING
            {
            lv_wrapper_10_0=(Token)match(input,RULE_STRING,FOLLOW_33); 

            					newLeafNode(lv_wrapper_10_0, grammarAccess.getAccessFrameworkAccess().getWrapperSTRINGTerminalRuleCall_8_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"wrapper",
            						lv_wrapper_10_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_11=(Token)match(input,33,FOLLOW_32); 

            			newLeafNode(otherlv_11, grammarAccess.getAccessFrameworkAccess().getPROTOCOLKeyword_9());
            		
            // InternalCQL.g:1284:3: ( (lv_protocol_12_0= RULE_STRING ) )
            // InternalCQL.g:1285:4: (lv_protocol_12_0= RULE_STRING )
            {
            // InternalCQL.g:1285:4: (lv_protocol_12_0= RULE_STRING )
            // InternalCQL.g:1286:5: lv_protocol_12_0= RULE_STRING
            {
            lv_protocol_12_0=(Token)match(input,RULE_STRING,FOLLOW_34); 

            					newLeafNode(lv_protocol_12_0, grammarAccess.getAccessFrameworkAccess().getProtocolSTRINGTerminalRuleCall_10_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"protocol",
            						lv_protocol_12_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_13=(Token)match(input,34,FOLLOW_32); 

            			newLeafNode(otherlv_13, grammarAccess.getAccessFrameworkAccess().getTRANSPORTKeyword_11());
            		
            // InternalCQL.g:1306:3: ( (lv_transport_14_0= RULE_STRING ) )
            // InternalCQL.g:1307:4: (lv_transport_14_0= RULE_STRING )
            {
            // InternalCQL.g:1307:4: (lv_transport_14_0= RULE_STRING )
            // InternalCQL.g:1308:5: lv_transport_14_0= RULE_STRING
            {
            lv_transport_14_0=(Token)match(input,RULE_STRING,FOLLOW_35); 

            					newLeafNode(lv_transport_14_0, grammarAccess.getAccessFrameworkAccess().getTransportSTRINGTerminalRuleCall_12_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"transport",
            						lv_transport_14_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_15=(Token)match(input,35,FOLLOW_32); 

            			newLeafNode(otherlv_15, grammarAccess.getAccessFrameworkAccess().getDATAHANDLERKeyword_13());
            		
            // InternalCQL.g:1328:3: ( (lv_datahandler_16_0= RULE_STRING ) )
            // InternalCQL.g:1329:4: (lv_datahandler_16_0= RULE_STRING )
            {
            // InternalCQL.g:1329:4: (lv_datahandler_16_0= RULE_STRING )
            // InternalCQL.g:1330:5: lv_datahandler_16_0= RULE_STRING
            {
            lv_datahandler_16_0=(Token)match(input,RULE_STRING,FOLLOW_36); 

            					newLeafNode(lv_datahandler_16_0, grammarAccess.getAccessFrameworkAccess().getDatahandlerSTRINGTerminalRuleCall_14_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAccessFrameworkRule());
            					}
            					setWithLastConsumed(
            						current,
            						"datahandler",
            						lv_datahandler_16_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_17=(Token)match(input,36,FOLLOW_25); 

            			newLeafNode(otherlv_17, grammarAccess.getAccessFrameworkAccess().getOPTIONSKeyword_15());
            		
            otherlv_18=(Token)match(input,22,FOLLOW_32); 

            			newLeafNode(otherlv_18, grammarAccess.getAccessFrameworkAccess().getLeftParenthesisKeyword_16());
            		
            // InternalCQL.g:1354:3: ( ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) ) )+
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
            	    // InternalCQL.g:1355:4: ( (lv_keys_19_0= RULE_STRING ) ) ( (lv_values_20_0= RULE_STRING ) )
            	    {
            	    // InternalCQL.g:1355:4: ( (lv_keys_19_0= RULE_STRING ) )
            	    // InternalCQL.g:1356:5: (lv_keys_19_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:1356:5: (lv_keys_19_0= RULE_STRING )
            	    // InternalCQL.g:1357:6: lv_keys_19_0= RULE_STRING
            	    {
            	    lv_keys_19_0=(Token)match(input,RULE_STRING,FOLLOW_32); 

            	    						newLeafNode(lv_keys_19_0, grammarAccess.getAccessFrameworkAccess().getKeysSTRINGTerminalRuleCall_17_0_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"keys",
            	    							lv_keys_19_0,
            	    							"org.eclipse.xtext.common.Terminals.STRING");
            	    					

            	    }


            	    }

            	    // InternalCQL.g:1373:4: ( (lv_values_20_0= RULE_STRING ) )
            	    // InternalCQL.g:1374:5: (lv_values_20_0= RULE_STRING )
            	    {
            	    // InternalCQL.g:1374:5: (lv_values_20_0= RULE_STRING )
            	    // InternalCQL.g:1375:6: lv_values_20_0= RULE_STRING
            	    {
            	    lv_values_20_0=(Token)match(input,RULE_STRING,FOLLOW_37); 

            	    						newLeafNode(lv_values_20_0, grammarAccess.getAccessFrameworkAccess().getValuesSTRINGTerminalRuleCall_17_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"values",
            	    							lv_values_20_0,
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

            // InternalCQL.g:1392:3: (otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) ) )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==16) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // InternalCQL.g:1393:4: otherlv_21= ',' ( (lv_keys_22_0= RULE_STRING ) ) ( (lv_values_23_0= RULE_STRING ) )
                    {
                    otherlv_21=(Token)match(input,16,FOLLOW_32); 

                    				newLeafNode(otherlv_21, grammarAccess.getAccessFrameworkAccess().getCommaKeyword_18_0());
                    			
                    // InternalCQL.g:1397:4: ( (lv_keys_22_0= RULE_STRING ) )
                    // InternalCQL.g:1398:5: (lv_keys_22_0= RULE_STRING )
                    {
                    // InternalCQL.g:1398:5: (lv_keys_22_0= RULE_STRING )
                    // InternalCQL.g:1399:6: lv_keys_22_0= RULE_STRING
                    {
                    lv_keys_22_0=(Token)match(input,RULE_STRING,FOLLOW_32); 

                    						newLeafNode(lv_keys_22_0, grammarAccess.getAccessFrameworkAccess().getKeysSTRINGTerminalRuleCall_18_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"keys",
                    							lv_keys_22_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }

                    // InternalCQL.g:1415:4: ( (lv_values_23_0= RULE_STRING ) )
                    // InternalCQL.g:1416:5: (lv_values_23_0= RULE_STRING )
                    {
                    // InternalCQL.g:1416:5: (lv_values_23_0= RULE_STRING )
                    // InternalCQL.g:1417:6: lv_values_23_0= RULE_STRING
                    {
                    lv_values_23_0=(Token)match(input,RULE_STRING,FOLLOW_19); 

                    						newLeafNode(lv_values_23_0, grammarAccess.getAccessFrameworkAccess().getValuesSTRINGTerminalRuleCall_18_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAccessFrameworkRule());
                    						}
                    						addWithLastConsumed(
                    							current,
                    							"values",
                    							lv_values_23_0,
                    							"org.eclipse.xtext.common.Terminals.STRING");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_24=(Token)match(input,23,FOLLOW_2); 

            			newLeafNode(otherlv_24, grammarAccess.getAccessFrameworkAccess().getRightParenthesisKeyword_19());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAccessFramework"


    // $ANTLR start "entryRuleChannelFormat"
    // InternalCQL.g:1442:1: entryRuleChannelFormat returns [EObject current=null] : iv_ruleChannelFormat= ruleChannelFormat EOF ;
    public final EObject entryRuleChannelFormat() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleChannelFormat = null;


        try {
            // InternalCQL.g:1442:54: (iv_ruleChannelFormat= ruleChannelFormat EOF )
            // InternalCQL.g:1443:2: iv_ruleChannelFormat= ruleChannelFormat EOF
            {
             newCompositeNode(grammarAccess.getChannelFormatRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleChannelFormat=ruleChannelFormat();

            state._fsp--;

             current =iv_ruleChannelFormat; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleChannelFormat"


    // $ANTLR start "ruleChannelFormat"
    // InternalCQL.g:1449:1: ruleChannelFormat returns [EObject current=null] : ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) ) ;
    public final EObject ruleChannelFormat() throws RecognitionException {
        EObject current = null;

        EObject lv_stream_0_0 = null;

        EObject lv_view_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1455:2: ( ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) ) )
            // InternalCQL.g:1456:2: ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) )
            {
            // InternalCQL.g:1456:2: ( ( (lv_stream_0_0= ruleChannelFormatStream ) ) | ( (lv_view_1_0= ruleChannelFormatView ) ) )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==30) ) {
                alt32=1;
            }
            else if ( (LA32_0==39) ) {
                alt32=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // InternalCQL.g:1457:3: ( (lv_stream_0_0= ruleChannelFormatStream ) )
                    {
                    // InternalCQL.g:1457:3: ( (lv_stream_0_0= ruleChannelFormatStream ) )
                    // InternalCQL.g:1458:4: (lv_stream_0_0= ruleChannelFormatStream )
                    {
                    // InternalCQL.g:1458:4: (lv_stream_0_0= ruleChannelFormatStream )
                    // InternalCQL.g:1459:5: lv_stream_0_0= ruleChannelFormatStream
                    {

                    					newCompositeNode(grammarAccess.getChannelFormatAccess().getStreamChannelFormatStreamParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_stream_0_0=ruleChannelFormatStream();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getChannelFormatRule());
                    					}
                    					set(
                    						current,
                    						"stream",
                    						lv_stream_0_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ChannelFormatStream");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1477:3: ( (lv_view_1_0= ruleChannelFormatView ) )
                    {
                    // InternalCQL.g:1477:3: ( (lv_view_1_0= ruleChannelFormatView ) )
                    // InternalCQL.g:1478:4: (lv_view_1_0= ruleChannelFormatView )
                    {
                    // InternalCQL.g:1478:4: (lv_view_1_0= ruleChannelFormatView )
                    // InternalCQL.g:1479:5: lv_view_1_0= ruleChannelFormatView
                    {

                    					newCompositeNode(grammarAccess.getChannelFormatAccess().getViewChannelFormatViewParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_view_1_0=ruleChannelFormatView();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getChannelFormatRule());
                    					}
                    					set(
                    						current,
                    						"view",
                    						lv_view_1_0,
                    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.ChannelFormatView");
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
    // $ANTLR end "ruleChannelFormat"


    // $ANTLR start "entryRuleChannelFormatStream"
    // InternalCQL.g:1500:1: entryRuleChannelFormatStream returns [EObject current=null] : iv_ruleChannelFormatStream= ruleChannelFormatStream EOF ;
    public final EObject entryRuleChannelFormatStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleChannelFormatStream = null;


        try {
            // InternalCQL.g:1500:60: (iv_ruleChannelFormatStream= ruleChannelFormatStream EOF )
            // InternalCQL.g:1501:2: iv_ruleChannelFormatStream= ruleChannelFormatStream EOF
            {
             newCompositeNode(grammarAccess.getChannelFormatStreamRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleChannelFormatStream=ruleChannelFormatStream();

            state._fsp--;

             current =iv_ruleChannelFormatStream; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleChannelFormatStream"


    // $ANTLR start "ruleChannelFormatStream"
    // InternalCQL.g:1507:1: ruleChannelFormatStream returns [EObject current=null] : (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) ) ;
    public final EObject ruleChannelFormatStream() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_5=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token lv_host_10_0=null;
        Token otherlv_11=null;
        Token lv_port_12_0=null;
        EObject lv_attributes_3_0 = null;

        EObject lv_datatypes_4_0 = null;

        EObject lv_attributes_6_0 = null;

        EObject lv_datatypes_7_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1513:2: ( (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) ) )
            // InternalCQL.g:1514:2: (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) )
            {
            // InternalCQL.g:1514:2: (otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) ) )
            // InternalCQL.g:1515:3: otherlv_0= 'STREAM' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_attributes_3_0= ruleAttribute ) )+ ( (lv_datatypes_4_0= ruleDataType ) )+ (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )* otherlv_8= ')' otherlv_9= 'CHANNEL' ( (lv_host_10_0= RULE_ID ) ) otherlv_11= ':' ( (lv_port_12_0= RULE_INT ) )
            {
            otherlv_0=(Token)match(input,30,FOLLOW_9); 

            			newLeafNode(otherlv_0, grammarAccess.getChannelFormatStreamAccess().getSTREAMKeyword_0());
            		
            // InternalCQL.g:1519:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:1520:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:1520:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:1521:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_25); 

            					newLeafNode(lv_name_1_0, grammarAccess.getChannelFormatStreamAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getChannelFormatStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,22,FOLLOW_9); 

            			newLeafNode(otherlv_2, grammarAccess.getChannelFormatStreamAccess().getLeftParenthesisKeyword_2());
            		
            // InternalCQL.g:1541:3: ( (lv_attributes_3_0= ruleAttribute ) )+
            int cnt33=0;
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==RULE_ID) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // InternalCQL.g:1542:4: (lv_attributes_3_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1542:4: (lv_attributes_3_0= ruleAttribute )
            	    // InternalCQL.g:1543:5: lv_attributes_3_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getAttributesAttributeParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_27);
            	    lv_attributes_3_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getChannelFormatStreamRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_3_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
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

            // InternalCQL.g:1560:3: ( (lv_datatypes_4_0= ruleDataType ) )+
            int cnt34=0;
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( ((LA34_0>=64 && LA34_0<=70)) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // InternalCQL.g:1561:4: (lv_datatypes_4_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1561:4: (lv_datatypes_4_0= ruleDataType )
            	    // InternalCQL.g:1562:5: lv_datatypes_4_0= ruleDataType
            	    {

            	    					newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getDatatypesDataTypeParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_28);
            	    lv_datatypes_4_0=ruleDataType();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getChannelFormatStreamRule());
            	    					}
            	    					add(
            	    						current,
            	    						"datatypes",
            	    						lv_datatypes_4_0,
            	    						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DataType");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt34 >= 1 ) break loop34;
                        EarlyExitException eee =
                            new EarlyExitException(34, input);
                        throw eee;
                }
                cnt34++;
            } while (true);

            // InternalCQL.g:1579:3: (otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) ) )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==16) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // InternalCQL.g:1580:4: otherlv_5= ',' ( (lv_attributes_6_0= ruleAttribute ) ) ( (lv_datatypes_7_0= ruleDataType ) )
            	    {
            	    otherlv_5=(Token)match(input,16,FOLLOW_9); 

            	    				newLeafNode(otherlv_5, grammarAccess.getChannelFormatStreamAccess().getCommaKeyword_5_0());
            	    			
            	    // InternalCQL.g:1584:4: ( (lv_attributes_6_0= ruleAttribute ) )
            	    // InternalCQL.g:1585:5: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCQL.g:1585:5: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCQL.g:1586:6: lv_attributes_6_0= ruleAttribute
            	    {

            	    						newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getAttributesAttributeParserRuleCall_5_1_0());
            	    					
            	    pushFollow(FOLLOW_29);
            	    lv_attributes_6_0=ruleAttribute();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getChannelFormatStreamRule());
            	    						}
            	    						add(
            	    							current,
            	    							"attributes",
            	    							lv_attributes_6_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Attribute");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCQL.g:1603:4: ( (lv_datatypes_7_0= ruleDataType ) )
            	    // InternalCQL.g:1604:5: (lv_datatypes_7_0= ruleDataType )
            	    {
            	    // InternalCQL.g:1604:5: (lv_datatypes_7_0= ruleDataType )
            	    // InternalCQL.g:1605:6: lv_datatypes_7_0= ruleDataType
            	    {

            	    						newCompositeNode(grammarAccess.getChannelFormatStreamAccess().getDatatypesDataTypeParserRuleCall_5_2_0());
            	    					
            	    pushFollow(FOLLOW_30);
            	    lv_datatypes_7_0=ruleDataType();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getChannelFormatStreamRule());
            	    						}
            	    						add(
            	    							current,
            	    							"datatypes",
            	    							lv_datatypes_7_0,
            	    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.DataType");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);

            otherlv_8=(Token)match(input,23,FOLLOW_38); 

            			newLeafNode(otherlv_8, grammarAccess.getChannelFormatStreamAccess().getRightParenthesisKeyword_6());
            		
            otherlv_9=(Token)match(input,37,FOLLOW_9); 

            			newLeafNode(otherlv_9, grammarAccess.getChannelFormatStreamAccess().getCHANNELKeyword_7());
            		
            // InternalCQL.g:1631:3: ( (lv_host_10_0= RULE_ID ) )
            // InternalCQL.g:1632:4: (lv_host_10_0= RULE_ID )
            {
            // InternalCQL.g:1632:4: (lv_host_10_0= RULE_ID )
            // InternalCQL.g:1633:5: lv_host_10_0= RULE_ID
            {
            lv_host_10_0=(Token)match(input,RULE_ID,FOLLOW_39); 

            					newLeafNode(lv_host_10_0, grammarAccess.getChannelFormatStreamAccess().getHostIDTerminalRuleCall_8_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getChannelFormatStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"host",
            						lv_host_10_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_11=(Token)match(input,38,FOLLOW_40); 

            			newLeafNode(otherlv_11, grammarAccess.getChannelFormatStreamAccess().getColonKeyword_9());
            		
            // InternalCQL.g:1653:3: ( (lv_port_12_0= RULE_INT ) )
            // InternalCQL.g:1654:4: (lv_port_12_0= RULE_INT )
            {
            // InternalCQL.g:1654:4: (lv_port_12_0= RULE_INT )
            // InternalCQL.g:1655:5: lv_port_12_0= RULE_INT
            {
            lv_port_12_0=(Token)match(input,RULE_INT,FOLLOW_2); 

            					newLeafNode(lv_port_12_0, grammarAccess.getChannelFormatStreamAccess().getPortINTTerminalRuleCall_10_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getChannelFormatStreamRule());
            					}
            					setWithLastConsumed(
            						current,
            						"port",
            						lv_port_12_0,
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
    // $ANTLR end "ruleChannelFormatStream"


    // $ANTLR start "entryRuleChannelFormatView"
    // InternalCQL.g:1675:1: entryRuleChannelFormatView returns [EObject current=null] : iv_ruleChannelFormatView= ruleChannelFormatView EOF ;
    public final EObject entryRuleChannelFormatView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleChannelFormatView = null;


        try {
            // InternalCQL.g:1675:58: (iv_ruleChannelFormatView= ruleChannelFormatView EOF )
            // InternalCQL.g:1676:2: iv_ruleChannelFormatView= ruleChannelFormatView EOF
            {
             newCompositeNode(grammarAccess.getChannelFormatViewRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleChannelFormatView=ruleChannelFormatView();

            state._fsp--;

             current =iv_ruleChannelFormatView; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleChannelFormatView"


    // $ANTLR start "ruleChannelFormatView"
    // InternalCQL.g:1682:1: ruleChannelFormatView returns [EObject current=null] : (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')' ) ;
    public final EObject ruleChannelFormatView() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        EObject lv_select_4_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1688:2: ( (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')' ) )
            // InternalCQL.g:1689:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')' )
            {
            // InternalCQL.g:1689:2: (otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')' )
            // InternalCQL.g:1690:3: otherlv_0= 'VIEW' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'FROM' otherlv_3= '(' ( (lv_select_4_0= ruleSelect ) ) (otherlv_5= ';' )? otherlv_6= ')'
            {
            otherlv_0=(Token)match(input,39,FOLLOW_9); 

            			newLeafNode(otherlv_0, grammarAccess.getChannelFormatViewAccess().getVIEWKeyword_0());
            		
            // InternalCQL.g:1694:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCQL.g:1695:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCQL.g:1695:4: (lv_name_1_0= RULE_ID )
            // InternalCQL.g:1696:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_7); 

            					newLeafNode(lv_name_1_0, grammarAccess.getChannelFormatViewAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getChannelFormatViewRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,17,FOLLOW_25); 

            			newLeafNode(otherlv_2, grammarAccess.getChannelFormatViewAccess().getFROMKeyword_2());
            		
            otherlv_3=(Token)match(input,22,FOLLOW_18); 

            			newLeafNode(otherlv_3, grammarAccess.getChannelFormatViewAccess().getLeftParenthesisKeyword_3());
            		
            // InternalCQL.g:1720:3: ( (lv_select_4_0= ruleSelect ) )
            // InternalCQL.g:1721:4: (lv_select_4_0= ruleSelect )
            {
            // InternalCQL.g:1721:4: (lv_select_4_0= ruleSelect )
            // InternalCQL.g:1722:5: lv_select_4_0= ruleSelect
            {

            					newCompositeNode(grammarAccess.getChannelFormatViewAccess().getSelectSelectParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_41);
            lv_select_4_0=ruleSelect();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getChannelFormatViewRule());
            					}
            					set(
            						current,
            						"select",
            						lv_select_4_0,
            						"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.Select");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCQL.g:1739:3: (otherlv_5= ';' )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==12) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // InternalCQL.g:1740:4: otherlv_5= ';'
                    {
                    otherlv_5=(Token)match(input,12,FOLLOW_19); 

                    				newLeafNode(otherlv_5, grammarAccess.getChannelFormatViewAccess().getSemicolonKeyword_5());
                    			

                    }
                    break;

            }

            otherlv_6=(Token)match(input,23,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getChannelFormatViewAccess().getRightParenthesisKeyword_6());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleChannelFormatView"


    // $ANTLR start "entryRuleStreamTo"
    // InternalCQL.g:1753:1: entryRuleStreamTo returns [EObject current=null] : iv_ruleStreamTo= ruleStreamTo EOF ;
    public final EObject entryRuleStreamTo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStreamTo = null;


        try {
            // InternalCQL.g:1753:49: (iv_ruleStreamTo= ruleStreamTo EOF )
            // InternalCQL.g:1754:2: iv_ruleStreamTo= ruleStreamTo EOF
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
    // InternalCQL.g:1760:1: ruleStreamTo returns [EObject current=null] : (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) ;
    public final EObject ruleStreamTo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token lv_inputname_4_0=null;
        EObject lv_statement_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:1766:2: ( (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) ) )
            // InternalCQL.g:1767:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            {
            // InternalCQL.g:1767:2: (otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) ) )
            // InternalCQL.g:1768:3: otherlv_0= 'STREAM' otherlv_1= 'TO' ( (lv_name_2_0= RULE_ID ) ) ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            {
            otherlv_0=(Token)match(input,30,FOLLOW_42); 

            			newLeafNode(otherlv_0, grammarAccess.getStreamToAccess().getSTREAMKeyword_0());
            		
            otherlv_1=(Token)match(input,40,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getStreamToAccess().getTOKeyword_1());
            		
            // InternalCQL.g:1776:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCQL.g:1777:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCQL.g:1777:4: (lv_name_2_0= RULE_ID )
            // InternalCQL.g:1778:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_43); 

            					newLeafNode(lv_name_2_0, grammarAccess.getStreamToAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getStreamToRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalCQL.g:1794:3: ( ( (lv_statement_3_0= ruleSelect ) ) | ( (lv_inputname_4_0= RULE_ID ) ) )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==13) ) {
                alt37=1;
            }
            else if ( (LA37_0==RULE_ID) ) {
                alt37=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // InternalCQL.g:1795:4: ( (lv_statement_3_0= ruleSelect ) )
                    {
                    // InternalCQL.g:1795:4: ( (lv_statement_3_0= ruleSelect ) )
                    // InternalCQL.g:1796:5: (lv_statement_3_0= ruleSelect )
                    {
                    // InternalCQL.g:1796:5: (lv_statement_3_0= ruleSelect )
                    // InternalCQL.g:1797:6: lv_statement_3_0= ruleSelect
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
                    // InternalCQL.g:1815:4: ( (lv_inputname_4_0= RULE_ID ) )
                    {
                    // InternalCQL.g:1815:4: ( (lv_inputname_4_0= RULE_ID ) )
                    // InternalCQL.g:1816:5: (lv_inputname_4_0= RULE_ID )
                    {
                    // InternalCQL.g:1816:5: (lv_inputname_4_0= RULE_ID )
                    // InternalCQL.g:1817:6: lv_inputname_4_0= RULE_ID
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
                    							"org.eclipse.xtext.common.Terminals.ID");
                    					

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
    // InternalCQL.g:1838:1: entryRuleDrop returns [EObject current=null] : iv_ruleDrop= ruleDrop EOF ;
    public final EObject entryRuleDrop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDrop = null;


        try {
            // InternalCQL.g:1838:45: (iv_ruleDrop= ruleDrop EOF )
            // InternalCQL.g:1839:2: iv_ruleDrop= ruleDrop EOF
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
    // InternalCQL.g:1845:1: ruleDrop returns [EObject current=null] : (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? ) ;
    public final EObject ruleDrop() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalCQL.g:1851:2: ( (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? ) )
            // InternalCQL.g:1852:2: (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? )
            {
            // InternalCQL.g:1852:2: (otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )? )
            // InternalCQL.g:1853:3: otherlv_0= 'DROP' ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) ) (otherlv_3= 'IF' otherlv_4= 'EXISTS' )?
            {
            otherlv_0=(Token)match(input,41,FOLLOW_44); 

            			newLeafNode(otherlv_0, grammarAccess.getDropAccess().getDROPKeyword_0());
            		
            // InternalCQL.g:1857:3: ( ( (lv_name_1_0= 'SINK' ) ) | ( (lv_name_2_0= 'STREAM' ) ) )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==31) ) {
                alt38=1;
            }
            else if ( (LA38_0==30) ) {
                alt38=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // InternalCQL.g:1858:4: ( (lv_name_1_0= 'SINK' ) )
                    {
                    // InternalCQL.g:1858:4: ( (lv_name_1_0= 'SINK' ) )
                    // InternalCQL.g:1859:5: (lv_name_1_0= 'SINK' )
                    {
                    // InternalCQL.g:1859:5: (lv_name_1_0= 'SINK' )
                    // InternalCQL.g:1860:6: lv_name_1_0= 'SINK'
                    {
                    lv_name_1_0=(Token)match(input,31,FOLLOW_45); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getDropAccess().getNameSINKKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_1_0, "SINK");
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:1873:4: ( (lv_name_2_0= 'STREAM' ) )
                    {
                    // InternalCQL.g:1873:4: ( (lv_name_2_0= 'STREAM' ) )
                    // InternalCQL.g:1874:5: (lv_name_2_0= 'STREAM' )
                    {
                    // InternalCQL.g:1874:5: (lv_name_2_0= 'STREAM' )
                    // InternalCQL.g:1875:6: lv_name_2_0= 'STREAM'
                    {
                    lv_name_2_0=(Token)match(input,30,FOLLOW_45); 

                    						newLeafNode(lv_name_2_0, grammarAccess.getDropAccess().getNameSTREAMKeyword_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDropRule());
                    						}
                    						setWithLastConsumed(current, "name", lv_name_2_0, "STREAM");
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCQL.g:1888:3: (otherlv_3= 'IF' otherlv_4= 'EXISTS' )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==42) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // InternalCQL.g:1889:4: otherlv_3= 'IF' otherlv_4= 'EXISTS'
                    {
                    otherlv_3=(Token)match(input,42,FOLLOW_46); 

                    				newLeafNode(otherlv_3, grammarAccess.getDropAccess().getIFKeyword_2_0());
                    			
                    otherlv_4=(Token)match(input,43,FOLLOW_2); 

                    				newLeafNode(otherlv_4, grammarAccess.getDropAccess().getEXISTSKeyword_2_1());
                    			

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
    // InternalCQL.g:1902:1: entryRuleWindow_Unbounded returns [String current=null] : iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF ;
    public final String entryRuleWindow_Unbounded() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleWindow_Unbounded = null;


        try {
            // InternalCQL.g:1902:56: (iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF )
            // InternalCQL.g:1903:2: iv_ruleWindow_Unbounded= ruleWindow_Unbounded EOF
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
    // InternalCQL.g:1909:1: ruleWindow_Unbounded returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : kw= 'UNBOUNDED' ;
    public final AntlrDatatypeRuleToken ruleWindow_Unbounded() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCQL.g:1915:2: (kw= 'UNBOUNDED' )
            // InternalCQL.g:1916:2: kw= 'UNBOUNDED'
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
    // InternalCQL.g:1924:1: entryRuleWindow_Timebased returns [EObject current=null] : iv_ruleWindow_Timebased= ruleWindow_Timebased EOF ;
    public final EObject entryRuleWindow_Timebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Timebased = null;


        try {
            // InternalCQL.g:1924:57: (iv_ruleWindow_Timebased= ruleWindow_Timebased EOF )
            // InternalCQL.g:1925:2: iv_ruleWindow_Timebased= ruleWindow_Timebased EOF
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
    // InternalCQL.g:1931:1: ruleWindow_Timebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) ;
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
            // InternalCQL.g:1937:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' ) )
            // InternalCQL.g:1938:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            {
            // InternalCQL.g:1938:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME' )
            // InternalCQL.g:1939:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) ( (lv_unit_2_0= RULE_ID ) ) (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )? otherlv_6= 'TIME'
            {
            otherlv_0=(Token)match(input,45,FOLLOW_40); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TimebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:1943:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:1944:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:1944:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:1945:5: lv_size_1_0= RULE_INT
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

            // InternalCQL.g:1961:3: ( (lv_unit_2_0= RULE_ID ) )
            // InternalCQL.g:1962:4: (lv_unit_2_0= RULE_ID )
            {
            // InternalCQL.g:1962:4: (lv_unit_2_0= RULE_ID )
            // InternalCQL.g:1963:5: lv_unit_2_0= RULE_ID
            {
            lv_unit_2_0=(Token)match(input,RULE_ID,FOLLOW_47); 

            					newLeafNode(lv_unit_2_0, grammarAccess.getWindow_TimebasedAccess().getUnitIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getWindow_TimebasedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"unit",
            						lv_unit_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalCQL.g:1979:3: (otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) ) )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==46) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalCQL.g:1980:4: otherlv_3= 'ADVANCE' ( (lv_advance_size_4_0= RULE_INT ) ) ( (lv_advance_unit_5_0= RULE_ID ) )
                    {
                    otherlv_3=(Token)match(input,46,FOLLOW_40); 

                    				newLeafNode(otherlv_3, grammarAccess.getWindow_TimebasedAccess().getADVANCEKeyword_3_0());
                    			
                    // InternalCQL.g:1984:4: ( (lv_advance_size_4_0= RULE_INT ) )
                    // InternalCQL.g:1985:5: (lv_advance_size_4_0= RULE_INT )
                    {
                    // InternalCQL.g:1985:5: (lv_advance_size_4_0= RULE_INT )
                    // InternalCQL.g:1986:6: lv_advance_size_4_0= RULE_INT
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

                    // InternalCQL.g:2002:4: ( (lv_advance_unit_5_0= RULE_ID ) )
                    // InternalCQL.g:2003:5: (lv_advance_unit_5_0= RULE_ID )
                    {
                    // InternalCQL.g:2003:5: (lv_advance_unit_5_0= RULE_ID )
                    // InternalCQL.g:2004:6: lv_advance_unit_5_0= RULE_ID
                    {
                    lv_advance_unit_5_0=(Token)match(input,RULE_ID,FOLLOW_48); 

                    						newLeafNode(lv_advance_unit_5_0, grammarAccess.getWindow_TimebasedAccess().getAdvance_unitIDTerminalRuleCall_3_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getWindow_TimebasedRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"advance_unit",
                    							lv_advance_unit_5_0,
                    							"org.eclipse.xtext.common.Terminals.ID");
                    					

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
    // InternalCQL.g:2029:1: entryRuleWindow_Tuplebased returns [EObject current=null] : iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF ;
    public final EObject entryRuleWindow_Tuplebased() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow_Tuplebased = null;


        try {
            // InternalCQL.g:2029:58: (iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF )
            // InternalCQL.g:2030:2: iv_ruleWindow_Tuplebased= ruleWindow_Tuplebased EOF
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
    // InternalCQL.g:2036:1: ruleWindow_Tuplebased returns [EObject current=null] : (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) ;
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
            // InternalCQL.g:2042:2: ( (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? ) )
            // InternalCQL.g:2043:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            {
            // InternalCQL.g:2043:2: (otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )? )
            // InternalCQL.g:2044:3: otherlv_0= 'SIZE' ( (lv_size_1_0= RULE_INT ) ) (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )? otherlv_4= 'TUPLE' (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            {
            otherlv_0=(Token)match(input,45,FOLLOW_40); 

            			newLeafNode(otherlv_0, grammarAccess.getWindow_TuplebasedAccess().getSIZEKeyword_0());
            		
            // InternalCQL.g:2048:3: ( (lv_size_1_0= RULE_INT ) )
            // InternalCQL.g:2049:4: (lv_size_1_0= RULE_INT )
            {
            // InternalCQL.g:2049:4: (lv_size_1_0= RULE_INT )
            // InternalCQL.g:2050:5: lv_size_1_0= RULE_INT
            {
            lv_size_1_0=(Token)match(input,RULE_INT,FOLLOW_49); 

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

            // InternalCQL.g:2066:3: (otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) ) )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==46) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // InternalCQL.g:2067:4: otherlv_2= 'ADVANCE' ( (lv_advance_size_3_0= RULE_INT ) )
                    {
                    otherlv_2=(Token)match(input,46,FOLLOW_40); 

                    				newLeafNode(otherlv_2, grammarAccess.getWindow_TuplebasedAccess().getADVANCEKeyword_2_0());
                    			
                    // InternalCQL.g:2071:4: ( (lv_advance_size_3_0= RULE_INT ) )
                    // InternalCQL.g:2072:5: (lv_advance_size_3_0= RULE_INT )
                    {
                    // InternalCQL.g:2072:5: (lv_advance_size_3_0= RULE_INT )
                    // InternalCQL.g:2073:6: lv_advance_size_3_0= RULE_INT
                    {
                    lv_advance_size_3_0=(Token)match(input,RULE_INT,FOLLOW_50); 

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

            otherlv_4=(Token)match(input,48,FOLLOW_51); 

            			newLeafNode(otherlv_4, grammarAccess.getWindow_TuplebasedAccess().getTUPLEKeyword_3());
            		
            // InternalCQL.g:2094:3: (otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==49) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // InternalCQL.g:2095:4: otherlv_5= 'PARTITION' otherlv_6= 'BY' ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    {
                    otherlv_5=(Token)match(input,49,FOLLOW_15); 

                    				newLeafNode(otherlv_5, grammarAccess.getWindow_TuplebasedAccess().getPARTITIONKeyword_4_0());
                    			
                    otherlv_6=(Token)match(input,20,FOLLOW_9); 

                    				newLeafNode(otherlv_6, grammarAccess.getWindow_TuplebasedAccess().getBYKeyword_4_1());
                    			
                    // InternalCQL.g:2103:4: ( (lv_partition_attribute_7_0= ruleAttribute ) )
                    // InternalCQL.g:2104:5: (lv_partition_attribute_7_0= ruleAttribute )
                    {
                    // InternalCQL.g:2104:5: (lv_partition_attribute_7_0= ruleAttribute )
                    // InternalCQL.g:2105:6: lv_partition_attribute_7_0= ruleAttribute
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
    // InternalCQL.g:2127:1: entryRuleExpressionsModel returns [EObject current=null] : iv_ruleExpressionsModel= ruleExpressionsModel EOF ;
    public final EObject entryRuleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionsModel = null;


        try {
            // InternalCQL.g:2127:57: (iv_ruleExpressionsModel= ruleExpressionsModel EOF )
            // InternalCQL.g:2128:2: iv_ruleExpressionsModel= ruleExpressionsModel EOF
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
    // InternalCQL.g:2134:1: ruleExpressionsModel returns [EObject current=null] : ( () ( (lv_elements_1_0= ruleExpression ) ) ) ;
    public final EObject ruleExpressionsModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2140:2: ( ( () ( (lv_elements_1_0= ruleExpression ) ) ) )
            // InternalCQL.g:2141:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            {
            // InternalCQL.g:2141:2: ( () ( (lv_elements_1_0= ruleExpression ) ) )
            // InternalCQL.g:2142:3: () ( (lv_elements_1_0= ruleExpression ) )
            {
            // InternalCQL.g:2142:3: ()
            // InternalCQL.g:2143:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0(),
            					current);
            			

            }

            // InternalCQL.g:2149:3: ( (lv_elements_1_0= ruleExpression ) )
            // InternalCQL.g:2150:4: (lv_elements_1_0= ruleExpression )
            {
            // InternalCQL.g:2150:4: (lv_elements_1_0= ruleExpression )
            // InternalCQL.g:2151:5: lv_elements_1_0= ruleExpression
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
    // InternalCQL.g:2172:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // InternalCQL.g:2172:51: (iv_ruleExpression= ruleExpression EOF )
            // InternalCQL.g:2173:2: iv_ruleExpression= ruleExpression EOF
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
    // InternalCQL.g:2179:1: ruleExpression returns [EObject current=null] : this_Or_0= ruleOr ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Or_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2185:2: (this_Or_0= ruleOr )
            // InternalCQL.g:2186:2: this_Or_0= ruleOr
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
    // InternalCQL.g:2197:1: entryRuleOr returns [EObject current=null] : iv_ruleOr= ruleOr EOF ;
    public final EObject entryRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOr = null;


        try {
            // InternalCQL.g:2197:43: (iv_ruleOr= ruleOr EOF )
            // InternalCQL.g:2198:2: iv_ruleOr= ruleOr EOF
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
    // InternalCQL.g:2204:1: ruleOr returns [EObject current=null] : (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) ;
    public final EObject ruleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_And_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2210:2: ( (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* ) )
            // InternalCQL.g:2211:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            {
            // InternalCQL.g:2211:2: (this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )* )
            // InternalCQL.g:2212:3: this_And_0= ruleAnd ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0());
            		
            pushFollow(FOLLOW_52);
            this_And_0=ruleAnd();

            state._fsp--;


            			current = this_And_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2220:3: ( () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) ) )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==50) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalCQL.g:2221:4: () otherlv_2= 'OR' ( (lv_right_3_0= ruleAnd ) )
            	    {
            	    // InternalCQL.g:2221:4: ()
            	    // InternalCQL.g:2222:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrAccess().getOrLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,50,FOLLOW_13); 

            	    				newLeafNode(otherlv_2, grammarAccess.getOrAccess().getORKeyword_1_1());
            	    			
            	    // InternalCQL.g:2232:4: ( (lv_right_3_0= ruleAnd ) )
            	    // InternalCQL.g:2233:5: (lv_right_3_0= ruleAnd )
            	    {
            	    // InternalCQL.g:2233:5: (lv_right_3_0= ruleAnd )
            	    // InternalCQL.g:2234:6: lv_right_3_0= ruleAnd
            	    {

            	    						newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_52);
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
    // $ANTLR end "ruleOr"


    // $ANTLR start "entryRuleAnd"
    // InternalCQL.g:2256:1: entryRuleAnd returns [EObject current=null] : iv_ruleAnd= ruleAnd EOF ;
    public final EObject entryRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnd = null;


        try {
            // InternalCQL.g:2256:44: (iv_ruleAnd= ruleAnd EOF )
            // InternalCQL.g:2257:2: iv_ruleAnd= ruleAnd EOF
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
    // InternalCQL.g:2263:1: ruleAnd returns [EObject current=null] : (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) ;
    public final EObject ruleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_Equalitiy_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2269:2: ( (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* ) )
            // InternalCQL.g:2270:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            {
            // InternalCQL.g:2270:2: (this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )* )
            // InternalCQL.g:2271:3: this_Equalitiy_0= ruleEqualitiy ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0());
            		
            pushFollow(FOLLOW_53);
            this_Equalitiy_0=ruleEqualitiy();

            state._fsp--;


            			current = this_Equalitiy_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2279:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) ) )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( (LA44_0==51) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // InternalCQL.g:2280:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleEqualitiy ) )
            	    {
            	    // InternalCQL.g:2280:4: ()
            	    // InternalCQL.g:2281:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndAccess().getAndLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,51,FOLLOW_13); 

            	    				newLeafNode(otherlv_2, grammarAccess.getAndAccess().getANDKeyword_1_1());
            	    			
            	    // InternalCQL.g:2291:4: ( (lv_right_3_0= ruleEqualitiy ) )
            	    // InternalCQL.g:2292:5: (lv_right_3_0= ruleEqualitiy )
            	    {
            	    // InternalCQL.g:2292:5: (lv_right_3_0= ruleEqualitiy )
            	    // InternalCQL.g:2293:6: lv_right_3_0= ruleEqualitiy
            	    {

            	    						newCompositeNode(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_53);
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
    // $ANTLR end "ruleAnd"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQL.g:2315:1: entryRuleEqualitiy returns [EObject current=null] : iv_ruleEqualitiy= ruleEqualitiy EOF ;
    public final EObject entryRuleEqualitiy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualitiy = null;


        try {
            // InternalCQL.g:2315:50: (iv_ruleEqualitiy= ruleEqualitiy EOF )
            // InternalCQL.g:2316:2: iv_ruleEqualitiy= ruleEqualitiy EOF
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
    // InternalCQL.g:2322:1: ruleEqualitiy returns [EObject current=null] : (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) ;
    public final EObject ruleEqualitiy() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Comparison_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2328:2: ( (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* ) )
            // InternalCQL.g:2329:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            {
            // InternalCQL.g:2329:2: (this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )* )
            // InternalCQL.g:2330:3: this_Comparison_0= ruleComparison ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0());
            		
            pushFollow(FOLLOW_54);
            this_Comparison_0=ruleComparison();

            state._fsp--;


            			current = this_Comparison_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2338:3: ( () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) ) )*
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( ((LA46_0>=52 && LA46_0<=53)) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // InternalCQL.g:2339:4: () ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) ) ( (lv_right_3_0= ruleComparison ) )
            	    {
            	    // InternalCQL.g:2339:4: ()
            	    // InternalCQL.g:2340:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:2346:4: ( ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) ) )
            	    // InternalCQL.g:2347:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    {
            	    // InternalCQL.g:2347:5: ( (lv_op_2_1= '==' | lv_op_2_2= '!=' ) )
            	    // InternalCQL.g:2348:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    {
            	    // InternalCQL.g:2348:6: (lv_op_2_1= '==' | lv_op_2_2= '!=' )
            	    int alt45=2;
            	    int LA45_0 = input.LA(1);

            	    if ( (LA45_0==52) ) {
            	        alt45=1;
            	    }
            	    else if ( (LA45_0==53) ) {
            	        alt45=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 45, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt45) {
            	        case 1 :
            	            // InternalCQL.g:2349:7: lv_op_2_1= '=='
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
            	            // InternalCQL.g:2360:7: lv_op_2_2= '!='
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

            	    // InternalCQL.g:2373:4: ( (lv_right_3_0= ruleComparison ) )
            	    // InternalCQL.g:2374:5: (lv_right_3_0= ruleComparison )
            	    {
            	    // InternalCQL.g:2374:5: (lv_right_3_0= ruleComparison )
            	    // InternalCQL.g:2375:6: lv_right_3_0= ruleComparison
            	    {

            	    						newCompositeNode(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_54);
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
    // $ANTLR end "ruleEqualitiy"


    // $ANTLR start "entryRuleComparison"
    // InternalCQL.g:2397:1: entryRuleComparison returns [EObject current=null] : iv_ruleComparison= ruleComparison EOF ;
    public final EObject entryRuleComparison() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComparison = null;


        try {
            // InternalCQL.g:2397:51: (iv_ruleComparison= ruleComparison EOF )
            // InternalCQL.g:2398:2: iv_ruleComparison= ruleComparison EOF
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
    // InternalCQL.g:2404:1: ruleComparison returns [EObject current=null] : (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) ;
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
            // InternalCQL.g:2410:2: ( (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* ) )
            // InternalCQL.g:2411:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            {
            // InternalCQL.g:2411:2: (this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )* )
            // InternalCQL.g:2412:3: this_PlusOrMinus_0= rulePlusOrMinus ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_55);
            this_PlusOrMinus_0=rulePlusOrMinus();

            state._fsp--;


            			current = this_PlusOrMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2420:3: ( () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) ) )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( ((LA48_0>=54 && LA48_0<=57)) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // InternalCQL.g:2421:4: () ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) ) ( (lv_right_3_0= rulePlusOrMinus ) )
            	    {
            	    // InternalCQL.g:2421:4: ()
            	    // InternalCQL.g:2422:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:2428:4: ( ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) ) )
            	    // InternalCQL.g:2429:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    {
            	    // InternalCQL.g:2429:5: ( (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' ) )
            	    // InternalCQL.g:2430:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    {
            	    // InternalCQL.g:2430:6: (lv_op_2_1= '>=' | lv_op_2_2= '<=' | lv_op_2_3= '<' | lv_op_2_4= '>' )
            	    int alt47=4;
            	    switch ( input.LA(1) ) {
            	    case 54:
            	        {
            	        alt47=1;
            	        }
            	        break;
            	    case 55:
            	        {
            	        alt47=2;
            	        }
            	        break;
            	    case 56:
            	        {
            	        alt47=3;
            	        }
            	        break;
            	    case 57:
            	        {
            	        alt47=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 47, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt47) {
            	        case 1 :
            	            // InternalCQL.g:2431:7: lv_op_2_1= '>='
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
            	            // InternalCQL.g:2442:7: lv_op_2_2= '<='
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
            	            // InternalCQL.g:2453:7: lv_op_2_3= '<'
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
            	            // InternalCQL.g:2464:7: lv_op_2_4= '>'
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

            	    // InternalCQL.g:2477:4: ( (lv_right_3_0= rulePlusOrMinus ) )
            	    // InternalCQL.g:2478:5: (lv_right_3_0= rulePlusOrMinus )
            	    {
            	    // InternalCQL.g:2478:5: (lv_right_3_0= rulePlusOrMinus )
            	    // InternalCQL.g:2479:6: lv_right_3_0= rulePlusOrMinus
            	    {

            	    						newCompositeNode(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_55);
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
    // $ANTLR end "ruleComparison"


    // $ANTLR start "entryRulePlusOrMinus"
    // InternalCQL.g:2501:1: entryRulePlusOrMinus returns [EObject current=null] : iv_rulePlusOrMinus= rulePlusOrMinus EOF ;
    public final EObject entryRulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusOrMinus = null;


        try {
            // InternalCQL.g:2501:52: (iv_rulePlusOrMinus= rulePlusOrMinus EOF )
            // InternalCQL.g:2502:2: iv_rulePlusOrMinus= rulePlusOrMinus EOF
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
    // InternalCQL.g:2508:1: rulePlusOrMinus returns [EObject current=null] : (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) ;
    public final EObject rulePlusOrMinus() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject this_MulOrDiv_0 = null;

        EObject lv_right_5_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2514:2: ( (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* ) )
            // InternalCQL.g:2515:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            {
            // InternalCQL.g:2515:2: (this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )* )
            // InternalCQL.g:2516:3: this_MulOrDiv_0= ruleMulOrDiv ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_56);
            this_MulOrDiv_0=ruleMulOrDiv();

            state._fsp--;


            			current = this_MulOrDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2524:3: ( ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) ) )*
            loop50:
            do {
                int alt50=2;
                int LA50_0 = input.LA(1);

                if ( ((LA50_0>=58 && LA50_0<=59)) ) {
                    alt50=1;
                }


                switch (alt50) {
            	case 1 :
            	    // InternalCQL.g:2525:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) ) ( (lv_right_5_0= ruleMulOrDiv ) )
            	    {
            	    // InternalCQL.g:2525:4: ( ( () otherlv_2= '+' ) | ( () otherlv_4= '-' ) )
            	    int alt49=2;
            	    int LA49_0 = input.LA(1);

            	    if ( (LA49_0==58) ) {
            	        alt49=1;
            	    }
            	    else if ( (LA49_0==59) ) {
            	        alt49=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 49, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt49) {
            	        case 1 :
            	            // InternalCQL.g:2526:5: ( () otherlv_2= '+' )
            	            {
            	            // InternalCQL.g:2526:5: ( () otherlv_2= '+' )
            	            // InternalCQL.g:2527:6: () otherlv_2= '+'
            	            {
            	            // InternalCQL.g:2527:6: ()
            	            // InternalCQL.g:2528:7: 
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
            	            // InternalCQL.g:2540:5: ( () otherlv_4= '-' )
            	            {
            	            // InternalCQL.g:2540:5: ( () otherlv_4= '-' )
            	            // InternalCQL.g:2541:6: () otherlv_4= '-'
            	            {
            	            // InternalCQL.g:2541:6: ()
            	            // InternalCQL.g:2542:7: 
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

            	    // InternalCQL.g:2554:4: ( (lv_right_5_0= ruleMulOrDiv ) )
            	    // InternalCQL.g:2555:5: (lv_right_5_0= ruleMulOrDiv )
            	    {
            	    // InternalCQL.g:2555:5: (lv_right_5_0= ruleMulOrDiv )
            	    // InternalCQL.g:2556:6: lv_right_5_0= ruleMulOrDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_56);
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
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCQL.g:2578:1: entryRuleMulOrDiv returns [EObject current=null] : iv_ruleMulOrDiv= ruleMulOrDiv EOF ;
    public final EObject entryRuleMulOrDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMulOrDiv = null;


        try {
            // InternalCQL.g:2578:49: (iv_ruleMulOrDiv= ruleMulOrDiv EOF )
            // InternalCQL.g:2579:2: iv_ruleMulOrDiv= ruleMulOrDiv EOF
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
    // InternalCQL.g:2585:1: ruleMulOrDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleMulOrDiv() throws RecognitionException {
        EObject current = null;

        Token lv_op_2_1=null;
        Token lv_op_2_2=null;
        EObject this_Primary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCQL.g:2591:2: ( (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCQL.g:2592:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCQL.g:2592:2: (this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCQL.g:2593:3: this_Primary_0= rulePrimary ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_57);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCQL.g:2601:3: ( () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==15||LA52_0==60) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // InternalCQL.g:2602:4: () ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCQL.g:2602:4: ()
            	    // InternalCQL.g:2603:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCQL.g:2609:4: ( ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) ) )
            	    // InternalCQL.g:2610:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    {
            	    // InternalCQL.g:2610:5: ( (lv_op_2_1= '*' | lv_op_2_2= '/' ) )
            	    // InternalCQL.g:2611:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    {
            	    // InternalCQL.g:2611:6: (lv_op_2_1= '*' | lv_op_2_2= '/' )
            	    int alt51=2;
            	    int LA51_0 = input.LA(1);

            	    if ( (LA51_0==15) ) {
            	        alt51=1;
            	    }
            	    else if ( (LA51_0==60) ) {
            	        alt51=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 51, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt51) {
            	        case 1 :
            	            // InternalCQL.g:2612:7: lv_op_2_1= '*'
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
            	            // InternalCQL.g:2623:7: lv_op_2_2= '/'
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

            	    // InternalCQL.g:2636:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCQL.g:2637:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCQL.g:2637:5: (lv_right_3_0= rulePrimary )
            	    // InternalCQL.g:2638:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_57);
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
    // $ANTLR end "ruleMulOrDiv"


    // $ANTLR start "entryRulePrimary"
    // InternalCQL.g:2660:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCQL.g:2660:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCQL.g:2661:2: iv_rulePrimary= rulePrimary EOF
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
    // InternalCQL.g:2667:1: rulePrimary returns [EObject current=null] : ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) ;
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
            // InternalCQL.g:2673:2: ( ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic ) )
            // InternalCQL.g:2674:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            {
            // InternalCQL.g:2674:2: ( ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' ) | ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) ) | this_Atomic_7= ruleAtomic )
            int alt53=3;
            switch ( input.LA(1) ) {
            case 22:
                {
                alt53=1;
                }
                break;
            case 61:
                {
                alt53=2;
                }
                break;
            case RULE_ID:
            case RULE_STRING:
            case RULE_INT:
            case RULE_FLOAT_NUMBER:
            case 62:
            case 63:
                {
                alt53=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 53, 0, input);

                throw nvae;
            }

            switch (alt53) {
                case 1 :
                    // InternalCQL.g:2675:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    {
                    // InternalCQL.g:2675:3: ( () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')' )
                    // InternalCQL.g:2676:4: () otherlv_1= '(' ( (lv_inner_2_0= ruleExpression ) ) otherlv_3= ')'
                    {
                    // InternalCQL.g:2676:4: ()
                    // InternalCQL.g:2677:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getBracketAction_0_0(),
                    						current);
                    				

                    }

                    otherlv_1=(Token)match(input,22,FOLLOW_13); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalCQL.g:2687:4: ( (lv_inner_2_0= ruleExpression ) )
                    // InternalCQL.g:2688:5: (lv_inner_2_0= ruleExpression )
                    {
                    // InternalCQL.g:2688:5: (lv_inner_2_0= ruleExpression )
                    // InternalCQL.g:2689:6: lv_inner_2_0= ruleExpression
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
                    // InternalCQL.g:2712:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    {
                    // InternalCQL.g:2712:3: ( () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) ) )
                    // InternalCQL.g:2713:4: () otherlv_5= 'NOT' ( (lv_expression_6_0= rulePrimary ) )
                    {
                    // InternalCQL.g:2713:4: ()
                    // InternalCQL.g:2714:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getPrimaryAccess().getNOTAction_1_0(),
                    						current);
                    				

                    }

                    otherlv_5=(Token)match(input,61,FOLLOW_13); 

                    				newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getNOTKeyword_1_1());
                    			
                    // InternalCQL.g:2724:4: ( (lv_expression_6_0= rulePrimary ) )
                    // InternalCQL.g:2725:5: (lv_expression_6_0= rulePrimary )
                    {
                    // InternalCQL.g:2725:5: (lv_expression_6_0= rulePrimary )
                    // InternalCQL.g:2726:6: lv_expression_6_0= rulePrimary
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
                    // InternalCQL.g:2745:3: this_Atomic_7= ruleAtomic
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
    // InternalCQL.g:2757:1: entryRuleAtomic returns [EObject current=null] : iv_ruleAtomic= ruleAtomic EOF ;
    public final EObject entryRuleAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomic = null;


        try {
            // InternalCQL.g:2757:47: (iv_ruleAtomic= ruleAtomic EOF )
            // InternalCQL.g:2758:2: iv_ruleAtomic= ruleAtomic EOF
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
    // InternalCQL.g:2764:1: ruleAtomic returns [EObject current=null] : ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) ;
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
            // InternalCQL.g:2770:2: ( ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) ) )
            // InternalCQL.g:2771:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            {
            // InternalCQL.g:2771:2: ( ( () ( (lv_value_1_0= RULE_INT ) ) ) | ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) ) | ( () ( (lv_value_5_0= RULE_STRING ) ) ) | ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) ) | ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) ) )
            int alt56=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt56=1;
                }
                break;
            case RULE_FLOAT_NUMBER:
                {
                alt56=2;
                }
                break;
            case RULE_STRING:
                {
                alt56=3;
                }
                break;
            case 62:
            case 63:
                {
                alt56=4;
                }
                break;
            case RULE_ID:
                {
                alt56=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }

            switch (alt56) {
                case 1 :
                    // InternalCQL.g:2772:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    {
                    // InternalCQL.g:2772:3: ( () ( (lv_value_1_0= RULE_INT ) ) )
                    // InternalCQL.g:2773:4: () ( (lv_value_1_0= RULE_INT ) )
                    {
                    // InternalCQL.g:2773:4: ()
                    // InternalCQL.g:2774:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getIntConstantAction_0_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2780:4: ( (lv_value_1_0= RULE_INT ) )
                    // InternalCQL.g:2781:5: (lv_value_1_0= RULE_INT )
                    {
                    // InternalCQL.g:2781:5: (lv_value_1_0= RULE_INT )
                    // InternalCQL.g:2782:6: lv_value_1_0= RULE_INT
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
                    // InternalCQL.g:2800:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    {
                    // InternalCQL.g:2800:3: ( () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) ) )
                    // InternalCQL.g:2801:4: () ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    {
                    // InternalCQL.g:2801:4: ()
                    // InternalCQL.g:2802:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getFloatConstantAction_1_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2808:4: ( (lv_value_3_0= RULE_FLOAT_NUMBER ) )
                    // InternalCQL.g:2809:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    {
                    // InternalCQL.g:2809:5: (lv_value_3_0= RULE_FLOAT_NUMBER )
                    // InternalCQL.g:2810:6: lv_value_3_0= RULE_FLOAT_NUMBER
                    {
                    lv_value_3_0=(Token)match(input,RULE_FLOAT_NUMBER,FOLLOW_2); 

                    						newLeafNode(lv_value_3_0, grammarAccess.getAtomicAccess().getValueFLOAT_NUMBERTerminalRuleCall_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"value",
                    							lv_value_3_0,
                    							"de.uniol.inf.is.odysseus.parser.novel.cql.CQL.FLOAT_NUMBER");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:2828:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    {
                    // InternalCQL.g:2828:3: ( () ( (lv_value_5_0= RULE_STRING ) ) )
                    // InternalCQL.g:2829:4: () ( (lv_value_5_0= RULE_STRING ) )
                    {
                    // InternalCQL.g:2829:4: ()
                    // InternalCQL.g:2830:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getStringConstantAction_2_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2836:4: ( (lv_value_5_0= RULE_STRING ) )
                    // InternalCQL.g:2837:5: (lv_value_5_0= RULE_STRING )
                    {
                    // InternalCQL.g:2837:5: (lv_value_5_0= RULE_STRING )
                    // InternalCQL.g:2838:6: lv_value_5_0= RULE_STRING
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
                    // InternalCQL.g:2856:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    {
                    // InternalCQL.g:2856:3: ( () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) ) )
                    // InternalCQL.g:2857:4: () ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    {
                    // InternalCQL.g:2857:4: ()
                    // InternalCQL.g:2858:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getBoolConstantAction_3_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2864:4: ( ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) ) )
                    // InternalCQL.g:2865:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    {
                    // InternalCQL.g:2865:5: ( (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' ) )
                    // InternalCQL.g:2866:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    {
                    // InternalCQL.g:2866:6: (lv_value_7_1= 'TRUE' | lv_value_7_2= 'FALSE' )
                    int alt54=2;
                    int LA54_0 = input.LA(1);

                    if ( (LA54_0==62) ) {
                        alt54=1;
                    }
                    else if ( (LA54_0==63) ) {
                        alt54=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 54, 0, input);

                        throw nvae;
                    }
                    switch (alt54) {
                        case 1 :
                            // InternalCQL.g:2867:7: lv_value_7_1= 'TRUE'
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
                            // InternalCQL.g:2878:7: lv_value_7_2= 'FALSE'
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
                    // InternalCQL.g:2893:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    {
                    // InternalCQL.g:2893:3: ( () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) ) )
                    // InternalCQL.g:2894:4: () ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    {
                    // InternalCQL.g:2894:4: ()
                    // InternalCQL.g:2895:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getAtomicAccess().getAttributeRefAction_4_0(),
                    						current);
                    				

                    }

                    // InternalCQL.g:2901:4: ( ( (lv_value_9_0= ruleAttributeWithoutAlias ) ) | ( (lv_value_10_0= ruleAttributeWithNestedStatement ) ) )
                    int alt55=2;
                    int LA55_0 = input.LA(1);

                    if ( (LA55_0==RULE_ID) ) {
                        int LA55_1 = input.LA(2);

                        if ( (LA55_1==EOF||(LA55_1>=12 && LA55_1<=13)||LA55_1==15||LA55_1==19||LA55_1==21||LA55_1==23||(LA55_1>=28 && LA55_1<=30)||LA55_1==41||(LA55_1>=50 && LA55_1<=60)) ) {
                            alt55=1;
                        }
                        else if ( ((LA55_1>=26 && LA55_1<=27)) ) {
                            alt55=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 55, 1, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 55, 0, input);

                        throw nvae;
                    }
                    switch (alt55) {
                        case 1 :
                            // InternalCQL.g:2902:5: ( (lv_value_9_0= ruleAttributeWithoutAlias ) )
                            {
                            // InternalCQL.g:2902:5: ( (lv_value_9_0= ruleAttributeWithoutAlias ) )
                            // InternalCQL.g:2903:6: (lv_value_9_0= ruleAttributeWithoutAlias )
                            {
                            // InternalCQL.g:2903:6: (lv_value_9_0= ruleAttributeWithoutAlias )
                            // InternalCQL.g:2904:7: lv_value_9_0= ruleAttributeWithoutAlias
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
                            // InternalCQL.g:2922:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            {
                            // InternalCQL.g:2922:5: ( (lv_value_10_0= ruleAttributeWithNestedStatement ) )
                            // InternalCQL.g:2923:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            {
                            // InternalCQL.g:2923:6: (lv_value_10_0= ruleAttributeWithNestedStatement )
                            // InternalCQL.g:2924:7: lv_value_10_0= ruleAttributeWithNestedStatement
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
    // InternalCQL.g:2947:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalCQL.g:2947:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalCQL.g:2948:2: iv_ruleDataType= ruleDataType EOF
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
    // InternalCQL.g:2954:1: ruleDataType returns [EObject current=null] : ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) ) ;
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
            // InternalCQL.g:2960:2: ( ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) ) )
            // InternalCQL.g:2961:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) )
            {
            // InternalCQL.g:2961:2: ( ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) ) )
            // InternalCQL.g:2962:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) )
            {
            // InternalCQL.g:2962:3: ( (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' ) )
            // InternalCQL.g:2963:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' )
            {
            // InternalCQL.g:2963:4: (lv_value_0_1= 'INTEGER' | lv_value_0_2= 'DOUBLE' | lv_value_0_3= 'FLOAT' | lv_value_0_4= 'STRING' | lv_value_0_5= 'BOOLEAN' | lv_value_0_6= 'STARTTIMESTAMP' | lv_value_0_7= 'ENDTIMESTAMP' )
            int alt57=7;
            switch ( input.LA(1) ) {
            case 64:
                {
                alt57=1;
                }
                break;
            case 65:
                {
                alt57=2;
                }
                break;
            case 66:
                {
                alt57=3;
                }
                break;
            case 67:
                {
                alt57=4;
                }
                break;
            case 68:
                {
                alt57=5;
                }
                break;
            case 69:
                {
                alt57=6;
                }
                break;
            case 70:
                {
                alt57=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 57, 0, input);

                throw nvae;
            }

            switch (alt57) {
                case 1 :
                    // InternalCQL.g:2964:5: lv_value_0_1= 'INTEGER'
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
                    // InternalCQL.g:2975:5: lv_value_0_2= 'DOUBLE'
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
                    // InternalCQL.g:2986:5: lv_value_0_3= 'FLOAT'
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
                    // InternalCQL.g:2997:5: lv_value_0_4= 'STRING'
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
                    // InternalCQL.g:3008:5: lv_value_0_5= 'BOOLEAN'
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
                    // InternalCQL.g:3019:5: lv_value_0_6= 'STARTTIMESTAMP'
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
                    // InternalCQL.g:3030:5: lv_value_0_7= 'ENDTIMESTAMP'
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


    protected DFA25 dfa25 = new DFA25(this);
    static final String dfa_1s = "\34\uffff";
    static final String dfa_2s = "\1\36\1\4\2\uffff\1\26\3\4\7\20\2\4\1\40\1\32\1\4\7\20\1\100";
    static final String dfa_3s = "\1\47\1\4\2\uffff\1\26\1\4\1\106\1\4\10\106\1\4\1\45\1\106\1\4\7\27\1\106";
    static final String dfa_4s = "\2\uffff\1\1\1\2\30\uffff";
    static final String dfa_5s = "\34\uffff}>";
    static final String[] dfa_6s = {
            "\1\1\1\3\7\uffff\1\2",
            "\1\4",
            "",
            "",
            "\1\5",
            "\1\6",
            "\1\6\25\uffff\1\7\45\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\17",
            "\1\20\6\uffff\1\21\50\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\20\6\uffff\1\21\50\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\20\6\uffff\1\21\50\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\20\6\uffff\1\21\50\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\20\6\uffff\1\21\50\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\20\6\uffff\1\21\50\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\20\6\uffff\1\21\50\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\6\73\uffff\1\10\1\11\1\12\1\13\1\14\1\15\1\16",
            "\1\22",
            "\1\3\4\uffff\1\2",
            "\1\23\45\uffff\1\24\1\25\1\26\1\27\1\30\1\31\1\32",
            "\1\33",
            "\1\20\6\uffff\1\21",
            "\1\20\6\uffff\1\21",
            "\1\20\6\uffff\1\21",
            "\1\20\6\uffff\1\21",
            "\1\20\6\uffff\1\21",
            "\1\20\6\uffff\1\21",
            "\1\20\6\uffff\1\21",
            "\1\24\1\25\1\26\1\27\1\30\1\31\1\32"
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final char[] dfa_2 = DFA.unpackEncodedStringToUnsignedChars(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final short[] dfa_4 = DFA.unpackEncodedString(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[][] dfa_6 = unpackEncodedStringArray(dfa_6s);

    class DFA25 extends DFA {

        public DFA25(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 25;
            this.eot = dfa_1;
            this.eof = dfa_1;
            this.min = dfa_2;
            this.max = dfa_3;
            this.accept = dfa_4;
            this.special = dfa_5;
            this.transition = dfa_6;
        }
        public String getDescription() {
            return "1063:3: ( ( (lv_channelformat_1_0= ruleChannelFormat ) ) | ( (lv_accessframework_2_0= ruleAccessFramework ) ) )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000020070002002L});
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
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x00000080C0000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000000000010L,0x000000000000007FL});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000000810000L,0x000000000000007FL});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000000000000L,0x000000000000007FL});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000000810000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000000810020L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000000801000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000000002010L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x00000000C0000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000C00000000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0001400000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0030000000000002L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x03C0000000000002L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0C00000000000002L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x1000000000008002L});

}
