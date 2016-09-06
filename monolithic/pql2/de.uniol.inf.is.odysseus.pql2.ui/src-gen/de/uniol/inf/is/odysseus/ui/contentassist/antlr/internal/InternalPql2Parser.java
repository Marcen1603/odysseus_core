package de.uniol.inf.is.odysseus.ui.contentassist.antlr.internal; 

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.DFA;
import de.uniol.inf.is.odysseus.services.Pql2GrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
@SuppressWarnings("all")
public class InternalPql2Parser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_DOUBLE", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'='", "':='", "'::='", "'('", "')'", "','", "':'", "'{'", "'}'", "'['", "']'"
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
    public String getGrammarFileName() { return "../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g"; }


     
     	private Pql2GrammarAccess grammarAccess;
     	
        public void setGrammarAccess(Pql2GrammarAccess grammarAccess) {
        	this.grammarAccess = grammarAccess;
        }
        
        @Override
        protected Grammar getGrammar() {
        	return grammarAccess.getGrammar();
        }
        
        @Override
        protected String getValueForTokenName(String tokenName) {
        	return tokenName;
        }




    // $ANTLR start "entryRulePQLModel"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:61:1: entryRulePQLModel : rulePQLModel EOF ;
    public final void entryRulePQLModel() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:62:1: ( rulePQLModel EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:63:1: rulePQLModel EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getPQLModelRule()); 
            }
            pushFollow(FOLLOW_rulePQLModel_in_entryRulePQLModel67);
            rulePQLModel();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getPQLModelRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRulePQLModel74); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRulePQLModel"


    // $ANTLR start "rulePQLModel"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:70:1: rulePQLModel : ( ( rule__PQLModel__QueriesAssignment )* ) ;
    public final void rulePQLModel() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:74:2: ( ( ( rule__PQLModel__QueriesAssignment )* ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:75:1: ( ( rule__PQLModel__QueriesAssignment )* )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:75:1: ( ( rule__PQLModel__QueriesAssignment )* )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:76:1: ( rule__PQLModel__QueriesAssignment )*
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getPQLModelAccess().getQueriesAssignment()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:77:1: ( rule__PQLModel__QueriesAssignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:77:2: rule__PQLModel__QueriesAssignment
            	    {
            	    pushFollow(FOLLOW_rule__PQLModel__QueriesAssignment_in_rulePQLModel100);
            	    rule__PQLModel__QueriesAssignment();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            if ( state.backtracking==0 ) {
               after(grammarAccess.getPQLModelAccess().getQueriesAssignment()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rulePQLModel"


    // $ANTLR start "entryRuleQuery"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:89:1: entryRuleQuery : ruleQuery EOF ;
    public final void entryRuleQuery() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:90:1: ( ruleQuery EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:91:1: ruleQuery EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getQueryRule()); 
            }
            pushFollow(FOLLOW_ruleQuery_in_entryRuleQuery128);
            ruleQuery();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getQueryRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleQuery135); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleQuery"


    // $ANTLR start "ruleQuery"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:98:1: ruleQuery : ( ( rule__Query__Alternatives ) ) ;
    public final void ruleQuery() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:102:2: ( ( ( rule__Query__Alternatives ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:103:1: ( ( rule__Query__Alternatives ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:103:1: ( ( rule__Query__Alternatives ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:104:1: ( rule__Query__Alternatives )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getQueryAccess().getAlternatives()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:105:1: ( rule__Query__Alternatives )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:105:2: rule__Query__Alternatives
            {
            pushFollow(FOLLOW_rule__Query__Alternatives_in_ruleQuery161);
            rule__Query__Alternatives();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getQueryAccess().getAlternatives()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleQuery"


    // $ANTLR start "entryRuleTemporaryStream"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:117:1: entryRuleTemporaryStream : ruleTemporaryStream EOF ;
    public final void entryRuleTemporaryStream() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:118:1: ( ruleTemporaryStream EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:119:1: ruleTemporaryStream EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getTemporaryStreamRule()); 
            }
            pushFollow(FOLLOW_ruleTemporaryStream_in_entryRuleTemporaryStream188);
            ruleTemporaryStream();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getTemporaryStreamRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleTemporaryStream195); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleTemporaryStream"


    // $ANTLR start "ruleTemporaryStream"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:126:1: ruleTemporaryStream : ( ( rule__TemporaryStream__Group__0 ) ) ;
    public final void ruleTemporaryStream() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:130:2: ( ( ( rule__TemporaryStream__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:131:1: ( ( rule__TemporaryStream__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:131:1: ( ( rule__TemporaryStream__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:132:1: ( rule__TemporaryStream__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getTemporaryStreamAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:133:1: ( rule__TemporaryStream__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:133:2: rule__TemporaryStream__Group__0
            {
            pushFollow(FOLLOW_rule__TemporaryStream__Group__0_in_ruleTemporaryStream221);
            rule__TemporaryStream__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getTemporaryStreamAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleTemporaryStream"


    // $ANTLR start "entryRuleView"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:145:1: entryRuleView : ruleView EOF ;
    public final void entryRuleView() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:146:1: ( ruleView EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:147:1: ruleView EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getViewRule()); 
            }
            pushFollow(FOLLOW_ruleView_in_entryRuleView248);
            ruleView();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getViewRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleView255); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleView"


    // $ANTLR start "ruleView"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:154:1: ruleView : ( ( rule__View__Group__0 ) ) ;
    public final void ruleView() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:158:2: ( ( ( rule__View__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:159:1: ( ( rule__View__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:159:1: ( ( rule__View__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:160:1: ( rule__View__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getViewAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:161:1: ( rule__View__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:161:2: rule__View__Group__0
            {
            pushFollow(FOLLOW_rule__View__Group__0_in_ruleView281);
            rule__View__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getViewAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleView"


    // $ANTLR start "entryRuleSharedStream"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:173:1: entryRuleSharedStream : ruleSharedStream EOF ;
    public final void entryRuleSharedStream() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:174:1: ( ruleSharedStream EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:175:1: ruleSharedStream EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getSharedStreamRule()); 
            }
            pushFollow(FOLLOW_ruleSharedStream_in_entryRuleSharedStream308);
            ruleSharedStream();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getSharedStreamRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleSharedStream315); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleSharedStream"


    // $ANTLR start "ruleSharedStream"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:182:1: ruleSharedStream : ( ( rule__SharedStream__Group__0 ) ) ;
    public final void ruleSharedStream() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:186:2: ( ( ( rule__SharedStream__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:187:1: ( ( rule__SharedStream__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:187:1: ( ( rule__SharedStream__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:188:1: ( rule__SharedStream__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getSharedStreamAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:189:1: ( rule__SharedStream__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:189:2: rule__SharedStream__Group__0
            {
            pushFollow(FOLLOW_rule__SharedStream__Group__0_in_ruleSharedStream341);
            rule__SharedStream__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getSharedStreamAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleSharedStream"


    // $ANTLR start "entryRuleOperator"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:201:1: entryRuleOperator : ruleOperator EOF ;
    public final void entryRuleOperator() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:202:1: ( ruleOperator EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:203:1: ruleOperator EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorRule()); 
            }
            pushFollow(FOLLOW_ruleOperator_in_entryRuleOperator368);
            ruleOperator();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperator375); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleOperator"


    // $ANTLR start "ruleOperator"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:210:1: ruleOperator : ( ( rule__Operator__Group__0 ) ) ;
    public final void ruleOperator() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:214:2: ( ( ( rule__Operator__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:215:1: ( ( rule__Operator__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:215:1: ( ( rule__Operator__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:216:1: ( rule__Operator__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:217:1: ( rule__Operator__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:217:2: rule__Operator__Group__0
            {
            pushFollow(FOLLOW_rule__Operator__Group__0_in_ruleOperator401);
            rule__Operator__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleOperator"


    // $ANTLR start "entryRuleOperatorList"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:229:1: entryRuleOperatorList : ruleOperatorList EOF ;
    public final void entryRuleOperatorList() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:230:1: ( ruleOperatorList EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:231:1: ruleOperatorList EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorListRule()); 
            }
            pushFollow(FOLLOW_ruleOperatorList_in_entryRuleOperatorList428);
            ruleOperatorList();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorListRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperatorList435); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleOperatorList"


    // $ANTLR start "ruleOperatorList"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:238:1: ruleOperatorList : ( ( rule__OperatorList__Group__0 ) ) ;
    public final void ruleOperatorList() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:242:2: ( ( ( rule__OperatorList__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:243:1: ( ( rule__OperatorList__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:243:1: ( ( rule__OperatorList__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:244:1: ( rule__OperatorList__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorListAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:245:1: ( rule__OperatorList__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:245:2: rule__OperatorList__Group__0
            {
            pushFollow(FOLLOW_rule__OperatorList__Group__0_in_ruleOperatorList461);
            rule__OperatorList__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorListAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleOperatorList"


    // $ANTLR start "entryRuleOperatorOrQuery"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:257:1: entryRuleOperatorOrQuery : ruleOperatorOrQuery EOF ;
    public final void entryRuleOperatorOrQuery() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:258:1: ( ruleOperatorOrQuery EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:259:1: ruleOperatorOrQuery EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryRule()); 
            }
            pushFollow(FOLLOW_ruleOperatorOrQuery_in_entryRuleOperatorOrQuery488);
            ruleOperatorOrQuery();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperatorOrQuery495); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleOperatorOrQuery"


    // $ANTLR start "ruleOperatorOrQuery"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:266:1: ruleOperatorOrQuery : ( ( rule__OperatorOrQuery__Group__0 ) ) ;
    public final void ruleOperatorOrQuery() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:270:2: ( ( ( rule__OperatorOrQuery__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:271:1: ( ( rule__OperatorOrQuery__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:271:1: ( ( rule__OperatorOrQuery__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:272:1: ( rule__OperatorOrQuery__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:273:1: ( rule__OperatorOrQuery__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:273:2: rule__OperatorOrQuery__Group__0
            {
            pushFollow(FOLLOW_rule__OperatorOrQuery__Group__0_in_ruleOperatorOrQuery521);
            rule__OperatorOrQuery__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleOperatorOrQuery"


    // $ANTLR start "entryRuleParameterList"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:285:1: entryRuleParameterList : ruleParameterList EOF ;
    public final void entryRuleParameterList() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:286:1: ( ruleParameterList EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:287:1: ruleParameterList EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListRule()); 
            }
            pushFollow(FOLLOW_ruleParameterList_in_entryRuleParameterList548);
            ruleParameterList();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleParameterList555); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleParameterList"


    // $ANTLR start "ruleParameterList"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:294:1: ruleParameterList : ( ( rule__ParameterList__Group__0 ) ) ;
    public final void ruleParameterList() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:298:2: ( ( ( rule__ParameterList__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:299:1: ( ( rule__ParameterList__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:299:1: ( ( rule__ParameterList__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:300:1: ( rule__ParameterList__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:301:1: ( rule__ParameterList__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:301:2: rule__ParameterList__Group__0
            {
            pushFollow(FOLLOW_rule__ParameterList__Group__0_in_ruleParameterList581);
            rule__ParameterList__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleParameterList"


    // $ANTLR start "entryRuleParameter"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:313:1: entryRuleParameter : ruleParameter EOF ;
    public final void entryRuleParameter() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:314:1: ( ruleParameter EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:315:1: ruleParameter EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterRule()); 
            }
            pushFollow(FOLLOW_ruleParameter_in_entryRuleParameter608);
            ruleParameter();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleParameter615); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleParameter"


    // $ANTLR start "ruleParameter"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:322:1: ruleParameter : ( ( rule__Parameter__Group__0 ) ) ;
    public final void ruleParameter() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:326:2: ( ( ( rule__Parameter__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:327:1: ( ( rule__Parameter__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:327:1: ( ( rule__Parameter__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:328:1: ( rule__Parameter__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:329:1: ( rule__Parameter__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:329:2: rule__Parameter__Group__0
            {
            pushFollow(FOLLOW_rule__Parameter__Group__0_in_ruleParameter641);
            rule__Parameter__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleParameter"


    // $ANTLR start "entryRuleParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:341:1: entryRuleParameterValue : ruleParameterValue EOF ;
    public final void entryRuleParameterValue() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:342:1: ( ruleParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:343:1: ruleParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleParameterValue_in_entryRuleParameterValue668);
            ruleParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterValueRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleParameterValue675); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleParameterValue"


    // $ANTLR start "ruleParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:350:1: ruleParameterValue : ( ( rule__ParameterValue__Alternatives ) ) ;
    public final void ruleParameterValue() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:354:2: ( ( ( rule__ParameterValue__Alternatives ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:355:1: ( ( rule__ParameterValue__Alternatives ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:355:1: ( ( rule__ParameterValue__Alternatives ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:356:1: ( rule__ParameterValue__Alternatives )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterValueAccess().getAlternatives()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:357:1: ( rule__ParameterValue__Alternatives )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:357:2: rule__ParameterValue__Alternatives
            {
            pushFollow(FOLLOW_rule__ParameterValue__Alternatives_in_ruleParameterValue701);
            rule__ParameterValue__Alternatives();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterValueAccess().getAlternatives()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleParameterValue"


    // $ANTLR start "entryRuleLongParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:369:1: entryRuleLongParameterValue : ruleLongParameterValue EOF ;
    public final void entryRuleLongParameterValue() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:370:1: ( ruleLongParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:371:1: ruleLongParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getLongParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleLongParameterValue_in_entryRuleLongParameterValue728);
            ruleLongParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getLongParameterValueRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleLongParameterValue735); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleLongParameterValue"


    // $ANTLR start "ruleLongParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:378:1: ruleLongParameterValue : ( ( rule__LongParameterValue__ValueAssignment ) ) ;
    public final void ruleLongParameterValue() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:382:2: ( ( ( rule__LongParameterValue__ValueAssignment ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:383:1: ( ( rule__LongParameterValue__ValueAssignment ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:383:1: ( ( rule__LongParameterValue__ValueAssignment ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:384:1: ( rule__LongParameterValue__ValueAssignment )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getLongParameterValueAccess().getValueAssignment()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:385:1: ( rule__LongParameterValue__ValueAssignment )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:385:2: rule__LongParameterValue__ValueAssignment
            {
            pushFollow(FOLLOW_rule__LongParameterValue__ValueAssignment_in_ruleLongParameterValue761);
            rule__LongParameterValue__ValueAssignment();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getLongParameterValueAccess().getValueAssignment()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleLongParameterValue"


    // $ANTLR start "entryRuleDoubleParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:397:1: entryRuleDoubleParameterValue : ruleDoubleParameterValue EOF ;
    public final void entryRuleDoubleParameterValue() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:398:1: ( ruleDoubleParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:399:1: ruleDoubleParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getDoubleParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleDoubleParameterValue_in_entryRuleDoubleParameterValue788);
            ruleDoubleParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getDoubleParameterValueRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleDoubleParameterValue795); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleDoubleParameterValue"


    // $ANTLR start "ruleDoubleParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:406:1: ruleDoubleParameterValue : ( ( rule__DoubleParameterValue__ValueAssignment ) ) ;
    public final void ruleDoubleParameterValue() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:410:2: ( ( ( rule__DoubleParameterValue__ValueAssignment ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:411:1: ( ( rule__DoubleParameterValue__ValueAssignment ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:411:1: ( ( rule__DoubleParameterValue__ValueAssignment ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:412:1: ( rule__DoubleParameterValue__ValueAssignment )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getDoubleParameterValueAccess().getValueAssignment()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:413:1: ( rule__DoubleParameterValue__ValueAssignment )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:413:2: rule__DoubleParameterValue__ValueAssignment
            {
            pushFollow(FOLLOW_rule__DoubleParameterValue__ValueAssignment_in_ruleDoubleParameterValue821);
            rule__DoubleParameterValue__ValueAssignment();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getDoubleParameterValueAccess().getValueAssignment()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleDoubleParameterValue"


    // $ANTLR start "entryRuleStringParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:425:1: entryRuleStringParameterValue : ruleStringParameterValue EOF ;
    public final void entryRuleStringParameterValue() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:426:1: ( ruleStringParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:427:1: ruleStringParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getStringParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleStringParameterValue_in_entryRuleStringParameterValue848);
            ruleStringParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getStringParameterValueRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleStringParameterValue855); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleStringParameterValue"


    // $ANTLR start "ruleStringParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:434:1: ruleStringParameterValue : ( ( rule__StringParameterValue__ValueAssignment ) ) ;
    public final void ruleStringParameterValue() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:438:2: ( ( ( rule__StringParameterValue__ValueAssignment ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:439:1: ( ( rule__StringParameterValue__ValueAssignment ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:439:1: ( ( rule__StringParameterValue__ValueAssignment ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:440:1: ( rule__StringParameterValue__ValueAssignment )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getStringParameterValueAccess().getValueAssignment()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:441:1: ( rule__StringParameterValue__ValueAssignment )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:441:2: rule__StringParameterValue__ValueAssignment
            {
            pushFollow(FOLLOW_rule__StringParameterValue__ValueAssignment_in_ruleStringParameterValue881);
            rule__StringParameterValue__ValueAssignment();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getStringParameterValueAccess().getValueAssignment()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleStringParameterValue"


    // $ANTLR start "entryRuleListParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:453:1: entryRuleListParameterValue : ruleListParameterValue EOF ;
    public final void entryRuleListParameterValue() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:454:1: ( ruleListParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:455:1: ruleListParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleListParameterValue_in_entryRuleListParameterValue908);
            ruleListParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleListParameterValue915); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleListParameterValue"


    // $ANTLR start "ruleListParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:462:1: ruleListParameterValue : ( ( rule__ListParameterValue__Group__0 ) ) ;
    public final void ruleListParameterValue() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:466:2: ( ( ( rule__ListParameterValue__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:467:1: ( ( rule__ListParameterValue__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:467:1: ( ( rule__ListParameterValue__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:468:1: ( rule__ListParameterValue__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:469:1: ( rule__ListParameterValue__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:469:2: rule__ListParameterValue__Group__0
            {
            pushFollow(FOLLOW_rule__ListParameterValue__Group__0_in_ruleListParameterValue941);
            rule__ListParameterValue__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleListParameterValue"


    // $ANTLR start "entryRuleMapParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:481:1: entryRuleMapParameterValue : ruleMapParameterValue EOF ;
    public final void entryRuleMapParameterValue() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:482:1: ( ruleMapParameterValue EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:483:1: ruleMapParameterValue EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueRule()); 
            }
            pushFollow(FOLLOW_ruleMapParameterValue_in_entryRuleMapParameterValue968);
            ruleMapParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapParameterValue975); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleMapParameterValue"


    // $ANTLR start "ruleMapParameterValue"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:490:1: ruleMapParameterValue : ( ( rule__MapParameterValue__Group__0 ) ) ;
    public final void ruleMapParameterValue() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:494:2: ( ( ( rule__MapParameterValue__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:495:1: ( ( rule__MapParameterValue__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:495:1: ( ( rule__MapParameterValue__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:496:1: ( rule__MapParameterValue__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:497:1: ( rule__MapParameterValue__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:497:2: rule__MapParameterValue__Group__0
            {
            pushFollow(FOLLOW_rule__MapParameterValue__Group__0_in_ruleMapParameterValue1001);
            rule__MapParameterValue__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleMapParameterValue"


    // $ANTLR start "entryRuleMapEntry"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:509:1: entryRuleMapEntry : ruleMapEntry EOF ;
    public final void entryRuleMapEntry() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:510:1: ( ruleMapEntry EOF )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:511:1: ruleMapEntry EOF
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapEntryRule()); 
            }
            pushFollow(FOLLOW_ruleMapEntry_in_entryRuleMapEntry1028);
            ruleMapEntry();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapEntryRule()); 
            }
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapEntry1035); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleMapEntry"


    // $ANTLR start "ruleMapEntry"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:518:1: ruleMapEntry : ( ( rule__MapEntry__Group__0 ) ) ;
    public final void ruleMapEntry() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:522:2: ( ( ( rule__MapEntry__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:523:1: ( ( rule__MapEntry__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:523:1: ( ( rule__MapEntry__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:524:1: ( rule__MapEntry__Group__0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapEntryAccess().getGroup()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:525:1: ( rule__MapEntry__Group__0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:525:2: rule__MapEntry__Group__0
            {
            pushFollow(FOLLOW_rule__MapEntry__Group__0_in_ruleMapEntry1061);
            rule__MapEntry__Group__0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapEntryAccess().getGroup()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleMapEntry"


    // $ANTLR start "rule__Query__Alternatives"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:537:1: rule__Query__Alternatives : ( ( ruleTemporaryStream ) | ( ruleView ) | ( ruleSharedStream ) );
    public final void rule__Query__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:541:1: ( ( ruleTemporaryStream ) | ( ruleView ) | ( ruleSharedStream ) )
            int alt2=3;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case 12:
                    {
                    alt2=1;
                    }
                    break;
                case 14:
                    {
                    alt2=3;
                    }
                    break;
                case 13:
                    {
                    alt2=2;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }

            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:542:1: ( ruleTemporaryStream )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:542:1: ( ruleTemporaryStream )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:543:1: ruleTemporaryStream
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getQueryAccess().getTemporaryStreamParserRuleCall_0()); 
                    }
                    pushFollow(FOLLOW_ruleTemporaryStream_in_rule__Query__Alternatives1097);
                    ruleTemporaryStream();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getQueryAccess().getTemporaryStreamParserRuleCall_0()); 
                    }

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:548:6: ( ruleView )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:548:6: ( ruleView )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:549:1: ruleView
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getQueryAccess().getViewParserRuleCall_1()); 
                    }
                    pushFollow(FOLLOW_ruleView_in_rule__Query__Alternatives1114);
                    ruleView();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getQueryAccess().getViewParserRuleCall_1()); 
                    }

                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:554:6: ( ruleSharedStream )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:554:6: ( ruleSharedStream )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:555:1: ruleSharedStream
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getQueryAccess().getSharedStreamParserRuleCall_2()); 
                    }
                    pushFollow(FOLLOW_ruleSharedStream_in_rule__Query__Alternatives1131);
                    ruleSharedStream();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getQueryAccess().getSharedStreamParserRuleCall_2()); 
                    }

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Query__Alternatives"


    // $ANTLR start "rule__Operator__Alternatives_2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:565:1: rule__Operator__Alternatives_2 : ( ( ( rule__Operator__OperatorsAssignment_2_0 ) ) | ( ( rule__Operator__Group_2_1__0 ) ) );
    public final void rule__Operator__Alternatives_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:569:1: ( ( ( rule__Operator__OperatorsAssignment_2_0 ) ) | ( ( rule__Operator__Group_2_1__0 ) ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0>=RULE_ID && LA3_0<=RULE_INT)) ) {
                alt3=1;
            }
            else if ( (LA3_0==19) ) {
                alt3=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:570:1: ( ( rule__Operator__OperatorsAssignment_2_0 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:570:1: ( ( rule__Operator__OperatorsAssignment_2_0 ) )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:571:1: ( rule__Operator__OperatorsAssignment_2_0 )
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getOperatorAccess().getOperatorsAssignment_2_0()); 
                    }
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:572:1: ( rule__Operator__OperatorsAssignment_2_0 )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:572:2: rule__Operator__OperatorsAssignment_2_0
                    {
                    pushFollow(FOLLOW_rule__Operator__OperatorsAssignment_2_0_in_rule__Operator__Alternatives_21163);
                    rule__Operator__OperatorsAssignment_2_0();

                    state._fsp--;
                    if (state.failed) return ;

                    }

                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getOperatorAccess().getOperatorsAssignment_2_0()); 
                    }

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:576:6: ( ( rule__Operator__Group_2_1__0 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:576:6: ( ( rule__Operator__Group_2_1__0 ) )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:577:1: ( rule__Operator__Group_2_1__0 )
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getOperatorAccess().getGroup_2_1()); 
                    }
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:578:1: ( rule__Operator__Group_2_1__0 )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:578:2: rule__Operator__Group_2_1__0
                    {
                    pushFollow(FOLLOW_rule__Operator__Group_2_1__0_in_rule__Operator__Alternatives_21181);
                    rule__Operator__Group_2_1__0();

                    state._fsp--;
                    if (state.failed) return ;

                    }

                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getOperatorAccess().getGroup_2_1()); 
                    }

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Alternatives_2"


    // $ANTLR start "rule__OperatorOrQuery__Alternatives_1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:587:1: rule__OperatorOrQuery__Alternatives_1 : ( ( ( rule__OperatorOrQuery__OpAssignment_1_0 ) ) | ( ( rule__OperatorOrQuery__QueryAssignment_1_1 ) ) );
    public final void rule__OperatorOrQuery__Alternatives_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:591:1: ( ( ( rule__OperatorOrQuery__OpAssignment_1_0 ) ) | ( ( rule__OperatorOrQuery__QueryAssignment_1_1 ) ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==RULE_ID) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==EOF||(LA4_1>=16 && LA4_1<=17)) ) {
                    alt4=2;
                }
                else if ( (LA4_1==15) ) {
                    alt4=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:592:1: ( ( rule__OperatorOrQuery__OpAssignment_1_0 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:592:1: ( ( rule__OperatorOrQuery__OpAssignment_1_0 ) )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:593:1: ( rule__OperatorOrQuery__OpAssignment_1_0 )
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getOperatorOrQueryAccess().getOpAssignment_1_0()); 
                    }
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:594:1: ( rule__OperatorOrQuery__OpAssignment_1_0 )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:594:2: rule__OperatorOrQuery__OpAssignment_1_0
                    {
                    pushFollow(FOLLOW_rule__OperatorOrQuery__OpAssignment_1_0_in_rule__OperatorOrQuery__Alternatives_11214);
                    rule__OperatorOrQuery__OpAssignment_1_0();

                    state._fsp--;
                    if (state.failed) return ;

                    }

                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getOperatorOrQueryAccess().getOpAssignment_1_0()); 
                    }

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:598:6: ( ( rule__OperatorOrQuery__QueryAssignment_1_1 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:598:6: ( ( rule__OperatorOrQuery__QueryAssignment_1_1 ) )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:599:1: ( rule__OperatorOrQuery__QueryAssignment_1_1 )
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getOperatorOrQueryAccess().getQueryAssignment_1_1()); 
                    }
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:600:1: ( rule__OperatorOrQuery__QueryAssignment_1_1 )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:600:2: rule__OperatorOrQuery__QueryAssignment_1_1
                    {
                    pushFollow(FOLLOW_rule__OperatorOrQuery__QueryAssignment_1_1_in_rule__OperatorOrQuery__Alternatives_11232);
                    rule__OperatorOrQuery__QueryAssignment_1_1();

                    state._fsp--;
                    if (state.failed) return ;

                    }

                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getOperatorOrQueryAccess().getQueryAssignment_1_1()); 
                    }

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__Alternatives_1"


    // $ANTLR start "rule__ParameterValue__Alternatives"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:609:1: rule__ParameterValue__Alternatives : ( ( ruleLongParameterValue ) | ( ruleDoubleParameterValue ) | ( ruleStringParameterValue ) | ( ( ruleListParameterValue ) ) | ( ruleMapParameterValue ) );
    public final void rule__ParameterValue__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:613:1: ( ( ruleLongParameterValue ) | ( ruleDoubleParameterValue ) | ( ruleStringParameterValue ) | ( ( ruleListParameterValue ) ) | ( ruleMapParameterValue ) )
            int alt5=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt5=1;
                }
                break;
            case RULE_DOUBLE:
                {
                alt5=2;
                }
                break;
            case RULE_STRING:
                {
                alt5=3;
                }
                break;
            case 21:
                {
                int LA5_4 = input.LA(2);

                if ( (synpred9_InternalPql2()) ) {
                    alt5=4;
                }
                else if ( (true) ) {
                    alt5=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 4, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:614:1: ( ruleLongParameterValue )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:614:1: ( ruleLongParameterValue )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:615:1: ruleLongParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getParameterValueAccess().getLongParameterValueParserRuleCall_0()); 
                    }
                    pushFollow(FOLLOW_ruleLongParameterValue_in_rule__ParameterValue__Alternatives1265);
                    ruleLongParameterValue();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getParameterValueAccess().getLongParameterValueParserRuleCall_0()); 
                    }

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:620:6: ( ruleDoubleParameterValue )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:620:6: ( ruleDoubleParameterValue )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:621:1: ruleDoubleParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getParameterValueAccess().getDoubleParameterValueParserRuleCall_1()); 
                    }
                    pushFollow(FOLLOW_ruleDoubleParameterValue_in_rule__ParameterValue__Alternatives1282);
                    ruleDoubleParameterValue();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getParameterValueAccess().getDoubleParameterValueParserRuleCall_1()); 
                    }

                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:626:6: ( ruleStringParameterValue )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:626:6: ( ruleStringParameterValue )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:627:1: ruleStringParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getParameterValueAccess().getStringParameterValueParserRuleCall_2()); 
                    }
                    pushFollow(FOLLOW_ruleStringParameterValue_in_rule__ParameterValue__Alternatives1299);
                    ruleStringParameterValue();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getParameterValueAccess().getStringParameterValueParserRuleCall_2()); 
                    }

                    }


                    }
                    break;
                case 4 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:632:6: ( ( ruleListParameterValue ) )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:632:6: ( ( ruleListParameterValue ) )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:633:1: ( ruleListParameterValue )
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getParameterValueAccess().getListParameterValueParserRuleCall_3()); 
                    }
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:634:1: ( ruleListParameterValue )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:634:3: ruleListParameterValue
                    {
                    pushFollow(FOLLOW_ruleListParameterValue_in_rule__ParameterValue__Alternatives1317);
                    ruleListParameterValue();

                    state._fsp--;
                    if (state.failed) return ;

                    }

                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getParameterValueAccess().getListParameterValueParserRuleCall_3()); 
                    }

                    }


                    }
                    break;
                case 5 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:638:6: ( ruleMapParameterValue )
                    {
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:638:6: ( ruleMapParameterValue )
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:639:1: ruleMapParameterValue
                    {
                    if ( state.backtracking==0 ) {
                       before(grammarAccess.getParameterValueAccess().getMapParameterValueParserRuleCall_4()); 
                    }
                    pushFollow(FOLLOW_ruleMapParameterValue_in_rule__ParameterValue__Alternatives1335);
                    ruleMapParameterValue();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       after(grammarAccess.getParameterValueAccess().getMapParameterValueParserRuleCall_4()); 
                    }

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterValue__Alternatives"


    // $ANTLR start "rule__TemporaryStream__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:651:1: rule__TemporaryStream__Group__0 : rule__TemporaryStream__Group__0__Impl rule__TemporaryStream__Group__1 ;
    public final void rule__TemporaryStream__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:655:1: ( rule__TemporaryStream__Group__0__Impl rule__TemporaryStream__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:656:2: rule__TemporaryStream__Group__0__Impl rule__TemporaryStream__Group__1
            {
            pushFollow(FOLLOW_rule__TemporaryStream__Group__0__Impl_in_rule__TemporaryStream__Group__01365);
            rule__TemporaryStream__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__TemporaryStream__Group__1_in_rule__TemporaryStream__Group__01368);
            rule__TemporaryStream__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TemporaryStream__Group__0"


    // $ANTLR start "rule__TemporaryStream__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:663:1: rule__TemporaryStream__Group__0__Impl : ( ( rule__TemporaryStream__NameAssignment_0 ) ) ;
    public final void rule__TemporaryStream__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:667:1: ( ( ( rule__TemporaryStream__NameAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:668:1: ( ( rule__TemporaryStream__NameAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:668:1: ( ( rule__TemporaryStream__NameAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:669:1: ( rule__TemporaryStream__NameAssignment_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getTemporaryStreamAccess().getNameAssignment_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:670:1: ( rule__TemporaryStream__NameAssignment_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:670:2: rule__TemporaryStream__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__TemporaryStream__NameAssignment_0_in_rule__TemporaryStream__Group__0__Impl1395);
            rule__TemporaryStream__NameAssignment_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getTemporaryStreamAccess().getNameAssignment_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TemporaryStream__Group__0__Impl"


    // $ANTLR start "rule__TemporaryStream__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:680:1: rule__TemporaryStream__Group__1 : rule__TemporaryStream__Group__1__Impl rule__TemporaryStream__Group__2 ;
    public final void rule__TemporaryStream__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:684:1: ( rule__TemporaryStream__Group__1__Impl rule__TemporaryStream__Group__2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:685:2: rule__TemporaryStream__Group__1__Impl rule__TemporaryStream__Group__2
            {
            pushFollow(FOLLOW_rule__TemporaryStream__Group__1__Impl_in_rule__TemporaryStream__Group__11425);
            rule__TemporaryStream__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__TemporaryStream__Group__2_in_rule__TemporaryStream__Group__11428);
            rule__TemporaryStream__Group__2();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TemporaryStream__Group__1"


    // $ANTLR start "rule__TemporaryStream__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:692:1: rule__TemporaryStream__Group__1__Impl : ( '=' ) ;
    public final void rule__TemporaryStream__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:696:1: ( ( '=' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:697:1: ( '=' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:697:1: ( '=' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:698:1: '='
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getTemporaryStreamAccess().getEqualsSignKeyword_1()); 
            }
            match(input,12,FOLLOW_12_in_rule__TemporaryStream__Group__1__Impl1456); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getTemporaryStreamAccess().getEqualsSignKeyword_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TemporaryStream__Group__1__Impl"


    // $ANTLR start "rule__TemporaryStream__Group__2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:711:1: rule__TemporaryStream__Group__2 : rule__TemporaryStream__Group__2__Impl ;
    public final void rule__TemporaryStream__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:715:1: ( rule__TemporaryStream__Group__2__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:716:2: rule__TemporaryStream__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__TemporaryStream__Group__2__Impl_in_rule__TemporaryStream__Group__21487);
            rule__TemporaryStream__Group__2__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TemporaryStream__Group__2"


    // $ANTLR start "rule__TemporaryStream__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:722:1: rule__TemporaryStream__Group__2__Impl : ( ( rule__TemporaryStream__OpAssignment_2 ) ) ;
    public final void rule__TemporaryStream__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:726:1: ( ( ( rule__TemporaryStream__OpAssignment_2 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:727:1: ( ( rule__TemporaryStream__OpAssignment_2 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:727:1: ( ( rule__TemporaryStream__OpAssignment_2 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:728:1: ( rule__TemporaryStream__OpAssignment_2 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getTemporaryStreamAccess().getOpAssignment_2()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:729:1: ( rule__TemporaryStream__OpAssignment_2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:729:2: rule__TemporaryStream__OpAssignment_2
            {
            pushFollow(FOLLOW_rule__TemporaryStream__OpAssignment_2_in_rule__TemporaryStream__Group__2__Impl1514);
            rule__TemporaryStream__OpAssignment_2();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getTemporaryStreamAccess().getOpAssignment_2()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TemporaryStream__Group__2__Impl"


    // $ANTLR start "rule__View__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:745:1: rule__View__Group__0 : rule__View__Group__0__Impl rule__View__Group__1 ;
    public final void rule__View__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:749:1: ( rule__View__Group__0__Impl rule__View__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:750:2: rule__View__Group__0__Impl rule__View__Group__1
            {
            pushFollow(FOLLOW_rule__View__Group__0__Impl_in_rule__View__Group__01550);
            rule__View__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__View__Group__1_in_rule__View__Group__01553);
            rule__View__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__View__Group__0"


    // $ANTLR start "rule__View__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:757:1: rule__View__Group__0__Impl : ( ( rule__View__NameAssignment_0 ) ) ;
    public final void rule__View__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:761:1: ( ( ( rule__View__NameAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:762:1: ( ( rule__View__NameAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:762:1: ( ( rule__View__NameAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:763:1: ( rule__View__NameAssignment_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getViewAccess().getNameAssignment_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:764:1: ( rule__View__NameAssignment_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:764:2: rule__View__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__View__NameAssignment_0_in_rule__View__Group__0__Impl1580);
            rule__View__NameAssignment_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getViewAccess().getNameAssignment_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__View__Group__0__Impl"


    // $ANTLR start "rule__View__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:774:1: rule__View__Group__1 : rule__View__Group__1__Impl rule__View__Group__2 ;
    public final void rule__View__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:778:1: ( rule__View__Group__1__Impl rule__View__Group__2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:779:2: rule__View__Group__1__Impl rule__View__Group__2
            {
            pushFollow(FOLLOW_rule__View__Group__1__Impl_in_rule__View__Group__11610);
            rule__View__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__View__Group__2_in_rule__View__Group__11613);
            rule__View__Group__2();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__View__Group__1"


    // $ANTLR start "rule__View__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:786:1: rule__View__Group__1__Impl : ( ':=' ) ;
    public final void rule__View__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:790:1: ( ( ':=' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:791:1: ( ':=' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:791:1: ( ':=' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:792:1: ':='
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getViewAccess().getColonEqualsSignKeyword_1()); 
            }
            match(input,13,FOLLOW_13_in_rule__View__Group__1__Impl1641); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getViewAccess().getColonEqualsSignKeyword_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__View__Group__1__Impl"


    // $ANTLR start "rule__View__Group__2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:805:1: rule__View__Group__2 : rule__View__Group__2__Impl ;
    public final void rule__View__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:809:1: ( rule__View__Group__2__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:810:2: rule__View__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__View__Group__2__Impl_in_rule__View__Group__21672);
            rule__View__Group__2__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__View__Group__2"


    // $ANTLR start "rule__View__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:816:1: rule__View__Group__2__Impl : ( ( rule__View__OpAssignment_2 ) ) ;
    public final void rule__View__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:820:1: ( ( ( rule__View__OpAssignment_2 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:821:1: ( ( rule__View__OpAssignment_2 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:821:1: ( ( rule__View__OpAssignment_2 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:822:1: ( rule__View__OpAssignment_2 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getViewAccess().getOpAssignment_2()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:823:1: ( rule__View__OpAssignment_2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:823:2: rule__View__OpAssignment_2
            {
            pushFollow(FOLLOW_rule__View__OpAssignment_2_in_rule__View__Group__2__Impl1699);
            rule__View__OpAssignment_2();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getViewAccess().getOpAssignment_2()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__View__Group__2__Impl"


    // $ANTLR start "rule__SharedStream__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:839:1: rule__SharedStream__Group__0 : rule__SharedStream__Group__0__Impl rule__SharedStream__Group__1 ;
    public final void rule__SharedStream__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:843:1: ( rule__SharedStream__Group__0__Impl rule__SharedStream__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:844:2: rule__SharedStream__Group__0__Impl rule__SharedStream__Group__1
            {
            pushFollow(FOLLOW_rule__SharedStream__Group__0__Impl_in_rule__SharedStream__Group__01735);
            rule__SharedStream__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__SharedStream__Group__1_in_rule__SharedStream__Group__01738);
            rule__SharedStream__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SharedStream__Group__0"


    // $ANTLR start "rule__SharedStream__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:851:1: rule__SharedStream__Group__0__Impl : ( ( rule__SharedStream__NameAssignment_0 ) ) ;
    public final void rule__SharedStream__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:855:1: ( ( ( rule__SharedStream__NameAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:856:1: ( ( rule__SharedStream__NameAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:856:1: ( ( rule__SharedStream__NameAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:857:1: ( rule__SharedStream__NameAssignment_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getSharedStreamAccess().getNameAssignment_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:858:1: ( rule__SharedStream__NameAssignment_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:858:2: rule__SharedStream__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__SharedStream__NameAssignment_0_in_rule__SharedStream__Group__0__Impl1765);
            rule__SharedStream__NameAssignment_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getSharedStreamAccess().getNameAssignment_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SharedStream__Group__0__Impl"


    // $ANTLR start "rule__SharedStream__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:868:1: rule__SharedStream__Group__1 : rule__SharedStream__Group__1__Impl rule__SharedStream__Group__2 ;
    public final void rule__SharedStream__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:872:1: ( rule__SharedStream__Group__1__Impl rule__SharedStream__Group__2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:873:2: rule__SharedStream__Group__1__Impl rule__SharedStream__Group__2
            {
            pushFollow(FOLLOW_rule__SharedStream__Group__1__Impl_in_rule__SharedStream__Group__11795);
            rule__SharedStream__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__SharedStream__Group__2_in_rule__SharedStream__Group__11798);
            rule__SharedStream__Group__2();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SharedStream__Group__1"


    // $ANTLR start "rule__SharedStream__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:880:1: rule__SharedStream__Group__1__Impl : ( '::=' ) ;
    public final void rule__SharedStream__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:884:1: ( ( '::=' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:885:1: ( '::=' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:885:1: ( '::=' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:886:1: '::='
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getSharedStreamAccess().getColonColonEqualsSignKeyword_1()); 
            }
            match(input,14,FOLLOW_14_in_rule__SharedStream__Group__1__Impl1826); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getSharedStreamAccess().getColonColonEqualsSignKeyword_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SharedStream__Group__1__Impl"


    // $ANTLR start "rule__SharedStream__Group__2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:899:1: rule__SharedStream__Group__2 : rule__SharedStream__Group__2__Impl ;
    public final void rule__SharedStream__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:903:1: ( rule__SharedStream__Group__2__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:904:2: rule__SharedStream__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__SharedStream__Group__2__Impl_in_rule__SharedStream__Group__21857);
            rule__SharedStream__Group__2__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SharedStream__Group__2"


    // $ANTLR start "rule__SharedStream__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:910:1: rule__SharedStream__Group__2__Impl : ( ( rule__SharedStream__OpAssignment_2 ) ) ;
    public final void rule__SharedStream__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:914:1: ( ( ( rule__SharedStream__OpAssignment_2 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:915:1: ( ( rule__SharedStream__OpAssignment_2 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:915:1: ( ( rule__SharedStream__OpAssignment_2 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:916:1: ( rule__SharedStream__OpAssignment_2 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getSharedStreamAccess().getOpAssignment_2()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:917:1: ( rule__SharedStream__OpAssignment_2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:917:2: rule__SharedStream__OpAssignment_2
            {
            pushFollow(FOLLOW_rule__SharedStream__OpAssignment_2_in_rule__SharedStream__Group__2__Impl1884);
            rule__SharedStream__OpAssignment_2();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getSharedStreamAccess().getOpAssignment_2()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SharedStream__Group__2__Impl"


    // $ANTLR start "rule__Operator__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:933:1: rule__Operator__Group__0 : rule__Operator__Group__0__Impl rule__Operator__Group__1 ;
    public final void rule__Operator__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:937:1: ( rule__Operator__Group__0__Impl rule__Operator__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:938:2: rule__Operator__Group__0__Impl rule__Operator__Group__1
            {
            pushFollow(FOLLOW_rule__Operator__Group__0__Impl_in_rule__Operator__Group__01920);
            rule__Operator__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__Operator__Group__1_in_rule__Operator__Group__01923);
            rule__Operator__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group__0"


    // $ANTLR start "rule__Operator__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:945:1: rule__Operator__Group__0__Impl : ( ( rule__Operator__OperatorTypeAssignment_0 ) ) ;
    public final void rule__Operator__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:949:1: ( ( ( rule__Operator__OperatorTypeAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:950:1: ( ( rule__Operator__OperatorTypeAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:950:1: ( ( rule__Operator__OperatorTypeAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:951:1: ( rule__Operator__OperatorTypeAssignment_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getOperatorTypeAssignment_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:952:1: ( rule__Operator__OperatorTypeAssignment_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:952:2: rule__Operator__OperatorTypeAssignment_0
            {
            pushFollow(FOLLOW_rule__Operator__OperatorTypeAssignment_0_in_rule__Operator__Group__0__Impl1950);
            rule__Operator__OperatorTypeAssignment_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getOperatorTypeAssignment_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group__0__Impl"


    // $ANTLR start "rule__Operator__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:962:1: rule__Operator__Group__1 : rule__Operator__Group__1__Impl rule__Operator__Group__2 ;
    public final void rule__Operator__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:966:1: ( rule__Operator__Group__1__Impl rule__Operator__Group__2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:967:2: rule__Operator__Group__1__Impl rule__Operator__Group__2
            {
            pushFollow(FOLLOW_rule__Operator__Group__1__Impl_in_rule__Operator__Group__11980);
            rule__Operator__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__Operator__Group__2_in_rule__Operator__Group__11983);
            rule__Operator__Group__2();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group__1"


    // $ANTLR start "rule__Operator__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:974:1: rule__Operator__Group__1__Impl : ( '(' ) ;
    public final void rule__Operator__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:978:1: ( ( '(' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:979:1: ( '(' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:979:1: ( '(' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:980:1: '('
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getLeftParenthesisKeyword_1()); 
            }
            match(input,15,FOLLOW_15_in_rule__Operator__Group__1__Impl2011); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getLeftParenthesisKeyword_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group__1__Impl"


    // $ANTLR start "rule__Operator__Group__2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:993:1: rule__Operator__Group__2 : rule__Operator__Group__2__Impl rule__Operator__Group__3 ;
    public final void rule__Operator__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:997:1: ( rule__Operator__Group__2__Impl rule__Operator__Group__3 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:998:2: rule__Operator__Group__2__Impl rule__Operator__Group__3
            {
            pushFollow(FOLLOW_rule__Operator__Group__2__Impl_in_rule__Operator__Group__22042);
            rule__Operator__Group__2__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__Operator__Group__3_in_rule__Operator__Group__22045);
            rule__Operator__Group__3();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group__2"


    // $ANTLR start "rule__Operator__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1005:1: rule__Operator__Group__2__Impl : ( ( rule__Operator__Alternatives_2 )? ) ;
    public final void rule__Operator__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1009:1: ( ( ( rule__Operator__Alternatives_2 )? ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1010:1: ( ( rule__Operator__Alternatives_2 )? )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1010:1: ( ( rule__Operator__Alternatives_2 )? )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1011:1: ( rule__Operator__Alternatives_2 )?
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getAlternatives_2()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1012:1: ( rule__Operator__Alternatives_2 )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( ((LA6_0>=RULE_ID && LA6_0<=RULE_INT)||LA6_0==19) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1012:2: rule__Operator__Alternatives_2
                    {
                    pushFollow(FOLLOW_rule__Operator__Alternatives_2_in_rule__Operator__Group__2__Impl2072);
                    rule__Operator__Alternatives_2();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getAlternatives_2()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group__2__Impl"


    // $ANTLR start "rule__Operator__Group__3"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1022:1: rule__Operator__Group__3 : rule__Operator__Group__3__Impl ;
    public final void rule__Operator__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1026:1: ( rule__Operator__Group__3__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1027:2: rule__Operator__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__Operator__Group__3__Impl_in_rule__Operator__Group__32103);
            rule__Operator__Group__3__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group__3"


    // $ANTLR start "rule__Operator__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1033:1: rule__Operator__Group__3__Impl : ( ')' ) ;
    public final void rule__Operator__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1037:1: ( ( ')' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1038:1: ( ')' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1038:1: ( ')' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1039:1: ')'
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getRightParenthesisKeyword_3()); 
            }
            match(input,16,FOLLOW_16_in_rule__Operator__Group__3__Impl2131); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getRightParenthesisKeyword_3()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group__3__Impl"


    // $ANTLR start "rule__Operator__Group_2_1__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1060:1: rule__Operator__Group_2_1__0 : rule__Operator__Group_2_1__0__Impl rule__Operator__Group_2_1__1 ;
    public final void rule__Operator__Group_2_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1064:1: ( rule__Operator__Group_2_1__0__Impl rule__Operator__Group_2_1__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1065:2: rule__Operator__Group_2_1__0__Impl rule__Operator__Group_2_1__1
            {
            pushFollow(FOLLOW_rule__Operator__Group_2_1__0__Impl_in_rule__Operator__Group_2_1__02170);
            rule__Operator__Group_2_1__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__Operator__Group_2_1__1_in_rule__Operator__Group_2_1__02173);
            rule__Operator__Group_2_1__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group_2_1__0"


    // $ANTLR start "rule__Operator__Group_2_1__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1072:1: rule__Operator__Group_2_1__0__Impl : ( ( rule__Operator__ParametersAssignment_2_1_0 ) ) ;
    public final void rule__Operator__Group_2_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1076:1: ( ( ( rule__Operator__ParametersAssignment_2_1_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1077:1: ( ( rule__Operator__ParametersAssignment_2_1_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1077:1: ( ( rule__Operator__ParametersAssignment_2_1_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1078:1: ( rule__Operator__ParametersAssignment_2_1_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getParametersAssignment_2_1_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1079:1: ( rule__Operator__ParametersAssignment_2_1_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1079:2: rule__Operator__ParametersAssignment_2_1_0
            {
            pushFollow(FOLLOW_rule__Operator__ParametersAssignment_2_1_0_in_rule__Operator__Group_2_1__0__Impl2200);
            rule__Operator__ParametersAssignment_2_1_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getParametersAssignment_2_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group_2_1__0__Impl"


    // $ANTLR start "rule__Operator__Group_2_1__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1089:1: rule__Operator__Group_2_1__1 : rule__Operator__Group_2_1__1__Impl ;
    public final void rule__Operator__Group_2_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1093:1: ( rule__Operator__Group_2_1__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1094:2: rule__Operator__Group_2_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Operator__Group_2_1__1__Impl_in_rule__Operator__Group_2_1__12230);
            rule__Operator__Group_2_1__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group_2_1__1"


    // $ANTLR start "rule__Operator__Group_2_1__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1100:1: rule__Operator__Group_2_1__1__Impl : ( ( rule__Operator__Group_2_1_1__0 )? ) ;
    public final void rule__Operator__Group_2_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1104:1: ( ( ( rule__Operator__Group_2_1_1__0 )? ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1105:1: ( ( rule__Operator__Group_2_1_1__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1105:1: ( ( rule__Operator__Group_2_1_1__0 )? )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1106:1: ( rule__Operator__Group_2_1_1__0 )?
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getGroup_2_1_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1107:1: ( rule__Operator__Group_2_1_1__0 )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==17) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1107:2: rule__Operator__Group_2_1_1__0
                    {
                    pushFollow(FOLLOW_rule__Operator__Group_2_1_1__0_in_rule__Operator__Group_2_1__1__Impl2257);
                    rule__Operator__Group_2_1_1__0();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getGroup_2_1_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group_2_1__1__Impl"


    // $ANTLR start "rule__Operator__Group_2_1_1__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1121:1: rule__Operator__Group_2_1_1__0 : rule__Operator__Group_2_1_1__0__Impl rule__Operator__Group_2_1_1__1 ;
    public final void rule__Operator__Group_2_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1125:1: ( rule__Operator__Group_2_1_1__0__Impl rule__Operator__Group_2_1_1__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1126:2: rule__Operator__Group_2_1_1__0__Impl rule__Operator__Group_2_1_1__1
            {
            pushFollow(FOLLOW_rule__Operator__Group_2_1_1__0__Impl_in_rule__Operator__Group_2_1_1__02292);
            rule__Operator__Group_2_1_1__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__Operator__Group_2_1_1__1_in_rule__Operator__Group_2_1_1__02295);
            rule__Operator__Group_2_1_1__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group_2_1_1__0"


    // $ANTLR start "rule__Operator__Group_2_1_1__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1133:1: rule__Operator__Group_2_1_1__0__Impl : ( ',' ) ;
    public final void rule__Operator__Group_2_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1137:1: ( ( ',' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1138:1: ( ',' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1138:1: ( ',' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1139:1: ','
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getCommaKeyword_2_1_1_0()); 
            }
            match(input,17,FOLLOW_17_in_rule__Operator__Group_2_1_1__0__Impl2323); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getCommaKeyword_2_1_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group_2_1_1__0__Impl"


    // $ANTLR start "rule__Operator__Group_2_1_1__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1152:1: rule__Operator__Group_2_1_1__1 : rule__Operator__Group_2_1_1__1__Impl ;
    public final void rule__Operator__Group_2_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1156:1: ( rule__Operator__Group_2_1_1__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1157:2: rule__Operator__Group_2_1_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Operator__Group_2_1_1__1__Impl_in_rule__Operator__Group_2_1_1__12354);
            rule__Operator__Group_2_1_1__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group_2_1_1__1"


    // $ANTLR start "rule__Operator__Group_2_1_1__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1163:1: rule__Operator__Group_2_1_1__1__Impl : ( ( rule__Operator__OperatorsAssignment_2_1_1_1 ) ) ;
    public final void rule__Operator__Group_2_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1167:1: ( ( ( rule__Operator__OperatorsAssignment_2_1_1_1 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1168:1: ( ( rule__Operator__OperatorsAssignment_2_1_1_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1168:1: ( ( rule__Operator__OperatorsAssignment_2_1_1_1 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1169:1: ( rule__Operator__OperatorsAssignment_2_1_1_1 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getOperatorsAssignment_2_1_1_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1170:1: ( rule__Operator__OperatorsAssignment_2_1_1_1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1170:2: rule__Operator__OperatorsAssignment_2_1_1_1
            {
            pushFollow(FOLLOW_rule__Operator__OperatorsAssignment_2_1_1_1_in_rule__Operator__Group_2_1_1__1__Impl2381);
            rule__Operator__OperatorsAssignment_2_1_1_1();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getOperatorsAssignment_2_1_1_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Group_2_1_1__1__Impl"


    // $ANTLR start "rule__OperatorList__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1184:1: rule__OperatorList__Group__0 : rule__OperatorList__Group__0__Impl rule__OperatorList__Group__1 ;
    public final void rule__OperatorList__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1188:1: ( rule__OperatorList__Group__0__Impl rule__OperatorList__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1189:2: rule__OperatorList__Group__0__Impl rule__OperatorList__Group__1
            {
            pushFollow(FOLLOW_rule__OperatorList__Group__0__Impl_in_rule__OperatorList__Group__02415);
            rule__OperatorList__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__OperatorList__Group__1_in_rule__OperatorList__Group__02418);
            rule__OperatorList__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__Group__0"


    // $ANTLR start "rule__OperatorList__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1196:1: rule__OperatorList__Group__0__Impl : ( ( rule__OperatorList__ElementsAssignment_0 ) ) ;
    public final void rule__OperatorList__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1200:1: ( ( ( rule__OperatorList__ElementsAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1201:1: ( ( rule__OperatorList__ElementsAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1201:1: ( ( rule__OperatorList__ElementsAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1202:1: ( rule__OperatorList__ElementsAssignment_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorListAccess().getElementsAssignment_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1203:1: ( rule__OperatorList__ElementsAssignment_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1203:2: rule__OperatorList__ElementsAssignment_0
            {
            pushFollow(FOLLOW_rule__OperatorList__ElementsAssignment_0_in_rule__OperatorList__Group__0__Impl2445);
            rule__OperatorList__ElementsAssignment_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorListAccess().getElementsAssignment_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__Group__0__Impl"


    // $ANTLR start "rule__OperatorList__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1213:1: rule__OperatorList__Group__1 : rule__OperatorList__Group__1__Impl ;
    public final void rule__OperatorList__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1217:1: ( rule__OperatorList__Group__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1218:2: rule__OperatorList__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__OperatorList__Group__1__Impl_in_rule__OperatorList__Group__12475);
            rule__OperatorList__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__Group__1"


    // $ANTLR start "rule__OperatorList__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1224:1: rule__OperatorList__Group__1__Impl : ( ( rule__OperatorList__Group_1__0 )* ) ;
    public final void rule__OperatorList__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1228:1: ( ( ( rule__OperatorList__Group_1__0 )* ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1229:1: ( ( rule__OperatorList__Group_1__0 )* )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1229:1: ( ( rule__OperatorList__Group_1__0 )* )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1230:1: ( rule__OperatorList__Group_1__0 )*
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorListAccess().getGroup_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1231:1: ( rule__OperatorList__Group_1__0 )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==17) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1231:2: rule__OperatorList__Group_1__0
            	    {
            	    pushFollow(FOLLOW_rule__OperatorList__Group_1__0_in_rule__OperatorList__Group__1__Impl2502);
            	    rule__OperatorList__Group_1__0();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorListAccess().getGroup_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__Group__1__Impl"


    // $ANTLR start "rule__OperatorList__Group_1__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1245:1: rule__OperatorList__Group_1__0 : rule__OperatorList__Group_1__0__Impl rule__OperatorList__Group_1__1 ;
    public final void rule__OperatorList__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1249:1: ( rule__OperatorList__Group_1__0__Impl rule__OperatorList__Group_1__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1250:2: rule__OperatorList__Group_1__0__Impl rule__OperatorList__Group_1__1
            {
            pushFollow(FOLLOW_rule__OperatorList__Group_1__0__Impl_in_rule__OperatorList__Group_1__02537);
            rule__OperatorList__Group_1__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__OperatorList__Group_1__1_in_rule__OperatorList__Group_1__02540);
            rule__OperatorList__Group_1__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__Group_1__0"


    // $ANTLR start "rule__OperatorList__Group_1__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1257:1: rule__OperatorList__Group_1__0__Impl : ( ',' ) ;
    public final void rule__OperatorList__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1261:1: ( ( ',' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1262:1: ( ',' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1262:1: ( ',' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1263:1: ','
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorListAccess().getCommaKeyword_1_0()); 
            }
            match(input,17,FOLLOW_17_in_rule__OperatorList__Group_1__0__Impl2568); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorListAccess().getCommaKeyword_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__Group_1__0__Impl"


    // $ANTLR start "rule__OperatorList__Group_1__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1276:1: rule__OperatorList__Group_1__1 : rule__OperatorList__Group_1__1__Impl ;
    public final void rule__OperatorList__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1280:1: ( rule__OperatorList__Group_1__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1281:2: rule__OperatorList__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__OperatorList__Group_1__1__Impl_in_rule__OperatorList__Group_1__12599);
            rule__OperatorList__Group_1__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__Group_1__1"


    // $ANTLR start "rule__OperatorList__Group_1__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1287:1: rule__OperatorList__Group_1__1__Impl : ( ( rule__OperatorList__ElementsAssignment_1_1 ) ) ;
    public final void rule__OperatorList__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1291:1: ( ( ( rule__OperatorList__ElementsAssignment_1_1 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1292:1: ( ( rule__OperatorList__ElementsAssignment_1_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1292:1: ( ( rule__OperatorList__ElementsAssignment_1_1 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1293:1: ( rule__OperatorList__ElementsAssignment_1_1 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorListAccess().getElementsAssignment_1_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1294:1: ( rule__OperatorList__ElementsAssignment_1_1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1294:2: rule__OperatorList__ElementsAssignment_1_1
            {
            pushFollow(FOLLOW_rule__OperatorList__ElementsAssignment_1_1_in_rule__OperatorList__Group_1__1__Impl2626);
            rule__OperatorList__ElementsAssignment_1_1();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorListAccess().getElementsAssignment_1_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__Group_1__1__Impl"


    // $ANTLR start "rule__OperatorOrQuery__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1308:1: rule__OperatorOrQuery__Group__0 : rule__OperatorOrQuery__Group__0__Impl rule__OperatorOrQuery__Group__1 ;
    public final void rule__OperatorOrQuery__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1312:1: ( rule__OperatorOrQuery__Group__0__Impl rule__OperatorOrQuery__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1313:2: rule__OperatorOrQuery__Group__0__Impl rule__OperatorOrQuery__Group__1
            {
            pushFollow(FOLLOW_rule__OperatorOrQuery__Group__0__Impl_in_rule__OperatorOrQuery__Group__02660);
            rule__OperatorOrQuery__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__OperatorOrQuery__Group__1_in_rule__OperatorOrQuery__Group__02663);
            rule__OperatorOrQuery__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__Group__0"


    // $ANTLR start "rule__OperatorOrQuery__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1320:1: rule__OperatorOrQuery__Group__0__Impl : ( ( rule__OperatorOrQuery__Group_0__0 )? ) ;
    public final void rule__OperatorOrQuery__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1324:1: ( ( ( rule__OperatorOrQuery__Group_0__0 )? ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1325:1: ( ( rule__OperatorOrQuery__Group_0__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1325:1: ( ( rule__OperatorOrQuery__Group_0__0 )? )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1326:1: ( rule__OperatorOrQuery__Group_0__0 )?
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryAccess().getGroup_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1327:1: ( rule__OperatorOrQuery__Group_0__0 )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==RULE_INT) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1327:2: rule__OperatorOrQuery__Group_0__0
                    {
                    pushFollow(FOLLOW_rule__OperatorOrQuery__Group_0__0_in_rule__OperatorOrQuery__Group__0__Impl2690);
                    rule__OperatorOrQuery__Group_0__0();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryAccess().getGroup_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__Group__0__Impl"


    // $ANTLR start "rule__OperatorOrQuery__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1337:1: rule__OperatorOrQuery__Group__1 : rule__OperatorOrQuery__Group__1__Impl ;
    public final void rule__OperatorOrQuery__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1341:1: ( rule__OperatorOrQuery__Group__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1342:2: rule__OperatorOrQuery__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__OperatorOrQuery__Group__1__Impl_in_rule__OperatorOrQuery__Group__12721);
            rule__OperatorOrQuery__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__Group__1"


    // $ANTLR start "rule__OperatorOrQuery__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1348:1: rule__OperatorOrQuery__Group__1__Impl : ( ( rule__OperatorOrQuery__Alternatives_1 ) ) ;
    public final void rule__OperatorOrQuery__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1352:1: ( ( ( rule__OperatorOrQuery__Alternatives_1 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1353:1: ( ( rule__OperatorOrQuery__Alternatives_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1353:1: ( ( rule__OperatorOrQuery__Alternatives_1 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1354:1: ( rule__OperatorOrQuery__Alternatives_1 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryAccess().getAlternatives_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1355:1: ( rule__OperatorOrQuery__Alternatives_1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1355:2: rule__OperatorOrQuery__Alternatives_1
            {
            pushFollow(FOLLOW_rule__OperatorOrQuery__Alternatives_1_in_rule__OperatorOrQuery__Group__1__Impl2748);
            rule__OperatorOrQuery__Alternatives_1();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryAccess().getAlternatives_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__Group__1__Impl"


    // $ANTLR start "rule__OperatorOrQuery__Group_0__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1369:1: rule__OperatorOrQuery__Group_0__0 : rule__OperatorOrQuery__Group_0__0__Impl rule__OperatorOrQuery__Group_0__1 ;
    public final void rule__OperatorOrQuery__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1373:1: ( rule__OperatorOrQuery__Group_0__0__Impl rule__OperatorOrQuery__Group_0__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1374:2: rule__OperatorOrQuery__Group_0__0__Impl rule__OperatorOrQuery__Group_0__1
            {
            pushFollow(FOLLOW_rule__OperatorOrQuery__Group_0__0__Impl_in_rule__OperatorOrQuery__Group_0__02782);
            rule__OperatorOrQuery__Group_0__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__OperatorOrQuery__Group_0__1_in_rule__OperatorOrQuery__Group_0__02785);
            rule__OperatorOrQuery__Group_0__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__Group_0__0"


    // $ANTLR start "rule__OperatorOrQuery__Group_0__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1381:1: rule__OperatorOrQuery__Group_0__0__Impl : ( ( rule__OperatorOrQuery__OutputPortAssignment_0_0 ) ) ;
    public final void rule__OperatorOrQuery__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1385:1: ( ( ( rule__OperatorOrQuery__OutputPortAssignment_0_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1386:1: ( ( rule__OperatorOrQuery__OutputPortAssignment_0_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1386:1: ( ( rule__OperatorOrQuery__OutputPortAssignment_0_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1387:1: ( rule__OperatorOrQuery__OutputPortAssignment_0_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryAccess().getOutputPortAssignment_0_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1388:1: ( rule__OperatorOrQuery__OutputPortAssignment_0_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1388:2: rule__OperatorOrQuery__OutputPortAssignment_0_0
            {
            pushFollow(FOLLOW_rule__OperatorOrQuery__OutputPortAssignment_0_0_in_rule__OperatorOrQuery__Group_0__0__Impl2812);
            rule__OperatorOrQuery__OutputPortAssignment_0_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryAccess().getOutputPortAssignment_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__Group_0__0__Impl"


    // $ANTLR start "rule__OperatorOrQuery__Group_0__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1398:1: rule__OperatorOrQuery__Group_0__1 : rule__OperatorOrQuery__Group_0__1__Impl ;
    public final void rule__OperatorOrQuery__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1402:1: ( rule__OperatorOrQuery__Group_0__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1403:2: rule__OperatorOrQuery__Group_0__1__Impl
            {
            pushFollow(FOLLOW_rule__OperatorOrQuery__Group_0__1__Impl_in_rule__OperatorOrQuery__Group_0__12842);
            rule__OperatorOrQuery__Group_0__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__Group_0__1"


    // $ANTLR start "rule__OperatorOrQuery__Group_0__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1409:1: rule__OperatorOrQuery__Group_0__1__Impl : ( ':' ) ;
    public final void rule__OperatorOrQuery__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1413:1: ( ( ':' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1414:1: ( ':' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1414:1: ( ':' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1415:1: ':'
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryAccess().getColonKeyword_0_1()); 
            }
            match(input,18,FOLLOW_18_in_rule__OperatorOrQuery__Group_0__1__Impl2870); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryAccess().getColonKeyword_0_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__Group_0__1__Impl"


    // $ANTLR start "rule__ParameterList__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1432:1: rule__ParameterList__Group__0 : rule__ParameterList__Group__0__Impl rule__ParameterList__Group__1 ;
    public final void rule__ParameterList__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1436:1: ( rule__ParameterList__Group__0__Impl rule__ParameterList__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1437:2: rule__ParameterList__Group__0__Impl rule__ParameterList__Group__1
            {
            pushFollow(FOLLOW_rule__ParameterList__Group__0__Impl_in_rule__ParameterList__Group__02905);
            rule__ParameterList__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__ParameterList__Group__1_in_rule__ParameterList__Group__02908);
            rule__ParameterList__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group__0"


    // $ANTLR start "rule__ParameterList__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1444:1: rule__ParameterList__Group__0__Impl : ( '{' ) ;
    public final void rule__ParameterList__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1448:1: ( ( '{' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1449:1: ( '{' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1449:1: ( '{' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1450:1: '{'
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListAccess().getLeftCurlyBracketKeyword_0()); 
            }
            match(input,19,FOLLOW_19_in_rule__ParameterList__Group__0__Impl2936); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListAccess().getLeftCurlyBracketKeyword_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group__0__Impl"


    // $ANTLR start "rule__ParameterList__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1463:1: rule__ParameterList__Group__1 : rule__ParameterList__Group__1__Impl rule__ParameterList__Group__2 ;
    public final void rule__ParameterList__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1467:1: ( rule__ParameterList__Group__1__Impl rule__ParameterList__Group__2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1468:2: rule__ParameterList__Group__1__Impl rule__ParameterList__Group__2
            {
            pushFollow(FOLLOW_rule__ParameterList__Group__1__Impl_in_rule__ParameterList__Group__12967);
            rule__ParameterList__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__ParameterList__Group__2_in_rule__ParameterList__Group__12970);
            rule__ParameterList__Group__2();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group__1"


    // $ANTLR start "rule__ParameterList__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1475:1: rule__ParameterList__Group__1__Impl : ( ( rule__ParameterList__ElementsAssignment_1 ) ) ;
    public final void rule__ParameterList__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1479:1: ( ( ( rule__ParameterList__ElementsAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1480:1: ( ( rule__ParameterList__ElementsAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1480:1: ( ( rule__ParameterList__ElementsAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1481:1: ( rule__ParameterList__ElementsAssignment_1 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListAccess().getElementsAssignment_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1482:1: ( rule__ParameterList__ElementsAssignment_1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1482:2: rule__ParameterList__ElementsAssignment_1
            {
            pushFollow(FOLLOW_rule__ParameterList__ElementsAssignment_1_in_rule__ParameterList__Group__1__Impl2997);
            rule__ParameterList__ElementsAssignment_1();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListAccess().getElementsAssignment_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group__1__Impl"


    // $ANTLR start "rule__ParameterList__Group__2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1492:1: rule__ParameterList__Group__2 : rule__ParameterList__Group__2__Impl rule__ParameterList__Group__3 ;
    public final void rule__ParameterList__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1496:1: ( rule__ParameterList__Group__2__Impl rule__ParameterList__Group__3 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1497:2: rule__ParameterList__Group__2__Impl rule__ParameterList__Group__3
            {
            pushFollow(FOLLOW_rule__ParameterList__Group__2__Impl_in_rule__ParameterList__Group__23027);
            rule__ParameterList__Group__2__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__ParameterList__Group__3_in_rule__ParameterList__Group__23030);
            rule__ParameterList__Group__3();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group__2"


    // $ANTLR start "rule__ParameterList__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1504:1: rule__ParameterList__Group__2__Impl : ( ( rule__ParameterList__Group_2__0 )* ) ;
    public final void rule__ParameterList__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1508:1: ( ( ( rule__ParameterList__Group_2__0 )* ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1509:1: ( ( rule__ParameterList__Group_2__0 )* )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1509:1: ( ( rule__ParameterList__Group_2__0 )* )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1510:1: ( rule__ParameterList__Group_2__0 )*
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListAccess().getGroup_2()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1511:1: ( rule__ParameterList__Group_2__0 )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==17) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1511:2: rule__ParameterList__Group_2__0
            	    {
            	    pushFollow(FOLLOW_rule__ParameterList__Group_2__0_in_rule__ParameterList__Group__2__Impl3057);
            	    rule__ParameterList__Group_2__0();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListAccess().getGroup_2()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group__2__Impl"


    // $ANTLR start "rule__ParameterList__Group__3"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1521:1: rule__ParameterList__Group__3 : rule__ParameterList__Group__3__Impl ;
    public final void rule__ParameterList__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1525:1: ( rule__ParameterList__Group__3__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1526:2: rule__ParameterList__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__ParameterList__Group__3__Impl_in_rule__ParameterList__Group__33088);
            rule__ParameterList__Group__3__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group__3"


    // $ANTLR start "rule__ParameterList__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1532:1: rule__ParameterList__Group__3__Impl : ( '}' ) ;
    public final void rule__ParameterList__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1536:1: ( ( '}' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1537:1: ( '}' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1537:1: ( '}' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1538:1: '}'
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListAccess().getRightCurlyBracketKeyword_3()); 
            }
            match(input,20,FOLLOW_20_in_rule__ParameterList__Group__3__Impl3116); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListAccess().getRightCurlyBracketKeyword_3()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group__3__Impl"


    // $ANTLR start "rule__ParameterList__Group_2__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1559:1: rule__ParameterList__Group_2__0 : rule__ParameterList__Group_2__0__Impl rule__ParameterList__Group_2__1 ;
    public final void rule__ParameterList__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1563:1: ( rule__ParameterList__Group_2__0__Impl rule__ParameterList__Group_2__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1564:2: rule__ParameterList__Group_2__0__Impl rule__ParameterList__Group_2__1
            {
            pushFollow(FOLLOW_rule__ParameterList__Group_2__0__Impl_in_rule__ParameterList__Group_2__03155);
            rule__ParameterList__Group_2__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__ParameterList__Group_2__1_in_rule__ParameterList__Group_2__03158);
            rule__ParameterList__Group_2__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group_2__0"


    // $ANTLR start "rule__ParameterList__Group_2__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1571:1: rule__ParameterList__Group_2__0__Impl : ( ',' ) ;
    public final void rule__ParameterList__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1575:1: ( ( ',' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1576:1: ( ',' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1576:1: ( ',' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1577:1: ','
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListAccess().getCommaKeyword_2_0()); 
            }
            match(input,17,FOLLOW_17_in_rule__ParameterList__Group_2__0__Impl3186); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListAccess().getCommaKeyword_2_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group_2__0__Impl"


    // $ANTLR start "rule__ParameterList__Group_2__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1590:1: rule__ParameterList__Group_2__1 : rule__ParameterList__Group_2__1__Impl ;
    public final void rule__ParameterList__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1594:1: ( rule__ParameterList__Group_2__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1595:2: rule__ParameterList__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ParameterList__Group_2__1__Impl_in_rule__ParameterList__Group_2__13217);
            rule__ParameterList__Group_2__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group_2__1"


    // $ANTLR start "rule__ParameterList__Group_2__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1601:1: rule__ParameterList__Group_2__1__Impl : ( ( rule__ParameterList__ElementsAssignment_2_1 ) ) ;
    public final void rule__ParameterList__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1605:1: ( ( ( rule__ParameterList__ElementsAssignment_2_1 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1606:1: ( ( rule__ParameterList__ElementsAssignment_2_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1606:1: ( ( rule__ParameterList__ElementsAssignment_2_1 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1607:1: ( rule__ParameterList__ElementsAssignment_2_1 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListAccess().getElementsAssignment_2_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1608:1: ( rule__ParameterList__ElementsAssignment_2_1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1608:2: rule__ParameterList__ElementsAssignment_2_1
            {
            pushFollow(FOLLOW_rule__ParameterList__ElementsAssignment_2_1_in_rule__ParameterList__Group_2__1__Impl3244);
            rule__ParameterList__ElementsAssignment_2_1();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListAccess().getElementsAssignment_2_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__Group_2__1__Impl"


    // $ANTLR start "rule__Parameter__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1622:1: rule__Parameter__Group__0 : rule__Parameter__Group__0__Impl rule__Parameter__Group__1 ;
    public final void rule__Parameter__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1626:1: ( rule__Parameter__Group__0__Impl rule__Parameter__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1627:2: rule__Parameter__Group__0__Impl rule__Parameter__Group__1
            {
            pushFollow(FOLLOW_rule__Parameter__Group__0__Impl_in_rule__Parameter__Group__03278);
            rule__Parameter__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__Parameter__Group__1_in_rule__Parameter__Group__03281);
            rule__Parameter__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__0"


    // $ANTLR start "rule__Parameter__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1634:1: rule__Parameter__Group__0__Impl : ( ( rule__Parameter__NameAssignment_0 ) ) ;
    public final void rule__Parameter__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1638:1: ( ( ( rule__Parameter__NameAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1639:1: ( ( rule__Parameter__NameAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1639:1: ( ( rule__Parameter__NameAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1640:1: ( rule__Parameter__NameAssignment_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterAccess().getNameAssignment_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1641:1: ( rule__Parameter__NameAssignment_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1641:2: rule__Parameter__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__Parameter__NameAssignment_0_in_rule__Parameter__Group__0__Impl3308);
            rule__Parameter__NameAssignment_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterAccess().getNameAssignment_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__0__Impl"


    // $ANTLR start "rule__Parameter__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1651:1: rule__Parameter__Group__1 : rule__Parameter__Group__1__Impl rule__Parameter__Group__2 ;
    public final void rule__Parameter__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1655:1: ( rule__Parameter__Group__1__Impl rule__Parameter__Group__2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1656:2: rule__Parameter__Group__1__Impl rule__Parameter__Group__2
            {
            pushFollow(FOLLOW_rule__Parameter__Group__1__Impl_in_rule__Parameter__Group__13338);
            rule__Parameter__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__Parameter__Group__2_in_rule__Parameter__Group__13341);
            rule__Parameter__Group__2();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__1"


    // $ANTLR start "rule__Parameter__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1663:1: rule__Parameter__Group__1__Impl : ( '=' ) ;
    public final void rule__Parameter__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1667:1: ( ( '=' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1668:1: ( '=' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1668:1: ( '=' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1669:1: '='
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterAccess().getEqualsSignKeyword_1()); 
            }
            match(input,12,FOLLOW_12_in_rule__Parameter__Group__1__Impl3369); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterAccess().getEqualsSignKeyword_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__1__Impl"


    // $ANTLR start "rule__Parameter__Group__2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1682:1: rule__Parameter__Group__2 : rule__Parameter__Group__2__Impl ;
    public final void rule__Parameter__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1686:1: ( rule__Parameter__Group__2__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1687:2: rule__Parameter__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Parameter__Group__2__Impl_in_rule__Parameter__Group__23400);
            rule__Parameter__Group__2__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__2"


    // $ANTLR start "rule__Parameter__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1693:1: rule__Parameter__Group__2__Impl : ( ( rule__Parameter__ValueAssignment_2 ) ) ;
    public final void rule__Parameter__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1697:1: ( ( ( rule__Parameter__ValueAssignment_2 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1698:1: ( ( rule__Parameter__ValueAssignment_2 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1698:1: ( ( rule__Parameter__ValueAssignment_2 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1699:1: ( rule__Parameter__ValueAssignment_2 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterAccess().getValueAssignment_2()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1700:1: ( rule__Parameter__ValueAssignment_2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1700:2: rule__Parameter__ValueAssignment_2
            {
            pushFollow(FOLLOW_rule__Parameter__ValueAssignment_2_in_rule__Parameter__Group__2__Impl3427);
            rule__Parameter__ValueAssignment_2();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterAccess().getValueAssignment_2()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__2__Impl"


    // $ANTLR start "rule__ListParameterValue__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1716:1: rule__ListParameterValue__Group__0 : rule__ListParameterValue__Group__0__Impl rule__ListParameterValue__Group__1 ;
    public final void rule__ListParameterValue__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1720:1: ( rule__ListParameterValue__Group__0__Impl rule__ListParameterValue__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1721:2: rule__ListParameterValue__Group__0__Impl rule__ListParameterValue__Group__1
            {
            pushFollow(FOLLOW_rule__ListParameterValue__Group__0__Impl_in_rule__ListParameterValue__Group__03463);
            rule__ListParameterValue__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__ListParameterValue__Group__1_in_rule__ListParameterValue__Group__03466);
            rule__ListParameterValue__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group__0"


    // $ANTLR start "rule__ListParameterValue__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1728:1: rule__ListParameterValue__Group__0__Impl : ( () ) ;
    public final void rule__ListParameterValue__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1732:1: ( ( () ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1733:1: ( () )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1733:1: ( () )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1734:1: ()
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getListAction_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1735:1: ()
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1737:1: 
            {
            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getListAction_0()); 
            }

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group__0__Impl"


    // $ANTLR start "rule__ListParameterValue__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1747:1: rule__ListParameterValue__Group__1 : rule__ListParameterValue__Group__1__Impl rule__ListParameterValue__Group__2 ;
    public final void rule__ListParameterValue__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1751:1: ( rule__ListParameterValue__Group__1__Impl rule__ListParameterValue__Group__2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1752:2: rule__ListParameterValue__Group__1__Impl rule__ListParameterValue__Group__2
            {
            pushFollow(FOLLOW_rule__ListParameterValue__Group__1__Impl_in_rule__ListParameterValue__Group__13524);
            rule__ListParameterValue__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__ListParameterValue__Group__2_in_rule__ListParameterValue__Group__13527);
            rule__ListParameterValue__Group__2();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group__1"


    // $ANTLR start "rule__ListParameterValue__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1759:1: rule__ListParameterValue__Group__1__Impl : ( '[' ) ;
    public final void rule__ListParameterValue__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1763:1: ( ( '[' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1764:1: ( '[' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1764:1: ( '[' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1765:1: '['
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getLeftSquareBracketKeyword_1()); 
            }
            match(input,21,FOLLOW_21_in_rule__ListParameterValue__Group__1__Impl3555); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getLeftSquareBracketKeyword_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group__1__Impl"


    // $ANTLR start "rule__ListParameterValue__Group__2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1778:1: rule__ListParameterValue__Group__2 : rule__ListParameterValue__Group__2__Impl rule__ListParameterValue__Group__3 ;
    public final void rule__ListParameterValue__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1782:1: ( rule__ListParameterValue__Group__2__Impl rule__ListParameterValue__Group__3 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1783:2: rule__ListParameterValue__Group__2__Impl rule__ListParameterValue__Group__3
            {
            pushFollow(FOLLOW_rule__ListParameterValue__Group__2__Impl_in_rule__ListParameterValue__Group__23586);
            rule__ListParameterValue__Group__2__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__ListParameterValue__Group__3_in_rule__ListParameterValue__Group__23589);
            rule__ListParameterValue__Group__3();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group__2"


    // $ANTLR start "rule__ListParameterValue__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1790:1: rule__ListParameterValue__Group__2__Impl : ( ( rule__ListParameterValue__Group_2__0 )? ) ;
    public final void rule__ListParameterValue__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1794:1: ( ( ( rule__ListParameterValue__Group_2__0 )? ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1795:1: ( ( rule__ListParameterValue__Group_2__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1795:1: ( ( rule__ListParameterValue__Group_2__0 )? )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1796:1: ( rule__ListParameterValue__Group_2__0 )?
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getGroup_2()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1797:1: ( rule__ListParameterValue__Group_2__0 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0>=RULE_INT && LA11_0<=RULE_STRING)||LA11_0==21) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1797:2: rule__ListParameterValue__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__ListParameterValue__Group_2__0_in_rule__ListParameterValue__Group__2__Impl3616);
                    rule__ListParameterValue__Group_2__0();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getGroup_2()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group__2__Impl"


    // $ANTLR start "rule__ListParameterValue__Group__3"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1807:1: rule__ListParameterValue__Group__3 : rule__ListParameterValue__Group__3__Impl ;
    public final void rule__ListParameterValue__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1811:1: ( rule__ListParameterValue__Group__3__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1812:2: rule__ListParameterValue__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__ListParameterValue__Group__3__Impl_in_rule__ListParameterValue__Group__33647);
            rule__ListParameterValue__Group__3__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group__3"


    // $ANTLR start "rule__ListParameterValue__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1818:1: rule__ListParameterValue__Group__3__Impl : ( ']' ) ;
    public final void rule__ListParameterValue__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1822:1: ( ( ']' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1823:1: ( ']' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1823:1: ( ']' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1824:1: ']'
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getRightSquareBracketKeyword_3()); 
            }
            match(input,22,FOLLOW_22_in_rule__ListParameterValue__Group__3__Impl3675); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getRightSquareBracketKeyword_3()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group__3__Impl"


    // $ANTLR start "rule__ListParameterValue__Group_2__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1845:1: rule__ListParameterValue__Group_2__0 : rule__ListParameterValue__Group_2__0__Impl rule__ListParameterValue__Group_2__1 ;
    public final void rule__ListParameterValue__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1849:1: ( rule__ListParameterValue__Group_2__0__Impl rule__ListParameterValue__Group_2__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1850:2: rule__ListParameterValue__Group_2__0__Impl rule__ListParameterValue__Group_2__1
            {
            pushFollow(FOLLOW_rule__ListParameterValue__Group_2__0__Impl_in_rule__ListParameterValue__Group_2__03714);
            rule__ListParameterValue__Group_2__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__ListParameterValue__Group_2__1_in_rule__ListParameterValue__Group_2__03717);
            rule__ListParameterValue__Group_2__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group_2__0"


    // $ANTLR start "rule__ListParameterValue__Group_2__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1857:1: rule__ListParameterValue__Group_2__0__Impl : ( ( rule__ListParameterValue__ElementsAssignment_2_0 ) ) ;
    public final void rule__ListParameterValue__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1861:1: ( ( ( rule__ListParameterValue__ElementsAssignment_2_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1862:1: ( ( rule__ListParameterValue__ElementsAssignment_2_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1862:1: ( ( rule__ListParameterValue__ElementsAssignment_2_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1863:1: ( rule__ListParameterValue__ElementsAssignment_2_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getElementsAssignment_2_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1864:1: ( rule__ListParameterValue__ElementsAssignment_2_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1864:2: rule__ListParameterValue__ElementsAssignment_2_0
            {
            pushFollow(FOLLOW_rule__ListParameterValue__ElementsAssignment_2_0_in_rule__ListParameterValue__Group_2__0__Impl3744);
            rule__ListParameterValue__ElementsAssignment_2_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getElementsAssignment_2_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group_2__0__Impl"


    // $ANTLR start "rule__ListParameterValue__Group_2__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1874:1: rule__ListParameterValue__Group_2__1 : rule__ListParameterValue__Group_2__1__Impl ;
    public final void rule__ListParameterValue__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1878:1: ( rule__ListParameterValue__Group_2__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1879:2: rule__ListParameterValue__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__ListParameterValue__Group_2__1__Impl_in_rule__ListParameterValue__Group_2__13774);
            rule__ListParameterValue__Group_2__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group_2__1"


    // $ANTLR start "rule__ListParameterValue__Group_2__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1885:1: rule__ListParameterValue__Group_2__1__Impl : ( ( rule__ListParameterValue__Group_2_1__0 )* ) ;
    public final void rule__ListParameterValue__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1889:1: ( ( ( rule__ListParameterValue__Group_2_1__0 )* ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1890:1: ( ( rule__ListParameterValue__Group_2_1__0 )* )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1890:1: ( ( rule__ListParameterValue__Group_2_1__0 )* )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1891:1: ( rule__ListParameterValue__Group_2_1__0 )*
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getGroup_2_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1892:1: ( rule__ListParameterValue__Group_2_1__0 )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==17) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1892:2: rule__ListParameterValue__Group_2_1__0
            	    {
            	    pushFollow(FOLLOW_rule__ListParameterValue__Group_2_1__0_in_rule__ListParameterValue__Group_2__1__Impl3801);
            	    rule__ListParameterValue__Group_2_1__0();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getGroup_2_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group_2__1__Impl"


    // $ANTLR start "rule__ListParameterValue__Group_2_1__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1906:1: rule__ListParameterValue__Group_2_1__0 : rule__ListParameterValue__Group_2_1__0__Impl rule__ListParameterValue__Group_2_1__1 ;
    public final void rule__ListParameterValue__Group_2_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1910:1: ( rule__ListParameterValue__Group_2_1__0__Impl rule__ListParameterValue__Group_2_1__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1911:2: rule__ListParameterValue__Group_2_1__0__Impl rule__ListParameterValue__Group_2_1__1
            {
            pushFollow(FOLLOW_rule__ListParameterValue__Group_2_1__0__Impl_in_rule__ListParameterValue__Group_2_1__03836);
            rule__ListParameterValue__Group_2_1__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__ListParameterValue__Group_2_1__1_in_rule__ListParameterValue__Group_2_1__03839);
            rule__ListParameterValue__Group_2_1__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group_2_1__0"


    // $ANTLR start "rule__ListParameterValue__Group_2_1__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1918:1: rule__ListParameterValue__Group_2_1__0__Impl : ( ',' ) ;
    public final void rule__ListParameterValue__Group_2_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1922:1: ( ( ',' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1923:1: ( ',' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1923:1: ( ',' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1924:1: ','
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getCommaKeyword_2_1_0()); 
            }
            match(input,17,FOLLOW_17_in_rule__ListParameterValue__Group_2_1__0__Impl3867); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getCommaKeyword_2_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group_2_1__0__Impl"


    // $ANTLR start "rule__ListParameterValue__Group_2_1__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1937:1: rule__ListParameterValue__Group_2_1__1 : rule__ListParameterValue__Group_2_1__1__Impl ;
    public final void rule__ListParameterValue__Group_2_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1941:1: ( rule__ListParameterValue__Group_2_1__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1942:2: rule__ListParameterValue__Group_2_1__1__Impl
            {
            pushFollow(FOLLOW_rule__ListParameterValue__Group_2_1__1__Impl_in_rule__ListParameterValue__Group_2_1__13898);
            rule__ListParameterValue__Group_2_1__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group_2_1__1"


    // $ANTLR start "rule__ListParameterValue__Group_2_1__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1948:1: rule__ListParameterValue__Group_2_1__1__Impl : ( ( rule__ListParameterValue__ElementsAssignment_2_1_1 ) ) ;
    public final void rule__ListParameterValue__Group_2_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1952:1: ( ( ( rule__ListParameterValue__ElementsAssignment_2_1_1 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1953:1: ( ( rule__ListParameterValue__ElementsAssignment_2_1_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1953:1: ( ( rule__ListParameterValue__ElementsAssignment_2_1_1 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1954:1: ( rule__ListParameterValue__ElementsAssignment_2_1_1 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getElementsAssignment_2_1_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1955:1: ( rule__ListParameterValue__ElementsAssignment_2_1_1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1955:2: rule__ListParameterValue__ElementsAssignment_2_1_1
            {
            pushFollow(FOLLOW_rule__ListParameterValue__ElementsAssignment_2_1_1_in_rule__ListParameterValue__Group_2_1__1__Impl3925);
            rule__ListParameterValue__ElementsAssignment_2_1_1();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getElementsAssignment_2_1_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__Group_2_1__1__Impl"


    // $ANTLR start "rule__MapParameterValue__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1969:1: rule__MapParameterValue__Group__0 : rule__MapParameterValue__Group__0__Impl rule__MapParameterValue__Group__1 ;
    public final void rule__MapParameterValue__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1973:1: ( rule__MapParameterValue__Group__0__Impl rule__MapParameterValue__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1974:2: rule__MapParameterValue__Group__0__Impl rule__MapParameterValue__Group__1
            {
            pushFollow(FOLLOW_rule__MapParameterValue__Group__0__Impl_in_rule__MapParameterValue__Group__03959);
            rule__MapParameterValue__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__MapParameterValue__Group__1_in_rule__MapParameterValue__Group__03962);
            rule__MapParameterValue__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group__0"


    // $ANTLR start "rule__MapParameterValue__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1981:1: rule__MapParameterValue__Group__0__Impl : ( () ) ;
    public final void rule__MapParameterValue__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1985:1: ( ( () ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1986:1: ( () )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1986:1: ( () )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1987:1: ()
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getMapAction_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1988:1: ()
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:1990:1: 
            {
            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getMapAction_0()); 
            }

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group__0__Impl"


    // $ANTLR start "rule__MapParameterValue__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2000:1: rule__MapParameterValue__Group__1 : rule__MapParameterValue__Group__1__Impl rule__MapParameterValue__Group__2 ;
    public final void rule__MapParameterValue__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2004:1: ( rule__MapParameterValue__Group__1__Impl rule__MapParameterValue__Group__2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2005:2: rule__MapParameterValue__Group__1__Impl rule__MapParameterValue__Group__2
            {
            pushFollow(FOLLOW_rule__MapParameterValue__Group__1__Impl_in_rule__MapParameterValue__Group__14020);
            rule__MapParameterValue__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__MapParameterValue__Group__2_in_rule__MapParameterValue__Group__14023);
            rule__MapParameterValue__Group__2();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group__1"


    // $ANTLR start "rule__MapParameterValue__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2012:1: rule__MapParameterValue__Group__1__Impl : ( '[' ) ;
    public final void rule__MapParameterValue__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2016:1: ( ( '[' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2017:1: ( '[' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2017:1: ( '[' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2018:1: '['
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getLeftSquareBracketKeyword_1()); 
            }
            match(input,21,FOLLOW_21_in_rule__MapParameterValue__Group__1__Impl4051); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getLeftSquareBracketKeyword_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group__1__Impl"


    // $ANTLR start "rule__MapParameterValue__Group__2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2031:1: rule__MapParameterValue__Group__2 : rule__MapParameterValue__Group__2__Impl rule__MapParameterValue__Group__3 ;
    public final void rule__MapParameterValue__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2035:1: ( rule__MapParameterValue__Group__2__Impl rule__MapParameterValue__Group__3 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2036:2: rule__MapParameterValue__Group__2__Impl rule__MapParameterValue__Group__3
            {
            pushFollow(FOLLOW_rule__MapParameterValue__Group__2__Impl_in_rule__MapParameterValue__Group__24082);
            rule__MapParameterValue__Group__2__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__MapParameterValue__Group__3_in_rule__MapParameterValue__Group__24085);
            rule__MapParameterValue__Group__3();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group__2"


    // $ANTLR start "rule__MapParameterValue__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2043:1: rule__MapParameterValue__Group__2__Impl : ( ( rule__MapParameterValue__Group_2__0 )? ) ;
    public final void rule__MapParameterValue__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2047:1: ( ( ( rule__MapParameterValue__Group_2__0 )? ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2048:1: ( ( rule__MapParameterValue__Group_2__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2048:1: ( ( rule__MapParameterValue__Group_2__0 )? )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2049:1: ( rule__MapParameterValue__Group_2__0 )?
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getGroup_2()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2050:1: ( rule__MapParameterValue__Group_2__0 )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( ((LA13_0>=RULE_INT && LA13_0<=RULE_STRING)||LA13_0==21) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2050:2: rule__MapParameterValue__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__MapParameterValue__Group_2__0_in_rule__MapParameterValue__Group__2__Impl4112);
                    rule__MapParameterValue__Group_2__0();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getGroup_2()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group__2__Impl"


    // $ANTLR start "rule__MapParameterValue__Group__3"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2060:1: rule__MapParameterValue__Group__3 : rule__MapParameterValue__Group__3__Impl ;
    public final void rule__MapParameterValue__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2064:1: ( rule__MapParameterValue__Group__3__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2065:2: rule__MapParameterValue__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__MapParameterValue__Group__3__Impl_in_rule__MapParameterValue__Group__34143);
            rule__MapParameterValue__Group__3__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group__3"


    // $ANTLR start "rule__MapParameterValue__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2071:1: rule__MapParameterValue__Group__3__Impl : ( ']' ) ;
    public final void rule__MapParameterValue__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2075:1: ( ( ']' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2076:1: ( ']' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2076:1: ( ']' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2077:1: ']'
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getRightSquareBracketKeyword_3()); 
            }
            match(input,22,FOLLOW_22_in_rule__MapParameterValue__Group__3__Impl4171); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getRightSquareBracketKeyword_3()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group__3__Impl"


    // $ANTLR start "rule__MapParameterValue__Group_2__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2098:1: rule__MapParameterValue__Group_2__0 : rule__MapParameterValue__Group_2__0__Impl rule__MapParameterValue__Group_2__1 ;
    public final void rule__MapParameterValue__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2102:1: ( rule__MapParameterValue__Group_2__0__Impl rule__MapParameterValue__Group_2__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2103:2: rule__MapParameterValue__Group_2__0__Impl rule__MapParameterValue__Group_2__1
            {
            pushFollow(FOLLOW_rule__MapParameterValue__Group_2__0__Impl_in_rule__MapParameterValue__Group_2__04210);
            rule__MapParameterValue__Group_2__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__MapParameterValue__Group_2__1_in_rule__MapParameterValue__Group_2__04213);
            rule__MapParameterValue__Group_2__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group_2__0"


    // $ANTLR start "rule__MapParameterValue__Group_2__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2110:1: rule__MapParameterValue__Group_2__0__Impl : ( ( rule__MapParameterValue__ElementsAssignment_2_0 ) ) ;
    public final void rule__MapParameterValue__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2114:1: ( ( ( rule__MapParameterValue__ElementsAssignment_2_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2115:1: ( ( rule__MapParameterValue__ElementsAssignment_2_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2115:1: ( ( rule__MapParameterValue__ElementsAssignment_2_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2116:1: ( rule__MapParameterValue__ElementsAssignment_2_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getElementsAssignment_2_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2117:1: ( rule__MapParameterValue__ElementsAssignment_2_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2117:2: rule__MapParameterValue__ElementsAssignment_2_0
            {
            pushFollow(FOLLOW_rule__MapParameterValue__ElementsAssignment_2_0_in_rule__MapParameterValue__Group_2__0__Impl4240);
            rule__MapParameterValue__ElementsAssignment_2_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getElementsAssignment_2_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group_2__0__Impl"


    // $ANTLR start "rule__MapParameterValue__Group_2__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2127:1: rule__MapParameterValue__Group_2__1 : rule__MapParameterValue__Group_2__1__Impl ;
    public final void rule__MapParameterValue__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2131:1: ( rule__MapParameterValue__Group_2__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2132:2: rule__MapParameterValue__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__MapParameterValue__Group_2__1__Impl_in_rule__MapParameterValue__Group_2__14270);
            rule__MapParameterValue__Group_2__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group_2__1"


    // $ANTLR start "rule__MapParameterValue__Group_2__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2138:1: rule__MapParameterValue__Group_2__1__Impl : ( ( rule__MapParameterValue__Group_2_1__0 )* ) ;
    public final void rule__MapParameterValue__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2142:1: ( ( ( rule__MapParameterValue__Group_2_1__0 )* ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2143:1: ( ( rule__MapParameterValue__Group_2_1__0 )* )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2143:1: ( ( rule__MapParameterValue__Group_2_1__0 )* )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2144:1: ( rule__MapParameterValue__Group_2_1__0 )*
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getGroup_2_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2145:1: ( rule__MapParameterValue__Group_2_1__0 )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==17) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2145:2: rule__MapParameterValue__Group_2_1__0
            	    {
            	    pushFollow(FOLLOW_rule__MapParameterValue__Group_2_1__0_in_rule__MapParameterValue__Group_2__1__Impl4297);
            	    rule__MapParameterValue__Group_2_1__0();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getGroup_2_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group_2__1__Impl"


    // $ANTLR start "rule__MapParameterValue__Group_2_1__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2159:1: rule__MapParameterValue__Group_2_1__0 : rule__MapParameterValue__Group_2_1__0__Impl rule__MapParameterValue__Group_2_1__1 ;
    public final void rule__MapParameterValue__Group_2_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2163:1: ( rule__MapParameterValue__Group_2_1__0__Impl rule__MapParameterValue__Group_2_1__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2164:2: rule__MapParameterValue__Group_2_1__0__Impl rule__MapParameterValue__Group_2_1__1
            {
            pushFollow(FOLLOW_rule__MapParameterValue__Group_2_1__0__Impl_in_rule__MapParameterValue__Group_2_1__04332);
            rule__MapParameterValue__Group_2_1__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__MapParameterValue__Group_2_1__1_in_rule__MapParameterValue__Group_2_1__04335);
            rule__MapParameterValue__Group_2_1__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group_2_1__0"


    // $ANTLR start "rule__MapParameterValue__Group_2_1__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2171:1: rule__MapParameterValue__Group_2_1__0__Impl : ( ',' ) ;
    public final void rule__MapParameterValue__Group_2_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2175:1: ( ( ',' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2176:1: ( ',' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2176:1: ( ',' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2177:1: ','
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getCommaKeyword_2_1_0()); 
            }
            match(input,17,FOLLOW_17_in_rule__MapParameterValue__Group_2_1__0__Impl4363); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getCommaKeyword_2_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group_2_1__0__Impl"


    // $ANTLR start "rule__MapParameterValue__Group_2_1__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2190:1: rule__MapParameterValue__Group_2_1__1 : rule__MapParameterValue__Group_2_1__1__Impl ;
    public final void rule__MapParameterValue__Group_2_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2194:1: ( rule__MapParameterValue__Group_2_1__1__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2195:2: rule__MapParameterValue__Group_2_1__1__Impl
            {
            pushFollow(FOLLOW_rule__MapParameterValue__Group_2_1__1__Impl_in_rule__MapParameterValue__Group_2_1__14394);
            rule__MapParameterValue__Group_2_1__1__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group_2_1__1"


    // $ANTLR start "rule__MapParameterValue__Group_2_1__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2201:1: rule__MapParameterValue__Group_2_1__1__Impl : ( ( rule__MapParameterValue__ElementsAssignment_2_1_1 ) ) ;
    public final void rule__MapParameterValue__Group_2_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2205:1: ( ( ( rule__MapParameterValue__ElementsAssignment_2_1_1 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2206:1: ( ( rule__MapParameterValue__ElementsAssignment_2_1_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2206:1: ( ( rule__MapParameterValue__ElementsAssignment_2_1_1 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2207:1: ( rule__MapParameterValue__ElementsAssignment_2_1_1 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getElementsAssignment_2_1_1()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2208:1: ( rule__MapParameterValue__ElementsAssignment_2_1_1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2208:2: rule__MapParameterValue__ElementsAssignment_2_1_1
            {
            pushFollow(FOLLOW_rule__MapParameterValue__ElementsAssignment_2_1_1_in_rule__MapParameterValue__Group_2_1__1__Impl4421);
            rule__MapParameterValue__ElementsAssignment_2_1_1();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getElementsAssignment_2_1_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__Group_2_1__1__Impl"


    // $ANTLR start "rule__MapEntry__Group__0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2222:1: rule__MapEntry__Group__0 : rule__MapEntry__Group__0__Impl rule__MapEntry__Group__1 ;
    public final void rule__MapEntry__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2226:1: ( rule__MapEntry__Group__0__Impl rule__MapEntry__Group__1 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2227:2: rule__MapEntry__Group__0__Impl rule__MapEntry__Group__1
            {
            pushFollow(FOLLOW_rule__MapEntry__Group__0__Impl_in_rule__MapEntry__Group__04455);
            rule__MapEntry__Group__0__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__MapEntry__Group__1_in_rule__MapEntry__Group__04458);
            rule__MapEntry__Group__1();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapEntry__Group__0"


    // $ANTLR start "rule__MapEntry__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2234:1: rule__MapEntry__Group__0__Impl : ( ( rule__MapEntry__KeyAssignment_0 ) ) ;
    public final void rule__MapEntry__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2238:1: ( ( ( rule__MapEntry__KeyAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2239:1: ( ( rule__MapEntry__KeyAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2239:1: ( ( rule__MapEntry__KeyAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2240:1: ( rule__MapEntry__KeyAssignment_0 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapEntryAccess().getKeyAssignment_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2241:1: ( rule__MapEntry__KeyAssignment_0 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2241:2: rule__MapEntry__KeyAssignment_0
            {
            pushFollow(FOLLOW_rule__MapEntry__KeyAssignment_0_in_rule__MapEntry__Group__0__Impl4485);
            rule__MapEntry__KeyAssignment_0();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapEntryAccess().getKeyAssignment_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapEntry__Group__0__Impl"


    // $ANTLR start "rule__MapEntry__Group__1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2251:1: rule__MapEntry__Group__1 : rule__MapEntry__Group__1__Impl rule__MapEntry__Group__2 ;
    public final void rule__MapEntry__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2255:1: ( rule__MapEntry__Group__1__Impl rule__MapEntry__Group__2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2256:2: rule__MapEntry__Group__1__Impl rule__MapEntry__Group__2
            {
            pushFollow(FOLLOW_rule__MapEntry__Group__1__Impl_in_rule__MapEntry__Group__14515);
            rule__MapEntry__Group__1__Impl();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_rule__MapEntry__Group__2_in_rule__MapEntry__Group__14518);
            rule__MapEntry__Group__2();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapEntry__Group__1"


    // $ANTLR start "rule__MapEntry__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2263:1: rule__MapEntry__Group__1__Impl : ( '=' ) ;
    public final void rule__MapEntry__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2267:1: ( ( '=' ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2268:1: ( '=' )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2268:1: ( '=' )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2269:1: '='
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapEntryAccess().getEqualsSignKeyword_1()); 
            }
            match(input,12,FOLLOW_12_in_rule__MapEntry__Group__1__Impl4546); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapEntryAccess().getEqualsSignKeyword_1()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapEntry__Group__1__Impl"


    // $ANTLR start "rule__MapEntry__Group__2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2282:1: rule__MapEntry__Group__2 : rule__MapEntry__Group__2__Impl ;
    public final void rule__MapEntry__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2286:1: ( rule__MapEntry__Group__2__Impl )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2287:2: rule__MapEntry__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__MapEntry__Group__2__Impl_in_rule__MapEntry__Group__24577);
            rule__MapEntry__Group__2__Impl();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapEntry__Group__2"


    // $ANTLR start "rule__MapEntry__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2293:1: rule__MapEntry__Group__2__Impl : ( ( rule__MapEntry__ValueAssignment_2 ) ) ;
    public final void rule__MapEntry__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2297:1: ( ( ( rule__MapEntry__ValueAssignment_2 ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2298:1: ( ( rule__MapEntry__ValueAssignment_2 ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2298:1: ( ( rule__MapEntry__ValueAssignment_2 ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2299:1: ( rule__MapEntry__ValueAssignment_2 )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapEntryAccess().getValueAssignment_2()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2300:1: ( rule__MapEntry__ValueAssignment_2 )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2300:2: rule__MapEntry__ValueAssignment_2
            {
            pushFollow(FOLLOW_rule__MapEntry__ValueAssignment_2_in_rule__MapEntry__Group__2__Impl4604);
            rule__MapEntry__ValueAssignment_2();

            state._fsp--;
            if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapEntryAccess().getValueAssignment_2()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapEntry__Group__2__Impl"


    // $ANTLR start "rule__PQLModel__QueriesAssignment"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2317:1: rule__PQLModel__QueriesAssignment : ( ruleQuery ) ;
    public final void rule__PQLModel__QueriesAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2321:1: ( ( ruleQuery ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2322:1: ( ruleQuery )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2322:1: ( ruleQuery )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2323:1: ruleQuery
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getPQLModelAccess().getQueriesQueryParserRuleCall_0()); 
            }
            pushFollow(FOLLOW_ruleQuery_in_rule__PQLModel__QueriesAssignment4645);
            ruleQuery();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getPQLModelAccess().getQueriesQueryParserRuleCall_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PQLModel__QueriesAssignment"


    // $ANTLR start "rule__TemporaryStream__NameAssignment_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2332:1: rule__TemporaryStream__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__TemporaryStream__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2336:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2337:1: ( RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2337:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2338:1: RULE_ID
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getTemporaryStreamAccess().getNameIDTerminalRuleCall_0_0()); 
            }
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__TemporaryStream__NameAssignment_04676); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getTemporaryStreamAccess().getNameIDTerminalRuleCall_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TemporaryStream__NameAssignment_0"


    // $ANTLR start "rule__TemporaryStream__OpAssignment_2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2347:1: rule__TemporaryStream__OpAssignment_2 : ( ruleOperator ) ;
    public final void rule__TemporaryStream__OpAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2351:1: ( ( ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2352:1: ( ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2352:1: ( ruleOperator )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2353:1: ruleOperator
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getTemporaryStreamAccess().getOpOperatorParserRuleCall_2_0()); 
            }
            pushFollow(FOLLOW_ruleOperator_in_rule__TemporaryStream__OpAssignment_24707);
            ruleOperator();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getTemporaryStreamAccess().getOpOperatorParserRuleCall_2_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TemporaryStream__OpAssignment_2"


    // $ANTLR start "rule__View__NameAssignment_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2362:1: rule__View__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__View__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2366:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2367:1: ( RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2367:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2368:1: RULE_ID
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getViewAccess().getNameIDTerminalRuleCall_0_0()); 
            }
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__View__NameAssignment_04738); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getViewAccess().getNameIDTerminalRuleCall_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__View__NameAssignment_0"


    // $ANTLR start "rule__View__OpAssignment_2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2377:1: rule__View__OpAssignment_2 : ( ruleOperator ) ;
    public final void rule__View__OpAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2381:1: ( ( ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2382:1: ( ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2382:1: ( ruleOperator )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2383:1: ruleOperator
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getViewAccess().getOpOperatorParserRuleCall_2_0()); 
            }
            pushFollow(FOLLOW_ruleOperator_in_rule__View__OpAssignment_24769);
            ruleOperator();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getViewAccess().getOpOperatorParserRuleCall_2_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__View__OpAssignment_2"


    // $ANTLR start "rule__SharedStream__NameAssignment_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2392:1: rule__SharedStream__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__SharedStream__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2396:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2397:1: ( RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2397:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2398:1: RULE_ID
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getSharedStreamAccess().getNameIDTerminalRuleCall_0_0()); 
            }
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__SharedStream__NameAssignment_04800); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getSharedStreamAccess().getNameIDTerminalRuleCall_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SharedStream__NameAssignment_0"


    // $ANTLR start "rule__SharedStream__OpAssignment_2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2407:1: rule__SharedStream__OpAssignment_2 : ( ruleOperator ) ;
    public final void rule__SharedStream__OpAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2411:1: ( ( ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2412:1: ( ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2412:1: ( ruleOperator )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2413:1: ruleOperator
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getSharedStreamAccess().getOpOperatorParserRuleCall_2_0()); 
            }
            pushFollow(FOLLOW_ruleOperator_in_rule__SharedStream__OpAssignment_24831);
            ruleOperator();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getSharedStreamAccess().getOpOperatorParserRuleCall_2_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SharedStream__OpAssignment_2"


    // $ANTLR start "rule__Operator__OperatorTypeAssignment_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2422:1: rule__Operator__OperatorTypeAssignment_0 : ( RULE_ID ) ;
    public final void rule__Operator__OperatorTypeAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2426:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2427:1: ( RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2427:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2428:1: RULE_ID
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getOperatorTypeIDTerminalRuleCall_0_0()); 
            }
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Operator__OperatorTypeAssignment_04862); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getOperatorTypeIDTerminalRuleCall_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__OperatorTypeAssignment_0"


    // $ANTLR start "rule__Operator__OperatorsAssignment_2_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2437:1: rule__Operator__OperatorsAssignment_2_0 : ( ruleOperatorList ) ;
    public final void rule__Operator__OperatorsAssignment_2_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2441:1: ( ( ruleOperatorList ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2442:1: ( ruleOperatorList )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2442:1: ( ruleOperatorList )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2443:1: ruleOperatorList
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getOperatorsOperatorListParserRuleCall_2_0_0()); 
            }
            pushFollow(FOLLOW_ruleOperatorList_in_rule__Operator__OperatorsAssignment_2_04893);
            ruleOperatorList();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getOperatorsOperatorListParserRuleCall_2_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__OperatorsAssignment_2_0"


    // $ANTLR start "rule__Operator__ParametersAssignment_2_1_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2452:1: rule__Operator__ParametersAssignment_2_1_0 : ( ruleParameterList ) ;
    public final void rule__Operator__ParametersAssignment_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2456:1: ( ( ruleParameterList ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2457:1: ( ruleParameterList )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2457:1: ( ruleParameterList )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2458:1: ruleParameterList
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getParametersParameterListParserRuleCall_2_1_0_0()); 
            }
            pushFollow(FOLLOW_ruleParameterList_in_rule__Operator__ParametersAssignment_2_1_04924);
            ruleParameterList();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getParametersParameterListParserRuleCall_2_1_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__ParametersAssignment_2_1_0"


    // $ANTLR start "rule__Operator__OperatorsAssignment_2_1_1_1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2467:1: rule__Operator__OperatorsAssignment_2_1_1_1 : ( ruleOperatorList ) ;
    public final void rule__Operator__OperatorsAssignment_2_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2471:1: ( ( ruleOperatorList ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2472:1: ( ruleOperatorList )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2472:1: ( ruleOperatorList )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2473:1: ruleOperatorList
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorAccess().getOperatorsOperatorListParserRuleCall_2_1_1_1_0()); 
            }
            pushFollow(FOLLOW_ruleOperatorList_in_rule__Operator__OperatorsAssignment_2_1_1_14955);
            ruleOperatorList();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorAccess().getOperatorsOperatorListParserRuleCall_2_1_1_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__OperatorsAssignment_2_1_1_1"


    // $ANTLR start "rule__OperatorList__ElementsAssignment_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2482:1: rule__OperatorList__ElementsAssignment_0 : ( ruleOperatorOrQuery ) ;
    public final void rule__OperatorList__ElementsAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2486:1: ( ( ruleOperatorOrQuery ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2487:1: ( ruleOperatorOrQuery )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2487:1: ( ruleOperatorOrQuery )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2488:1: ruleOperatorOrQuery
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorListAccess().getElementsOperatorOrQueryParserRuleCall_0_0()); 
            }
            pushFollow(FOLLOW_ruleOperatorOrQuery_in_rule__OperatorList__ElementsAssignment_04986);
            ruleOperatorOrQuery();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorListAccess().getElementsOperatorOrQueryParserRuleCall_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__ElementsAssignment_0"


    // $ANTLR start "rule__OperatorList__ElementsAssignment_1_1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2497:1: rule__OperatorList__ElementsAssignment_1_1 : ( ruleOperatorOrQuery ) ;
    public final void rule__OperatorList__ElementsAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2501:1: ( ( ruleOperatorOrQuery ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2502:1: ( ruleOperatorOrQuery )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2502:1: ( ruleOperatorOrQuery )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2503:1: ruleOperatorOrQuery
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorListAccess().getElementsOperatorOrQueryParserRuleCall_1_1_0()); 
            }
            pushFollow(FOLLOW_ruleOperatorOrQuery_in_rule__OperatorList__ElementsAssignment_1_15017);
            ruleOperatorOrQuery();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorListAccess().getElementsOperatorOrQueryParserRuleCall_1_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorList__ElementsAssignment_1_1"


    // $ANTLR start "rule__OperatorOrQuery__OutputPortAssignment_0_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2512:1: rule__OperatorOrQuery__OutputPortAssignment_0_0 : ( RULE_INT ) ;
    public final void rule__OperatorOrQuery__OutputPortAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2516:1: ( ( RULE_INT ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2517:1: ( RULE_INT )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2517:1: ( RULE_INT )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2518:1: RULE_INT
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryAccess().getOutputPortINTTerminalRuleCall_0_0_0()); 
            }
            match(input,RULE_INT,FOLLOW_RULE_INT_in_rule__OperatorOrQuery__OutputPortAssignment_0_05048); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryAccess().getOutputPortINTTerminalRuleCall_0_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__OutputPortAssignment_0_0"


    // $ANTLR start "rule__OperatorOrQuery__OpAssignment_1_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2527:1: rule__OperatorOrQuery__OpAssignment_1_0 : ( ruleOperator ) ;
    public final void rule__OperatorOrQuery__OpAssignment_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2531:1: ( ( ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2532:1: ( ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2532:1: ( ruleOperator )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2533:1: ruleOperator
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryAccess().getOpOperatorParserRuleCall_1_0_0()); 
            }
            pushFollow(FOLLOW_ruleOperator_in_rule__OperatorOrQuery__OpAssignment_1_05079);
            ruleOperator();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryAccess().getOpOperatorParserRuleCall_1_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__OpAssignment_1_0"


    // $ANTLR start "rule__OperatorOrQuery__QueryAssignment_1_1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2542:1: rule__OperatorOrQuery__QueryAssignment_1_1 : ( ( RULE_ID ) ) ;
    public final void rule__OperatorOrQuery__QueryAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2546:1: ( ( ( RULE_ID ) ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2547:1: ( ( RULE_ID ) )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2547:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2548:1: ( RULE_ID )
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryAccess().getQueryQueryCrossReference_1_1_0()); 
            }
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2549:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2550:1: RULE_ID
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getOperatorOrQueryAccess().getQueryQueryIDTerminalRuleCall_1_1_0_1()); 
            }
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__OperatorOrQuery__QueryAssignment_1_15114); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryAccess().getQueryQueryIDTerminalRuleCall_1_1_0_1()); 
            }

            }

            if ( state.backtracking==0 ) {
               after(grammarAccess.getOperatorOrQueryAccess().getQueryQueryCrossReference_1_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorOrQuery__QueryAssignment_1_1"


    // $ANTLR start "rule__ParameterList__ElementsAssignment_1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2561:1: rule__ParameterList__ElementsAssignment_1 : ( ruleParameter ) ;
    public final void rule__ParameterList__ElementsAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2565:1: ( ( ruleParameter ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2566:1: ( ruleParameter )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2566:1: ( ruleParameter )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2567:1: ruleParameter
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListAccess().getElementsParameterParserRuleCall_1_0()); 
            }
            pushFollow(FOLLOW_ruleParameter_in_rule__ParameterList__ElementsAssignment_15149);
            ruleParameter();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListAccess().getElementsParameterParserRuleCall_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__ElementsAssignment_1"


    // $ANTLR start "rule__ParameterList__ElementsAssignment_2_1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2576:1: rule__ParameterList__ElementsAssignment_2_1 : ( ruleParameter ) ;
    public final void rule__ParameterList__ElementsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2580:1: ( ( ruleParameter ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2581:1: ( ruleParameter )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2581:1: ( ruleParameter )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2582:1: ruleParameter
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterListAccess().getElementsParameterParserRuleCall_2_1_0()); 
            }
            pushFollow(FOLLOW_ruleParameter_in_rule__ParameterList__ElementsAssignment_2_15180);
            ruleParameter();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterListAccess().getElementsParameterParserRuleCall_2_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ParameterList__ElementsAssignment_2_1"


    // $ANTLR start "rule__Parameter__NameAssignment_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2591:1: rule__Parameter__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Parameter__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2595:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2596:1: ( RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2596:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2597:1: RULE_ID
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterAccess().getNameIDTerminalRuleCall_0_0()); 
            }
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Parameter__NameAssignment_05211); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterAccess().getNameIDTerminalRuleCall_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__NameAssignment_0"


    // $ANTLR start "rule__Parameter__ValueAssignment_2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2606:1: rule__Parameter__ValueAssignment_2 : ( ruleParameterValue ) ;
    public final void rule__Parameter__ValueAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2610:1: ( ( ruleParameterValue ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2611:1: ( ruleParameterValue )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2611:1: ( ruleParameterValue )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2612:1: ruleParameterValue
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getParameterAccess().getValueParameterValueParserRuleCall_2_0()); 
            }
            pushFollow(FOLLOW_ruleParameterValue_in_rule__Parameter__ValueAssignment_25242);
            ruleParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getParameterAccess().getValueParameterValueParserRuleCall_2_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__ValueAssignment_2"


    // $ANTLR start "rule__LongParameterValue__ValueAssignment"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2621:1: rule__LongParameterValue__ValueAssignment : ( RULE_INT ) ;
    public final void rule__LongParameterValue__ValueAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2625:1: ( ( RULE_INT ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2626:1: ( RULE_INT )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2626:1: ( RULE_INT )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2627:1: RULE_INT
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getLongParameterValueAccess().getValueINTTerminalRuleCall_0()); 
            }
            match(input,RULE_INT,FOLLOW_RULE_INT_in_rule__LongParameterValue__ValueAssignment5273); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getLongParameterValueAccess().getValueINTTerminalRuleCall_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__LongParameterValue__ValueAssignment"


    // $ANTLR start "rule__DoubleParameterValue__ValueAssignment"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2636:1: rule__DoubleParameterValue__ValueAssignment : ( RULE_DOUBLE ) ;
    public final void rule__DoubleParameterValue__ValueAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2640:1: ( ( RULE_DOUBLE ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2641:1: ( RULE_DOUBLE )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2641:1: ( RULE_DOUBLE )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2642:1: RULE_DOUBLE
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getDoubleParameterValueAccess().getValueDOUBLETerminalRuleCall_0()); 
            }
            match(input,RULE_DOUBLE,FOLLOW_RULE_DOUBLE_in_rule__DoubleParameterValue__ValueAssignment5304); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getDoubleParameterValueAccess().getValueDOUBLETerminalRuleCall_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DoubleParameterValue__ValueAssignment"


    // $ANTLR start "rule__StringParameterValue__ValueAssignment"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2651:1: rule__StringParameterValue__ValueAssignment : ( RULE_STRING ) ;
    public final void rule__StringParameterValue__ValueAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2655:1: ( ( RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2656:1: ( RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2656:1: ( RULE_STRING )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2657:1: RULE_STRING
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getStringParameterValueAccess().getValueSTRINGTerminalRuleCall_0()); 
            }
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__StringParameterValue__ValueAssignment5335); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getStringParameterValueAccess().getValueSTRINGTerminalRuleCall_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__StringParameterValue__ValueAssignment"


    // $ANTLR start "rule__ListParameterValue__ElementsAssignment_2_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2666:1: rule__ListParameterValue__ElementsAssignment_2_0 : ( ruleParameterValue ) ;
    public final void rule__ListParameterValue__ElementsAssignment_2_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2670:1: ( ( ruleParameterValue ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2671:1: ( ruleParameterValue )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2671:1: ( ruleParameterValue )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2672:1: ruleParameterValue
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getElementsParameterValueParserRuleCall_2_0_0()); 
            }
            pushFollow(FOLLOW_ruleParameterValue_in_rule__ListParameterValue__ElementsAssignment_2_05366);
            ruleParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getElementsParameterValueParserRuleCall_2_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__ElementsAssignment_2_0"


    // $ANTLR start "rule__ListParameterValue__ElementsAssignment_2_1_1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2681:1: rule__ListParameterValue__ElementsAssignment_2_1_1 : ( ruleParameterValue ) ;
    public final void rule__ListParameterValue__ElementsAssignment_2_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2685:1: ( ( ruleParameterValue ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2686:1: ( ruleParameterValue )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2686:1: ( ruleParameterValue )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2687:1: ruleParameterValue
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getListParameterValueAccess().getElementsParameterValueParserRuleCall_2_1_1_0()); 
            }
            pushFollow(FOLLOW_ruleParameterValue_in_rule__ListParameterValue__ElementsAssignment_2_1_15397);
            ruleParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getListParameterValueAccess().getElementsParameterValueParserRuleCall_2_1_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ListParameterValue__ElementsAssignment_2_1_1"


    // $ANTLR start "rule__MapParameterValue__ElementsAssignment_2_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2696:1: rule__MapParameterValue__ElementsAssignment_2_0 : ( ruleMapEntry ) ;
    public final void rule__MapParameterValue__ElementsAssignment_2_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2700:1: ( ( ruleMapEntry ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2701:1: ( ruleMapEntry )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2701:1: ( ruleMapEntry )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2702:1: ruleMapEntry
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getElementsMapEntryParserRuleCall_2_0_0()); 
            }
            pushFollow(FOLLOW_ruleMapEntry_in_rule__MapParameterValue__ElementsAssignment_2_05428);
            ruleMapEntry();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getElementsMapEntryParserRuleCall_2_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__ElementsAssignment_2_0"


    // $ANTLR start "rule__MapParameterValue__ElementsAssignment_2_1_1"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2711:1: rule__MapParameterValue__ElementsAssignment_2_1_1 : ( ruleMapEntry ) ;
    public final void rule__MapParameterValue__ElementsAssignment_2_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2715:1: ( ( ruleMapEntry ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2716:1: ( ruleMapEntry )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2716:1: ( ruleMapEntry )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2717:1: ruleMapEntry
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapParameterValueAccess().getElementsMapEntryParserRuleCall_2_1_1_0()); 
            }
            pushFollow(FOLLOW_ruleMapEntry_in_rule__MapParameterValue__ElementsAssignment_2_1_15459);
            ruleMapEntry();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapParameterValueAccess().getElementsMapEntryParserRuleCall_2_1_1_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapParameterValue__ElementsAssignment_2_1_1"


    // $ANTLR start "rule__MapEntry__KeyAssignment_0"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2726:1: rule__MapEntry__KeyAssignment_0 : ( ruleParameterValue ) ;
    public final void rule__MapEntry__KeyAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2730:1: ( ( ruleParameterValue ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2731:1: ( ruleParameterValue )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2731:1: ( ruleParameterValue )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2732:1: ruleParameterValue
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapEntryAccess().getKeyParameterValueParserRuleCall_0_0()); 
            }
            pushFollow(FOLLOW_ruleParameterValue_in_rule__MapEntry__KeyAssignment_05490);
            ruleParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapEntryAccess().getKeyParameterValueParserRuleCall_0_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapEntry__KeyAssignment_0"


    // $ANTLR start "rule__MapEntry__ValueAssignment_2"
    // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2741:1: rule__MapEntry__ValueAssignment_2 : ( ruleParameterValue ) ;
    public final void rule__MapEntry__ValueAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2745:1: ( ( ruleParameterValue ) )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2746:1: ( ruleParameterValue )
            {
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2746:1: ( ruleParameterValue )
            // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:2747:1: ruleParameterValue
            {
            if ( state.backtracking==0 ) {
               before(grammarAccess.getMapEntryAccess().getValueParameterValueParserRuleCall_2_0()); 
            }
            pushFollow(FOLLOW_ruleParameterValue_in_rule__MapEntry__ValueAssignment_25521);
            ruleParameterValue();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               after(grammarAccess.getMapEntryAccess().getValueParameterValueParserRuleCall_2_0()); 
            }

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MapEntry__ValueAssignment_2"

    // $ANTLR start synpred9_InternalPql2
    public final void synpred9_InternalPql2_fragment() throws RecognitionException {   
        // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:632:6: ( ( ( ruleListParameterValue ) ) )
        // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:632:6: ( ( ruleListParameterValue ) )
        {
        // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:632:6: ( ( ruleListParameterValue ) )
        // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:633:1: ( ruleListParameterValue )
        {
        if ( state.backtracking==0 ) {
           before(grammarAccess.getParameterValueAccess().getListParameterValueParserRuleCall_3()); 
        }
        // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:634:1: ( ruleListParameterValue )
        // ../de.uniol.inf.is.odysseus.pql2.ui/src-gen/de/uniol/inf/is/odysseus/ui/contentassist/antlr/internal/InternalPql2.g:634:3: ruleListParameterValue
        {
        pushFollow(FOLLOW_ruleListParameterValue_in_synpred9_InternalPql21317);
        ruleListParameterValue();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }
    }
    // $ANTLR end synpred9_InternalPql2

    // Delegated rules

    public final boolean synpred9_InternalPql2() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_InternalPql2_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_rulePQLModel_in_entryRulePQLModel67 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRulePQLModel74 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__PQLModel__QueriesAssignment_in_rulePQLModel100 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_ruleQuery_in_entryRuleQuery128 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleQuery135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Query__Alternatives_in_ruleQuery161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTemporaryStream_in_entryRuleTemporaryStream188 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTemporaryStream195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TemporaryStream__Group__0_in_ruleTemporaryStream221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleView_in_entryRuleView248 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleView255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__View__Group__0_in_ruleView281 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSharedStream_in_entryRuleSharedStream308 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSharedStream315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SharedStream__Group__0_in_ruleSharedStream341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_entryRuleOperator368 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperator375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group__0_in_ruleOperator401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorList_in_entryRuleOperatorList428 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperatorList435 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorList__Group__0_in_ruleOperatorList461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorOrQuery_in_entryRuleOperatorOrQuery488 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperatorOrQuery495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__Group__0_in_ruleOperatorOrQuery521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterList_in_entryRuleParameterList548 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParameterList555 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterList__Group__0_in_ruleParameterList581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameter_in_entryRuleParameter608 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParameter615 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__Group__0_in_ruleParameter641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterValue_in_entryRuleParameterValue668 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParameterValue675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterValue__Alternatives_in_ruleParameterValue701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLongParameterValue_in_entryRuleLongParameterValue728 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLongParameterValue735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LongParameterValue__ValueAssignment_in_ruleLongParameterValue761 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDoubleParameterValue_in_entryRuleDoubleParameterValue788 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDoubleParameterValue795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DoubleParameterValue__ValueAssignment_in_ruleDoubleParameterValue821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStringParameterValue_in_entryRuleStringParameterValue848 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleStringParameterValue855 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__StringParameterValue__ValueAssignment_in_ruleStringParameterValue881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleListParameterValue_in_entryRuleListParameterValue908 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleListParameterValue915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group__0_in_ruleListParameterValue941 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapParameterValue_in_entryRuleMapParameterValue968 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapParameterValue975 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group__0_in_ruleMapParameterValue1001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapEntry_in_entryRuleMapEntry1028 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapEntry1035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapEntry__Group__0_in_ruleMapEntry1061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTemporaryStream_in_rule__Query__Alternatives1097 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleView_in_rule__Query__Alternatives1114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSharedStream_in_rule__Query__Alternatives1131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__OperatorsAssignment_2_0_in_rule__Operator__Alternatives_21163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group_2_1__0_in_rule__Operator__Alternatives_21181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__OpAssignment_1_0_in_rule__OperatorOrQuery__Alternatives_11214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__QueryAssignment_1_1_in_rule__OperatorOrQuery__Alternatives_11232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLongParameterValue_in_rule__ParameterValue__Alternatives1265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDoubleParameterValue_in_rule__ParameterValue__Alternatives1282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStringParameterValue_in_rule__ParameterValue__Alternatives1299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleListParameterValue_in_rule__ParameterValue__Alternatives1317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapParameterValue_in_rule__ParameterValue__Alternatives1335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TemporaryStream__Group__0__Impl_in_rule__TemporaryStream__Group__01365 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__TemporaryStream__Group__1_in_rule__TemporaryStream__Group__01368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TemporaryStream__NameAssignment_0_in_rule__TemporaryStream__Group__0__Impl1395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TemporaryStream__Group__1__Impl_in_rule__TemporaryStream__Group__11425 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__TemporaryStream__Group__2_in_rule__TemporaryStream__Group__11428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__TemporaryStream__Group__1__Impl1456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TemporaryStream__Group__2__Impl_in_rule__TemporaryStream__Group__21487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TemporaryStream__OpAssignment_2_in_rule__TemporaryStream__Group__2__Impl1514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__View__Group__0__Impl_in_rule__View__Group__01550 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_rule__View__Group__1_in_rule__View__Group__01553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__View__NameAssignment_0_in_rule__View__Group__0__Impl1580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__View__Group__1__Impl_in_rule__View__Group__11610 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__View__Group__2_in_rule__View__Group__11613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__View__Group__1__Impl1641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__View__Group__2__Impl_in_rule__View__Group__21672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__View__OpAssignment_2_in_rule__View__Group__2__Impl1699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SharedStream__Group__0__Impl_in_rule__SharedStream__Group__01735 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_rule__SharedStream__Group__1_in_rule__SharedStream__Group__01738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SharedStream__NameAssignment_0_in_rule__SharedStream__Group__0__Impl1765 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SharedStream__Group__1__Impl_in_rule__SharedStream__Group__11795 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__SharedStream__Group__2_in_rule__SharedStream__Group__11798 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__SharedStream__Group__1__Impl1826 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SharedStream__Group__2__Impl_in_rule__SharedStream__Group__21857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SharedStream__OpAssignment_2_in_rule__SharedStream__Group__2__Impl1884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group__0__Impl_in_rule__Operator__Group__01920 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_rule__Operator__Group__1_in_rule__Operator__Group__01923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__OperatorTypeAssignment_0_in_rule__Operator__Group__0__Impl1950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group__1__Impl_in_rule__Operator__Group__11980 = new BitSet(new long[]{0x0000000000090030L});
    public static final BitSet FOLLOW_rule__Operator__Group__2_in_rule__Operator__Group__11983 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Operator__Group__1__Impl2011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group__2__Impl_in_rule__Operator__Group__22042 = new BitSet(new long[]{0x0000000000090030L});
    public static final BitSet FOLLOW_rule__Operator__Group__3_in_rule__Operator__Group__22045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Alternatives_2_in_rule__Operator__Group__2__Impl2072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group__3__Impl_in_rule__Operator__Group__32103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Operator__Group__3__Impl2131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group_2_1__0__Impl_in_rule__Operator__Group_2_1__02170 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Operator__Group_2_1__1_in_rule__Operator__Group_2_1__02173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__ParametersAssignment_2_1_0_in_rule__Operator__Group_2_1__0__Impl2200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group_2_1__1__Impl_in_rule__Operator__Group_2_1__12230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group_2_1_1__0_in_rule__Operator__Group_2_1__1__Impl2257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group_2_1_1__0__Impl_in_rule__Operator__Group_2_1_1__02292 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Operator__Group_2_1_1__1_in_rule__Operator__Group_2_1_1__02295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Operator__Group_2_1_1__0__Impl2323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Group_2_1_1__1__Impl_in_rule__Operator__Group_2_1_1__12354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__OperatorsAssignment_2_1_1_1_in_rule__Operator__Group_2_1_1__1__Impl2381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorList__Group__0__Impl_in_rule__OperatorList__Group__02415 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__OperatorList__Group__1_in_rule__OperatorList__Group__02418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorList__ElementsAssignment_0_in_rule__OperatorList__Group__0__Impl2445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorList__Group__1__Impl_in_rule__OperatorList__Group__12475 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorList__Group_1__0_in_rule__OperatorList__Group__1__Impl2502 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__OperatorList__Group_1__0__Impl_in_rule__OperatorList__Group_1__02537 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__OperatorList__Group_1__1_in_rule__OperatorList__Group_1__02540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__OperatorList__Group_1__0__Impl2568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorList__Group_1__1__Impl_in_rule__OperatorList__Group_1__12599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorList__ElementsAssignment_1_1_in_rule__OperatorList__Group_1__1__Impl2626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__Group__0__Impl_in_rule__OperatorOrQuery__Group__02660 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__Group__1_in_rule__OperatorOrQuery__Group__02663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__Group_0__0_in_rule__OperatorOrQuery__Group__0__Impl2690 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__Group__1__Impl_in_rule__OperatorOrQuery__Group__12721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__Alternatives_1_in_rule__OperatorOrQuery__Group__1__Impl2748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__Group_0__0__Impl_in_rule__OperatorOrQuery__Group_0__02782 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__Group_0__1_in_rule__OperatorOrQuery__Group_0__02785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__OutputPortAssignment_0_0_in_rule__OperatorOrQuery__Group_0__0__Impl2812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorOrQuery__Group_0__1__Impl_in_rule__OperatorOrQuery__Group_0__12842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__OperatorOrQuery__Group_0__1__Impl2870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterList__Group__0__Impl_in_rule__ParameterList__Group__02905 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ParameterList__Group__1_in_rule__ParameterList__Group__02908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__ParameterList__Group__0__Impl2936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterList__Group__1__Impl_in_rule__ParameterList__Group__12967 = new BitSet(new long[]{0x0000000000120000L});
    public static final BitSet FOLLOW_rule__ParameterList__Group__2_in_rule__ParameterList__Group__12970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterList__ElementsAssignment_1_in_rule__ParameterList__Group__1__Impl2997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterList__Group__2__Impl_in_rule__ParameterList__Group__23027 = new BitSet(new long[]{0x0000000000120000L});
    public static final BitSet FOLLOW_rule__ParameterList__Group__3_in_rule__ParameterList__Group__23030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterList__Group_2__0_in_rule__ParameterList__Group__2__Impl3057 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__ParameterList__Group__3__Impl_in_rule__ParameterList__Group__33088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__ParameterList__Group__3__Impl3116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterList__Group_2__0__Impl_in_rule__ParameterList__Group_2__03155 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__ParameterList__Group_2__1_in_rule__ParameterList__Group_2__03158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__ParameterList__Group_2__0__Impl3186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterList__Group_2__1__Impl_in_rule__ParameterList__Group_2__13217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ParameterList__ElementsAssignment_2_1_in_rule__ParameterList__Group_2__1__Impl3244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__Group__0__Impl_in_rule__Parameter__Group__03278 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Parameter__Group__1_in_rule__Parameter__Group__03281 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__NameAssignment_0_in_rule__Parameter__Group__0__Impl3308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__Group__1__Impl_in_rule__Parameter__Group__13338 = new BitSet(new long[]{0x00000000002000E0L});
    public static final BitSet FOLLOW_rule__Parameter__Group__2_in_rule__Parameter__Group__13341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Parameter__Group__1__Impl3369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__Group__2__Impl_in_rule__Parameter__Group__23400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__ValueAssignment_2_in_rule__Parameter__Group__2__Impl3427 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group__0__Impl_in_rule__ListParameterValue__Group__03463 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group__1_in_rule__ListParameterValue__Group__03466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group__1__Impl_in_rule__ListParameterValue__Group__13524 = new BitSet(new long[]{0x00000000006000E0L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group__2_in_rule__ListParameterValue__Group__13527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__ListParameterValue__Group__1__Impl3555 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group__2__Impl_in_rule__ListParameterValue__Group__23586 = new BitSet(new long[]{0x00000000006000E0L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group__3_in_rule__ListParameterValue__Group__23589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group_2__0_in_rule__ListParameterValue__Group__2__Impl3616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group__3__Impl_in_rule__ListParameterValue__Group__33647 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__ListParameterValue__Group__3__Impl3675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group_2__0__Impl_in_rule__ListParameterValue__Group_2__03714 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group_2__1_in_rule__ListParameterValue__Group_2__03717 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__ElementsAssignment_2_0_in_rule__ListParameterValue__Group_2__0__Impl3744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group_2__1__Impl_in_rule__ListParameterValue__Group_2__13774 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group_2_1__0_in_rule__ListParameterValue__Group_2__1__Impl3801 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group_2_1__0__Impl_in_rule__ListParameterValue__Group_2_1__03836 = new BitSet(new long[]{0x00000000002000E0L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group_2_1__1_in_rule__ListParameterValue__Group_2_1__03839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__ListParameterValue__Group_2_1__0__Impl3867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__Group_2_1__1__Impl_in_rule__ListParameterValue__Group_2_1__13898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ListParameterValue__ElementsAssignment_2_1_1_in_rule__ListParameterValue__Group_2_1__1__Impl3925 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group__0__Impl_in_rule__MapParameterValue__Group__03959 = new BitSet(new long[]{0x00000000002000E0L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group__1_in_rule__MapParameterValue__Group__03962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group__1__Impl_in_rule__MapParameterValue__Group__14020 = new BitSet(new long[]{0x00000000006000E0L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group__2_in_rule__MapParameterValue__Group__14023 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__MapParameterValue__Group__1__Impl4051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group__2__Impl_in_rule__MapParameterValue__Group__24082 = new BitSet(new long[]{0x00000000006000E0L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group__3_in_rule__MapParameterValue__Group__24085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group_2__0_in_rule__MapParameterValue__Group__2__Impl4112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group__3__Impl_in_rule__MapParameterValue__Group__34143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__MapParameterValue__Group__3__Impl4171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group_2__0__Impl_in_rule__MapParameterValue__Group_2__04210 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group_2__1_in_rule__MapParameterValue__Group_2__04213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__ElementsAssignment_2_0_in_rule__MapParameterValue__Group_2__0__Impl4240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group_2__1__Impl_in_rule__MapParameterValue__Group_2__14270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group_2_1__0_in_rule__MapParameterValue__Group_2__1__Impl4297 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group_2_1__0__Impl_in_rule__MapParameterValue__Group_2_1__04332 = new BitSet(new long[]{0x00000000002000E0L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group_2_1__1_in_rule__MapParameterValue__Group_2_1__04335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__MapParameterValue__Group_2_1__0__Impl4363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__Group_2_1__1__Impl_in_rule__MapParameterValue__Group_2_1__14394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapParameterValue__ElementsAssignment_2_1_1_in_rule__MapParameterValue__Group_2_1__1__Impl4421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapEntry__Group__0__Impl_in_rule__MapEntry__Group__04455 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__MapEntry__Group__1_in_rule__MapEntry__Group__04458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapEntry__KeyAssignment_0_in_rule__MapEntry__Group__0__Impl4485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapEntry__Group__1__Impl_in_rule__MapEntry__Group__14515 = new BitSet(new long[]{0x00000000002000E0L});
    public static final BitSet FOLLOW_rule__MapEntry__Group__2_in_rule__MapEntry__Group__14518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__MapEntry__Group__1__Impl4546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapEntry__Group__2__Impl_in_rule__MapEntry__Group__24577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__MapEntry__ValueAssignment_2_in_rule__MapEntry__Group__2__Impl4604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleQuery_in_rule__PQLModel__QueriesAssignment4645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__TemporaryStream__NameAssignment_04676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_rule__TemporaryStream__OpAssignment_24707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__View__NameAssignment_04738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_rule__View__OpAssignment_24769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__SharedStream__NameAssignment_04800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_rule__SharedStream__OpAssignment_24831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Operator__OperatorTypeAssignment_04862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorList_in_rule__Operator__OperatorsAssignment_2_04893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterList_in_rule__Operator__ParametersAssignment_2_1_04924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorList_in_rule__Operator__OperatorsAssignment_2_1_1_14955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorOrQuery_in_rule__OperatorList__ElementsAssignment_04986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorOrQuery_in_rule__OperatorList__ElementsAssignment_1_15017 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_INT_in_rule__OperatorOrQuery__OutputPortAssignment_0_05048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_rule__OperatorOrQuery__OpAssignment_1_05079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__OperatorOrQuery__QueryAssignment_1_15114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameter_in_rule__ParameterList__ElementsAssignment_15149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameter_in_rule__ParameterList__ElementsAssignment_2_15180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Parameter__NameAssignment_05211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterValue_in_rule__Parameter__ValueAssignment_25242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_INT_in_rule__LongParameterValue__ValueAssignment5273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_DOUBLE_in_rule__DoubleParameterValue__ValueAssignment5304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__StringParameterValue__ValueAssignment5335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterValue_in_rule__ListParameterValue__ElementsAssignment_2_05366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterValue_in_rule__ListParameterValue__ElementsAssignment_2_1_15397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapEntry_in_rule__MapParameterValue__ElementsAssignment_2_05428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMapEntry_in_rule__MapParameterValue__ElementsAssignment_2_1_15459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterValue_in_rule__MapEntry__KeyAssignment_05490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameterValue_in_rule__MapEntry__ValueAssignment_25521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleListParameterValue_in_synpred9_InternalPql21317 = new BitSet(new long[]{0x0000000000000002L});

}