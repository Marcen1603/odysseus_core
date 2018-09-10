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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_STRING", "RULE_DOUBLE", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'DEFINE'", "'CONSTANT'", "':'", "';'", "'WINDOWSIZE'", "'TIMEINTERVALL'", "'EVENT'", "'WITH'", "'ON'", "'IF'", "'THEN'", "'AND'", "'${'", "'}'", "'!'", "'queryExists'", "'('", "')'", "'SYSTEM.'", "'GET'", "'prio'", "'MIN'", "'MAX'", "','", "'state'", "'='", "'TimerEvent'", "'QueryEvent'", "'curCPULoad'", "'curMEMLoad'", "'curNETLoad'", "'<'", "'>'", "'<='", "'>='"
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
    public static final int T__46=46;
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
    public String getGrammarFileName() { return "InternalECA.g"; }



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
    // InternalECA.g:64:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalECA.g:64:46: (iv_ruleModel= ruleModel EOF )
            // InternalECA.g:65:2: iv_ruleModel= ruleModel EOF
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
    // InternalECA.g:71:1: ruleModel returns [EObject current=null] : ( ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) ) ) ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_constants_2_0 = null;

        EObject lv_defEvents_3_0 = null;

        EObject lv_windowSize_4_0 = null;

        EObject lv_timeIntervall_5_0 = null;

        EObject lv_rules_6_0 = null;



        	enterRule();

        try {
            // InternalECA.g:77:2: ( ( ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) ) ) )
            // InternalECA.g:78:2: ( ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) ) )
            {
            // InternalECA.g:78:2: ( ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) ) )
            // InternalECA.g:79:3: ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) )
            {
            // InternalECA.g:79:3: ( ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?) )
            // InternalECA.g:80:4: ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?)
            {
             
            			  getUnorderedGroupHelper().enter(grammarAccess.getModelAccess().getUnorderedGroup());
            			
            // InternalECA.g:83:4: ( ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?)
            // InternalECA.g:84:5: ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+ {...}?
            {
            // InternalECA.g:84:5: ( ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) ) )+
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
                else if ( LA5_0 == 12 && getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0) ) {
                    alt5=1;
                }
                else if ( LA5_0 == 20 && ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 1) || getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0) ) ) {
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
            	    // InternalECA.g:85:3: ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) )
            	    {
            	    // InternalECA.g:85:3: ({...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) ) )
            	    // InternalECA.g:86:4: {...}? => ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0) ) {
            	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 0)");
            	    }
            	    // InternalECA.g:86:99: ( ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) ) )
            	    // InternalECA.g:87:5: ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) )
            	    {

            	    					getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup(), 0);
            	    				
            	    // InternalECA.g:90:8: ({...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) ) )
            	    // InternalECA.g:90:9: {...}? => ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) )
            	    {
            	    if ( !((true)) ) {
            	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    }
            	    // InternalECA.g:90:18: ( ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) ) )
            	    // InternalECA.g:90:19: ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) )
            	    {
            	    // InternalECA.g:90:19: ( ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* ) )
            	    // InternalECA.g:91:9: ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* )
            	    {
            	     
            	    								  getUnorderedGroupHelper().enter(grammarAccess.getModelAccess().getUnorderedGroup_0());
            	    								
            	    // InternalECA.g:94:9: ( ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )* )
            	    // InternalECA.g:95:10: ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )*
            	    {
            	    // InternalECA.g:95:10: ( ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) ) | ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) ) )*
            	    loop3:
            	    do {
            	        int alt3=5;
            	        int LA3_0 = input.LA(1);

            	        if ( (LA3_0==12) ) {
            	            int LA3_2 = input.LA(2);

            	            if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 0) ) {
            	                alt3=1;
            	            }
            	            else if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 1) ) {
            	                alt3=2;
            	            }
            	            else if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 2) ) {
            	                alt3=3;
            	            }
            	            else if ( getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 3) ) {
            	                alt3=4;
            	            }


            	        }


            	        switch (alt3) {
            	    	case 1 :
            	    	    // InternalECA.g:96:8: ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) )
            	    	    {
            	    	    // InternalECA.g:96:8: ({...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ ) )
            	    	    // InternalECA.g:97:9: {...}? => ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ )
            	    	    {
            	    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 0) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 0)");
            	    	    }
            	    	    // InternalECA.g:97:106: ( ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+ )
            	    	    // InternalECA.g:98:10: ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+
            	    	    {

            	    	    										getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup_0(), 0);
            	    	    									
            	    	    // InternalECA.g:101:13: ({...}? => ( (lv_constants_2_0= ruleConstant ) ) )+
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
            	    	    	    // InternalECA.g:101:14: {...}? => ( (lv_constants_2_0= ruleConstant ) )
            	    	    	    {
            	    	    	    if ( !((true)) ) {
            	    	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    	    }
            	    	    	    // InternalECA.g:101:23: ( (lv_constants_2_0= ruleConstant ) )
            	    	    	    // InternalECA.g:101:24: (lv_constants_2_0= ruleConstant )
            	    	    	    {
            	    	    	    // InternalECA.g:101:24: (lv_constants_2_0= ruleConstant )
            	    	    	    // InternalECA.g:102:14: lv_constants_2_0= ruleConstant
            	    	    	    {

            	    	    	    														newCompositeNode(grammarAccess.getModelAccess().getConstantsConstantParserRuleCall_0_0_0());
            	    	    	    													
            	    	    	    pushFollow(FOLLOW_3);
            	    	    	    lv_constants_2_0=ruleConstant();

            	    	    	    state._fsp--;


            	    	    	    														if (current==null) {
            	    	    	    															current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    	    														}
            	    	    	    														add(
            	    	    	    															current,
            	    	    	    															"constants",
            	    	    	    															lv_constants_2_0,
            	    	    	    															"de.uniol.inf.is.odysseus.eca.ECA.Constant");
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
            	    	    // InternalECA.g:124:8: ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) )
            	    	    {
            	    	    // InternalECA.g:124:8: ({...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ ) )
            	    	    // InternalECA.g:125:9: {...}? => ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ )
            	    	    {
            	    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 1) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 1)");
            	    	    }
            	    	    // InternalECA.g:125:106: ( ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+ )
            	    	    // InternalECA.g:126:10: ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+
            	    	    {

            	    	    										getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup_0(), 1);
            	    	    									
            	    	    // InternalECA.g:129:13: ({...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) ) )+
            	    	    int cnt2=0;
            	    	    loop2:
            	    	    do {
            	    	        int alt2=2;
            	    	        int LA2_0 = input.LA(1);

            	    	        if ( (LA2_0==12) ) {
            	    	            int LA2_2 = input.LA(2);

            	    	            if ( ((true)) ) {
            	    	                alt2=1;
            	    	            }


            	    	        }


            	    	        switch (alt2) {
            	    	    	case 1 :
            	    	    	    // InternalECA.g:129:14: {...}? => ( (lv_defEvents_3_0= ruleDefinedEvent ) )
            	    	    	    {
            	    	    	    if ( !((true)) ) {
            	    	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    	    }
            	    	    	    // InternalECA.g:129:23: ( (lv_defEvents_3_0= ruleDefinedEvent ) )
            	    	    	    // InternalECA.g:129:24: (lv_defEvents_3_0= ruleDefinedEvent )
            	    	    	    {
            	    	    	    // InternalECA.g:129:24: (lv_defEvents_3_0= ruleDefinedEvent )
            	    	    	    // InternalECA.g:130:14: lv_defEvents_3_0= ruleDefinedEvent
            	    	    	    {

            	    	    	    														newCompositeNode(grammarAccess.getModelAccess().getDefEventsDefinedEventParserRuleCall_0_1_0());
            	    	    	    													
            	    	    	    pushFollow(FOLLOW_3);
            	    	    	    lv_defEvents_3_0=ruleDefinedEvent();

            	    	    	    state._fsp--;


            	    	    	    														if (current==null) {
            	    	    	    															current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    	    														}
            	    	    	    														add(
            	    	    	    															current,
            	    	    	    															"defEvents",
            	    	    	    															lv_defEvents_3_0,
            	    	    	    															"de.uniol.inf.is.odysseus.eca.ECA.DefinedEvent");
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
            	    	    // InternalECA.g:152:8: ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) )
            	    	    {
            	    	    // InternalECA.g:152:8: ({...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) ) )
            	    	    // InternalECA.g:153:9: {...}? => ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) )
            	    	    {
            	    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 2) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 2)");
            	    	    }
            	    	    // InternalECA.g:153:106: ( ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) ) )
            	    	    // InternalECA.g:154:10: ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) )
            	    	    {

            	    	    										getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup_0(), 2);
            	    	    									
            	    	    // InternalECA.g:157:13: ({...}? => ( (lv_windowSize_4_0= ruleWindow ) ) )
            	    	    // InternalECA.g:157:14: {...}? => ( (lv_windowSize_4_0= ruleWindow ) )
            	    	    {
            	    	    if ( !((true)) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    }
            	    	    // InternalECA.g:157:23: ( (lv_windowSize_4_0= ruleWindow ) )
            	    	    // InternalECA.g:157:24: (lv_windowSize_4_0= ruleWindow )
            	    	    {
            	    	    // InternalECA.g:157:24: (lv_windowSize_4_0= ruleWindow )
            	    	    // InternalECA.g:158:14: lv_windowSize_4_0= ruleWindow
            	    	    {

            	    	    														newCompositeNode(grammarAccess.getModelAccess().getWindowSizeWindowParserRuleCall_0_2_0());
            	    	    													
            	    	    pushFollow(FOLLOW_3);
            	    	    lv_windowSize_4_0=ruleWindow();

            	    	    state._fsp--;


            	    	    														if (current==null) {
            	    	    															current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    														}
            	    	    														set(
            	    	    															current,
            	    	    															"windowSize",
            	    	    															lv_windowSize_4_0,
            	    	    															"de.uniol.inf.is.odysseus.eca.ECA.Window");
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
            	    	    // InternalECA.g:180:8: ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) )
            	    	    {
            	    	    // InternalECA.g:180:8: ({...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) ) )
            	    	    // InternalECA.g:181:9: {...}? => ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) )
            	    	    {
            	    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 3) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup_0(), 3)");
            	    	    }
            	    	    // InternalECA.g:181:106: ( ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) ) )
            	    	    // InternalECA.g:182:10: ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) )
            	    	    {

            	    	    										getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup_0(), 3);
            	    	    									
            	    	    // InternalECA.g:185:13: ({...}? => ( (lv_timeIntervall_5_0= ruleTimer ) ) )
            	    	    // InternalECA.g:185:14: {...}? => ( (lv_timeIntervall_5_0= ruleTimer ) )
            	    	    {
            	    	    if ( !((true)) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    }
            	    	    // InternalECA.g:185:23: ( (lv_timeIntervall_5_0= ruleTimer ) )
            	    	    // InternalECA.g:185:24: (lv_timeIntervall_5_0= ruleTimer )
            	    	    {
            	    	    // InternalECA.g:185:24: (lv_timeIntervall_5_0= ruleTimer )
            	    	    // InternalECA.g:186:14: lv_timeIntervall_5_0= ruleTimer
            	    	    {

            	    	    														newCompositeNode(grammarAccess.getModelAccess().getTimeIntervallTimerParserRuleCall_0_3_0());
            	    	    													
            	    	    pushFollow(FOLLOW_3);
            	    	    lv_timeIntervall_5_0=ruleTimer();

            	    	    state._fsp--;


            	    	    														if (current==null) {
            	    	    															current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    														}
            	    	    														set(
            	    	    															current,
            	    	    															"timeIntervall",
            	    	    															lv_timeIntervall_5_0,
            	    	    															"de.uniol.inf.is.odysseus.eca.ECA.Timer");
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
            	    // InternalECA.g:220:3: ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) )
            	    {
            	    // InternalECA.g:220:3: ({...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ ) )
            	    // InternalECA.g:221:4: {...}? => ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ )
            	    {
            	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 1) ) {
            	        throw new FailedPredicateException(input, "ruleModel", "getUnorderedGroupHelper().canSelect(grammarAccess.getModelAccess().getUnorderedGroup(), 1)");
            	    }
            	    // InternalECA.g:221:99: ( ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+ )
            	    // InternalECA.g:222:5: ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+
            	    {

            	    					getUnorderedGroupHelper().select(grammarAccess.getModelAccess().getUnorderedGroup(), 1);
            	    				
            	    // InternalECA.g:225:8: ({...}? => ( (lv_rules_6_0= ruleRule ) ) )+
            	    int cnt4=0;
            	    loop4:
            	    do {
            	        int alt4=2;
            	        int LA4_0 = input.LA(1);

            	        if ( (LA4_0==20) ) {
            	            int LA4_2 = input.LA(2);

            	            if ( ((true)) ) {
            	                alt4=1;
            	            }


            	        }


            	        switch (alt4) {
            	    	case 1 :
            	    	    // InternalECA.g:225:9: {...}? => ( (lv_rules_6_0= ruleRule ) )
            	    	    {
            	    	    if ( !((true)) ) {
            	    	        throw new FailedPredicateException(input, "ruleModel", "true");
            	    	    }
            	    	    // InternalECA.g:225:18: ( (lv_rules_6_0= ruleRule ) )
            	    	    // InternalECA.g:225:19: (lv_rules_6_0= ruleRule )
            	    	    {
            	    	    // InternalECA.g:225:19: (lv_rules_6_0= ruleRule )
            	    	    // InternalECA.g:226:9: lv_rules_6_0= ruleRule
            	    	    {

            	    	    									newCompositeNode(grammarAccess.getModelAccess().getRulesRuleParserRuleCall_1_0());
            	    	    								
            	    	    pushFollow(FOLLOW_3);
            	    	    lv_rules_6_0=ruleRule();

            	    	    state._fsp--;


            	    	    									if (current==null) {
            	    	    										current = createModelElementForParent(grammarAccess.getModelRule());
            	    	    									}
            	    	    									add(
            	    	    										current,
            	    	    										"rules",
            	    	    										lv_rules_6_0,
            	    	    										"de.uniol.inf.is.odysseus.eca.ECA.Rule");
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
    // InternalECA.g:259:1: entryRuleConstant returns [EObject current=null] : iv_ruleConstant= ruleConstant EOF ;
    public final EObject entryRuleConstant() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstant = null;


        try {
            // InternalECA.g:259:49: (iv_ruleConstant= ruleConstant EOF )
            // InternalECA.g:260:2: iv_ruleConstant= ruleConstant EOF
            {
             newCompositeNode(grammarAccess.getConstantRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleConstant=ruleConstant();

            state._fsp--;

             current =iv_ruleConstant; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:266:1: ruleConstant returns [EObject current=null] : (otherlv_0= 'DEFINE' otherlv_1= 'CONSTANT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_constValue_4_0= RULE_INT ) ) otherlv_5= ';' ) ;
    public final EObject ruleConstant() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token lv_constValue_4_0=null;
        Token otherlv_5=null;


        	enterRule();

        try {
            // InternalECA.g:272:2: ( (otherlv_0= 'DEFINE' otherlv_1= 'CONSTANT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_constValue_4_0= RULE_INT ) ) otherlv_5= ';' ) )
            // InternalECA.g:273:2: (otherlv_0= 'DEFINE' otherlv_1= 'CONSTANT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_constValue_4_0= RULE_INT ) ) otherlv_5= ';' )
            {
            // InternalECA.g:273:2: (otherlv_0= 'DEFINE' otherlv_1= 'CONSTANT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_constValue_4_0= RULE_INT ) ) otherlv_5= ';' )
            // InternalECA.g:274:3: otherlv_0= 'DEFINE' otherlv_1= 'CONSTANT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_constValue_4_0= RULE_INT ) ) otherlv_5= ';'
            {
            otherlv_0=(Token)match(input,12,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getConstantAccess().getDEFINEKeyword_0());
            		
            otherlv_1=(Token)match(input,13,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getConstantAccess().getCONSTANTKeyword_1());
            		
            // InternalECA.g:282:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalECA.g:283:4: (lv_name_2_0= RULE_ID )
            {
            // InternalECA.g:283:4: (lv_name_2_0= RULE_ID )
            // InternalECA.g:284:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_2_0, grammarAccess.getConstantAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getConstantRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_7); 

            			newLeafNode(otherlv_3, grammarAccess.getConstantAccess().getColonKeyword_3());
            		
            // InternalECA.g:304:3: ( (lv_constValue_4_0= RULE_INT ) )
            // InternalECA.g:305:4: (lv_constValue_4_0= RULE_INT )
            {
            // InternalECA.g:305:4: (lv_constValue_4_0= RULE_INT )
            // InternalECA.g:306:5: lv_constValue_4_0= RULE_INT
            {
            lv_constValue_4_0=(Token)match(input,RULE_INT,FOLLOW_8); 

            					newLeafNode(lv_constValue_4_0, grammarAccess.getConstantAccess().getConstValueINTTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getConstantRule());
            					}
            					setWithLastConsumed(
            						current,
            						"constValue",
            						lv_constValue_4_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            otherlv_5=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getConstantAccess().getSemicolonKeyword_5());
            		

            }


            }


            	leaveRule();

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
    // InternalECA.g:330:1: entryRuleWindow returns [EObject current=null] : iv_ruleWindow= ruleWindow EOF ;
    public final EObject entryRuleWindow() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWindow = null;


        try {
            // InternalECA.g:330:47: (iv_ruleWindow= ruleWindow EOF )
            // InternalECA.g:331:2: iv_ruleWindow= ruleWindow EOF
            {
             newCompositeNode(grammarAccess.getWindowRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWindow=ruleWindow();

            state._fsp--;

             current =iv_ruleWindow; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:337:1: ruleWindow returns [EObject current=null] : (otherlv_0= 'DEFINE' otherlv_1= 'WINDOWSIZE' otherlv_2= ':' ( (lv_windowValue_3_0= RULE_INT ) ) otherlv_4= ';' ) ;
    public final EObject ruleWindow() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_windowValue_3_0=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalECA.g:343:2: ( (otherlv_0= 'DEFINE' otherlv_1= 'WINDOWSIZE' otherlv_2= ':' ( (lv_windowValue_3_0= RULE_INT ) ) otherlv_4= ';' ) )
            // InternalECA.g:344:2: (otherlv_0= 'DEFINE' otherlv_1= 'WINDOWSIZE' otherlv_2= ':' ( (lv_windowValue_3_0= RULE_INT ) ) otherlv_4= ';' )
            {
            // InternalECA.g:344:2: (otherlv_0= 'DEFINE' otherlv_1= 'WINDOWSIZE' otherlv_2= ':' ( (lv_windowValue_3_0= RULE_INT ) ) otherlv_4= ';' )
            // InternalECA.g:345:3: otherlv_0= 'DEFINE' otherlv_1= 'WINDOWSIZE' otherlv_2= ':' ( (lv_windowValue_3_0= RULE_INT ) ) otherlv_4= ';'
            {
            otherlv_0=(Token)match(input,12,FOLLOW_9); 

            			newLeafNode(otherlv_0, grammarAccess.getWindowAccess().getDEFINEKeyword_0());
            		
            otherlv_1=(Token)match(input,16,FOLLOW_6); 

            			newLeafNode(otherlv_1, grammarAccess.getWindowAccess().getWINDOWSIZEKeyword_1());
            		
            otherlv_2=(Token)match(input,14,FOLLOW_7); 

            			newLeafNode(otherlv_2, grammarAccess.getWindowAccess().getColonKeyword_2());
            		
            // InternalECA.g:357:3: ( (lv_windowValue_3_0= RULE_INT ) )
            // InternalECA.g:358:4: (lv_windowValue_3_0= RULE_INT )
            {
            // InternalECA.g:358:4: (lv_windowValue_3_0= RULE_INT )
            // InternalECA.g:359:5: lv_windowValue_3_0= RULE_INT
            {
            lv_windowValue_3_0=(Token)match(input,RULE_INT,FOLLOW_8); 

            					newLeafNode(lv_windowValue_3_0, grammarAccess.getWindowAccess().getWindowValueINTTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getWindowRule());
            					}
            					setWithLastConsumed(
            						current,
            						"windowValue",
            						lv_windowValue_3_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            otherlv_4=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getWindowAccess().getSemicolonKeyword_4());
            		

            }


            }


            	leaveRule();

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
    // InternalECA.g:383:1: entryRuleTimer returns [EObject current=null] : iv_ruleTimer= ruleTimer EOF ;
    public final EObject entryRuleTimer() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimer = null;


        try {
            // InternalECA.g:383:46: (iv_ruleTimer= ruleTimer EOF )
            // InternalECA.g:384:2: iv_ruleTimer= ruleTimer EOF
            {
             newCompositeNode(grammarAccess.getTimerRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTimer=ruleTimer();

            state._fsp--;

             current =iv_ruleTimer; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:390:1: ruleTimer returns [EObject current=null] : (otherlv_0= 'DEFINE' otherlv_1= 'TIMEINTERVALL' otherlv_2= ':' ( (lv_timerIntervallValue_3_0= RULE_INT ) ) otherlv_4= ';' ) ;
    public final EObject ruleTimer() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_timerIntervallValue_3_0=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalECA.g:396:2: ( (otherlv_0= 'DEFINE' otherlv_1= 'TIMEINTERVALL' otherlv_2= ':' ( (lv_timerIntervallValue_3_0= RULE_INT ) ) otherlv_4= ';' ) )
            // InternalECA.g:397:2: (otherlv_0= 'DEFINE' otherlv_1= 'TIMEINTERVALL' otherlv_2= ':' ( (lv_timerIntervallValue_3_0= RULE_INT ) ) otherlv_4= ';' )
            {
            // InternalECA.g:397:2: (otherlv_0= 'DEFINE' otherlv_1= 'TIMEINTERVALL' otherlv_2= ':' ( (lv_timerIntervallValue_3_0= RULE_INT ) ) otherlv_4= ';' )
            // InternalECA.g:398:3: otherlv_0= 'DEFINE' otherlv_1= 'TIMEINTERVALL' otherlv_2= ':' ( (lv_timerIntervallValue_3_0= RULE_INT ) ) otherlv_4= ';'
            {
            otherlv_0=(Token)match(input,12,FOLLOW_10); 

            			newLeafNode(otherlv_0, grammarAccess.getTimerAccess().getDEFINEKeyword_0());
            		
            otherlv_1=(Token)match(input,17,FOLLOW_6); 

            			newLeafNode(otherlv_1, grammarAccess.getTimerAccess().getTIMEINTERVALLKeyword_1());
            		
            otherlv_2=(Token)match(input,14,FOLLOW_7); 

            			newLeafNode(otherlv_2, grammarAccess.getTimerAccess().getColonKeyword_2());
            		
            // InternalECA.g:410:3: ( (lv_timerIntervallValue_3_0= RULE_INT ) )
            // InternalECA.g:411:4: (lv_timerIntervallValue_3_0= RULE_INT )
            {
            // InternalECA.g:411:4: (lv_timerIntervallValue_3_0= RULE_INT )
            // InternalECA.g:412:5: lv_timerIntervallValue_3_0= RULE_INT
            {
            lv_timerIntervallValue_3_0=(Token)match(input,RULE_INT,FOLLOW_8); 

            					newLeafNode(lv_timerIntervallValue_3_0, grammarAccess.getTimerAccess().getTimerIntervallValueINTTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTimerRule());
            					}
            					setWithLastConsumed(
            						current,
            						"timerIntervallValue",
            						lv_timerIntervallValue_3_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            otherlv_4=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getTimerAccess().getSemicolonKeyword_4());
            		

            }


            }


            	leaveRule();

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
    // InternalECA.g:436:1: entryRuleDefinedEvent returns [EObject current=null] : iv_ruleDefinedEvent= ruleDefinedEvent EOF ;
    public final EObject entryRuleDefinedEvent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDefinedEvent = null;


        try {
            // InternalECA.g:436:53: (iv_ruleDefinedEvent= ruleDefinedEvent EOF )
            // InternalECA.g:437:2: iv_ruleDefinedEvent= ruleDefinedEvent EOF
            {
             newCompositeNode(grammarAccess.getDefinedEventRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDefinedEvent=ruleDefinedEvent();

            state._fsp--;

             current =iv_ruleDefinedEvent; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:443:1: ruleDefinedEvent returns [EObject current=null] : (otherlv_0= 'DEFINE' otherlv_1= 'EVENT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_definedSource_4_0= ruleSource ) ) otherlv_5= 'WITH' ( (lv_definedAttribute_6_0= RULE_ID ) ) ( (lv_definedOperator_7_0= ruleOperator ) ) ( (lv_definedValue_8_0= ruleEcaValue ) ) otherlv_9= ';' ) ;
    public final EObject ruleDefinedEvent() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token lv_definedAttribute_6_0=null;
        Token otherlv_9=null;
        EObject lv_definedSource_4_0 = null;

        AntlrDatatypeRuleToken lv_definedOperator_7_0 = null;

        EObject lv_definedValue_8_0 = null;



        	enterRule();

        try {
            // InternalECA.g:449:2: ( (otherlv_0= 'DEFINE' otherlv_1= 'EVENT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_definedSource_4_0= ruleSource ) ) otherlv_5= 'WITH' ( (lv_definedAttribute_6_0= RULE_ID ) ) ( (lv_definedOperator_7_0= ruleOperator ) ) ( (lv_definedValue_8_0= ruleEcaValue ) ) otherlv_9= ';' ) )
            // InternalECA.g:450:2: (otherlv_0= 'DEFINE' otherlv_1= 'EVENT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_definedSource_4_0= ruleSource ) ) otherlv_5= 'WITH' ( (lv_definedAttribute_6_0= RULE_ID ) ) ( (lv_definedOperator_7_0= ruleOperator ) ) ( (lv_definedValue_8_0= ruleEcaValue ) ) otherlv_9= ';' )
            {
            // InternalECA.g:450:2: (otherlv_0= 'DEFINE' otherlv_1= 'EVENT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_definedSource_4_0= ruleSource ) ) otherlv_5= 'WITH' ( (lv_definedAttribute_6_0= RULE_ID ) ) ( (lv_definedOperator_7_0= ruleOperator ) ) ( (lv_definedValue_8_0= ruleEcaValue ) ) otherlv_9= ';' )
            // InternalECA.g:451:3: otherlv_0= 'DEFINE' otherlv_1= 'EVENT' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (lv_definedSource_4_0= ruleSource ) ) otherlv_5= 'WITH' ( (lv_definedAttribute_6_0= RULE_ID ) ) ( (lv_definedOperator_7_0= ruleOperator ) ) ( (lv_definedValue_8_0= ruleEcaValue ) ) otherlv_9= ';'
            {
            otherlv_0=(Token)match(input,12,FOLLOW_11); 

            			newLeafNode(otherlv_0, grammarAccess.getDefinedEventAccess().getDEFINEKeyword_0());
            		
            otherlv_1=(Token)match(input,18,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getDefinedEventAccess().getEVENTKeyword_1());
            		
            // InternalECA.g:459:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalECA.g:460:4: (lv_name_2_0= RULE_ID )
            {
            // InternalECA.g:460:4: (lv_name_2_0= RULE_ID )
            // InternalECA.g:461:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_2_0, grammarAccess.getDefinedEventAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDefinedEventRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getDefinedEventAccess().getColonKeyword_3());
            		
            // InternalECA.g:481:3: ( (lv_definedSource_4_0= ruleSource ) )
            // InternalECA.g:482:4: (lv_definedSource_4_0= ruleSource )
            {
            // InternalECA.g:482:4: (lv_definedSource_4_0= ruleSource )
            // InternalECA.g:483:5: lv_definedSource_4_0= ruleSource
            {

            					newCompositeNode(grammarAccess.getDefinedEventAccess().getDefinedSourceSourceParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_12);
            lv_definedSource_4_0=ruleSource();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDefinedEventRule());
            					}
            					set(
            						current,
            						"definedSource",
            						lv_definedSource_4_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.Source");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_5=(Token)match(input,19,FOLLOW_5); 

            			newLeafNode(otherlv_5, grammarAccess.getDefinedEventAccess().getWITHKeyword_5());
            		
            // InternalECA.g:504:3: ( (lv_definedAttribute_6_0= RULE_ID ) )
            // InternalECA.g:505:4: (lv_definedAttribute_6_0= RULE_ID )
            {
            // InternalECA.g:505:4: (lv_definedAttribute_6_0= RULE_ID )
            // InternalECA.g:506:5: lv_definedAttribute_6_0= RULE_ID
            {
            lv_definedAttribute_6_0=(Token)match(input,RULE_ID,FOLLOW_13); 

            					newLeafNode(lv_definedAttribute_6_0, grammarAccess.getDefinedEventAccess().getDefinedAttributeIDTerminalRuleCall_6_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDefinedEventRule());
            					}
            					setWithLastConsumed(
            						current,
            						"definedAttribute",
            						lv_definedAttribute_6_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalECA.g:522:3: ( (lv_definedOperator_7_0= ruleOperator ) )
            // InternalECA.g:523:4: (lv_definedOperator_7_0= ruleOperator )
            {
            // InternalECA.g:523:4: (lv_definedOperator_7_0= ruleOperator )
            // InternalECA.g:524:5: lv_definedOperator_7_0= ruleOperator
            {

            					newCompositeNode(grammarAccess.getDefinedEventAccess().getDefinedOperatorOperatorParserRuleCall_7_0());
            				
            pushFollow(FOLLOW_14);
            lv_definedOperator_7_0=ruleOperator();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDefinedEventRule());
            					}
            					set(
            						current,
            						"definedOperator",
            						lv_definedOperator_7_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.Operator");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalECA.g:541:3: ( (lv_definedValue_8_0= ruleEcaValue ) )
            // InternalECA.g:542:4: (lv_definedValue_8_0= ruleEcaValue )
            {
            // InternalECA.g:542:4: (lv_definedValue_8_0= ruleEcaValue )
            // InternalECA.g:543:5: lv_definedValue_8_0= ruleEcaValue
            {

            					newCompositeNode(grammarAccess.getDefinedEventAccess().getDefinedValueEcaValueParserRuleCall_8_0());
            				
            pushFollow(FOLLOW_8);
            lv_definedValue_8_0=ruleEcaValue();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDefinedEventRule());
            					}
            					set(
            						current,
            						"definedValue",
            						lv_definedValue_8_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.EcaValue");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_9=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_9, grammarAccess.getDefinedEventAccess().getSemicolonKeyword_9());
            		

            }


            }


            	leaveRule();

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
    // InternalECA.g:568:1: entryRuleRule returns [EObject current=null] : iv_ruleRule= ruleRule EOF ;
    public final EObject entryRuleRule() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRule = null;


        try {
            // InternalECA.g:568:45: (iv_ruleRule= ruleRule EOF )
            // InternalECA.g:569:2: iv_ruleRule= ruleRule EOF
            {
             newCompositeNode(grammarAccess.getRuleRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRule=ruleRule();

            state._fsp--;

             current =iv_ruleRule; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:575:1: ruleRule returns [EObject current=null] : (otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';' ) ;
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
            // InternalECA.g:581:2: ( (otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';' ) )
            // InternalECA.g:582:2: (otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';' )
            {
            // InternalECA.g:582:2: (otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';' )
            // InternalECA.g:583:3: otherlv_0= 'ON' ( (lv_name_1_0= RULE_ID ) ) ( (lv_source_2_0= ruleRuleSource ) ) otherlv_3= 'IF' ( (lv_ruleConditions_4_0= ruleCONDITIONS ) ) otherlv_5= 'THEN' ( (lv_ruleActions_6_0= ruleACTIONS ) ) otherlv_7= ';'
            {
            otherlv_0=(Token)match(input,20,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getRuleAccess().getONKeyword_0());
            		
            // InternalECA.g:587:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalECA.g:588:4: (lv_name_1_0= RULE_ID )
            {
            // InternalECA.g:588:4: (lv_name_1_0= RULE_ID )
            // InternalECA.g:589:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_15); 

            					newLeafNode(lv_name_1_0, grammarAccess.getRuleAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getRuleRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalECA.g:605:3: ( (lv_source_2_0= ruleRuleSource ) )
            // InternalECA.g:606:4: (lv_source_2_0= ruleRuleSource )
            {
            // InternalECA.g:606:4: (lv_source_2_0= ruleRuleSource )
            // InternalECA.g:607:5: lv_source_2_0= ruleRuleSource
            {

            					newCompositeNode(grammarAccess.getRuleAccess().getSourceRuleSourceParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_16);
            lv_source_2_0=ruleRuleSource();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getRuleRule());
            					}
            					set(
            						current,
            						"source",
            						lv_source_2_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.RuleSource");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,21,FOLLOW_17); 

            			newLeafNode(otherlv_3, grammarAccess.getRuleAccess().getIFKeyword_3());
            		
            // InternalECA.g:628:3: ( (lv_ruleConditions_4_0= ruleCONDITIONS ) )
            // InternalECA.g:629:4: (lv_ruleConditions_4_0= ruleCONDITIONS )
            {
            // InternalECA.g:629:4: (lv_ruleConditions_4_0= ruleCONDITIONS )
            // InternalECA.g:630:5: lv_ruleConditions_4_0= ruleCONDITIONS
            {

            					newCompositeNode(grammarAccess.getRuleAccess().getRuleConditionsCONDITIONSParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_18);
            lv_ruleConditions_4_0=ruleCONDITIONS();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getRuleRule());
            					}
            					set(
            						current,
            						"ruleConditions",
            						lv_ruleConditions_4_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.CONDITIONS");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_5=(Token)match(input,22,FOLLOW_5); 

            			newLeafNode(otherlv_5, grammarAccess.getRuleAccess().getTHENKeyword_5());
            		
            // InternalECA.g:651:3: ( (lv_ruleActions_6_0= ruleACTIONS ) )
            // InternalECA.g:652:4: (lv_ruleActions_6_0= ruleACTIONS )
            {
            // InternalECA.g:652:4: (lv_ruleActions_6_0= ruleACTIONS )
            // InternalECA.g:653:5: lv_ruleActions_6_0= ruleACTIONS
            {

            					newCompositeNode(grammarAccess.getRuleAccess().getRuleActionsACTIONSParserRuleCall_6_0());
            				
            pushFollow(FOLLOW_8);
            lv_ruleActions_6_0=ruleACTIONS();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getRuleRule());
            					}
            					set(
            						current,
            						"ruleActions",
            						lv_ruleActions_6_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.ACTIONS");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_7=(Token)match(input,15,FOLLOW_2); 

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
    // InternalECA.g:678:1: entryRuleCONDITIONS returns [EObject current=null] : iv_ruleCONDITIONS= ruleCONDITIONS EOF ;
    public final EObject entryRuleCONDITIONS() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCONDITIONS = null;


        try {
            // InternalECA.g:678:51: (iv_ruleCONDITIONS= ruleCONDITIONS EOF )
            // InternalECA.g:679:2: iv_ruleCONDITIONS= ruleCONDITIONS EOF
            {
             newCompositeNode(grammarAccess.getCONDITIONSRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCONDITIONS=ruleCONDITIONS();

            state._fsp--;

             current =iv_ruleCONDITIONS; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:685:1: ruleCONDITIONS returns [EObject current=null] : (this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )* ) ;
    public final EObject ruleCONDITIONS() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_SUBCONDITION_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalECA.g:691:2: ( (this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )* ) )
            // InternalECA.g:692:2: (this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )* )
            {
            // InternalECA.g:692:2: (this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )* )
            // InternalECA.g:693:3: this_SUBCONDITION_0= ruleSUBCONDITION ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )*
            {

            			newCompositeNode(grammarAccess.getCONDITIONSAccess().getSUBCONDITIONParserRuleCall_0());
            		
            pushFollow(FOLLOW_19);
            this_SUBCONDITION_0=ruleSUBCONDITION();

            state._fsp--;


            			current = this_SUBCONDITION_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalECA.g:701:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==23) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalECA.g:702:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBCONDITION ) )
            	    {
            	    // InternalECA.g:702:4: ()
            	    // InternalECA.g:703:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getCONDITIONSAccess().getCONDITIONSLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,23,FOLLOW_17); 

            	    				newLeafNode(otherlv_2, grammarAccess.getCONDITIONSAccess().getANDKeyword_1_1());
            	    			
            	    // InternalECA.g:713:4: ( (lv_right_3_0= ruleSUBCONDITION ) )
            	    // InternalECA.g:714:5: (lv_right_3_0= ruleSUBCONDITION )
            	    {
            	    // InternalECA.g:714:5: (lv_right_3_0= ruleSUBCONDITION )
            	    // InternalECA.g:715:6: lv_right_3_0= ruleSUBCONDITION
            	    {

            	    						newCompositeNode(grammarAccess.getCONDITIONSAccess().getRightSUBCONDITIONParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_19);
            	    lv_right_3_0=ruleSUBCONDITION();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getCONDITIONSRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.eca.ECA.SUBCONDITION");
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
    // InternalECA.g:737:1: entryRuleSUBCONDITION returns [EObject current=null] : iv_ruleSUBCONDITION= ruleSUBCONDITION EOF ;
    public final EObject entryRuleSUBCONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSUBCONDITION = null;


        try {
            // InternalECA.g:737:53: (iv_ruleSUBCONDITION= ruleSUBCONDITION EOF )
            // InternalECA.g:738:2: iv_ruleSUBCONDITION= ruleSUBCONDITION EOF
            {
             newCompositeNode(grammarAccess.getSUBCONDITIONRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSUBCONDITION=ruleSUBCONDITION();

            state._fsp--;

             current =iv_ruleSUBCONDITION; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:744:1: ruleSUBCONDITION returns [EObject current=null] : ( ( (lv_subsource_0_0= ruleSOURCECONDITION ) ) | ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) ) | ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) ) | ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? ) | ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) ) ) ;
    public final EObject ruleSUBCONDITION() throws RecognitionException {
        EObject current = null;

        EObject lv_subsource_0_0 = null;

        EObject lv_subsys_1_0 = null;

        EObject lv_subfree_3_0 = null;

        EObject lv_submap_5_0 = null;

        EObject lv_queryCond_7_0 = null;



        	enterRule();

        try {
            // InternalECA.g:750:2: ( ( ( (lv_subsource_0_0= ruleSOURCECONDITION ) ) | ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) ) | ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) ) | ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? ) | ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) ) ) )
            // InternalECA.g:751:2: ( ( (lv_subsource_0_0= ruleSOURCECONDITION ) ) | ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) ) | ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) ) | ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? ) | ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) ) )
            {
            // InternalECA.g:751:2: ( ( (lv_subsource_0_0= ruleSOURCECONDITION ) ) | ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) ) | ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) ) | ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? ) | ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) ) )
            int alt8=5;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                alt8=1;
                }
                break;
            case 30:
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
            case 22:
            case 23:
            case 31:
                {
                alt8=4;
                }
                break;
            case 26:
            case 27:
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
                    // InternalECA.g:752:3: ( (lv_subsource_0_0= ruleSOURCECONDITION ) )
                    {
                    // InternalECA.g:752:3: ( (lv_subsource_0_0= ruleSOURCECONDITION ) )
                    // InternalECA.g:753:4: (lv_subsource_0_0= ruleSOURCECONDITION )
                    {
                    // InternalECA.g:753:4: (lv_subsource_0_0= ruleSOURCECONDITION )
                    // InternalECA.g:754:5: lv_subsource_0_0= ruleSOURCECONDITION
                    {

                    					newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getSubsourceSOURCECONDITIONParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_subsource_0_0=ruleSOURCECONDITION();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                    					}
                    					set(
                    						current,
                    						"subsource",
                    						lv_subsource_0_0,
                    						"de.uniol.inf.is.odysseus.eca.ECA.SOURCECONDITION");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalECA.g:772:3: ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) )
                    {
                    // InternalECA.g:772:3: ( (lv_subsys_1_0= ruleSYSTEMCONDITION ) )
                    // InternalECA.g:773:4: (lv_subsys_1_0= ruleSYSTEMCONDITION )
                    {
                    // InternalECA.g:773:4: (lv_subsys_1_0= ruleSYSTEMCONDITION )
                    // InternalECA.g:774:5: lv_subsys_1_0= ruleSYSTEMCONDITION
                    {

                    					newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getSubsysSYSTEMCONDITIONParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_subsys_1_0=ruleSYSTEMCONDITION();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                    					}
                    					set(
                    						current,
                    						"subsys",
                    						lv_subsys_1_0,
                    						"de.uniol.inf.is.odysseus.eca.ECA.SYSTEMCONDITION");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalECA.g:792:3: ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) )
                    {
                    // InternalECA.g:792:3: ( () ( (lv_subfree_3_0= ruleFREECONDITION ) ) )
                    // InternalECA.g:793:4: () ( (lv_subfree_3_0= ruleFREECONDITION ) )
                    {
                    // InternalECA.g:793:4: ()
                    // InternalECA.g:794:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getSUBCONDITIONAccess().getSUBCONDITIONAction_2_0(),
                    						current);
                    				

                    }

                    // InternalECA.g:800:4: ( (lv_subfree_3_0= ruleFREECONDITION ) )
                    // InternalECA.g:801:5: (lv_subfree_3_0= ruleFREECONDITION )
                    {
                    // InternalECA.g:801:5: (lv_subfree_3_0= ruleFREECONDITION )
                    // InternalECA.g:802:6: lv_subfree_3_0= ruleFREECONDITION
                    {

                    						newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getSubfreeFREECONDITIONParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_subfree_3_0=ruleFREECONDITION();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                    						}
                    						set(
                    							current,
                    							"subfree",
                    							lv_subfree_3_0,
                    							"de.uniol.inf.is.odysseus.eca.ECA.FREECONDITION");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalECA.g:821:3: ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? )
                    {
                    // InternalECA.g:821:3: ( () ( (lv_submap_5_0= ruleMAPCONDITION ) )? )
                    // InternalECA.g:822:4: () ( (lv_submap_5_0= ruleMAPCONDITION ) )?
                    {
                    // InternalECA.g:822:4: ()
                    // InternalECA.g:823:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getSUBCONDITIONAccess().getSUBCONDITIONAction_3_0(),
                    						current);
                    				

                    }

                    // InternalECA.g:829:4: ( (lv_submap_5_0= ruleMAPCONDITION ) )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==31) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // InternalECA.g:830:5: (lv_submap_5_0= ruleMAPCONDITION )
                            {
                            // InternalECA.g:830:5: (lv_submap_5_0= ruleMAPCONDITION )
                            // InternalECA.g:831:6: lv_submap_5_0= ruleMAPCONDITION
                            {

                            						newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getSubmapMAPCONDITIONParserRuleCall_3_1_0());
                            					
                            pushFollow(FOLLOW_2);
                            lv_submap_5_0=ruleMAPCONDITION();

                            state._fsp--;


                            						if (current==null) {
                            							current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                            						}
                            						set(
                            							current,
                            							"submap",
                            							lv_submap_5_0,
                            							"de.uniol.inf.is.odysseus.eca.ECA.MAPCONDITION");
                            						afterParserOrEnumRuleCall();
                            					

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalECA.g:850:3: ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) )
                    {
                    // InternalECA.g:850:3: ( () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) ) )
                    // InternalECA.g:851:4: () ( (lv_queryCond_7_0= ruleQUERYCONDITION ) )
                    {
                    // InternalECA.g:851:4: ()
                    // InternalECA.g:852:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getSUBCONDITIONAccess().getSUBCONDITIONAction_4_0(),
                    						current);
                    				

                    }

                    // InternalECA.g:858:4: ( (lv_queryCond_7_0= ruleQUERYCONDITION ) )
                    // InternalECA.g:859:5: (lv_queryCond_7_0= ruleQUERYCONDITION )
                    {
                    // InternalECA.g:859:5: (lv_queryCond_7_0= ruleQUERYCONDITION )
                    // InternalECA.g:860:6: lv_queryCond_7_0= ruleQUERYCONDITION
                    {

                    						newCompositeNode(grammarAccess.getSUBCONDITIONAccess().getQueryCondQUERYCONDITIONParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_queryCond_7_0=ruleQUERYCONDITION();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSUBCONDITIONRule());
                    						}
                    						set(
                    							current,
                    							"queryCond",
                    							lv_queryCond_7_0,
                    							"de.uniol.inf.is.odysseus.eca.ECA.QUERYCONDITION");
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
    // InternalECA.g:882:1: entryRuleRuleSource returns [EObject current=null] : iv_ruleRuleSource= ruleRuleSource EOF ;
    public final EObject entryRuleRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleSource = null;


        try {
            // InternalECA.g:882:51: (iv_ruleRuleSource= ruleRuleSource EOF )
            // InternalECA.g:883:2: iv_ruleRuleSource= ruleRuleSource EOF
            {
             newCompositeNode(grammarAccess.getRuleSourceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRuleSource=ruleRuleSource();

            state._fsp--;

             current =iv_ruleRuleSource; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:889:1: ruleRuleSource returns [EObject current=null] : ( (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' ) | ( (lv_newSource_3_0= ruleSource ) ) | ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) ) ) ;
    public final EObject ruleRuleSource() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        EObject lv_newSource_3_0 = null;

        AntlrDatatypeRuleToken lv_preSource_4_0 = null;



        	enterRule();

        try {
            // InternalECA.g:895:2: ( ( (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' ) | ( (lv_newSource_3_0= ruleSource ) ) | ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) ) ) )
            // InternalECA.g:896:2: ( (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' ) | ( (lv_newSource_3_0= ruleSource ) ) | ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) ) )
            {
            // InternalECA.g:896:2: ( (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' ) | ( (lv_newSource_3_0= ruleSource ) ) | ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) ) )
            int alt9=3;
            switch ( input.LA(1) ) {
            case 24:
                {
                alt9=1;
                }
                break;
            case RULE_ID:
                {
                alt9=2;
                }
                break;
            case 38:
            case 39:
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
                    // InternalECA.g:897:3: (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' )
                    {
                    // InternalECA.g:897:3: (otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}' )
                    // InternalECA.g:898:4: otherlv_0= '${' ( (otherlv_1= RULE_ID ) ) otherlv_2= '}'
                    {
                    otherlv_0=(Token)match(input,24,FOLLOW_5); 

                    				newLeafNode(otherlv_0, grammarAccess.getRuleSourceAccess().getDollarSignLeftCurlyBracketKeyword_0_0());
                    			
                    // InternalECA.g:902:4: ( (otherlv_1= RULE_ID ) )
                    // InternalECA.g:903:5: (otherlv_1= RULE_ID )
                    {
                    // InternalECA.g:903:5: (otherlv_1= RULE_ID )
                    // InternalECA.g:904:6: otherlv_1= RULE_ID
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRuleSourceRule());
                    						}
                    					
                    otherlv_1=(Token)match(input,RULE_ID,FOLLOW_20); 

                    						newLeafNode(otherlv_1, grammarAccess.getRuleSourceAccess().getDefSourceDefinedEventCrossReference_0_1_0());
                    					

                    }


                    }

                    otherlv_2=(Token)match(input,25,FOLLOW_2); 

                    				newLeafNode(otherlv_2, grammarAccess.getRuleSourceAccess().getRightCurlyBracketKeyword_0_2());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalECA.g:921:3: ( (lv_newSource_3_0= ruleSource ) )
                    {
                    // InternalECA.g:921:3: ( (lv_newSource_3_0= ruleSource ) )
                    // InternalECA.g:922:4: (lv_newSource_3_0= ruleSource )
                    {
                    // InternalECA.g:922:4: (lv_newSource_3_0= ruleSource )
                    // InternalECA.g:923:5: lv_newSource_3_0= ruleSource
                    {

                    					newCompositeNode(grammarAccess.getRuleSourceAccess().getNewSourceSourceParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_newSource_3_0=ruleSource();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getRuleSourceRule());
                    					}
                    					set(
                    						current,
                    						"newSource",
                    						lv_newSource_3_0,
                    						"de.uniol.inf.is.odysseus.eca.ECA.Source");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalECA.g:941:3: ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) )
                    {
                    // InternalECA.g:941:3: ( (lv_preSource_4_0= rulePREDEFINEDSOURCE ) )
                    // InternalECA.g:942:4: (lv_preSource_4_0= rulePREDEFINEDSOURCE )
                    {
                    // InternalECA.g:942:4: (lv_preSource_4_0= rulePREDEFINEDSOURCE )
                    // InternalECA.g:943:5: lv_preSource_4_0= rulePREDEFINEDSOURCE
                    {

                    					newCompositeNode(grammarAccess.getRuleSourceAccess().getPreSourcePREDEFINEDSOURCEParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_preSource_4_0=rulePREDEFINEDSOURCE();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getRuleSourceRule());
                    					}
                    					set(
                    						current,
                    						"preSource",
                    						lv_preSource_4_0,
                    						"de.uniol.inf.is.odysseus.eca.ECA.PREDEFINEDSOURCE");
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
    // InternalECA.g:964:1: entryRuleSOURCECONDITION returns [EObject current=null] : iv_ruleSOURCECONDITION= ruleSOURCECONDITION EOF ;
    public final EObject entryRuleSOURCECONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSOURCECONDITION = null;


        try {
            // InternalECA.g:964:56: (iv_ruleSOURCECONDITION= ruleSOURCECONDITION EOF )
            // InternalECA.g:965:2: iv_ruleSOURCECONDITION= ruleSOURCECONDITION EOF
            {
             newCompositeNode(grammarAccess.getSOURCECONDITIONRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSOURCECONDITION=ruleSOURCECONDITION();

            state._fsp--;

             current =iv_ruleSOURCECONDITION; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:971:1: ruleSOURCECONDITION returns [EObject current=null] : ( ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) ) ) ;
    public final EObject ruleSOURCECONDITION() throws RecognitionException {
        EObject current = null;

        Token lv_condAttribute_0_0=null;
        AntlrDatatypeRuleToken lv_operator_1_0 = null;

        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalECA.g:977:2: ( ( ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) ) ) )
            // InternalECA.g:978:2: ( ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) ) )
            {
            // InternalECA.g:978:2: ( ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) ) )
            // InternalECA.g:979:3: ( (lv_condAttribute_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_value_2_0= ruleEcaValue ) )
            {
            // InternalECA.g:979:3: ( (lv_condAttribute_0_0= RULE_ID ) )
            // InternalECA.g:980:4: (lv_condAttribute_0_0= RULE_ID )
            {
            // InternalECA.g:980:4: (lv_condAttribute_0_0= RULE_ID )
            // InternalECA.g:981:5: lv_condAttribute_0_0= RULE_ID
            {
            lv_condAttribute_0_0=(Token)match(input,RULE_ID,FOLLOW_13); 

            					newLeafNode(lv_condAttribute_0_0, grammarAccess.getSOURCECONDITIONAccess().getCondAttributeIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSOURCECONDITIONRule());
            					}
            					setWithLastConsumed(
            						current,
            						"condAttribute",
            						lv_condAttribute_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalECA.g:997:3: ( (lv_operator_1_0= ruleOperator ) )
            // InternalECA.g:998:4: (lv_operator_1_0= ruleOperator )
            {
            // InternalECA.g:998:4: (lv_operator_1_0= ruleOperator )
            // InternalECA.g:999:5: lv_operator_1_0= ruleOperator
            {

            					newCompositeNode(grammarAccess.getSOURCECONDITIONAccess().getOperatorOperatorParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_14);
            lv_operator_1_0=ruleOperator();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSOURCECONDITIONRule());
            					}
            					set(
            						current,
            						"operator",
            						lv_operator_1_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.Operator");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalECA.g:1016:3: ( (lv_value_2_0= ruleEcaValue ) )
            // InternalECA.g:1017:4: (lv_value_2_0= ruleEcaValue )
            {
            // InternalECA.g:1017:4: (lv_value_2_0= ruleEcaValue )
            // InternalECA.g:1018:5: lv_value_2_0= ruleEcaValue
            {

            					newCompositeNode(grammarAccess.getSOURCECONDITIONAccess().getValueEcaValueParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_value_2_0=ruleEcaValue();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSOURCECONDITIONRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_2_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.EcaValue");
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
    // InternalECA.g:1039:1: entryRuleQUERYCONDITION returns [EObject current=null] : iv_ruleQUERYCONDITION= ruleQUERYCONDITION EOF ;
    public final EObject entryRuleQUERYCONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQUERYCONDITION = null;


        try {
            // InternalECA.g:1039:55: (iv_ruleQUERYCONDITION= ruleQUERYCONDITION EOF )
            // InternalECA.g:1040:2: iv_ruleQUERYCONDITION= ruleQUERYCONDITION EOF
            {
             newCompositeNode(grammarAccess.getQUERYCONDITIONRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQUERYCONDITION=ruleQUERYCONDITION();

            state._fsp--;

             current =iv_ruleQUERYCONDITION; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1046:1: ruleQUERYCONDITION returns [EObject current=null] : ( ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')' ) ;
    public final EObject ruleQUERYCONDITION() throws RecognitionException {
        EObject current = null;

        Token lv_queryNot_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_queryFunct_3_0 = null;



        	enterRule();

        try {
            // InternalECA.g:1052:2: ( ( ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')' ) )
            // InternalECA.g:1053:2: ( ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')' )
            {
            // InternalECA.g:1053:2: ( ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')' )
            // InternalECA.g:1054:3: ( (lv_queryNot_0_0= '!' ) )? otherlv_1= 'queryExists' otherlv_2= '(' ( (lv_queryFunct_3_0= ruleRNDQUERY ) ) otherlv_4= ')'
            {
            // InternalECA.g:1054:3: ( (lv_queryNot_0_0= '!' ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==26) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalECA.g:1055:4: (lv_queryNot_0_0= '!' )
                    {
                    // InternalECA.g:1055:4: (lv_queryNot_0_0= '!' )
                    // InternalECA.g:1056:5: lv_queryNot_0_0= '!'
                    {
                    lv_queryNot_0_0=(Token)match(input,26,FOLLOW_21); 

                    					newLeafNode(lv_queryNot_0_0, grammarAccess.getQUERYCONDITIONAccess().getQueryNotExclamationMarkKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getQUERYCONDITIONRule());
                    					}
                    					setWithLastConsumed(current, "queryNot", lv_queryNot_0_0, "!");
                    				

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,27,FOLLOW_22); 

            			newLeafNode(otherlv_1, grammarAccess.getQUERYCONDITIONAccess().getQueryExistsKeyword_1());
            		
            otherlv_2=(Token)match(input,28,FOLLOW_23); 

            			newLeafNode(otherlv_2, grammarAccess.getQUERYCONDITIONAccess().getLeftParenthesisKeyword_2());
            		
            // InternalECA.g:1076:3: ( (lv_queryFunct_3_0= ruleRNDQUERY ) )
            // InternalECA.g:1077:4: (lv_queryFunct_3_0= ruleRNDQUERY )
            {
            // InternalECA.g:1077:4: (lv_queryFunct_3_0= ruleRNDQUERY )
            // InternalECA.g:1078:5: lv_queryFunct_3_0= ruleRNDQUERY
            {

            					newCompositeNode(grammarAccess.getQUERYCONDITIONAccess().getQueryFunctRNDQUERYParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_24);
            lv_queryFunct_3_0=ruleRNDQUERY();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getQUERYCONDITIONRule());
            					}
            					set(
            						current,
            						"queryFunct",
            						lv_queryFunct_3_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.RNDQUERY");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_4=(Token)match(input,29,FOLLOW_2); 

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
    // InternalECA.g:1103:1: entryRuleSYSTEMCONDITION returns [EObject current=null] : iv_ruleSYSTEMCONDITION= ruleSYSTEMCONDITION EOF ;
    public final EObject entryRuleSYSTEMCONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSYSTEMCONDITION = null;


        try {
            // InternalECA.g:1103:56: (iv_ruleSYSTEMCONDITION= ruleSYSTEMCONDITION EOF )
            // InternalECA.g:1104:2: iv_ruleSYSTEMCONDITION= ruleSYSTEMCONDITION EOF
            {
             newCompositeNode(grammarAccess.getSYSTEMCONDITIONRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSYSTEMCONDITION=ruleSYSTEMCONDITION();

            state._fsp--;

             current =iv_ruleSYSTEMCONDITION; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1110:1: ruleSYSTEMCONDITION returns [EObject current=null] : (otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) ) ) ;
    public final EObject ruleSYSTEMCONDITION() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        AntlrDatatypeRuleToken lv_systemAttribute_1_0 = null;

        AntlrDatatypeRuleToken lv_operator_2_0 = null;

        EObject lv_value_3_0 = null;



        	enterRule();

        try {
            // InternalECA.g:1116:2: ( (otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) ) ) )
            // InternalECA.g:1117:2: (otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) ) )
            {
            // InternalECA.g:1117:2: (otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) ) )
            // InternalECA.g:1118:3: otherlv_0= 'SYSTEM.' ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) ) ( (lv_operator_2_0= ruleOperator ) ) ( (lv_value_3_0= ruleEcaValue ) )
            {
            otherlv_0=(Token)match(input,30,FOLLOW_25); 

            			newLeafNode(otherlv_0, grammarAccess.getSYSTEMCONDITIONAccess().getSYSTEMKeyword_0());
            		
            // InternalECA.g:1122:3: ( (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION ) )
            // InternalECA.g:1123:4: (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION )
            {
            // InternalECA.g:1123:4: (lv_systemAttribute_1_0= ruleSYSTEMFUNCTION )
            // InternalECA.g:1124:5: lv_systemAttribute_1_0= ruleSYSTEMFUNCTION
            {

            					newCompositeNode(grammarAccess.getSYSTEMCONDITIONAccess().getSystemAttributeSYSTEMFUNCTIONParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_13);
            lv_systemAttribute_1_0=ruleSYSTEMFUNCTION();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSYSTEMCONDITIONRule());
            					}
            					set(
            						current,
            						"systemAttribute",
            						lv_systemAttribute_1_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.SYSTEMFUNCTION");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalECA.g:1141:3: ( (lv_operator_2_0= ruleOperator ) )
            // InternalECA.g:1142:4: (lv_operator_2_0= ruleOperator )
            {
            // InternalECA.g:1142:4: (lv_operator_2_0= ruleOperator )
            // InternalECA.g:1143:5: lv_operator_2_0= ruleOperator
            {

            					newCompositeNode(grammarAccess.getSYSTEMCONDITIONAccess().getOperatorOperatorParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_14);
            lv_operator_2_0=ruleOperator();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSYSTEMCONDITIONRule());
            					}
            					set(
            						current,
            						"operator",
            						lv_operator_2_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.Operator");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalECA.g:1160:3: ( (lv_value_3_0= ruleEcaValue ) )
            // InternalECA.g:1161:4: (lv_value_3_0= ruleEcaValue )
            {
            // InternalECA.g:1161:4: (lv_value_3_0= ruleEcaValue )
            // InternalECA.g:1162:5: lv_value_3_0= ruleEcaValue
            {

            					newCompositeNode(grammarAccess.getSYSTEMCONDITIONAccess().getValueEcaValueParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_value_3_0=ruleEcaValue();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSYSTEMCONDITIONRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_3_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.EcaValue");
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
    // InternalECA.g:1183:1: entryRuleFREECONDITION returns [EObject current=null] : iv_ruleFREECONDITION= ruleFREECONDITION EOF ;
    public final EObject entryRuleFREECONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFREECONDITION = null;


        try {
            // InternalECA.g:1183:54: (iv_ruleFREECONDITION= ruleFREECONDITION EOF )
            // InternalECA.g:1184:2: iv_ruleFREECONDITION= ruleFREECONDITION EOF
            {
             newCompositeNode(grammarAccess.getFREECONDITIONRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFREECONDITION=ruleFREECONDITION();

            state._fsp--;

             current =iv_ruleFREECONDITION; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1190:1: ruleFREECONDITION returns [EObject current=null] : ( (lv_freeCondition_0_0= RULE_STRING ) ) ;
    public final EObject ruleFREECONDITION() throws RecognitionException {
        EObject current = null;

        Token lv_freeCondition_0_0=null;


        	enterRule();

        try {
            // InternalECA.g:1196:2: ( ( (lv_freeCondition_0_0= RULE_STRING ) ) )
            // InternalECA.g:1197:2: ( (lv_freeCondition_0_0= RULE_STRING ) )
            {
            // InternalECA.g:1197:2: ( (lv_freeCondition_0_0= RULE_STRING ) )
            // InternalECA.g:1198:3: (lv_freeCondition_0_0= RULE_STRING )
            {
            // InternalECA.g:1198:3: (lv_freeCondition_0_0= RULE_STRING )
            // InternalECA.g:1199:4: lv_freeCondition_0_0= RULE_STRING
            {
            lv_freeCondition_0_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            				newLeafNode(lv_freeCondition_0_0, grammarAccess.getFREECONDITIONAccess().getFreeConditionSTRINGTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getFREECONDITIONRule());
            				}
            				setWithLastConsumed(
            					current,
            					"freeCondition",
            					lv_freeCondition_0_0,
            					"org.eclipse.xtext.common.Terminals.STRING");
            			

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
    // InternalECA.g:1218:1: entryRuleMAPCONDITION returns [EObject current=null] : iv_ruleMAPCONDITION= ruleMAPCONDITION EOF ;
    public final EObject entryRuleMAPCONDITION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMAPCONDITION = null;


        try {
            // InternalECA.g:1218:53: (iv_ruleMAPCONDITION= ruleMAPCONDITION EOF )
            // InternalECA.g:1219:2: iv_ruleMAPCONDITION= ruleMAPCONDITION EOF
            {
             newCompositeNode(grammarAccess.getMAPCONDITIONRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMAPCONDITION=ruleMAPCONDITION();

            state._fsp--;

             current =iv_ruleMAPCONDITION; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1225:1: ruleMAPCONDITION returns [EObject current=null] : (otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) ) ) ;
    public final EObject ruleMAPCONDITION() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_mapCond_1_0=null;


        	enterRule();

        try {
            // InternalECA.g:1231:2: ( (otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) ) ) )
            // InternalECA.g:1232:2: (otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) ) )
            {
            // InternalECA.g:1232:2: (otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) ) )
            // InternalECA.g:1233:3: otherlv_0= 'GET' ( (lv_mapCond_1_0= RULE_STRING ) )
            {
            otherlv_0=(Token)match(input,31,FOLLOW_26); 

            			newLeafNode(otherlv_0, grammarAccess.getMAPCONDITIONAccess().getGETKeyword_0());
            		
            // InternalECA.g:1237:3: ( (lv_mapCond_1_0= RULE_STRING ) )
            // InternalECA.g:1238:4: (lv_mapCond_1_0= RULE_STRING )
            {
            // InternalECA.g:1238:4: (lv_mapCond_1_0= RULE_STRING )
            // InternalECA.g:1239:5: lv_mapCond_1_0= RULE_STRING
            {
            lv_mapCond_1_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            					newLeafNode(lv_mapCond_1_0, grammarAccess.getMAPCONDITIONAccess().getMapCondSTRINGTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getMAPCONDITIONRule());
            					}
            					setWithLastConsumed(
            						current,
            						"mapCond",
            						lv_mapCond_1_0,
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
    // $ANTLR end "ruleMAPCONDITION"


    // $ANTLR start "entryRuleACTIONS"
    // InternalECA.g:1259:1: entryRuleACTIONS returns [EObject current=null] : iv_ruleACTIONS= ruleACTIONS EOF ;
    public final EObject entryRuleACTIONS() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleACTIONS = null;


        try {
            // InternalECA.g:1259:48: (iv_ruleACTIONS= ruleACTIONS EOF )
            // InternalECA.g:1260:2: iv_ruleACTIONS= ruleACTIONS EOF
            {
             newCompositeNode(grammarAccess.getACTIONSRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleACTIONS=ruleACTIONS();

            state._fsp--;

             current =iv_ruleACTIONS; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1266:1: ruleACTIONS returns [EObject current=null] : (this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )* ) ;
    public final EObject ruleACTIONS() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_SUBACTIONS_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalECA.g:1272:2: ( (this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )* ) )
            // InternalECA.g:1273:2: (this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )* )
            {
            // InternalECA.g:1273:2: (this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )* )
            // InternalECA.g:1274:3: this_SUBACTIONS_0= ruleSUBACTIONS ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )*
            {

            			newCompositeNode(grammarAccess.getACTIONSAccess().getSUBACTIONSParserRuleCall_0());
            		
            pushFollow(FOLLOW_19);
            this_SUBACTIONS_0=ruleSUBACTIONS();

            state._fsp--;


            			current = this_SUBACTIONS_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalECA.g:1282:3: ( () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) ) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==23) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalECA.g:1283:4: () otherlv_2= 'AND' ( (lv_right_3_0= ruleSUBACTIONS ) )
            	    {
            	    // InternalECA.g:1283:4: ()
            	    // InternalECA.g:1284:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getACTIONSAccess().getACTIONSLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    otherlv_2=(Token)match(input,23,FOLLOW_5); 

            	    				newLeafNode(otherlv_2, grammarAccess.getACTIONSAccess().getANDKeyword_1_1());
            	    			
            	    // InternalECA.g:1294:4: ( (lv_right_3_0= ruleSUBACTIONS ) )
            	    // InternalECA.g:1295:5: (lv_right_3_0= ruleSUBACTIONS )
            	    {
            	    // InternalECA.g:1295:5: (lv_right_3_0= ruleSUBACTIONS )
            	    // InternalECA.g:1296:6: lv_right_3_0= ruleSUBACTIONS
            	    {

            	    						newCompositeNode(grammarAccess.getACTIONSAccess().getRightSUBACTIONSParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_19);
            	    lv_right_3_0=ruleSUBACTIONS();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getACTIONSRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"de.uniol.inf.is.odysseus.eca.ECA.SUBACTIONS");
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
    // InternalECA.g:1318:1: entryRuleSUBACTIONS returns [EObject current=null] : iv_ruleSUBACTIONS= ruleSUBACTIONS EOF ;
    public final EObject entryRuleSUBACTIONS() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSUBACTIONS = null;


        try {
            // InternalECA.g:1318:51: (iv_ruleSUBACTIONS= ruleSUBACTIONS EOF )
            // InternalECA.g:1319:2: iv_ruleSUBACTIONS= ruleSUBACTIONS EOF
            {
             newCompositeNode(grammarAccess.getSUBACTIONSRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSUBACTIONS=ruleSUBACTIONS();

            state._fsp--;

             current =iv_ruleSUBACTIONS; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1325:1: ruleSUBACTIONS returns [EObject current=null] : ( (lv_comAction_0_0= ruleCOMMANDACTION ) ) ;
    public final EObject ruleSUBACTIONS() throws RecognitionException {
        EObject current = null;

        EObject lv_comAction_0_0 = null;



        	enterRule();

        try {
            // InternalECA.g:1331:2: ( ( (lv_comAction_0_0= ruleCOMMANDACTION ) ) )
            // InternalECA.g:1332:2: ( (lv_comAction_0_0= ruleCOMMANDACTION ) )
            {
            // InternalECA.g:1332:2: ( (lv_comAction_0_0= ruleCOMMANDACTION ) )
            // InternalECA.g:1333:3: (lv_comAction_0_0= ruleCOMMANDACTION )
            {
            // InternalECA.g:1333:3: (lv_comAction_0_0= ruleCOMMANDACTION )
            // InternalECA.g:1334:4: lv_comAction_0_0= ruleCOMMANDACTION
            {

            				newCompositeNode(grammarAccess.getSUBACTIONSAccess().getComActionCOMMANDACTIONParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_comAction_0_0=ruleCOMMANDACTION();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getSUBACTIONSRule());
            				}
            				set(
            					current,
            					"comAction",
            					lv_comAction_0_0,
            					"de.uniol.inf.is.odysseus.eca.ECA.COMMANDACTION");
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
    // InternalECA.g:1354:1: entryRuleCOMMANDACTION returns [EObject current=null] : iv_ruleCOMMANDACTION= ruleCOMMANDACTION EOF ;
    public final EObject entryRuleCOMMANDACTION() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCOMMANDACTION = null;


        try {
            // InternalECA.g:1354:54: (iv_ruleCOMMANDACTION= ruleCOMMANDACTION EOF )
            // InternalECA.g:1355:2: iv_ruleCOMMANDACTION= ruleCOMMANDACTION EOF
            {
             newCompositeNode(grammarAccess.getCOMMANDACTIONRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCOMMANDACTION=ruleCOMMANDACTION();

            state._fsp--;

             current =iv_ruleCOMMANDACTION; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1361:1: ruleCOMMANDACTION returns [EObject current=null] : ( ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')' ) ;
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
            // InternalECA.g:1367:2: ( ( ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')' ) )
            // InternalECA.g:1368:2: ( ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')' )
            {
            // InternalECA.g:1368:2: ( ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')' )
            // InternalECA.g:1369:3: ( (lv_subActname_0_0= RULE_ID ) ) otherlv_1= '(' ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* ) otherlv_5= ')'
            {
            // InternalECA.g:1369:3: ( (lv_subActname_0_0= RULE_ID ) )
            // InternalECA.g:1370:4: (lv_subActname_0_0= RULE_ID )
            {
            // InternalECA.g:1370:4: (lv_subActname_0_0= RULE_ID )
            // InternalECA.g:1371:5: lv_subActname_0_0= RULE_ID
            {
            lv_subActname_0_0=(Token)match(input,RULE_ID,FOLLOW_22); 

            					newLeafNode(lv_subActname_0_0, grammarAccess.getCOMMANDACTIONAccess().getSubActnameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCOMMANDACTIONRule());
            					}
            					setWithLastConsumed(
            						current,
            						"subActname",
            						lv_subActname_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,28,FOLLOW_27); 

            			newLeafNode(otherlv_1, grammarAccess.getCOMMANDACTIONAccess().getLeftParenthesisKeyword_1());
            		
            // InternalECA.g:1391:3: ( ( (lv_functAction_2_0= ruleRNDQUERY ) ) | ( (lv_actionValue_3_0= ruleEcaValue ) ) | ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )* )
            int alt13=3;
            switch ( input.LA(1) ) {
            case 32:
                {
                alt13=1;
                }
                break;
            case RULE_INT:
            case RULE_STRING:
            case RULE_DOUBLE:
            case 24:
                {
                alt13=2;
                }
                break;
            case RULE_ID:
                {
                int LA13_3 = input.LA(2);

                if ( (LA13_3==29) ) {
                    alt13=2;
                }
                else if ( (LA13_3==28) ) {
                    alt13=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 3, input);

                    throw nvae;
                }
                }
                break;
            case 29:
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
                    // InternalECA.g:1392:4: ( (lv_functAction_2_0= ruleRNDQUERY ) )
                    {
                    // InternalECA.g:1392:4: ( (lv_functAction_2_0= ruleRNDQUERY ) )
                    // InternalECA.g:1393:5: (lv_functAction_2_0= ruleRNDQUERY )
                    {
                    // InternalECA.g:1393:5: (lv_functAction_2_0= ruleRNDQUERY )
                    // InternalECA.g:1394:6: lv_functAction_2_0= ruleRNDQUERY
                    {

                    						newCompositeNode(grammarAccess.getCOMMANDACTIONAccess().getFunctActionRNDQUERYParserRuleCall_2_0_0());
                    					
                    pushFollow(FOLLOW_24);
                    lv_functAction_2_0=ruleRNDQUERY();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCOMMANDACTIONRule());
                    						}
                    						set(
                    							current,
                    							"functAction",
                    							lv_functAction_2_0,
                    							"de.uniol.inf.is.odysseus.eca.ECA.RNDQUERY");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalECA.g:1412:4: ( (lv_actionValue_3_0= ruleEcaValue ) )
                    {
                    // InternalECA.g:1412:4: ( (lv_actionValue_3_0= ruleEcaValue ) )
                    // InternalECA.g:1413:5: (lv_actionValue_3_0= ruleEcaValue )
                    {
                    // InternalECA.g:1413:5: (lv_actionValue_3_0= ruleEcaValue )
                    // InternalECA.g:1414:6: lv_actionValue_3_0= ruleEcaValue
                    {

                    						newCompositeNode(grammarAccess.getCOMMANDACTIONAccess().getActionValueEcaValueParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_24);
                    lv_actionValue_3_0=ruleEcaValue();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCOMMANDACTIONRule());
                    						}
                    						set(
                    							current,
                    							"actionValue",
                    							lv_actionValue_3_0,
                    							"de.uniol.inf.is.odysseus.eca.ECA.EcaValue");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalECA.g:1432:4: ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )*
                    {
                    // InternalECA.g:1432:4: ( (lv_innerAction_4_0= ruleCOMMANDACTION ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==RULE_ID) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // InternalECA.g:1433:5: (lv_innerAction_4_0= ruleCOMMANDACTION )
                    	    {
                    	    // InternalECA.g:1433:5: (lv_innerAction_4_0= ruleCOMMANDACTION )
                    	    // InternalECA.g:1434:6: lv_innerAction_4_0= ruleCOMMANDACTION
                    	    {

                    	    						newCompositeNode(grammarAccess.getCOMMANDACTIONAccess().getInnerActionCOMMANDACTIONParserRuleCall_2_2_0());
                    	    					
                    	    pushFollow(FOLLOW_28);
                    	    lv_innerAction_4_0=ruleCOMMANDACTION();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getCOMMANDACTIONRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"innerAction",
                    	    							lv_innerAction_4_0,
                    	    							"de.uniol.inf.is.odysseus.eca.ECA.COMMANDACTION");
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

            otherlv_5=(Token)match(input,29,FOLLOW_2); 

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
    // InternalECA.g:1460:1: entryRuleRNDQUERY returns [EObject current=null] : iv_ruleRNDQUERY= ruleRNDQUERY EOF ;
    public final EObject entryRuleRNDQUERY() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRNDQUERY = null;


        try {
            // InternalECA.g:1460:49: (iv_ruleRNDQUERY= ruleRNDQUERY EOF )
            // InternalECA.g:1461:2: iv_ruleRNDQUERY= ruleRNDQUERY EOF
            {
             newCompositeNode(grammarAccess.getRNDQUERYRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRNDQUERY=ruleRNDQUERY();

            state._fsp--;

             current =iv_ruleRNDQUERY; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1467:1: ruleRNDQUERY returns [EObject current=null] : (otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) ) ) ;
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
            // InternalECA.g:1473:2: ( (otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) ) ) )
            // InternalECA.g:1474:2: (otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) ) )
            {
            // InternalECA.g:1474:2: (otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) ) )
            // InternalECA.g:1475:3: otherlv_0= 'prio' ( (lv_priOperator_1_0= ruleOperator ) ) ( (lv_priVal_2_0= RULE_INT ) ) (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )? otherlv_6= ',' otherlv_7= 'state' otherlv_8= '=' ( (lv_stateName_9_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,32,FOLLOW_13); 

            			newLeafNode(otherlv_0, grammarAccess.getRNDQUERYAccess().getPrioKeyword_0());
            		
            // InternalECA.g:1479:3: ( (lv_priOperator_1_0= ruleOperator ) )
            // InternalECA.g:1480:4: (lv_priOperator_1_0= ruleOperator )
            {
            // InternalECA.g:1480:4: (lv_priOperator_1_0= ruleOperator )
            // InternalECA.g:1481:5: lv_priOperator_1_0= ruleOperator
            {

            					newCompositeNode(grammarAccess.getRNDQUERYAccess().getPriOperatorOperatorParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_7);
            lv_priOperator_1_0=ruleOperator();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getRNDQUERYRule());
            					}
            					set(
            						current,
            						"priOperator",
            						lv_priOperator_1_0,
            						"de.uniol.inf.is.odysseus.eca.ECA.Operator");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalECA.g:1498:3: ( (lv_priVal_2_0= RULE_INT ) )
            // InternalECA.g:1499:4: (lv_priVal_2_0= RULE_INT )
            {
            // InternalECA.g:1499:4: (lv_priVal_2_0= RULE_INT )
            // InternalECA.g:1500:5: lv_priVal_2_0= RULE_INT
            {
            lv_priVal_2_0=(Token)match(input,RULE_INT,FOLLOW_29); 

            					newLeafNode(lv_priVal_2_0, grammarAccess.getRNDQUERYAccess().getPriValINTTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getRNDQUERYRule());
            					}
            					setWithLastConsumed(
            						current,
            						"priVal",
            						lv_priVal_2_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            // InternalECA.g:1516:3: (otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')' )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==28) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalECA.g:1517:4: otherlv_3= '(' ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) ) otherlv_5= ')'
                    {
                    otherlv_3=(Token)match(input,28,FOLLOW_30); 

                    				newLeafNode(otherlv_3, grammarAccess.getRNDQUERYAccess().getLeftParenthesisKeyword_3_0());
                    			
                    // InternalECA.g:1521:4: ( ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) ) )
                    // InternalECA.g:1522:5: ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) )
                    {
                    // InternalECA.g:1522:5: ( (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' ) )
                    // InternalECA.g:1523:6: (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' )
                    {
                    // InternalECA.g:1523:6: (lv_sel_4_1= 'MIN' | lv_sel_4_2= 'MAX' )
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==33) ) {
                        alt14=1;
                    }
                    else if ( (LA14_0==34) ) {
                        alt14=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 0, input);

                        throw nvae;
                    }
                    switch (alt14) {
                        case 1 :
                            // InternalECA.g:1524:7: lv_sel_4_1= 'MIN'
                            {
                            lv_sel_4_1=(Token)match(input,33,FOLLOW_24); 

                            							newLeafNode(lv_sel_4_1, grammarAccess.getRNDQUERYAccess().getSelMINKeyword_3_1_0_0());
                            						

                            							if (current==null) {
                            								current = createModelElement(grammarAccess.getRNDQUERYRule());
                            							}
                            							setWithLastConsumed(current, "sel", lv_sel_4_1, null);
                            						

                            }
                            break;
                        case 2 :
                            // InternalECA.g:1535:7: lv_sel_4_2= 'MAX'
                            {
                            lv_sel_4_2=(Token)match(input,34,FOLLOW_24); 

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

                    otherlv_5=(Token)match(input,29,FOLLOW_31); 

                    				newLeafNode(otherlv_5, grammarAccess.getRNDQUERYAccess().getRightParenthesisKeyword_3_2());
                    			

                    }
                    break;

            }

            otherlv_6=(Token)match(input,35,FOLLOW_32); 

            			newLeafNode(otherlv_6, grammarAccess.getRNDQUERYAccess().getCommaKeyword_4());
            		
            otherlv_7=(Token)match(input,36,FOLLOW_33); 

            			newLeafNode(otherlv_7, grammarAccess.getRNDQUERYAccess().getStateKeyword_5());
            		
            otherlv_8=(Token)match(input,37,FOLLOW_5); 

            			newLeafNode(otherlv_8, grammarAccess.getRNDQUERYAccess().getEqualsSignKeyword_6());
            		
            // InternalECA.g:1565:3: ( (lv_stateName_9_0= RULE_ID ) )
            // InternalECA.g:1566:4: (lv_stateName_9_0= RULE_ID )
            {
            // InternalECA.g:1566:4: (lv_stateName_9_0= RULE_ID )
            // InternalECA.g:1567:5: lv_stateName_9_0= RULE_ID
            {
            lv_stateName_9_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_stateName_9_0, grammarAccess.getRNDQUERYAccess().getStateNameIDTerminalRuleCall_7_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getRNDQUERYRule());
            					}
            					setWithLastConsumed(
            						current,
            						"stateName",
            						lv_stateName_9_0,
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
    // $ANTLR end "ruleRNDQUERY"


    // $ANTLR start "entryRuleSource"
    // InternalECA.g:1587:1: entryRuleSource returns [EObject current=null] : iv_ruleSource= ruleSource EOF ;
    public final EObject entryRuleSource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSource = null;


        try {
            // InternalECA.g:1587:47: (iv_ruleSource= ruleSource EOF )
            // InternalECA.g:1588:2: iv_ruleSource= ruleSource EOF
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
    // InternalECA.g:1594:1: ruleSource returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleSource() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalECA.g:1600:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalECA.g:1601:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalECA.g:1601:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalECA.g:1602:3: (lv_name_0_0= RULE_ID )
            {
            // InternalECA.g:1602:3: (lv_name_0_0= RULE_ID )
            // InternalECA.g:1603:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0());
            			

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


            }


            	leaveRule();

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
    // InternalECA.g:1622:1: entryRuleEcaValue returns [EObject current=null] : iv_ruleEcaValue= ruleEcaValue EOF ;
    public final EObject entryRuleEcaValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEcaValue = null;


        try {
            // InternalECA.g:1622:49: (iv_ruleEcaValue= ruleEcaValue EOF )
            // InternalECA.g:1623:2: iv_ruleEcaValue= ruleEcaValue EOF
            {
             newCompositeNode(grammarAccess.getEcaValueRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEcaValue=ruleEcaValue();

            state._fsp--;

             current =iv_ruleEcaValue; 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1629:1: ruleEcaValue returns [EObject current=null] : ( ( (lv_intValue_0_0= RULE_INT ) ) | ( (lv_idValue_1_0= RULE_ID ) ) | ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' ) | ( (lv_stringValue_5_0= RULE_STRING ) ) | ( (lv_doubleValue_6_0= RULE_DOUBLE ) ) ) ;
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
            // InternalECA.g:1635:2: ( ( ( (lv_intValue_0_0= RULE_INT ) ) | ( (lv_idValue_1_0= RULE_ID ) ) | ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' ) | ( (lv_stringValue_5_0= RULE_STRING ) ) | ( (lv_doubleValue_6_0= RULE_DOUBLE ) ) ) )
            // InternalECA.g:1636:2: ( ( (lv_intValue_0_0= RULE_INT ) ) | ( (lv_idValue_1_0= RULE_ID ) ) | ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' ) | ( (lv_stringValue_5_0= RULE_STRING ) ) | ( (lv_doubleValue_6_0= RULE_DOUBLE ) ) )
            {
            // InternalECA.g:1636:2: ( ( (lv_intValue_0_0= RULE_INT ) ) | ( (lv_idValue_1_0= RULE_ID ) ) | ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' ) | ( (lv_stringValue_5_0= RULE_STRING ) ) | ( (lv_doubleValue_6_0= RULE_DOUBLE ) ) )
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
            case 24:
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
                    // InternalECA.g:1637:3: ( (lv_intValue_0_0= RULE_INT ) )
                    {
                    // InternalECA.g:1637:3: ( (lv_intValue_0_0= RULE_INT ) )
                    // InternalECA.g:1638:4: (lv_intValue_0_0= RULE_INT )
                    {
                    // InternalECA.g:1638:4: (lv_intValue_0_0= RULE_INT )
                    // InternalECA.g:1639:5: lv_intValue_0_0= RULE_INT
                    {
                    lv_intValue_0_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    					newLeafNode(lv_intValue_0_0, grammarAccess.getEcaValueAccess().getIntValueINTTerminalRuleCall_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getEcaValueRule());
                    					}
                    					setWithLastConsumed(
                    						current,
                    						"intValue",
                    						lv_intValue_0_0,
                    						"org.eclipse.xtext.common.Terminals.INT");
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalECA.g:1656:3: ( (lv_idValue_1_0= RULE_ID ) )
                    {
                    // InternalECA.g:1656:3: ( (lv_idValue_1_0= RULE_ID ) )
                    // InternalECA.g:1657:4: (lv_idValue_1_0= RULE_ID )
                    {
                    // InternalECA.g:1657:4: (lv_idValue_1_0= RULE_ID )
                    // InternalECA.g:1658:5: lv_idValue_1_0= RULE_ID
                    {
                    lv_idValue_1_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    					newLeafNode(lv_idValue_1_0, grammarAccess.getEcaValueAccess().getIdValueIDTerminalRuleCall_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getEcaValueRule());
                    					}
                    					setWithLastConsumed(
                    						current,
                    						"idValue",
                    						lv_idValue_1_0,
                    						"org.eclipse.xtext.common.Terminals.ID");
                    				

                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalECA.g:1675:3: ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' )
                    {
                    // InternalECA.g:1675:3: ( (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}' )
                    // InternalECA.g:1676:4: (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) ) otherlv_4= '}'
                    {
                    // InternalECA.g:1676:4: (otherlv_2= '${' ( (otherlv_3= RULE_ID ) ) )
                    // InternalECA.g:1677:5: otherlv_2= '${' ( (otherlv_3= RULE_ID ) )
                    {
                    otherlv_2=(Token)match(input,24,FOLLOW_5); 

                    					newLeafNode(otherlv_2, grammarAccess.getEcaValueAccess().getDollarSignLeftCurlyBracketKeyword_2_0_0());
                    				
                    // InternalECA.g:1681:5: ( (otherlv_3= RULE_ID ) )
                    // InternalECA.g:1682:6: (otherlv_3= RULE_ID )
                    {
                    // InternalECA.g:1682:6: (otherlv_3= RULE_ID )
                    // InternalECA.g:1683:7: otherlv_3= RULE_ID
                    {

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getEcaValueRule());
                    							}
                    						
                    otherlv_3=(Token)match(input,RULE_ID,FOLLOW_20); 

                    							newLeafNode(otherlv_3, grammarAccess.getEcaValueAccess().getConstValueConstantCrossReference_2_0_1_0());
                    						

                    }


                    }


                    }

                    otherlv_4=(Token)match(input,25,FOLLOW_2); 

                    				newLeafNode(otherlv_4, grammarAccess.getEcaValueAccess().getRightCurlyBracketKeyword_2_1());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalECA.g:1701:3: ( (lv_stringValue_5_0= RULE_STRING ) )
                    {
                    // InternalECA.g:1701:3: ( (lv_stringValue_5_0= RULE_STRING ) )
                    // InternalECA.g:1702:4: (lv_stringValue_5_0= RULE_STRING )
                    {
                    // InternalECA.g:1702:4: (lv_stringValue_5_0= RULE_STRING )
                    // InternalECA.g:1703:5: lv_stringValue_5_0= RULE_STRING
                    {
                    lv_stringValue_5_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    					newLeafNode(lv_stringValue_5_0, grammarAccess.getEcaValueAccess().getStringValueSTRINGTerminalRuleCall_3_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getEcaValueRule());
                    					}
                    					setWithLastConsumed(
                    						current,
                    						"stringValue",
                    						lv_stringValue_5_0,
                    						"org.eclipse.xtext.common.Terminals.STRING");
                    				

                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalECA.g:1720:3: ( (lv_doubleValue_6_0= RULE_DOUBLE ) )
                    {
                    // InternalECA.g:1720:3: ( (lv_doubleValue_6_0= RULE_DOUBLE ) )
                    // InternalECA.g:1721:4: (lv_doubleValue_6_0= RULE_DOUBLE )
                    {
                    // InternalECA.g:1721:4: (lv_doubleValue_6_0= RULE_DOUBLE )
                    // InternalECA.g:1722:5: lv_doubleValue_6_0= RULE_DOUBLE
                    {
                    lv_doubleValue_6_0=(Token)match(input,RULE_DOUBLE,FOLLOW_2); 

                    					newLeafNode(lv_doubleValue_6_0, grammarAccess.getEcaValueAccess().getDoubleValueDOUBLETerminalRuleCall_4_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getEcaValueRule());
                    					}
                    					setWithLastConsumed(
                    						current,
                    						"doubleValue",
                    						lv_doubleValue_6_0,
                    						"de.uniol.inf.is.odysseus.eca.ECA.DOUBLE");
                    				

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
    // InternalECA.g:1742:1: entryRulePREDEFINEDSOURCE returns [String current=null] : iv_rulePREDEFINEDSOURCE= rulePREDEFINEDSOURCE EOF ;
    public final String entryRulePREDEFINEDSOURCE() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_rulePREDEFINEDSOURCE = null;


        try {
            // InternalECA.g:1742:56: (iv_rulePREDEFINEDSOURCE= rulePREDEFINEDSOURCE EOF )
            // InternalECA.g:1743:2: iv_rulePREDEFINEDSOURCE= rulePREDEFINEDSOURCE EOF
            {
             newCompositeNode(grammarAccess.getPREDEFINEDSOURCERule()); 
            pushFollow(FOLLOW_1);
            iv_rulePREDEFINEDSOURCE=rulePREDEFINEDSOURCE();

            state._fsp--;

             current =iv_rulePREDEFINEDSOURCE.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1749:1: rulePREDEFINEDSOURCE returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'TimerEvent' | kw= 'QueryEvent' ) ;
    public final AntlrDatatypeRuleToken rulePREDEFINEDSOURCE() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalECA.g:1755:2: ( (kw= 'TimerEvent' | kw= 'QueryEvent' ) )
            // InternalECA.g:1756:2: (kw= 'TimerEvent' | kw= 'QueryEvent' )
            {
            // InternalECA.g:1756:2: (kw= 'TimerEvent' | kw= 'QueryEvent' )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==38) ) {
                alt17=1;
            }
            else if ( (LA17_0==39) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // InternalECA.g:1757:3: kw= 'TimerEvent'
                    {
                    kw=(Token)match(input,38,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getPREDEFINEDSOURCEAccess().getTimerEventKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalECA.g:1763:3: kw= 'QueryEvent'
                    {
                    kw=(Token)match(input,39,FOLLOW_2); 

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
    // InternalECA.g:1772:1: entryRuleSYSTEMFUNCTION returns [String current=null] : iv_ruleSYSTEMFUNCTION= ruleSYSTEMFUNCTION EOF ;
    public final String entryRuleSYSTEMFUNCTION() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleSYSTEMFUNCTION = null;


        try {
            // InternalECA.g:1772:54: (iv_ruleSYSTEMFUNCTION= ruleSYSTEMFUNCTION EOF )
            // InternalECA.g:1773:2: iv_ruleSYSTEMFUNCTION= ruleSYSTEMFUNCTION EOF
            {
             newCompositeNode(grammarAccess.getSYSTEMFUNCTIONRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSYSTEMFUNCTION=ruleSYSTEMFUNCTION();

            state._fsp--;

             current =iv_ruleSYSTEMFUNCTION.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1779:1: ruleSYSTEMFUNCTION returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'curCPULoad' | kw= 'curMEMLoad' | kw= 'curNETLoad' ) ;
    public final AntlrDatatypeRuleToken ruleSYSTEMFUNCTION() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalECA.g:1785:2: ( (kw= 'curCPULoad' | kw= 'curMEMLoad' | kw= 'curNETLoad' ) )
            // InternalECA.g:1786:2: (kw= 'curCPULoad' | kw= 'curMEMLoad' | kw= 'curNETLoad' )
            {
            // InternalECA.g:1786:2: (kw= 'curCPULoad' | kw= 'curMEMLoad' | kw= 'curNETLoad' )
            int alt18=3;
            switch ( input.LA(1) ) {
            case 40:
                {
                alt18=1;
                }
                break;
            case 41:
                {
                alt18=2;
                }
                break;
            case 42:
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
                    // InternalECA.g:1787:3: kw= 'curCPULoad'
                    {
                    kw=(Token)match(input,40,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getSYSTEMFUNCTIONAccess().getCurCPULoadKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalECA.g:1793:3: kw= 'curMEMLoad'
                    {
                    kw=(Token)match(input,41,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getSYSTEMFUNCTIONAccess().getCurMEMLoadKeyword_1());
                    		

                    }
                    break;
                case 3 :
                    // InternalECA.g:1799:3: kw= 'curNETLoad'
                    {
                    kw=(Token)match(input,42,FOLLOW_2); 

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
    // InternalECA.g:1808:1: entryRuleOperator returns [String current=null] : iv_ruleOperator= ruleOperator EOF ;
    public final String entryRuleOperator() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOperator = null;


        try {
            // InternalECA.g:1808:48: (iv_ruleOperator= ruleOperator EOF )
            // InternalECA.g:1809:2: iv_ruleOperator= ruleOperator EOF
            {
             newCompositeNode(grammarAccess.getOperatorRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOperator=ruleOperator();

            state._fsp--;

             current =iv_ruleOperator.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

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
    // InternalECA.g:1815:1: ruleOperator returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '<' | kw= '>' | kw= '=' | kw= '<=' | kw= '>=' ) ;
    public final AntlrDatatypeRuleToken ruleOperator() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalECA.g:1821:2: ( (kw= '<' | kw= '>' | kw= '=' | kw= '<=' | kw= '>=' ) )
            // InternalECA.g:1822:2: (kw= '<' | kw= '>' | kw= '=' | kw= '<=' | kw= '>=' )
            {
            // InternalECA.g:1822:2: (kw= '<' | kw= '>' | kw= '=' | kw= '<=' | kw= '>=' )
            int alt19=5;
            switch ( input.LA(1) ) {
            case 43:
                {
                alt19=1;
                }
                break;
            case 44:
                {
                alt19=2;
                }
                break;
            case 37:
                {
                alt19=3;
                }
                break;
            case 45:
                {
                alt19=4;
                }
                break;
            case 46:
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
                    // InternalECA.g:1823:3: kw= '<'
                    {
                    kw=(Token)match(input,43,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getOperatorAccess().getLessThanSignKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalECA.g:1829:3: kw= '>'
                    {
                    kw=(Token)match(input,44,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getOperatorAccess().getGreaterThanSignKeyword_1());
                    		

                    }
                    break;
                case 3 :
                    // InternalECA.g:1835:3: kw= '='
                    {
                    kw=(Token)match(input,37,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getOperatorAccess().getEqualsSignKeyword_2());
                    		

                    }
                    break;
                case 4 :
                    // InternalECA.g:1841:3: kw= '<='
                    {
                    kw=(Token)match(input,45,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getOperatorAccess().getLessThanSignEqualsSignKeyword_3());
                    		

                    }
                    break;
                case 5 :
                    // InternalECA.g:1847:3: kw= '>='
                    {
                    kw=(Token)match(input,46,FOLLOW_2); 

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


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000101002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000782000000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x00000000010000F0L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x000000C001000010L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x00000000CC800050L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000070000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x00000001210000F0L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000020000010L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000810000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000002000000000L});

}
