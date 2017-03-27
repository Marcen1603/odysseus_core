package de.uniol.inf.is.odysseus.server.ui.contentassist.antlr.internal; 

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
import de.uniol.inf.is.odysseus.server.services.StreamingsparqlGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalStreamingsparqlParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_IRI_TERMINAL", "RULE_AGG_FUNCTION", "RULE_STRING", "RULE_WINDOWTYPE", "RULE_INT", "RULE_UNITTYPE", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'#RUNQUERY'", "'<'", "'>'", "'<='", "'>='", "'='", "'!='", "'+'", "'/'", "'-'", "'*'", "'PREFIX'", "':'", "'BASE'", "'SELECT'", "'AGGREGATE('", "')'", "'aggregations = ['", "']'", "','", "'group_by=['", "'['", "'CSVFILESINK('", "'FILTER('", "'FROM'", "'AS'", "'[TYPE '", "'SIZE '", "'ADVANCE'", "'UNIT '", "'WHERE'", "'{'", "'}'", "'.'", "';'", "'?'", "'#ADDQUERY'", "'DISTINCT'", "'REDUCED'"
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
    public String getGrammarFileName() { return "../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g"; }


     
     	private StreamingsparqlGrammarAccess grammarAccess;
     	
        public void setGrammarAccess(StreamingsparqlGrammarAccess grammarAccess) {
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




    // $ANTLR start "entryRuleSPARQLQuery"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:60:1: entryRuleSPARQLQuery : ruleSPARQLQuery EOF ;
    public final void entryRuleSPARQLQuery() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:61:1: ( ruleSPARQLQuery EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:62:1: ruleSPARQLQuery EOF
            {
             before(grammarAccess.getSPARQLQueryRule()); 
            pushFollow(FOLLOW_ruleSPARQLQuery_in_entryRuleSPARQLQuery61);
            ruleSPARQLQuery();

            state._fsp--;

             after(grammarAccess.getSPARQLQueryRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSPARQLQuery68); 

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
    // $ANTLR end "entryRuleSPARQLQuery"


    // $ANTLR start "ruleSPARQLQuery"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:69:1: ruleSPARQLQuery : ( ruleSelectQuery ) ;
    public final void ruleSPARQLQuery() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:73:2: ( ( ruleSelectQuery ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:74:1: ( ruleSelectQuery )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:74:1: ( ruleSelectQuery )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:75:1: ruleSelectQuery
            {
             before(grammarAccess.getSPARQLQueryAccess().getSelectQueryParserRuleCall()); 
            pushFollow(FOLLOW_ruleSelectQuery_in_ruleSPARQLQuery94);
            ruleSelectQuery();

            state._fsp--;

             after(grammarAccess.getSPARQLQueryAccess().getSelectQueryParserRuleCall()); 

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
    // $ANTLR end "ruleSPARQLQuery"


    // $ANTLR start "entryRulePrefix"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:88:1: entryRulePrefix : rulePrefix EOF ;
    public final void entryRulePrefix() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:89:1: ( rulePrefix EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:90:1: rulePrefix EOF
            {
             before(grammarAccess.getPrefixRule()); 
            pushFollow(FOLLOW_rulePrefix_in_entryRulePrefix120);
            rulePrefix();

            state._fsp--;

             after(grammarAccess.getPrefixRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRulePrefix127); 

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
    // $ANTLR end "entryRulePrefix"


    // $ANTLR start "rulePrefix"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:97:1: rulePrefix : ( ( rule__Prefix__Alternatives ) ) ;
    public final void rulePrefix() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:101:2: ( ( ( rule__Prefix__Alternatives ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:102:1: ( ( rule__Prefix__Alternatives ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:102:1: ( ( rule__Prefix__Alternatives ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:103:1: ( rule__Prefix__Alternatives )
            {
             before(grammarAccess.getPrefixAccess().getAlternatives()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:104:1: ( rule__Prefix__Alternatives )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:104:2: rule__Prefix__Alternatives
            {
            pushFollow(FOLLOW_rule__Prefix__Alternatives_in_rulePrefix153);
            rule__Prefix__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getPrefixAccess().getAlternatives()); 

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
    // $ANTLR end "rulePrefix"


    // $ANTLR start "entryRuleUnNamedPrefix"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:116:1: entryRuleUnNamedPrefix : ruleUnNamedPrefix EOF ;
    public final void entryRuleUnNamedPrefix() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:117:1: ( ruleUnNamedPrefix EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:118:1: ruleUnNamedPrefix EOF
            {
             before(grammarAccess.getUnNamedPrefixRule()); 
            pushFollow(FOLLOW_ruleUnNamedPrefix_in_entryRuleUnNamedPrefix180);
            ruleUnNamedPrefix();

            state._fsp--;

             after(grammarAccess.getUnNamedPrefixRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleUnNamedPrefix187); 

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
    // $ANTLR end "entryRuleUnNamedPrefix"


    // $ANTLR start "ruleUnNamedPrefix"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:125:1: ruleUnNamedPrefix : ( ( rule__UnNamedPrefix__Group__0 ) ) ;
    public final void ruleUnNamedPrefix() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:129:2: ( ( ( rule__UnNamedPrefix__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:130:1: ( ( rule__UnNamedPrefix__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:130:1: ( ( rule__UnNamedPrefix__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:131:1: ( rule__UnNamedPrefix__Group__0 )
            {
             before(grammarAccess.getUnNamedPrefixAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:132:1: ( rule__UnNamedPrefix__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:132:2: rule__UnNamedPrefix__Group__0
            {
            pushFollow(FOLLOW_rule__UnNamedPrefix__Group__0_in_ruleUnNamedPrefix213);
            rule__UnNamedPrefix__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getUnNamedPrefixAccess().getGroup()); 

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
    // $ANTLR end "ruleUnNamedPrefix"


    // $ANTLR start "entryRuleBase"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:144:1: entryRuleBase : ruleBase EOF ;
    public final void entryRuleBase() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:145:1: ( ruleBase EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:146:1: ruleBase EOF
            {
             before(grammarAccess.getBaseRule()); 
            pushFollow(FOLLOW_ruleBase_in_entryRuleBase240);
            ruleBase();

            state._fsp--;

             after(grammarAccess.getBaseRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBase247); 

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
    // $ANTLR end "entryRuleBase"


    // $ANTLR start "ruleBase"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:153:1: ruleBase : ( ( rule__Base__Group__0 ) ) ;
    public final void ruleBase() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:157:2: ( ( ( rule__Base__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:158:1: ( ( rule__Base__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:158:1: ( ( rule__Base__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:159:1: ( rule__Base__Group__0 )
            {
             before(grammarAccess.getBaseAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:160:1: ( rule__Base__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:160:2: rule__Base__Group__0
            {
            pushFollow(FOLLOW_rule__Base__Group__0_in_ruleBase273);
            rule__Base__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getBaseAccess().getGroup()); 

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
    // $ANTLR end "ruleBase"


    // $ANTLR start "entryRuleSelectQuery"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:172:1: entryRuleSelectQuery : ruleSelectQuery EOF ;
    public final void entryRuleSelectQuery() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:173:1: ( ruleSelectQuery EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:174:1: ruleSelectQuery EOF
            {
             before(grammarAccess.getSelectQueryRule()); 
            pushFollow(FOLLOW_ruleSelectQuery_in_entryRuleSelectQuery300);
            ruleSelectQuery();

            state._fsp--;

             after(grammarAccess.getSelectQueryRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSelectQuery307); 

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
    // $ANTLR end "entryRuleSelectQuery"


    // $ANTLR start "ruleSelectQuery"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:181:1: ruleSelectQuery : ( ( rule__SelectQuery__Group__0 ) ) ;
    public final void ruleSelectQuery() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:185:2: ( ( ( rule__SelectQuery__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:186:1: ( ( rule__SelectQuery__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:186:1: ( ( rule__SelectQuery__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:187:1: ( rule__SelectQuery__Group__0 )
            {
             before(grammarAccess.getSelectQueryAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:188:1: ( rule__SelectQuery__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:188:2: rule__SelectQuery__Group__0
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__0_in_ruleSelectQuery333);
            rule__SelectQuery__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getSelectQueryAccess().getGroup()); 

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
    // $ANTLR end "ruleSelectQuery"


    // $ANTLR start "entryRuleAggregate"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:200:1: entryRuleAggregate : ruleAggregate EOF ;
    public final void entryRuleAggregate() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:201:1: ( ruleAggregate EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:202:1: ruleAggregate EOF
            {
             before(grammarAccess.getAggregateRule()); 
            pushFollow(FOLLOW_ruleAggregate_in_entryRuleAggregate360);
            ruleAggregate();

            state._fsp--;

             after(grammarAccess.getAggregateRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAggregate367); 

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
    // $ANTLR end "entryRuleAggregate"


    // $ANTLR start "ruleAggregate"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:209:1: ruleAggregate : ( ( rule__Aggregate__Group__0 ) ) ;
    public final void ruleAggregate() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:213:2: ( ( ( rule__Aggregate__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:214:1: ( ( rule__Aggregate__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:214:1: ( ( rule__Aggregate__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:215:1: ( rule__Aggregate__Group__0 )
            {
             before(grammarAccess.getAggregateAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:216:1: ( rule__Aggregate__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:216:2: rule__Aggregate__Group__0
            {
            pushFollow(FOLLOW_rule__Aggregate__Group__0_in_ruleAggregate393);
            rule__Aggregate__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getAggregateAccess().getGroup()); 

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
    // $ANTLR end "ruleAggregate"


    // $ANTLR start "entryRuleGroupBy"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:228:1: entryRuleGroupBy : ruleGroupBy EOF ;
    public final void entryRuleGroupBy() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:229:1: ( ruleGroupBy EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:230:1: ruleGroupBy EOF
            {
             before(grammarAccess.getGroupByRule()); 
            pushFollow(FOLLOW_ruleGroupBy_in_entryRuleGroupBy420);
            ruleGroupBy();

            state._fsp--;

             after(grammarAccess.getGroupByRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroupBy427); 

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
    // $ANTLR end "entryRuleGroupBy"


    // $ANTLR start "ruleGroupBy"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:237:1: ruleGroupBy : ( ( rule__GroupBy__Group__0 ) ) ;
    public final void ruleGroupBy() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:241:2: ( ( ( rule__GroupBy__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:242:1: ( ( rule__GroupBy__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:242:1: ( ( rule__GroupBy__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:243:1: ( rule__GroupBy__Group__0 )
            {
             before(grammarAccess.getGroupByAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:244:1: ( rule__GroupBy__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:244:2: rule__GroupBy__Group__0
            {
            pushFollow(FOLLOW_rule__GroupBy__Group__0_in_ruleGroupBy453);
            rule__GroupBy__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getGroupByAccess().getGroup()); 

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
    // $ANTLR end "ruleGroupBy"


    // $ANTLR start "entryRuleAggregation"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:256:1: entryRuleAggregation : ruleAggregation EOF ;
    public final void entryRuleAggregation() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:257:1: ( ruleAggregation EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:258:1: ruleAggregation EOF
            {
             before(grammarAccess.getAggregationRule()); 
            pushFollow(FOLLOW_ruleAggregation_in_entryRuleAggregation480);
            ruleAggregation();

            state._fsp--;

             after(grammarAccess.getAggregationRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAggregation487); 

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
    // $ANTLR end "entryRuleAggregation"


    // $ANTLR start "ruleAggregation"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:265:1: ruleAggregation : ( ( rule__Aggregation__Group__0 ) ) ;
    public final void ruleAggregation() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:269:2: ( ( ( rule__Aggregation__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:270:1: ( ( rule__Aggregation__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:270:1: ( ( rule__Aggregation__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:271:1: ( rule__Aggregation__Group__0 )
            {
             before(grammarAccess.getAggregationAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:272:1: ( rule__Aggregation__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:272:2: rule__Aggregation__Group__0
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__0_in_ruleAggregation513);
            rule__Aggregation__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getAggregationAccess().getGroup()); 

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
    // $ANTLR end "ruleAggregation"


    // $ANTLR start "entryRuleFilesinkclause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:284:1: entryRuleFilesinkclause : ruleFilesinkclause EOF ;
    public final void entryRuleFilesinkclause() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:285:1: ( ruleFilesinkclause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:286:1: ruleFilesinkclause EOF
            {
             before(grammarAccess.getFilesinkclauseRule()); 
            pushFollow(FOLLOW_ruleFilesinkclause_in_entryRuleFilesinkclause540);
            ruleFilesinkclause();

            state._fsp--;

             after(grammarAccess.getFilesinkclauseRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFilesinkclause547); 

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
    // $ANTLR end "entryRuleFilesinkclause"


    // $ANTLR start "ruleFilesinkclause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:293:1: ruleFilesinkclause : ( ( rule__Filesinkclause__Group__0 ) ) ;
    public final void ruleFilesinkclause() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:297:2: ( ( ( rule__Filesinkclause__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:298:1: ( ( rule__Filesinkclause__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:298:1: ( ( rule__Filesinkclause__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:299:1: ( rule__Filesinkclause__Group__0 )
            {
             before(grammarAccess.getFilesinkclauseAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:300:1: ( rule__Filesinkclause__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:300:2: rule__Filesinkclause__Group__0
            {
            pushFollow(FOLLOW_rule__Filesinkclause__Group__0_in_ruleFilesinkclause573);
            rule__Filesinkclause__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getFilesinkclauseAccess().getGroup()); 

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
    // $ANTLR end "ruleFilesinkclause"


    // $ANTLR start "entryRuleFilterclause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:312:1: entryRuleFilterclause : ruleFilterclause EOF ;
    public final void entryRuleFilterclause() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:313:1: ( ruleFilterclause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:314:1: ruleFilterclause EOF
            {
             before(grammarAccess.getFilterclauseRule()); 
            pushFollow(FOLLOW_ruleFilterclause_in_entryRuleFilterclause600);
            ruleFilterclause();

            state._fsp--;

             after(grammarAccess.getFilterclauseRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFilterclause607); 

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
    // $ANTLR end "entryRuleFilterclause"


    // $ANTLR start "ruleFilterclause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:321:1: ruleFilterclause : ( ( rule__Filterclause__Group__0 ) ) ;
    public final void ruleFilterclause() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:325:2: ( ( ( rule__Filterclause__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:326:1: ( ( rule__Filterclause__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:326:1: ( ( rule__Filterclause__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:327:1: ( rule__Filterclause__Group__0 )
            {
             before(grammarAccess.getFilterclauseAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:328:1: ( rule__Filterclause__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:328:2: rule__Filterclause__Group__0
            {
            pushFollow(FOLLOW_rule__Filterclause__Group__0_in_ruleFilterclause633);
            rule__Filterclause__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getFilterclauseAccess().getGroup()); 

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
    // $ANTLR end "ruleFilterclause"


    // $ANTLR start "entryRuleDatasetClause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:342:1: entryRuleDatasetClause : ruleDatasetClause EOF ;
    public final void entryRuleDatasetClause() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:343:1: ( ruleDatasetClause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:344:1: ruleDatasetClause EOF
            {
             before(grammarAccess.getDatasetClauseRule()); 
            pushFollow(FOLLOW_ruleDatasetClause_in_entryRuleDatasetClause662);
            ruleDatasetClause();

            state._fsp--;

             after(grammarAccess.getDatasetClauseRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDatasetClause669); 

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
    // $ANTLR end "entryRuleDatasetClause"


    // $ANTLR start "ruleDatasetClause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:351:1: ruleDatasetClause : ( ( rule__DatasetClause__Group__0 ) ) ;
    public final void ruleDatasetClause() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:355:2: ( ( ( rule__DatasetClause__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:356:1: ( ( rule__DatasetClause__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:356:1: ( ( rule__DatasetClause__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:357:1: ( rule__DatasetClause__Group__0 )
            {
             before(grammarAccess.getDatasetClauseAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:358:1: ( rule__DatasetClause__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:358:2: rule__DatasetClause__Group__0
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group__0_in_ruleDatasetClause695);
            rule__DatasetClause__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getGroup()); 

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
    // $ANTLR end "ruleDatasetClause"


    // $ANTLR start "entryRuleWhereClause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:370:1: entryRuleWhereClause : ruleWhereClause EOF ;
    public final void entryRuleWhereClause() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:371:1: ( ruleWhereClause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:372:1: ruleWhereClause EOF
            {
             before(grammarAccess.getWhereClauseRule()); 
            pushFollow(FOLLOW_ruleWhereClause_in_entryRuleWhereClause722);
            ruleWhereClause();

            state._fsp--;

             after(grammarAccess.getWhereClauseRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleWhereClause729); 

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
    // $ANTLR end "entryRuleWhereClause"


    // $ANTLR start "ruleWhereClause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:379:1: ruleWhereClause : ( ( rule__WhereClause__Group__0 ) ) ;
    public final void ruleWhereClause() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:383:2: ( ( ( rule__WhereClause__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:384:1: ( ( rule__WhereClause__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:384:1: ( ( rule__WhereClause__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:385:1: ( rule__WhereClause__Group__0 )
            {
             before(grammarAccess.getWhereClauseAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:386:1: ( rule__WhereClause__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:386:2: rule__WhereClause__Group__0
            {
            pushFollow(FOLLOW_rule__WhereClause__Group__0_in_ruleWhereClause755);
            rule__WhereClause__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getWhereClauseAccess().getGroup()); 

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
    // $ANTLR end "ruleWhereClause"


    // $ANTLR start "entryRuleInnerWhereClause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:398:1: entryRuleInnerWhereClause : ruleInnerWhereClause EOF ;
    public final void entryRuleInnerWhereClause() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:399:1: ( ruleInnerWhereClause EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:400:1: ruleInnerWhereClause EOF
            {
             before(grammarAccess.getInnerWhereClauseRule()); 
            pushFollow(FOLLOW_ruleInnerWhereClause_in_entryRuleInnerWhereClause782);
            ruleInnerWhereClause();

            state._fsp--;

             after(grammarAccess.getInnerWhereClauseRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleInnerWhereClause789); 

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
    // $ANTLR end "entryRuleInnerWhereClause"


    // $ANTLR start "ruleInnerWhereClause"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:407:1: ruleInnerWhereClause : ( ( rule__InnerWhereClause__Group__0 ) ) ;
    public final void ruleInnerWhereClause() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:411:2: ( ( ( rule__InnerWhereClause__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:412:1: ( ( rule__InnerWhereClause__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:412:1: ( ( rule__InnerWhereClause__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:413:1: ( rule__InnerWhereClause__Group__0 )
            {
             before(grammarAccess.getInnerWhereClauseAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:414:1: ( rule__InnerWhereClause__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:414:2: rule__InnerWhereClause__Group__0
            {
            pushFollow(FOLLOW_rule__InnerWhereClause__Group__0_in_ruleInnerWhereClause815);
            rule__InnerWhereClause__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getInnerWhereClauseAccess().getGroup()); 

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
    // $ANTLR end "ruleInnerWhereClause"


    // $ANTLR start "entryRuleGroupGraphPatternSub"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:426:1: entryRuleGroupGraphPatternSub : ruleGroupGraphPatternSub EOF ;
    public final void entryRuleGroupGraphPatternSub() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:427:1: ( ruleGroupGraphPatternSub EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:428:1: ruleGroupGraphPatternSub EOF
            {
             before(grammarAccess.getGroupGraphPatternSubRule()); 
            pushFollow(FOLLOW_ruleGroupGraphPatternSub_in_entryRuleGroupGraphPatternSub842);
            ruleGroupGraphPatternSub();

            state._fsp--;

             after(grammarAccess.getGroupGraphPatternSubRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGroupGraphPatternSub849); 

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
    // $ANTLR end "entryRuleGroupGraphPatternSub"


    // $ANTLR start "ruleGroupGraphPatternSub"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:435:1: ruleGroupGraphPatternSub : ( ( rule__GroupGraphPatternSub__Group__0 ) ) ;
    public final void ruleGroupGraphPatternSub() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:439:2: ( ( ( rule__GroupGraphPatternSub__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:440:1: ( ( rule__GroupGraphPatternSub__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:440:1: ( ( rule__GroupGraphPatternSub__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:441:1: ( rule__GroupGraphPatternSub__Group__0 )
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:442:1: ( rule__GroupGraphPatternSub__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:442:2: rule__GroupGraphPatternSub__Group__0
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__0_in_ruleGroupGraphPatternSub875);
            rule__GroupGraphPatternSub__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getGroupGraphPatternSubAccess().getGroup()); 

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
    // $ANTLR end "ruleGroupGraphPatternSub"


    // $ANTLR start "entryRuleTriplesSameSubject"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:454:1: entryRuleTriplesSameSubject : ruleTriplesSameSubject EOF ;
    public final void entryRuleTriplesSameSubject() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:455:1: ( ruleTriplesSameSubject EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:456:1: ruleTriplesSameSubject EOF
            {
             before(grammarAccess.getTriplesSameSubjectRule()); 
            pushFollow(FOLLOW_ruleTriplesSameSubject_in_entryRuleTriplesSameSubject902);
            ruleTriplesSameSubject();

            state._fsp--;

             after(grammarAccess.getTriplesSameSubjectRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTriplesSameSubject909); 

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
    // $ANTLR end "entryRuleTriplesSameSubject"


    // $ANTLR start "ruleTriplesSameSubject"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:463:1: ruleTriplesSameSubject : ( ( rule__TriplesSameSubject__Group__0 ) ) ;
    public final void ruleTriplesSameSubject() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:467:2: ( ( ( rule__TriplesSameSubject__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:468:1: ( ( rule__TriplesSameSubject__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:468:1: ( ( rule__TriplesSameSubject__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:469:1: ( rule__TriplesSameSubject__Group__0 )
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:470:1: ( rule__TriplesSameSubject__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:470:2: rule__TriplesSameSubject__Group__0
            {
            pushFollow(FOLLOW_rule__TriplesSameSubject__Group__0_in_ruleTriplesSameSubject935);
            rule__TriplesSameSubject__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getTriplesSameSubjectAccess().getGroup()); 

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
    // $ANTLR end "ruleTriplesSameSubject"


    // $ANTLR start "entryRulePropertyList"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:482:1: entryRulePropertyList : rulePropertyList EOF ;
    public final void entryRulePropertyList() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:483:1: ( rulePropertyList EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:484:1: rulePropertyList EOF
            {
             before(grammarAccess.getPropertyListRule()); 
            pushFollow(FOLLOW_rulePropertyList_in_entryRulePropertyList962);
            rulePropertyList();

            state._fsp--;

             after(grammarAccess.getPropertyListRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRulePropertyList969); 

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
    // $ANTLR end "entryRulePropertyList"


    // $ANTLR start "rulePropertyList"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:491:1: rulePropertyList : ( ( rule__PropertyList__Group__0 ) ) ;
    public final void rulePropertyList() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:495:2: ( ( ( rule__PropertyList__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:496:1: ( ( rule__PropertyList__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:496:1: ( ( rule__PropertyList__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:497:1: ( rule__PropertyList__Group__0 )
            {
             before(grammarAccess.getPropertyListAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:498:1: ( rule__PropertyList__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:498:2: rule__PropertyList__Group__0
            {
            pushFollow(FOLLOW_rule__PropertyList__Group__0_in_rulePropertyList995);
            rule__PropertyList__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getPropertyListAccess().getGroup()); 

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
    // $ANTLR end "rulePropertyList"


    // $ANTLR start "entryRuleGraphNode"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:510:1: entryRuleGraphNode : ruleGraphNode EOF ;
    public final void entryRuleGraphNode() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:511:1: ( ruleGraphNode EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:512:1: ruleGraphNode EOF
            {
             before(grammarAccess.getGraphNodeRule()); 
            pushFollow(FOLLOW_ruleGraphNode_in_entryRuleGraphNode1022);
            ruleGraphNode();

            state._fsp--;

             after(grammarAccess.getGraphNodeRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleGraphNode1029); 

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
    // $ANTLR end "entryRuleGraphNode"


    // $ANTLR start "ruleGraphNode"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:519:1: ruleGraphNode : ( ( rule__GraphNode__Alternatives ) ) ;
    public final void ruleGraphNode() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:523:2: ( ( ( rule__GraphNode__Alternatives ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:524:1: ( ( rule__GraphNode__Alternatives ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:524:1: ( ( rule__GraphNode__Alternatives ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:525:1: ( rule__GraphNode__Alternatives )
            {
             before(grammarAccess.getGraphNodeAccess().getAlternatives()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:526:1: ( rule__GraphNode__Alternatives )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:526:2: rule__GraphNode__Alternatives
            {
            pushFollow(FOLLOW_rule__GraphNode__Alternatives_in_ruleGraphNode1055);
            rule__GraphNode__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getGraphNodeAccess().getAlternatives()); 

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
    // $ANTLR end "ruleGraphNode"


    // $ANTLR start "entryRuleVariable"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:538:1: entryRuleVariable : ruleVariable EOF ;
    public final void entryRuleVariable() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:539:1: ( ruleVariable EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:540:1: ruleVariable EOF
            {
             before(grammarAccess.getVariableRule()); 
            pushFollow(FOLLOW_ruleVariable_in_entryRuleVariable1082);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getVariableRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleVariable1089); 

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
    // $ANTLR end "entryRuleVariable"


    // $ANTLR start "ruleVariable"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:547:1: ruleVariable : ( ( rule__Variable__Alternatives ) ) ;
    public final void ruleVariable() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:551:2: ( ( ( rule__Variable__Alternatives ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:552:1: ( ( rule__Variable__Alternatives ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:552:1: ( ( rule__Variable__Alternatives ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:553:1: ( rule__Variable__Alternatives )
            {
             before(grammarAccess.getVariableAccess().getAlternatives()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:554:1: ( rule__Variable__Alternatives )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:554:2: rule__Variable__Alternatives
            {
            pushFollow(FOLLOW_rule__Variable__Alternatives_in_ruleVariable1115);
            rule__Variable__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getVariableAccess().getAlternatives()); 

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
    // $ANTLR end "ruleVariable"


    // $ANTLR start "entryRuleUnNamedVariable"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:566:1: entryRuleUnNamedVariable : ruleUnNamedVariable EOF ;
    public final void entryRuleUnNamedVariable() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:567:1: ( ruleUnNamedVariable EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:568:1: ruleUnNamedVariable EOF
            {
             before(grammarAccess.getUnNamedVariableRule()); 
            pushFollow(FOLLOW_ruleUnNamedVariable_in_entryRuleUnNamedVariable1142);
            ruleUnNamedVariable();

            state._fsp--;

             after(grammarAccess.getUnNamedVariableRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleUnNamedVariable1149); 

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
    // $ANTLR end "entryRuleUnNamedVariable"


    // $ANTLR start "ruleUnNamedVariable"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:575:1: ruleUnNamedVariable : ( ( rule__UnNamedVariable__Group__0 ) ) ;
    public final void ruleUnNamedVariable() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:579:2: ( ( ( rule__UnNamedVariable__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:580:1: ( ( rule__UnNamedVariable__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:580:1: ( ( rule__UnNamedVariable__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:581:1: ( rule__UnNamedVariable__Group__0 )
            {
             before(grammarAccess.getUnNamedVariableAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:582:1: ( rule__UnNamedVariable__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:582:2: rule__UnNamedVariable__Group__0
            {
            pushFollow(FOLLOW_rule__UnNamedVariable__Group__0_in_ruleUnNamedVariable1175);
            rule__UnNamedVariable__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getUnNamedVariableAccess().getGroup()); 

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
    // $ANTLR end "ruleUnNamedVariable"


    // $ANTLR start "entryRuleProperty"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:594:1: entryRuleProperty : ruleProperty EOF ;
    public final void entryRuleProperty() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:595:1: ( ruleProperty EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:596:1: ruleProperty EOF
            {
             before(grammarAccess.getPropertyRule()); 
            pushFollow(FOLLOW_ruleProperty_in_entryRuleProperty1202);
            ruleProperty();

            state._fsp--;

             after(grammarAccess.getPropertyRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleProperty1209); 

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
    // $ANTLR end "entryRuleProperty"


    // $ANTLR start "ruleProperty"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:603:1: ruleProperty : ( ( rule__Property__Group__0 ) ) ;
    public final void ruleProperty() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:607:2: ( ( ( rule__Property__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:608:1: ( ( rule__Property__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:608:1: ( ( rule__Property__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:609:1: ( rule__Property__Group__0 )
            {
             before(grammarAccess.getPropertyAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:610:1: ( rule__Property__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:610:2: rule__Property__Group__0
            {
            pushFollow(FOLLOW_rule__Property__Group__0_in_ruleProperty1235);
            rule__Property__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getPropertyAccess().getGroup()); 

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
    // $ANTLR end "ruleProperty"


    // $ANTLR start "entryRuleIRI"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:622:1: entryRuleIRI : ruleIRI EOF ;
    public final void entryRuleIRI() throws RecognitionException {
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:623:1: ( ruleIRI EOF )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:624:1: ruleIRI EOF
            {
             before(grammarAccess.getIRIRule()); 
            pushFollow(FOLLOW_ruleIRI_in_entryRuleIRI1262);
            ruleIRI();

            state._fsp--;

             after(grammarAccess.getIRIRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleIRI1269); 

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
    // $ANTLR end "entryRuleIRI"


    // $ANTLR start "ruleIRI"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:631:1: ruleIRI : ( ( rule__IRI__Group__0 ) ) ;
    public final void ruleIRI() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:635:2: ( ( ( rule__IRI__Group__0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:636:1: ( ( rule__IRI__Group__0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:636:1: ( ( rule__IRI__Group__0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:637:1: ( rule__IRI__Group__0 )
            {
             before(grammarAccess.getIRIAccess().getGroup()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:638:1: ( rule__IRI__Group__0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:638:2: rule__IRI__Group__0
            {
            pushFollow(FOLLOW_rule__IRI__Group__0_in_ruleIRI1295);
            rule__IRI__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getIRIAccess().getGroup()); 

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
    // $ANTLR end "ruleIRI"


    // $ANTLR start "ruleOperator"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:655:1: ruleOperator : ( ( rule__Operator__Alternatives ) ) ;
    public final void ruleOperator() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:659:1: ( ( ( rule__Operator__Alternatives ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:660:1: ( ( rule__Operator__Alternatives ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:660:1: ( ( rule__Operator__Alternatives ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:661:1: ( rule__Operator__Alternatives )
            {
             before(grammarAccess.getOperatorAccess().getAlternatives()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:662:1: ( rule__Operator__Alternatives )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:662:2: rule__Operator__Alternatives
            {
            pushFollow(FOLLOW_rule__Operator__Alternatives_in_ruleOperator1336);
            rule__Operator__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getOperatorAccess().getAlternatives()); 

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


    // $ANTLR start "rule__Prefix__Alternatives"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:673:1: rule__Prefix__Alternatives : ( ( ( rule__Prefix__Group_0__0 ) ) | ( ruleUnNamedPrefix ) );
    public final void rule__Prefix__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:677:1: ( ( ( rule__Prefix__Group_0__0 ) ) | ( ruleUnNamedPrefix ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==26) ) {
                int LA1_1 = input.LA(2);

                if ( (LA1_1==RULE_ID) ) {
                    alt1=1;
                }
                else if ( (LA1_1==27) ) {
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
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:678:1: ( ( rule__Prefix__Group_0__0 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:678:1: ( ( rule__Prefix__Group_0__0 ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:679:1: ( rule__Prefix__Group_0__0 )
                    {
                     before(grammarAccess.getPrefixAccess().getGroup_0()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:680:1: ( rule__Prefix__Group_0__0 )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:680:2: rule__Prefix__Group_0__0
                    {
                    pushFollow(FOLLOW_rule__Prefix__Group_0__0_in_rule__Prefix__Alternatives1371);
                    rule__Prefix__Group_0__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getPrefixAccess().getGroup_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:684:6: ( ruleUnNamedPrefix )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:684:6: ( ruleUnNamedPrefix )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:685:1: ruleUnNamedPrefix
                    {
                     before(grammarAccess.getPrefixAccess().getUnNamedPrefixParserRuleCall_1()); 
                    pushFollow(FOLLOW_ruleUnNamedPrefix_in_rule__Prefix__Alternatives1389);
                    ruleUnNamedPrefix();

                    state._fsp--;

                     after(grammarAccess.getPrefixAccess().getUnNamedPrefixParserRuleCall_1()); 

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
    // $ANTLR end "rule__Prefix__Alternatives"


    // $ANTLR start "rule__SelectQuery__Alternatives_0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:695:1: rule__SelectQuery__Alternatives_0 : ( ( ( rule__SelectQuery__MethodAssignment_0_0 ) ) | ( '#RUNQUERY' ) );
    public final void rule__SelectQuery__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:699:1: ( ( ( rule__SelectQuery__MethodAssignment_0_0 ) ) | ( '#RUNQUERY' ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==51) ) {
                alt2=1;
            }
            else if ( (LA2_0==15) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:700:1: ( ( rule__SelectQuery__MethodAssignment_0_0 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:700:1: ( ( rule__SelectQuery__MethodAssignment_0_0 ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:701:1: ( rule__SelectQuery__MethodAssignment_0_0 )
                    {
                     before(grammarAccess.getSelectQueryAccess().getMethodAssignment_0_0()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:702:1: ( rule__SelectQuery__MethodAssignment_0_0 )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:702:2: rule__SelectQuery__MethodAssignment_0_0
                    {
                    pushFollow(FOLLOW_rule__SelectQuery__MethodAssignment_0_0_in_rule__SelectQuery__Alternatives_01421);
                    rule__SelectQuery__MethodAssignment_0_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getSelectQueryAccess().getMethodAssignment_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:706:6: ( '#RUNQUERY' )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:706:6: ( '#RUNQUERY' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:707:1: '#RUNQUERY'
                    {
                     before(grammarAccess.getSelectQueryAccess().getRUNQUERYKeyword_0_1()); 
                    match(input,15,FOLLOW_15_in_rule__SelectQuery__Alternatives_01440); 
                     after(grammarAccess.getSelectQueryAccess().getRUNQUERYKeyword_0_1()); 

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
    // $ANTLR end "rule__SelectQuery__Alternatives_0"


    // $ANTLR start "rule__SelectQuery__Alternatives_5"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:719:1: rule__SelectQuery__Alternatives_5 : ( ( ( rule__SelectQuery__IsDistinctAssignment_5_0 ) ) | ( ( rule__SelectQuery__IsReducedAssignment_5_1 ) ) );
    public final void rule__SelectQuery__Alternatives_5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:723:1: ( ( ( rule__SelectQuery__IsDistinctAssignment_5_0 ) ) | ( ( rule__SelectQuery__IsReducedAssignment_5_1 ) ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==52) ) {
                alt3=1;
            }
            else if ( (LA3_0==53) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:724:1: ( ( rule__SelectQuery__IsDistinctAssignment_5_0 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:724:1: ( ( rule__SelectQuery__IsDistinctAssignment_5_0 ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:725:1: ( rule__SelectQuery__IsDistinctAssignment_5_0 )
                    {
                     before(grammarAccess.getSelectQueryAccess().getIsDistinctAssignment_5_0()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:726:1: ( rule__SelectQuery__IsDistinctAssignment_5_0 )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:726:2: rule__SelectQuery__IsDistinctAssignment_5_0
                    {
                    pushFollow(FOLLOW_rule__SelectQuery__IsDistinctAssignment_5_0_in_rule__SelectQuery__Alternatives_51474);
                    rule__SelectQuery__IsDistinctAssignment_5_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getSelectQueryAccess().getIsDistinctAssignment_5_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:730:6: ( ( rule__SelectQuery__IsReducedAssignment_5_1 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:730:6: ( ( rule__SelectQuery__IsReducedAssignment_5_1 ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:731:1: ( rule__SelectQuery__IsReducedAssignment_5_1 )
                    {
                     before(grammarAccess.getSelectQueryAccess().getIsReducedAssignment_5_1()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:732:1: ( rule__SelectQuery__IsReducedAssignment_5_1 )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:732:2: rule__SelectQuery__IsReducedAssignment_5_1
                    {
                    pushFollow(FOLLOW_rule__SelectQuery__IsReducedAssignment_5_1_in_rule__SelectQuery__Alternatives_51492);
                    rule__SelectQuery__IsReducedAssignment_5_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getSelectQueryAccess().getIsReducedAssignment_5_1()); 

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
    // $ANTLR end "rule__SelectQuery__Alternatives_5"


    // $ANTLR start "rule__GraphNode__Alternatives"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:741:1: rule__GraphNode__Alternatives : ( ( ( rule__GraphNode__VariableAssignment_0 ) ) | ( ( rule__GraphNode__LiteralAssignment_1 ) ) | ( ( rule__GraphNode__IriAssignment_2 ) ) );
    public final void rule__GraphNode__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:745:1: ( ( ( rule__GraphNode__VariableAssignment_0 ) ) | ( ( rule__GraphNode__LiteralAssignment_1 ) ) | ( ( rule__GraphNode__IriAssignment_2 ) ) )
            int alt4=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
            case 50:
                {
                alt4=1;
                }
                break;
            case RULE_STRING:
                {
                alt4=2;
                }
                break;
            case RULE_IRI_TERMINAL:
                {
                alt4=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:746:1: ( ( rule__GraphNode__VariableAssignment_0 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:746:1: ( ( rule__GraphNode__VariableAssignment_0 ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:747:1: ( rule__GraphNode__VariableAssignment_0 )
                    {
                     before(grammarAccess.getGraphNodeAccess().getVariableAssignment_0()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:748:1: ( rule__GraphNode__VariableAssignment_0 )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:748:2: rule__GraphNode__VariableAssignment_0
                    {
                    pushFollow(FOLLOW_rule__GraphNode__VariableAssignment_0_in_rule__GraphNode__Alternatives1525);
                    rule__GraphNode__VariableAssignment_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getGraphNodeAccess().getVariableAssignment_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:752:6: ( ( rule__GraphNode__LiteralAssignment_1 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:752:6: ( ( rule__GraphNode__LiteralAssignment_1 ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:753:1: ( rule__GraphNode__LiteralAssignment_1 )
                    {
                     before(grammarAccess.getGraphNodeAccess().getLiteralAssignment_1()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:754:1: ( rule__GraphNode__LiteralAssignment_1 )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:754:2: rule__GraphNode__LiteralAssignment_1
                    {
                    pushFollow(FOLLOW_rule__GraphNode__LiteralAssignment_1_in_rule__GraphNode__Alternatives1543);
                    rule__GraphNode__LiteralAssignment_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getGraphNodeAccess().getLiteralAssignment_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:758:6: ( ( rule__GraphNode__IriAssignment_2 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:758:6: ( ( rule__GraphNode__IriAssignment_2 ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:759:1: ( rule__GraphNode__IriAssignment_2 )
                    {
                     before(grammarAccess.getGraphNodeAccess().getIriAssignment_2()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:760:1: ( rule__GraphNode__IriAssignment_2 )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:760:2: rule__GraphNode__IriAssignment_2
                    {
                    pushFollow(FOLLOW_rule__GraphNode__IriAssignment_2_in_rule__GraphNode__Alternatives1561);
                    rule__GraphNode__IriAssignment_2();

                    state._fsp--;


                    }

                     after(grammarAccess.getGraphNodeAccess().getIriAssignment_2()); 

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
    // $ANTLR end "rule__GraphNode__Alternatives"


    // $ANTLR start "rule__Variable__Alternatives"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:769:1: rule__Variable__Alternatives : ( ( ( rule__Variable__UnnamedAssignment_0 ) ) | ( ( rule__Variable__PropertyAssignment_1 ) ) );
    public final void rule__Variable__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:773:1: ( ( ( rule__Variable__UnnamedAssignment_0 ) ) | ( ( rule__Variable__PropertyAssignment_1 ) ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==50) ) {
                alt5=1;
            }
            else if ( (LA5_0==RULE_ID) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:774:1: ( ( rule__Variable__UnnamedAssignment_0 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:774:1: ( ( rule__Variable__UnnamedAssignment_0 ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:775:1: ( rule__Variable__UnnamedAssignment_0 )
                    {
                     before(grammarAccess.getVariableAccess().getUnnamedAssignment_0()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:776:1: ( rule__Variable__UnnamedAssignment_0 )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:776:2: rule__Variable__UnnamedAssignment_0
                    {
                    pushFollow(FOLLOW_rule__Variable__UnnamedAssignment_0_in_rule__Variable__Alternatives1594);
                    rule__Variable__UnnamedAssignment_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getVariableAccess().getUnnamedAssignment_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:780:6: ( ( rule__Variable__PropertyAssignment_1 ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:780:6: ( ( rule__Variable__PropertyAssignment_1 ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:781:1: ( rule__Variable__PropertyAssignment_1 )
                    {
                     before(grammarAccess.getVariableAccess().getPropertyAssignment_1()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:782:1: ( rule__Variable__PropertyAssignment_1 )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:782:2: rule__Variable__PropertyAssignment_1
                    {
                    pushFollow(FOLLOW_rule__Variable__PropertyAssignment_1_in_rule__Variable__Alternatives1612);
                    rule__Variable__PropertyAssignment_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getVariableAccess().getPropertyAssignment_1()); 

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
    // $ANTLR end "rule__Variable__Alternatives"


    // $ANTLR start "rule__Operator__Alternatives"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:791:1: rule__Operator__Alternatives : ( ( ( '<' ) ) | ( ( '>' ) ) | ( ( '<=' ) ) | ( ( '>=' ) ) | ( ( '=' ) ) | ( ( '!=' ) ) | ( ( '+' ) ) | ( ( '/' ) ) | ( ( '-' ) ) | ( ( '*' ) ) );
    public final void rule__Operator__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:795:1: ( ( ( '<' ) ) | ( ( '>' ) ) | ( ( '<=' ) ) | ( ( '>=' ) ) | ( ( '=' ) ) | ( ( '!=' ) ) | ( ( '+' ) ) | ( ( '/' ) ) | ( ( '-' ) ) | ( ( '*' ) ) )
            int alt6=10;
            switch ( input.LA(1) ) {
            case 16:
                {
                alt6=1;
                }
                break;
            case 17:
                {
                alt6=2;
                }
                break;
            case 18:
                {
                alt6=3;
                }
                break;
            case 19:
                {
                alt6=4;
                }
                break;
            case 20:
                {
                alt6=5;
                }
                break;
            case 21:
                {
                alt6=6;
                }
                break;
            case 22:
                {
                alt6=7;
                }
                break;
            case 23:
                {
                alt6=8;
                }
                break;
            case 24:
                {
                alt6=9;
                }
                break;
            case 25:
                {
                alt6=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:796:1: ( ( '<' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:796:1: ( ( '<' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:797:1: ( '<' )
                    {
                     before(grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:798:1: ( '<' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:798:3: '<'
                    {
                    match(input,16,FOLLOW_16_in_rule__Operator__Alternatives1646); 

                    }

                     after(grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:803:6: ( ( '>' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:803:6: ( ( '>' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:804:1: ( '>' )
                    {
                     before(grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:805:1: ( '>' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:805:3: '>'
                    {
                    match(input,17,FOLLOW_17_in_rule__Operator__Alternatives1667); 

                    }

                     after(grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:810:6: ( ( '<=' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:810:6: ( ( '<=' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:811:1: ( '<=' )
                    {
                     before(grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:812:1: ( '<=' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:812:3: '<='
                    {
                    match(input,18,FOLLOW_18_in_rule__Operator__Alternatives1688); 

                    }

                     after(grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:817:6: ( ( '>=' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:817:6: ( ( '>=' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:818:1: ( '>=' )
                    {
                     before(grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:819:1: ( '>=' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:819:3: '>='
                    {
                    match(input,19,FOLLOW_19_in_rule__Operator__Alternatives1709); 

                    }

                     after(grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3()); 

                    }


                    }
                    break;
                case 5 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:824:6: ( ( '=' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:824:6: ( ( '=' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:825:1: ( '=' )
                    {
                     before(grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:826:1: ( '=' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:826:3: '='
                    {
                    match(input,20,FOLLOW_20_in_rule__Operator__Alternatives1730); 

                    }

                     after(grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4()); 

                    }


                    }
                    break;
                case 6 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:831:6: ( ( '!=' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:831:6: ( ( '!=' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:832:1: ( '!=' )
                    {
                     before(grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:833:1: ( '!=' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:833:3: '!='
                    {
                    match(input,21,FOLLOW_21_in_rule__Operator__Alternatives1751); 

                    }

                     after(grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5()); 

                    }


                    }
                    break;
                case 7 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:838:6: ( ( '+' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:838:6: ( ( '+' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:839:1: ( '+' )
                    {
                     before(grammarAccess.getOperatorAccess().getSumEnumLiteralDeclaration_6()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:840:1: ( '+' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:840:3: '+'
                    {
                    match(input,22,FOLLOW_22_in_rule__Operator__Alternatives1772); 

                    }

                     after(grammarAccess.getOperatorAccess().getSumEnumLiteralDeclaration_6()); 

                    }


                    }
                    break;
                case 8 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:845:6: ( ( '/' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:845:6: ( ( '/' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:846:1: ( '/' )
                    {
                     before(grammarAccess.getOperatorAccess().getDivEnumLiteralDeclaration_7()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:847:1: ( '/' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:847:3: '/'
                    {
                    match(input,23,FOLLOW_23_in_rule__Operator__Alternatives1793); 

                    }

                     after(grammarAccess.getOperatorAccess().getDivEnumLiteralDeclaration_7()); 

                    }


                    }
                    break;
                case 9 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:852:6: ( ( '-' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:852:6: ( ( '-' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:853:1: ( '-' )
                    {
                     before(grammarAccess.getOperatorAccess().getSubEnumLiteralDeclaration_8()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:854:1: ( '-' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:854:3: '-'
                    {
                    match(input,24,FOLLOW_24_in_rule__Operator__Alternatives1814); 

                    }

                     after(grammarAccess.getOperatorAccess().getSubEnumLiteralDeclaration_8()); 

                    }


                    }
                    break;
                case 10 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:859:6: ( ( '*' ) )
                    {
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:859:6: ( ( '*' ) )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:860:1: ( '*' )
                    {
                     before(grammarAccess.getOperatorAccess().getMultiplicityEnumLiteralDeclaration_9()); 
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:861:1: ( '*' )
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:861:3: '*'
                    {
                    match(input,25,FOLLOW_25_in_rule__Operator__Alternatives1835); 

                    }

                     after(grammarAccess.getOperatorAccess().getMultiplicityEnumLiteralDeclaration_9()); 

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
    // $ANTLR end "rule__Operator__Alternatives"


    // $ANTLR start "rule__Prefix__Group_0__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:873:1: rule__Prefix__Group_0__0 : rule__Prefix__Group_0__0__Impl rule__Prefix__Group_0__1 ;
    public final void rule__Prefix__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:877:1: ( rule__Prefix__Group_0__0__Impl rule__Prefix__Group_0__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:878:2: rule__Prefix__Group_0__0__Impl rule__Prefix__Group_0__1
            {
            pushFollow(FOLLOW_rule__Prefix__Group_0__0__Impl_in_rule__Prefix__Group_0__01868);
            rule__Prefix__Group_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Prefix__Group_0__1_in_rule__Prefix__Group_0__01871);
            rule__Prefix__Group_0__1();

            state._fsp--;


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
    // $ANTLR end "rule__Prefix__Group_0__0"


    // $ANTLR start "rule__Prefix__Group_0__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:885:1: rule__Prefix__Group_0__0__Impl : ( 'PREFIX' ) ;
    public final void rule__Prefix__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:889:1: ( ( 'PREFIX' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:890:1: ( 'PREFIX' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:890:1: ( 'PREFIX' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:891:1: 'PREFIX'
            {
             before(grammarAccess.getPrefixAccess().getPREFIXKeyword_0_0()); 
            match(input,26,FOLLOW_26_in_rule__Prefix__Group_0__0__Impl1899); 
             after(grammarAccess.getPrefixAccess().getPREFIXKeyword_0_0()); 

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
    // $ANTLR end "rule__Prefix__Group_0__0__Impl"


    // $ANTLR start "rule__Prefix__Group_0__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:904:1: rule__Prefix__Group_0__1 : rule__Prefix__Group_0__1__Impl rule__Prefix__Group_0__2 ;
    public final void rule__Prefix__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:908:1: ( rule__Prefix__Group_0__1__Impl rule__Prefix__Group_0__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:909:2: rule__Prefix__Group_0__1__Impl rule__Prefix__Group_0__2
            {
            pushFollow(FOLLOW_rule__Prefix__Group_0__1__Impl_in_rule__Prefix__Group_0__11930);
            rule__Prefix__Group_0__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Prefix__Group_0__2_in_rule__Prefix__Group_0__11933);
            rule__Prefix__Group_0__2();

            state._fsp--;


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
    // $ANTLR end "rule__Prefix__Group_0__1"


    // $ANTLR start "rule__Prefix__Group_0__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:916:1: rule__Prefix__Group_0__1__Impl : ( ( rule__Prefix__NameAssignment_0_1 ) ) ;
    public final void rule__Prefix__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:920:1: ( ( ( rule__Prefix__NameAssignment_0_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:921:1: ( ( rule__Prefix__NameAssignment_0_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:921:1: ( ( rule__Prefix__NameAssignment_0_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:922:1: ( rule__Prefix__NameAssignment_0_1 )
            {
             before(grammarAccess.getPrefixAccess().getNameAssignment_0_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:923:1: ( rule__Prefix__NameAssignment_0_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:923:2: rule__Prefix__NameAssignment_0_1
            {
            pushFollow(FOLLOW_rule__Prefix__NameAssignment_0_1_in_rule__Prefix__Group_0__1__Impl1960);
            rule__Prefix__NameAssignment_0_1();

            state._fsp--;


            }

             after(grammarAccess.getPrefixAccess().getNameAssignment_0_1()); 

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
    // $ANTLR end "rule__Prefix__Group_0__1__Impl"


    // $ANTLR start "rule__Prefix__Group_0__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:933:1: rule__Prefix__Group_0__2 : rule__Prefix__Group_0__2__Impl rule__Prefix__Group_0__3 ;
    public final void rule__Prefix__Group_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:937:1: ( rule__Prefix__Group_0__2__Impl rule__Prefix__Group_0__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:938:2: rule__Prefix__Group_0__2__Impl rule__Prefix__Group_0__3
            {
            pushFollow(FOLLOW_rule__Prefix__Group_0__2__Impl_in_rule__Prefix__Group_0__21990);
            rule__Prefix__Group_0__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Prefix__Group_0__3_in_rule__Prefix__Group_0__21993);
            rule__Prefix__Group_0__3();

            state._fsp--;


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
    // $ANTLR end "rule__Prefix__Group_0__2"


    // $ANTLR start "rule__Prefix__Group_0__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:945:1: rule__Prefix__Group_0__2__Impl : ( ':' ) ;
    public final void rule__Prefix__Group_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:949:1: ( ( ':' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:950:1: ( ':' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:950:1: ( ':' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:951:1: ':'
            {
             before(grammarAccess.getPrefixAccess().getColonKeyword_0_2()); 
            match(input,27,FOLLOW_27_in_rule__Prefix__Group_0__2__Impl2021); 
             after(grammarAccess.getPrefixAccess().getColonKeyword_0_2()); 

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
    // $ANTLR end "rule__Prefix__Group_0__2__Impl"


    // $ANTLR start "rule__Prefix__Group_0__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:964:1: rule__Prefix__Group_0__3 : rule__Prefix__Group_0__3__Impl ;
    public final void rule__Prefix__Group_0__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:968:1: ( rule__Prefix__Group_0__3__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:969:2: rule__Prefix__Group_0__3__Impl
            {
            pushFollow(FOLLOW_rule__Prefix__Group_0__3__Impl_in_rule__Prefix__Group_0__32052);
            rule__Prefix__Group_0__3__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Prefix__Group_0__3"


    // $ANTLR start "rule__Prefix__Group_0__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:975:1: rule__Prefix__Group_0__3__Impl : ( ( rule__Prefix__IrefAssignment_0_3 ) ) ;
    public final void rule__Prefix__Group_0__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:979:1: ( ( ( rule__Prefix__IrefAssignment_0_3 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:980:1: ( ( rule__Prefix__IrefAssignment_0_3 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:980:1: ( ( rule__Prefix__IrefAssignment_0_3 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:981:1: ( rule__Prefix__IrefAssignment_0_3 )
            {
             before(grammarAccess.getPrefixAccess().getIrefAssignment_0_3()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:982:1: ( rule__Prefix__IrefAssignment_0_3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:982:2: rule__Prefix__IrefAssignment_0_3
            {
            pushFollow(FOLLOW_rule__Prefix__IrefAssignment_0_3_in_rule__Prefix__Group_0__3__Impl2079);
            rule__Prefix__IrefAssignment_0_3();

            state._fsp--;


            }

             after(grammarAccess.getPrefixAccess().getIrefAssignment_0_3()); 

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
    // $ANTLR end "rule__Prefix__Group_0__3__Impl"


    // $ANTLR start "rule__UnNamedPrefix__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1000:1: rule__UnNamedPrefix__Group__0 : rule__UnNamedPrefix__Group__0__Impl rule__UnNamedPrefix__Group__1 ;
    public final void rule__UnNamedPrefix__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1004:1: ( rule__UnNamedPrefix__Group__0__Impl rule__UnNamedPrefix__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1005:2: rule__UnNamedPrefix__Group__0__Impl rule__UnNamedPrefix__Group__1
            {
            pushFollow(FOLLOW_rule__UnNamedPrefix__Group__0__Impl_in_rule__UnNamedPrefix__Group__02117);
            rule__UnNamedPrefix__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UnNamedPrefix__Group__1_in_rule__UnNamedPrefix__Group__02120);
            rule__UnNamedPrefix__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__UnNamedPrefix__Group__0"


    // $ANTLR start "rule__UnNamedPrefix__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1012:1: rule__UnNamedPrefix__Group__0__Impl : ( 'PREFIX' ) ;
    public final void rule__UnNamedPrefix__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1016:1: ( ( 'PREFIX' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1017:1: ( 'PREFIX' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1017:1: ( 'PREFIX' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1018:1: 'PREFIX'
            {
             before(grammarAccess.getUnNamedPrefixAccess().getPREFIXKeyword_0()); 
            match(input,26,FOLLOW_26_in_rule__UnNamedPrefix__Group__0__Impl2148); 
             after(grammarAccess.getUnNamedPrefixAccess().getPREFIXKeyword_0()); 

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
    // $ANTLR end "rule__UnNamedPrefix__Group__0__Impl"


    // $ANTLR start "rule__UnNamedPrefix__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1031:1: rule__UnNamedPrefix__Group__1 : rule__UnNamedPrefix__Group__1__Impl rule__UnNamedPrefix__Group__2 ;
    public final void rule__UnNamedPrefix__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1035:1: ( rule__UnNamedPrefix__Group__1__Impl rule__UnNamedPrefix__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1036:2: rule__UnNamedPrefix__Group__1__Impl rule__UnNamedPrefix__Group__2
            {
            pushFollow(FOLLOW_rule__UnNamedPrefix__Group__1__Impl_in_rule__UnNamedPrefix__Group__12179);
            rule__UnNamedPrefix__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UnNamedPrefix__Group__2_in_rule__UnNamedPrefix__Group__12182);
            rule__UnNamedPrefix__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__UnNamedPrefix__Group__1"


    // $ANTLR start "rule__UnNamedPrefix__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1043:1: rule__UnNamedPrefix__Group__1__Impl : ( ':' ) ;
    public final void rule__UnNamedPrefix__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1047:1: ( ( ':' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1048:1: ( ':' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1048:1: ( ':' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1049:1: ':'
            {
             before(grammarAccess.getUnNamedPrefixAccess().getColonKeyword_1()); 
            match(input,27,FOLLOW_27_in_rule__UnNamedPrefix__Group__1__Impl2210); 
             after(grammarAccess.getUnNamedPrefixAccess().getColonKeyword_1()); 

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
    // $ANTLR end "rule__UnNamedPrefix__Group__1__Impl"


    // $ANTLR start "rule__UnNamedPrefix__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1062:1: rule__UnNamedPrefix__Group__2 : rule__UnNamedPrefix__Group__2__Impl ;
    public final void rule__UnNamedPrefix__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1066:1: ( rule__UnNamedPrefix__Group__2__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1067:2: rule__UnNamedPrefix__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__UnNamedPrefix__Group__2__Impl_in_rule__UnNamedPrefix__Group__22241);
            rule__UnNamedPrefix__Group__2__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__UnNamedPrefix__Group__2"


    // $ANTLR start "rule__UnNamedPrefix__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1073:1: rule__UnNamedPrefix__Group__2__Impl : ( ( rule__UnNamedPrefix__IrefAssignment_2 ) ) ;
    public final void rule__UnNamedPrefix__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1077:1: ( ( ( rule__UnNamedPrefix__IrefAssignment_2 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1078:1: ( ( rule__UnNamedPrefix__IrefAssignment_2 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1078:1: ( ( rule__UnNamedPrefix__IrefAssignment_2 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1079:1: ( rule__UnNamedPrefix__IrefAssignment_2 )
            {
             before(grammarAccess.getUnNamedPrefixAccess().getIrefAssignment_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1080:1: ( rule__UnNamedPrefix__IrefAssignment_2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1080:2: rule__UnNamedPrefix__IrefAssignment_2
            {
            pushFollow(FOLLOW_rule__UnNamedPrefix__IrefAssignment_2_in_rule__UnNamedPrefix__Group__2__Impl2268);
            rule__UnNamedPrefix__IrefAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getUnNamedPrefixAccess().getIrefAssignment_2()); 

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
    // $ANTLR end "rule__UnNamedPrefix__Group__2__Impl"


    // $ANTLR start "rule__Base__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1096:1: rule__Base__Group__0 : rule__Base__Group__0__Impl rule__Base__Group__1 ;
    public final void rule__Base__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1100:1: ( rule__Base__Group__0__Impl rule__Base__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1101:2: rule__Base__Group__0__Impl rule__Base__Group__1
            {
            pushFollow(FOLLOW_rule__Base__Group__0__Impl_in_rule__Base__Group__02304);
            rule__Base__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Base__Group__1_in_rule__Base__Group__02307);
            rule__Base__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__Base__Group__0"


    // $ANTLR start "rule__Base__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1108:1: rule__Base__Group__0__Impl : ( 'BASE' ) ;
    public final void rule__Base__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1112:1: ( ( 'BASE' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1113:1: ( 'BASE' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1113:1: ( 'BASE' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1114:1: 'BASE'
            {
             before(grammarAccess.getBaseAccess().getBASEKeyword_0()); 
            match(input,28,FOLLOW_28_in_rule__Base__Group__0__Impl2335); 
             after(grammarAccess.getBaseAccess().getBASEKeyword_0()); 

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
    // $ANTLR end "rule__Base__Group__0__Impl"


    // $ANTLR start "rule__Base__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1127:1: rule__Base__Group__1 : rule__Base__Group__1__Impl ;
    public final void rule__Base__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1131:1: ( rule__Base__Group__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1132:2: rule__Base__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Base__Group__1__Impl_in_rule__Base__Group__12366);
            rule__Base__Group__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Base__Group__1"


    // $ANTLR start "rule__Base__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1138:1: rule__Base__Group__1__Impl : ( ( rule__Base__IrefAssignment_1 ) ) ;
    public final void rule__Base__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1142:1: ( ( ( rule__Base__IrefAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1143:1: ( ( rule__Base__IrefAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1143:1: ( ( rule__Base__IrefAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1144:1: ( rule__Base__IrefAssignment_1 )
            {
             before(grammarAccess.getBaseAccess().getIrefAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1145:1: ( rule__Base__IrefAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1145:2: rule__Base__IrefAssignment_1
            {
            pushFollow(FOLLOW_rule__Base__IrefAssignment_1_in_rule__Base__Group__1__Impl2393);
            rule__Base__IrefAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getBaseAccess().getIrefAssignment_1()); 

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
    // $ANTLR end "rule__Base__Group__1__Impl"


    // $ANTLR start "rule__SelectQuery__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1159:1: rule__SelectQuery__Group__0 : rule__SelectQuery__Group__0__Impl rule__SelectQuery__Group__1 ;
    public final void rule__SelectQuery__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1163:1: ( rule__SelectQuery__Group__0__Impl rule__SelectQuery__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1164:2: rule__SelectQuery__Group__0__Impl rule__SelectQuery__Group__1
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__0__Impl_in_rule__SelectQuery__Group__02427);
            rule__SelectQuery__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__1_in_rule__SelectQuery__Group__02430);
            rule__SelectQuery__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__0"


    // $ANTLR start "rule__SelectQuery__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1171:1: rule__SelectQuery__Group__0__Impl : ( ( rule__SelectQuery__Alternatives_0 )? ) ;
    public final void rule__SelectQuery__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1175:1: ( ( ( rule__SelectQuery__Alternatives_0 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1176:1: ( ( rule__SelectQuery__Alternatives_0 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1176:1: ( ( rule__SelectQuery__Alternatives_0 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1177:1: ( rule__SelectQuery__Alternatives_0 )?
            {
             before(grammarAccess.getSelectQueryAccess().getAlternatives_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1178:1: ( rule__SelectQuery__Alternatives_0 )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==15||LA7_0==51) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1178:2: rule__SelectQuery__Alternatives_0
                    {
                    pushFollow(FOLLOW_rule__SelectQuery__Alternatives_0_in_rule__SelectQuery__Group__0__Impl2457);
                    rule__SelectQuery__Alternatives_0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectQueryAccess().getAlternatives_0()); 

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
    // $ANTLR end "rule__SelectQuery__Group__0__Impl"


    // $ANTLR start "rule__SelectQuery__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1188:1: rule__SelectQuery__Group__1 : rule__SelectQuery__Group__1__Impl rule__SelectQuery__Group__2 ;
    public final void rule__SelectQuery__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1192:1: ( rule__SelectQuery__Group__1__Impl rule__SelectQuery__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1193:2: rule__SelectQuery__Group__1__Impl rule__SelectQuery__Group__2
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__1__Impl_in_rule__SelectQuery__Group__12488);
            rule__SelectQuery__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__2_in_rule__SelectQuery__Group__12491);
            rule__SelectQuery__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__1"


    // $ANTLR start "rule__SelectQuery__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1200:1: rule__SelectQuery__Group__1__Impl : ( ( rule__SelectQuery__BaseAssignment_1 )? ) ;
    public final void rule__SelectQuery__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1204:1: ( ( ( rule__SelectQuery__BaseAssignment_1 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1205:1: ( ( rule__SelectQuery__BaseAssignment_1 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1205:1: ( ( rule__SelectQuery__BaseAssignment_1 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1206:1: ( rule__SelectQuery__BaseAssignment_1 )?
            {
             before(grammarAccess.getSelectQueryAccess().getBaseAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1207:1: ( rule__SelectQuery__BaseAssignment_1 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==28) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1207:2: rule__SelectQuery__BaseAssignment_1
                    {
                    pushFollow(FOLLOW_rule__SelectQuery__BaseAssignment_1_in_rule__SelectQuery__Group__1__Impl2518);
                    rule__SelectQuery__BaseAssignment_1();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectQueryAccess().getBaseAssignment_1()); 

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
    // $ANTLR end "rule__SelectQuery__Group__1__Impl"


    // $ANTLR start "rule__SelectQuery__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1217:1: rule__SelectQuery__Group__2 : rule__SelectQuery__Group__2__Impl rule__SelectQuery__Group__3 ;
    public final void rule__SelectQuery__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1221:1: ( rule__SelectQuery__Group__2__Impl rule__SelectQuery__Group__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1222:2: rule__SelectQuery__Group__2__Impl rule__SelectQuery__Group__3
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__2__Impl_in_rule__SelectQuery__Group__22549);
            rule__SelectQuery__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__3_in_rule__SelectQuery__Group__22552);
            rule__SelectQuery__Group__3();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__2"


    // $ANTLR start "rule__SelectQuery__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1229:1: rule__SelectQuery__Group__2__Impl : ( ( rule__SelectQuery__PrefixesAssignment_2 )* ) ;
    public final void rule__SelectQuery__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1233:1: ( ( ( rule__SelectQuery__PrefixesAssignment_2 )* ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1234:1: ( ( rule__SelectQuery__PrefixesAssignment_2 )* )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1234:1: ( ( rule__SelectQuery__PrefixesAssignment_2 )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1235:1: ( rule__SelectQuery__PrefixesAssignment_2 )*
            {
             before(grammarAccess.getSelectQueryAccess().getPrefixesAssignment_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1236:1: ( rule__SelectQuery__PrefixesAssignment_2 )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==26) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1236:2: rule__SelectQuery__PrefixesAssignment_2
            	    {
            	    pushFollow(FOLLOW_rule__SelectQuery__PrefixesAssignment_2_in_rule__SelectQuery__Group__2__Impl2579);
            	    rule__SelectQuery__PrefixesAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

             after(grammarAccess.getSelectQueryAccess().getPrefixesAssignment_2()); 

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
    // $ANTLR end "rule__SelectQuery__Group__2__Impl"


    // $ANTLR start "rule__SelectQuery__Group__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1246:1: rule__SelectQuery__Group__3 : rule__SelectQuery__Group__3__Impl rule__SelectQuery__Group__4 ;
    public final void rule__SelectQuery__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1250:1: ( rule__SelectQuery__Group__3__Impl rule__SelectQuery__Group__4 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1251:2: rule__SelectQuery__Group__3__Impl rule__SelectQuery__Group__4
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__3__Impl_in_rule__SelectQuery__Group__32610);
            rule__SelectQuery__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__4_in_rule__SelectQuery__Group__32613);
            rule__SelectQuery__Group__4();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__3"


    // $ANTLR start "rule__SelectQuery__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1258:1: rule__SelectQuery__Group__3__Impl : ( ( rule__SelectQuery__DatasetClausesAssignment_3 )* ) ;
    public final void rule__SelectQuery__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1262:1: ( ( ( rule__SelectQuery__DatasetClausesAssignment_3 )* ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1263:1: ( ( rule__SelectQuery__DatasetClausesAssignment_3 )* )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1263:1: ( ( rule__SelectQuery__DatasetClausesAssignment_3 )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1264:1: ( rule__SelectQuery__DatasetClausesAssignment_3 )*
            {
             before(grammarAccess.getSelectQueryAccess().getDatasetClausesAssignment_3()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1265:1: ( rule__SelectQuery__DatasetClausesAssignment_3 )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==39) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1265:2: rule__SelectQuery__DatasetClausesAssignment_3
            	    {
            	    pushFollow(FOLLOW_rule__SelectQuery__DatasetClausesAssignment_3_in_rule__SelectQuery__Group__3__Impl2640);
            	    rule__SelectQuery__DatasetClausesAssignment_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

             after(grammarAccess.getSelectQueryAccess().getDatasetClausesAssignment_3()); 

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
    // $ANTLR end "rule__SelectQuery__Group__3__Impl"


    // $ANTLR start "rule__SelectQuery__Group__4"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1275:1: rule__SelectQuery__Group__4 : rule__SelectQuery__Group__4__Impl rule__SelectQuery__Group__5 ;
    public final void rule__SelectQuery__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1279:1: ( rule__SelectQuery__Group__4__Impl rule__SelectQuery__Group__5 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1280:2: rule__SelectQuery__Group__4__Impl rule__SelectQuery__Group__5
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__4__Impl_in_rule__SelectQuery__Group__42671);
            rule__SelectQuery__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__5_in_rule__SelectQuery__Group__42674);
            rule__SelectQuery__Group__5();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__4"


    // $ANTLR start "rule__SelectQuery__Group__4__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1287:1: rule__SelectQuery__Group__4__Impl : ( 'SELECT' ) ;
    public final void rule__SelectQuery__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1291:1: ( ( 'SELECT' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1292:1: ( 'SELECT' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1292:1: ( 'SELECT' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1293:1: 'SELECT'
            {
             before(grammarAccess.getSelectQueryAccess().getSELECTKeyword_4()); 
            match(input,29,FOLLOW_29_in_rule__SelectQuery__Group__4__Impl2702); 
             after(grammarAccess.getSelectQueryAccess().getSELECTKeyword_4()); 

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
    // $ANTLR end "rule__SelectQuery__Group__4__Impl"


    // $ANTLR start "rule__SelectQuery__Group__5"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1306:1: rule__SelectQuery__Group__5 : rule__SelectQuery__Group__5__Impl rule__SelectQuery__Group__6 ;
    public final void rule__SelectQuery__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1310:1: ( rule__SelectQuery__Group__5__Impl rule__SelectQuery__Group__6 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1311:2: rule__SelectQuery__Group__5__Impl rule__SelectQuery__Group__6
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__5__Impl_in_rule__SelectQuery__Group__52733);
            rule__SelectQuery__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__6_in_rule__SelectQuery__Group__52736);
            rule__SelectQuery__Group__6();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__5"


    // $ANTLR start "rule__SelectQuery__Group__5__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1318:1: rule__SelectQuery__Group__5__Impl : ( ( rule__SelectQuery__Alternatives_5 )? ) ;
    public final void rule__SelectQuery__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1322:1: ( ( ( rule__SelectQuery__Alternatives_5 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1323:1: ( ( rule__SelectQuery__Alternatives_5 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1323:1: ( ( rule__SelectQuery__Alternatives_5 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1324:1: ( rule__SelectQuery__Alternatives_5 )?
            {
             before(grammarAccess.getSelectQueryAccess().getAlternatives_5()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1325:1: ( rule__SelectQuery__Alternatives_5 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0>=52 && LA11_0<=53)) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1325:2: rule__SelectQuery__Alternatives_5
                    {
                    pushFollow(FOLLOW_rule__SelectQuery__Alternatives_5_in_rule__SelectQuery__Group__5__Impl2763);
                    rule__SelectQuery__Alternatives_5();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectQueryAccess().getAlternatives_5()); 

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
    // $ANTLR end "rule__SelectQuery__Group__5__Impl"


    // $ANTLR start "rule__SelectQuery__Group__6"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1335:1: rule__SelectQuery__Group__6 : rule__SelectQuery__Group__6__Impl rule__SelectQuery__Group__7 ;
    public final void rule__SelectQuery__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1339:1: ( rule__SelectQuery__Group__6__Impl rule__SelectQuery__Group__7 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1340:2: rule__SelectQuery__Group__6__Impl rule__SelectQuery__Group__7
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__6__Impl_in_rule__SelectQuery__Group__62794);
            rule__SelectQuery__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__7_in_rule__SelectQuery__Group__62797);
            rule__SelectQuery__Group__7();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__6"


    // $ANTLR start "rule__SelectQuery__Group__6__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1347:1: rule__SelectQuery__Group__6__Impl : ( ( rule__SelectQuery__VariablesAssignment_6 ) ) ;
    public final void rule__SelectQuery__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1351:1: ( ( ( rule__SelectQuery__VariablesAssignment_6 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1352:1: ( ( rule__SelectQuery__VariablesAssignment_6 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1352:1: ( ( rule__SelectQuery__VariablesAssignment_6 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1353:1: ( rule__SelectQuery__VariablesAssignment_6 )
            {
             before(grammarAccess.getSelectQueryAccess().getVariablesAssignment_6()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1354:1: ( rule__SelectQuery__VariablesAssignment_6 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1354:2: rule__SelectQuery__VariablesAssignment_6
            {
            pushFollow(FOLLOW_rule__SelectQuery__VariablesAssignment_6_in_rule__SelectQuery__Group__6__Impl2824);
            rule__SelectQuery__VariablesAssignment_6();

            state._fsp--;


            }

             after(grammarAccess.getSelectQueryAccess().getVariablesAssignment_6()); 

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
    // $ANTLR end "rule__SelectQuery__Group__6__Impl"


    // $ANTLR start "rule__SelectQuery__Group__7"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1364:1: rule__SelectQuery__Group__7 : rule__SelectQuery__Group__7__Impl rule__SelectQuery__Group__8 ;
    public final void rule__SelectQuery__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1368:1: ( rule__SelectQuery__Group__7__Impl rule__SelectQuery__Group__8 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1369:2: rule__SelectQuery__Group__7__Impl rule__SelectQuery__Group__8
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__7__Impl_in_rule__SelectQuery__Group__72854);
            rule__SelectQuery__Group__7__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__8_in_rule__SelectQuery__Group__72857);
            rule__SelectQuery__Group__8();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__7"


    // $ANTLR start "rule__SelectQuery__Group__7__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1376:1: rule__SelectQuery__Group__7__Impl : ( ( rule__SelectQuery__VariablesAssignment_7 )* ) ;
    public final void rule__SelectQuery__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1380:1: ( ( ( rule__SelectQuery__VariablesAssignment_7 )* ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1381:1: ( ( rule__SelectQuery__VariablesAssignment_7 )* )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1381:1: ( ( rule__SelectQuery__VariablesAssignment_7 )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1382:1: ( rule__SelectQuery__VariablesAssignment_7 )*
            {
             before(grammarAccess.getSelectQueryAccess().getVariablesAssignment_7()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1383:1: ( rule__SelectQuery__VariablesAssignment_7 )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==RULE_ID||LA12_0==50) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1383:2: rule__SelectQuery__VariablesAssignment_7
            	    {
            	    pushFollow(FOLLOW_rule__SelectQuery__VariablesAssignment_7_in_rule__SelectQuery__Group__7__Impl2884);
            	    rule__SelectQuery__VariablesAssignment_7();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

             after(grammarAccess.getSelectQueryAccess().getVariablesAssignment_7()); 

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
    // $ANTLR end "rule__SelectQuery__Group__7__Impl"


    // $ANTLR start "rule__SelectQuery__Group__8"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1393:1: rule__SelectQuery__Group__8 : rule__SelectQuery__Group__8__Impl rule__SelectQuery__Group__9 ;
    public final void rule__SelectQuery__Group__8() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1397:1: ( rule__SelectQuery__Group__8__Impl rule__SelectQuery__Group__9 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1398:2: rule__SelectQuery__Group__8__Impl rule__SelectQuery__Group__9
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__8__Impl_in_rule__SelectQuery__Group__82915);
            rule__SelectQuery__Group__8__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__9_in_rule__SelectQuery__Group__82918);
            rule__SelectQuery__Group__9();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__8"


    // $ANTLR start "rule__SelectQuery__Group__8__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1405:1: rule__SelectQuery__Group__8__Impl : ( ( rule__SelectQuery__WhereClauseAssignment_8 ) ) ;
    public final void rule__SelectQuery__Group__8__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1409:1: ( ( ( rule__SelectQuery__WhereClauseAssignment_8 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1410:1: ( ( rule__SelectQuery__WhereClauseAssignment_8 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1410:1: ( ( rule__SelectQuery__WhereClauseAssignment_8 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1411:1: ( rule__SelectQuery__WhereClauseAssignment_8 )
            {
             before(grammarAccess.getSelectQueryAccess().getWhereClauseAssignment_8()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1412:1: ( rule__SelectQuery__WhereClauseAssignment_8 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1412:2: rule__SelectQuery__WhereClauseAssignment_8
            {
            pushFollow(FOLLOW_rule__SelectQuery__WhereClauseAssignment_8_in_rule__SelectQuery__Group__8__Impl2945);
            rule__SelectQuery__WhereClauseAssignment_8();

            state._fsp--;


            }

             after(grammarAccess.getSelectQueryAccess().getWhereClauseAssignment_8()); 

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
    // $ANTLR end "rule__SelectQuery__Group__8__Impl"


    // $ANTLR start "rule__SelectQuery__Group__9"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1422:1: rule__SelectQuery__Group__9 : rule__SelectQuery__Group__9__Impl rule__SelectQuery__Group__10 ;
    public final void rule__SelectQuery__Group__9() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1426:1: ( rule__SelectQuery__Group__9__Impl rule__SelectQuery__Group__10 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1427:2: rule__SelectQuery__Group__9__Impl rule__SelectQuery__Group__10
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__9__Impl_in_rule__SelectQuery__Group__92975);
            rule__SelectQuery__Group__9__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__10_in_rule__SelectQuery__Group__92978);
            rule__SelectQuery__Group__10();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__9"


    // $ANTLR start "rule__SelectQuery__Group__9__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1434:1: rule__SelectQuery__Group__9__Impl : ( ( rule__SelectQuery__FilterclauseAssignment_9 )? ) ;
    public final void rule__SelectQuery__Group__9__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1438:1: ( ( ( rule__SelectQuery__FilterclauseAssignment_9 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1439:1: ( ( rule__SelectQuery__FilterclauseAssignment_9 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1439:1: ( ( rule__SelectQuery__FilterclauseAssignment_9 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1440:1: ( rule__SelectQuery__FilterclauseAssignment_9 )?
            {
             before(grammarAccess.getSelectQueryAccess().getFilterclauseAssignment_9()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1441:1: ( rule__SelectQuery__FilterclauseAssignment_9 )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==38) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1441:2: rule__SelectQuery__FilterclauseAssignment_9
                    {
                    pushFollow(FOLLOW_rule__SelectQuery__FilterclauseAssignment_9_in_rule__SelectQuery__Group__9__Impl3005);
                    rule__SelectQuery__FilterclauseAssignment_9();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectQueryAccess().getFilterclauseAssignment_9()); 

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
    // $ANTLR end "rule__SelectQuery__Group__9__Impl"


    // $ANTLR start "rule__SelectQuery__Group__10"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1451:1: rule__SelectQuery__Group__10 : rule__SelectQuery__Group__10__Impl rule__SelectQuery__Group__11 ;
    public final void rule__SelectQuery__Group__10() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1455:1: ( rule__SelectQuery__Group__10__Impl rule__SelectQuery__Group__11 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1456:2: rule__SelectQuery__Group__10__Impl rule__SelectQuery__Group__11
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__10__Impl_in_rule__SelectQuery__Group__103036);
            rule__SelectQuery__Group__10__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SelectQuery__Group__11_in_rule__SelectQuery__Group__103039);
            rule__SelectQuery__Group__11();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__10"


    // $ANTLR start "rule__SelectQuery__Group__10__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1463:1: rule__SelectQuery__Group__10__Impl : ( ( rule__SelectQuery__AggregateClauseAssignment_10 )? ) ;
    public final void rule__SelectQuery__Group__10__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1467:1: ( ( ( rule__SelectQuery__AggregateClauseAssignment_10 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1468:1: ( ( rule__SelectQuery__AggregateClauseAssignment_10 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1468:1: ( ( rule__SelectQuery__AggregateClauseAssignment_10 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1469:1: ( rule__SelectQuery__AggregateClauseAssignment_10 )?
            {
             before(grammarAccess.getSelectQueryAccess().getAggregateClauseAssignment_10()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1470:1: ( rule__SelectQuery__AggregateClauseAssignment_10 )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==30) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1470:2: rule__SelectQuery__AggregateClauseAssignment_10
                    {
                    pushFollow(FOLLOW_rule__SelectQuery__AggregateClauseAssignment_10_in_rule__SelectQuery__Group__10__Impl3066);
                    rule__SelectQuery__AggregateClauseAssignment_10();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectQueryAccess().getAggregateClauseAssignment_10()); 

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
    // $ANTLR end "rule__SelectQuery__Group__10__Impl"


    // $ANTLR start "rule__SelectQuery__Group__11"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1480:1: rule__SelectQuery__Group__11 : rule__SelectQuery__Group__11__Impl ;
    public final void rule__SelectQuery__Group__11() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1484:1: ( rule__SelectQuery__Group__11__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1485:2: rule__SelectQuery__Group__11__Impl
            {
            pushFollow(FOLLOW_rule__SelectQuery__Group__11__Impl_in_rule__SelectQuery__Group__113097);
            rule__SelectQuery__Group__11__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__SelectQuery__Group__11"


    // $ANTLR start "rule__SelectQuery__Group__11__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1491:1: rule__SelectQuery__Group__11__Impl : ( ( rule__SelectQuery__FilesinkclauseAssignment_11 )? ) ;
    public final void rule__SelectQuery__Group__11__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1495:1: ( ( ( rule__SelectQuery__FilesinkclauseAssignment_11 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1496:1: ( ( rule__SelectQuery__FilesinkclauseAssignment_11 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1496:1: ( ( rule__SelectQuery__FilesinkclauseAssignment_11 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1497:1: ( rule__SelectQuery__FilesinkclauseAssignment_11 )?
            {
             before(grammarAccess.getSelectQueryAccess().getFilesinkclauseAssignment_11()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1498:1: ( rule__SelectQuery__FilesinkclauseAssignment_11 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==37) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1498:2: rule__SelectQuery__FilesinkclauseAssignment_11
                    {
                    pushFollow(FOLLOW_rule__SelectQuery__FilesinkclauseAssignment_11_in_rule__SelectQuery__Group__11__Impl3124);
                    rule__SelectQuery__FilesinkclauseAssignment_11();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectQueryAccess().getFilesinkclauseAssignment_11()); 

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
    // $ANTLR end "rule__SelectQuery__Group__11__Impl"


    // $ANTLR start "rule__Aggregate__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1532:1: rule__Aggregate__Group__0 : rule__Aggregate__Group__0__Impl rule__Aggregate__Group__1 ;
    public final void rule__Aggregate__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1536:1: ( rule__Aggregate__Group__0__Impl rule__Aggregate__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1537:2: rule__Aggregate__Group__0__Impl rule__Aggregate__Group__1
            {
            pushFollow(FOLLOW_rule__Aggregate__Group__0__Impl_in_rule__Aggregate__Group__03179);
            rule__Aggregate__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregate__Group__1_in_rule__Aggregate__Group__03182);
            rule__Aggregate__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregate__Group__0"


    // $ANTLR start "rule__Aggregate__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1544:1: rule__Aggregate__Group__0__Impl : ( 'AGGREGATE(' ) ;
    public final void rule__Aggregate__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1548:1: ( ( 'AGGREGATE(' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1549:1: ( 'AGGREGATE(' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1549:1: ( 'AGGREGATE(' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1550:1: 'AGGREGATE('
            {
             before(grammarAccess.getAggregateAccess().getAGGREGATEKeyword_0()); 
            match(input,30,FOLLOW_30_in_rule__Aggregate__Group__0__Impl3210); 
             after(grammarAccess.getAggregateAccess().getAGGREGATEKeyword_0()); 

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
    // $ANTLR end "rule__Aggregate__Group__0__Impl"


    // $ANTLR start "rule__Aggregate__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1563:1: rule__Aggregate__Group__1 : rule__Aggregate__Group__1__Impl rule__Aggregate__Group__2 ;
    public final void rule__Aggregate__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1567:1: ( rule__Aggregate__Group__1__Impl rule__Aggregate__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1568:2: rule__Aggregate__Group__1__Impl rule__Aggregate__Group__2
            {
            pushFollow(FOLLOW_rule__Aggregate__Group__1__Impl_in_rule__Aggregate__Group__13241);
            rule__Aggregate__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregate__Group__2_in_rule__Aggregate__Group__13244);
            rule__Aggregate__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregate__Group__1"


    // $ANTLR start "rule__Aggregate__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1575:1: rule__Aggregate__Group__1__Impl : ( ( rule__Aggregate__Group_1__0 )? ) ;
    public final void rule__Aggregate__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1579:1: ( ( ( rule__Aggregate__Group_1__0 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1580:1: ( ( rule__Aggregate__Group_1__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1580:1: ( ( rule__Aggregate__Group_1__0 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1581:1: ( rule__Aggregate__Group_1__0 )?
            {
             before(grammarAccess.getAggregateAccess().getGroup_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1582:1: ( rule__Aggregate__Group_1__0 )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==32) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1582:2: rule__Aggregate__Group_1__0
                    {
                    pushFollow(FOLLOW_rule__Aggregate__Group_1__0_in_rule__Aggregate__Group__1__Impl3271);
                    rule__Aggregate__Group_1__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getAggregateAccess().getGroup_1()); 

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
    // $ANTLR end "rule__Aggregate__Group__1__Impl"


    // $ANTLR start "rule__Aggregate__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1592:1: rule__Aggregate__Group__2 : rule__Aggregate__Group__2__Impl rule__Aggregate__Group__3 ;
    public final void rule__Aggregate__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1596:1: ( rule__Aggregate__Group__2__Impl rule__Aggregate__Group__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1597:2: rule__Aggregate__Group__2__Impl rule__Aggregate__Group__3
            {
            pushFollow(FOLLOW_rule__Aggregate__Group__2__Impl_in_rule__Aggregate__Group__23302);
            rule__Aggregate__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregate__Group__3_in_rule__Aggregate__Group__23305);
            rule__Aggregate__Group__3();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregate__Group__2"


    // $ANTLR start "rule__Aggregate__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1604:1: rule__Aggregate__Group__2__Impl : ( ( rule__Aggregate__Group_2__0 )? ) ;
    public final void rule__Aggregate__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1608:1: ( ( ( rule__Aggregate__Group_2__0 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1609:1: ( ( rule__Aggregate__Group_2__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1609:1: ( ( rule__Aggregate__Group_2__0 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1610:1: ( rule__Aggregate__Group_2__0 )?
            {
             before(grammarAccess.getAggregateAccess().getGroup_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1611:1: ( rule__Aggregate__Group_2__0 )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( ((LA17_0>=34 && LA17_0<=35)) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1611:2: rule__Aggregate__Group_2__0
                    {
                    pushFollow(FOLLOW_rule__Aggregate__Group_2__0_in_rule__Aggregate__Group__2__Impl3332);
                    rule__Aggregate__Group_2__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getAggregateAccess().getGroup_2()); 

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
    // $ANTLR end "rule__Aggregate__Group__2__Impl"


    // $ANTLR start "rule__Aggregate__Group__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1621:1: rule__Aggregate__Group__3 : rule__Aggregate__Group__3__Impl ;
    public final void rule__Aggregate__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1625:1: ( rule__Aggregate__Group__3__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1626:2: rule__Aggregate__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__Aggregate__Group__3__Impl_in_rule__Aggregate__Group__33363);
            rule__Aggregate__Group__3__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregate__Group__3"


    // $ANTLR start "rule__Aggregate__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1632:1: rule__Aggregate__Group__3__Impl : ( ')' ) ;
    public final void rule__Aggregate__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1636:1: ( ( ')' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1637:1: ( ')' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1637:1: ( ')' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1638:1: ')'
            {
             before(grammarAccess.getAggregateAccess().getRightParenthesisKeyword_3()); 
            match(input,31,FOLLOW_31_in_rule__Aggregate__Group__3__Impl3391); 
             after(grammarAccess.getAggregateAccess().getRightParenthesisKeyword_3()); 

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
    // $ANTLR end "rule__Aggregate__Group__3__Impl"


    // $ANTLR start "rule__Aggregate__Group_1__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1659:1: rule__Aggregate__Group_1__0 : rule__Aggregate__Group_1__0__Impl rule__Aggregate__Group_1__1 ;
    public final void rule__Aggregate__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1663:1: ( rule__Aggregate__Group_1__0__Impl rule__Aggregate__Group_1__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1664:2: rule__Aggregate__Group_1__0__Impl rule__Aggregate__Group_1__1
            {
            pushFollow(FOLLOW_rule__Aggregate__Group_1__0__Impl_in_rule__Aggregate__Group_1__03430);
            rule__Aggregate__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregate__Group_1__1_in_rule__Aggregate__Group_1__03433);
            rule__Aggregate__Group_1__1();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregate__Group_1__0"


    // $ANTLR start "rule__Aggregate__Group_1__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1671:1: rule__Aggregate__Group_1__0__Impl : ( 'aggregations = [' ) ;
    public final void rule__Aggregate__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1675:1: ( ( 'aggregations = [' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1676:1: ( 'aggregations = [' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1676:1: ( 'aggregations = [' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1677:1: 'aggregations = ['
            {
             before(grammarAccess.getAggregateAccess().getAggregationsKeyword_1_0()); 
            match(input,32,FOLLOW_32_in_rule__Aggregate__Group_1__0__Impl3461); 
             after(grammarAccess.getAggregateAccess().getAggregationsKeyword_1_0()); 

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
    // $ANTLR end "rule__Aggregate__Group_1__0__Impl"


    // $ANTLR start "rule__Aggregate__Group_1__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1690:1: rule__Aggregate__Group_1__1 : rule__Aggregate__Group_1__1__Impl rule__Aggregate__Group_1__2 ;
    public final void rule__Aggregate__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1694:1: ( rule__Aggregate__Group_1__1__Impl rule__Aggregate__Group_1__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1695:2: rule__Aggregate__Group_1__1__Impl rule__Aggregate__Group_1__2
            {
            pushFollow(FOLLOW_rule__Aggregate__Group_1__1__Impl_in_rule__Aggregate__Group_1__13492);
            rule__Aggregate__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregate__Group_1__2_in_rule__Aggregate__Group_1__13495);
            rule__Aggregate__Group_1__2();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregate__Group_1__1"


    // $ANTLR start "rule__Aggregate__Group_1__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1702:1: rule__Aggregate__Group_1__1__Impl : ( ( rule__Aggregate__AggregationsAssignment_1_1 )* ) ;
    public final void rule__Aggregate__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1706:1: ( ( ( rule__Aggregate__AggregationsAssignment_1_1 )* ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1707:1: ( ( rule__Aggregate__AggregationsAssignment_1_1 )* )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1707:1: ( ( rule__Aggregate__AggregationsAssignment_1_1 )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1708:1: ( rule__Aggregate__AggregationsAssignment_1_1 )*
            {
             before(grammarAccess.getAggregateAccess().getAggregationsAssignment_1_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1709:1: ( rule__Aggregate__AggregationsAssignment_1_1 )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==36) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1709:2: rule__Aggregate__AggregationsAssignment_1_1
            	    {
            	    pushFollow(FOLLOW_rule__Aggregate__AggregationsAssignment_1_1_in_rule__Aggregate__Group_1__1__Impl3522);
            	    rule__Aggregate__AggregationsAssignment_1_1();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

             after(grammarAccess.getAggregateAccess().getAggregationsAssignment_1_1()); 

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
    // $ANTLR end "rule__Aggregate__Group_1__1__Impl"


    // $ANTLR start "rule__Aggregate__Group_1__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1719:1: rule__Aggregate__Group_1__2 : rule__Aggregate__Group_1__2__Impl ;
    public final void rule__Aggregate__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1723:1: ( rule__Aggregate__Group_1__2__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1724:2: rule__Aggregate__Group_1__2__Impl
            {
            pushFollow(FOLLOW_rule__Aggregate__Group_1__2__Impl_in_rule__Aggregate__Group_1__23553);
            rule__Aggregate__Group_1__2__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregate__Group_1__2"


    // $ANTLR start "rule__Aggregate__Group_1__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1730:1: rule__Aggregate__Group_1__2__Impl : ( ']' ) ;
    public final void rule__Aggregate__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1734:1: ( ( ']' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1735:1: ( ']' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1735:1: ( ']' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1736:1: ']'
            {
             before(grammarAccess.getAggregateAccess().getRightSquareBracketKeyword_1_2()); 
            match(input,33,FOLLOW_33_in_rule__Aggregate__Group_1__2__Impl3581); 
             after(grammarAccess.getAggregateAccess().getRightSquareBracketKeyword_1_2()); 

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
    // $ANTLR end "rule__Aggregate__Group_1__2__Impl"


    // $ANTLR start "rule__Aggregate__Group_2__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1755:1: rule__Aggregate__Group_2__0 : rule__Aggregate__Group_2__0__Impl rule__Aggregate__Group_2__1 ;
    public final void rule__Aggregate__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1759:1: ( rule__Aggregate__Group_2__0__Impl rule__Aggregate__Group_2__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1760:2: rule__Aggregate__Group_2__0__Impl rule__Aggregate__Group_2__1
            {
            pushFollow(FOLLOW_rule__Aggregate__Group_2__0__Impl_in_rule__Aggregate__Group_2__03618);
            rule__Aggregate__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregate__Group_2__1_in_rule__Aggregate__Group_2__03621);
            rule__Aggregate__Group_2__1();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregate__Group_2__0"


    // $ANTLR start "rule__Aggregate__Group_2__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1767:1: rule__Aggregate__Group_2__0__Impl : ( ( ',' )? ) ;
    public final void rule__Aggregate__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1771:1: ( ( ( ',' )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1772:1: ( ( ',' )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1772:1: ( ( ',' )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1773:1: ( ',' )?
            {
             before(grammarAccess.getAggregateAccess().getCommaKeyword_2_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1774:1: ( ',' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==34) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1775:2: ','
                    {
                    match(input,34,FOLLOW_34_in_rule__Aggregate__Group_2__0__Impl3650); 

                    }
                    break;

            }

             after(grammarAccess.getAggregateAccess().getCommaKeyword_2_0()); 

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
    // $ANTLR end "rule__Aggregate__Group_2__0__Impl"


    // $ANTLR start "rule__Aggregate__Group_2__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1786:1: rule__Aggregate__Group_2__1 : rule__Aggregate__Group_2__1__Impl ;
    public final void rule__Aggregate__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1790:1: ( rule__Aggregate__Group_2__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1791:2: rule__Aggregate__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__Aggregate__Group_2__1__Impl_in_rule__Aggregate__Group_2__13683);
            rule__Aggregate__Group_2__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregate__Group_2__1"


    // $ANTLR start "rule__Aggregate__Group_2__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1797:1: rule__Aggregate__Group_2__1__Impl : ( ( rule__Aggregate__GroupbyAssignment_2_1 ) ) ;
    public final void rule__Aggregate__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1801:1: ( ( ( rule__Aggregate__GroupbyAssignment_2_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1802:1: ( ( rule__Aggregate__GroupbyAssignment_2_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1802:1: ( ( rule__Aggregate__GroupbyAssignment_2_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1803:1: ( rule__Aggregate__GroupbyAssignment_2_1 )
            {
             before(grammarAccess.getAggregateAccess().getGroupbyAssignment_2_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1804:1: ( rule__Aggregate__GroupbyAssignment_2_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1804:2: rule__Aggregate__GroupbyAssignment_2_1
            {
            pushFollow(FOLLOW_rule__Aggregate__GroupbyAssignment_2_1_in_rule__Aggregate__Group_2__1__Impl3710);
            rule__Aggregate__GroupbyAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getAggregateAccess().getGroupbyAssignment_2_1()); 

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
    // $ANTLR end "rule__Aggregate__Group_2__1__Impl"


    // $ANTLR start "rule__GroupBy__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1818:1: rule__GroupBy__Group__0 : rule__GroupBy__Group__0__Impl rule__GroupBy__Group__1 ;
    public final void rule__GroupBy__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1822:1: ( rule__GroupBy__Group__0__Impl rule__GroupBy__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1823:2: rule__GroupBy__Group__0__Impl rule__GroupBy__Group__1
            {
            pushFollow(FOLLOW_rule__GroupBy__Group__0__Impl_in_rule__GroupBy__Group__03744);
            rule__GroupBy__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__GroupBy__Group__1_in_rule__GroupBy__Group__03747);
            rule__GroupBy__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__GroupBy__Group__0"


    // $ANTLR start "rule__GroupBy__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1830:1: rule__GroupBy__Group__0__Impl : ( 'group_by=[' ) ;
    public final void rule__GroupBy__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1834:1: ( ( 'group_by=[' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1835:1: ( 'group_by=[' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1835:1: ( 'group_by=[' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1836:1: 'group_by=['
            {
             before(grammarAccess.getGroupByAccess().getGroup_byKeyword_0()); 
            match(input,35,FOLLOW_35_in_rule__GroupBy__Group__0__Impl3775); 
             after(grammarAccess.getGroupByAccess().getGroup_byKeyword_0()); 

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
    // $ANTLR end "rule__GroupBy__Group__0__Impl"


    // $ANTLR start "rule__GroupBy__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1849:1: rule__GroupBy__Group__1 : rule__GroupBy__Group__1__Impl rule__GroupBy__Group__2 ;
    public final void rule__GroupBy__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1853:1: ( rule__GroupBy__Group__1__Impl rule__GroupBy__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1854:2: rule__GroupBy__Group__1__Impl rule__GroupBy__Group__2
            {
            pushFollow(FOLLOW_rule__GroupBy__Group__1__Impl_in_rule__GroupBy__Group__13806);
            rule__GroupBy__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__GroupBy__Group__2_in_rule__GroupBy__Group__13809);
            rule__GroupBy__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__GroupBy__Group__1"


    // $ANTLR start "rule__GroupBy__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1861:1: rule__GroupBy__Group__1__Impl : ( ( rule__GroupBy__VariablesAssignment_1 ) ) ;
    public final void rule__GroupBy__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1865:1: ( ( ( rule__GroupBy__VariablesAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1866:1: ( ( rule__GroupBy__VariablesAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1866:1: ( ( rule__GroupBy__VariablesAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1867:1: ( rule__GroupBy__VariablesAssignment_1 )
            {
             before(grammarAccess.getGroupByAccess().getVariablesAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1868:1: ( rule__GroupBy__VariablesAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1868:2: rule__GroupBy__VariablesAssignment_1
            {
            pushFollow(FOLLOW_rule__GroupBy__VariablesAssignment_1_in_rule__GroupBy__Group__1__Impl3836);
            rule__GroupBy__VariablesAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getGroupByAccess().getVariablesAssignment_1()); 

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
    // $ANTLR end "rule__GroupBy__Group__1__Impl"


    // $ANTLR start "rule__GroupBy__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1878:1: rule__GroupBy__Group__2 : rule__GroupBy__Group__2__Impl rule__GroupBy__Group__3 ;
    public final void rule__GroupBy__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1882:1: ( rule__GroupBy__Group__2__Impl rule__GroupBy__Group__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1883:2: rule__GroupBy__Group__2__Impl rule__GroupBy__Group__3
            {
            pushFollow(FOLLOW_rule__GroupBy__Group__2__Impl_in_rule__GroupBy__Group__23866);
            rule__GroupBy__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__GroupBy__Group__3_in_rule__GroupBy__Group__23869);
            rule__GroupBy__Group__3();

            state._fsp--;


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
    // $ANTLR end "rule__GroupBy__Group__2"


    // $ANTLR start "rule__GroupBy__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1890:1: rule__GroupBy__Group__2__Impl : ( ( rule__GroupBy__Group_2__0 )* ) ;
    public final void rule__GroupBy__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1894:1: ( ( ( rule__GroupBy__Group_2__0 )* ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1895:1: ( ( rule__GroupBy__Group_2__0 )* )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1895:1: ( ( rule__GroupBy__Group_2__0 )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1896:1: ( rule__GroupBy__Group_2__0 )*
            {
             before(grammarAccess.getGroupByAccess().getGroup_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1897:1: ( rule__GroupBy__Group_2__0 )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==34) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1897:2: rule__GroupBy__Group_2__0
            	    {
            	    pushFollow(FOLLOW_rule__GroupBy__Group_2__0_in_rule__GroupBy__Group__2__Impl3896);
            	    rule__GroupBy__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

             after(grammarAccess.getGroupByAccess().getGroup_2()); 

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
    // $ANTLR end "rule__GroupBy__Group__2__Impl"


    // $ANTLR start "rule__GroupBy__Group__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1907:1: rule__GroupBy__Group__3 : rule__GroupBy__Group__3__Impl ;
    public final void rule__GroupBy__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1911:1: ( rule__GroupBy__Group__3__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1912:2: rule__GroupBy__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__GroupBy__Group__3__Impl_in_rule__GroupBy__Group__33927);
            rule__GroupBy__Group__3__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__GroupBy__Group__3"


    // $ANTLR start "rule__GroupBy__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1918:1: rule__GroupBy__Group__3__Impl : ( ']' ) ;
    public final void rule__GroupBy__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1922:1: ( ( ']' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1923:1: ( ']' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1923:1: ( ']' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1924:1: ']'
            {
             before(grammarAccess.getGroupByAccess().getRightSquareBracketKeyword_3()); 
            match(input,33,FOLLOW_33_in_rule__GroupBy__Group__3__Impl3955); 
             after(grammarAccess.getGroupByAccess().getRightSquareBracketKeyword_3()); 

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
    // $ANTLR end "rule__GroupBy__Group__3__Impl"


    // $ANTLR start "rule__GroupBy__Group_2__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1945:1: rule__GroupBy__Group_2__0 : rule__GroupBy__Group_2__0__Impl rule__GroupBy__Group_2__1 ;
    public final void rule__GroupBy__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1949:1: ( rule__GroupBy__Group_2__0__Impl rule__GroupBy__Group_2__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1950:2: rule__GroupBy__Group_2__0__Impl rule__GroupBy__Group_2__1
            {
            pushFollow(FOLLOW_rule__GroupBy__Group_2__0__Impl_in_rule__GroupBy__Group_2__03994);
            rule__GroupBy__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__GroupBy__Group_2__1_in_rule__GroupBy__Group_2__03997);
            rule__GroupBy__Group_2__1();

            state._fsp--;


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
    // $ANTLR end "rule__GroupBy__Group_2__0"


    // $ANTLR start "rule__GroupBy__Group_2__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1957:1: rule__GroupBy__Group_2__0__Impl : ( ',' ) ;
    public final void rule__GroupBy__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1961:1: ( ( ',' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1962:1: ( ',' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1962:1: ( ',' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1963:1: ','
            {
             before(grammarAccess.getGroupByAccess().getCommaKeyword_2_0()); 
            match(input,34,FOLLOW_34_in_rule__GroupBy__Group_2__0__Impl4025); 
             after(grammarAccess.getGroupByAccess().getCommaKeyword_2_0()); 

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
    // $ANTLR end "rule__GroupBy__Group_2__0__Impl"


    // $ANTLR start "rule__GroupBy__Group_2__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1976:1: rule__GroupBy__Group_2__1 : rule__GroupBy__Group_2__1__Impl ;
    public final void rule__GroupBy__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1980:1: ( rule__GroupBy__Group_2__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1981:2: rule__GroupBy__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__GroupBy__Group_2__1__Impl_in_rule__GroupBy__Group_2__14056);
            rule__GroupBy__Group_2__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__GroupBy__Group_2__1"


    // $ANTLR start "rule__GroupBy__Group_2__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1987:1: rule__GroupBy__Group_2__1__Impl : ( ( rule__GroupBy__VariablesAssignment_2_1 ) ) ;
    public final void rule__GroupBy__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1991:1: ( ( ( rule__GroupBy__VariablesAssignment_2_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1992:1: ( ( rule__GroupBy__VariablesAssignment_2_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1992:1: ( ( rule__GroupBy__VariablesAssignment_2_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1993:1: ( rule__GroupBy__VariablesAssignment_2_1 )
            {
             before(grammarAccess.getGroupByAccess().getVariablesAssignment_2_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1994:1: ( rule__GroupBy__VariablesAssignment_2_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:1994:2: rule__GroupBy__VariablesAssignment_2_1
            {
            pushFollow(FOLLOW_rule__GroupBy__VariablesAssignment_2_1_in_rule__GroupBy__Group_2__1__Impl4083);
            rule__GroupBy__VariablesAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getGroupByAccess().getVariablesAssignment_2_1()); 

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
    // $ANTLR end "rule__GroupBy__Group_2__1__Impl"


    // $ANTLR start "rule__Aggregation__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2008:1: rule__Aggregation__Group__0 : rule__Aggregation__Group__0__Impl rule__Aggregation__Group__1 ;
    public final void rule__Aggregation__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2012:1: ( rule__Aggregation__Group__0__Impl rule__Aggregation__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2013:2: rule__Aggregation__Group__0__Impl rule__Aggregation__Group__1
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__0__Impl_in_rule__Aggregation__Group__04117);
            rule__Aggregation__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregation__Group__1_in_rule__Aggregation__Group__04120);
            rule__Aggregation__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group__0"


    // $ANTLR start "rule__Aggregation__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2020:1: rule__Aggregation__Group__0__Impl : ( '[' ) ;
    public final void rule__Aggregation__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2024:1: ( ( '[' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2025:1: ( '[' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2025:1: ( '[' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2026:1: '['
            {
             before(grammarAccess.getAggregationAccess().getLeftSquareBracketKeyword_0()); 
            match(input,36,FOLLOW_36_in_rule__Aggregation__Group__0__Impl4148); 
             after(grammarAccess.getAggregationAccess().getLeftSquareBracketKeyword_0()); 

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
    // $ANTLR end "rule__Aggregation__Group__0__Impl"


    // $ANTLR start "rule__Aggregation__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2039:1: rule__Aggregation__Group__1 : rule__Aggregation__Group__1__Impl rule__Aggregation__Group__2 ;
    public final void rule__Aggregation__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2043:1: ( rule__Aggregation__Group__1__Impl rule__Aggregation__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2044:2: rule__Aggregation__Group__1__Impl rule__Aggregation__Group__2
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__1__Impl_in_rule__Aggregation__Group__14179);
            rule__Aggregation__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregation__Group__2_in_rule__Aggregation__Group__14182);
            rule__Aggregation__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group__1"


    // $ANTLR start "rule__Aggregation__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2051:1: rule__Aggregation__Group__1__Impl : ( ( rule__Aggregation__FunctionAssignment_1 ) ) ;
    public final void rule__Aggregation__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2055:1: ( ( ( rule__Aggregation__FunctionAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2056:1: ( ( rule__Aggregation__FunctionAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2056:1: ( ( rule__Aggregation__FunctionAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2057:1: ( rule__Aggregation__FunctionAssignment_1 )
            {
             before(grammarAccess.getAggregationAccess().getFunctionAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2058:1: ( rule__Aggregation__FunctionAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2058:2: rule__Aggregation__FunctionAssignment_1
            {
            pushFollow(FOLLOW_rule__Aggregation__FunctionAssignment_1_in_rule__Aggregation__Group__1__Impl4209);
            rule__Aggregation__FunctionAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getAggregationAccess().getFunctionAssignment_1()); 

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
    // $ANTLR end "rule__Aggregation__Group__1__Impl"


    // $ANTLR start "rule__Aggregation__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2068:1: rule__Aggregation__Group__2 : rule__Aggregation__Group__2__Impl rule__Aggregation__Group__3 ;
    public final void rule__Aggregation__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2072:1: ( rule__Aggregation__Group__2__Impl rule__Aggregation__Group__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2073:2: rule__Aggregation__Group__2__Impl rule__Aggregation__Group__3
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__2__Impl_in_rule__Aggregation__Group__24239);
            rule__Aggregation__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregation__Group__3_in_rule__Aggregation__Group__24242);
            rule__Aggregation__Group__3();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group__2"


    // $ANTLR start "rule__Aggregation__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2080:1: rule__Aggregation__Group__2__Impl : ( ',' ) ;
    public final void rule__Aggregation__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2084:1: ( ( ',' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2085:1: ( ',' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2085:1: ( ',' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2086:1: ','
            {
             before(grammarAccess.getAggregationAccess().getCommaKeyword_2()); 
            match(input,34,FOLLOW_34_in_rule__Aggregation__Group__2__Impl4270); 
             after(grammarAccess.getAggregationAccess().getCommaKeyword_2()); 

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
    // $ANTLR end "rule__Aggregation__Group__2__Impl"


    // $ANTLR start "rule__Aggregation__Group__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2099:1: rule__Aggregation__Group__3 : rule__Aggregation__Group__3__Impl rule__Aggregation__Group__4 ;
    public final void rule__Aggregation__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2103:1: ( rule__Aggregation__Group__3__Impl rule__Aggregation__Group__4 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2104:2: rule__Aggregation__Group__3__Impl rule__Aggregation__Group__4
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__3__Impl_in_rule__Aggregation__Group__34301);
            rule__Aggregation__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregation__Group__4_in_rule__Aggregation__Group__34304);
            rule__Aggregation__Group__4();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group__3"


    // $ANTLR start "rule__Aggregation__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2111:1: rule__Aggregation__Group__3__Impl : ( ( rule__Aggregation__VarToAggAssignment_3 ) ) ;
    public final void rule__Aggregation__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2115:1: ( ( ( rule__Aggregation__VarToAggAssignment_3 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2116:1: ( ( rule__Aggregation__VarToAggAssignment_3 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2116:1: ( ( rule__Aggregation__VarToAggAssignment_3 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2117:1: ( rule__Aggregation__VarToAggAssignment_3 )
            {
             before(grammarAccess.getAggregationAccess().getVarToAggAssignment_3()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2118:1: ( rule__Aggregation__VarToAggAssignment_3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2118:2: rule__Aggregation__VarToAggAssignment_3
            {
            pushFollow(FOLLOW_rule__Aggregation__VarToAggAssignment_3_in_rule__Aggregation__Group__3__Impl4331);
            rule__Aggregation__VarToAggAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getAggregationAccess().getVarToAggAssignment_3()); 

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
    // $ANTLR end "rule__Aggregation__Group__3__Impl"


    // $ANTLR start "rule__Aggregation__Group__4"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2128:1: rule__Aggregation__Group__4 : rule__Aggregation__Group__4__Impl rule__Aggregation__Group__5 ;
    public final void rule__Aggregation__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2132:1: ( rule__Aggregation__Group__4__Impl rule__Aggregation__Group__5 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2133:2: rule__Aggregation__Group__4__Impl rule__Aggregation__Group__5
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__4__Impl_in_rule__Aggregation__Group__44361);
            rule__Aggregation__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregation__Group__5_in_rule__Aggregation__Group__44364);
            rule__Aggregation__Group__5();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group__4"


    // $ANTLR start "rule__Aggregation__Group__4__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2140:1: rule__Aggregation__Group__4__Impl : ( ',' ) ;
    public final void rule__Aggregation__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2144:1: ( ( ',' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2145:1: ( ',' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2145:1: ( ',' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2146:1: ','
            {
             before(grammarAccess.getAggregationAccess().getCommaKeyword_4()); 
            match(input,34,FOLLOW_34_in_rule__Aggregation__Group__4__Impl4392); 
             after(grammarAccess.getAggregationAccess().getCommaKeyword_4()); 

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
    // $ANTLR end "rule__Aggregation__Group__4__Impl"


    // $ANTLR start "rule__Aggregation__Group__5"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2159:1: rule__Aggregation__Group__5 : rule__Aggregation__Group__5__Impl rule__Aggregation__Group__6 ;
    public final void rule__Aggregation__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2163:1: ( rule__Aggregation__Group__5__Impl rule__Aggregation__Group__6 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2164:2: rule__Aggregation__Group__5__Impl rule__Aggregation__Group__6
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__5__Impl_in_rule__Aggregation__Group__54423);
            rule__Aggregation__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregation__Group__6_in_rule__Aggregation__Group__54426);
            rule__Aggregation__Group__6();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group__5"


    // $ANTLR start "rule__Aggregation__Group__5__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2171:1: rule__Aggregation__Group__5__Impl : ( ( rule__Aggregation__AggNameAssignment_5 ) ) ;
    public final void rule__Aggregation__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2175:1: ( ( ( rule__Aggregation__AggNameAssignment_5 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2176:1: ( ( rule__Aggregation__AggNameAssignment_5 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2176:1: ( ( rule__Aggregation__AggNameAssignment_5 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2177:1: ( rule__Aggregation__AggNameAssignment_5 )
            {
             before(grammarAccess.getAggregationAccess().getAggNameAssignment_5()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2178:1: ( rule__Aggregation__AggNameAssignment_5 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2178:2: rule__Aggregation__AggNameAssignment_5
            {
            pushFollow(FOLLOW_rule__Aggregation__AggNameAssignment_5_in_rule__Aggregation__Group__5__Impl4453);
            rule__Aggregation__AggNameAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getAggregationAccess().getAggNameAssignment_5()); 

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
    // $ANTLR end "rule__Aggregation__Group__5__Impl"


    // $ANTLR start "rule__Aggregation__Group__6"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2188:1: rule__Aggregation__Group__6 : rule__Aggregation__Group__6__Impl rule__Aggregation__Group__7 ;
    public final void rule__Aggregation__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2192:1: ( rule__Aggregation__Group__6__Impl rule__Aggregation__Group__7 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2193:2: rule__Aggregation__Group__6__Impl rule__Aggregation__Group__7
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__6__Impl_in_rule__Aggregation__Group__64483);
            rule__Aggregation__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregation__Group__7_in_rule__Aggregation__Group__64486);
            rule__Aggregation__Group__7();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group__6"


    // $ANTLR start "rule__Aggregation__Group__6__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2200:1: rule__Aggregation__Group__6__Impl : ( ( rule__Aggregation__Group_6__0 )? ) ;
    public final void rule__Aggregation__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2204:1: ( ( ( rule__Aggregation__Group_6__0 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2205:1: ( ( rule__Aggregation__Group_6__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2205:1: ( ( rule__Aggregation__Group_6__0 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2206:1: ( rule__Aggregation__Group_6__0 )?
            {
             before(grammarAccess.getAggregationAccess().getGroup_6()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2207:1: ( rule__Aggregation__Group_6__0 )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==34) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2207:2: rule__Aggregation__Group_6__0
                    {
                    pushFollow(FOLLOW_rule__Aggregation__Group_6__0_in_rule__Aggregation__Group__6__Impl4513);
                    rule__Aggregation__Group_6__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getAggregationAccess().getGroup_6()); 

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
    // $ANTLR end "rule__Aggregation__Group__6__Impl"


    // $ANTLR start "rule__Aggregation__Group__7"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2217:1: rule__Aggregation__Group__7 : rule__Aggregation__Group__7__Impl rule__Aggregation__Group__8 ;
    public final void rule__Aggregation__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2221:1: ( rule__Aggregation__Group__7__Impl rule__Aggregation__Group__8 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2222:2: rule__Aggregation__Group__7__Impl rule__Aggregation__Group__8
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__7__Impl_in_rule__Aggregation__Group__74544);
            rule__Aggregation__Group__7__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregation__Group__8_in_rule__Aggregation__Group__74547);
            rule__Aggregation__Group__8();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group__7"


    // $ANTLR start "rule__Aggregation__Group__7__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2229:1: rule__Aggregation__Group__7__Impl : ( ']' ) ;
    public final void rule__Aggregation__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2233:1: ( ( ']' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2234:1: ( ']' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2234:1: ( ']' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2235:1: ']'
            {
             before(grammarAccess.getAggregationAccess().getRightSquareBracketKeyword_7()); 
            match(input,33,FOLLOW_33_in_rule__Aggregation__Group__7__Impl4575); 
             after(grammarAccess.getAggregationAccess().getRightSquareBracketKeyword_7()); 

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
    // $ANTLR end "rule__Aggregation__Group__7__Impl"


    // $ANTLR start "rule__Aggregation__Group__8"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2248:1: rule__Aggregation__Group__8 : rule__Aggregation__Group__8__Impl ;
    public final void rule__Aggregation__Group__8() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2252:1: ( rule__Aggregation__Group__8__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2253:2: rule__Aggregation__Group__8__Impl
            {
            pushFollow(FOLLOW_rule__Aggregation__Group__8__Impl_in_rule__Aggregation__Group__84606);
            rule__Aggregation__Group__8__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group__8"


    // $ANTLR start "rule__Aggregation__Group__8__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2259:1: rule__Aggregation__Group__8__Impl : ( ( ',' )? ) ;
    public final void rule__Aggregation__Group__8__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2263:1: ( ( ( ',' )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2264:1: ( ( ',' )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2264:1: ( ( ',' )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2265:1: ( ',' )?
            {
             before(grammarAccess.getAggregationAccess().getCommaKeyword_8()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2266:1: ( ',' )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==34) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2267:2: ','
                    {
                    match(input,34,FOLLOW_34_in_rule__Aggregation__Group__8__Impl4635); 

                    }
                    break;

            }

             after(grammarAccess.getAggregationAccess().getCommaKeyword_8()); 

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
    // $ANTLR end "rule__Aggregation__Group__8__Impl"


    // $ANTLR start "rule__Aggregation__Group_6__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2296:1: rule__Aggregation__Group_6__0 : rule__Aggregation__Group_6__0__Impl rule__Aggregation__Group_6__1 ;
    public final void rule__Aggregation__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2300:1: ( rule__Aggregation__Group_6__0__Impl rule__Aggregation__Group_6__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2301:2: rule__Aggregation__Group_6__0__Impl rule__Aggregation__Group_6__1
            {
            pushFollow(FOLLOW_rule__Aggregation__Group_6__0__Impl_in_rule__Aggregation__Group_6__04686);
            rule__Aggregation__Group_6__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Aggregation__Group_6__1_in_rule__Aggregation__Group_6__04689);
            rule__Aggregation__Group_6__1();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group_6__0"


    // $ANTLR start "rule__Aggregation__Group_6__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2308:1: rule__Aggregation__Group_6__0__Impl : ( ',' ) ;
    public final void rule__Aggregation__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2312:1: ( ( ',' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2313:1: ( ',' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2313:1: ( ',' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2314:1: ','
            {
             before(grammarAccess.getAggregationAccess().getCommaKeyword_6_0()); 
            match(input,34,FOLLOW_34_in_rule__Aggregation__Group_6__0__Impl4717); 
             after(grammarAccess.getAggregationAccess().getCommaKeyword_6_0()); 

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
    // $ANTLR end "rule__Aggregation__Group_6__0__Impl"


    // $ANTLR start "rule__Aggregation__Group_6__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2327:1: rule__Aggregation__Group_6__1 : rule__Aggregation__Group_6__1__Impl ;
    public final void rule__Aggregation__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2331:1: ( rule__Aggregation__Group_6__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2332:2: rule__Aggregation__Group_6__1__Impl
            {
            pushFollow(FOLLOW_rule__Aggregation__Group_6__1__Impl_in_rule__Aggregation__Group_6__14748);
            rule__Aggregation__Group_6__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Aggregation__Group_6__1"


    // $ANTLR start "rule__Aggregation__Group_6__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2338:1: rule__Aggregation__Group_6__1__Impl : ( ( rule__Aggregation__DatatypeAssignment_6_1 ) ) ;
    public final void rule__Aggregation__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2342:1: ( ( ( rule__Aggregation__DatatypeAssignment_6_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2343:1: ( ( rule__Aggregation__DatatypeAssignment_6_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2343:1: ( ( rule__Aggregation__DatatypeAssignment_6_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2344:1: ( rule__Aggregation__DatatypeAssignment_6_1 )
            {
             before(grammarAccess.getAggregationAccess().getDatatypeAssignment_6_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2345:1: ( rule__Aggregation__DatatypeAssignment_6_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2345:2: rule__Aggregation__DatatypeAssignment_6_1
            {
            pushFollow(FOLLOW_rule__Aggregation__DatatypeAssignment_6_1_in_rule__Aggregation__Group_6__1__Impl4775);
            rule__Aggregation__DatatypeAssignment_6_1();

            state._fsp--;


            }

             after(grammarAccess.getAggregationAccess().getDatatypeAssignment_6_1()); 

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
    // $ANTLR end "rule__Aggregation__Group_6__1__Impl"


    // $ANTLR start "rule__Filesinkclause__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2359:1: rule__Filesinkclause__Group__0 : rule__Filesinkclause__Group__0__Impl rule__Filesinkclause__Group__1 ;
    public final void rule__Filesinkclause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2363:1: ( rule__Filesinkclause__Group__0__Impl rule__Filesinkclause__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2364:2: rule__Filesinkclause__Group__0__Impl rule__Filesinkclause__Group__1
            {
            pushFollow(FOLLOW_rule__Filesinkclause__Group__0__Impl_in_rule__Filesinkclause__Group__04809);
            rule__Filesinkclause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Filesinkclause__Group__1_in_rule__Filesinkclause__Group__04812);
            rule__Filesinkclause__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__Filesinkclause__Group__0"


    // $ANTLR start "rule__Filesinkclause__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2371:1: rule__Filesinkclause__Group__0__Impl : ( 'CSVFILESINK(' ) ;
    public final void rule__Filesinkclause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2375:1: ( ( 'CSVFILESINK(' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2376:1: ( 'CSVFILESINK(' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2376:1: ( 'CSVFILESINK(' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2377:1: 'CSVFILESINK('
            {
             before(grammarAccess.getFilesinkclauseAccess().getCSVFILESINKKeyword_0()); 
            match(input,37,FOLLOW_37_in_rule__Filesinkclause__Group__0__Impl4840); 
             after(grammarAccess.getFilesinkclauseAccess().getCSVFILESINKKeyword_0()); 

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
    // $ANTLR end "rule__Filesinkclause__Group__0__Impl"


    // $ANTLR start "rule__Filesinkclause__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2390:1: rule__Filesinkclause__Group__1 : rule__Filesinkclause__Group__1__Impl rule__Filesinkclause__Group__2 ;
    public final void rule__Filesinkclause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2394:1: ( rule__Filesinkclause__Group__1__Impl rule__Filesinkclause__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2395:2: rule__Filesinkclause__Group__1__Impl rule__Filesinkclause__Group__2
            {
            pushFollow(FOLLOW_rule__Filesinkclause__Group__1__Impl_in_rule__Filesinkclause__Group__14871);
            rule__Filesinkclause__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Filesinkclause__Group__2_in_rule__Filesinkclause__Group__14874);
            rule__Filesinkclause__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__Filesinkclause__Group__1"


    // $ANTLR start "rule__Filesinkclause__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2402:1: rule__Filesinkclause__Group__1__Impl : ( ( rule__Filesinkclause__PathAssignment_1 ) ) ;
    public final void rule__Filesinkclause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2406:1: ( ( ( rule__Filesinkclause__PathAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2407:1: ( ( rule__Filesinkclause__PathAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2407:1: ( ( rule__Filesinkclause__PathAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2408:1: ( rule__Filesinkclause__PathAssignment_1 )
            {
             before(grammarAccess.getFilesinkclauseAccess().getPathAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2409:1: ( rule__Filesinkclause__PathAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2409:2: rule__Filesinkclause__PathAssignment_1
            {
            pushFollow(FOLLOW_rule__Filesinkclause__PathAssignment_1_in_rule__Filesinkclause__Group__1__Impl4901);
            rule__Filesinkclause__PathAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getFilesinkclauseAccess().getPathAssignment_1()); 

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
    // $ANTLR end "rule__Filesinkclause__Group__1__Impl"


    // $ANTLR start "rule__Filesinkclause__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2419:1: rule__Filesinkclause__Group__2 : rule__Filesinkclause__Group__2__Impl ;
    public final void rule__Filesinkclause__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2423:1: ( rule__Filesinkclause__Group__2__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2424:2: rule__Filesinkclause__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Filesinkclause__Group__2__Impl_in_rule__Filesinkclause__Group__24931);
            rule__Filesinkclause__Group__2__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Filesinkclause__Group__2"


    // $ANTLR start "rule__Filesinkclause__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2430:1: rule__Filesinkclause__Group__2__Impl : ( ')' ) ;
    public final void rule__Filesinkclause__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2434:1: ( ( ')' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2435:1: ( ')' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2435:1: ( ')' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2436:1: ')'
            {
             before(grammarAccess.getFilesinkclauseAccess().getRightParenthesisKeyword_2()); 
            match(input,31,FOLLOW_31_in_rule__Filesinkclause__Group__2__Impl4959); 
             after(grammarAccess.getFilesinkclauseAccess().getRightParenthesisKeyword_2()); 

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
    // $ANTLR end "rule__Filesinkclause__Group__2__Impl"


    // $ANTLR start "rule__Filterclause__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2455:1: rule__Filterclause__Group__0 : rule__Filterclause__Group__0__Impl rule__Filterclause__Group__1 ;
    public final void rule__Filterclause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2459:1: ( rule__Filterclause__Group__0__Impl rule__Filterclause__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2460:2: rule__Filterclause__Group__0__Impl rule__Filterclause__Group__1
            {
            pushFollow(FOLLOW_rule__Filterclause__Group__0__Impl_in_rule__Filterclause__Group__04996);
            rule__Filterclause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Filterclause__Group__1_in_rule__Filterclause__Group__04999);
            rule__Filterclause__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__Filterclause__Group__0"


    // $ANTLR start "rule__Filterclause__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2467:1: rule__Filterclause__Group__0__Impl : ( 'FILTER(' ) ;
    public final void rule__Filterclause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2471:1: ( ( 'FILTER(' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2472:1: ( 'FILTER(' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2472:1: ( 'FILTER(' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2473:1: 'FILTER('
            {
             before(grammarAccess.getFilterclauseAccess().getFILTERKeyword_0()); 
            match(input,38,FOLLOW_38_in_rule__Filterclause__Group__0__Impl5027); 
             after(grammarAccess.getFilterclauseAccess().getFILTERKeyword_0()); 

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
    // $ANTLR end "rule__Filterclause__Group__0__Impl"


    // $ANTLR start "rule__Filterclause__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2486:1: rule__Filterclause__Group__1 : rule__Filterclause__Group__1__Impl rule__Filterclause__Group__2 ;
    public final void rule__Filterclause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2490:1: ( rule__Filterclause__Group__1__Impl rule__Filterclause__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2491:2: rule__Filterclause__Group__1__Impl rule__Filterclause__Group__2
            {
            pushFollow(FOLLOW_rule__Filterclause__Group__1__Impl_in_rule__Filterclause__Group__15058);
            rule__Filterclause__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Filterclause__Group__2_in_rule__Filterclause__Group__15061);
            rule__Filterclause__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__Filterclause__Group__1"


    // $ANTLR start "rule__Filterclause__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2498:1: rule__Filterclause__Group__1__Impl : ( ( rule__Filterclause__LeftAssignment_1 ) ) ;
    public final void rule__Filterclause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2502:1: ( ( ( rule__Filterclause__LeftAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2503:1: ( ( rule__Filterclause__LeftAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2503:1: ( ( rule__Filterclause__LeftAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2504:1: ( rule__Filterclause__LeftAssignment_1 )
            {
             before(grammarAccess.getFilterclauseAccess().getLeftAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2505:1: ( rule__Filterclause__LeftAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2505:2: rule__Filterclause__LeftAssignment_1
            {
            pushFollow(FOLLOW_rule__Filterclause__LeftAssignment_1_in_rule__Filterclause__Group__1__Impl5088);
            rule__Filterclause__LeftAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getFilterclauseAccess().getLeftAssignment_1()); 

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
    // $ANTLR end "rule__Filterclause__Group__1__Impl"


    // $ANTLR start "rule__Filterclause__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2515:1: rule__Filterclause__Group__2 : rule__Filterclause__Group__2__Impl rule__Filterclause__Group__3 ;
    public final void rule__Filterclause__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2519:1: ( rule__Filterclause__Group__2__Impl rule__Filterclause__Group__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2520:2: rule__Filterclause__Group__2__Impl rule__Filterclause__Group__3
            {
            pushFollow(FOLLOW_rule__Filterclause__Group__2__Impl_in_rule__Filterclause__Group__25118);
            rule__Filterclause__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Filterclause__Group__3_in_rule__Filterclause__Group__25121);
            rule__Filterclause__Group__3();

            state._fsp--;


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
    // $ANTLR end "rule__Filterclause__Group__2"


    // $ANTLR start "rule__Filterclause__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2527:1: rule__Filterclause__Group__2__Impl : ( ( rule__Filterclause__OperatorAssignment_2 ) ) ;
    public final void rule__Filterclause__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2531:1: ( ( ( rule__Filterclause__OperatorAssignment_2 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2532:1: ( ( rule__Filterclause__OperatorAssignment_2 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2532:1: ( ( rule__Filterclause__OperatorAssignment_2 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2533:1: ( rule__Filterclause__OperatorAssignment_2 )
            {
             before(grammarAccess.getFilterclauseAccess().getOperatorAssignment_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2534:1: ( rule__Filterclause__OperatorAssignment_2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2534:2: rule__Filterclause__OperatorAssignment_2
            {
            pushFollow(FOLLOW_rule__Filterclause__OperatorAssignment_2_in_rule__Filterclause__Group__2__Impl5148);
            rule__Filterclause__OperatorAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getFilterclauseAccess().getOperatorAssignment_2()); 

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
    // $ANTLR end "rule__Filterclause__Group__2__Impl"


    // $ANTLR start "rule__Filterclause__Group__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2544:1: rule__Filterclause__Group__3 : rule__Filterclause__Group__3__Impl rule__Filterclause__Group__4 ;
    public final void rule__Filterclause__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2548:1: ( rule__Filterclause__Group__3__Impl rule__Filterclause__Group__4 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2549:2: rule__Filterclause__Group__3__Impl rule__Filterclause__Group__4
            {
            pushFollow(FOLLOW_rule__Filterclause__Group__3__Impl_in_rule__Filterclause__Group__35178);
            rule__Filterclause__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Filterclause__Group__4_in_rule__Filterclause__Group__35181);
            rule__Filterclause__Group__4();

            state._fsp--;


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
    // $ANTLR end "rule__Filterclause__Group__3"


    // $ANTLR start "rule__Filterclause__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2556:1: rule__Filterclause__Group__3__Impl : ( ( rule__Filterclause__RightAssignment_3 ) ) ;
    public final void rule__Filterclause__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2560:1: ( ( ( rule__Filterclause__RightAssignment_3 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2561:1: ( ( rule__Filterclause__RightAssignment_3 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2561:1: ( ( rule__Filterclause__RightAssignment_3 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2562:1: ( rule__Filterclause__RightAssignment_3 )
            {
             before(grammarAccess.getFilterclauseAccess().getRightAssignment_3()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2563:1: ( rule__Filterclause__RightAssignment_3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2563:2: rule__Filterclause__RightAssignment_3
            {
            pushFollow(FOLLOW_rule__Filterclause__RightAssignment_3_in_rule__Filterclause__Group__3__Impl5208);
            rule__Filterclause__RightAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getFilterclauseAccess().getRightAssignment_3()); 

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
    // $ANTLR end "rule__Filterclause__Group__3__Impl"


    // $ANTLR start "rule__Filterclause__Group__4"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2573:1: rule__Filterclause__Group__4 : rule__Filterclause__Group__4__Impl ;
    public final void rule__Filterclause__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2577:1: ( rule__Filterclause__Group__4__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2578:2: rule__Filterclause__Group__4__Impl
            {
            pushFollow(FOLLOW_rule__Filterclause__Group__4__Impl_in_rule__Filterclause__Group__45238);
            rule__Filterclause__Group__4__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Filterclause__Group__4"


    // $ANTLR start "rule__Filterclause__Group__4__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2584:1: rule__Filterclause__Group__4__Impl : ( ')' ) ;
    public final void rule__Filterclause__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2588:1: ( ( ')' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2589:1: ( ')' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2589:1: ( ')' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2590:1: ')'
            {
             before(grammarAccess.getFilterclauseAccess().getRightParenthesisKeyword_4()); 
            match(input,31,FOLLOW_31_in_rule__Filterclause__Group__4__Impl5266); 
             after(grammarAccess.getFilterclauseAccess().getRightParenthesisKeyword_4()); 

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
    // $ANTLR end "rule__Filterclause__Group__4__Impl"


    // $ANTLR start "rule__DatasetClause__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2614:1: rule__DatasetClause__Group__0 : rule__DatasetClause__Group__0__Impl rule__DatasetClause__Group__1 ;
    public final void rule__DatasetClause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2618:1: ( rule__DatasetClause__Group__0__Impl rule__DatasetClause__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2619:2: rule__DatasetClause__Group__0__Impl rule__DatasetClause__Group__1
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group__0__Impl_in_rule__DatasetClause__Group__05308);
            rule__DatasetClause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group__1_in_rule__DatasetClause__Group__05311);
            rule__DatasetClause__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group__0"


    // $ANTLR start "rule__DatasetClause__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2626:1: rule__DatasetClause__Group__0__Impl : ( 'FROM' ) ;
    public final void rule__DatasetClause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2630:1: ( ( 'FROM' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2631:1: ( 'FROM' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2631:1: ( 'FROM' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2632:1: 'FROM'
            {
             before(grammarAccess.getDatasetClauseAccess().getFROMKeyword_0()); 
            match(input,39,FOLLOW_39_in_rule__DatasetClause__Group__0__Impl5339); 
             after(grammarAccess.getDatasetClauseAccess().getFROMKeyword_0()); 

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
    // $ANTLR end "rule__DatasetClause__Group__0__Impl"


    // $ANTLR start "rule__DatasetClause__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2645:1: rule__DatasetClause__Group__1 : rule__DatasetClause__Group__1__Impl rule__DatasetClause__Group__2 ;
    public final void rule__DatasetClause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2649:1: ( rule__DatasetClause__Group__1__Impl rule__DatasetClause__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2650:2: rule__DatasetClause__Group__1__Impl rule__DatasetClause__Group__2
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group__1__Impl_in_rule__DatasetClause__Group__15370);
            rule__DatasetClause__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group__2_in_rule__DatasetClause__Group__15373);
            rule__DatasetClause__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group__1"


    // $ANTLR start "rule__DatasetClause__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2657:1: rule__DatasetClause__Group__1__Impl : ( ( rule__DatasetClause__DataSetAssignment_1 ) ) ;
    public final void rule__DatasetClause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2661:1: ( ( ( rule__DatasetClause__DataSetAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2662:1: ( ( rule__DatasetClause__DataSetAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2662:1: ( ( rule__DatasetClause__DataSetAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2663:1: ( rule__DatasetClause__DataSetAssignment_1 )
            {
             before(grammarAccess.getDatasetClauseAccess().getDataSetAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2664:1: ( rule__DatasetClause__DataSetAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2664:2: rule__DatasetClause__DataSetAssignment_1
            {
            pushFollow(FOLLOW_rule__DatasetClause__DataSetAssignment_1_in_rule__DatasetClause__Group__1__Impl5400);
            rule__DatasetClause__DataSetAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getDataSetAssignment_1()); 

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
    // $ANTLR end "rule__DatasetClause__Group__1__Impl"


    // $ANTLR start "rule__DatasetClause__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2674:1: rule__DatasetClause__Group__2 : rule__DatasetClause__Group__2__Impl rule__DatasetClause__Group__3 ;
    public final void rule__DatasetClause__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2678:1: ( rule__DatasetClause__Group__2__Impl rule__DatasetClause__Group__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2679:2: rule__DatasetClause__Group__2__Impl rule__DatasetClause__Group__3
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group__2__Impl_in_rule__DatasetClause__Group__25430);
            rule__DatasetClause__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group__3_in_rule__DatasetClause__Group__25433);
            rule__DatasetClause__Group__3();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group__2"


    // $ANTLR start "rule__DatasetClause__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2686:1: rule__DatasetClause__Group__2__Impl : ( 'AS' ) ;
    public final void rule__DatasetClause__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2690:1: ( ( 'AS' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2691:1: ( 'AS' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2691:1: ( 'AS' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2692:1: 'AS'
            {
             before(grammarAccess.getDatasetClauseAccess().getASKeyword_2()); 
            match(input,40,FOLLOW_40_in_rule__DatasetClause__Group__2__Impl5461); 
             after(grammarAccess.getDatasetClauseAccess().getASKeyword_2()); 

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
    // $ANTLR end "rule__DatasetClause__Group__2__Impl"


    // $ANTLR start "rule__DatasetClause__Group__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2705:1: rule__DatasetClause__Group__3 : rule__DatasetClause__Group__3__Impl rule__DatasetClause__Group__4 ;
    public final void rule__DatasetClause__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2709:1: ( rule__DatasetClause__Group__3__Impl rule__DatasetClause__Group__4 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2710:2: rule__DatasetClause__Group__3__Impl rule__DatasetClause__Group__4
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group__3__Impl_in_rule__DatasetClause__Group__35492);
            rule__DatasetClause__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group__4_in_rule__DatasetClause__Group__35495);
            rule__DatasetClause__Group__4();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group__3"


    // $ANTLR start "rule__DatasetClause__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2717:1: rule__DatasetClause__Group__3__Impl : ( ( rule__DatasetClause__NameAssignment_3 ) ) ;
    public final void rule__DatasetClause__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2721:1: ( ( ( rule__DatasetClause__NameAssignment_3 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2722:1: ( ( rule__DatasetClause__NameAssignment_3 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2722:1: ( ( rule__DatasetClause__NameAssignment_3 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2723:1: ( rule__DatasetClause__NameAssignment_3 )
            {
             before(grammarAccess.getDatasetClauseAccess().getNameAssignment_3()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2724:1: ( rule__DatasetClause__NameAssignment_3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2724:2: rule__DatasetClause__NameAssignment_3
            {
            pushFollow(FOLLOW_rule__DatasetClause__NameAssignment_3_in_rule__DatasetClause__Group__3__Impl5522);
            rule__DatasetClause__NameAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getNameAssignment_3()); 

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
    // $ANTLR end "rule__DatasetClause__Group__3__Impl"


    // $ANTLR start "rule__DatasetClause__Group__4"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2734:1: rule__DatasetClause__Group__4 : rule__DatasetClause__Group__4__Impl ;
    public final void rule__DatasetClause__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2738:1: ( rule__DatasetClause__Group__4__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2739:2: rule__DatasetClause__Group__4__Impl
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group__4__Impl_in_rule__DatasetClause__Group__45552);
            rule__DatasetClause__Group__4__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group__4"


    // $ANTLR start "rule__DatasetClause__Group__4__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2745:1: rule__DatasetClause__Group__4__Impl : ( ( rule__DatasetClause__Group_4__0 )? ) ;
    public final void rule__DatasetClause__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2749:1: ( ( ( rule__DatasetClause__Group_4__0 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2750:1: ( ( rule__DatasetClause__Group_4__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2750:1: ( ( rule__DatasetClause__Group_4__0 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2751:1: ( rule__DatasetClause__Group_4__0 )?
            {
             before(grammarAccess.getDatasetClauseAccess().getGroup_4()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2752:1: ( rule__DatasetClause__Group_4__0 )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==41) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2752:2: rule__DatasetClause__Group_4__0
                    {
                    pushFollow(FOLLOW_rule__DatasetClause__Group_4__0_in_rule__DatasetClause__Group__4__Impl5579);
                    rule__DatasetClause__Group_4__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getDatasetClauseAccess().getGroup_4()); 

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
    // $ANTLR end "rule__DatasetClause__Group__4__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2772:1: rule__DatasetClause__Group_4__0 : rule__DatasetClause__Group_4__0__Impl rule__DatasetClause__Group_4__1 ;
    public final void rule__DatasetClause__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2776:1: ( rule__DatasetClause__Group_4__0__Impl rule__DatasetClause__Group_4__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2777:2: rule__DatasetClause__Group_4__0__Impl rule__DatasetClause__Group_4__1
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4__0__Impl_in_rule__DatasetClause__Group_4__05620);
            rule__DatasetClause__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group_4__1_in_rule__DatasetClause__Group_4__05623);
            rule__DatasetClause__Group_4__1();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4__0"


    // $ANTLR start "rule__DatasetClause__Group_4__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2784:1: rule__DatasetClause__Group_4__0__Impl : ( '[TYPE ' ) ;
    public final void rule__DatasetClause__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2788:1: ( ( '[TYPE ' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2789:1: ( '[TYPE ' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2789:1: ( '[TYPE ' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2790:1: '[TYPE '
            {
             before(grammarAccess.getDatasetClauseAccess().getTYPEKeyword_4_0()); 
            match(input,41,FOLLOW_41_in_rule__DatasetClause__Group_4__0__Impl5651); 
             after(grammarAccess.getDatasetClauseAccess().getTYPEKeyword_4_0()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4__0__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2803:1: rule__DatasetClause__Group_4__1 : rule__DatasetClause__Group_4__1__Impl rule__DatasetClause__Group_4__2 ;
    public final void rule__DatasetClause__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2807:1: ( rule__DatasetClause__Group_4__1__Impl rule__DatasetClause__Group_4__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2808:2: rule__DatasetClause__Group_4__1__Impl rule__DatasetClause__Group_4__2
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4__1__Impl_in_rule__DatasetClause__Group_4__15682);
            rule__DatasetClause__Group_4__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group_4__2_in_rule__DatasetClause__Group_4__15685);
            rule__DatasetClause__Group_4__2();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4__1"


    // $ANTLR start "rule__DatasetClause__Group_4__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2815:1: rule__DatasetClause__Group_4__1__Impl : ( ( rule__DatasetClause__TypeAssignment_4_1 ) ) ;
    public final void rule__DatasetClause__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2819:1: ( ( ( rule__DatasetClause__TypeAssignment_4_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2820:1: ( ( rule__DatasetClause__TypeAssignment_4_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2820:1: ( ( rule__DatasetClause__TypeAssignment_4_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2821:1: ( rule__DatasetClause__TypeAssignment_4_1 )
            {
             before(grammarAccess.getDatasetClauseAccess().getTypeAssignment_4_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2822:1: ( rule__DatasetClause__TypeAssignment_4_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2822:2: rule__DatasetClause__TypeAssignment_4_1
            {
            pushFollow(FOLLOW_rule__DatasetClause__TypeAssignment_4_1_in_rule__DatasetClause__Group_4__1__Impl5712);
            rule__DatasetClause__TypeAssignment_4_1();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getTypeAssignment_4_1()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4__1__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2832:1: rule__DatasetClause__Group_4__2 : rule__DatasetClause__Group_4__2__Impl rule__DatasetClause__Group_4__3 ;
    public final void rule__DatasetClause__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2836:1: ( rule__DatasetClause__Group_4__2__Impl rule__DatasetClause__Group_4__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2837:2: rule__DatasetClause__Group_4__2__Impl rule__DatasetClause__Group_4__3
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4__2__Impl_in_rule__DatasetClause__Group_4__25742);
            rule__DatasetClause__Group_4__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group_4__3_in_rule__DatasetClause__Group_4__25745);
            rule__DatasetClause__Group_4__3();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4__2"


    // $ANTLR start "rule__DatasetClause__Group_4__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2844:1: rule__DatasetClause__Group_4__2__Impl : ( 'SIZE ' ) ;
    public final void rule__DatasetClause__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2848:1: ( ( 'SIZE ' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2849:1: ( 'SIZE ' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2849:1: ( 'SIZE ' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2850:1: 'SIZE '
            {
             before(grammarAccess.getDatasetClauseAccess().getSIZEKeyword_4_2()); 
            match(input,42,FOLLOW_42_in_rule__DatasetClause__Group_4__2__Impl5773); 
             after(grammarAccess.getDatasetClauseAccess().getSIZEKeyword_4_2()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4__2__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2863:1: rule__DatasetClause__Group_4__3 : rule__DatasetClause__Group_4__3__Impl rule__DatasetClause__Group_4__4 ;
    public final void rule__DatasetClause__Group_4__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2867:1: ( rule__DatasetClause__Group_4__3__Impl rule__DatasetClause__Group_4__4 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2868:2: rule__DatasetClause__Group_4__3__Impl rule__DatasetClause__Group_4__4
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4__3__Impl_in_rule__DatasetClause__Group_4__35804);
            rule__DatasetClause__Group_4__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group_4__4_in_rule__DatasetClause__Group_4__35807);
            rule__DatasetClause__Group_4__4();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4__3"


    // $ANTLR start "rule__DatasetClause__Group_4__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2875:1: rule__DatasetClause__Group_4__3__Impl : ( ( rule__DatasetClause__SizeAssignment_4_3 ) ) ;
    public final void rule__DatasetClause__Group_4__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2879:1: ( ( ( rule__DatasetClause__SizeAssignment_4_3 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2880:1: ( ( rule__DatasetClause__SizeAssignment_4_3 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2880:1: ( ( rule__DatasetClause__SizeAssignment_4_3 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2881:1: ( rule__DatasetClause__SizeAssignment_4_3 )
            {
             before(grammarAccess.getDatasetClauseAccess().getSizeAssignment_4_3()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2882:1: ( rule__DatasetClause__SizeAssignment_4_3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2882:2: rule__DatasetClause__SizeAssignment_4_3
            {
            pushFollow(FOLLOW_rule__DatasetClause__SizeAssignment_4_3_in_rule__DatasetClause__Group_4__3__Impl5834);
            rule__DatasetClause__SizeAssignment_4_3();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getSizeAssignment_4_3()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4__3__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4__4"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2892:1: rule__DatasetClause__Group_4__4 : rule__DatasetClause__Group_4__4__Impl rule__DatasetClause__Group_4__5 ;
    public final void rule__DatasetClause__Group_4__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2896:1: ( rule__DatasetClause__Group_4__4__Impl rule__DatasetClause__Group_4__5 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2897:2: rule__DatasetClause__Group_4__4__Impl rule__DatasetClause__Group_4__5
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4__4__Impl_in_rule__DatasetClause__Group_4__45864);
            rule__DatasetClause__Group_4__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group_4__5_in_rule__DatasetClause__Group_4__45867);
            rule__DatasetClause__Group_4__5();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4__4"


    // $ANTLR start "rule__DatasetClause__Group_4__4__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2904:1: rule__DatasetClause__Group_4__4__Impl : ( ( rule__DatasetClause__Group_4_4__0 )? ) ;
    public final void rule__DatasetClause__Group_4__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2908:1: ( ( ( rule__DatasetClause__Group_4_4__0 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2909:1: ( ( rule__DatasetClause__Group_4_4__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2909:1: ( ( rule__DatasetClause__Group_4_4__0 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2910:1: ( rule__DatasetClause__Group_4_4__0 )?
            {
             before(grammarAccess.getDatasetClauseAccess().getGroup_4_4()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2911:1: ( rule__DatasetClause__Group_4_4__0 )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==43) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2911:2: rule__DatasetClause__Group_4_4__0
                    {
                    pushFollow(FOLLOW_rule__DatasetClause__Group_4_4__0_in_rule__DatasetClause__Group_4__4__Impl5894);
                    rule__DatasetClause__Group_4_4__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getDatasetClauseAccess().getGroup_4_4()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4__4__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4__5"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2921:1: rule__DatasetClause__Group_4__5 : rule__DatasetClause__Group_4__5__Impl rule__DatasetClause__Group_4__6 ;
    public final void rule__DatasetClause__Group_4__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2925:1: ( rule__DatasetClause__Group_4__5__Impl rule__DatasetClause__Group_4__6 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2926:2: rule__DatasetClause__Group_4__5__Impl rule__DatasetClause__Group_4__6
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4__5__Impl_in_rule__DatasetClause__Group_4__55925);
            rule__DatasetClause__Group_4__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group_4__6_in_rule__DatasetClause__Group_4__55928);
            rule__DatasetClause__Group_4__6();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4__5"


    // $ANTLR start "rule__DatasetClause__Group_4__5__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2933:1: rule__DatasetClause__Group_4__5__Impl : ( ( rule__DatasetClause__Group_4_5__0 )? ) ;
    public final void rule__DatasetClause__Group_4__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2937:1: ( ( ( rule__DatasetClause__Group_4_5__0 )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2938:1: ( ( rule__DatasetClause__Group_4_5__0 )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2938:1: ( ( rule__DatasetClause__Group_4_5__0 )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2939:1: ( rule__DatasetClause__Group_4_5__0 )?
            {
             before(grammarAccess.getDatasetClauseAccess().getGroup_4_5()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2940:1: ( rule__DatasetClause__Group_4_5__0 )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==44) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2940:2: rule__DatasetClause__Group_4_5__0
                    {
                    pushFollow(FOLLOW_rule__DatasetClause__Group_4_5__0_in_rule__DatasetClause__Group_4__5__Impl5955);
                    rule__DatasetClause__Group_4_5__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getDatasetClauseAccess().getGroup_4_5()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4__5__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4__6"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2950:1: rule__DatasetClause__Group_4__6 : rule__DatasetClause__Group_4__6__Impl ;
    public final void rule__DatasetClause__Group_4__6() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2954:1: ( rule__DatasetClause__Group_4__6__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2955:2: rule__DatasetClause__Group_4__6__Impl
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4__6__Impl_in_rule__DatasetClause__Group_4__65986);
            rule__DatasetClause__Group_4__6__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4__6"


    // $ANTLR start "rule__DatasetClause__Group_4__6__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2961:1: rule__DatasetClause__Group_4__6__Impl : ( ']' ) ;
    public final void rule__DatasetClause__Group_4__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2965:1: ( ( ']' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2966:1: ( ']' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2966:1: ( ']' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2967:1: ']'
            {
             before(grammarAccess.getDatasetClauseAccess().getRightSquareBracketKeyword_4_6()); 
            match(input,33,FOLLOW_33_in_rule__DatasetClause__Group_4__6__Impl6014); 
             after(grammarAccess.getDatasetClauseAccess().getRightSquareBracketKeyword_4_6()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4__6__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4_4__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2994:1: rule__DatasetClause__Group_4_4__0 : rule__DatasetClause__Group_4_4__0__Impl rule__DatasetClause__Group_4_4__1 ;
    public final void rule__DatasetClause__Group_4_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2998:1: ( rule__DatasetClause__Group_4_4__0__Impl rule__DatasetClause__Group_4_4__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:2999:2: rule__DatasetClause__Group_4_4__0__Impl rule__DatasetClause__Group_4_4__1
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4_4__0__Impl_in_rule__DatasetClause__Group_4_4__06059);
            rule__DatasetClause__Group_4_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group_4_4__1_in_rule__DatasetClause__Group_4_4__06062);
            rule__DatasetClause__Group_4_4__1();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4_4__0"


    // $ANTLR start "rule__DatasetClause__Group_4_4__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3006:1: rule__DatasetClause__Group_4_4__0__Impl : ( 'ADVANCE' ) ;
    public final void rule__DatasetClause__Group_4_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3010:1: ( ( 'ADVANCE' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3011:1: ( 'ADVANCE' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3011:1: ( 'ADVANCE' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3012:1: 'ADVANCE'
            {
             before(grammarAccess.getDatasetClauseAccess().getADVANCEKeyword_4_4_0()); 
            match(input,43,FOLLOW_43_in_rule__DatasetClause__Group_4_4__0__Impl6090); 
             after(grammarAccess.getDatasetClauseAccess().getADVANCEKeyword_4_4_0()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4_4__0__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4_4__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3025:1: rule__DatasetClause__Group_4_4__1 : rule__DatasetClause__Group_4_4__1__Impl ;
    public final void rule__DatasetClause__Group_4_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3029:1: ( rule__DatasetClause__Group_4_4__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3030:2: rule__DatasetClause__Group_4_4__1__Impl
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4_4__1__Impl_in_rule__DatasetClause__Group_4_4__16121);
            rule__DatasetClause__Group_4_4__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4_4__1"


    // $ANTLR start "rule__DatasetClause__Group_4_4__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3036:1: rule__DatasetClause__Group_4_4__1__Impl : ( ( rule__DatasetClause__AdvanceAssignment_4_4_1 ) ) ;
    public final void rule__DatasetClause__Group_4_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3040:1: ( ( ( rule__DatasetClause__AdvanceAssignment_4_4_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3041:1: ( ( rule__DatasetClause__AdvanceAssignment_4_4_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3041:1: ( ( rule__DatasetClause__AdvanceAssignment_4_4_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3042:1: ( rule__DatasetClause__AdvanceAssignment_4_4_1 )
            {
             before(grammarAccess.getDatasetClauseAccess().getAdvanceAssignment_4_4_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3043:1: ( rule__DatasetClause__AdvanceAssignment_4_4_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3043:2: rule__DatasetClause__AdvanceAssignment_4_4_1
            {
            pushFollow(FOLLOW_rule__DatasetClause__AdvanceAssignment_4_4_1_in_rule__DatasetClause__Group_4_4__1__Impl6148);
            rule__DatasetClause__AdvanceAssignment_4_4_1();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getAdvanceAssignment_4_4_1()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4_4__1__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4_5__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3057:1: rule__DatasetClause__Group_4_5__0 : rule__DatasetClause__Group_4_5__0__Impl rule__DatasetClause__Group_4_5__1 ;
    public final void rule__DatasetClause__Group_4_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3061:1: ( rule__DatasetClause__Group_4_5__0__Impl rule__DatasetClause__Group_4_5__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3062:2: rule__DatasetClause__Group_4_5__0__Impl rule__DatasetClause__Group_4_5__1
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4_5__0__Impl_in_rule__DatasetClause__Group_4_5__06182);
            rule__DatasetClause__Group_4_5__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__DatasetClause__Group_4_5__1_in_rule__DatasetClause__Group_4_5__06185);
            rule__DatasetClause__Group_4_5__1();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4_5__0"


    // $ANTLR start "rule__DatasetClause__Group_4_5__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3069:1: rule__DatasetClause__Group_4_5__0__Impl : ( 'UNIT ' ) ;
    public final void rule__DatasetClause__Group_4_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3073:1: ( ( 'UNIT ' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3074:1: ( 'UNIT ' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3074:1: ( 'UNIT ' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3075:1: 'UNIT '
            {
             before(grammarAccess.getDatasetClauseAccess().getUNITKeyword_4_5_0()); 
            match(input,44,FOLLOW_44_in_rule__DatasetClause__Group_4_5__0__Impl6213); 
             after(grammarAccess.getDatasetClauseAccess().getUNITKeyword_4_5_0()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4_5__0__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4_5__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3088:1: rule__DatasetClause__Group_4_5__1 : rule__DatasetClause__Group_4_5__1__Impl ;
    public final void rule__DatasetClause__Group_4_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3092:1: ( rule__DatasetClause__Group_4_5__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3093:2: rule__DatasetClause__Group_4_5__1__Impl
            {
            pushFollow(FOLLOW_rule__DatasetClause__Group_4_5__1__Impl_in_rule__DatasetClause__Group_4_5__16244);
            rule__DatasetClause__Group_4_5__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__DatasetClause__Group_4_5__1"


    // $ANTLR start "rule__DatasetClause__Group_4_5__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3099:1: rule__DatasetClause__Group_4_5__1__Impl : ( ( rule__DatasetClause__UnitAssignment_4_5_1 ) ) ;
    public final void rule__DatasetClause__Group_4_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3103:1: ( ( ( rule__DatasetClause__UnitAssignment_4_5_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3104:1: ( ( rule__DatasetClause__UnitAssignment_4_5_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3104:1: ( ( rule__DatasetClause__UnitAssignment_4_5_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3105:1: ( rule__DatasetClause__UnitAssignment_4_5_1 )
            {
             before(grammarAccess.getDatasetClauseAccess().getUnitAssignment_4_5_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3106:1: ( rule__DatasetClause__UnitAssignment_4_5_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3106:2: rule__DatasetClause__UnitAssignment_4_5_1
            {
            pushFollow(FOLLOW_rule__DatasetClause__UnitAssignment_4_5_1_in_rule__DatasetClause__Group_4_5__1__Impl6271);
            rule__DatasetClause__UnitAssignment_4_5_1();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getUnitAssignment_4_5_1()); 

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
    // $ANTLR end "rule__DatasetClause__Group_4_5__1__Impl"


    // $ANTLR start "rule__WhereClause__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3120:1: rule__WhereClause__Group__0 : rule__WhereClause__Group__0__Impl rule__WhereClause__Group__1 ;
    public final void rule__WhereClause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3124:1: ( rule__WhereClause__Group__0__Impl rule__WhereClause__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3125:2: rule__WhereClause__Group__0__Impl rule__WhereClause__Group__1
            {
            pushFollow(FOLLOW_rule__WhereClause__Group__0__Impl_in_rule__WhereClause__Group__06305);
            rule__WhereClause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__WhereClause__Group__1_in_rule__WhereClause__Group__06308);
            rule__WhereClause__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__WhereClause__Group__0"


    // $ANTLR start "rule__WhereClause__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3132:1: rule__WhereClause__Group__0__Impl : ( 'WHERE' ) ;
    public final void rule__WhereClause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3136:1: ( ( 'WHERE' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3137:1: ( 'WHERE' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3137:1: ( 'WHERE' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3138:1: 'WHERE'
            {
             before(grammarAccess.getWhereClauseAccess().getWHEREKeyword_0()); 
            match(input,45,FOLLOW_45_in_rule__WhereClause__Group__0__Impl6336); 
             after(grammarAccess.getWhereClauseAccess().getWHEREKeyword_0()); 

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
    // $ANTLR end "rule__WhereClause__Group__0__Impl"


    // $ANTLR start "rule__WhereClause__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3151:1: rule__WhereClause__Group__1 : rule__WhereClause__Group__1__Impl rule__WhereClause__Group__2 ;
    public final void rule__WhereClause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3155:1: ( rule__WhereClause__Group__1__Impl rule__WhereClause__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3156:2: rule__WhereClause__Group__1__Impl rule__WhereClause__Group__2
            {
            pushFollow(FOLLOW_rule__WhereClause__Group__1__Impl_in_rule__WhereClause__Group__16367);
            rule__WhereClause__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__WhereClause__Group__2_in_rule__WhereClause__Group__16370);
            rule__WhereClause__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__WhereClause__Group__1"


    // $ANTLR start "rule__WhereClause__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3163:1: rule__WhereClause__Group__1__Impl : ( '{' ) ;
    public final void rule__WhereClause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3167:1: ( ( '{' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3168:1: ( '{' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3168:1: ( '{' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3169:1: '{'
            {
             before(grammarAccess.getWhereClauseAccess().getLeftCurlyBracketKeyword_1()); 
            match(input,46,FOLLOW_46_in_rule__WhereClause__Group__1__Impl6398); 
             after(grammarAccess.getWhereClauseAccess().getLeftCurlyBracketKeyword_1()); 

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
    // $ANTLR end "rule__WhereClause__Group__1__Impl"


    // $ANTLR start "rule__WhereClause__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3182:1: rule__WhereClause__Group__2 : rule__WhereClause__Group__2__Impl rule__WhereClause__Group__3 ;
    public final void rule__WhereClause__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3186:1: ( rule__WhereClause__Group__2__Impl rule__WhereClause__Group__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3187:2: rule__WhereClause__Group__2__Impl rule__WhereClause__Group__3
            {
            pushFollow(FOLLOW_rule__WhereClause__Group__2__Impl_in_rule__WhereClause__Group__26429);
            rule__WhereClause__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__WhereClause__Group__3_in_rule__WhereClause__Group__26432);
            rule__WhereClause__Group__3();

            state._fsp--;


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
    // $ANTLR end "rule__WhereClause__Group__2"


    // $ANTLR start "rule__WhereClause__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3194:1: rule__WhereClause__Group__2__Impl : ( ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* ) ) ;
    public final void rule__WhereClause__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3198:1: ( ( ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3199:1: ( ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3199:1: ( ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3200:1: ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3200:1: ( ( rule__WhereClause__WhereclausesAssignment_2 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3201:1: ( rule__WhereClause__WhereclausesAssignment_2 )
            {
             before(grammarAccess.getWhereClauseAccess().getWhereclausesAssignment_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3202:1: ( rule__WhereClause__WhereclausesAssignment_2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3202:2: rule__WhereClause__WhereclausesAssignment_2
            {
            pushFollow(FOLLOW_rule__WhereClause__WhereclausesAssignment_2_in_rule__WhereClause__Group__2__Impl6461);
            rule__WhereClause__WhereclausesAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getWhereClauseAccess().getWhereclausesAssignment_2()); 

            }

            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3205:1: ( ( rule__WhereClause__WhereclausesAssignment_2 )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3206:1: ( rule__WhereClause__WhereclausesAssignment_2 )*
            {
             before(grammarAccess.getWhereClauseAccess().getWhereclausesAssignment_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3207:1: ( rule__WhereClause__WhereclausesAssignment_2 )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==RULE_ID) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3207:2: rule__WhereClause__WhereclausesAssignment_2
            	    {
            	    pushFollow(FOLLOW_rule__WhereClause__WhereclausesAssignment_2_in_rule__WhereClause__Group__2__Impl6473);
            	    rule__WhereClause__WhereclausesAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);

             after(grammarAccess.getWhereClauseAccess().getWhereclausesAssignment_2()); 

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
    // $ANTLR end "rule__WhereClause__Group__2__Impl"


    // $ANTLR start "rule__WhereClause__Group__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3218:1: rule__WhereClause__Group__3 : rule__WhereClause__Group__3__Impl ;
    public final void rule__WhereClause__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3222:1: ( rule__WhereClause__Group__3__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3223:2: rule__WhereClause__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__WhereClause__Group__3__Impl_in_rule__WhereClause__Group__36506);
            rule__WhereClause__Group__3__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__WhereClause__Group__3"


    // $ANTLR start "rule__WhereClause__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3229:1: rule__WhereClause__Group__3__Impl : ( '}' ) ;
    public final void rule__WhereClause__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3233:1: ( ( '}' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3234:1: ( '}' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3234:1: ( '}' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3235:1: '}'
            {
             before(grammarAccess.getWhereClauseAccess().getRightCurlyBracketKeyword_3()); 
            match(input,47,FOLLOW_47_in_rule__WhereClause__Group__3__Impl6534); 
             after(grammarAccess.getWhereClauseAccess().getRightCurlyBracketKeyword_3()); 

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
    // $ANTLR end "rule__WhereClause__Group__3__Impl"


    // $ANTLR start "rule__InnerWhereClause__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3256:1: rule__InnerWhereClause__Group__0 : rule__InnerWhereClause__Group__0__Impl rule__InnerWhereClause__Group__1 ;
    public final void rule__InnerWhereClause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3260:1: ( rule__InnerWhereClause__Group__0__Impl rule__InnerWhereClause__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3261:2: rule__InnerWhereClause__Group__0__Impl rule__InnerWhereClause__Group__1
            {
            pushFollow(FOLLOW_rule__InnerWhereClause__Group__0__Impl_in_rule__InnerWhereClause__Group__06573);
            rule__InnerWhereClause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__InnerWhereClause__Group__1_in_rule__InnerWhereClause__Group__06576);
            rule__InnerWhereClause__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__InnerWhereClause__Group__0"


    // $ANTLR start "rule__InnerWhereClause__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3268:1: rule__InnerWhereClause__Group__0__Impl : ( ( rule__InnerWhereClause__NameAssignment_0 ) ) ;
    public final void rule__InnerWhereClause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3272:1: ( ( ( rule__InnerWhereClause__NameAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3273:1: ( ( rule__InnerWhereClause__NameAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3273:1: ( ( rule__InnerWhereClause__NameAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3274:1: ( rule__InnerWhereClause__NameAssignment_0 )
            {
             before(grammarAccess.getInnerWhereClauseAccess().getNameAssignment_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3275:1: ( rule__InnerWhereClause__NameAssignment_0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3275:2: rule__InnerWhereClause__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__InnerWhereClause__NameAssignment_0_in_rule__InnerWhereClause__Group__0__Impl6603);
            rule__InnerWhereClause__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getInnerWhereClauseAccess().getNameAssignment_0()); 

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
    // $ANTLR end "rule__InnerWhereClause__Group__0__Impl"


    // $ANTLR start "rule__InnerWhereClause__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3285:1: rule__InnerWhereClause__Group__1 : rule__InnerWhereClause__Group__1__Impl ;
    public final void rule__InnerWhereClause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3289:1: ( rule__InnerWhereClause__Group__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3290:2: rule__InnerWhereClause__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__InnerWhereClause__Group__1__Impl_in_rule__InnerWhereClause__Group__16633);
            rule__InnerWhereClause__Group__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__InnerWhereClause__Group__1"


    // $ANTLR start "rule__InnerWhereClause__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3296:1: rule__InnerWhereClause__Group__1__Impl : ( ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 ) ) ;
    public final void rule__InnerWhereClause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3300:1: ( ( ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3301:1: ( ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3301:1: ( ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3302:1: ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 )
            {
             before(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3303:1: ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3303:2: rule__InnerWhereClause__GroupGraphPatternAssignment_1
            {
            pushFollow(FOLLOW_rule__InnerWhereClause__GroupGraphPatternAssignment_1_in_rule__InnerWhereClause__Group__1__Impl6660);
            rule__InnerWhereClause__GroupGraphPatternAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternAssignment_1()); 

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
    // $ANTLR end "rule__InnerWhereClause__Group__1__Impl"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3317:1: rule__GroupGraphPatternSub__Group__0 : rule__GroupGraphPatternSub__Group__0__Impl rule__GroupGraphPatternSub__Group__1 ;
    public final void rule__GroupGraphPatternSub__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3321:1: ( rule__GroupGraphPatternSub__Group__0__Impl rule__GroupGraphPatternSub__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3322:2: rule__GroupGraphPatternSub__Group__0__Impl rule__GroupGraphPatternSub__Group__1
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__0__Impl_in_rule__GroupGraphPatternSub__Group__06694);
            rule__GroupGraphPatternSub__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__1_in_rule__GroupGraphPatternSub__Group__06697);
            rule__GroupGraphPatternSub__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__0"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3329:1: rule__GroupGraphPatternSub__Group__0__Impl : ( '{' ) ;
    public final void rule__GroupGraphPatternSub__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3333:1: ( ( '{' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3334:1: ( '{' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3334:1: ( '{' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3335:1: '{'
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getLeftCurlyBracketKeyword_0()); 
            match(input,46,FOLLOW_46_in_rule__GroupGraphPatternSub__Group__0__Impl6725); 
             after(grammarAccess.getGroupGraphPatternSubAccess().getLeftCurlyBracketKeyword_0()); 

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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__0__Impl"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3348:1: rule__GroupGraphPatternSub__Group__1 : rule__GroupGraphPatternSub__Group__1__Impl rule__GroupGraphPatternSub__Group__2 ;
    public final void rule__GroupGraphPatternSub__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3352:1: ( rule__GroupGraphPatternSub__Group__1__Impl rule__GroupGraphPatternSub__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3353:2: rule__GroupGraphPatternSub__Group__1__Impl rule__GroupGraphPatternSub__Group__2
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__1__Impl_in_rule__GroupGraphPatternSub__Group__16756);
            rule__GroupGraphPatternSub__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__2_in_rule__GroupGraphPatternSub__Group__16759);
            rule__GroupGraphPatternSub__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__1"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3360:1: rule__GroupGraphPatternSub__Group__1__Impl : ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 ) ) ;
    public final void rule__GroupGraphPatternSub__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3364:1: ( ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3365:1: ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3365:1: ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3366:1: ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 )
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3367:1: ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3367:2: rule__GroupGraphPatternSub__GraphPatternsAssignment_1
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__GraphPatternsAssignment_1_in_rule__GroupGraphPatternSub__Group__1__Impl6786);
            rule__GroupGraphPatternSub__GraphPatternsAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsAssignment_1()); 

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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__1__Impl"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3377:1: rule__GroupGraphPatternSub__Group__2 : rule__GroupGraphPatternSub__Group__2__Impl rule__GroupGraphPatternSub__Group__3 ;
    public final void rule__GroupGraphPatternSub__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3381:1: ( rule__GroupGraphPatternSub__Group__2__Impl rule__GroupGraphPatternSub__Group__3 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3382:2: rule__GroupGraphPatternSub__Group__2__Impl rule__GroupGraphPatternSub__Group__3
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__2__Impl_in_rule__GroupGraphPatternSub__Group__26816);
            rule__GroupGraphPatternSub__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__3_in_rule__GroupGraphPatternSub__Group__26819);
            rule__GroupGraphPatternSub__Group__3();

            state._fsp--;


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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__2"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3389:1: rule__GroupGraphPatternSub__Group__2__Impl : ( ( rule__GroupGraphPatternSub__Group_2__0 )* ) ;
    public final void rule__GroupGraphPatternSub__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3393:1: ( ( ( rule__GroupGraphPatternSub__Group_2__0 )* ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3394:1: ( ( rule__GroupGraphPatternSub__Group_2__0 )* )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3394:1: ( ( rule__GroupGraphPatternSub__Group_2__0 )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3395:1: ( rule__GroupGraphPatternSub__Group_2__0 )*
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGroup_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3396:1: ( rule__GroupGraphPatternSub__Group_2__0 )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==48) ) {
                    int LA27_1 = input.LA(2);

                    if ( ((LA27_1>=RULE_ID && LA27_1<=RULE_IRI_TERMINAL)||LA27_1==RULE_STRING||LA27_1==50) ) {
                        alt27=1;
                    }


                }


                switch (alt27) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3396:2: rule__GroupGraphPatternSub__Group_2__0
            	    {
            	    pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group_2__0_in_rule__GroupGraphPatternSub__Group__2__Impl6846);
            	    rule__GroupGraphPatternSub__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);

             after(grammarAccess.getGroupGraphPatternSubAccess().getGroup_2()); 

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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__2__Impl"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3406:1: rule__GroupGraphPatternSub__Group__3 : rule__GroupGraphPatternSub__Group__3__Impl rule__GroupGraphPatternSub__Group__4 ;
    public final void rule__GroupGraphPatternSub__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3410:1: ( rule__GroupGraphPatternSub__Group__3__Impl rule__GroupGraphPatternSub__Group__4 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3411:2: rule__GroupGraphPatternSub__Group__3__Impl rule__GroupGraphPatternSub__Group__4
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__3__Impl_in_rule__GroupGraphPatternSub__Group__36877);
            rule__GroupGraphPatternSub__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__4_in_rule__GroupGraphPatternSub__Group__36880);
            rule__GroupGraphPatternSub__Group__4();

            state._fsp--;


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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__3"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__3__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3418:1: rule__GroupGraphPatternSub__Group__3__Impl : ( ( '.' )? ) ;
    public final void rule__GroupGraphPatternSub__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3422:1: ( ( ( '.' )? ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3423:1: ( ( '.' )? )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3423:1: ( ( '.' )? )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3424:1: ( '.' )?
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_3()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3425:1: ( '.' )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==48) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3426:2: '.'
                    {
                    match(input,48,FOLLOW_48_in_rule__GroupGraphPatternSub__Group__3__Impl6909); 

                    }
                    break;

            }

             after(grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_3()); 

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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__3__Impl"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__4"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3437:1: rule__GroupGraphPatternSub__Group__4 : rule__GroupGraphPatternSub__Group__4__Impl ;
    public final void rule__GroupGraphPatternSub__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3441:1: ( rule__GroupGraphPatternSub__Group__4__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3442:2: rule__GroupGraphPatternSub__Group__4__Impl
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group__4__Impl_in_rule__GroupGraphPatternSub__Group__46942);
            rule__GroupGraphPatternSub__Group__4__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__4"


    // $ANTLR start "rule__GroupGraphPatternSub__Group__4__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3448:1: rule__GroupGraphPatternSub__Group__4__Impl : ( '}' ) ;
    public final void rule__GroupGraphPatternSub__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3452:1: ( ( '}' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3453:1: ( '}' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3453:1: ( '}' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3454:1: '}'
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getRightCurlyBracketKeyword_4()); 
            match(input,47,FOLLOW_47_in_rule__GroupGraphPatternSub__Group__4__Impl6970); 
             after(grammarAccess.getGroupGraphPatternSubAccess().getRightCurlyBracketKeyword_4()); 

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
    // $ANTLR end "rule__GroupGraphPatternSub__Group__4__Impl"


    // $ANTLR start "rule__GroupGraphPatternSub__Group_2__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3477:1: rule__GroupGraphPatternSub__Group_2__0 : rule__GroupGraphPatternSub__Group_2__0__Impl rule__GroupGraphPatternSub__Group_2__1 ;
    public final void rule__GroupGraphPatternSub__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3481:1: ( rule__GroupGraphPatternSub__Group_2__0__Impl rule__GroupGraphPatternSub__Group_2__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3482:2: rule__GroupGraphPatternSub__Group_2__0__Impl rule__GroupGraphPatternSub__Group_2__1
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group_2__0__Impl_in_rule__GroupGraphPatternSub__Group_2__07011);
            rule__GroupGraphPatternSub__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group_2__1_in_rule__GroupGraphPatternSub__Group_2__07014);
            rule__GroupGraphPatternSub__Group_2__1();

            state._fsp--;


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
    // $ANTLR end "rule__GroupGraphPatternSub__Group_2__0"


    // $ANTLR start "rule__GroupGraphPatternSub__Group_2__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3489:1: rule__GroupGraphPatternSub__Group_2__0__Impl : ( '.' ) ;
    public final void rule__GroupGraphPatternSub__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3493:1: ( ( '.' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3494:1: ( '.' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3494:1: ( '.' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3495:1: '.'
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_2_0()); 
            match(input,48,FOLLOW_48_in_rule__GroupGraphPatternSub__Group_2__0__Impl7042); 
             after(grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_2_0()); 

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
    // $ANTLR end "rule__GroupGraphPatternSub__Group_2__0__Impl"


    // $ANTLR start "rule__GroupGraphPatternSub__Group_2__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3508:1: rule__GroupGraphPatternSub__Group_2__1 : rule__GroupGraphPatternSub__Group_2__1__Impl ;
    public final void rule__GroupGraphPatternSub__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3512:1: ( rule__GroupGraphPatternSub__Group_2__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3513:2: rule__GroupGraphPatternSub__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__Group_2__1__Impl_in_rule__GroupGraphPatternSub__Group_2__17073);
            rule__GroupGraphPatternSub__Group_2__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__GroupGraphPatternSub__Group_2__1"


    // $ANTLR start "rule__GroupGraphPatternSub__Group_2__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3519:1: rule__GroupGraphPatternSub__Group_2__1__Impl : ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 ) ) ;
    public final void rule__GroupGraphPatternSub__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3523:1: ( ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3524:1: ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3524:1: ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3525:1: ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 )
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsAssignment_2_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3526:1: ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3526:2: rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1
            {
            pushFollow(FOLLOW_rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1_in_rule__GroupGraphPatternSub__Group_2__1__Impl7100);
            rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsAssignment_2_1()); 

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
    // $ANTLR end "rule__GroupGraphPatternSub__Group_2__1__Impl"


    // $ANTLR start "rule__TriplesSameSubject__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3540:1: rule__TriplesSameSubject__Group__0 : rule__TriplesSameSubject__Group__0__Impl rule__TriplesSameSubject__Group__1 ;
    public final void rule__TriplesSameSubject__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3544:1: ( rule__TriplesSameSubject__Group__0__Impl rule__TriplesSameSubject__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3545:2: rule__TriplesSameSubject__Group__0__Impl rule__TriplesSameSubject__Group__1
            {
            pushFollow(FOLLOW_rule__TriplesSameSubject__Group__0__Impl_in_rule__TriplesSameSubject__Group__07134);
            rule__TriplesSameSubject__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__TriplesSameSubject__Group__1_in_rule__TriplesSameSubject__Group__07137);
            rule__TriplesSameSubject__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__TriplesSameSubject__Group__0"


    // $ANTLR start "rule__TriplesSameSubject__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3552:1: rule__TriplesSameSubject__Group__0__Impl : ( ( rule__TriplesSameSubject__SubjectAssignment_0 ) ) ;
    public final void rule__TriplesSameSubject__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3556:1: ( ( ( rule__TriplesSameSubject__SubjectAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3557:1: ( ( rule__TriplesSameSubject__SubjectAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3557:1: ( ( rule__TriplesSameSubject__SubjectAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3558:1: ( rule__TriplesSameSubject__SubjectAssignment_0 )
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getSubjectAssignment_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3559:1: ( rule__TriplesSameSubject__SubjectAssignment_0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3559:2: rule__TriplesSameSubject__SubjectAssignment_0
            {
            pushFollow(FOLLOW_rule__TriplesSameSubject__SubjectAssignment_0_in_rule__TriplesSameSubject__Group__0__Impl7164);
            rule__TriplesSameSubject__SubjectAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getTriplesSameSubjectAccess().getSubjectAssignment_0()); 

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
    // $ANTLR end "rule__TriplesSameSubject__Group__0__Impl"


    // $ANTLR start "rule__TriplesSameSubject__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3569:1: rule__TriplesSameSubject__Group__1 : rule__TriplesSameSubject__Group__1__Impl rule__TriplesSameSubject__Group__2 ;
    public final void rule__TriplesSameSubject__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3573:1: ( rule__TriplesSameSubject__Group__1__Impl rule__TriplesSameSubject__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3574:2: rule__TriplesSameSubject__Group__1__Impl rule__TriplesSameSubject__Group__2
            {
            pushFollow(FOLLOW_rule__TriplesSameSubject__Group__1__Impl_in_rule__TriplesSameSubject__Group__17194);
            rule__TriplesSameSubject__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__TriplesSameSubject__Group__2_in_rule__TriplesSameSubject__Group__17197);
            rule__TriplesSameSubject__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__TriplesSameSubject__Group__1"


    // $ANTLR start "rule__TriplesSameSubject__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3581:1: rule__TriplesSameSubject__Group__1__Impl : ( ( rule__TriplesSameSubject__PropertyListAssignment_1 ) ) ;
    public final void rule__TriplesSameSubject__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3585:1: ( ( ( rule__TriplesSameSubject__PropertyListAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3586:1: ( ( rule__TriplesSameSubject__PropertyListAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3586:1: ( ( rule__TriplesSameSubject__PropertyListAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3587:1: ( rule__TriplesSameSubject__PropertyListAssignment_1 )
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getPropertyListAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3588:1: ( rule__TriplesSameSubject__PropertyListAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3588:2: rule__TriplesSameSubject__PropertyListAssignment_1
            {
            pushFollow(FOLLOW_rule__TriplesSameSubject__PropertyListAssignment_1_in_rule__TriplesSameSubject__Group__1__Impl7224);
            rule__TriplesSameSubject__PropertyListAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getTriplesSameSubjectAccess().getPropertyListAssignment_1()); 

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
    // $ANTLR end "rule__TriplesSameSubject__Group__1__Impl"


    // $ANTLR start "rule__TriplesSameSubject__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3598:1: rule__TriplesSameSubject__Group__2 : rule__TriplesSameSubject__Group__2__Impl ;
    public final void rule__TriplesSameSubject__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3602:1: ( rule__TriplesSameSubject__Group__2__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3603:2: rule__TriplesSameSubject__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__TriplesSameSubject__Group__2__Impl_in_rule__TriplesSameSubject__Group__27254);
            rule__TriplesSameSubject__Group__2__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__TriplesSameSubject__Group__2"


    // $ANTLR start "rule__TriplesSameSubject__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3609:1: rule__TriplesSameSubject__Group__2__Impl : ( ( rule__TriplesSameSubject__Group_2__0 )* ) ;
    public final void rule__TriplesSameSubject__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3613:1: ( ( ( rule__TriplesSameSubject__Group_2__0 )* ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3614:1: ( ( rule__TriplesSameSubject__Group_2__0 )* )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3614:1: ( ( rule__TriplesSameSubject__Group_2__0 )* )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3615:1: ( rule__TriplesSameSubject__Group_2__0 )*
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getGroup_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3616:1: ( rule__TriplesSameSubject__Group_2__0 )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( (LA29_0==49) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3616:2: rule__TriplesSameSubject__Group_2__0
            	    {
            	    pushFollow(FOLLOW_rule__TriplesSameSubject__Group_2__0_in_rule__TriplesSameSubject__Group__2__Impl7281);
            	    rule__TriplesSameSubject__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop29;
                }
            } while (true);

             after(grammarAccess.getTriplesSameSubjectAccess().getGroup_2()); 

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
    // $ANTLR end "rule__TriplesSameSubject__Group__2__Impl"


    // $ANTLR start "rule__TriplesSameSubject__Group_2__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3632:1: rule__TriplesSameSubject__Group_2__0 : rule__TriplesSameSubject__Group_2__0__Impl rule__TriplesSameSubject__Group_2__1 ;
    public final void rule__TriplesSameSubject__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3636:1: ( rule__TriplesSameSubject__Group_2__0__Impl rule__TriplesSameSubject__Group_2__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3637:2: rule__TriplesSameSubject__Group_2__0__Impl rule__TriplesSameSubject__Group_2__1
            {
            pushFollow(FOLLOW_rule__TriplesSameSubject__Group_2__0__Impl_in_rule__TriplesSameSubject__Group_2__07318);
            rule__TriplesSameSubject__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__TriplesSameSubject__Group_2__1_in_rule__TriplesSameSubject__Group_2__07321);
            rule__TriplesSameSubject__Group_2__1();

            state._fsp--;


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
    // $ANTLR end "rule__TriplesSameSubject__Group_2__0"


    // $ANTLR start "rule__TriplesSameSubject__Group_2__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3644:1: rule__TriplesSameSubject__Group_2__0__Impl : ( ';' ) ;
    public final void rule__TriplesSameSubject__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3648:1: ( ( ';' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3649:1: ( ';' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3649:1: ( ';' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3650:1: ';'
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getSemicolonKeyword_2_0()); 
            match(input,49,FOLLOW_49_in_rule__TriplesSameSubject__Group_2__0__Impl7349); 
             after(grammarAccess.getTriplesSameSubjectAccess().getSemicolonKeyword_2_0()); 

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
    // $ANTLR end "rule__TriplesSameSubject__Group_2__0__Impl"


    // $ANTLR start "rule__TriplesSameSubject__Group_2__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3663:1: rule__TriplesSameSubject__Group_2__1 : rule__TriplesSameSubject__Group_2__1__Impl ;
    public final void rule__TriplesSameSubject__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3667:1: ( rule__TriplesSameSubject__Group_2__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3668:2: rule__TriplesSameSubject__Group_2__1__Impl
            {
            pushFollow(FOLLOW_rule__TriplesSameSubject__Group_2__1__Impl_in_rule__TriplesSameSubject__Group_2__17380);
            rule__TriplesSameSubject__Group_2__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__TriplesSameSubject__Group_2__1"


    // $ANTLR start "rule__TriplesSameSubject__Group_2__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3674:1: rule__TriplesSameSubject__Group_2__1__Impl : ( ( rule__TriplesSameSubject__PropertyListAssignment_2_1 ) ) ;
    public final void rule__TriplesSameSubject__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3678:1: ( ( ( rule__TriplesSameSubject__PropertyListAssignment_2_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3679:1: ( ( rule__TriplesSameSubject__PropertyListAssignment_2_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3679:1: ( ( rule__TriplesSameSubject__PropertyListAssignment_2_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3680:1: ( rule__TriplesSameSubject__PropertyListAssignment_2_1 )
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getPropertyListAssignment_2_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3681:1: ( rule__TriplesSameSubject__PropertyListAssignment_2_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3681:2: rule__TriplesSameSubject__PropertyListAssignment_2_1
            {
            pushFollow(FOLLOW_rule__TriplesSameSubject__PropertyListAssignment_2_1_in_rule__TriplesSameSubject__Group_2__1__Impl7407);
            rule__TriplesSameSubject__PropertyListAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getTriplesSameSubjectAccess().getPropertyListAssignment_2_1()); 

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
    // $ANTLR end "rule__TriplesSameSubject__Group_2__1__Impl"


    // $ANTLR start "rule__PropertyList__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3695:1: rule__PropertyList__Group__0 : rule__PropertyList__Group__0__Impl rule__PropertyList__Group__1 ;
    public final void rule__PropertyList__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3699:1: ( rule__PropertyList__Group__0__Impl rule__PropertyList__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3700:2: rule__PropertyList__Group__0__Impl rule__PropertyList__Group__1
            {
            pushFollow(FOLLOW_rule__PropertyList__Group__0__Impl_in_rule__PropertyList__Group__07441);
            rule__PropertyList__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__PropertyList__Group__1_in_rule__PropertyList__Group__07444);
            rule__PropertyList__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__PropertyList__Group__0"


    // $ANTLR start "rule__PropertyList__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3707:1: rule__PropertyList__Group__0__Impl : ( ( rule__PropertyList__PropertyAssignment_0 ) ) ;
    public final void rule__PropertyList__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3711:1: ( ( ( rule__PropertyList__PropertyAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3712:1: ( ( rule__PropertyList__PropertyAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3712:1: ( ( rule__PropertyList__PropertyAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3713:1: ( rule__PropertyList__PropertyAssignment_0 )
            {
             before(grammarAccess.getPropertyListAccess().getPropertyAssignment_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3714:1: ( rule__PropertyList__PropertyAssignment_0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3714:2: rule__PropertyList__PropertyAssignment_0
            {
            pushFollow(FOLLOW_rule__PropertyList__PropertyAssignment_0_in_rule__PropertyList__Group__0__Impl7471);
            rule__PropertyList__PropertyAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getPropertyListAccess().getPropertyAssignment_0()); 

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
    // $ANTLR end "rule__PropertyList__Group__0__Impl"


    // $ANTLR start "rule__PropertyList__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3724:1: rule__PropertyList__Group__1 : rule__PropertyList__Group__1__Impl ;
    public final void rule__PropertyList__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3728:1: ( rule__PropertyList__Group__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3729:2: rule__PropertyList__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__PropertyList__Group__1__Impl_in_rule__PropertyList__Group__17501);
            rule__PropertyList__Group__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__PropertyList__Group__1"


    // $ANTLR start "rule__PropertyList__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3735:1: rule__PropertyList__Group__1__Impl : ( ( rule__PropertyList__ObjectAssignment_1 ) ) ;
    public final void rule__PropertyList__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3739:1: ( ( ( rule__PropertyList__ObjectAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3740:1: ( ( rule__PropertyList__ObjectAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3740:1: ( ( rule__PropertyList__ObjectAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3741:1: ( rule__PropertyList__ObjectAssignment_1 )
            {
             before(grammarAccess.getPropertyListAccess().getObjectAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3742:1: ( rule__PropertyList__ObjectAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3742:2: rule__PropertyList__ObjectAssignment_1
            {
            pushFollow(FOLLOW_rule__PropertyList__ObjectAssignment_1_in_rule__PropertyList__Group__1__Impl7528);
            rule__PropertyList__ObjectAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getPropertyListAccess().getObjectAssignment_1()); 

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
    // $ANTLR end "rule__PropertyList__Group__1__Impl"


    // $ANTLR start "rule__UnNamedVariable__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3756:1: rule__UnNamedVariable__Group__0 : rule__UnNamedVariable__Group__0__Impl rule__UnNamedVariable__Group__1 ;
    public final void rule__UnNamedVariable__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3760:1: ( rule__UnNamedVariable__Group__0__Impl rule__UnNamedVariable__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3761:2: rule__UnNamedVariable__Group__0__Impl rule__UnNamedVariable__Group__1
            {
            pushFollow(FOLLOW_rule__UnNamedVariable__Group__0__Impl_in_rule__UnNamedVariable__Group__07562);
            rule__UnNamedVariable__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UnNamedVariable__Group__1_in_rule__UnNamedVariable__Group__07565);
            rule__UnNamedVariable__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__UnNamedVariable__Group__0"


    // $ANTLR start "rule__UnNamedVariable__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3768:1: rule__UnNamedVariable__Group__0__Impl : ( '?' ) ;
    public final void rule__UnNamedVariable__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3772:1: ( ( '?' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3773:1: ( '?' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3773:1: ( '?' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3774:1: '?'
            {
             before(grammarAccess.getUnNamedVariableAccess().getQuestionMarkKeyword_0()); 
            match(input,50,FOLLOW_50_in_rule__UnNamedVariable__Group__0__Impl7593); 
             after(grammarAccess.getUnNamedVariableAccess().getQuestionMarkKeyword_0()); 

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
    // $ANTLR end "rule__UnNamedVariable__Group__0__Impl"


    // $ANTLR start "rule__UnNamedVariable__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3787:1: rule__UnNamedVariable__Group__1 : rule__UnNamedVariable__Group__1__Impl ;
    public final void rule__UnNamedVariable__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3791:1: ( rule__UnNamedVariable__Group__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3792:2: rule__UnNamedVariable__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__UnNamedVariable__Group__1__Impl_in_rule__UnNamedVariable__Group__17624);
            rule__UnNamedVariable__Group__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__UnNamedVariable__Group__1"


    // $ANTLR start "rule__UnNamedVariable__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3798:1: rule__UnNamedVariable__Group__1__Impl : ( ( rule__UnNamedVariable__NameAssignment_1 ) ) ;
    public final void rule__UnNamedVariable__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3802:1: ( ( ( rule__UnNamedVariable__NameAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3803:1: ( ( rule__UnNamedVariable__NameAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3803:1: ( ( rule__UnNamedVariable__NameAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3804:1: ( rule__UnNamedVariable__NameAssignment_1 )
            {
             before(grammarAccess.getUnNamedVariableAccess().getNameAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3805:1: ( rule__UnNamedVariable__NameAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3805:2: rule__UnNamedVariable__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__UnNamedVariable__NameAssignment_1_in_rule__UnNamedVariable__Group__1__Impl7651);
            rule__UnNamedVariable__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getUnNamedVariableAccess().getNameAssignment_1()); 

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
    // $ANTLR end "rule__UnNamedVariable__Group__1__Impl"


    // $ANTLR start "rule__Property__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3819:1: rule__Property__Group__0 : rule__Property__Group__0__Impl rule__Property__Group__1 ;
    public final void rule__Property__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3823:1: ( rule__Property__Group__0__Impl rule__Property__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3824:2: rule__Property__Group__0__Impl rule__Property__Group__1
            {
            pushFollow(FOLLOW_rule__Property__Group__0__Impl_in_rule__Property__Group__07685);
            rule__Property__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Property__Group__1_in_rule__Property__Group__07688);
            rule__Property__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__Property__Group__0"


    // $ANTLR start "rule__Property__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3831:1: rule__Property__Group__0__Impl : ( ( rule__Property__PrefixAssignment_0 ) ) ;
    public final void rule__Property__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3835:1: ( ( ( rule__Property__PrefixAssignment_0 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3836:1: ( ( rule__Property__PrefixAssignment_0 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3836:1: ( ( rule__Property__PrefixAssignment_0 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3837:1: ( rule__Property__PrefixAssignment_0 )
            {
             before(grammarAccess.getPropertyAccess().getPrefixAssignment_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3838:1: ( rule__Property__PrefixAssignment_0 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3838:2: rule__Property__PrefixAssignment_0
            {
            pushFollow(FOLLOW_rule__Property__PrefixAssignment_0_in_rule__Property__Group__0__Impl7715);
            rule__Property__PrefixAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getPropertyAccess().getPrefixAssignment_0()); 

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
    // $ANTLR end "rule__Property__Group__0__Impl"


    // $ANTLR start "rule__Property__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3848:1: rule__Property__Group__1 : rule__Property__Group__1__Impl rule__Property__Group__2 ;
    public final void rule__Property__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3852:1: ( rule__Property__Group__1__Impl rule__Property__Group__2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3853:2: rule__Property__Group__1__Impl rule__Property__Group__2
            {
            pushFollow(FOLLOW_rule__Property__Group__1__Impl_in_rule__Property__Group__17745);
            rule__Property__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Property__Group__2_in_rule__Property__Group__17748);
            rule__Property__Group__2();

            state._fsp--;


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
    // $ANTLR end "rule__Property__Group__1"


    // $ANTLR start "rule__Property__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3860:1: rule__Property__Group__1__Impl : ( ':' ) ;
    public final void rule__Property__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3864:1: ( ( ':' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3865:1: ( ':' )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3865:1: ( ':' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3866:1: ':'
            {
             before(grammarAccess.getPropertyAccess().getColonKeyword_1()); 
            match(input,27,FOLLOW_27_in_rule__Property__Group__1__Impl7776); 
             after(grammarAccess.getPropertyAccess().getColonKeyword_1()); 

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
    // $ANTLR end "rule__Property__Group__1__Impl"


    // $ANTLR start "rule__Property__Group__2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3879:1: rule__Property__Group__2 : rule__Property__Group__2__Impl ;
    public final void rule__Property__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3883:1: ( rule__Property__Group__2__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3884:2: rule__Property__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__Property__Group__2__Impl_in_rule__Property__Group__27807);
            rule__Property__Group__2__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__Property__Group__2"


    // $ANTLR start "rule__Property__Group__2__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3890:1: rule__Property__Group__2__Impl : ( ( rule__Property__NameAssignment_2 ) ) ;
    public final void rule__Property__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3894:1: ( ( ( rule__Property__NameAssignment_2 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3895:1: ( ( rule__Property__NameAssignment_2 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3895:1: ( ( rule__Property__NameAssignment_2 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3896:1: ( rule__Property__NameAssignment_2 )
            {
             before(grammarAccess.getPropertyAccess().getNameAssignment_2()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3897:1: ( rule__Property__NameAssignment_2 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3897:2: rule__Property__NameAssignment_2
            {
            pushFollow(FOLLOW_rule__Property__NameAssignment_2_in_rule__Property__Group__2__Impl7834);
            rule__Property__NameAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getPropertyAccess().getNameAssignment_2()); 

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
    // $ANTLR end "rule__Property__Group__2__Impl"


    // $ANTLR start "rule__IRI__Group__0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3913:1: rule__IRI__Group__0 : rule__IRI__Group__0__Impl rule__IRI__Group__1 ;
    public final void rule__IRI__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3917:1: ( rule__IRI__Group__0__Impl rule__IRI__Group__1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3918:2: rule__IRI__Group__0__Impl rule__IRI__Group__1
            {
            pushFollow(FOLLOW_rule__IRI__Group__0__Impl_in_rule__IRI__Group__07870);
            rule__IRI__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__IRI__Group__1_in_rule__IRI__Group__07873);
            rule__IRI__Group__1();

            state._fsp--;


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
    // $ANTLR end "rule__IRI__Group__0"


    // $ANTLR start "rule__IRI__Group__0__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3925:1: rule__IRI__Group__0__Impl : ( () ) ;
    public final void rule__IRI__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3929:1: ( ( () ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3930:1: ( () )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3930:1: ( () )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3931:1: ()
            {
             before(grammarAccess.getIRIAccess().getIRIAction_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3932:1: ()
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3934:1: 
            {
            }

             after(grammarAccess.getIRIAccess().getIRIAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__IRI__Group__0__Impl"


    // $ANTLR start "rule__IRI__Group__1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3944:1: rule__IRI__Group__1 : rule__IRI__Group__1__Impl ;
    public final void rule__IRI__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3948:1: ( rule__IRI__Group__1__Impl )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3949:2: rule__IRI__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__IRI__Group__1__Impl_in_rule__IRI__Group__17931);
            rule__IRI__Group__1__Impl();

            state._fsp--;


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
    // $ANTLR end "rule__IRI__Group__1"


    // $ANTLR start "rule__IRI__Group__1__Impl"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3955:1: rule__IRI__Group__1__Impl : ( ( rule__IRI__ValueAssignment_1 ) ) ;
    public final void rule__IRI__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3959:1: ( ( ( rule__IRI__ValueAssignment_1 ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3960:1: ( ( rule__IRI__ValueAssignment_1 ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3960:1: ( ( rule__IRI__ValueAssignment_1 ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3961:1: ( rule__IRI__ValueAssignment_1 )
            {
             before(grammarAccess.getIRIAccess().getValueAssignment_1()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3962:1: ( rule__IRI__ValueAssignment_1 )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3962:2: rule__IRI__ValueAssignment_1
            {
            pushFollow(FOLLOW_rule__IRI__ValueAssignment_1_in_rule__IRI__Group__1__Impl7958);
            rule__IRI__ValueAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getIRIAccess().getValueAssignment_1()); 

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
    // $ANTLR end "rule__IRI__Group__1__Impl"


    // $ANTLR start "rule__Prefix__NameAssignment_0_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3979:1: rule__Prefix__NameAssignment_0_1 : ( RULE_ID ) ;
    public final void rule__Prefix__NameAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3983:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3984:1: ( RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3984:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3985:1: RULE_ID
            {
             before(grammarAccess.getPrefixAccess().getNameIDTerminalRuleCall_0_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Prefix__NameAssignment_0_17999); 
             after(grammarAccess.getPrefixAccess().getNameIDTerminalRuleCall_0_1_0()); 

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
    // $ANTLR end "rule__Prefix__NameAssignment_0_1"


    // $ANTLR start "rule__Prefix__IrefAssignment_0_3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3994:1: rule__Prefix__IrefAssignment_0_3 : ( RULE_IRI_TERMINAL ) ;
    public final void rule__Prefix__IrefAssignment_0_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3998:1: ( ( RULE_IRI_TERMINAL ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3999:1: ( RULE_IRI_TERMINAL )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:3999:1: ( RULE_IRI_TERMINAL )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4000:1: RULE_IRI_TERMINAL
            {
             before(grammarAccess.getPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_0_3_0()); 
            match(input,RULE_IRI_TERMINAL,FOLLOW_RULE_IRI_TERMINAL_in_rule__Prefix__IrefAssignment_0_38030); 
             after(grammarAccess.getPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_0_3_0()); 

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
    // $ANTLR end "rule__Prefix__IrefAssignment_0_3"


    // $ANTLR start "rule__UnNamedPrefix__IrefAssignment_2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4009:1: rule__UnNamedPrefix__IrefAssignment_2 : ( RULE_IRI_TERMINAL ) ;
    public final void rule__UnNamedPrefix__IrefAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4013:1: ( ( RULE_IRI_TERMINAL ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4014:1: ( RULE_IRI_TERMINAL )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4014:1: ( RULE_IRI_TERMINAL )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4015:1: RULE_IRI_TERMINAL
            {
             before(grammarAccess.getUnNamedPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_2_0()); 
            match(input,RULE_IRI_TERMINAL,FOLLOW_RULE_IRI_TERMINAL_in_rule__UnNamedPrefix__IrefAssignment_28061); 
             after(grammarAccess.getUnNamedPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_2_0()); 

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
    // $ANTLR end "rule__UnNamedPrefix__IrefAssignment_2"


    // $ANTLR start "rule__Base__IrefAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4024:1: rule__Base__IrefAssignment_1 : ( ruleIRI ) ;
    public final void rule__Base__IrefAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4028:1: ( ( ruleIRI ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4029:1: ( ruleIRI )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4029:1: ( ruleIRI )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4030:1: ruleIRI
            {
             before(grammarAccess.getBaseAccess().getIrefIRIParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleIRI_in_rule__Base__IrefAssignment_18092);
            ruleIRI();

            state._fsp--;

             after(grammarAccess.getBaseAccess().getIrefIRIParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__Base__IrefAssignment_1"


    // $ANTLR start "rule__SelectQuery__MethodAssignment_0_0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4039:1: rule__SelectQuery__MethodAssignment_0_0 : ( ( '#ADDQUERY' ) ) ;
    public final void rule__SelectQuery__MethodAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4043:1: ( ( ( '#ADDQUERY' ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4044:1: ( ( '#ADDQUERY' ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4044:1: ( ( '#ADDQUERY' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4045:1: ( '#ADDQUERY' )
            {
             before(grammarAccess.getSelectQueryAccess().getMethodADDQUERYKeyword_0_0_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4046:1: ( '#ADDQUERY' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4047:1: '#ADDQUERY'
            {
             before(grammarAccess.getSelectQueryAccess().getMethodADDQUERYKeyword_0_0_0()); 
            match(input,51,FOLLOW_51_in_rule__SelectQuery__MethodAssignment_0_08128); 
             after(grammarAccess.getSelectQueryAccess().getMethodADDQUERYKeyword_0_0_0()); 

            }

             after(grammarAccess.getSelectQueryAccess().getMethodADDQUERYKeyword_0_0_0()); 

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
    // $ANTLR end "rule__SelectQuery__MethodAssignment_0_0"


    // $ANTLR start "rule__SelectQuery__BaseAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4062:1: rule__SelectQuery__BaseAssignment_1 : ( ruleBase ) ;
    public final void rule__SelectQuery__BaseAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4066:1: ( ( ruleBase ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4067:1: ( ruleBase )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4067:1: ( ruleBase )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4068:1: ruleBase
            {
             before(grammarAccess.getSelectQueryAccess().getBaseBaseParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleBase_in_rule__SelectQuery__BaseAssignment_18167);
            ruleBase();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getBaseBaseParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__SelectQuery__BaseAssignment_1"


    // $ANTLR start "rule__SelectQuery__PrefixesAssignment_2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4077:1: rule__SelectQuery__PrefixesAssignment_2 : ( rulePrefix ) ;
    public final void rule__SelectQuery__PrefixesAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4081:1: ( ( rulePrefix ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4082:1: ( rulePrefix )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4082:1: ( rulePrefix )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4083:1: rulePrefix
            {
             before(grammarAccess.getSelectQueryAccess().getPrefixesPrefixParserRuleCall_2_0()); 
            pushFollow(FOLLOW_rulePrefix_in_rule__SelectQuery__PrefixesAssignment_28198);
            rulePrefix();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getPrefixesPrefixParserRuleCall_2_0()); 

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
    // $ANTLR end "rule__SelectQuery__PrefixesAssignment_2"


    // $ANTLR start "rule__SelectQuery__DatasetClausesAssignment_3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4092:1: rule__SelectQuery__DatasetClausesAssignment_3 : ( ruleDatasetClause ) ;
    public final void rule__SelectQuery__DatasetClausesAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4096:1: ( ( ruleDatasetClause ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4097:1: ( ruleDatasetClause )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4097:1: ( ruleDatasetClause )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4098:1: ruleDatasetClause
            {
             before(grammarAccess.getSelectQueryAccess().getDatasetClausesDatasetClauseParserRuleCall_3_0()); 
            pushFollow(FOLLOW_ruleDatasetClause_in_rule__SelectQuery__DatasetClausesAssignment_38229);
            ruleDatasetClause();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getDatasetClausesDatasetClauseParserRuleCall_3_0()); 

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
    // $ANTLR end "rule__SelectQuery__DatasetClausesAssignment_3"


    // $ANTLR start "rule__SelectQuery__IsDistinctAssignment_5_0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4107:1: rule__SelectQuery__IsDistinctAssignment_5_0 : ( ( 'DISTINCT' ) ) ;
    public final void rule__SelectQuery__IsDistinctAssignment_5_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4111:1: ( ( ( 'DISTINCT' ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4112:1: ( ( 'DISTINCT' ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4112:1: ( ( 'DISTINCT' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4113:1: ( 'DISTINCT' )
            {
             before(grammarAccess.getSelectQueryAccess().getIsDistinctDISTINCTKeyword_5_0_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4114:1: ( 'DISTINCT' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4115:1: 'DISTINCT'
            {
             before(grammarAccess.getSelectQueryAccess().getIsDistinctDISTINCTKeyword_5_0_0()); 
            match(input,52,FOLLOW_52_in_rule__SelectQuery__IsDistinctAssignment_5_08265); 
             after(grammarAccess.getSelectQueryAccess().getIsDistinctDISTINCTKeyword_5_0_0()); 

            }

             after(grammarAccess.getSelectQueryAccess().getIsDistinctDISTINCTKeyword_5_0_0()); 

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
    // $ANTLR end "rule__SelectQuery__IsDistinctAssignment_5_0"


    // $ANTLR start "rule__SelectQuery__IsReducedAssignment_5_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4130:1: rule__SelectQuery__IsReducedAssignment_5_1 : ( ( 'REDUCED' ) ) ;
    public final void rule__SelectQuery__IsReducedAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4134:1: ( ( ( 'REDUCED' ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4135:1: ( ( 'REDUCED' ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4135:1: ( ( 'REDUCED' ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4136:1: ( 'REDUCED' )
            {
             before(grammarAccess.getSelectQueryAccess().getIsReducedREDUCEDKeyword_5_1_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4137:1: ( 'REDUCED' )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4138:1: 'REDUCED'
            {
             before(grammarAccess.getSelectQueryAccess().getIsReducedREDUCEDKeyword_5_1_0()); 
            match(input,53,FOLLOW_53_in_rule__SelectQuery__IsReducedAssignment_5_18309); 
             after(grammarAccess.getSelectQueryAccess().getIsReducedREDUCEDKeyword_5_1_0()); 

            }

             after(grammarAccess.getSelectQueryAccess().getIsReducedREDUCEDKeyword_5_1_0()); 

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
    // $ANTLR end "rule__SelectQuery__IsReducedAssignment_5_1"


    // $ANTLR start "rule__SelectQuery__VariablesAssignment_6"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4153:1: rule__SelectQuery__VariablesAssignment_6 : ( ruleVariable ) ;
    public final void rule__SelectQuery__VariablesAssignment_6() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4157:1: ( ( ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4158:1: ( ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4158:1: ( ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4159:1: ruleVariable
            {
             before(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_6_0()); 
            pushFollow(FOLLOW_ruleVariable_in_rule__SelectQuery__VariablesAssignment_68348);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_6_0()); 

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
    // $ANTLR end "rule__SelectQuery__VariablesAssignment_6"


    // $ANTLR start "rule__SelectQuery__VariablesAssignment_7"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4168:1: rule__SelectQuery__VariablesAssignment_7 : ( ruleVariable ) ;
    public final void rule__SelectQuery__VariablesAssignment_7() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4172:1: ( ( ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4173:1: ( ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4173:1: ( ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4174:1: ruleVariable
            {
             before(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_7_0()); 
            pushFollow(FOLLOW_ruleVariable_in_rule__SelectQuery__VariablesAssignment_78379);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_7_0()); 

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
    // $ANTLR end "rule__SelectQuery__VariablesAssignment_7"


    // $ANTLR start "rule__SelectQuery__WhereClauseAssignment_8"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4183:1: rule__SelectQuery__WhereClauseAssignment_8 : ( ruleWhereClause ) ;
    public final void rule__SelectQuery__WhereClauseAssignment_8() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4187:1: ( ( ruleWhereClause ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4188:1: ( ruleWhereClause )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4188:1: ( ruleWhereClause )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4189:1: ruleWhereClause
            {
             before(grammarAccess.getSelectQueryAccess().getWhereClauseWhereClauseParserRuleCall_8_0()); 
            pushFollow(FOLLOW_ruleWhereClause_in_rule__SelectQuery__WhereClauseAssignment_88410);
            ruleWhereClause();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getWhereClauseWhereClauseParserRuleCall_8_0()); 

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
    // $ANTLR end "rule__SelectQuery__WhereClauseAssignment_8"


    // $ANTLR start "rule__SelectQuery__FilterclauseAssignment_9"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4198:1: rule__SelectQuery__FilterclauseAssignment_9 : ( ruleFilterclause ) ;
    public final void rule__SelectQuery__FilterclauseAssignment_9() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4202:1: ( ( ruleFilterclause ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4203:1: ( ruleFilterclause )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4203:1: ( ruleFilterclause )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4204:1: ruleFilterclause
            {
             before(grammarAccess.getSelectQueryAccess().getFilterclauseFilterclauseParserRuleCall_9_0()); 
            pushFollow(FOLLOW_ruleFilterclause_in_rule__SelectQuery__FilterclauseAssignment_98441);
            ruleFilterclause();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getFilterclauseFilterclauseParserRuleCall_9_0()); 

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
    // $ANTLR end "rule__SelectQuery__FilterclauseAssignment_9"


    // $ANTLR start "rule__SelectQuery__AggregateClauseAssignment_10"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4213:1: rule__SelectQuery__AggregateClauseAssignment_10 : ( ruleAggregate ) ;
    public final void rule__SelectQuery__AggregateClauseAssignment_10() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4217:1: ( ( ruleAggregate ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4218:1: ( ruleAggregate )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4218:1: ( ruleAggregate )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4219:1: ruleAggregate
            {
             before(grammarAccess.getSelectQueryAccess().getAggregateClauseAggregateParserRuleCall_10_0()); 
            pushFollow(FOLLOW_ruleAggregate_in_rule__SelectQuery__AggregateClauseAssignment_108472);
            ruleAggregate();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getAggregateClauseAggregateParserRuleCall_10_0()); 

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
    // $ANTLR end "rule__SelectQuery__AggregateClauseAssignment_10"


    // $ANTLR start "rule__SelectQuery__FilesinkclauseAssignment_11"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4228:1: rule__SelectQuery__FilesinkclauseAssignment_11 : ( ruleFilesinkclause ) ;
    public final void rule__SelectQuery__FilesinkclauseAssignment_11() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4232:1: ( ( ruleFilesinkclause ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4233:1: ( ruleFilesinkclause )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4233:1: ( ruleFilesinkclause )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4234:1: ruleFilesinkclause
            {
             before(grammarAccess.getSelectQueryAccess().getFilesinkclauseFilesinkclauseParserRuleCall_11_0()); 
            pushFollow(FOLLOW_ruleFilesinkclause_in_rule__SelectQuery__FilesinkclauseAssignment_118503);
            ruleFilesinkclause();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getFilesinkclauseFilesinkclauseParserRuleCall_11_0()); 

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
    // $ANTLR end "rule__SelectQuery__FilesinkclauseAssignment_11"


    // $ANTLR start "rule__Aggregate__AggregationsAssignment_1_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4243:1: rule__Aggregate__AggregationsAssignment_1_1 : ( ruleAggregation ) ;
    public final void rule__Aggregate__AggregationsAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4247:1: ( ( ruleAggregation ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4248:1: ( ruleAggregation )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4248:1: ( ruleAggregation )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4249:1: ruleAggregation
            {
             before(grammarAccess.getAggregateAccess().getAggregationsAggregationParserRuleCall_1_1_0()); 
            pushFollow(FOLLOW_ruleAggregation_in_rule__Aggregate__AggregationsAssignment_1_18534);
            ruleAggregation();

            state._fsp--;

             after(grammarAccess.getAggregateAccess().getAggregationsAggregationParserRuleCall_1_1_0()); 

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
    // $ANTLR end "rule__Aggregate__AggregationsAssignment_1_1"


    // $ANTLR start "rule__Aggregate__GroupbyAssignment_2_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4258:1: rule__Aggregate__GroupbyAssignment_2_1 : ( ruleGroupBy ) ;
    public final void rule__Aggregate__GroupbyAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4262:1: ( ( ruleGroupBy ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4263:1: ( ruleGroupBy )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4263:1: ( ruleGroupBy )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4264:1: ruleGroupBy
            {
             before(grammarAccess.getAggregateAccess().getGroupbyGroupByParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_ruleGroupBy_in_rule__Aggregate__GroupbyAssignment_2_18565);
            ruleGroupBy();

            state._fsp--;

             after(grammarAccess.getAggregateAccess().getGroupbyGroupByParserRuleCall_2_1_0()); 

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
    // $ANTLR end "rule__Aggregate__GroupbyAssignment_2_1"


    // $ANTLR start "rule__GroupBy__VariablesAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4273:1: rule__GroupBy__VariablesAssignment_1 : ( ruleVariable ) ;
    public final void rule__GroupBy__VariablesAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4277:1: ( ( ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4278:1: ( ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4278:1: ( ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4279:1: ruleVariable
            {
             before(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleVariable_in_rule__GroupBy__VariablesAssignment_18596);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__GroupBy__VariablesAssignment_1"


    // $ANTLR start "rule__GroupBy__VariablesAssignment_2_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4288:1: rule__GroupBy__VariablesAssignment_2_1 : ( ruleVariable ) ;
    public final void rule__GroupBy__VariablesAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4292:1: ( ( ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4293:1: ( ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4293:1: ( ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4294:1: ruleVariable
            {
             before(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_ruleVariable_in_rule__GroupBy__VariablesAssignment_2_18627);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_2_1_0()); 

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
    // $ANTLR end "rule__GroupBy__VariablesAssignment_2_1"


    // $ANTLR start "rule__Aggregation__FunctionAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4303:1: rule__Aggregation__FunctionAssignment_1 : ( RULE_AGG_FUNCTION ) ;
    public final void rule__Aggregation__FunctionAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4307:1: ( ( RULE_AGG_FUNCTION ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4308:1: ( RULE_AGG_FUNCTION )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4308:1: ( RULE_AGG_FUNCTION )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4309:1: RULE_AGG_FUNCTION
            {
             before(grammarAccess.getAggregationAccess().getFunctionAGG_FUNCTIONTerminalRuleCall_1_0()); 
            match(input,RULE_AGG_FUNCTION,FOLLOW_RULE_AGG_FUNCTION_in_rule__Aggregation__FunctionAssignment_18658); 
             after(grammarAccess.getAggregationAccess().getFunctionAGG_FUNCTIONTerminalRuleCall_1_0()); 

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
    // $ANTLR end "rule__Aggregation__FunctionAssignment_1"


    // $ANTLR start "rule__Aggregation__VarToAggAssignment_3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4318:1: rule__Aggregation__VarToAggAssignment_3 : ( ruleVariable ) ;
    public final void rule__Aggregation__VarToAggAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4322:1: ( ( ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4323:1: ( ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4323:1: ( ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4324:1: ruleVariable
            {
             before(grammarAccess.getAggregationAccess().getVarToAggVariableParserRuleCall_3_0()); 
            pushFollow(FOLLOW_ruleVariable_in_rule__Aggregation__VarToAggAssignment_38689);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getAggregationAccess().getVarToAggVariableParserRuleCall_3_0()); 

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
    // $ANTLR end "rule__Aggregation__VarToAggAssignment_3"


    // $ANTLR start "rule__Aggregation__AggNameAssignment_5"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4333:1: rule__Aggregation__AggNameAssignment_5 : ( RULE_STRING ) ;
    public final void rule__Aggregation__AggNameAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4337:1: ( ( RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4338:1: ( RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4338:1: ( RULE_STRING )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4339:1: RULE_STRING
            {
             before(grammarAccess.getAggregationAccess().getAggNameSTRINGTerminalRuleCall_5_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Aggregation__AggNameAssignment_58720); 
             after(grammarAccess.getAggregationAccess().getAggNameSTRINGTerminalRuleCall_5_0()); 

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
    // $ANTLR end "rule__Aggregation__AggNameAssignment_5"


    // $ANTLR start "rule__Aggregation__DatatypeAssignment_6_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4348:1: rule__Aggregation__DatatypeAssignment_6_1 : ( RULE_STRING ) ;
    public final void rule__Aggregation__DatatypeAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4352:1: ( ( RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4353:1: ( RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4353:1: ( RULE_STRING )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4354:1: RULE_STRING
            {
             before(grammarAccess.getAggregationAccess().getDatatypeSTRINGTerminalRuleCall_6_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Aggregation__DatatypeAssignment_6_18751); 
             after(grammarAccess.getAggregationAccess().getDatatypeSTRINGTerminalRuleCall_6_1_0()); 

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
    // $ANTLR end "rule__Aggregation__DatatypeAssignment_6_1"


    // $ANTLR start "rule__Filesinkclause__PathAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4363:1: rule__Filesinkclause__PathAssignment_1 : ( RULE_STRING ) ;
    public final void rule__Filesinkclause__PathAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4367:1: ( ( RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4368:1: ( RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4368:1: ( RULE_STRING )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4369:1: RULE_STRING
            {
             before(grammarAccess.getFilesinkclauseAccess().getPathSTRINGTerminalRuleCall_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Filesinkclause__PathAssignment_18782); 
             after(grammarAccess.getFilesinkclauseAccess().getPathSTRINGTerminalRuleCall_1_0()); 

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
    // $ANTLR end "rule__Filesinkclause__PathAssignment_1"


    // $ANTLR start "rule__Filterclause__LeftAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4378:1: rule__Filterclause__LeftAssignment_1 : ( ruleVariable ) ;
    public final void rule__Filterclause__LeftAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4382:1: ( ( ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4383:1: ( ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4383:1: ( ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4384:1: ruleVariable
            {
             before(grammarAccess.getFilterclauseAccess().getLeftVariableParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleVariable_in_rule__Filterclause__LeftAssignment_18813);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getFilterclauseAccess().getLeftVariableParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__Filterclause__LeftAssignment_1"


    // $ANTLR start "rule__Filterclause__OperatorAssignment_2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4393:1: rule__Filterclause__OperatorAssignment_2 : ( ruleOperator ) ;
    public final void rule__Filterclause__OperatorAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4397:1: ( ( ruleOperator ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4398:1: ( ruleOperator )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4398:1: ( ruleOperator )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4399:1: ruleOperator
            {
             before(grammarAccess.getFilterclauseAccess().getOperatorOperatorEnumRuleCall_2_0()); 
            pushFollow(FOLLOW_ruleOperator_in_rule__Filterclause__OperatorAssignment_28844);
            ruleOperator();

            state._fsp--;

             after(grammarAccess.getFilterclauseAccess().getOperatorOperatorEnumRuleCall_2_0()); 

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
    // $ANTLR end "rule__Filterclause__OperatorAssignment_2"


    // $ANTLR start "rule__Filterclause__RightAssignment_3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4408:1: rule__Filterclause__RightAssignment_3 : ( ruleVariable ) ;
    public final void rule__Filterclause__RightAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4412:1: ( ( ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4413:1: ( ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4413:1: ( ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4414:1: ruleVariable
            {
             before(grammarAccess.getFilterclauseAccess().getRightVariableParserRuleCall_3_0()); 
            pushFollow(FOLLOW_ruleVariable_in_rule__Filterclause__RightAssignment_38875);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getFilterclauseAccess().getRightVariableParserRuleCall_3_0()); 

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
    // $ANTLR end "rule__Filterclause__RightAssignment_3"


    // $ANTLR start "rule__DatasetClause__DataSetAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4425:1: rule__DatasetClause__DataSetAssignment_1 : ( ruleIRI ) ;
    public final void rule__DatasetClause__DataSetAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4429:1: ( ( ruleIRI ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4430:1: ( ruleIRI )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4430:1: ( ruleIRI )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4431:1: ruleIRI
            {
             before(grammarAccess.getDatasetClauseAccess().getDataSetIRIParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleIRI_in_rule__DatasetClause__DataSetAssignment_18908);
            ruleIRI();

            state._fsp--;

             after(grammarAccess.getDatasetClauseAccess().getDataSetIRIParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__DatasetClause__DataSetAssignment_1"


    // $ANTLR start "rule__DatasetClause__NameAssignment_3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4440:1: rule__DatasetClause__NameAssignment_3 : ( RULE_ID ) ;
    public final void rule__DatasetClause__NameAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4444:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4445:1: ( RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4445:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4446:1: RULE_ID
            {
             before(grammarAccess.getDatasetClauseAccess().getNameIDTerminalRuleCall_3_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__DatasetClause__NameAssignment_38939); 
             after(grammarAccess.getDatasetClauseAccess().getNameIDTerminalRuleCall_3_0()); 

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
    // $ANTLR end "rule__DatasetClause__NameAssignment_3"


    // $ANTLR start "rule__DatasetClause__TypeAssignment_4_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4455:1: rule__DatasetClause__TypeAssignment_4_1 : ( RULE_WINDOWTYPE ) ;
    public final void rule__DatasetClause__TypeAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4459:1: ( ( RULE_WINDOWTYPE ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4460:1: ( RULE_WINDOWTYPE )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4460:1: ( RULE_WINDOWTYPE )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4461:1: RULE_WINDOWTYPE
            {
             before(grammarAccess.getDatasetClauseAccess().getTypeWINDOWTYPETerminalRuleCall_4_1_0()); 
            match(input,RULE_WINDOWTYPE,FOLLOW_RULE_WINDOWTYPE_in_rule__DatasetClause__TypeAssignment_4_18970); 
             after(grammarAccess.getDatasetClauseAccess().getTypeWINDOWTYPETerminalRuleCall_4_1_0()); 

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
    // $ANTLR end "rule__DatasetClause__TypeAssignment_4_1"


    // $ANTLR start "rule__DatasetClause__SizeAssignment_4_3"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4470:1: rule__DatasetClause__SizeAssignment_4_3 : ( RULE_INT ) ;
    public final void rule__DatasetClause__SizeAssignment_4_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4474:1: ( ( RULE_INT ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4475:1: ( RULE_INT )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4475:1: ( RULE_INT )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4476:1: RULE_INT
            {
             before(grammarAccess.getDatasetClauseAccess().getSizeINTTerminalRuleCall_4_3_0()); 
            match(input,RULE_INT,FOLLOW_RULE_INT_in_rule__DatasetClause__SizeAssignment_4_39001); 
             after(grammarAccess.getDatasetClauseAccess().getSizeINTTerminalRuleCall_4_3_0()); 

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
    // $ANTLR end "rule__DatasetClause__SizeAssignment_4_3"


    // $ANTLR start "rule__DatasetClause__AdvanceAssignment_4_4_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4485:1: rule__DatasetClause__AdvanceAssignment_4_4_1 : ( RULE_INT ) ;
    public final void rule__DatasetClause__AdvanceAssignment_4_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4489:1: ( ( RULE_INT ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4490:1: ( RULE_INT )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4490:1: ( RULE_INT )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4491:1: RULE_INT
            {
             before(grammarAccess.getDatasetClauseAccess().getAdvanceINTTerminalRuleCall_4_4_1_0()); 
            match(input,RULE_INT,FOLLOW_RULE_INT_in_rule__DatasetClause__AdvanceAssignment_4_4_19032); 
             after(grammarAccess.getDatasetClauseAccess().getAdvanceINTTerminalRuleCall_4_4_1_0()); 

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
    // $ANTLR end "rule__DatasetClause__AdvanceAssignment_4_4_1"


    // $ANTLR start "rule__DatasetClause__UnitAssignment_4_5_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4500:1: rule__DatasetClause__UnitAssignment_4_5_1 : ( RULE_UNITTYPE ) ;
    public final void rule__DatasetClause__UnitAssignment_4_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4504:1: ( ( RULE_UNITTYPE ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4505:1: ( RULE_UNITTYPE )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4505:1: ( RULE_UNITTYPE )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4506:1: RULE_UNITTYPE
            {
             before(grammarAccess.getDatasetClauseAccess().getUnitUNITTYPETerminalRuleCall_4_5_1_0()); 
            match(input,RULE_UNITTYPE,FOLLOW_RULE_UNITTYPE_in_rule__DatasetClause__UnitAssignment_4_5_19063); 
             after(grammarAccess.getDatasetClauseAccess().getUnitUNITTYPETerminalRuleCall_4_5_1_0()); 

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
    // $ANTLR end "rule__DatasetClause__UnitAssignment_4_5_1"


    // $ANTLR start "rule__WhereClause__WhereclausesAssignment_2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4515:1: rule__WhereClause__WhereclausesAssignment_2 : ( ruleInnerWhereClause ) ;
    public final void rule__WhereClause__WhereclausesAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4519:1: ( ( ruleInnerWhereClause ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4520:1: ( ruleInnerWhereClause )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4520:1: ( ruleInnerWhereClause )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4521:1: ruleInnerWhereClause
            {
             before(grammarAccess.getWhereClauseAccess().getWhereclausesInnerWhereClauseParserRuleCall_2_0()); 
            pushFollow(FOLLOW_ruleInnerWhereClause_in_rule__WhereClause__WhereclausesAssignment_29094);
            ruleInnerWhereClause();

            state._fsp--;

             after(grammarAccess.getWhereClauseAccess().getWhereclausesInnerWhereClauseParserRuleCall_2_0()); 

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
    // $ANTLR end "rule__WhereClause__WhereclausesAssignment_2"


    // $ANTLR start "rule__InnerWhereClause__NameAssignment_0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4530:1: rule__InnerWhereClause__NameAssignment_0 : ( ( RULE_ID ) ) ;
    public final void rule__InnerWhereClause__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4534:1: ( ( ( RULE_ID ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4535:1: ( ( RULE_ID ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4535:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4536:1: ( RULE_ID )
            {
             before(grammarAccess.getInnerWhereClauseAccess().getNameDatasetClauseCrossReference_0_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4537:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4538:1: RULE_ID
            {
             before(grammarAccess.getInnerWhereClauseAccess().getNameDatasetClauseIDTerminalRuleCall_0_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__InnerWhereClause__NameAssignment_09129); 
             after(grammarAccess.getInnerWhereClauseAccess().getNameDatasetClauseIDTerminalRuleCall_0_0_1()); 

            }

             after(grammarAccess.getInnerWhereClauseAccess().getNameDatasetClauseCrossReference_0_0()); 

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
    // $ANTLR end "rule__InnerWhereClause__NameAssignment_0"


    // $ANTLR start "rule__InnerWhereClause__GroupGraphPatternAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4549:1: rule__InnerWhereClause__GroupGraphPatternAssignment_1 : ( ruleGroupGraphPatternSub ) ;
    public final void rule__InnerWhereClause__GroupGraphPatternAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4553:1: ( ( ruleGroupGraphPatternSub ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4554:1: ( ruleGroupGraphPatternSub )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4554:1: ( ruleGroupGraphPatternSub )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4555:1: ruleGroupGraphPatternSub
            {
             before(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternGroupGraphPatternSubParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleGroupGraphPatternSub_in_rule__InnerWhereClause__GroupGraphPatternAssignment_19164);
            ruleGroupGraphPatternSub();

            state._fsp--;

             after(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternGroupGraphPatternSubParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__InnerWhereClause__GroupGraphPatternAssignment_1"


    // $ANTLR start "rule__GroupGraphPatternSub__GraphPatternsAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4564:1: rule__GroupGraphPatternSub__GraphPatternsAssignment_1 : ( ruleTriplesSameSubject ) ;
    public final void rule__GroupGraphPatternSub__GraphPatternsAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4568:1: ( ( ruleTriplesSameSubject ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4569:1: ( ruleTriplesSameSubject )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4569:1: ( ruleTriplesSameSubject )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4570:1: ruleTriplesSameSubject
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleTriplesSameSubject_in_rule__GroupGraphPatternSub__GraphPatternsAssignment_19195);
            ruleTriplesSameSubject();

            state._fsp--;

             after(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__GroupGraphPatternSub__GraphPatternsAssignment_1"


    // $ANTLR start "rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4579:1: rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 : ( ruleTriplesSameSubject ) ;
    public final void rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4583:1: ( ( ruleTriplesSameSubject ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4584:1: ( ruleTriplesSameSubject )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4584:1: ( ruleTriplesSameSubject )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4585:1: ruleTriplesSameSubject
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_ruleTriplesSameSubject_in_rule__GroupGraphPatternSub__GraphPatternsAssignment_2_19226);
            ruleTriplesSameSubject();

            state._fsp--;

             after(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_2_1_0()); 

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
    // $ANTLR end "rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1"


    // $ANTLR start "rule__TriplesSameSubject__SubjectAssignment_0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4594:1: rule__TriplesSameSubject__SubjectAssignment_0 : ( ruleGraphNode ) ;
    public final void rule__TriplesSameSubject__SubjectAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4598:1: ( ( ruleGraphNode ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4599:1: ( ruleGraphNode )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4599:1: ( ruleGraphNode )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4600:1: ruleGraphNode
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getSubjectGraphNodeParserRuleCall_0_0()); 
            pushFollow(FOLLOW_ruleGraphNode_in_rule__TriplesSameSubject__SubjectAssignment_09257);
            ruleGraphNode();

            state._fsp--;

             after(grammarAccess.getTriplesSameSubjectAccess().getSubjectGraphNodeParserRuleCall_0_0()); 

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
    // $ANTLR end "rule__TriplesSameSubject__SubjectAssignment_0"


    // $ANTLR start "rule__TriplesSameSubject__PropertyListAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4609:1: rule__TriplesSameSubject__PropertyListAssignment_1 : ( rulePropertyList ) ;
    public final void rule__TriplesSameSubject__PropertyListAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4613:1: ( ( rulePropertyList ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4614:1: ( rulePropertyList )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4614:1: ( rulePropertyList )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4615:1: rulePropertyList
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_1_0()); 
            pushFollow(FOLLOW_rulePropertyList_in_rule__TriplesSameSubject__PropertyListAssignment_19288);
            rulePropertyList();

            state._fsp--;

             after(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__TriplesSameSubject__PropertyListAssignment_1"


    // $ANTLR start "rule__TriplesSameSubject__PropertyListAssignment_2_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4624:1: rule__TriplesSameSubject__PropertyListAssignment_2_1 : ( rulePropertyList ) ;
    public final void rule__TriplesSameSubject__PropertyListAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4628:1: ( ( rulePropertyList ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4629:1: ( rulePropertyList )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4629:1: ( rulePropertyList )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4630:1: rulePropertyList
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_rulePropertyList_in_rule__TriplesSameSubject__PropertyListAssignment_2_19319);
            rulePropertyList();

            state._fsp--;

             after(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_2_1_0()); 

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
    // $ANTLR end "rule__TriplesSameSubject__PropertyListAssignment_2_1"


    // $ANTLR start "rule__PropertyList__PropertyAssignment_0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4639:1: rule__PropertyList__PropertyAssignment_0 : ( ruleGraphNode ) ;
    public final void rule__PropertyList__PropertyAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4643:1: ( ( ruleGraphNode ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4644:1: ( ruleGraphNode )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4644:1: ( ruleGraphNode )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4645:1: ruleGraphNode
            {
             before(grammarAccess.getPropertyListAccess().getPropertyGraphNodeParserRuleCall_0_0()); 
            pushFollow(FOLLOW_ruleGraphNode_in_rule__PropertyList__PropertyAssignment_09350);
            ruleGraphNode();

            state._fsp--;

             after(grammarAccess.getPropertyListAccess().getPropertyGraphNodeParserRuleCall_0_0()); 

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
    // $ANTLR end "rule__PropertyList__PropertyAssignment_0"


    // $ANTLR start "rule__PropertyList__ObjectAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4654:1: rule__PropertyList__ObjectAssignment_1 : ( ruleGraphNode ) ;
    public final void rule__PropertyList__ObjectAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4658:1: ( ( ruleGraphNode ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4659:1: ( ruleGraphNode )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4659:1: ( ruleGraphNode )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4660:1: ruleGraphNode
            {
             before(grammarAccess.getPropertyListAccess().getObjectGraphNodeParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleGraphNode_in_rule__PropertyList__ObjectAssignment_19381);
            ruleGraphNode();

            state._fsp--;

             after(grammarAccess.getPropertyListAccess().getObjectGraphNodeParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__PropertyList__ObjectAssignment_1"


    // $ANTLR start "rule__GraphNode__VariableAssignment_0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4669:1: rule__GraphNode__VariableAssignment_0 : ( ruleVariable ) ;
    public final void rule__GraphNode__VariableAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4673:1: ( ( ruleVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4674:1: ( ruleVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4674:1: ( ruleVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4675:1: ruleVariable
            {
             before(grammarAccess.getGraphNodeAccess().getVariableVariableParserRuleCall_0_0()); 
            pushFollow(FOLLOW_ruleVariable_in_rule__GraphNode__VariableAssignment_09412);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getGraphNodeAccess().getVariableVariableParserRuleCall_0_0()); 

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
    // $ANTLR end "rule__GraphNode__VariableAssignment_0"


    // $ANTLR start "rule__GraphNode__LiteralAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4684:1: rule__GraphNode__LiteralAssignment_1 : ( RULE_STRING ) ;
    public final void rule__GraphNode__LiteralAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4688:1: ( ( RULE_STRING ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4689:1: ( RULE_STRING )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4689:1: ( RULE_STRING )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4690:1: RULE_STRING
            {
             before(grammarAccess.getGraphNodeAccess().getLiteralSTRINGTerminalRuleCall_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__GraphNode__LiteralAssignment_19443); 
             after(grammarAccess.getGraphNodeAccess().getLiteralSTRINGTerminalRuleCall_1_0()); 

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
    // $ANTLR end "rule__GraphNode__LiteralAssignment_1"


    // $ANTLR start "rule__GraphNode__IriAssignment_2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4699:1: rule__GraphNode__IriAssignment_2 : ( ruleIRI ) ;
    public final void rule__GraphNode__IriAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4703:1: ( ( ruleIRI ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4704:1: ( ruleIRI )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4704:1: ( ruleIRI )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4705:1: ruleIRI
            {
             before(grammarAccess.getGraphNodeAccess().getIriIRIParserRuleCall_2_0()); 
            pushFollow(FOLLOW_ruleIRI_in_rule__GraphNode__IriAssignment_29474);
            ruleIRI();

            state._fsp--;

             after(grammarAccess.getGraphNodeAccess().getIriIRIParserRuleCall_2_0()); 

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
    // $ANTLR end "rule__GraphNode__IriAssignment_2"


    // $ANTLR start "rule__Variable__UnnamedAssignment_0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4714:1: rule__Variable__UnnamedAssignment_0 : ( ruleUnNamedVariable ) ;
    public final void rule__Variable__UnnamedAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4718:1: ( ( ruleUnNamedVariable ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4719:1: ( ruleUnNamedVariable )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4719:1: ( ruleUnNamedVariable )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4720:1: ruleUnNamedVariable
            {
             before(grammarAccess.getVariableAccess().getUnnamedUnNamedVariableParserRuleCall_0_0()); 
            pushFollow(FOLLOW_ruleUnNamedVariable_in_rule__Variable__UnnamedAssignment_09505);
            ruleUnNamedVariable();

            state._fsp--;

             after(grammarAccess.getVariableAccess().getUnnamedUnNamedVariableParserRuleCall_0_0()); 

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
    // $ANTLR end "rule__Variable__UnnamedAssignment_0"


    // $ANTLR start "rule__Variable__PropertyAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4729:1: rule__Variable__PropertyAssignment_1 : ( ruleProperty ) ;
    public final void rule__Variable__PropertyAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4733:1: ( ( ruleProperty ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4734:1: ( ruleProperty )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4734:1: ( ruleProperty )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4735:1: ruleProperty
            {
             before(grammarAccess.getVariableAccess().getPropertyPropertyParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleProperty_in_rule__Variable__PropertyAssignment_19536);
            ruleProperty();

            state._fsp--;

             after(grammarAccess.getVariableAccess().getPropertyPropertyParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__Variable__PropertyAssignment_1"


    // $ANTLR start "rule__UnNamedVariable__NameAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4744:1: rule__UnNamedVariable__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__UnNamedVariable__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4748:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4749:1: ( RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4749:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4750:1: RULE_ID
            {
             before(grammarAccess.getUnNamedVariableAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__UnNamedVariable__NameAssignment_19567); 
             after(grammarAccess.getUnNamedVariableAccess().getNameIDTerminalRuleCall_1_0()); 

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
    // $ANTLR end "rule__UnNamedVariable__NameAssignment_1"


    // $ANTLR start "rule__Property__PrefixAssignment_0"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4759:1: rule__Property__PrefixAssignment_0 : ( ( RULE_ID ) ) ;
    public final void rule__Property__PrefixAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4763:1: ( ( ( RULE_ID ) ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4764:1: ( ( RULE_ID ) )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4764:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4765:1: ( RULE_ID )
            {
             before(grammarAccess.getPropertyAccess().getPrefixPrefixCrossReference_0_0()); 
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4766:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4767:1: RULE_ID
            {
             before(grammarAccess.getPropertyAccess().getPrefixPrefixIDTerminalRuleCall_0_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Property__PrefixAssignment_09602); 
             after(grammarAccess.getPropertyAccess().getPrefixPrefixIDTerminalRuleCall_0_0_1()); 

            }

             after(grammarAccess.getPropertyAccess().getPrefixPrefixCrossReference_0_0()); 

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
    // $ANTLR end "rule__Property__PrefixAssignment_0"


    // $ANTLR start "rule__Property__NameAssignment_2"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4778:1: rule__Property__NameAssignment_2 : ( RULE_ID ) ;
    public final void rule__Property__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4782:1: ( ( RULE_ID ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4783:1: ( RULE_ID )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4783:1: ( RULE_ID )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4784:1: RULE_ID
            {
             before(grammarAccess.getPropertyAccess().getNameIDTerminalRuleCall_2_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Property__NameAssignment_29637); 
             after(grammarAccess.getPropertyAccess().getNameIDTerminalRuleCall_2_0()); 

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
    // $ANTLR end "rule__Property__NameAssignment_2"


    // $ANTLR start "rule__IRI__ValueAssignment_1"
    // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4793:1: rule__IRI__ValueAssignment_1 : ( RULE_IRI_TERMINAL ) ;
    public final void rule__IRI__ValueAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4797:1: ( ( RULE_IRI_TERMINAL ) )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4798:1: ( RULE_IRI_TERMINAL )
            {
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4798:1: ( RULE_IRI_TERMINAL )
            // ../de.uniol.inf.is.odysseus.server.streamingsparql.ui/src-gen/de/uniol/inf/is/odysseus/server/ui/contentassist/antlr/internal/InternalStreamingsparql.g:4799:1: RULE_IRI_TERMINAL
            {
             before(grammarAccess.getIRIAccess().getValueIRI_TERMINALTerminalRuleCall_1_0()); 
            match(input,RULE_IRI_TERMINAL,FOLLOW_RULE_IRI_TERMINAL_in_rule__IRI__ValueAssignment_19668); 
             after(grammarAccess.getIRIAccess().getValueIRI_TERMINALTerminalRuleCall_1_0()); 

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
    // $ANTLR end "rule__IRI__ValueAssignment_1"

    // Delegated rules


 

    public static final BitSet FOLLOW_ruleSPARQLQuery_in_entryRuleSPARQLQuery61 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSPARQLQuery68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelectQuery_in_ruleSPARQLQuery94 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rulePrefix_in_entryRulePrefix120 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRulePrefix127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Prefix__Alternatives_in_rulePrefix153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUnNamedPrefix_in_entryRuleUnNamedPrefix180 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleUnNamedPrefix187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UnNamedPrefix__Group__0_in_ruleUnNamedPrefix213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBase_in_entryRuleBase240 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBase247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Base__Group__0_in_ruleBase273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSelectQuery_in_entryRuleSelectQuery300 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSelectQuery307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__0_in_ruleSelectQuery333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAggregate_in_entryRuleAggregate360 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAggregate367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group__0_in_ruleAggregate393 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupBy_in_entryRuleGroupBy420 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroupBy427 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupBy__Group__0_in_ruleGroupBy453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAggregation_in_entryRuleAggregation480 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAggregation487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__0_in_ruleAggregation513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFilesinkclause_in_entryRuleFilesinkclause540 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFilesinkclause547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filesinkclause__Group__0_in_ruleFilesinkclause573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFilterclause_in_entryRuleFilterclause600 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFilterclause607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__0_in_ruleFilterclause633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDatasetClause_in_entryRuleDatasetClause662 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDatasetClause669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__0_in_ruleDatasetClause695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWhereClause_in_entryRuleWhereClause722 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWhereClause729 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__WhereClause__Group__0_in_ruleWhereClause755 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleInnerWhereClause_in_entryRuleInnerWhereClause782 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleInnerWhereClause789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__InnerWhereClause__Group__0_in_ruleInnerWhereClause815 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupGraphPatternSub_in_entryRuleGroupGraphPatternSub842 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGroupGraphPatternSub849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__0_in_ruleGroupGraphPatternSub875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTriplesSameSubject_in_entryRuleTriplesSameSubject902 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTriplesSameSubject909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group__0_in_ruleTriplesSameSubject935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rulePropertyList_in_entryRulePropertyList962 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRulePropertyList969 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__PropertyList__Group__0_in_rulePropertyList995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGraphNode_in_entryRuleGraphNode1022 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleGraphNode1029 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GraphNode__Alternatives_in_ruleGraphNode1055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_entryRuleVariable1082 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleVariable1089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Variable__Alternatives_in_ruleVariable1115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUnNamedVariable_in_entryRuleUnNamedVariable1142 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleUnNamedVariable1149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UnNamedVariable__Group__0_in_ruleUnNamedVariable1175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleProperty_in_entryRuleProperty1202 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleProperty1209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Property__Group__0_in_ruleProperty1235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleIRI_in_entryRuleIRI1262 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleIRI1269 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__IRI__Group__0_in_ruleIRI1295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Alternatives_in_ruleOperator1336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Prefix__Group_0__0_in_rule__Prefix__Alternatives1371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUnNamedPrefix_in_rule__Prefix__Alternatives1389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__MethodAssignment_0_0_in_rule__SelectQuery__Alternatives_01421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__SelectQuery__Alternatives_01440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__IsDistinctAssignment_5_0_in_rule__SelectQuery__Alternatives_51474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__IsReducedAssignment_5_1_in_rule__SelectQuery__Alternatives_51492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GraphNode__VariableAssignment_0_in_rule__GraphNode__Alternatives1525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GraphNode__LiteralAssignment_1_in_rule__GraphNode__Alternatives1543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GraphNode__IriAssignment_2_in_rule__GraphNode__Alternatives1561 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Variable__UnnamedAssignment_0_in_rule__Variable__Alternatives1594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Variable__PropertyAssignment_1_in_rule__Variable__Alternatives1612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Operator__Alternatives1646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Operator__Alternatives1667 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Operator__Alternatives1688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__Operator__Alternatives1709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__Operator__Alternatives1730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__Operator__Alternatives1751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__Operator__Alternatives1772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__Operator__Alternatives1793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__Operator__Alternatives1814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__Operator__Alternatives1835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Prefix__Group_0__0__Impl_in_rule__Prefix__Group_0__01868 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Prefix__Group_0__1_in_rule__Prefix__Group_0__01871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__Prefix__Group_0__0__Impl1899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Prefix__Group_0__1__Impl_in_rule__Prefix__Group_0__11930 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_rule__Prefix__Group_0__2_in_rule__Prefix__Group_0__11933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Prefix__NameAssignment_0_1_in_rule__Prefix__Group_0__1__Impl1960 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Prefix__Group_0__2__Impl_in_rule__Prefix__Group_0__21990 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Prefix__Group_0__3_in_rule__Prefix__Group_0__21993 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__Prefix__Group_0__2__Impl2021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Prefix__Group_0__3__Impl_in_rule__Prefix__Group_0__32052 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Prefix__IrefAssignment_0_3_in_rule__Prefix__Group_0__3__Impl2079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UnNamedPrefix__Group__0__Impl_in_rule__UnNamedPrefix__Group__02117 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_rule__UnNamedPrefix__Group__1_in_rule__UnNamedPrefix__Group__02120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__UnNamedPrefix__Group__0__Impl2148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UnNamedPrefix__Group__1__Impl_in_rule__UnNamedPrefix__Group__12179 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__UnNamedPrefix__Group__2_in_rule__UnNamedPrefix__Group__12182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__UnNamedPrefix__Group__1__Impl2210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UnNamedPrefix__Group__2__Impl_in_rule__UnNamedPrefix__Group__22241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UnNamedPrefix__IrefAssignment_2_in_rule__UnNamedPrefix__Group__2__Impl2268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Base__Group__0__Impl_in_rule__Base__Group__02304 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Base__Group__1_in_rule__Base__Group__02307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_rule__Base__Group__0__Impl2335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Base__Group__1__Impl_in_rule__Base__Group__12366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Base__IrefAssignment_1_in_rule__Base__Group__1__Impl2393 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__0__Impl_in_rule__SelectQuery__Group__02427 = new BitSet(new long[]{0x0000008034000000L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__1_in_rule__SelectQuery__Group__02430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Alternatives_0_in_rule__SelectQuery__Group__0__Impl2457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__1__Impl_in_rule__SelectQuery__Group__12488 = new BitSet(new long[]{0x0000008034000000L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__2_in_rule__SelectQuery__Group__12491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__BaseAssignment_1_in_rule__SelectQuery__Group__1__Impl2518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__2__Impl_in_rule__SelectQuery__Group__22549 = new BitSet(new long[]{0x0000008034000000L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__3_in_rule__SelectQuery__Group__22552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__PrefixesAssignment_2_in_rule__SelectQuery__Group__2__Impl2579 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__3__Impl_in_rule__SelectQuery__Group__32610 = new BitSet(new long[]{0x0000008034000000L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__4_in_rule__SelectQuery__Group__32613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__DatasetClausesAssignment_3_in_rule__SelectQuery__Group__3__Impl2640 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__4__Impl_in_rule__SelectQuery__Group__42671 = new BitSet(new long[]{0x0034000000000010L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__5_in_rule__SelectQuery__Group__42674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_rule__SelectQuery__Group__4__Impl2702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__5__Impl_in_rule__SelectQuery__Group__52733 = new BitSet(new long[]{0x0034000000000010L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__6_in_rule__SelectQuery__Group__52736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Alternatives_5_in_rule__SelectQuery__Group__5__Impl2763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__6__Impl_in_rule__SelectQuery__Group__62794 = new BitSet(new long[]{0x0034200000000010L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__7_in_rule__SelectQuery__Group__62797 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__VariablesAssignment_6_in_rule__SelectQuery__Group__6__Impl2824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__7__Impl_in_rule__SelectQuery__Group__72854 = new BitSet(new long[]{0x0034200000000010L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__8_in_rule__SelectQuery__Group__72857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__VariablesAssignment_7_in_rule__SelectQuery__Group__7__Impl2884 = new BitSet(new long[]{0x0034000000000012L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__8__Impl_in_rule__SelectQuery__Group__82915 = new BitSet(new long[]{0x0000006040000000L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__9_in_rule__SelectQuery__Group__82918 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__WhereClauseAssignment_8_in_rule__SelectQuery__Group__8__Impl2945 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__9__Impl_in_rule__SelectQuery__Group__92975 = new BitSet(new long[]{0x0000006040000000L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__10_in_rule__SelectQuery__Group__92978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__FilterclauseAssignment_9_in_rule__SelectQuery__Group__9__Impl3005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__10__Impl_in_rule__SelectQuery__Group__103036 = new BitSet(new long[]{0x0000006040000000L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__11_in_rule__SelectQuery__Group__103039 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__AggregateClauseAssignment_10_in_rule__SelectQuery__Group__10__Impl3066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__Group__11__Impl_in_rule__SelectQuery__Group__113097 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SelectQuery__FilesinkclauseAssignment_11_in_rule__SelectQuery__Group__11__Impl3124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group__0__Impl_in_rule__Aggregate__Group__03179 = new BitSet(new long[]{0x0000000D80000000L});
    public static final BitSet FOLLOW_rule__Aggregate__Group__1_in_rule__Aggregate__Group__03182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_rule__Aggregate__Group__0__Impl3210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group__1__Impl_in_rule__Aggregate__Group__13241 = new BitSet(new long[]{0x0000000D80000000L});
    public static final BitSet FOLLOW_rule__Aggregate__Group__2_in_rule__Aggregate__Group__13244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_1__0_in_rule__Aggregate__Group__1__Impl3271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group__2__Impl_in_rule__Aggregate__Group__23302 = new BitSet(new long[]{0x0000000D80000000L});
    public static final BitSet FOLLOW_rule__Aggregate__Group__3_in_rule__Aggregate__Group__23305 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_2__0_in_rule__Aggregate__Group__2__Impl3332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group__3__Impl_in_rule__Aggregate__Group__33363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_rule__Aggregate__Group__3__Impl3391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_1__0__Impl_in_rule__Aggregate__Group_1__03430 = new BitSet(new long[]{0x0000001200000000L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_1__1_in_rule__Aggregate__Group_1__03433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_rule__Aggregate__Group_1__0__Impl3461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_1__1__Impl_in_rule__Aggregate__Group_1__13492 = new BitSet(new long[]{0x0000001200000000L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_1__2_in_rule__Aggregate__Group_1__13495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__AggregationsAssignment_1_1_in_rule__Aggregate__Group_1__1__Impl3522 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_1__2__Impl_in_rule__Aggregate__Group_1__23553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_rule__Aggregate__Group_1__2__Impl3581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_2__0__Impl_in_rule__Aggregate__Group_2__03618 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_2__1_in_rule__Aggregate__Group_2__03621 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_rule__Aggregate__Group_2__0__Impl3650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__Group_2__1__Impl_in_rule__Aggregate__Group_2__13683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregate__GroupbyAssignment_2_1_in_rule__Aggregate__Group_2__1__Impl3710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupBy__Group__0__Impl_in_rule__GroupBy__Group__03744 = new BitSet(new long[]{0x0034000000000010L});
    public static final BitSet FOLLOW_rule__GroupBy__Group__1_in_rule__GroupBy__Group__03747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_rule__GroupBy__Group__0__Impl3775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupBy__Group__1__Impl_in_rule__GroupBy__Group__13806 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_rule__GroupBy__Group__2_in_rule__GroupBy__Group__13809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupBy__VariablesAssignment_1_in_rule__GroupBy__Group__1__Impl3836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupBy__Group__2__Impl_in_rule__GroupBy__Group__23866 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_rule__GroupBy__Group__3_in_rule__GroupBy__Group__23869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupBy__Group_2__0_in_rule__GroupBy__Group__2__Impl3896 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_rule__GroupBy__Group__3__Impl_in_rule__GroupBy__Group__33927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_rule__GroupBy__Group__3__Impl3955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupBy__Group_2__0__Impl_in_rule__GroupBy__Group_2__03994 = new BitSet(new long[]{0x0034000000000010L});
    public static final BitSet FOLLOW_rule__GroupBy__Group_2__1_in_rule__GroupBy__Group_2__03997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_rule__GroupBy__Group_2__0__Impl4025 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupBy__Group_2__1__Impl_in_rule__GroupBy__Group_2__14056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupBy__VariablesAssignment_2_1_in_rule__GroupBy__Group_2__1__Impl4083 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__0__Impl_in_rule__Aggregation__Group__04117 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__1_in_rule__Aggregation__Group__04120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_rule__Aggregation__Group__0__Impl4148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__1__Impl_in_rule__Aggregation__Group__14179 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__2_in_rule__Aggregation__Group__14182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__FunctionAssignment_1_in_rule__Aggregation__Group__1__Impl4209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__2__Impl_in_rule__Aggregation__Group__24239 = new BitSet(new long[]{0x0034000000000010L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__3_in_rule__Aggregation__Group__24242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_rule__Aggregation__Group__2__Impl4270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__3__Impl_in_rule__Aggregation__Group__34301 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__4_in_rule__Aggregation__Group__34304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__VarToAggAssignment_3_in_rule__Aggregation__Group__3__Impl4331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__4__Impl_in_rule__Aggregation__Group__44361 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__5_in_rule__Aggregation__Group__44364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_rule__Aggregation__Group__4__Impl4392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__5__Impl_in_rule__Aggregation__Group__54423 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__6_in_rule__Aggregation__Group__54426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__AggNameAssignment_5_in_rule__Aggregation__Group__5__Impl4453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__6__Impl_in_rule__Aggregation__Group__64483 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__7_in_rule__Aggregation__Group__64486 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group_6__0_in_rule__Aggregation__Group__6__Impl4513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__7__Impl_in_rule__Aggregation__Group__74544 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__8_in_rule__Aggregation__Group__74547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_rule__Aggregation__Group__7__Impl4575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group__8__Impl_in_rule__Aggregation__Group__84606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_rule__Aggregation__Group__8__Impl4635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group_6__0__Impl_in_rule__Aggregation__Group_6__04686 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_rule__Aggregation__Group_6__1_in_rule__Aggregation__Group_6__04689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_rule__Aggregation__Group_6__0__Impl4717 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__Group_6__1__Impl_in_rule__Aggregation__Group_6__14748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Aggregation__DatatypeAssignment_6_1_in_rule__Aggregation__Group_6__1__Impl4775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filesinkclause__Group__0__Impl_in_rule__Filesinkclause__Group__04809 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_rule__Filesinkclause__Group__1_in_rule__Filesinkclause__Group__04812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_rule__Filesinkclause__Group__0__Impl4840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filesinkclause__Group__1__Impl_in_rule__Filesinkclause__Group__14871 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_rule__Filesinkclause__Group__2_in_rule__Filesinkclause__Group__14874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filesinkclause__PathAssignment_1_in_rule__Filesinkclause__Group__1__Impl4901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filesinkclause__Group__2__Impl_in_rule__Filesinkclause__Group__24931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_rule__Filesinkclause__Group__2__Impl4959 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__0__Impl_in_rule__Filterclause__Group__04996 = new BitSet(new long[]{0x0034000000000010L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__1_in_rule__Filterclause__Group__04999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_rule__Filterclause__Group__0__Impl5027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__1__Impl_in_rule__Filterclause__Group__15058 = new BitSet(new long[]{0x0000000003FF0000L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__2_in_rule__Filterclause__Group__15061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filterclause__LeftAssignment_1_in_rule__Filterclause__Group__1__Impl5088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__2__Impl_in_rule__Filterclause__Group__25118 = new BitSet(new long[]{0x0034000000000010L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__3_in_rule__Filterclause__Group__25121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filterclause__OperatorAssignment_2_in_rule__Filterclause__Group__2__Impl5148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__3__Impl_in_rule__Filterclause__Group__35178 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__4_in_rule__Filterclause__Group__35181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filterclause__RightAssignment_3_in_rule__Filterclause__Group__3__Impl5208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Filterclause__Group__4__Impl_in_rule__Filterclause__Group__45238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_rule__Filterclause__Group__4__Impl5266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__0__Impl_in_rule__DatasetClause__Group__05308 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__1_in_rule__DatasetClause__Group__05311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_rule__DatasetClause__Group__0__Impl5339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__1__Impl_in_rule__DatasetClause__Group__15370 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__2_in_rule__DatasetClause__Group__15373 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__DataSetAssignment_1_in_rule__DatasetClause__Group__1__Impl5400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__2__Impl_in_rule__DatasetClause__Group__25430 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__3_in_rule__DatasetClause__Group__25433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_rule__DatasetClause__Group__2__Impl5461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__3__Impl_in_rule__DatasetClause__Group__35492 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__4_in_rule__DatasetClause__Group__35495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__NameAssignment_3_in_rule__DatasetClause__Group__3__Impl5522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group__4__Impl_in_rule__DatasetClause__Group__45552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__0_in_rule__DatasetClause__Group__4__Impl5579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__0__Impl_in_rule__DatasetClause__Group_4__05620 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__1_in_rule__DatasetClause__Group_4__05623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_rule__DatasetClause__Group_4__0__Impl5651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__1__Impl_in_rule__DatasetClause__Group_4__15682 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__2_in_rule__DatasetClause__Group_4__15685 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__TypeAssignment_4_1_in_rule__DatasetClause__Group_4__1__Impl5712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__2__Impl_in_rule__DatasetClause__Group_4__25742 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__3_in_rule__DatasetClause__Group_4__25745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_rule__DatasetClause__Group_4__2__Impl5773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__3__Impl_in_rule__DatasetClause__Group_4__35804 = new BitSet(new long[]{0x0000180200000000L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__4_in_rule__DatasetClause__Group_4__35807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__SizeAssignment_4_3_in_rule__DatasetClause__Group_4__3__Impl5834 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__4__Impl_in_rule__DatasetClause__Group_4__45864 = new BitSet(new long[]{0x0000180200000000L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__5_in_rule__DatasetClause__Group_4__45867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4_4__0_in_rule__DatasetClause__Group_4__4__Impl5894 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__5__Impl_in_rule__DatasetClause__Group_4__55925 = new BitSet(new long[]{0x0000180200000000L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__6_in_rule__DatasetClause__Group_4__55928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4_5__0_in_rule__DatasetClause__Group_4__5__Impl5955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4__6__Impl_in_rule__DatasetClause__Group_4__65986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_rule__DatasetClause__Group_4__6__Impl6014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4_4__0__Impl_in_rule__DatasetClause__Group_4_4__06059 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4_4__1_in_rule__DatasetClause__Group_4_4__06062 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_rule__DatasetClause__Group_4_4__0__Impl6090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4_4__1__Impl_in_rule__DatasetClause__Group_4_4__16121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__AdvanceAssignment_4_4_1_in_rule__DatasetClause__Group_4_4__1__Impl6148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4_5__0__Impl_in_rule__DatasetClause__Group_4_5__06182 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4_5__1_in_rule__DatasetClause__Group_4_5__06185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_rule__DatasetClause__Group_4_5__0__Impl6213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__Group_4_5__1__Impl_in_rule__DatasetClause__Group_4_5__16244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__DatasetClause__UnitAssignment_4_5_1_in_rule__DatasetClause__Group_4_5__1__Impl6271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__WhereClause__Group__0__Impl_in_rule__WhereClause__Group__06305 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_rule__WhereClause__Group__1_in_rule__WhereClause__Group__06308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_rule__WhereClause__Group__0__Impl6336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__WhereClause__Group__1__Impl_in_rule__WhereClause__Group__16367 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__WhereClause__Group__2_in_rule__WhereClause__Group__16370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_rule__WhereClause__Group__1__Impl6398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__WhereClause__Group__2__Impl_in_rule__WhereClause__Group__26429 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_rule__WhereClause__Group__3_in_rule__WhereClause__Group__26432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__WhereClause__WhereclausesAssignment_2_in_rule__WhereClause__Group__2__Impl6461 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_rule__WhereClause__WhereclausesAssignment_2_in_rule__WhereClause__Group__2__Impl6473 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_rule__WhereClause__Group__3__Impl_in_rule__WhereClause__Group__36506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_rule__WhereClause__Group__3__Impl6534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__InnerWhereClause__Group__0__Impl_in_rule__InnerWhereClause__Group__06573 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_rule__InnerWhereClause__Group__1_in_rule__InnerWhereClause__Group__06576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__InnerWhereClause__NameAssignment_0_in_rule__InnerWhereClause__Group__0__Impl6603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__InnerWhereClause__Group__1__Impl_in_rule__InnerWhereClause__Group__16633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__InnerWhereClause__GroupGraphPatternAssignment_1_in_rule__InnerWhereClause__Group__1__Impl6660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__0__Impl_in_rule__GroupGraphPatternSub__Group__06694 = new BitSet(new long[]{0x00340000000000B0L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__1_in_rule__GroupGraphPatternSub__Group__06697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_rule__GroupGraphPatternSub__Group__0__Impl6725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__1__Impl_in_rule__GroupGraphPatternSub__Group__16756 = new BitSet(new long[]{0x0001800000000000L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__2_in_rule__GroupGraphPatternSub__Group__16759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__GraphPatternsAssignment_1_in_rule__GroupGraphPatternSub__Group__1__Impl6786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__2__Impl_in_rule__GroupGraphPatternSub__Group__26816 = new BitSet(new long[]{0x0001800000000000L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__3_in_rule__GroupGraphPatternSub__Group__26819 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group_2__0_in_rule__GroupGraphPatternSub__Group__2__Impl6846 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__3__Impl_in_rule__GroupGraphPatternSub__Group__36877 = new BitSet(new long[]{0x0001800000000000L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__4_in_rule__GroupGraphPatternSub__Group__36880 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_rule__GroupGraphPatternSub__Group__3__Impl6909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group__4__Impl_in_rule__GroupGraphPatternSub__Group__46942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_rule__GroupGraphPatternSub__Group__4__Impl6970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group_2__0__Impl_in_rule__GroupGraphPatternSub__Group_2__07011 = new BitSet(new long[]{0x00340000000000B0L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group_2__1_in_rule__GroupGraphPatternSub__Group_2__07014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_rule__GroupGraphPatternSub__Group_2__0__Impl7042 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__Group_2__1__Impl_in_rule__GroupGraphPatternSub__Group_2__17073 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1_in_rule__GroupGraphPatternSub__Group_2__1__Impl7100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group__0__Impl_in_rule__TriplesSameSubject__Group__07134 = new BitSet(new long[]{0x00340000000000B0L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group__1_in_rule__TriplesSameSubject__Group__07137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__SubjectAssignment_0_in_rule__TriplesSameSubject__Group__0__Impl7164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group__1__Impl_in_rule__TriplesSameSubject__Group__17194 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group__2_in_rule__TriplesSameSubject__Group__17197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__PropertyListAssignment_1_in_rule__TriplesSameSubject__Group__1__Impl7224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group__2__Impl_in_rule__TriplesSameSubject__Group__27254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group_2__0_in_rule__TriplesSameSubject__Group__2__Impl7281 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group_2__0__Impl_in_rule__TriplesSameSubject__Group_2__07318 = new BitSet(new long[]{0x00340000000000B0L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group_2__1_in_rule__TriplesSameSubject__Group_2__07321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_rule__TriplesSameSubject__Group_2__0__Impl7349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__Group_2__1__Impl_in_rule__TriplesSameSubject__Group_2__17380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__TriplesSameSubject__PropertyListAssignment_2_1_in_rule__TriplesSameSubject__Group_2__1__Impl7407 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__PropertyList__Group__0__Impl_in_rule__PropertyList__Group__07441 = new BitSet(new long[]{0x00340000000000B0L});
    public static final BitSet FOLLOW_rule__PropertyList__Group__1_in_rule__PropertyList__Group__07444 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__PropertyList__PropertyAssignment_0_in_rule__PropertyList__Group__0__Impl7471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__PropertyList__Group__1__Impl_in_rule__PropertyList__Group__17501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__PropertyList__ObjectAssignment_1_in_rule__PropertyList__Group__1__Impl7528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UnNamedVariable__Group__0__Impl_in_rule__UnNamedVariable__Group__07562 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__UnNamedVariable__Group__1_in_rule__UnNamedVariable__Group__07565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_rule__UnNamedVariable__Group__0__Impl7593 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UnNamedVariable__Group__1__Impl_in_rule__UnNamedVariable__Group__17624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UnNamedVariable__NameAssignment_1_in_rule__UnNamedVariable__Group__1__Impl7651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Property__Group__0__Impl_in_rule__Property__Group__07685 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_rule__Property__Group__1_in_rule__Property__Group__07688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Property__PrefixAssignment_0_in_rule__Property__Group__0__Impl7715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Property__Group__1__Impl_in_rule__Property__Group__17745 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Property__Group__2_in_rule__Property__Group__17748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__Property__Group__1__Impl7776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Property__Group__2__Impl_in_rule__Property__Group__27807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Property__NameAssignment_2_in_rule__Property__Group__2__Impl7834 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__IRI__Group__0__Impl_in_rule__IRI__Group__07870 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__IRI__Group__1_in_rule__IRI__Group__07873 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__IRI__Group__1__Impl_in_rule__IRI__Group__17931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__IRI__ValueAssignment_1_in_rule__IRI__Group__1__Impl7958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Prefix__NameAssignment_0_17999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_IRI_TERMINAL_in_rule__Prefix__IrefAssignment_0_38030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_IRI_TERMINAL_in_rule__UnNamedPrefix__IrefAssignment_28061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleIRI_in_rule__Base__IrefAssignment_18092 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_rule__SelectQuery__MethodAssignment_0_08128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBase_in_rule__SelectQuery__BaseAssignment_18167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rulePrefix_in_rule__SelectQuery__PrefixesAssignment_28198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDatasetClause_in_rule__SelectQuery__DatasetClausesAssignment_38229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_rule__SelectQuery__IsDistinctAssignment_5_08265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_rule__SelectQuery__IsReducedAssignment_5_18309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_rule__SelectQuery__VariablesAssignment_68348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_rule__SelectQuery__VariablesAssignment_78379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWhereClause_in_rule__SelectQuery__WhereClauseAssignment_88410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFilterclause_in_rule__SelectQuery__FilterclauseAssignment_98441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAggregate_in_rule__SelectQuery__AggregateClauseAssignment_108472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFilesinkclause_in_rule__SelectQuery__FilesinkclauseAssignment_118503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAggregation_in_rule__Aggregate__AggregationsAssignment_1_18534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupBy_in_rule__Aggregate__GroupbyAssignment_2_18565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_rule__GroupBy__VariablesAssignment_18596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_rule__GroupBy__VariablesAssignment_2_18627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_AGG_FUNCTION_in_rule__Aggregation__FunctionAssignment_18658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_rule__Aggregation__VarToAggAssignment_38689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Aggregation__AggNameAssignment_58720 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Aggregation__DatatypeAssignment_6_18751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Filesinkclause__PathAssignment_18782 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_rule__Filterclause__LeftAssignment_18813 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_rule__Filterclause__OperatorAssignment_28844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_rule__Filterclause__RightAssignment_38875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleIRI_in_rule__DatasetClause__DataSetAssignment_18908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__DatasetClause__NameAssignment_38939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_WINDOWTYPE_in_rule__DatasetClause__TypeAssignment_4_18970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_INT_in_rule__DatasetClause__SizeAssignment_4_39001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_INT_in_rule__DatasetClause__AdvanceAssignment_4_4_19032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_UNITTYPE_in_rule__DatasetClause__UnitAssignment_4_5_19063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleInnerWhereClause_in_rule__WhereClause__WhereclausesAssignment_29094 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__InnerWhereClause__NameAssignment_09129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGroupGraphPatternSub_in_rule__InnerWhereClause__GroupGraphPatternAssignment_19164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTriplesSameSubject_in_rule__GroupGraphPatternSub__GraphPatternsAssignment_19195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTriplesSameSubject_in_rule__GroupGraphPatternSub__GraphPatternsAssignment_2_19226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGraphNode_in_rule__TriplesSameSubject__SubjectAssignment_09257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rulePropertyList_in_rule__TriplesSameSubject__PropertyListAssignment_19288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rulePropertyList_in_rule__TriplesSameSubject__PropertyListAssignment_2_19319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGraphNode_in_rule__PropertyList__PropertyAssignment_09350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleGraphNode_in_rule__PropertyList__ObjectAssignment_19381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleVariable_in_rule__GraphNode__VariableAssignment_09412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__GraphNode__LiteralAssignment_19443 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleIRI_in_rule__GraphNode__IriAssignment_29474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUnNamedVariable_in_rule__Variable__UnnamedAssignment_09505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleProperty_in_rule__Variable__PropertyAssignment_19536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__UnNamedVariable__NameAssignment_19567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Property__PrefixAssignment_09602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Property__NameAssignment_29637 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_IRI_TERMINAL_in_rule__IRI__ValueAssignment_19668 = new BitSet(new long[]{0x0000000000000002L});

}