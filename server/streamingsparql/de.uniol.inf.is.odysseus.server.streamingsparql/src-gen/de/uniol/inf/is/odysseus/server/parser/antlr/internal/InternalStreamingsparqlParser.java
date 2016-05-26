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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_IRI_TERMINAL", "RULE_AGG_FUNCTION", "RULE_STRING", "RULE_WINDOWTYPE", "RULE_INT", "RULE_UNITTYPE", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'PREFIX'", "':'", "'BASE'", "'#ADDQUERY'", "'#RUNQUERY'", "'SELECT'", "'DISTINCT'", "'REDUCED'", "'AGGREGATE('", "'aggregations = ['", "']'", "','", "')'", "'group_by=['", "'['", "'CSVFILESINK('", "'FILTER('", "'FROM'", "'AS'", "'[TYPE '", "'SIZE '", "'ADVANCE'", "'UNIT '", "'WHERE'", "'{'", "'}'", "'.'", "';'", "'?'", "'<'", "'>'", "'<='", "'>='", "'='", "'!='", "'+'", "'/'", "'-'", "'*'"
    };
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
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
    public String getGrammarFileName() { return "../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g"; }



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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:68:1: entryRuleSPARQLQuery returns [EObject current=null] : iv_ruleSPARQLQuery= ruleSPARQLQuery EOF ;
    public final EObject entryRuleSPARQLQuery() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSPARQLQuery = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:69:2: (iv_ruleSPARQLQuery= ruleSPARQLQuery EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:70:2: iv_ruleSPARQLQuery= ruleSPARQLQuery EOF
            {
             newCompositeNode(grammarAccess.getSPARQLQueryRule()); 
            pushFollow(FOLLOW_ruleSPARQLQuery_in_entryRuleSPARQLQuery75);
            iv_ruleSPARQLQuery=ruleSPARQLQuery();

            state._fsp--;

             current =iv_ruleSPARQLQuery; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSPARQLQuery85); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:77:1: ruleSPARQLQuery returns [EObject current=null] : this_SelectQuery_0= ruleSelectQuery ;
    public final EObject ruleSPARQLQuery() throws RecognitionException {
        EObject current = null;

        EObject this_SelectQuery_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:80:28: (this_SelectQuery_0= ruleSelectQuery )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:82:5: this_SelectQuery_0= ruleSelectQuery
            {
             
                    newCompositeNode(grammarAccess.getSPARQLQueryAccess().getSelectQueryParserRuleCall()); 
                
            pushFollow(FOLLOW_ruleSelectQuery_in_ruleSPARQLQuery131);
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:98:1: entryRulePrefix returns [EObject current=null] : iv_rulePrefix= rulePrefix EOF ;
    public final EObject entryRulePrefix() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrefix = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:99:2: (iv_rulePrefix= rulePrefix EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:100:2: iv_rulePrefix= rulePrefix EOF
            {
             newCompositeNode(grammarAccess.getPrefixRule()); 
            pushFollow(FOLLOW_rulePrefix_in_entryRulePrefix165);
            iv_rulePrefix=rulePrefix();

            state._fsp--;

             current =iv_rulePrefix; 
            match(input,EOF,FOLLOW_EOF_in_entryRulePrefix175); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:107:1: rulePrefix returns [EObject current=null] : ( (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) ) | this_UnNamedPrefix_4= ruleUnNamedPrefix ) ;
    public final EObject rulePrefix() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token lv_iref_3_0=null;
        EObject this_UnNamedPrefix_4 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:110:28: ( ( (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) ) | this_UnNamedPrefix_4= ruleUnNamedPrefix ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:111:1: ( (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) ) | this_UnNamedPrefix_4= ruleUnNamedPrefix )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:111:1: ( (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) ) | this_UnNamedPrefix_4= ruleUnNamedPrefix )
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
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:111:2: (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:111:2: (otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:111:4: otherlv_0= 'PREFIX' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_iref_3_0= RULE_IRI_TERMINAL ) )
                    {
                    otherlv_0=(Token)match(input,15,FOLLOW_15_in_rulePrefix213); 

                        	newLeafNode(otherlv_0, grammarAccess.getPrefixAccess().getPREFIXKeyword_0_0());
                        
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:115:1: ( (lv_name_1_0= RULE_ID ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:116:1: (lv_name_1_0= RULE_ID )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:116:1: (lv_name_1_0= RULE_ID )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:117:3: lv_name_1_0= RULE_ID
                    {
                    lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_rulePrefix230); 

                    			newLeafNode(lv_name_1_0, grammarAccess.getPrefixAccess().getNameIDTerminalRuleCall_0_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getPrefixRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"name",
                            		lv_name_1_0, 
                            		"ID");
                    	    

                    }


                    }

                    otherlv_2=(Token)match(input,16,FOLLOW_16_in_rulePrefix247); 

                        	newLeafNode(otherlv_2, grammarAccess.getPrefixAccess().getColonKeyword_0_2());
                        
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:137:1: ( (lv_iref_3_0= RULE_IRI_TERMINAL ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:138:1: (lv_iref_3_0= RULE_IRI_TERMINAL )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:138:1: (lv_iref_3_0= RULE_IRI_TERMINAL )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:139:3: lv_iref_3_0= RULE_IRI_TERMINAL
                    {
                    lv_iref_3_0=(Token)match(input,RULE_IRI_TERMINAL,FOLLOW_RULE_IRI_TERMINAL_in_rulePrefix264); 

                    			newLeafNode(lv_iref_3_0, grammarAccess.getPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_0_3_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getPrefixRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"iref",
                            		lv_iref_3_0, 
                            		"IRI_TERMINAL");
                    	    

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:157:5: this_UnNamedPrefix_4= ruleUnNamedPrefix
                    {
                     
                            newCompositeNode(grammarAccess.getPrefixAccess().getUnNamedPrefixParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleUnNamedPrefix_in_rulePrefix298);
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:173:1: entryRuleUnNamedPrefix returns [EObject current=null] : iv_ruleUnNamedPrefix= ruleUnNamedPrefix EOF ;
    public final EObject entryRuleUnNamedPrefix() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUnNamedPrefix = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:174:2: (iv_ruleUnNamedPrefix= ruleUnNamedPrefix EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:175:2: iv_ruleUnNamedPrefix= ruleUnNamedPrefix EOF
            {
             newCompositeNode(grammarAccess.getUnNamedPrefixRule()); 
            pushFollow(FOLLOW_ruleUnNamedPrefix_in_entryRuleUnNamedPrefix333);
            iv_ruleUnNamedPrefix=ruleUnNamedPrefix();

            state._fsp--;

             current =iv_ruleUnNamedPrefix; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleUnNamedPrefix343); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:182:1: ruleUnNamedPrefix returns [EObject current=null] : (otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) ) ) ;
    public final EObject ruleUnNamedPrefix() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_iref_2_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:185:28: ( (otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:186:1: (otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:186:1: (otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:186:3: otherlv_0= 'PREFIX' otherlv_1= ':' ( (lv_iref_2_0= RULE_IRI_TERMINAL ) )
            {
            otherlv_0=(Token)match(input,15,FOLLOW_15_in_ruleUnNamedPrefix380); 

                	newLeafNode(otherlv_0, grammarAccess.getUnNamedPrefixAccess().getPREFIXKeyword_0());
                
            otherlv_1=(Token)match(input,16,FOLLOW_16_in_ruleUnNamedPrefix392); 

                	newLeafNode(otherlv_1, grammarAccess.getUnNamedPrefixAccess().getColonKeyword_1());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:194:1: ( (lv_iref_2_0= RULE_IRI_TERMINAL ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:195:1: (lv_iref_2_0= RULE_IRI_TERMINAL )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:195:1: (lv_iref_2_0= RULE_IRI_TERMINAL )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:196:3: lv_iref_2_0= RULE_IRI_TERMINAL
            {
            lv_iref_2_0=(Token)match(input,RULE_IRI_TERMINAL,FOLLOW_RULE_IRI_TERMINAL_in_ruleUnNamedPrefix409); 

            			newLeafNode(lv_iref_2_0, grammarAccess.getUnNamedPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getUnNamedPrefixRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"iref",
                    		lv_iref_2_0, 
                    		"IRI_TERMINAL");
            	    

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:220:1: entryRuleBase returns [EObject current=null] : iv_ruleBase= ruleBase EOF ;
    public final EObject entryRuleBase() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBase = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:221:2: (iv_ruleBase= ruleBase EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:222:2: iv_ruleBase= ruleBase EOF
            {
             newCompositeNode(grammarAccess.getBaseRule()); 
            pushFollow(FOLLOW_ruleBase_in_entryRuleBase450);
            iv_ruleBase=ruleBase();

            state._fsp--;

             current =iv_ruleBase; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBase460); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:229:1: ruleBase returns [EObject current=null] : (otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) ) ) ;
    public final EObject ruleBase() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_iref_1_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:232:28: ( (otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:233:1: (otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:233:1: (otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:233:3: otherlv_0= 'BASE' ( (lv_iref_1_0= ruleIRI ) )
            {
            otherlv_0=(Token)match(input,17,FOLLOW_17_in_ruleBase497); 

                	newLeafNode(otherlv_0, grammarAccess.getBaseAccess().getBASEKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:237:1: ( (lv_iref_1_0= ruleIRI ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:238:1: (lv_iref_1_0= ruleIRI )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:238:1: (lv_iref_1_0= ruleIRI )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:239:3: lv_iref_1_0= ruleIRI
            {
             
            	        newCompositeNode(grammarAccess.getBaseAccess().getIrefIRIParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleIRI_in_ruleBase518);
            lv_iref_1_0=ruleIRI();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getBaseRule());
            	        }
                   		set(
                   			current, 
                   			"iref",
                    		lv_iref_1_0, 
                    		"IRI");
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:263:1: entryRuleSelectQuery returns [EObject current=null] : iv_ruleSelectQuery= ruleSelectQuery EOF ;
    public final EObject entryRuleSelectQuery() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSelectQuery = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:264:2: (iv_ruleSelectQuery= ruleSelectQuery EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:265:2: iv_ruleSelectQuery= ruleSelectQuery EOF
            {
             newCompositeNode(grammarAccess.getSelectQueryRule()); 
            pushFollow(FOLLOW_ruleSelectQuery_in_entryRuleSelectQuery554);
            iv_ruleSelectQuery=ruleSelectQuery();

            state._fsp--;

             current =iv_ruleSelectQuery; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSelectQuery564); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:272:1: ruleSelectQuery returns [EObject current=null] : ( ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( ( (lv_isDistinct_6_0= 'DISTINCT' ) ) | ( (lv_isReduced_7_0= 'REDUCED' ) ) )? ( (lv_variables_8_0= ruleVariable ) ) ( (lv_variables_9_0= ruleVariable ) )* ( (lv_whereClause_10_0= ruleWhereClause ) ) ( (lv_filterclause_11_0= ruleFilterclause ) )? ( (lv_aggregateClause_12_0= ruleAggregate ) )? ( (lv_filesinkclause_13_0= ruleFilesinkclause ) )? ) ;
    public final EObject ruleSelectQuery() throws RecognitionException {
        EObject current = null;

        Token lv_method_0_0=null;
        Token otherlv_1=null;
        Token otherlv_5=null;
        Token lv_isDistinct_6_0=null;
        Token lv_isReduced_7_0=null;
        EObject lv_base_2_0 = null;

        EObject lv_prefixes_3_0 = null;

        EObject lv_datasetClauses_4_0 = null;

        EObject lv_variables_8_0 = null;

        EObject lv_variables_9_0 = null;

        EObject lv_whereClause_10_0 = null;

        EObject lv_filterclause_11_0 = null;

        EObject lv_aggregateClause_12_0 = null;

        EObject lv_filesinkclause_13_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:275:28: ( ( ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( ( (lv_isDistinct_6_0= 'DISTINCT' ) ) | ( (lv_isReduced_7_0= 'REDUCED' ) ) )? ( (lv_variables_8_0= ruleVariable ) ) ( (lv_variables_9_0= ruleVariable ) )* ( (lv_whereClause_10_0= ruleWhereClause ) ) ( (lv_filterclause_11_0= ruleFilterclause ) )? ( (lv_aggregateClause_12_0= ruleAggregate ) )? ( (lv_filesinkclause_13_0= ruleFilesinkclause ) )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:276:1: ( ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( ( (lv_isDistinct_6_0= 'DISTINCT' ) ) | ( (lv_isReduced_7_0= 'REDUCED' ) ) )? ( (lv_variables_8_0= ruleVariable ) ) ( (lv_variables_9_0= ruleVariable ) )* ( (lv_whereClause_10_0= ruleWhereClause ) ) ( (lv_filterclause_11_0= ruleFilterclause ) )? ( (lv_aggregateClause_12_0= ruleAggregate ) )? ( (lv_filesinkclause_13_0= ruleFilesinkclause ) )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:276:1: ( ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( ( (lv_isDistinct_6_0= 'DISTINCT' ) ) | ( (lv_isReduced_7_0= 'REDUCED' ) ) )? ( (lv_variables_8_0= ruleVariable ) ) ( (lv_variables_9_0= ruleVariable ) )* ( (lv_whereClause_10_0= ruleWhereClause ) ) ( (lv_filterclause_11_0= ruleFilterclause ) )? ( (lv_aggregateClause_12_0= ruleAggregate ) )? ( (lv_filesinkclause_13_0= ruleFilesinkclause ) )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:276:2: ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )? ( (lv_base_2_0= ruleBase ) )? ( (lv_prefixes_3_0= rulePrefix ) )* ( (lv_datasetClauses_4_0= ruleDatasetClause ) )* otherlv_5= 'SELECT' ( ( (lv_isDistinct_6_0= 'DISTINCT' ) ) | ( (lv_isReduced_7_0= 'REDUCED' ) ) )? ( (lv_variables_8_0= ruleVariable ) ) ( (lv_variables_9_0= ruleVariable ) )* ( (lv_whereClause_10_0= ruleWhereClause ) ) ( (lv_filterclause_11_0= ruleFilterclause ) )? ( (lv_aggregateClause_12_0= ruleAggregate ) )? ( (lv_filesinkclause_13_0= ruleFilesinkclause ) )?
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:276:2: ( ( (lv_method_0_0= '#ADDQUERY' ) ) | otherlv_1= '#RUNQUERY' )?
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
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:276:3: ( (lv_method_0_0= '#ADDQUERY' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:276:3: ( (lv_method_0_0= '#ADDQUERY' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:277:1: (lv_method_0_0= '#ADDQUERY' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:277:1: (lv_method_0_0= '#ADDQUERY' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:278:3: lv_method_0_0= '#ADDQUERY'
                    {
                    lv_method_0_0=(Token)match(input,18,FOLLOW_18_in_ruleSelectQuery608); 

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
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:292:7: otherlv_1= '#RUNQUERY'
                    {
                    otherlv_1=(Token)match(input,19,FOLLOW_19_in_ruleSelectQuery639); 

                        	newLeafNode(otherlv_1, grammarAccess.getSelectQueryAccess().getRUNQUERYKeyword_0_1());
                        

                    }
                    break;

            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:296:3: ( (lv_base_2_0= ruleBase ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==17) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:297:1: (lv_base_2_0= ruleBase )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:297:1: (lv_base_2_0= ruleBase )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:298:3: lv_base_2_0= ruleBase
                    {
                     
                    	        newCompositeNode(grammarAccess.getSelectQueryAccess().getBaseBaseParserRuleCall_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleBase_in_ruleSelectQuery662);
                    lv_base_2_0=ruleBase();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSelectQueryRule());
                    	        }
                           		set(
                           			current, 
                           			"base",
                            		lv_base_2_0, 
                            		"Base");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }
                    break;

            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:314:3: ( (lv_prefixes_3_0= rulePrefix ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==15) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:315:1: (lv_prefixes_3_0= rulePrefix )
            	    {
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:315:1: (lv_prefixes_3_0= rulePrefix )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:316:3: lv_prefixes_3_0= rulePrefix
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getSelectQueryAccess().getPrefixesPrefixParserRuleCall_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_rulePrefix_in_ruleSelectQuery684);
            	    lv_prefixes_3_0=rulePrefix();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"prefixes",
            	            		lv_prefixes_3_0, 
            	            		"Prefix");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:332:3: ( (lv_datasetClauses_4_0= ruleDatasetClause ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==32) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:333:1: (lv_datasetClauses_4_0= ruleDatasetClause )
            	    {
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:333:1: (lv_datasetClauses_4_0= ruleDatasetClause )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:334:3: lv_datasetClauses_4_0= ruleDatasetClause
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getSelectQueryAccess().getDatasetClausesDatasetClauseParserRuleCall_3_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleDatasetClause_in_ruleSelectQuery706);
            	    lv_datasetClauses_4_0=ruleDatasetClause();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"datasetClauses",
            	            		lv_datasetClauses_4_0, 
            	            		"DatasetClause");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            otherlv_5=(Token)match(input,20,FOLLOW_20_in_ruleSelectQuery719); 

                	newLeafNode(otherlv_5, grammarAccess.getSelectQueryAccess().getSELECTKeyword_4());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:354:1: ( ( (lv_isDistinct_6_0= 'DISTINCT' ) ) | ( (lv_isReduced_7_0= 'REDUCED' ) ) )?
            int alt6=3;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==21) ) {
                alt6=1;
            }
            else if ( (LA6_0==22) ) {
                alt6=2;
            }
            switch (alt6) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:354:2: ( (lv_isDistinct_6_0= 'DISTINCT' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:354:2: ( (lv_isDistinct_6_0= 'DISTINCT' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:355:1: (lv_isDistinct_6_0= 'DISTINCT' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:355:1: (lv_isDistinct_6_0= 'DISTINCT' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:356:3: lv_isDistinct_6_0= 'DISTINCT'
                    {
                    lv_isDistinct_6_0=(Token)match(input,21,FOLLOW_21_in_ruleSelectQuery738); 

                            newLeafNode(lv_isDistinct_6_0, grammarAccess.getSelectQueryAccess().getIsDistinctDISTINCTKeyword_5_0_0());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getSelectQueryRule());
                    	        }
                           		setWithLastConsumed(current, "isDistinct", true, "DISTINCT");
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:370:6: ( (lv_isReduced_7_0= 'REDUCED' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:370:6: ( (lv_isReduced_7_0= 'REDUCED' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:371:1: (lv_isReduced_7_0= 'REDUCED' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:371:1: (lv_isReduced_7_0= 'REDUCED' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:372:3: lv_isReduced_7_0= 'REDUCED'
                    {
                    lv_isReduced_7_0=(Token)match(input,22,FOLLOW_22_in_ruleSelectQuery775); 

                            newLeafNode(lv_isReduced_7_0, grammarAccess.getSelectQueryAccess().getIsReducedREDUCEDKeyword_5_1_0());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getSelectQueryRule());
                    	        }
                           		setWithLastConsumed(current, "isReduced", true, "REDUCED");
                    	    

                    }


                    }


                    }
                    break;

            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:385:4: ( (lv_variables_8_0= ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:386:1: (lv_variables_8_0= ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:386:1: (lv_variables_8_0= ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:387:3: lv_variables_8_0= ruleVariable
            {
             
            	        newCompositeNode(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_6_0()); 
            	    
            pushFollow(FOLLOW_ruleVariable_in_ruleSelectQuery811);
            lv_variables_8_0=ruleVariable();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            	        }
                   		add(
                   			current, 
                   			"variables",
                    		lv_variables_8_0, 
                    		"Variable");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:403:2: ( (lv_variables_9_0= ruleVariable ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==RULE_ID||LA7_0==43) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:404:1: (lv_variables_9_0= ruleVariable )
            	    {
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:404:1: (lv_variables_9_0= ruleVariable )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:405:3: lv_variables_9_0= ruleVariable
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_7_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleVariable_in_ruleSelectQuery832);
            	    lv_variables_9_0=ruleVariable();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"variables",
            	            		lv_variables_9_0, 
            	            		"Variable");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:421:3: ( (lv_whereClause_10_0= ruleWhereClause ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:422:1: (lv_whereClause_10_0= ruleWhereClause )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:422:1: (lv_whereClause_10_0= ruleWhereClause )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:423:3: lv_whereClause_10_0= ruleWhereClause
            {
             
            	        newCompositeNode(grammarAccess.getSelectQueryAccess().getWhereClauseWhereClauseParserRuleCall_8_0()); 
            	    
            pushFollow(FOLLOW_ruleWhereClause_in_ruleSelectQuery854);
            lv_whereClause_10_0=ruleWhereClause();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSelectQueryRule());
            	        }
                   		set(
                   			current, 
                   			"whereClause",
                    		lv_whereClause_10_0, 
                    		"WhereClause");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:439:2: ( (lv_filterclause_11_0= ruleFilterclause ) )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==31) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:440:1: (lv_filterclause_11_0= ruleFilterclause )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:440:1: (lv_filterclause_11_0= ruleFilterclause )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:441:3: lv_filterclause_11_0= ruleFilterclause
                    {
                     
                    	        newCompositeNode(grammarAccess.getSelectQueryAccess().getFilterclauseFilterclauseParserRuleCall_9_0()); 
                    	    
                    pushFollow(FOLLOW_ruleFilterclause_in_ruleSelectQuery875);
                    lv_filterclause_11_0=ruleFilterclause();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSelectQueryRule());
                    	        }
                           		set(
                           			current, 
                           			"filterclause",
                            		lv_filterclause_11_0, 
                            		"Filterclause");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }
                    break;

            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:457:3: ( (lv_aggregateClause_12_0= ruleAggregate ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==23) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:458:1: (lv_aggregateClause_12_0= ruleAggregate )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:458:1: (lv_aggregateClause_12_0= ruleAggregate )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:459:3: lv_aggregateClause_12_0= ruleAggregate
                    {
                     
                    	        newCompositeNode(grammarAccess.getSelectQueryAccess().getAggregateClauseAggregateParserRuleCall_10_0()); 
                    	    
                    pushFollow(FOLLOW_ruleAggregate_in_ruleSelectQuery897);
                    lv_aggregateClause_12_0=ruleAggregate();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSelectQueryRule());
                    	        }
                           		set(
                           			current, 
                           			"aggregateClause",
                            		lv_aggregateClause_12_0, 
                            		"Aggregate");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }
                    break;

            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:475:3: ( (lv_filesinkclause_13_0= ruleFilesinkclause ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==30) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:476:1: (lv_filesinkclause_13_0= ruleFilesinkclause )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:476:1: (lv_filesinkclause_13_0= ruleFilesinkclause )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:477:3: lv_filesinkclause_13_0= ruleFilesinkclause
                    {
                     
                    	        newCompositeNode(grammarAccess.getSelectQueryAccess().getFilesinkclauseFilesinkclauseParserRuleCall_11_0()); 
                    	    
                    pushFollow(FOLLOW_ruleFilesinkclause_in_ruleSelectQuery919);
                    lv_filesinkclause_13_0=ruleFilesinkclause();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSelectQueryRule());
                    	        }
                           		set(
                           			current, 
                           			"filesinkclause",
                            		lv_filesinkclause_13_0, 
                            		"Filesinkclause");
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:501:1: entryRuleAggregate returns [EObject current=null] : iv_ruleAggregate= ruleAggregate EOF ;
    public final EObject entryRuleAggregate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregate = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:502:2: (iv_ruleAggregate= ruleAggregate EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:503:2: iv_ruleAggregate= ruleAggregate EOF
            {
             newCompositeNode(grammarAccess.getAggregateRule()); 
            pushFollow(FOLLOW_ruleAggregate_in_entryRuleAggregate956);
            iv_ruleAggregate=ruleAggregate();

            state._fsp--;

             current =iv_ruleAggregate; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAggregate966); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:510:1: ruleAggregate returns [EObject current=null] : (otherlv_0= 'AGGREGATE(' (otherlv_1= 'aggregations = [' ( (lv_aggregations_2_0= ruleAggregation ) )* otherlv_3= ']' )? ( (otherlv_4= ',' )? ( (lv_groupby_5_0= ruleGroupBy ) ) )? otherlv_6= ')' ) ;
    public final EObject ruleAggregate() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_aggregations_2_0 = null;

        EObject lv_groupby_5_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:513:28: ( (otherlv_0= 'AGGREGATE(' (otherlv_1= 'aggregations = [' ( (lv_aggregations_2_0= ruleAggregation ) )* otherlv_3= ']' )? ( (otherlv_4= ',' )? ( (lv_groupby_5_0= ruleGroupBy ) ) )? otherlv_6= ')' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:514:1: (otherlv_0= 'AGGREGATE(' (otherlv_1= 'aggregations = [' ( (lv_aggregations_2_0= ruleAggregation ) )* otherlv_3= ']' )? ( (otherlv_4= ',' )? ( (lv_groupby_5_0= ruleGroupBy ) ) )? otherlv_6= ')' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:514:1: (otherlv_0= 'AGGREGATE(' (otherlv_1= 'aggregations = [' ( (lv_aggregations_2_0= ruleAggregation ) )* otherlv_3= ']' )? ( (otherlv_4= ',' )? ( (lv_groupby_5_0= ruleGroupBy ) ) )? otherlv_6= ')' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:514:3: otherlv_0= 'AGGREGATE(' (otherlv_1= 'aggregations = [' ( (lv_aggregations_2_0= ruleAggregation ) )* otherlv_3= ']' )? ( (otherlv_4= ',' )? ( (lv_groupby_5_0= ruleGroupBy ) ) )? otherlv_6= ')'
            {
            otherlv_0=(Token)match(input,23,FOLLOW_23_in_ruleAggregate1003); 

                	newLeafNode(otherlv_0, grammarAccess.getAggregateAccess().getAGGREGATEKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:518:1: (otherlv_1= 'aggregations = [' ( (lv_aggregations_2_0= ruleAggregation ) )* otherlv_3= ']' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==24) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:518:3: otherlv_1= 'aggregations = [' ( (lv_aggregations_2_0= ruleAggregation ) )* otherlv_3= ']'
                    {
                    otherlv_1=(Token)match(input,24,FOLLOW_24_in_ruleAggregate1016); 

                        	newLeafNode(otherlv_1, grammarAccess.getAggregateAccess().getAggregationsKeyword_1_0());
                        
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:522:1: ( (lv_aggregations_2_0= ruleAggregation ) )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==29) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:523:1: (lv_aggregations_2_0= ruleAggregation )
                    	    {
                    	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:523:1: (lv_aggregations_2_0= ruleAggregation )
                    	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:524:3: lv_aggregations_2_0= ruleAggregation
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getAggregateAccess().getAggregationsAggregationParserRuleCall_1_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleAggregation_in_ruleAggregate1037);
                    	    lv_aggregations_2_0=ruleAggregation();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getAggregateRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"aggregations",
                    	            		lv_aggregations_2_0, 
                    	            		"Aggregation");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);

                    otherlv_3=(Token)match(input,25,FOLLOW_25_in_ruleAggregate1050); 

                        	newLeafNode(otherlv_3, grammarAccess.getAggregateAccess().getRightSquareBracketKeyword_1_2());
                        

                    }
                    break;

            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:544:3: ( (otherlv_4= ',' )? ( (lv_groupby_5_0= ruleGroupBy ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==26||LA14_0==28) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:544:4: (otherlv_4= ',' )? ( (lv_groupby_5_0= ruleGroupBy ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:544:4: (otherlv_4= ',' )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==26) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:544:6: otherlv_4= ','
                            {
                            otherlv_4=(Token)match(input,26,FOLLOW_26_in_ruleAggregate1066); 

                                	newLeafNode(otherlv_4, grammarAccess.getAggregateAccess().getCommaKeyword_2_0());
                                

                            }
                            break;

                    }

                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:548:3: ( (lv_groupby_5_0= ruleGroupBy ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:549:1: (lv_groupby_5_0= ruleGroupBy )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:549:1: (lv_groupby_5_0= ruleGroupBy )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:550:3: lv_groupby_5_0= ruleGroupBy
                    {
                     
                    	        newCompositeNode(grammarAccess.getAggregateAccess().getGroupbyGroupByParserRuleCall_2_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleGroupBy_in_ruleAggregate1089);
                    lv_groupby_5_0=ruleGroupBy();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getAggregateRule());
                    	        }
                           		set(
                           			current, 
                           			"groupby",
                            		lv_groupby_5_0, 
                            		"GroupBy");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,27,FOLLOW_27_in_ruleAggregate1103); 

                	newLeafNode(otherlv_6, grammarAccess.getAggregateAccess().getRightParenthesisKeyword_3());
                

            }


            }

             leaveRule(); 
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:578:1: entryRuleGroupBy returns [EObject current=null] : iv_ruleGroupBy= ruleGroupBy EOF ;
    public final EObject entryRuleGroupBy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroupBy = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:579:2: (iv_ruleGroupBy= ruleGroupBy EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:580:2: iv_ruleGroupBy= ruleGroupBy EOF
            {
             newCompositeNode(grammarAccess.getGroupByRule()); 
            pushFollow(FOLLOW_ruleGroupBy_in_entryRuleGroupBy1139);
            iv_ruleGroupBy=ruleGroupBy();

            state._fsp--;

             current =iv_ruleGroupBy; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroupBy1149); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:587:1: ruleGroupBy returns [EObject current=null] : (otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleGroupBy() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_variables_1_0 = null;

        EObject lv_variables_3_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:590:28: ( (otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:591:1: (otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:591:1: (otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:591:3: otherlv_0= 'group_by=[' ( (lv_variables_1_0= ruleVariable ) ) (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,28,FOLLOW_28_in_ruleGroupBy1186); 

                	newLeafNode(otherlv_0, grammarAccess.getGroupByAccess().getGroup_byKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:595:1: ( (lv_variables_1_0= ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:596:1: (lv_variables_1_0= ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:596:1: (lv_variables_1_0= ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:597:3: lv_variables_1_0= ruleVariable
            {
             
            	        newCompositeNode(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleVariable_in_ruleGroupBy1207);
            lv_variables_1_0=ruleVariable();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getGroupByRule());
            	        }
                   		add(
                   			current, 
                   			"variables",
                    		lv_variables_1_0, 
                    		"Variable");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:613:2: (otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==26) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:613:4: otherlv_2= ',' ( (lv_variables_3_0= ruleVariable ) )
            	    {
            	    otherlv_2=(Token)match(input,26,FOLLOW_26_in_ruleGroupBy1220); 

            	        	newLeafNode(otherlv_2, grammarAccess.getGroupByAccess().getCommaKeyword_2_0());
            	        
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:617:1: ( (lv_variables_3_0= ruleVariable ) )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:618:1: (lv_variables_3_0= ruleVariable )
            	    {
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:618:1: (lv_variables_3_0= ruleVariable )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:619:3: lv_variables_3_0= ruleVariable
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_2_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleVariable_in_ruleGroupBy1241);
            	    lv_variables_3_0=ruleVariable();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getGroupByRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"variables",
            	            		lv_variables_3_0, 
            	            		"Variable");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            otherlv_4=(Token)match(input,25,FOLLOW_25_in_ruleGroupBy1255); 

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:647:1: entryRuleAggregation returns [EObject current=null] : iv_ruleAggregation= ruleAggregation EOF ;
    public final EObject entryRuleAggregation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregation = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:648:2: (iv_ruleAggregation= ruleAggregation EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:649:2: iv_ruleAggregation= ruleAggregation EOF
            {
             newCompositeNode(grammarAccess.getAggregationRule()); 
            pushFollow(FOLLOW_ruleAggregation_in_entryRuleAggregation1291);
            iv_ruleAggregation=ruleAggregation();

            state._fsp--;

             current =iv_ruleAggregation; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAggregation1301); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:656:1: ruleAggregation returns [EObject current=null] : (otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )? ) ;
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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:659:28: ( (otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:660:1: (otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:660:1: (otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:660:3: otherlv_0= '[' ( (lv_function_1_0= RULE_AGG_FUNCTION ) ) otherlv_2= ',' ( (lv_varToAgg_3_0= ruleVariable ) ) otherlv_4= ',' ( (lv_aggName_5_0= RULE_STRING ) ) (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )? otherlv_8= ']' (otherlv_9= ',' )?
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleAggregation1338); 

                	newLeafNode(otherlv_0, grammarAccess.getAggregationAccess().getLeftSquareBracketKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:664:1: ( (lv_function_1_0= RULE_AGG_FUNCTION ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:665:1: (lv_function_1_0= RULE_AGG_FUNCTION )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:665:1: (lv_function_1_0= RULE_AGG_FUNCTION )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:666:3: lv_function_1_0= RULE_AGG_FUNCTION
            {
            lv_function_1_0=(Token)match(input,RULE_AGG_FUNCTION,FOLLOW_RULE_AGG_FUNCTION_in_ruleAggregation1355); 

            			newLeafNode(lv_function_1_0, grammarAccess.getAggregationAccess().getFunctionAGG_FUNCTIONTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAggregationRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"function",
                    		lv_function_1_0, 
                    		"AGG_FUNCTION");
            	    

            }


            }

            otherlv_2=(Token)match(input,26,FOLLOW_26_in_ruleAggregation1372); 

                	newLeafNode(otherlv_2, grammarAccess.getAggregationAccess().getCommaKeyword_2());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:686:1: ( (lv_varToAgg_3_0= ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:687:1: (lv_varToAgg_3_0= ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:687:1: (lv_varToAgg_3_0= ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:688:3: lv_varToAgg_3_0= ruleVariable
            {
             
            	        newCompositeNode(grammarAccess.getAggregationAccess().getVarToAggVariableParserRuleCall_3_0()); 
            	    
            pushFollow(FOLLOW_ruleVariable_in_ruleAggregation1393);
            lv_varToAgg_3_0=ruleVariable();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getAggregationRule());
            	        }
                   		set(
                   			current, 
                   			"varToAgg",
                    		lv_varToAgg_3_0, 
                    		"Variable");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_4=(Token)match(input,26,FOLLOW_26_in_ruleAggregation1405); 

                	newLeafNode(otherlv_4, grammarAccess.getAggregationAccess().getCommaKeyword_4());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:708:1: ( (lv_aggName_5_0= RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:709:1: (lv_aggName_5_0= RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:709:1: (lv_aggName_5_0= RULE_STRING )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:710:3: lv_aggName_5_0= RULE_STRING
            {
            lv_aggName_5_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAggregation1422); 

            			newLeafNode(lv_aggName_5_0, grammarAccess.getAggregationAccess().getAggNameSTRINGTerminalRuleCall_5_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAggregationRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"aggName",
                    		lv_aggName_5_0, 
                    		"STRING");
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:726:2: (otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) ) )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==26) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:726:4: otherlv_6= ',' ( (lv_datatype_7_0= RULE_STRING ) )
                    {
                    otherlv_6=(Token)match(input,26,FOLLOW_26_in_ruleAggregation1440); 

                        	newLeafNode(otherlv_6, grammarAccess.getAggregationAccess().getCommaKeyword_6_0());
                        
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:730:1: ( (lv_datatype_7_0= RULE_STRING ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:731:1: (lv_datatype_7_0= RULE_STRING )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:731:1: (lv_datatype_7_0= RULE_STRING )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:732:3: lv_datatype_7_0= RULE_STRING
                    {
                    lv_datatype_7_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAggregation1457); 

                    			newLeafNode(lv_datatype_7_0, grammarAccess.getAggregationAccess().getDatatypeSTRINGTerminalRuleCall_6_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getAggregationRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"datatype",
                            		lv_datatype_7_0, 
                            		"STRING");
                    	    

                    }


                    }


                    }
                    break;

            }

            otherlv_8=(Token)match(input,25,FOLLOW_25_in_ruleAggregation1476); 

                	newLeafNode(otherlv_8, grammarAccess.getAggregationAccess().getRightSquareBracketKeyword_7());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:752:1: (otherlv_9= ',' )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==26) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:752:3: otherlv_9= ','
                    {
                    otherlv_9=(Token)match(input,26,FOLLOW_26_in_ruleAggregation1489); 

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:764:1: entryRuleFilesinkclause returns [EObject current=null] : iv_ruleFilesinkclause= ruleFilesinkclause EOF ;
    public final EObject entryRuleFilesinkclause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFilesinkclause = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:765:2: (iv_ruleFilesinkclause= ruleFilesinkclause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:766:2: iv_ruleFilesinkclause= ruleFilesinkclause EOF
            {
             newCompositeNode(grammarAccess.getFilesinkclauseRule()); 
            pushFollow(FOLLOW_ruleFilesinkclause_in_entryRuleFilesinkclause1527);
            iv_ruleFilesinkclause=ruleFilesinkclause();

            state._fsp--;

             current =iv_ruleFilesinkclause; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFilesinkclause1537); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:773:1: ruleFilesinkclause returns [EObject current=null] : (otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')' ) ;
    public final EObject ruleFilesinkclause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_path_1_0=null;
        Token otherlv_2=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:776:28: ( (otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:777:1: (otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:777:1: (otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:777:3: otherlv_0= 'CSVFILESINK(' ( (lv_path_1_0= RULE_STRING ) ) otherlv_2= ')'
            {
            otherlv_0=(Token)match(input,30,FOLLOW_30_in_ruleFilesinkclause1574); 

                	newLeafNode(otherlv_0, grammarAccess.getFilesinkclauseAccess().getCSVFILESINKKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:781:1: ( (lv_path_1_0= RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:782:1: (lv_path_1_0= RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:782:1: (lv_path_1_0= RULE_STRING )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:783:3: lv_path_1_0= RULE_STRING
            {
            lv_path_1_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleFilesinkclause1591); 

            			newLeafNode(lv_path_1_0, grammarAccess.getFilesinkclauseAccess().getPathSTRINGTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getFilesinkclauseRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"path",
                    		lv_path_1_0, 
                    		"STRING");
            	    

            }


            }

            otherlv_2=(Token)match(input,27,FOLLOW_27_in_ruleFilesinkclause1608); 

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:811:1: entryRuleFilterclause returns [EObject current=null] : iv_ruleFilterclause= ruleFilterclause EOF ;
    public final EObject entryRuleFilterclause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFilterclause = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:812:2: (iv_ruleFilterclause= ruleFilterclause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:813:2: iv_ruleFilterclause= ruleFilterclause EOF
            {
             newCompositeNode(grammarAccess.getFilterclauseRule()); 
            pushFollow(FOLLOW_ruleFilterclause_in_entryRuleFilterclause1644);
            iv_ruleFilterclause=ruleFilterclause();

            state._fsp--;

             current =iv_ruleFilterclause; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFilterclause1654); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:820:1: ruleFilterclause returns [EObject current=null] : (otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')' ) ;
    public final EObject ruleFilterclause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_4=null;
        EObject lv_left_1_0 = null;

        Enumerator lv_operator_2_0 = null;

        EObject lv_right_3_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:823:28: ( (otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:824:1: (otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:824:1: (otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:824:3: otherlv_0= 'FILTER(' ( (lv_left_1_0= ruleVariable ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_right_3_0= ruleVariable ) ) otherlv_4= ')'
            {
            otherlv_0=(Token)match(input,31,FOLLOW_31_in_ruleFilterclause1691); 

                	newLeafNode(otherlv_0, grammarAccess.getFilterclauseAccess().getFILTERKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:828:1: ( (lv_left_1_0= ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:829:1: (lv_left_1_0= ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:829:1: (lv_left_1_0= ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:830:3: lv_left_1_0= ruleVariable
            {
             
            	        newCompositeNode(grammarAccess.getFilterclauseAccess().getLeftVariableParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleVariable_in_ruleFilterclause1712);
            lv_left_1_0=ruleVariable();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getFilterclauseRule());
            	        }
                   		set(
                   			current, 
                   			"left",
                    		lv_left_1_0, 
                    		"Variable");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:846:2: ( (lv_operator_2_0= ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:847:1: (lv_operator_2_0= ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:847:1: (lv_operator_2_0= ruleOperator )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:848:3: lv_operator_2_0= ruleOperator
            {
             
            	        newCompositeNode(grammarAccess.getFilterclauseAccess().getOperatorOperatorEnumRuleCall_2_0()); 
            	    
            pushFollow(FOLLOW_ruleOperator_in_ruleFilterclause1733);
            lv_operator_2_0=ruleOperator();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getFilterclauseRule());
            	        }
                   		set(
                   			current, 
                   			"operator",
                    		lv_operator_2_0, 
                    		"Operator");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:864:2: ( (lv_right_3_0= ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:865:1: (lv_right_3_0= ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:865:1: (lv_right_3_0= ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:866:3: lv_right_3_0= ruleVariable
            {
             
            	        newCompositeNode(grammarAccess.getFilterclauseAccess().getRightVariableParserRuleCall_3_0()); 
            	    
            pushFollow(FOLLOW_ruleVariable_in_ruleFilterclause1754);
            lv_right_3_0=ruleVariable();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getFilterclauseRule());
            	        }
                   		set(
                   			current, 
                   			"right",
                    		lv_right_3_0, 
                    		"Variable");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_4=(Token)match(input,27,FOLLOW_27_in_ruleFilterclause1766); 

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:896:1: entryRuleDatasetClause returns [EObject current=null] : iv_ruleDatasetClause= ruleDatasetClause EOF ;
    public final EObject entryRuleDatasetClause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDatasetClause = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:897:2: (iv_ruleDatasetClause= ruleDatasetClause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:898:2: iv_ruleDatasetClause= ruleDatasetClause EOF
            {
             newCompositeNode(grammarAccess.getDatasetClauseRule()); 
            pushFollow(FOLLOW_ruleDatasetClause_in_entryRuleDatasetClause1804);
            iv_ruleDatasetClause=ruleDatasetClause();

            state._fsp--;

             current =iv_ruleDatasetClause; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDatasetClause1814); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:905:1: ruleDatasetClause returns [EObject current=null] : (otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[TYPE ' ( (lv_type_5_0= RULE_WINDOWTYPE ) ) otherlv_6= 'SIZE ' ( (lv_size_7_0= RULE_INT ) ) (otherlv_8= 'ADVANCE' ( (lv_advance_9_0= RULE_INT ) ) )? (otherlv_10= 'UNIT ' ( (lv_unit_11_0= RULE_UNITTYPE ) ) )? otherlv_12= ']' )? ) ;
    public final EObject ruleDatasetClause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token otherlv_4=null;
        Token lv_type_5_0=null;
        Token otherlv_6=null;
        Token lv_size_7_0=null;
        Token otherlv_8=null;
        Token lv_advance_9_0=null;
        Token otherlv_10=null;
        Token lv_unit_11_0=null;
        Token otherlv_12=null;
        EObject lv_dataSet_1_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:908:28: ( (otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[TYPE ' ( (lv_type_5_0= RULE_WINDOWTYPE ) ) otherlv_6= 'SIZE ' ( (lv_size_7_0= RULE_INT ) ) (otherlv_8= 'ADVANCE' ( (lv_advance_9_0= RULE_INT ) ) )? (otherlv_10= 'UNIT ' ( (lv_unit_11_0= RULE_UNITTYPE ) ) )? otherlv_12= ']' )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:909:1: (otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[TYPE ' ( (lv_type_5_0= RULE_WINDOWTYPE ) ) otherlv_6= 'SIZE ' ( (lv_size_7_0= RULE_INT ) ) (otherlv_8= 'ADVANCE' ( (lv_advance_9_0= RULE_INT ) ) )? (otherlv_10= 'UNIT ' ( (lv_unit_11_0= RULE_UNITTYPE ) ) )? otherlv_12= ']' )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:909:1: (otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[TYPE ' ( (lv_type_5_0= RULE_WINDOWTYPE ) ) otherlv_6= 'SIZE ' ( (lv_size_7_0= RULE_INT ) ) (otherlv_8= 'ADVANCE' ( (lv_advance_9_0= RULE_INT ) ) )? (otherlv_10= 'UNIT ' ( (lv_unit_11_0= RULE_UNITTYPE ) ) )? otherlv_12= ']' )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:909:3: otherlv_0= 'FROM' ( (lv_dataSet_1_0= ruleIRI ) ) otherlv_2= 'AS' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= '[TYPE ' ( (lv_type_5_0= RULE_WINDOWTYPE ) ) otherlv_6= 'SIZE ' ( (lv_size_7_0= RULE_INT ) ) (otherlv_8= 'ADVANCE' ( (lv_advance_9_0= RULE_INT ) ) )? (otherlv_10= 'UNIT ' ( (lv_unit_11_0= RULE_UNITTYPE ) ) )? otherlv_12= ']' )?
            {
            otherlv_0=(Token)match(input,32,FOLLOW_32_in_ruleDatasetClause1851); 

                	newLeafNode(otherlv_0, grammarAccess.getDatasetClauseAccess().getFROMKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:913:1: ( (lv_dataSet_1_0= ruleIRI ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:914:1: (lv_dataSet_1_0= ruleIRI )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:914:1: (lv_dataSet_1_0= ruleIRI )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:915:3: lv_dataSet_1_0= ruleIRI
            {
             
            	        newCompositeNode(grammarAccess.getDatasetClauseAccess().getDataSetIRIParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleIRI_in_ruleDatasetClause1872);
            lv_dataSet_1_0=ruleIRI();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getDatasetClauseRule());
            	        }
                   		set(
                   			current, 
                   			"dataSet",
                    		lv_dataSet_1_0, 
                    		"IRI");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_2=(Token)match(input,33,FOLLOW_33_in_ruleDatasetClause1884); 

                	newLeafNode(otherlv_2, grammarAccess.getDatasetClauseAccess().getASKeyword_2());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:935:1: ( (lv_name_3_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:936:1: (lv_name_3_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:936:1: (lv_name_3_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:937:3: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleDatasetClause1901); 

            			newLeafNode(lv_name_3_0, grammarAccess.getDatasetClauseAccess().getNameIDTerminalRuleCall_3_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDatasetClauseRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_3_0, 
                    		"ID");
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:953:2: (otherlv_4= '[TYPE ' ( (lv_type_5_0= RULE_WINDOWTYPE ) ) otherlv_6= 'SIZE ' ( (lv_size_7_0= RULE_INT ) ) (otherlv_8= 'ADVANCE' ( (lv_advance_9_0= RULE_INT ) ) )? (otherlv_10= 'UNIT ' ( (lv_unit_11_0= RULE_UNITTYPE ) ) )? otherlv_12= ']' )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==34) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:953:4: otherlv_4= '[TYPE ' ( (lv_type_5_0= RULE_WINDOWTYPE ) ) otherlv_6= 'SIZE ' ( (lv_size_7_0= RULE_INT ) ) (otherlv_8= 'ADVANCE' ( (lv_advance_9_0= RULE_INT ) ) )? (otherlv_10= 'UNIT ' ( (lv_unit_11_0= RULE_UNITTYPE ) ) )? otherlv_12= ']'
                    {
                    otherlv_4=(Token)match(input,34,FOLLOW_34_in_ruleDatasetClause1919); 

                        	newLeafNode(otherlv_4, grammarAccess.getDatasetClauseAccess().getTYPEKeyword_4_0());
                        
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:957:1: ( (lv_type_5_0= RULE_WINDOWTYPE ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:958:1: (lv_type_5_0= RULE_WINDOWTYPE )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:958:1: (lv_type_5_0= RULE_WINDOWTYPE )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:959:3: lv_type_5_0= RULE_WINDOWTYPE
                    {
                    lv_type_5_0=(Token)match(input,RULE_WINDOWTYPE,FOLLOW_RULE_WINDOWTYPE_in_ruleDatasetClause1936); 

                    			newLeafNode(lv_type_5_0, grammarAccess.getDatasetClauseAccess().getTypeWINDOWTYPETerminalRuleCall_4_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getDatasetClauseRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"type",
                            		lv_type_5_0, 
                            		"WINDOWTYPE");
                    	    

                    }


                    }

                    otherlv_6=(Token)match(input,35,FOLLOW_35_in_ruleDatasetClause1953); 

                        	newLeafNode(otherlv_6, grammarAccess.getDatasetClauseAccess().getSIZEKeyword_4_2());
                        
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:979:1: ( (lv_size_7_0= RULE_INT ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:980:1: (lv_size_7_0= RULE_INT )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:980:1: (lv_size_7_0= RULE_INT )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:981:3: lv_size_7_0= RULE_INT
                    {
                    lv_size_7_0=(Token)match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleDatasetClause1970); 

                    			newLeafNode(lv_size_7_0, grammarAccess.getDatasetClauseAccess().getSizeINTTerminalRuleCall_4_3_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getDatasetClauseRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"size",
                            		lv_size_7_0, 
                            		"INT");
                    	    

                    }


                    }

                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:997:2: (otherlv_8= 'ADVANCE' ( (lv_advance_9_0= RULE_INT ) ) )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==36) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:997:4: otherlv_8= 'ADVANCE' ( (lv_advance_9_0= RULE_INT ) )
                            {
                            otherlv_8=(Token)match(input,36,FOLLOW_36_in_ruleDatasetClause1988); 

                                	newLeafNode(otherlv_8, grammarAccess.getDatasetClauseAccess().getADVANCEKeyword_4_4_0());
                                
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1001:1: ( (lv_advance_9_0= RULE_INT ) )
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1002:1: (lv_advance_9_0= RULE_INT )
                            {
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1002:1: (lv_advance_9_0= RULE_INT )
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1003:3: lv_advance_9_0= RULE_INT
                            {
                            lv_advance_9_0=(Token)match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleDatasetClause2005); 

                            			newLeafNode(lv_advance_9_0, grammarAccess.getDatasetClauseAccess().getAdvanceINTTerminalRuleCall_4_4_1_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getDatasetClauseRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"advance",
                                    		lv_advance_9_0, 
                                    		"INT");
                            	    

                            }


                            }


                            }
                            break;

                    }

                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1019:4: (otherlv_10= 'UNIT ' ( (lv_unit_11_0= RULE_UNITTYPE ) ) )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==37) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1019:6: otherlv_10= 'UNIT ' ( (lv_unit_11_0= RULE_UNITTYPE ) )
                            {
                            otherlv_10=(Token)match(input,37,FOLLOW_37_in_ruleDatasetClause2025); 

                                	newLeafNode(otherlv_10, grammarAccess.getDatasetClauseAccess().getUNITKeyword_4_5_0());
                                
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1023:1: ( (lv_unit_11_0= RULE_UNITTYPE ) )
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1024:1: (lv_unit_11_0= RULE_UNITTYPE )
                            {
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1024:1: (lv_unit_11_0= RULE_UNITTYPE )
                            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1025:3: lv_unit_11_0= RULE_UNITTYPE
                            {
                            lv_unit_11_0=(Token)match(input,RULE_UNITTYPE,FOLLOW_RULE_UNITTYPE_in_ruleDatasetClause2042); 

                            			newLeafNode(lv_unit_11_0, grammarAccess.getDatasetClauseAccess().getUnitUNITTYPETerminalRuleCall_4_5_1_0()); 
                            		

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getDatasetClauseRule());
                            	        }
                                   		setWithLastConsumed(
                                   			current, 
                                   			"unit",
                                    		lv_unit_11_0, 
                                    		"UNITTYPE");
                            	    

                            }


                            }


                            }
                            break;

                    }

                    otherlv_12=(Token)match(input,25,FOLLOW_25_in_ruleDatasetClause2061); 

                        	newLeafNode(otherlv_12, grammarAccess.getDatasetClauseAccess().getRightSquareBracketKeyword_4_6());
                        

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1053:1: entryRuleWhereClause returns [EObject current=null] : iv_ruleWhereClause= ruleWhereClause EOF ;
    public final EObject entryRuleWhereClause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWhereClause = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1054:2: (iv_ruleWhereClause= ruleWhereClause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1055:2: iv_ruleWhereClause= ruleWhereClause EOF
            {
             newCompositeNode(grammarAccess.getWhereClauseRule()); 
            pushFollow(FOLLOW_ruleWhereClause_in_entryRuleWhereClause2099);
            iv_ruleWhereClause=ruleWhereClause();

            state._fsp--;

             current =iv_ruleWhereClause; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleWhereClause2109); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1062:1: ruleWhereClause returns [EObject current=null] : (otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}' ) ;
    public final EObject ruleWhereClause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_whereclauses_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1065:28: ( (otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1066:1: (otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1066:1: (otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1066:3: otherlv_0= 'WHERE' otherlv_1= '{' ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+ otherlv_3= '}'
            {
            otherlv_0=(Token)match(input,38,FOLLOW_38_in_ruleWhereClause2146); 

                	newLeafNode(otherlv_0, grammarAccess.getWhereClauseAccess().getWHEREKeyword_0());
                
            otherlv_1=(Token)match(input,39,FOLLOW_39_in_ruleWhereClause2158); 

                	newLeafNode(otherlv_1, grammarAccess.getWhereClauseAccess().getLeftCurlyBracketKeyword_1());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1074:1: ( (lv_whereclauses_2_0= ruleInnerWhereClause ) )+
            int cnt21=0;
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==RULE_ID) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1075:1: (lv_whereclauses_2_0= ruleInnerWhereClause )
            	    {
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1075:1: (lv_whereclauses_2_0= ruleInnerWhereClause )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1076:3: lv_whereclauses_2_0= ruleInnerWhereClause
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getWhereClauseAccess().getWhereclausesInnerWhereClauseParserRuleCall_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleInnerWhereClause_in_ruleWhereClause2179);
            	    lv_whereclauses_2_0=ruleInnerWhereClause();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getWhereClauseRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"whereclauses",
            	            		lv_whereclauses_2_0, 
            	            		"InnerWhereClause");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt21 >= 1 ) break loop21;
                        EarlyExitException eee =
                            new EarlyExitException(21, input);
                        throw eee;
                }
                cnt21++;
            } while (true);

            otherlv_3=(Token)match(input,40,FOLLOW_40_in_ruleWhereClause2192); 

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1104:1: entryRuleInnerWhereClause returns [EObject current=null] : iv_ruleInnerWhereClause= ruleInnerWhereClause EOF ;
    public final EObject entryRuleInnerWhereClause() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInnerWhereClause = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1105:2: (iv_ruleInnerWhereClause= ruleInnerWhereClause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1106:2: iv_ruleInnerWhereClause= ruleInnerWhereClause EOF
            {
             newCompositeNode(grammarAccess.getInnerWhereClauseRule()); 
            pushFollow(FOLLOW_ruleInnerWhereClause_in_entryRuleInnerWhereClause2228);
            iv_ruleInnerWhereClause=ruleInnerWhereClause();

            state._fsp--;

             current =iv_ruleInnerWhereClause; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleInnerWhereClause2238); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1113:1: ruleInnerWhereClause returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) ) ) ;
    public final EObject ruleInnerWhereClause() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_groupGraphPattern_1_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1116:28: ( ( ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1117:1: ( ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1117:1: ( ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1117:2: ( (otherlv_0= RULE_ID ) ) ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1117:2: ( (otherlv_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1118:1: (otherlv_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1118:1: (otherlv_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1119:3: otherlv_0= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getInnerWhereClauseRule());
            	        }
                    
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleInnerWhereClause2283); 

            		newLeafNode(otherlv_0, grammarAccess.getInnerWhereClauseAccess().getNameDatasetClauseCrossReference_0_0()); 
            	

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1130:2: ( (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1131:1: (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1131:1: (lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1132:3: lv_groupGraphPattern_1_0= ruleGroupGraphPatternSub
            {
             
            	        newCompositeNode(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternGroupGraphPatternSubParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleGroupGraphPatternSub_in_ruleInnerWhereClause2304);
            lv_groupGraphPattern_1_0=ruleGroupGraphPatternSub();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getInnerWhereClauseRule());
            	        }
                   		set(
                   			current, 
                   			"groupGraphPattern",
                    		lv_groupGraphPattern_1_0, 
                    		"GroupGraphPatternSub");
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1156:1: entryRuleGroupGraphPatternSub returns [EObject current=null] : iv_ruleGroupGraphPatternSub= ruleGroupGraphPatternSub EOF ;
    public final EObject entryRuleGroupGraphPatternSub() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGroupGraphPatternSub = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1157:2: (iv_ruleGroupGraphPatternSub= ruleGroupGraphPatternSub EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1158:2: iv_ruleGroupGraphPatternSub= ruleGroupGraphPatternSub EOF
            {
             newCompositeNode(grammarAccess.getGroupGraphPatternSubRule()); 
            pushFollow(FOLLOW_ruleGroupGraphPatternSub_in_entryRuleGroupGraphPatternSub2340);
            iv_ruleGroupGraphPatternSub=ruleGroupGraphPatternSub();

            state._fsp--;

             current =iv_ruleGroupGraphPatternSub; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroupGraphPatternSub2350); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1165:1: ruleGroupGraphPatternSub returns [EObject current=null] : (otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}' ) ;
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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1168:28: ( (otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1169:1: (otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1169:1: (otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1169:3: otherlv_0= '{' ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) ) (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )* (otherlv_4= '.' )? otherlv_5= '}'
            {
            otherlv_0=(Token)match(input,39,FOLLOW_39_in_ruleGroupGraphPatternSub2387); 

                	newLeafNode(otherlv_0, grammarAccess.getGroupGraphPatternSubAccess().getLeftCurlyBracketKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1173:1: ( (lv_graphPatterns_1_0= ruleTriplesSameSubject ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1174:1: (lv_graphPatterns_1_0= ruleTriplesSameSubject )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1174:1: (lv_graphPatterns_1_0= ruleTriplesSameSubject )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1175:3: lv_graphPatterns_1_0= ruleTriplesSameSubject
            {
             
            	        newCompositeNode(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleTriplesSameSubject_in_ruleGroupGraphPatternSub2408);
            lv_graphPatterns_1_0=ruleTriplesSameSubject();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getGroupGraphPatternSubRule());
            	        }
                   		add(
                   			current, 
                   			"graphPatterns",
                    		lv_graphPatterns_1_0, 
                    		"TriplesSameSubject");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1191:2: (otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) ) )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==41) ) {
                    int LA22_1 = input.LA(2);

                    if ( ((LA22_1>=RULE_ID && LA22_1<=RULE_IRI_TERMINAL)||LA22_1==RULE_STRING||LA22_1==43) ) {
                        alt22=1;
                    }


                }


                switch (alt22) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1191:4: otherlv_2= '.' ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) )
            	    {
            	    otherlv_2=(Token)match(input,41,FOLLOW_41_in_ruleGroupGraphPatternSub2421); 

            	        	newLeafNode(otherlv_2, grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_2_0());
            	        
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1195:1: ( (lv_graphPatterns_3_0= ruleTriplesSameSubject ) )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1196:1: (lv_graphPatterns_3_0= ruleTriplesSameSubject )
            	    {
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1196:1: (lv_graphPatterns_3_0= ruleTriplesSameSubject )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1197:3: lv_graphPatterns_3_0= ruleTriplesSameSubject
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_2_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleTriplesSameSubject_in_ruleGroupGraphPatternSub2442);
            	    lv_graphPatterns_3_0=ruleTriplesSameSubject();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getGroupGraphPatternSubRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"graphPatterns",
            	            		lv_graphPatterns_3_0, 
            	            		"TriplesSameSubject");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1213:4: (otherlv_4= '.' )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==41) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1213:6: otherlv_4= '.'
                    {
                    otherlv_4=(Token)match(input,41,FOLLOW_41_in_ruleGroupGraphPatternSub2457); 

                        	newLeafNode(otherlv_4, grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_3());
                        

                    }
                    break;

            }

            otherlv_5=(Token)match(input,40,FOLLOW_40_in_ruleGroupGraphPatternSub2471); 

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1229:1: entryRuleTriplesSameSubject returns [EObject current=null] : iv_ruleTriplesSameSubject= ruleTriplesSameSubject EOF ;
    public final EObject entryRuleTriplesSameSubject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTriplesSameSubject = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1230:2: (iv_ruleTriplesSameSubject= ruleTriplesSameSubject EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1231:2: iv_ruleTriplesSameSubject= ruleTriplesSameSubject EOF
            {
             newCompositeNode(grammarAccess.getTriplesSameSubjectRule()); 
            pushFollow(FOLLOW_ruleTriplesSameSubject_in_entryRuleTriplesSameSubject2507);
            iv_ruleTriplesSameSubject=ruleTriplesSameSubject();

            state._fsp--;

             current =iv_ruleTriplesSameSubject; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTriplesSameSubject2517); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1238:1: ruleTriplesSameSubject returns [EObject current=null] : ( ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )* ) ;
    public final EObject ruleTriplesSameSubject() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject lv_subject_0_0 = null;

        EObject lv_propertyList_1_0 = null;

        EObject lv_propertyList_3_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1241:28: ( ( ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )* ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1242:1: ( ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )* )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1242:1: ( ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1242:2: ( (lv_subject_0_0= ruleGraphNode ) ) ( (lv_propertyList_1_0= rulePropertyList ) ) (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )*
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1242:2: ( (lv_subject_0_0= ruleGraphNode ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1243:1: (lv_subject_0_0= ruleGraphNode )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1243:1: (lv_subject_0_0= ruleGraphNode )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1244:3: lv_subject_0_0= ruleGraphNode
            {
             
            	        newCompositeNode(grammarAccess.getTriplesSameSubjectAccess().getSubjectGraphNodeParserRuleCall_0_0()); 
            	    
            pushFollow(FOLLOW_ruleGraphNode_in_ruleTriplesSameSubject2563);
            lv_subject_0_0=ruleGraphNode();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getTriplesSameSubjectRule());
            	        }
                   		set(
                   			current, 
                   			"subject",
                    		lv_subject_0_0, 
                    		"GraphNode");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1260:2: ( (lv_propertyList_1_0= rulePropertyList ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1261:1: (lv_propertyList_1_0= rulePropertyList )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1261:1: (lv_propertyList_1_0= rulePropertyList )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1262:3: lv_propertyList_1_0= rulePropertyList
            {
             
            	        newCompositeNode(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_rulePropertyList_in_ruleTriplesSameSubject2584);
            lv_propertyList_1_0=rulePropertyList();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getTriplesSameSubjectRule());
            	        }
                   		add(
                   			current, 
                   			"propertyList",
                    		lv_propertyList_1_0, 
                    		"PropertyList");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1278:2: (otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) ) )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==42) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1278:4: otherlv_2= ';' ( (lv_propertyList_3_0= rulePropertyList ) )
            	    {
            	    otherlv_2=(Token)match(input,42,FOLLOW_42_in_ruleTriplesSameSubject2597); 

            	        	newLeafNode(otherlv_2, grammarAccess.getTriplesSameSubjectAccess().getSemicolonKeyword_2_0());
            	        
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1282:1: ( (lv_propertyList_3_0= rulePropertyList ) )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1283:1: (lv_propertyList_3_0= rulePropertyList )
            	    {
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1283:1: (lv_propertyList_3_0= rulePropertyList )
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1284:3: lv_propertyList_3_0= rulePropertyList
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_2_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_rulePropertyList_in_ruleTriplesSameSubject2618);
            	    lv_propertyList_3_0=rulePropertyList();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getTriplesSameSubjectRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"propertyList",
            	            		lv_propertyList_3_0, 
            	            		"PropertyList");
            	    	        afterParserOrEnumRuleCall();
            	    	    

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

             leaveRule(); 
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1308:1: entryRulePropertyList returns [EObject current=null] : iv_rulePropertyList= rulePropertyList EOF ;
    public final EObject entryRulePropertyList() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePropertyList = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1309:2: (iv_rulePropertyList= rulePropertyList EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1310:2: iv_rulePropertyList= rulePropertyList EOF
            {
             newCompositeNode(grammarAccess.getPropertyListRule()); 
            pushFollow(FOLLOW_rulePropertyList_in_entryRulePropertyList2656);
            iv_rulePropertyList=rulePropertyList();

            state._fsp--;

             current =iv_rulePropertyList; 
            match(input,EOF,FOLLOW_EOF_in_entryRulePropertyList2666); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1317:1: rulePropertyList returns [EObject current=null] : ( ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) ) ) ;
    public final EObject rulePropertyList() throws RecognitionException {
        EObject current = null;

        EObject lv_property_0_0 = null;

        EObject lv_object_1_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1320:28: ( ( ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1321:1: ( ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1321:1: ( ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1321:2: ( (lv_property_0_0= ruleGraphNode ) ) ( (lv_object_1_0= ruleGraphNode ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1321:2: ( (lv_property_0_0= ruleGraphNode ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1322:1: (lv_property_0_0= ruleGraphNode )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1322:1: (lv_property_0_0= ruleGraphNode )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1323:3: lv_property_0_0= ruleGraphNode
            {
             
            	        newCompositeNode(grammarAccess.getPropertyListAccess().getPropertyGraphNodeParserRuleCall_0_0()); 
            	    
            pushFollow(FOLLOW_ruleGraphNode_in_rulePropertyList2712);
            lv_property_0_0=ruleGraphNode();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getPropertyListRule());
            	        }
                   		set(
                   			current, 
                   			"property",
                    		lv_property_0_0, 
                    		"GraphNode");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1339:2: ( (lv_object_1_0= ruleGraphNode ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1340:1: (lv_object_1_0= ruleGraphNode )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1340:1: (lv_object_1_0= ruleGraphNode )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1341:3: lv_object_1_0= ruleGraphNode
            {
             
            	        newCompositeNode(grammarAccess.getPropertyListAccess().getObjectGraphNodeParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleGraphNode_in_rulePropertyList2733);
            lv_object_1_0=ruleGraphNode();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getPropertyListRule());
            	        }
                   		set(
                   			current, 
                   			"object",
                    		lv_object_1_0, 
                    		"GraphNode");
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1365:1: entryRuleGraphNode returns [EObject current=null] : iv_ruleGraphNode= ruleGraphNode EOF ;
    public final EObject entryRuleGraphNode() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGraphNode = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1366:2: (iv_ruleGraphNode= ruleGraphNode EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1367:2: iv_ruleGraphNode= ruleGraphNode EOF
            {
             newCompositeNode(grammarAccess.getGraphNodeRule()); 
            pushFollow(FOLLOW_ruleGraphNode_in_entryRuleGraphNode2769);
            iv_ruleGraphNode=ruleGraphNode();

            state._fsp--;

             current =iv_ruleGraphNode; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGraphNode2779); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1374:1: ruleGraphNode returns [EObject current=null] : ( ( (lv_variable_0_0= ruleVariable ) ) | ( (lv_literal_1_0= RULE_STRING ) ) | ( (lv_iri_2_0= ruleIRI ) ) ) ;
    public final EObject ruleGraphNode() throws RecognitionException {
        EObject current = null;

        Token lv_literal_1_0=null;
        EObject lv_variable_0_0 = null;

        EObject lv_iri_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1377:28: ( ( ( (lv_variable_0_0= ruleVariable ) ) | ( (lv_literal_1_0= RULE_STRING ) ) | ( (lv_iri_2_0= ruleIRI ) ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1378:1: ( ( (lv_variable_0_0= ruleVariable ) ) | ( (lv_literal_1_0= RULE_STRING ) ) | ( (lv_iri_2_0= ruleIRI ) ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1378:1: ( ( (lv_variable_0_0= ruleVariable ) ) | ( (lv_literal_1_0= RULE_STRING ) ) | ( (lv_iri_2_0= ruleIRI ) ) )
            int alt25=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
            case 43:
                {
                alt25=1;
                }
                break;
            case RULE_STRING:
                {
                alt25=2;
                }
                break;
            case RULE_IRI_TERMINAL:
                {
                alt25=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }

            switch (alt25) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1378:2: ( (lv_variable_0_0= ruleVariable ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1378:2: ( (lv_variable_0_0= ruleVariable ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1379:1: (lv_variable_0_0= ruleVariable )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1379:1: (lv_variable_0_0= ruleVariable )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1380:3: lv_variable_0_0= ruleVariable
                    {
                     
                    	        newCompositeNode(grammarAccess.getGraphNodeAccess().getVariableVariableParserRuleCall_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleVariable_in_ruleGraphNode2825);
                    lv_variable_0_0=ruleVariable();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getGraphNodeRule());
                    	        }
                           		set(
                           			current, 
                           			"variable",
                            		lv_variable_0_0, 
                            		"Variable");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1397:6: ( (lv_literal_1_0= RULE_STRING ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1397:6: ( (lv_literal_1_0= RULE_STRING ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1398:1: (lv_literal_1_0= RULE_STRING )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1398:1: (lv_literal_1_0= RULE_STRING )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1399:3: lv_literal_1_0= RULE_STRING
                    {
                    lv_literal_1_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleGraphNode2848); 

                    			newLeafNode(lv_literal_1_0, grammarAccess.getGraphNodeAccess().getLiteralSTRINGTerminalRuleCall_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getGraphNodeRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"literal",
                            		lv_literal_1_0, 
                            		"STRING");
                    	    

                    }


                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1416:6: ( (lv_iri_2_0= ruleIRI ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1416:6: ( (lv_iri_2_0= ruleIRI ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1417:1: (lv_iri_2_0= ruleIRI )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1417:1: (lv_iri_2_0= ruleIRI )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1418:3: lv_iri_2_0= ruleIRI
                    {
                     
                    	        newCompositeNode(grammarAccess.getGraphNodeAccess().getIriIRIParserRuleCall_2_0()); 
                    	    
                    pushFollow(FOLLOW_ruleIRI_in_ruleGraphNode2880);
                    lv_iri_2_0=ruleIRI();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getGraphNodeRule());
                    	        }
                           		set(
                           			current, 
                           			"iri",
                            		lv_iri_2_0, 
                            		"IRI");
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1442:1: entryRuleVariable returns [EObject current=null] : iv_ruleVariable= ruleVariable EOF ;
    public final EObject entryRuleVariable() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleVariable = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1443:2: (iv_ruleVariable= ruleVariable EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1444:2: iv_ruleVariable= ruleVariable EOF
            {
             newCompositeNode(grammarAccess.getVariableRule()); 
            pushFollow(FOLLOW_ruleVariable_in_entryRuleVariable2916);
            iv_ruleVariable=ruleVariable();

            state._fsp--;

             current =iv_ruleVariable; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleVariable2926); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1451:1: ruleVariable returns [EObject current=null] : ( ( (lv_unnamed_0_0= ruleUnNamedVariable ) ) | ( (lv_property_1_0= ruleProperty ) ) ) ;
    public final EObject ruleVariable() throws RecognitionException {
        EObject current = null;

        EObject lv_unnamed_0_0 = null;

        EObject lv_property_1_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1454:28: ( ( ( (lv_unnamed_0_0= ruleUnNamedVariable ) ) | ( (lv_property_1_0= ruleProperty ) ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1455:1: ( ( (lv_unnamed_0_0= ruleUnNamedVariable ) ) | ( (lv_property_1_0= ruleProperty ) ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1455:1: ( ( (lv_unnamed_0_0= ruleUnNamedVariable ) ) | ( (lv_property_1_0= ruleProperty ) ) )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==43) ) {
                alt26=1;
            }
            else if ( (LA26_0==RULE_ID) ) {
                alt26=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1455:2: ( (lv_unnamed_0_0= ruleUnNamedVariable ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1455:2: ( (lv_unnamed_0_0= ruleUnNamedVariable ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1456:1: (lv_unnamed_0_0= ruleUnNamedVariable )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1456:1: (lv_unnamed_0_0= ruleUnNamedVariable )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1457:3: lv_unnamed_0_0= ruleUnNamedVariable
                    {
                     
                    	        newCompositeNode(grammarAccess.getVariableAccess().getUnnamedUnNamedVariableParserRuleCall_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleUnNamedVariable_in_ruleVariable2972);
                    lv_unnamed_0_0=ruleUnNamedVariable();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getVariableRule());
                    	        }
                           		set(
                           			current, 
                           			"unnamed",
                            		lv_unnamed_0_0, 
                            		"UnNamedVariable");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1474:6: ( (lv_property_1_0= ruleProperty ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1474:6: ( (lv_property_1_0= ruleProperty ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1475:1: (lv_property_1_0= ruleProperty )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1475:1: (lv_property_1_0= ruleProperty )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1476:3: lv_property_1_0= ruleProperty
                    {
                     
                    	        newCompositeNode(grammarAccess.getVariableAccess().getPropertyPropertyParserRuleCall_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleProperty_in_ruleVariable2999);
                    lv_property_1_0=ruleProperty();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getVariableRule());
                    	        }
                           		set(
                           			current, 
                           			"property",
                            		lv_property_1_0, 
                            		"Property");
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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1500:1: entryRuleUnNamedVariable returns [EObject current=null] : iv_ruleUnNamedVariable= ruleUnNamedVariable EOF ;
    public final EObject entryRuleUnNamedVariable() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUnNamedVariable = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1501:2: (iv_ruleUnNamedVariable= ruleUnNamedVariable EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1502:2: iv_ruleUnNamedVariable= ruleUnNamedVariable EOF
            {
             newCompositeNode(grammarAccess.getUnNamedVariableRule()); 
            pushFollow(FOLLOW_ruleUnNamedVariable_in_entryRuleUnNamedVariable3035);
            iv_ruleUnNamedVariable=ruleUnNamedVariable();

            state._fsp--;

             current =iv_ruleUnNamedVariable; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleUnNamedVariable3045); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1509:1: ruleUnNamedVariable returns [EObject current=null] : (otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleUnNamedVariable() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1512:28: ( (otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1513:1: (otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1513:1: (otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1513:3: otherlv_0= '?' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,43,FOLLOW_43_in_ruleUnNamedVariable3082); 

                	newLeafNode(otherlv_0, grammarAccess.getUnNamedVariableAccess().getQuestionMarkKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1517:1: ( (lv_name_1_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1518:1: (lv_name_1_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1518:1: (lv_name_1_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1519:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleUnNamedVariable3099); 

            			newLeafNode(lv_name_1_0, grammarAccess.getUnNamedVariableAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getUnNamedVariableRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1543:1: entryRuleProperty returns [EObject current=null] : iv_ruleProperty= ruleProperty EOF ;
    public final EObject entryRuleProperty() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProperty = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1544:2: (iv_ruleProperty= ruleProperty EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1545:2: iv_ruleProperty= ruleProperty EOF
            {
             newCompositeNode(grammarAccess.getPropertyRule()); 
            pushFollow(FOLLOW_ruleProperty_in_entryRuleProperty3140);
            iv_ruleProperty=ruleProperty();

            state._fsp--;

             current =iv_ruleProperty; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleProperty3150); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1552:1: ruleProperty returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) ) ) ;
    public final EObject ruleProperty() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1555:28: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1556:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1556:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1556:2: ( (otherlv_0= RULE_ID ) ) otherlv_1= ':' ( (lv_name_2_0= RULE_ID ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1556:2: ( (otherlv_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1557:1: (otherlv_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1557:1: (otherlv_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1558:3: otherlv_0= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getPropertyRule());
            	        }
                    
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleProperty3195); 

            		newLeafNode(otherlv_0, grammarAccess.getPropertyAccess().getPrefixPrefixCrossReference_0_0()); 
            	

            }


            }

            otherlv_1=(Token)match(input,16,FOLLOW_16_in_ruleProperty3207); 

                	newLeafNode(otherlv_1, grammarAccess.getPropertyAccess().getColonKeyword_1());
                
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1573:1: ( (lv_name_2_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1574:1: (lv_name_2_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1574:1: (lv_name_2_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1575:3: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleProperty3224); 

            			newLeafNode(lv_name_2_0, grammarAccess.getPropertyAccess().getNameIDTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getPropertyRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_2_0, 
                    		"ID");
            	    

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1599:1: entryRuleIRI returns [EObject current=null] : iv_ruleIRI= ruleIRI EOF ;
    public final EObject entryRuleIRI() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIRI = null;


        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1600:2: (iv_ruleIRI= ruleIRI EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1601:2: iv_ruleIRI= ruleIRI EOF
            {
             newCompositeNode(grammarAccess.getIRIRule()); 
            pushFollow(FOLLOW_ruleIRI_in_entryRuleIRI3265);
            iv_ruleIRI=ruleIRI();

            state._fsp--;

             current =iv_ruleIRI; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleIRI3275); 

            }

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1608:1: ruleIRI returns [EObject current=null] : ( () ( (lv_value_1_0= RULE_IRI_TERMINAL ) ) ) ;
    public final EObject ruleIRI() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1611:28: ( ( () ( (lv_value_1_0= RULE_IRI_TERMINAL ) ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1612:1: ( () ( (lv_value_1_0= RULE_IRI_TERMINAL ) ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1612:1: ( () ( (lv_value_1_0= RULE_IRI_TERMINAL ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1612:2: () ( (lv_value_1_0= RULE_IRI_TERMINAL ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1612:2: ()
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1613:5: 
            {

                    current = forceCreateModelElement(
                        grammarAccess.getIRIAccess().getIRIAction_0(),
                        current);
                

            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1618:2: ( (lv_value_1_0= RULE_IRI_TERMINAL ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1619:1: (lv_value_1_0= RULE_IRI_TERMINAL )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1619:1: (lv_value_1_0= RULE_IRI_TERMINAL )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1620:3: lv_value_1_0= RULE_IRI_TERMINAL
            {
            lv_value_1_0=(Token)match(input,RULE_IRI_TERMINAL,FOLLOW_RULE_IRI_TERMINAL_in_ruleIRI3326); 

            			newLeafNode(lv_value_1_0, grammarAccess.getIRIAccess().getValueIRI_TERMINALTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getIRIRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"value",
                    		lv_value_1_0, 
                    		"IRI_TERMINAL");
            	    

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
    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1648:1: ruleOperator returns [Enumerator current=null] : ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= '+' ) | (enumLiteral_7= '/' ) | (enumLiteral_8= '-' ) | (enumLiteral_9= '*' ) ) ;
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
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1650:28: ( ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= '+' ) | (enumLiteral_7= '/' ) | (enumLiteral_8= '-' ) | (enumLiteral_9= '*' ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1651:1: ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= '+' ) | (enumLiteral_7= '/' ) | (enumLiteral_8= '-' ) | (enumLiteral_9= '*' ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1651:1: ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= '+' ) | (enumLiteral_7= '/' ) | (enumLiteral_8= '-' ) | (enumLiteral_9= '*' ) )
            int alt27=10;
            switch ( input.LA(1) ) {
            case 44:
                {
                alt27=1;
                }
                break;
            case 45:
                {
                alt27=2;
                }
                break;
            case 46:
                {
                alt27=3;
                }
                break;
            case 47:
                {
                alt27=4;
                }
                break;
            case 48:
                {
                alt27=5;
                }
                break;
            case 49:
                {
                alt27=6;
                }
                break;
            case 50:
                {
                alt27=7;
                }
                break;
            case 51:
                {
                alt27=8;
                }
                break;
            case 52:
                {
                alt27=9;
                }
                break;
            case 53:
                {
                alt27=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1651:2: (enumLiteral_0= '<' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1651:2: (enumLiteral_0= '<' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1651:4: enumLiteral_0= '<'
                    {
                    enumLiteral_0=(Token)match(input,44,FOLLOW_44_in_ruleOperator3385); 

                            current = grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_0, grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0()); 
                        

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1657:6: (enumLiteral_1= '>' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1657:6: (enumLiteral_1= '>' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1657:8: enumLiteral_1= '>'
                    {
                    enumLiteral_1=(Token)match(input,45,FOLLOW_45_in_ruleOperator3402); 

                            current = grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_1, grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1()); 
                        

                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1663:6: (enumLiteral_2= '<=' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1663:6: (enumLiteral_2= '<=' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1663:8: enumLiteral_2= '<='
                    {
                    enumLiteral_2=(Token)match(input,46,FOLLOW_46_in_ruleOperator3419); 

                            current = grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_2, grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2()); 
                        

                    }


                    }
                    break;
                case 4 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1669:6: (enumLiteral_3= '>=' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1669:6: (enumLiteral_3= '>=' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1669:8: enumLiteral_3= '>='
                    {
                    enumLiteral_3=(Token)match(input,47,FOLLOW_47_in_ruleOperator3436); 

                            current = grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_3, grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3()); 
                        

                    }


                    }
                    break;
                case 5 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1675:6: (enumLiteral_4= '=' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1675:6: (enumLiteral_4= '=' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1675:8: enumLiteral_4= '='
                    {
                    enumLiteral_4=(Token)match(input,48,FOLLOW_48_in_ruleOperator3453); 

                            current = grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_4, grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4()); 
                        

                    }


                    }
                    break;
                case 6 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1681:6: (enumLiteral_5= '!=' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1681:6: (enumLiteral_5= '!=' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1681:8: enumLiteral_5= '!='
                    {
                    enumLiteral_5=(Token)match(input,49,FOLLOW_49_in_ruleOperator3470); 

                            current = grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_5, grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5()); 
                        

                    }


                    }
                    break;
                case 7 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1687:6: (enumLiteral_6= '+' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1687:6: (enumLiteral_6= '+' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1687:8: enumLiteral_6= '+'
                    {
                    enumLiteral_6=(Token)match(input,50,FOLLOW_50_in_ruleOperator3487); 

                            current = grammarAccess.getOperatorAccess().getSumEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_6, grammarAccess.getOperatorAccess().getSumEnumLiteralDeclaration_6()); 
                        

                    }


                    }
                    break;
                case 8 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1693:6: (enumLiteral_7= '/' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1693:6: (enumLiteral_7= '/' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1693:8: enumLiteral_7= '/'
                    {
                    enumLiteral_7=(Token)match(input,51,FOLLOW_51_in_ruleOperator3504); 

                            current = grammarAccess.getOperatorAccess().getDivEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_7, grammarAccess.getOperatorAccess().getDivEnumLiteralDeclaration_7()); 
                        

                    }


                    }
                    break;
                case 9 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1699:6: (enumLiteral_8= '-' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1699:6: (enumLiteral_8= '-' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1699:8: enumLiteral_8= '-'
                    {
                    enumLiteral_8=(Token)match(input,52,FOLLOW_52_in_ruleOperator3521); 

                            current = grammarAccess.getOperatorAccess().getSubEnumLiteralDeclaration_8().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_8, grammarAccess.getOperatorAccess().getSubEnumLiteralDeclaration_8()); 
                        

                    }


                    }
                    break;
                case 10 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1705:6: (enumLiteral_9= '*' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1705:6: (enumLiteral_9= '*' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql/src-gen/de/uniol/inf/is/odysseus/server/parser/antlr/internal/InternalStreamingsparql.g:1705:8: enumLiteral_9= '*'
                    {
                    enumLiteral_9=(Token)match(input,53,FOLLOW_53_in_ruleOperator3538); 

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


 

    public static final BitSet FOLLOW_ruleSPARQLQuery_in_entryRuleSPARQLQuery75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSPARQLQuery85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelectQuery_in_ruleSPARQLQuery131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rulePrefix_in_entryRulePrefix165 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRulePrefix175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rulePrefix213 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_rulePrefix230 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_rulePrefix247 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_IRI_TERMINAL_in_rulePrefix264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUnNamedPrefix_in_rulePrefix298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUnNamedPrefix_in_entryRuleUnNamedPrefix333 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleUnNamedPrefix343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_ruleUnNamedPrefix380 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleUnNamedPrefix392 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_IRI_TERMINAL_in_ruleUnNamedPrefix409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBase_in_entryRuleBase450 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBase460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_ruleBase497 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ruleIRI_in_ruleBase518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelectQuery_in_entryRuleSelectQuery554 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSelectQuery564 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_ruleSelectQuery608 = new BitSet(new long[]{0x0000000100128000L});
    public static final BitSet FOLLOW_19_in_ruleSelectQuery639 = new BitSet(new long[]{0x0000000100128000L});
    public static final BitSet FOLLOW_ruleBase_in_ruleSelectQuery662 = new BitSet(new long[]{0x0000000100108000L});
    public static final BitSet FOLLOW_rulePrefix_in_ruleSelectQuery684 = new BitSet(new long[]{0x0000000100108000L});
    public static final BitSet FOLLOW_ruleDatasetClause_in_ruleSelectQuery706 = new BitSet(new long[]{0x0000000100100000L});
    public static final BitSet FOLLOW_20_in_ruleSelectQuery719 = new BitSet(new long[]{0x0000080000600010L});
    public static final BitSet FOLLOW_21_in_ruleSelectQuery738 = new BitSet(new long[]{0x0000080000600010L});
    public static final BitSet FOLLOW_22_in_ruleSelectQuery775 = new BitSet(new long[]{0x0000080000600010L});
    public static final BitSet FOLLOW_ruleVariable_in_ruleSelectQuery811 = new BitSet(new long[]{0x0000084000600010L});
    public static final BitSet FOLLOW_ruleVariable_in_ruleSelectQuery832 = new BitSet(new long[]{0x0000084000600010L});
    public static final BitSet FOLLOW_ruleWhereClause_in_ruleSelectQuery854 = new BitSet(new long[]{0x00000000C0800002L});
    public static final BitSet FOLLOW_ruleFilterclause_in_ruleSelectQuery875 = new BitSet(new long[]{0x0000000040800002L});
    public static final BitSet FOLLOW_ruleAggregate_in_ruleSelectQuery897 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_ruleFilesinkclause_in_ruleSelectQuery919 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAggregate_in_entryRuleAggregate956 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAggregate966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_ruleAggregate1003 = new BitSet(new long[]{0x000000001D000000L});
    public static final BitSet FOLLOW_24_in_ruleAggregate1016 = new BitSet(new long[]{0x0000000022000000L});
    public static final BitSet FOLLOW_ruleAggregation_in_ruleAggregate1037 = new BitSet(new long[]{0x0000000022000000L});
    public static final BitSet FOLLOW_25_in_ruleAggregate1050 = new BitSet(new long[]{0x000000001C000000L});
    public static final BitSet FOLLOW_26_in_ruleAggregate1066 = new BitSet(new long[]{0x0000000014000000L});
    public static final BitSet FOLLOW_ruleGroupBy_in_ruleAggregate1089 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleAggregate1103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupBy_in_entryRuleGroupBy1139 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroupBy1149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_ruleGroupBy1186 = new BitSet(new long[]{0x0000080000600010L});
    public static final BitSet FOLLOW_ruleVariable_in_ruleGroupBy1207 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_26_in_ruleGroupBy1220 = new BitSet(new long[]{0x0000080000600010L});
    public static final BitSet FOLLOW_ruleVariable_in_ruleGroupBy1241 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_25_in_ruleGroupBy1255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAggregation_in_entryRuleAggregation1291 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAggregation1301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleAggregation1338 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_AGG_FUNCTION_in_ruleAggregation1355 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleAggregation1372 = new BitSet(new long[]{0x0000080000600010L});
    public static final BitSet FOLLOW_ruleVariable_in_ruleAggregation1393 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleAggregation1405 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAggregation1422 = new BitSet(new long[]{0x0000000006000000L});
    public static final BitSet FOLLOW_26_in_ruleAggregation1440 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAggregation1457 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_ruleAggregation1476 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_ruleAggregation1489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFilesinkclause_in_entryRuleFilesinkclause1527 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFilesinkclause1537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_ruleFilesinkclause1574 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleFilesinkclause1591 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleFilesinkclause1608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFilterclause_in_entryRuleFilterclause1644 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFilterclause1654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_ruleFilterclause1691 = new BitSet(new long[]{0x0000080000600010L});
    public static final BitSet FOLLOW_ruleVariable_in_ruleFilterclause1712 = new BitSet(new long[]{0x003FF00000000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleFilterclause1733 = new BitSet(new long[]{0x0000080000600010L});
    public static final BitSet FOLLOW_ruleVariable_in_ruleFilterclause1754 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleFilterclause1766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDatasetClause_in_entryRuleDatasetClause1804 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDatasetClause1814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_ruleDatasetClause1851 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ruleIRI_in_ruleDatasetClause1872 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_ruleDatasetClause1884 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleDatasetClause1901 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_ruleDatasetClause1919 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RULE_WINDOWTYPE_in_ruleDatasetClause1936 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_ruleDatasetClause1953 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleDatasetClause1970 = new BitSet(new long[]{0x0000003002000000L});
    public static final BitSet FOLLOW_36_in_ruleDatasetClause1988 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleDatasetClause2005 = new BitSet(new long[]{0x0000002002000000L});
    public static final BitSet FOLLOW_37_in_ruleDatasetClause2025 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RULE_UNITTYPE_in_ruleDatasetClause2042 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_ruleDatasetClause2061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWhereClause_in_entryRuleWhereClause2099 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWhereClause2109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_ruleWhereClause2146 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_ruleWhereClause2158 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleInnerWhereClause_in_ruleWhereClause2179 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_40_in_ruleWhereClause2192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleInnerWhereClause_in_entryRuleInnerWhereClause2228 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleInnerWhereClause2238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleInnerWhereClause2283 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ruleGroupGraphPatternSub_in_ruleInnerWhereClause2304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupGraphPatternSub_in_entryRuleGroupGraphPatternSub2340 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroupGraphPatternSub2350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_ruleGroupGraphPatternSub2387 = new BitSet(new long[]{0x00000800006000B0L});
    public static final BitSet FOLLOW_ruleTriplesSameSubject_in_ruleGroupGraphPatternSub2408 = new BitSet(new long[]{0x0000030000000000L});
    public static final BitSet FOLLOW_41_in_ruleGroupGraphPatternSub2421 = new BitSet(new long[]{0x00000800006000B0L});
    public static final BitSet FOLLOW_ruleTriplesSameSubject_in_ruleGroupGraphPatternSub2442 = new BitSet(new long[]{0x0000030000000000L});
    public static final BitSet FOLLOW_41_in_ruleGroupGraphPatternSub2457 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_ruleGroupGraphPatternSub2471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTriplesSameSubject_in_entryRuleTriplesSameSubject2507 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTriplesSameSubject2517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGraphNode_in_ruleTriplesSameSubject2563 = new BitSet(new long[]{0x00000800006000B0L});
    public static final BitSet FOLLOW_rulePropertyList_in_ruleTriplesSameSubject2584 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_42_in_ruleTriplesSameSubject2597 = new BitSet(new long[]{0x00000800006000B0L});
    public static final BitSet FOLLOW_rulePropertyList_in_ruleTriplesSameSubject2618 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_rulePropertyList_in_entryRulePropertyList2656 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRulePropertyList2666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGraphNode_in_rulePropertyList2712 = new BitSet(new long[]{0x00000800006000B0L});
    public static final BitSet FOLLOW_ruleGraphNode_in_rulePropertyList2733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGraphNode_in_entryRuleGraphNode2769 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGraphNode2779 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_ruleGraphNode2825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleGraphNode2848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleIRI_in_ruleGraphNode2880 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_entryRuleVariable2916 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleVariable2926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUnNamedVariable_in_ruleVariable2972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleProperty_in_ruleVariable2999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUnNamedVariable_in_entryRuleUnNamedVariable3035 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleUnNamedVariable3045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_ruleUnNamedVariable3082 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleUnNamedVariable3099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleProperty_in_entryRuleProperty3140 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleProperty3150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleProperty3195 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleProperty3207 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleProperty3224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleIRI_in_entryRuleIRI3265 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleIRI3275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_IRI_TERMINAL_in_ruleIRI3326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_ruleOperator3385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_ruleOperator3402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_ruleOperator3419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_ruleOperator3436 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_ruleOperator3453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_ruleOperator3470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_ruleOperator3487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_ruleOperator3504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_ruleOperator3521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_ruleOperator3538 = new BitSet(new long[]{0x0000000000000002L});

}