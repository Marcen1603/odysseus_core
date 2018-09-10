package de.uniol.inf.is.odysseus.server.ide.contentassist.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.DFA;
import de.uniol.inf.is.odysseus.server.services.StreamingsparqlGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalStreamingsparqlParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_IRI_TERMINAL", "RULE_AGG_FUNCTION", "RULE_STRING", "RULE_WINDOWTYPE", "RULE_INT", "RULE_UNITTYPE", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'#RUNQUERY'", "'<'", "'>'", "'<='", "'>='", "'='", "'!='", "'+'", "'/'", "'-'", "'*'", "'PREFIX'", "':'", "'BASE'", "'SELECT'", "'AGGREGATE('", "')'", "'aggregations'", "'['", "']'", "','", "'group_by=['", "'CSVFILESINK('", "'FILTER('", "'FROM'", "'AS'", "'TYPE'", "'SIZE'", "'ADVANCE'", "'UNIT'", "'WHERE'", "'{'", "'}'", "'.'", "';'", "'?'", "'#ADDQUERY'"
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
    // InternalStreamingsparql.g:53:1: entryRuleSPARQLQuery : ruleSPARQLQuery EOF ;
    public final void entryRuleSPARQLQuery() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:54:1: ( ruleSPARQLQuery EOF )
            // InternalStreamingsparql.g:55:1: ruleSPARQLQuery EOF
            {
             before(grammarAccess.getSPARQLQueryRule()); 
            pushFollow(FOLLOW_1);
            ruleSPARQLQuery();

            state._fsp--;

             after(grammarAccess.getSPARQLQueryRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:62:1: ruleSPARQLQuery : ( ruleSelectQuery ) ;
    public final void ruleSPARQLQuery() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:66:2: ( ( ruleSelectQuery ) )
            // InternalStreamingsparql.g:67:2: ( ruleSelectQuery )
            {
            // InternalStreamingsparql.g:67:2: ( ruleSelectQuery )
            // InternalStreamingsparql.g:68:3: ruleSelectQuery
            {
             before(grammarAccess.getSPARQLQueryAccess().getSelectQueryParserRuleCall()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:78:1: entryRulePrefix : rulePrefix EOF ;
    public final void entryRulePrefix() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:79:1: ( rulePrefix EOF )
            // InternalStreamingsparql.g:80:1: rulePrefix EOF
            {
             before(grammarAccess.getPrefixRule()); 
            pushFollow(FOLLOW_1);
            rulePrefix();

            state._fsp--;

             after(grammarAccess.getPrefixRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:87:1: rulePrefix : ( ( rule__Prefix__Alternatives ) ) ;
    public final void rulePrefix() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:91:2: ( ( ( rule__Prefix__Alternatives ) ) )
            // InternalStreamingsparql.g:92:2: ( ( rule__Prefix__Alternatives ) )
            {
            // InternalStreamingsparql.g:92:2: ( ( rule__Prefix__Alternatives ) )
            // InternalStreamingsparql.g:93:3: ( rule__Prefix__Alternatives )
            {
             before(grammarAccess.getPrefixAccess().getAlternatives()); 
            // InternalStreamingsparql.g:94:3: ( rule__Prefix__Alternatives )
            // InternalStreamingsparql.g:94:4: rule__Prefix__Alternatives
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:103:1: entryRuleUnNamedPrefix : ruleUnNamedPrefix EOF ;
    public final void entryRuleUnNamedPrefix() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:104:1: ( ruleUnNamedPrefix EOF )
            // InternalStreamingsparql.g:105:1: ruleUnNamedPrefix EOF
            {
             before(grammarAccess.getUnNamedPrefixRule()); 
            pushFollow(FOLLOW_1);
            ruleUnNamedPrefix();

            state._fsp--;

             after(grammarAccess.getUnNamedPrefixRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:112:1: ruleUnNamedPrefix : ( ( rule__UnNamedPrefix__Group__0 ) ) ;
    public final void ruleUnNamedPrefix() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:116:2: ( ( ( rule__UnNamedPrefix__Group__0 ) ) )
            // InternalStreamingsparql.g:117:2: ( ( rule__UnNamedPrefix__Group__0 ) )
            {
            // InternalStreamingsparql.g:117:2: ( ( rule__UnNamedPrefix__Group__0 ) )
            // InternalStreamingsparql.g:118:3: ( rule__UnNamedPrefix__Group__0 )
            {
             before(grammarAccess.getUnNamedPrefixAccess().getGroup()); 
            // InternalStreamingsparql.g:119:3: ( rule__UnNamedPrefix__Group__0 )
            // InternalStreamingsparql.g:119:4: rule__UnNamedPrefix__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:128:1: entryRuleBase : ruleBase EOF ;
    public final void entryRuleBase() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:129:1: ( ruleBase EOF )
            // InternalStreamingsparql.g:130:1: ruleBase EOF
            {
             before(grammarAccess.getBaseRule()); 
            pushFollow(FOLLOW_1);
            ruleBase();

            state._fsp--;

             after(grammarAccess.getBaseRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:137:1: ruleBase : ( ( rule__Base__Group__0 ) ) ;
    public final void ruleBase() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:141:2: ( ( ( rule__Base__Group__0 ) ) )
            // InternalStreamingsparql.g:142:2: ( ( rule__Base__Group__0 ) )
            {
            // InternalStreamingsparql.g:142:2: ( ( rule__Base__Group__0 ) )
            // InternalStreamingsparql.g:143:3: ( rule__Base__Group__0 )
            {
             before(grammarAccess.getBaseAccess().getGroup()); 
            // InternalStreamingsparql.g:144:3: ( rule__Base__Group__0 )
            // InternalStreamingsparql.g:144:4: rule__Base__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:153:1: entryRuleSelectQuery : ruleSelectQuery EOF ;
    public final void entryRuleSelectQuery() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:154:1: ( ruleSelectQuery EOF )
            // InternalStreamingsparql.g:155:1: ruleSelectQuery EOF
            {
             before(grammarAccess.getSelectQueryRule()); 
            pushFollow(FOLLOW_1);
            ruleSelectQuery();

            state._fsp--;

             after(grammarAccess.getSelectQueryRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:162:1: ruleSelectQuery : ( ( rule__SelectQuery__Group__0 ) ) ;
    public final void ruleSelectQuery() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:166:2: ( ( ( rule__SelectQuery__Group__0 ) ) )
            // InternalStreamingsparql.g:167:2: ( ( rule__SelectQuery__Group__0 ) )
            {
            // InternalStreamingsparql.g:167:2: ( ( rule__SelectQuery__Group__0 ) )
            // InternalStreamingsparql.g:168:3: ( rule__SelectQuery__Group__0 )
            {
             before(grammarAccess.getSelectQueryAccess().getGroup()); 
            // InternalStreamingsparql.g:169:3: ( rule__SelectQuery__Group__0 )
            // InternalStreamingsparql.g:169:4: rule__SelectQuery__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:178:1: entryRuleAggregate : ruleAggregate EOF ;
    public final void entryRuleAggregate() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:179:1: ( ruleAggregate EOF )
            // InternalStreamingsparql.g:180:1: ruleAggregate EOF
            {
             before(grammarAccess.getAggregateRule()); 
            pushFollow(FOLLOW_1);
            ruleAggregate();

            state._fsp--;

             after(grammarAccess.getAggregateRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:187:1: ruleAggregate : ( ( rule__Aggregate__Group__0 ) ) ;
    public final void ruleAggregate() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:191:2: ( ( ( rule__Aggregate__Group__0 ) ) )
            // InternalStreamingsparql.g:192:2: ( ( rule__Aggregate__Group__0 ) )
            {
            // InternalStreamingsparql.g:192:2: ( ( rule__Aggregate__Group__0 ) )
            // InternalStreamingsparql.g:193:3: ( rule__Aggregate__Group__0 )
            {
             before(grammarAccess.getAggregateAccess().getGroup()); 
            // InternalStreamingsparql.g:194:3: ( rule__Aggregate__Group__0 )
            // InternalStreamingsparql.g:194:4: rule__Aggregate__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:203:1: entryRuleGroupBy : ruleGroupBy EOF ;
    public final void entryRuleGroupBy() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:204:1: ( ruleGroupBy EOF )
            // InternalStreamingsparql.g:205:1: ruleGroupBy EOF
            {
             before(grammarAccess.getGroupByRule()); 
            pushFollow(FOLLOW_1);
            ruleGroupBy();

            state._fsp--;

             after(grammarAccess.getGroupByRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:212:1: ruleGroupBy : ( ( rule__GroupBy__Group__0 ) ) ;
    public final void ruleGroupBy() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:216:2: ( ( ( rule__GroupBy__Group__0 ) ) )
            // InternalStreamingsparql.g:217:2: ( ( rule__GroupBy__Group__0 ) )
            {
            // InternalStreamingsparql.g:217:2: ( ( rule__GroupBy__Group__0 ) )
            // InternalStreamingsparql.g:218:3: ( rule__GroupBy__Group__0 )
            {
             before(grammarAccess.getGroupByAccess().getGroup()); 
            // InternalStreamingsparql.g:219:3: ( rule__GroupBy__Group__0 )
            // InternalStreamingsparql.g:219:4: rule__GroupBy__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:228:1: entryRuleAggregation : ruleAggregation EOF ;
    public final void entryRuleAggregation() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:229:1: ( ruleAggregation EOF )
            // InternalStreamingsparql.g:230:1: ruleAggregation EOF
            {
             before(grammarAccess.getAggregationRule()); 
            pushFollow(FOLLOW_1);
            ruleAggregation();

            state._fsp--;

             after(grammarAccess.getAggregationRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:237:1: ruleAggregation : ( ( rule__Aggregation__Group__0 ) ) ;
    public final void ruleAggregation() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:241:2: ( ( ( rule__Aggregation__Group__0 ) ) )
            // InternalStreamingsparql.g:242:2: ( ( rule__Aggregation__Group__0 ) )
            {
            // InternalStreamingsparql.g:242:2: ( ( rule__Aggregation__Group__0 ) )
            // InternalStreamingsparql.g:243:3: ( rule__Aggregation__Group__0 )
            {
             before(grammarAccess.getAggregationAccess().getGroup()); 
            // InternalStreamingsparql.g:244:3: ( rule__Aggregation__Group__0 )
            // InternalStreamingsparql.g:244:4: rule__Aggregation__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:253:1: entryRuleFilesinkclause : ruleFilesinkclause EOF ;
    public final void entryRuleFilesinkclause() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:254:1: ( ruleFilesinkclause EOF )
            // InternalStreamingsparql.g:255:1: ruleFilesinkclause EOF
            {
             before(grammarAccess.getFilesinkclauseRule()); 
            pushFollow(FOLLOW_1);
            ruleFilesinkclause();

            state._fsp--;

             after(grammarAccess.getFilesinkclauseRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:262:1: ruleFilesinkclause : ( ( rule__Filesinkclause__Group__0 ) ) ;
    public final void ruleFilesinkclause() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:266:2: ( ( ( rule__Filesinkclause__Group__0 ) ) )
            // InternalStreamingsparql.g:267:2: ( ( rule__Filesinkclause__Group__0 ) )
            {
            // InternalStreamingsparql.g:267:2: ( ( rule__Filesinkclause__Group__0 ) )
            // InternalStreamingsparql.g:268:3: ( rule__Filesinkclause__Group__0 )
            {
             before(grammarAccess.getFilesinkclauseAccess().getGroup()); 
            // InternalStreamingsparql.g:269:3: ( rule__Filesinkclause__Group__0 )
            // InternalStreamingsparql.g:269:4: rule__Filesinkclause__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:278:1: entryRuleFilterclause : ruleFilterclause EOF ;
    public final void entryRuleFilterclause() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:279:1: ( ruleFilterclause EOF )
            // InternalStreamingsparql.g:280:1: ruleFilterclause EOF
            {
             before(grammarAccess.getFilterclauseRule()); 
            pushFollow(FOLLOW_1);
            ruleFilterclause();

            state._fsp--;

             after(grammarAccess.getFilterclauseRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:287:1: ruleFilterclause : ( ( rule__Filterclause__Group__0 ) ) ;
    public final void ruleFilterclause() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:291:2: ( ( ( rule__Filterclause__Group__0 ) ) )
            // InternalStreamingsparql.g:292:2: ( ( rule__Filterclause__Group__0 ) )
            {
            // InternalStreamingsparql.g:292:2: ( ( rule__Filterclause__Group__0 ) )
            // InternalStreamingsparql.g:293:3: ( rule__Filterclause__Group__0 )
            {
             before(grammarAccess.getFilterclauseAccess().getGroup()); 
            // InternalStreamingsparql.g:294:3: ( rule__Filterclause__Group__0 )
            // InternalStreamingsparql.g:294:4: rule__Filterclause__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:303:1: entryRuleDatasetClause : ruleDatasetClause EOF ;
    public final void entryRuleDatasetClause() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:304:1: ( ruleDatasetClause EOF )
            // InternalStreamingsparql.g:305:1: ruleDatasetClause EOF
            {
             before(grammarAccess.getDatasetClauseRule()); 
            pushFollow(FOLLOW_1);
            ruleDatasetClause();

            state._fsp--;

             after(grammarAccess.getDatasetClauseRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:312:1: ruleDatasetClause : ( ( rule__DatasetClause__Group__0 ) ) ;
    public final void ruleDatasetClause() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:316:2: ( ( ( rule__DatasetClause__Group__0 ) ) )
            // InternalStreamingsparql.g:317:2: ( ( rule__DatasetClause__Group__0 ) )
            {
            // InternalStreamingsparql.g:317:2: ( ( rule__DatasetClause__Group__0 ) )
            // InternalStreamingsparql.g:318:3: ( rule__DatasetClause__Group__0 )
            {
             before(grammarAccess.getDatasetClauseAccess().getGroup()); 
            // InternalStreamingsparql.g:319:3: ( rule__DatasetClause__Group__0 )
            // InternalStreamingsparql.g:319:4: rule__DatasetClause__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:328:1: entryRuleWhereClause : ruleWhereClause EOF ;
    public final void entryRuleWhereClause() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:329:1: ( ruleWhereClause EOF )
            // InternalStreamingsparql.g:330:1: ruleWhereClause EOF
            {
             before(grammarAccess.getWhereClauseRule()); 
            pushFollow(FOLLOW_1);
            ruleWhereClause();

            state._fsp--;

             after(grammarAccess.getWhereClauseRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:337:1: ruleWhereClause : ( ( rule__WhereClause__Group__0 ) ) ;
    public final void ruleWhereClause() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:341:2: ( ( ( rule__WhereClause__Group__0 ) ) )
            // InternalStreamingsparql.g:342:2: ( ( rule__WhereClause__Group__0 ) )
            {
            // InternalStreamingsparql.g:342:2: ( ( rule__WhereClause__Group__0 ) )
            // InternalStreamingsparql.g:343:3: ( rule__WhereClause__Group__0 )
            {
             before(grammarAccess.getWhereClauseAccess().getGroup()); 
            // InternalStreamingsparql.g:344:3: ( rule__WhereClause__Group__0 )
            // InternalStreamingsparql.g:344:4: rule__WhereClause__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:353:1: entryRuleInnerWhereClause : ruleInnerWhereClause EOF ;
    public final void entryRuleInnerWhereClause() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:354:1: ( ruleInnerWhereClause EOF )
            // InternalStreamingsparql.g:355:1: ruleInnerWhereClause EOF
            {
             before(grammarAccess.getInnerWhereClauseRule()); 
            pushFollow(FOLLOW_1);
            ruleInnerWhereClause();

            state._fsp--;

             after(grammarAccess.getInnerWhereClauseRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:362:1: ruleInnerWhereClause : ( ( rule__InnerWhereClause__Group__0 ) ) ;
    public final void ruleInnerWhereClause() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:366:2: ( ( ( rule__InnerWhereClause__Group__0 ) ) )
            // InternalStreamingsparql.g:367:2: ( ( rule__InnerWhereClause__Group__0 ) )
            {
            // InternalStreamingsparql.g:367:2: ( ( rule__InnerWhereClause__Group__0 ) )
            // InternalStreamingsparql.g:368:3: ( rule__InnerWhereClause__Group__0 )
            {
             before(grammarAccess.getInnerWhereClauseAccess().getGroup()); 
            // InternalStreamingsparql.g:369:3: ( rule__InnerWhereClause__Group__0 )
            // InternalStreamingsparql.g:369:4: rule__InnerWhereClause__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:378:1: entryRuleGroupGraphPatternSub : ruleGroupGraphPatternSub EOF ;
    public final void entryRuleGroupGraphPatternSub() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:379:1: ( ruleGroupGraphPatternSub EOF )
            // InternalStreamingsparql.g:380:1: ruleGroupGraphPatternSub EOF
            {
             before(grammarAccess.getGroupGraphPatternSubRule()); 
            pushFollow(FOLLOW_1);
            ruleGroupGraphPatternSub();

            state._fsp--;

             after(grammarAccess.getGroupGraphPatternSubRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:387:1: ruleGroupGraphPatternSub : ( ( rule__GroupGraphPatternSub__Group__0 ) ) ;
    public final void ruleGroupGraphPatternSub() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:391:2: ( ( ( rule__GroupGraphPatternSub__Group__0 ) ) )
            // InternalStreamingsparql.g:392:2: ( ( rule__GroupGraphPatternSub__Group__0 ) )
            {
            // InternalStreamingsparql.g:392:2: ( ( rule__GroupGraphPatternSub__Group__0 ) )
            // InternalStreamingsparql.g:393:3: ( rule__GroupGraphPatternSub__Group__0 )
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGroup()); 
            // InternalStreamingsparql.g:394:3: ( rule__GroupGraphPatternSub__Group__0 )
            // InternalStreamingsparql.g:394:4: rule__GroupGraphPatternSub__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:403:1: entryRuleTriplesSameSubject : ruleTriplesSameSubject EOF ;
    public final void entryRuleTriplesSameSubject() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:404:1: ( ruleTriplesSameSubject EOF )
            // InternalStreamingsparql.g:405:1: ruleTriplesSameSubject EOF
            {
             before(grammarAccess.getTriplesSameSubjectRule()); 
            pushFollow(FOLLOW_1);
            ruleTriplesSameSubject();

            state._fsp--;

             after(grammarAccess.getTriplesSameSubjectRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:412:1: ruleTriplesSameSubject : ( ( rule__TriplesSameSubject__Group__0 ) ) ;
    public final void ruleTriplesSameSubject() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:416:2: ( ( ( rule__TriplesSameSubject__Group__0 ) ) )
            // InternalStreamingsparql.g:417:2: ( ( rule__TriplesSameSubject__Group__0 ) )
            {
            // InternalStreamingsparql.g:417:2: ( ( rule__TriplesSameSubject__Group__0 ) )
            // InternalStreamingsparql.g:418:3: ( rule__TriplesSameSubject__Group__0 )
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getGroup()); 
            // InternalStreamingsparql.g:419:3: ( rule__TriplesSameSubject__Group__0 )
            // InternalStreamingsparql.g:419:4: rule__TriplesSameSubject__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:428:1: entryRulePropertyList : rulePropertyList EOF ;
    public final void entryRulePropertyList() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:429:1: ( rulePropertyList EOF )
            // InternalStreamingsparql.g:430:1: rulePropertyList EOF
            {
             before(grammarAccess.getPropertyListRule()); 
            pushFollow(FOLLOW_1);
            rulePropertyList();

            state._fsp--;

             after(grammarAccess.getPropertyListRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:437:1: rulePropertyList : ( ( rule__PropertyList__Group__0 ) ) ;
    public final void rulePropertyList() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:441:2: ( ( ( rule__PropertyList__Group__0 ) ) )
            // InternalStreamingsparql.g:442:2: ( ( rule__PropertyList__Group__0 ) )
            {
            // InternalStreamingsparql.g:442:2: ( ( rule__PropertyList__Group__0 ) )
            // InternalStreamingsparql.g:443:3: ( rule__PropertyList__Group__0 )
            {
             before(grammarAccess.getPropertyListAccess().getGroup()); 
            // InternalStreamingsparql.g:444:3: ( rule__PropertyList__Group__0 )
            // InternalStreamingsparql.g:444:4: rule__PropertyList__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:453:1: entryRuleGraphNode : ruleGraphNode EOF ;
    public final void entryRuleGraphNode() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:454:1: ( ruleGraphNode EOF )
            // InternalStreamingsparql.g:455:1: ruleGraphNode EOF
            {
             before(grammarAccess.getGraphNodeRule()); 
            pushFollow(FOLLOW_1);
            ruleGraphNode();

            state._fsp--;

             after(grammarAccess.getGraphNodeRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:462:1: ruleGraphNode : ( ( rule__GraphNode__Alternatives ) ) ;
    public final void ruleGraphNode() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:466:2: ( ( ( rule__GraphNode__Alternatives ) ) )
            // InternalStreamingsparql.g:467:2: ( ( rule__GraphNode__Alternatives ) )
            {
            // InternalStreamingsparql.g:467:2: ( ( rule__GraphNode__Alternatives ) )
            // InternalStreamingsparql.g:468:3: ( rule__GraphNode__Alternatives )
            {
             before(grammarAccess.getGraphNodeAccess().getAlternatives()); 
            // InternalStreamingsparql.g:469:3: ( rule__GraphNode__Alternatives )
            // InternalStreamingsparql.g:469:4: rule__GraphNode__Alternatives
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:478:1: entryRuleVariable : ruleVariable EOF ;
    public final void entryRuleVariable() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:479:1: ( ruleVariable EOF )
            // InternalStreamingsparql.g:480:1: ruleVariable EOF
            {
             before(grammarAccess.getVariableRule()); 
            pushFollow(FOLLOW_1);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getVariableRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:487:1: ruleVariable : ( ( rule__Variable__Alternatives ) ) ;
    public final void ruleVariable() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:491:2: ( ( ( rule__Variable__Alternatives ) ) )
            // InternalStreamingsparql.g:492:2: ( ( rule__Variable__Alternatives ) )
            {
            // InternalStreamingsparql.g:492:2: ( ( rule__Variable__Alternatives ) )
            // InternalStreamingsparql.g:493:3: ( rule__Variable__Alternatives )
            {
             before(grammarAccess.getVariableAccess().getAlternatives()); 
            // InternalStreamingsparql.g:494:3: ( rule__Variable__Alternatives )
            // InternalStreamingsparql.g:494:4: rule__Variable__Alternatives
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:503:1: entryRuleUnNamedVariable : ruleUnNamedVariable EOF ;
    public final void entryRuleUnNamedVariable() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:504:1: ( ruleUnNamedVariable EOF )
            // InternalStreamingsparql.g:505:1: ruleUnNamedVariable EOF
            {
             before(grammarAccess.getUnNamedVariableRule()); 
            pushFollow(FOLLOW_1);
            ruleUnNamedVariable();

            state._fsp--;

             after(grammarAccess.getUnNamedVariableRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:512:1: ruleUnNamedVariable : ( ( rule__UnNamedVariable__Group__0 ) ) ;
    public final void ruleUnNamedVariable() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:516:2: ( ( ( rule__UnNamedVariable__Group__0 ) ) )
            // InternalStreamingsparql.g:517:2: ( ( rule__UnNamedVariable__Group__0 ) )
            {
            // InternalStreamingsparql.g:517:2: ( ( rule__UnNamedVariable__Group__0 ) )
            // InternalStreamingsparql.g:518:3: ( rule__UnNamedVariable__Group__0 )
            {
             before(grammarAccess.getUnNamedVariableAccess().getGroup()); 
            // InternalStreamingsparql.g:519:3: ( rule__UnNamedVariable__Group__0 )
            // InternalStreamingsparql.g:519:4: rule__UnNamedVariable__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:528:1: entryRuleProperty : ruleProperty EOF ;
    public final void entryRuleProperty() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:529:1: ( ruleProperty EOF )
            // InternalStreamingsparql.g:530:1: ruleProperty EOF
            {
             before(grammarAccess.getPropertyRule()); 
            pushFollow(FOLLOW_1);
            ruleProperty();

            state._fsp--;

             after(grammarAccess.getPropertyRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:537:1: ruleProperty : ( ( rule__Property__Group__0 ) ) ;
    public final void ruleProperty() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:541:2: ( ( ( rule__Property__Group__0 ) ) )
            // InternalStreamingsparql.g:542:2: ( ( rule__Property__Group__0 ) )
            {
            // InternalStreamingsparql.g:542:2: ( ( rule__Property__Group__0 ) )
            // InternalStreamingsparql.g:543:3: ( rule__Property__Group__0 )
            {
             before(grammarAccess.getPropertyAccess().getGroup()); 
            // InternalStreamingsparql.g:544:3: ( rule__Property__Group__0 )
            // InternalStreamingsparql.g:544:4: rule__Property__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:553:1: entryRuleIRI : ruleIRI EOF ;
    public final void entryRuleIRI() throws RecognitionException {
        try {
            // InternalStreamingsparql.g:554:1: ( ruleIRI EOF )
            // InternalStreamingsparql.g:555:1: ruleIRI EOF
            {
             before(grammarAccess.getIRIRule()); 
            pushFollow(FOLLOW_1);
            ruleIRI();

            state._fsp--;

             after(grammarAccess.getIRIRule()); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalStreamingsparql.g:562:1: ruleIRI : ( ( rule__IRI__Group__0 ) ) ;
    public final void ruleIRI() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:566:2: ( ( ( rule__IRI__Group__0 ) ) )
            // InternalStreamingsparql.g:567:2: ( ( rule__IRI__Group__0 ) )
            {
            // InternalStreamingsparql.g:567:2: ( ( rule__IRI__Group__0 ) )
            // InternalStreamingsparql.g:568:3: ( rule__IRI__Group__0 )
            {
             before(grammarAccess.getIRIAccess().getGroup()); 
            // InternalStreamingsparql.g:569:3: ( rule__IRI__Group__0 )
            // InternalStreamingsparql.g:569:4: rule__IRI__Group__0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:578:1: ruleOperator : ( ( rule__Operator__Alternatives ) ) ;
    public final void ruleOperator() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:582:1: ( ( ( rule__Operator__Alternatives ) ) )
            // InternalStreamingsparql.g:583:2: ( ( rule__Operator__Alternatives ) )
            {
            // InternalStreamingsparql.g:583:2: ( ( rule__Operator__Alternatives ) )
            // InternalStreamingsparql.g:584:3: ( rule__Operator__Alternatives )
            {
             before(grammarAccess.getOperatorAccess().getAlternatives()); 
            // InternalStreamingsparql.g:585:3: ( rule__Operator__Alternatives )
            // InternalStreamingsparql.g:585:4: rule__Operator__Alternatives
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:593:1: rule__Prefix__Alternatives : ( ( ( rule__Prefix__Group_0__0 ) ) | ( ruleUnNamedPrefix ) );
    public final void rule__Prefix__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:597:1: ( ( ( rule__Prefix__Group_0__0 ) ) | ( ruleUnNamedPrefix ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==26) ) {
                int LA1_1 = input.LA(2);

                if ( (LA1_1==27) ) {
                    alt1=2;
                }
                else if ( (LA1_1==RULE_ID) ) {
                    alt1=1;
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
                    // InternalStreamingsparql.g:598:2: ( ( rule__Prefix__Group_0__0 ) )
                    {
                    // InternalStreamingsparql.g:598:2: ( ( rule__Prefix__Group_0__0 ) )
                    // InternalStreamingsparql.g:599:3: ( rule__Prefix__Group_0__0 )
                    {
                     before(grammarAccess.getPrefixAccess().getGroup_0()); 
                    // InternalStreamingsparql.g:600:3: ( rule__Prefix__Group_0__0 )
                    // InternalStreamingsparql.g:600:4: rule__Prefix__Group_0__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Prefix__Group_0__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getPrefixAccess().getGroup_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:604:2: ( ruleUnNamedPrefix )
                    {
                    // InternalStreamingsparql.g:604:2: ( ruleUnNamedPrefix )
                    // InternalStreamingsparql.g:605:3: ruleUnNamedPrefix
                    {
                     before(grammarAccess.getPrefixAccess().getUnNamedPrefixParserRuleCall_1()); 
                    pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:614:1: rule__SelectQuery__Alternatives_0 : ( ( ( rule__SelectQuery__MethodAssignment_0_0 ) ) | ( '#RUNQUERY' ) );
    public final void rule__SelectQuery__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:618:1: ( ( ( rule__SelectQuery__MethodAssignment_0_0 ) ) | ( '#RUNQUERY' ) )
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
                    // InternalStreamingsparql.g:619:2: ( ( rule__SelectQuery__MethodAssignment_0_0 ) )
                    {
                    // InternalStreamingsparql.g:619:2: ( ( rule__SelectQuery__MethodAssignment_0_0 ) )
                    // InternalStreamingsparql.g:620:3: ( rule__SelectQuery__MethodAssignment_0_0 )
                    {
                     before(grammarAccess.getSelectQueryAccess().getMethodAssignment_0_0()); 
                    // InternalStreamingsparql.g:621:3: ( rule__SelectQuery__MethodAssignment_0_0 )
                    // InternalStreamingsparql.g:621:4: rule__SelectQuery__MethodAssignment_0_0
                    {
                    pushFollow(FOLLOW_2);
                    rule__SelectQuery__MethodAssignment_0_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getSelectQueryAccess().getMethodAssignment_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:625:2: ( '#RUNQUERY' )
                    {
                    // InternalStreamingsparql.g:625:2: ( '#RUNQUERY' )
                    // InternalStreamingsparql.g:626:3: '#RUNQUERY'
                    {
                     before(grammarAccess.getSelectQueryAccess().getRUNQUERYKeyword_0_1()); 
                    match(input,15,FOLLOW_2); 
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


    // $ANTLR start "rule__GraphNode__Alternatives"
    // InternalStreamingsparql.g:635:1: rule__GraphNode__Alternatives : ( ( ( rule__GraphNode__VariableAssignment_0 ) ) | ( ( rule__GraphNode__LiteralAssignment_1 ) ) | ( ( rule__GraphNode__IriAssignment_2 ) ) );
    public final void rule__GraphNode__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:639:1: ( ( ( rule__GraphNode__VariableAssignment_0 ) ) | ( ( rule__GraphNode__LiteralAssignment_1 ) ) | ( ( rule__GraphNode__IriAssignment_2 ) ) )
            int alt3=3;
            switch ( input.LA(1) ) {
            case RULE_ID:
            case 50:
                {
                alt3=1;
                }
                break;
            case RULE_STRING:
                {
                alt3=2;
                }
                break;
            case RULE_IRI_TERMINAL:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // InternalStreamingsparql.g:640:2: ( ( rule__GraphNode__VariableAssignment_0 ) )
                    {
                    // InternalStreamingsparql.g:640:2: ( ( rule__GraphNode__VariableAssignment_0 ) )
                    // InternalStreamingsparql.g:641:3: ( rule__GraphNode__VariableAssignment_0 )
                    {
                     before(grammarAccess.getGraphNodeAccess().getVariableAssignment_0()); 
                    // InternalStreamingsparql.g:642:3: ( rule__GraphNode__VariableAssignment_0 )
                    // InternalStreamingsparql.g:642:4: rule__GraphNode__VariableAssignment_0
                    {
                    pushFollow(FOLLOW_2);
                    rule__GraphNode__VariableAssignment_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getGraphNodeAccess().getVariableAssignment_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:646:2: ( ( rule__GraphNode__LiteralAssignment_1 ) )
                    {
                    // InternalStreamingsparql.g:646:2: ( ( rule__GraphNode__LiteralAssignment_1 ) )
                    // InternalStreamingsparql.g:647:3: ( rule__GraphNode__LiteralAssignment_1 )
                    {
                     before(grammarAccess.getGraphNodeAccess().getLiteralAssignment_1()); 
                    // InternalStreamingsparql.g:648:3: ( rule__GraphNode__LiteralAssignment_1 )
                    // InternalStreamingsparql.g:648:4: rule__GraphNode__LiteralAssignment_1
                    {
                    pushFollow(FOLLOW_2);
                    rule__GraphNode__LiteralAssignment_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getGraphNodeAccess().getLiteralAssignment_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalStreamingsparql.g:652:2: ( ( rule__GraphNode__IriAssignment_2 ) )
                    {
                    // InternalStreamingsparql.g:652:2: ( ( rule__GraphNode__IriAssignment_2 ) )
                    // InternalStreamingsparql.g:653:3: ( rule__GraphNode__IriAssignment_2 )
                    {
                     before(grammarAccess.getGraphNodeAccess().getIriAssignment_2()); 
                    // InternalStreamingsparql.g:654:3: ( rule__GraphNode__IriAssignment_2 )
                    // InternalStreamingsparql.g:654:4: rule__GraphNode__IriAssignment_2
                    {
                    pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:662:1: rule__Variable__Alternatives : ( ( ( rule__Variable__UnnamedAssignment_0 ) ) | ( ( rule__Variable__PropertyAssignment_1 ) ) );
    public final void rule__Variable__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:666:1: ( ( ( rule__Variable__UnnamedAssignment_0 ) ) | ( ( rule__Variable__PropertyAssignment_1 ) ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==50) ) {
                alt4=1;
            }
            else if ( (LA4_0==RULE_ID) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // InternalStreamingsparql.g:667:2: ( ( rule__Variable__UnnamedAssignment_0 ) )
                    {
                    // InternalStreamingsparql.g:667:2: ( ( rule__Variable__UnnamedAssignment_0 ) )
                    // InternalStreamingsparql.g:668:3: ( rule__Variable__UnnamedAssignment_0 )
                    {
                     before(grammarAccess.getVariableAccess().getUnnamedAssignment_0()); 
                    // InternalStreamingsparql.g:669:3: ( rule__Variable__UnnamedAssignment_0 )
                    // InternalStreamingsparql.g:669:4: rule__Variable__UnnamedAssignment_0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Variable__UnnamedAssignment_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getVariableAccess().getUnnamedAssignment_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:673:2: ( ( rule__Variable__PropertyAssignment_1 ) )
                    {
                    // InternalStreamingsparql.g:673:2: ( ( rule__Variable__PropertyAssignment_1 ) )
                    // InternalStreamingsparql.g:674:3: ( rule__Variable__PropertyAssignment_1 )
                    {
                     before(grammarAccess.getVariableAccess().getPropertyAssignment_1()); 
                    // InternalStreamingsparql.g:675:3: ( rule__Variable__PropertyAssignment_1 )
                    // InternalStreamingsparql.g:675:4: rule__Variable__PropertyAssignment_1
                    {
                    pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:683:1: rule__Operator__Alternatives : ( ( ( '<' ) ) | ( ( '>' ) ) | ( ( '<=' ) ) | ( ( '>=' ) ) | ( ( '=' ) ) | ( ( '!=' ) ) | ( ( '+' ) ) | ( ( '/' ) ) | ( ( '-' ) ) | ( ( '*' ) ) );
    public final void rule__Operator__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:687:1: ( ( ( '<' ) ) | ( ( '>' ) ) | ( ( '<=' ) ) | ( ( '>=' ) ) | ( ( '=' ) ) | ( ( '!=' ) ) | ( ( '+' ) ) | ( ( '/' ) ) | ( ( '-' ) ) | ( ( '*' ) ) )
            int alt5=10;
            switch ( input.LA(1) ) {
            case 16:
                {
                alt5=1;
                }
                break;
            case 17:
                {
                alt5=2;
                }
                break;
            case 18:
                {
                alt5=3;
                }
                break;
            case 19:
                {
                alt5=4;
                }
                break;
            case 20:
                {
                alt5=5;
                }
                break;
            case 21:
                {
                alt5=6;
                }
                break;
            case 22:
                {
                alt5=7;
                }
                break;
            case 23:
                {
                alt5=8;
                }
                break;
            case 24:
                {
                alt5=9;
                }
                break;
            case 25:
                {
                alt5=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // InternalStreamingsparql.g:688:2: ( ( '<' ) )
                    {
                    // InternalStreamingsparql.g:688:2: ( ( '<' ) )
                    // InternalStreamingsparql.g:689:3: ( '<' )
                    {
                     before(grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0()); 
                    // InternalStreamingsparql.g:690:3: ( '<' )
                    // InternalStreamingsparql.g:690:4: '<'
                    {
                    match(input,16,FOLLOW_2); 

                    }

                     after(grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalStreamingsparql.g:694:2: ( ( '>' ) )
                    {
                    // InternalStreamingsparql.g:694:2: ( ( '>' ) )
                    // InternalStreamingsparql.g:695:3: ( '>' )
                    {
                     before(grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1()); 
                    // InternalStreamingsparql.g:696:3: ( '>' )
                    // InternalStreamingsparql.g:696:4: '>'
                    {
                    match(input,17,FOLLOW_2); 

                    }

                     after(grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalStreamingsparql.g:700:2: ( ( '<=' ) )
                    {
                    // InternalStreamingsparql.g:700:2: ( ( '<=' ) )
                    // InternalStreamingsparql.g:701:3: ( '<=' )
                    {
                     before(grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2()); 
                    // InternalStreamingsparql.g:702:3: ( '<=' )
                    // InternalStreamingsparql.g:702:4: '<='
                    {
                    match(input,18,FOLLOW_2); 

                    }

                     after(grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalStreamingsparql.g:706:2: ( ( '>=' ) )
                    {
                    // InternalStreamingsparql.g:706:2: ( ( '>=' ) )
                    // InternalStreamingsparql.g:707:3: ( '>=' )
                    {
                     before(grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3()); 
                    // InternalStreamingsparql.g:708:3: ( '>=' )
                    // InternalStreamingsparql.g:708:4: '>='
                    {
                    match(input,19,FOLLOW_2); 

                    }

                     after(grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalStreamingsparql.g:712:2: ( ( '=' ) )
                    {
                    // InternalStreamingsparql.g:712:2: ( ( '=' ) )
                    // InternalStreamingsparql.g:713:3: ( '=' )
                    {
                     before(grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4()); 
                    // InternalStreamingsparql.g:714:3: ( '=' )
                    // InternalStreamingsparql.g:714:4: '='
                    {
                    match(input,20,FOLLOW_2); 

                    }

                     after(grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4()); 

                    }


                    }
                    break;
                case 6 :
                    // InternalStreamingsparql.g:718:2: ( ( '!=' ) )
                    {
                    // InternalStreamingsparql.g:718:2: ( ( '!=' ) )
                    // InternalStreamingsparql.g:719:3: ( '!=' )
                    {
                     before(grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5()); 
                    // InternalStreamingsparql.g:720:3: ( '!=' )
                    // InternalStreamingsparql.g:720:4: '!='
                    {
                    match(input,21,FOLLOW_2); 

                    }

                     after(grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5()); 

                    }


                    }
                    break;
                case 7 :
                    // InternalStreamingsparql.g:724:2: ( ( '+' ) )
                    {
                    // InternalStreamingsparql.g:724:2: ( ( '+' ) )
                    // InternalStreamingsparql.g:725:3: ( '+' )
                    {
                     before(grammarAccess.getOperatorAccess().getSumEnumLiteralDeclaration_6()); 
                    // InternalStreamingsparql.g:726:3: ( '+' )
                    // InternalStreamingsparql.g:726:4: '+'
                    {
                    match(input,22,FOLLOW_2); 

                    }

                     after(grammarAccess.getOperatorAccess().getSumEnumLiteralDeclaration_6()); 

                    }


                    }
                    break;
                case 8 :
                    // InternalStreamingsparql.g:730:2: ( ( '/' ) )
                    {
                    // InternalStreamingsparql.g:730:2: ( ( '/' ) )
                    // InternalStreamingsparql.g:731:3: ( '/' )
                    {
                     before(grammarAccess.getOperatorAccess().getDivEnumLiteralDeclaration_7()); 
                    // InternalStreamingsparql.g:732:3: ( '/' )
                    // InternalStreamingsparql.g:732:4: '/'
                    {
                    match(input,23,FOLLOW_2); 

                    }

                     after(grammarAccess.getOperatorAccess().getDivEnumLiteralDeclaration_7()); 

                    }


                    }
                    break;
                case 9 :
                    // InternalStreamingsparql.g:736:2: ( ( '-' ) )
                    {
                    // InternalStreamingsparql.g:736:2: ( ( '-' ) )
                    // InternalStreamingsparql.g:737:3: ( '-' )
                    {
                     before(grammarAccess.getOperatorAccess().getSubEnumLiteralDeclaration_8()); 
                    // InternalStreamingsparql.g:738:3: ( '-' )
                    // InternalStreamingsparql.g:738:4: '-'
                    {
                    match(input,24,FOLLOW_2); 

                    }

                     after(grammarAccess.getOperatorAccess().getSubEnumLiteralDeclaration_8()); 

                    }


                    }
                    break;
                case 10 :
                    // InternalStreamingsparql.g:742:2: ( ( '*' ) )
                    {
                    // InternalStreamingsparql.g:742:2: ( ( '*' ) )
                    // InternalStreamingsparql.g:743:3: ( '*' )
                    {
                     before(grammarAccess.getOperatorAccess().getMultiplicityEnumLiteralDeclaration_9()); 
                    // InternalStreamingsparql.g:744:3: ( '*' )
                    // InternalStreamingsparql.g:744:4: '*'
                    {
                    match(input,25,FOLLOW_2); 

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
    // InternalStreamingsparql.g:752:1: rule__Prefix__Group_0__0 : rule__Prefix__Group_0__0__Impl rule__Prefix__Group_0__1 ;
    public final void rule__Prefix__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:756:1: ( rule__Prefix__Group_0__0__Impl rule__Prefix__Group_0__1 )
            // InternalStreamingsparql.g:757:2: rule__Prefix__Group_0__0__Impl rule__Prefix__Group_0__1
            {
            pushFollow(FOLLOW_3);
            rule__Prefix__Group_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:764:1: rule__Prefix__Group_0__0__Impl : ( 'PREFIX' ) ;
    public final void rule__Prefix__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:768:1: ( ( 'PREFIX' ) )
            // InternalStreamingsparql.g:769:1: ( 'PREFIX' )
            {
            // InternalStreamingsparql.g:769:1: ( 'PREFIX' )
            // InternalStreamingsparql.g:770:2: 'PREFIX'
            {
             before(grammarAccess.getPrefixAccess().getPREFIXKeyword_0_0()); 
            match(input,26,FOLLOW_2); 
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
    // InternalStreamingsparql.g:779:1: rule__Prefix__Group_0__1 : rule__Prefix__Group_0__1__Impl rule__Prefix__Group_0__2 ;
    public final void rule__Prefix__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:783:1: ( rule__Prefix__Group_0__1__Impl rule__Prefix__Group_0__2 )
            // InternalStreamingsparql.g:784:2: rule__Prefix__Group_0__1__Impl rule__Prefix__Group_0__2
            {
            pushFollow(FOLLOW_4);
            rule__Prefix__Group_0__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:791:1: rule__Prefix__Group_0__1__Impl : ( ( rule__Prefix__NameAssignment_0_1 ) ) ;
    public final void rule__Prefix__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:795:1: ( ( ( rule__Prefix__NameAssignment_0_1 ) ) )
            // InternalStreamingsparql.g:796:1: ( ( rule__Prefix__NameAssignment_0_1 ) )
            {
            // InternalStreamingsparql.g:796:1: ( ( rule__Prefix__NameAssignment_0_1 ) )
            // InternalStreamingsparql.g:797:2: ( rule__Prefix__NameAssignment_0_1 )
            {
             before(grammarAccess.getPrefixAccess().getNameAssignment_0_1()); 
            // InternalStreamingsparql.g:798:2: ( rule__Prefix__NameAssignment_0_1 )
            // InternalStreamingsparql.g:798:3: rule__Prefix__NameAssignment_0_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:806:1: rule__Prefix__Group_0__2 : rule__Prefix__Group_0__2__Impl rule__Prefix__Group_0__3 ;
    public final void rule__Prefix__Group_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:810:1: ( rule__Prefix__Group_0__2__Impl rule__Prefix__Group_0__3 )
            // InternalStreamingsparql.g:811:2: rule__Prefix__Group_0__2__Impl rule__Prefix__Group_0__3
            {
            pushFollow(FOLLOW_5);
            rule__Prefix__Group_0__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:818:1: rule__Prefix__Group_0__2__Impl : ( ':' ) ;
    public final void rule__Prefix__Group_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:822:1: ( ( ':' ) )
            // InternalStreamingsparql.g:823:1: ( ':' )
            {
            // InternalStreamingsparql.g:823:1: ( ':' )
            // InternalStreamingsparql.g:824:2: ':'
            {
             before(grammarAccess.getPrefixAccess().getColonKeyword_0_2()); 
            match(input,27,FOLLOW_2); 
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
    // InternalStreamingsparql.g:833:1: rule__Prefix__Group_0__3 : rule__Prefix__Group_0__3__Impl ;
    public final void rule__Prefix__Group_0__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:837:1: ( rule__Prefix__Group_0__3__Impl )
            // InternalStreamingsparql.g:838:2: rule__Prefix__Group_0__3__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:844:1: rule__Prefix__Group_0__3__Impl : ( ( rule__Prefix__IrefAssignment_0_3 ) ) ;
    public final void rule__Prefix__Group_0__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:848:1: ( ( ( rule__Prefix__IrefAssignment_0_3 ) ) )
            // InternalStreamingsparql.g:849:1: ( ( rule__Prefix__IrefAssignment_0_3 ) )
            {
            // InternalStreamingsparql.g:849:1: ( ( rule__Prefix__IrefAssignment_0_3 ) )
            // InternalStreamingsparql.g:850:2: ( rule__Prefix__IrefAssignment_0_3 )
            {
             before(grammarAccess.getPrefixAccess().getIrefAssignment_0_3()); 
            // InternalStreamingsparql.g:851:2: ( rule__Prefix__IrefAssignment_0_3 )
            // InternalStreamingsparql.g:851:3: rule__Prefix__IrefAssignment_0_3
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:860:1: rule__UnNamedPrefix__Group__0 : rule__UnNamedPrefix__Group__0__Impl rule__UnNamedPrefix__Group__1 ;
    public final void rule__UnNamedPrefix__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:864:1: ( rule__UnNamedPrefix__Group__0__Impl rule__UnNamedPrefix__Group__1 )
            // InternalStreamingsparql.g:865:2: rule__UnNamedPrefix__Group__0__Impl rule__UnNamedPrefix__Group__1
            {
            pushFollow(FOLLOW_4);
            rule__UnNamedPrefix__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:872:1: rule__UnNamedPrefix__Group__0__Impl : ( 'PREFIX' ) ;
    public final void rule__UnNamedPrefix__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:876:1: ( ( 'PREFIX' ) )
            // InternalStreamingsparql.g:877:1: ( 'PREFIX' )
            {
            // InternalStreamingsparql.g:877:1: ( 'PREFIX' )
            // InternalStreamingsparql.g:878:2: 'PREFIX'
            {
             before(grammarAccess.getUnNamedPrefixAccess().getPREFIXKeyword_0()); 
            match(input,26,FOLLOW_2); 
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
    // InternalStreamingsparql.g:887:1: rule__UnNamedPrefix__Group__1 : rule__UnNamedPrefix__Group__1__Impl rule__UnNamedPrefix__Group__2 ;
    public final void rule__UnNamedPrefix__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:891:1: ( rule__UnNamedPrefix__Group__1__Impl rule__UnNamedPrefix__Group__2 )
            // InternalStreamingsparql.g:892:2: rule__UnNamedPrefix__Group__1__Impl rule__UnNamedPrefix__Group__2
            {
            pushFollow(FOLLOW_5);
            rule__UnNamedPrefix__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:899:1: rule__UnNamedPrefix__Group__1__Impl : ( ':' ) ;
    public final void rule__UnNamedPrefix__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:903:1: ( ( ':' ) )
            // InternalStreamingsparql.g:904:1: ( ':' )
            {
            // InternalStreamingsparql.g:904:1: ( ':' )
            // InternalStreamingsparql.g:905:2: ':'
            {
             before(grammarAccess.getUnNamedPrefixAccess().getColonKeyword_1()); 
            match(input,27,FOLLOW_2); 
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
    // InternalStreamingsparql.g:914:1: rule__UnNamedPrefix__Group__2 : rule__UnNamedPrefix__Group__2__Impl ;
    public final void rule__UnNamedPrefix__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:918:1: ( rule__UnNamedPrefix__Group__2__Impl )
            // InternalStreamingsparql.g:919:2: rule__UnNamedPrefix__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:925:1: rule__UnNamedPrefix__Group__2__Impl : ( ( rule__UnNamedPrefix__IrefAssignment_2 ) ) ;
    public final void rule__UnNamedPrefix__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:929:1: ( ( ( rule__UnNamedPrefix__IrefAssignment_2 ) ) )
            // InternalStreamingsparql.g:930:1: ( ( rule__UnNamedPrefix__IrefAssignment_2 ) )
            {
            // InternalStreamingsparql.g:930:1: ( ( rule__UnNamedPrefix__IrefAssignment_2 ) )
            // InternalStreamingsparql.g:931:2: ( rule__UnNamedPrefix__IrefAssignment_2 )
            {
             before(grammarAccess.getUnNamedPrefixAccess().getIrefAssignment_2()); 
            // InternalStreamingsparql.g:932:2: ( rule__UnNamedPrefix__IrefAssignment_2 )
            // InternalStreamingsparql.g:932:3: rule__UnNamedPrefix__IrefAssignment_2
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:941:1: rule__Base__Group__0 : rule__Base__Group__0__Impl rule__Base__Group__1 ;
    public final void rule__Base__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:945:1: ( rule__Base__Group__0__Impl rule__Base__Group__1 )
            // InternalStreamingsparql.g:946:2: rule__Base__Group__0__Impl rule__Base__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__Base__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:953:1: rule__Base__Group__0__Impl : ( 'BASE' ) ;
    public final void rule__Base__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:957:1: ( ( 'BASE' ) )
            // InternalStreamingsparql.g:958:1: ( 'BASE' )
            {
            // InternalStreamingsparql.g:958:1: ( 'BASE' )
            // InternalStreamingsparql.g:959:2: 'BASE'
            {
             before(grammarAccess.getBaseAccess().getBASEKeyword_0()); 
            match(input,28,FOLLOW_2); 
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
    // InternalStreamingsparql.g:968:1: rule__Base__Group__1 : rule__Base__Group__1__Impl ;
    public final void rule__Base__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:972:1: ( rule__Base__Group__1__Impl )
            // InternalStreamingsparql.g:973:2: rule__Base__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:979:1: rule__Base__Group__1__Impl : ( ( rule__Base__IrefAssignment_1 ) ) ;
    public final void rule__Base__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:983:1: ( ( ( rule__Base__IrefAssignment_1 ) ) )
            // InternalStreamingsparql.g:984:1: ( ( rule__Base__IrefAssignment_1 ) )
            {
            // InternalStreamingsparql.g:984:1: ( ( rule__Base__IrefAssignment_1 ) )
            // InternalStreamingsparql.g:985:2: ( rule__Base__IrefAssignment_1 )
            {
             before(grammarAccess.getBaseAccess().getIrefAssignment_1()); 
            // InternalStreamingsparql.g:986:2: ( rule__Base__IrefAssignment_1 )
            // InternalStreamingsparql.g:986:3: rule__Base__IrefAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:995:1: rule__SelectQuery__Group__0 : rule__SelectQuery__Group__0__Impl rule__SelectQuery__Group__1 ;
    public final void rule__SelectQuery__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:999:1: ( rule__SelectQuery__Group__0__Impl rule__SelectQuery__Group__1 )
            // InternalStreamingsparql.g:1000:2: rule__SelectQuery__Group__0__Impl rule__SelectQuery__Group__1
            {
            pushFollow(FOLLOW_6);
            rule__SelectQuery__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1007:1: rule__SelectQuery__Group__0__Impl : ( ( rule__SelectQuery__Alternatives_0 )? ) ;
    public final void rule__SelectQuery__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1011:1: ( ( ( rule__SelectQuery__Alternatives_0 )? ) )
            // InternalStreamingsparql.g:1012:1: ( ( rule__SelectQuery__Alternatives_0 )? )
            {
            // InternalStreamingsparql.g:1012:1: ( ( rule__SelectQuery__Alternatives_0 )? )
            // InternalStreamingsparql.g:1013:2: ( rule__SelectQuery__Alternatives_0 )?
            {
             before(grammarAccess.getSelectQueryAccess().getAlternatives_0()); 
            // InternalStreamingsparql.g:1014:2: ( rule__SelectQuery__Alternatives_0 )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==15||LA6_0==51) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // InternalStreamingsparql.g:1014:3: rule__SelectQuery__Alternatives_0
                    {
                    pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1022:1: rule__SelectQuery__Group__1 : rule__SelectQuery__Group__1__Impl rule__SelectQuery__Group__2 ;
    public final void rule__SelectQuery__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1026:1: ( rule__SelectQuery__Group__1__Impl rule__SelectQuery__Group__2 )
            // InternalStreamingsparql.g:1027:2: rule__SelectQuery__Group__1__Impl rule__SelectQuery__Group__2
            {
            pushFollow(FOLLOW_6);
            rule__SelectQuery__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1034:1: rule__SelectQuery__Group__1__Impl : ( ( rule__SelectQuery__BaseAssignment_1 )? ) ;
    public final void rule__SelectQuery__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1038:1: ( ( ( rule__SelectQuery__BaseAssignment_1 )? ) )
            // InternalStreamingsparql.g:1039:1: ( ( rule__SelectQuery__BaseAssignment_1 )? )
            {
            // InternalStreamingsparql.g:1039:1: ( ( rule__SelectQuery__BaseAssignment_1 )? )
            // InternalStreamingsparql.g:1040:2: ( rule__SelectQuery__BaseAssignment_1 )?
            {
             before(grammarAccess.getSelectQueryAccess().getBaseAssignment_1()); 
            // InternalStreamingsparql.g:1041:2: ( rule__SelectQuery__BaseAssignment_1 )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==28) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // InternalStreamingsparql.g:1041:3: rule__SelectQuery__BaseAssignment_1
                    {
                    pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1049:1: rule__SelectQuery__Group__2 : rule__SelectQuery__Group__2__Impl rule__SelectQuery__Group__3 ;
    public final void rule__SelectQuery__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1053:1: ( rule__SelectQuery__Group__2__Impl rule__SelectQuery__Group__3 )
            // InternalStreamingsparql.g:1054:2: rule__SelectQuery__Group__2__Impl rule__SelectQuery__Group__3
            {
            pushFollow(FOLLOW_6);
            rule__SelectQuery__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1061:1: rule__SelectQuery__Group__2__Impl : ( ( rule__SelectQuery__PrefixesAssignment_2 )* ) ;
    public final void rule__SelectQuery__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1065:1: ( ( ( rule__SelectQuery__PrefixesAssignment_2 )* ) )
            // InternalStreamingsparql.g:1066:1: ( ( rule__SelectQuery__PrefixesAssignment_2 )* )
            {
            // InternalStreamingsparql.g:1066:1: ( ( rule__SelectQuery__PrefixesAssignment_2 )* )
            // InternalStreamingsparql.g:1067:2: ( rule__SelectQuery__PrefixesAssignment_2 )*
            {
             before(grammarAccess.getSelectQueryAccess().getPrefixesAssignment_2()); 
            // InternalStreamingsparql.g:1068:2: ( rule__SelectQuery__PrefixesAssignment_2 )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==26) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalStreamingsparql.g:1068:3: rule__SelectQuery__PrefixesAssignment_2
            	    {
            	    pushFollow(FOLLOW_7);
            	    rule__SelectQuery__PrefixesAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop8;
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
    // InternalStreamingsparql.g:1076:1: rule__SelectQuery__Group__3 : rule__SelectQuery__Group__3__Impl rule__SelectQuery__Group__4 ;
    public final void rule__SelectQuery__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1080:1: ( rule__SelectQuery__Group__3__Impl rule__SelectQuery__Group__4 )
            // InternalStreamingsparql.g:1081:2: rule__SelectQuery__Group__3__Impl rule__SelectQuery__Group__4
            {
            pushFollow(FOLLOW_6);
            rule__SelectQuery__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1088:1: rule__SelectQuery__Group__3__Impl : ( ( rule__SelectQuery__DatasetClausesAssignment_3 )* ) ;
    public final void rule__SelectQuery__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1092:1: ( ( ( rule__SelectQuery__DatasetClausesAssignment_3 )* ) )
            // InternalStreamingsparql.g:1093:1: ( ( rule__SelectQuery__DatasetClausesAssignment_3 )* )
            {
            // InternalStreamingsparql.g:1093:1: ( ( rule__SelectQuery__DatasetClausesAssignment_3 )* )
            // InternalStreamingsparql.g:1094:2: ( rule__SelectQuery__DatasetClausesAssignment_3 )*
            {
             before(grammarAccess.getSelectQueryAccess().getDatasetClausesAssignment_3()); 
            // InternalStreamingsparql.g:1095:2: ( rule__SelectQuery__DatasetClausesAssignment_3 )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==39) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalStreamingsparql.g:1095:3: rule__SelectQuery__DatasetClausesAssignment_3
            	    {
            	    pushFollow(FOLLOW_8);
            	    rule__SelectQuery__DatasetClausesAssignment_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
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
    // InternalStreamingsparql.g:1103:1: rule__SelectQuery__Group__4 : rule__SelectQuery__Group__4__Impl rule__SelectQuery__Group__5 ;
    public final void rule__SelectQuery__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1107:1: ( rule__SelectQuery__Group__4__Impl rule__SelectQuery__Group__5 )
            // InternalStreamingsparql.g:1108:2: rule__SelectQuery__Group__4__Impl rule__SelectQuery__Group__5
            {
            pushFollow(FOLLOW_9);
            rule__SelectQuery__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1115:1: rule__SelectQuery__Group__4__Impl : ( 'SELECT' ) ;
    public final void rule__SelectQuery__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1119:1: ( ( 'SELECT' ) )
            // InternalStreamingsparql.g:1120:1: ( 'SELECT' )
            {
            // InternalStreamingsparql.g:1120:1: ( 'SELECT' )
            // InternalStreamingsparql.g:1121:2: 'SELECT'
            {
             before(grammarAccess.getSelectQueryAccess().getSELECTKeyword_4()); 
            match(input,29,FOLLOW_2); 
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
    // InternalStreamingsparql.g:1130:1: rule__SelectQuery__Group__5 : rule__SelectQuery__Group__5__Impl rule__SelectQuery__Group__6 ;
    public final void rule__SelectQuery__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1134:1: ( rule__SelectQuery__Group__5__Impl rule__SelectQuery__Group__6 )
            // InternalStreamingsparql.g:1135:2: rule__SelectQuery__Group__5__Impl rule__SelectQuery__Group__6
            {
            pushFollow(FOLLOW_10);
            rule__SelectQuery__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1142:1: rule__SelectQuery__Group__5__Impl : ( ( rule__SelectQuery__VariablesAssignment_5 ) ) ;
    public final void rule__SelectQuery__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1146:1: ( ( ( rule__SelectQuery__VariablesAssignment_5 ) ) )
            // InternalStreamingsparql.g:1147:1: ( ( rule__SelectQuery__VariablesAssignment_5 ) )
            {
            // InternalStreamingsparql.g:1147:1: ( ( rule__SelectQuery__VariablesAssignment_5 ) )
            // InternalStreamingsparql.g:1148:2: ( rule__SelectQuery__VariablesAssignment_5 )
            {
             before(grammarAccess.getSelectQueryAccess().getVariablesAssignment_5()); 
            // InternalStreamingsparql.g:1149:2: ( rule__SelectQuery__VariablesAssignment_5 )
            // InternalStreamingsparql.g:1149:3: rule__SelectQuery__VariablesAssignment_5
            {
            pushFollow(FOLLOW_2);
            rule__SelectQuery__VariablesAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getSelectQueryAccess().getVariablesAssignment_5()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:1157:1: rule__SelectQuery__Group__6 : rule__SelectQuery__Group__6__Impl rule__SelectQuery__Group__7 ;
    public final void rule__SelectQuery__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1161:1: ( rule__SelectQuery__Group__6__Impl rule__SelectQuery__Group__7 )
            // InternalStreamingsparql.g:1162:2: rule__SelectQuery__Group__6__Impl rule__SelectQuery__Group__7
            {
            pushFollow(FOLLOW_10);
            rule__SelectQuery__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1169:1: rule__SelectQuery__Group__6__Impl : ( ( rule__SelectQuery__VariablesAssignment_6 )* ) ;
    public final void rule__SelectQuery__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1173:1: ( ( ( rule__SelectQuery__VariablesAssignment_6 )* ) )
            // InternalStreamingsparql.g:1174:1: ( ( rule__SelectQuery__VariablesAssignment_6 )* )
            {
            // InternalStreamingsparql.g:1174:1: ( ( rule__SelectQuery__VariablesAssignment_6 )* )
            // InternalStreamingsparql.g:1175:2: ( rule__SelectQuery__VariablesAssignment_6 )*
            {
             before(grammarAccess.getSelectQueryAccess().getVariablesAssignment_6()); 
            // InternalStreamingsparql.g:1176:2: ( rule__SelectQuery__VariablesAssignment_6 )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==RULE_ID||LA10_0==50) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // InternalStreamingsparql.g:1176:3: rule__SelectQuery__VariablesAssignment_6
            	    {
            	    pushFollow(FOLLOW_11);
            	    rule__SelectQuery__VariablesAssignment_6();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

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
    // InternalStreamingsparql.g:1184:1: rule__SelectQuery__Group__7 : rule__SelectQuery__Group__7__Impl rule__SelectQuery__Group__8 ;
    public final void rule__SelectQuery__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1188:1: ( rule__SelectQuery__Group__7__Impl rule__SelectQuery__Group__8 )
            // InternalStreamingsparql.g:1189:2: rule__SelectQuery__Group__7__Impl rule__SelectQuery__Group__8
            {
            pushFollow(FOLLOW_12);
            rule__SelectQuery__Group__7__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1196:1: rule__SelectQuery__Group__7__Impl : ( ( rule__SelectQuery__WhereClauseAssignment_7 ) ) ;
    public final void rule__SelectQuery__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1200:1: ( ( ( rule__SelectQuery__WhereClauseAssignment_7 ) ) )
            // InternalStreamingsparql.g:1201:1: ( ( rule__SelectQuery__WhereClauseAssignment_7 ) )
            {
            // InternalStreamingsparql.g:1201:1: ( ( rule__SelectQuery__WhereClauseAssignment_7 ) )
            // InternalStreamingsparql.g:1202:2: ( rule__SelectQuery__WhereClauseAssignment_7 )
            {
             before(grammarAccess.getSelectQueryAccess().getWhereClauseAssignment_7()); 
            // InternalStreamingsparql.g:1203:2: ( rule__SelectQuery__WhereClauseAssignment_7 )
            // InternalStreamingsparql.g:1203:3: rule__SelectQuery__WhereClauseAssignment_7
            {
            pushFollow(FOLLOW_2);
            rule__SelectQuery__WhereClauseAssignment_7();

            state._fsp--;


            }

             after(grammarAccess.getSelectQueryAccess().getWhereClauseAssignment_7()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:1211:1: rule__SelectQuery__Group__8 : rule__SelectQuery__Group__8__Impl rule__SelectQuery__Group__9 ;
    public final void rule__SelectQuery__Group__8() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1215:1: ( rule__SelectQuery__Group__8__Impl rule__SelectQuery__Group__9 )
            // InternalStreamingsparql.g:1216:2: rule__SelectQuery__Group__8__Impl rule__SelectQuery__Group__9
            {
            pushFollow(FOLLOW_12);
            rule__SelectQuery__Group__8__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1223:1: rule__SelectQuery__Group__8__Impl : ( ( rule__SelectQuery__FilterclauseAssignment_8 )? ) ;
    public final void rule__SelectQuery__Group__8__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1227:1: ( ( ( rule__SelectQuery__FilterclauseAssignment_8 )? ) )
            // InternalStreamingsparql.g:1228:1: ( ( rule__SelectQuery__FilterclauseAssignment_8 )? )
            {
            // InternalStreamingsparql.g:1228:1: ( ( rule__SelectQuery__FilterclauseAssignment_8 )? )
            // InternalStreamingsparql.g:1229:2: ( rule__SelectQuery__FilterclauseAssignment_8 )?
            {
             before(grammarAccess.getSelectQueryAccess().getFilterclauseAssignment_8()); 
            // InternalStreamingsparql.g:1230:2: ( rule__SelectQuery__FilterclauseAssignment_8 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==38) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalStreamingsparql.g:1230:3: rule__SelectQuery__FilterclauseAssignment_8
                    {
                    pushFollow(FOLLOW_2);
                    rule__SelectQuery__FilterclauseAssignment_8();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectQueryAccess().getFilterclauseAssignment_8()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:1238:1: rule__SelectQuery__Group__9 : rule__SelectQuery__Group__9__Impl rule__SelectQuery__Group__10 ;
    public final void rule__SelectQuery__Group__9() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1242:1: ( rule__SelectQuery__Group__9__Impl rule__SelectQuery__Group__10 )
            // InternalStreamingsparql.g:1243:2: rule__SelectQuery__Group__9__Impl rule__SelectQuery__Group__10
            {
            pushFollow(FOLLOW_12);
            rule__SelectQuery__Group__9__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1250:1: rule__SelectQuery__Group__9__Impl : ( ( rule__SelectQuery__AggregateClauseAssignment_9 )? ) ;
    public final void rule__SelectQuery__Group__9__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1254:1: ( ( ( rule__SelectQuery__AggregateClauseAssignment_9 )? ) )
            // InternalStreamingsparql.g:1255:1: ( ( rule__SelectQuery__AggregateClauseAssignment_9 )? )
            {
            // InternalStreamingsparql.g:1255:1: ( ( rule__SelectQuery__AggregateClauseAssignment_9 )? )
            // InternalStreamingsparql.g:1256:2: ( rule__SelectQuery__AggregateClauseAssignment_9 )?
            {
             before(grammarAccess.getSelectQueryAccess().getAggregateClauseAssignment_9()); 
            // InternalStreamingsparql.g:1257:2: ( rule__SelectQuery__AggregateClauseAssignment_9 )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==30) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalStreamingsparql.g:1257:3: rule__SelectQuery__AggregateClauseAssignment_9
                    {
                    pushFollow(FOLLOW_2);
                    rule__SelectQuery__AggregateClauseAssignment_9();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectQueryAccess().getAggregateClauseAssignment_9()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:1265:1: rule__SelectQuery__Group__10 : rule__SelectQuery__Group__10__Impl ;
    public final void rule__SelectQuery__Group__10() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1269:1: ( rule__SelectQuery__Group__10__Impl )
            // InternalStreamingsparql.g:1270:2: rule__SelectQuery__Group__10__Impl
            {
            pushFollow(FOLLOW_2);
            rule__SelectQuery__Group__10__Impl();

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
    // InternalStreamingsparql.g:1276:1: rule__SelectQuery__Group__10__Impl : ( ( rule__SelectQuery__FilesinkclauseAssignment_10 )? ) ;
    public final void rule__SelectQuery__Group__10__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1280:1: ( ( ( rule__SelectQuery__FilesinkclauseAssignment_10 )? ) )
            // InternalStreamingsparql.g:1281:1: ( ( rule__SelectQuery__FilesinkclauseAssignment_10 )? )
            {
            // InternalStreamingsparql.g:1281:1: ( ( rule__SelectQuery__FilesinkclauseAssignment_10 )? )
            // InternalStreamingsparql.g:1282:2: ( rule__SelectQuery__FilesinkclauseAssignment_10 )?
            {
             before(grammarAccess.getSelectQueryAccess().getFilesinkclauseAssignment_10()); 
            // InternalStreamingsparql.g:1283:2: ( rule__SelectQuery__FilesinkclauseAssignment_10 )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==37) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalStreamingsparql.g:1283:3: rule__SelectQuery__FilesinkclauseAssignment_10
                    {
                    pushFollow(FOLLOW_2);
                    rule__SelectQuery__FilesinkclauseAssignment_10();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelectQueryAccess().getFilesinkclauseAssignment_10()); 

            }


            }

        }
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


    // $ANTLR start "rule__Aggregate__Group__0"
    // InternalStreamingsparql.g:1292:1: rule__Aggregate__Group__0 : rule__Aggregate__Group__0__Impl rule__Aggregate__Group__1 ;
    public final void rule__Aggregate__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1296:1: ( rule__Aggregate__Group__0__Impl rule__Aggregate__Group__1 )
            // InternalStreamingsparql.g:1297:2: rule__Aggregate__Group__0__Impl rule__Aggregate__Group__1
            {
            pushFollow(FOLLOW_13);
            rule__Aggregate__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1304:1: rule__Aggregate__Group__0__Impl : ( () ) ;
    public final void rule__Aggregate__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1308:1: ( ( () ) )
            // InternalStreamingsparql.g:1309:1: ( () )
            {
            // InternalStreamingsparql.g:1309:1: ( () )
            // InternalStreamingsparql.g:1310:2: ()
            {
             before(grammarAccess.getAggregateAccess().getAggregateAction_0()); 
            // InternalStreamingsparql.g:1311:2: ()
            // InternalStreamingsparql.g:1311:3: 
            {
            }

             after(grammarAccess.getAggregateAccess().getAggregateAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Aggregate__Group__0__Impl"


    // $ANTLR start "rule__Aggregate__Group__1"
    // InternalStreamingsparql.g:1319:1: rule__Aggregate__Group__1 : rule__Aggregate__Group__1__Impl rule__Aggregate__Group__2 ;
    public final void rule__Aggregate__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1323:1: ( rule__Aggregate__Group__1__Impl rule__Aggregate__Group__2 )
            // InternalStreamingsparql.g:1324:2: rule__Aggregate__Group__1__Impl rule__Aggregate__Group__2
            {
            pushFollow(FOLLOW_14);
            rule__Aggregate__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1331:1: rule__Aggregate__Group__1__Impl : ( 'AGGREGATE(' ) ;
    public final void rule__Aggregate__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1335:1: ( ( 'AGGREGATE(' ) )
            // InternalStreamingsparql.g:1336:1: ( 'AGGREGATE(' )
            {
            // InternalStreamingsparql.g:1336:1: ( 'AGGREGATE(' )
            // InternalStreamingsparql.g:1337:2: 'AGGREGATE('
            {
             before(grammarAccess.getAggregateAccess().getAGGREGATEKeyword_1()); 
            match(input,30,FOLLOW_2); 
             after(grammarAccess.getAggregateAccess().getAGGREGATEKeyword_1()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:1346:1: rule__Aggregate__Group__2 : rule__Aggregate__Group__2__Impl rule__Aggregate__Group__3 ;
    public final void rule__Aggregate__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1350:1: ( rule__Aggregate__Group__2__Impl rule__Aggregate__Group__3 )
            // InternalStreamingsparql.g:1351:2: rule__Aggregate__Group__2__Impl rule__Aggregate__Group__3
            {
            pushFollow(FOLLOW_14);
            rule__Aggregate__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1358:1: rule__Aggregate__Group__2__Impl : ( ( rule__Aggregate__Group_2__0 )? ) ;
    public final void rule__Aggregate__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1362:1: ( ( ( rule__Aggregate__Group_2__0 )? ) )
            // InternalStreamingsparql.g:1363:1: ( ( rule__Aggregate__Group_2__0 )? )
            {
            // InternalStreamingsparql.g:1363:1: ( ( rule__Aggregate__Group_2__0 )? )
            // InternalStreamingsparql.g:1364:2: ( rule__Aggregate__Group_2__0 )?
            {
             before(grammarAccess.getAggregateAccess().getGroup_2()); 
            // InternalStreamingsparql.g:1365:2: ( rule__Aggregate__Group_2__0 )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==32) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // InternalStreamingsparql.g:1365:3: rule__Aggregate__Group_2__0
                    {
                    pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1373:1: rule__Aggregate__Group__3 : rule__Aggregate__Group__3__Impl rule__Aggregate__Group__4 ;
    public final void rule__Aggregate__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1377:1: ( rule__Aggregate__Group__3__Impl rule__Aggregate__Group__4 )
            // InternalStreamingsparql.g:1378:2: rule__Aggregate__Group__3__Impl rule__Aggregate__Group__4
            {
            pushFollow(FOLLOW_14);
            rule__Aggregate__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Aggregate__Group__4();

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
    // InternalStreamingsparql.g:1385:1: rule__Aggregate__Group__3__Impl : ( ( rule__Aggregate__Group_3__0 )? ) ;
    public final void rule__Aggregate__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1389:1: ( ( ( rule__Aggregate__Group_3__0 )? ) )
            // InternalStreamingsparql.g:1390:1: ( ( rule__Aggregate__Group_3__0 )? )
            {
            // InternalStreamingsparql.g:1390:1: ( ( rule__Aggregate__Group_3__0 )? )
            // InternalStreamingsparql.g:1391:2: ( rule__Aggregate__Group_3__0 )?
            {
             before(grammarAccess.getAggregateAccess().getGroup_3()); 
            // InternalStreamingsparql.g:1392:2: ( rule__Aggregate__Group_3__0 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( ((LA15_0>=35 && LA15_0<=36)) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalStreamingsparql.g:1392:3: rule__Aggregate__Group_3__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Aggregate__Group_3__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getAggregateAccess().getGroup_3()); 

            }


            }

        }
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


    // $ANTLR start "rule__Aggregate__Group__4"
    // InternalStreamingsparql.g:1400:1: rule__Aggregate__Group__4 : rule__Aggregate__Group__4__Impl ;
    public final void rule__Aggregate__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1404:1: ( rule__Aggregate__Group__4__Impl )
            // InternalStreamingsparql.g:1405:2: rule__Aggregate__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Aggregate__Group__4__Impl();

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
    // $ANTLR end "rule__Aggregate__Group__4"


    // $ANTLR start "rule__Aggregate__Group__4__Impl"
    // InternalStreamingsparql.g:1411:1: rule__Aggregate__Group__4__Impl : ( ')' ) ;
    public final void rule__Aggregate__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1415:1: ( ( ')' ) )
            // InternalStreamingsparql.g:1416:1: ( ')' )
            {
            // InternalStreamingsparql.g:1416:1: ( ')' )
            // InternalStreamingsparql.g:1417:2: ')'
            {
             before(grammarAccess.getAggregateAccess().getRightParenthesisKeyword_4()); 
            match(input,31,FOLLOW_2); 
             after(grammarAccess.getAggregateAccess().getRightParenthesisKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Aggregate__Group__4__Impl"


    // $ANTLR start "rule__Aggregate__Group_2__0"
    // InternalStreamingsparql.g:1427:1: rule__Aggregate__Group_2__0 : rule__Aggregate__Group_2__0__Impl rule__Aggregate__Group_2__1 ;
    public final void rule__Aggregate__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1431:1: ( rule__Aggregate__Group_2__0__Impl rule__Aggregate__Group_2__1 )
            // InternalStreamingsparql.g:1432:2: rule__Aggregate__Group_2__0__Impl rule__Aggregate__Group_2__1
            {
            pushFollow(FOLLOW_15);
            rule__Aggregate__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1439:1: rule__Aggregate__Group_2__0__Impl : ( 'aggregations' ) ;
    public final void rule__Aggregate__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1443:1: ( ( 'aggregations' ) )
            // InternalStreamingsparql.g:1444:1: ( 'aggregations' )
            {
            // InternalStreamingsparql.g:1444:1: ( 'aggregations' )
            // InternalStreamingsparql.g:1445:2: 'aggregations'
            {
             before(grammarAccess.getAggregateAccess().getAggregationsKeyword_2_0()); 
            match(input,32,FOLLOW_2); 
             after(grammarAccess.getAggregateAccess().getAggregationsKeyword_2_0()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:1454:1: rule__Aggregate__Group_2__1 : rule__Aggregate__Group_2__1__Impl rule__Aggregate__Group_2__2 ;
    public final void rule__Aggregate__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1458:1: ( rule__Aggregate__Group_2__1__Impl rule__Aggregate__Group_2__2 )
            // InternalStreamingsparql.g:1459:2: rule__Aggregate__Group_2__1__Impl rule__Aggregate__Group_2__2
            {
            pushFollow(FOLLOW_16);
            rule__Aggregate__Group_2__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Aggregate__Group_2__2();

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
    // InternalStreamingsparql.g:1466:1: rule__Aggregate__Group_2__1__Impl : ( '=' ) ;
    public final void rule__Aggregate__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1470:1: ( ( '=' ) )
            // InternalStreamingsparql.g:1471:1: ( '=' )
            {
            // InternalStreamingsparql.g:1471:1: ( '=' )
            // InternalStreamingsparql.g:1472:2: '='
            {
             before(grammarAccess.getAggregateAccess().getEqualsSignKeyword_2_1()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getAggregateAccess().getEqualsSignKeyword_2_1()); 

            }


            }

        }
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


    // $ANTLR start "rule__Aggregate__Group_2__2"
    // InternalStreamingsparql.g:1481:1: rule__Aggregate__Group_2__2 : rule__Aggregate__Group_2__2__Impl rule__Aggregate__Group_2__3 ;
    public final void rule__Aggregate__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1485:1: ( rule__Aggregate__Group_2__2__Impl rule__Aggregate__Group_2__3 )
            // InternalStreamingsparql.g:1486:2: rule__Aggregate__Group_2__2__Impl rule__Aggregate__Group_2__3
            {
            pushFollow(FOLLOW_17);
            rule__Aggregate__Group_2__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Aggregate__Group_2__3();

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
    // $ANTLR end "rule__Aggregate__Group_2__2"


    // $ANTLR start "rule__Aggregate__Group_2__2__Impl"
    // InternalStreamingsparql.g:1493:1: rule__Aggregate__Group_2__2__Impl : ( '[' ) ;
    public final void rule__Aggregate__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1497:1: ( ( '[' ) )
            // InternalStreamingsparql.g:1498:1: ( '[' )
            {
            // InternalStreamingsparql.g:1498:1: ( '[' )
            // InternalStreamingsparql.g:1499:2: '['
            {
             before(grammarAccess.getAggregateAccess().getLeftSquareBracketKeyword_2_2()); 
            match(input,33,FOLLOW_2); 
             after(grammarAccess.getAggregateAccess().getLeftSquareBracketKeyword_2_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Aggregate__Group_2__2__Impl"


    // $ANTLR start "rule__Aggregate__Group_2__3"
    // InternalStreamingsparql.g:1508:1: rule__Aggregate__Group_2__3 : rule__Aggregate__Group_2__3__Impl rule__Aggregate__Group_2__4 ;
    public final void rule__Aggregate__Group_2__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1512:1: ( rule__Aggregate__Group_2__3__Impl rule__Aggregate__Group_2__4 )
            // InternalStreamingsparql.g:1513:2: rule__Aggregate__Group_2__3__Impl rule__Aggregate__Group_2__4
            {
            pushFollow(FOLLOW_17);
            rule__Aggregate__Group_2__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Aggregate__Group_2__4();

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
    // $ANTLR end "rule__Aggregate__Group_2__3"


    // $ANTLR start "rule__Aggregate__Group_2__3__Impl"
    // InternalStreamingsparql.g:1520:1: rule__Aggregate__Group_2__3__Impl : ( ( rule__Aggregate__AggregationsAssignment_2_3 )* ) ;
    public final void rule__Aggregate__Group_2__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1524:1: ( ( ( rule__Aggregate__AggregationsAssignment_2_3 )* ) )
            // InternalStreamingsparql.g:1525:1: ( ( rule__Aggregate__AggregationsAssignment_2_3 )* )
            {
            // InternalStreamingsparql.g:1525:1: ( ( rule__Aggregate__AggregationsAssignment_2_3 )* )
            // InternalStreamingsparql.g:1526:2: ( rule__Aggregate__AggregationsAssignment_2_3 )*
            {
             before(grammarAccess.getAggregateAccess().getAggregationsAssignment_2_3()); 
            // InternalStreamingsparql.g:1527:2: ( rule__Aggregate__AggregationsAssignment_2_3 )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==33) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalStreamingsparql.g:1527:3: rule__Aggregate__AggregationsAssignment_2_3
            	    {
            	    pushFollow(FOLLOW_18);
            	    rule__Aggregate__AggregationsAssignment_2_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

             after(grammarAccess.getAggregateAccess().getAggregationsAssignment_2_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Aggregate__Group_2__3__Impl"


    // $ANTLR start "rule__Aggregate__Group_2__4"
    // InternalStreamingsparql.g:1535:1: rule__Aggregate__Group_2__4 : rule__Aggregate__Group_2__4__Impl ;
    public final void rule__Aggregate__Group_2__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1539:1: ( rule__Aggregate__Group_2__4__Impl )
            // InternalStreamingsparql.g:1540:2: rule__Aggregate__Group_2__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Aggregate__Group_2__4__Impl();

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
    // $ANTLR end "rule__Aggregate__Group_2__4"


    // $ANTLR start "rule__Aggregate__Group_2__4__Impl"
    // InternalStreamingsparql.g:1546:1: rule__Aggregate__Group_2__4__Impl : ( ']' ) ;
    public final void rule__Aggregate__Group_2__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1550:1: ( ( ']' ) )
            // InternalStreamingsparql.g:1551:1: ( ']' )
            {
            // InternalStreamingsparql.g:1551:1: ( ']' )
            // InternalStreamingsparql.g:1552:2: ']'
            {
             before(grammarAccess.getAggregateAccess().getRightSquareBracketKeyword_2_4()); 
            match(input,34,FOLLOW_2); 
             after(grammarAccess.getAggregateAccess().getRightSquareBracketKeyword_2_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Aggregate__Group_2__4__Impl"


    // $ANTLR start "rule__Aggregate__Group_3__0"
    // InternalStreamingsparql.g:1562:1: rule__Aggregate__Group_3__0 : rule__Aggregate__Group_3__0__Impl rule__Aggregate__Group_3__1 ;
    public final void rule__Aggregate__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1566:1: ( rule__Aggregate__Group_3__0__Impl rule__Aggregate__Group_3__1 )
            // InternalStreamingsparql.g:1567:2: rule__Aggregate__Group_3__0__Impl rule__Aggregate__Group_3__1
            {
            pushFollow(FOLLOW_19);
            rule__Aggregate__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Aggregate__Group_3__1();

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
    // $ANTLR end "rule__Aggregate__Group_3__0"


    // $ANTLR start "rule__Aggregate__Group_3__0__Impl"
    // InternalStreamingsparql.g:1574:1: rule__Aggregate__Group_3__0__Impl : ( ( ',' )? ) ;
    public final void rule__Aggregate__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1578:1: ( ( ( ',' )? ) )
            // InternalStreamingsparql.g:1579:1: ( ( ',' )? )
            {
            // InternalStreamingsparql.g:1579:1: ( ( ',' )? )
            // InternalStreamingsparql.g:1580:2: ( ',' )?
            {
             before(grammarAccess.getAggregateAccess().getCommaKeyword_3_0()); 
            // InternalStreamingsparql.g:1581:2: ( ',' )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==35) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // InternalStreamingsparql.g:1581:3: ','
                    {
                    match(input,35,FOLLOW_2); 

                    }
                    break;

            }

             after(grammarAccess.getAggregateAccess().getCommaKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Aggregate__Group_3__0__Impl"


    // $ANTLR start "rule__Aggregate__Group_3__1"
    // InternalStreamingsparql.g:1589:1: rule__Aggregate__Group_3__1 : rule__Aggregate__Group_3__1__Impl ;
    public final void rule__Aggregate__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1593:1: ( rule__Aggregate__Group_3__1__Impl )
            // InternalStreamingsparql.g:1594:2: rule__Aggregate__Group_3__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Aggregate__Group_3__1__Impl();

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
    // $ANTLR end "rule__Aggregate__Group_3__1"


    // $ANTLR start "rule__Aggregate__Group_3__1__Impl"
    // InternalStreamingsparql.g:1600:1: rule__Aggregate__Group_3__1__Impl : ( ( rule__Aggregate__GroupbyAssignment_3_1 ) ) ;
    public final void rule__Aggregate__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1604:1: ( ( ( rule__Aggregate__GroupbyAssignment_3_1 ) ) )
            // InternalStreamingsparql.g:1605:1: ( ( rule__Aggregate__GroupbyAssignment_3_1 ) )
            {
            // InternalStreamingsparql.g:1605:1: ( ( rule__Aggregate__GroupbyAssignment_3_1 ) )
            // InternalStreamingsparql.g:1606:2: ( rule__Aggregate__GroupbyAssignment_3_1 )
            {
             before(grammarAccess.getAggregateAccess().getGroupbyAssignment_3_1()); 
            // InternalStreamingsparql.g:1607:2: ( rule__Aggregate__GroupbyAssignment_3_1 )
            // InternalStreamingsparql.g:1607:3: rule__Aggregate__GroupbyAssignment_3_1
            {
            pushFollow(FOLLOW_2);
            rule__Aggregate__GroupbyAssignment_3_1();

            state._fsp--;


            }

             after(grammarAccess.getAggregateAccess().getGroupbyAssignment_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Aggregate__Group_3__1__Impl"


    // $ANTLR start "rule__GroupBy__Group__0"
    // InternalStreamingsparql.g:1616:1: rule__GroupBy__Group__0 : rule__GroupBy__Group__0__Impl rule__GroupBy__Group__1 ;
    public final void rule__GroupBy__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1620:1: ( rule__GroupBy__Group__0__Impl rule__GroupBy__Group__1 )
            // InternalStreamingsparql.g:1621:2: rule__GroupBy__Group__0__Impl rule__GroupBy__Group__1
            {
            pushFollow(FOLLOW_9);
            rule__GroupBy__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1628:1: rule__GroupBy__Group__0__Impl : ( 'group_by=[' ) ;
    public final void rule__GroupBy__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1632:1: ( ( 'group_by=[' ) )
            // InternalStreamingsparql.g:1633:1: ( 'group_by=[' )
            {
            // InternalStreamingsparql.g:1633:1: ( 'group_by=[' )
            // InternalStreamingsparql.g:1634:2: 'group_by=['
            {
             before(grammarAccess.getGroupByAccess().getGroup_byKeyword_0()); 
            match(input,36,FOLLOW_2); 
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
    // InternalStreamingsparql.g:1643:1: rule__GroupBy__Group__1 : rule__GroupBy__Group__1__Impl rule__GroupBy__Group__2 ;
    public final void rule__GroupBy__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1647:1: ( rule__GroupBy__Group__1__Impl rule__GroupBy__Group__2 )
            // InternalStreamingsparql.g:1648:2: rule__GroupBy__Group__1__Impl rule__GroupBy__Group__2
            {
            pushFollow(FOLLOW_20);
            rule__GroupBy__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1655:1: rule__GroupBy__Group__1__Impl : ( ( rule__GroupBy__VariablesAssignment_1 ) ) ;
    public final void rule__GroupBy__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1659:1: ( ( ( rule__GroupBy__VariablesAssignment_1 ) ) )
            // InternalStreamingsparql.g:1660:1: ( ( rule__GroupBy__VariablesAssignment_1 ) )
            {
            // InternalStreamingsparql.g:1660:1: ( ( rule__GroupBy__VariablesAssignment_1 ) )
            // InternalStreamingsparql.g:1661:2: ( rule__GroupBy__VariablesAssignment_1 )
            {
             before(grammarAccess.getGroupByAccess().getVariablesAssignment_1()); 
            // InternalStreamingsparql.g:1662:2: ( rule__GroupBy__VariablesAssignment_1 )
            // InternalStreamingsparql.g:1662:3: rule__GroupBy__VariablesAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1670:1: rule__GroupBy__Group__2 : rule__GroupBy__Group__2__Impl rule__GroupBy__Group__3 ;
    public final void rule__GroupBy__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1674:1: ( rule__GroupBy__Group__2__Impl rule__GroupBy__Group__3 )
            // InternalStreamingsparql.g:1675:2: rule__GroupBy__Group__2__Impl rule__GroupBy__Group__3
            {
            pushFollow(FOLLOW_20);
            rule__GroupBy__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1682:1: rule__GroupBy__Group__2__Impl : ( ( rule__GroupBy__Group_2__0 )* ) ;
    public final void rule__GroupBy__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1686:1: ( ( ( rule__GroupBy__Group_2__0 )* ) )
            // InternalStreamingsparql.g:1687:1: ( ( rule__GroupBy__Group_2__0 )* )
            {
            // InternalStreamingsparql.g:1687:1: ( ( rule__GroupBy__Group_2__0 )* )
            // InternalStreamingsparql.g:1688:2: ( rule__GroupBy__Group_2__0 )*
            {
             before(grammarAccess.getGroupByAccess().getGroup_2()); 
            // InternalStreamingsparql.g:1689:2: ( rule__GroupBy__Group_2__0 )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==35) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // InternalStreamingsparql.g:1689:3: rule__GroupBy__Group_2__0
            	    {
            	    pushFollow(FOLLOW_21);
            	    rule__GroupBy__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop18;
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
    // InternalStreamingsparql.g:1697:1: rule__GroupBy__Group__3 : rule__GroupBy__Group__3__Impl ;
    public final void rule__GroupBy__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1701:1: ( rule__GroupBy__Group__3__Impl )
            // InternalStreamingsparql.g:1702:2: rule__GroupBy__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1708:1: rule__GroupBy__Group__3__Impl : ( ']' ) ;
    public final void rule__GroupBy__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1712:1: ( ( ']' ) )
            // InternalStreamingsparql.g:1713:1: ( ']' )
            {
            // InternalStreamingsparql.g:1713:1: ( ']' )
            // InternalStreamingsparql.g:1714:2: ']'
            {
             before(grammarAccess.getGroupByAccess().getRightSquareBracketKeyword_3()); 
            match(input,34,FOLLOW_2); 
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
    // InternalStreamingsparql.g:1724:1: rule__GroupBy__Group_2__0 : rule__GroupBy__Group_2__0__Impl rule__GroupBy__Group_2__1 ;
    public final void rule__GroupBy__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1728:1: ( rule__GroupBy__Group_2__0__Impl rule__GroupBy__Group_2__1 )
            // InternalStreamingsparql.g:1729:2: rule__GroupBy__Group_2__0__Impl rule__GroupBy__Group_2__1
            {
            pushFollow(FOLLOW_9);
            rule__GroupBy__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1736:1: rule__GroupBy__Group_2__0__Impl : ( ',' ) ;
    public final void rule__GroupBy__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1740:1: ( ( ',' ) )
            // InternalStreamingsparql.g:1741:1: ( ',' )
            {
            // InternalStreamingsparql.g:1741:1: ( ',' )
            // InternalStreamingsparql.g:1742:2: ','
            {
             before(grammarAccess.getGroupByAccess().getCommaKeyword_2_0()); 
            match(input,35,FOLLOW_2); 
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
    // InternalStreamingsparql.g:1751:1: rule__GroupBy__Group_2__1 : rule__GroupBy__Group_2__1__Impl ;
    public final void rule__GroupBy__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1755:1: ( rule__GroupBy__Group_2__1__Impl )
            // InternalStreamingsparql.g:1756:2: rule__GroupBy__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1762:1: rule__GroupBy__Group_2__1__Impl : ( ( rule__GroupBy__VariablesAssignment_2_1 ) ) ;
    public final void rule__GroupBy__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1766:1: ( ( ( rule__GroupBy__VariablesAssignment_2_1 ) ) )
            // InternalStreamingsparql.g:1767:1: ( ( rule__GroupBy__VariablesAssignment_2_1 ) )
            {
            // InternalStreamingsparql.g:1767:1: ( ( rule__GroupBy__VariablesAssignment_2_1 ) )
            // InternalStreamingsparql.g:1768:2: ( rule__GroupBy__VariablesAssignment_2_1 )
            {
             before(grammarAccess.getGroupByAccess().getVariablesAssignment_2_1()); 
            // InternalStreamingsparql.g:1769:2: ( rule__GroupBy__VariablesAssignment_2_1 )
            // InternalStreamingsparql.g:1769:3: rule__GroupBy__VariablesAssignment_2_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1778:1: rule__Aggregation__Group__0 : rule__Aggregation__Group__0__Impl rule__Aggregation__Group__1 ;
    public final void rule__Aggregation__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1782:1: ( rule__Aggregation__Group__0__Impl rule__Aggregation__Group__1 )
            // InternalStreamingsparql.g:1783:2: rule__Aggregation__Group__0__Impl rule__Aggregation__Group__1
            {
            pushFollow(FOLLOW_22);
            rule__Aggregation__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1790:1: rule__Aggregation__Group__0__Impl : ( '[' ) ;
    public final void rule__Aggregation__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1794:1: ( ( '[' ) )
            // InternalStreamingsparql.g:1795:1: ( '[' )
            {
            // InternalStreamingsparql.g:1795:1: ( '[' )
            // InternalStreamingsparql.g:1796:2: '['
            {
             before(grammarAccess.getAggregationAccess().getLeftSquareBracketKeyword_0()); 
            match(input,33,FOLLOW_2); 
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
    // InternalStreamingsparql.g:1805:1: rule__Aggregation__Group__1 : rule__Aggregation__Group__1__Impl rule__Aggregation__Group__2 ;
    public final void rule__Aggregation__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1809:1: ( rule__Aggregation__Group__1__Impl rule__Aggregation__Group__2 )
            // InternalStreamingsparql.g:1810:2: rule__Aggregation__Group__1__Impl rule__Aggregation__Group__2
            {
            pushFollow(FOLLOW_23);
            rule__Aggregation__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1817:1: rule__Aggregation__Group__1__Impl : ( ( rule__Aggregation__FunctionAssignment_1 ) ) ;
    public final void rule__Aggregation__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1821:1: ( ( ( rule__Aggregation__FunctionAssignment_1 ) ) )
            // InternalStreamingsparql.g:1822:1: ( ( rule__Aggregation__FunctionAssignment_1 ) )
            {
            // InternalStreamingsparql.g:1822:1: ( ( rule__Aggregation__FunctionAssignment_1 ) )
            // InternalStreamingsparql.g:1823:2: ( rule__Aggregation__FunctionAssignment_1 )
            {
             before(grammarAccess.getAggregationAccess().getFunctionAssignment_1()); 
            // InternalStreamingsparql.g:1824:2: ( rule__Aggregation__FunctionAssignment_1 )
            // InternalStreamingsparql.g:1824:3: rule__Aggregation__FunctionAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1832:1: rule__Aggregation__Group__2 : rule__Aggregation__Group__2__Impl rule__Aggregation__Group__3 ;
    public final void rule__Aggregation__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1836:1: ( rule__Aggregation__Group__2__Impl rule__Aggregation__Group__3 )
            // InternalStreamingsparql.g:1837:2: rule__Aggregation__Group__2__Impl rule__Aggregation__Group__3
            {
            pushFollow(FOLLOW_9);
            rule__Aggregation__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1844:1: rule__Aggregation__Group__2__Impl : ( ',' ) ;
    public final void rule__Aggregation__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1848:1: ( ( ',' ) )
            // InternalStreamingsparql.g:1849:1: ( ',' )
            {
            // InternalStreamingsparql.g:1849:1: ( ',' )
            // InternalStreamingsparql.g:1850:2: ','
            {
             before(grammarAccess.getAggregationAccess().getCommaKeyword_2()); 
            match(input,35,FOLLOW_2); 
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
    // InternalStreamingsparql.g:1859:1: rule__Aggregation__Group__3 : rule__Aggregation__Group__3__Impl rule__Aggregation__Group__4 ;
    public final void rule__Aggregation__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1863:1: ( rule__Aggregation__Group__3__Impl rule__Aggregation__Group__4 )
            // InternalStreamingsparql.g:1864:2: rule__Aggregation__Group__3__Impl rule__Aggregation__Group__4
            {
            pushFollow(FOLLOW_23);
            rule__Aggregation__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1871:1: rule__Aggregation__Group__3__Impl : ( ( rule__Aggregation__VarToAggAssignment_3 ) ) ;
    public final void rule__Aggregation__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1875:1: ( ( ( rule__Aggregation__VarToAggAssignment_3 ) ) )
            // InternalStreamingsparql.g:1876:1: ( ( rule__Aggregation__VarToAggAssignment_3 ) )
            {
            // InternalStreamingsparql.g:1876:1: ( ( rule__Aggregation__VarToAggAssignment_3 ) )
            // InternalStreamingsparql.g:1877:2: ( rule__Aggregation__VarToAggAssignment_3 )
            {
             before(grammarAccess.getAggregationAccess().getVarToAggAssignment_3()); 
            // InternalStreamingsparql.g:1878:2: ( rule__Aggregation__VarToAggAssignment_3 )
            // InternalStreamingsparql.g:1878:3: rule__Aggregation__VarToAggAssignment_3
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1886:1: rule__Aggregation__Group__4 : rule__Aggregation__Group__4__Impl rule__Aggregation__Group__5 ;
    public final void rule__Aggregation__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1890:1: ( rule__Aggregation__Group__4__Impl rule__Aggregation__Group__5 )
            // InternalStreamingsparql.g:1891:2: rule__Aggregation__Group__4__Impl rule__Aggregation__Group__5
            {
            pushFollow(FOLLOW_24);
            rule__Aggregation__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1898:1: rule__Aggregation__Group__4__Impl : ( ',' ) ;
    public final void rule__Aggregation__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1902:1: ( ( ',' ) )
            // InternalStreamingsparql.g:1903:1: ( ',' )
            {
            // InternalStreamingsparql.g:1903:1: ( ',' )
            // InternalStreamingsparql.g:1904:2: ','
            {
             before(grammarAccess.getAggregationAccess().getCommaKeyword_4()); 
            match(input,35,FOLLOW_2); 
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
    // InternalStreamingsparql.g:1913:1: rule__Aggregation__Group__5 : rule__Aggregation__Group__5__Impl rule__Aggregation__Group__6 ;
    public final void rule__Aggregation__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1917:1: ( rule__Aggregation__Group__5__Impl rule__Aggregation__Group__6 )
            // InternalStreamingsparql.g:1918:2: rule__Aggregation__Group__5__Impl rule__Aggregation__Group__6
            {
            pushFollow(FOLLOW_20);
            rule__Aggregation__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1925:1: rule__Aggregation__Group__5__Impl : ( ( rule__Aggregation__AggNameAssignment_5 ) ) ;
    public final void rule__Aggregation__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1929:1: ( ( ( rule__Aggregation__AggNameAssignment_5 ) ) )
            // InternalStreamingsparql.g:1930:1: ( ( rule__Aggregation__AggNameAssignment_5 ) )
            {
            // InternalStreamingsparql.g:1930:1: ( ( rule__Aggregation__AggNameAssignment_5 ) )
            // InternalStreamingsparql.g:1931:2: ( rule__Aggregation__AggNameAssignment_5 )
            {
             before(grammarAccess.getAggregationAccess().getAggNameAssignment_5()); 
            // InternalStreamingsparql.g:1932:2: ( rule__Aggregation__AggNameAssignment_5 )
            // InternalStreamingsparql.g:1932:3: rule__Aggregation__AggNameAssignment_5
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1940:1: rule__Aggregation__Group__6 : rule__Aggregation__Group__6__Impl rule__Aggregation__Group__7 ;
    public final void rule__Aggregation__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1944:1: ( rule__Aggregation__Group__6__Impl rule__Aggregation__Group__7 )
            // InternalStreamingsparql.g:1945:2: rule__Aggregation__Group__6__Impl rule__Aggregation__Group__7
            {
            pushFollow(FOLLOW_20);
            rule__Aggregation__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1952:1: rule__Aggregation__Group__6__Impl : ( ( rule__Aggregation__Group_6__0 )? ) ;
    public final void rule__Aggregation__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1956:1: ( ( ( rule__Aggregation__Group_6__0 )? ) )
            // InternalStreamingsparql.g:1957:1: ( ( rule__Aggregation__Group_6__0 )? )
            {
            // InternalStreamingsparql.g:1957:1: ( ( rule__Aggregation__Group_6__0 )? )
            // InternalStreamingsparql.g:1958:2: ( rule__Aggregation__Group_6__0 )?
            {
             before(grammarAccess.getAggregationAccess().getGroup_6()); 
            // InternalStreamingsparql.g:1959:2: ( rule__Aggregation__Group_6__0 )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==35) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalStreamingsparql.g:1959:3: rule__Aggregation__Group_6__0
                    {
                    pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1967:1: rule__Aggregation__Group__7 : rule__Aggregation__Group__7__Impl rule__Aggregation__Group__8 ;
    public final void rule__Aggregation__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1971:1: ( rule__Aggregation__Group__7__Impl rule__Aggregation__Group__8 )
            // InternalStreamingsparql.g:1972:2: rule__Aggregation__Group__7__Impl rule__Aggregation__Group__8
            {
            pushFollow(FOLLOW_23);
            rule__Aggregation__Group__7__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:1979:1: rule__Aggregation__Group__7__Impl : ( ']' ) ;
    public final void rule__Aggregation__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1983:1: ( ( ']' ) )
            // InternalStreamingsparql.g:1984:1: ( ']' )
            {
            // InternalStreamingsparql.g:1984:1: ( ']' )
            // InternalStreamingsparql.g:1985:2: ']'
            {
             before(grammarAccess.getAggregationAccess().getRightSquareBracketKeyword_7()); 
            match(input,34,FOLLOW_2); 
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
    // InternalStreamingsparql.g:1994:1: rule__Aggregation__Group__8 : rule__Aggregation__Group__8__Impl ;
    public final void rule__Aggregation__Group__8() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:1998:1: ( rule__Aggregation__Group__8__Impl )
            // InternalStreamingsparql.g:1999:2: rule__Aggregation__Group__8__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2005:1: rule__Aggregation__Group__8__Impl : ( ( ',' )? ) ;
    public final void rule__Aggregation__Group__8__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2009:1: ( ( ( ',' )? ) )
            // InternalStreamingsparql.g:2010:1: ( ( ',' )? )
            {
            // InternalStreamingsparql.g:2010:1: ( ( ',' )? )
            // InternalStreamingsparql.g:2011:2: ( ',' )?
            {
             before(grammarAccess.getAggregationAccess().getCommaKeyword_8()); 
            // InternalStreamingsparql.g:2012:2: ( ',' )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==35) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalStreamingsparql.g:2012:3: ','
                    {
                    match(input,35,FOLLOW_2); 

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
    // InternalStreamingsparql.g:2021:1: rule__Aggregation__Group_6__0 : rule__Aggregation__Group_6__0__Impl rule__Aggregation__Group_6__1 ;
    public final void rule__Aggregation__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2025:1: ( rule__Aggregation__Group_6__0__Impl rule__Aggregation__Group_6__1 )
            // InternalStreamingsparql.g:2026:2: rule__Aggregation__Group_6__0__Impl rule__Aggregation__Group_6__1
            {
            pushFollow(FOLLOW_24);
            rule__Aggregation__Group_6__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2033:1: rule__Aggregation__Group_6__0__Impl : ( ',' ) ;
    public final void rule__Aggregation__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2037:1: ( ( ',' ) )
            // InternalStreamingsparql.g:2038:1: ( ',' )
            {
            // InternalStreamingsparql.g:2038:1: ( ',' )
            // InternalStreamingsparql.g:2039:2: ','
            {
             before(grammarAccess.getAggregationAccess().getCommaKeyword_6_0()); 
            match(input,35,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2048:1: rule__Aggregation__Group_6__1 : rule__Aggregation__Group_6__1__Impl ;
    public final void rule__Aggregation__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2052:1: ( rule__Aggregation__Group_6__1__Impl )
            // InternalStreamingsparql.g:2053:2: rule__Aggregation__Group_6__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2059:1: rule__Aggregation__Group_6__1__Impl : ( ( rule__Aggregation__DatatypeAssignment_6_1 ) ) ;
    public final void rule__Aggregation__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2063:1: ( ( ( rule__Aggregation__DatatypeAssignment_6_1 ) ) )
            // InternalStreamingsparql.g:2064:1: ( ( rule__Aggregation__DatatypeAssignment_6_1 ) )
            {
            // InternalStreamingsparql.g:2064:1: ( ( rule__Aggregation__DatatypeAssignment_6_1 ) )
            // InternalStreamingsparql.g:2065:2: ( rule__Aggregation__DatatypeAssignment_6_1 )
            {
             before(grammarAccess.getAggregationAccess().getDatatypeAssignment_6_1()); 
            // InternalStreamingsparql.g:2066:2: ( rule__Aggregation__DatatypeAssignment_6_1 )
            // InternalStreamingsparql.g:2066:3: rule__Aggregation__DatatypeAssignment_6_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2075:1: rule__Filesinkclause__Group__0 : rule__Filesinkclause__Group__0__Impl rule__Filesinkclause__Group__1 ;
    public final void rule__Filesinkclause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2079:1: ( rule__Filesinkclause__Group__0__Impl rule__Filesinkclause__Group__1 )
            // InternalStreamingsparql.g:2080:2: rule__Filesinkclause__Group__0__Impl rule__Filesinkclause__Group__1
            {
            pushFollow(FOLLOW_24);
            rule__Filesinkclause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2087:1: rule__Filesinkclause__Group__0__Impl : ( 'CSVFILESINK(' ) ;
    public final void rule__Filesinkclause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2091:1: ( ( 'CSVFILESINK(' ) )
            // InternalStreamingsparql.g:2092:1: ( 'CSVFILESINK(' )
            {
            // InternalStreamingsparql.g:2092:1: ( 'CSVFILESINK(' )
            // InternalStreamingsparql.g:2093:2: 'CSVFILESINK('
            {
             before(grammarAccess.getFilesinkclauseAccess().getCSVFILESINKKeyword_0()); 
            match(input,37,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2102:1: rule__Filesinkclause__Group__1 : rule__Filesinkclause__Group__1__Impl rule__Filesinkclause__Group__2 ;
    public final void rule__Filesinkclause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2106:1: ( rule__Filesinkclause__Group__1__Impl rule__Filesinkclause__Group__2 )
            // InternalStreamingsparql.g:2107:2: rule__Filesinkclause__Group__1__Impl rule__Filesinkclause__Group__2
            {
            pushFollow(FOLLOW_25);
            rule__Filesinkclause__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2114:1: rule__Filesinkclause__Group__1__Impl : ( ( rule__Filesinkclause__PathAssignment_1 ) ) ;
    public final void rule__Filesinkclause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2118:1: ( ( ( rule__Filesinkclause__PathAssignment_1 ) ) )
            // InternalStreamingsparql.g:2119:1: ( ( rule__Filesinkclause__PathAssignment_1 ) )
            {
            // InternalStreamingsparql.g:2119:1: ( ( rule__Filesinkclause__PathAssignment_1 ) )
            // InternalStreamingsparql.g:2120:2: ( rule__Filesinkclause__PathAssignment_1 )
            {
             before(grammarAccess.getFilesinkclauseAccess().getPathAssignment_1()); 
            // InternalStreamingsparql.g:2121:2: ( rule__Filesinkclause__PathAssignment_1 )
            // InternalStreamingsparql.g:2121:3: rule__Filesinkclause__PathAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2129:1: rule__Filesinkclause__Group__2 : rule__Filesinkclause__Group__2__Impl ;
    public final void rule__Filesinkclause__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2133:1: ( rule__Filesinkclause__Group__2__Impl )
            // InternalStreamingsparql.g:2134:2: rule__Filesinkclause__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2140:1: rule__Filesinkclause__Group__2__Impl : ( ')' ) ;
    public final void rule__Filesinkclause__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2144:1: ( ( ')' ) )
            // InternalStreamingsparql.g:2145:1: ( ')' )
            {
            // InternalStreamingsparql.g:2145:1: ( ')' )
            // InternalStreamingsparql.g:2146:2: ')'
            {
             before(grammarAccess.getFilesinkclauseAccess().getRightParenthesisKeyword_2()); 
            match(input,31,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2156:1: rule__Filterclause__Group__0 : rule__Filterclause__Group__0__Impl rule__Filterclause__Group__1 ;
    public final void rule__Filterclause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2160:1: ( rule__Filterclause__Group__0__Impl rule__Filterclause__Group__1 )
            // InternalStreamingsparql.g:2161:2: rule__Filterclause__Group__0__Impl rule__Filterclause__Group__1
            {
            pushFollow(FOLLOW_9);
            rule__Filterclause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2168:1: rule__Filterclause__Group__0__Impl : ( 'FILTER(' ) ;
    public final void rule__Filterclause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2172:1: ( ( 'FILTER(' ) )
            // InternalStreamingsparql.g:2173:1: ( 'FILTER(' )
            {
            // InternalStreamingsparql.g:2173:1: ( 'FILTER(' )
            // InternalStreamingsparql.g:2174:2: 'FILTER('
            {
             before(grammarAccess.getFilterclauseAccess().getFILTERKeyword_0()); 
            match(input,38,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2183:1: rule__Filterclause__Group__1 : rule__Filterclause__Group__1__Impl rule__Filterclause__Group__2 ;
    public final void rule__Filterclause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2187:1: ( rule__Filterclause__Group__1__Impl rule__Filterclause__Group__2 )
            // InternalStreamingsparql.g:2188:2: rule__Filterclause__Group__1__Impl rule__Filterclause__Group__2
            {
            pushFollow(FOLLOW_26);
            rule__Filterclause__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2195:1: rule__Filterclause__Group__1__Impl : ( ( rule__Filterclause__LeftAssignment_1 ) ) ;
    public final void rule__Filterclause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2199:1: ( ( ( rule__Filterclause__LeftAssignment_1 ) ) )
            // InternalStreamingsparql.g:2200:1: ( ( rule__Filterclause__LeftAssignment_1 ) )
            {
            // InternalStreamingsparql.g:2200:1: ( ( rule__Filterclause__LeftAssignment_1 ) )
            // InternalStreamingsparql.g:2201:2: ( rule__Filterclause__LeftAssignment_1 )
            {
             before(grammarAccess.getFilterclauseAccess().getLeftAssignment_1()); 
            // InternalStreamingsparql.g:2202:2: ( rule__Filterclause__LeftAssignment_1 )
            // InternalStreamingsparql.g:2202:3: rule__Filterclause__LeftAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2210:1: rule__Filterclause__Group__2 : rule__Filterclause__Group__2__Impl rule__Filterclause__Group__3 ;
    public final void rule__Filterclause__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2214:1: ( rule__Filterclause__Group__2__Impl rule__Filterclause__Group__3 )
            // InternalStreamingsparql.g:2215:2: rule__Filterclause__Group__2__Impl rule__Filterclause__Group__3
            {
            pushFollow(FOLLOW_9);
            rule__Filterclause__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2222:1: rule__Filterclause__Group__2__Impl : ( ( rule__Filterclause__OperatorAssignment_2 ) ) ;
    public final void rule__Filterclause__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2226:1: ( ( ( rule__Filterclause__OperatorAssignment_2 ) ) )
            // InternalStreamingsparql.g:2227:1: ( ( rule__Filterclause__OperatorAssignment_2 ) )
            {
            // InternalStreamingsparql.g:2227:1: ( ( rule__Filterclause__OperatorAssignment_2 ) )
            // InternalStreamingsparql.g:2228:2: ( rule__Filterclause__OperatorAssignment_2 )
            {
             before(grammarAccess.getFilterclauseAccess().getOperatorAssignment_2()); 
            // InternalStreamingsparql.g:2229:2: ( rule__Filterclause__OperatorAssignment_2 )
            // InternalStreamingsparql.g:2229:3: rule__Filterclause__OperatorAssignment_2
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2237:1: rule__Filterclause__Group__3 : rule__Filterclause__Group__3__Impl rule__Filterclause__Group__4 ;
    public final void rule__Filterclause__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2241:1: ( rule__Filterclause__Group__3__Impl rule__Filterclause__Group__4 )
            // InternalStreamingsparql.g:2242:2: rule__Filterclause__Group__3__Impl rule__Filterclause__Group__4
            {
            pushFollow(FOLLOW_25);
            rule__Filterclause__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2249:1: rule__Filterclause__Group__3__Impl : ( ( rule__Filterclause__RightAssignment_3 ) ) ;
    public final void rule__Filterclause__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2253:1: ( ( ( rule__Filterclause__RightAssignment_3 ) ) )
            // InternalStreamingsparql.g:2254:1: ( ( rule__Filterclause__RightAssignment_3 ) )
            {
            // InternalStreamingsparql.g:2254:1: ( ( rule__Filterclause__RightAssignment_3 ) )
            // InternalStreamingsparql.g:2255:2: ( rule__Filterclause__RightAssignment_3 )
            {
             before(grammarAccess.getFilterclauseAccess().getRightAssignment_3()); 
            // InternalStreamingsparql.g:2256:2: ( rule__Filterclause__RightAssignment_3 )
            // InternalStreamingsparql.g:2256:3: rule__Filterclause__RightAssignment_3
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2264:1: rule__Filterclause__Group__4 : rule__Filterclause__Group__4__Impl ;
    public final void rule__Filterclause__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2268:1: ( rule__Filterclause__Group__4__Impl )
            // InternalStreamingsparql.g:2269:2: rule__Filterclause__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2275:1: rule__Filterclause__Group__4__Impl : ( ')' ) ;
    public final void rule__Filterclause__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2279:1: ( ( ')' ) )
            // InternalStreamingsparql.g:2280:1: ( ')' )
            {
            // InternalStreamingsparql.g:2280:1: ( ')' )
            // InternalStreamingsparql.g:2281:2: ')'
            {
             before(grammarAccess.getFilterclauseAccess().getRightParenthesisKeyword_4()); 
            match(input,31,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2291:1: rule__DatasetClause__Group__0 : rule__DatasetClause__Group__0__Impl rule__DatasetClause__Group__1 ;
    public final void rule__DatasetClause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2295:1: ( rule__DatasetClause__Group__0__Impl rule__DatasetClause__Group__1 )
            // InternalStreamingsparql.g:2296:2: rule__DatasetClause__Group__0__Impl rule__DatasetClause__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__DatasetClause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2303:1: rule__DatasetClause__Group__0__Impl : ( 'FROM' ) ;
    public final void rule__DatasetClause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2307:1: ( ( 'FROM' ) )
            // InternalStreamingsparql.g:2308:1: ( 'FROM' )
            {
            // InternalStreamingsparql.g:2308:1: ( 'FROM' )
            // InternalStreamingsparql.g:2309:2: 'FROM'
            {
             before(grammarAccess.getDatasetClauseAccess().getFROMKeyword_0()); 
            match(input,39,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2318:1: rule__DatasetClause__Group__1 : rule__DatasetClause__Group__1__Impl rule__DatasetClause__Group__2 ;
    public final void rule__DatasetClause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2322:1: ( rule__DatasetClause__Group__1__Impl rule__DatasetClause__Group__2 )
            // InternalStreamingsparql.g:2323:2: rule__DatasetClause__Group__1__Impl rule__DatasetClause__Group__2
            {
            pushFollow(FOLLOW_27);
            rule__DatasetClause__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2330:1: rule__DatasetClause__Group__1__Impl : ( ( rule__DatasetClause__DataSetAssignment_1 ) ) ;
    public final void rule__DatasetClause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2334:1: ( ( ( rule__DatasetClause__DataSetAssignment_1 ) ) )
            // InternalStreamingsparql.g:2335:1: ( ( rule__DatasetClause__DataSetAssignment_1 ) )
            {
            // InternalStreamingsparql.g:2335:1: ( ( rule__DatasetClause__DataSetAssignment_1 ) )
            // InternalStreamingsparql.g:2336:2: ( rule__DatasetClause__DataSetAssignment_1 )
            {
             before(grammarAccess.getDatasetClauseAccess().getDataSetAssignment_1()); 
            // InternalStreamingsparql.g:2337:2: ( rule__DatasetClause__DataSetAssignment_1 )
            // InternalStreamingsparql.g:2337:3: rule__DatasetClause__DataSetAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2345:1: rule__DatasetClause__Group__2 : rule__DatasetClause__Group__2__Impl rule__DatasetClause__Group__3 ;
    public final void rule__DatasetClause__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2349:1: ( rule__DatasetClause__Group__2__Impl rule__DatasetClause__Group__3 )
            // InternalStreamingsparql.g:2350:2: rule__DatasetClause__Group__2__Impl rule__DatasetClause__Group__3
            {
            pushFollow(FOLLOW_3);
            rule__DatasetClause__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2357:1: rule__DatasetClause__Group__2__Impl : ( 'AS' ) ;
    public final void rule__DatasetClause__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2361:1: ( ( 'AS' ) )
            // InternalStreamingsparql.g:2362:1: ( 'AS' )
            {
            // InternalStreamingsparql.g:2362:1: ( 'AS' )
            // InternalStreamingsparql.g:2363:2: 'AS'
            {
             before(grammarAccess.getDatasetClauseAccess().getASKeyword_2()); 
            match(input,40,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2372:1: rule__DatasetClause__Group__3 : rule__DatasetClause__Group__3__Impl rule__DatasetClause__Group__4 ;
    public final void rule__DatasetClause__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2376:1: ( rule__DatasetClause__Group__3__Impl rule__DatasetClause__Group__4 )
            // InternalStreamingsparql.g:2377:2: rule__DatasetClause__Group__3__Impl rule__DatasetClause__Group__4
            {
            pushFollow(FOLLOW_16);
            rule__DatasetClause__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2384:1: rule__DatasetClause__Group__3__Impl : ( ( rule__DatasetClause__NameAssignment_3 ) ) ;
    public final void rule__DatasetClause__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2388:1: ( ( ( rule__DatasetClause__NameAssignment_3 ) ) )
            // InternalStreamingsparql.g:2389:1: ( ( rule__DatasetClause__NameAssignment_3 ) )
            {
            // InternalStreamingsparql.g:2389:1: ( ( rule__DatasetClause__NameAssignment_3 ) )
            // InternalStreamingsparql.g:2390:2: ( rule__DatasetClause__NameAssignment_3 )
            {
             before(grammarAccess.getDatasetClauseAccess().getNameAssignment_3()); 
            // InternalStreamingsparql.g:2391:2: ( rule__DatasetClause__NameAssignment_3 )
            // InternalStreamingsparql.g:2391:3: rule__DatasetClause__NameAssignment_3
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2399:1: rule__DatasetClause__Group__4 : rule__DatasetClause__Group__4__Impl ;
    public final void rule__DatasetClause__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2403:1: ( rule__DatasetClause__Group__4__Impl )
            // InternalStreamingsparql.g:2404:2: rule__DatasetClause__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2410:1: rule__DatasetClause__Group__4__Impl : ( ( rule__DatasetClause__Group_4__0 )? ) ;
    public final void rule__DatasetClause__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2414:1: ( ( ( rule__DatasetClause__Group_4__0 )? ) )
            // InternalStreamingsparql.g:2415:1: ( ( rule__DatasetClause__Group_4__0 )? )
            {
            // InternalStreamingsparql.g:2415:1: ( ( rule__DatasetClause__Group_4__0 )? )
            // InternalStreamingsparql.g:2416:2: ( rule__DatasetClause__Group_4__0 )?
            {
             before(grammarAccess.getDatasetClauseAccess().getGroup_4()); 
            // InternalStreamingsparql.g:2417:2: ( rule__DatasetClause__Group_4__0 )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==33) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalStreamingsparql.g:2417:3: rule__DatasetClause__Group_4__0
                    {
                    pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2426:1: rule__DatasetClause__Group_4__0 : rule__DatasetClause__Group_4__0__Impl rule__DatasetClause__Group_4__1 ;
    public final void rule__DatasetClause__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2430:1: ( rule__DatasetClause__Group_4__0__Impl rule__DatasetClause__Group_4__1 )
            // InternalStreamingsparql.g:2431:2: rule__DatasetClause__Group_4__0__Impl rule__DatasetClause__Group_4__1
            {
            pushFollow(FOLLOW_28);
            rule__DatasetClause__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2438:1: rule__DatasetClause__Group_4__0__Impl : ( '[' ) ;
    public final void rule__DatasetClause__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2442:1: ( ( '[' ) )
            // InternalStreamingsparql.g:2443:1: ( '[' )
            {
            // InternalStreamingsparql.g:2443:1: ( '[' )
            // InternalStreamingsparql.g:2444:2: '['
            {
             before(grammarAccess.getDatasetClauseAccess().getLeftSquareBracketKeyword_4_0()); 
            match(input,33,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getLeftSquareBracketKeyword_4_0()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:2453:1: rule__DatasetClause__Group_4__1 : rule__DatasetClause__Group_4__1__Impl rule__DatasetClause__Group_4__2 ;
    public final void rule__DatasetClause__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2457:1: ( rule__DatasetClause__Group_4__1__Impl rule__DatasetClause__Group_4__2 )
            // InternalStreamingsparql.g:2458:2: rule__DatasetClause__Group_4__1__Impl rule__DatasetClause__Group_4__2
            {
            pushFollow(FOLLOW_29);
            rule__DatasetClause__Group_4__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2465:1: rule__DatasetClause__Group_4__1__Impl : ( 'TYPE' ) ;
    public final void rule__DatasetClause__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2469:1: ( ( 'TYPE' ) )
            // InternalStreamingsparql.g:2470:1: ( 'TYPE' )
            {
            // InternalStreamingsparql.g:2470:1: ( 'TYPE' )
            // InternalStreamingsparql.g:2471:2: 'TYPE'
            {
             before(grammarAccess.getDatasetClauseAccess().getTYPEKeyword_4_1()); 
            match(input,41,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getTYPEKeyword_4_1()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:2480:1: rule__DatasetClause__Group_4__2 : rule__DatasetClause__Group_4__2__Impl rule__DatasetClause__Group_4__3 ;
    public final void rule__DatasetClause__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2484:1: ( rule__DatasetClause__Group_4__2__Impl rule__DatasetClause__Group_4__3 )
            // InternalStreamingsparql.g:2485:2: rule__DatasetClause__Group_4__2__Impl rule__DatasetClause__Group_4__3
            {
            pushFollow(FOLLOW_30);
            rule__DatasetClause__Group_4__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2492:1: rule__DatasetClause__Group_4__2__Impl : ( ( rule__DatasetClause__TypeAssignment_4_2 ) ) ;
    public final void rule__DatasetClause__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2496:1: ( ( ( rule__DatasetClause__TypeAssignment_4_2 ) ) )
            // InternalStreamingsparql.g:2497:1: ( ( rule__DatasetClause__TypeAssignment_4_2 ) )
            {
            // InternalStreamingsparql.g:2497:1: ( ( rule__DatasetClause__TypeAssignment_4_2 ) )
            // InternalStreamingsparql.g:2498:2: ( rule__DatasetClause__TypeAssignment_4_2 )
            {
             before(grammarAccess.getDatasetClauseAccess().getTypeAssignment_4_2()); 
            // InternalStreamingsparql.g:2499:2: ( rule__DatasetClause__TypeAssignment_4_2 )
            // InternalStreamingsparql.g:2499:3: rule__DatasetClause__TypeAssignment_4_2
            {
            pushFollow(FOLLOW_2);
            rule__DatasetClause__TypeAssignment_4_2();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getTypeAssignment_4_2()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:2507:1: rule__DatasetClause__Group_4__3 : rule__DatasetClause__Group_4__3__Impl rule__DatasetClause__Group_4__4 ;
    public final void rule__DatasetClause__Group_4__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2511:1: ( rule__DatasetClause__Group_4__3__Impl rule__DatasetClause__Group_4__4 )
            // InternalStreamingsparql.g:2512:2: rule__DatasetClause__Group_4__3__Impl rule__DatasetClause__Group_4__4
            {
            pushFollow(FOLLOW_31);
            rule__DatasetClause__Group_4__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2519:1: rule__DatasetClause__Group_4__3__Impl : ( 'SIZE' ) ;
    public final void rule__DatasetClause__Group_4__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2523:1: ( ( 'SIZE' ) )
            // InternalStreamingsparql.g:2524:1: ( 'SIZE' )
            {
            // InternalStreamingsparql.g:2524:1: ( 'SIZE' )
            // InternalStreamingsparql.g:2525:2: 'SIZE'
            {
             before(grammarAccess.getDatasetClauseAccess().getSIZEKeyword_4_3()); 
            match(input,42,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getSIZEKeyword_4_3()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:2534:1: rule__DatasetClause__Group_4__4 : rule__DatasetClause__Group_4__4__Impl rule__DatasetClause__Group_4__5 ;
    public final void rule__DatasetClause__Group_4__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2538:1: ( rule__DatasetClause__Group_4__4__Impl rule__DatasetClause__Group_4__5 )
            // InternalStreamingsparql.g:2539:2: rule__DatasetClause__Group_4__4__Impl rule__DatasetClause__Group_4__5
            {
            pushFollow(FOLLOW_32);
            rule__DatasetClause__Group_4__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2546:1: rule__DatasetClause__Group_4__4__Impl : ( ( rule__DatasetClause__SizeAssignment_4_4 ) ) ;
    public final void rule__DatasetClause__Group_4__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2550:1: ( ( ( rule__DatasetClause__SizeAssignment_4_4 ) ) )
            // InternalStreamingsparql.g:2551:1: ( ( rule__DatasetClause__SizeAssignment_4_4 ) )
            {
            // InternalStreamingsparql.g:2551:1: ( ( rule__DatasetClause__SizeAssignment_4_4 ) )
            // InternalStreamingsparql.g:2552:2: ( rule__DatasetClause__SizeAssignment_4_4 )
            {
             before(grammarAccess.getDatasetClauseAccess().getSizeAssignment_4_4()); 
            // InternalStreamingsparql.g:2553:2: ( rule__DatasetClause__SizeAssignment_4_4 )
            // InternalStreamingsparql.g:2553:3: rule__DatasetClause__SizeAssignment_4_4
            {
            pushFollow(FOLLOW_2);
            rule__DatasetClause__SizeAssignment_4_4();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getSizeAssignment_4_4()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:2561:1: rule__DatasetClause__Group_4__5 : rule__DatasetClause__Group_4__5__Impl rule__DatasetClause__Group_4__6 ;
    public final void rule__DatasetClause__Group_4__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2565:1: ( rule__DatasetClause__Group_4__5__Impl rule__DatasetClause__Group_4__6 )
            // InternalStreamingsparql.g:2566:2: rule__DatasetClause__Group_4__5__Impl rule__DatasetClause__Group_4__6
            {
            pushFollow(FOLLOW_32);
            rule__DatasetClause__Group_4__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2573:1: rule__DatasetClause__Group_4__5__Impl : ( ( rule__DatasetClause__Group_4_5__0 )? ) ;
    public final void rule__DatasetClause__Group_4__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2577:1: ( ( ( rule__DatasetClause__Group_4_5__0 )? ) )
            // InternalStreamingsparql.g:2578:1: ( ( rule__DatasetClause__Group_4_5__0 )? )
            {
            // InternalStreamingsparql.g:2578:1: ( ( rule__DatasetClause__Group_4_5__0 )? )
            // InternalStreamingsparql.g:2579:2: ( rule__DatasetClause__Group_4_5__0 )?
            {
             before(grammarAccess.getDatasetClauseAccess().getGroup_4_5()); 
            // InternalStreamingsparql.g:2580:2: ( rule__DatasetClause__Group_4_5__0 )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==43) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalStreamingsparql.g:2580:3: rule__DatasetClause__Group_4_5__0
                    {
                    pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2588:1: rule__DatasetClause__Group_4__6 : rule__DatasetClause__Group_4__6__Impl rule__DatasetClause__Group_4__7 ;
    public final void rule__DatasetClause__Group_4__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2592:1: ( rule__DatasetClause__Group_4__6__Impl rule__DatasetClause__Group_4__7 )
            // InternalStreamingsparql.g:2593:2: rule__DatasetClause__Group_4__6__Impl rule__DatasetClause__Group_4__7
            {
            pushFollow(FOLLOW_32);
            rule__DatasetClause__Group_4__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DatasetClause__Group_4__7();

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
    // InternalStreamingsparql.g:2600:1: rule__DatasetClause__Group_4__6__Impl : ( ( rule__DatasetClause__Group_4_6__0 )? ) ;
    public final void rule__DatasetClause__Group_4__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2604:1: ( ( ( rule__DatasetClause__Group_4_6__0 )? ) )
            // InternalStreamingsparql.g:2605:1: ( ( rule__DatasetClause__Group_4_6__0 )? )
            {
            // InternalStreamingsparql.g:2605:1: ( ( rule__DatasetClause__Group_4_6__0 )? )
            // InternalStreamingsparql.g:2606:2: ( rule__DatasetClause__Group_4_6__0 )?
            {
             before(grammarAccess.getDatasetClauseAccess().getGroup_4_6()); 
            // InternalStreamingsparql.g:2607:2: ( rule__DatasetClause__Group_4_6__0 )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==44) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalStreamingsparql.g:2607:3: rule__DatasetClause__Group_4_6__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__DatasetClause__Group_4_6__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getDatasetClauseAccess().getGroup_4_6()); 

            }


            }

        }
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


    // $ANTLR start "rule__DatasetClause__Group_4__7"
    // InternalStreamingsparql.g:2615:1: rule__DatasetClause__Group_4__7 : rule__DatasetClause__Group_4__7__Impl ;
    public final void rule__DatasetClause__Group_4__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2619:1: ( rule__DatasetClause__Group_4__7__Impl )
            // InternalStreamingsparql.g:2620:2: rule__DatasetClause__Group_4__7__Impl
            {
            pushFollow(FOLLOW_2);
            rule__DatasetClause__Group_4__7__Impl();

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
    // $ANTLR end "rule__DatasetClause__Group_4__7"


    // $ANTLR start "rule__DatasetClause__Group_4__7__Impl"
    // InternalStreamingsparql.g:2626:1: rule__DatasetClause__Group_4__7__Impl : ( ']' ) ;
    public final void rule__DatasetClause__Group_4__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2630:1: ( ( ']' ) )
            // InternalStreamingsparql.g:2631:1: ( ']' )
            {
            // InternalStreamingsparql.g:2631:1: ( ']' )
            // InternalStreamingsparql.g:2632:2: ']'
            {
             before(grammarAccess.getDatasetClauseAccess().getRightSquareBracketKeyword_4_7()); 
            match(input,34,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getRightSquareBracketKeyword_4_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DatasetClause__Group_4__7__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4_5__0"
    // InternalStreamingsparql.g:2642:1: rule__DatasetClause__Group_4_5__0 : rule__DatasetClause__Group_4_5__0__Impl rule__DatasetClause__Group_4_5__1 ;
    public final void rule__DatasetClause__Group_4_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2646:1: ( rule__DatasetClause__Group_4_5__0__Impl rule__DatasetClause__Group_4_5__1 )
            // InternalStreamingsparql.g:2647:2: rule__DatasetClause__Group_4_5__0__Impl rule__DatasetClause__Group_4_5__1
            {
            pushFollow(FOLLOW_31);
            rule__DatasetClause__Group_4_5__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2654:1: rule__DatasetClause__Group_4_5__0__Impl : ( 'ADVANCE' ) ;
    public final void rule__DatasetClause__Group_4_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2658:1: ( ( 'ADVANCE' ) )
            // InternalStreamingsparql.g:2659:1: ( 'ADVANCE' )
            {
            // InternalStreamingsparql.g:2659:1: ( 'ADVANCE' )
            // InternalStreamingsparql.g:2660:2: 'ADVANCE'
            {
             before(grammarAccess.getDatasetClauseAccess().getADVANCEKeyword_4_5_0()); 
            match(input,43,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getADVANCEKeyword_4_5_0()); 

            }


            }

        }
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
    // InternalStreamingsparql.g:2669:1: rule__DatasetClause__Group_4_5__1 : rule__DatasetClause__Group_4_5__1__Impl ;
    public final void rule__DatasetClause__Group_4_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2673:1: ( rule__DatasetClause__Group_4_5__1__Impl )
            // InternalStreamingsparql.g:2674:2: rule__DatasetClause__Group_4_5__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2680:1: rule__DatasetClause__Group_4_5__1__Impl : ( ( rule__DatasetClause__AdvanceAssignment_4_5_1 ) ) ;
    public final void rule__DatasetClause__Group_4_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2684:1: ( ( ( rule__DatasetClause__AdvanceAssignment_4_5_1 ) ) )
            // InternalStreamingsparql.g:2685:1: ( ( rule__DatasetClause__AdvanceAssignment_4_5_1 ) )
            {
            // InternalStreamingsparql.g:2685:1: ( ( rule__DatasetClause__AdvanceAssignment_4_5_1 ) )
            // InternalStreamingsparql.g:2686:2: ( rule__DatasetClause__AdvanceAssignment_4_5_1 )
            {
             before(grammarAccess.getDatasetClauseAccess().getAdvanceAssignment_4_5_1()); 
            // InternalStreamingsparql.g:2687:2: ( rule__DatasetClause__AdvanceAssignment_4_5_1 )
            // InternalStreamingsparql.g:2687:3: rule__DatasetClause__AdvanceAssignment_4_5_1
            {
            pushFollow(FOLLOW_2);
            rule__DatasetClause__AdvanceAssignment_4_5_1();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getAdvanceAssignment_4_5_1()); 

            }


            }

        }
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


    // $ANTLR start "rule__DatasetClause__Group_4_6__0"
    // InternalStreamingsparql.g:2696:1: rule__DatasetClause__Group_4_6__0 : rule__DatasetClause__Group_4_6__0__Impl rule__DatasetClause__Group_4_6__1 ;
    public final void rule__DatasetClause__Group_4_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2700:1: ( rule__DatasetClause__Group_4_6__0__Impl rule__DatasetClause__Group_4_6__1 )
            // InternalStreamingsparql.g:2701:2: rule__DatasetClause__Group_4_6__0__Impl rule__DatasetClause__Group_4_6__1
            {
            pushFollow(FOLLOW_33);
            rule__DatasetClause__Group_4_6__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DatasetClause__Group_4_6__1();

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
    // $ANTLR end "rule__DatasetClause__Group_4_6__0"


    // $ANTLR start "rule__DatasetClause__Group_4_6__0__Impl"
    // InternalStreamingsparql.g:2708:1: rule__DatasetClause__Group_4_6__0__Impl : ( 'UNIT' ) ;
    public final void rule__DatasetClause__Group_4_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2712:1: ( ( 'UNIT' ) )
            // InternalStreamingsparql.g:2713:1: ( 'UNIT' )
            {
            // InternalStreamingsparql.g:2713:1: ( 'UNIT' )
            // InternalStreamingsparql.g:2714:2: 'UNIT'
            {
             before(grammarAccess.getDatasetClauseAccess().getUNITKeyword_4_6_0()); 
            match(input,44,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getUNITKeyword_4_6_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DatasetClause__Group_4_6__0__Impl"


    // $ANTLR start "rule__DatasetClause__Group_4_6__1"
    // InternalStreamingsparql.g:2723:1: rule__DatasetClause__Group_4_6__1 : rule__DatasetClause__Group_4_6__1__Impl ;
    public final void rule__DatasetClause__Group_4_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2727:1: ( rule__DatasetClause__Group_4_6__1__Impl )
            // InternalStreamingsparql.g:2728:2: rule__DatasetClause__Group_4_6__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__DatasetClause__Group_4_6__1__Impl();

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
    // $ANTLR end "rule__DatasetClause__Group_4_6__1"


    // $ANTLR start "rule__DatasetClause__Group_4_6__1__Impl"
    // InternalStreamingsparql.g:2734:1: rule__DatasetClause__Group_4_6__1__Impl : ( ( rule__DatasetClause__UnitAssignment_4_6_1 ) ) ;
    public final void rule__DatasetClause__Group_4_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2738:1: ( ( ( rule__DatasetClause__UnitAssignment_4_6_1 ) ) )
            // InternalStreamingsparql.g:2739:1: ( ( rule__DatasetClause__UnitAssignment_4_6_1 ) )
            {
            // InternalStreamingsparql.g:2739:1: ( ( rule__DatasetClause__UnitAssignment_4_6_1 ) )
            // InternalStreamingsparql.g:2740:2: ( rule__DatasetClause__UnitAssignment_4_6_1 )
            {
             before(grammarAccess.getDatasetClauseAccess().getUnitAssignment_4_6_1()); 
            // InternalStreamingsparql.g:2741:2: ( rule__DatasetClause__UnitAssignment_4_6_1 )
            // InternalStreamingsparql.g:2741:3: rule__DatasetClause__UnitAssignment_4_6_1
            {
            pushFollow(FOLLOW_2);
            rule__DatasetClause__UnitAssignment_4_6_1();

            state._fsp--;


            }

             after(grammarAccess.getDatasetClauseAccess().getUnitAssignment_4_6_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DatasetClause__Group_4_6__1__Impl"


    // $ANTLR start "rule__WhereClause__Group__0"
    // InternalStreamingsparql.g:2750:1: rule__WhereClause__Group__0 : rule__WhereClause__Group__0__Impl rule__WhereClause__Group__1 ;
    public final void rule__WhereClause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2754:1: ( rule__WhereClause__Group__0__Impl rule__WhereClause__Group__1 )
            // InternalStreamingsparql.g:2755:2: rule__WhereClause__Group__0__Impl rule__WhereClause__Group__1
            {
            pushFollow(FOLLOW_34);
            rule__WhereClause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2762:1: rule__WhereClause__Group__0__Impl : ( 'WHERE' ) ;
    public final void rule__WhereClause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2766:1: ( ( 'WHERE' ) )
            // InternalStreamingsparql.g:2767:1: ( 'WHERE' )
            {
            // InternalStreamingsparql.g:2767:1: ( 'WHERE' )
            // InternalStreamingsparql.g:2768:2: 'WHERE'
            {
             before(grammarAccess.getWhereClauseAccess().getWHEREKeyword_0()); 
            match(input,45,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2777:1: rule__WhereClause__Group__1 : rule__WhereClause__Group__1__Impl rule__WhereClause__Group__2 ;
    public final void rule__WhereClause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2781:1: ( rule__WhereClause__Group__1__Impl rule__WhereClause__Group__2 )
            // InternalStreamingsparql.g:2782:2: rule__WhereClause__Group__1__Impl rule__WhereClause__Group__2
            {
            pushFollow(FOLLOW_3);
            rule__WhereClause__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2789:1: rule__WhereClause__Group__1__Impl : ( '{' ) ;
    public final void rule__WhereClause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2793:1: ( ( '{' ) )
            // InternalStreamingsparql.g:2794:1: ( '{' )
            {
            // InternalStreamingsparql.g:2794:1: ( '{' )
            // InternalStreamingsparql.g:2795:2: '{'
            {
             before(grammarAccess.getWhereClauseAccess().getLeftCurlyBracketKeyword_1()); 
            match(input,46,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2804:1: rule__WhereClause__Group__2 : rule__WhereClause__Group__2__Impl rule__WhereClause__Group__3 ;
    public final void rule__WhereClause__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2808:1: ( rule__WhereClause__Group__2__Impl rule__WhereClause__Group__3 )
            // InternalStreamingsparql.g:2809:2: rule__WhereClause__Group__2__Impl rule__WhereClause__Group__3
            {
            pushFollow(FOLLOW_35);
            rule__WhereClause__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2816:1: rule__WhereClause__Group__2__Impl : ( ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* ) ) ;
    public final void rule__WhereClause__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2820:1: ( ( ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* ) ) )
            // InternalStreamingsparql.g:2821:1: ( ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* ) )
            {
            // InternalStreamingsparql.g:2821:1: ( ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* ) )
            // InternalStreamingsparql.g:2822:2: ( ( rule__WhereClause__WhereclausesAssignment_2 ) ) ( ( rule__WhereClause__WhereclausesAssignment_2 )* )
            {
            // InternalStreamingsparql.g:2822:2: ( ( rule__WhereClause__WhereclausesAssignment_2 ) )
            // InternalStreamingsparql.g:2823:3: ( rule__WhereClause__WhereclausesAssignment_2 )
            {
             before(grammarAccess.getWhereClauseAccess().getWhereclausesAssignment_2()); 
            // InternalStreamingsparql.g:2824:3: ( rule__WhereClause__WhereclausesAssignment_2 )
            // InternalStreamingsparql.g:2824:4: rule__WhereClause__WhereclausesAssignment_2
            {
            pushFollow(FOLLOW_36);
            rule__WhereClause__WhereclausesAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getWhereClauseAccess().getWhereclausesAssignment_2()); 

            }

            // InternalStreamingsparql.g:2827:2: ( ( rule__WhereClause__WhereclausesAssignment_2 )* )
            // InternalStreamingsparql.g:2828:3: ( rule__WhereClause__WhereclausesAssignment_2 )*
            {
             before(grammarAccess.getWhereClauseAccess().getWhereclausesAssignment_2()); 
            // InternalStreamingsparql.g:2829:3: ( rule__WhereClause__WhereclausesAssignment_2 )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==RULE_ID) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // InternalStreamingsparql.g:2829:4: rule__WhereClause__WhereclausesAssignment_2
            	    {
            	    pushFollow(FOLLOW_36);
            	    rule__WhereClause__WhereclausesAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop24;
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
    // InternalStreamingsparql.g:2838:1: rule__WhereClause__Group__3 : rule__WhereClause__Group__3__Impl ;
    public final void rule__WhereClause__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2842:1: ( rule__WhereClause__Group__3__Impl )
            // InternalStreamingsparql.g:2843:2: rule__WhereClause__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2849:1: rule__WhereClause__Group__3__Impl : ( '}' ) ;
    public final void rule__WhereClause__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2853:1: ( ( '}' ) )
            // InternalStreamingsparql.g:2854:1: ( '}' )
            {
            // InternalStreamingsparql.g:2854:1: ( '}' )
            // InternalStreamingsparql.g:2855:2: '}'
            {
             before(grammarAccess.getWhereClauseAccess().getRightCurlyBracketKeyword_3()); 
            match(input,47,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2865:1: rule__InnerWhereClause__Group__0 : rule__InnerWhereClause__Group__0__Impl rule__InnerWhereClause__Group__1 ;
    public final void rule__InnerWhereClause__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2869:1: ( rule__InnerWhereClause__Group__0__Impl rule__InnerWhereClause__Group__1 )
            // InternalStreamingsparql.g:2870:2: rule__InnerWhereClause__Group__0__Impl rule__InnerWhereClause__Group__1
            {
            pushFollow(FOLLOW_34);
            rule__InnerWhereClause__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2877:1: rule__InnerWhereClause__Group__0__Impl : ( ( rule__InnerWhereClause__NameAssignment_0 ) ) ;
    public final void rule__InnerWhereClause__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2881:1: ( ( ( rule__InnerWhereClause__NameAssignment_0 ) ) )
            // InternalStreamingsparql.g:2882:1: ( ( rule__InnerWhereClause__NameAssignment_0 ) )
            {
            // InternalStreamingsparql.g:2882:1: ( ( rule__InnerWhereClause__NameAssignment_0 ) )
            // InternalStreamingsparql.g:2883:2: ( rule__InnerWhereClause__NameAssignment_0 )
            {
             before(grammarAccess.getInnerWhereClauseAccess().getNameAssignment_0()); 
            // InternalStreamingsparql.g:2884:2: ( rule__InnerWhereClause__NameAssignment_0 )
            // InternalStreamingsparql.g:2884:3: rule__InnerWhereClause__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2892:1: rule__InnerWhereClause__Group__1 : rule__InnerWhereClause__Group__1__Impl ;
    public final void rule__InnerWhereClause__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2896:1: ( rule__InnerWhereClause__Group__1__Impl )
            // InternalStreamingsparql.g:2897:2: rule__InnerWhereClause__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2903:1: rule__InnerWhereClause__Group__1__Impl : ( ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 ) ) ;
    public final void rule__InnerWhereClause__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2907:1: ( ( ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 ) ) )
            // InternalStreamingsparql.g:2908:1: ( ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 ) )
            {
            // InternalStreamingsparql.g:2908:1: ( ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 ) )
            // InternalStreamingsparql.g:2909:2: ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 )
            {
             before(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternAssignment_1()); 
            // InternalStreamingsparql.g:2910:2: ( rule__InnerWhereClause__GroupGraphPatternAssignment_1 )
            // InternalStreamingsparql.g:2910:3: rule__InnerWhereClause__GroupGraphPatternAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2919:1: rule__GroupGraphPatternSub__Group__0 : rule__GroupGraphPatternSub__Group__0__Impl rule__GroupGraphPatternSub__Group__1 ;
    public final void rule__GroupGraphPatternSub__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2923:1: ( rule__GroupGraphPatternSub__Group__0__Impl rule__GroupGraphPatternSub__Group__1 )
            // InternalStreamingsparql.g:2924:2: rule__GroupGraphPatternSub__Group__0__Impl rule__GroupGraphPatternSub__Group__1
            {
            pushFollow(FOLLOW_37);
            rule__GroupGraphPatternSub__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2931:1: rule__GroupGraphPatternSub__Group__0__Impl : ( '{' ) ;
    public final void rule__GroupGraphPatternSub__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2935:1: ( ( '{' ) )
            // InternalStreamingsparql.g:2936:1: ( '{' )
            {
            // InternalStreamingsparql.g:2936:1: ( '{' )
            // InternalStreamingsparql.g:2937:2: '{'
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getLeftCurlyBracketKeyword_0()); 
            match(input,46,FOLLOW_2); 
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
    // InternalStreamingsparql.g:2946:1: rule__GroupGraphPatternSub__Group__1 : rule__GroupGraphPatternSub__Group__1__Impl rule__GroupGraphPatternSub__Group__2 ;
    public final void rule__GroupGraphPatternSub__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2950:1: ( rule__GroupGraphPatternSub__Group__1__Impl rule__GroupGraphPatternSub__Group__2 )
            // InternalStreamingsparql.g:2951:2: rule__GroupGraphPatternSub__Group__1__Impl rule__GroupGraphPatternSub__Group__2
            {
            pushFollow(FOLLOW_38);
            rule__GroupGraphPatternSub__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2958:1: rule__GroupGraphPatternSub__Group__1__Impl : ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 ) ) ;
    public final void rule__GroupGraphPatternSub__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2962:1: ( ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 ) ) )
            // InternalStreamingsparql.g:2963:1: ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 ) )
            {
            // InternalStreamingsparql.g:2963:1: ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 ) )
            // InternalStreamingsparql.g:2964:2: ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 )
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsAssignment_1()); 
            // InternalStreamingsparql.g:2965:2: ( rule__GroupGraphPatternSub__GraphPatternsAssignment_1 )
            // InternalStreamingsparql.g:2965:3: rule__GroupGraphPatternSub__GraphPatternsAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2973:1: rule__GroupGraphPatternSub__Group__2 : rule__GroupGraphPatternSub__Group__2__Impl rule__GroupGraphPatternSub__Group__3 ;
    public final void rule__GroupGraphPatternSub__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2977:1: ( rule__GroupGraphPatternSub__Group__2__Impl rule__GroupGraphPatternSub__Group__3 )
            // InternalStreamingsparql.g:2978:2: rule__GroupGraphPatternSub__Group__2__Impl rule__GroupGraphPatternSub__Group__3
            {
            pushFollow(FOLLOW_38);
            rule__GroupGraphPatternSub__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:2985:1: rule__GroupGraphPatternSub__Group__2__Impl : ( ( rule__GroupGraphPatternSub__Group_2__0 )* ) ;
    public final void rule__GroupGraphPatternSub__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:2989:1: ( ( ( rule__GroupGraphPatternSub__Group_2__0 )* ) )
            // InternalStreamingsparql.g:2990:1: ( ( rule__GroupGraphPatternSub__Group_2__0 )* )
            {
            // InternalStreamingsparql.g:2990:1: ( ( rule__GroupGraphPatternSub__Group_2__0 )* )
            // InternalStreamingsparql.g:2991:2: ( rule__GroupGraphPatternSub__Group_2__0 )*
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGroup_2()); 
            // InternalStreamingsparql.g:2992:2: ( rule__GroupGraphPatternSub__Group_2__0 )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==48) ) {
                    int LA25_1 = input.LA(2);

                    if ( ((LA25_1>=RULE_ID && LA25_1<=RULE_IRI_TERMINAL)||LA25_1==RULE_STRING||LA25_1==50) ) {
                        alt25=1;
                    }


                }


                switch (alt25) {
            	case 1 :
            	    // InternalStreamingsparql.g:2992:3: rule__GroupGraphPatternSub__Group_2__0
            	    {
            	    pushFollow(FOLLOW_39);
            	    rule__GroupGraphPatternSub__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop25;
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
    // InternalStreamingsparql.g:3000:1: rule__GroupGraphPatternSub__Group__3 : rule__GroupGraphPatternSub__Group__3__Impl rule__GroupGraphPatternSub__Group__4 ;
    public final void rule__GroupGraphPatternSub__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3004:1: ( rule__GroupGraphPatternSub__Group__3__Impl rule__GroupGraphPatternSub__Group__4 )
            // InternalStreamingsparql.g:3005:2: rule__GroupGraphPatternSub__Group__3__Impl rule__GroupGraphPatternSub__Group__4
            {
            pushFollow(FOLLOW_38);
            rule__GroupGraphPatternSub__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3012:1: rule__GroupGraphPatternSub__Group__3__Impl : ( ( '.' )? ) ;
    public final void rule__GroupGraphPatternSub__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3016:1: ( ( ( '.' )? ) )
            // InternalStreamingsparql.g:3017:1: ( ( '.' )? )
            {
            // InternalStreamingsparql.g:3017:1: ( ( '.' )? )
            // InternalStreamingsparql.g:3018:2: ( '.' )?
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_3()); 
            // InternalStreamingsparql.g:3019:2: ( '.' )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==48) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // InternalStreamingsparql.g:3019:3: '.'
                    {
                    match(input,48,FOLLOW_2); 

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
    // InternalStreamingsparql.g:3027:1: rule__GroupGraphPatternSub__Group__4 : rule__GroupGraphPatternSub__Group__4__Impl ;
    public final void rule__GroupGraphPatternSub__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3031:1: ( rule__GroupGraphPatternSub__Group__4__Impl )
            // InternalStreamingsparql.g:3032:2: rule__GroupGraphPatternSub__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3038:1: rule__GroupGraphPatternSub__Group__4__Impl : ( '}' ) ;
    public final void rule__GroupGraphPatternSub__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3042:1: ( ( '}' ) )
            // InternalStreamingsparql.g:3043:1: ( '}' )
            {
            // InternalStreamingsparql.g:3043:1: ( '}' )
            // InternalStreamingsparql.g:3044:2: '}'
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getRightCurlyBracketKeyword_4()); 
            match(input,47,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3054:1: rule__GroupGraphPatternSub__Group_2__0 : rule__GroupGraphPatternSub__Group_2__0__Impl rule__GroupGraphPatternSub__Group_2__1 ;
    public final void rule__GroupGraphPatternSub__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3058:1: ( rule__GroupGraphPatternSub__Group_2__0__Impl rule__GroupGraphPatternSub__Group_2__1 )
            // InternalStreamingsparql.g:3059:2: rule__GroupGraphPatternSub__Group_2__0__Impl rule__GroupGraphPatternSub__Group_2__1
            {
            pushFollow(FOLLOW_37);
            rule__GroupGraphPatternSub__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3066:1: rule__GroupGraphPatternSub__Group_2__0__Impl : ( '.' ) ;
    public final void rule__GroupGraphPatternSub__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3070:1: ( ( '.' ) )
            // InternalStreamingsparql.g:3071:1: ( '.' )
            {
            // InternalStreamingsparql.g:3071:1: ( '.' )
            // InternalStreamingsparql.g:3072:2: '.'
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getFullStopKeyword_2_0()); 
            match(input,48,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3081:1: rule__GroupGraphPatternSub__Group_2__1 : rule__GroupGraphPatternSub__Group_2__1__Impl ;
    public final void rule__GroupGraphPatternSub__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3085:1: ( rule__GroupGraphPatternSub__Group_2__1__Impl )
            // InternalStreamingsparql.g:3086:2: rule__GroupGraphPatternSub__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3092:1: rule__GroupGraphPatternSub__Group_2__1__Impl : ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 ) ) ;
    public final void rule__GroupGraphPatternSub__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3096:1: ( ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 ) ) )
            // InternalStreamingsparql.g:3097:1: ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 ) )
            {
            // InternalStreamingsparql.g:3097:1: ( ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 ) )
            // InternalStreamingsparql.g:3098:2: ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 )
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsAssignment_2_1()); 
            // InternalStreamingsparql.g:3099:2: ( rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 )
            // InternalStreamingsparql.g:3099:3: rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3108:1: rule__TriplesSameSubject__Group__0 : rule__TriplesSameSubject__Group__0__Impl rule__TriplesSameSubject__Group__1 ;
    public final void rule__TriplesSameSubject__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3112:1: ( rule__TriplesSameSubject__Group__0__Impl rule__TriplesSameSubject__Group__1 )
            // InternalStreamingsparql.g:3113:2: rule__TriplesSameSubject__Group__0__Impl rule__TriplesSameSubject__Group__1
            {
            pushFollow(FOLLOW_37);
            rule__TriplesSameSubject__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3120:1: rule__TriplesSameSubject__Group__0__Impl : ( ( rule__TriplesSameSubject__SubjectAssignment_0 ) ) ;
    public final void rule__TriplesSameSubject__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3124:1: ( ( ( rule__TriplesSameSubject__SubjectAssignment_0 ) ) )
            // InternalStreamingsparql.g:3125:1: ( ( rule__TriplesSameSubject__SubjectAssignment_0 ) )
            {
            // InternalStreamingsparql.g:3125:1: ( ( rule__TriplesSameSubject__SubjectAssignment_0 ) )
            // InternalStreamingsparql.g:3126:2: ( rule__TriplesSameSubject__SubjectAssignment_0 )
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getSubjectAssignment_0()); 
            // InternalStreamingsparql.g:3127:2: ( rule__TriplesSameSubject__SubjectAssignment_0 )
            // InternalStreamingsparql.g:3127:3: rule__TriplesSameSubject__SubjectAssignment_0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3135:1: rule__TriplesSameSubject__Group__1 : rule__TriplesSameSubject__Group__1__Impl rule__TriplesSameSubject__Group__2 ;
    public final void rule__TriplesSameSubject__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3139:1: ( rule__TriplesSameSubject__Group__1__Impl rule__TriplesSameSubject__Group__2 )
            // InternalStreamingsparql.g:3140:2: rule__TriplesSameSubject__Group__1__Impl rule__TriplesSameSubject__Group__2
            {
            pushFollow(FOLLOW_40);
            rule__TriplesSameSubject__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3147:1: rule__TriplesSameSubject__Group__1__Impl : ( ( rule__TriplesSameSubject__PropertyListAssignment_1 ) ) ;
    public final void rule__TriplesSameSubject__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3151:1: ( ( ( rule__TriplesSameSubject__PropertyListAssignment_1 ) ) )
            // InternalStreamingsparql.g:3152:1: ( ( rule__TriplesSameSubject__PropertyListAssignment_1 ) )
            {
            // InternalStreamingsparql.g:3152:1: ( ( rule__TriplesSameSubject__PropertyListAssignment_1 ) )
            // InternalStreamingsparql.g:3153:2: ( rule__TriplesSameSubject__PropertyListAssignment_1 )
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getPropertyListAssignment_1()); 
            // InternalStreamingsparql.g:3154:2: ( rule__TriplesSameSubject__PropertyListAssignment_1 )
            // InternalStreamingsparql.g:3154:3: rule__TriplesSameSubject__PropertyListAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3162:1: rule__TriplesSameSubject__Group__2 : rule__TriplesSameSubject__Group__2__Impl ;
    public final void rule__TriplesSameSubject__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3166:1: ( rule__TriplesSameSubject__Group__2__Impl )
            // InternalStreamingsparql.g:3167:2: rule__TriplesSameSubject__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3173:1: rule__TriplesSameSubject__Group__2__Impl : ( ( rule__TriplesSameSubject__Group_2__0 )* ) ;
    public final void rule__TriplesSameSubject__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3177:1: ( ( ( rule__TriplesSameSubject__Group_2__0 )* ) )
            // InternalStreamingsparql.g:3178:1: ( ( rule__TriplesSameSubject__Group_2__0 )* )
            {
            // InternalStreamingsparql.g:3178:1: ( ( rule__TriplesSameSubject__Group_2__0 )* )
            // InternalStreamingsparql.g:3179:2: ( rule__TriplesSameSubject__Group_2__0 )*
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getGroup_2()); 
            // InternalStreamingsparql.g:3180:2: ( rule__TriplesSameSubject__Group_2__0 )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==49) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // InternalStreamingsparql.g:3180:3: rule__TriplesSameSubject__Group_2__0
            	    {
            	    pushFollow(FOLLOW_41);
            	    rule__TriplesSameSubject__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop27;
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
    // InternalStreamingsparql.g:3189:1: rule__TriplesSameSubject__Group_2__0 : rule__TriplesSameSubject__Group_2__0__Impl rule__TriplesSameSubject__Group_2__1 ;
    public final void rule__TriplesSameSubject__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3193:1: ( rule__TriplesSameSubject__Group_2__0__Impl rule__TriplesSameSubject__Group_2__1 )
            // InternalStreamingsparql.g:3194:2: rule__TriplesSameSubject__Group_2__0__Impl rule__TriplesSameSubject__Group_2__1
            {
            pushFollow(FOLLOW_37);
            rule__TriplesSameSubject__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3201:1: rule__TriplesSameSubject__Group_2__0__Impl : ( ';' ) ;
    public final void rule__TriplesSameSubject__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3205:1: ( ( ';' ) )
            // InternalStreamingsparql.g:3206:1: ( ';' )
            {
            // InternalStreamingsparql.g:3206:1: ( ';' )
            // InternalStreamingsparql.g:3207:2: ';'
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getSemicolonKeyword_2_0()); 
            match(input,49,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3216:1: rule__TriplesSameSubject__Group_2__1 : rule__TriplesSameSubject__Group_2__1__Impl ;
    public final void rule__TriplesSameSubject__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3220:1: ( rule__TriplesSameSubject__Group_2__1__Impl )
            // InternalStreamingsparql.g:3221:2: rule__TriplesSameSubject__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3227:1: rule__TriplesSameSubject__Group_2__1__Impl : ( ( rule__TriplesSameSubject__PropertyListAssignment_2_1 ) ) ;
    public final void rule__TriplesSameSubject__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3231:1: ( ( ( rule__TriplesSameSubject__PropertyListAssignment_2_1 ) ) )
            // InternalStreamingsparql.g:3232:1: ( ( rule__TriplesSameSubject__PropertyListAssignment_2_1 ) )
            {
            // InternalStreamingsparql.g:3232:1: ( ( rule__TriplesSameSubject__PropertyListAssignment_2_1 ) )
            // InternalStreamingsparql.g:3233:2: ( rule__TriplesSameSubject__PropertyListAssignment_2_1 )
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getPropertyListAssignment_2_1()); 
            // InternalStreamingsparql.g:3234:2: ( rule__TriplesSameSubject__PropertyListAssignment_2_1 )
            // InternalStreamingsparql.g:3234:3: rule__TriplesSameSubject__PropertyListAssignment_2_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3243:1: rule__PropertyList__Group__0 : rule__PropertyList__Group__0__Impl rule__PropertyList__Group__1 ;
    public final void rule__PropertyList__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3247:1: ( rule__PropertyList__Group__0__Impl rule__PropertyList__Group__1 )
            // InternalStreamingsparql.g:3248:2: rule__PropertyList__Group__0__Impl rule__PropertyList__Group__1
            {
            pushFollow(FOLLOW_37);
            rule__PropertyList__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3255:1: rule__PropertyList__Group__0__Impl : ( ( rule__PropertyList__PropertyAssignment_0 ) ) ;
    public final void rule__PropertyList__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3259:1: ( ( ( rule__PropertyList__PropertyAssignment_0 ) ) )
            // InternalStreamingsparql.g:3260:1: ( ( rule__PropertyList__PropertyAssignment_0 ) )
            {
            // InternalStreamingsparql.g:3260:1: ( ( rule__PropertyList__PropertyAssignment_0 ) )
            // InternalStreamingsparql.g:3261:2: ( rule__PropertyList__PropertyAssignment_0 )
            {
             before(grammarAccess.getPropertyListAccess().getPropertyAssignment_0()); 
            // InternalStreamingsparql.g:3262:2: ( rule__PropertyList__PropertyAssignment_0 )
            // InternalStreamingsparql.g:3262:3: rule__PropertyList__PropertyAssignment_0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3270:1: rule__PropertyList__Group__1 : rule__PropertyList__Group__1__Impl ;
    public final void rule__PropertyList__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3274:1: ( rule__PropertyList__Group__1__Impl )
            // InternalStreamingsparql.g:3275:2: rule__PropertyList__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3281:1: rule__PropertyList__Group__1__Impl : ( ( rule__PropertyList__ObjectAssignment_1 ) ) ;
    public final void rule__PropertyList__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3285:1: ( ( ( rule__PropertyList__ObjectAssignment_1 ) ) )
            // InternalStreamingsparql.g:3286:1: ( ( rule__PropertyList__ObjectAssignment_1 ) )
            {
            // InternalStreamingsparql.g:3286:1: ( ( rule__PropertyList__ObjectAssignment_1 ) )
            // InternalStreamingsparql.g:3287:2: ( rule__PropertyList__ObjectAssignment_1 )
            {
             before(grammarAccess.getPropertyListAccess().getObjectAssignment_1()); 
            // InternalStreamingsparql.g:3288:2: ( rule__PropertyList__ObjectAssignment_1 )
            // InternalStreamingsparql.g:3288:3: rule__PropertyList__ObjectAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3297:1: rule__UnNamedVariable__Group__0 : rule__UnNamedVariable__Group__0__Impl rule__UnNamedVariable__Group__1 ;
    public final void rule__UnNamedVariable__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3301:1: ( rule__UnNamedVariable__Group__0__Impl rule__UnNamedVariable__Group__1 )
            // InternalStreamingsparql.g:3302:2: rule__UnNamedVariable__Group__0__Impl rule__UnNamedVariable__Group__1
            {
            pushFollow(FOLLOW_3);
            rule__UnNamedVariable__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3309:1: rule__UnNamedVariable__Group__0__Impl : ( '?' ) ;
    public final void rule__UnNamedVariable__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3313:1: ( ( '?' ) )
            // InternalStreamingsparql.g:3314:1: ( '?' )
            {
            // InternalStreamingsparql.g:3314:1: ( '?' )
            // InternalStreamingsparql.g:3315:2: '?'
            {
             before(grammarAccess.getUnNamedVariableAccess().getQuestionMarkKeyword_0()); 
            match(input,50,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3324:1: rule__UnNamedVariable__Group__1 : rule__UnNamedVariable__Group__1__Impl ;
    public final void rule__UnNamedVariable__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3328:1: ( rule__UnNamedVariable__Group__1__Impl )
            // InternalStreamingsparql.g:3329:2: rule__UnNamedVariable__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3335:1: rule__UnNamedVariable__Group__1__Impl : ( ( rule__UnNamedVariable__NameAssignment_1 ) ) ;
    public final void rule__UnNamedVariable__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3339:1: ( ( ( rule__UnNamedVariable__NameAssignment_1 ) ) )
            // InternalStreamingsparql.g:3340:1: ( ( rule__UnNamedVariable__NameAssignment_1 ) )
            {
            // InternalStreamingsparql.g:3340:1: ( ( rule__UnNamedVariable__NameAssignment_1 ) )
            // InternalStreamingsparql.g:3341:2: ( rule__UnNamedVariable__NameAssignment_1 )
            {
             before(grammarAccess.getUnNamedVariableAccess().getNameAssignment_1()); 
            // InternalStreamingsparql.g:3342:2: ( rule__UnNamedVariable__NameAssignment_1 )
            // InternalStreamingsparql.g:3342:3: rule__UnNamedVariable__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3351:1: rule__Property__Group__0 : rule__Property__Group__0__Impl rule__Property__Group__1 ;
    public final void rule__Property__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3355:1: ( rule__Property__Group__0__Impl rule__Property__Group__1 )
            // InternalStreamingsparql.g:3356:2: rule__Property__Group__0__Impl rule__Property__Group__1
            {
            pushFollow(FOLLOW_4);
            rule__Property__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3363:1: rule__Property__Group__0__Impl : ( ( rule__Property__PrefixAssignment_0 ) ) ;
    public final void rule__Property__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3367:1: ( ( ( rule__Property__PrefixAssignment_0 ) ) )
            // InternalStreamingsparql.g:3368:1: ( ( rule__Property__PrefixAssignment_0 ) )
            {
            // InternalStreamingsparql.g:3368:1: ( ( rule__Property__PrefixAssignment_0 ) )
            // InternalStreamingsparql.g:3369:2: ( rule__Property__PrefixAssignment_0 )
            {
             before(grammarAccess.getPropertyAccess().getPrefixAssignment_0()); 
            // InternalStreamingsparql.g:3370:2: ( rule__Property__PrefixAssignment_0 )
            // InternalStreamingsparql.g:3370:3: rule__Property__PrefixAssignment_0
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3378:1: rule__Property__Group__1 : rule__Property__Group__1__Impl rule__Property__Group__2 ;
    public final void rule__Property__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3382:1: ( rule__Property__Group__1__Impl rule__Property__Group__2 )
            // InternalStreamingsparql.g:3383:2: rule__Property__Group__1__Impl rule__Property__Group__2
            {
            pushFollow(FOLLOW_3);
            rule__Property__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3390:1: rule__Property__Group__1__Impl : ( ':' ) ;
    public final void rule__Property__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3394:1: ( ( ':' ) )
            // InternalStreamingsparql.g:3395:1: ( ':' )
            {
            // InternalStreamingsparql.g:3395:1: ( ':' )
            // InternalStreamingsparql.g:3396:2: ':'
            {
             before(grammarAccess.getPropertyAccess().getColonKeyword_1()); 
            match(input,27,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3405:1: rule__Property__Group__2 : rule__Property__Group__2__Impl ;
    public final void rule__Property__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3409:1: ( rule__Property__Group__2__Impl )
            // InternalStreamingsparql.g:3410:2: rule__Property__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3416:1: rule__Property__Group__2__Impl : ( ( rule__Property__NameAssignment_2 ) ) ;
    public final void rule__Property__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3420:1: ( ( ( rule__Property__NameAssignment_2 ) ) )
            // InternalStreamingsparql.g:3421:1: ( ( rule__Property__NameAssignment_2 ) )
            {
            // InternalStreamingsparql.g:3421:1: ( ( rule__Property__NameAssignment_2 ) )
            // InternalStreamingsparql.g:3422:2: ( rule__Property__NameAssignment_2 )
            {
             before(grammarAccess.getPropertyAccess().getNameAssignment_2()); 
            // InternalStreamingsparql.g:3423:2: ( rule__Property__NameAssignment_2 )
            // InternalStreamingsparql.g:3423:3: rule__Property__NameAssignment_2
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3432:1: rule__IRI__Group__0 : rule__IRI__Group__0__Impl rule__IRI__Group__1 ;
    public final void rule__IRI__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3436:1: ( rule__IRI__Group__0__Impl rule__IRI__Group__1 )
            // InternalStreamingsparql.g:3437:2: rule__IRI__Group__0__Impl rule__IRI__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__IRI__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3444:1: rule__IRI__Group__0__Impl : ( () ) ;
    public final void rule__IRI__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3448:1: ( ( () ) )
            // InternalStreamingsparql.g:3449:1: ( () )
            {
            // InternalStreamingsparql.g:3449:1: ( () )
            // InternalStreamingsparql.g:3450:2: ()
            {
             before(grammarAccess.getIRIAccess().getIRIAction_0()); 
            // InternalStreamingsparql.g:3451:2: ()
            // InternalStreamingsparql.g:3451:3: 
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
    // InternalStreamingsparql.g:3459:1: rule__IRI__Group__1 : rule__IRI__Group__1__Impl ;
    public final void rule__IRI__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3463:1: ( rule__IRI__Group__1__Impl )
            // InternalStreamingsparql.g:3464:2: rule__IRI__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3470:1: rule__IRI__Group__1__Impl : ( ( rule__IRI__ValueAssignment_1 ) ) ;
    public final void rule__IRI__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3474:1: ( ( ( rule__IRI__ValueAssignment_1 ) ) )
            // InternalStreamingsparql.g:3475:1: ( ( rule__IRI__ValueAssignment_1 ) )
            {
            // InternalStreamingsparql.g:3475:1: ( ( rule__IRI__ValueAssignment_1 ) )
            // InternalStreamingsparql.g:3476:2: ( rule__IRI__ValueAssignment_1 )
            {
             before(grammarAccess.getIRIAccess().getValueAssignment_1()); 
            // InternalStreamingsparql.g:3477:2: ( rule__IRI__ValueAssignment_1 )
            // InternalStreamingsparql.g:3477:3: rule__IRI__ValueAssignment_1
            {
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3486:1: rule__Prefix__NameAssignment_0_1 : ( RULE_ID ) ;
    public final void rule__Prefix__NameAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3490:1: ( ( RULE_ID ) )
            // InternalStreamingsparql.g:3491:2: ( RULE_ID )
            {
            // InternalStreamingsparql.g:3491:2: ( RULE_ID )
            // InternalStreamingsparql.g:3492:3: RULE_ID
            {
             before(grammarAccess.getPrefixAccess().getNameIDTerminalRuleCall_0_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3501:1: rule__Prefix__IrefAssignment_0_3 : ( RULE_IRI_TERMINAL ) ;
    public final void rule__Prefix__IrefAssignment_0_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3505:1: ( ( RULE_IRI_TERMINAL ) )
            // InternalStreamingsparql.g:3506:2: ( RULE_IRI_TERMINAL )
            {
            // InternalStreamingsparql.g:3506:2: ( RULE_IRI_TERMINAL )
            // InternalStreamingsparql.g:3507:3: RULE_IRI_TERMINAL
            {
             before(grammarAccess.getPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_0_3_0()); 
            match(input,RULE_IRI_TERMINAL,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3516:1: rule__UnNamedPrefix__IrefAssignment_2 : ( RULE_IRI_TERMINAL ) ;
    public final void rule__UnNamedPrefix__IrefAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3520:1: ( ( RULE_IRI_TERMINAL ) )
            // InternalStreamingsparql.g:3521:2: ( RULE_IRI_TERMINAL )
            {
            // InternalStreamingsparql.g:3521:2: ( RULE_IRI_TERMINAL )
            // InternalStreamingsparql.g:3522:3: RULE_IRI_TERMINAL
            {
             before(grammarAccess.getUnNamedPrefixAccess().getIrefIRI_TERMINALTerminalRuleCall_2_0()); 
            match(input,RULE_IRI_TERMINAL,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3531:1: rule__Base__IrefAssignment_1 : ( ruleIRI ) ;
    public final void rule__Base__IrefAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3535:1: ( ( ruleIRI ) )
            // InternalStreamingsparql.g:3536:2: ( ruleIRI )
            {
            // InternalStreamingsparql.g:3536:2: ( ruleIRI )
            // InternalStreamingsparql.g:3537:3: ruleIRI
            {
             before(grammarAccess.getBaseAccess().getIrefIRIParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3546:1: rule__SelectQuery__MethodAssignment_0_0 : ( ( '#ADDQUERY' ) ) ;
    public final void rule__SelectQuery__MethodAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3550:1: ( ( ( '#ADDQUERY' ) ) )
            // InternalStreamingsparql.g:3551:2: ( ( '#ADDQUERY' ) )
            {
            // InternalStreamingsparql.g:3551:2: ( ( '#ADDQUERY' ) )
            // InternalStreamingsparql.g:3552:3: ( '#ADDQUERY' )
            {
             before(grammarAccess.getSelectQueryAccess().getMethodADDQUERYKeyword_0_0_0()); 
            // InternalStreamingsparql.g:3553:3: ( '#ADDQUERY' )
            // InternalStreamingsparql.g:3554:4: '#ADDQUERY'
            {
             before(grammarAccess.getSelectQueryAccess().getMethodADDQUERYKeyword_0_0_0()); 
            match(input,51,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3565:1: rule__SelectQuery__BaseAssignment_1 : ( ruleBase ) ;
    public final void rule__SelectQuery__BaseAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3569:1: ( ( ruleBase ) )
            // InternalStreamingsparql.g:3570:2: ( ruleBase )
            {
            // InternalStreamingsparql.g:3570:2: ( ruleBase )
            // InternalStreamingsparql.g:3571:3: ruleBase
            {
             before(grammarAccess.getSelectQueryAccess().getBaseBaseParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3580:1: rule__SelectQuery__PrefixesAssignment_2 : ( rulePrefix ) ;
    public final void rule__SelectQuery__PrefixesAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3584:1: ( ( rulePrefix ) )
            // InternalStreamingsparql.g:3585:2: ( rulePrefix )
            {
            // InternalStreamingsparql.g:3585:2: ( rulePrefix )
            // InternalStreamingsparql.g:3586:3: rulePrefix
            {
             before(grammarAccess.getSelectQueryAccess().getPrefixesPrefixParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3595:1: rule__SelectQuery__DatasetClausesAssignment_3 : ( ruleDatasetClause ) ;
    public final void rule__SelectQuery__DatasetClausesAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3599:1: ( ( ruleDatasetClause ) )
            // InternalStreamingsparql.g:3600:2: ( ruleDatasetClause )
            {
            // InternalStreamingsparql.g:3600:2: ( ruleDatasetClause )
            // InternalStreamingsparql.g:3601:3: ruleDatasetClause
            {
             before(grammarAccess.getSelectQueryAccess().getDatasetClausesDatasetClauseParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
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


    // $ANTLR start "rule__SelectQuery__VariablesAssignment_5"
    // InternalStreamingsparql.g:3610:1: rule__SelectQuery__VariablesAssignment_5 : ( ruleVariable ) ;
    public final void rule__SelectQuery__VariablesAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3614:1: ( ( ruleVariable ) )
            // InternalStreamingsparql.g:3615:2: ( ruleVariable )
            {
            // InternalStreamingsparql.g:3615:2: ( ruleVariable )
            // InternalStreamingsparql.g:3616:3: ruleVariable
            {
             before(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_5_0()); 
            pushFollow(FOLLOW_2);
            ruleVariable();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SelectQuery__VariablesAssignment_5"


    // $ANTLR start "rule__SelectQuery__VariablesAssignment_6"
    // InternalStreamingsparql.g:3625:1: rule__SelectQuery__VariablesAssignment_6 : ( ruleVariable ) ;
    public final void rule__SelectQuery__VariablesAssignment_6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3629:1: ( ( ruleVariable ) )
            // InternalStreamingsparql.g:3630:2: ( ruleVariable )
            {
            // InternalStreamingsparql.g:3630:2: ( ruleVariable )
            // InternalStreamingsparql.g:3631:3: ruleVariable
            {
             before(grammarAccess.getSelectQueryAccess().getVariablesVariableParserRuleCall_6_0()); 
            pushFollow(FOLLOW_2);
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


    // $ANTLR start "rule__SelectQuery__WhereClauseAssignment_7"
    // InternalStreamingsparql.g:3640:1: rule__SelectQuery__WhereClauseAssignment_7 : ( ruleWhereClause ) ;
    public final void rule__SelectQuery__WhereClauseAssignment_7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3644:1: ( ( ruleWhereClause ) )
            // InternalStreamingsparql.g:3645:2: ( ruleWhereClause )
            {
            // InternalStreamingsparql.g:3645:2: ( ruleWhereClause )
            // InternalStreamingsparql.g:3646:3: ruleWhereClause
            {
             before(grammarAccess.getSelectQueryAccess().getWhereClauseWhereClauseParserRuleCall_7_0()); 
            pushFollow(FOLLOW_2);
            ruleWhereClause();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getWhereClauseWhereClauseParserRuleCall_7_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SelectQuery__WhereClauseAssignment_7"


    // $ANTLR start "rule__SelectQuery__FilterclauseAssignment_8"
    // InternalStreamingsparql.g:3655:1: rule__SelectQuery__FilterclauseAssignment_8 : ( ruleFilterclause ) ;
    public final void rule__SelectQuery__FilterclauseAssignment_8() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3659:1: ( ( ruleFilterclause ) )
            // InternalStreamingsparql.g:3660:2: ( ruleFilterclause )
            {
            // InternalStreamingsparql.g:3660:2: ( ruleFilterclause )
            // InternalStreamingsparql.g:3661:3: ruleFilterclause
            {
             before(grammarAccess.getSelectQueryAccess().getFilterclauseFilterclauseParserRuleCall_8_0()); 
            pushFollow(FOLLOW_2);
            ruleFilterclause();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getFilterclauseFilterclauseParserRuleCall_8_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SelectQuery__FilterclauseAssignment_8"


    // $ANTLR start "rule__SelectQuery__AggregateClauseAssignment_9"
    // InternalStreamingsparql.g:3670:1: rule__SelectQuery__AggregateClauseAssignment_9 : ( ruleAggregate ) ;
    public final void rule__SelectQuery__AggregateClauseAssignment_9() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3674:1: ( ( ruleAggregate ) )
            // InternalStreamingsparql.g:3675:2: ( ruleAggregate )
            {
            // InternalStreamingsparql.g:3675:2: ( ruleAggregate )
            // InternalStreamingsparql.g:3676:3: ruleAggregate
            {
             before(grammarAccess.getSelectQueryAccess().getAggregateClauseAggregateParserRuleCall_9_0()); 
            pushFollow(FOLLOW_2);
            ruleAggregate();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getAggregateClauseAggregateParserRuleCall_9_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SelectQuery__AggregateClauseAssignment_9"


    // $ANTLR start "rule__SelectQuery__FilesinkclauseAssignment_10"
    // InternalStreamingsparql.g:3685:1: rule__SelectQuery__FilesinkclauseAssignment_10 : ( ruleFilesinkclause ) ;
    public final void rule__SelectQuery__FilesinkclauseAssignment_10() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3689:1: ( ( ruleFilesinkclause ) )
            // InternalStreamingsparql.g:3690:2: ( ruleFilesinkclause )
            {
            // InternalStreamingsparql.g:3690:2: ( ruleFilesinkclause )
            // InternalStreamingsparql.g:3691:3: ruleFilesinkclause
            {
             before(grammarAccess.getSelectQueryAccess().getFilesinkclauseFilesinkclauseParserRuleCall_10_0()); 
            pushFollow(FOLLOW_2);
            ruleFilesinkclause();

            state._fsp--;

             after(grammarAccess.getSelectQueryAccess().getFilesinkclauseFilesinkclauseParserRuleCall_10_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SelectQuery__FilesinkclauseAssignment_10"


    // $ANTLR start "rule__Aggregate__AggregationsAssignment_2_3"
    // InternalStreamingsparql.g:3700:1: rule__Aggregate__AggregationsAssignment_2_3 : ( ruleAggregation ) ;
    public final void rule__Aggregate__AggregationsAssignment_2_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3704:1: ( ( ruleAggregation ) )
            // InternalStreamingsparql.g:3705:2: ( ruleAggregation )
            {
            // InternalStreamingsparql.g:3705:2: ( ruleAggregation )
            // InternalStreamingsparql.g:3706:3: ruleAggregation
            {
             before(grammarAccess.getAggregateAccess().getAggregationsAggregationParserRuleCall_2_3_0()); 
            pushFollow(FOLLOW_2);
            ruleAggregation();

            state._fsp--;

             after(grammarAccess.getAggregateAccess().getAggregationsAggregationParserRuleCall_2_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Aggregate__AggregationsAssignment_2_3"


    // $ANTLR start "rule__Aggregate__GroupbyAssignment_3_1"
    // InternalStreamingsparql.g:3715:1: rule__Aggregate__GroupbyAssignment_3_1 : ( ruleGroupBy ) ;
    public final void rule__Aggregate__GroupbyAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3719:1: ( ( ruleGroupBy ) )
            // InternalStreamingsparql.g:3720:2: ( ruleGroupBy )
            {
            // InternalStreamingsparql.g:3720:2: ( ruleGroupBy )
            // InternalStreamingsparql.g:3721:3: ruleGroupBy
            {
             before(grammarAccess.getAggregateAccess().getGroupbyGroupByParserRuleCall_3_1_0()); 
            pushFollow(FOLLOW_2);
            ruleGroupBy();

            state._fsp--;

             after(grammarAccess.getAggregateAccess().getGroupbyGroupByParserRuleCall_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Aggregate__GroupbyAssignment_3_1"


    // $ANTLR start "rule__GroupBy__VariablesAssignment_1"
    // InternalStreamingsparql.g:3730:1: rule__GroupBy__VariablesAssignment_1 : ( ruleVariable ) ;
    public final void rule__GroupBy__VariablesAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3734:1: ( ( ruleVariable ) )
            // InternalStreamingsparql.g:3735:2: ( ruleVariable )
            {
            // InternalStreamingsparql.g:3735:2: ( ruleVariable )
            // InternalStreamingsparql.g:3736:3: ruleVariable
            {
             before(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3745:1: rule__GroupBy__VariablesAssignment_2_1 : ( ruleVariable ) ;
    public final void rule__GroupBy__VariablesAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3749:1: ( ( ruleVariable ) )
            // InternalStreamingsparql.g:3750:2: ( ruleVariable )
            {
            // InternalStreamingsparql.g:3750:2: ( ruleVariable )
            // InternalStreamingsparql.g:3751:3: ruleVariable
            {
             before(grammarAccess.getGroupByAccess().getVariablesVariableParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3760:1: rule__Aggregation__FunctionAssignment_1 : ( RULE_AGG_FUNCTION ) ;
    public final void rule__Aggregation__FunctionAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3764:1: ( ( RULE_AGG_FUNCTION ) )
            // InternalStreamingsparql.g:3765:2: ( RULE_AGG_FUNCTION )
            {
            // InternalStreamingsparql.g:3765:2: ( RULE_AGG_FUNCTION )
            // InternalStreamingsparql.g:3766:3: RULE_AGG_FUNCTION
            {
             before(grammarAccess.getAggregationAccess().getFunctionAGG_FUNCTIONTerminalRuleCall_1_0()); 
            match(input,RULE_AGG_FUNCTION,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3775:1: rule__Aggregation__VarToAggAssignment_3 : ( ruleVariable ) ;
    public final void rule__Aggregation__VarToAggAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3779:1: ( ( ruleVariable ) )
            // InternalStreamingsparql.g:3780:2: ( ruleVariable )
            {
            // InternalStreamingsparql.g:3780:2: ( ruleVariable )
            // InternalStreamingsparql.g:3781:3: ruleVariable
            {
             before(grammarAccess.getAggregationAccess().getVarToAggVariableParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3790:1: rule__Aggregation__AggNameAssignment_5 : ( RULE_STRING ) ;
    public final void rule__Aggregation__AggNameAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3794:1: ( ( RULE_STRING ) )
            // InternalStreamingsparql.g:3795:2: ( RULE_STRING )
            {
            // InternalStreamingsparql.g:3795:2: ( RULE_STRING )
            // InternalStreamingsparql.g:3796:3: RULE_STRING
            {
             before(grammarAccess.getAggregationAccess().getAggNameSTRINGTerminalRuleCall_5_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3805:1: rule__Aggregation__DatatypeAssignment_6_1 : ( RULE_STRING ) ;
    public final void rule__Aggregation__DatatypeAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3809:1: ( ( RULE_STRING ) )
            // InternalStreamingsparql.g:3810:2: ( RULE_STRING )
            {
            // InternalStreamingsparql.g:3810:2: ( RULE_STRING )
            // InternalStreamingsparql.g:3811:3: RULE_STRING
            {
             before(grammarAccess.getAggregationAccess().getDatatypeSTRINGTerminalRuleCall_6_1_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3820:1: rule__Filesinkclause__PathAssignment_1 : ( RULE_STRING ) ;
    public final void rule__Filesinkclause__PathAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3824:1: ( ( RULE_STRING ) )
            // InternalStreamingsparql.g:3825:2: ( RULE_STRING )
            {
            // InternalStreamingsparql.g:3825:2: ( RULE_STRING )
            // InternalStreamingsparql.g:3826:3: RULE_STRING
            {
             before(grammarAccess.getFilesinkclauseAccess().getPathSTRINGTerminalRuleCall_1_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
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
    // InternalStreamingsparql.g:3835:1: rule__Filterclause__LeftAssignment_1 : ( ruleVariable ) ;
    public final void rule__Filterclause__LeftAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3839:1: ( ( ruleVariable ) )
            // InternalStreamingsparql.g:3840:2: ( ruleVariable )
            {
            // InternalStreamingsparql.g:3840:2: ( ruleVariable )
            // InternalStreamingsparql.g:3841:3: ruleVariable
            {
             before(grammarAccess.getFilterclauseAccess().getLeftVariableParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3850:1: rule__Filterclause__OperatorAssignment_2 : ( ruleOperator ) ;
    public final void rule__Filterclause__OperatorAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3854:1: ( ( ruleOperator ) )
            // InternalStreamingsparql.g:3855:2: ( ruleOperator )
            {
            // InternalStreamingsparql.g:3855:2: ( ruleOperator )
            // InternalStreamingsparql.g:3856:3: ruleOperator
            {
             before(grammarAccess.getFilterclauseAccess().getOperatorOperatorEnumRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3865:1: rule__Filterclause__RightAssignment_3 : ( ruleVariable ) ;
    public final void rule__Filterclause__RightAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3869:1: ( ( ruleVariable ) )
            // InternalStreamingsparql.g:3870:2: ( ruleVariable )
            {
            // InternalStreamingsparql.g:3870:2: ( ruleVariable )
            // InternalStreamingsparql.g:3871:3: ruleVariable
            {
             before(grammarAccess.getFilterclauseAccess().getRightVariableParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3880:1: rule__DatasetClause__DataSetAssignment_1 : ( ruleIRI ) ;
    public final void rule__DatasetClause__DataSetAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3884:1: ( ( ruleIRI ) )
            // InternalStreamingsparql.g:3885:2: ( ruleIRI )
            {
            // InternalStreamingsparql.g:3885:2: ( ruleIRI )
            // InternalStreamingsparql.g:3886:3: ruleIRI
            {
             before(grammarAccess.getDatasetClauseAccess().getDataSetIRIParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3895:1: rule__DatasetClause__NameAssignment_3 : ( RULE_ID ) ;
    public final void rule__DatasetClause__NameAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3899:1: ( ( RULE_ID ) )
            // InternalStreamingsparql.g:3900:2: ( RULE_ID )
            {
            // InternalStreamingsparql.g:3900:2: ( RULE_ID )
            // InternalStreamingsparql.g:3901:3: RULE_ID
            {
             before(grammarAccess.getDatasetClauseAccess().getNameIDTerminalRuleCall_3_0()); 
            match(input,RULE_ID,FOLLOW_2); 
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


    // $ANTLR start "rule__DatasetClause__TypeAssignment_4_2"
    // InternalStreamingsparql.g:3910:1: rule__DatasetClause__TypeAssignment_4_2 : ( RULE_WINDOWTYPE ) ;
    public final void rule__DatasetClause__TypeAssignment_4_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3914:1: ( ( RULE_WINDOWTYPE ) )
            // InternalStreamingsparql.g:3915:2: ( RULE_WINDOWTYPE )
            {
            // InternalStreamingsparql.g:3915:2: ( RULE_WINDOWTYPE )
            // InternalStreamingsparql.g:3916:3: RULE_WINDOWTYPE
            {
             before(grammarAccess.getDatasetClauseAccess().getTypeWINDOWTYPETerminalRuleCall_4_2_0()); 
            match(input,RULE_WINDOWTYPE,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getTypeWINDOWTYPETerminalRuleCall_4_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DatasetClause__TypeAssignment_4_2"


    // $ANTLR start "rule__DatasetClause__SizeAssignment_4_4"
    // InternalStreamingsparql.g:3925:1: rule__DatasetClause__SizeAssignment_4_4 : ( RULE_INT ) ;
    public final void rule__DatasetClause__SizeAssignment_4_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3929:1: ( ( RULE_INT ) )
            // InternalStreamingsparql.g:3930:2: ( RULE_INT )
            {
            // InternalStreamingsparql.g:3930:2: ( RULE_INT )
            // InternalStreamingsparql.g:3931:3: RULE_INT
            {
             before(grammarAccess.getDatasetClauseAccess().getSizeINTTerminalRuleCall_4_4_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getSizeINTTerminalRuleCall_4_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DatasetClause__SizeAssignment_4_4"


    // $ANTLR start "rule__DatasetClause__AdvanceAssignment_4_5_1"
    // InternalStreamingsparql.g:3940:1: rule__DatasetClause__AdvanceAssignment_4_5_1 : ( RULE_INT ) ;
    public final void rule__DatasetClause__AdvanceAssignment_4_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3944:1: ( ( RULE_INT ) )
            // InternalStreamingsparql.g:3945:2: ( RULE_INT )
            {
            // InternalStreamingsparql.g:3945:2: ( RULE_INT )
            // InternalStreamingsparql.g:3946:3: RULE_INT
            {
             before(grammarAccess.getDatasetClauseAccess().getAdvanceINTTerminalRuleCall_4_5_1_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getAdvanceINTTerminalRuleCall_4_5_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DatasetClause__AdvanceAssignment_4_5_1"


    // $ANTLR start "rule__DatasetClause__UnitAssignment_4_6_1"
    // InternalStreamingsparql.g:3955:1: rule__DatasetClause__UnitAssignment_4_6_1 : ( RULE_UNITTYPE ) ;
    public final void rule__DatasetClause__UnitAssignment_4_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3959:1: ( ( RULE_UNITTYPE ) )
            // InternalStreamingsparql.g:3960:2: ( RULE_UNITTYPE )
            {
            // InternalStreamingsparql.g:3960:2: ( RULE_UNITTYPE )
            // InternalStreamingsparql.g:3961:3: RULE_UNITTYPE
            {
             before(grammarAccess.getDatasetClauseAccess().getUnitUNITTYPETerminalRuleCall_4_6_1_0()); 
            match(input,RULE_UNITTYPE,FOLLOW_2); 
             after(grammarAccess.getDatasetClauseAccess().getUnitUNITTYPETerminalRuleCall_4_6_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DatasetClause__UnitAssignment_4_6_1"


    // $ANTLR start "rule__WhereClause__WhereclausesAssignment_2"
    // InternalStreamingsparql.g:3970:1: rule__WhereClause__WhereclausesAssignment_2 : ( ruleInnerWhereClause ) ;
    public final void rule__WhereClause__WhereclausesAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3974:1: ( ( ruleInnerWhereClause ) )
            // InternalStreamingsparql.g:3975:2: ( ruleInnerWhereClause )
            {
            // InternalStreamingsparql.g:3975:2: ( ruleInnerWhereClause )
            // InternalStreamingsparql.g:3976:3: ruleInnerWhereClause
            {
             before(grammarAccess.getWhereClauseAccess().getWhereclausesInnerWhereClauseParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:3985:1: rule__InnerWhereClause__NameAssignment_0 : ( ( RULE_ID ) ) ;
    public final void rule__InnerWhereClause__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:3989:1: ( ( ( RULE_ID ) ) )
            // InternalStreamingsparql.g:3990:2: ( ( RULE_ID ) )
            {
            // InternalStreamingsparql.g:3990:2: ( ( RULE_ID ) )
            // InternalStreamingsparql.g:3991:3: ( RULE_ID )
            {
             before(grammarAccess.getInnerWhereClauseAccess().getNameDatasetClauseCrossReference_0_0()); 
            // InternalStreamingsparql.g:3992:3: ( RULE_ID )
            // InternalStreamingsparql.g:3993:4: RULE_ID
            {
             before(grammarAccess.getInnerWhereClauseAccess().getNameDatasetClauseIDTerminalRuleCall_0_0_1()); 
            match(input,RULE_ID,FOLLOW_2); 
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
    // InternalStreamingsparql.g:4004:1: rule__InnerWhereClause__GroupGraphPatternAssignment_1 : ( ruleGroupGraphPatternSub ) ;
    public final void rule__InnerWhereClause__GroupGraphPatternAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4008:1: ( ( ruleGroupGraphPatternSub ) )
            // InternalStreamingsparql.g:4009:2: ( ruleGroupGraphPatternSub )
            {
            // InternalStreamingsparql.g:4009:2: ( ruleGroupGraphPatternSub )
            // InternalStreamingsparql.g:4010:3: ruleGroupGraphPatternSub
            {
             before(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternGroupGraphPatternSubParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4019:1: rule__GroupGraphPatternSub__GraphPatternsAssignment_1 : ( ruleTriplesSameSubject ) ;
    public final void rule__GroupGraphPatternSub__GraphPatternsAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4023:1: ( ( ruleTriplesSameSubject ) )
            // InternalStreamingsparql.g:4024:2: ( ruleTriplesSameSubject )
            {
            // InternalStreamingsparql.g:4024:2: ( ruleTriplesSameSubject )
            // InternalStreamingsparql.g:4025:3: ruleTriplesSameSubject
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4034:1: rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1 : ( ruleTriplesSameSubject ) ;
    public final void rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4038:1: ( ( ruleTriplesSameSubject ) )
            // InternalStreamingsparql.g:4039:2: ( ruleTriplesSameSubject )
            {
            // InternalStreamingsparql.g:4039:2: ( ruleTriplesSameSubject )
            // InternalStreamingsparql.g:4040:3: ruleTriplesSameSubject
            {
             before(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsTriplesSameSubjectParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4049:1: rule__TriplesSameSubject__SubjectAssignment_0 : ( ruleGraphNode ) ;
    public final void rule__TriplesSameSubject__SubjectAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4053:1: ( ( ruleGraphNode ) )
            // InternalStreamingsparql.g:4054:2: ( ruleGraphNode )
            {
            // InternalStreamingsparql.g:4054:2: ( ruleGraphNode )
            // InternalStreamingsparql.g:4055:3: ruleGraphNode
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getSubjectGraphNodeParserRuleCall_0_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4064:1: rule__TriplesSameSubject__PropertyListAssignment_1 : ( rulePropertyList ) ;
    public final void rule__TriplesSameSubject__PropertyListAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4068:1: ( ( rulePropertyList ) )
            // InternalStreamingsparql.g:4069:2: ( rulePropertyList )
            {
            // InternalStreamingsparql.g:4069:2: ( rulePropertyList )
            // InternalStreamingsparql.g:4070:3: rulePropertyList
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4079:1: rule__TriplesSameSubject__PropertyListAssignment_2_1 : ( rulePropertyList ) ;
    public final void rule__TriplesSameSubject__PropertyListAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4083:1: ( ( rulePropertyList ) )
            // InternalStreamingsparql.g:4084:2: ( rulePropertyList )
            {
            // InternalStreamingsparql.g:4084:2: ( rulePropertyList )
            // InternalStreamingsparql.g:4085:3: rulePropertyList
            {
             before(grammarAccess.getTriplesSameSubjectAccess().getPropertyListPropertyListParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4094:1: rule__PropertyList__PropertyAssignment_0 : ( ruleGraphNode ) ;
    public final void rule__PropertyList__PropertyAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4098:1: ( ( ruleGraphNode ) )
            // InternalStreamingsparql.g:4099:2: ( ruleGraphNode )
            {
            // InternalStreamingsparql.g:4099:2: ( ruleGraphNode )
            // InternalStreamingsparql.g:4100:3: ruleGraphNode
            {
             before(grammarAccess.getPropertyListAccess().getPropertyGraphNodeParserRuleCall_0_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4109:1: rule__PropertyList__ObjectAssignment_1 : ( ruleGraphNode ) ;
    public final void rule__PropertyList__ObjectAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4113:1: ( ( ruleGraphNode ) )
            // InternalStreamingsparql.g:4114:2: ( ruleGraphNode )
            {
            // InternalStreamingsparql.g:4114:2: ( ruleGraphNode )
            // InternalStreamingsparql.g:4115:3: ruleGraphNode
            {
             before(grammarAccess.getPropertyListAccess().getObjectGraphNodeParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4124:1: rule__GraphNode__VariableAssignment_0 : ( ruleVariable ) ;
    public final void rule__GraphNode__VariableAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4128:1: ( ( ruleVariable ) )
            // InternalStreamingsparql.g:4129:2: ( ruleVariable )
            {
            // InternalStreamingsparql.g:4129:2: ( ruleVariable )
            // InternalStreamingsparql.g:4130:3: ruleVariable
            {
             before(grammarAccess.getGraphNodeAccess().getVariableVariableParserRuleCall_0_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4139:1: rule__GraphNode__LiteralAssignment_1 : ( RULE_STRING ) ;
    public final void rule__GraphNode__LiteralAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4143:1: ( ( RULE_STRING ) )
            // InternalStreamingsparql.g:4144:2: ( RULE_STRING )
            {
            // InternalStreamingsparql.g:4144:2: ( RULE_STRING )
            // InternalStreamingsparql.g:4145:3: RULE_STRING
            {
             before(grammarAccess.getGraphNodeAccess().getLiteralSTRINGTerminalRuleCall_1_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
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
    // InternalStreamingsparql.g:4154:1: rule__GraphNode__IriAssignment_2 : ( ruleIRI ) ;
    public final void rule__GraphNode__IriAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4158:1: ( ( ruleIRI ) )
            // InternalStreamingsparql.g:4159:2: ( ruleIRI )
            {
            // InternalStreamingsparql.g:4159:2: ( ruleIRI )
            // InternalStreamingsparql.g:4160:3: ruleIRI
            {
             before(grammarAccess.getGraphNodeAccess().getIriIRIParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4169:1: rule__Variable__UnnamedAssignment_0 : ( ruleUnNamedVariable ) ;
    public final void rule__Variable__UnnamedAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4173:1: ( ( ruleUnNamedVariable ) )
            // InternalStreamingsparql.g:4174:2: ( ruleUnNamedVariable )
            {
            // InternalStreamingsparql.g:4174:2: ( ruleUnNamedVariable )
            // InternalStreamingsparql.g:4175:3: ruleUnNamedVariable
            {
             before(grammarAccess.getVariableAccess().getUnnamedUnNamedVariableParserRuleCall_0_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4184:1: rule__Variable__PropertyAssignment_1 : ( ruleProperty ) ;
    public final void rule__Variable__PropertyAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4188:1: ( ( ruleProperty ) )
            // InternalStreamingsparql.g:4189:2: ( ruleProperty )
            {
            // InternalStreamingsparql.g:4189:2: ( ruleProperty )
            // InternalStreamingsparql.g:4190:3: ruleProperty
            {
             before(grammarAccess.getVariableAccess().getPropertyPropertyParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
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
    // InternalStreamingsparql.g:4199:1: rule__UnNamedVariable__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__UnNamedVariable__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4203:1: ( ( RULE_ID ) )
            // InternalStreamingsparql.g:4204:2: ( RULE_ID )
            {
            // InternalStreamingsparql.g:4204:2: ( RULE_ID )
            // InternalStreamingsparql.g:4205:3: RULE_ID
            {
             before(grammarAccess.getUnNamedVariableAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
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
    // InternalStreamingsparql.g:4214:1: rule__Property__PrefixAssignment_0 : ( ( RULE_ID ) ) ;
    public final void rule__Property__PrefixAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4218:1: ( ( ( RULE_ID ) ) )
            // InternalStreamingsparql.g:4219:2: ( ( RULE_ID ) )
            {
            // InternalStreamingsparql.g:4219:2: ( ( RULE_ID ) )
            // InternalStreamingsparql.g:4220:3: ( RULE_ID )
            {
             before(grammarAccess.getPropertyAccess().getPrefixPrefixCrossReference_0_0()); 
            // InternalStreamingsparql.g:4221:3: ( RULE_ID )
            // InternalStreamingsparql.g:4222:4: RULE_ID
            {
             before(grammarAccess.getPropertyAccess().getPrefixPrefixIDTerminalRuleCall_0_0_1()); 
            match(input,RULE_ID,FOLLOW_2); 
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
    // InternalStreamingsparql.g:4233:1: rule__Property__NameAssignment_2 : ( RULE_ID ) ;
    public final void rule__Property__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4237:1: ( ( RULE_ID ) )
            // InternalStreamingsparql.g:4238:2: ( RULE_ID )
            {
            // InternalStreamingsparql.g:4238:2: ( RULE_ID )
            // InternalStreamingsparql.g:4239:3: RULE_ID
            {
             before(grammarAccess.getPropertyAccess().getNameIDTerminalRuleCall_2_0()); 
            match(input,RULE_ID,FOLLOW_2); 
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
    // InternalStreamingsparql.g:4248:1: rule__IRI__ValueAssignment_1 : ( RULE_IRI_TERMINAL ) ;
    public final void rule__IRI__ValueAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalStreamingsparql.g:4252:1: ( ( RULE_IRI_TERMINAL ) )
            // InternalStreamingsparql.g:4253:2: ( RULE_IRI_TERMINAL )
            {
            // InternalStreamingsparql.g:4253:2: ( RULE_IRI_TERMINAL )
            // InternalStreamingsparql.g:4254:3: RULE_IRI_TERMINAL
            {
             before(grammarAccess.getIRIAccess().getValueIRI_TERMINALTerminalRuleCall_1_0()); 
            match(input,RULE_IRI_TERMINAL,FOLLOW_2); 
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


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000008034000000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0004000000000010L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0004200000000010L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0004000000000012L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000006040000000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000001980000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000001800000000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000003FF0000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000180400000000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x00040000000000B0L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0001800000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0002000000000002L});

}