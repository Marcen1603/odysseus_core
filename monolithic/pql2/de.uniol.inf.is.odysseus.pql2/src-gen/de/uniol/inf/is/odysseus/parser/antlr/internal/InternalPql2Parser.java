package de.uniol.inf.is.odysseus.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.uniol.inf.is.odysseus.services.Pql2GrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
@SuppressWarnings("all")
public class InternalPql2Parser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_DOUBLE", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'='", "':='", "'::='", "'('", "','", "')'", "':'", "'{'", "'}'", "'['", "']'"
    };
    public static final int RULE_STRING=7;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int RULE_DOUBLE=6;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int EOF=-1;
    public static final int RULE_ID=4;
    public static final int RULE_WS=10;
    public static final int RULE_ANY_OTHER=11;
    public static final int RULE_INT=5;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__20=20;
    public static final int T__21=21;

    // delegates
    // delegators


        public InternalPql2Parser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalPql2Parser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalPql2Parser.tokenNames; }
    public String getGrammarFileName() { return "../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g"; }



     	private Pql2GrammarAccess grammarAccess;
     	
        public InternalPql2Parser(TokenStream input, Pql2GrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "PQLModel";	
       	}
       	
       	@Override
       	protected Pql2GrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRulePQLModel"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:67:1: entryRulePQLModel returns [EObject current=null] : iv_rulePQLModel= rulePQLModel EOF ;
    public final EObject entryRulePQLModel() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePQLModel = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:68:2: (iv_rulePQLModel= rulePQLModel EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:69:2: iv_rulePQLModel= rulePQLModel EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getPQLModelRule()); 
            }
            pushFollow(FOLLOW_rulePQLModel_in_entryRulePQLModel75);
            iv_rulePQLModel=rulePQLModel();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_rulePQLModel; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRulePQLModel85); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePQLModel"


    // $ANTLR start "rulePQLModel"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:76:1: rulePQLModel returns [EObject current=null] : ( (lv_queries_0_0= ruleQuery ) )* ;
    public final EObject rulePQLModel() throws RecognitionException {
        EObject current = null;

        EObject lv_queries_0_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:79:28: ( ( (lv_queries_0_0= ruleQuery ) )* )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:80:1: ( (lv_queries_0_0= ruleQuery ) )*
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:80:1: ( (lv_queries_0_0= ruleQuery ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:81:1: (lv_queries_0_0= ruleQuery )
            	    {
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:81:1: (lv_queries_0_0= ruleQuery )
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:82:3: lv_queries_0_0= ruleQuery
            	    {
            	    if ( state.backtracking==0 ) {
            	       
            	      	        newCompositeNode(grammarAccess.getPQLModelAccess().getQueriesQueryParserRuleCall_0()); 
            	      	    
            	    }
            	    pushFollow(FOLLOW_ruleQuery_in_rulePQLModel130);
            	    lv_queries_0_0=ruleQuery();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      	        if (current==null) {
            	      	            current = createModelElementForParent(grammarAccess.getPQLModelRule());
            	      	        }
            	             		add(
            	             			current, 
            	             			"queries",
            	              		lv_queries_0_0, 
            	              		"Query");
            	      	        afterParserOrEnumRuleCall();
            	      	    
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePQLModel"


    // $ANTLR start "entryRuleQuery"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:106:1: entryRuleQuery returns [EObject current=null] : iv_ruleQuery= ruleQuery EOF ;
    public final EObject entryRuleQuery() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQuery = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:107:2: (iv_ruleQuery= ruleQuery EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:108:2: iv_ruleQuery= ruleQuery EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getQueryRule()); 
            }
            pushFollow(FOLLOW_ruleQuery_in_entryRuleQuery166);
            iv_ruleQuery=ruleQuery();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleQuery; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleQuery176); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQuery"


    // $ANTLR start "ruleQuery"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:115:1: ruleQuery returns [EObject current=null] : (this_TemporaryStream_0= ruleTemporaryStream | this_View_1= ruleView | this_SharedStream_2= ruleSharedStream ) ;
    public final EObject ruleQuery() throws RecognitionException {
        EObject current = null;

        EObject this_TemporaryStream_0 = null;

        EObject this_View_1 = null;

        EObject this_SharedStream_2 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:118:28: ( (this_TemporaryStream_0= ruleTemporaryStream | this_View_1= ruleView | this_SharedStream_2= ruleSharedStream ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:119:1: (this_TemporaryStream_0= ruleTemporaryStream | this_View_1= ruleView | this_SharedStream_2= ruleSharedStream )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:119:1: (this_TemporaryStream_0= ruleTemporaryStream | this_View_1= ruleView | this_SharedStream_2= ruleSharedStream )
            int alt2=3;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case 13:
                    {
                    alt2=2;
                    }
                    break;
                case 14:
                    {
                    alt2=3;
                    }
                    break;
                case 12:
                    {
                    alt2=1;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }

            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:120:5: this_TemporaryStream_0= ruleTemporaryStream
                    {
                    if ( state.backtracking==0 ) {
                       
                              newCompositeNode(grammarAccess.getQueryAccess().getTemporaryStreamParserRuleCall_0()); 
                          
                    }
                    pushFollow(FOLLOW_ruleTemporaryStream_in_ruleQuery223);
                    this_TemporaryStream_0=ruleTemporaryStream();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {
                       
                              current = this_TemporaryStream_0; 
                              afterParserOrEnumRuleCall();
                          
                    }

                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:130:5: this_View_1= ruleView
                    {
                    if ( state.backtracking==0 ) {
                       
                              newCompositeNode(grammarAccess.getQueryAccess().getViewParserRuleCall_1()); 
                          
                    }
                    pushFollow(FOLLOW_ruleView_in_ruleQuery250);
                    this_View_1=ruleView();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {
                       
                              current = this_View_1; 
                              afterParserOrEnumRuleCall();
                          
                    }

                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:140:5: this_SharedStream_2= ruleSharedStream
                    {
                    if ( state.backtracking==0 ) {
                       
                              newCompositeNode(grammarAccess.getQueryAccess().getSharedStreamParserRuleCall_2()); 
                          
                    }
                    pushFollow(FOLLOW_ruleSharedStream_in_ruleQuery277);
                    this_SharedStream_2=ruleSharedStream();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {
                       
                              current = this_SharedStream_2; 
                              afterParserOrEnumRuleCall();
                          
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQuery"


    // $ANTLR start "entryRuleTemporaryStream"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:156:1: entryRuleTemporaryStream returns [EObject current=null] : iv_ruleTemporaryStream= ruleTemporaryStream EOF ;
    public final EObject entryRuleTemporaryStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTemporaryStream = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:157:2: (iv_ruleTemporaryStream= ruleTemporaryStream EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:158:2: iv_ruleTemporaryStream= ruleTemporaryStream EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getTemporaryStreamRule()); 
            }
            pushFollow(FOLLOW_ruleTemporaryStream_in_entryRuleTemporaryStream312);
            iv_ruleTemporaryStream=ruleTemporaryStream();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleTemporaryStream; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleTemporaryStream322); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTemporaryStream"


    // $ANTLR start "ruleTemporaryStream"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:165:1: ruleTemporaryStream returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_op_2_0= ruleOperator ) ) ) ;
    public final EObject ruleTemporaryStream() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_op_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:168:28: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_op_2_0= ruleOperator ) ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:169:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_op_2_0= ruleOperator ) ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:169:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_op_2_0= ruleOperator ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:169:2: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_op_2_0= ruleOperator ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:169:2: ( (lv_name_0_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:170:1: (lv_name_0_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:170:1: (lv_name_0_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:171:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleTemporaryStream364); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(lv_name_0_0, grammarAccess.getTemporaryStreamAccess().getNameIDTerminalRuleCall_0_0()); 
              		
            }
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElement(grammarAccess.getTemporaryStreamRule());
              	        }
                     		setWithLastConsumed(
                     			current, 
                     			"name",
                      		lv_name_0_0, 
                      		"ID");
              	    
            }

            }


            }

            otherlv_1=(Token)match(input,12,FOLLOW_12_in_ruleTemporaryStream381); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_1, grammarAccess.getTemporaryStreamAccess().getEqualsSignKeyword_1());
                  
            }
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:191:1: ( (lv_op_2_0= ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:192:1: (lv_op_2_0= ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:192:1: (lv_op_2_0= ruleOperator )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:193:3: lv_op_2_0= ruleOperator
            {
            if ( state.backtracking==0 ) {
               
              	        newCompositeNode(grammarAccess.getTemporaryStreamAccess().getOpOperatorParserRuleCall_2_0()); 
              	    
            }
            pushFollow(FOLLOW_ruleOperator_in_ruleTemporaryStream402);
            lv_op_2_0=ruleOperator();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElementForParent(grammarAccess.getTemporaryStreamRule());
              	        }
                     		set(
                     			current, 
                     			"op",
                      		lv_op_2_0, 
                      		"Operator");
              	        afterParserOrEnumRuleCall();
              	    
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTemporaryStream"


    // $ANTLR start "entryRuleView"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:217:1: entryRuleView returns [EObject current=null] : iv_ruleView= ruleView EOF ;
    public final EObject entryRuleView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleView = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:218:2: (iv_ruleView= ruleView EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:219:2: iv_ruleView= ruleView EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getViewRule()); 
            }
            pushFollow(FOLLOW_ruleView_in_entryRuleView438);
            iv_ruleView=ruleView();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleView; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleView448); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleView"


    // $ANTLR start "ruleView"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:226:1: ruleView returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':=' ( (lv_op_2_0= ruleOperator ) ) ) ;
    public final EObject ruleView() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_op_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:229:28: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':=' ( (lv_op_2_0= ruleOperator ) ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:230:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':=' ( (lv_op_2_0= ruleOperator ) ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:230:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':=' ( (lv_op_2_0= ruleOperator ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:230:2: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':=' ( (lv_op_2_0= ruleOperator ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:230:2: ( (lv_name_0_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:231:1: (lv_name_0_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:231:1: (lv_name_0_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:232:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleView490); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(lv_name_0_0, grammarAccess.getViewAccess().getNameIDTerminalRuleCall_0_0()); 
              		
            }
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElement(grammarAccess.getViewRule());
              	        }
                     		setWithLastConsumed(
                     			current, 
                     			"name",
                      		lv_name_0_0, 
                      		"ID");
              	    
            }

            }


            }

            otherlv_1=(Token)match(input,13,FOLLOW_13_in_ruleView507); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_1, grammarAccess.getViewAccess().getColonEqualsSignKeyword_1());
                  
            }
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:252:1: ( (lv_op_2_0= ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:253:1: (lv_op_2_0= ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:253:1: (lv_op_2_0= ruleOperator )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:254:3: lv_op_2_0= ruleOperator
            {
            if ( state.backtracking==0 ) {
               
              	        newCompositeNode(grammarAccess.getViewAccess().getOpOperatorParserRuleCall_2_0()); 
              	    
            }
            pushFollow(FOLLOW_ruleOperator_in_ruleView528);
            lv_op_2_0=ruleOperator();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElementForParent(grammarAccess.getViewRule());
              	        }
                     		set(
                     			current, 
                     			"op",
                      		lv_op_2_0, 
                      		"Operator");
              	        afterParserOrEnumRuleCall();
              	    
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleView"


    // $ANTLR start "entryRuleSharedStream"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:278:1: entryRuleSharedStream returns [EObject current=null] : iv_ruleSharedStream= ruleSharedStream EOF ;
    public final EObject entryRuleSharedStream() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSharedStream = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:279:2: (iv_ruleSharedStream= ruleSharedStream EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:280:2: iv_ruleSharedStream= ruleSharedStream EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getSharedStreamRule()); 
            }
            pushFollow(FOLLOW_ruleSharedStream_in_entryRuleSharedStream564);
            iv_ruleSharedStream=ruleSharedStream();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleSharedStream; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleSharedStream574); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSharedStream"


    // $ANTLR start "ruleSharedStream"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:287:1: ruleSharedStream returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '::=' ( (lv_op_2_0= ruleOperator ) ) ) ;
    public final EObject ruleSharedStream() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_op_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:290:28: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '::=' ( (lv_op_2_0= ruleOperator ) ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:291:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '::=' ( (lv_op_2_0= ruleOperator ) ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:291:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '::=' ( (lv_op_2_0= ruleOperator ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:291:2: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '::=' ( (lv_op_2_0= ruleOperator ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:291:2: ( (lv_name_0_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:292:1: (lv_name_0_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:292:1: (lv_name_0_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:293:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSharedStream616); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(lv_name_0_0, grammarAccess.getSharedStreamAccess().getNameIDTerminalRuleCall_0_0()); 
              		
            }
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElement(grammarAccess.getSharedStreamRule());
              	        }
                     		setWithLastConsumed(
                     			current, 
                     			"name",
                      		lv_name_0_0, 
                      		"ID");
              	    
            }

            }


            }

            otherlv_1=(Token)match(input,14,FOLLOW_14_in_ruleSharedStream633); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_1, grammarAccess.getSharedStreamAccess().getColonColonEqualsSignKeyword_1());
                  
            }
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:313:1: ( (lv_op_2_0= ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:314:1: (lv_op_2_0= ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:314:1: (lv_op_2_0= ruleOperator )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:315:3: lv_op_2_0= ruleOperator
            {
            if ( state.backtracking==0 ) {
               
              	        newCompositeNode(grammarAccess.getSharedStreamAccess().getOpOperatorParserRuleCall_2_0()); 
              	    
            }
            pushFollow(FOLLOW_ruleOperator_in_ruleSharedStream654);
            lv_op_2_0=ruleOperator();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElementForParent(grammarAccess.getSharedStreamRule());
              	        }
                     		set(
                     			current, 
                     			"op",
                      		lv_op_2_0, 
                      		"Operator");
              	        afterParserOrEnumRuleCall();
              	    
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSharedStream"


    // $ANTLR start "entryRuleOperator"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:339:1: entryRuleOperator returns [EObject current=null] : iv_ruleOperator= ruleOperator EOF ;
    public final EObject entryRuleOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOperator = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:340:2: (iv_ruleOperator= ruleOperator EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:341:2: iv_ruleOperator= ruleOperator EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOperatorRule()); 
            }
            pushFollow(FOLLOW_ruleOperator_in_entryRuleOperator690);
            iv_ruleOperator=ruleOperator();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOperator; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperator700); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOperator"


    // $ANTLR start "ruleOperator"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:348:1: ruleOperator returns [EObject current=null] : ( ( (lv_operatorType_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_operators_2_0= ruleOperatorList ) ) | ( ( (lv_parameters_3_0= ruleParameterList ) ) (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )? ) )? otherlv_6= ')' ) ;
    public final EObject ruleOperator() throws RecognitionException {
        EObject current = null;

        Token lv_operatorType_0_0=null;
        Token otherlv_1=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_operators_2_0 = null;

        EObject lv_parameters_3_0 = null;

        EObject lv_operators_5_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:351:28: ( ( ( (lv_operatorType_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_operators_2_0= ruleOperatorList ) ) | ( ( (lv_parameters_3_0= ruleParameterList ) ) (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )? ) )? otherlv_6= ')' ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:352:1: ( ( (lv_operatorType_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_operators_2_0= ruleOperatorList ) ) | ( ( (lv_parameters_3_0= ruleParameterList ) ) (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )? ) )? otherlv_6= ')' )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:352:1: ( ( (lv_operatorType_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_operators_2_0= ruleOperatorList ) ) | ( ( (lv_parameters_3_0= ruleParameterList ) ) (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )? ) )? otherlv_6= ')' )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:352:2: ( (lv_operatorType_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_operators_2_0= ruleOperatorList ) ) | ( ( (lv_parameters_3_0= ruleParameterList ) ) (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )? ) )? otherlv_6= ')'
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:352:2: ( (lv_operatorType_0_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:353:1: (lv_operatorType_0_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:353:1: (lv_operatorType_0_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:354:3: lv_operatorType_0_0= RULE_ID
            {
            lv_operatorType_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleOperator742); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(lv_operatorType_0_0, grammarAccess.getOperatorAccess().getOperatorTypeIDTerminalRuleCall_0_0()); 
              		
            }
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElement(grammarAccess.getOperatorRule());
              	        }
                     		setWithLastConsumed(
                     			current, 
                     			"operatorType",
                      		lv_operatorType_0_0, 
                      		"ID");
              	    
            }

            }


            }

            otherlv_1=(Token)match(input,15,FOLLOW_15_in_ruleOperator759); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_1, grammarAccess.getOperatorAccess().getLeftParenthesisKeyword_1());
                  
            }
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:374:1: ( ( (lv_operators_2_0= ruleOperatorList ) ) | ( ( (lv_parameters_3_0= ruleParameterList ) ) (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )? ) )?
            int alt4=3;
            int LA4_0 = input.LA(1);

            if ( ((LA4_0>=RULE_ID && LA4_0<=RULE_INT)) ) {
                alt4=1;
            }
            else if ( (LA4_0==19) ) {
                alt4=2;
            }
            switch (alt4) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:374:2: ( (lv_operators_2_0= ruleOperatorList ) )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:374:2: ( (lv_operators_2_0= ruleOperatorList ) )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:375:1: (lv_operators_2_0= ruleOperatorList )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:375:1: (lv_operators_2_0= ruleOperatorList )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:376:3: lv_operators_2_0= ruleOperatorList
                    {
                    if ( state.backtracking==0 ) {
                       
                      	        newCompositeNode(grammarAccess.getOperatorAccess().getOperatorsOperatorListParserRuleCall_2_0_0()); 
                      	    
                    }
                    pushFollow(FOLLOW_ruleOperatorList_in_ruleOperator781);
                    lv_operators_2_0=ruleOperatorList();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      	        if (current==null) {
                      	            current = createModelElementForParent(grammarAccess.getOperatorRule());
                      	        }
                             		set(
                             			current, 
                             			"operators",
                              		lv_operators_2_0, 
                              		"OperatorList");
                      	        afterParserOrEnumRuleCall();
                      	    
                    }

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:393:6: ( ( (lv_parameters_3_0= ruleParameterList ) ) (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )? )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:393:6: ( ( (lv_parameters_3_0= ruleParameterList ) ) (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )? )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:393:7: ( (lv_parameters_3_0= ruleParameterList ) ) (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )?
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:393:7: ( (lv_parameters_3_0= ruleParameterList ) )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:394:1: (lv_parameters_3_0= ruleParameterList )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:394:1: (lv_parameters_3_0= ruleParameterList )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:395:3: lv_parameters_3_0= ruleParameterList
                    {
                    if ( state.backtracking==0 ) {
                       
                      	        newCompositeNode(grammarAccess.getOperatorAccess().getParametersParameterListParserRuleCall_2_1_0_0()); 
                      	    
                    }
                    pushFollow(FOLLOW_ruleParameterList_in_ruleOperator809);
                    lv_parameters_3_0=ruleParameterList();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      	        if (current==null) {
                      	            current = createModelElementForParent(grammarAccess.getOperatorRule());
                      	        }
                             		set(
                             			current, 
                             			"parameters",
                              		lv_parameters_3_0, 
                              		"ParameterList");
                      	        afterParserOrEnumRuleCall();
                      	    
                    }

                    }


                    }

                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:411:2: (otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) ) )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==16) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:411:4: otherlv_4= ',' ( (lv_operators_5_0= ruleOperatorList ) )
                            {
                            otherlv_4=(Token)match(input,16,FOLLOW_16_in_ruleOperator822); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                                  	newLeafNode(otherlv_4, grammarAccess.getOperatorAccess().getCommaKeyword_2_1_1_0());
                                  
                            }
                            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:415:1: ( (lv_operators_5_0= ruleOperatorList ) )
                            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:416:1: (lv_operators_5_0= ruleOperatorList )
                            {
                            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:416:1: (lv_operators_5_0= ruleOperatorList )
                            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:417:3: lv_operators_5_0= ruleOperatorList
                            {
                            if ( state.backtracking==0 ) {
                               
                              	        newCompositeNode(grammarAccess.getOperatorAccess().getOperatorsOperatorListParserRuleCall_2_1_1_1_0()); 
                              	    
                            }
                            pushFollow(FOLLOW_ruleOperatorList_in_ruleOperator843);
                            lv_operators_5_0=ruleOperatorList();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              	        if (current==null) {
                              	            current = createModelElementForParent(grammarAccess.getOperatorRule());
                              	        }
                                     		set(
                                     			current, 
                                     			"operators",
                                      		lv_operators_5_0, 
                                      		"OperatorList");
                              	        afterParserOrEnumRuleCall();
                              	    
                            }

                            }


                            }


                            }
                            break;

                    }


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,17,FOLLOW_17_in_ruleOperator860); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_6, grammarAccess.getOperatorAccess().getRightParenthesisKeyword_3());
                  
            }

            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
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


    // $ANTLR start "entryRuleOperatorList"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:445:1: entryRuleOperatorList returns [EObject current=null] : iv_ruleOperatorList= ruleOperatorList EOF ;
    public final EObject entryRuleOperatorList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOperatorList = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:446:2: (iv_ruleOperatorList= ruleOperatorList EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:447:2: iv_ruleOperatorList= ruleOperatorList EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOperatorListRule()); 
            }
            pushFollow(FOLLOW_ruleOperatorList_in_entryRuleOperatorList896);
            iv_ruleOperatorList=ruleOperatorList();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOperatorList; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperatorList906); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOperatorList"


    // $ANTLR start "ruleOperatorList"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:454:1: ruleOperatorList returns [EObject current=null] : ( ( (lv_elements_0_0= ruleOperatorOrQuery ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleOperatorOrQuery ) ) )* ) ;
    public final EObject ruleOperatorList() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_elements_0_0 = null;

        EObject lv_elements_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:457:28: ( ( ( (lv_elements_0_0= ruleOperatorOrQuery ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleOperatorOrQuery ) ) )* ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:458:1: ( ( (lv_elements_0_0= ruleOperatorOrQuery ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleOperatorOrQuery ) ) )* )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:458:1: ( ( (lv_elements_0_0= ruleOperatorOrQuery ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleOperatorOrQuery ) ) )* )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:458:2: ( (lv_elements_0_0= ruleOperatorOrQuery ) ) (otherlv_1= ',' ( (lv_elements_2_0= ruleOperatorOrQuery ) ) )*
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:458:2: ( (lv_elements_0_0= ruleOperatorOrQuery ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:459:1: (lv_elements_0_0= ruleOperatorOrQuery )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:459:1: (lv_elements_0_0= ruleOperatorOrQuery )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:460:3: lv_elements_0_0= ruleOperatorOrQuery
            {
            if ( state.backtracking==0 ) {
               
              	        newCompositeNode(grammarAccess.getOperatorListAccess().getElementsOperatorOrQueryParserRuleCall_0_0()); 
              	    
            }
            pushFollow(FOLLOW_ruleOperatorOrQuery_in_ruleOperatorList952);
            lv_elements_0_0=ruleOperatorOrQuery();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElementForParent(grammarAccess.getOperatorListRule());
              	        }
                     		add(
                     			current, 
                     			"elements",
                      		lv_elements_0_0, 
                      		"OperatorOrQuery");
              	        afterParserOrEnumRuleCall();
              	    
            }

            }


            }

            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:476:2: (otherlv_1= ',' ( (lv_elements_2_0= ruleOperatorOrQuery ) ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==16) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:476:4: otherlv_1= ',' ( (lv_elements_2_0= ruleOperatorOrQuery ) )
            	    {
            	    otherlv_1=(Token)match(input,16,FOLLOW_16_in_ruleOperatorList965); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	          	newLeafNode(otherlv_1, grammarAccess.getOperatorListAccess().getCommaKeyword_1_0());
            	          
            	    }
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:480:1: ( (lv_elements_2_0= ruleOperatorOrQuery ) )
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:481:1: (lv_elements_2_0= ruleOperatorOrQuery )
            	    {
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:481:1: (lv_elements_2_0= ruleOperatorOrQuery )
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:482:3: lv_elements_2_0= ruleOperatorOrQuery
            	    {
            	    if ( state.backtracking==0 ) {
            	       
            	      	        newCompositeNode(grammarAccess.getOperatorListAccess().getElementsOperatorOrQueryParserRuleCall_1_1_0()); 
            	      	    
            	    }
            	    pushFollow(FOLLOW_ruleOperatorOrQuery_in_ruleOperatorList986);
            	    lv_elements_2_0=ruleOperatorOrQuery();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      	        if (current==null) {
            	      	            current = createModelElementForParent(grammarAccess.getOperatorListRule());
            	      	        }
            	             		add(
            	             			current, 
            	             			"elements",
            	              		lv_elements_2_0, 
            	              		"OperatorOrQuery");
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

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOperatorList"


    // $ANTLR start "entryRuleOperatorOrQuery"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:506:1: entryRuleOperatorOrQuery returns [EObject current=null] : iv_ruleOperatorOrQuery= ruleOperatorOrQuery EOF ;
    public final EObject entryRuleOperatorOrQuery() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOperatorOrQuery = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:507:2: (iv_ruleOperatorOrQuery= ruleOperatorOrQuery EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:508:2: iv_ruleOperatorOrQuery= ruleOperatorOrQuery EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOperatorOrQueryRule()); 
            }
            pushFollow(FOLLOW_ruleOperatorOrQuery_in_entryRuleOperatorOrQuery1024);
            iv_ruleOperatorOrQuery=ruleOperatorOrQuery();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOperatorOrQuery; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperatorOrQuery1034); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOperatorOrQuery"


    // $ANTLR start "ruleOperatorOrQuery"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:515:1: ruleOperatorOrQuery returns [EObject current=null] : ( ( ( (lv_outputPort_0_0= RULE_INT ) ) otherlv_1= ':' )? ( ( (lv_op_2_0= ruleOperator ) ) | ( (otherlv_3= RULE_ID ) ) ) ) ;
    public final EObject ruleOperatorOrQuery() throws RecognitionException {
        EObject current = null;

        Token lv_outputPort_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_op_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:518:28: ( ( ( ( (lv_outputPort_0_0= RULE_INT ) ) otherlv_1= ':' )? ( ( (lv_op_2_0= ruleOperator ) ) | ( (otherlv_3= RULE_ID ) ) ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:519:1: ( ( ( (lv_outputPort_0_0= RULE_INT ) ) otherlv_1= ':' )? ( ( (lv_op_2_0= ruleOperator ) ) | ( (otherlv_3= RULE_ID ) ) ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:519:1: ( ( ( (lv_outputPort_0_0= RULE_INT ) ) otherlv_1= ':' )? ( ( (lv_op_2_0= ruleOperator ) ) | ( (otherlv_3= RULE_ID ) ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:519:2: ( ( (lv_outputPort_0_0= RULE_INT ) ) otherlv_1= ':' )? ( ( (lv_op_2_0= ruleOperator ) ) | ( (otherlv_3= RULE_ID ) ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:519:2: ( ( (lv_outputPort_0_0= RULE_INT ) ) otherlv_1= ':' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==RULE_INT) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:519:3: ( (lv_outputPort_0_0= RULE_INT ) ) otherlv_1= ':'
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:519:3: ( (lv_outputPort_0_0= RULE_INT ) )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:520:1: (lv_outputPort_0_0= RULE_INT )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:520:1: (lv_outputPort_0_0= RULE_INT )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:521:3: lv_outputPort_0_0= RULE_INT
                    {
                    lv_outputPort_0_0=(Token)match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleOperatorOrQuery1077); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			newLeafNode(lv_outputPort_0_0, grammarAccess.getOperatorOrQueryAccess().getOutputPortINTTerminalRuleCall_0_0_0()); 
                      		
                    }
                    if ( state.backtracking==0 ) {

                      	        if (current==null) {
                      	            current = createModelElement(grammarAccess.getOperatorOrQueryRule());
                      	        }
                             		setWithLastConsumed(
                             			current, 
                             			"outputPort",
                              		lv_outputPort_0_0, 
                              		"INT");
                      	    
                    }

                    }


                    }

                    otherlv_1=(Token)match(input,18,FOLLOW_18_in_ruleOperatorOrQuery1094); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                          	newLeafNode(otherlv_1, grammarAccess.getOperatorOrQueryAccess().getColonKeyword_0_1());
                          
                    }

                    }
                    break;

            }

            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:541:3: ( ( (lv_op_2_0= ruleOperator ) ) | ( (otherlv_3= RULE_ID ) ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==RULE_ID) ) {
                int LA7_1 = input.LA(2);

                if ( (LA7_1==15) ) {
                    alt7=1;
                }
                else if ( (LA7_1==EOF||(LA7_1>=16 && LA7_1<=17)) ) {
                    alt7=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:541:4: ( (lv_op_2_0= ruleOperator ) )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:541:4: ( (lv_op_2_0= ruleOperator ) )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:542:1: (lv_op_2_0= ruleOperator )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:542:1: (lv_op_2_0= ruleOperator )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:543:3: lv_op_2_0= ruleOperator
                    {
                    if ( state.backtracking==0 ) {
                       
                      	        newCompositeNode(grammarAccess.getOperatorOrQueryAccess().getOpOperatorParserRuleCall_1_0_0()); 
                      	    
                    }
                    pushFollow(FOLLOW_ruleOperator_in_ruleOperatorOrQuery1118);
                    lv_op_2_0=ruleOperator();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      	        if (current==null) {
                      	            current = createModelElementForParent(grammarAccess.getOperatorOrQueryRule());
                      	        }
                             		set(
                             			current, 
                             			"op",
                              		lv_op_2_0, 
                              		"Operator");
                      	        afterParserOrEnumRuleCall();
                      	    
                    }

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:560:6: ( (otherlv_3= RULE_ID ) )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:560:6: ( (otherlv_3= RULE_ID ) )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:561:1: (otherlv_3= RULE_ID )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:561:1: (otherlv_3= RULE_ID )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:562:3: otherlv_3= RULE_ID
                    {
                    if ( state.backtracking==0 ) {

                      			if (current==null) {
                      	            current = createModelElement(grammarAccess.getOperatorOrQueryRule());
                      	        }
                              
                    }
                    otherlv_3=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleOperatorOrQuery1144); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      		newLeafNode(otherlv_3, grammarAccess.getOperatorOrQueryAccess().getQueryQueryCrossReference_1_1_0()); 
                      	
                    }

                    }


                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOperatorOrQuery"


    // $ANTLR start "entryRuleParameterList"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:581:1: entryRuleParameterList returns [EObject current=null] : iv_ruleParameterList= ruleParameterList EOF ;
    public final EObject entryRuleParameterList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameterList = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:582:2: (iv_ruleParameterList= ruleParameterList EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:583:2: iv_ruleParameterList= ruleParameterList EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getParameterListRule()); 
            }
            pushFollow(FOLLOW_ruleParameterList_in_entryRuleParameterList1181);
            iv_ruleParameterList=ruleParameterList();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleParameterList; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleParameterList1191); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParameterList"


    // $ANTLR start "ruleParameterList"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:590:1: ruleParameterList returns [EObject current=null] : (otherlv_0= '{' ( (lv_elements_1_0= ruleParameter ) ) (otherlv_2= ',' ( (lv_elements_3_0= ruleParameter ) ) )* otherlv_4= '}' ) ;
    public final EObject ruleParameterList() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_elements_1_0 = null;

        EObject lv_elements_3_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:593:28: ( (otherlv_0= '{' ( (lv_elements_1_0= ruleParameter ) ) (otherlv_2= ',' ( (lv_elements_3_0= ruleParameter ) ) )* otherlv_4= '}' ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:594:1: (otherlv_0= '{' ( (lv_elements_1_0= ruleParameter ) ) (otherlv_2= ',' ( (lv_elements_3_0= ruleParameter ) ) )* otherlv_4= '}' )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:594:1: (otherlv_0= '{' ( (lv_elements_1_0= ruleParameter ) ) (otherlv_2= ',' ( (lv_elements_3_0= ruleParameter ) ) )* otherlv_4= '}' )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:594:3: otherlv_0= '{' ( (lv_elements_1_0= ruleParameter ) ) (otherlv_2= ',' ( (lv_elements_3_0= ruleParameter ) ) )* otherlv_4= '}'
            {
            otherlv_0=(Token)match(input,19,FOLLOW_19_in_ruleParameterList1228); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_0, grammarAccess.getParameterListAccess().getLeftCurlyBracketKeyword_0());
                  
            }
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:598:1: ( (lv_elements_1_0= ruleParameter ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:599:1: (lv_elements_1_0= ruleParameter )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:599:1: (lv_elements_1_0= ruleParameter )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:600:3: lv_elements_1_0= ruleParameter
            {
            if ( state.backtracking==0 ) {
               
              	        newCompositeNode(grammarAccess.getParameterListAccess().getElementsParameterParserRuleCall_1_0()); 
              	    
            }
            pushFollow(FOLLOW_ruleParameter_in_ruleParameterList1249);
            lv_elements_1_0=ruleParameter();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElementForParent(grammarAccess.getParameterListRule());
              	        }
                     		add(
                     			current, 
                     			"elements",
                      		lv_elements_1_0, 
                      		"Parameter");
              	        afterParserOrEnumRuleCall();
              	    
            }

            }


            }

            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:616:2: (otherlv_2= ',' ( (lv_elements_3_0= ruleParameter ) ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==16) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:616:4: otherlv_2= ',' ( (lv_elements_3_0= ruleParameter ) )
            	    {
            	    otherlv_2=(Token)match(input,16,FOLLOW_16_in_ruleParameterList1262); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	          	newLeafNode(otherlv_2, grammarAccess.getParameterListAccess().getCommaKeyword_2_0());
            	          
            	    }
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:620:1: ( (lv_elements_3_0= ruleParameter ) )
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:621:1: (lv_elements_3_0= ruleParameter )
            	    {
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:621:1: (lv_elements_3_0= ruleParameter )
            	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:622:3: lv_elements_3_0= ruleParameter
            	    {
            	    if ( state.backtracking==0 ) {
            	       
            	      	        newCompositeNode(grammarAccess.getParameterListAccess().getElementsParameterParserRuleCall_2_1_0()); 
            	      	    
            	    }
            	    pushFollow(FOLLOW_ruleParameter_in_ruleParameterList1283);
            	    lv_elements_3_0=ruleParameter();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      	        if (current==null) {
            	      	            current = createModelElementForParent(grammarAccess.getParameterListRule());
            	      	        }
            	             		add(
            	             			current, 
            	             			"elements",
            	              		lv_elements_3_0, 
            	              		"Parameter");
            	      	        afterParserOrEnumRuleCall();
            	      	    
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            otherlv_4=(Token)match(input,20,FOLLOW_20_in_ruleParameterList1297); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_4, grammarAccess.getParameterListAccess().getRightCurlyBracketKeyword_3());
                  
            }

            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParameterList"


    // $ANTLR start "entryRuleParameter"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:650:1: entryRuleParameter returns [EObject current=null] : iv_ruleParameter= ruleParameter EOF ;
    public final EObject entryRuleParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameter = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:651:2: (iv_ruleParameter= ruleParameter EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:652:2: iv_ruleParameter= ruleParameter EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getParameterRule()); 
            }
            pushFollow(FOLLOW_ruleParameter_in_entryRuleParameter1333);
            iv_ruleParameter=ruleParameter();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleParameter; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleParameter1343); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParameter"


    // $ANTLR start "ruleParameter"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:659:1: ruleParameter returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) ) ) ;
    public final EObject ruleParameter() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_value_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:662:28: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:663:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:663:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:663:2: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:663:2: ( (lv_name_0_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:664:1: (lv_name_0_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:664:1: (lv_name_0_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:665:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleParameter1385); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(lv_name_0_0, grammarAccess.getParameterAccess().getNameIDTerminalRuleCall_0_0()); 
              		
            }
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElement(grammarAccess.getParameterRule());
              	        }
                     		setWithLastConsumed(
                     			current, 
                     			"name",
                      		lv_name_0_0, 
                      		"ID");
              	    
            }

            }


            }

            otherlv_1=(Token)match(input,12,FOLLOW_12_in_ruleParameter1402); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_1, grammarAccess.getParameterAccess().getEqualsSignKeyword_1());
                  
            }
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:685:1: ( (lv_value_2_0= ruleParameterValue ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:686:1: (lv_value_2_0= ruleParameterValue )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:686:1: (lv_value_2_0= ruleParameterValue )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:687:3: lv_value_2_0= ruleParameterValue
            {
            if ( state.backtracking==0 ) {
               
              	        newCompositeNode(grammarAccess.getParameterAccess().getValueParameterValueParserRuleCall_2_0()); 
              	    
            }
            pushFollow(FOLLOW_ruleParameterValue_in_ruleParameter1423);
            lv_value_2_0=ruleParameterValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElementForParent(grammarAccess.getParameterRule());
              	        }
                     		set(
                     			current, 
                     			"value",
                      		lv_value_2_0, 
                      		"ParameterValue");
              	        afterParserOrEnumRuleCall();
              	    
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParameter"


    // $ANTLR start "entryRuleParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:711:1: entryRuleParameterValue returns [EObject current=null] : iv_ruleParameterValue= ruleParameterValue EOF ;
    public final EObject entryRuleParameterValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameterValue = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:712:2: (iv_ruleParameterValue= ruleParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:713:2: iv_ruleParameterValue= ruleParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleParameterValue_in_entryRuleParameterValue1459);
            iv_ruleParameterValue=ruleParameterValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleParameterValue; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleParameterValue1469); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParameterValue"


    // $ANTLR start "ruleParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:720:1: ruleParameterValue returns [EObject current=null] : (this_LongParameterValue_0= ruleLongParameterValue | this_DoubleParameterValue_1= ruleDoubleParameterValue | this_StringParameterValue_2= ruleStringParameterValue | ( ( ruleListParameterValue )=>this_ListParameterValue_3= ruleListParameterValue ) | this_MapParameterValue_4= ruleMapParameterValue ) ;
    public final EObject ruleParameterValue() throws RecognitionException {
        EObject current = null;

        EObject this_LongParameterValue_0 = null;

        EObject this_DoubleParameterValue_1 = null;

        EObject this_StringParameterValue_2 = null;

        EObject this_ListParameterValue_3 = null;

        EObject this_MapParameterValue_4 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:723:28: ( (this_LongParameterValue_0= ruleLongParameterValue | this_DoubleParameterValue_1= ruleDoubleParameterValue | this_StringParameterValue_2= ruleStringParameterValue | ( ( ruleListParameterValue )=>this_ListParameterValue_3= ruleListParameterValue ) | this_MapParameterValue_4= ruleMapParameterValue ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:724:1: (this_LongParameterValue_0= ruleLongParameterValue | this_DoubleParameterValue_1= ruleDoubleParameterValue | this_StringParameterValue_2= ruleStringParameterValue | ( ( ruleListParameterValue )=>this_ListParameterValue_3= ruleListParameterValue ) | this_MapParameterValue_4= ruleMapParameterValue )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:724:1: (this_LongParameterValue_0= ruleLongParameterValue | this_DoubleParameterValue_1= ruleDoubleParameterValue | this_StringParameterValue_2= ruleStringParameterValue | ( ( ruleListParameterValue )=>this_ListParameterValue_3= ruleListParameterValue ) | this_MapParameterValue_4= ruleMapParameterValue )
            int alt9=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt9=1;
                }
                break;
            case RULE_DOUBLE:
                {
                alt9=2;
                }
                break;
            case RULE_STRING:
                {
                alt9=3;
                }
                break;
            case 21:
                {
                int LA9_4 = input.LA(2);

                if ( (synpred1_InternalPql2()) ) {
                    alt9=4;
                }
                else if ( (true) ) {
                    alt9=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 4, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:725:5: this_LongParameterValue_0= ruleLongParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       
                              newCompositeNode(grammarAccess.getParameterValueAccess().getLongParameterValueParserRuleCall_0()); 
                          
                    }
                    pushFollow(FOLLOW_ruleLongParameterValue_in_ruleParameterValue1516);
                    this_LongParameterValue_0=ruleLongParameterValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {
                       
                              current = this_LongParameterValue_0; 
                              afterParserOrEnumRuleCall();
                          
                    }

                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:735:5: this_DoubleParameterValue_1= ruleDoubleParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       
                              newCompositeNode(grammarAccess.getParameterValueAccess().getDoubleParameterValueParserRuleCall_1()); 
                          
                    }
                    pushFollow(FOLLOW_ruleDoubleParameterValue_in_ruleParameterValue1543);
                    this_DoubleParameterValue_1=ruleDoubleParameterValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {
                       
                              current = this_DoubleParameterValue_1; 
                              afterParserOrEnumRuleCall();
                          
                    }

                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:745:5: this_StringParameterValue_2= ruleStringParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       
                              newCompositeNode(grammarAccess.getParameterValueAccess().getStringParameterValueParserRuleCall_2()); 
                          
                    }
                    pushFollow(FOLLOW_ruleStringParameterValue_in_ruleParameterValue1570);
                    this_StringParameterValue_2=ruleStringParameterValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {
                       
                              current = this_StringParameterValue_2; 
                              afterParserOrEnumRuleCall();
                          
                    }

                    }
                    break;
                case 4 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:754:6: ( ( ruleListParameterValue )=>this_ListParameterValue_3= ruleListParameterValue )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:754:6: ( ( ruleListParameterValue )=>this_ListParameterValue_3= ruleListParameterValue )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:754:7: ( ruleListParameterValue )=>this_ListParameterValue_3= ruleListParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       
                              newCompositeNode(grammarAccess.getParameterValueAccess().getListParameterValueParserRuleCall_3()); 
                          
                    }
                    pushFollow(FOLLOW_ruleListParameterValue_in_ruleParameterValue1603);
                    this_ListParameterValue_3=ruleListParameterValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {
                       
                              current = this_ListParameterValue_3; 
                              afterParserOrEnumRuleCall();
                          
                    }

                    }


                    }
                    break;
                case 5 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:765:5: this_MapParameterValue_4= ruleMapParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       
                              newCompositeNode(grammarAccess.getParameterValueAccess().getMapParameterValueParserRuleCall_4()); 
                          
                    }
                    pushFollow(FOLLOW_ruleMapParameterValue_in_ruleParameterValue1631);
                    this_MapParameterValue_4=ruleMapParameterValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {
                       
                              current = this_MapParameterValue_4; 
                              afterParserOrEnumRuleCall();
                          
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParameterValue"


    // $ANTLR start "entryRuleLongParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:781:1: entryRuleLongParameterValue returns [EObject current=null] : iv_ruleLongParameterValue= ruleLongParameterValue EOF ;
    public final EObject entryRuleLongParameterValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLongParameterValue = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:782:2: (iv_ruleLongParameterValue= ruleLongParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:783:2: iv_ruleLongParameterValue= ruleLongParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getLongParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleLongParameterValue_in_entryRuleLongParameterValue1666);
            iv_ruleLongParameterValue=ruleLongParameterValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleLongParameterValue; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleLongParameterValue1676); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleLongParameterValue"


    // $ANTLR start "ruleLongParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:790:1: ruleLongParameterValue returns [EObject current=null] : ( (lv_value_0_0= RULE_INT ) ) ;
    public final EObject ruleLongParameterValue() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:793:28: ( ( (lv_value_0_0= RULE_INT ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:794:1: ( (lv_value_0_0= RULE_INT ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:794:1: ( (lv_value_0_0= RULE_INT ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:795:1: (lv_value_0_0= RULE_INT )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:795:1: (lv_value_0_0= RULE_INT )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:796:3: lv_value_0_0= RULE_INT
            {
            lv_value_0_0=(Token)match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleLongParameterValue1717); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(lv_value_0_0, grammarAccess.getLongParameterValueAccess().getValueINTTerminalRuleCall_0()); 
              		
            }
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElement(grammarAccess.getLongParameterValueRule());
              	        }
                     		setWithLastConsumed(
                     			current, 
                     			"value",
                      		lv_value_0_0, 
                      		"INT");
              	    
            }

            }


            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleLongParameterValue"


    // $ANTLR start "entryRuleDoubleParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:820:1: entryRuleDoubleParameterValue returns [EObject current=null] : iv_ruleDoubleParameterValue= ruleDoubleParameterValue EOF ;
    public final EObject entryRuleDoubleParameterValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDoubleParameterValue = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:821:2: (iv_ruleDoubleParameterValue= ruleDoubleParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:822:2: iv_ruleDoubleParameterValue= ruleDoubleParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getDoubleParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleDoubleParameterValue_in_entryRuleDoubleParameterValue1757);
            iv_ruleDoubleParameterValue=ruleDoubleParameterValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleDoubleParameterValue; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleDoubleParameterValue1767); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDoubleParameterValue"


    // $ANTLR start "ruleDoubleParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:829:1: ruleDoubleParameterValue returns [EObject current=null] : ( (lv_value_0_0= RULE_DOUBLE ) ) ;
    public final EObject ruleDoubleParameterValue() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:832:28: ( ( (lv_value_0_0= RULE_DOUBLE ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:833:1: ( (lv_value_0_0= RULE_DOUBLE ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:833:1: ( (lv_value_0_0= RULE_DOUBLE ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:834:1: (lv_value_0_0= RULE_DOUBLE )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:834:1: (lv_value_0_0= RULE_DOUBLE )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:835:3: lv_value_0_0= RULE_DOUBLE
            {
            lv_value_0_0=(Token)match(input,RULE_DOUBLE,FOLLOW_RULE_DOUBLE_in_ruleDoubleParameterValue1808); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(lv_value_0_0, grammarAccess.getDoubleParameterValueAccess().getValueDOUBLETerminalRuleCall_0()); 
              		
            }
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElement(grammarAccess.getDoubleParameterValueRule());
              	        }
                     		setWithLastConsumed(
                     			current, 
                     			"value",
                      		lv_value_0_0, 
                      		"DOUBLE");
              	    
            }

            }


            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDoubleParameterValue"


    // $ANTLR start "entryRuleStringParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:859:1: entryRuleStringParameterValue returns [EObject current=null] : iv_ruleStringParameterValue= ruleStringParameterValue EOF ;
    public final EObject entryRuleStringParameterValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStringParameterValue = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:860:2: (iv_ruleStringParameterValue= ruleStringParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:861:2: iv_ruleStringParameterValue= ruleStringParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getStringParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleStringParameterValue_in_entryRuleStringParameterValue1848);
            iv_ruleStringParameterValue=ruleStringParameterValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleStringParameterValue; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleStringParameterValue1858); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStringParameterValue"


    // $ANTLR start "ruleStringParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:868:1: ruleStringParameterValue returns [EObject current=null] : ( (lv_value_0_0= RULE_STRING ) ) ;
    public final EObject ruleStringParameterValue() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:871:28: ( ( (lv_value_0_0= RULE_STRING ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:872:1: ( (lv_value_0_0= RULE_STRING ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:872:1: ( (lv_value_0_0= RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:873:1: (lv_value_0_0= RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:873:1: (lv_value_0_0= RULE_STRING )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:874:3: lv_value_0_0= RULE_STRING
            {
            lv_value_0_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleStringParameterValue1899); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(lv_value_0_0, grammarAccess.getStringParameterValueAccess().getValueSTRINGTerminalRuleCall_0()); 
              		
            }
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElement(grammarAccess.getStringParameterValueRule());
              	        }
                     		setWithLastConsumed(
                     			current, 
                     			"value",
                      		lv_value_0_0, 
                      		"STRING");
              	    
            }

            }


            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStringParameterValue"


    // $ANTLR start "entryRuleListParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:898:1: entryRuleListParameterValue returns [EObject current=null] : iv_ruleListParameterValue= ruleListParameterValue EOF ;
    public final EObject entryRuleListParameterValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleListParameterValue = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:899:2: (iv_ruleListParameterValue= ruleListParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:900:2: iv_ruleListParameterValue= ruleListParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getListParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleListParameterValue_in_entryRuleListParameterValue1939);
            iv_ruleListParameterValue=ruleListParameterValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleListParameterValue; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleListParameterValue1949); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleListParameterValue"


    // $ANTLR start "ruleListParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:907:1: ruleListParameterValue returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleParameterValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleParameterValue ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleListParameterValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:910:28: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleParameterValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleParameterValue ) ) )* )? otherlv_5= ']' ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:911:1: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleParameterValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleParameterValue ) ) )* )? otherlv_5= ']' )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:911:1: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleParameterValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleParameterValue ) ) )* )? otherlv_5= ']' )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:911:2: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleParameterValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleParameterValue ) ) )* )? otherlv_5= ']'
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:911:2: ()
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:912:5: 
            {
            if ( state.backtracking==0 ) {

                      current = forceCreateModelElement(
                          grammarAccess.getListParameterValueAccess().getListAction_0(),
                          current);
                  
            }

            }

            otherlv_1=(Token)match(input,21,FOLLOW_21_in_ruleListParameterValue1995); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_1, grammarAccess.getListParameterValueAccess().getLeftSquareBracketKeyword_1());
                  
            }
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:921:1: ( ( (lv_elements_2_0= ruleParameterValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleParameterValue ) ) )* )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0>=RULE_INT && LA11_0<=RULE_STRING)||LA11_0==21) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:921:2: ( (lv_elements_2_0= ruleParameterValue ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleParameterValue ) ) )*
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:921:2: ( (lv_elements_2_0= ruleParameterValue ) )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:922:1: (lv_elements_2_0= ruleParameterValue )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:922:1: (lv_elements_2_0= ruleParameterValue )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:923:3: lv_elements_2_0= ruleParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       
                      	        newCompositeNode(grammarAccess.getListParameterValueAccess().getElementsParameterValueParserRuleCall_2_0_0()); 
                      	    
                    }
                    pushFollow(FOLLOW_ruleParameterValue_in_ruleListParameterValue2017);
                    lv_elements_2_0=ruleParameterValue();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      	        if (current==null) {
                      	            current = createModelElementForParent(grammarAccess.getListParameterValueRule());
                      	        }
                             		add(
                             			current, 
                             			"elements",
                              		lv_elements_2_0, 
                              		"ParameterValue");
                      	        afterParserOrEnumRuleCall();
                      	    
                    }

                    }


                    }

                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:939:2: (otherlv_3= ',' ( (lv_elements_4_0= ruleParameterValue ) ) )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==16) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:939:4: otherlv_3= ',' ( (lv_elements_4_0= ruleParameterValue ) )
                    	    {
                    	    otherlv_3=(Token)match(input,16,FOLLOW_16_in_ruleListParameterValue2030); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	          	newLeafNode(otherlv_3, grammarAccess.getListParameterValueAccess().getCommaKeyword_2_1_0());
                    	          
                    	    }
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:943:1: ( (lv_elements_4_0= ruleParameterValue ) )
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:944:1: (lv_elements_4_0= ruleParameterValue )
                    	    {
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:944:1: (lv_elements_4_0= ruleParameterValue )
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:945:3: lv_elements_4_0= ruleParameterValue
                    	    {
                    	    if ( state.backtracking==0 ) {
                    	       
                    	      	        newCompositeNode(grammarAccess.getListParameterValueAccess().getElementsParameterValueParserRuleCall_2_1_1_0()); 
                    	      	    
                    	    }
                    	    pushFollow(FOLLOW_ruleParameterValue_in_ruleListParameterValue2051);
                    	    lv_elements_4_0=ruleParameterValue();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      	        if (current==null) {
                    	      	            current = createModelElementForParent(grammarAccess.getListParameterValueRule());
                    	      	        }
                    	             		add(
                    	             			current, 
                    	             			"elements",
                    	              		lv_elements_4_0, 
                    	              		"ParameterValue");
                    	      	        afterParserOrEnumRuleCall();
                    	      	    
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,22,FOLLOW_22_in_ruleListParameterValue2067); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_5, grammarAccess.getListParameterValueAccess().getRightSquareBracketKeyword_3());
                  
            }

            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleListParameterValue"


    // $ANTLR start "entryRuleMapParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:973:1: entryRuleMapParameterValue returns [EObject current=null] : iv_ruleMapParameterValue= ruleMapParameterValue EOF ;
    public final EObject entryRuleMapParameterValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapParameterValue = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:974:2: (iv_ruleMapParameterValue= ruleMapParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:975:2: iv_ruleMapParameterValue= ruleMapParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getMapParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleMapParameterValue_in_entryRuleMapParameterValue2103);
            iv_ruleMapParameterValue=ruleMapParameterValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleMapParameterValue; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapParameterValue2113); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMapParameterValue"


    // $ANTLR start "ruleMapParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:982:1: ruleMapParameterValue returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleMapEntry ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleMapEntry ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleMapParameterValue() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:985:28: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleMapEntry ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleMapEntry ) ) )* )? otherlv_5= ']' ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:986:1: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleMapEntry ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleMapEntry ) ) )* )? otherlv_5= ']' )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:986:1: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleMapEntry ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleMapEntry ) ) )* )? otherlv_5= ']' )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:986:2: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleMapEntry ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleMapEntry ) ) )* )? otherlv_5= ']'
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:986:2: ()
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:987:5: 
            {
            if ( state.backtracking==0 ) {

                      current = forceCreateModelElement(
                          grammarAccess.getMapParameterValueAccess().getMapAction_0(),
                          current);
                  
            }

            }

            otherlv_1=(Token)match(input,21,FOLLOW_21_in_ruleMapParameterValue2159); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_1, grammarAccess.getMapParameterValueAccess().getLeftSquareBracketKeyword_1());
                  
            }
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:996:1: ( ( (lv_elements_2_0= ruleMapEntry ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleMapEntry ) ) )* )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( ((LA13_0>=RULE_INT && LA13_0<=RULE_STRING)||LA13_0==21) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:996:2: ( (lv_elements_2_0= ruleMapEntry ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleMapEntry ) ) )*
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:996:2: ( (lv_elements_2_0= ruleMapEntry ) )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:997:1: (lv_elements_2_0= ruleMapEntry )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:997:1: (lv_elements_2_0= ruleMapEntry )
                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:998:3: lv_elements_2_0= ruleMapEntry
                    {
                    if ( state.backtracking==0 ) {
                       
                      	        newCompositeNode(grammarAccess.getMapParameterValueAccess().getElementsMapEntryParserRuleCall_2_0_0()); 
                      	    
                    }
                    pushFollow(FOLLOW_ruleMapEntry_in_ruleMapParameterValue2181);
                    lv_elements_2_0=ruleMapEntry();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      	        if (current==null) {
                      	            current = createModelElementForParent(grammarAccess.getMapParameterValueRule());
                      	        }
                             		add(
                             			current, 
                             			"elements",
                              		lv_elements_2_0, 
                              		"MapEntry");
                      	        afterParserOrEnumRuleCall();
                      	    
                    }

                    }


                    }

                    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1014:2: (otherlv_3= ',' ( (lv_elements_4_0= ruleMapEntry ) ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==16) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1014:4: otherlv_3= ',' ( (lv_elements_4_0= ruleMapEntry ) )
                    	    {
                    	    otherlv_3=(Token)match(input,16,FOLLOW_16_in_ruleMapParameterValue2194); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	          	newLeafNode(otherlv_3, grammarAccess.getMapParameterValueAccess().getCommaKeyword_2_1_0());
                    	          
                    	    }
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1018:1: ( (lv_elements_4_0= ruleMapEntry ) )
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1019:1: (lv_elements_4_0= ruleMapEntry )
                    	    {
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1019:1: (lv_elements_4_0= ruleMapEntry )
                    	    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1020:3: lv_elements_4_0= ruleMapEntry
                    	    {
                    	    if ( state.backtracking==0 ) {
                    	       
                    	      	        newCompositeNode(grammarAccess.getMapParameterValueAccess().getElementsMapEntryParserRuleCall_2_1_1_0()); 
                    	      	    
                    	    }
                    	    pushFollow(FOLLOW_ruleMapEntry_in_ruleMapParameterValue2215);
                    	    lv_elements_4_0=ruleMapEntry();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      	        if (current==null) {
                    	      	            current = createModelElementForParent(grammarAccess.getMapParameterValueRule());
                    	      	        }
                    	             		add(
                    	             			current, 
                    	             			"elements",
                    	              		lv_elements_4_0, 
                    	              		"MapEntry");
                    	      	        afterParserOrEnumRuleCall();
                    	      	    
                    	    }

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

            otherlv_5=(Token)match(input,22,FOLLOW_22_in_ruleMapParameterValue2231); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_5, grammarAccess.getMapParameterValueAccess().getRightSquareBracketKeyword_3());
                  
            }

            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMapParameterValue"


    // $ANTLR start "entryRuleMapEntry"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1048:1: entryRuleMapEntry returns [EObject current=null] : iv_ruleMapEntry= ruleMapEntry EOF ;
    public final EObject entryRuleMapEntry() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapEntry = null;


        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1049:2: (iv_ruleMapEntry= ruleMapEntry EOF )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1050:2: iv_ruleMapEntry= ruleMapEntry EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getMapEntryRule()); 
            }
            pushFollow(FOLLOW_ruleMapEntry_in_entryRuleMapEntry2267);
            iv_ruleMapEntry=ruleMapEntry();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleMapEntry; 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapEntry2277); if (state.failed) return current;

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMapEntry"


    // $ANTLR start "ruleMapEntry"
    // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1057:1: ruleMapEntry returns [EObject current=null] : ( ( (lv_key_0_0= ruleParameterValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) ) ) ;
    public final EObject ruleMapEntry() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_key_0_0 = null;

        EObject lv_value_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1060:28: ( ( ( (lv_key_0_0= ruleParameterValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1061:1: ( ( (lv_key_0_0= ruleParameterValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1061:1: ( ( (lv_key_0_0= ruleParameterValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1061:2: ( (lv_key_0_0= ruleParameterValue ) ) otherlv_1= '=' ( (lv_value_2_0= ruleParameterValue ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1061:2: ( (lv_key_0_0= ruleParameterValue ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1062:1: (lv_key_0_0= ruleParameterValue )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1062:1: (lv_key_0_0= ruleParameterValue )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1063:3: lv_key_0_0= ruleParameterValue
            {
            if ( state.backtracking==0 ) {
               
              	        newCompositeNode(grammarAccess.getMapEntryAccess().getKeyParameterValueParserRuleCall_0_0()); 
              	    
            }
            pushFollow(FOLLOW_ruleParameterValue_in_ruleMapEntry2323);
            lv_key_0_0=ruleParameterValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElementForParent(grammarAccess.getMapEntryRule());
              	        }
                     		set(
                     			current, 
                     			"key",
                      		lv_key_0_0, 
                      		"ParameterValue");
              	        afterParserOrEnumRuleCall();
              	    
            }

            }


            }

            otherlv_1=(Token)match(input,12,FOLLOW_12_in_ruleMapEntry2335); if (state.failed) return current;
            if ( state.backtracking==0 ) {

                  	newLeafNode(otherlv_1, grammarAccess.getMapEntryAccess().getEqualsSignKeyword_1());
                  
            }
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1083:1: ( (lv_value_2_0= ruleParameterValue ) )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1084:1: (lv_value_2_0= ruleParameterValue )
            {
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1084:1: (lv_value_2_0= ruleParameterValue )
            // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:1085:3: lv_value_2_0= ruleParameterValue
            {
            if ( state.backtracking==0 ) {
               
              	        newCompositeNode(grammarAccess.getMapEntryAccess().getValueParameterValueParserRuleCall_2_0()); 
              	    
            }
            pushFollow(FOLLOW_ruleParameterValue_in_ruleMapEntry2356);
            lv_value_2_0=ruleParameterValue();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              	        if (current==null) {
              	            current = createModelElementForParent(grammarAccess.getMapEntryRule());
              	        }
                     		set(
                     			current, 
                     			"value",
                      		lv_value_2_0, 
                      		"ParameterValue");
              	        afterParserOrEnumRuleCall();
              	    
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {
               leaveRule(); 
            }
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMapEntry"

    // $ANTLR start synpred1_InternalPql2
    public final void synpred1_InternalPql2_fragment() throws RecognitionException {   
        // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:754:7: ( ruleListParameterValue )
        // ../de.uniol.inf.is.odysseus.pql2/src-gen/de/uniol/inf/is/odysseus/parser/antlr/internal/InternalPql2.g:754:9: ruleListParameterValue
        {
        pushFollow(FOLLOW_ruleListParameterValue_in_synpred1_InternalPql21587);
        ruleListParameterValue();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_InternalPql2

    // Delegated rules

    public final boolean synpred1_InternalPql2() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_InternalPql2_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_rulePQLModel_in_entryRulePQLModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRulePQLModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleQuery_in_rulePQLModel130 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_ruleQuery_in_entryRuleQuery166 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleQuery176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTemporaryStream_in_ruleQuery223 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleView_in_ruleQuery250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSharedStream_in_ruleQuery277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTemporaryStream_in_entryRuleTemporaryStream312 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTemporaryStream322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleTemporaryStream364 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleTemporaryStream381 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleTemporaryStream402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleView_in_entryRuleView438 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleView448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleView490 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleView507 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleView528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSharedStream_in_entryRuleSharedStream564 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSharedStream574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSharedStream616 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleSharedStream633 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleSharedStream654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_entryRuleOperator690 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperator700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleOperator742 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_ruleOperator759 = new BitSet(new long[]{0x00000000000A0030L});
    public static final BitSet FOLLOW_ruleOperatorList_in_ruleOperator781 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParameterList_in_ruleOperator809 = new BitSet(new long[]{0x0000000000030000L});
    public static final BitSet FOLLOW_16_in_ruleOperator822 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleOperatorList_in_ruleOperator843 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleOperator860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorList_in_entryRuleOperatorList896 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperatorList906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorOrQuery_in_ruleOperatorList952 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_16_in_ruleOperatorList965 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleOperatorOrQuery_in_ruleOperatorList986 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_ruleOperatorOrQuery_in_entryRuleOperatorOrQuery1024 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperatorOrQuery1034 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleOperatorOrQuery1077 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleOperatorOrQuery1094 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleOperatorOrQuery1118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleOperatorOrQuery1144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterList_in_entryRuleParameterList1181 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParameterList1191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_ruleParameterList1228 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleParameter_in_ruleParameterList1249 = new BitSet(new long[]{0x0000000000110000L});
    public static final BitSet FOLLOW_16_in_ruleParameterList1262 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleParameter_in_ruleParameterList1283 = new BitSet(new long[]{0x0000000000110000L});
    public static final BitSet FOLLOW_20_in_ruleParameterList1297 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameter_in_entryRuleParameter1333 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParameter1343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleParameter1385 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleParameter1402 = new BitSet(new long[]{0x00000000002000E0L});
    public static final BitSet FOLLOW_ruleParameterValue_in_ruleParameter1423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterValue_in_entryRuleParameterValue1459 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParameterValue1469 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLongParameterValue_in_ruleParameterValue1516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDoubleParameterValue_in_ruleParameterValue1543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStringParameterValue_in_ruleParameterValue1570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleListParameterValue_in_ruleParameterValue1603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapParameterValue_in_ruleParameterValue1631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLongParameterValue_in_entryRuleLongParameterValue1666 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLongParameterValue1676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleLongParameterValue1717 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDoubleParameterValue_in_entryRuleDoubleParameterValue1757 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDoubleParameterValue1767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_DOUBLE_in_ruleDoubleParameterValue1808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStringParameterValue_in_entryRuleStringParameterValue1848 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleStringParameterValue1858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleStringParameterValue1899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleListParameterValue_in_entryRuleListParameterValue1939 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleListParameterValue1949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_ruleListParameterValue1995 = new BitSet(new long[]{0x00000000006000E0L});
    public static final BitSet FOLLOW_ruleParameterValue_in_ruleListParameterValue2017 = new BitSet(new long[]{0x0000000000410000L});
    public static final BitSet FOLLOW_16_in_ruleListParameterValue2030 = new BitSet(new long[]{0x00000000002000E0L});
    public static final BitSet FOLLOW_ruleParameterValue_in_ruleListParameterValue2051 = new BitSet(new long[]{0x0000000000410000L});
    public static final BitSet FOLLOW_22_in_ruleListParameterValue2067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapParameterValue_in_entryRuleMapParameterValue2103 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapParameterValue2113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_ruleMapParameterValue2159 = new BitSet(new long[]{0x00000000006000E0L});
    public static final BitSet FOLLOW_ruleMapEntry_in_ruleMapParameterValue2181 = new BitSet(new long[]{0x0000000000410000L});
    public static final BitSet FOLLOW_16_in_ruleMapParameterValue2194 = new BitSet(new long[]{0x00000000002000E0L});
    public static final BitSet FOLLOW_ruleMapEntry_in_ruleMapParameterValue2215 = new BitSet(new long[]{0x0000000000410000L});
    public static final BitSet FOLLOW_22_in_ruleMapParameterValue2231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapEntry_in_entryRuleMapEntry2267 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapEntry2277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterValue_in_ruleMapEntry2323 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleMapEntry2335 = new BitSet(new long[]{0x00000000002000E0L});
    public static final BitSet FOLLOW_ruleParameterValue_in_ruleMapEntry2356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleListParameterValue_in_synpred1_InternalPql21587 = new BitSet(new long[]{0x0000000000000002L});

}