package de.uniol.inf.is.odysseus.server.parser.antlr.internal;

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
import de.uniol.inf.is.odysseus.server.services.StreamingsparqlGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalStreamingsparqlParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_IRI_TERMINAL", "RULE_AGG_FUNCTION", "RULE_STRING", "RULE_WINDOWTYPE", "RULE_INT", "RULE_UNITTYPE", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'PREFIX'", "':'", "'BASE'", "'#ADDQUERY'", "'#RUNQUERY'", "'SELECT'", "'AGGREGATE('", "'aggregations'", "'='", "'['", "']'", "','", "')'", "'group_by=['", "'CSVFILESINK('", "'FILTER('", "'FROM'", "'AS'", "'TYPE'", "'SIZE'", "'ADVANCE'", "'UNIT'", "'WHERE'", "'{'", "'}'", "'.'", "';'", "'?'", "'<'", "'>'", "'<='", "'>='", "'!='", "'+'", "'/'", "'-'", "'*'"
    };
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__51=51;
    public static final int RULE_IRI_TERMINAL=5;
    public static final int RULE_ID=4;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=9;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=11;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int RULE_WINDOWTYPE=8;
    public static final int RULE_STRING=7;
    public static final int RULE_SL_COMMENT=12;
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
    public static final int RULE_WS=13;
    public static final int RULE_ANY_OTHER=14;
    public static final int RULE_UNITTYPE=10;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int RULE_AGG_FUNCTION=6;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;

    // delegates
    // delegators


        public InternalStreamingsparqlParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalStreamingsparqlParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalStreamingsparqlParser.tokenNames; }
    public String getGrammarFileName() { return "InternalStreamingsparql.g"; }



     	private StreamingsparqlGrammarAccess grammarAccess;

        public InternalStreamingsparqlParser(TokenStream input, StreamingsparqlGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "SPARQLQuery";
       	}

       	@Override
       	protected StreamingsparqlGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleSPARQLQuery"
    // InternalStreamingsparql.g:65:1: entryRuleSPARQLQuery returns [EObject current=null] : iv_ruleSPARQLQuery= ruleSPARQLQuery EOF ;
    public final EObject entryRuleSPARQLQuery() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSPARQLQuery = null;


        try {
            // InternalStreamingsparql.g:65:52: (iv_ruleSPARQLQuery= ruleSPARQLQuery EOF )
            // InternalStreamingsparql.g:66:2: iv_ruleSPARQLQuery= ruleSPARQLQuery EOF
            {
             newCompositeNode(grammarAccess.getSPARQLQueryRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSPARQLQuery=ruleSPARQLQuery();

            state._fsp--;

             current =iv_ruleSPARQLQuery; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSPARQLQuery"


    // $ANTLR start "ruleSPARQLQuery"
    // InternalStreamingsparql.g:72:1: ruleSPARQLQuery returns [EObject current=null] : this_SelectQuery_0= ruleSelectQuery ;
    public final EObject ruleSPARQLQuery() throws RecognitionException {
        EObject current = null;

        EObject this_SelectQuery_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:78:2: (this_SelectQuery_0= ruleSelectQuery )
            // InternalStreamingsparql.g:79:2: this_SelectQuery_0= ruleSelectQuery
            {

            		newCompositeNode(grammarAccess.getSPARQLQueryAccess().getSelectQueryParserRuleCall());
            	
            pushFollow(FOLLOW_2);
            this_SelectQuery_0=ruleSelectQuery();

            state._fsp--;


            		current = this_SelectQuery_0;
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
    // $ANTLR end "ruleSPARQLQuery"


    // $ANTLR start "entryRulePrefix"
    // InternalStreamingsparql.g:90:1: entryRulePrefix returns [EObject current=null] : iv_rulePrefix= rulePrefix EOF ;
    public final EObject entryRulePrefix() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrefix = null;


        try {
            // InternalStreamingsparql.g:90:47: (iv_rulePrefix= rulePrefix EOF )
            // InternalStreamingsparql.g:91:2: iv_rulePrefix= rulePrefix EOF
            {
             newCompositeNode(grammarAccess.getPrefixRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePrefix=rulePrefix();

            state._fsp--;

             current =iv_rulePrefix; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePrefix"


    // $ANTLR start "rulePrefix"
    // InternalStreamingsparql.g:97:1: rulePrefix returns [EObject current=null] : ( (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) ) | this_UnNamedPrefix_4= ruleUnNamedPrefix ) ;
    public final EObject rulePrefix() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token lv_iref_3_0=null;
        EObject this_UnNamedPrefix_4 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:103:2: ( ( (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) ) | this_UnNamedPrefix_4= ruleUnNamedPrefix ) )
            // InternalStreamingsparql.g:104:2: ( (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) ) | this_UnNamedPrefix_4= ruleUnNamedPrefix )
            {
            // InternalStreamingsparql.g:104:2: ( (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) ) | this_UnNamedPrefix_4= ruleUnNamedPrefix )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==15) ) {
                int LA1_1 = input.LA(2);

                if ( (LA1_1==RULE_ID) ) {
                    alt1=1;
                }
                else if ( (LA1_1==16) ) {
                    alt1=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // InternalStreamingsparql.g:105:3: (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) )
                    {
                    // InternalStreamingsparql.g:105:3: (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) )
                    // InternalStreamingsparql.g:106:4: otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) )
                    {
                    otherlv_0=(Token)match(input,15,FOLLOW_3); 

                    				newLeafNode(otherlv_0, grammarAccess.getPrefixAccess().getPREFIXKeyword_0_0());
                    			
                    // InternalStreamingsparql.g:110:4: ( (lv_name_1_0= RULE_ID ) )
                    // InternalStreamingsparql.g:111:5: (lv_name_1_0= RULE_ID )
                    {
                    // InternalStreamingsparql.g:111:5: (lv_name_1_0= RULE_ID )
                    // InternalStreamingsparql.g:112:6: lv_name_1_0= RULE_ID
                    {
                    lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_4); 

                    						newLeafNode(lv_name_1_0, grammarAccess.getPrefixAccess().getNameIDTerminalRuleCall_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getPrefixRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"name",
                    							lv_name_1_0,
                    							"org.eclipse.xtext.common.Terminals.ID");
                    					

                    }


                    }

                    otherlv_2=(Token)match(input,16,FOLLOW_5); 

                    				newLeafNode(otherlv_2, grammarAccess.getPrefixAccess().getColonKeyword_0_2());
                    			
                    // InternalStreamingsparql.g:132:4: ( (lv_iref_3_0= RULE_IRI_TERMINAL ) )
                    // InternalStreamingsparql.g:133:5: (lv_iref_3_0= RULE_IRI_TERMINAL )
                    {
                    // InternalStreamingsparql.g:133:5: (lv_iref_3_0= RULE_IRI_TERMINAL )
                    // InternalStreamingsparql.g:134:6: lv_iref_3_0= RULE_IRI_TERMINAL
                    {
                    lv_iref_3_0=(Token)match(input,RULE_IRI_TERMINAL,FOLLOW_2); 

                    						newLeafNode(lv_iref_3_0, grammarAccess.getPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_0_3_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getPrefixRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"iref",
                    							lv_iref_3_0,
                    							"de.uniol.inf.is.odysseus.server.Streamingsparql.IRI_TERMINAL");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:152:3: this_UnNamedPrefix_4= ruleUnNamedPrefix
                    {

                    			newCompositeNode(grammarAccess.getPrefixAccess().getUnNamedPrefixParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_UnNamedPrefix_4=ruleUnNamedPrefix();

                    state._fsp--;


                    			current = this_UnNamedPrefix_4;
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
    // $ANTLR end "rulePrefix"


    // $ANTLR start "entryRuleUnNamedPrefix"
    // InternalStreamingsparql.g:164:1: entryRuleUnNamedPrefix returns [EObject current=null] : iv_ruleUnNamedPrefix= ruleUnNamedPrefix EOF ;
    public final EObject entryRuleUnNamedPrefix() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUnNamedPrefix = null;


        try {
            // InternalStreamingsparql.g:164:54: (iv_ruleUnNamedPrefix= ruleUnNamedPrefix EOF )
            // InternalStreamingsparql.g:165:2: iv_ruleUnNamedPrefix= ruleUnNamedPrefix EOF
            {
             newCompositeNode(grammarAccess.getUnNamedPrefixRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleUnNamedPrefix=ruleUnNamedPrefix();

            state._fsp--;

             current =iv_ruleUnNamedPrefix; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleUnNamedPrefix"


    // $ANTLR start "ruleUnNamedPrefix"
    // InternalStreamingsparql.g:171:1: ruleUnNamedPrefix returns [EObject current=null] : (otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) ) ) ;
    public final EObject ruleUnNamedPrefix() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_iref_2_0=null;


        	enterRule();

        try {
            // InternalStreamingsparql.g:177:2: ( (otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) ) ) )
            // InternalStreamingsparql.g:178:2: (otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) ) )
            {
            // InternalStreamingsparql.g:178:2: (otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) ) )
            // InternalStreamingsparql.g:179:3: otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) )
            {
            otherlv_0=(Token)match(input,15,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getUnNamedPrefixAccess().getPREFIXKeyword_0());
            		
            otherlv_1=(Token)match(input,16,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getUnNamedPrefixAccess().getColonKeyword_1());
            		
            // InternalStreamingsparql.g:187:3: ( (lv_iref_2_0= RULE_IRI_TERMINAL ) )
            // InternalStreamingsparql.g:188:4: (lv_iref_2_0= RULE_IRI_TERMINAL )
            {
            // InternalStreamingsparql.g:188:4: (lv_iref_2_0= RULE_IRI_TERMINAL )
            // InternalStreamingsparql.g:189:5: lv_iref_2_0= RULE_IRI_TERMINAL
            {
            lv_iref_2_0=(Token)match(input,RULE_IRI_TERMINAL,FOLLOW_2); 

            					newLeafNode(lv_iref_2_0, grammarAccess.getUnNamedPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getUnNamedPrefixRule());
            					}
            					setWithLastConsumed(
            						current,
            						"iref",
            						lv_iref_2_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.IRI_TERMINAL");
            				

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
    // $ANTLR end "ruleUnNamedPrefix"


    // $ANTLR start "entryRuleBase"
    // InternalStreamingsparql.g:209:1: entryRuleBase returns [EObject current=null] : iv_ruleBase= ruleBase EOF ;
    public final EObject entryRuleBase() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBase = null;


        try {
            // InternalStreamingsparql.g:209:45: (iv_ruleBase= ruleBase EOF )
            // InternalStreamingsparql.g:210:2: iv_ruleBase= ruleBase EOF
            {
             newCompositeNode(grammarAccess.getBaseRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBase=ruleBase();

            state._fsp--;

             current =iv_ruleBase; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBase"


    // $ANTLR start "ruleBase"
    // InternalStreamingsparql.g:216:1: ruleBase returns [EObject current=null] : (otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) ) ) ;
    public final EObject ruleBase() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_iref_1_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:222:2: ( (otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) ) ) )
            // InternalStreamingsparql.g:223:2: (otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) ) )
            {
            // InternalStreamingsparql.g:223:2: (otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) ) )
            // InternalStreamingsparql.g:224:3: otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) )
            {
            otherlv_0=(Token)match(input,17,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getBaseAccess().getBASEKeyword_0());
            		
            // InternalStreamingsparql.g:228:3: ( (lv_iref_1_0= ruleIRI ) )
            // InternalStreamingsparql.g:229:4: (lv_iref_1_0= ruleIRI )
            {
            // InternalStreamingsparql.g:229:4: (lv_iref_1_0= ruleIRI )
            // InternalStreamingsparql.g:230:5: lv_iref_1_0= ruleIRI
            {

            					newCompositeNode(grammarAccess.getBaseAccess().getIrefIRIParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_iref_1_0=ruleIRI();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getBaseRule());
            					}
            					set(
            						current,
            						"iref",
            						lv_iref_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.IRI");
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
    // $ANTLR end "ruleBase"


    // $ANTLR start "entryRuleSelectQuery"
    // InternalStreamingsparql.g:251:1: entryRuleSelectQuery returns [EObject current=null] : iv_ruleSelectQuery= ruleSelectQuery EOF ;
    public final EObject entryRuleSelectQuery() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectQuery = null;


        try {
            // InternalStreamingsparql.g:251:52: (iv_ruleSelectQuery= ruleSelectQuery EOF )
            // InternalStreamingsparql.g:252:2: iv_ruleSelectQuery= ruleSelectQuery EOF
            {
             newCompositeNode(grammarAccess.getSelectQueryRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSelectQuery=ruleSelectQuery();

            state._fsp--;

             current =iv_ruleSelectQuery; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSelectQuery"


    // $ANTLR start "ruleSelectQuery"
    // InternalStreamingsparql.g:258:1: ruleSelectQuery returns [EObject current=null] : ( ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( (lv_variables_6_0= ruleVariable ) ) ( (lv_variables_7_0= ruleVariable ) )* ( (lv_whereClause_8_0= ruleWhereClause ) ) ( (lv_filterclause_9_0= ruleFilterclause ) )? ( (lv_aggregateClause_10_0= ruleAggregate ) )? ( (lv_filesinkclause_11_0= ruleFilesinkclause ) )? ) ;
    public final EObject ruleSelectQuery() throws RecognitionException {
        EObject current = null;

        Token lv_method_0_0=null;
        Token otherlv_1=null;
        Token otherlv_5=null;
        EObject lv_base_2_0 = null;

        EObject lv_prefixes_3_0 = null;

        EObject lv_datasetClauses_4_0 = null;

        EObject lv_variables_6_0 = null;

        EObject lv_variables_7_0 = null;

        EObject lv_whereClause_8_0 = null;

        EObject lv_filterclause_9_0 = null;

        EObject lv_aggregateClause_10_0 = null;

        EObject lv_filesinkclause_11_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:264:2: ( ( ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( (lv_variables_6_0= ruleVariable ) ) ( (lv_variables_7_0= ruleVariable ) )* ( (lv_whereClause_8_0= ruleWhereClause ) ) ( (lv_filterclause_9_0= ruleFilterclause ) )? ( (lv_aggregateClause_10_0= ruleAggregate ) )? ( (lv_filesinkclause_11_0= ruleFilesinkclause ) )? ) )
            // InternalStreamingsparql.g:265:2: ( ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( (lv_variables_6_0= ruleVariable ) ) ( (lv_variables_7_0= ruleVariable ) )* ( (lv_whereClause_8_0= ruleWhereClause ) ) ( (lv_filterclause_9_0= ruleFilterclause ) )? ( (lv_aggregateClause_10_0= ruleAggregate ) )? ( (lv_filesinkclause_11_0= ruleFilesinkclause ) )? )
            {
            // InternalStreamingsparql.g:265:2: ( ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( (lv_variables_6_0= ruleVariable ) ) ( (lv_variables_7_0= ruleVariable ) )* ( (lv_whereClause_8_0= ruleWhereClause ) ) ( (lv_filterclause_9_0= ruleFilterclause ) )? ( (lv_aggregateClause_10_0= ruleAggregate ) )? ( (lv_filesinkclause_11_0= ruleFilesinkclause ) )? )
            // InternalStreamingsparql.g:266:3: ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( (lv_variables_6_0= ruleVariable ) ) ( (lv_variables_7_0= ruleVariable ) )* ( (lv_whereClause_8_0= ruleWhereClause ) ) ( (lv_filterclause_9_0= ruleFilterclause ) )? ( (lv_aggregateClause_10_0= ruleAggregate ) )? ( (lv_filesinkclause_11_0= ruleFilesinkclause ) )?
            {
            // InternalStreamingsparql.g:266:3: ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )?
            int alt2=3;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==18) ) {
                alt2=1;
            }
            else if ( (LA2_0==19) ) {
                alt2=2;
            }
            switch (alt2) {
                case 1 :
                    // InternalStreamingsparql.g:267:4: ( (lv_method_0_0= '#ADDQUERY' ) )
                    {
                    // InternalStreamingsparql.g:267:4: ( (lv_method_0_0= '#ADDQUERY' ) )
                    // InternalStreamingsparql.g:268:5: (lv_method_0_0= '#ADDQUERY' )
                    {
                    // InternalStreamingsparql.g:268:5: (lv_method_0_0= '#ADDQUERY' )
                    // InternalStreamingsparql.g:269:6: lv_method_0_0= '#ADDQUERY'
                    {
                    lv_method_0_0=(Token)match(input,18,FOLLOW_6); 

                    						newLeafNode(lv_method_0_0, grammarAccess.getSelectQueryAccess().getMethodADDQUERYKeyword_0_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSelectQueryRule());
                    						}
                    						setWithLastConsumed(current, "method", lv_method_0_0, "#ADDQUERY");
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:282:4: otherlv_1= '#RUNQUERY'
                    {
                    otherlv_1=(Token)match(input,19,FOLLOW_6); 

                    				newLeafNode(otherlv_1, grammarAccess.getSelectQueryAccess().getRUNQUERYKeyword_0_1());
                    			

                    }
                    break;

            }

            // InternalStreamingsparql.g:287:3: ( (lv_base_2_0= ruleBase ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==17) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalStreamingsparql.g:288:4: (lv_base_2_0= ruleBase )
                    {
                    // InternalStreamingsparql.g:288:4: (lv_base_2_0= ruleBase )
                    // InternalStreamingsparql.g:289:5: lv_base_2_0= ruleBase
                    {

                    					newCompositeNode(grammarAccess.getSelectQueryAccess().getBaseBaseParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_7);
                    lv_base_2_0=ruleBase();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectQueryRule());
                    					}
                    					set(
                    						current,
                    						"base",
                    						lv_base_2_0,
                    						"de.uniol.inf.is.odysseus.server.Streamingsparql.Base");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalStreamingsparql.g:306:3: ( (lv_prefixes_3_0= rulePrefix ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==15) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalStreamingsparql.g:307:4: (lv_prefixes_3_0= rulePrefix )
            	    {
            	    // InternalStreamingsparql.g:307:4: (lv_prefixes_3_0= rulePrefix )
            	    // InternalStreamingsparql.g:308:5: lv_prefixes_3_0= rulePrefix
            	    {

            	    					newCompositeNode(grammarAccess.getSelectQueryAccess().getPrefixesPrefixParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_7);
            	    lv_prefixes_3_0=rulePrefix();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            	    					}
            	    					add(
            	    						current,
            	    						"prefixes",
            	    						lv_prefixes_3_0,
            	    						"de.uniol.inf.is.odysseus.server.Streamingsparql.Prefix");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // InternalStreamingsparql.g:325:3: ( (lv_datasetClauses_4_0= ruleDatasetClause ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==31) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalStreamingsparql.g:326:4: (lv_datasetClauses_4_0= ruleDatasetClause )
            	    {
            	    // InternalStreamingsparql.g:326:4: (lv_datasetClauses_4_0= ruleDatasetClause )
            	    // InternalStreamingsparql.g:327:5: lv_datasetClauses_4_0= ruleDatasetClause
            	    {

            	    					newCompositeNode(grammarAccess.getSelectQueryAccess().getDatasetClausesDatasetClauseParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_8);
            	    lv_datasetClauses_4_0=ruleDatasetClause();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            	    					}
            	    					add(
            	    						current,
            	    						"datasetClauses",
            	    						lv_datasetClauses_4_0,
            	    						"de.uniol.inf.is.odysseus.server.Streamingsparql.DatasetClause");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            otherlv_5=(Token)match(input,20,FOLLOW_9); 

            			newLeafNode(otherlv_5, grammarAccess.getSelectQueryAccess().getSELECTKeyword_4());
            		
            // InternalStreamingsparql.g:348:3: ( (lv_variables_6_0= ruleVariable ) )
            // InternalStreamingsparql.g:349:4: (lv_variables_6_0= ruleVariable )
            {
            // InternalStreamingsparql.g:349:4: (lv_variables_6_0= ruleVariable )
            // InternalStreamingsparql.g:350:5: lv_variables_6_0= ruleVariable
            {

            					newCompositeNode(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_5_0());
            				
            pushFollow(FOLLOW_10);
            lv_variables_6_0=ruleVariable();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            					}
            					add(
            						current,
            						"variables",
            						lv_variables_6_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.Variable");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalStreamingsparql.g:367:3: ( (lv_variables_7_0= ruleVariable ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==RULE_ID||LA6_0==42) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalStreamingsparql.g:368:4: (lv_variables_7_0= ruleVariable )
            	    {
            	    // InternalStreamingsparql.g:368:4: (lv_variables_7_0= ruleVariable )
            	    // InternalStreamingsparql.g:369:5: lv_variables_7_0= ruleVariable
            	    {

            	    					newCompositeNode(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_6_0());
            	    				
            	    pushFollow(FOLLOW_10);
            	    lv_variables_7_0=ruleVariable();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            	    					}
            	    					add(
            	    						current,
            	    						"variables",
            	    						lv_variables_7_0,
            	    						"de.uniol.inf.is.odysseus.server.Streamingsparql.Variable");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            // InternalStreamingsparql.g:386:3: ( (lv_whereClause_8_0= ruleWhereClause ) )
            // InternalStreamingsparql.g:387:4: (lv_whereClause_8_0= ruleWhereClause )
            {
            // InternalStreamingsparql.g:387:4: (lv_whereClause_8_0= ruleWhereClause )
            // InternalStreamingsparql.g:388:5: lv_whereClause_8_0= ruleWhereClause
            {

            					newCompositeNode(grammarAccess.getSelectQueryAccess().getWhereClauseWhereClauseParserRuleCall_7_0());
            				
            pushFollow(FOLLOW_11);
            lv_whereClause_8_0=ruleWhereClause();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            					}
            					set(
            						current,
            						"whereClause",
            						lv_whereClause_8_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.WhereClause");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalStreamingsparql.g:405:3: ( (lv_filterclause_9_0= ruleFilterclause ) )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==30) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // InternalStreamingsparql.g:406:4: (lv_filterclause_9_0= ruleFilterclause )
                    {
                    // InternalStreamingsparql.g:406:4: (lv_filterclause_9_0= ruleFilterclause )
                    // InternalStreamingsparql.g:407:5: lv_filterclause_9_0= ruleFilterclause
                    {

                    					newCompositeNode(grammarAccess.getSelectQueryAccess().getFilterclauseFilterclauseParserRuleCall_8_0());
                    				
                    pushFollow(FOLLOW_12);
                    lv_filterclause_9_0=ruleFilterclause();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectQueryRule());
                    					}
                    					set(
                    						current,
                    						"filterclause",
                    						lv_filterclause_9_0,
                    						"de.uniol.inf.is.odysseus.server.Streamingsparql.Filterclause");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalStreamingsparql.g:424:3: ( (lv_aggregateClause_10_0= ruleAggregate ) )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==21) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // InternalStreamingsparql.g:425:4: (lv_aggregateClause_10_0= ruleAggregate )
                    {
                    // InternalStreamingsparql.g:425:4: (lv_aggregateClause_10_0= ruleAggregate )
                    // InternalStreamingsparql.g:426:5: lv_aggregateClause_10_0= ruleAggregate
                    {

                    					newCompositeNode(grammarAccess.getSelectQueryAccess().getAggregateClauseAggregateParserRuleCall_9_0());
                    				
                    pushFollow(FOLLOW_13);
                    lv_aggregateClause_10_0=ruleAggregate();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectQueryRule());
                    					}
                    					set(
                    						current,
                    						"aggregateClause",
                    						lv_aggregateClause_10_0,
                    						"de.uniol.inf.is.odysseus.server.Streamingsparql.Aggregate");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalStreamingsparql.g:443:3: ( (lv_filesinkclause_11_0= ruleFilesinkclause ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==29) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalStreamingsparql.g:444:4: (lv_filesinkclause_11_0= ruleFilesinkclause )
                    {
                    // InternalStreamingsparql.g:444:4: (lv_filesinkclause_11_0= ruleFilesinkclause )
                    // InternalStreamingsparql.g:445:5: lv_filesinkclause_11_0= ruleFilesinkclause
                    {

                    					newCompositeNode(grammarAccess.getSelectQueryAccess().getFilesinkclauseFilesinkclauseParserRuleCall_10_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_filesinkclause_11_0=ruleFilesinkclause();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSelectQueryRule());
                    					}
                    					set(
                    						current,
                    						"filesinkclause",
                    						lv_filesinkclause_11_0,
                    						"de.uniol.inf.is.odysseus.server.Streamingsparql.Filesinkclause");
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
    // $ANTLR end "ruleSelectQuery"


    // $ANTLR start "entryRuleAggregate"
    // InternalStreamingsparql.g:466:1: entryRuleAggregate returns [EObject current=null] : iv_ruleAggregate= ruleAggregate EOF ;
    public final EObject entryRuleAggregate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregate = null;


        try {
            // InternalStreamingsparql.g:466:50: (iv_ruleAggregate= ruleAggregate EOF )
            // InternalStreamingsparql.g:467:2: iv_ruleAggregate= ruleAggregate EOF
            {
             newCompositeNode(grammarAccess.getAggregateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAggregate=ruleAggregate();

            state._fsp--;

             current =iv_ruleAggregate; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAggregate"


    // $ANTLR start "ruleAggregate"
    // InternalStreamingsparql.g:473:1: ruleAggregate returns [EObject current=null] : ( () otherlv_1= 'AGGREGATE(' (otherlv_2= 'aggregations' otherlv_3= '=' otherlv_4= '[' ( (lv_aggregations_5_0= ruleAggregation ) )* otherlv_6= ']' )? ( (otherlv_7= ',' )? ( (lv_groupby_8_0= ruleGroupBy ) ) )? otherlv_9= ')' ) ;
    public final EObject ruleAggregate() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        EObject lv_aggregations_5_0 = null;

        EObject lv_groupby_8_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:479:2: ( ( () otherlv_1= 'AGGREGATE(' (otherlv_2= 'aggregations' otherlv_3= '=' otherlv_4= '[' ( (lv_aggregations_5_0= ruleAggregation ) )* otherlv_6= ']' )? ( (otherlv_7= ',' )? ( (lv_groupby_8_0= ruleGroupBy ) ) )? otherlv_9= ')' ) )
            // InternalStreamingsparql.g:480:2: ( () otherlv_1= 'AGGREGATE(' (otherlv_2= 'aggregations' otherlv_3= '=' otherlv_4= '[' ( (lv_aggregations_5_0= ruleAggregation ) )* otherlv_6= ']' )? ( (otherlv_7= ',' )? ( (lv_groupby_8_0= ruleGroupBy ) ) )? otherlv_9= ')' )
            {
            // InternalStreamingsparql.g:480:2: ( () otherlv_1= 'AGGREGATE(' (otherlv_2= 'aggregations' otherlv_3= '=' otherlv_4= '[' ( (lv_aggregations_5_0= ruleAggregation ) )* otherlv_6= ']' )? ( (otherlv_7= ',' )? ( (lv_groupby_8_0= ruleGroupBy ) ) )? otherlv_9= ')' )
            // InternalStreamingsparql.g:481:3: () otherlv_1= 'AGGREGATE(' (otherlv_2= 'aggregations' otherlv_3= '=' otherlv_4= '[' ( (lv_aggregations_5_0= ruleAggregation ) )* otherlv_6= ']' )? ( (otherlv_7= ',' )? ( (lv_groupby_8_0= ruleGroupBy ) ) )? otherlv_9= ')'
            {
            // InternalStreamingsparql.g:481:3: ()
            // InternalStreamingsparql.g:482:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getAggregateAccess().getAggregateAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,21,FOLLOW_14); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregateAccess().getAGGREGATEKeyword_1());
            		
            // InternalStreamingsparql.g:492:3: (otherlv_2= 'aggregations' otherlv_3= '=' otherlv_4= '[' ( (lv_aggregations_5_0= ruleAggregation ) )* otherlv_6= ']' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==22) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalStreamingsparql.g:493:4: otherlv_2= 'aggregations' otherlv_3= '=' otherlv_4= '[' ( (lv_aggregations_5_0= ruleAggregation ) )* otherlv_6= ']'
                    {
                    otherlv_2=(Token)match(input,22,FOLLOW_15); 

                    				newLeafNode(otherlv_2, grammarAccess.getAggregateAccess().getAggregationsKeyword_2_0());
                    			
                    otherlv_3=(Token)match(input,23,FOLLOW_16); 

                    				newLeafNode(otherlv_3, grammarAccess.getAggregateAccess().getEqualsSignKeyword_2_1());
                    			
                    otherlv_4=(Token)match(input,24,FOLLOW_17); 

                    				newLeafNode(otherlv_4, grammarAccess.getAggregateAccess().getLeftSquareBracketKeyword_2_2());
                    			
                    // InternalStreamingsparql.g:505:4: ( (lv_aggregations_5_0= ruleAggregation ) )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==24) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // InternalStreamingsparql.g:506:5: (lv_aggregations_5_0= ruleAggregation )
                    	    {
                    	    // InternalStreamingsparql.g:506:5: (lv_aggregations_5_0= ruleAggregation )
                    	    // InternalStreamingsparql.g:507:6: lv_aggregations_5_0= ruleAggregation
                    	    {

                    	    						newCompositeNode(grammarAccess.getAggregateAccess().getAggregationsAggregationParserRuleCall_2_3_0());
                    	    					
                    	    pushFollow(FOLLOW_17);
                    	    lv_aggregations_5_0=ruleAggregation();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getAggregateRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"aggregations",
                    	    							lv_aggregations_5_0,
                    	    							"de.uniol.inf.is.odysseus.server.Streamingsparql.Aggregation");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,25,FOLLOW_18); 

                    				newLeafNode(otherlv_6, grammarAccess.getAggregateAccess().getRightSquareBracketKeyword_2_4());
                    			

                    }
                    break;

            }

            // InternalStreamingsparql.g:529:3: ( (otherlv_7= ',' )? ( (lv_groupby_8_0= ruleGroupBy ) ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==26||LA13_0==28) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalStreamingsparql.g:530:4: (otherlv_7= ',' )? ( (lv_groupby_8_0= ruleGroupBy ) )
                    {
                    // InternalStreamingsparql.g:530:4: (otherlv_7= ',' )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==26) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // InternalStreamingsparql.g:531:5: otherlv_7= ','
                            {
                            otherlv_7=(Token)match(input,26,FOLLOW_19); 

                            					newLeafNode(otherlv_7, grammarAccess.getAggregateAccess().getCommaKeyword_3_0());
                            				

                            }
                            break;

                    }

                    // InternalStreamingsparql.g:536:4: ( (lv_groupby_8_0= ruleGroupBy ) )
                    // InternalStreamingsparql.g:537:5: (lv_groupby_8_0= ruleGroupBy )
                    {
                    // InternalStreamingsparql.g:537:5: (lv_groupby_8_0= ruleGroupBy )
                    // InternalStreamingsparql.g:538:6: lv_groupby_8_0= ruleGroupBy
                    {

                    						newCompositeNode(grammarAccess.getAggregateAccess().getGroupbyGroupByParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_20);
                    lv_groupby_8_0=ruleGroupBy();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAggregateRule());
                    						}
                    						set(
                    							current,
                    							"groupby",
                    							lv_groupby_8_0,
                    							"de.uniol.inf.is.odysseus.server.Streamingsparql.GroupBy");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_9=(Token)match(input,27,FOLLOW_2); 

            			newLeafNode(otherlv_9, grammarAccess.getAggregateAccess().getRightParenthesisKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAggregate"


    // $ANTLR start "entryRuleGroupBy"
    // InternalStreamingsparql.g:564:1: entryRuleGroupBy returns [EObject current=null] : iv_ruleGroupBy= ruleGroupBy EOF ;
    public final EObject entryRuleGroupBy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroupBy = null;


        try {
            // InternalStreamingsparql.g:564:48: (iv_ruleGroupBy= ruleGroupBy EOF )
            // InternalStreamingsparql.g:565:2: iv_ruleGroupBy= ruleGroupBy EOF
            {
             newCompositeNode(grammarAccess.getGroupByRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGroupBy=ruleGroupBy();

            state._fsp--;

             current =iv_ruleGroupBy; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGroupBy"


    // $ANTLR start "ruleGroupBy"
    // InternalStreamingsparql.g:571:1: ruleGroupBy returns [EObject current=null] : (otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleGroupBy() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_variables_1_0 = null;

        EObject lv_variables_3_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:577:2: ( (otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']' ) )
            // InternalStreamingsparql.g:578:2: (otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']' )
            {
            // InternalStreamingsparql.g:578:2: (otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']' )
            // InternalStreamingsparql.g:579:3: otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,28,FOLLOW_9); 

            			newLeafNode(otherlv_0, grammarAccess.getGroupByAccess().getGroup_byKeyword_0());
            		
            // InternalStreamingsparql.g:583:3: ( (lv_variables_1_0= ruleVariable ) )
            // InternalStreamingsparql.g:584:4: (lv_variables_1_0= ruleVariable )
            {
            // InternalStreamingsparql.g:584:4: (lv_variables_1_0= ruleVariable )
            // InternalStreamingsparql.g:585:5: lv_variables_1_0= ruleVariable
            {

            					newCompositeNode(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_21);
            lv_variables_1_0=ruleVariable();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getGroupByRule());
            					}
            					add(
            						current,
            						"variables",
            						lv_variables_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.Variable");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalStreamingsparql.g:602:3: (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==26) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalStreamingsparql.g:603:4: otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) )
            	    {
            	    otherlv_2=(Token)match(input,26,FOLLOW_9); 

            	    				newLeafNode(otherlv_2, grammarAccess.getGroupByAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalStreamingsparql.g:607:4: ( (lv_variables_3_0= ruleVariable ) )
            	    // InternalStreamingsparql.g:608:5: (lv_variables_3_0= ruleVariable )
            	    {
            	    // InternalStreamingsparql.g:608:5: (lv_variables_3_0= ruleVariable )
            	    // InternalStreamingsparql.g:609:6: lv_variables_3_0= ruleVariable
            	    {

            	    						newCompositeNode(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_21);
            	    lv_variables_3_0=ruleVariable();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getGroupByRule());
            	    						}
            	    						add(
            	    							current,
            	    							"variables",
            	    							lv_variables_3_0,
            	    							"de.uniol.inf.is.odysseus.server.Streamingsparql.Variable");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            otherlv_4=(Token)match(input,25,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getGroupByAccess().getRightSquareBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGroupBy"


    // $ANTLR start "entryRuleAggregation"
    // InternalStreamingsparql.g:635:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // InternalStreamingsparql.g:635:52: (iv_ruleAggregation= ruleAggregation EOF )
            // InternalStreamingsparql.g:636:2: iv_ruleAggregation= ruleAggregation EOF
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
    // InternalStreamingsparql.g:642:1: ruleAggregation returns [EObject current=null] : (otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )? ) ;
    public final EObject ruleAggregation() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_function_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token lv_aggName_5_0=null;
        Token otherlv_6=null;
        Token lv_datatype_7_0=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        EObject lv_varToAgg_3_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:648:2: ( (otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )? ) )
            // InternalStreamingsparql.g:649:2: (otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )? )
            {
            // InternalStreamingsparql.g:649:2: (otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )? )
            // InternalStreamingsparql.g:650:3: otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )?
            {
            otherlv_0=(Token)match(input,24,FOLLOW_22); 

            			newLeafNode(otherlv_0, grammarAccess.getAggregationAccess().getLeftSquareBracketKeyword_0());
            		
            // InternalStreamingsparql.g:654:3: ( (lv_function_1_0= RULE_AGG_FUNCTION ) )
            // InternalStreamingsparql.g:655:4: (lv_function_1_0= RULE_AGG_FUNCTION )
            {
            // InternalStreamingsparql.g:655:4: (lv_function_1_0= RULE_AGG_FUNCTION )
            // InternalStreamingsparql.g:656:5: lv_function_1_0= RULE_AGG_FUNCTION
            {
            lv_function_1_0=(Token)match(input,RULE_AGG_FUNCTION,FOLLOW_23); 

            					newLeafNode(lv_function_1_0, grammarAccess.getAggregationAccess().getFunctionAGG_FUNCTIONTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAggregationRule());
            					}
            					setWithLastConsumed(
            						current,
            						"function",
            						lv_function_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.AGG_FUNCTION");
            				

            }


            }

            otherlv_2=(Token)match(input,26,FOLLOW_9); 

            			newLeafNode(otherlv_2, grammarAccess.getAggregationAccess().getCommaKeyword_2());
            		
            // InternalStreamingsparql.g:676:3: ( (lv_varToAgg_3_0= ruleVariable ) )
            // InternalStreamingsparql.g:677:4: (lv_varToAgg_3_0= ruleVariable )
            {
            // InternalStreamingsparql.g:677:4: (lv_varToAgg_3_0= ruleVariable )
            // InternalStreamingsparql.g:678:5: lv_varToAgg_3_0= ruleVariable
            {

            					newCompositeNode(grammarAccess.getAggregationAccess().getVarToAggVariableParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_23);
            lv_varToAgg_3_0=ruleVariable();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAggregationRule());
            					}
            					set(
            						current,
            						"varToAgg",
            						lv_varToAgg_3_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.Variable");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_4=(Token)match(input,26,FOLLOW_24); 

            			newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getCommaKeyword_4());
            		
            // InternalStreamingsparql.g:699:3: ( (lv_aggName_5_0= RULE_STRING ) )
            // InternalStreamingsparql.g:700:4: (lv_aggName_5_0= RULE_STRING )
            {
            // InternalStreamingsparql.g:700:4: (lv_aggName_5_0= RULE_STRING )
            // InternalStreamingsparql.g:701:5: lv_aggName_5_0= RULE_STRING
            {
            lv_aggName_5_0=(Token)match(input,RULE_STRING,FOLLOW_21); 

            					newLeafNode(lv_aggName_5_0, grammarAccess.getAggregationAccess().getAggNameSTRINGTerminalRuleCall_5_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAggregationRule());
            					}
            					setWithLastConsumed(
            						current,
            						"aggName",
            						lv_aggName_5_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.STRING");
            				

            }


            }

            // InternalStreamingsparql.g:717:3: (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==26) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalStreamingsparql.g:718:4: otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) )
                    {
                    otherlv_6=(Token)match(input,26,FOLLOW_24); 

                    				newLeafNode(otherlv_6, grammarAccess.getAggregationAccess().getCommaKeyword_6_0());
                    			
                    // InternalStreamingsparql.g:722:4: ( (lv_datatype_7_0= RULE_STRING ) )
                    // InternalStreamingsparql.g:723:5: (lv_datatype_7_0= RULE_STRING )
                    {
                    // InternalStreamingsparql.g:723:5: (lv_datatype_7_0= RULE_STRING )
                    // InternalStreamingsparql.g:724:6: lv_datatype_7_0= RULE_STRING
                    {
                    lv_datatype_7_0=(Token)match(input,RULE_STRING,FOLLOW_25); 

                    						newLeafNode(lv_datatype_7_0, grammarAccess.getAggregationAccess().getDatatypeSTRINGTerminalRuleCall_6_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregationRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"datatype",
                    							lv_datatype_7_0,
                    							"de.uniol.inf.is.odysseus.server.Streamingsparql.STRING");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_8=(Token)match(input,25,FOLLOW_26); 

            			newLeafNode(otherlv_8, grammarAccess.getAggregationAccess().getRightSquareBracketKeyword_7());
            		
            // InternalStreamingsparql.g:745:3: (otherlv_9= ',' )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==26) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalStreamingsparql.g:746:4: otherlv_9= ','
                    {
                    otherlv_9=(Token)match(input,26,FOLLOW_2); 

                    				newLeafNode(otherlv_9, grammarAccess.getAggregationAccess().getCommaKeyword_8());
                    			

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


    // $ANTLR start "entryRuleFilesinkclause"
    // InternalStreamingsparql.g:755:1: entryRuleFilesinkclause returns [EObject current=null] : iv_ruleFilesinkclause= ruleFilesinkclause EOF ;
    public final EObject entryRuleFilesinkclause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFilesinkclause = null;


        try {
            // InternalStreamingsparql.g:755:55: (iv_ruleFilesinkclause= ruleFilesinkclause EOF )
            // InternalStreamingsparql.g:756:2: iv_ruleFilesinkclause= ruleFilesinkclause EOF
            {
             newCompositeNode(grammarAccess.getFilesinkclauseRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFilesinkclause=ruleFilesinkclause();

            state._fsp--;

             current =iv_ruleFilesinkclause; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFilesinkclause"


    // $ANTLR start "ruleFilesinkclause"
    // InternalStreamingsparql.g:762:1: ruleFilesinkclause returns [EObject current=null] : (otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')' ) ;
    public final EObject ruleFilesinkclause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_path_1_0=null;
        Token otherlv_2=null;


        	enterRule();

        try {
            // InternalStreamingsparql.g:768:2: ( (otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')' ) )
            // InternalStreamingsparql.g:769:2: (otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')' )
            {
            // InternalStreamingsparql.g:769:2: (otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')' )
            // InternalStreamingsparql.g:770:3: otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_24); 

            			newLeafNode(otherlv_0, grammarAccess.getFilesinkclauseAccess().getCSVFILESINKKeyword_0());
            		
            // InternalStreamingsparql.g:774:3: ( (lv_path_1_0= RULE_STRING ) )
            // InternalStreamingsparql.g:775:4: (lv_path_1_0= RULE_STRING )
            {
            // InternalStreamingsparql.g:775:4: (lv_path_1_0= RULE_STRING )
            // InternalStreamingsparql.g:776:5: lv_path_1_0= RULE_STRING
            {
            lv_path_1_0=(Token)match(input,RULE_STRING,FOLLOW_20); 

            					newLeafNode(lv_path_1_0, grammarAccess.getFilesinkclauseAccess().getPathSTRINGTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFilesinkclauseRule());
            					}
            					setWithLastConsumed(
            						current,
            						"path",
            						lv_path_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.STRING");
            				

            }


            }

            otherlv_2=(Token)match(input,27,FOLLOW_2); 

            			newLeafNode(otherlv_2, grammarAccess.getFilesinkclauseAccess().getRightParenthesisKeyword_2());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFilesinkclause"


    // $ANTLR start "entryRuleFilterclause"
    // InternalStreamingsparql.g:800:1: entryRuleFilterclause returns [EObject current=null] : iv_ruleFilterclause= ruleFilterclause EOF ;
    public final EObject entryRuleFilterclause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFilterclause = null;


        try {
            // InternalStreamingsparql.g:800:53: (iv_ruleFilterclause= ruleFilterclause EOF )
            // InternalStreamingsparql.g:801:2: iv_ruleFilterclause= ruleFilterclause EOF
            {
             newCompositeNode(grammarAccess.getFilterclauseRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFilterclause=ruleFilterclause();

            state._fsp--;

             current =iv_ruleFilterclause; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFilterclause"


    // $ANTLR start "ruleFilterclause"
    // InternalStreamingsparql.g:807:1: ruleFilterclause returns [EObject current=null] : (otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')' ) ;
    public final EObject ruleFilterclause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_4=null;
        EObject lv_left_1_0 = null;

        Enumerator lv_operator_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:813:2: ( (otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')' ) )
            // InternalStreamingsparql.g:814:2: (otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')' )
            {
            // InternalStreamingsparql.g:814:2: (otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')' )
            // InternalStreamingsparql.g:815:3: otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')'
            {
            otherlv_0=(Token)match(input,30,FOLLOW_9); 

            			newLeafNode(otherlv_0, grammarAccess.getFilterclauseAccess().getFILTERKeyword_0());
            		
            // InternalStreamingsparql.g:819:3: ( (lv_left_1_0= ruleVariable ) )
            // InternalStreamingsparql.g:820:4: (lv_left_1_0= ruleVariable )
            {
            // InternalStreamingsparql.g:820:4: (lv_left_1_0= ruleVariable )
            // InternalStreamingsparql.g:821:5: lv_left_1_0= ruleVariable
            {

            					newCompositeNode(grammarAccess.getFilterclauseAccess().getLeftVariableParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_27);
            lv_left_1_0=ruleVariable();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getFilterclauseRule());
            					}
            					set(
            						current,
            						"left",
            						lv_left_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.Variable");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalStreamingsparql.g:838:3: ( (lv_operator_2_0= ruleOperator ) )
            // InternalStreamingsparql.g:839:4: (lv_operator_2_0= ruleOperator )
            {
            // InternalStreamingsparql.g:839:4: (lv_operator_2_0= ruleOperator )
            // InternalStreamingsparql.g:840:5: lv_operator_2_0= ruleOperator
            {

            					newCompositeNode(grammarAccess.getFilterclauseAccess().getOperatorOperatorEnumRuleCall_2_0());
            				
            pushFollow(FOLLOW_9);
            lv_operator_2_0=ruleOperator();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getFilterclauseRule());
            					}
            					set(
            						current,
            						"operator",
            						lv_operator_2_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.Operator");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalStreamingsparql.g:857:3: ( (lv_right_3_0= ruleVariable ) )
            // InternalStreamingsparql.g:858:4: (lv_right_3_0= ruleVariable )
            {
            // InternalStreamingsparql.g:858:4: (lv_right_3_0= ruleVariable )
            // InternalStreamingsparql.g:859:5: lv_right_3_0= ruleVariable
            {

            					newCompositeNode(grammarAccess.getFilterclauseAccess().getRightVariableParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_20);
            lv_right_3_0=ruleVariable();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getFilterclauseRule());
            					}
            					set(
            						current,
            						"right",
            						lv_right_3_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.Variable");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_4=(Token)match(input,27,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getFilterclauseAccess().getRightParenthesisKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFilterclause"


    // $ANTLR start "entryRuleDatasetClause"
    // InternalStreamingsparql.g:884:1: entryRuleDatasetClause returns [EObject current=null] : iv_ruleDatasetClause= ruleDatasetClause EOF ;
    public final EObject entryRuleDatasetClause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDatasetClause = null;


        try {
            // InternalStreamingsparql.g:884:54: (iv_ruleDatasetClause= ruleDatasetClause EOF )
            // InternalStreamingsparql.g:885:2: iv_ruleDatasetClause= ruleDatasetClause EOF
            {
             newCompositeNode(grammarAccess.getDatasetClauseRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDatasetClause=ruleDatasetClause();

            state._fsp--;

             current =iv_ruleDatasetClause; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDatasetClause"


    // $ANTLR start "ruleDatasetClause"
    // InternalStreamingsparql.g:891:1: ruleDatasetClause returns [EObject current=null] : (otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[' otherlv_5= 'TYPE' ( (lv_type_6_0= RULE_WINDOWTYPE ) ) otherlv_7= 'SIZE' ( (lv_size_8_0= RULE_INT ) ) (otherlv_9= 'ADVANCE' ( (lv_advance_10_0= RULE_INT ) ) )? (otherlv_11= 'UNIT' ( (lv_unit_12_0= RULE_UNITTYPE ) ) )? otherlv_13= ']' )? ) ;
    public final EObject ruleDatasetClause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token lv_type_6_0=null;
        Token otherlv_7=null;
        Token lv_size_8_0=null;
        Token otherlv_9=null;
        Token lv_advance_10_0=null;
        Token otherlv_11=null;
        Token lv_unit_12_0=null;
        Token otherlv_13=null;
        EObject lv_dataSet_1_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:897:2: ( (otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[' otherlv_5= 'TYPE' ( (lv_type_6_0= RULE_WINDOWTYPE ) ) otherlv_7= 'SIZE' ( (lv_size_8_0= RULE_INT ) ) (otherlv_9= 'ADVANCE' ( (lv_advance_10_0= RULE_INT ) ) )? (otherlv_11= 'UNIT' ( (lv_unit_12_0= RULE_UNITTYPE ) ) )? otherlv_13= ']' )? ) )
            // InternalStreamingsparql.g:898:2: (otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[' otherlv_5= 'TYPE' ( (lv_type_6_0= RULE_WINDOWTYPE ) ) otherlv_7= 'SIZE' ( (lv_size_8_0= RULE_INT ) ) (otherlv_9= 'ADVANCE' ( (lv_advance_10_0= RULE_INT ) ) )? (otherlv_11= 'UNIT' ( (lv_unit_12_0= RULE_UNITTYPE ) ) )? otherlv_13= ']' )? )
            {
            // InternalStreamingsparql.g:898:2: (otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[' otherlv_5= 'TYPE' ( (lv_type_6_0= RULE_WINDOWTYPE ) ) otherlv_7= 'SIZE' ( (lv_size_8_0= RULE_INT ) ) (otherlv_9= 'ADVANCE' ( (lv_advance_10_0= RULE_INT ) ) )? (otherlv_11= 'UNIT' ( (lv_unit_12_0= RULE_UNITTYPE ) ) )? otherlv_13= ']' )? )
            // InternalStreamingsparql.g:899:3: otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[' otherlv_5= 'TYPE' ( (lv_type_6_0= RULE_WINDOWTYPE ) ) otherlv_7= 'SIZE' ( (lv_size_8_0= RULE_INT ) ) (otherlv_9= 'ADVANCE' ( (lv_advance_10_0= RULE_INT ) ) )? (otherlv_11= 'UNIT' ( (lv_unit_12_0= RULE_UNITTYPE ) ) )? otherlv_13= ']' )?
            {
            otherlv_0=(Token)match(input,31,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getDatasetClauseAccess().getFROMKeyword_0());
            		
            // InternalStreamingsparql.g:903:3: ( (lv_dataSet_1_0= ruleIRI ) )
            // InternalStreamingsparql.g:904:4: (lv_dataSet_1_0= ruleIRI )
            {
            // InternalStreamingsparql.g:904:4: (lv_dataSet_1_0= ruleIRI )
            // InternalStreamingsparql.g:905:5: lv_dataSet_1_0= ruleIRI
            {

            					newCompositeNode(grammarAccess.getDatasetClauseAccess().getDataSetIRIParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_28);
            lv_dataSet_1_0=ruleIRI();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDatasetClauseRule());
            					}
            					set(
            						current,
            						"dataSet",
            						lv_dataSet_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.IRI");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,32,FOLLOW_3); 

            			newLeafNode(otherlv_2, grammarAccess.getDatasetClauseAccess().getASKeyword_2());
            		
            // InternalStreamingsparql.g:926:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalStreamingsparql.g:927:4: (lv_name_3_0= RULE_ID )
            {
            // InternalStreamingsparql.g:927:4: (lv_name_3_0= RULE_ID )
            // InternalStreamingsparql.g:928:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_29); 

            					newLeafNode(lv_name_3_0, grammarAccess.getDatasetClauseAccess().getNameIDTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDatasetClauseRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_3_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalStreamingsparql.g:944:3: (otherlv_4= '[' otherlv_5= 'TYPE' ( (lv_type_6_0= RULE_WINDOWTYPE ) ) otherlv_7= 'SIZE' ( (lv_size_8_0= RULE_INT ) ) (otherlv_9= 'ADVANCE' ( (lv_advance_10_0= RULE_INT ) ) )? (otherlv_11= 'UNIT' ( (lv_unit_12_0= RULE_UNITTYPE ) ) )? otherlv_13= ']' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==24) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalStreamingsparql.g:945:4: otherlv_4= '[' otherlv_5= 'TYPE' ( (lv_type_6_0= RULE_WINDOWTYPE ) ) otherlv_7= 'SIZE' ( (lv_size_8_0= RULE_INT ) ) (otherlv_9= 'ADVANCE' ( (lv_advance_10_0= RULE_INT ) ) )? (otherlv_11= 'UNIT' ( (lv_unit_12_0= RULE_UNITTYPE ) ) )? otherlv_13= ']'
                    {
                    otherlv_4=(Token)match(input,24,FOLLOW_30); 

                    				newLeafNode(otherlv_4, grammarAccess.getDatasetClauseAccess().getLeftSquareBracketKeyword_4_0());
                    			
                    otherlv_5=(Token)match(input,33,FOLLOW_31); 

                    				newLeafNode(otherlv_5, grammarAccess.getDatasetClauseAccess().getTYPEKeyword_4_1());
                    			
                    // InternalStreamingsparql.g:953:4: ( (lv_type_6_0= RULE_WINDOWTYPE ) )
                    // InternalStreamingsparql.g:954:5: (lv_type_6_0= RULE_WINDOWTYPE )
                    {
                    // InternalStreamingsparql.g:954:5: (lv_type_6_0= RULE_WINDOWTYPE )
                    // InternalStreamingsparql.g:955:6: lv_type_6_0= RULE_WINDOWTYPE
                    {
                    lv_type_6_0=(Token)match(input,RULE_WINDOWTYPE,FOLLOW_32); 

                    						newLeafNode(lv_type_6_0, grammarAccess.getDatasetClauseAccess().getTypeWINDOWTYPETerminalRuleCall_4_2_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDatasetClauseRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"type",
                    							lv_type_6_0,
                    							"de.uniol.inf.is.odysseus.server.Streamingsparql.WINDOWTYPE");
                    					

                    }


                    }

                    otherlv_7=(Token)match(input,34,FOLLOW_33); 

                    				newLeafNode(otherlv_7, grammarAccess.getDatasetClauseAccess().getSIZEKeyword_4_3());
                    			
                    // InternalStreamingsparql.g:975:4: ( (lv_size_8_0= RULE_INT ) )
                    // InternalStreamingsparql.g:976:5: (lv_size_8_0= RULE_INT )
                    {
                    // InternalStreamingsparql.g:976:5: (lv_size_8_0= RULE_INT )
                    // InternalStreamingsparql.g:977:6: lv_size_8_0= RULE_INT
                    {
                    lv_size_8_0=(Token)match(input,RULE_INT,FOLLOW_34); 

                    						newLeafNode(lv_size_8_0, grammarAccess.getDatasetClauseAccess().getSizeINTTerminalRuleCall_4_4_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDatasetClauseRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"size",
                    							lv_size_8_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }

                    // InternalStreamingsparql.g:993:4: (otherlv_9= 'ADVANCE' ( (lv_advance_10_0= RULE_INT ) ) )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==35) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // InternalStreamingsparql.g:994:5: otherlv_9= 'ADVANCE' ( (lv_advance_10_0= RULE_INT ) )
                            {
                            otherlv_9=(Token)match(input,35,FOLLOW_33); 

                            					newLeafNode(otherlv_9, grammarAccess.getDatasetClauseAccess().getADVANCEKeyword_4_5_0());
                            				
                            // InternalStreamingsparql.g:998:5: ( (lv_advance_10_0= RULE_INT ) )
                            // InternalStreamingsparql.g:999:6: (lv_advance_10_0= RULE_INT )
                            {
                            // InternalStreamingsparql.g:999:6: (lv_advance_10_0= RULE_INT )
                            // InternalStreamingsparql.g:1000:7: lv_advance_10_0= RULE_INT
                            {
                            lv_advance_10_0=(Token)match(input,RULE_INT,FOLLOW_35); 

                            							newLeafNode(lv_advance_10_0, grammarAccess.getDatasetClauseAccess().getAdvanceINTTerminalRuleCall_4_5_1_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getDatasetClauseRule());
                            							}
                            							setWithLastConsumed(
                            								current,
                            								"advance",
                            								lv_advance_10_0,
                            								"org.eclipse.xtext.common.Terminals.INT");
                            						

                            }


                            }


                            }
                            break;

                    }

                    // InternalStreamingsparql.g:1017:4: (otherlv_11= 'UNIT' ( (lv_unit_12_0= RULE_UNITTYPE ) ) )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==36) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // InternalStreamingsparql.g:1018:5: otherlv_11= 'UNIT' ( (lv_unit_12_0= RULE_UNITTYPE ) )
                            {
                            otherlv_11=(Token)match(input,36,FOLLOW_36); 

                            					newLeafNode(otherlv_11, grammarAccess.getDatasetClauseAccess().getUNITKeyword_4_6_0());
                            				
                            // InternalStreamingsparql.g:1022:5: ( (lv_unit_12_0= RULE_UNITTYPE ) )
                            // InternalStreamingsparql.g:1023:6: (lv_unit_12_0= RULE_UNITTYPE )
                            {
                            // InternalStreamingsparql.g:1023:6: (lv_unit_12_0= RULE_UNITTYPE )
                            // InternalStreamingsparql.g:1024:7: lv_unit_12_0= RULE_UNITTYPE
                            {
                            lv_unit_12_0=(Token)match(input,RULE_UNITTYPE,FOLLOW_25); 

                            							newLeafNode(lv_unit_12_0, grammarAccess.getDatasetClauseAccess().getUnitUNITTYPETerminalRuleCall_4_6_1_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getDatasetClauseRule());
                            							}
                            							setWithLastConsumed(
                            								current,
                            								"unit",
                            								lv_unit_12_0,
                            								"de.uniol.inf.is.odysseus.server.Streamingsparql.UNITTYPE");
                            						

                            }


                            }


                            }
                            break;

                    }

                    otherlv_13=(Token)match(input,25,FOLLOW_2); 

                    				newLeafNode(otherlv_13, grammarAccess.getDatasetClauseAccess().getRightSquareBracketKeyword_4_7());
                    			

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
    // $ANTLR end "ruleDatasetClause"


    // $ANTLR start "entryRuleWhereClause"
    // InternalStreamingsparql.g:1050:1: entryRuleWhereClause returns [EObject current=null] : iv_ruleWhereClause= ruleWhereClause EOF ;
    public final EObject entryRuleWhereClause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWhereClause = null;


        try {
            // InternalStreamingsparql.g:1050:52: (iv_ruleWhereClause= ruleWhereClause EOF )
            // InternalStreamingsparql.g:1051:2: iv_ruleWhereClause= ruleWhereClause EOF
            {
             newCompositeNode(grammarAccess.getWhereClauseRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWhereClause=ruleWhereClause();

            state._fsp--;

             current =iv_ruleWhereClause; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWhereClause"


    // $ANTLR start "ruleWhereClause"
    // InternalStreamingsparql.g:1057:1: ruleWhereClause returns [EObject current=null] : (otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}' ) ;
    public final EObject ruleWhereClause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_whereclauses_2_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:1063:2: ( (otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}' ) )
            // InternalStreamingsparql.g:1064:2: (otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}' )
            {
            // InternalStreamingsparql.g:1064:2: (otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}' )
            // InternalStreamingsparql.g:1065:3: otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}'
            {
            otherlv_0=(Token)match(input,37,FOLLOW_37); 

            			newLeafNode(otherlv_0, grammarAccess.getWhereClauseAccess().getWHEREKeyword_0());
            		
            otherlv_1=(Token)match(input,38,FOLLOW_3); 

            			newLeafNode(otherlv_1, grammarAccess.getWhereClauseAccess().getLeftCurlyBracketKeyword_1());
            		
            // InternalStreamingsparql.g:1073:3: ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+
            int cnt20=0;
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==RULE_ID) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // InternalStreamingsparql.g:1074:4: (lv_whereclauses_2_0= ruleInnerWhereClause )
            	    {
            	    // InternalStreamingsparql.g:1074:4: (lv_whereclauses_2_0= ruleInnerWhereClause )
            	    // InternalStreamingsparql.g:1075:5: lv_whereclauses_2_0= ruleInnerWhereClause
            	    {

            	    					newCompositeNode(grammarAccess.getWhereClauseAccess().getWhereclausesInnerWhereClauseParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_38);
            	    lv_whereclauses_2_0=ruleInnerWhereClause();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getWhereClauseRule());
            	    					}
            	    					add(
            	    						current,
            	    						"whereclauses",
            	    						lv_whereclauses_2_0,
            	    						"de.uniol.inf.is.odysseus.server.Streamingsparql.InnerWhereClause");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt20 >= 1 ) break loop20;
                        EarlyExitException eee =
                            new EarlyExitException(20, input);
                        throw eee;
                }
                cnt20++;
            } while (true);

            otherlv_3=(Token)match(input,39,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getWhereClauseAccess().getRightCurlyBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWhereClause"


    // $ANTLR start "entryRuleInnerWhereClause"
    // InternalStreamingsparql.g:1100:1: entryRuleInnerWhereClause returns [EObject current=null] : iv_ruleInnerWhereClause= ruleInnerWhereClause EOF ;
    public final EObject entryRuleInnerWhereClause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInnerWhereClause = null;


        try {
            // InternalStreamingsparql.g:1100:57: (iv_ruleInnerWhereClause= ruleInnerWhereClause EOF )
            // InternalStreamingsparql.g:1101:2: iv_ruleInnerWhereClause= ruleInnerWhereClause EOF
            {
             newCompositeNode(grammarAccess.getInnerWhereClauseRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleInnerWhereClause=ruleInnerWhereClause();

            state._fsp--;

             current =iv_ruleInnerWhereClause; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleInnerWhereClause"


    // $ANTLR start "ruleInnerWhereClause"
    // InternalStreamingsparql.g:1107:1: ruleInnerWhereClause returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) ) ) ;
    public final EObject ruleInnerWhereClause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_groupGraphPattern_1_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:1113:2: ( ( ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) ) ) )
            // InternalStreamingsparql.g:1114:2: ( ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) ) )
            {
            // InternalStreamingsparql.g:1114:2: ( ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) ) )
            // InternalStreamingsparql.g:1115:3: ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) )
            {
            // InternalStreamingsparql.g:1115:3: ( (otherlv_0= RULE_ID ) )
            // InternalStreamingsparql.g:1116:4: (otherlv_0= RULE_ID )
            {
            // InternalStreamingsparql.g:1116:4: (otherlv_0= RULE_ID )
            // InternalStreamingsparql.g:1117:5: otherlv_0= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getInnerWhereClauseRule());
            					}
            				
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_37); 

            					newLeafNode(otherlv_0, grammarAccess.getInnerWhereClauseAccess().getNameDatasetClauseCrossReference_0_0());
            				

            }


            }

            // InternalStreamingsparql.g:1128:3: ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) )
            // InternalStreamingsparql.g:1129:4: (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub )
            {
            // InternalStreamingsparql.g:1129:4: (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub )
            // InternalStreamingsparql.g:1130:5: lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub
            {

            					newCompositeNode(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternGroupGraphPatternSubParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_groupGraphPattern_1_0=ruleGroupGraphPatternSub();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getInnerWhereClauseRule());
            					}
            					set(
            						current,
            						"groupGraphPattern",
            						lv_groupGraphPattern_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.GroupGraphPatternSub");
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
    // $ANTLR end "ruleInnerWhereClause"


    // $ANTLR start "entryRuleGroupGraphPatternSub"
    // InternalStreamingsparql.g:1151:1: entryRuleGroupGraphPatternSub returns [EObject current=null] : iv_ruleGroupGraphPatternSub= ruleGroupGraphPatternSub EOF ;
    public final EObject entryRuleGroupGraphPatternSub() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroupGraphPatternSub = null;


        try {
            // InternalStreamingsparql.g:1151:61: (iv_ruleGroupGraphPatternSub= ruleGroupGraphPatternSub EOF )
            // InternalStreamingsparql.g:1152:2: iv_ruleGroupGraphPatternSub= ruleGroupGraphPatternSub EOF
            {
             newCompositeNode(grammarAccess.getGroupGraphPatternSubRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGroupGraphPatternSub=ruleGroupGraphPatternSub();

            state._fsp--;

             current =iv_ruleGroupGraphPatternSub; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGroupGraphPatternSub"


    // $ANTLR start "ruleGroupGraphPatternSub"
    // InternalStreamingsparql.g:1158:1: ruleGroupGraphPatternSub returns [EObject current=null] : (otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}' ) ;
    public final EObject ruleGroupGraphPatternSub() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        EObject lv_graphPatterns_1_0 = null;

        EObject lv_graphPatterns_3_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:1164:2: ( (otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}' ) )
            // InternalStreamingsparql.g:1165:2: (otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}' )
            {
            // InternalStreamingsparql.g:1165:2: (otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}' )
            // InternalStreamingsparql.g:1166:3: otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}'
            {
            otherlv_0=(Token)match(input,38,FOLLOW_39); 

            			newLeafNode(otherlv_0, grammarAccess.getGroupGraphPatternSubAccess().getLeftCurlyBracketKeyword_0());
            		
            // InternalStreamingsparql.g:1170:3: ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) )
            // InternalStreamingsparql.g:1171:4: (lv_graphPatterns_1_0= ruleTriplesSameSubject )
            {
            // InternalStreamingsparql.g:1171:4: (lv_graphPatterns_1_0= ruleTriplesSameSubject )
            // InternalStreamingsparql.g:1172:5: lv_graphPatterns_1_0= ruleTriplesSameSubject
            {

            					newCompositeNode(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_40);
            lv_graphPatterns_1_0=ruleTriplesSameSubject();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getGroupGraphPatternSubRule());
            					}
            					add(
            						current,
            						"graphPatterns",
            						lv_graphPatterns_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.TriplesSameSubject");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalStreamingsparql.g:1189:3: (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==40) ) {
                    int LA21_1 = input.LA(2);

                    if ( ((LA21_1>=RULE_ID && LA21_1<=RULE_IRI_TERMINAL)||LA21_1==RULE_STRING||LA21_1==42) ) {
                        alt21=1;
                    }


                }


                switch (alt21) {
            	case 1 :
            	    // InternalStreamingsparql.g:1190:4: otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) )
            	    {
            	    otherlv_2=(Token)match(input,40,FOLLOW_39); 

            	    				newLeafNode(otherlv_2, grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_2_0());
            	    			
            	    // InternalStreamingsparql.g:1194:4: ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) )
            	    // InternalStreamingsparql.g:1195:5: (lv_graphPatterns_3_0= ruleTriplesSameSubject )
            	    {
            	    // InternalStreamingsparql.g:1195:5: (lv_graphPatterns_3_0= ruleTriplesSameSubject )
            	    // InternalStreamingsparql.g:1196:6: lv_graphPatterns_3_0= ruleTriplesSameSubject
            	    {

            	    						newCompositeNode(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_40);
            	    lv_graphPatterns_3_0=ruleTriplesSameSubject();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getGroupGraphPatternSubRule());
            	    						}
            	    						add(
            	    							current,
            	    							"graphPatterns",
            	    							lv_graphPatterns_3_0,
            	    							"de.uniol.inf.is.odysseus.server.Streamingsparql.TriplesSameSubject");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

            // InternalStreamingsparql.g:1214:3: (otherlv_4= '.' )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==40) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalStreamingsparql.g:1215:4: otherlv_4= '.'
                    {
                    otherlv_4=(Token)match(input,40,FOLLOW_41); 

                    				newLeafNode(otherlv_4, grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_3());
                    			

                    }
                    break;

            }

            otherlv_5=(Token)match(input,39,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getGroupGraphPatternSubAccess().getRightCurlyBracketKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGroupGraphPatternSub"


    // $ANTLR start "entryRuleTriplesSameSubject"
    // InternalStreamingsparql.g:1228:1: entryRuleTriplesSameSubject returns [EObject current=null] : iv_ruleTriplesSameSubject= ruleTriplesSameSubject EOF ;
    public final EObject entryRuleTriplesSameSubject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTriplesSameSubject = null;


        try {
            // InternalStreamingsparql.g:1228:59: (iv_ruleTriplesSameSubject= ruleTriplesSameSubject EOF )
            // InternalStreamingsparql.g:1229:2: iv_ruleTriplesSameSubject= ruleTriplesSameSubject EOF
            {
             newCompositeNode(grammarAccess.getTriplesSameSubjectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTriplesSameSubject=ruleTriplesSameSubject();

            state._fsp--;

             current =iv_ruleTriplesSameSubject; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTriplesSameSubject"


    // $ANTLR start "ruleTriplesSameSubject"
    // InternalStreamingsparql.g:1235:1: ruleTriplesSameSubject returns [EObject current=null] : ( ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )* ) ;
    public final EObject ruleTriplesSameSubject() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_subject_0_0 = null;

        EObject lv_propertyList_1_0 = null;

        EObject lv_propertyList_3_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:1241:2: ( ( ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )* ) )
            // InternalStreamingsparql.g:1242:2: ( ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )* )
            {
            // InternalStreamingsparql.g:1242:2: ( ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )* )
            // InternalStreamingsparql.g:1243:3: ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )*
            {
            // InternalStreamingsparql.g:1243:3: ( (lv_subject_0_0= ruleGraphNode ) )
            // InternalStreamingsparql.g:1244:4: (lv_subject_0_0= ruleGraphNode )
            {
            // InternalStreamingsparql.g:1244:4: (lv_subject_0_0= ruleGraphNode )
            // InternalStreamingsparql.g:1245:5: lv_subject_0_0= ruleGraphNode
            {

            					newCompositeNode(grammarAccess.getTriplesSameSubjectAccess().getSubjectGraphNodeParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_39);
            lv_subject_0_0=ruleGraphNode();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTriplesSameSubjectRule());
            					}
            					set(
            						current,
            						"subject",
            						lv_subject_0_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.GraphNode");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalStreamingsparql.g:1262:3: ( (lv_propertyList_1_0= rulePropertyList ) )
            // InternalStreamingsparql.g:1263:4: (lv_propertyList_1_0= rulePropertyList )
            {
            // InternalStreamingsparql.g:1263:4: (lv_propertyList_1_0= rulePropertyList )
            // InternalStreamingsparql.g:1264:5: lv_propertyList_1_0= rulePropertyList
            {

            					newCompositeNode(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_42);
            lv_propertyList_1_0=rulePropertyList();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTriplesSameSubjectRule());
            					}
            					add(
            						current,
            						"propertyList",
            						lv_propertyList_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.PropertyList");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalStreamingsparql.g:1281:3: (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==41) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // InternalStreamingsparql.g:1282:4: otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) )
            	    {
            	    otherlv_2=(Token)match(input,41,FOLLOW_39); 

            	    				newLeafNode(otherlv_2, grammarAccess.getTriplesSameSubjectAccess().getSemicolonKeyword_2_0());
            	    			
            	    // InternalStreamingsparql.g:1286:4: ( (lv_propertyList_3_0= rulePropertyList ) )
            	    // InternalStreamingsparql.g:1287:5: (lv_propertyList_3_0= rulePropertyList )
            	    {
            	    // InternalStreamingsparql.g:1287:5: (lv_propertyList_3_0= rulePropertyList )
            	    // InternalStreamingsparql.g:1288:6: lv_propertyList_3_0= rulePropertyList
            	    {

            	    						newCompositeNode(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_42);
            	    lv_propertyList_3_0=rulePropertyList();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getTriplesSameSubjectRule());
            	    						}
            	    						add(
            	    							current,
            	    							"propertyList",
            	    							lv_propertyList_3_0,
            	    							"de.uniol.inf.is.odysseus.server.Streamingsparql.PropertyList");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop23;
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
    // $ANTLR end "ruleTriplesSameSubject"


    // $ANTLR start "entryRulePropertyList"
    // InternalStreamingsparql.g:1310:1: entryRulePropertyList returns [EObject current=null] : iv_rulePropertyList= rulePropertyList EOF ;
    public final EObject entryRulePropertyList() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePropertyList = null;


        try {
            // InternalStreamingsparql.g:1310:53: (iv_rulePropertyList= rulePropertyList EOF )
            // InternalStreamingsparql.g:1311:2: iv_rulePropertyList= rulePropertyList EOF
            {
             newCompositeNode(grammarAccess.getPropertyListRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePropertyList=rulePropertyList();

            state._fsp--;

             current =iv_rulePropertyList; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePropertyList"


    // $ANTLR start "rulePropertyList"
    // InternalStreamingsparql.g:1317:1: rulePropertyList returns [EObject current=null] : ( ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) ) ) ;
    public final EObject rulePropertyList() throws RecognitionException {
        EObject current = null;

        EObject lv_property_0_0 = null;

        EObject lv_object_1_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:1323:2: ( ( ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) ) ) )
            // InternalStreamingsparql.g:1324:2: ( ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) ) )
            {
            // InternalStreamingsparql.g:1324:2: ( ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) ) )
            // InternalStreamingsparql.g:1325:3: ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) )
            {
            // InternalStreamingsparql.g:1325:3: ( (lv_property_0_0= ruleGraphNode ) )
            // InternalStreamingsparql.g:1326:4: (lv_property_0_0= ruleGraphNode )
            {
            // InternalStreamingsparql.g:1326:4: (lv_property_0_0= ruleGraphNode )
            // InternalStreamingsparql.g:1327:5: lv_property_0_0= ruleGraphNode
            {

            					newCompositeNode(grammarAccess.getPropertyListAccess().getPropertyGraphNodeParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_39);
            lv_property_0_0=ruleGraphNode();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getPropertyListRule());
            					}
            					set(
            						current,
            						"property",
            						lv_property_0_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.GraphNode");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalStreamingsparql.g:1344:3: ( (lv_object_1_0= ruleGraphNode ) )
            // InternalStreamingsparql.g:1345:4: (lv_object_1_0= ruleGraphNode )
            {
            // InternalStreamingsparql.g:1345:4: (lv_object_1_0= ruleGraphNode )
            // InternalStreamingsparql.g:1346:5: lv_object_1_0= ruleGraphNode
            {

            					newCompositeNode(grammarAccess.getPropertyListAccess().getObjectGraphNodeParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_object_1_0=ruleGraphNode();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getPropertyListRule());
            					}
            					set(
            						current,
            						"object",
            						lv_object_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.GraphNode");
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
    // $ANTLR end "rulePropertyList"


    // $ANTLR start "entryRuleGraphNode"
    // InternalStreamingsparql.g:1367:1: entryRuleGraphNode returns [EObject current=null] : iv_ruleGraphNode= ruleGraphNode EOF ;
    public final EObject entryRuleGraphNode() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGraphNode = null;


        try {
            // InternalStreamingsparql.g:1367:50: (iv_ruleGraphNode= ruleGraphNode EOF )
            // InternalStreamingsparql.g:1368:2: iv_ruleGraphNode= ruleGraphNode EOF
            {
             newCompositeNode(grammarAccess.getGraphNodeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGraphNode=ruleGraphNode();

            state._fsp--;

             current =iv_ruleGraphNode; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGraphNode"


    // $ANTLR start "ruleGraphNode"
    // InternalStreamingsparql.g:1374:1: ruleGraphNode returns [EObject current=null] : ( ( (lv_variable_0_0= ruleVariable ) ) | ( (lv_literal_1_0= RULE_STRING ) ) | ( (lv_iri_2_0= ruleIRI ) ) ) ;
    public final EObject ruleGraphNode() throws RecognitionException {
        EObject current = null;

        Token lv_literal_1_0=null;
        EObject lv_variable_0_0 = null;

        EObject lv_iri_2_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:1380:2: ( ( ( (lv_variable_0_0= ruleVariable ) ) | ( (lv_literal_1_0= RULE_STRING ) ) | ( (lv_iri_2_0= ruleIRI ) ) ) )
            // InternalStreamingsparql.g:1381:2: ( ( (lv_variable_0_0= ruleVariable ) ) | ( (lv_literal_1_0= RULE_STRING ) ) | ( (lv_iri_2_0= ruleIRI ) ) )
            {
            // InternalStreamingsparql.g:1381:2: ( ( (lv_variable_0_0= ruleVariable ) ) | ( (lv_literal_1_0= RULE_STRING ) ) | ( (lv_iri_2_0= ruleIRI ) ) )
            int alt24=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
            case 42:
                {
                alt24=1;
                }
                break;
            case RULE_STRING:
                {
                alt24=2;
                }
                break;
            case RULE_IRI_TERMINAL:
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
                    // InternalStreamingsparql.g:1382:3: ( (lv_variable_0_0= ruleVariable ) )
                    {
                    // InternalStreamingsparql.g:1382:3: ( (lv_variable_0_0= ruleVariable ) )
                    // InternalStreamingsparql.g:1383:4: (lv_variable_0_0= ruleVariable )
                    {
                    // InternalStreamingsparql.g:1383:4: (lv_variable_0_0= ruleVariable )
                    // InternalStreamingsparql.g:1384:5: lv_variable_0_0= ruleVariable
                    {

                    					newCompositeNode(grammarAccess.getGraphNodeAccess().getVariableVariableParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_variable_0_0=ruleVariable();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getGraphNodeRule());
                    					}
                    					set(
                    						current,
                    						"variable",
                    						lv_variable_0_0,
                    						"de.uniol.inf.is.odysseus.server.Streamingsparql.Variable");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:1402:3: ( (lv_literal_1_0= RULE_STRING ) )
                    {
                    // InternalStreamingsparql.g:1402:3: ( (lv_literal_1_0= RULE_STRING ) )
                    // InternalStreamingsparql.g:1403:4: (lv_literal_1_0= RULE_STRING )
                    {
                    // InternalStreamingsparql.g:1403:4: (lv_literal_1_0= RULE_STRING )
                    // InternalStreamingsparql.g:1404:5: lv_literal_1_0= RULE_STRING
                    {
                    lv_literal_1_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    					newLeafNode(lv_literal_1_0, grammarAccess.getGraphNodeAccess().getLiteralSTRINGTerminalRuleCall_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getGraphNodeRule());
                    					}
                    					setWithLastConsumed(
                    						current,
                    						"literal",
                    						lv_literal_1_0,
                    						"de.uniol.inf.is.odysseus.server.Streamingsparql.STRING");
                    				

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalStreamingsparql.g:1421:3: ( (lv_iri_2_0= ruleIRI ) )
                    {
                    // InternalStreamingsparql.g:1421:3: ( (lv_iri_2_0= ruleIRI ) )
                    // InternalStreamingsparql.g:1422:4: (lv_iri_2_0= ruleIRI )
                    {
                    // InternalStreamingsparql.g:1422:4: (lv_iri_2_0= ruleIRI )
                    // InternalStreamingsparql.g:1423:5: lv_iri_2_0= ruleIRI
                    {

                    					newCompositeNode(grammarAccess.getGraphNodeAccess().getIriIRIParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_iri_2_0=ruleIRI();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getGraphNodeRule());
                    					}
                    					set(
                    						current,
                    						"iri",
                    						lv_iri_2_0,
                    						"de.uniol.inf.is.odysseus.server.Streamingsparql.IRI");
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
    // $ANTLR end "ruleGraphNode"


    // $ANTLR start "entryRuleVariable"
    // InternalStreamingsparql.g:1444:1: entryRuleVariable returns [EObject current=null] : iv_ruleVariable= ruleVariable EOF ;
    public final EObject entryRuleVariable() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleVariable = null;


        try {
            // InternalStreamingsparql.g:1444:49: (iv_ruleVariable= ruleVariable EOF )
            // InternalStreamingsparql.g:1445:2: iv_ruleVariable= ruleVariable EOF
            {
             newCompositeNode(grammarAccess.getVariableRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleVariable=ruleVariable();

            state._fsp--;

             current =iv_ruleVariable; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleVariable"


    // $ANTLR start "ruleVariable"
    // InternalStreamingsparql.g:1451:1: ruleVariable returns [EObject current=null] : ( ( (lv_unnamed_0_0= ruleUnNamedVariable ) ) | ( (lv_property_1_0= ruleProperty ) ) ) ;
    public final EObject ruleVariable() throws RecognitionException {
        EObject current = null;

        EObject lv_unnamed_0_0 = null;

        EObject lv_property_1_0 = null;



        	enterRule();

        try {
            // InternalStreamingsparql.g:1457:2: ( ( ( (lv_unnamed_0_0= ruleUnNamedVariable ) ) | ( (lv_property_1_0= ruleProperty ) ) ) )
            // InternalStreamingsparql.g:1458:2: ( ( (lv_unnamed_0_0= ruleUnNamedVariable ) ) | ( (lv_property_1_0= ruleProperty ) ) )
            {
            // InternalStreamingsparql.g:1458:2: ( ( (lv_unnamed_0_0= ruleUnNamedVariable ) ) | ( (lv_property_1_0= ruleProperty ) ) )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==42) ) {
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
                    // InternalStreamingsparql.g:1459:3: ( (lv_unnamed_0_0= ruleUnNamedVariable ) )
                    {
                    // InternalStreamingsparql.g:1459:3: ( (lv_unnamed_0_0= ruleUnNamedVariable ) )
                    // InternalStreamingsparql.g:1460:4: (lv_unnamed_0_0= ruleUnNamedVariable )
                    {
                    // InternalStreamingsparql.g:1460:4: (lv_unnamed_0_0= ruleUnNamedVariable )
                    // InternalStreamingsparql.g:1461:5: lv_unnamed_0_0= ruleUnNamedVariable
                    {

                    					newCompositeNode(grammarAccess.getVariableAccess().getUnnamedUnNamedVariableParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_unnamed_0_0=ruleUnNamedVariable();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getVariableRule());
                    					}
                    					set(
                    						current,
                    						"unnamed",
                    						lv_unnamed_0_0,
                    						"de.uniol.inf.is.odysseus.server.Streamingsparql.UnNamedVariable");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:1479:3: ( (lv_property_1_0= ruleProperty ) )
                    {
                    // InternalStreamingsparql.g:1479:3: ( (lv_property_1_0= ruleProperty ) )
                    // InternalStreamingsparql.g:1480:4: (lv_property_1_0= ruleProperty )
                    {
                    // InternalStreamingsparql.g:1480:4: (lv_property_1_0= ruleProperty )
                    // InternalStreamingsparql.g:1481:5: lv_property_1_0= ruleProperty
                    {

                    					newCompositeNode(grammarAccess.getVariableAccess().getPropertyPropertyParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_property_1_0=ruleProperty();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getVariableRule());
                    					}
                    					set(
                    						current,
                    						"property",
                    						lv_property_1_0,
                    						"de.uniol.inf.is.odysseus.server.Streamingsparql.Property");
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
    // $ANTLR end "ruleVariable"


    // $ANTLR start "entryRuleUnNamedVariable"
    // InternalStreamingsparql.g:1502:1: entryRuleUnNamedVariable returns [EObject current=null] : iv_ruleUnNamedVariable= ruleUnNamedVariable EOF ;
    public final EObject entryRuleUnNamedVariable() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUnNamedVariable = null;


        try {
            // InternalStreamingsparql.g:1502:56: (iv_ruleUnNamedVariable= ruleUnNamedVariable EOF )
            // InternalStreamingsparql.g:1503:2: iv_ruleUnNamedVariable= ruleUnNamedVariable EOF
            {
             newCompositeNode(grammarAccess.getUnNamedVariableRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleUnNamedVariable=ruleUnNamedVariable();

            state._fsp--;

             current =iv_ruleUnNamedVariable; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleUnNamedVariable"


    // $ANTLR start "ruleUnNamedVariable"
    // InternalStreamingsparql.g:1509:1: ruleUnNamedVariable returns [EObject current=null] : (otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleUnNamedVariable() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;


        	enterRule();

        try {
            // InternalStreamingsparql.g:1515:2: ( (otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) ) ) )
            // InternalStreamingsparql.g:1516:2: (otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // InternalStreamingsparql.g:1516:2: (otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) ) )
            // InternalStreamingsparql.g:1517:3: otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,42,FOLLOW_3); 

            			newLeafNode(otherlv_0, grammarAccess.getUnNamedVariableAccess().getQuestionMarkKeyword_0());
            		
            // InternalStreamingsparql.g:1521:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalStreamingsparql.g:1522:4: (lv_name_1_0= RULE_ID )
            {
            // InternalStreamingsparql.g:1522:4: (lv_name_1_0= RULE_ID )
            // InternalStreamingsparql.g:1523:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_name_1_0, grammarAccess.getUnNamedVariableAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getUnNamedVariableRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

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
    // $ANTLR end "ruleUnNamedVariable"


    // $ANTLR start "entryRuleProperty"
    // InternalStreamingsparql.g:1543:1: entryRuleProperty returns [EObject current=null] : iv_ruleProperty= ruleProperty EOF ;
    public final EObject entryRuleProperty() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProperty = null;


        try {
            // InternalStreamingsparql.g:1543:49: (iv_ruleProperty= ruleProperty EOF )
            // InternalStreamingsparql.g:1544:2: iv_ruleProperty= ruleProperty EOF
            {
             newCompositeNode(grammarAccess.getPropertyRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleProperty=ruleProperty();

            state._fsp--;

             current =iv_ruleProperty; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleProperty"


    // $ANTLR start "ruleProperty"
    // InternalStreamingsparql.g:1550:1: ruleProperty returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) ) ) ;
    public final EObject ruleProperty() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;


        	enterRule();

        try {
            // InternalStreamingsparql.g:1556:2: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) ) ) )
            // InternalStreamingsparql.g:1557:2: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) ) )
            {
            // InternalStreamingsparql.g:1557:2: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) ) )
            // InternalStreamingsparql.g:1558:3: ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) )
            {
            // InternalStreamingsparql.g:1558:3: ( (otherlv_0= RULE_ID ) )
            // InternalStreamingsparql.g:1559:4: (otherlv_0= RULE_ID )
            {
            // InternalStreamingsparql.g:1559:4: (otherlv_0= RULE_ID )
            // InternalStreamingsparql.g:1560:5: otherlv_0= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getPropertyRule());
            					}
            				
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_4); 

            					newLeafNode(otherlv_0, grammarAccess.getPropertyAccess().getPrefixPrefixCrossReference_0_0());
            				

            }


            }

            otherlv_1=(Token)match(input,16,FOLLOW_3); 

            			newLeafNode(otherlv_1, grammarAccess.getPropertyAccess().getColonKeyword_1());
            		
            // InternalStreamingsparql.g:1575:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalStreamingsparql.g:1576:4: (lv_name_2_0= RULE_ID )
            {
            // InternalStreamingsparql.g:1576:4: (lv_name_2_0= RULE_ID )
            // InternalStreamingsparql.g:1577:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_name_2_0, grammarAccess.getPropertyAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getPropertyRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

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
    // $ANTLR end "ruleProperty"


    // $ANTLR start "entryRuleIRI"
    // InternalStreamingsparql.g:1597:1: entryRuleIRI returns [EObject current=null] : iv_ruleIRI= ruleIRI EOF ;
    public final EObject entryRuleIRI() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIRI = null;


        try {
            // InternalStreamingsparql.g:1597:44: (iv_ruleIRI= ruleIRI EOF )
            // InternalStreamingsparql.g:1598:2: iv_ruleIRI= ruleIRI EOF
            {
             newCompositeNode(grammarAccess.getIRIRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleIRI=ruleIRI();

            state._fsp--;

             current =iv_ruleIRI; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleIRI"


    // $ANTLR start "ruleIRI"
    // InternalStreamingsparql.g:1604:1: ruleIRI returns [EObject current=null] : ( () ( (lv_value_1_0= RULE_IRI_TERMINAL ) ) ) ;
    public final EObject ruleIRI() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;


        	enterRule();

        try {
            // InternalStreamingsparql.g:1610:2: ( ( () ( (lv_value_1_0= RULE_IRI_TERMINAL ) ) ) )
            // InternalStreamingsparql.g:1611:2: ( () ( (lv_value_1_0= RULE_IRI_TERMINAL ) ) )
            {
            // InternalStreamingsparql.g:1611:2: ( () ( (lv_value_1_0= RULE_IRI_TERMINAL ) ) )
            // InternalStreamingsparql.g:1612:3: () ( (lv_value_1_0= RULE_IRI_TERMINAL ) )
            {
            // InternalStreamingsparql.g:1612:3: ()
            // InternalStreamingsparql.g:1613:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getIRIAccess().getIRIAction_0(),
            					current);
            			

            }

            // InternalStreamingsparql.g:1619:3: ( (lv_value_1_0= RULE_IRI_TERMINAL ) )
            // InternalStreamingsparql.g:1620:4: (lv_value_1_0= RULE_IRI_TERMINAL )
            {
            // InternalStreamingsparql.g:1620:4: (lv_value_1_0= RULE_IRI_TERMINAL )
            // InternalStreamingsparql.g:1621:5: lv_value_1_0= RULE_IRI_TERMINAL
            {
            lv_value_1_0=(Token)match(input,RULE_IRI_TERMINAL,FOLLOW_2); 

            					newLeafNode(lv_value_1_0, grammarAccess.getIRIAccess().getValueIRI_TERMINALTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getIRIRule());
            					}
            					setWithLastConsumed(
            						current,
            						"value",
            						lv_value_1_0,
            						"de.uniol.inf.is.odysseus.server.Streamingsparql.IRI_TERMINAL");
            				

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
    // $ANTLR end "ruleIRI"


    // $ANTLR start "ruleOperator"
    // InternalStreamingsparql.g:1641:1: ruleOperator returns [Enumerator current=null] : ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= '+' ) | (enumLiteral_7= '/' ) | (enumLiteral_8= '-' ) | (enumLiteral_9= '*' ) ) ;
    public final Enumerator ruleOperator() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;
        Token enumLiteral_5=null;
        Token enumLiteral_6=null;
        Token enumLiteral_7=null;
        Token enumLiteral_8=null;
        Token enumLiteral_9=null;


        	enterRule();

        try {
            // InternalStreamingsparql.g:1647:2: ( ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= '+' ) | (enumLiteral_7= '/' ) | (enumLiteral_8= '-' ) | (enumLiteral_9= '*' ) ) )
            // InternalStreamingsparql.g:1648:2: ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= '+' ) | (enumLiteral_7= '/' ) | (enumLiteral_8= '-' ) | (enumLiteral_9= '*' ) )
            {
            // InternalStreamingsparql.g:1648:2: ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= '+' ) | (enumLiteral_7= '/' ) | (enumLiteral_8= '-' ) | (enumLiteral_9= '*' ) )
            int alt26=10;
            switch ( input.LA(1) ) {
            case 43:
                {
                alt26=1;
                }
                break;
            case 44:
                {
                alt26=2;
                }
                break;
            case 45:
                {
                alt26=3;
                }
                break;
            case 46:
                {
                alt26=4;
                }
                break;
            case 23:
                {
                alt26=5;
                }
                break;
            case 47:
                {
                alt26=6;
                }
                break;
            case 48:
                {
                alt26=7;
                }
                break;
            case 49:
                {
                alt26=8;
                }
                break;
            case 50:
                {
                alt26=9;
                }
                break;
            case 51:
                {
                alt26=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }

            switch (alt26) {
                case 1 :
                    // InternalStreamingsparql.g:1649:3: (enumLiteral_0= '<' )
                    {
                    // InternalStreamingsparql.g:1649:3: (enumLiteral_0= '<' )
                    // InternalStreamingsparql.g:1650:4: enumLiteral_0= '<'
                    {
                    enumLiteral_0=(Token)match(input,43,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:1657:3: (enumLiteral_1= '>' )
                    {
                    // InternalStreamingsparql.g:1657:3: (enumLiteral_1= '>' )
                    // InternalStreamingsparql.g:1658:4: enumLiteral_1= '>'
                    {
                    enumLiteral_1=(Token)match(input,44,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalStreamingsparql.g:1665:3: (enumLiteral_2= '<=' )
                    {
                    // InternalStreamingsparql.g:1665:3: (enumLiteral_2= '<=' )
                    // InternalStreamingsparql.g:1666:4: enumLiteral_2= '<='
                    {
                    enumLiteral_2=(Token)match(input,45,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalStreamingsparql.g:1673:3: (enumLiteral_3= '>=' )
                    {
                    // InternalStreamingsparql.g:1673:3: (enumLiteral_3= '>=' )
                    // InternalStreamingsparql.g:1674:4: enumLiteral_3= '>='
                    {
                    enumLiteral_3=(Token)match(input,46,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalStreamingsparql.g:1681:3: (enumLiteral_4= '=' )
                    {
                    // InternalStreamingsparql.g:1681:3: (enumLiteral_4= '=' )
                    // InternalStreamingsparql.g:1682:4: enumLiteral_4= '='
                    {
                    enumLiteral_4=(Token)match(input,23,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;
                case 6 :
                    // InternalStreamingsparql.g:1689:3: (enumLiteral_5= '!=' )
                    {
                    // InternalStreamingsparql.g:1689:3: (enumLiteral_5= '!=' )
                    // InternalStreamingsparql.g:1690:4: enumLiteral_5= '!='
                    {
                    enumLiteral_5=(Token)match(input,47,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_5, grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5());
                    			

                    }


                    }
                    break;
                case 7 :
                    // InternalStreamingsparql.g:1697:3: (enumLiteral_6= '+' )
                    {
                    // InternalStreamingsparql.g:1697:3: (enumLiteral_6= '+' )
                    // InternalStreamingsparql.g:1698:4: enumLiteral_6= '+'
                    {
                    enumLiteral_6=(Token)match(input,48,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getSumEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_6, grammarAccess.getOperatorAccess().getSumEnumLiteralDeclaration_6());
                    			

                    }


                    }
                    break;
                case 8 :
                    // InternalStreamingsparql.g:1705:3: (enumLiteral_7= '/' )
                    {
                    // InternalStreamingsparql.g:1705:3: (enumLiteral_7= '/' )
                    // InternalStreamingsparql.g:1706:4: enumLiteral_7= '/'
                    {
                    enumLiteral_7=(Token)match(input,49,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getDivEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_7, grammarAccess.getOperatorAccess().getDivEnumLiteralDeclaration_7());
                    			

                    }


                    }
                    break;
                case 9 :
                    // InternalStreamingsparql.g:1713:3: (enumLiteral_8= '-' )
                    {
                    // InternalStreamingsparql.g:1713:3: (enumLiteral_8= '-' )
                    // InternalStreamingsparql.g:1714:4: enumLiteral_8= '-'
                    {
                    enumLiteral_8=(Token)match(input,50,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getSubEnumLiteralDeclaration_8().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_8, grammarAccess.getOperatorAccess().getSubEnumLiteralDeclaration_8());
                    			

                    }


                    }
                    break;
                case 10 :
                    // InternalStreamingsparql.g:1721:3: (enumLiteral_9= '*' )
                    {
                    // InternalStreamingsparql.g:1721:3: (enumLiteral_9= '*' )
                    // InternalStreamingsparql.g:1722:4: enumLiteral_9= '*'
                    {
                    enumLiteral_9=(Token)match(input,51,FOLLOW_2); 

                    				current = grammarAccess.getOperatorAccess().getMultiplicityEnumLiteralDeclaration_9().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_9, grammarAccess.getOperatorAccess().getMultiplicityEnumLiteralDeclaration_9());
                    			

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
    // $ANTLR end "ruleOperator"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000080128000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000080108000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000080100000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000040000000010L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000042000000010L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000060200002L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000020200002L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x000000001C400000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x000000001C000000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000014000000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x000FF80000800000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000001802000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000001002000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000008000000010L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x00000400000000B0L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000018000000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000020000000002L});

}