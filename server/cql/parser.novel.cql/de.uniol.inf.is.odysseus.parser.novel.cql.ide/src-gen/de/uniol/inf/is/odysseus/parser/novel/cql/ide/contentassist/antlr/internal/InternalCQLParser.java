package de.uniol.inf.is.odysseus.parser.novel.cql.ide.contentassist.antlr.internal;

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
import de.uniol.inf.is.odysseus.parser.novel.cql.services.CQLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCQLParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_INT", "RULE_FLOAT_NUMBER", "RULE_STRING", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'TRUE'", "'FALSE'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'*'", "'/'", "';'", "'.'", "'OR'", "'AND'", "'+'", "'-'", "'('", "')'", "'NOT'", "'DISTINCT'", "'FROM'", "','", "'WHERE'", "'SELECT'", "'CREATE'"
    };
    public static final int RULE_STRING=6;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__33=33;
    public static final int T__12=12;
    public static final int T__34=34;
    public static final int T__13=13;
    public static final int T__35=35;
    public static final int T__14=14;
    public static final int T__36=36;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_ID=7;
    public static final int RULE_WS=10;
    public static final int RULE_ANY_OTHER=11;
    public static final int RULE_FLOAT_NUMBER=5;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=4;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;

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

    	public void setGrammarAccess(CQLGrammarAccess grammarAccess) {
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



    // $ANTLR start "entryRuleModel"
    // InternalCQL.g:53:1: entryRuleModel : ruleModel EOF ;
    public final void entryRuleModel() throws RecognitionException {
        try {
            // InternalCQL.g:54:1: ( ruleModel EOF )
            // InternalCQL.g:55:1: ruleModel EOF
            {
             before(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            ruleModel();

            state._fsp--;

             after(grammarAccess.getModelRule()); 
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
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalCQL.g:62:1: ruleModel : ( ( rule__Model__StatementsAssignment )* ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:66:2: ( ( ( rule__Model__StatementsAssignment )* ) )
            // InternalCQL.g:67:2: ( ( rule__Model__StatementsAssignment )* )
            {
            // InternalCQL.g:67:2: ( ( rule__Model__StatementsAssignment )* )
            // InternalCQL.g:68:3: ( rule__Model__StatementsAssignment )*
            {
             before(grammarAccess.getModelAccess().getStatementsAssignment()); 
            // InternalCQL.g:69:3: ( rule__Model__StatementsAssignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=35 && LA1_0<=36)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCQL.g:69:4: rule__Model__StatementsAssignment
            	    {
            	    pushFollow(FOLLOW_3);
            	    rule__Model__StatementsAssignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             after(grammarAccess.getModelAccess().getStatementsAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleStatement"
    // InternalCQL.g:78:1: entryRuleStatement : ruleStatement EOF ;
    public final void entryRuleStatement() throws RecognitionException {
        try {
            // InternalCQL.g:79:1: ( ruleStatement EOF )
            // InternalCQL.g:80:1: ruleStatement EOF
            {
             before(grammarAccess.getStatementRule()); 
            pushFollow(FOLLOW_1);
            ruleStatement();

            state._fsp--;

             after(grammarAccess.getStatementRule()); 
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
    // $ANTLR end "entryRuleStatement"


    // $ANTLR start "ruleStatement"
    // InternalCQL.g:87:1: ruleStatement : ( ( rule__Statement__Group__0 ) ) ;
    public final void ruleStatement() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:91:2: ( ( ( rule__Statement__Group__0 ) ) )
            // InternalCQL.g:92:2: ( ( rule__Statement__Group__0 ) )
            {
            // InternalCQL.g:92:2: ( ( rule__Statement__Group__0 ) )
            // InternalCQL.g:93:3: ( rule__Statement__Group__0 )
            {
             before(grammarAccess.getStatementAccess().getGroup()); 
            // InternalCQL.g:94:3: ( rule__Statement__Group__0 )
            // InternalCQL.g:94:4: rule__Statement__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Statement__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getStatementAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleStatement"


    // $ANTLR start "entryRuleAtomic"
    // InternalCQL.g:103:1: entryRuleAtomic : ruleAtomic EOF ;
    public final void entryRuleAtomic() throws RecognitionException {
        try {
            // InternalCQL.g:104:1: ( ruleAtomic EOF )
            // InternalCQL.g:105:1: ruleAtomic EOF
            {
             before(grammarAccess.getAtomicRule()); 
            pushFollow(FOLLOW_1);
            ruleAtomic();

            state._fsp--;

             after(grammarAccess.getAtomicRule()); 
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
    // $ANTLR end "entryRuleAtomic"


    // $ANTLR start "ruleAtomic"
    // InternalCQL.g:112:1: ruleAtomic : ( ( rule__Atomic__Alternatives ) ) ;
    public final void ruleAtomic() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:116:2: ( ( ( rule__Atomic__Alternatives ) ) )
            // InternalCQL.g:117:2: ( ( rule__Atomic__Alternatives ) )
            {
            // InternalCQL.g:117:2: ( ( rule__Atomic__Alternatives ) )
            // InternalCQL.g:118:3: ( rule__Atomic__Alternatives )
            {
             before(grammarAccess.getAtomicAccess().getAlternatives()); 
            // InternalCQL.g:119:3: ( rule__Atomic__Alternatives )
            // InternalCQL.g:119:4: rule__Atomic__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getAtomicAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleAtomic"


    // $ANTLR start "entryRuleSource"
    // InternalCQL.g:128:1: entryRuleSource : ruleSource EOF ;
    public final void entryRuleSource() throws RecognitionException {
        try {
            // InternalCQL.g:129:1: ( ruleSource EOF )
            // InternalCQL.g:130:1: ruleSource EOF
            {
             before(grammarAccess.getSourceRule()); 
            pushFollow(FOLLOW_1);
            ruleSource();

            state._fsp--;

             after(grammarAccess.getSourceRule()); 
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
    // $ANTLR end "entryRuleSource"


    // $ANTLR start "ruleSource"
    // InternalCQL.g:137:1: ruleSource : ( ( rule__Source__NameAssignment ) ) ;
    public final void ruleSource() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:141:2: ( ( ( rule__Source__NameAssignment ) ) )
            // InternalCQL.g:142:2: ( ( rule__Source__NameAssignment ) )
            {
            // InternalCQL.g:142:2: ( ( rule__Source__NameAssignment ) )
            // InternalCQL.g:143:3: ( rule__Source__NameAssignment )
            {
             before(grammarAccess.getSourceAccess().getNameAssignment()); 
            // InternalCQL.g:144:3: ( rule__Source__NameAssignment )
            // InternalCQL.g:144:4: rule__Source__NameAssignment
            {
            pushFollow(FOLLOW_2);
            rule__Source__NameAssignment();

            state._fsp--;


            }

             after(grammarAccess.getSourceAccess().getNameAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleSource"


    // $ANTLR start "entryRuleAttribute"
    // InternalCQL.g:153:1: entryRuleAttribute : ruleAttribute EOF ;
    public final void entryRuleAttribute() throws RecognitionException {
        try {
            // InternalCQL.g:154:1: ( ruleAttribute EOF )
            // InternalCQL.g:155:1: ruleAttribute EOF
            {
             before(grammarAccess.getAttributeRule()); 
            pushFollow(FOLLOW_1);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getAttributeRule()); 
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
    // $ANTLR end "entryRuleAttribute"


    // $ANTLR start "ruleAttribute"
    // InternalCQL.g:162:1: ruleAttribute : ( ( rule__Attribute__Group__0 ) ) ;
    public final void ruleAttribute() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:166:2: ( ( ( rule__Attribute__Group__0 ) ) )
            // InternalCQL.g:167:2: ( ( rule__Attribute__Group__0 ) )
            {
            // InternalCQL.g:167:2: ( ( rule__Attribute__Group__0 ) )
            // InternalCQL.g:168:3: ( rule__Attribute__Group__0 )
            {
             before(grammarAccess.getAttributeAccess().getGroup()); 
            // InternalCQL.g:169:3: ( rule__Attribute__Group__0 )
            // InternalCQL.g:169:4: rule__Attribute__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Attribute__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getAttributeAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleAttribute"


    // $ANTLR start "entryRuleExpressionsModel"
    // InternalCQL.g:178:1: entryRuleExpressionsModel : ruleExpressionsModel EOF ;
    public final void entryRuleExpressionsModel() throws RecognitionException {
        try {
            // InternalCQL.g:179:1: ( ruleExpressionsModel EOF )
            // InternalCQL.g:180:1: ruleExpressionsModel EOF
            {
             before(grammarAccess.getExpressionsModelRule()); 
            pushFollow(FOLLOW_1);
            ruleExpressionsModel();

            state._fsp--;

             after(grammarAccess.getExpressionsModelRule()); 
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
    // $ANTLR end "entryRuleExpressionsModel"


    // $ANTLR start "ruleExpressionsModel"
    // InternalCQL.g:187:1: ruleExpressionsModel : ( ( rule__ExpressionsModel__Group__0 ) ) ;
    public final void ruleExpressionsModel() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:191:2: ( ( ( rule__ExpressionsModel__Group__0 ) ) )
            // InternalCQL.g:192:2: ( ( rule__ExpressionsModel__Group__0 ) )
            {
            // InternalCQL.g:192:2: ( ( rule__ExpressionsModel__Group__0 ) )
            // InternalCQL.g:193:3: ( rule__ExpressionsModel__Group__0 )
            {
             before(grammarAccess.getExpressionsModelAccess().getGroup()); 
            // InternalCQL.g:194:3: ( rule__ExpressionsModel__Group__0 )
            // InternalCQL.g:194:4: rule__ExpressionsModel__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__ExpressionsModel__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getExpressionsModelAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleExpressionsModel"


    // $ANTLR start "entryRuleExpression"
    // InternalCQL.g:203:1: entryRuleExpression : ruleExpression EOF ;
    public final void entryRuleExpression() throws RecognitionException {
        try {
            // InternalCQL.g:204:1: ( ruleExpression EOF )
            // InternalCQL.g:205:1: ruleExpression EOF
            {
             before(grammarAccess.getExpressionRule()); 
            pushFollow(FOLLOW_1);
            ruleExpression();

            state._fsp--;

             after(grammarAccess.getExpressionRule()); 
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
    // $ANTLR end "entryRuleExpression"


    // $ANTLR start "ruleExpression"
    // InternalCQL.g:212:1: ruleExpression : ( ruleOr ) ;
    public final void ruleExpression() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:216:2: ( ( ruleOr ) )
            // InternalCQL.g:217:2: ( ruleOr )
            {
            // InternalCQL.g:217:2: ( ruleOr )
            // InternalCQL.g:218:3: ruleOr
            {
             before(grammarAccess.getExpressionAccess().getOrParserRuleCall()); 
            pushFollow(FOLLOW_2);
            ruleOr();

            state._fsp--;

             after(grammarAccess.getExpressionAccess().getOrParserRuleCall()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleExpression"


    // $ANTLR start "entryRuleOr"
    // InternalCQL.g:228:1: entryRuleOr : ruleOr EOF ;
    public final void entryRuleOr() throws RecognitionException {
        try {
            // InternalCQL.g:229:1: ( ruleOr EOF )
            // InternalCQL.g:230:1: ruleOr EOF
            {
             before(grammarAccess.getOrRule()); 
            pushFollow(FOLLOW_1);
            ruleOr();

            state._fsp--;

             after(grammarAccess.getOrRule()); 
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
    // $ANTLR end "entryRuleOr"


    // $ANTLR start "ruleOr"
    // InternalCQL.g:237:1: ruleOr : ( ( rule__Or__Group__0 ) ) ;
    public final void ruleOr() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:241:2: ( ( ( rule__Or__Group__0 ) ) )
            // InternalCQL.g:242:2: ( ( rule__Or__Group__0 ) )
            {
            // InternalCQL.g:242:2: ( ( rule__Or__Group__0 ) )
            // InternalCQL.g:243:3: ( rule__Or__Group__0 )
            {
             before(grammarAccess.getOrAccess().getGroup()); 
            // InternalCQL.g:244:3: ( rule__Or__Group__0 )
            // InternalCQL.g:244:4: rule__Or__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Or__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getOrAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleOr"


    // $ANTLR start "entryRuleAnd"
    // InternalCQL.g:253:1: entryRuleAnd : ruleAnd EOF ;
    public final void entryRuleAnd() throws RecognitionException {
        try {
            // InternalCQL.g:254:1: ( ruleAnd EOF )
            // InternalCQL.g:255:1: ruleAnd EOF
            {
             before(grammarAccess.getAndRule()); 
            pushFollow(FOLLOW_1);
            ruleAnd();

            state._fsp--;

             after(grammarAccess.getAndRule()); 
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
    // $ANTLR end "entryRuleAnd"


    // $ANTLR start "ruleAnd"
    // InternalCQL.g:262:1: ruleAnd : ( ( rule__And__Group__0 ) ) ;
    public final void ruleAnd() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:266:2: ( ( ( rule__And__Group__0 ) ) )
            // InternalCQL.g:267:2: ( ( rule__And__Group__0 ) )
            {
            // InternalCQL.g:267:2: ( ( rule__And__Group__0 ) )
            // InternalCQL.g:268:3: ( rule__And__Group__0 )
            {
             before(grammarAccess.getAndAccess().getGroup()); 
            // InternalCQL.g:269:3: ( rule__And__Group__0 )
            // InternalCQL.g:269:4: rule__And__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__And__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getAndAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleAnd"


    // $ANTLR start "entryRuleEqualitiy"
    // InternalCQL.g:278:1: entryRuleEqualitiy : ruleEqualitiy EOF ;
    public final void entryRuleEqualitiy() throws RecognitionException {
        try {
            // InternalCQL.g:279:1: ( ruleEqualitiy EOF )
            // InternalCQL.g:280:1: ruleEqualitiy EOF
            {
             before(grammarAccess.getEqualitiyRule()); 
            pushFollow(FOLLOW_1);
            ruleEqualitiy();

            state._fsp--;

             after(grammarAccess.getEqualitiyRule()); 
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
    // $ANTLR end "entryRuleEqualitiy"


    // $ANTLR start "ruleEqualitiy"
    // InternalCQL.g:287:1: ruleEqualitiy : ( ( rule__Equalitiy__Group__0 ) ) ;
    public final void ruleEqualitiy() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:291:2: ( ( ( rule__Equalitiy__Group__0 ) ) )
            // InternalCQL.g:292:2: ( ( rule__Equalitiy__Group__0 ) )
            {
            // InternalCQL.g:292:2: ( ( rule__Equalitiy__Group__0 ) )
            // InternalCQL.g:293:3: ( rule__Equalitiy__Group__0 )
            {
             before(grammarAccess.getEqualitiyAccess().getGroup()); 
            // InternalCQL.g:294:3: ( rule__Equalitiy__Group__0 )
            // InternalCQL.g:294:4: rule__Equalitiy__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Equalitiy__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getEqualitiyAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleEqualitiy"


    // $ANTLR start "entryRuleComparison"
    // InternalCQL.g:303:1: entryRuleComparison : ruleComparison EOF ;
    public final void entryRuleComparison() throws RecognitionException {
        try {
            // InternalCQL.g:304:1: ( ruleComparison EOF )
            // InternalCQL.g:305:1: ruleComparison EOF
            {
             before(grammarAccess.getComparisonRule()); 
            pushFollow(FOLLOW_1);
            ruleComparison();

            state._fsp--;

             after(grammarAccess.getComparisonRule()); 
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
    // $ANTLR end "entryRuleComparison"


    // $ANTLR start "ruleComparison"
    // InternalCQL.g:312:1: ruleComparison : ( ( rule__Comparison__Group__0 ) ) ;
    public final void ruleComparison() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:316:2: ( ( ( rule__Comparison__Group__0 ) ) )
            // InternalCQL.g:317:2: ( ( rule__Comparison__Group__0 ) )
            {
            // InternalCQL.g:317:2: ( ( rule__Comparison__Group__0 ) )
            // InternalCQL.g:318:3: ( rule__Comparison__Group__0 )
            {
             before(grammarAccess.getComparisonAccess().getGroup()); 
            // InternalCQL.g:319:3: ( rule__Comparison__Group__0 )
            // InternalCQL.g:319:4: rule__Comparison__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Comparison__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getComparisonAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleComparison"


    // $ANTLR start "entryRulePlusOrMinus"
    // InternalCQL.g:328:1: entryRulePlusOrMinus : rulePlusOrMinus EOF ;
    public final void entryRulePlusOrMinus() throws RecognitionException {
        try {
            // InternalCQL.g:329:1: ( rulePlusOrMinus EOF )
            // InternalCQL.g:330:1: rulePlusOrMinus EOF
            {
             before(grammarAccess.getPlusOrMinusRule()); 
            pushFollow(FOLLOW_1);
            rulePlusOrMinus();

            state._fsp--;

             after(grammarAccess.getPlusOrMinusRule()); 
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
    // $ANTLR end "entryRulePlusOrMinus"


    // $ANTLR start "rulePlusOrMinus"
    // InternalCQL.g:337:1: rulePlusOrMinus : ( ( rule__PlusOrMinus__Group__0 ) ) ;
    public final void rulePlusOrMinus() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:341:2: ( ( ( rule__PlusOrMinus__Group__0 ) ) )
            // InternalCQL.g:342:2: ( ( rule__PlusOrMinus__Group__0 ) )
            {
            // InternalCQL.g:342:2: ( ( rule__PlusOrMinus__Group__0 ) )
            // InternalCQL.g:343:3: ( rule__PlusOrMinus__Group__0 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getGroup()); 
            // InternalCQL.g:344:3: ( rule__PlusOrMinus__Group__0 )
            // InternalCQL.g:344:4: rule__PlusOrMinus__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getPlusOrMinusAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rulePlusOrMinus"


    // $ANTLR start "entryRuleMulOrDiv"
    // InternalCQL.g:353:1: entryRuleMulOrDiv : ruleMulOrDiv EOF ;
    public final void entryRuleMulOrDiv() throws RecognitionException {
        try {
            // InternalCQL.g:354:1: ( ruleMulOrDiv EOF )
            // InternalCQL.g:355:1: ruleMulOrDiv EOF
            {
             before(grammarAccess.getMulOrDivRule()); 
            pushFollow(FOLLOW_1);
            ruleMulOrDiv();

            state._fsp--;

             after(grammarAccess.getMulOrDivRule()); 
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
    // $ANTLR end "entryRuleMulOrDiv"


    // $ANTLR start "ruleMulOrDiv"
    // InternalCQL.g:362:1: ruleMulOrDiv : ( ( rule__MulOrDiv__Group__0 ) ) ;
    public final void ruleMulOrDiv() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:366:2: ( ( ( rule__MulOrDiv__Group__0 ) ) )
            // InternalCQL.g:367:2: ( ( rule__MulOrDiv__Group__0 ) )
            {
            // InternalCQL.g:367:2: ( ( rule__MulOrDiv__Group__0 ) )
            // InternalCQL.g:368:3: ( rule__MulOrDiv__Group__0 )
            {
             before(grammarAccess.getMulOrDivAccess().getGroup()); 
            // InternalCQL.g:369:3: ( rule__MulOrDiv__Group__0 )
            // InternalCQL.g:369:4: rule__MulOrDiv__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__MulOrDiv__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getMulOrDivAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleMulOrDiv"


    // $ANTLR start "entryRulePrimary"
    // InternalCQL.g:378:1: entryRulePrimary : rulePrimary EOF ;
    public final void entryRulePrimary() throws RecognitionException {
        try {
            // InternalCQL.g:379:1: ( rulePrimary EOF )
            // InternalCQL.g:380:1: rulePrimary EOF
            {
             before(grammarAccess.getPrimaryRule()); 
            pushFollow(FOLLOW_1);
            rulePrimary();

            state._fsp--;

             after(grammarAccess.getPrimaryRule()); 
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
    // $ANTLR end "entryRulePrimary"


    // $ANTLR start "rulePrimary"
    // InternalCQL.g:387:1: rulePrimary : ( ( rule__Primary__Alternatives ) ) ;
    public final void rulePrimary() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:391:2: ( ( ( rule__Primary__Alternatives ) ) )
            // InternalCQL.g:392:2: ( ( rule__Primary__Alternatives ) )
            {
            // InternalCQL.g:392:2: ( ( rule__Primary__Alternatives ) )
            // InternalCQL.g:393:3: ( rule__Primary__Alternatives )
            {
             before(grammarAccess.getPrimaryAccess().getAlternatives()); 
            // InternalCQL.g:394:3: ( rule__Primary__Alternatives )
            // InternalCQL.g:394:4: rule__Primary__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__Primary__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getPrimaryAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rulePrimary"


    // $ANTLR start "entryRuleSelect_Statement"
    // InternalCQL.g:403:1: entryRuleSelect_Statement : ruleSelect_Statement EOF ;
    public final void entryRuleSelect_Statement() throws RecognitionException {
        try {
            // InternalCQL.g:404:1: ( ruleSelect_Statement EOF )
            // InternalCQL.g:405:1: ruleSelect_Statement EOF
            {
             before(grammarAccess.getSelect_StatementRule()); 
            pushFollow(FOLLOW_1);
            ruleSelect_Statement();

            state._fsp--;

             after(grammarAccess.getSelect_StatementRule()); 
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
    // $ANTLR end "entryRuleSelect_Statement"


    // $ANTLR start "ruleSelect_Statement"
    // InternalCQL.g:412:1: ruleSelect_Statement : ( ( rule__Select_Statement__Group__0 ) ) ;
    public final void ruleSelect_Statement() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:416:2: ( ( ( rule__Select_Statement__Group__0 ) ) )
            // InternalCQL.g:417:2: ( ( rule__Select_Statement__Group__0 ) )
            {
            // InternalCQL.g:417:2: ( ( rule__Select_Statement__Group__0 ) )
            // InternalCQL.g:418:3: ( rule__Select_Statement__Group__0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup()); 
            // InternalCQL.g:419:3: ( rule__Select_Statement__Group__0 )
            // InternalCQL.g:419:4: rule__Select_Statement__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleSelect_Statement"


    // $ANTLR start "entryRuleCreate_Statement"
    // InternalCQL.g:428:1: entryRuleCreate_Statement : ruleCreate_Statement EOF ;
    public final void entryRuleCreate_Statement() throws RecognitionException {
        try {
            // InternalCQL.g:429:1: ( ruleCreate_Statement EOF )
            // InternalCQL.g:430:1: ruleCreate_Statement EOF
            {
             before(grammarAccess.getCreate_StatementRule()); 
            pushFollow(FOLLOW_1);
            ruleCreate_Statement();

            state._fsp--;

             after(grammarAccess.getCreate_StatementRule()); 
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
    // $ANTLR end "entryRuleCreate_Statement"


    // $ANTLR start "ruleCreate_Statement"
    // InternalCQL.g:437:1: ruleCreate_Statement : ( ( rule__Create_Statement__NameAssignment ) ) ;
    public final void ruleCreate_Statement() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:441:2: ( ( ( rule__Create_Statement__NameAssignment ) ) )
            // InternalCQL.g:442:2: ( ( rule__Create_Statement__NameAssignment ) )
            {
            // InternalCQL.g:442:2: ( ( rule__Create_Statement__NameAssignment ) )
            // InternalCQL.g:443:3: ( rule__Create_Statement__NameAssignment )
            {
             before(grammarAccess.getCreate_StatementAccess().getNameAssignment()); 
            // InternalCQL.g:444:3: ( rule__Create_Statement__NameAssignment )
            // InternalCQL.g:444:4: rule__Create_Statement__NameAssignment
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__NameAssignment();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getNameAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleCreate_Statement"


    // $ANTLR start "rule__Statement__Alternatives_0"
    // InternalCQL.g:452:1: rule__Statement__Alternatives_0 : ( ( ( rule__Statement__TypeAssignment_0_0 ) ) | ( ( rule__Statement__TypeAssignment_0_1 ) ) );
    public final void rule__Statement__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:456:1: ( ( ( rule__Statement__TypeAssignment_0_0 ) ) | ( ( rule__Statement__TypeAssignment_0_1 ) ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==35) ) {
                alt2=1;
            }
            else if ( (LA2_0==36) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // InternalCQL.g:457:2: ( ( rule__Statement__TypeAssignment_0_0 ) )
                    {
                    // InternalCQL.g:457:2: ( ( rule__Statement__TypeAssignment_0_0 ) )
                    // InternalCQL.g:458:3: ( rule__Statement__TypeAssignment_0_0 )
                    {
                     before(grammarAccess.getStatementAccess().getTypeAssignment_0_0()); 
                    // InternalCQL.g:459:3: ( rule__Statement__TypeAssignment_0_0 )
                    // InternalCQL.g:459:4: rule__Statement__TypeAssignment_0_0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Statement__TypeAssignment_0_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getStatementAccess().getTypeAssignment_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:463:2: ( ( rule__Statement__TypeAssignment_0_1 ) )
                    {
                    // InternalCQL.g:463:2: ( ( rule__Statement__TypeAssignment_0_1 ) )
                    // InternalCQL.g:464:3: ( rule__Statement__TypeAssignment_0_1 )
                    {
                     before(grammarAccess.getStatementAccess().getTypeAssignment_0_1()); 
                    // InternalCQL.g:465:3: ( rule__Statement__TypeAssignment_0_1 )
                    // InternalCQL.g:465:4: rule__Statement__TypeAssignment_0_1
                    {
                    pushFollow(FOLLOW_2);
                    rule__Statement__TypeAssignment_0_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getStatementAccess().getTypeAssignment_0_1()); 

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
    // $ANTLR end "rule__Statement__Alternatives_0"


    // $ANTLR start "rule__Atomic__Alternatives"
    // InternalCQL.g:473:1: rule__Atomic__Alternatives : ( ( ( rule__Atomic__Group_0__0 ) ) | ( ( rule__Atomic__Group_1__0 ) ) | ( ( rule__Atomic__Group_2__0 ) ) | ( ( rule__Atomic__Group_3__0 ) ) | ( ( rule__Atomic__Group_4__0 ) ) );
    public final void rule__Atomic__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:477:1: ( ( ( rule__Atomic__Group_0__0 ) ) | ( ( rule__Atomic__Group_1__0 ) ) | ( ( rule__Atomic__Group_2__0 ) ) | ( ( rule__Atomic__Group_3__0 ) ) | ( ( rule__Atomic__Group_4__0 ) ) )
            int alt3=5;
            switch ( input.LA(1) ) {
            case RULE_INT:
                {
                alt3=1;
                }
                break;
            case RULE_FLOAT_NUMBER:
                {
                alt3=2;
                }
                break;
            case RULE_STRING:
                {
                alt3=3;
                }
                break;
            case 12:
            case 13:
                {
                alt3=4;
                }
                break;
            case RULE_ID:
                {
                alt3=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // InternalCQL.g:478:2: ( ( rule__Atomic__Group_0__0 ) )
                    {
                    // InternalCQL.g:478:2: ( ( rule__Atomic__Group_0__0 ) )
                    // InternalCQL.g:479:3: ( rule__Atomic__Group_0__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_0()); 
                    // InternalCQL.g:480:3: ( rule__Atomic__Group_0__0 )
                    // InternalCQL.g:480:4: rule__Atomic__Group_0__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Atomic__Group_0__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getAtomicAccess().getGroup_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:484:2: ( ( rule__Atomic__Group_1__0 ) )
                    {
                    // InternalCQL.g:484:2: ( ( rule__Atomic__Group_1__0 ) )
                    // InternalCQL.g:485:3: ( rule__Atomic__Group_1__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_1()); 
                    // InternalCQL.g:486:3: ( rule__Atomic__Group_1__0 )
                    // InternalCQL.g:486:4: rule__Atomic__Group_1__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Atomic__Group_1__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getAtomicAccess().getGroup_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:490:2: ( ( rule__Atomic__Group_2__0 ) )
                    {
                    // InternalCQL.g:490:2: ( ( rule__Atomic__Group_2__0 ) )
                    // InternalCQL.g:491:3: ( rule__Atomic__Group_2__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_2()); 
                    // InternalCQL.g:492:3: ( rule__Atomic__Group_2__0 )
                    // InternalCQL.g:492:4: rule__Atomic__Group_2__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Atomic__Group_2__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getAtomicAccess().getGroup_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:496:2: ( ( rule__Atomic__Group_3__0 ) )
                    {
                    // InternalCQL.g:496:2: ( ( rule__Atomic__Group_3__0 ) )
                    // InternalCQL.g:497:3: ( rule__Atomic__Group_3__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_3()); 
                    // InternalCQL.g:498:3: ( rule__Atomic__Group_3__0 )
                    // InternalCQL.g:498:4: rule__Atomic__Group_3__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Atomic__Group_3__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getAtomicAccess().getGroup_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalCQL.g:502:2: ( ( rule__Atomic__Group_4__0 ) )
                    {
                    // InternalCQL.g:502:2: ( ( rule__Atomic__Group_4__0 ) )
                    // InternalCQL.g:503:3: ( rule__Atomic__Group_4__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_4()); 
                    // InternalCQL.g:504:3: ( rule__Atomic__Group_4__0 )
                    // InternalCQL.g:504:4: rule__Atomic__Group_4__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Atomic__Group_4__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getAtomicAccess().getGroup_4()); 

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
    // $ANTLR end "rule__Atomic__Alternatives"


    // $ANTLR start "rule__Atomic__ValueAlternatives_3_1_0"
    // InternalCQL.g:512:1: rule__Atomic__ValueAlternatives_3_1_0 : ( ( 'TRUE' ) | ( 'FALSE' ) );
    public final void rule__Atomic__ValueAlternatives_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:516:1: ( ( 'TRUE' ) | ( 'FALSE' ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==12) ) {
                alt4=1;
            }
            else if ( (LA4_0==13) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // InternalCQL.g:517:2: ( 'TRUE' )
                    {
                    // InternalCQL.g:517:2: ( 'TRUE' )
                    // InternalCQL.g:518:3: 'TRUE'
                    {
                     before(grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0()); 
                    match(input,12,FOLLOW_2); 
                     after(grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:523:2: ( 'FALSE' )
                    {
                    // InternalCQL.g:523:2: ( 'FALSE' )
                    // InternalCQL.g:524:3: 'FALSE'
                    {
                     before(grammarAccess.getAtomicAccess().getValueFALSEKeyword_3_1_0_1()); 
                    match(input,13,FOLLOW_2); 
                     after(grammarAccess.getAtomicAccess().getValueFALSEKeyword_3_1_0_1()); 

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
    // $ANTLR end "rule__Atomic__ValueAlternatives_3_1_0"


    // $ANTLR start "rule__Equalitiy__OpAlternatives_1_1_0"
    // InternalCQL.g:533:1: rule__Equalitiy__OpAlternatives_1_1_0 : ( ( '==' ) | ( '!=' ) );
    public final void rule__Equalitiy__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:537:1: ( ( '==' ) | ( '!=' ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==14) ) {
                alt5=1;
            }
            else if ( (LA5_0==15) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalCQL.g:538:2: ( '==' )
                    {
                    // InternalCQL.g:538:2: ( '==' )
                    // InternalCQL.g:539:3: '=='
                    {
                     before(grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0()); 
                    match(input,14,FOLLOW_2); 
                     after(grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:544:2: ( '!=' )
                    {
                    // InternalCQL.g:544:2: ( '!=' )
                    // InternalCQL.g:545:3: '!='
                    {
                     before(grammarAccess.getEqualitiyAccess().getOpExclamationMarkEqualsSignKeyword_1_1_0_1()); 
                    match(input,15,FOLLOW_2); 
                     after(grammarAccess.getEqualitiyAccess().getOpExclamationMarkEqualsSignKeyword_1_1_0_1()); 

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
    // $ANTLR end "rule__Equalitiy__OpAlternatives_1_1_0"


    // $ANTLR start "rule__Comparison__OpAlternatives_1_1_0"
    // InternalCQL.g:554:1: rule__Comparison__OpAlternatives_1_1_0 : ( ( '>=' ) | ( '<=' ) | ( '<' ) | ( '>' ) );
    public final void rule__Comparison__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:558:1: ( ( '>=' ) | ( '<=' ) | ( '<' ) | ( '>' ) )
            int alt6=4;
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
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // InternalCQL.g:559:2: ( '>=' )
                    {
                    // InternalCQL.g:559:2: ( '>=' )
                    // InternalCQL.g:560:3: '>='
                    {
                     before(grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0()); 
                    match(input,16,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:565:2: ( '<=' )
                    {
                    // InternalCQL.g:565:2: ( '<=' )
                    // InternalCQL.g:566:3: '<='
                    {
                     before(grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1()); 
                    match(input,17,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:571:2: ( '<' )
                    {
                    // InternalCQL.g:571:2: ( '<' )
                    // InternalCQL.g:572:3: '<'
                    {
                     before(grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2()); 
                    match(input,18,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:577:2: ( '>' )
                    {
                    // InternalCQL.g:577:2: ( '>' )
                    // InternalCQL.g:578:3: '>'
                    {
                     before(grammarAccess.getComparisonAccess().getOpGreaterThanSignKeyword_1_1_0_3()); 
                    match(input,19,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpGreaterThanSignKeyword_1_1_0_3()); 

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
    // $ANTLR end "rule__Comparison__OpAlternatives_1_1_0"


    // $ANTLR start "rule__PlusOrMinus__Alternatives_1_0"
    // InternalCQL.g:587:1: rule__PlusOrMinus__Alternatives_1_0 : ( ( ( rule__PlusOrMinus__Group_1_0_0__0 ) ) | ( ( rule__PlusOrMinus__Group_1_0_1__0 ) ) );
    public final void rule__PlusOrMinus__Alternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:591:1: ( ( ( rule__PlusOrMinus__Group_1_0_0__0 ) ) | ( ( rule__PlusOrMinus__Group_1_0_1__0 ) ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==26) ) {
                alt7=1;
            }
            else if ( (LA7_0==27) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // InternalCQL.g:592:2: ( ( rule__PlusOrMinus__Group_1_0_0__0 ) )
                    {
                    // InternalCQL.g:592:2: ( ( rule__PlusOrMinus__Group_1_0_0__0 ) )
                    // InternalCQL.g:593:3: ( rule__PlusOrMinus__Group_1_0_0__0 )
                    {
                     before(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_0()); 
                    // InternalCQL.g:594:3: ( rule__PlusOrMinus__Group_1_0_0__0 )
                    // InternalCQL.g:594:4: rule__PlusOrMinus__Group_1_0_0__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__PlusOrMinus__Group_1_0_0__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:598:2: ( ( rule__PlusOrMinus__Group_1_0_1__0 ) )
                    {
                    // InternalCQL.g:598:2: ( ( rule__PlusOrMinus__Group_1_0_1__0 ) )
                    // InternalCQL.g:599:3: ( rule__PlusOrMinus__Group_1_0_1__0 )
                    {
                     before(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_1()); 
                    // InternalCQL.g:600:3: ( rule__PlusOrMinus__Group_1_0_1__0 )
                    // InternalCQL.g:600:4: rule__PlusOrMinus__Group_1_0_1__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__PlusOrMinus__Group_1_0_1__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_1()); 

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
    // $ANTLR end "rule__PlusOrMinus__Alternatives_1_0"


    // $ANTLR start "rule__MulOrDiv__OpAlternatives_1_1_0"
    // InternalCQL.g:608:1: rule__MulOrDiv__OpAlternatives_1_1_0 : ( ( '*' ) | ( '/' ) );
    public final void rule__MulOrDiv__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:612:1: ( ( '*' ) | ( '/' ) )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==20) ) {
                alt8=1;
            }
            else if ( (LA8_0==21) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // InternalCQL.g:613:2: ( '*' )
                    {
                    // InternalCQL.g:613:2: ( '*' )
                    // InternalCQL.g:614:3: '*'
                    {
                     before(grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0()); 
                    match(input,20,FOLLOW_2); 
                     after(grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:619:2: ( '/' )
                    {
                    // InternalCQL.g:619:2: ( '/' )
                    // InternalCQL.g:620:3: '/'
                    {
                     before(grammarAccess.getMulOrDivAccess().getOpSolidusKeyword_1_1_0_1()); 
                    match(input,21,FOLLOW_2); 
                     after(grammarAccess.getMulOrDivAccess().getOpSolidusKeyword_1_1_0_1()); 

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
    // $ANTLR end "rule__MulOrDiv__OpAlternatives_1_1_0"


    // $ANTLR start "rule__Primary__Alternatives"
    // InternalCQL.g:629:1: rule__Primary__Alternatives : ( ( ( rule__Primary__Group_0__0 ) ) | ( ( rule__Primary__Group_1__0 ) ) | ( ruleAtomic ) );
    public final void rule__Primary__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:633:1: ( ( ( rule__Primary__Group_0__0 ) ) | ( ( rule__Primary__Group_1__0 ) ) | ( ruleAtomic ) )
            int alt9=3;
            switch ( input.LA(1) ) {
            case 28:
                {
                alt9=1;
                }
                break;
            case 30:
                {
                alt9=2;
                }
                break;
            case RULE_INT:
            case RULE_FLOAT_NUMBER:
            case RULE_STRING:
            case RULE_ID:
            case 12:
            case 13:
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
                    // InternalCQL.g:634:2: ( ( rule__Primary__Group_0__0 ) )
                    {
                    // InternalCQL.g:634:2: ( ( rule__Primary__Group_0__0 ) )
                    // InternalCQL.g:635:3: ( rule__Primary__Group_0__0 )
                    {
                     before(grammarAccess.getPrimaryAccess().getGroup_0()); 
                    // InternalCQL.g:636:3: ( rule__Primary__Group_0__0 )
                    // InternalCQL.g:636:4: rule__Primary__Group_0__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Primary__Group_0__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getPrimaryAccess().getGroup_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:640:2: ( ( rule__Primary__Group_1__0 ) )
                    {
                    // InternalCQL.g:640:2: ( ( rule__Primary__Group_1__0 ) )
                    // InternalCQL.g:641:3: ( rule__Primary__Group_1__0 )
                    {
                     before(grammarAccess.getPrimaryAccess().getGroup_1()); 
                    // InternalCQL.g:642:3: ( rule__Primary__Group_1__0 )
                    // InternalCQL.g:642:4: rule__Primary__Group_1__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Primary__Group_1__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getPrimaryAccess().getGroup_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:646:2: ( ruleAtomic )
                    {
                    // InternalCQL.g:646:2: ( ruleAtomic )
                    // InternalCQL.g:647:3: ruleAtomic
                    {
                     before(grammarAccess.getPrimaryAccess().getAtomicParserRuleCall_2()); 
                    pushFollow(FOLLOW_2);
                    ruleAtomic();

                    state._fsp--;

                     after(grammarAccess.getPrimaryAccess().getAtomicParserRuleCall_2()); 

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
    // $ANTLR end "rule__Primary__Alternatives"


    // $ANTLR start "rule__Statement__Group__0"
    // InternalCQL.g:656:1: rule__Statement__Group__0 : rule__Statement__Group__0__Impl rule__Statement__Group__1 ;
    public final void rule__Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:660:1: ( rule__Statement__Group__0__Impl rule__Statement__Group__1 )
            // InternalCQL.g:661:2: rule__Statement__Group__0__Impl rule__Statement__Group__1
            {
            pushFollow(FOLLOW_4);
            rule__Statement__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Statement__Group__1();

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
    // $ANTLR end "rule__Statement__Group__0"


    // $ANTLR start "rule__Statement__Group__0__Impl"
    // InternalCQL.g:668:1: rule__Statement__Group__0__Impl : ( ( rule__Statement__Alternatives_0 ) ) ;
    public final void rule__Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:672:1: ( ( ( rule__Statement__Alternatives_0 ) ) )
            // InternalCQL.g:673:1: ( ( rule__Statement__Alternatives_0 ) )
            {
            // InternalCQL.g:673:1: ( ( rule__Statement__Alternatives_0 ) )
            // InternalCQL.g:674:2: ( rule__Statement__Alternatives_0 )
            {
             before(grammarAccess.getStatementAccess().getAlternatives_0()); 
            // InternalCQL.g:675:2: ( rule__Statement__Alternatives_0 )
            // InternalCQL.g:675:3: rule__Statement__Alternatives_0
            {
            pushFollow(FOLLOW_2);
            rule__Statement__Alternatives_0();

            state._fsp--;


            }

             after(grammarAccess.getStatementAccess().getAlternatives_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Statement__Group__0__Impl"


    // $ANTLR start "rule__Statement__Group__1"
    // InternalCQL.g:683:1: rule__Statement__Group__1 : rule__Statement__Group__1__Impl ;
    public final void rule__Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:687:1: ( rule__Statement__Group__1__Impl )
            // InternalCQL.g:688:2: rule__Statement__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Statement__Group__1__Impl();

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
    // $ANTLR end "rule__Statement__Group__1"


    // $ANTLR start "rule__Statement__Group__1__Impl"
    // InternalCQL.g:694:1: rule__Statement__Group__1__Impl : ( ( ';' )? ) ;
    public final void rule__Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:698:1: ( ( ( ';' )? ) )
            // InternalCQL.g:699:1: ( ( ';' )? )
            {
            // InternalCQL.g:699:1: ( ( ';' )? )
            // InternalCQL.g:700:2: ( ';' )?
            {
             before(grammarAccess.getStatementAccess().getSemicolonKeyword_1()); 
            // InternalCQL.g:701:2: ( ';' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==22) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCQL.g:701:3: ';'
                    {
                    match(input,22,FOLLOW_2); 

                    }
                    break;

            }

             after(grammarAccess.getStatementAccess().getSemicolonKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Statement__Group__1__Impl"


    // $ANTLR start "rule__Atomic__Group_0__0"
    // InternalCQL.g:710:1: rule__Atomic__Group_0__0 : rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 ;
    public final void rule__Atomic__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:714:1: ( rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 )
            // InternalCQL.g:715:2: rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1
            {
            pushFollow(FOLLOW_5);
            rule__Atomic__Group_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Atomic__Group_0__1();

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
    // $ANTLR end "rule__Atomic__Group_0__0"


    // $ANTLR start "rule__Atomic__Group_0__0__Impl"
    // InternalCQL.g:722:1: rule__Atomic__Group_0__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:726:1: ( ( () ) )
            // InternalCQL.g:727:1: ( () )
            {
            // InternalCQL.g:727:1: ( () )
            // InternalCQL.g:728:2: ()
            {
             before(grammarAccess.getAtomicAccess().getIntConstantAction_0_0()); 
            // InternalCQL.g:729:2: ()
            // InternalCQL.g:729:3: 
            {
            }

             after(grammarAccess.getAtomicAccess().getIntConstantAction_0_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_0__0__Impl"


    // $ANTLR start "rule__Atomic__Group_0__1"
    // InternalCQL.g:737:1: rule__Atomic__Group_0__1 : rule__Atomic__Group_0__1__Impl ;
    public final void rule__Atomic__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:741:1: ( rule__Atomic__Group_0__1__Impl )
            // InternalCQL.g:742:2: rule__Atomic__Group_0__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__Group_0__1__Impl();

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
    // $ANTLR end "rule__Atomic__Group_0__1"


    // $ANTLR start "rule__Atomic__Group_0__1__Impl"
    // InternalCQL.g:748:1: rule__Atomic__Group_0__1__Impl : ( ( rule__Atomic__ValueAssignment_0_1 ) ) ;
    public final void rule__Atomic__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:752:1: ( ( ( rule__Atomic__ValueAssignment_0_1 ) ) )
            // InternalCQL.g:753:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            {
            // InternalCQL.g:753:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            // InternalCQL.g:754:2: ( rule__Atomic__ValueAssignment_0_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_0_1()); 
            // InternalCQL.g:755:2: ( rule__Atomic__ValueAssignment_0_1 )
            // InternalCQL.g:755:3: rule__Atomic__ValueAssignment_0_1
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__ValueAssignment_0_1();

            state._fsp--;


            }

             after(grammarAccess.getAtomicAccess().getValueAssignment_0_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_0__1__Impl"


    // $ANTLR start "rule__Atomic__Group_1__0"
    // InternalCQL.g:764:1: rule__Atomic__Group_1__0 : rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 ;
    public final void rule__Atomic__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:768:1: ( rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 )
            // InternalCQL.g:769:2: rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1
            {
            pushFollow(FOLLOW_6);
            rule__Atomic__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Atomic__Group_1__1();

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
    // $ANTLR end "rule__Atomic__Group_1__0"


    // $ANTLR start "rule__Atomic__Group_1__0__Impl"
    // InternalCQL.g:776:1: rule__Atomic__Group_1__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:780:1: ( ( () ) )
            // InternalCQL.g:781:1: ( () )
            {
            // InternalCQL.g:781:1: ( () )
            // InternalCQL.g:782:2: ()
            {
             before(grammarAccess.getAtomicAccess().getFloatConstantAction_1_0()); 
            // InternalCQL.g:783:2: ()
            // InternalCQL.g:783:3: 
            {
            }

             after(grammarAccess.getAtomicAccess().getFloatConstantAction_1_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_1__0__Impl"


    // $ANTLR start "rule__Atomic__Group_1__1"
    // InternalCQL.g:791:1: rule__Atomic__Group_1__1 : rule__Atomic__Group_1__1__Impl ;
    public final void rule__Atomic__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:795:1: ( rule__Atomic__Group_1__1__Impl )
            // InternalCQL.g:796:2: rule__Atomic__Group_1__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__Group_1__1__Impl();

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
    // $ANTLR end "rule__Atomic__Group_1__1"


    // $ANTLR start "rule__Atomic__Group_1__1__Impl"
    // InternalCQL.g:802:1: rule__Atomic__Group_1__1__Impl : ( ( rule__Atomic__ValueAssignment_1_1 ) ) ;
    public final void rule__Atomic__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:806:1: ( ( ( rule__Atomic__ValueAssignment_1_1 ) ) )
            // InternalCQL.g:807:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            {
            // InternalCQL.g:807:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            // InternalCQL.g:808:2: ( rule__Atomic__ValueAssignment_1_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_1_1()); 
            // InternalCQL.g:809:2: ( rule__Atomic__ValueAssignment_1_1 )
            // InternalCQL.g:809:3: rule__Atomic__ValueAssignment_1_1
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__ValueAssignment_1_1();

            state._fsp--;


            }

             after(grammarAccess.getAtomicAccess().getValueAssignment_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_1__1__Impl"


    // $ANTLR start "rule__Atomic__Group_2__0"
    // InternalCQL.g:818:1: rule__Atomic__Group_2__0 : rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 ;
    public final void rule__Atomic__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:822:1: ( rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 )
            // InternalCQL.g:823:2: rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1
            {
            pushFollow(FOLLOW_7);
            rule__Atomic__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Atomic__Group_2__1();

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
    // $ANTLR end "rule__Atomic__Group_2__0"


    // $ANTLR start "rule__Atomic__Group_2__0__Impl"
    // InternalCQL.g:830:1: rule__Atomic__Group_2__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:834:1: ( ( () ) )
            // InternalCQL.g:835:1: ( () )
            {
            // InternalCQL.g:835:1: ( () )
            // InternalCQL.g:836:2: ()
            {
             before(grammarAccess.getAtomicAccess().getStringConstantAction_2_0()); 
            // InternalCQL.g:837:2: ()
            // InternalCQL.g:837:3: 
            {
            }

             after(grammarAccess.getAtomicAccess().getStringConstantAction_2_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_2__0__Impl"


    // $ANTLR start "rule__Atomic__Group_2__1"
    // InternalCQL.g:845:1: rule__Atomic__Group_2__1 : rule__Atomic__Group_2__1__Impl ;
    public final void rule__Atomic__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:849:1: ( rule__Atomic__Group_2__1__Impl )
            // InternalCQL.g:850:2: rule__Atomic__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__Group_2__1__Impl();

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
    // $ANTLR end "rule__Atomic__Group_2__1"


    // $ANTLR start "rule__Atomic__Group_2__1__Impl"
    // InternalCQL.g:856:1: rule__Atomic__Group_2__1__Impl : ( ( rule__Atomic__ValueAssignment_2_1 ) ) ;
    public final void rule__Atomic__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:860:1: ( ( ( rule__Atomic__ValueAssignment_2_1 ) ) )
            // InternalCQL.g:861:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            {
            // InternalCQL.g:861:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            // InternalCQL.g:862:2: ( rule__Atomic__ValueAssignment_2_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_2_1()); 
            // InternalCQL.g:863:2: ( rule__Atomic__ValueAssignment_2_1 )
            // InternalCQL.g:863:3: rule__Atomic__ValueAssignment_2_1
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__ValueAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getAtomicAccess().getValueAssignment_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_2__1__Impl"


    // $ANTLR start "rule__Atomic__Group_3__0"
    // InternalCQL.g:872:1: rule__Atomic__Group_3__0 : rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 ;
    public final void rule__Atomic__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:876:1: ( rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 )
            // InternalCQL.g:877:2: rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1
            {
            pushFollow(FOLLOW_8);
            rule__Atomic__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Atomic__Group_3__1();

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
    // $ANTLR end "rule__Atomic__Group_3__0"


    // $ANTLR start "rule__Atomic__Group_3__0__Impl"
    // InternalCQL.g:884:1: rule__Atomic__Group_3__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:888:1: ( ( () ) )
            // InternalCQL.g:889:1: ( () )
            {
            // InternalCQL.g:889:1: ( () )
            // InternalCQL.g:890:2: ()
            {
             before(grammarAccess.getAtomicAccess().getBoolConstantAction_3_0()); 
            // InternalCQL.g:891:2: ()
            // InternalCQL.g:891:3: 
            {
            }

             after(grammarAccess.getAtomicAccess().getBoolConstantAction_3_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_3__0__Impl"


    // $ANTLR start "rule__Atomic__Group_3__1"
    // InternalCQL.g:899:1: rule__Atomic__Group_3__1 : rule__Atomic__Group_3__1__Impl ;
    public final void rule__Atomic__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:903:1: ( rule__Atomic__Group_3__1__Impl )
            // InternalCQL.g:904:2: rule__Atomic__Group_3__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__Group_3__1__Impl();

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
    // $ANTLR end "rule__Atomic__Group_3__1"


    // $ANTLR start "rule__Atomic__Group_3__1__Impl"
    // InternalCQL.g:910:1: rule__Atomic__Group_3__1__Impl : ( ( rule__Atomic__ValueAssignment_3_1 ) ) ;
    public final void rule__Atomic__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:914:1: ( ( ( rule__Atomic__ValueAssignment_3_1 ) ) )
            // InternalCQL.g:915:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            {
            // InternalCQL.g:915:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            // InternalCQL.g:916:2: ( rule__Atomic__ValueAssignment_3_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_3_1()); 
            // InternalCQL.g:917:2: ( rule__Atomic__ValueAssignment_3_1 )
            // InternalCQL.g:917:3: rule__Atomic__ValueAssignment_3_1
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__ValueAssignment_3_1();

            state._fsp--;


            }

             after(grammarAccess.getAtomicAccess().getValueAssignment_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_3__1__Impl"


    // $ANTLR start "rule__Atomic__Group_4__0"
    // InternalCQL.g:926:1: rule__Atomic__Group_4__0 : rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 ;
    public final void rule__Atomic__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:930:1: ( rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 )
            // InternalCQL.g:931:2: rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1
            {
            pushFollow(FOLLOW_9);
            rule__Atomic__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Atomic__Group_4__1();

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
    // $ANTLR end "rule__Atomic__Group_4__0"


    // $ANTLR start "rule__Atomic__Group_4__0__Impl"
    // InternalCQL.g:938:1: rule__Atomic__Group_4__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:942:1: ( ( () ) )
            // InternalCQL.g:943:1: ( () )
            {
            // InternalCQL.g:943:1: ( () )
            // InternalCQL.g:944:2: ()
            {
             before(grammarAccess.getAtomicAccess().getAttributeAction_4_0()); 
            // InternalCQL.g:945:2: ()
            // InternalCQL.g:945:3: 
            {
            }

             after(grammarAccess.getAtomicAccess().getAttributeAction_4_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_4__0__Impl"


    // $ANTLR start "rule__Atomic__Group_4__1"
    // InternalCQL.g:953:1: rule__Atomic__Group_4__1 : rule__Atomic__Group_4__1__Impl ;
    public final void rule__Atomic__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:957:1: ( rule__Atomic__Group_4__1__Impl )
            // InternalCQL.g:958:2: rule__Atomic__Group_4__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__Group_4__1__Impl();

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
    // $ANTLR end "rule__Atomic__Group_4__1"


    // $ANTLR start "rule__Atomic__Group_4__1__Impl"
    // InternalCQL.g:964:1: rule__Atomic__Group_4__1__Impl : ( ( rule__Atomic__ValueAssignment_4_1 ) ) ;
    public final void rule__Atomic__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:968:1: ( ( ( rule__Atomic__ValueAssignment_4_1 ) ) )
            // InternalCQL.g:969:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            {
            // InternalCQL.g:969:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            // InternalCQL.g:970:2: ( rule__Atomic__ValueAssignment_4_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_4_1()); 
            // InternalCQL.g:971:2: ( rule__Atomic__ValueAssignment_4_1 )
            // InternalCQL.g:971:3: rule__Atomic__ValueAssignment_4_1
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__ValueAssignment_4_1();

            state._fsp--;


            }

             after(grammarAccess.getAtomicAccess().getValueAssignment_4_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__Group_4__1__Impl"


    // $ANTLR start "rule__Attribute__Group__0"
    // InternalCQL.g:980:1: rule__Attribute__Group__0 : rule__Attribute__Group__0__Impl rule__Attribute__Group__1 ;
    public final void rule__Attribute__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:984:1: ( rule__Attribute__Group__0__Impl rule__Attribute__Group__1 )
            // InternalCQL.g:985:2: rule__Attribute__Group__0__Impl rule__Attribute__Group__1
            {
            pushFollow(FOLLOW_9);
            rule__Attribute__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Attribute__Group__1();

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
    // $ANTLR end "rule__Attribute__Group__0"


    // $ANTLR start "rule__Attribute__Group__0__Impl"
    // InternalCQL.g:992:1: rule__Attribute__Group__0__Impl : ( ( rule__Attribute__Group_0__0 )? ) ;
    public final void rule__Attribute__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:996:1: ( ( ( rule__Attribute__Group_0__0 )? ) )
            // InternalCQL.g:997:1: ( ( rule__Attribute__Group_0__0 )? )
            {
            // InternalCQL.g:997:1: ( ( rule__Attribute__Group_0__0 )? )
            // InternalCQL.g:998:2: ( rule__Attribute__Group_0__0 )?
            {
             before(grammarAccess.getAttributeAccess().getGroup_0()); 
            // InternalCQL.g:999:2: ( rule__Attribute__Group_0__0 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==RULE_ID) ) {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==23) ) {
                    alt11=1;
                }
            }
            switch (alt11) {
                case 1 :
                    // InternalCQL.g:999:3: rule__Attribute__Group_0__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Attribute__Group_0__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getAttributeAccess().getGroup_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Attribute__Group__0__Impl"


    // $ANTLR start "rule__Attribute__Group__1"
    // InternalCQL.g:1007:1: rule__Attribute__Group__1 : rule__Attribute__Group__1__Impl ;
    public final void rule__Attribute__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1011:1: ( rule__Attribute__Group__1__Impl )
            // InternalCQL.g:1012:2: rule__Attribute__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Attribute__Group__1__Impl();

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
    // $ANTLR end "rule__Attribute__Group__1"


    // $ANTLR start "rule__Attribute__Group__1__Impl"
    // InternalCQL.g:1018:1: rule__Attribute__Group__1__Impl : ( ( rule__Attribute__NameAssignment_1 ) ) ;
    public final void rule__Attribute__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1022:1: ( ( ( rule__Attribute__NameAssignment_1 ) ) )
            // InternalCQL.g:1023:1: ( ( rule__Attribute__NameAssignment_1 ) )
            {
            // InternalCQL.g:1023:1: ( ( rule__Attribute__NameAssignment_1 ) )
            // InternalCQL.g:1024:2: ( rule__Attribute__NameAssignment_1 )
            {
             before(grammarAccess.getAttributeAccess().getNameAssignment_1()); 
            // InternalCQL.g:1025:2: ( rule__Attribute__NameAssignment_1 )
            // InternalCQL.g:1025:3: rule__Attribute__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Attribute__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getAttributeAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Attribute__Group__1__Impl"


    // $ANTLR start "rule__Attribute__Group_0__0"
    // InternalCQL.g:1034:1: rule__Attribute__Group_0__0 : rule__Attribute__Group_0__0__Impl rule__Attribute__Group_0__1 ;
    public final void rule__Attribute__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1038:1: ( rule__Attribute__Group_0__0__Impl rule__Attribute__Group_0__1 )
            // InternalCQL.g:1039:2: rule__Attribute__Group_0__0__Impl rule__Attribute__Group_0__1
            {
            pushFollow(FOLLOW_10);
            rule__Attribute__Group_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Attribute__Group_0__1();

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
    // $ANTLR end "rule__Attribute__Group_0__0"


    // $ANTLR start "rule__Attribute__Group_0__0__Impl"
    // InternalCQL.g:1046:1: rule__Attribute__Group_0__0__Impl : ( ( rule__Attribute__SourceAssignment_0_0 ) ) ;
    public final void rule__Attribute__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1050:1: ( ( ( rule__Attribute__SourceAssignment_0_0 ) ) )
            // InternalCQL.g:1051:1: ( ( rule__Attribute__SourceAssignment_0_0 ) )
            {
            // InternalCQL.g:1051:1: ( ( rule__Attribute__SourceAssignment_0_0 ) )
            // InternalCQL.g:1052:2: ( rule__Attribute__SourceAssignment_0_0 )
            {
             before(grammarAccess.getAttributeAccess().getSourceAssignment_0_0()); 
            // InternalCQL.g:1053:2: ( rule__Attribute__SourceAssignment_0_0 )
            // InternalCQL.g:1053:3: rule__Attribute__SourceAssignment_0_0
            {
            pushFollow(FOLLOW_2);
            rule__Attribute__SourceAssignment_0_0();

            state._fsp--;


            }

             after(grammarAccess.getAttributeAccess().getSourceAssignment_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Attribute__Group_0__0__Impl"


    // $ANTLR start "rule__Attribute__Group_0__1"
    // InternalCQL.g:1061:1: rule__Attribute__Group_0__1 : rule__Attribute__Group_0__1__Impl ;
    public final void rule__Attribute__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1065:1: ( rule__Attribute__Group_0__1__Impl )
            // InternalCQL.g:1066:2: rule__Attribute__Group_0__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Attribute__Group_0__1__Impl();

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
    // $ANTLR end "rule__Attribute__Group_0__1"


    // $ANTLR start "rule__Attribute__Group_0__1__Impl"
    // InternalCQL.g:1072:1: rule__Attribute__Group_0__1__Impl : ( '.' ) ;
    public final void rule__Attribute__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1076:1: ( ( '.' ) )
            // InternalCQL.g:1077:1: ( '.' )
            {
            // InternalCQL.g:1077:1: ( '.' )
            // InternalCQL.g:1078:2: '.'
            {
             before(grammarAccess.getAttributeAccess().getFullStopKeyword_0_1()); 
            match(input,23,FOLLOW_2); 
             after(grammarAccess.getAttributeAccess().getFullStopKeyword_0_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Attribute__Group_0__1__Impl"


    // $ANTLR start "rule__ExpressionsModel__Group__0"
    // InternalCQL.g:1088:1: rule__ExpressionsModel__Group__0 : rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 ;
    public final void rule__ExpressionsModel__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1092:1: ( rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 )
            // InternalCQL.g:1093:2: rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1
            {
            pushFollow(FOLLOW_11);
            rule__ExpressionsModel__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__ExpressionsModel__Group__1();

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
    // $ANTLR end "rule__ExpressionsModel__Group__0"


    // $ANTLR start "rule__ExpressionsModel__Group__0__Impl"
    // InternalCQL.g:1100:1: rule__ExpressionsModel__Group__0__Impl : ( () ) ;
    public final void rule__ExpressionsModel__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1104:1: ( ( () ) )
            // InternalCQL.g:1105:1: ( () )
            {
            // InternalCQL.g:1105:1: ( () )
            // InternalCQL.g:1106:2: ()
            {
             before(grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0()); 
            // InternalCQL.g:1107:2: ()
            // InternalCQL.g:1107:3: 
            {
            }

             after(grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ExpressionsModel__Group__0__Impl"


    // $ANTLR start "rule__ExpressionsModel__Group__1"
    // InternalCQL.g:1115:1: rule__ExpressionsModel__Group__1 : rule__ExpressionsModel__Group__1__Impl ;
    public final void rule__ExpressionsModel__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1119:1: ( rule__ExpressionsModel__Group__1__Impl )
            // InternalCQL.g:1120:2: rule__ExpressionsModel__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__ExpressionsModel__Group__1__Impl();

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
    // $ANTLR end "rule__ExpressionsModel__Group__1"


    // $ANTLR start "rule__ExpressionsModel__Group__1__Impl"
    // InternalCQL.g:1126:1: rule__ExpressionsModel__Group__1__Impl : ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) ;
    public final void rule__ExpressionsModel__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1130:1: ( ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) )
            // InternalCQL.g:1131:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            {
            // InternalCQL.g:1131:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            // InternalCQL.g:1132:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            {
            // InternalCQL.g:1132:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) )
            // InternalCQL.g:1133:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1134:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            // InternalCQL.g:1134:4: rule__ExpressionsModel__ElementsAssignment_1
            {
            pushFollow(FOLLOW_12);
            rule__ExpressionsModel__ElementsAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 

            }

            // InternalCQL.g:1137:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            // InternalCQL.g:1138:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1139:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=RULE_INT && LA12_0<=RULE_ID)||(LA12_0>=12 && LA12_0<=13)||LA12_0==28||LA12_0==30) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalCQL.g:1139:4: rule__ExpressionsModel__ElementsAssignment_1
            	    {
            	    pushFollow(FOLLOW_12);
            	    rule__ExpressionsModel__ElementsAssignment_1();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

             after(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 

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
    // $ANTLR end "rule__ExpressionsModel__Group__1__Impl"


    // $ANTLR start "rule__Or__Group__0"
    // InternalCQL.g:1149:1: rule__Or__Group__0 : rule__Or__Group__0__Impl rule__Or__Group__1 ;
    public final void rule__Or__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1153:1: ( rule__Or__Group__0__Impl rule__Or__Group__1 )
            // InternalCQL.g:1154:2: rule__Or__Group__0__Impl rule__Or__Group__1
            {
            pushFollow(FOLLOW_13);
            rule__Or__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Or__Group__1();

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
    // $ANTLR end "rule__Or__Group__0"


    // $ANTLR start "rule__Or__Group__0__Impl"
    // InternalCQL.g:1161:1: rule__Or__Group__0__Impl : ( ruleAnd ) ;
    public final void rule__Or__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1165:1: ( ( ruleAnd ) )
            // InternalCQL.g:1166:1: ( ruleAnd )
            {
            // InternalCQL.g:1166:1: ( ruleAnd )
            // InternalCQL.g:1167:2: ruleAnd
            {
             before(grammarAccess.getOrAccess().getAndParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            ruleAnd();

            state._fsp--;

             after(grammarAccess.getOrAccess().getAndParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Or__Group__0__Impl"


    // $ANTLR start "rule__Or__Group__1"
    // InternalCQL.g:1176:1: rule__Or__Group__1 : rule__Or__Group__1__Impl ;
    public final void rule__Or__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1180:1: ( rule__Or__Group__1__Impl )
            // InternalCQL.g:1181:2: rule__Or__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Or__Group__1__Impl();

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
    // $ANTLR end "rule__Or__Group__1"


    // $ANTLR start "rule__Or__Group__1__Impl"
    // InternalCQL.g:1187:1: rule__Or__Group__1__Impl : ( ( rule__Or__Group_1__0 )* ) ;
    public final void rule__Or__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1191:1: ( ( ( rule__Or__Group_1__0 )* ) )
            // InternalCQL.g:1192:1: ( ( rule__Or__Group_1__0 )* )
            {
            // InternalCQL.g:1192:1: ( ( rule__Or__Group_1__0 )* )
            // InternalCQL.g:1193:2: ( rule__Or__Group_1__0 )*
            {
             before(grammarAccess.getOrAccess().getGroup_1()); 
            // InternalCQL.g:1194:2: ( rule__Or__Group_1__0 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==24) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalCQL.g:1194:3: rule__Or__Group_1__0
            	    {
            	    pushFollow(FOLLOW_14);
            	    rule__Or__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

             after(grammarAccess.getOrAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Or__Group__1__Impl"


    // $ANTLR start "rule__Or__Group_1__0"
    // InternalCQL.g:1203:1: rule__Or__Group_1__0 : rule__Or__Group_1__0__Impl rule__Or__Group_1__1 ;
    public final void rule__Or__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1207:1: ( rule__Or__Group_1__0__Impl rule__Or__Group_1__1 )
            // InternalCQL.g:1208:2: rule__Or__Group_1__0__Impl rule__Or__Group_1__1
            {
            pushFollow(FOLLOW_13);
            rule__Or__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Or__Group_1__1();

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
    // $ANTLR end "rule__Or__Group_1__0"


    // $ANTLR start "rule__Or__Group_1__0__Impl"
    // InternalCQL.g:1215:1: rule__Or__Group_1__0__Impl : ( () ) ;
    public final void rule__Or__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1219:1: ( ( () ) )
            // InternalCQL.g:1220:1: ( () )
            {
            // InternalCQL.g:1220:1: ( () )
            // InternalCQL.g:1221:2: ()
            {
             before(grammarAccess.getOrAccess().getOrLeftAction_1_0()); 
            // InternalCQL.g:1222:2: ()
            // InternalCQL.g:1222:3: 
            {
            }

             after(grammarAccess.getOrAccess().getOrLeftAction_1_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Or__Group_1__0__Impl"


    // $ANTLR start "rule__Or__Group_1__1"
    // InternalCQL.g:1230:1: rule__Or__Group_1__1 : rule__Or__Group_1__1__Impl rule__Or__Group_1__2 ;
    public final void rule__Or__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1234:1: ( rule__Or__Group_1__1__Impl rule__Or__Group_1__2 )
            // InternalCQL.g:1235:2: rule__Or__Group_1__1__Impl rule__Or__Group_1__2
            {
            pushFollow(FOLLOW_11);
            rule__Or__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Or__Group_1__2();

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
    // $ANTLR end "rule__Or__Group_1__1"


    // $ANTLR start "rule__Or__Group_1__1__Impl"
    // InternalCQL.g:1242:1: rule__Or__Group_1__1__Impl : ( 'OR' ) ;
    public final void rule__Or__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1246:1: ( ( 'OR' ) )
            // InternalCQL.g:1247:1: ( 'OR' )
            {
            // InternalCQL.g:1247:1: ( 'OR' )
            // InternalCQL.g:1248:2: 'OR'
            {
             before(grammarAccess.getOrAccess().getORKeyword_1_1()); 
            match(input,24,FOLLOW_2); 
             after(grammarAccess.getOrAccess().getORKeyword_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Or__Group_1__1__Impl"


    // $ANTLR start "rule__Or__Group_1__2"
    // InternalCQL.g:1257:1: rule__Or__Group_1__2 : rule__Or__Group_1__2__Impl ;
    public final void rule__Or__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1261:1: ( rule__Or__Group_1__2__Impl )
            // InternalCQL.g:1262:2: rule__Or__Group_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Or__Group_1__2__Impl();

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
    // $ANTLR end "rule__Or__Group_1__2"


    // $ANTLR start "rule__Or__Group_1__2__Impl"
    // InternalCQL.g:1268:1: rule__Or__Group_1__2__Impl : ( ( rule__Or__RightAssignment_1_2 ) ) ;
    public final void rule__Or__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1272:1: ( ( ( rule__Or__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1273:1: ( ( rule__Or__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1273:1: ( ( rule__Or__RightAssignment_1_2 ) )
            // InternalCQL.g:1274:2: ( rule__Or__RightAssignment_1_2 )
            {
             before(grammarAccess.getOrAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1275:2: ( rule__Or__RightAssignment_1_2 )
            // InternalCQL.g:1275:3: rule__Or__RightAssignment_1_2
            {
            pushFollow(FOLLOW_2);
            rule__Or__RightAssignment_1_2();

            state._fsp--;


            }

             after(grammarAccess.getOrAccess().getRightAssignment_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Or__Group_1__2__Impl"


    // $ANTLR start "rule__And__Group__0"
    // InternalCQL.g:1284:1: rule__And__Group__0 : rule__And__Group__0__Impl rule__And__Group__1 ;
    public final void rule__And__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1288:1: ( rule__And__Group__0__Impl rule__And__Group__1 )
            // InternalCQL.g:1289:2: rule__And__Group__0__Impl rule__And__Group__1
            {
            pushFollow(FOLLOW_15);
            rule__And__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__And__Group__1();

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
    // $ANTLR end "rule__And__Group__0"


    // $ANTLR start "rule__And__Group__0__Impl"
    // InternalCQL.g:1296:1: rule__And__Group__0__Impl : ( ruleEqualitiy ) ;
    public final void rule__And__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1300:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:1301:1: ( ruleEqualitiy )
            {
            // InternalCQL.g:1301:1: ( ruleEqualitiy )
            // InternalCQL.g:1302:2: ruleEqualitiy
            {
             before(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            ruleEqualitiy();

            state._fsp--;

             after(grammarAccess.getAndAccess().getEqualitiyParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__And__Group__0__Impl"


    // $ANTLR start "rule__And__Group__1"
    // InternalCQL.g:1311:1: rule__And__Group__1 : rule__And__Group__1__Impl ;
    public final void rule__And__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1315:1: ( rule__And__Group__1__Impl )
            // InternalCQL.g:1316:2: rule__And__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__And__Group__1__Impl();

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
    // $ANTLR end "rule__And__Group__1"


    // $ANTLR start "rule__And__Group__1__Impl"
    // InternalCQL.g:1322:1: rule__And__Group__1__Impl : ( ( rule__And__Group_1__0 )* ) ;
    public final void rule__And__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1326:1: ( ( ( rule__And__Group_1__0 )* ) )
            // InternalCQL.g:1327:1: ( ( rule__And__Group_1__0 )* )
            {
            // InternalCQL.g:1327:1: ( ( rule__And__Group_1__0 )* )
            // InternalCQL.g:1328:2: ( rule__And__Group_1__0 )*
            {
             before(grammarAccess.getAndAccess().getGroup_1()); 
            // InternalCQL.g:1329:2: ( rule__And__Group_1__0 )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==25) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalCQL.g:1329:3: rule__And__Group_1__0
            	    {
            	    pushFollow(FOLLOW_16);
            	    rule__And__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

             after(grammarAccess.getAndAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__And__Group__1__Impl"


    // $ANTLR start "rule__And__Group_1__0"
    // InternalCQL.g:1338:1: rule__And__Group_1__0 : rule__And__Group_1__0__Impl rule__And__Group_1__1 ;
    public final void rule__And__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1342:1: ( rule__And__Group_1__0__Impl rule__And__Group_1__1 )
            // InternalCQL.g:1343:2: rule__And__Group_1__0__Impl rule__And__Group_1__1
            {
            pushFollow(FOLLOW_15);
            rule__And__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__And__Group_1__1();

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
    // $ANTLR end "rule__And__Group_1__0"


    // $ANTLR start "rule__And__Group_1__0__Impl"
    // InternalCQL.g:1350:1: rule__And__Group_1__0__Impl : ( () ) ;
    public final void rule__And__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1354:1: ( ( () ) )
            // InternalCQL.g:1355:1: ( () )
            {
            // InternalCQL.g:1355:1: ( () )
            // InternalCQL.g:1356:2: ()
            {
             before(grammarAccess.getAndAccess().getAndLeftAction_1_0()); 
            // InternalCQL.g:1357:2: ()
            // InternalCQL.g:1357:3: 
            {
            }

             after(grammarAccess.getAndAccess().getAndLeftAction_1_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__And__Group_1__0__Impl"


    // $ANTLR start "rule__And__Group_1__1"
    // InternalCQL.g:1365:1: rule__And__Group_1__1 : rule__And__Group_1__1__Impl rule__And__Group_1__2 ;
    public final void rule__And__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1369:1: ( rule__And__Group_1__1__Impl rule__And__Group_1__2 )
            // InternalCQL.g:1370:2: rule__And__Group_1__1__Impl rule__And__Group_1__2
            {
            pushFollow(FOLLOW_11);
            rule__And__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__And__Group_1__2();

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
    // $ANTLR end "rule__And__Group_1__1"


    // $ANTLR start "rule__And__Group_1__1__Impl"
    // InternalCQL.g:1377:1: rule__And__Group_1__1__Impl : ( 'AND' ) ;
    public final void rule__And__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1381:1: ( ( 'AND' ) )
            // InternalCQL.g:1382:1: ( 'AND' )
            {
            // InternalCQL.g:1382:1: ( 'AND' )
            // InternalCQL.g:1383:2: 'AND'
            {
             before(grammarAccess.getAndAccess().getANDKeyword_1_1()); 
            match(input,25,FOLLOW_2); 
             after(grammarAccess.getAndAccess().getANDKeyword_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__And__Group_1__1__Impl"


    // $ANTLR start "rule__And__Group_1__2"
    // InternalCQL.g:1392:1: rule__And__Group_1__2 : rule__And__Group_1__2__Impl ;
    public final void rule__And__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1396:1: ( rule__And__Group_1__2__Impl )
            // InternalCQL.g:1397:2: rule__And__Group_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__And__Group_1__2__Impl();

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
    // $ANTLR end "rule__And__Group_1__2"


    // $ANTLR start "rule__And__Group_1__2__Impl"
    // InternalCQL.g:1403:1: rule__And__Group_1__2__Impl : ( ( rule__And__RightAssignment_1_2 ) ) ;
    public final void rule__And__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1407:1: ( ( ( rule__And__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1408:1: ( ( rule__And__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1408:1: ( ( rule__And__RightAssignment_1_2 ) )
            // InternalCQL.g:1409:2: ( rule__And__RightAssignment_1_2 )
            {
             before(grammarAccess.getAndAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1410:2: ( rule__And__RightAssignment_1_2 )
            // InternalCQL.g:1410:3: rule__And__RightAssignment_1_2
            {
            pushFollow(FOLLOW_2);
            rule__And__RightAssignment_1_2();

            state._fsp--;


            }

             after(grammarAccess.getAndAccess().getRightAssignment_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__And__Group_1__2__Impl"


    // $ANTLR start "rule__Equalitiy__Group__0"
    // InternalCQL.g:1419:1: rule__Equalitiy__Group__0 : rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 ;
    public final void rule__Equalitiy__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1423:1: ( rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 )
            // InternalCQL.g:1424:2: rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1
            {
            pushFollow(FOLLOW_17);
            rule__Equalitiy__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Equalitiy__Group__1();

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
    // $ANTLR end "rule__Equalitiy__Group__0"


    // $ANTLR start "rule__Equalitiy__Group__0__Impl"
    // InternalCQL.g:1431:1: rule__Equalitiy__Group__0__Impl : ( ruleComparison ) ;
    public final void rule__Equalitiy__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1435:1: ( ( ruleComparison ) )
            // InternalCQL.g:1436:1: ( ruleComparison )
            {
            // InternalCQL.g:1436:1: ( ruleComparison )
            // InternalCQL.g:1437:2: ruleComparison
            {
             before(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            ruleComparison();

            state._fsp--;

             after(grammarAccess.getEqualitiyAccess().getComparisonParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Equalitiy__Group__0__Impl"


    // $ANTLR start "rule__Equalitiy__Group__1"
    // InternalCQL.g:1446:1: rule__Equalitiy__Group__1 : rule__Equalitiy__Group__1__Impl ;
    public final void rule__Equalitiy__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1450:1: ( rule__Equalitiy__Group__1__Impl )
            // InternalCQL.g:1451:2: rule__Equalitiy__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Equalitiy__Group__1__Impl();

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
    // $ANTLR end "rule__Equalitiy__Group__1"


    // $ANTLR start "rule__Equalitiy__Group__1__Impl"
    // InternalCQL.g:1457:1: rule__Equalitiy__Group__1__Impl : ( ( rule__Equalitiy__Group_1__0 )* ) ;
    public final void rule__Equalitiy__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1461:1: ( ( ( rule__Equalitiy__Group_1__0 )* ) )
            // InternalCQL.g:1462:1: ( ( rule__Equalitiy__Group_1__0 )* )
            {
            // InternalCQL.g:1462:1: ( ( rule__Equalitiy__Group_1__0 )* )
            // InternalCQL.g:1463:2: ( rule__Equalitiy__Group_1__0 )*
            {
             before(grammarAccess.getEqualitiyAccess().getGroup_1()); 
            // InternalCQL.g:1464:2: ( rule__Equalitiy__Group_1__0 )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>=14 && LA15_0<=15)) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCQL.g:1464:3: rule__Equalitiy__Group_1__0
            	    {
            	    pushFollow(FOLLOW_18);
            	    rule__Equalitiy__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

             after(grammarAccess.getEqualitiyAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Equalitiy__Group__1__Impl"


    // $ANTLR start "rule__Equalitiy__Group_1__0"
    // InternalCQL.g:1473:1: rule__Equalitiy__Group_1__0 : rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 ;
    public final void rule__Equalitiy__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1477:1: ( rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 )
            // InternalCQL.g:1478:2: rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1
            {
            pushFollow(FOLLOW_17);
            rule__Equalitiy__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Equalitiy__Group_1__1();

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
    // $ANTLR end "rule__Equalitiy__Group_1__0"


    // $ANTLR start "rule__Equalitiy__Group_1__0__Impl"
    // InternalCQL.g:1485:1: rule__Equalitiy__Group_1__0__Impl : ( () ) ;
    public final void rule__Equalitiy__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1489:1: ( ( () ) )
            // InternalCQL.g:1490:1: ( () )
            {
            // InternalCQL.g:1490:1: ( () )
            // InternalCQL.g:1491:2: ()
            {
             before(grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0()); 
            // InternalCQL.g:1492:2: ()
            // InternalCQL.g:1492:3: 
            {
            }

             after(grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Equalitiy__Group_1__0__Impl"


    // $ANTLR start "rule__Equalitiy__Group_1__1"
    // InternalCQL.g:1500:1: rule__Equalitiy__Group_1__1 : rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 ;
    public final void rule__Equalitiy__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1504:1: ( rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 )
            // InternalCQL.g:1505:2: rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2
            {
            pushFollow(FOLLOW_11);
            rule__Equalitiy__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Equalitiy__Group_1__2();

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
    // $ANTLR end "rule__Equalitiy__Group_1__1"


    // $ANTLR start "rule__Equalitiy__Group_1__1__Impl"
    // InternalCQL.g:1512:1: rule__Equalitiy__Group_1__1__Impl : ( ( rule__Equalitiy__OpAssignment_1_1 ) ) ;
    public final void rule__Equalitiy__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1516:1: ( ( ( rule__Equalitiy__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1517:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1517:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            // InternalCQL.g:1518:2: ( rule__Equalitiy__OpAssignment_1_1 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1519:2: ( rule__Equalitiy__OpAssignment_1_1 )
            // InternalCQL.g:1519:3: rule__Equalitiy__OpAssignment_1_1
            {
            pushFollow(FOLLOW_2);
            rule__Equalitiy__OpAssignment_1_1();

            state._fsp--;


            }

             after(grammarAccess.getEqualitiyAccess().getOpAssignment_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Equalitiy__Group_1__1__Impl"


    // $ANTLR start "rule__Equalitiy__Group_1__2"
    // InternalCQL.g:1527:1: rule__Equalitiy__Group_1__2 : rule__Equalitiy__Group_1__2__Impl ;
    public final void rule__Equalitiy__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1531:1: ( rule__Equalitiy__Group_1__2__Impl )
            // InternalCQL.g:1532:2: rule__Equalitiy__Group_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Equalitiy__Group_1__2__Impl();

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
    // $ANTLR end "rule__Equalitiy__Group_1__2"


    // $ANTLR start "rule__Equalitiy__Group_1__2__Impl"
    // InternalCQL.g:1538:1: rule__Equalitiy__Group_1__2__Impl : ( ( rule__Equalitiy__RightAssignment_1_2 ) ) ;
    public final void rule__Equalitiy__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1542:1: ( ( ( rule__Equalitiy__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1543:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1543:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            // InternalCQL.g:1544:2: ( rule__Equalitiy__RightAssignment_1_2 )
            {
             before(grammarAccess.getEqualitiyAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1545:2: ( rule__Equalitiy__RightAssignment_1_2 )
            // InternalCQL.g:1545:3: rule__Equalitiy__RightAssignment_1_2
            {
            pushFollow(FOLLOW_2);
            rule__Equalitiy__RightAssignment_1_2();

            state._fsp--;


            }

             after(grammarAccess.getEqualitiyAccess().getRightAssignment_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Equalitiy__Group_1__2__Impl"


    // $ANTLR start "rule__Comparison__Group__0"
    // InternalCQL.g:1554:1: rule__Comparison__Group__0 : rule__Comparison__Group__0__Impl rule__Comparison__Group__1 ;
    public final void rule__Comparison__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1558:1: ( rule__Comparison__Group__0__Impl rule__Comparison__Group__1 )
            // InternalCQL.g:1559:2: rule__Comparison__Group__0__Impl rule__Comparison__Group__1
            {
            pushFollow(FOLLOW_19);
            rule__Comparison__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Comparison__Group__1();

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
    // $ANTLR end "rule__Comparison__Group__0"


    // $ANTLR start "rule__Comparison__Group__0__Impl"
    // InternalCQL.g:1566:1: rule__Comparison__Group__0__Impl : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1570:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:1571:1: ( rulePlusOrMinus )
            {
            // InternalCQL.g:1571:1: ( rulePlusOrMinus )
            // InternalCQL.g:1572:2: rulePlusOrMinus
            {
             before(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            rulePlusOrMinus();

            state._fsp--;

             after(grammarAccess.getComparisonAccess().getPlusOrMinusParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Comparison__Group__0__Impl"


    // $ANTLR start "rule__Comparison__Group__1"
    // InternalCQL.g:1581:1: rule__Comparison__Group__1 : rule__Comparison__Group__1__Impl ;
    public final void rule__Comparison__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1585:1: ( rule__Comparison__Group__1__Impl )
            // InternalCQL.g:1586:2: rule__Comparison__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Comparison__Group__1__Impl();

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
    // $ANTLR end "rule__Comparison__Group__1"


    // $ANTLR start "rule__Comparison__Group__1__Impl"
    // InternalCQL.g:1592:1: rule__Comparison__Group__1__Impl : ( ( rule__Comparison__Group_1__0 )* ) ;
    public final void rule__Comparison__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1596:1: ( ( ( rule__Comparison__Group_1__0 )* ) )
            // InternalCQL.g:1597:1: ( ( rule__Comparison__Group_1__0 )* )
            {
            // InternalCQL.g:1597:1: ( ( rule__Comparison__Group_1__0 )* )
            // InternalCQL.g:1598:2: ( rule__Comparison__Group_1__0 )*
            {
             before(grammarAccess.getComparisonAccess().getGroup_1()); 
            // InternalCQL.g:1599:2: ( rule__Comparison__Group_1__0 )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( ((LA16_0>=16 && LA16_0<=19)) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalCQL.g:1599:3: rule__Comparison__Group_1__0
            	    {
            	    pushFollow(FOLLOW_20);
            	    rule__Comparison__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

             after(grammarAccess.getComparisonAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Comparison__Group__1__Impl"


    // $ANTLR start "rule__Comparison__Group_1__0"
    // InternalCQL.g:1608:1: rule__Comparison__Group_1__0 : rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 ;
    public final void rule__Comparison__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1612:1: ( rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 )
            // InternalCQL.g:1613:2: rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1
            {
            pushFollow(FOLLOW_19);
            rule__Comparison__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Comparison__Group_1__1();

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
    // $ANTLR end "rule__Comparison__Group_1__0"


    // $ANTLR start "rule__Comparison__Group_1__0__Impl"
    // InternalCQL.g:1620:1: rule__Comparison__Group_1__0__Impl : ( () ) ;
    public final void rule__Comparison__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1624:1: ( ( () ) )
            // InternalCQL.g:1625:1: ( () )
            {
            // InternalCQL.g:1625:1: ( () )
            // InternalCQL.g:1626:2: ()
            {
             before(grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0()); 
            // InternalCQL.g:1627:2: ()
            // InternalCQL.g:1627:3: 
            {
            }

             after(grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Comparison__Group_1__0__Impl"


    // $ANTLR start "rule__Comparison__Group_1__1"
    // InternalCQL.g:1635:1: rule__Comparison__Group_1__1 : rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 ;
    public final void rule__Comparison__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1639:1: ( rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 )
            // InternalCQL.g:1640:2: rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2
            {
            pushFollow(FOLLOW_11);
            rule__Comparison__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Comparison__Group_1__2();

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
    // $ANTLR end "rule__Comparison__Group_1__1"


    // $ANTLR start "rule__Comparison__Group_1__1__Impl"
    // InternalCQL.g:1647:1: rule__Comparison__Group_1__1__Impl : ( ( rule__Comparison__OpAssignment_1_1 ) ) ;
    public final void rule__Comparison__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1651:1: ( ( ( rule__Comparison__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1652:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1652:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            // InternalCQL.g:1653:2: ( rule__Comparison__OpAssignment_1_1 )
            {
             before(grammarAccess.getComparisonAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1654:2: ( rule__Comparison__OpAssignment_1_1 )
            // InternalCQL.g:1654:3: rule__Comparison__OpAssignment_1_1
            {
            pushFollow(FOLLOW_2);
            rule__Comparison__OpAssignment_1_1();

            state._fsp--;


            }

             after(grammarAccess.getComparisonAccess().getOpAssignment_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Comparison__Group_1__1__Impl"


    // $ANTLR start "rule__Comparison__Group_1__2"
    // InternalCQL.g:1662:1: rule__Comparison__Group_1__2 : rule__Comparison__Group_1__2__Impl ;
    public final void rule__Comparison__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1666:1: ( rule__Comparison__Group_1__2__Impl )
            // InternalCQL.g:1667:2: rule__Comparison__Group_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Comparison__Group_1__2__Impl();

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
    // $ANTLR end "rule__Comparison__Group_1__2"


    // $ANTLR start "rule__Comparison__Group_1__2__Impl"
    // InternalCQL.g:1673:1: rule__Comparison__Group_1__2__Impl : ( ( rule__Comparison__RightAssignment_1_2 ) ) ;
    public final void rule__Comparison__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1677:1: ( ( ( rule__Comparison__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1678:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1678:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            // InternalCQL.g:1679:2: ( rule__Comparison__RightAssignment_1_2 )
            {
             before(grammarAccess.getComparisonAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1680:2: ( rule__Comparison__RightAssignment_1_2 )
            // InternalCQL.g:1680:3: rule__Comparison__RightAssignment_1_2
            {
            pushFollow(FOLLOW_2);
            rule__Comparison__RightAssignment_1_2();

            state._fsp--;


            }

             after(grammarAccess.getComparisonAccess().getRightAssignment_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Comparison__Group_1__2__Impl"


    // $ANTLR start "rule__PlusOrMinus__Group__0"
    // InternalCQL.g:1689:1: rule__PlusOrMinus__Group__0 : rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 ;
    public final void rule__PlusOrMinus__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1693:1: ( rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 )
            // InternalCQL.g:1694:2: rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1
            {
            pushFollow(FOLLOW_21);
            rule__PlusOrMinus__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Group__1();

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
    // $ANTLR end "rule__PlusOrMinus__Group__0"


    // $ANTLR start "rule__PlusOrMinus__Group__0__Impl"
    // InternalCQL.g:1701:1: rule__PlusOrMinus__Group__0__Impl : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1705:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:1706:1: ( ruleMulOrDiv )
            {
            // InternalCQL.g:1706:1: ( ruleMulOrDiv )
            // InternalCQL.g:1707:2: ruleMulOrDiv
            {
             before(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            ruleMulOrDiv();

            state._fsp--;

             after(grammarAccess.getPlusOrMinusAccess().getMulOrDivParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlusOrMinus__Group__0__Impl"


    // $ANTLR start "rule__PlusOrMinus__Group__1"
    // InternalCQL.g:1716:1: rule__PlusOrMinus__Group__1 : rule__PlusOrMinus__Group__1__Impl ;
    public final void rule__PlusOrMinus__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1720:1: ( rule__PlusOrMinus__Group__1__Impl )
            // InternalCQL.g:1721:2: rule__PlusOrMinus__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Group__1__Impl();

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
    // $ANTLR end "rule__PlusOrMinus__Group__1"


    // $ANTLR start "rule__PlusOrMinus__Group__1__Impl"
    // InternalCQL.g:1727:1: rule__PlusOrMinus__Group__1__Impl : ( ( rule__PlusOrMinus__Group_1__0 )* ) ;
    public final void rule__PlusOrMinus__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1731:1: ( ( ( rule__PlusOrMinus__Group_1__0 )* ) )
            // InternalCQL.g:1732:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            {
            // InternalCQL.g:1732:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            // InternalCQL.g:1733:2: ( rule__PlusOrMinus__Group_1__0 )*
            {
             before(grammarAccess.getPlusOrMinusAccess().getGroup_1()); 
            // InternalCQL.g:1734:2: ( rule__PlusOrMinus__Group_1__0 )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>=26 && LA17_0<=27)) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // InternalCQL.g:1734:3: rule__PlusOrMinus__Group_1__0
            	    {
            	    pushFollow(FOLLOW_22);
            	    rule__PlusOrMinus__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

             after(grammarAccess.getPlusOrMinusAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlusOrMinus__Group__1__Impl"


    // $ANTLR start "rule__PlusOrMinus__Group_1__0"
    // InternalCQL.g:1743:1: rule__PlusOrMinus__Group_1__0 : rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 ;
    public final void rule__PlusOrMinus__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1747:1: ( rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 )
            // InternalCQL.g:1748:2: rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1
            {
            pushFollow(FOLLOW_11);
            rule__PlusOrMinus__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Group_1__1();

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
    // $ANTLR end "rule__PlusOrMinus__Group_1__0"


    // $ANTLR start "rule__PlusOrMinus__Group_1__0__Impl"
    // InternalCQL.g:1755:1: rule__PlusOrMinus__Group_1__0__Impl : ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) ;
    public final void rule__PlusOrMinus__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1759:1: ( ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) )
            // InternalCQL.g:1760:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            {
            // InternalCQL.g:1760:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            // InternalCQL.g:1761:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getAlternatives_1_0()); 
            // InternalCQL.g:1762:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            // InternalCQL.g:1762:3: rule__PlusOrMinus__Alternatives_1_0
            {
            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Alternatives_1_0();

            state._fsp--;


            }

             after(grammarAccess.getPlusOrMinusAccess().getAlternatives_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlusOrMinus__Group_1__0__Impl"


    // $ANTLR start "rule__PlusOrMinus__Group_1__1"
    // InternalCQL.g:1770:1: rule__PlusOrMinus__Group_1__1 : rule__PlusOrMinus__Group_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1774:1: ( rule__PlusOrMinus__Group_1__1__Impl )
            // InternalCQL.g:1775:2: rule__PlusOrMinus__Group_1__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Group_1__1__Impl();

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
    // $ANTLR end "rule__PlusOrMinus__Group_1__1"


    // $ANTLR start "rule__PlusOrMinus__Group_1__1__Impl"
    // InternalCQL.g:1781:1: rule__PlusOrMinus__Group_1__1__Impl : ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) ;
    public final void rule__PlusOrMinus__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1785:1: ( ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) )
            // InternalCQL.g:1786:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            {
            // InternalCQL.g:1786:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            // InternalCQL.g:1787:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getRightAssignment_1_1()); 
            // InternalCQL.g:1788:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            // InternalCQL.g:1788:3: rule__PlusOrMinus__RightAssignment_1_1
            {
            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__RightAssignment_1_1();

            state._fsp--;


            }

             after(grammarAccess.getPlusOrMinusAccess().getRightAssignment_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlusOrMinus__Group_1__1__Impl"


    // $ANTLR start "rule__PlusOrMinus__Group_1_0_0__0"
    // InternalCQL.g:1797:1: rule__PlusOrMinus__Group_1_0_0__0 : rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 ;
    public final void rule__PlusOrMinus__Group_1_0_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1801:1: ( rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 )
            // InternalCQL.g:1802:2: rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1
            {
            pushFollow(FOLLOW_23);
            rule__PlusOrMinus__Group_1_0_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Group_1_0_0__1();

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
    // $ANTLR end "rule__PlusOrMinus__Group_1_0_0__0"


    // $ANTLR start "rule__PlusOrMinus__Group_1_0_0__0__Impl"
    // InternalCQL.g:1809:1: rule__PlusOrMinus__Group_1_0_0__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1813:1: ( ( () ) )
            // InternalCQL.g:1814:1: ( () )
            {
            // InternalCQL.g:1814:1: ( () )
            // InternalCQL.g:1815:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0()); 
            // InternalCQL.g:1816:2: ()
            // InternalCQL.g:1816:3: 
            {
            }

             after(grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlusOrMinus__Group_1_0_0__0__Impl"


    // $ANTLR start "rule__PlusOrMinus__Group_1_0_0__1"
    // InternalCQL.g:1824:1: rule__PlusOrMinus__Group_1_0_0__1 : rule__PlusOrMinus__Group_1_0_0__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1828:1: ( rule__PlusOrMinus__Group_1_0_0__1__Impl )
            // InternalCQL.g:1829:2: rule__PlusOrMinus__Group_1_0_0__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Group_1_0_0__1__Impl();

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
    // $ANTLR end "rule__PlusOrMinus__Group_1_0_0__1"


    // $ANTLR start "rule__PlusOrMinus__Group_1_0_0__1__Impl"
    // InternalCQL.g:1835:1: rule__PlusOrMinus__Group_1_0_0__1__Impl : ( '+' ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1839:1: ( ( '+' ) )
            // InternalCQL.g:1840:1: ( '+' )
            {
            // InternalCQL.g:1840:1: ( '+' )
            // InternalCQL.g:1841:2: '+'
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1()); 
            match(input,26,FOLLOW_2); 
             after(grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlusOrMinus__Group_1_0_0__1__Impl"


    // $ANTLR start "rule__PlusOrMinus__Group_1_0_1__0"
    // InternalCQL.g:1851:1: rule__PlusOrMinus__Group_1_0_1__0 : rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 ;
    public final void rule__PlusOrMinus__Group_1_0_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1855:1: ( rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 )
            // InternalCQL.g:1856:2: rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1
            {
            pushFollow(FOLLOW_21);
            rule__PlusOrMinus__Group_1_0_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Group_1_0_1__1();

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
    // $ANTLR end "rule__PlusOrMinus__Group_1_0_1__0"


    // $ANTLR start "rule__PlusOrMinus__Group_1_0_1__0__Impl"
    // InternalCQL.g:1863:1: rule__PlusOrMinus__Group_1_0_1__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1867:1: ( ( () ) )
            // InternalCQL.g:1868:1: ( () )
            {
            // InternalCQL.g:1868:1: ( () )
            // InternalCQL.g:1869:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0()); 
            // InternalCQL.g:1870:2: ()
            // InternalCQL.g:1870:3: 
            {
            }

             after(grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlusOrMinus__Group_1_0_1__0__Impl"


    // $ANTLR start "rule__PlusOrMinus__Group_1_0_1__1"
    // InternalCQL.g:1878:1: rule__PlusOrMinus__Group_1_0_1__1 : rule__PlusOrMinus__Group_1_0_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1882:1: ( rule__PlusOrMinus__Group_1_0_1__1__Impl )
            // InternalCQL.g:1883:2: rule__PlusOrMinus__Group_1_0_1__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__PlusOrMinus__Group_1_0_1__1__Impl();

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
    // $ANTLR end "rule__PlusOrMinus__Group_1_0_1__1"


    // $ANTLR start "rule__PlusOrMinus__Group_1_0_1__1__Impl"
    // InternalCQL.g:1889:1: rule__PlusOrMinus__Group_1_0_1__1__Impl : ( '-' ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1893:1: ( ( '-' ) )
            // InternalCQL.g:1894:1: ( '-' )
            {
            // InternalCQL.g:1894:1: ( '-' )
            // InternalCQL.g:1895:2: '-'
            {
             before(grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1()); 
            match(input,27,FOLLOW_2); 
             after(grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlusOrMinus__Group_1_0_1__1__Impl"


    // $ANTLR start "rule__MulOrDiv__Group__0"
    // InternalCQL.g:1905:1: rule__MulOrDiv__Group__0 : rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 ;
    public final void rule__MulOrDiv__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1909:1: ( rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 )
            // InternalCQL.g:1910:2: rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1
            {
            pushFollow(FOLLOW_24);
            rule__MulOrDiv__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__MulOrDiv__Group__1();

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
    // $ANTLR end "rule__MulOrDiv__Group__0"


    // $ANTLR start "rule__MulOrDiv__Group__0__Impl"
    // InternalCQL.g:1917:1: rule__MulOrDiv__Group__0__Impl : ( rulePrimary ) ;
    public final void rule__MulOrDiv__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1921:1: ( ( rulePrimary ) )
            // InternalCQL.g:1922:1: ( rulePrimary )
            {
            // InternalCQL.g:1922:1: ( rulePrimary )
            // InternalCQL.g:1923:2: rulePrimary
            {
             before(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            rulePrimary();

            state._fsp--;

             after(grammarAccess.getMulOrDivAccess().getPrimaryParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MulOrDiv__Group__0__Impl"


    // $ANTLR start "rule__MulOrDiv__Group__1"
    // InternalCQL.g:1932:1: rule__MulOrDiv__Group__1 : rule__MulOrDiv__Group__1__Impl ;
    public final void rule__MulOrDiv__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1936:1: ( rule__MulOrDiv__Group__1__Impl )
            // InternalCQL.g:1937:2: rule__MulOrDiv__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__MulOrDiv__Group__1__Impl();

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
    // $ANTLR end "rule__MulOrDiv__Group__1"


    // $ANTLR start "rule__MulOrDiv__Group__1__Impl"
    // InternalCQL.g:1943:1: rule__MulOrDiv__Group__1__Impl : ( ( rule__MulOrDiv__Group_1__0 )* ) ;
    public final void rule__MulOrDiv__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1947:1: ( ( ( rule__MulOrDiv__Group_1__0 )* ) )
            // InternalCQL.g:1948:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            {
            // InternalCQL.g:1948:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            // InternalCQL.g:1949:2: ( rule__MulOrDiv__Group_1__0 )*
            {
             before(grammarAccess.getMulOrDivAccess().getGroup_1()); 
            // InternalCQL.g:1950:2: ( rule__MulOrDiv__Group_1__0 )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>=20 && LA18_0<=21)) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // InternalCQL.g:1950:3: rule__MulOrDiv__Group_1__0
            	    {
            	    pushFollow(FOLLOW_25);
            	    rule__MulOrDiv__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

             after(grammarAccess.getMulOrDivAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MulOrDiv__Group__1__Impl"


    // $ANTLR start "rule__MulOrDiv__Group_1__0"
    // InternalCQL.g:1959:1: rule__MulOrDiv__Group_1__0 : rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 ;
    public final void rule__MulOrDiv__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1963:1: ( rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 )
            // InternalCQL.g:1964:2: rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1
            {
            pushFollow(FOLLOW_24);
            rule__MulOrDiv__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__MulOrDiv__Group_1__1();

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
    // $ANTLR end "rule__MulOrDiv__Group_1__0"


    // $ANTLR start "rule__MulOrDiv__Group_1__0__Impl"
    // InternalCQL.g:1971:1: rule__MulOrDiv__Group_1__0__Impl : ( () ) ;
    public final void rule__MulOrDiv__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1975:1: ( ( () ) )
            // InternalCQL.g:1976:1: ( () )
            {
            // InternalCQL.g:1976:1: ( () )
            // InternalCQL.g:1977:2: ()
            {
             before(grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0()); 
            // InternalCQL.g:1978:2: ()
            // InternalCQL.g:1978:3: 
            {
            }

             after(grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MulOrDiv__Group_1__0__Impl"


    // $ANTLR start "rule__MulOrDiv__Group_1__1"
    // InternalCQL.g:1986:1: rule__MulOrDiv__Group_1__1 : rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 ;
    public final void rule__MulOrDiv__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1990:1: ( rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 )
            // InternalCQL.g:1991:2: rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2
            {
            pushFollow(FOLLOW_11);
            rule__MulOrDiv__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__MulOrDiv__Group_1__2();

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
    // $ANTLR end "rule__MulOrDiv__Group_1__1"


    // $ANTLR start "rule__MulOrDiv__Group_1__1__Impl"
    // InternalCQL.g:1998:1: rule__MulOrDiv__Group_1__1__Impl : ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) ;
    public final void rule__MulOrDiv__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2002:1: ( ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) )
            // InternalCQL.g:2003:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:2003:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            // InternalCQL.g:2004:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:2005:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            // InternalCQL.g:2005:3: rule__MulOrDiv__OpAssignment_1_1
            {
            pushFollow(FOLLOW_2);
            rule__MulOrDiv__OpAssignment_1_1();

            state._fsp--;


            }

             after(grammarAccess.getMulOrDivAccess().getOpAssignment_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MulOrDiv__Group_1__1__Impl"


    // $ANTLR start "rule__MulOrDiv__Group_1__2"
    // InternalCQL.g:2013:1: rule__MulOrDiv__Group_1__2 : rule__MulOrDiv__Group_1__2__Impl ;
    public final void rule__MulOrDiv__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2017:1: ( rule__MulOrDiv__Group_1__2__Impl )
            // InternalCQL.g:2018:2: rule__MulOrDiv__Group_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__MulOrDiv__Group_1__2__Impl();

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
    // $ANTLR end "rule__MulOrDiv__Group_1__2"


    // $ANTLR start "rule__MulOrDiv__Group_1__2__Impl"
    // InternalCQL.g:2024:1: rule__MulOrDiv__Group_1__2__Impl : ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) ;
    public final void rule__MulOrDiv__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2028:1: ( ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) )
            // InternalCQL.g:2029:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:2029:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            // InternalCQL.g:2030:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            {
             before(grammarAccess.getMulOrDivAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:2031:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            // InternalCQL.g:2031:3: rule__MulOrDiv__RightAssignment_1_2
            {
            pushFollow(FOLLOW_2);
            rule__MulOrDiv__RightAssignment_1_2();

            state._fsp--;


            }

             after(grammarAccess.getMulOrDivAccess().getRightAssignment_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MulOrDiv__Group_1__2__Impl"


    // $ANTLR start "rule__Primary__Group_0__0"
    // InternalCQL.g:2040:1: rule__Primary__Group_0__0 : rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 ;
    public final void rule__Primary__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2044:1: ( rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 )
            // InternalCQL.g:2045:2: rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1
            {
            pushFollow(FOLLOW_26);
            rule__Primary__Group_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Primary__Group_0__1();

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
    // $ANTLR end "rule__Primary__Group_0__0"


    // $ANTLR start "rule__Primary__Group_0__0__Impl"
    // InternalCQL.g:2052:1: rule__Primary__Group_0__0__Impl : ( () ) ;
    public final void rule__Primary__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2056:1: ( ( () ) )
            // InternalCQL.g:2057:1: ( () )
            {
            // InternalCQL.g:2057:1: ( () )
            // InternalCQL.g:2058:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getBracketAction_0_0()); 
            // InternalCQL.g:2059:2: ()
            // InternalCQL.g:2059:3: 
            {
            }

             after(grammarAccess.getPrimaryAccess().getBracketAction_0_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Primary__Group_0__0__Impl"


    // $ANTLR start "rule__Primary__Group_0__1"
    // InternalCQL.g:2067:1: rule__Primary__Group_0__1 : rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 ;
    public final void rule__Primary__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2071:1: ( rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 )
            // InternalCQL.g:2072:2: rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2
            {
            pushFollow(FOLLOW_11);
            rule__Primary__Group_0__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Primary__Group_0__2();

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
    // $ANTLR end "rule__Primary__Group_0__1"


    // $ANTLR start "rule__Primary__Group_0__1__Impl"
    // InternalCQL.g:2079:1: rule__Primary__Group_0__1__Impl : ( '(' ) ;
    public final void rule__Primary__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2083:1: ( ( '(' ) )
            // InternalCQL.g:2084:1: ( '(' )
            {
            // InternalCQL.g:2084:1: ( '(' )
            // InternalCQL.g:2085:2: '('
            {
             before(grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1()); 
            match(input,28,FOLLOW_2); 
             after(grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Primary__Group_0__1__Impl"


    // $ANTLR start "rule__Primary__Group_0__2"
    // InternalCQL.g:2094:1: rule__Primary__Group_0__2 : rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 ;
    public final void rule__Primary__Group_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2098:1: ( rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 )
            // InternalCQL.g:2099:2: rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3
            {
            pushFollow(FOLLOW_27);
            rule__Primary__Group_0__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Primary__Group_0__3();

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
    // $ANTLR end "rule__Primary__Group_0__2"


    // $ANTLR start "rule__Primary__Group_0__2__Impl"
    // InternalCQL.g:2106:1: rule__Primary__Group_0__2__Impl : ( ( rule__Primary__InnerAssignment_0_2 ) ) ;
    public final void rule__Primary__Group_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2110:1: ( ( ( rule__Primary__InnerAssignment_0_2 ) ) )
            // InternalCQL.g:2111:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            {
            // InternalCQL.g:2111:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            // InternalCQL.g:2112:2: ( rule__Primary__InnerAssignment_0_2 )
            {
             before(grammarAccess.getPrimaryAccess().getInnerAssignment_0_2()); 
            // InternalCQL.g:2113:2: ( rule__Primary__InnerAssignment_0_2 )
            // InternalCQL.g:2113:3: rule__Primary__InnerAssignment_0_2
            {
            pushFollow(FOLLOW_2);
            rule__Primary__InnerAssignment_0_2();

            state._fsp--;


            }

             after(grammarAccess.getPrimaryAccess().getInnerAssignment_0_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Primary__Group_0__2__Impl"


    // $ANTLR start "rule__Primary__Group_0__3"
    // InternalCQL.g:2121:1: rule__Primary__Group_0__3 : rule__Primary__Group_0__3__Impl ;
    public final void rule__Primary__Group_0__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2125:1: ( rule__Primary__Group_0__3__Impl )
            // InternalCQL.g:2126:2: rule__Primary__Group_0__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Primary__Group_0__3__Impl();

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
    // $ANTLR end "rule__Primary__Group_0__3"


    // $ANTLR start "rule__Primary__Group_0__3__Impl"
    // InternalCQL.g:2132:1: rule__Primary__Group_0__3__Impl : ( ')' ) ;
    public final void rule__Primary__Group_0__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2136:1: ( ( ')' ) )
            // InternalCQL.g:2137:1: ( ')' )
            {
            // InternalCQL.g:2137:1: ( ')' )
            // InternalCQL.g:2138:2: ')'
            {
             before(grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3()); 
            match(input,29,FOLLOW_2); 
             after(grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Primary__Group_0__3__Impl"


    // $ANTLR start "rule__Primary__Group_1__0"
    // InternalCQL.g:2148:1: rule__Primary__Group_1__0 : rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 ;
    public final void rule__Primary__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2152:1: ( rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 )
            // InternalCQL.g:2153:2: rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1
            {
            pushFollow(FOLLOW_28);
            rule__Primary__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Primary__Group_1__1();

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
    // $ANTLR end "rule__Primary__Group_1__0"


    // $ANTLR start "rule__Primary__Group_1__0__Impl"
    // InternalCQL.g:2160:1: rule__Primary__Group_1__0__Impl : ( () ) ;
    public final void rule__Primary__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2164:1: ( ( () ) )
            // InternalCQL.g:2165:1: ( () )
            {
            // InternalCQL.g:2165:1: ( () )
            // InternalCQL.g:2166:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getNOTAction_1_0()); 
            // InternalCQL.g:2167:2: ()
            // InternalCQL.g:2167:3: 
            {
            }

             after(grammarAccess.getPrimaryAccess().getNOTAction_1_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Primary__Group_1__0__Impl"


    // $ANTLR start "rule__Primary__Group_1__1"
    // InternalCQL.g:2175:1: rule__Primary__Group_1__1 : rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 ;
    public final void rule__Primary__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2179:1: ( rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 )
            // InternalCQL.g:2180:2: rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2
            {
            pushFollow(FOLLOW_11);
            rule__Primary__Group_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Primary__Group_1__2();

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
    // $ANTLR end "rule__Primary__Group_1__1"


    // $ANTLR start "rule__Primary__Group_1__1__Impl"
    // InternalCQL.g:2187:1: rule__Primary__Group_1__1__Impl : ( 'NOT' ) ;
    public final void rule__Primary__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2191:1: ( ( 'NOT' ) )
            // InternalCQL.g:2192:1: ( 'NOT' )
            {
            // InternalCQL.g:2192:1: ( 'NOT' )
            // InternalCQL.g:2193:2: 'NOT'
            {
             before(grammarAccess.getPrimaryAccess().getNOTKeyword_1_1()); 
            match(input,30,FOLLOW_2); 
             after(grammarAccess.getPrimaryAccess().getNOTKeyword_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Primary__Group_1__1__Impl"


    // $ANTLR start "rule__Primary__Group_1__2"
    // InternalCQL.g:2202:1: rule__Primary__Group_1__2 : rule__Primary__Group_1__2__Impl ;
    public final void rule__Primary__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2206:1: ( rule__Primary__Group_1__2__Impl )
            // InternalCQL.g:2207:2: rule__Primary__Group_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Primary__Group_1__2__Impl();

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
    // $ANTLR end "rule__Primary__Group_1__2"


    // $ANTLR start "rule__Primary__Group_1__2__Impl"
    // InternalCQL.g:2213:1: rule__Primary__Group_1__2__Impl : ( ( rule__Primary__ExpressionAssignment_1_2 ) ) ;
    public final void rule__Primary__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2217:1: ( ( ( rule__Primary__ExpressionAssignment_1_2 ) ) )
            // InternalCQL.g:2218:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            {
            // InternalCQL.g:2218:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            // InternalCQL.g:2219:2: ( rule__Primary__ExpressionAssignment_1_2 )
            {
             before(grammarAccess.getPrimaryAccess().getExpressionAssignment_1_2()); 
            // InternalCQL.g:2220:2: ( rule__Primary__ExpressionAssignment_1_2 )
            // InternalCQL.g:2220:3: rule__Primary__ExpressionAssignment_1_2
            {
            pushFollow(FOLLOW_2);
            rule__Primary__ExpressionAssignment_1_2();

            state._fsp--;


            }

             after(grammarAccess.getPrimaryAccess().getExpressionAssignment_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Primary__Group_1__2__Impl"


    // $ANTLR start "rule__Select_Statement__Group__0"
    // InternalCQL.g:2229:1: rule__Select_Statement__Group__0 : rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 ;
    public final void rule__Select_Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2233:1: ( rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 )
            // InternalCQL.g:2234:2: rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1
            {
            pushFollow(FOLLOW_29);
            rule__Select_Statement__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__1();

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
    // $ANTLR end "rule__Select_Statement__Group__0"


    // $ANTLR start "rule__Select_Statement__Group__0__Impl"
    // InternalCQL.g:2241:1: rule__Select_Statement__Group__0__Impl : ( ( rule__Select_Statement__NameAssignment_0 ) ) ;
    public final void rule__Select_Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2245:1: ( ( ( rule__Select_Statement__NameAssignment_0 ) ) )
            // InternalCQL.g:2246:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            {
            // InternalCQL.g:2246:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            // InternalCQL.g:2247:2: ( rule__Select_Statement__NameAssignment_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameAssignment_0()); 
            // InternalCQL.g:2248:2: ( rule__Select_Statement__NameAssignment_0 )
            // InternalCQL.g:2248:3: rule__Select_Statement__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group__1"
    // InternalCQL.g:2256:1: rule__Select_Statement__Group__1 : rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 ;
    public final void rule__Select_Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2260:1: ( rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 )
            // InternalCQL.g:2261:2: rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2
            {
            pushFollow(FOLLOW_29);
            rule__Select_Statement__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__2();

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
    // $ANTLR end "rule__Select_Statement__Group__1"


    // $ANTLR start "rule__Select_Statement__Group__1__Impl"
    // InternalCQL.g:2268:1: rule__Select_Statement__Group__1__Impl : ( ( 'DISTINCT' )? ) ;
    public final void rule__Select_Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2272:1: ( ( ( 'DISTINCT' )? ) )
            // InternalCQL.g:2273:1: ( ( 'DISTINCT' )? )
            {
            // InternalCQL.g:2273:1: ( ( 'DISTINCT' )? )
            // InternalCQL.g:2274:2: ( 'DISTINCT' )?
            {
             before(grammarAccess.getSelect_StatementAccess().getDISTINCTKeyword_1()); 
            // InternalCQL.g:2275:2: ( 'DISTINCT' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==31) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalCQL.g:2275:3: 'DISTINCT'
                    {
                    match(input,31,FOLLOW_2); 

                    }
                    break;

            }

             after(grammarAccess.getSelect_StatementAccess().getDISTINCTKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group__1__Impl"


    // $ANTLR start "rule__Select_Statement__Group__2"
    // InternalCQL.g:2283:1: rule__Select_Statement__Group__2 : rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 ;
    public final void rule__Select_Statement__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2287:1: ( rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 )
            // InternalCQL.g:2288:2: rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3
            {
            pushFollow(FOLLOW_30);
            rule__Select_Statement__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__3();

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
    // $ANTLR end "rule__Select_Statement__Group__2"


    // $ANTLR start "rule__Select_Statement__Group__2__Impl"
    // InternalCQL.g:2295:1: rule__Select_Statement__Group__2__Impl : ( ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* ) ) ;
    public final void rule__Select_Statement__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2299:1: ( ( ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* ) ) )
            // InternalCQL.g:2300:1: ( ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* ) )
            {
            // InternalCQL.g:2300:1: ( ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* ) )
            // InternalCQL.g:2301:2: ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* )
            {
            // InternalCQL.g:2301:2: ( ( rule__Select_Statement__AttributesAssignment_2 ) )
            // InternalCQL.g:2302:3: ( rule__Select_Statement__AttributesAssignment_2 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2()); 
            // InternalCQL.g:2303:3: ( rule__Select_Statement__AttributesAssignment_2 )
            // InternalCQL.g:2303:4: rule__Select_Statement__AttributesAssignment_2
            {
            pushFollow(FOLLOW_31);
            rule__Select_Statement__AttributesAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2()); 

            }

            // InternalCQL.g:2306:2: ( ( rule__Select_Statement__AttributesAssignment_2 )* )
            // InternalCQL.g:2307:3: ( rule__Select_Statement__AttributesAssignment_2 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2()); 
            // InternalCQL.g:2308:3: ( rule__Select_Statement__AttributesAssignment_2 )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==RULE_ID) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // InternalCQL.g:2308:4: rule__Select_Statement__AttributesAssignment_2
            	    {
            	    pushFollow(FOLLOW_31);
            	    rule__Select_Statement__AttributesAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

             after(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2()); 

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
    // $ANTLR end "rule__Select_Statement__Group__2__Impl"


    // $ANTLR start "rule__Select_Statement__Group__3"
    // InternalCQL.g:2317:1: rule__Select_Statement__Group__3 : rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 ;
    public final void rule__Select_Statement__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2321:1: ( rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 )
            // InternalCQL.g:2322:2: rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4
            {
            pushFollow(FOLLOW_30);
            rule__Select_Statement__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__4();

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
    // $ANTLR end "rule__Select_Statement__Group__3"


    // $ANTLR start "rule__Select_Statement__Group__3__Impl"
    // InternalCQL.g:2329:1: rule__Select_Statement__Group__3__Impl : ( ( rule__Select_Statement__Group_3__0 )* ) ;
    public final void rule__Select_Statement__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2333:1: ( ( ( rule__Select_Statement__Group_3__0 )* ) )
            // InternalCQL.g:2334:1: ( ( rule__Select_Statement__Group_3__0 )* )
            {
            // InternalCQL.g:2334:1: ( ( rule__Select_Statement__Group_3__0 )* )
            // InternalCQL.g:2335:2: ( rule__Select_Statement__Group_3__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_3()); 
            // InternalCQL.g:2336:2: ( rule__Select_Statement__Group_3__0 )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==33) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // InternalCQL.g:2336:3: rule__Select_Statement__Group_3__0
            	    {
            	    pushFollow(FOLLOW_32);
            	    rule__Select_Statement__Group_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

             after(grammarAccess.getSelect_StatementAccess().getGroup_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group__3__Impl"


    // $ANTLR start "rule__Select_Statement__Group__4"
    // InternalCQL.g:2344:1: rule__Select_Statement__Group__4 : rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 ;
    public final void rule__Select_Statement__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2348:1: ( rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 )
            // InternalCQL.g:2349:2: rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5
            {
            pushFollow(FOLLOW_9);
            rule__Select_Statement__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__5();

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
    // $ANTLR end "rule__Select_Statement__Group__4"


    // $ANTLR start "rule__Select_Statement__Group__4__Impl"
    // InternalCQL.g:2356:1: rule__Select_Statement__Group__4__Impl : ( 'FROM' ) ;
    public final void rule__Select_Statement__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2360:1: ( ( 'FROM' ) )
            // InternalCQL.g:2361:1: ( 'FROM' )
            {
            // InternalCQL.g:2361:1: ( 'FROM' )
            // InternalCQL.g:2362:2: 'FROM'
            {
             before(grammarAccess.getSelect_StatementAccess().getFROMKeyword_4()); 
            match(input,32,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getFROMKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group__4__Impl"


    // $ANTLR start "rule__Select_Statement__Group__5"
    // InternalCQL.g:2371:1: rule__Select_Statement__Group__5 : rule__Select_Statement__Group__5__Impl rule__Select_Statement__Group__6 ;
    public final void rule__Select_Statement__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2375:1: ( rule__Select_Statement__Group__5__Impl rule__Select_Statement__Group__6 )
            // InternalCQL.g:2376:2: rule__Select_Statement__Group__5__Impl rule__Select_Statement__Group__6
            {
            pushFollow(FOLLOW_33);
            rule__Select_Statement__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__6();

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
    // $ANTLR end "rule__Select_Statement__Group__5"


    // $ANTLR start "rule__Select_Statement__Group__5__Impl"
    // InternalCQL.g:2383:1: rule__Select_Statement__Group__5__Impl : ( ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* ) ) ;
    public final void rule__Select_Statement__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2387:1: ( ( ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* ) ) )
            // InternalCQL.g:2388:1: ( ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* ) )
            {
            // InternalCQL.g:2388:1: ( ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* ) )
            // InternalCQL.g:2389:2: ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* )
            {
            // InternalCQL.g:2389:2: ( ( rule__Select_Statement__SourcesAssignment_5 ) )
            // InternalCQL.g:2390:3: ( rule__Select_Statement__SourcesAssignment_5 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_5()); 
            // InternalCQL.g:2391:3: ( rule__Select_Statement__SourcesAssignment_5 )
            // InternalCQL.g:2391:4: rule__Select_Statement__SourcesAssignment_5
            {
            pushFollow(FOLLOW_34);
            rule__Select_Statement__SourcesAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_5()); 

            }

            // InternalCQL.g:2394:2: ( ( rule__Select_Statement__SourcesAssignment_5 )* )
            // InternalCQL.g:2395:3: ( rule__Select_Statement__SourcesAssignment_5 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_5()); 
            // InternalCQL.g:2396:3: ( rule__Select_Statement__SourcesAssignment_5 )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==RULE_ID) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // InternalCQL.g:2396:4: rule__Select_Statement__SourcesAssignment_5
            	    {
            	    pushFollow(FOLLOW_34);
            	    rule__Select_Statement__SourcesAssignment_5();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_5()); 

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
    // $ANTLR end "rule__Select_Statement__Group__5__Impl"


    // $ANTLR start "rule__Select_Statement__Group__6"
    // InternalCQL.g:2405:1: rule__Select_Statement__Group__6 : rule__Select_Statement__Group__6__Impl rule__Select_Statement__Group__7 ;
    public final void rule__Select_Statement__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2409:1: ( rule__Select_Statement__Group__6__Impl rule__Select_Statement__Group__7 )
            // InternalCQL.g:2410:2: rule__Select_Statement__Group__6__Impl rule__Select_Statement__Group__7
            {
            pushFollow(FOLLOW_33);
            rule__Select_Statement__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__7();

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
    // $ANTLR end "rule__Select_Statement__Group__6"


    // $ANTLR start "rule__Select_Statement__Group__6__Impl"
    // InternalCQL.g:2417:1: rule__Select_Statement__Group__6__Impl : ( ( rule__Select_Statement__Group_6__0 )* ) ;
    public final void rule__Select_Statement__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2421:1: ( ( ( rule__Select_Statement__Group_6__0 )* ) )
            // InternalCQL.g:2422:1: ( ( rule__Select_Statement__Group_6__0 )* )
            {
            // InternalCQL.g:2422:1: ( ( rule__Select_Statement__Group_6__0 )* )
            // InternalCQL.g:2423:2: ( rule__Select_Statement__Group_6__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_6()); 
            // InternalCQL.g:2424:2: ( rule__Select_Statement__Group_6__0 )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==33) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // InternalCQL.g:2424:3: rule__Select_Statement__Group_6__0
            	    {
            	    pushFollow(FOLLOW_32);
            	    rule__Select_Statement__Group_6__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);

             after(grammarAccess.getSelect_StatementAccess().getGroup_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group__6__Impl"


    // $ANTLR start "rule__Select_Statement__Group__7"
    // InternalCQL.g:2432:1: rule__Select_Statement__Group__7 : rule__Select_Statement__Group__7__Impl ;
    public final void rule__Select_Statement__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2436:1: ( rule__Select_Statement__Group__7__Impl )
            // InternalCQL.g:2437:2: rule__Select_Statement__Group__7__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__7__Impl();

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
    // $ANTLR end "rule__Select_Statement__Group__7"


    // $ANTLR start "rule__Select_Statement__Group__7__Impl"
    // InternalCQL.g:2443:1: rule__Select_Statement__Group__7__Impl : ( ( rule__Select_Statement__Group_7__0 ) ) ;
    public final void rule__Select_Statement__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2447:1: ( ( ( rule__Select_Statement__Group_7__0 ) ) )
            // InternalCQL.g:2448:1: ( ( rule__Select_Statement__Group_7__0 ) )
            {
            // InternalCQL.g:2448:1: ( ( rule__Select_Statement__Group_7__0 ) )
            // InternalCQL.g:2449:2: ( rule__Select_Statement__Group_7__0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_7()); 
            // InternalCQL.g:2450:2: ( rule__Select_Statement__Group_7__0 )
            // InternalCQL.g:2450:3: rule__Select_Statement__Group_7__0
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_7__0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getGroup_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group__7__Impl"


    // $ANTLR start "rule__Select_Statement__Group_3__0"
    // InternalCQL.g:2459:1: rule__Select_Statement__Group_3__0 : rule__Select_Statement__Group_3__0__Impl rule__Select_Statement__Group_3__1 ;
    public final void rule__Select_Statement__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2463:1: ( rule__Select_Statement__Group_3__0__Impl rule__Select_Statement__Group_3__1 )
            // InternalCQL.g:2464:2: rule__Select_Statement__Group_3__0__Impl rule__Select_Statement__Group_3__1
            {
            pushFollow(FOLLOW_29);
            rule__Select_Statement__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_3__1();

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
    // $ANTLR end "rule__Select_Statement__Group_3__0"


    // $ANTLR start "rule__Select_Statement__Group_3__0__Impl"
    // InternalCQL.g:2471:1: rule__Select_Statement__Group_3__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2475:1: ( ( ',' ) )
            // InternalCQL.g:2476:1: ( ',' )
            {
            // InternalCQL.g:2476:1: ( ',' )
            // InternalCQL.g:2477:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_3_0()); 
            match(input,33,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getCommaKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_3__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group_3__1"
    // InternalCQL.g:2486:1: rule__Select_Statement__Group_3__1 : rule__Select_Statement__Group_3__1__Impl ;
    public final void rule__Select_Statement__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2490:1: ( rule__Select_Statement__Group_3__1__Impl )
            // InternalCQL.g:2491:2: rule__Select_Statement__Group_3__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_3__1__Impl();

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
    // $ANTLR end "rule__Select_Statement__Group_3__1"


    // $ANTLR start "rule__Select_Statement__Group_3__1__Impl"
    // InternalCQL.g:2497:1: rule__Select_Statement__Group_3__1__Impl : ( ( rule__Select_Statement__AttributesAssignment_3_1 ) ) ;
    public final void rule__Select_Statement__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2501:1: ( ( ( rule__Select_Statement__AttributesAssignment_3_1 ) ) )
            // InternalCQL.g:2502:1: ( ( rule__Select_Statement__AttributesAssignment_3_1 ) )
            {
            // InternalCQL.g:2502:1: ( ( rule__Select_Statement__AttributesAssignment_3_1 ) )
            // InternalCQL.g:2503:2: ( rule__Select_Statement__AttributesAssignment_3_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_3_1()); 
            // InternalCQL.g:2504:2: ( rule__Select_Statement__AttributesAssignment_3_1 )
            // InternalCQL.g:2504:3: rule__Select_Statement__AttributesAssignment_3_1
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__AttributesAssignment_3_1();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_3__1__Impl"


    // $ANTLR start "rule__Select_Statement__Group_6__0"
    // InternalCQL.g:2513:1: rule__Select_Statement__Group_6__0 : rule__Select_Statement__Group_6__0__Impl rule__Select_Statement__Group_6__1 ;
    public final void rule__Select_Statement__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2517:1: ( rule__Select_Statement__Group_6__0__Impl rule__Select_Statement__Group_6__1 )
            // InternalCQL.g:2518:2: rule__Select_Statement__Group_6__0__Impl rule__Select_Statement__Group_6__1
            {
            pushFollow(FOLLOW_9);
            rule__Select_Statement__Group_6__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_6__1();

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
    // $ANTLR end "rule__Select_Statement__Group_6__0"


    // $ANTLR start "rule__Select_Statement__Group_6__0__Impl"
    // InternalCQL.g:2525:1: rule__Select_Statement__Group_6__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2529:1: ( ( ',' ) )
            // InternalCQL.g:2530:1: ( ',' )
            {
            // InternalCQL.g:2530:1: ( ',' )
            // InternalCQL.g:2531:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_6_0()); 
            match(input,33,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getCommaKeyword_6_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_6__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group_6__1"
    // InternalCQL.g:2540:1: rule__Select_Statement__Group_6__1 : rule__Select_Statement__Group_6__1__Impl ;
    public final void rule__Select_Statement__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2544:1: ( rule__Select_Statement__Group_6__1__Impl )
            // InternalCQL.g:2545:2: rule__Select_Statement__Group_6__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_6__1__Impl();

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
    // $ANTLR end "rule__Select_Statement__Group_6__1"


    // $ANTLR start "rule__Select_Statement__Group_6__1__Impl"
    // InternalCQL.g:2551:1: rule__Select_Statement__Group_6__1__Impl : ( ( rule__Select_Statement__SourcesAssignment_6_1 ) ) ;
    public final void rule__Select_Statement__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2555:1: ( ( ( rule__Select_Statement__SourcesAssignment_6_1 ) ) )
            // InternalCQL.g:2556:1: ( ( rule__Select_Statement__SourcesAssignment_6_1 ) )
            {
            // InternalCQL.g:2556:1: ( ( rule__Select_Statement__SourcesAssignment_6_1 ) )
            // InternalCQL.g:2557:2: ( rule__Select_Statement__SourcesAssignment_6_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_6_1()); 
            // InternalCQL.g:2558:2: ( rule__Select_Statement__SourcesAssignment_6_1 )
            // InternalCQL.g:2558:3: rule__Select_Statement__SourcesAssignment_6_1
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__SourcesAssignment_6_1();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_6_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_6__1__Impl"


    // $ANTLR start "rule__Select_Statement__Group_7__0"
    // InternalCQL.g:2567:1: rule__Select_Statement__Group_7__0 : rule__Select_Statement__Group_7__0__Impl rule__Select_Statement__Group_7__1 ;
    public final void rule__Select_Statement__Group_7__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2571:1: ( rule__Select_Statement__Group_7__0__Impl rule__Select_Statement__Group_7__1 )
            // InternalCQL.g:2572:2: rule__Select_Statement__Group_7__0__Impl rule__Select_Statement__Group_7__1
            {
            pushFollow(FOLLOW_11);
            rule__Select_Statement__Group_7__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_7__1();

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
    // $ANTLR end "rule__Select_Statement__Group_7__0"


    // $ANTLR start "rule__Select_Statement__Group_7__0__Impl"
    // InternalCQL.g:2579:1: rule__Select_Statement__Group_7__0__Impl : ( 'WHERE' ) ;
    public final void rule__Select_Statement__Group_7__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2583:1: ( ( 'WHERE' ) )
            // InternalCQL.g:2584:1: ( 'WHERE' )
            {
            // InternalCQL.g:2584:1: ( 'WHERE' )
            // InternalCQL.g:2585:2: 'WHERE'
            {
             before(grammarAccess.getSelect_StatementAccess().getWHEREKeyword_7_0()); 
            match(input,34,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getWHEREKeyword_7_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_7__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group_7__1"
    // InternalCQL.g:2594:1: rule__Select_Statement__Group_7__1 : rule__Select_Statement__Group_7__1__Impl ;
    public final void rule__Select_Statement__Group_7__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2598:1: ( rule__Select_Statement__Group_7__1__Impl )
            // InternalCQL.g:2599:2: rule__Select_Statement__Group_7__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_7__1__Impl();

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
    // $ANTLR end "rule__Select_Statement__Group_7__1"


    // $ANTLR start "rule__Select_Statement__Group_7__1__Impl"
    // InternalCQL.g:2605:1: rule__Select_Statement__Group_7__1__Impl : ( ( rule__Select_Statement__PredicatesAssignment_7_1 ) ) ;
    public final void rule__Select_Statement__Group_7__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2609:1: ( ( ( rule__Select_Statement__PredicatesAssignment_7_1 ) ) )
            // InternalCQL.g:2610:1: ( ( rule__Select_Statement__PredicatesAssignment_7_1 ) )
            {
            // InternalCQL.g:2610:1: ( ( rule__Select_Statement__PredicatesAssignment_7_1 ) )
            // InternalCQL.g:2611:2: ( rule__Select_Statement__PredicatesAssignment_7_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getPredicatesAssignment_7_1()); 
            // InternalCQL.g:2612:2: ( rule__Select_Statement__PredicatesAssignment_7_1 )
            // InternalCQL.g:2612:3: rule__Select_Statement__PredicatesAssignment_7_1
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__PredicatesAssignment_7_1();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getPredicatesAssignment_7_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_7__1__Impl"


    // $ANTLR start "rule__Model__StatementsAssignment"
    // InternalCQL.g:2621:1: rule__Model__StatementsAssignment : ( ruleStatement ) ;
    public final void rule__Model__StatementsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2625:1: ( ( ruleStatement ) )
            // InternalCQL.g:2626:2: ( ruleStatement )
            {
            // InternalCQL.g:2626:2: ( ruleStatement )
            // InternalCQL.g:2627:3: ruleStatement
            {
             before(grammarAccess.getModelAccess().getStatementsStatementParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            ruleStatement();

            state._fsp--;

             after(grammarAccess.getModelAccess().getStatementsStatementParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__StatementsAssignment"


    // $ANTLR start "rule__Statement__TypeAssignment_0_0"
    // InternalCQL.g:2636:1: rule__Statement__TypeAssignment_0_0 : ( ruleSelect_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2640:1: ( ( ruleSelect_Statement ) )
            // InternalCQL.g:2641:2: ( ruleSelect_Statement )
            {
            // InternalCQL.g:2641:2: ( ruleSelect_Statement )
            // InternalCQL.g:2642:3: ruleSelect_Statement
            {
             before(grammarAccess.getStatementAccess().getTypeSelect_StatementParserRuleCall_0_0_0()); 
            pushFollow(FOLLOW_2);
            ruleSelect_Statement();

            state._fsp--;

             after(grammarAccess.getStatementAccess().getTypeSelect_StatementParserRuleCall_0_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Statement__TypeAssignment_0_0"


    // $ANTLR start "rule__Statement__TypeAssignment_0_1"
    // InternalCQL.g:2651:1: rule__Statement__TypeAssignment_0_1 : ( ruleCreate_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2655:1: ( ( ruleCreate_Statement ) )
            // InternalCQL.g:2656:2: ( ruleCreate_Statement )
            {
            // InternalCQL.g:2656:2: ( ruleCreate_Statement )
            // InternalCQL.g:2657:3: ruleCreate_Statement
            {
             before(grammarAccess.getStatementAccess().getTypeCreate_StatementParserRuleCall_0_1_0()); 
            pushFollow(FOLLOW_2);
            ruleCreate_Statement();

            state._fsp--;

             after(grammarAccess.getStatementAccess().getTypeCreate_StatementParserRuleCall_0_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Statement__TypeAssignment_0_1"


    // $ANTLR start "rule__Atomic__ValueAssignment_0_1"
    // InternalCQL.g:2666:1: rule__Atomic__ValueAssignment_0_1 : ( RULE_INT ) ;
    public final void rule__Atomic__ValueAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2670:1: ( ( RULE_INT ) )
            // InternalCQL.g:2671:2: ( RULE_INT )
            {
            // InternalCQL.g:2671:2: ( RULE_INT )
            // InternalCQL.g:2672:3: RULE_INT
            {
             before(grammarAccess.getAtomicAccess().getValueINTTerminalRuleCall_0_1_0()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getAtomicAccess().getValueINTTerminalRuleCall_0_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__ValueAssignment_0_1"


    // $ANTLR start "rule__Atomic__ValueAssignment_1_1"
    // InternalCQL.g:2681:1: rule__Atomic__ValueAssignment_1_1 : ( RULE_FLOAT_NUMBER ) ;
    public final void rule__Atomic__ValueAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2685:1: ( ( RULE_FLOAT_NUMBER ) )
            // InternalCQL.g:2686:2: ( RULE_FLOAT_NUMBER )
            {
            // InternalCQL.g:2686:2: ( RULE_FLOAT_NUMBER )
            // InternalCQL.g:2687:3: RULE_FLOAT_NUMBER
            {
             before(grammarAccess.getAtomicAccess().getValueFLOAT_NUMBERTerminalRuleCall_1_1_0()); 
            match(input,RULE_FLOAT_NUMBER,FOLLOW_2); 
             after(grammarAccess.getAtomicAccess().getValueFLOAT_NUMBERTerminalRuleCall_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__ValueAssignment_1_1"


    // $ANTLR start "rule__Atomic__ValueAssignment_2_1"
    // InternalCQL.g:2696:1: rule__Atomic__ValueAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Atomic__ValueAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2700:1: ( ( RULE_STRING ) )
            // InternalCQL.g:2701:2: ( RULE_STRING )
            {
            // InternalCQL.g:2701:2: ( RULE_STRING )
            // InternalCQL.g:2702:3: RULE_STRING
            {
             before(grammarAccess.getAtomicAccess().getValueSTRINGTerminalRuleCall_2_1_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getAtomicAccess().getValueSTRINGTerminalRuleCall_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__ValueAssignment_2_1"


    // $ANTLR start "rule__Atomic__ValueAssignment_3_1"
    // InternalCQL.g:2711:1: rule__Atomic__ValueAssignment_3_1 : ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) ;
    public final void rule__Atomic__ValueAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2715:1: ( ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) )
            // InternalCQL.g:2716:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            {
            // InternalCQL.g:2716:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            // InternalCQL.g:2717:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            {
             before(grammarAccess.getAtomicAccess().getValueAlternatives_3_1_0()); 
            // InternalCQL.g:2718:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            // InternalCQL.g:2718:4: rule__Atomic__ValueAlternatives_3_1_0
            {
            pushFollow(FOLLOW_2);
            rule__Atomic__ValueAlternatives_3_1_0();

            state._fsp--;


            }

             after(grammarAccess.getAtomicAccess().getValueAlternatives_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__ValueAssignment_3_1"


    // $ANTLR start "rule__Atomic__ValueAssignment_4_1"
    // InternalCQL.g:2726:1: rule__Atomic__ValueAssignment_4_1 : ( ( RULE_ID ) ) ;
    public final void rule__Atomic__ValueAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2730:1: ( ( ( RULE_ID ) ) )
            // InternalCQL.g:2731:2: ( ( RULE_ID ) )
            {
            // InternalCQL.g:2731:2: ( ( RULE_ID ) )
            // InternalCQL.g:2732:3: ( RULE_ID )
            {
             before(grammarAccess.getAtomicAccess().getValueAttributeCrossReference_4_1_0()); 
            // InternalCQL.g:2733:3: ( RULE_ID )
            // InternalCQL.g:2734:4: RULE_ID
            {
             before(grammarAccess.getAtomicAccess().getValueAttributeIDTerminalRuleCall_4_1_0_1()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getAtomicAccess().getValueAttributeIDTerminalRuleCall_4_1_0_1()); 

            }

             after(grammarAccess.getAtomicAccess().getValueAttributeCrossReference_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Atomic__ValueAssignment_4_1"


    // $ANTLR start "rule__Source__NameAssignment"
    // InternalCQL.g:2745:1: rule__Source__NameAssignment : ( RULE_ID ) ;
    public final void rule__Source__NameAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2749:1: ( ( RULE_ID ) )
            // InternalCQL.g:2750:2: ( RULE_ID )
            {
            // InternalCQL.g:2750:2: ( RULE_ID )
            // InternalCQL.g:2751:3: RULE_ID
            {
             before(grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Source__NameAssignment"


    // $ANTLR start "rule__Attribute__SourceAssignment_0_0"
    // InternalCQL.g:2760:1: rule__Attribute__SourceAssignment_0_0 : ( ( RULE_ID ) ) ;
    public final void rule__Attribute__SourceAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2764:1: ( ( ( RULE_ID ) ) )
            // InternalCQL.g:2765:2: ( ( RULE_ID ) )
            {
            // InternalCQL.g:2765:2: ( ( RULE_ID ) )
            // InternalCQL.g:2766:3: ( RULE_ID )
            {
             before(grammarAccess.getAttributeAccess().getSourceSourceCrossReference_0_0_0()); 
            // InternalCQL.g:2767:3: ( RULE_ID )
            // InternalCQL.g:2768:4: RULE_ID
            {
             before(grammarAccess.getAttributeAccess().getSourceSourceIDTerminalRuleCall_0_0_0_1()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getAttributeAccess().getSourceSourceIDTerminalRuleCall_0_0_0_1()); 

            }

             after(grammarAccess.getAttributeAccess().getSourceSourceCrossReference_0_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Attribute__SourceAssignment_0_0"


    // $ANTLR start "rule__Attribute__NameAssignment_1"
    // InternalCQL.g:2779:1: rule__Attribute__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__Attribute__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2783:1: ( ( RULE_ID ) )
            // InternalCQL.g:2784:2: ( RULE_ID )
            {
            // InternalCQL.g:2784:2: ( RULE_ID )
            // InternalCQL.g:2785:3: RULE_ID
            {
             before(grammarAccess.getAttributeAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getAttributeAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Attribute__NameAssignment_1"


    // $ANTLR start "rule__ExpressionsModel__ElementsAssignment_1"
    // InternalCQL.g:2794:1: rule__ExpressionsModel__ElementsAssignment_1 : ( ruleExpression ) ;
    public final void rule__ExpressionsModel__ElementsAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2798:1: ( ( ruleExpression ) )
            // InternalCQL.g:2799:2: ( ruleExpression )
            {
            // InternalCQL.g:2799:2: ( ruleExpression )
            // InternalCQL.g:2800:3: ruleExpression
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsExpressionParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
            ruleExpression();

            state._fsp--;

             after(grammarAccess.getExpressionsModelAccess().getElementsExpressionParserRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ExpressionsModel__ElementsAssignment_1"


    // $ANTLR start "rule__Or__RightAssignment_1_2"
    // InternalCQL.g:2809:1: rule__Or__RightAssignment_1_2 : ( ruleAnd ) ;
    public final void rule__Or__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2813:1: ( ( ruleAnd ) )
            // InternalCQL.g:2814:2: ( ruleAnd )
            {
            // InternalCQL.g:2814:2: ( ruleAnd )
            // InternalCQL.g:2815:3: ruleAnd
            {
             before(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0()); 
            pushFollow(FOLLOW_2);
            ruleAnd();

            state._fsp--;

             after(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Or__RightAssignment_1_2"


    // $ANTLR start "rule__And__RightAssignment_1_2"
    // InternalCQL.g:2824:1: rule__And__RightAssignment_1_2 : ( ruleEqualitiy ) ;
    public final void rule__And__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2828:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:2829:2: ( ruleEqualitiy )
            {
            // InternalCQL.g:2829:2: ( ruleEqualitiy )
            // InternalCQL.g:2830:3: ruleEqualitiy
            {
             before(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0()); 
            pushFollow(FOLLOW_2);
            ruleEqualitiy();

            state._fsp--;

             after(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__And__RightAssignment_1_2"


    // $ANTLR start "rule__Equalitiy__OpAssignment_1_1"
    // InternalCQL.g:2839:1: rule__Equalitiy__OpAssignment_1_1 : ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Equalitiy__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2843:1: ( ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:2844:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:2844:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:2845:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:2846:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            // InternalCQL.g:2846:4: rule__Equalitiy__OpAlternatives_1_1_0
            {
            pushFollow(FOLLOW_2);
            rule__Equalitiy__OpAlternatives_1_1_0();

            state._fsp--;


            }

             after(grammarAccess.getEqualitiyAccess().getOpAlternatives_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Equalitiy__OpAssignment_1_1"


    // $ANTLR start "rule__Equalitiy__RightAssignment_1_2"
    // InternalCQL.g:2854:1: rule__Equalitiy__RightAssignment_1_2 : ( ruleComparison ) ;
    public final void rule__Equalitiy__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2858:1: ( ( ruleComparison ) )
            // InternalCQL.g:2859:2: ( ruleComparison )
            {
            // InternalCQL.g:2859:2: ( ruleComparison )
            // InternalCQL.g:2860:3: ruleComparison
            {
             before(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0()); 
            pushFollow(FOLLOW_2);
            ruleComparison();

            state._fsp--;

             after(grammarAccess.getEqualitiyAccess().getRightComparisonParserRuleCall_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Equalitiy__RightAssignment_1_2"


    // $ANTLR start "rule__Comparison__OpAssignment_1_1"
    // InternalCQL.g:2869:1: rule__Comparison__OpAssignment_1_1 : ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Comparison__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2873:1: ( ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:2874:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:2874:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:2875:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getComparisonAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:2876:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            // InternalCQL.g:2876:4: rule__Comparison__OpAlternatives_1_1_0
            {
            pushFollow(FOLLOW_2);
            rule__Comparison__OpAlternatives_1_1_0();

            state._fsp--;


            }

             after(grammarAccess.getComparisonAccess().getOpAlternatives_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Comparison__OpAssignment_1_1"


    // $ANTLR start "rule__Comparison__RightAssignment_1_2"
    // InternalCQL.g:2884:1: rule__Comparison__RightAssignment_1_2 : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2888:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:2889:2: ( rulePlusOrMinus )
            {
            // InternalCQL.g:2889:2: ( rulePlusOrMinus )
            // InternalCQL.g:2890:3: rulePlusOrMinus
            {
             before(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0()); 
            pushFollow(FOLLOW_2);
            rulePlusOrMinus();

            state._fsp--;

             after(grammarAccess.getComparisonAccess().getRightPlusOrMinusParserRuleCall_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Comparison__RightAssignment_1_2"


    // $ANTLR start "rule__PlusOrMinus__RightAssignment_1_1"
    // InternalCQL.g:2899:1: rule__PlusOrMinus__RightAssignment_1_1 : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__RightAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2903:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:2904:2: ( ruleMulOrDiv )
            {
            // InternalCQL.g:2904:2: ( ruleMulOrDiv )
            // InternalCQL.g:2905:3: ruleMulOrDiv
            {
             before(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0()); 
            pushFollow(FOLLOW_2);
            ruleMulOrDiv();

            state._fsp--;

             after(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlusOrMinus__RightAssignment_1_1"


    // $ANTLR start "rule__MulOrDiv__OpAssignment_1_1"
    // InternalCQL.g:2914:1: rule__MulOrDiv__OpAssignment_1_1 : ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) ;
    public final void rule__MulOrDiv__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2918:1: ( ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:2919:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:2919:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:2920:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:2921:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            // InternalCQL.g:2921:4: rule__MulOrDiv__OpAlternatives_1_1_0
            {
            pushFollow(FOLLOW_2);
            rule__MulOrDiv__OpAlternatives_1_1_0();

            state._fsp--;


            }

             after(grammarAccess.getMulOrDivAccess().getOpAlternatives_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MulOrDiv__OpAssignment_1_1"


    // $ANTLR start "rule__MulOrDiv__RightAssignment_1_2"
    // InternalCQL.g:2929:1: rule__MulOrDiv__RightAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__MulOrDiv__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2933:1: ( ( rulePrimary ) )
            // InternalCQL.g:2934:2: ( rulePrimary )
            {
            // InternalCQL.g:2934:2: ( rulePrimary )
            // InternalCQL.g:2935:3: rulePrimary
            {
             before(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0()); 
            pushFollow(FOLLOW_2);
            rulePrimary();

            state._fsp--;

             after(grammarAccess.getMulOrDivAccess().getRightPrimaryParserRuleCall_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__MulOrDiv__RightAssignment_1_2"


    // $ANTLR start "rule__Primary__InnerAssignment_0_2"
    // InternalCQL.g:2944:1: rule__Primary__InnerAssignment_0_2 : ( ruleExpression ) ;
    public final void rule__Primary__InnerAssignment_0_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2948:1: ( ( ruleExpression ) )
            // InternalCQL.g:2949:2: ( ruleExpression )
            {
            // InternalCQL.g:2949:2: ( ruleExpression )
            // InternalCQL.g:2950:3: ruleExpression
            {
             before(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0()); 
            pushFollow(FOLLOW_2);
            ruleExpression();

            state._fsp--;

             after(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Primary__InnerAssignment_0_2"


    // $ANTLR start "rule__Primary__ExpressionAssignment_1_2"
    // InternalCQL.g:2959:1: rule__Primary__ExpressionAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__Primary__ExpressionAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2963:1: ( ( rulePrimary ) )
            // InternalCQL.g:2964:2: ( rulePrimary )
            {
            // InternalCQL.g:2964:2: ( rulePrimary )
            // InternalCQL.g:2965:3: rulePrimary
            {
             before(grammarAccess.getPrimaryAccess().getExpressionPrimaryParserRuleCall_1_2_0()); 
            pushFollow(FOLLOW_2);
            rulePrimary();

            state._fsp--;

             after(grammarAccess.getPrimaryAccess().getExpressionPrimaryParserRuleCall_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Primary__ExpressionAssignment_1_2"


    // $ANTLR start "rule__Select_Statement__NameAssignment_0"
    // InternalCQL.g:2974:1: rule__Select_Statement__NameAssignment_0 : ( ( 'SELECT' ) ) ;
    public final void rule__Select_Statement__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2978:1: ( ( ( 'SELECT' ) ) )
            // InternalCQL.g:2979:2: ( ( 'SELECT' ) )
            {
            // InternalCQL.g:2979:2: ( ( 'SELECT' ) )
            // InternalCQL.g:2980:3: ( 'SELECT' )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            // InternalCQL.g:2981:3: ( 'SELECT' )
            // InternalCQL.g:2982:4: 'SELECT'
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            match(input,35,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 

            }

             after(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__NameAssignment_0"


    // $ANTLR start "rule__Select_Statement__AttributesAssignment_2"
    // InternalCQL.g:2993:1: rule__Select_Statement__AttributesAssignment_2 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2997:1: ( ( ruleAttribute ) )
            // InternalCQL.g:2998:2: ( ruleAttribute )
            {
            // InternalCQL.g:2998:2: ( ruleAttribute )
            // InternalCQL.g:2999:3: ruleAttribute
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__AttributesAssignment_2"


    // $ANTLR start "rule__Select_Statement__AttributesAssignment_3_1"
    // InternalCQL.g:3008:1: rule__Select_Statement__AttributesAssignment_3_1 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3012:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3013:2: ( ruleAttribute )
            {
            // InternalCQL.g:3013:2: ( ruleAttribute )
            // InternalCQL.g:3014:3: ruleAttribute
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_3_1_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__AttributesAssignment_3_1"


    // $ANTLR start "rule__Select_Statement__SourcesAssignment_5"
    // InternalCQL.g:3023:1: rule__Select_Statement__SourcesAssignment_5 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3027:1: ( ( ruleSource ) )
            // InternalCQL.g:3028:2: ( ruleSource )
            {
            // InternalCQL.g:3028:2: ( ruleSource )
            // InternalCQL.g:3029:3: ruleSource
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_5_0()); 
            pushFollow(FOLLOW_2);
            ruleSource();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__SourcesAssignment_5"


    // $ANTLR start "rule__Select_Statement__SourcesAssignment_6_1"
    // InternalCQL.g:3038:1: rule__Select_Statement__SourcesAssignment_6_1 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3042:1: ( ( ruleSource ) )
            // InternalCQL.g:3043:2: ( ruleSource )
            {
            // InternalCQL.g:3043:2: ( ruleSource )
            // InternalCQL.g:3044:3: ruleSource
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_6_1_0()); 
            pushFollow(FOLLOW_2);
            ruleSource();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_6_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__SourcesAssignment_6_1"


    // $ANTLR start "rule__Select_Statement__PredicatesAssignment_7_1"
    // InternalCQL.g:3053:1: rule__Select_Statement__PredicatesAssignment_7_1 : ( ruleExpressionsModel ) ;
    public final void rule__Select_Statement__PredicatesAssignment_7_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3057:1: ( ( ruleExpressionsModel ) )
            // InternalCQL.g:3058:2: ( ruleExpressionsModel )
            {
            // InternalCQL.g:3058:2: ( ruleExpressionsModel )
            // InternalCQL.g:3059:3: ruleExpressionsModel
            {
             before(grammarAccess.getSelect_StatementAccess().getPredicatesExpressionsModelParserRuleCall_7_1_0()); 
            pushFollow(FOLLOW_2);
            ruleExpressionsModel();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getPredicatesExpressionsModelParserRuleCall_7_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__PredicatesAssignment_7_1"


    // $ANTLR start "rule__Create_Statement__NameAssignment"
    // InternalCQL.g:3068:1: rule__Create_Statement__NameAssignment : ( ( 'CREATE' ) ) ;
    public final void rule__Create_Statement__NameAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3072:1: ( ( ( 'CREATE' ) ) )
            // InternalCQL.g:3073:2: ( ( 'CREATE' ) )
            {
            // InternalCQL.g:3073:2: ( ( 'CREATE' ) )
            // InternalCQL.g:3074:3: ( 'CREATE' )
            {
             before(grammarAccess.getCreate_StatementAccess().getNameCREATEKeyword_0()); 
            // InternalCQL.g:3075:3: ( 'CREATE' )
            // InternalCQL.g:3076:4: 'CREATE'
            {
             before(grammarAccess.getCreate_StatementAccess().getNameCREATEKeyword_0()); 
            match(input,36,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getNameCREATEKeyword_0()); 

            }

             after(grammarAccess.getCreate_StatementAccess().getNameCREATEKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__NameAssignment"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000003000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x00000000500030F0L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x00000000500030F2L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x000000000000C002L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x00000000000F0000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x00000000000F0002L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x000000000C000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x000000000C000002L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000000300002L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000080000080L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000300000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000080000082L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000082L});

}