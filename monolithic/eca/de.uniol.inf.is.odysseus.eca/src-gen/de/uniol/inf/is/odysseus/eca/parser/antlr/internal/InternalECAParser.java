package de.uniol.inf.is.odysseus.eca.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.uniol.inf.is.odysseus.eca.services.ECAGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalECAParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_STRING", "RULE_DOUBLE", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'DEFINE CONSTANT'", "':'", "';'", "'DEFINE WINDOWSIZE'", "'DEFINE TIMEINTERVALL'", "'DEFINE EVENT'", "'WITH'", "'ON'", "'IF'", "'THEN'", "'AND'", "'${'", "'}'", "'!'", "'queryExists'", "'('", "')'", "'SYSTEM.'", "'GET'", "'prio'", "'MIN'", "'MAX'", "','", "'state'", "'='", "'TimerEvent'", "'QueryEvent'", "'curCPULoad'", "'curMEMLoad'", "'curNETLoad'", "'<'", "'>'", "'<='", "'>='"
    };
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int RULE_ID=4;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=5;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int RULE_STRING=6;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__37=37;
    public static final int RULE_DOUBLE=7;
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
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;

    // delegates
    // delegators


        public InternalECAParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalECAParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalECAParser.tokenNames; }
    public String getGrammarFileName() { return "../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g"; }



     	private ECAGrammarAccess grammarAccess;
     	
        public InternalECAParser(TokenStream input, ECAGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "Model";	
       	}
       	
       	@Override
       	protected ECAGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRuleModel"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:67:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:68:2: (iv_ruleModel= ruleModel EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:69:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_ruleModel_in_entryRuleModel75);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModel85); 

            }

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
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:76:1: ruleModel returns [EObject current=null] : ( ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) ) ) ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_constants_2_0 = null;

        EObject lv_defEvents_3_0 = null;

        EObject lv_windowSize_4_0 = null;

        EObject lv_timeIntervall_5_0 = null;

        EObject lv_rules_6_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:79:28: ( ( ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:80:1: ( ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:80:1: ( ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:82:1: ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:82:1: ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:83:2: ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?)
            {
             
            	  getUnorderedGroupHelper().enter(grammarAccess.getModelAccess().getUnorderedGroup());
            	
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:86:2: ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?)
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:87:3: ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:87:3: ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+
            int cnt5=0;
            loop5:
            do {
                int alt5=3;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==EOF) ) {
                    int LA5_1 = input.LA(2);

                    if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0) ) {
                        alt5=1;
                    }


                }
                else if ( (LA5_0==12|| LA5_0 >=15 && LA5_0<=17) && getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0) ) {
                    alt5=1;
                }
                else if ( LA5_0 ==19 && ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 1) || getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0) ) ) {
                    int LA5_3 = input.LA(2);

                    if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0) ) {
                        alt5=1;
                    }
                    else if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 1) ) {
                        alt5=2;
                    }


                }


                switch (alt5) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:89:4: ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) )
            	    {
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:89:4: ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:90:5: {...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0) ) {
            	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0)");
            	    }
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:90:100: ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:91:6: ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) )
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup(), 0);
            	    	 				
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:94:6: ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:94:7: {...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    }
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:94:16: ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:96:1: ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) )
            	    {
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:96:1: ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:97:2: ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* )
            	    {
            	     
            	    	  getUnorderedGroupHelper().enter(grammarAccess.getModelAccess().getUnorderedGroup_0());
            	    	
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:100:2: ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:101:3: ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )*
            	    {
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:101:3: ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )*
            	    loop3:
            	    do {
            	        int alt3=5;
            	        alt3 = dfa3.predict(input);
            	        switch (alt3) {
            	    	case 1 :
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:103:4: ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) )
            	    	    {
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:103:4: ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:104:5: {...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ )
            	    	    {
            	    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 0) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 0)");
            	    	    }
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:104:102: ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:105:6: ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+
            	    	    {
            	    	     
            	    	    	 				  getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup_0(), 0);
            	    	    	 				
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:108:6: ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+
            	    	    int cnt1=0;
            	    	    loop1:
            	    	    do {
            	    	        int alt1=2;
            	    	        int LA1_0 = input.LA(1);

            	    	        if ( (LA1_0==12) ) {
            	    	            int LA1_2 = input.LA(2);

            	    	            if ( ((true)) ) {
            	    	                alt1=1;
            	    	            }


            	    	        }


            	    	        switch (alt1) {
            	    	    	case 1 :
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:108:7: {...}? => ( (lv_constants_2_0= ruleConstant ) )
            	    	    	    {
            	    	    	    if ( !((true)) ) {
            	    	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    	    }
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:108:16: ( (lv_constants_2_0= ruleConstant ) )
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:109:1: (lv_constants_2_0= ruleConstant )
            	    	    	    {
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:109:1: (lv_constants_2_0= ruleConstant )
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:110:3: lv_constants_2_0= ruleConstant
            	    	    	    {
            	    	    	     
            	    	    	    	        newCompositeNode(grammarAccess.getModelAccess().getConstantsConstantParserRuleCall_0_0_0()); 
            	    	    	    	    
            	    	    	    pushFollow(FOLLOW_ruleConstant_in_ruleModel220);
            	    	    	    lv_constants_2_0=ruleConstant();

            	    	    	    state._fsp--;


            	    	    	    	        if (current==null) {
            	    	    	    	            current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    	    	        }
            	    	    	           		add(
            	    	    	           			current, 
            	    	    	           			"constants",
            	    	    	            		lv_constants_2_0, 
            	    	    	            		"Constant");
            	    	    	    	        afterParserOrEnumRuleCall();
            	    	    	    	    

            	    	    	    }


            	    	    	    }


            	    	    	    }
            	    	    	    break;

            	    	    	default :
            	    	    	    if ( cnt1 >= 1 ) break loop1;
            	    	                EarlyExitException eee =
            	    	                    new EarlyExitException(1, input);
            	    	                throw eee;
            	    	        }
            	    	        cnt1++;
            	    	    } while (true);

            	    	     
            	    	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getModelAccess().getUnorderedGroup_0());
            	    	    	 				

            	    	    }


            	    	    }


            	    	    }
            	    	    break;
            	    	case 2 :
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:133:4: ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) )
            	    	    {
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:133:4: ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:134:5: {...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ )
            	    	    {
            	    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 1) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 1)");
            	    	    }
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:134:102: ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:135:6: ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+
            	    	    {
            	    	     
            	    	    	 				  getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup_0(), 1);
            	    	    	 				
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:138:6: ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+
            	    	    int cnt2=0;
            	    	    loop2:
            	    	    do {
            	    	        int alt2=2;
            	    	        int LA2_0 = input.LA(1);

            	    	        if ( (LA2_0==17) ) {
            	    	            int LA2_2 = input.LA(2);

            	    	            if ( ((true)) ) {
            	    	                alt2=1;
            	    	            }


            	    	        }


            	    	        switch (alt2) {
            	    	    	case 1 :
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:138:7: {...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) )
            	    	    	    {
            	    	    	    if ( !((true)) ) {
            	    	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    	    }
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:138:16: ( (lv_defEvents_3_0= ruleDefinedEvent ) )
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:139:1: (lv_defEvents_3_0= ruleDefinedEvent )
            	    	    	    {
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:139:1: (lv_defEvents_3_0= ruleDefinedEvent )
            	    	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:140:3: lv_defEvents_3_0= ruleDefinedEvent
            	    	    	    {
            	    	    	     
            	    	    	    	        newCompositeNode(grammarAccess.getModelAccess().getDefEventsDefinedEventParserRuleCall_0_1_0()); 
            	    	    	    	    
            	    	    	    pushFollow(FOLLOW_ruleDefinedEvent_in_ruleModel296);
            	    	    	    lv_defEvents_3_0=ruleDefinedEvent();

            	    	    	    state._fsp--;


            	    	    	    	        if (current==null) {
            	    	    	    	            current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    	    	        }
            	    	    	           		add(
            	    	    	           			current, 
            	    	    	           			"defEvents",
            	    	    	            		lv_defEvents_3_0, 
            	    	    	            		"DefinedEvent");
            	    	    	    	        afterParserOrEnumRuleCall();
            	    	    	    	    

            	    	    	    }


            	    	    	    }


            	    	    	    }
            	    	    	    break;

            	    	    	default :
            	    	    	    if ( cnt2 >= 1 ) break loop2;
            	    	                EarlyExitException eee =
            	    	                    new EarlyExitException(2, input);
            	    	                throw eee;
            	    	        }
            	    	        cnt2++;
            	    	    } while (true);

            	    	     
            	    	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getModelAccess().getUnorderedGroup_0());
            	    	    	 				

            	    	    }


            	    	    }


            	    	    }
            	    	    break;
            	    	case 3 :
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:163:4: ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) )
            	    	    {
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:163:4: ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:164:5: {...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) )
            	    	    {
            	    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 2) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 2)");
            	    	    }
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:164:102: ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:165:6: ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) )
            	    	    {
            	    	     
            	    	    	 				  getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup_0(), 2);
            	    	    	 				
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:168:6: ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:168:7: {...}? => ( (lv_windowSize_4_0= ruleWindow ) )
            	    	    {
            	    	    if ( !((true)) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    }
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:168:16: ( (lv_windowSize_4_0= ruleWindow ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:169:1: (lv_windowSize_4_0= ruleWindow )
            	    	    {
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:169:1: (lv_windowSize_4_0= ruleWindow )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:170:3: lv_windowSize_4_0= ruleWindow
            	    	    {
            	    	     
            	    	    	        newCompositeNode(grammarAccess.getModelAccess().getWindowSizeWindowParserRuleCall_0_2_0()); 
            	    	    	    
            	    	    pushFollow(FOLLOW_ruleWindow_in_ruleModel372);
            	    	    lv_windowSize_4_0=ruleWindow();

            	    	    state._fsp--;


            	    	    	        if (current==null) {
            	    	    	            current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    	        }
            	    	           		set(
            	    	           			current, 
            	    	           			"windowSize",
            	    	            		lv_windowSize_4_0, 
            	    	            		"Window");
            	    	    	        afterParserOrEnumRuleCall();
            	    	    	    

            	    	    }


            	    	    }


            	    	    }

            	    	     
            	    	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getModelAccess().getUnorderedGroup_0());
            	    	    	 				

            	    	    }


            	    	    }


            	    	    }
            	    	    break;
            	    	case 4 :
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:193:4: ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) )
            	    	    {
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:193:4: ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:194:5: {...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) )
            	    	    {
            	    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 3) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 3)");
            	    	    }
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:194:102: ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:195:6: ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) )
            	    	    {
            	    	     
            	    	    	 				  getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup_0(), 3);
            	    	    	 				
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:198:6: ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:198:7: {...}? => ( (lv_timeIntervall_5_0= ruleTimer ) )
            	    	    {
            	    	    if ( !((true)) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    }
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:198:16: ( (lv_timeIntervall_5_0= ruleTimer ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:199:1: (lv_timeIntervall_5_0= ruleTimer )
            	    	    {
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:199:1: (lv_timeIntervall_5_0= ruleTimer )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:200:3: lv_timeIntervall_5_0= ruleTimer
            	    	    {
            	    	     
            	    	    	        newCompositeNode(grammarAccess.getModelAccess().getTimeIntervallTimerParserRuleCall_0_3_0()); 
            	    	    	    
            	    	    pushFollow(FOLLOW_ruleTimer_in_ruleModel447);
            	    	    lv_timeIntervall_5_0=ruleTimer();

            	    	    state._fsp--;


            	    	    	        if (current==null) {
            	    	    	            current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    	        }
            	    	           		set(
            	    	           			current, 
            	    	           			"timeIntervall",
            	    	            		lv_timeIntervall_5_0, 
            	    	            		"Timer");
            	    	    	        afterParserOrEnumRuleCall();
            	    	    	    

            	    	    }


            	    	    }


            	    	    }

            	    	     
            	    	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getModelAccess().getUnorderedGroup_0());
            	    	    	 				

            	    	    }


            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop3;
            	        }
            	    } while (true);


            	    }


            	    }

            	     
            	    	  getUnorderedGroupHelper().leave(grammarAccess.getModelAccess().getUnorderedGroup_0());
            	    	

            	    }


            	    }

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getModelAccess().getUnorderedGroup());
            	    	 				

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:237:4: ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) )
            	    {
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:237:4: ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:238:5: {...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 1) ) {
            	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 1)");
            	    }
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:238:100: ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:239:6: ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+
            	    {
            	     
            	    	 				  getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup(), 1);
            	    	 				
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:242:6: ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+
            	    int cnt4=0;
            	    loop4:
            	    do {
            	        int alt4=2;
            	        int LA4_0 = input.LA(1);

            	        if ( (LA4_0==19) ) {
            	            int LA4_2 = input.LA(2);

            	            if ( ((true)) ) {
            	                alt4=1;
            	            }


            	        }


            	        switch (alt4) {
            	    	case 1 :
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:242:7: {...}? => ( (lv_rules_6_0= ruleRule ) )
            	    	    {
            	    	    if ( !((true)) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    }
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:242:16: ( (lv_rules_6_0= ruleRule ) )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:243:1: (lv_rules_6_0= ruleRule )
            	    	    {
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:243:1: (lv_rules_6_0= ruleRule )
            	    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:244:3: lv_rules_6_0= ruleRule
            	    	    {
            	    	     
            	    	    	        newCompositeNode(grammarAccess.getModelAccess().getRulesRuleParserRuleCall_1_0()); 
            	    	    	    
            	    	    pushFollow(FOLLOW_ruleRule_in_ruleModel562);
            	    	    lv_rules_6_0=ruleRule();

            	    	    state._fsp--;


            	    	    	        if (current==null) {
            	    	    	            current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    	        }
            	    	           		add(
            	    	           			current, 
            	    	           			"rules",
            	    	            		lv_rules_6_0, 
            	    	            		"Rule");
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

            	     
            	    	 				  getUnorderedGroupHelper().returnFromSelection(grammarAccess.getModelAccess().getUnorderedGroup());
            	    	 				

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

            if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getModelAccess().getUnorderedGroup()) ) {
                throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canLeave(grammarAccess.getModelAccess().getUnorderedGroup())");
            }

            }


            }

             
            	  getUnorderedGroupHelper().leave(grammarAccess.getModelAccess().getUnorderedGroup());
            	

            }


            }

             leaveRule(); 
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


    // $ANTLR start "entryRuleConstant"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:283:1: entryRuleConstant returns [EObject current=null] : iv_ruleConstant= ruleConstant EOF ;
    public final EObject entryRuleConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstant = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:284:2: (iv_ruleConstant= ruleConstant EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:285:2: iv_ruleConstant= ruleConstant EOF
            {
             newCompositeNode(grammarAccess.getConstantRule()); 
            pushFollow(FOLLOW_ruleConstant_in_entryRuleConstant644);
            iv_ruleConstant=ruleConstant();

            state._fsp--;

             current =iv_ruleConstant; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleConstant654); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleConstant"


    // $ANTLR start "ruleConstant"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:292:1: ruleConstant returns [EObject current=null] : (otherlv_0= 'DEFINE CONSTANT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_constValue_3_0= RULE_INT ) ) otherlv_4= ';' ) ;
    public final EObject ruleConstant() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token lv_constValue_3_0=null;
        Token otherlv_4=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:295:28: ( (otherlv_0= 'DEFINE CONSTANT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_constValue_3_0= RULE_INT ) ) otherlv_4= ';' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:296:1: (otherlv_0= 'DEFINE CONSTANT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_constValue_3_0= RULE_INT ) ) otherlv_4= ';' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:296:1: (otherlv_0= 'DEFINE CONSTANT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_constValue_3_0= RULE_INT ) ) otherlv_4= ';' )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:296:3: otherlv_0= 'DEFINE CONSTANT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_constValue_3_0= RULE_INT ) ) otherlv_4= ';'
            {
            otherlv_0=(Token)match(input,12,FOLLOW_12_in_ruleConstant691); 

                	newLeafNode(otherlv_0, grammarAccess.getConstantAccess().getDEFINECONSTANTKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:300:1: ( (lv_name_1_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:301:1: (lv_name_1_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:301:1: (lv_name_1_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:302:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleConstant708); 

            			newLeafNode(lv_name_1_0, grammarAccess.getConstantAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getConstantRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,13,FOLLOW_13_in_ruleConstant725); 

                	newLeafNode(otherlv_2, grammarAccess.getConstantAccess().getColonKeyword_2());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:322:1: ( (lv_constValue_3_0= RULE_INT ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:323:1: (lv_constValue_3_0= RULE_INT )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:323:1: (lv_constValue_3_0= RULE_INT )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:324:3: lv_constValue_3_0= RULE_INT
            {
            lv_constValue_3_0=(Token)match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleConstant742); 

            			newLeafNode(lv_constValue_3_0, grammarAccess.getConstantAccess().getConstValueINTTerminalRuleCall_3_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getConstantRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"constValue",
                    		lv_constValue_3_0, 
                    		"INT");
            	    

            }


            }

            otherlv_4=(Token)match(input,14,FOLLOW_14_in_ruleConstant759); 

                	newLeafNode(otherlv_4, grammarAccess.getConstantAccess().getSemicolonKeyword_4());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConstant"


    // $ANTLR start "entryRuleWindow"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:352:1: entryRuleWindow returns [EObject current=null] : iv_ruleWindow= ruleWindow EOF ;
    public final EObject entryRuleWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:353:2: (iv_ruleWindow= ruleWindow EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:354:2: iv_ruleWindow= ruleWindow EOF
            {
             newCompositeNode(grammarAccess.getWindowRule()); 
            pushFollow(FOLLOW_ruleWindow_in_entryRuleWindow795);
            iv_ruleWindow=ruleWindow();

            state._fsp--;

             current =iv_ruleWindow; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleWindow805); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWindow"


    // $ANTLR start "ruleWindow"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:361:1: ruleWindow returns [EObject current=null] : (otherlv_0= 'DEFINE WINDOWSIZE' otherlv_1= ':' ( (lv_windowValue_2_0= RULE_INT ) ) otherlv_3= ';' ) ;
    public final EObject ruleWindow() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_windowValue_2_0=null;
        Token otherlv_3=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:364:28: ( (otherlv_0= 'DEFINE WINDOWSIZE' otherlv_1= ':' ( (lv_windowValue_2_0= RULE_INT ) ) otherlv_3= ';' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:365:1: (otherlv_0= 'DEFINE WINDOWSIZE' otherlv_1= ':' ( (lv_windowValue_2_0= RULE_INT ) ) otherlv_3= ';' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:365:1: (otherlv_0= 'DEFINE WINDOWSIZE' otherlv_1= ':' ( (lv_windowValue_2_0= RULE_INT ) ) otherlv_3= ';' )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:365:3: otherlv_0= 'DEFINE WINDOWSIZE' otherlv_1= ':' ( (lv_windowValue_2_0= RULE_INT ) ) otherlv_3= ';'
            {
            otherlv_0=(Token)match(input,15,FOLLOW_15_in_ruleWindow842); 

                	newLeafNode(otherlv_0, grammarAccess.getWindowAccess().getDEFINEWINDOWSIZEKeyword_0());
                
            otherlv_1=(Token)match(input,13,FOLLOW_13_in_ruleWindow854); 

                	newLeafNode(otherlv_1, grammarAccess.getWindowAccess().getColonKeyword_1());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:373:1: ( (lv_windowValue_2_0= RULE_INT ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:374:1: (lv_windowValue_2_0= RULE_INT )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:374:1: (lv_windowValue_2_0= RULE_INT )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:375:3: lv_windowValue_2_0= RULE_INT
            {
            lv_windowValue_2_0=(Token)match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleWindow871); 

            			newLeafNode(lv_windowValue_2_0, grammarAccess.getWindowAccess().getWindowValueINTTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getWindowRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"windowValue",
                    		lv_windowValue_2_0, 
                    		"INT");
            	    

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_14_in_ruleWindow888); 

                	newLeafNode(otherlv_3, grammarAccess.getWindowAccess().getSemicolonKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWindow"


    // $ANTLR start "entryRuleTimer"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:403:1: entryRuleTimer returns [EObject current=null] : iv_ruleTimer= ruleTimer EOF ;
    public final EObject entryRuleTimer() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimer = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:404:2: (iv_ruleTimer= ruleTimer EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:405:2: iv_ruleTimer= ruleTimer EOF
            {
             newCompositeNode(grammarAccess.getTimerRule()); 
            pushFollow(FOLLOW_ruleTimer_in_entryRuleTimer924);
            iv_ruleTimer=ruleTimer();

            state._fsp--;

             current =iv_ruleTimer; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTimer934); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTimer"


    // $ANTLR start "ruleTimer"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:412:1: ruleTimer returns [EObject current=null] : (otherlv_0= 'DEFINE TIMEINTERVALL' otherlv_1= ':' ( (lv_timerIntervallValue_2_0= RULE_INT ) ) otherlv_3= ';' ) ;
    public final EObject ruleTimer() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_timerIntervallValue_2_0=null;
        Token otherlv_3=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:415:28: ( (otherlv_0= 'DEFINE TIMEINTERVALL' otherlv_1= ':' ( (lv_timerIntervallValue_2_0= RULE_INT ) ) otherlv_3= ';' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:416:1: (otherlv_0= 'DEFINE TIMEINTERVALL' otherlv_1= ':' ( (lv_timerIntervallValue_2_0= RULE_INT ) ) otherlv_3= ';' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:416:1: (otherlv_0= 'DEFINE TIMEINTERVALL' otherlv_1= ':' ( (lv_timerIntervallValue_2_0= RULE_INT ) ) otherlv_3= ';' )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:416:3: otherlv_0= 'DEFINE TIMEINTERVALL' otherlv_1= ':' ( (lv_timerIntervallValue_2_0= RULE_INT ) ) otherlv_3= ';'
            {
            otherlv_0=(Token)match(input,16,FOLLOW_16_in_ruleTimer971); 

                	newLeafNode(otherlv_0, grammarAccess.getTimerAccess().getDEFINETIMEINTERVALLKeyword_0());
                
            otherlv_1=(Token)match(input,13,FOLLOW_13_in_ruleTimer983); 

                	newLeafNode(otherlv_1, grammarAccess.getTimerAccess().getColonKeyword_1());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:424:1: ( (lv_timerIntervallValue_2_0= RULE_INT ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:425:1: (lv_timerIntervallValue_2_0= RULE_INT )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:425:1: (lv_timerIntervallValue_2_0= RULE_INT )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:426:3: lv_timerIntervallValue_2_0= RULE_INT
            {
            lv_timerIntervallValue_2_0=(Token)match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleTimer1000); 

            			newLeafNode(lv_timerIntervallValue_2_0, grammarAccess.getTimerAccess().getTimerIntervallValueINTTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getTimerRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"timerIntervallValue",
                    		lv_timerIntervallValue_2_0, 
                    		"INT");
            	    

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_14_in_ruleTimer1017); 

                	newLeafNode(otherlv_3, grammarAccess.getTimerAccess().getSemicolonKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTimer"


    // $ANTLR start "entryRuleDefinedEvent"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:454:1: entryRuleDefinedEvent returns [EObject current=null] : iv_ruleDefinedEvent= ruleDefinedEvent EOF ;
    public final EObject entryRuleDefinedEvent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDefinedEvent = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:455:2: (iv_ruleDefinedEvent= ruleDefinedEvent EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:456:2: iv_ruleDefinedEvent= ruleDefinedEvent EOF
            {
             newCompositeNode(grammarAccess.getDefinedEventRule()); 
            pushFollow(FOLLOW_ruleDefinedEvent_in_entryRuleDefinedEvent1053);
            iv_ruleDefinedEvent=ruleDefinedEvent();

            state._fsp--;

             current =iv_ruleDefinedEvent; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDefinedEvent1063); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDefinedEvent"


    // $ANTLR start "ruleDefinedEvent"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:463:1: ruleDefinedEvent returns [EObject current=null] : (otherlv_0= 'DEFINE EVENT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_definedSource_3_0= ruleSource ) ) otherlv_4= 'WITH' ( (lv_definedAttribute_5_0= RULE_ID ) ) ( (lv_definedOperator_6_0= ruleOperator ) ) ( (lv_definedValue_7_0= ruleEcaValue ) ) otherlv_8= ';' ) ;
    public final EObject ruleDefinedEvent() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token lv_definedAttribute_5_0=null;
        Token otherlv_8=null;
        EObject lv_definedSource_3_0 = null;

        AntlrDatatypeRuleToken lv_definedOperator_6_0 = null;

        EObject lv_definedValue_7_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:466:28: ( (otherlv_0= 'DEFINE EVENT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_definedSource_3_0= ruleSource ) ) otherlv_4= 'WITH' ( (lv_definedAttribute_5_0= RULE_ID ) ) ( (lv_definedOperator_6_0= ruleOperator ) ) ( (lv_definedValue_7_0= ruleEcaValue ) ) otherlv_8= ';' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:467:1: (otherlv_0= 'DEFINE EVENT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_definedSource_3_0= ruleSource ) ) otherlv_4= 'WITH' ( (lv_definedAttribute_5_0= RULE_ID ) ) ( (lv_definedOperator_6_0= ruleOperator ) ) ( (lv_definedValue_7_0= ruleEcaValue ) ) otherlv_8= ';' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:467:1: (otherlv_0= 'DEFINE EVENT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_definedSource_3_0= ruleSource ) ) otherlv_4= 'WITH' ( (lv_definedAttribute_5_0= RULE_ID ) ) ( (lv_definedOperator_6_0= ruleOperator ) ) ( (lv_definedValue_7_0= ruleEcaValue ) ) otherlv_8= ';' )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:467:3: otherlv_0= 'DEFINE EVENT' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_definedSource_3_0= ruleSource ) ) otherlv_4= 'WITH' ( (lv_definedAttribute_5_0= RULE_ID ) ) ( (lv_definedOperator_6_0= ruleOperator ) ) ( (lv_definedValue_7_0= ruleEcaValue ) ) otherlv_8= ';'
            {
            otherlv_0=(Token)match(input,17,FOLLOW_17_in_ruleDefinedEvent1100); 

                	newLeafNode(otherlv_0, grammarAccess.getDefinedEventAccess().getDEFINEEVENTKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:471:1: ( (lv_name_1_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:472:1: (lv_name_1_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:472:1: (lv_name_1_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:473:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleDefinedEvent1117); 

            			newLeafNode(lv_name_1_0, grammarAccess.getDefinedEventAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDefinedEventRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,13,FOLLOW_13_in_ruleDefinedEvent1134); 

                	newLeafNode(otherlv_2, grammarAccess.getDefinedEventAccess().getColonKeyword_2());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:493:1: ( (lv_definedSource_3_0= ruleSource ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:494:1: (lv_definedSource_3_0= ruleSource )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:494:1: (lv_definedSource_3_0= ruleSource )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:495:3: lv_definedSource_3_0= ruleSource
            {
             
            	        newCompositeNode(grammarAccess.getDefinedEventAccess().getDefinedSourceSourceParserRuleCall_3_0()); 
            	    
            pushFollow(FOLLOW_ruleSource_in_ruleDefinedEvent1155);
            lv_definedSource_3_0=ruleSource();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getDefinedEventRule());
            	        }
                   		set(
                   			current, 
                   			"definedSource",
                    		lv_definedSource_3_0, 
                    		"Source");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_4=(Token)match(input,18,FOLLOW_18_in_ruleDefinedEvent1167); 

                	newLeafNode(otherlv_4, grammarAccess.getDefinedEventAccess().getWITHKeyword_4());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:515:1: ( (lv_definedAttribute_5_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:516:1: (lv_definedAttribute_5_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:516:1: (lv_definedAttribute_5_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:517:3: lv_definedAttribute_5_0= RULE_ID
            {
            lv_definedAttribute_5_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleDefinedEvent1184); 

            			newLeafNode(lv_definedAttribute_5_0, grammarAccess.getDefinedEventAccess().getDefinedAttributeIDTerminalRuleCall_5_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDefinedEventRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"definedAttribute",
                    		lv_definedAttribute_5_0, 
                    		"ID");
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:533:2: ( (lv_definedOperator_6_0= ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:534:1: (lv_definedOperator_6_0= ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:534:1: (lv_definedOperator_6_0= ruleOperator )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:535:3: lv_definedOperator_6_0= ruleOperator
            {
             
            	        newCompositeNode(grammarAccess.getDefinedEventAccess().getDefinedOperatorOperatorParserRuleCall_6_0()); 
            	    
            pushFollow(FOLLOW_ruleOperator_in_ruleDefinedEvent1210);
            lv_definedOperator_6_0=ruleOperator();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getDefinedEventRule());
            	        }
                   		set(
                   			current, 
                   			"definedOperator",
                    		lv_definedOperator_6_0, 
                    		"Operator");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:551:2: ( (lv_definedValue_7_0= ruleEcaValue ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:552:1: (lv_definedValue_7_0= ruleEcaValue )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:552:1: (lv_definedValue_7_0= ruleEcaValue )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:553:3: lv_definedValue_7_0= ruleEcaValue
            {
             
            	        newCompositeNode(grammarAccess.getDefinedEventAccess().getDefinedValueEcaValueParserRuleCall_7_0()); 
            	    
            pushFollow(FOLLOW_ruleEcaValue_in_ruleDefinedEvent1231);
            lv_definedValue_7_0=ruleEcaValue();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getDefinedEventRule());
            	        }
                   		set(
                   			current, 
                   			"definedValue",
                    		lv_definedValue_7_0, 
                    		"EcaValue");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_8=(Token)match(input,14,FOLLOW_14_in_ruleDefinedEvent1243); 

                	newLeafNode(otherlv_8, grammarAccess.getDefinedEventAccess().getSemicolonKeyword_8());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDefinedEvent"


    // $ANTLR start "entryRuleRule"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:581:1: entryRuleRule returns [EObject current=null] : iv_ruleRule= ruleRule EOF ;
    public final EObject entryRuleRule() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRule = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:582:2: (iv_ruleRule= ruleRule EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:583:2: iv_ruleRule= ruleRule EOF
            {
             newCompositeNode(grammarAccess.getRuleRule()); 
            pushFollow(FOLLOW_ruleRule_in_entryRuleRule1279);
            iv_ruleRule=ruleRule();

            state._fsp--;

             current =iv_ruleRule; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleRule1289); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRule"


    // $ANTLR start "ruleRule"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:590:1: ruleRule returns [EObject current=null] : (otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';' ) ;
    public final EObject ruleRule() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        EObject lv_source_2_0 = null;

        EObject lv_ruleConditions_4_0 = null;

        EObject lv_ruleActions_6_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:593:28: ( (otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:594:1: (otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:594:1: (otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';' )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:594:3: otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';'
            {
            otherlv_0=(Token)match(input,19,FOLLOW_19_in_ruleRule1326); 

                	newLeafNode(otherlv_0, grammarAccess.getRuleAccess().getONKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:598:1: ( (lv_name_1_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:599:1: (lv_name_1_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:599:1: (lv_name_1_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:600:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleRule1343); 

            			newLeafNode(lv_name_1_0, grammarAccess.getRuleAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getRuleRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:616:2: ( (lv_source_2_0= ruleRuleSource ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:617:1: (lv_source_2_0= ruleRuleSource )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:617:1: (lv_source_2_0= ruleRuleSource )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:618:3: lv_source_2_0= ruleRuleSource
            {
             
            	        newCompositeNode(grammarAccess.getRuleAccess().getSourceRuleSourceParserRuleCall_2_0()); 
            	    
            pushFollow(FOLLOW_ruleRuleSource_in_ruleRule1369);
            lv_source_2_0=ruleRuleSource();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getRuleRule());
            	        }
                   		set(
                   			current, 
                   			"source",
                    		lv_source_2_0, 
                    		"RuleSource");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_3=(Token)match(input,20,FOLLOW_20_in_ruleRule1381); 

                	newLeafNode(otherlv_3, grammarAccess.getRuleAccess().getIFKeyword_3());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:638:1: ( (lv_ruleConditions_4_0= ruleCONDITIONS ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:639:1: (lv_ruleConditions_4_0= ruleCONDITIONS )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:639:1: (lv_ruleConditions_4_0= ruleCONDITIONS )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:640:3: lv_ruleConditions_4_0= ruleCONDITIONS
            {
             
            	        newCompositeNode(grammarAccess.getRuleAccess().getRuleConditionsCONDITIONSParserRuleCall_4_0()); 
            	    
            pushFollow(FOLLOW_ruleCONDITIONS_in_ruleRule1402);
            lv_ruleConditions_4_0=ruleCONDITIONS();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getRuleRule());
            	        }
                   		set(
                   			current, 
                   			"ruleConditions",
                    		lv_ruleConditions_4_0, 
                    		"CONDITIONS");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_5=(Token)match(input,21,FOLLOW_21_in_ruleRule1414); 

                	newLeafNode(otherlv_5, grammarAccess.getRuleAccess().getTHENKeyword_5());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:660:1: ( (lv_ruleActions_6_0= ruleACTIONS ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:661:1: (lv_ruleActions_6_0= ruleACTIONS )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:661:1: (lv_ruleActions_6_0= ruleACTIONS )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:662:3: lv_ruleActions_6_0= ruleACTIONS
            {
             
            	        newCompositeNode(grammarAccess.getRuleAccess().getRuleActionsACTIONSParserRuleCall_6_0()); 
            	    
            pushFollow(FOLLOW_ruleACTIONS_in_ruleRule1435);
            lv_ruleActions_6_0=ruleACTIONS();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getRuleRule());
            	        }
                   		set(
                   			current, 
                   			"ruleActions",
                    		lv_ruleActions_6_0, 
                    		"ACTIONS");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_7=(Token)match(input,14,FOLLOW_14_in_ruleRule1447); 

                	newLeafNode(otherlv_7, grammarAccess.getRuleAccess().getSemicolonKeyword_7());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRule"


    // $ANTLR start "entryRuleCONDITIONS"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:690:1: entryRuleCONDITIONS returns [EObject current=null] : iv_ruleCONDITIONS= ruleCONDITIONS EOF ;
    public final EObject entryRuleCONDITIONS() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCONDITIONS = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:691:2: (iv_ruleCONDITIONS= ruleCONDITIONS EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:692:2: iv_ruleCONDITIONS= ruleCONDITIONS EOF
            {
             newCompositeNode(grammarAccess.getCONDITIONSRule()); 
            pushFollow(FOLLOW_ruleCONDITIONS_in_entryRuleCONDITIONS1483);
            iv_ruleCONDITIONS=ruleCONDITIONS();

            state._fsp--;

             current =iv_ruleCONDITIONS; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleCONDITIONS1493); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCONDITIONS"


    // $ANTLR start "ruleCONDITIONS"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:699:1: ruleCONDITIONS returns [EObject current=null] : (this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )* ) ;
    public final EObject ruleCONDITIONS() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_SUBCONDITION_0 = null;

        EObject lv_right_3_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:702:28: ( (this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )* ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:703:1: (this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )* )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:703:1: (this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )* )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:704:5: this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )*
            {
             
                    newCompositeNode(grammarAccess.getCONDITIONSAccess().getSUBCONDITIONParserRuleCall_0()); 
                
            pushFollow(FOLLOW_ruleSUBCONDITION_in_ruleCONDITIONS1540);
            this_SUBCONDITION_0=ruleSUBCONDITION();

            state._fsp--;

             
                    current = this_SUBCONDITION_0; 
                    afterParserOrEnumRuleCall();
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:712:1: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==22) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:712:2: () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) )
            	    {
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:712:2: ()
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:713:5: 
            	    {

            	            current = forceCreateModelElementAndSet(
            	                grammarAccess.getCONDITIONSAccess().getCONDITIONSLeftAction_1_0(),
            	                current);
            	        

            	    }

            	    otherlv_2=(Token)match(input,22,FOLLOW_22_in_ruleCONDITIONS1561); 

            	        	newLeafNode(otherlv_2, grammarAccess.getCONDITIONSAccess().getANDKeyword_1_1());
            	        
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:722:1: ( (lv_right_3_0= ruleSUBCONDITION ) )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:723:1: (lv_right_3_0= ruleSUBCONDITION )
            	    {
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:723:1: (lv_right_3_0= ruleSUBCONDITION )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:724:3: lv_right_3_0= ruleSUBCONDITION
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getCONDITIONSAccess().getRightSUBCONDITIONParserRuleCall_1_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleSUBCONDITION_in_ruleCONDITIONS1582);
            	    lv_right_3_0=ruleSUBCONDITION();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getCONDITIONSRule());
            	    	        }
            	           		set(
            	           			current, 
            	           			"right",
            	            		lv_right_3_0, 
            	            		"SUBCONDITION");
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

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCONDITIONS"


    // $ANTLR start "entryRuleSUBCONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:748:1: entryRuleSUBCONDITION returns [EObject current=null] : iv_ruleSUBCONDITION= ruleSUBCONDITION EOF ;
    public final EObject entryRuleSUBCONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSUBCONDITION = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:749:2: (iv_ruleSUBCONDITION= ruleSUBCONDITION EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:750:2: iv_ruleSUBCONDITION= ruleSUBCONDITION EOF
            {
             newCompositeNode(grammarAccess.getSUBCONDITIONRule()); 
            pushFollow(FOLLOW_ruleSUBCONDITION_in_entryRuleSUBCONDITION1620);
            iv_ruleSUBCONDITION=ruleSUBCONDITION();

            state._fsp--;

             current =iv_ruleSUBCONDITION; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSUBCONDITION1630); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSUBCONDITION"


    // $ANTLR start "ruleSUBCONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:757:1: ruleSUBCONDITION returns [EObject current=null] : ( ( (lv_subsource_0_0= ruleSOURCECONDITION ) ) | ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) ) | ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) ) | ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? ) | ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) ) ) ;
    public final EObject ruleSUBCONDITION() throws RecognitionException {
        EObject current = null;

        EObject lv_subsource_0_0 = null;

        EObject lv_subsys_1_0 = null;

        EObject lv_subfree_3_0 = null;

        EObject lv_submap_5_0 = null;

        EObject lv_queryCond_7_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:760:28: ( ( ( (lv_subsource_0_0= ruleSOURCECONDITION ) ) | ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) ) | ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) ) | ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? ) | ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:761:1: ( ( (lv_subsource_0_0= ruleSOURCECONDITION ) ) | ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) ) | ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) ) | ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? ) | ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:761:1: ( ( (lv_subsource_0_0= ruleSOURCECONDITION ) ) | ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) ) | ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) ) | ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? ) | ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) ) )
            int alt8=5;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                alt8=1;
                }
                break;
            case 29:
                {
                alt8=2;
                }
                break;
            case RULE_STRING:
                {
                alt8=3;
                }
                break;
            case EOF:
            case 21:
            case 22:
            case 30:
                {
                alt8=4;
                }
                break;
            case 25:
            case 26:
                {
                alt8=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:761:2: ( (lv_subsource_0_0= ruleSOURCECONDITION ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:761:2: ( (lv_subsource_0_0= ruleSOURCECONDITION ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:762:1: (lv_subsource_0_0= ruleSOURCECONDITION )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:762:1: (lv_subsource_0_0= ruleSOURCECONDITION )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:763:3: lv_subsource_0_0= ruleSOURCECONDITION
                    {
                     
                    	        newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getSubsourceSOURCECONDITIONParserRuleCall_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleSOURCECONDITION_in_ruleSUBCONDITION1676);
                    lv_subsource_0_0=ruleSOURCECONDITION();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                    	        }
                           		set(
                           			current, 
                           			"subsource",
                            		lv_subsource_0_0, 
                            		"SOURCECONDITION");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:780:6: ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:780:6: ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:781:1: (lv_subsys_1_0= ruleSYSTEMCONDITION )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:781:1: (lv_subsys_1_0= ruleSYSTEMCONDITION )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:782:3: lv_subsys_1_0= ruleSYSTEMCONDITION
                    {
                     
                    	        newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getSubsysSYSTEMCONDITIONParserRuleCall_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleSYSTEMCONDITION_in_ruleSUBCONDITION1703);
                    lv_subsys_1_0=ruleSYSTEMCONDITION();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                    	        }
                           		set(
                           			current, 
                           			"subsys",
                            		lv_subsys_1_0, 
                            		"SYSTEMCONDITION");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:799:6: ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:799:6: ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:799:7: () ( (lv_subfree_3_0= ruleFREECONDITION ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:799:7: ()
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:800:5: 
                    {

                            current = forceCreateModelElement(
                                grammarAccess.getSUBCONDITIONAccess().getSUBCONDITIONAction_2_0(),
                                current);
                        

                    }

                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:805:2: ( (lv_subfree_3_0= ruleFREECONDITION ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:806:1: (lv_subfree_3_0= ruleFREECONDITION )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:806:1: (lv_subfree_3_0= ruleFREECONDITION )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:807:3: lv_subfree_3_0= ruleFREECONDITION
                    {
                     
                    	        newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getSubfreeFREECONDITIONParserRuleCall_2_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleFREECONDITION_in_ruleSUBCONDITION1740);
                    lv_subfree_3_0=ruleFREECONDITION();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                    	        }
                           		set(
                           			current, 
                           			"subfree",
                            		lv_subfree_3_0, 
                            		"FREECONDITION");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }


                    }
                    break;
                case 4 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:824:6: ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:824:6: ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:824:7: () ( (lv_submap_5_0= ruleMAPCONDITION ) )?
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:824:7: ()
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:825:5: 
                    {

                            current = forceCreateModelElement(
                                grammarAccess.getSUBCONDITIONAccess().getSUBCONDITIONAction_3_0(),
                                current);
                        

                    }

                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:830:2: ( (lv_submap_5_0= ruleMAPCONDITION ) )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==30) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:831:1: (lv_submap_5_0= ruleMAPCONDITION )
                            {
                            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:831:1: (lv_submap_5_0= ruleMAPCONDITION )
                            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:832:3: lv_submap_5_0= ruleMAPCONDITION
                            {
                             
                            	        newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getSubmapMAPCONDITIONParserRuleCall_3_1_0()); 
                            	    
                            pushFollow(FOLLOW_ruleMAPCONDITION_in_ruleSUBCONDITION1778);
                            lv_submap_5_0=ruleMAPCONDITION();

                            state._fsp--;


                            	        if (current==null) {
                            	            current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                            	        }
                                   		set(
                                   			current, 
                                   			"submap",
                                    		lv_submap_5_0, 
                                    		"MAPCONDITION");
                            	        afterParserOrEnumRuleCall();
                            	    

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 5 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:849:6: ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:849:6: ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:849:7: () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:849:7: ()
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:850:5: 
                    {

                            current = forceCreateModelElement(
                                grammarAccess.getSUBCONDITIONAccess().getSUBCONDITIONAction_4_0(),
                                current);
                        

                    }

                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:855:2: ( (lv_queryCond_7_0= ruleQUERYCONDITION ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:856:1: (lv_queryCond_7_0= ruleQUERYCONDITION )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:856:1: (lv_queryCond_7_0= ruleQUERYCONDITION )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:857:3: lv_queryCond_7_0= ruleQUERYCONDITION
                    {
                     
                    	        newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getQueryCondQUERYCONDITIONParserRuleCall_4_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleQUERYCONDITION_in_ruleSUBCONDITION1817);
                    lv_queryCond_7_0=ruleQUERYCONDITION();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                    	        }
                           		set(
                           			current, 
                           			"queryCond",
                            		lv_queryCond_7_0, 
                            		"QUERYCONDITION");
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
    // $ANTLR end "ruleSUBCONDITION"


    // $ANTLR start "entryRuleRuleSource"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:881:1: entryRuleRuleSource returns [EObject current=null] : iv_ruleRuleSource= ruleRuleSource EOF ;
    public final EObject entryRuleRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleSource = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:882:2: (iv_ruleRuleSource= ruleRuleSource EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:883:2: iv_ruleRuleSource= ruleRuleSource EOF
            {
             newCompositeNode(grammarAccess.getRuleSourceRule()); 
            pushFollow(FOLLOW_ruleRuleSource_in_entryRuleRuleSource1854);
            iv_ruleRuleSource=ruleRuleSource();

            state._fsp--;

             current =iv_ruleRuleSource; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleRuleSource1864); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRuleSource"


    // $ANTLR start "ruleRuleSource"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:890:1: ruleRuleSource returns [EObject current=null] : ( (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' ) | ( (lv_newSource_3_0= ruleSource ) ) | ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) ) ) ;
    public final EObject ruleRuleSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        EObject lv_newSource_3_0 = null;

        AntlrDatatypeRuleToken lv_preSource_4_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:893:28: ( ( (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' ) | ( (lv_newSource_3_0= ruleSource ) ) | ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:894:1: ( (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' ) | ( (lv_newSource_3_0= ruleSource ) ) | ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:894:1: ( (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' ) | ( (lv_newSource_3_0= ruleSource ) ) | ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) ) )
            int alt9=3;
            switch ( input.LA(1) ) {
            case 23:
                {
                alt9=1;
                }
                break;
            case RULE_ID:
                {
                alt9=2;
                }
                break;
            case 37:
            case 38:
                {
                alt9=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:894:2: (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:894:2: (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:894:4: otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}'
                    {
                    otherlv_0=(Token)match(input,23,FOLLOW_23_in_ruleRuleSource1902); 

                        	newLeafNode(otherlv_0, grammarAccess.getRuleSourceAccess().getDollarSignLeftCurlyBracketKeyword_0_0());
                        
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:898:1: ( (otherlv_1= RULE_ID ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:899:1: (otherlv_1= RULE_ID )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:899:1: (otherlv_1= RULE_ID )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:900:3: otherlv_1= RULE_ID
                    {

                    			if (current==null) {
                    	            current = createModelElement(grammarAccess.getRuleSourceRule());
                    	        }
                            
                    otherlv_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleRuleSource1922); 

                    		newLeafNode(otherlv_1, grammarAccess.getRuleSourceAccess().getDefSourceDefinedEventCrossReference_0_1_0()); 
                    	

                    }


                    }

                    otherlv_2=(Token)match(input,24,FOLLOW_24_in_ruleRuleSource1934); 

                        	newLeafNode(otherlv_2, grammarAccess.getRuleSourceAccess().getRightCurlyBracketKeyword_0_2());
                        

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:916:6: ( (lv_newSource_3_0= ruleSource ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:916:6: ( (lv_newSource_3_0= ruleSource ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:917:1: (lv_newSource_3_0= ruleSource )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:917:1: (lv_newSource_3_0= ruleSource )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:918:3: lv_newSource_3_0= ruleSource
                    {
                     
                    	        newCompositeNode(grammarAccess.getRuleSourceAccess().getNewSourceSourceParserRuleCall_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleSource_in_ruleRuleSource1962);
                    lv_newSource_3_0=ruleSource();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getRuleSourceRule());
                    	        }
                           		set(
                           			current, 
                           			"newSource",
                            		lv_newSource_3_0, 
                            		"Source");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:935:6: ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:935:6: ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:936:1: (lv_preSource_4_0= rulePREDEFINEDSOURCE )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:936:1: (lv_preSource_4_0= rulePREDEFINEDSOURCE )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:937:3: lv_preSource_4_0= rulePREDEFINEDSOURCE
                    {
                     
                    	        newCompositeNode(grammarAccess.getRuleSourceAccess().getPreSourcePREDEFINEDSOURCEParserRuleCall_2_0()); 
                    	    
                    pushFollow(FOLLOW_rulePREDEFINEDSOURCE_in_ruleRuleSource1989);
                    lv_preSource_4_0=rulePREDEFINEDSOURCE();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getRuleSourceRule());
                    	        }
                           		set(
                           			current, 
                           			"preSource",
                            		lv_preSource_4_0, 
                            		"PREDEFINEDSOURCE");
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
    // $ANTLR end "ruleRuleSource"


    // $ANTLR start "entryRuleSOURCECONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:961:1: entryRuleSOURCECONDITION returns [EObject current=null] : iv_ruleSOURCECONDITION= ruleSOURCECONDITION EOF ;
    public final EObject entryRuleSOURCECONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSOURCECONDITION = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:962:2: (iv_ruleSOURCECONDITION= ruleSOURCECONDITION EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:963:2: iv_ruleSOURCECONDITION= ruleSOURCECONDITION EOF
            {
             newCompositeNode(grammarAccess.getSOURCECONDITIONRule()); 
            pushFollow(FOLLOW_ruleSOURCECONDITION_in_entryRuleSOURCECONDITION2025);
            iv_ruleSOURCECONDITION=ruleSOURCECONDITION();

            state._fsp--;

             current =iv_ruleSOURCECONDITION; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSOURCECONDITION2035); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSOURCECONDITION"


    // $ANTLR start "ruleSOURCECONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:970:1: ruleSOURCECONDITION returns [EObject current=null] : ( ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) ) ) ;
    public final EObject ruleSOURCECONDITION() throws RecognitionException {
        EObject current = null;

        Token lv_condAttribute_0_0=null;
        AntlrDatatypeRuleToken lv_operator_1_0 = null;

        EObject lv_value_2_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:973:28: ( ( ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:974:1: ( ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:974:1: ( ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:974:2: ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:974:2: ( (lv_condAttribute_0_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:975:1: (lv_condAttribute_0_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:975:1: (lv_condAttribute_0_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:976:3: lv_condAttribute_0_0= RULE_ID
            {
            lv_condAttribute_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSOURCECONDITION2077); 

            			newLeafNode(lv_condAttribute_0_0, grammarAccess.getSOURCECONDITIONAccess().getCondAttributeIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSOURCECONDITIONRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"condAttribute",
                    		lv_condAttribute_0_0, 
                    		"ID");
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:992:2: ( (lv_operator_1_0= ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:993:1: (lv_operator_1_0= ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:993:1: (lv_operator_1_0= ruleOperator )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:994:3: lv_operator_1_0= ruleOperator
            {
             
            	        newCompositeNode(grammarAccess.getSOURCECONDITIONAccess().getOperatorOperatorParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleOperator_in_ruleSOURCECONDITION2103);
            lv_operator_1_0=ruleOperator();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSOURCECONDITIONRule());
            	        }
                   		set(
                   			current, 
                   			"operator",
                    		lv_operator_1_0, 
                    		"Operator");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1010:2: ( (lv_value_2_0= ruleEcaValue ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1011:1: (lv_value_2_0= ruleEcaValue )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1011:1: (lv_value_2_0= ruleEcaValue )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1012:3: lv_value_2_0= ruleEcaValue
            {
             
            	        newCompositeNode(grammarAccess.getSOURCECONDITIONAccess().getValueEcaValueParserRuleCall_2_0()); 
            	    
            pushFollow(FOLLOW_ruleEcaValue_in_ruleSOURCECONDITION2124);
            lv_value_2_0=ruleEcaValue();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSOURCECONDITIONRule());
            	        }
                   		set(
                   			current, 
                   			"value",
                    		lv_value_2_0, 
                    		"EcaValue");
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
    // $ANTLR end "ruleSOURCECONDITION"


    // $ANTLR start "entryRuleQUERYCONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1036:1: entryRuleQUERYCONDITION returns [EObject current=null] : iv_ruleQUERYCONDITION= ruleQUERYCONDITION EOF ;
    public final EObject entryRuleQUERYCONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQUERYCONDITION = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1037:2: (iv_ruleQUERYCONDITION= ruleQUERYCONDITION EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1038:2: iv_ruleQUERYCONDITION= ruleQUERYCONDITION EOF
            {
             newCompositeNode(grammarAccess.getQUERYCONDITIONRule()); 
            pushFollow(FOLLOW_ruleQUERYCONDITION_in_entryRuleQUERYCONDITION2160);
            iv_ruleQUERYCONDITION=ruleQUERYCONDITION();

            state._fsp--;

             current =iv_ruleQUERYCONDITION; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleQUERYCONDITION2170); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQUERYCONDITION"


    // $ANTLR start "ruleQUERYCONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1045:1: ruleQUERYCONDITION returns [EObject current=null] : ( ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')' ) ;
    public final EObject ruleQUERYCONDITION() throws RecognitionException {
        EObject current = null;

        Token lv_queryNot_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_queryFunct_3_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1048:28: ( ( ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1049:1: ( ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1049:1: ( ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')' )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1049:2: ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')'
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1049:2: ( (lv_queryNot_0_0= '!' ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==25) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1050:1: (lv_queryNot_0_0= '!' )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1050:1: (lv_queryNot_0_0= '!' )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1051:3: lv_queryNot_0_0= '!'
                    {
                    lv_queryNot_0_0=(Token)match(input,25,FOLLOW_25_in_ruleQUERYCONDITION2213); 

                            newLeafNode(lv_queryNot_0_0, grammarAccess.getQUERYCONDITIONAccess().getQueryNotExclamationMarkKeyword_0_0());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getQUERYCONDITIONRule());
                    	        }
                           		setWithLastConsumed(current, "queryNot", lv_queryNot_0_0, "!");
                    	    

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,26,FOLLOW_26_in_ruleQUERYCONDITION2239); 

                	newLeafNode(otherlv_1, grammarAccess.getQUERYCONDITIONAccess().getQueryExistsKeyword_1());
                
            otherlv_2=(Token)match(input,27,FOLLOW_27_in_ruleQUERYCONDITION2251); 

                	newLeafNode(otherlv_2, grammarAccess.getQUERYCONDITIONAccess().getLeftParenthesisKeyword_2());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1072:1: ( (lv_queryFunct_3_0= ruleRNDQUERY ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1073:1: (lv_queryFunct_3_0= ruleRNDQUERY )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1073:1: (lv_queryFunct_3_0= ruleRNDQUERY )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1074:3: lv_queryFunct_3_0= ruleRNDQUERY
            {
             
            	        newCompositeNode(grammarAccess.getQUERYCONDITIONAccess().getQueryFunctRNDQUERYParserRuleCall_3_0()); 
            	    
            pushFollow(FOLLOW_ruleRNDQUERY_in_ruleQUERYCONDITION2272);
            lv_queryFunct_3_0=ruleRNDQUERY();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getQUERYCONDITIONRule());
            	        }
                   		set(
                   			current, 
                   			"queryFunct",
                    		lv_queryFunct_3_0, 
                    		"RNDQUERY");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_4=(Token)match(input,28,FOLLOW_28_in_ruleQUERYCONDITION2284); 

                	newLeafNode(otherlv_4, grammarAccess.getQUERYCONDITIONAccess().getRightParenthesisKeyword_4());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQUERYCONDITION"


    // $ANTLR start "entryRuleSYSTEMCONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1102:1: entryRuleSYSTEMCONDITION returns [EObject current=null] : iv_ruleSYSTEMCONDITION= ruleSYSTEMCONDITION EOF ;
    public final EObject entryRuleSYSTEMCONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSYSTEMCONDITION = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1103:2: (iv_ruleSYSTEMCONDITION= ruleSYSTEMCONDITION EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1104:2: iv_ruleSYSTEMCONDITION= ruleSYSTEMCONDITION EOF
            {
             newCompositeNode(grammarAccess.getSYSTEMCONDITIONRule()); 
            pushFollow(FOLLOW_ruleSYSTEMCONDITION_in_entryRuleSYSTEMCONDITION2320);
            iv_ruleSYSTEMCONDITION=ruleSYSTEMCONDITION();

            state._fsp--;

             current =iv_ruleSYSTEMCONDITION; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSYSTEMCONDITION2330); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSYSTEMCONDITION"


    // $ANTLR start "ruleSYSTEMCONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1111:1: ruleSYSTEMCONDITION returns [EObject current=null] : (otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) ) ) ;
    public final EObject ruleSYSTEMCONDITION() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        AntlrDatatypeRuleToken lv_systemAttribute_1_0 = null;

        AntlrDatatypeRuleToken lv_operator_2_0 = null;

        EObject lv_value_3_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1114:28: ( (otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1115:1: (otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1115:1: (otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1115:3: otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) )
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleSYSTEMCONDITION2367); 

                	newLeafNode(otherlv_0, grammarAccess.getSYSTEMCONDITIONAccess().getSYSTEMKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1119:1: ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1120:1: (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1120:1: (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1121:3: lv_systemAttribute_1_0= ruleSYSTEMFUNCTION
            {
             
            	        newCompositeNode(grammarAccess.getSYSTEMCONDITIONAccess().getSystemAttributeSYSTEMFUNCTIONParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleSYSTEMFUNCTION_in_ruleSYSTEMCONDITION2388);
            lv_systemAttribute_1_0=ruleSYSTEMFUNCTION();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSYSTEMCONDITIONRule());
            	        }
                   		set(
                   			current, 
                   			"systemAttribute",
                    		lv_systemAttribute_1_0, 
                    		"SYSTEMFUNCTION");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1137:2: ( (lv_operator_2_0= ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1138:1: (lv_operator_2_0= ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1138:1: (lv_operator_2_0= ruleOperator )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1139:3: lv_operator_2_0= ruleOperator
            {
             
            	        newCompositeNode(grammarAccess.getSYSTEMCONDITIONAccess().getOperatorOperatorParserRuleCall_2_0()); 
            	    
            pushFollow(FOLLOW_ruleOperator_in_ruleSYSTEMCONDITION2409);
            lv_operator_2_0=ruleOperator();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSYSTEMCONDITIONRule());
            	        }
                   		set(
                   			current, 
                   			"operator",
                    		lv_operator_2_0, 
                    		"Operator");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1155:2: ( (lv_value_3_0= ruleEcaValue ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1156:1: (lv_value_3_0= ruleEcaValue )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1156:1: (lv_value_3_0= ruleEcaValue )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1157:3: lv_value_3_0= ruleEcaValue
            {
             
            	        newCompositeNode(grammarAccess.getSYSTEMCONDITIONAccess().getValueEcaValueParserRuleCall_3_0()); 
            	    
            pushFollow(FOLLOW_ruleEcaValue_in_ruleSYSTEMCONDITION2430);
            lv_value_3_0=ruleEcaValue();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSYSTEMCONDITIONRule());
            	        }
                   		set(
                   			current, 
                   			"value",
                    		lv_value_3_0, 
                    		"EcaValue");
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
    // $ANTLR end "ruleSYSTEMCONDITION"


    // $ANTLR start "entryRuleFREECONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1181:1: entryRuleFREECONDITION returns [EObject current=null] : iv_ruleFREECONDITION= ruleFREECONDITION EOF ;
    public final EObject entryRuleFREECONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFREECONDITION = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1182:2: (iv_ruleFREECONDITION= ruleFREECONDITION EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1183:2: iv_ruleFREECONDITION= ruleFREECONDITION EOF
            {
             newCompositeNode(grammarAccess.getFREECONDITIONRule()); 
            pushFollow(FOLLOW_ruleFREECONDITION_in_entryRuleFREECONDITION2466);
            iv_ruleFREECONDITION=ruleFREECONDITION();

            state._fsp--;

             current =iv_ruleFREECONDITION; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFREECONDITION2476); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFREECONDITION"


    // $ANTLR start "ruleFREECONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1190:1: ruleFREECONDITION returns [EObject current=null] : ( (lv_freeCondition_0_0= RULE_STRING ) ) ;
    public final EObject ruleFREECONDITION() throws RecognitionException {
        EObject current = null;

        Token lv_freeCondition_0_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1193:28: ( ( (lv_freeCondition_0_0= RULE_STRING ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1194:1: ( (lv_freeCondition_0_0= RULE_STRING ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1194:1: ( (lv_freeCondition_0_0= RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1195:1: (lv_freeCondition_0_0= RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1195:1: (lv_freeCondition_0_0= RULE_STRING )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1196:3: lv_freeCondition_0_0= RULE_STRING
            {
            lv_freeCondition_0_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleFREECONDITION2517); 

            			newLeafNode(lv_freeCondition_0_0, grammarAccess.getFREECONDITIONAccess().getFreeConditionSTRINGTerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getFREECONDITIONRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"freeCondition",
                    		lv_freeCondition_0_0, 
                    		"STRING");
            	    

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
    // $ANTLR end "ruleFREECONDITION"


    // $ANTLR start "entryRuleMAPCONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1220:1: entryRuleMAPCONDITION returns [EObject current=null] : iv_ruleMAPCONDITION= ruleMAPCONDITION EOF ;
    public final EObject entryRuleMAPCONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMAPCONDITION = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1221:2: (iv_ruleMAPCONDITION= ruleMAPCONDITION EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1222:2: iv_ruleMAPCONDITION= ruleMAPCONDITION EOF
            {
             newCompositeNode(grammarAccess.getMAPCONDITIONRule()); 
            pushFollow(FOLLOW_ruleMAPCONDITION_in_entryRuleMAPCONDITION2557);
            iv_ruleMAPCONDITION=ruleMAPCONDITION();

            state._fsp--;

             current =iv_ruleMAPCONDITION; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMAPCONDITION2567); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMAPCONDITION"


    // $ANTLR start "ruleMAPCONDITION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1229:1: ruleMAPCONDITION returns [EObject current=null] : (otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) ) ) ;
    public final EObject ruleMAPCONDITION() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_mapCond_1_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1232:28: ( (otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1233:1: (otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1233:1: (otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1233:3: otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) )
            {
            otherlv_0=(Token)match(input,30,FOLLOW_30_in_ruleMAPCONDITION2604); 

                	newLeafNode(otherlv_0, grammarAccess.getMAPCONDITIONAccess().getGETKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1237:1: ( (lv_mapCond_1_0= RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1238:1: (lv_mapCond_1_0= RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1238:1: (lv_mapCond_1_0= RULE_STRING )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1239:3: lv_mapCond_1_0= RULE_STRING
            {
            lv_mapCond_1_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleMAPCONDITION2621); 

            			newLeafNode(lv_mapCond_1_0, grammarAccess.getMAPCONDITIONAccess().getMapCondSTRINGTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getMAPCONDITIONRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"mapCond",
                    		lv_mapCond_1_0, 
                    		"STRING");
            	    

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
    // $ANTLR end "ruleMAPCONDITION"


    // $ANTLR start "entryRuleACTIONS"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1263:1: entryRuleACTIONS returns [EObject current=null] : iv_ruleACTIONS= ruleACTIONS EOF ;
    public final EObject entryRuleACTIONS() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleACTIONS = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1264:2: (iv_ruleACTIONS= ruleACTIONS EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1265:2: iv_ruleACTIONS= ruleACTIONS EOF
            {
             newCompositeNode(grammarAccess.getACTIONSRule()); 
            pushFollow(FOLLOW_ruleACTIONS_in_entryRuleACTIONS2662);
            iv_ruleACTIONS=ruleACTIONS();

            state._fsp--;

             current =iv_ruleACTIONS; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleACTIONS2672); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleACTIONS"


    // $ANTLR start "ruleACTIONS"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1272:1: ruleACTIONS returns [EObject current=null] : (this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )* ) ;
    public final EObject ruleACTIONS() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_SUBACTIONS_0 = null;

        EObject lv_right_3_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1275:28: ( (this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )* ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1276:1: (this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )* )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1276:1: (this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )* )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1277:5: this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )*
            {
             
                    newCompositeNode(grammarAccess.getACTIONSAccess().getSUBACTIONSParserRuleCall_0()); 
                
            pushFollow(FOLLOW_ruleSUBACTIONS_in_ruleACTIONS2719);
            this_SUBACTIONS_0=ruleSUBACTIONS();

            state._fsp--;

             
                    current = this_SUBACTIONS_0; 
                    afterParserOrEnumRuleCall();
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1285:1: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==22) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1285:2: () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) )
            	    {
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1285:2: ()
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1286:5: 
            	    {

            	            current = forceCreateModelElementAndSet(
            	                grammarAccess.getACTIONSAccess().getACTIONSLeftAction_1_0(),
            	                current);
            	        

            	    }

            	    otherlv_2=(Token)match(input,22,FOLLOW_22_in_ruleACTIONS2740); 

            	        	newLeafNode(otherlv_2, grammarAccess.getACTIONSAccess().getANDKeyword_1_1());
            	        
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1295:1: ( (lv_right_3_0= ruleSUBACTIONS ) )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1296:1: (lv_right_3_0= ruleSUBACTIONS )
            	    {
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1296:1: (lv_right_3_0= ruleSUBACTIONS )
            	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1297:3: lv_right_3_0= ruleSUBACTIONS
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getACTIONSAccess().getRightSUBACTIONSParserRuleCall_1_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleSUBACTIONS_in_ruleACTIONS2761);
            	    lv_right_3_0=ruleSUBACTIONS();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getACTIONSRule());
            	    	        }
            	           		set(
            	           			current, 
            	           			"right",
            	            		lv_right_3_0, 
            	            		"SUBACTIONS");
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


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleACTIONS"


    // $ANTLR start "entryRuleSUBACTIONS"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1321:1: entryRuleSUBACTIONS returns [EObject current=null] : iv_ruleSUBACTIONS= ruleSUBACTIONS EOF ;
    public final EObject entryRuleSUBACTIONS() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSUBACTIONS = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1322:2: (iv_ruleSUBACTIONS= ruleSUBACTIONS EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1323:2: iv_ruleSUBACTIONS= ruleSUBACTIONS EOF
            {
             newCompositeNode(grammarAccess.getSUBACTIONSRule()); 
            pushFollow(FOLLOW_ruleSUBACTIONS_in_entryRuleSUBACTIONS2799);
            iv_ruleSUBACTIONS=ruleSUBACTIONS();

            state._fsp--;

             current =iv_ruleSUBACTIONS; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSUBACTIONS2809); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSUBACTIONS"


    // $ANTLR start "ruleSUBACTIONS"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1330:1: ruleSUBACTIONS returns [EObject current=null] : ( (lv_comAction_0_0= ruleCOMMANDACTION ) ) ;
    public final EObject ruleSUBACTIONS() throws RecognitionException {
        EObject current = null;

        EObject lv_comAction_0_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1333:28: ( ( (lv_comAction_0_0= ruleCOMMANDACTION ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1334:1: ( (lv_comAction_0_0= ruleCOMMANDACTION ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1334:1: ( (lv_comAction_0_0= ruleCOMMANDACTION ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1335:1: (lv_comAction_0_0= ruleCOMMANDACTION )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1335:1: (lv_comAction_0_0= ruleCOMMANDACTION )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1336:3: lv_comAction_0_0= ruleCOMMANDACTION
            {
             
            	        newCompositeNode(grammarAccess.getSUBACTIONSAccess().getComActionCOMMANDACTIONParserRuleCall_0()); 
            	    
            pushFollow(FOLLOW_ruleCOMMANDACTION_in_ruleSUBACTIONS2854);
            lv_comAction_0_0=ruleCOMMANDACTION();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSUBACTIONSRule());
            	        }
                   		set(
                   			current, 
                   			"comAction",
                    		lv_comAction_0_0, 
                    		"COMMANDACTION");
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
    // $ANTLR end "ruleSUBACTIONS"


    // $ANTLR start "entryRuleCOMMANDACTION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1360:1: entryRuleCOMMANDACTION returns [EObject current=null] : iv_ruleCOMMANDACTION= ruleCOMMANDACTION EOF ;
    public final EObject entryRuleCOMMANDACTION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCOMMANDACTION = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1361:2: (iv_ruleCOMMANDACTION= ruleCOMMANDACTION EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1362:2: iv_ruleCOMMANDACTION= ruleCOMMANDACTION EOF
            {
             newCompositeNode(grammarAccess.getCOMMANDACTIONRule()); 
            pushFollow(FOLLOW_ruleCOMMANDACTION_in_entryRuleCOMMANDACTION2889);
            iv_ruleCOMMANDACTION=ruleCOMMANDACTION();

            state._fsp--;

             current =iv_ruleCOMMANDACTION; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleCOMMANDACTION2899); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCOMMANDACTION"


    // $ANTLR start "ruleCOMMANDACTION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1369:1: ruleCOMMANDACTION returns [EObject current=null] : ( ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')' ) ;
    public final EObject ruleCOMMANDACTION() throws RecognitionException {
        EObject current = null;

        Token lv_subActname_0_0=null;
        Token otherlv_1=null;
        Token otherlv_5=null;
        EObject lv_functAction_2_0 = null;

        EObject lv_actionValue_3_0 = null;

        EObject lv_innerAction_4_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1372:28: ( ( ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1373:1: ( ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1373:1: ( ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')' )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1373:2: ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')'
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1373:2: ( (lv_subActname_0_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1374:1: (lv_subActname_0_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1374:1: (lv_subActname_0_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1375:3: lv_subActname_0_0= RULE_ID
            {
            lv_subActname_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleCOMMANDACTION2941); 

            			newLeafNode(lv_subActname_0_0, grammarAccess.getCOMMANDACTIONAccess().getSubActnameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getCOMMANDACTIONRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"subActname",
                    		lv_subActname_0_0, 
                    		"ID");
            	    

            }


            }

            otherlv_1=(Token)match(input,27,FOLLOW_27_in_ruleCOMMANDACTION2958); 

                	newLeafNode(otherlv_1, grammarAccess.getCOMMANDACTIONAccess().getLeftParenthesisKeyword_1());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1395:1: ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* )
            int alt13=3;
            switch ( input.LA(1) ) {
            case 31:
                {
                alt13=1;
                }
                break;
            case RULE_INT:
            case RULE_STRING:
            case RULE_DOUBLE:
            case 23:
                {
                alt13=2;
                }
                break;
            case RULE_ID:
                {
                int LA13_3 = input.LA(2);

                if ( (LA13_3==28) ) {
                    alt13=2;
                }
                else if ( (LA13_3==27) ) {
                    alt13=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 3, input);

                    throw nvae;
                }
                }
                break;
            case 28:
                {
                alt13=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1395:2: ( (lv_functAction_2_0= ruleRNDQUERY ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1395:2: ( (lv_functAction_2_0= ruleRNDQUERY ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1396:1: (lv_functAction_2_0= ruleRNDQUERY )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1396:1: (lv_functAction_2_0= ruleRNDQUERY )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1397:3: lv_functAction_2_0= ruleRNDQUERY
                    {
                     
                    	        newCompositeNode(grammarAccess.getCOMMANDACTIONAccess().getFunctActionRNDQUERYParserRuleCall_2_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleRNDQUERY_in_ruleCOMMANDACTION2980);
                    lv_functAction_2_0=ruleRNDQUERY();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getCOMMANDACTIONRule());
                    	        }
                           		set(
                           			current, 
                           			"functAction",
                            		lv_functAction_2_0, 
                            		"RNDQUERY");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1414:6: ( (lv_actionValue_3_0= ruleEcaValue ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1414:6: ( (lv_actionValue_3_0= ruleEcaValue ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1415:1: (lv_actionValue_3_0= ruleEcaValue )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1415:1: (lv_actionValue_3_0= ruleEcaValue )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1416:3: lv_actionValue_3_0= ruleEcaValue
                    {
                     
                    	        newCompositeNode(grammarAccess.getCOMMANDACTIONAccess().getActionValueEcaValueParserRuleCall_2_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleEcaValue_in_ruleCOMMANDACTION3007);
                    lv_actionValue_3_0=ruleEcaValue();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getCOMMANDACTIONRule());
                    	        }
                           		set(
                           			current, 
                           			"actionValue",
                            		lv_actionValue_3_0, 
                            		"EcaValue");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1433:6: ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )*
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1433:6: ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==RULE_ID) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1434:1: (lv_innerAction_4_0= ruleCOMMANDACTION )
                    	    {
                    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1434:1: (lv_innerAction_4_0= ruleCOMMANDACTION )
                    	    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1435:3: lv_innerAction_4_0= ruleCOMMANDACTION
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getCOMMANDACTIONAccess().getInnerActionCOMMANDACTIONParserRuleCall_2_2_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleCOMMANDACTION_in_ruleCOMMANDACTION3034);
                    	    lv_innerAction_4_0=ruleCOMMANDACTION();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getCOMMANDACTIONRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"innerAction",
                    	            		lv_innerAction_4_0, 
                    	            		"COMMANDACTION");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

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

            otherlv_5=(Token)match(input,28,FOLLOW_28_in_ruleCOMMANDACTION3048); 

                	newLeafNode(otherlv_5, grammarAccess.getCOMMANDACTIONAccess().getRightParenthesisKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCOMMANDACTION"


    // $ANTLR start "entryRuleRNDQUERY"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1463:1: entryRuleRNDQUERY returns [EObject current=null] : iv_ruleRNDQUERY= ruleRNDQUERY EOF ;
    public final EObject entryRuleRNDQUERY() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRNDQUERY = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1464:2: (iv_ruleRNDQUERY= ruleRNDQUERY EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1465:2: iv_ruleRNDQUERY= ruleRNDQUERY EOF
            {
             newCompositeNode(grammarAccess.getRNDQUERYRule()); 
            pushFollow(FOLLOW_ruleRNDQUERY_in_entryRuleRNDQUERY3084);
            iv_ruleRNDQUERY=ruleRNDQUERY();

            state._fsp--;

             current =iv_ruleRNDQUERY; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleRNDQUERY3094); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRNDQUERY"


    // $ANTLR start "ruleRNDQUERY"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1472:1: ruleRNDQUERY returns [EObject current=null] : (otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) ) ) ;
    public final EObject ruleRNDQUERY() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_priVal_2_0=null;
        Token otherlv_3=null;
        Token lv_sel_4_1=null;
        Token lv_sel_4_2=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token lv_stateName_9_0=null;
        AntlrDatatypeRuleToken lv_priOperator_1_0 = null;


         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1475:28: ( (otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1476:1: (otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1476:1: (otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1476:3: otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,31,FOLLOW_31_in_ruleRNDQUERY3131); 

                	newLeafNode(otherlv_0, grammarAccess.getRNDQUERYAccess().getPrioKeyword_0());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1480:1: ( (lv_priOperator_1_0= ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1481:1: (lv_priOperator_1_0= ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1481:1: (lv_priOperator_1_0= ruleOperator )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1482:3: lv_priOperator_1_0= ruleOperator
            {
             
            	        newCompositeNode(grammarAccess.getRNDQUERYAccess().getPriOperatorOperatorParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleOperator_in_ruleRNDQUERY3152);
            lv_priOperator_1_0=ruleOperator();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getRNDQUERYRule());
            	        }
                   		set(
                   			current, 
                   			"priOperator",
                    		lv_priOperator_1_0, 
                    		"Operator");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1498:2: ( (lv_priVal_2_0= RULE_INT ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1499:1: (lv_priVal_2_0= RULE_INT )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1499:1: (lv_priVal_2_0= RULE_INT )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1500:3: lv_priVal_2_0= RULE_INT
            {
            lv_priVal_2_0=(Token)match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleRNDQUERY3169); 

            			newLeafNode(lv_priVal_2_0, grammarAccess.getRNDQUERYAccess().getPriValINTTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getRNDQUERYRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"priVal",
                    		lv_priVal_2_0, 
                    		"INT");
            	    

            }


            }

            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1516:2: (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==27) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1516:4: otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')'
                    {
                    otherlv_3=(Token)match(input,27,FOLLOW_27_in_ruleRNDQUERY3187); 

                        	newLeafNode(otherlv_3, grammarAccess.getRNDQUERYAccess().getLeftParenthesisKeyword_3_0());
                        
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1520:1: ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1521:1: ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1521:1: ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1522:1: (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1522:1: (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' )
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==32) ) {
                        alt14=1;
                    }
                    else if ( (LA14_0==33) ) {
                        alt14=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 0, input);

                        throw nvae;
                    }
                    switch (alt14) {
                        case 1 :
                            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1523:3: lv_sel_4_1= 'MIN'
                            {
                            lv_sel_4_1=(Token)match(input,32,FOLLOW_32_in_ruleRNDQUERY3207); 

                                    newLeafNode(lv_sel_4_1, grammarAccess.getRNDQUERYAccess().getSelMINKeyword_3_1_0_0());
                                

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getRNDQUERYRule());
                            	        }
                                   		setWithLastConsumed(current, "sel", lv_sel_4_1, null);
                            	    

                            }
                            break;
                        case 2 :
                            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1535:8: lv_sel_4_2= 'MAX'
                            {
                            lv_sel_4_2=(Token)match(input,33,FOLLOW_33_in_ruleRNDQUERY3236); 

                                    newLeafNode(lv_sel_4_2, grammarAccess.getRNDQUERYAccess().getSelMAXKeyword_3_1_0_1());
                                

                            	        if (current==null) {
                            	            current = createModelElement(grammarAccess.getRNDQUERYRule());
                            	        }
                                   		setWithLastConsumed(current, "sel", lv_sel_4_2, null);
                            	    

                            }
                            break;

                    }


                    }


                    }

                    otherlv_5=(Token)match(input,28,FOLLOW_28_in_ruleRNDQUERY3264); 

                        	newLeafNode(otherlv_5, grammarAccess.getRNDQUERYAccess().getRightParenthesisKeyword_3_2());
                        

                    }
                    break;

            }

            otherlv_6=(Token)match(input,34,FOLLOW_34_in_ruleRNDQUERY3278); 

                	newLeafNode(otherlv_6, grammarAccess.getRNDQUERYAccess().getCommaKeyword_4());
                
            otherlv_7=(Token)match(input,35,FOLLOW_35_in_ruleRNDQUERY3290); 

                	newLeafNode(otherlv_7, grammarAccess.getRNDQUERYAccess().getStateKeyword_5());
                
            otherlv_8=(Token)match(input,36,FOLLOW_36_in_ruleRNDQUERY3302); 

                	newLeafNode(otherlv_8, grammarAccess.getRNDQUERYAccess().getEqualsSignKeyword_6());
                
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1566:1: ( (lv_stateName_9_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1567:1: (lv_stateName_9_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1567:1: (lv_stateName_9_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1568:3: lv_stateName_9_0= RULE_ID
            {
            lv_stateName_9_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleRNDQUERY3319); 

            			newLeafNode(lv_stateName_9_0, grammarAccess.getRNDQUERYAccess().getStateNameIDTerminalRuleCall_7_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getRNDQUERYRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"stateName",
                    		lv_stateName_9_0, 
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
    // $ANTLR end "ruleRNDQUERY"


    // $ANTLR start "entryRuleSource"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1592:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1593:2: (iv_ruleSource= ruleSource EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1594:2: iv_ruleSource= ruleSource EOF
            {
             newCompositeNode(grammarAccess.getSourceRule()); 
            pushFollow(FOLLOW_ruleSource_in_entryRuleSource3360);
            iv_ruleSource=ruleSource();

            state._fsp--;

             current =iv_ruleSource; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSource3370); 

            }

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
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1601:1: ruleSource returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1604:28: ( ( (lv_name_0_0= RULE_ID ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1605:1: ( (lv_name_0_0= RULE_ID ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1605:1: ( (lv_name_0_0= RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1606:1: (lv_name_0_0= RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1606:1: (lv_name_0_0= RULE_ID )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1607:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSource3411); 

            			newLeafNode(lv_name_0_0, grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSourceRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

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


    // $ANTLR start "entryRuleEcaValue"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1631:1: entryRuleEcaValue returns [EObject current=null] : iv_ruleEcaValue= ruleEcaValue EOF ;
    public final EObject entryRuleEcaValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEcaValue = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1632:2: (iv_ruleEcaValue= ruleEcaValue EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1633:2: iv_ruleEcaValue= ruleEcaValue EOF
            {
             newCompositeNode(grammarAccess.getEcaValueRule()); 
            pushFollow(FOLLOW_ruleEcaValue_in_entryRuleEcaValue3451);
            iv_ruleEcaValue=ruleEcaValue();

            state._fsp--;

             current =iv_ruleEcaValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleEcaValue3461); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEcaValue"


    // $ANTLR start "ruleEcaValue"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1640:1: ruleEcaValue returns [EObject current=null] : ( ( (lv_intValue_0_0= RULE_INT ) ) | ( (lv_idValue_1_0= RULE_ID ) ) | ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' ) | ( (lv_stringValue_5_0= RULE_STRING ) ) | ( (lv_doubleValue_6_0= RULE_DOUBLE ) ) ) ;
    public final EObject ruleEcaValue() throws RecognitionException {
        EObject current = null;

        Token lv_intValue_0_0=null;
        Token lv_idValue_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token lv_stringValue_5_0=null;
        Token lv_doubleValue_6_0=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1643:28: ( ( ( (lv_intValue_0_0= RULE_INT ) ) | ( (lv_idValue_1_0= RULE_ID ) ) | ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' ) | ( (lv_stringValue_5_0= RULE_STRING ) ) | ( (lv_doubleValue_6_0= RULE_DOUBLE ) ) ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1644:1: ( ( (lv_intValue_0_0= RULE_INT ) ) | ( (lv_idValue_1_0= RULE_ID ) ) | ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' ) | ( (lv_stringValue_5_0= RULE_STRING ) ) | ( (lv_doubleValue_6_0= RULE_DOUBLE ) ) )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1644:1: ( ( (lv_intValue_0_0= RULE_INT ) ) | ( (lv_idValue_1_0= RULE_ID ) ) | ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' ) | ( (lv_stringValue_5_0= RULE_STRING ) ) | ( (lv_doubleValue_6_0= RULE_DOUBLE ) ) )
            int alt16=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt16=1;
                }
                break;
            case RULE_ID:
                {
                alt16=2;
                }
                break;
            case 23:
                {
                alt16=3;
                }
                break;
            case RULE_STRING:
                {
                alt16=4;
                }
                break;
            case RULE_DOUBLE:
                {
                alt16=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1644:2: ( (lv_intValue_0_0= RULE_INT ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1644:2: ( (lv_intValue_0_0= RULE_INT ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1645:1: (lv_intValue_0_0= RULE_INT )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1645:1: (lv_intValue_0_0= RULE_INT )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1646:3: lv_intValue_0_0= RULE_INT
                    {
                    lv_intValue_0_0=(Token)match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleEcaValue3503); 

                    			newLeafNode(lv_intValue_0_0, grammarAccess.getEcaValueAccess().getIntValueINTTerminalRuleCall_0_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getEcaValueRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"intValue",
                            		lv_intValue_0_0, 
                            		"INT");
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1663:6: ( (lv_idValue_1_0= RULE_ID ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1663:6: ( (lv_idValue_1_0= RULE_ID ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1664:1: (lv_idValue_1_0= RULE_ID )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1664:1: (lv_idValue_1_0= RULE_ID )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1665:3: lv_idValue_1_0= RULE_ID
                    {
                    lv_idValue_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleEcaValue3531); 

                    			newLeafNode(lv_idValue_1_0, grammarAccess.getEcaValueAccess().getIdValueIDTerminalRuleCall_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getEcaValueRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"idValue",
                            		lv_idValue_1_0, 
                            		"ID");
                    	    

                    }


                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1682:6: ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1682:6: ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1682:7: (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}'
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1682:7: (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1682:9: otherlv_2= '${' ( (otherlv_3= RULE_ID ) )
                    {
                    otherlv_2=(Token)match(input,23,FOLLOW_23_in_ruleEcaValue3556); 

                        	newLeafNode(otherlv_2, grammarAccess.getEcaValueAccess().getDollarSignLeftCurlyBracketKeyword_2_0_0());
                        
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1686:1: ( (otherlv_3= RULE_ID ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1687:1: (otherlv_3= RULE_ID )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1687:1: (otherlv_3= RULE_ID )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1688:3: otherlv_3= RULE_ID
                    {

                    			if (current==null) {
                    	            current = createModelElement(grammarAccess.getEcaValueRule());
                    	        }
                            
                    otherlv_3=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleEcaValue3576); 

                    		newLeafNode(otherlv_3, grammarAccess.getEcaValueAccess().getConstValueConstantCrossReference_2_0_1_0()); 
                    	

                    }


                    }


                    }

                    otherlv_4=(Token)match(input,24,FOLLOW_24_in_ruleEcaValue3589); 

                        	newLeafNode(otherlv_4, grammarAccess.getEcaValueAccess().getRightCurlyBracketKeyword_2_1());
                        

                    }


                    }
                    break;
                case 4 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1704:6: ( (lv_stringValue_5_0= RULE_STRING ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1704:6: ( (lv_stringValue_5_0= RULE_STRING ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1705:1: (lv_stringValue_5_0= RULE_STRING )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1705:1: (lv_stringValue_5_0= RULE_STRING )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1706:3: lv_stringValue_5_0= RULE_STRING
                    {
                    lv_stringValue_5_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleEcaValue3613); 

                    			newLeafNode(lv_stringValue_5_0, grammarAccess.getEcaValueAccess().getStringValueSTRINGTerminalRuleCall_3_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getEcaValueRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"stringValue",
                            		lv_stringValue_5_0, 
                            		"STRING");
                    	    

                    }


                    }


                    }
                    break;
                case 5 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1723:6: ( (lv_doubleValue_6_0= RULE_DOUBLE ) )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1723:6: ( (lv_doubleValue_6_0= RULE_DOUBLE ) )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1724:1: (lv_doubleValue_6_0= RULE_DOUBLE )
                    {
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1724:1: (lv_doubleValue_6_0= RULE_DOUBLE )
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1725:3: lv_doubleValue_6_0= RULE_DOUBLE
                    {
                    lv_doubleValue_6_0=(Token)match(input,RULE_DOUBLE,FOLLOW_RULE_DOUBLE_in_ruleEcaValue3641); 

                    			newLeafNode(lv_doubleValue_6_0, grammarAccess.getEcaValueAccess().getDoubleValueDOUBLETerminalRuleCall_4_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getEcaValueRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"doubleValue",
                            		lv_doubleValue_6_0, 
                            		"DOUBLE");
                    	    

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
    // $ANTLR end "ruleEcaValue"


    // $ANTLR start "entryRulePREDEFINEDSOURCE"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1749:1: entryRulePREDEFINEDSOURCE returns [String current=null] : iv_rulePREDEFINEDSOURCE= rulePREDEFINEDSOURCE EOF ;
    public final String entryRulePREDEFINEDSOURCE() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_rulePREDEFINEDSOURCE = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1750:2: (iv_rulePREDEFINEDSOURCE= rulePREDEFINEDSOURCE EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1751:2: iv_rulePREDEFINEDSOURCE= rulePREDEFINEDSOURCE EOF
            {
             newCompositeNode(grammarAccess.getPREDEFINEDSOURCERule()); 
            pushFollow(FOLLOW_rulePREDEFINEDSOURCE_in_entryRulePREDEFINEDSOURCE3683);
            iv_rulePREDEFINEDSOURCE=rulePREDEFINEDSOURCE();

            state._fsp--;

             current =iv_rulePREDEFINEDSOURCE.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRulePREDEFINEDSOURCE3694); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePREDEFINEDSOURCE"


    // $ANTLR start "rulePREDEFINEDSOURCE"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1758:1: rulePREDEFINEDSOURCE returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'TimerEvent' | kw= 'QueryEvent' ) ;
    public final AntlrDatatypeRuleToken rulePREDEFINEDSOURCE() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1761:28: ( (kw= 'TimerEvent' | kw= 'QueryEvent' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1762:1: (kw= 'TimerEvent' | kw= 'QueryEvent' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1762:1: (kw= 'TimerEvent' | kw= 'QueryEvent' )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==37) ) {
                alt17=1;
            }
            else if ( (LA17_0==38) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1763:2: kw= 'TimerEvent'
                    {
                    kw=(Token)match(input,37,FOLLOW_37_in_rulePREDEFINEDSOURCE3732); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getPREDEFINEDSOURCEAccess().getTimerEventKeyword_0()); 
                        

                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1770:2: kw= 'QueryEvent'
                    {
                    kw=(Token)match(input,38,FOLLOW_38_in_rulePREDEFINEDSOURCE3751); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getPREDEFINEDSOURCEAccess().getQueryEventKeyword_1()); 
                        

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
    // $ANTLR end "rulePREDEFINEDSOURCE"


    // $ANTLR start "entryRuleSYSTEMFUNCTION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1783:1: entryRuleSYSTEMFUNCTION returns [String current=null] : iv_ruleSYSTEMFUNCTION= ruleSYSTEMFUNCTION EOF ;
    public final String entryRuleSYSTEMFUNCTION() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSYSTEMFUNCTION = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1784:2: (iv_ruleSYSTEMFUNCTION= ruleSYSTEMFUNCTION EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1785:2: iv_ruleSYSTEMFUNCTION= ruleSYSTEMFUNCTION EOF
            {
             newCompositeNode(grammarAccess.getSYSTEMFUNCTIONRule()); 
            pushFollow(FOLLOW_ruleSYSTEMFUNCTION_in_entryRuleSYSTEMFUNCTION3792);
            iv_ruleSYSTEMFUNCTION=ruleSYSTEMFUNCTION();

            state._fsp--;

             current =iv_ruleSYSTEMFUNCTION.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSYSTEMFUNCTION3803); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSYSTEMFUNCTION"


    // $ANTLR start "ruleSYSTEMFUNCTION"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1792:1: ruleSYSTEMFUNCTION returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'curCPULoad' | kw= 'curMEMLoad' | kw= 'curNETLoad' ) ;
    public final AntlrDatatypeRuleToken ruleSYSTEMFUNCTION() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1795:28: ( (kw= 'curCPULoad' | kw= 'curMEMLoad' | kw= 'curNETLoad' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1796:1: (kw= 'curCPULoad' | kw= 'curMEMLoad' | kw= 'curNETLoad' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1796:1: (kw= 'curCPULoad' | kw= 'curMEMLoad' | kw= 'curNETLoad' )
            int alt18=3;
            switch ( input.LA(1) ) {
            case 39:
                {
                alt18=1;
                }
                break;
            case 40:
                {
                alt18=2;
                }
                break;
            case 41:
                {
                alt18=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1797:2: kw= 'curCPULoad'
                    {
                    kw=(Token)match(input,39,FOLLOW_39_in_ruleSYSTEMFUNCTION3841); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getSYSTEMFUNCTIONAccess().getCurCPULoadKeyword_0()); 
                        

                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1804:2: kw= 'curMEMLoad'
                    {
                    kw=(Token)match(input,40,FOLLOW_40_in_ruleSYSTEMFUNCTION3860); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getSYSTEMFUNCTIONAccess().getCurMEMLoadKeyword_1()); 
                        

                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1811:2: kw= 'curNETLoad'
                    {
                    kw=(Token)match(input,41,FOLLOW_41_in_ruleSYSTEMFUNCTION3879); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getSYSTEMFUNCTIONAccess().getCurNETLoadKeyword_2()); 
                        

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
    // $ANTLR end "ruleSYSTEMFUNCTION"


    // $ANTLR start "entryRuleOperator"
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1824:1: entryRuleOperator returns [String current=null] : iv_ruleOperator= ruleOperator EOF ;
    public final String entryRuleOperator() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOperator = null;


        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1825:2: (iv_ruleOperator= ruleOperator EOF )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1826:2: iv_ruleOperator= ruleOperator EOF
            {
             newCompositeNode(grammarAccess.getOperatorRule()); 
            pushFollow(FOLLOW_ruleOperator_in_entryRuleOperator3920);
            iv_ruleOperator=ruleOperator();

            state._fsp--;

             current =iv_ruleOperator.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperator3931); 

            }

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
    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1833:1: ruleOperator returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '<' | kw= '>' | kw= '=' | kw= '<=' | kw= '>=' ) ;
    public final AntlrDatatypeRuleToken ruleOperator() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;

         enterRule(); 
            
        try {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1836:28: ( (kw= '<' | kw= '>' | kw= '=' | kw= '<=' | kw= '>=' ) )
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1837:1: (kw= '<' | kw= '>' | kw= '=' | kw= '<=' | kw= '>=' )
            {
            // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1837:1: (kw= '<' | kw= '>' | kw= '=' | kw= '<=' | kw= '>=' )
            int alt19=5;
            switch ( input.LA(1) ) {
            case 42:
                {
                alt19=1;
                }
                break;
            case 43:
                {
                alt19=2;
                }
                break;
            case 36:
                {
                alt19=3;
                }
                break;
            case 44:
                {
                alt19=4;
                }
                break;
            case 45:
                {
                alt19=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1838:2: kw= '<'
                    {
                    kw=(Token)match(input,42,FOLLOW_42_in_ruleOperator3969); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getOperatorAccess().getLessThanSignKeyword_0()); 
                        

                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1845:2: kw= '>'
                    {
                    kw=(Token)match(input,43,FOLLOW_43_in_ruleOperator3988); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getOperatorAccess().getGreaterThanSignKeyword_1()); 
                        

                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1852:2: kw= '='
                    {
                    kw=(Token)match(input,36,FOLLOW_36_in_ruleOperator4007); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getOperatorAccess().getEqualsSignKeyword_2()); 
                        

                    }
                    break;
                case 4 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1859:2: kw= '<='
                    {
                    kw=(Token)match(input,44,FOLLOW_44_in_ruleOperator4026); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getOperatorAccess().getLessThanSignEqualsSignKeyword_3()); 
                        

                    }
                    break;
                case 5 :
                    // ../de.uniol.inf.is.odysseus.eca/src-gen/de/uniol/inf/is/odysseus/eca/parser/antlr/internal/InternalECA.g:1866:2: kw= '>='
                    {
                    kw=(Token)match(input,45,FOLLOW_45_in_ruleOperator4045); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getOperatorAccess().getGreaterThanSignEqualsSignKeyword_4()); 
                        

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


    protected DFA3 dfa3 = new DFA3(this);
    static final String DFA3_eotS =
        "\12\uffff";
    static final String DFA3_eofS =
        "\1\1\11\uffff";
    static final String DFA3_minS =
        "\1\14\1\uffff\4\0\4\uffff";
    static final String DFA3_maxS =
        "\1\23\1\uffff\4\0\4\uffff";
    static final String DFA3_acceptS =
        "\1\uffff\1\5\4\uffff\1\1\1\2\1\3\1\4";
    static final String DFA3_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\4\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\2\2\uffff\1\4\1\5\1\3\1\uffff\1\1",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "()* loopback of 101:3: ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA3_2 = input.LA(1);

                         
                        int index3_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 0) ) {s = 6;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index3_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA3_3 = input.LA(1);

                         
                        int index3_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 1) ) {s = 7;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index3_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA3_4 = input.LA(1);

                         
                        int index3_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 2) ) {s = 8;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index3_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA3_5 = input.LA(1);

                         
                        int index3_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 3) ) {s = 9;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index3_5);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 3, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_ruleModel_in_entryRuleModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleConstant_in_ruleModel220 = new BitSet(new long[]{0x00000000000B9002L});
    public static final BitSet FOLLOW_ruleDefinedEvent_in_ruleModel296 = new BitSet(new long[]{0x00000000000B9002L});
    public static final BitSet FOLLOW_ruleWindow_in_ruleModel372 = new BitSet(new long[]{0x00000000000B9002L});
    public static final BitSet FOLLOW_ruleTimer_in_ruleModel447 = new BitSet(new long[]{0x00000000000B9002L});
    public static final BitSet FOLLOW_ruleRule_in_ruleModel562 = new BitSet(new long[]{0x00000000000B9002L});
    public static final BitSet FOLLOW_ruleConstant_in_entryRuleConstant644 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleConstant654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_ruleConstant691 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleConstant708 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleConstant725 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleConstant742 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleConstant759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWindow_in_entryRuleWindow795 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWindow805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_ruleWindow842 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleWindow854 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleWindow871 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleWindow888 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTimer_in_entryRuleTimer924 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTimer934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_ruleTimer971 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleTimer983 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleTimer1000 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleTimer1017 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDefinedEvent_in_entryRuleDefinedEvent1053 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDefinedEvent1063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_ruleDefinedEvent1100 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleDefinedEvent1117 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleDefinedEvent1134 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleSource_in_ruleDefinedEvent1155 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleDefinedEvent1167 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleDefinedEvent1184 = new BitSet(new long[]{0x00003C1000000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleDefinedEvent1210 = new BitSet(new long[]{0x00000000008000F0L});
    public static final BitSet FOLLOW_ruleEcaValue_in_ruleDefinedEvent1231 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleDefinedEvent1243 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleRule_in_entryRuleRule1279 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleRule1289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_ruleRule1326 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleRule1343 = new BitSet(new long[]{0x0000006000800010L});
    public static final BitSet FOLLOW_ruleRuleSource_in_ruleRule1369 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_ruleRule1381 = new BitSet(new long[]{0x0000000066400050L});
    public static final BitSet FOLLOW_ruleCONDITIONS_in_ruleRule1402 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_ruleRule1414 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleACTIONS_in_ruleRule1435 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleRule1447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCONDITIONS_in_entryRuleCONDITIONS1483 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleCONDITIONS1493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSUBCONDITION_in_ruleCONDITIONS1540 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_22_in_ruleCONDITIONS1561 = new BitSet(new long[]{0x0000000066400050L});
    public static final BitSet FOLLOW_ruleSUBCONDITION_in_ruleCONDITIONS1582 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_ruleSUBCONDITION_in_entryRuleSUBCONDITION1620 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSUBCONDITION1630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSOURCECONDITION_in_ruleSUBCONDITION1676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSYSTEMCONDITION_in_ruleSUBCONDITION1703 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFREECONDITION_in_ruleSUBCONDITION1740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMAPCONDITION_in_ruleSUBCONDITION1778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleQUERYCONDITION_in_ruleSUBCONDITION1817 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleRuleSource_in_entryRuleRuleSource1854 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleRuleSource1864 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_ruleRuleSource1902 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleRuleSource1922 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleRuleSource1934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSource_in_ruleRuleSource1962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rulePREDEFINEDSOURCE_in_ruleRuleSource1989 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSOURCECONDITION_in_entryRuleSOURCECONDITION2025 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSOURCECONDITION2035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSOURCECONDITION2077 = new BitSet(new long[]{0x00003C1000000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleSOURCECONDITION2103 = new BitSet(new long[]{0x00000000008000F0L});
    public static final BitSet FOLLOW_ruleEcaValue_in_ruleSOURCECONDITION2124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleQUERYCONDITION_in_entryRuleQUERYCONDITION2160 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleQUERYCONDITION2170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_ruleQUERYCONDITION2213 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleQUERYCONDITION2239 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleQUERYCONDITION2251 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ruleRNDQUERY_in_ruleQUERYCONDITION2272 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_ruleQUERYCONDITION2284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSYSTEMCONDITION_in_entryRuleSYSTEMCONDITION2320 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSYSTEMCONDITION2330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleSYSTEMCONDITION2367 = new BitSet(new long[]{0x0000038000000000L});
    public static final BitSet FOLLOW_ruleSYSTEMFUNCTION_in_ruleSYSTEMCONDITION2388 = new BitSet(new long[]{0x00003C1000000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleSYSTEMCONDITION2409 = new BitSet(new long[]{0x00000000008000F0L});
    public static final BitSet FOLLOW_ruleEcaValue_in_ruleSYSTEMCONDITION2430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFREECONDITION_in_entryRuleFREECONDITION2466 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFREECONDITION2476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleFREECONDITION2517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMAPCONDITION_in_entryRuleMAPCONDITION2557 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMAPCONDITION2567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_ruleMAPCONDITION2604 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleMAPCONDITION2621 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleACTIONS_in_entryRuleACTIONS2662 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleACTIONS2672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSUBACTIONS_in_ruleACTIONS2719 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_22_in_ruleACTIONS2740 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleSUBACTIONS_in_ruleACTIONS2761 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_ruleSUBACTIONS_in_entryRuleSUBACTIONS2799 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSUBACTIONS2809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCOMMANDACTION_in_ruleSUBACTIONS2854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCOMMANDACTION_in_entryRuleCOMMANDACTION2889 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleCOMMANDACTION2899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleCOMMANDACTION2941 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleCOMMANDACTION2958 = new BitSet(new long[]{0x00000000908000F0L});
    public static final BitSet FOLLOW_ruleRNDQUERY_in_ruleCOMMANDACTION2980 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_ruleEcaValue_in_ruleCOMMANDACTION3007 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_ruleCOMMANDACTION_in_ruleCOMMANDACTION3034 = new BitSet(new long[]{0x0000000010000010L});
    public static final BitSet FOLLOW_28_in_ruleCOMMANDACTION3048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleRNDQUERY_in_entryRuleRNDQUERY3084 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleRNDQUERY3094 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_ruleRNDQUERY3131 = new BitSet(new long[]{0x00003C1000000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleRNDQUERY3152 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleRNDQUERY3169 = new BitSet(new long[]{0x0000000408000000L});
    public static final BitSet FOLLOW_27_in_ruleRNDQUERY3187 = new BitSet(new long[]{0x0000000300000000L});
    public static final BitSet FOLLOW_32_in_ruleRNDQUERY3207 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_33_in_ruleRNDQUERY3236 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_ruleRNDQUERY3264 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_ruleRNDQUERY3278 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_ruleRNDQUERY3290 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_36_in_ruleRNDQUERY3302 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleRNDQUERY3319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSource_in_entryRuleSource3360 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSource3370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSource3411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleEcaValue_in_entryRuleEcaValue3451 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleEcaValue3461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleEcaValue3503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleEcaValue3531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_ruleEcaValue3556 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleEcaValue3576 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleEcaValue3589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleEcaValue3613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_DOUBLE_in_ruleEcaValue3641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rulePREDEFINEDSOURCE_in_entryRulePREDEFINEDSOURCE3683 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRulePREDEFINEDSOURCE3694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_rulePREDEFINEDSOURCE3732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_rulePREDEFINEDSOURCE3751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSYSTEMFUNCTION_in_entryRuleSYSTEMFUNCTION3792 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSYSTEMFUNCTION3803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_ruleSYSTEMFUNCTION3841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_ruleSYSTEMFUNCTION3860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_ruleSYSTEMFUNCTION3879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_entryRuleOperator3920 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperator3931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_ruleOperator3969 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_ruleOperator3988 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_ruleOperator4007 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_ruleOperator4026 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_ruleOperator4045 = new BitSet(new long[]{0x0000000000000002L});

}
