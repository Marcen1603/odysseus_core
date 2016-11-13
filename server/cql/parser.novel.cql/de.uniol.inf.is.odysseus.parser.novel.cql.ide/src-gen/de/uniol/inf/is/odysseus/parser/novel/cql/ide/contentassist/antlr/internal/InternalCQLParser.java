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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_INT", "RULE_FLOAT_NUMBER", "RULE_STRING", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'TRUE'", "'FALSE'", "'INTEGER'", "'DOUBLE'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'*'", "'/'", "';'", "'.'", "'OR'", "'AND'", "'+'", "'-'", "'('", "')'", "'NOT'", "'DISTINCT'", "'FROM'", "','", "'WHERE'", "'CREATE'", "'STREAM'", "'SELECT'"
    };
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int RULE_ID=7;
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
    public static final int RULE_STRING=6;
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
    public static final int T__44=44;
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

                if ( (LA1_0==42||LA1_0==44) ) {
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


    // $ANTLR start "entryRuleDataType"
    // InternalCQL.g:153:1: entryRuleDataType : ruleDataType EOF ;
    public final void entryRuleDataType() throws RecognitionException {
        try {
            // InternalCQL.g:154:1: ( ruleDataType EOF )
            // InternalCQL.g:155:1: ruleDataType EOF
            {
             before(grammarAccess.getDataTypeRule()); 
            pushFollow(FOLLOW_1);
            ruleDataType();

            state._fsp--;

             after(grammarAccess.getDataTypeRule()); 
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
    // $ANTLR end "entryRuleDataType"


    // $ANTLR start "ruleDataType"
    // InternalCQL.g:162:1: ruleDataType : ( ( rule__DataType__Alternatives ) ) ;
    public final void ruleDataType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:166:2: ( ( ( rule__DataType__Alternatives ) ) )
            // InternalCQL.g:167:2: ( ( rule__DataType__Alternatives ) )
            {
            // InternalCQL.g:167:2: ( ( rule__DataType__Alternatives ) )
            // InternalCQL.g:168:3: ( rule__DataType__Alternatives )
            {
             before(grammarAccess.getDataTypeAccess().getAlternatives()); 
            // InternalCQL.g:169:3: ( rule__DataType__Alternatives )
            // InternalCQL.g:169:4: rule__DataType__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__DataType__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getDataTypeAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleDataType"


    // $ANTLR start "entryRuleAttribute"
    // InternalCQL.g:178:1: entryRuleAttribute : ruleAttribute EOF ;
    public final void entryRuleAttribute() throws RecognitionException {
        try {
            // InternalCQL.g:179:1: ( ruleAttribute EOF )
            // InternalCQL.g:180:1: ruleAttribute EOF
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
    // InternalCQL.g:187:1: ruleAttribute : ( ( rule__Attribute__Group__0 ) ) ;
    public final void ruleAttribute() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:191:2: ( ( ( rule__Attribute__Group__0 ) ) )
            // InternalCQL.g:192:2: ( ( rule__Attribute__Group__0 ) )
            {
            // InternalCQL.g:192:2: ( ( rule__Attribute__Group__0 ) )
            // InternalCQL.g:193:3: ( rule__Attribute__Group__0 )
            {
             before(grammarAccess.getAttributeAccess().getGroup()); 
            // InternalCQL.g:194:3: ( rule__Attribute__Group__0 )
            // InternalCQL.g:194:4: rule__Attribute__Group__0
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
    // InternalCQL.g:203:1: entryRuleExpressionsModel : ruleExpressionsModel EOF ;
    public final void entryRuleExpressionsModel() throws RecognitionException {
        try {
            // InternalCQL.g:204:1: ( ruleExpressionsModel EOF )
            // InternalCQL.g:205:1: ruleExpressionsModel EOF
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
    // InternalCQL.g:212:1: ruleExpressionsModel : ( ( rule__ExpressionsModel__Group__0 ) ) ;
    public final void ruleExpressionsModel() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:216:2: ( ( ( rule__ExpressionsModel__Group__0 ) ) )
            // InternalCQL.g:217:2: ( ( rule__ExpressionsModel__Group__0 ) )
            {
            // InternalCQL.g:217:2: ( ( rule__ExpressionsModel__Group__0 ) )
            // InternalCQL.g:218:3: ( rule__ExpressionsModel__Group__0 )
            {
             before(grammarAccess.getExpressionsModelAccess().getGroup()); 
            // InternalCQL.g:219:3: ( rule__ExpressionsModel__Group__0 )
            // InternalCQL.g:219:4: rule__ExpressionsModel__Group__0
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
    // InternalCQL.g:228:1: entryRuleExpression : ruleExpression EOF ;
    public final void entryRuleExpression() throws RecognitionException {
        try {
            // InternalCQL.g:229:1: ( ruleExpression EOF )
            // InternalCQL.g:230:1: ruleExpression EOF
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
    // InternalCQL.g:237:1: ruleExpression : ( ruleOr ) ;
    public final void ruleExpression() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:241:2: ( ( ruleOr ) )
            // InternalCQL.g:242:2: ( ruleOr )
            {
            // InternalCQL.g:242:2: ( ruleOr )
            // InternalCQL.g:243:3: ruleOr
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
    // InternalCQL.g:253:1: entryRuleOr : ruleOr EOF ;
    public final void entryRuleOr() throws RecognitionException {
        try {
            // InternalCQL.g:254:1: ( ruleOr EOF )
            // InternalCQL.g:255:1: ruleOr EOF
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
    // InternalCQL.g:262:1: ruleOr : ( ( rule__Or__Group__0 ) ) ;
    public final void ruleOr() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:266:2: ( ( ( rule__Or__Group__0 ) ) )
            // InternalCQL.g:267:2: ( ( rule__Or__Group__0 ) )
            {
            // InternalCQL.g:267:2: ( ( rule__Or__Group__0 ) )
            // InternalCQL.g:268:3: ( rule__Or__Group__0 )
            {
             before(grammarAccess.getOrAccess().getGroup()); 
            // InternalCQL.g:269:3: ( rule__Or__Group__0 )
            // InternalCQL.g:269:4: rule__Or__Group__0
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
    // InternalCQL.g:278:1: entryRuleAnd : ruleAnd EOF ;
    public final void entryRuleAnd() throws RecognitionException {
        try {
            // InternalCQL.g:279:1: ( ruleAnd EOF )
            // InternalCQL.g:280:1: ruleAnd EOF
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
    // InternalCQL.g:287:1: ruleAnd : ( ( rule__And__Group__0 ) ) ;
    public final void ruleAnd() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:291:2: ( ( ( rule__And__Group__0 ) ) )
            // InternalCQL.g:292:2: ( ( rule__And__Group__0 ) )
            {
            // InternalCQL.g:292:2: ( ( rule__And__Group__0 ) )
            // InternalCQL.g:293:3: ( rule__And__Group__0 )
            {
             before(grammarAccess.getAndAccess().getGroup()); 
            // InternalCQL.g:294:3: ( rule__And__Group__0 )
            // InternalCQL.g:294:4: rule__And__Group__0
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
    // InternalCQL.g:303:1: entryRuleEqualitiy : ruleEqualitiy EOF ;
    public final void entryRuleEqualitiy() throws RecognitionException {
        try {
            // InternalCQL.g:304:1: ( ruleEqualitiy EOF )
            // InternalCQL.g:305:1: ruleEqualitiy EOF
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
    // InternalCQL.g:312:1: ruleEqualitiy : ( ( rule__Equalitiy__Group__0 ) ) ;
    public final void ruleEqualitiy() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:316:2: ( ( ( rule__Equalitiy__Group__0 ) ) )
            // InternalCQL.g:317:2: ( ( rule__Equalitiy__Group__0 ) )
            {
            // InternalCQL.g:317:2: ( ( rule__Equalitiy__Group__0 ) )
            // InternalCQL.g:318:3: ( rule__Equalitiy__Group__0 )
            {
             before(grammarAccess.getEqualitiyAccess().getGroup()); 
            // InternalCQL.g:319:3: ( rule__Equalitiy__Group__0 )
            // InternalCQL.g:319:4: rule__Equalitiy__Group__0
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
    // InternalCQL.g:328:1: entryRuleComparison : ruleComparison EOF ;
    public final void entryRuleComparison() throws RecognitionException {
        try {
            // InternalCQL.g:329:1: ( ruleComparison EOF )
            // InternalCQL.g:330:1: ruleComparison EOF
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
    // InternalCQL.g:337:1: ruleComparison : ( ( rule__Comparison__Group__0 ) ) ;
    public final void ruleComparison() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:341:2: ( ( ( rule__Comparison__Group__0 ) ) )
            // InternalCQL.g:342:2: ( ( rule__Comparison__Group__0 ) )
            {
            // InternalCQL.g:342:2: ( ( rule__Comparison__Group__0 ) )
            // InternalCQL.g:343:3: ( rule__Comparison__Group__0 )
            {
             before(grammarAccess.getComparisonAccess().getGroup()); 
            // InternalCQL.g:344:3: ( rule__Comparison__Group__0 )
            // InternalCQL.g:344:4: rule__Comparison__Group__0
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
    // InternalCQL.g:353:1: entryRulePlusOrMinus : rulePlusOrMinus EOF ;
    public final void entryRulePlusOrMinus() throws RecognitionException {
        try {
            // InternalCQL.g:354:1: ( rulePlusOrMinus EOF )
            // InternalCQL.g:355:1: rulePlusOrMinus EOF
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
    // InternalCQL.g:362:1: rulePlusOrMinus : ( ( rule__PlusOrMinus__Group__0 ) ) ;
    public final void rulePlusOrMinus() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:366:2: ( ( ( rule__PlusOrMinus__Group__0 ) ) )
            // InternalCQL.g:367:2: ( ( rule__PlusOrMinus__Group__0 ) )
            {
            // InternalCQL.g:367:2: ( ( rule__PlusOrMinus__Group__0 ) )
            // InternalCQL.g:368:3: ( rule__PlusOrMinus__Group__0 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getGroup()); 
            // InternalCQL.g:369:3: ( rule__PlusOrMinus__Group__0 )
            // InternalCQL.g:369:4: rule__PlusOrMinus__Group__0
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
    // InternalCQL.g:378:1: entryRuleMulOrDiv : ruleMulOrDiv EOF ;
    public final void entryRuleMulOrDiv() throws RecognitionException {
        try {
            // InternalCQL.g:379:1: ( ruleMulOrDiv EOF )
            // InternalCQL.g:380:1: ruleMulOrDiv EOF
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
    // InternalCQL.g:387:1: ruleMulOrDiv : ( ( rule__MulOrDiv__Group__0 ) ) ;
    public final void ruleMulOrDiv() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:391:2: ( ( ( rule__MulOrDiv__Group__0 ) ) )
            // InternalCQL.g:392:2: ( ( rule__MulOrDiv__Group__0 ) )
            {
            // InternalCQL.g:392:2: ( ( rule__MulOrDiv__Group__0 ) )
            // InternalCQL.g:393:3: ( rule__MulOrDiv__Group__0 )
            {
             before(grammarAccess.getMulOrDivAccess().getGroup()); 
            // InternalCQL.g:394:3: ( rule__MulOrDiv__Group__0 )
            // InternalCQL.g:394:4: rule__MulOrDiv__Group__0
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
    // InternalCQL.g:403:1: entryRulePrimary : rulePrimary EOF ;
    public final void entryRulePrimary() throws RecognitionException {
        try {
            // InternalCQL.g:404:1: ( rulePrimary EOF )
            // InternalCQL.g:405:1: rulePrimary EOF
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
    // InternalCQL.g:412:1: rulePrimary : ( ( rule__Primary__Alternatives ) ) ;
    public final void rulePrimary() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:416:2: ( ( ( rule__Primary__Alternatives ) ) )
            // InternalCQL.g:417:2: ( ( rule__Primary__Alternatives ) )
            {
            // InternalCQL.g:417:2: ( ( rule__Primary__Alternatives ) )
            // InternalCQL.g:418:3: ( rule__Primary__Alternatives )
            {
             before(grammarAccess.getPrimaryAccess().getAlternatives()); 
            // InternalCQL.g:419:3: ( rule__Primary__Alternatives )
            // InternalCQL.g:419:4: rule__Primary__Alternatives
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
    // InternalCQL.g:428:1: entryRuleSelect_Statement : ruleSelect_Statement EOF ;
    public final void entryRuleSelect_Statement() throws RecognitionException {
        try {
            // InternalCQL.g:429:1: ( ruleSelect_Statement EOF )
            // InternalCQL.g:430:1: ruleSelect_Statement EOF
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
    // InternalCQL.g:437:1: ruleSelect_Statement : ( ( rule__Select_Statement__Group__0 ) ) ;
    public final void ruleSelect_Statement() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:441:2: ( ( ( rule__Select_Statement__Group__0 ) ) )
            // InternalCQL.g:442:2: ( ( rule__Select_Statement__Group__0 ) )
            {
            // InternalCQL.g:442:2: ( ( rule__Select_Statement__Group__0 ) )
            // InternalCQL.g:443:3: ( rule__Select_Statement__Group__0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup()); 
            // InternalCQL.g:444:3: ( rule__Select_Statement__Group__0 )
            // InternalCQL.g:444:4: rule__Select_Statement__Group__0
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
    // InternalCQL.g:453:1: entryRuleCreate_Statement : ruleCreate_Statement EOF ;
    public final void entryRuleCreate_Statement() throws RecognitionException {
        try {
            // InternalCQL.g:454:1: ( ruleCreate_Statement EOF )
            // InternalCQL.g:455:1: ruleCreate_Statement EOF
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
    // InternalCQL.g:462:1: ruleCreate_Statement : ( ( rule__Create_Statement__Group__0 ) ) ;
    public final void ruleCreate_Statement() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:466:2: ( ( ( rule__Create_Statement__Group__0 ) ) )
            // InternalCQL.g:467:2: ( ( rule__Create_Statement__Group__0 ) )
            {
            // InternalCQL.g:467:2: ( ( rule__Create_Statement__Group__0 ) )
            // InternalCQL.g:468:3: ( rule__Create_Statement__Group__0 )
            {
             before(grammarAccess.getCreate_StatementAccess().getGroup()); 
            // InternalCQL.g:469:3: ( rule__Create_Statement__Group__0 )
            // InternalCQL.g:469:4: rule__Create_Statement__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getGroup()); 

            }


            }

        }
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
    // InternalCQL.g:477:1: rule__Statement__Alternatives_0 : ( ( ( rule__Statement__TypeAssignment_0_0 ) ) | ( ( rule__Statement__TypeAssignment_0_1 ) ) );
    public final void rule__Statement__Alternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:481:1: ( ( ( rule__Statement__TypeAssignment_0_0 ) ) | ( ( rule__Statement__TypeAssignment_0_1 ) ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==44) ) {
                alt2=1;
            }
            else if ( (LA2_0==42) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // InternalCQL.g:482:2: ( ( rule__Statement__TypeAssignment_0_0 ) )
                    {
                    // InternalCQL.g:482:2: ( ( rule__Statement__TypeAssignment_0_0 ) )
                    // InternalCQL.g:483:3: ( rule__Statement__TypeAssignment_0_0 )
                    {
                     before(grammarAccess.getStatementAccess().getTypeAssignment_0_0()); 
                    // InternalCQL.g:484:3: ( rule__Statement__TypeAssignment_0_0 )
                    // InternalCQL.g:484:4: rule__Statement__TypeAssignment_0_0
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
                    // InternalCQL.g:488:2: ( ( rule__Statement__TypeAssignment_0_1 ) )
                    {
                    // InternalCQL.g:488:2: ( ( rule__Statement__TypeAssignment_0_1 ) )
                    // InternalCQL.g:489:3: ( rule__Statement__TypeAssignment_0_1 )
                    {
                     before(grammarAccess.getStatementAccess().getTypeAssignment_0_1()); 
                    // InternalCQL.g:490:3: ( rule__Statement__TypeAssignment_0_1 )
                    // InternalCQL.g:490:4: rule__Statement__TypeAssignment_0_1
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
    // InternalCQL.g:498:1: rule__Atomic__Alternatives : ( ( ( rule__Atomic__Group_0__0 ) ) | ( ( rule__Atomic__Group_1__0 ) ) | ( ( rule__Atomic__Group_2__0 ) ) | ( ( rule__Atomic__Group_3__0 ) ) | ( ( rule__Atomic__Group_4__0 ) ) );
    public final void rule__Atomic__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:502:1: ( ( ( rule__Atomic__Group_0__0 ) ) | ( ( rule__Atomic__Group_1__0 ) ) | ( ( rule__Atomic__Group_2__0 ) ) | ( ( rule__Atomic__Group_3__0 ) ) | ( ( rule__Atomic__Group_4__0 ) ) )
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
                    // InternalCQL.g:503:2: ( ( rule__Atomic__Group_0__0 ) )
                    {
                    // InternalCQL.g:503:2: ( ( rule__Atomic__Group_0__0 ) )
                    // InternalCQL.g:504:3: ( rule__Atomic__Group_0__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_0()); 
                    // InternalCQL.g:505:3: ( rule__Atomic__Group_0__0 )
                    // InternalCQL.g:505:4: rule__Atomic__Group_0__0
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
                    // InternalCQL.g:509:2: ( ( rule__Atomic__Group_1__0 ) )
                    {
                    // InternalCQL.g:509:2: ( ( rule__Atomic__Group_1__0 ) )
                    // InternalCQL.g:510:3: ( rule__Atomic__Group_1__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_1()); 
                    // InternalCQL.g:511:3: ( rule__Atomic__Group_1__0 )
                    // InternalCQL.g:511:4: rule__Atomic__Group_1__0
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
                    // InternalCQL.g:515:2: ( ( rule__Atomic__Group_2__0 ) )
                    {
                    // InternalCQL.g:515:2: ( ( rule__Atomic__Group_2__0 ) )
                    // InternalCQL.g:516:3: ( rule__Atomic__Group_2__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_2()); 
                    // InternalCQL.g:517:3: ( rule__Atomic__Group_2__0 )
                    // InternalCQL.g:517:4: rule__Atomic__Group_2__0
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
                    // InternalCQL.g:521:2: ( ( rule__Atomic__Group_3__0 ) )
                    {
                    // InternalCQL.g:521:2: ( ( rule__Atomic__Group_3__0 ) )
                    // InternalCQL.g:522:3: ( rule__Atomic__Group_3__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_3()); 
                    // InternalCQL.g:523:3: ( rule__Atomic__Group_3__0 )
                    // InternalCQL.g:523:4: rule__Atomic__Group_3__0
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
                    // InternalCQL.g:527:2: ( ( rule__Atomic__Group_4__0 ) )
                    {
                    // InternalCQL.g:527:2: ( ( rule__Atomic__Group_4__0 ) )
                    // InternalCQL.g:528:3: ( rule__Atomic__Group_4__0 )
                    {
                     before(grammarAccess.getAtomicAccess().getGroup_4()); 
                    // InternalCQL.g:529:3: ( rule__Atomic__Group_4__0 )
                    // InternalCQL.g:529:4: rule__Atomic__Group_4__0
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
    // InternalCQL.g:537:1: rule__Atomic__ValueAlternatives_3_1_0 : ( ( 'TRUE' ) | ( 'FALSE' ) );
    public final void rule__Atomic__ValueAlternatives_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:541:1: ( ( 'TRUE' ) | ( 'FALSE' ) )
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
                    // InternalCQL.g:542:2: ( 'TRUE' )
                    {
                    // InternalCQL.g:542:2: ( 'TRUE' )
                    // InternalCQL.g:543:3: 'TRUE'
                    {
                     before(grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0()); 
                    match(input,12,FOLLOW_2); 
                     after(grammarAccess.getAtomicAccess().getValueTRUEKeyword_3_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:548:2: ( 'FALSE' )
                    {
                    // InternalCQL.g:548:2: ( 'FALSE' )
                    // InternalCQL.g:549:3: 'FALSE'
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


    // $ANTLR start "rule__DataType__Alternatives"
    // InternalCQL.g:558:1: rule__DataType__Alternatives : ( ( 'INTEGER' ) | ( 'DOUBLE' ) | ( 'FLOAT' ) | ( 'STRING' ) | ( 'BOOLEAN' ) | ( 'STARTTIMESTAMP' ) | ( 'ENDTIMESTAMP' ) );
    public final void rule__DataType__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:562:1: ( ( 'INTEGER' ) | ( 'DOUBLE' ) | ( 'FLOAT' ) | ( 'STRING' ) | ( 'BOOLEAN' ) | ( 'STARTTIMESTAMP' ) | ( 'ENDTIMESTAMP' ) )
            int alt5=7;
            switch ( input.LA(1) ) {
            case 14:
                {
                alt5=1;
                }
                break;
            case 15:
                {
                alt5=2;
                }
                break;
            case 16:
                {
                alt5=3;
                }
                break;
            case 17:
                {
                alt5=4;
                }
                break;
            case 18:
                {
                alt5=5;
                }
                break;
            case 19:
                {
                alt5=6;
                }
                break;
            case 20:
                {
                alt5=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // InternalCQL.g:563:2: ( 'INTEGER' )
                    {
                    // InternalCQL.g:563:2: ( 'INTEGER' )
                    // InternalCQL.g:564:3: 'INTEGER'
                    {
                     before(grammarAccess.getDataTypeAccess().getINTEGERKeyword_0()); 
                    match(input,14,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getINTEGERKeyword_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:569:2: ( 'DOUBLE' )
                    {
                    // InternalCQL.g:569:2: ( 'DOUBLE' )
                    // InternalCQL.g:570:3: 'DOUBLE'
                    {
                     before(grammarAccess.getDataTypeAccess().getDOUBLEKeyword_1()); 
                    match(input,15,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getDOUBLEKeyword_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:575:2: ( 'FLOAT' )
                    {
                    // InternalCQL.g:575:2: ( 'FLOAT' )
                    // InternalCQL.g:576:3: 'FLOAT'
                    {
                     before(grammarAccess.getDataTypeAccess().getFLOATKeyword_2()); 
                    match(input,16,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getFLOATKeyword_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:581:2: ( 'STRING' )
                    {
                    // InternalCQL.g:581:2: ( 'STRING' )
                    // InternalCQL.g:582:3: 'STRING'
                    {
                     before(grammarAccess.getDataTypeAccess().getSTRINGKeyword_3()); 
                    match(input,17,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getSTRINGKeyword_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalCQL.g:587:2: ( 'BOOLEAN' )
                    {
                    // InternalCQL.g:587:2: ( 'BOOLEAN' )
                    // InternalCQL.g:588:3: 'BOOLEAN'
                    {
                     before(grammarAccess.getDataTypeAccess().getBOOLEANKeyword_4()); 
                    match(input,18,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getBOOLEANKeyword_4()); 

                    }


                    }
                    break;
                case 6 :
                    // InternalCQL.g:593:2: ( 'STARTTIMESTAMP' )
                    {
                    // InternalCQL.g:593:2: ( 'STARTTIMESTAMP' )
                    // InternalCQL.g:594:3: 'STARTTIMESTAMP'
                    {
                     before(grammarAccess.getDataTypeAccess().getSTARTTIMESTAMPKeyword_5()); 
                    match(input,19,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getSTARTTIMESTAMPKeyword_5()); 

                    }


                    }
                    break;
                case 7 :
                    // InternalCQL.g:599:2: ( 'ENDTIMESTAMP' )
                    {
                    // InternalCQL.g:599:2: ( 'ENDTIMESTAMP' )
                    // InternalCQL.g:600:3: 'ENDTIMESTAMP'
                    {
                     before(grammarAccess.getDataTypeAccess().getENDTIMESTAMPKeyword_6()); 
                    match(input,20,FOLLOW_2); 
                     after(grammarAccess.getDataTypeAccess().getENDTIMESTAMPKeyword_6()); 

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
    // $ANTLR end "rule__DataType__Alternatives"


    // $ANTLR start "rule__Equalitiy__OpAlternatives_1_1_0"
    // InternalCQL.g:609:1: rule__Equalitiy__OpAlternatives_1_1_0 : ( ( '==' ) | ( '!=' ) );
    public final void rule__Equalitiy__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:613:1: ( ( '==' ) | ( '!=' ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==21) ) {
                alt6=1;
            }
            else if ( (LA6_0==22) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // InternalCQL.g:614:2: ( '==' )
                    {
                    // InternalCQL.g:614:2: ( '==' )
                    // InternalCQL.g:615:3: '=='
                    {
                     before(grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0()); 
                    match(input,21,FOLLOW_2); 
                     after(grammarAccess.getEqualitiyAccess().getOpEqualsSignEqualsSignKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:620:2: ( '!=' )
                    {
                    // InternalCQL.g:620:2: ( '!=' )
                    // InternalCQL.g:621:3: '!='
                    {
                     before(grammarAccess.getEqualitiyAccess().getOpExclamationMarkEqualsSignKeyword_1_1_0_1()); 
                    match(input,22,FOLLOW_2); 
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
    // InternalCQL.g:630:1: rule__Comparison__OpAlternatives_1_1_0 : ( ( '>=' ) | ( '<=' ) | ( '<' ) | ( '>' ) );
    public final void rule__Comparison__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:634:1: ( ( '>=' ) | ( '<=' ) | ( '<' ) | ( '>' ) )
            int alt7=4;
            switch ( input.LA(1) ) {
            case 23:
                {
                alt7=1;
                }
                break;
            case 24:
                {
                alt7=2;
                }
                break;
            case 25:
                {
                alt7=3;
                }
                break;
            case 26:
                {
                alt7=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // InternalCQL.g:635:2: ( '>=' )
                    {
                    // InternalCQL.g:635:2: ( '>=' )
                    // InternalCQL.g:636:3: '>='
                    {
                     before(grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0()); 
                    match(input,23,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:641:2: ( '<=' )
                    {
                    // InternalCQL.g:641:2: ( '<=' )
                    // InternalCQL.g:642:3: '<='
                    {
                     before(grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1()); 
                    match(input,24,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_1_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalCQL.g:647:2: ( '<' )
                    {
                    // InternalCQL.g:647:2: ( '<' )
                    // InternalCQL.g:648:3: '<'
                    {
                     before(grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2()); 
                    match(input,25,FOLLOW_2); 
                     after(grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_1_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalCQL.g:653:2: ( '>' )
                    {
                    // InternalCQL.g:653:2: ( '>' )
                    // InternalCQL.g:654:3: '>'
                    {
                     before(grammarAccess.getComparisonAccess().getOpGreaterThanSignKeyword_1_1_0_3()); 
                    match(input,26,FOLLOW_2); 
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
    // InternalCQL.g:663:1: rule__PlusOrMinus__Alternatives_1_0 : ( ( ( rule__PlusOrMinus__Group_1_0_0__0 ) ) | ( ( rule__PlusOrMinus__Group_1_0_1__0 ) ) );
    public final void rule__PlusOrMinus__Alternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:667:1: ( ( ( rule__PlusOrMinus__Group_1_0_0__0 ) ) | ( ( rule__PlusOrMinus__Group_1_0_1__0 ) ) )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==33) ) {
                alt8=1;
            }
            else if ( (LA8_0==34) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // InternalCQL.g:668:2: ( ( rule__PlusOrMinus__Group_1_0_0__0 ) )
                    {
                    // InternalCQL.g:668:2: ( ( rule__PlusOrMinus__Group_1_0_0__0 ) )
                    // InternalCQL.g:669:3: ( rule__PlusOrMinus__Group_1_0_0__0 )
                    {
                     before(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_0()); 
                    // InternalCQL.g:670:3: ( rule__PlusOrMinus__Group_1_0_0__0 )
                    // InternalCQL.g:670:4: rule__PlusOrMinus__Group_1_0_0__0
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
                    // InternalCQL.g:674:2: ( ( rule__PlusOrMinus__Group_1_0_1__0 ) )
                    {
                    // InternalCQL.g:674:2: ( ( rule__PlusOrMinus__Group_1_0_1__0 ) )
                    // InternalCQL.g:675:3: ( rule__PlusOrMinus__Group_1_0_1__0 )
                    {
                     before(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_1()); 
                    // InternalCQL.g:676:3: ( rule__PlusOrMinus__Group_1_0_1__0 )
                    // InternalCQL.g:676:4: rule__PlusOrMinus__Group_1_0_1__0
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
    // InternalCQL.g:684:1: rule__MulOrDiv__OpAlternatives_1_1_0 : ( ( '*' ) | ( '/' ) );
    public final void rule__MulOrDiv__OpAlternatives_1_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:688:1: ( ( '*' ) | ( '/' ) )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==27) ) {
                alt9=1;
            }
            else if ( (LA9_0==28) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // InternalCQL.g:689:2: ( '*' )
                    {
                    // InternalCQL.g:689:2: ( '*' )
                    // InternalCQL.g:690:3: '*'
                    {
                     before(grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0()); 
                    match(input,27,FOLLOW_2); 
                     after(grammarAccess.getMulOrDivAccess().getOpAsteriskKeyword_1_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:695:2: ( '/' )
                    {
                    // InternalCQL.g:695:2: ( '/' )
                    // InternalCQL.g:696:3: '/'
                    {
                     before(grammarAccess.getMulOrDivAccess().getOpSolidusKeyword_1_1_0_1()); 
                    match(input,28,FOLLOW_2); 
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
    // InternalCQL.g:705:1: rule__Primary__Alternatives : ( ( ( rule__Primary__Group_0__0 ) ) | ( ( rule__Primary__Group_1__0 ) ) | ( ruleAtomic ) );
    public final void rule__Primary__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:709:1: ( ( ( rule__Primary__Group_0__0 ) ) | ( ( rule__Primary__Group_1__0 ) ) | ( ruleAtomic ) )
            int alt10=3;
            switch ( input.LA(1) ) {
            case 35:
                {
                alt10=1;
                }
                break;
            case 37:
                {
                alt10=2;
                }
                break;
            case RULE_INT:
            case RULE_FLOAT_NUMBER:
            case RULE_STRING:
            case RULE_ID:
            case 12:
            case 13:
                {
                alt10=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // InternalCQL.g:710:2: ( ( rule__Primary__Group_0__0 ) )
                    {
                    // InternalCQL.g:710:2: ( ( rule__Primary__Group_0__0 ) )
                    // InternalCQL.g:711:3: ( rule__Primary__Group_0__0 )
                    {
                     before(grammarAccess.getPrimaryAccess().getGroup_0()); 
                    // InternalCQL.g:712:3: ( rule__Primary__Group_0__0 )
                    // InternalCQL.g:712:4: rule__Primary__Group_0__0
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
                    // InternalCQL.g:716:2: ( ( rule__Primary__Group_1__0 ) )
                    {
                    // InternalCQL.g:716:2: ( ( rule__Primary__Group_1__0 ) )
                    // InternalCQL.g:717:3: ( rule__Primary__Group_1__0 )
                    {
                     before(grammarAccess.getPrimaryAccess().getGroup_1()); 
                    // InternalCQL.g:718:3: ( rule__Primary__Group_1__0 )
                    // InternalCQL.g:718:4: rule__Primary__Group_1__0
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
                    // InternalCQL.g:722:2: ( ruleAtomic )
                    {
                    // InternalCQL.g:722:2: ( ruleAtomic )
                    // InternalCQL.g:723:3: ruleAtomic
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
    // InternalCQL.g:732:1: rule__Statement__Group__0 : rule__Statement__Group__0__Impl rule__Statement__Group__1 ;
    public final void rule__Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:736:1: ( rule__Statement__Group__0__Impl rule__Statement__Group__1 )
            // InternalCQL.g:737:2: rule__Statement__Group__0__Impl rule__Statement__Group__1
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
    // InternalCQL.g:744:1: rule__Statement__Group__0__Impl : ( ( rule__Statement__Alternatives_0 ) ) ;
    public final void rule__Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:748:1: ( ( ( rule__Statement__Alternatives_0 ) ) )
            // InternalCQL.g:749:1: ( ( rule__Statement__Alternatives_0 ) )
            {
            // InternalCQL.g:749:1: ( ( rule__Statement__Alternatives_0 ) )
            // InternalCQL.g:750:2: ( rule__Statement__Alternatives_0 )
            {
             before(grammarAccess.getStatementAccess().getAlternatives_0()); 
            // InternalCQL.g:751:2: ( rule__Statement__Alternatives_0 )
            // InternalCQL.g:751:3: rule__Statement__Alternatives_0
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
    // InternalCQL.g:759:1: rule__Statement__Group__1 : rule__Statement__Group__1__Impl ;
    public final void rule__Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:763:1: ( rule__Statement__Group__1__Impl )
            // InternalCQL.g:764:2: rule__Statement__Group__1__Impl
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
    // InternalCQL.g:770:1: rule__Statement__Group__1__Impl : ( ( ';' )? ) ;
    public final void rule__Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:774:1: ( ( ( ';' )? ) )
            // InternalCQL.g:775:1: ( ( ';' )? )
            {
            // InternalCQL.g:775:1: ( ( ';' )? )
            // InternalCQL.g:776:2: ( ';' )?
            {
             before(grammarAccess.getStatementAccess().getSemicolonKeyword_1()); 
            // InternalCQL.g:777:2: ( ';' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==29) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalCQL.g:777:3: ';'
                    {
                    match(input,29,FOLLOW_2); 

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
    // InternalCQL.g:786:1: rule__Atomic__Group_0__0 : rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 ;
    public final void rule__Atomic__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:790:1: ( rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 )
            // InternalCQL.g:791:2: rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1
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
    // InternalCQL.g:798:1: rule__Atomic__Group_0__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:802:1: ( ( () ) )
            // InternalCQL.g:803:1: ( () )
            {
            // InternalCQL.g:803:1: ( () )
            // InternalCQL.g:804:2: ()
            {
             before(grammarAccess.getAtomicAccess().getIntConstantAction_0_0()); 
            // InternalCQL.g:805:2: ()
            // InternalCQL.g:805:3: 
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
    // InternalCQL.g:813:1: rule__Atomic__Group_0__1 : rule__Atomic__Group_0__1__Impl ;
    public final void rule__Atomic__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:817:1: ( rule__Atomic__Group_0__1__Impl )
            // InternalCQL.g:818:2: rule__Atomic__Group_0__1__Impl
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
    // InternalCQL.g:824:1: rule__Atomic__Group_0__1__Impl : ( ( rule__Atomic__ValueAssignment_0_1 ) ) ;
    public final void rule__Atomic__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:828:1: ( ( ( rule__Atomic__ValueAssignment_0_1 ) ) )
            // InternalCQL.g:829:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            {
            // InternalCQL.g:829:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            // InternalCQL.g:830:2: ( rule__Atomic__ValueAssignment_0_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_0_1()); 
            // InternalCQL.g:831:2: ( rule__Atomic__ValueAssignment_0_1 )
            // InternalCQL.g:831:3: rule__Atomic__ValueAssignment_0_1
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
    // InternalCQL.g:840:1: rule__Atomic__Group_1__0 : rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 ;
    public final void rule__Atomic__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:844:1: ( rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 )
            // InternalCQL.g:845:2: rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1
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
    // InternalCQL.g:852:1: rule__Atomic__Group_1__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:856:1: ( ( () ) )
            // InternalCQL.g:857:1: ( () )
            {
            // InternalCQL.g:857:1: ( () )
            // InternalCQL.g:858:2: ()
            {
             before(grammarAccess.getAtomicAccess().getFloatConstantAction_1_0()); 
            // InternalCQL.g:859:2: ()
            // InternalCQL.g:859:3: 
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
    // InternalCQL.g:867:1: rule__Atomic__Group_1__1 : rule__Atomic__Group_1__1__Impl ;
    public final void rule__Atomic__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:871:1: ( rule__Atomic__Group_1__1__Impl )
            // InternalCQL.g:872:2: rule__Atomic__Group_1__1__Impl
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
    // InternalCQL.g:878:1: rule__Atomic__Group_1__1__Impl : ( ( rule__Atomic__ValueAssignment_1_1 ) ) ;
    public final void rule__Atomic__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:882:1: ( ( ( rule__Atomic__ValueAssignment_1_1 ) ) )
            // InternalCQL.g:883:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            {
            // InternalCQL.g:883:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            // InternalCQL.g:884:2: ( rule__Atomic__ValueAssignment_1_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_1_1()); 
            // InternalCQL.g:885:2: ( rule__Atomic__ValueAssignment_1_1 )
            // InternalCQL.g:885:3: rule__Atomic__ValueAssignment_1_1
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
    // InternalCQL.g:894:1: rule__Atomic__Group_2__0 : rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 ;
    public final void rule__Atomic__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:898:1: ( rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 )
            // InternalCQL.g:899:2: rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1
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
    // InternalCQL.g:906:1: rule__Atomic__Group_2__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:910:1: ( ( () ) )
            // InternalCQL.g:911:1: ( () )
            {
            // InternalCQL.g:911:1: ( () )
            // InternalCQL.g:912:2: ()
            {
             before(grammarAccess.getAtomicAccess().getStringConstantAction_2_0()); 
            // InternalCQL.g:913:2: ()
            // InternalCQL.g:913:3: 
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
    // InternalCQL.g:921:1: rule__Atomic__Group_2__1 : rule__Atomic__Group_2__1__Impl ;
    public final void rule__Atomic__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:925:1: ( rule__Atomic__Group_2__1__Impl )
            // InternalCQL.g:926:2: rule__Atomic__Group_2__1__Impl
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
    // InternalCQL.g:932:1: rule__Atomic__Group_2__1__Impl : ( ( rule__Atomic__ValueAssignment_2_1 ) ) ;
    public final void rule__Atomic__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:936:1: ( ( ( rule__Atomic__ValueAssignment_2_1 ) ) )
            // InternalCQL.g:937:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            {
            // InternalCQL.g:937:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            // InternalCQL.g:938:2: ( rule__Atomic__ValueAssignment_2_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_2_1()); 
            // InternalCQL.g:939:2: ( rule__Atomic__ValueAssignment_2_1 )
            // InternalCQL.g:939:3: rule__Atomic__ValueAssignment_2_1
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
    // InternalCQL.g:948:1: rule__Atomic__Group_3__0 : rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 ;
    public final void rule__Atomic__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:952:1: ( rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 )
            // InternalCQL.g:953:2: rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1
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
    // InternalCQL.g:960:1: rule__Atomic__Group_3__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:964:1: ( ( () ) )
            // InternalCQL.g:965:1: ( () )
            {
            // InternalCQL.g:965:1: ( () )
            // InternalCQL.g:966:2: ()
            {
             before(grammarAccess.getAtomicAccess().getBoolConstantAction_3_0()); 
            // InternalCQL.g:967:2: ()
            // InternalCQL.g:967:3: 
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
    // InternalCQL.g:975:1: rule__Atomic__Group_3__1 : rule__Atomic__Group_3__1__Impl ;
    public final void rule__Atomic__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:979:1: ( rule__Atomic__Group_3__1__Impl )
            // InternalCQL.g:980:2: rule__Atomic__Group_3__1__Impl
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
    // InternalCQL.g:986:1: rule__Atomic__Group_3__1__Impl : ( ( rule__Atomic__ValueAssignment_3_1 ) ) ;
    public final void rule__Atomic__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:990:1: ( ( ( rule__Atomic__ValueAssignment_3_1 ) ) )
            // InternalCQL.g:991:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            {
            // InternalCQL.g:991:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            // InternalCQL.g:992:2: ( rule__Atomic__ValueAssignment_3_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_3_1()); 
            // InternalCQL.g:993:2: ( rule__Atomic__ValueAssignment_3_1 )
            // InternalCQL.g:993:3: rule__Atomic__ValueAssignment_3_1
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
    // InternalCQL.g:1002:1: rule__Atomic__Group_4__0 : rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 ;
    public final void rule__Atomic__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1006:1: ( rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 )
            // InternalCQL.g:1007:2: rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1
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
    // InternalCQL.g:1014:1: rule__Atomic__Group_4__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1018:1: ( ( () ) )
            // InternalCQL.g:1019:1: ( () )
            {
            // InternalCQL.g:1019:1: ( () )
            // InternalCQL.g:1020:2: ()
            {
             before(grammarAccess.getAtomicAccess().getAttributeAction_4_0()); 
            // InternalCQL.g:1021:2: ()
            // InternalCQL.g:1021:3: 
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
    // InternalCQL.g:1029:1: rule__Atomic__Group_4__1 : rule__Atomic__Group_4__1__Impl ;
    public final void rule__Atomic__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1033:1: ( rule__Atomic__Group_4__1__Impl )
            // InternalCQL.g:1034:2: rule__Atomic__Group_4__1__Impl
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
    // InternalCQL.g:1040:1: rule__Atomic__Group_4__1__Impl : ( ( rule__Atomic__ValueAssignment_4_1 ) ) ;
    public final void rule__Atomic__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1044:1: ( ( ( rule__Atomic__ValueAssignment_4_1 ) ) )
            // InternalCQL.g:1045:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            {
            // InternalCQL.g:1045:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            // InternalCQL.g:1046:2: ( rule__Atomic__ValueAssignment_4_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_4_1()); 
            // InternalCQL.g:1047:2: ( rule__Atomic__ValueAssignment_4_1 )
            // InternalCQL.g:1047:3: rule__Atomic__ValueAssignment_4_1
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
    // InternalCQL.g:1056:1: rule__Attribute__Group__0 : rule__Attribute__Group__0__Impl rule__Attribute__Group__1 ;
    public final void rule__Attribute__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1060:1: ( rule__Attribute__Group__0__Impl rule__Attribute__Group__1 )
            // InternalCQL.g:1061:2: rule__Attribute__Group__0__Impl rule__Attribute__Group__1
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
    // InternalCQL.g:1068:1: rule__Attribute__Group__0__Impl : ( ( rule__Attribute__Group_0__0 )? ) ;
    public final void rule__Attribute__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1072:1: ( ( ( rule__Attribute__Group_0__0 )? ) )
            // InternalCQL.g:1073:1: ( ( rule__Attribute__Group_0__0 )? )
            {
            // InternalCQL.g:1073:1: ( ( rule__Attribute__Group_0__0 )? )
            // InternalCQL.g:1074:2: ( rule__Attribute__Group_0__0 )?
            {
             before(grammarAccess.getAttributeAccess().getGroup_0()); 
            // InternalCQL.g:1075:2: ( rule__Attribute__Group_0__0 )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==RULE_ID) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==30) ) {
                    alt12=1;
                }
            }
            switch (alt12) {
                case 1 :
                    // InternalCQL.g:1075:3: rule__Attribute__Group_0__0
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
    // InternalCQL.g:1083:1: rule__Attribute__Group__1 : rule__Attribute__Group__1__Impl ;
    public final void rule__Attribute__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1087:1: ( rule__Attribute__Group__1__Impl )
            // InternalCQL.g:1088:2: rule__Attribute__Group__1__Impl
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
    // InternalCQL.g:1094:1: rule__Attribute__Group__1__Impl : ( ( rule__Attribute__NameAssignment_1 ) ) ;
    public final void rule__Attribute__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1098:1: ( ( ( rule__Attribute__NameAssignment_1 ) ) )
            // InternalCQL.g:1099:1: ( ( rule__Attribute__NameAssignment_1 ) )
            {
            // InternalCQL.g:1099:1: ( ( rule__Attribute__NameAssignment_1 ) )
            // InternalCQL.g:1100:2: ( rule__Attribute__NameAssignment_1 )
            {
             before(grammarAccess.getAttributeAccess().getNameAssignment_1()); 
            // InternalCQL.g:1101:2: ( rule__Attribute__NameAssignment_1 )
            // InternalCQL.g:1101:3: rule__Attribute__NameAssignment_1
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
    // InternalCQL.g:1110:1: rule__Attribute__Group_0__0 : rule__Attribute__Group_0__0__Impl rule__Attribute__Group_0__1 ;
    public final void rule__Attribute__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1114:1: ( rule__Attribute__Group_0__0__Impl rule__Attribute__Group_0__1 )
            // InternalCQL.g:1115:2: rule__Attribute__Group_0__0__Impl rule__Attribute__Group_0__1
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
    // InternalCQL.g:1122:1: rule__Attribute__Group_0__0__Impl : ( ( rule__Attribute__SourceAssignment_0_0 ) ) ;
    public final void rule__Attribute__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1126:1: ( ( ( rule__Attribute__SourceAssignment_0_0 ) ) )
            // InternalCQL.g:1127:1: ( ( rule__Attribute__SourceAssignment_0_0 ) )
            {
            // InternalCQL.g:1127:1: ( ( rule__Attribute__SourceAssignment_0_0 ) )
            // InternalCQL.g:1128:2: ( rule__Attribute__SourceAssignment_0_0 )
            {
             before(grammarAccess.getAttributeAccess().getSourceAssignment_0_0()); 
            // InternalCQL.g:1129:2: ( rule__Attribute__SourceAssignment_0_0 )
            // InternalCQL.g:1129:3: rule__Attribute__SourceAssignment_0_0
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
    // InternalCQL.g:1137:1: rule__Attribute__Group_0__1 : rule__Attribute__Group_0__1__Impl ;
    public final void rule__Attribute__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1141:1: ( rule__Attribute__Group_0__1__Impl )
            // InternalCQL.g:1142:2: rule__Attribute__Group_0__1__Impl
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
    // InternalCQL.g:1148:1: rule__Attribute__Group_0__1__Impl : ( '.' ) ;
    public final void rule__Attribute__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1152:1: ( ( '.' ) )
            // InternalCQL.g:1153:1: ( '.' )
            {
            // InternalCQL.g:1153:1: ( '.' )
            // InternalCQL.g:1154:2: '.'
            {
             before(grammarAccess.getAttributeAccess().getFullStopKeyword_0_1()); 
            match(input,30,FOLLOW_2); 
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
    // InternalCQL.g:1164:1: rule__ExpressionsModel__Group__0 : rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 ;
    public final void rule__ExpressionsModel__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1168:1: ( rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 )
            // InternalCQL.g:1169:2: rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1
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
    // InternalCQL.g:1176:1: rule__ExpressionsModel__Group__0__Impl : ( () ) ;
    public final void rule__ExpressionsModel__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1180:1: ( ( () ) )
            // InternalCQL.g:1181:1: ( () )
            {
            // InternalCQL.g:1181:1: ( () )
            // InternalCQL.g:1182:2: ()
            {
             before(grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0()); 
            // InternalCQL.g:1183:2: ()
            // InternalCQL.g:1183:3: 
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
    // InternalCQL.g:1191:1: rule__ExpressionsModel__Group__1 : rule__ExpressionsModel__Group__1__Impl ;
    public final void rule__ExpressionsModel__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1195:1: ( rule__ExpressionsModel__Group__1__Impl )
            // InternalCQL.g:1196:2: rule__ExpressionsModel__Group__1__Impl
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
    // InternalCQL.g:1202:1: rule__ExpressionsModel__Group__1__Impl : ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) ;
    public final void rule__ExpressionsModel__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1206:1: ( ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) )
            // InternalCQL.g:1207:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            {
            // InternalCQL.g:1207:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            // InternalCQL.g:1208:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            {
            // InternalCQL.g:1208:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) )
            // InternalCQL.g:1209:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1210:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            // InternalCQL.g:1210:4: rule__ExpressionsModel__ElementsAssignment_1
            {
            pushFollow(FOLLOW_12);
            rule__ExpressionsModel__ElementsAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 

            }

            // InternalCQL.g:1213:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            // InternalCQL.g:1214:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1215:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=RULE_INT && LA13_0<=RULE_ID)||(LA13_0>=12 && LA13_0<=13)||LA13_0==35||LA13_0==37) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalCQL.g:1215:4: rule__ExpressionsModel__ElementsAssignment_1
            	    {
            	    pushFollow(FOLLOW_12);
            	    rule__ExpressionsModel__ElementsAssignment_1();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop13;
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
    // InternalCQL.g:1225:1: rule__Or__Group__0 : rule__Or__Group__0__Impl rule__Or__Group__1 ;
    public final void rule__Or__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1229:1: ( rule__Or__Group__0__Impl rule__Or__Group__1 )
            // InternalCQL.g:1230:2: rule__Or__Group__0__Impl rule__Or__Group__1
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
    // InternalCQL.g:1237:1: rule__Or__Group__0__Impl : ( ruleAnd ) ;
    public final void rule__Or__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1241:1: ( ( ruleAnd ) )
            // InternalCQL.g:1242:1: ( ruleAnd )
            {
            // InternalCQL.g:1242:1: ( ruleAnd )
            // InternalCQL.g:1243:2: ruleAnd
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
    // InternalCQL.g:1252:1: rule__Or__Group__1 : rule__Or__Group__1__Impl ;
    public final void rule__Or__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1256:1: ( rule__Or__Group__1__Impl )
            // InternalCQL.g:1257:2: rule__Or__Group__1__Impl
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
    // InternalCQL.g:1263:1: rule__Or__Group__1__Impl : ( ( rule__Or__Group_1__0 )* ) ;
    public final void rule__Or__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1267:1: ( ( ( rule__Or__Group_1__0 )* ) )
            // InternalCQL.g:1268:1: ( ( rule__Or__Group_1__0 )* )
            {
            // InternalCQL.g:1268:1: ( ( rule__Or__Group_1__0 )* )
            // InternalCQL.g:1269:2: ( rule__Or__Group_1__0 )*
            {
             before(grammarAccess.getOrAccess().getGroup_1()); 
            // InternalCQL.g:1270:2: ( rule__Or__Group_1__0 )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==31) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalCQL.g:1270:3: rule__Or__Group_1__0
            	    {
            	    pushFollow(FOLLOW_14);
            	    rule__Or__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop14;
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
    // InternalCQL.g:1279:1: rule__Or__Group_1__0 : rule__Or__Group_1__0__Impl rule__Or__Group_1__1 ;
    public final void rule__Or__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1283:1: ( rule__Or__Group_1__0__Impl rule__Or__Group_1__1 )
            // InternalCQL.g:1284:2: rule__Or__Group_1__0__Impl rule__Or__Group_1__1
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
    // InternalCQL.g:1291:1: rule__Or__Group_1__0__Impl : ( () ) ;
    public final void rule__Or__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1295:1: ( ( () ) )
            // InternalCQL.g:1296:1: ( () )
            {
            // InternalCQL.g:1296:1: ( () )
            // InternalCQL.g:1297:2: ()
            {
             before(grammarAccess.getOrAccess().getOrLeftAction_1_0()); 
            // InternalCQL.g:1298:2: ()
            // InternalCQL.g:1298:3: 
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
    // InternalCQL.g:1306:1: rule__Or__Group_1__1 : rule__Or__Group_1__1__Impl rule__Or__Group_1__2 ;
    public final void rule__Or__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1310:1: ( rule__Or__Group_1__1__Impl rule__Or__Group_1__2 )
            // InternalCQL.g:1311:2: rule__Or__Group_1__1__Impl rule__Or__Group_1__2
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
    // InternalCQL.g:1318:1: rule__Or__Group_1__1__Impl : ( 'OR' ) ;
    public final void rule__Or__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1322:1: ( ( 'OR' ) )
            // InternalCQL.g:1323:1: ( 'OR' )
            {
            // InternalCQL.g:1323:1: ( 'OR' )
            // InternalCQL.g:1324:2: 'OR'
            {
             before(grammarAccess.getOrAccess().getORKeyword_1_1()); 
            match(input,31,FOLLOW_2); 
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
    // InternalCQL.g:1333:1: rule__Or__Group_1__2 : rule__Or__Group_1__2__Impl ;
    public final void rule__Or__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1337:1: ( rule__Or__Group_1__2__Impl )
            // InternalCQL.g:1338:2: rule__Or__Group_1__2__Impl
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
    // InternalCQL.g:1344:1: rule__Or__Group_1__2__Impl : ( ( rule__Or__RightAssignment_1_2 ) ) ;
    public final void rule__Or__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1348:1: ( ( ( rule__Or__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1349:1: ( ( rule__Or__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1349:1: ( ( rule__Or__RightAssignment_1_2 ) )
            // InternalCQL.g:1350:2: ( rule__Or__RightAssignment_1_2 )
            {
             before(grammarAccess.getOrAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1351:2: ( rule__Or__RightAssignment_1_2 )
            // InternalCQL.g:1351:3: rule__Or__RightAssignment_1_2
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
    // InternalCQL.g:1360:1: rule__And__Group__0 : rule__And__Group__0__Impl rule__And__Group__1 ;
    public final void rule__And__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1364:1: ( rule__And__Group__0__Impl rule__And__Group__1 )
            // InternalCQL.g:1365:2: rule__And__Group__0__Impl rule__And__Group__1
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
    // InternalCQL.g:1372:1: rule__And__Group__0__Impl : ( ruleEqualitiy ) ;
    public final void rule__And__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1376:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:1377:1: ( ruleEqualitiy )
            {
            // InternalCQL.g:1377:1: ( ruleEqualitiy )
            // InternalCQL.g:1378:2: ruleEqualitiy
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
    // InternalCQL.g:1387:1: rule__And__Group__1 : rule__And__Group__1__Impl ;
    public final void rule__And__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1391:1: ( rule__And__Group__1__Impl )
            // InternalCQL.g:1392:2: rule__And__Group__1__Impl
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
    // InternalCQL.g:1398:1: rule__And__Group__1__Impl : ( ( rule__And__Group_1__0 )* ) ;
    public final void rule__And__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1402:1: ( ( ( rule__And__Group_1__0 )* ) )
            // InternalCQL.g:1403:1: ( ( rule__And__Group_1__0 )* )
            {
            // InternalCQL.g:1403:1: ( ( rule__And__Group_1__0 )* )
            // InternalCQL.g:1404:2: ( rule__And__Group_1__0 )*
            {
             before(grammarAccess.getAndAccess().getGroup_1()); 
            // InternalCQL.g:1405:2: ( rule__And__Group_1__0 )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==32) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCQL.g:1405:3: rule__And__Group_1__0
            	    {
            	    pushFollow(FOLLOW_16);
            	    rule__And__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop15;
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
    // InternalCQL.g:1414:1: rule__And__Group_1__0 : rule__And__Group_1__0__Impl rule__And__Group_1__1 ;
    public final void rule__And__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1418:1: ( rule__And__Group_1__0__Impl rule__And__Group_1__1 )
            // InternalCQL.g:1419:2: rule__And__Group_1__0__Impl rule__And__Group_1__1
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
    // InternalCQL.g:1426:1: rule__And__Group_1__0__Impl : ( () ) ;
    public final void rule__And__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1430:1: ( ( () ) )
            // InternalCQL.g:1431:1: ( () )
            {
            // InternalCQL.g:1431:1: ( () )
            // InternalCQL.g:1432:2: ()
            {
             before(grammarAccess.getAndAccess().getAndLeftAction_1_0()); 
            // InternalCQL.g:1433:2: ()
            // InternalCQL.g:1433:3: 
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
    // InternalCQL.g:1441:1: rule__And__Group_1__1 : rule__And__Group_1__1__Impl rule__And__Group_1__2 ;
    public final void rule__And__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1445:1: ( rule__And__Group_1__1__Impl rule__And__Group_1__2 )
            // InternalCQL.g:1446:2: rule__And__Group_1__1__Impl rule__And__Group_1__2
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
    // InternalCQL.g:1453:1: rule__And__Group_1__1__Impl : ( 'AND' ) ;
    public final void rule__And__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1457:1: ( ( 'AND' ) )
            // InternalCQL.g:1458:1: ( 'AND' )
            {
            // InternalCQL.g:1458:1: ( 'AND' )
            // InternalCQL.g:1459:2: 'AND'
            {
             before(grammarAccess.getAndAccess().getANDKeyword_1_1()); 
            match(input,32,FOLLOW_2); 
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
    // InternalCQL.g:1468:1: rule__And__Group_1__2 : rule__And__Group_1__2__Impl ;
    public final void rule__And__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1472:1: ( rule__And__Group_1__2__Impl )
            // InternalCQL.g:1473:2: rule__And__Group_1__2__Impl
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
    // InternalCQL.g:1479:1: rule__And__Group_1__2__Impl : ( ( rule__And__RightAssignment_1_2 ) ) ;
    public final void rule__And__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1483:1: ( ( ( rule__And__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1484:1: ( ( rule__And__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1484:1: ( ( rule__And__RightAssignment_1_2 ) )
            // InternalCQL.g:1485:2: ( rule__And__RightAssignment_1_2 )
            {
             before(grammarAccess.getAndAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1486:2: ( rule__And__RightAssignment_1_2 )
            // InternalCQL.g:1486:3: rule__And__RightAssignment_1_2
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
    // InternalCQL.g:1495:1: rule__Equalitiy__Group__0 : rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 ;
    public final void rule__Equalitiy__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1499:1: ( rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 )
            // InternalCQL.g:1500:2: rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1
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
    // InternalCQL.g:1507:1: rule__Equalitiy__Group__0__Impl : ( ruleComparison ) ;
    public final void rule__Equalitiy__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1511:1: ( ( ruleComparison ) )
            // InternalCQL.g:1512:1: ( ruleComparison )
            {
            // InternalCQL.g:1512:1: ( ruleComparison )
            // InternalCQL.g:1513:2: ruleComparison
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
    // InternalCQL.g:1522:1: rule__Equalitiy__Group__1 : rule__Equalitiy__Group__1__Impl ;
    public final void rule__Equalitiy__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1526:1: ( rule__Equalitiy__Group__1__Impl )
            // InternalCQL.g:1527:2: rule__Equalitiy__Group__1__Impl
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
    // InternalCQL.g:1533:1: rule__Equalitiy__Group__1__Impl : ( ( rule__Equalitiy__Group_1__0 )* ) ;
    public final void rule__Equalitiy__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1537:1: ( ( ( rule__Equalitiy__Group_1__0 )* ) )
            // InternalCQL.g:1538:1: ( ( rule__Equalitiy__Group_1__0 )* )
            {
            // InternalCQL.g:1538:1: ( ( rule__Equalitiy__Group_1__0 )* )
            // InternalCQL.g:1539:2: ( rule__Equalitiy__Group_1__0 )*
            {
             before(grammarAccess.getEqualitiyAccess().getGroup_1()); 
            // InternalCQL.g:1540:2: ( rule__Equalitiy__Group_1__0 )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( ((LA16_0>=21 && LA16_0<=22)) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalCQL.g:1540:3: rule__Equalitiy__Group_1__0
            	    {
            	    pushFollow(FOLLOW_18);
            	    rule__Equalitiy__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
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
    // InternalCQL.g:1549:1: rule__Equalitiy__Group_1__0 : rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 ;
    public final void rule__Equalitiy__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1553:1: ( rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 )
            // InternalCQL.g:1554:2: rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1
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
    // InternalCQL.g:1561:1: rule__Equalitiy__Group_1__0__Impl : ( () ) ;
    public final void rule__Equalitiy__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1565:1: ( ( () ) )
            // InternalCQL.g:1566:1: ( () )
            {
            // InternalCQL.g:1566:1: ( () )
            // InternalCQL.g:1567:2: ()
            {
             before(grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0()); 
            // InternalCQL.g:1568:2: ()
            // InternalCQL.g:1568:3: 
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
    // InternalCQL.g:1576:1: rule__Equalitiy__Group_1__1 : rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 ;
    public final void rule__Equalitiy__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1580:1: ( rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 )
            // InternalCQL.g:1581:2: rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2
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
    // InternalCQL.g:1588:1: rule__Equalitiy__Group_1__1__Impl : ( ( rule__Equalitiy__OpAssignment_1_1 ) ) ;
    public final void rule__Equalitiy__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1592:1: ( ( ( rule__Equalitiy__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1593:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1593:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            // InternalCQL.g:1594:2: ( rule__Equalitiy__OpAssignment_1_1 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1595:2: ( rule__Equalitiy__OpAssignment_1_1 )
            // InternalCQL.g:1595:3: rule__Equalitiy__OpAssignment_1_1
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
    // InternalCQL.g:1603:1: rule__Equalitiy__Group_1__2 : rule__Equalitiy__Group_1__2__Impl ;
    public final void rule__Equalitiy__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1607:1: ( rule__Equalitiy__Group_1__2__Impl )
            // InternalCQL.g:1608:2: rule__Equalitiy__Group_1__2__Impl
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
    // InternalCQL.g:1614:1: rule__Equalitiy__Group_1__2__Impl : ( ( rule__Equalitiy__RightAssignment_1_2 ) ) ;
    public final void rule__Equalitiy__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1618:1: ( ( ( rule__Equalitiy__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1619:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1619:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            // InternalCQL.g:1620:2: ( rule__Equalitiy__RightAssignment_1_2 )
            {
             before(grammarAccess.getEqualitiyAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1621:2: ( rule__Equalitiy__RightAssignment_1_2 )
            // InternalCQL.g:1621:3: rule__Equalitiy__RightAssignment_1_2
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
    // InternalCQL.g:1630:1: rule__Comparison__Group__0 : rule__Comparison__Group__0__Impl rule__Comparison__Group__1 ;
    public final void rule__Comparison__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1634:1: ( rule__Comparison__Group__0__Impl rule__Comparison__Group__1 )
            // InternalCQL.g:1635:2: rule__Comparison__Group__0__Impl rule__Comparison__Group__1
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
    // InternalCQL.g:1642:1: rule__Comparison__Group__0__Impl : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1646:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:1647:1: ( rulePlusOrMinus )
            {
            // InternalCQL.g:1647:1: ( rulePlusOrMinus )
            // InternalCQL.g:1648:2: rulePlusOrMinus
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
    // InternalCQL.g:1657:1: rule__Comparison__Group__1 : rule__Comparison__Group__1__Impl ;
    public final void rule__Comparison__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1661:1: ( rule__Comparison__Group__1__Impl )
            // InternalCQL.g:1662:2: rule__Comparison__Group__1__Impl
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
    // InternalCQL.g:1668:1: rule__Comparison__Group__1__Impl : ( ( rule__Comparison__Group_1__0 )* ) ;
    public final void rule__Comparison__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1672:1: ( ( ( rule__Comparison__Group_1__0 )* ) )
            // InternalCQL.g:1673:1: ( ( rule__Comparison__Group_1__0 )* )
            {
            // InternalCQL.g:1673:1: ( ( rule__Comparison__Group_1__0 )* )
            // InternalCQL.g:1674:2: ( rule__Comparison__Group_1__0 )*
            {
             before(grammarAccess.getComparisonAccess().getGroup_1()); 
            // InternalCQL.g:1675:2: ( rule__Comparison__Group_1__0 )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>=23 && LA17_0<=26)) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // InternalCQL.g:1675:3: rule__Comparison__Group_1__0
            	    {
            	    pushFollow(FOLLOW_20);
            	    rule__Comparison__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop17;
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
    // InternalCQL.g:1684:1: rule__Comparison__Group_1__0 : rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 ;
    public final void rule__Comparison__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1688:1: ( rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 )
            // InternalCQL.g:1689:2: rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1
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
    // InternalCQL.g:1696:1: rule__Comparison__Group_1__0__Impl : ( () ) ;
    public final void rule__Comparison__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1700:1: ( ( () ) )
            // InternalCQL.g:1701:1: ( () )
            {
            // InternalCQL.g:1701:1: ( () )
            // InternalCQL.g:1702:2: ()
            {
             before(grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0()); 
            // InternalCQL.g:1703:2: ()
            // InternalCQL.g:1703:3: 
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
    // InternalCQL.g:1711:1: rule__Comparison__Group_1__1 : rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 ;
    public final void rule__Comparison__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1715:1: ( rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 )
            // InternalCQL.g:1716:2: rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2
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
    // InternalCQL.g:1723:1: rule__Comparison__Group_1__1__Impl : ( ( rule__Comparison__OpAssignment_1_1 ) ) ;
    public final void rule__Comparison__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1727:1: ( ( ( rule__Comparison__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1728:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1728:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            // InternalCQL.g:1729:2: ( rule__Comparison__OpAssignment_1_1 )
            {
             before(grammarAccess.getComparisonAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1730:2: ( rule__Comparison__OpAssignment_1_1 )
            // InternalCQL.g:1730:3: rule__Comparison__OpAssignment_1_1
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
    // InternalCQL.g:1738:1: rule__Comparison__Group_1__2 : rule__Comparison__Group_1__2__Impl ;
    public final void rule__Comparison__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1742:1: ( rule__Comparison__Group_1__2__Impl )
            // InternalCQL.g:1743:2: rule__Comparison__Group_1__2__Impl
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
    // InternalCQL.g:1749:1: rule__Comparison__Group_1__2__Impl : ( ( rule__Comparison__RightAssignment_1_2 ) ) ;
    public final void rule__Comparison__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1753:1: ( ( ( rule__Comparison__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1754:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1754:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            // InternalCQL.g:1755:2: ( rule__Comparison__RightAssignment_1_2 )
            {
             before(grammarAccess.getComparisonAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1756:2: ( rule__Comparison__RightAssignment_1_2 )
            // InternalCQL.g:1756:3: rule__Comparison__RightAssignment_1_2
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
    // InternalCQL.g:1765:1: rule__PlusOrMinus__Group__0 : rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 ;
    public final void rule__PlusOrMinus__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1769:1: ( rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 )
            // InternalCQL.g:1770:2: rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1
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
    // InternalCQL.g:1777:1: rule__PlusOrMinus__Group__0__Impl : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1781:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:1782:1: ( ruleMulOrDiv )
            {
            // InternalCQL.g:1782:1: ( ruleMulOrDiv )
            // InternalCQL.g:1783:2: ruleMulOrDiv
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
    // InternalCQL.g:1792:1: rule__PlusOrMinus__Group__1 : rule__PlusOrMinus__Group__1__Impl ;
    public final void rule__PlusOrMinus__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1796:1: ( rule__PlusOrMinus__Group__1__Impl )
            // InternalCQL.g:1797:2: rule__PlusOrMinus__Group__1__Impl
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
    // InternalCQL.g:1803:1: rule__PlusOrMinus__Group__1__Impl : ( ( rule__PlusOrMinus__Group_1__0 )* ) ;
    public final void rule__PlusOrMinus__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1807:1: ( ( ( rule__PlusOrMinus__Group_1__0 )* ) )
            // InternalCQL.g:1808:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            {
            // InternalCQL.g:1808:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            // InternalCQL.g:1809:2: ( rule__PlusOrMinus__Group_1__0 )*
            {
             before(grammarAccess.getPlusOrMinusAccess().getGroup_1()); 
            // InternalCQL.g:1810:2: ( rule__PlusOrMinus__Group_1__0 )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>=33 && LA18_0<=34)) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // InternalCQL.g:1810:3: rule__PlusOrMinus__Group_1__0
            	    {
            	    pushFollow(FOLLOW_22);
            	    rule__PlusOrMinus__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop18;
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
    // InternalCQL.g:1819:1: rule__PlusOrMinus__Group_1__0 : rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 ;
    public final void rule__PlusOrMinus__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1823:1: ( rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 )
            // InternalCQL.g:1824:2: rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1
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
    // InternalCQL.g:1831:1: rule__PlusOrMinus__Group_1__0__Impl : ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) ;
    public final void rule__PlusOrMinus__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1835:1: ( ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) )
            // InternalCQL.g:1836:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            {
            // InternalCQL.g:1836:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            // InternalCQL.g:1837:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getAlternatives_1_0()); 
            // InternalCQL.g:1838:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            // InternalCQL.g:1838:3: rule__PlusOrMinus__Alternatives_1_0
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
    // InternalCQL.g:1846:1: rule__PlusOrMinus__Group_1__1 : rule__PlusOrMinus__Group_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1850:1: ( rule__PlusOrMinus__Group_1__1__Impl )
            // InternalCQL.g:1851:2: rule__PlusOrMinus__Group_1__1__Impl
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
    // InternalCQL.g:1857:1: rule__PlusOrMinus__Group_1__1__Impl : ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) ;
    public final void rule__PlusOrMinus__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1861:1: ( ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) )
            // InternalCQL.g:1862:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            {
            // InternalCQL.g:1862:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            // InternalCQL.g:1863:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getRightAssignment_1_1()); 
            // InternalCQL.g:1864:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            // InternalCQL.g:1864:3: rule__PlusOrMinus__RightAssignment_1_1
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
    // InternalCQL.g:1873:1: rule__PlusOrMinus__Group_1_0_0__0 : rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 ;
    public final void rule__PlusOrMinus__Group_1_0_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1877:1: ( rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 )
            // InternalCQL.g:1878:2: rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1
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
    // InternalCQL.g:1885:1: rule__PlusOrMinus__Group_1_0_0__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1889:1: ( ( () ) )
            // InternalCQL.g:1890:1: ( () )
            {
            // InternalCQL.g:1890:1: ( () )
            // InternalCQL.g:1891:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0()); 
            // InternalCQL.g:1892:2: ()
            // InternalCQL.g:1892:3: 
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
    // InternalCQL.g:1900:1: rule__PlusOrMinus__Group_1_0_0__1 : rule__PlusOrMinus__Group_1_0_0__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1904:1: ( rule__PlusOrMinus__Group_1_0_0__1__Impl )
            // InternalCQL.g:1905:2: rule__PlusOrMinus__Group_1_0_0__1__Impl
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
    // InternalCQL.g:1911:1: rule__PlusOrMinus__Group_1_0_0__1__Impl : ( '+' ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1915:1: ( ( '+' ) )
            // InternalCQL.g:1916:1: ( '+' )
            {
            // InternalCQL.g:1916:1: ( '+' )
            // InternalCQL.g:1917:2: '+'
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1()); 
            match(input,33,FOLLOW_2); 
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
    // InternalCQL.g:1927:1: rule__PlusOrMinus__Group_1_0_1__0 : rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 ;
    public final void rule__PlusOrMinus__Group_1_0_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1931:1: ( rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 )
            // InternalCQL.g:1932:2: rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1
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
    // InternalCQL.g:1939:1: rule__PlusOrMinus__Group_1_0_1__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1943:1: ( ( () ) )
            // InternalCQL.g:1944:1: ( () )
            {
            // InternalCQL.g:1944:1: ( () )
            // InternalCQL.g:1945:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0()); 
            // InternalCQL.g:1946:2: ()
            // InternalCQL.g:1946:3: 
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
    // InternalCQL.g:1954:1: rule__PlusOrMinus__Group_1_0_1__1 : rule__PlusOrMinus__Group_1_0_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1958:1: ( rule__PlusOrMinus__Group_1_0_1__1__Impl )
            // InternalCQL.g:1959:2: rule__PlusOrMinus__Group_1_0_1__1__Impl
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
    // InternalCQL.g:1965:1: rule__PlusOrMinus__Group_1_0_1__1__Impl : ( '-' ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1969:1: ( ( '-' ) )
            // InternalCQL.g:1970:1: ( '-' )
            {
            // InternalCQL.g:1970:1: ( '-' )
            // InternalCQL.g:1971:2: '-'
            {
             before(grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1()); 
            match(input,34,FOLLOW_2); 
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
    // InternalCQL.g:1981:1: rule__MulOrDiv__Group__0 : rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 ;
    public final void rule__MulOrDiv__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1985:1: ( rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 )
            // InternalCQL.g:1986:2: rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1
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
    // InternalCQL.g:1993:1: rule__MulOrDiv__Group__0__Impl : ( rulePrimary ) ;
    public final void rule__MulOrDiv__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1997:1: ( ( rulePrimary ) )
            // InternalCQL.g:1998:1: ( rulePrimary )
            {
            // InternalCQL.g:1998:1: ( rulePrimary )
            // InternalCQL.g:1999:2: rulePrimary
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
    // InternalCQL.g:2008:1: rule__MulOrDiv__Group__1 : rule__MulOrDiv__Group__1__Impl ;
    public final void rule__MulOrDiv__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2012:1: ( rule__MulOrDiv__Group__1__Impl )
            // InternalCQL.g:2013:2: rule__MulOrDiv__Group__1__Impl
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
    // InternalCQL.g:2019:1: rule__MulOrDiv__Group__1__Impl : ( ( rule__MulOrDiv__Group_1__0 )* ) ;
    public final void rule__MulOrDiv__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2023:1: ( ( ( rule__MulOrDiv__Group_1__0 )* ) )
            // InternalCQL.g:2024:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            {
            // InternalCQL.g:2024:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            // InternalCQL.g:2025:2: ( rule__MulOrDiv__Group_1__0 )*
            {
             before(grammarAccess.getMulOrDivAccess().getGroup_1()); 
            // InternalCQL.g:2026:2: ( rule__MulOrDiv__Group_1__0 )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>=27 && LA19_0<=28)) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalCQL.g:2026:3: rule__MulOrDiv__Group_1__0
            	    {
            	    pushFollow(FOLLOW_25);
            	    rule__MulOrDiv__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop19;
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
    // InternalCQL.g:2035:1: rule__MulOrDiv__Group_1__0 : rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 ;
    public final void rule__MulOrDiv__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2039:1: ( rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 )
            // InternalCQL.g:2040:2: rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1
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
    // InternalCQL.g:2047:1: rule__MulOrDiv__Group_1__0__Impl : ( () ) ;
    public final void rule__MulOrDiv__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2051:1: ( ( () ) )
            // InternalCQL.g:2052:1: ( () )
            {
            // InternalCQL.g:2052:1: ( () )
            // InternalCQL.g:2053:2: ()
            {
             before(grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0()); 
            // InternalCQL.g:2054:2: ()
            // InternalCQL.g:2054:3: 
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
    // InternalCQL.g:2062:1: rule__MulOrDiv__Group_1__1 : rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 ;
    public final void rule__MulOrDiv__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2066:1: ( rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 )
            // InternalCQL.g:2067:2: rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2
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
    // InternalCQL.g:2074:1: rule__MulOrDiv__Group_1__1__Impl : ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) ;
    public final void rule__MulOrDiv__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2078:1: ( ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) )
            // InternalCQL.g:2079:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:2079:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            // InternalCQL.g:2080:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:2081:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            // InternalCQL.g:2081:3: rule__MulOrDiv__OpAssignment_1_1
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
    // InternalCQL.g:2089:1: rule__MulOrDiv__Group_1__2 : rule__MulOrDiv__Group_1__2__Impl ;
    public final void rule__MulOrDiv__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2093:1: ( rule__MulOrDiv__Group_1__2__Impl )
            // InternalCQL.g:2094:2: rule__MulOrDiv__Group_1__2__Impl
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
    // InternalCQL.g:2100:1: rule__MulOrDiv__Group_1__2__Impl : ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) ;
    public final void rule__MulOrDiv__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2104:1: ( ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) )
            // InternalCQL.g:2105:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:2105:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            // InternalCQL.g:2106:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            {
             before(grammarAccess.getMulOrDivAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:2107:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            // InternalCQL.g:2107:3: rule__MulOrDiv__RightAssignment_1_2
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
    // InternalCQL.g:2116:1: rule__Primary__Group_0__0 : rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 ;
    public final void rule__Primary__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2120:1: ( rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 )
            // InternalCQL.g:2121:2: rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1
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
    // InternalCQL.g:2128:1: rule__Primary__Group_0__0__Impl : ( () ) ;
    public final void rule__Primary__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2132:1: ( ( () ) )
            // InternalCQL.g:2133:1: ( () )
            {
            // InternalCQL.g:2133:1: ( () )
            // InternalCQL.g:2134:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getBracketAction_0_0()); 
            // InternalCQL.g:2135:2: ()
            // InternalCQL.g:2135:3: 
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
    // InternalCQL.g:2143:1: rule__Primary__Group_0__1 : rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 ;
    public final void rule__Primary__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2147:1: ( rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 )
            // InternalCQL.g:2148:2: rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2
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
    // InternalCQL.g:2155:1: rule__Primary__Group_0__1__Impl : ( '(' ) ;
    public final void rule__Primary__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2159:1: ( ( '(' ) )
            // InternalCQL.g:2160:1: ( '(' )
            {
            // InternalCQL.g:2160:1: ( '(' )
            // InternalCQL.g:2161:2: '('
            {
             before(grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1()); 
            match(input,35,FOLLOW_2); 
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
    // InternalCQL.g:2170:1: rule__Primary__Group_0__2 : rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 ;
    public final void rule__Primary__Group_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2174:1: ( rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 )
            // InternalCQL.g:2175:2: rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3
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
    // InternalCQL.g:2182:1: rule__Primary__Group_0__2__Impl : ( ( rule__Primary__InnerAssignment_0_2 ) ) ;
    public final void rule__Primary__Group_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2186:1: ( ( ( rule__Primary__InnerAssignment_0_2 ) ) )
            // InternalCQL.g:2187:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            {
            // InternalCQL.g:2187:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            // InternalCQL.g:2188:2: ( rule__Primary__InnerAssignment_0_2 )
            {
             before(grammarAccess.getPrimaryAccess().getInnerAssignment_0_2()); 
            // InternalCQL.g:2189:2: ( rule__Primary__InnerAssignment_0_2 )
            // InternalCQL.g:2189:3: rule__Primary__InnerAssignment_0_2
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
    // InternalCQL.g:2197:1: rule__Primary__Group_0__3 : rule__Primary__Group_0__3__Impl ;
    public final void rule__Primary__Group_0__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2201:1: ( rule__Primary__Group_0__3__Impl )
            // InternalCQL.g:2202:2: rule__Primary__Group_0__3__Impl
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
    // InternalCQL.g:2208:1: rule__Primary__Group_0__3__Impl : ( ')' ) ;
    public final void rule__Primary__Group_0__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2212:1: ( ( ')' ) )
            // InternalCQL.g:2213:1: ( ')' )
            {
            // InternalCQL.g:2213:1: ( ')' )
            // InternalCQL.g:2214:2: ')'
            {
             before(grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3()); 
            match(input,36,FOLLOW_2); 
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
    // InternalCQL.g:2224:1: rule__Primary__Group_1__0 : rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 ;
    public final void rule__Primary__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2228:1: ( rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 )
            // InternalCQL.g:2229:2: rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1
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
    // InternalCQL.g:2236:1: rule__Primary__Group_1__0__Impl : ( () ) ;
    public final void rule__Primary__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2240:1: ( ( () ) )
            // InternalCQL.g:2241:1: ( () )
            {
            // InternalCQL.g:2241:1: ( () )
            // InternalCQL.g:2242:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getNOTAction_1_0()); 
            // InternalCQL.g:2243:2: ()
            // InternalCQL.g:2243:3: 
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
    // InternalCQL.g:2251:1: rule__Primary__Group_1__1 : rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 ;
    public final void rule__Primary__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2255:1: ( rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 )
            // InternalCQL.g:2256:2: rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2
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
    // InternalCQL.g:2263:1: rule__Primary__Group_1__1__Impl : ( 'NOT' ) ;
    public final void rule__Primary__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2267:1: ( ( 'NOT' ) )
            // InternalCQL.g:2268:1: ( 'NOT' )
            {
            // InternalCQL.g:2268:1: ( 'NOT' )
            // InternalCQL.g:2269:2: 'NOT'
            {
             before(grammarAccess.getPrimaryAccess().getNOTKeyword_1_1()); 
            match(input,37,FOLLOW_2); 
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
    // InternalCQL.g:2278:1: rule__Primary__Group_1__2 : rule__Primary__Group_1__2__Impl ;
    public final void rule__Primary__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2282:1: ( rule__Primary__Group_1__2__Impl )
            // InternalCQL.g:2283:2: rule__Primary__Group_1__2__Impl
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
    // InternalCQL.g:2289:1: rule__Primary__Group_1__2__Impl : ( ( rule__Primary__ExpressionAssignment_1_2 ) ) ;
    public final void rule__Primary__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2293:1: ( ( ( rule__Primary__ExpressionAssignment_1_2 ) ) )
            // InternalCQL.g:2294:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            {
            // InternalCQL.g:2294:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            // InternalCQL.g:2295:2: ( rule__Primary__ExpressionAssignment_1_2 )
            {
             before(grammarAccess.getPrimaryAccess().getExpressionAssignment_1_2()); 
            // InternalCQL.g:2296:2: ( rule__Primary__ExpressionAssignment_1_2 )
            // InternalCQL.g:2296:3: rule__Primary__ExpressionAssignment_1_2
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
    // InternalCQL.g:2305:1: rule__Select_Statement__Group__0 : rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 ;
    public final void rule__Select_Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2309:1: ( rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 )
            // InternalCQL.g:2310:2: rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1
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
    // InternalCQL.g:2317:1: rule__Select_Statement__Group__0__Impl : ( ( rule__Select_Statement__NameAssignment_0 ) ) ;
    public final void rule__Select_Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2321:1: ( ( ( rule__Select_Statement__NameAssignment_0 ) ) )
            // InternalCQL.g:2322:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            {
            // InternalCQL.g:2322:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            // InternalCQL.g:2323:2: ( rule__Select_Statement__NameAssignment_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameAssignment_0()); 
            // InternalCQL.g:2324:2: ( rule__Select_Statement__NameAssignment_0 )
            // InternalCQL.g:2324:3: rule__Select_Statement__NameAssignment_0
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
    // InternalCQL.g:2332:1: rule__Select_Statement__Group__1 : rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 ;
    public final void rule__Select_Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2336:1: ( rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 )
            // InternalCQL.g:2337:2: rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2
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
    // InternalCQL.g:2344:1: rule__Select_Statement__Group__1__Impl : ( ( 'DISTINCT' )? ) ;
    public final void rule__Select_Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2348:1: ( ( ( 'DISTINCT' )? ) )
            // InternalCQL.g:2349:1: ( ( 'DISTINCT' )? )
            {
            // InternalCQL.g:2349:1: ( ( 'DISTINCT' )? )
            // InternalCQL.g:2350:2: ( 'DISTINCT' )?
            {
             before(grammarAccess.getSelect_StatementAccess().getDISTINCTKeyword_1()); 
            // InternalCQL.g:2351:2: ( 'DISTINCT' )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==38) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQL.g:2351:3: 'DISTINCT'
                    {
                    match(input,38,FOLLOW_2); 

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
    // InternalCQL.g:2359:1: rule__Select_Statement__Group__2 : rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 ;
    public final void rule__Select_Statement__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2363:1: ( rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 )
            // InternalCQL.g:2364:2: rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3
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
    // InternalCQL.g:2371:1: rule__Select_Statement__Group__2__Impl : ( ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* ) ) ;
    public final void rule__Select_Statement__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2375:1: ( ( ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* ) ) )
            // InternalCQL.g:2376:1: ( ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* ) )
            {
            // InternalCQL.g:2376:1: ( ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* ) )
            // InternalCQL.g:2377:2: ( ( rule__Select_Statement__AttributesAssignment_2 ) ) ( ( rule__Select_Statement__AttributesAssignment_2 )* )
            {
            // InternalCQL.g:2377:2: ( ( rule__Select_Statement__AttributesAssignment_2 ) )
            // InternalCQL.g:2378:3: ( rule__Select_Statement__AttributesAssignment_2 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2()); 
            // InternalCQL.g:2379:3: ( rule__Select_Statement__AttributesAssignment_2 )
            // InternalCQL.g:2379:4: rule__Select_Statement__AttributesAssignment_2
            {
            pushFollow(FOLLOW_31);
            rule__Select_Statement__AttributesAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2()); 

            }

            // InternalCQL.g:2382:2: ( ( rule__Select_Statement__AttributesAssignment_2 )* )
            // InternalCQL.g:2383:3: ( rule__Select_Statement__AttributesAssignment_2 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2()); 
            // InternalCQL.g:2384:3: ( rule__Select_Statement__AttributesAssignment_2 )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==RULE_ID) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // InternalCQL.g:2384:4: rule__Select_Statement__AttributesAssignment_2
            	    {
            	    pushFollow(FOLLOW_31);
            	    rule__Select_Statement__AttributesAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop21;
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
    // InternalCQL.g:2393:1: rule__Select_Statement__Group__3 : rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 ;
    public final void rule__Select_Statement__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2397:1: ( rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 )
            // InternalCQL.g:2398:2: rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4
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
    // InternalCQL.g:2405:1: rule__Select_Statement__Group__3__Impl : ( ( rule__Select_Statement__Group_3__0 )* ) ;
    public final void rule__Select_Statement__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2409:1: ( ( ( rule__Select_Statement__Group_3__0 )* ) )
            // InternalCQL.g:2410:1: ( ( rule__Select_Statement__Group_3__0 )* )
            {
            // InternalCQL.g:2410:1: ( ( rule__Select_Statement__Group_3__0 )* )
            // InternalCQL.g:2411:2: ( rule__Select_Statement__Group_3__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_3()); 
            // InternalCQL.g:2412:2: ( rule__Select_Statement__Group_3__0 )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==40) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // InternalCQL.g:2412:3: rule__Select_Statement__Group_3__0
            	    {
            	    pushFollow(FOLLOW_32);
            	    rule__Select_Statement__Group_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop22;
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
    // InternalCQL.g:2420:1: rule__Select_Statement__Group__4 : rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 ;
    public final void rule__Select_Statement__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2424:1: ( rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 )
            // InternalCQL.g:2425:2: rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5
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
    // InternalCQL.g:2432:1: rule__Select_Statement__Group__4__Impl : ( 'FROM' ) ;
    public final void rule__Select_Statement__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2436:1: ( ( 'FROM' ) )
            // InternalCQL.g:2437:1: ( 'FROM' )
            {
            // InternalCQL.g:2437:1: ( 'FROM' )
            // InternalCQL.g:2438:2: 'FROM'
            {
             before(grammarAccess.getSelect_StatementAccess().getFROMKeyword_4()); 
            match(input,39,FOLLOW_2); 
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
    // InternalCQL.g:2447:1: rule__Select_Statement__Group__5 : rule__Select_Statement__Group__5__Impl rule__Select_Statement__Group__6 ;
    public final void rule__Select_Statement__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2451:1: ( rule__Select_Statement__Group__5__Impl rule__Select_Statement__Group__6 )
            // InternalCQL.g:2452:2: rule__Select_Statement__Group__5__Impl rule__Select_Statement__Group__6
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
    // InternalCQL.g:2459:1: rule__Select_Statement__Group__5__Impl : ( ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* ) ) ;
    public final void rule__Select_Statement__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2463:1: ( ( ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* ) ) )
            // InternalCQL.g:2464:1: ( ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* ) )
            {
            // InternalCQL.g:2464:1: ( ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* ) )
            // InternalCQL.g:2465:2: ( ( rule__Select_Statement__SourcesAssignment_5 ) ) ( ( rule__Select_Statement__SourcesAssignment_5 )* )
            {
            // InternalCQL.g:2465:2: ( ( rule__Select_Statement__SourcesAssignment_5 ) )
            // InternalCQL.g:2466:3: ( rule__Select_Statement__SourcesAssignment_5 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_5()); 
            // InternalCQL.g:2467:3: ( rule__Select_Statement__SourcesAssignment_5 )
            // InternalCQL.g:2467:4: rule__Select_Statement__SourcesAssignment_5
            {
            pushFollow(FOLLOW_34);
            rule__Select_Statement__SourcesAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_5()); 

            }

            // InternalCQL.g:2470:2: ( ( rule__Select_Statement__SourcesAssignment_5 )* )
            // InternalCQL.g:2471:3: ( rule__Select_Statement__SourcesAssignment_5 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_5()); 
            // InternalCQL.g:2472:3: ( rule__Select_Statement__SourcesAssignment_5 )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==RULE_ID) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // InternalCQL.g:2472:4: rule__Select_Statement__SourcesAssignment_5
            	    {
            	    pushFollow(FOLLOW_34);
            	    rule__Select_Statement__SourcesAssignment_5();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop23;
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
    // InternalCQL.g:2481:1: rule__Select_Statement__Group__6 : rule__Select_Statement__Group__6__Impl rule__Select_Statement__Group__7 ;
    public final void rule__Select_Statement__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2485:1: ( rule__Select_Statement__Group__6__Impl rule__Select_Statement__Group__7 )
            // InternalCQL.g:2486:2: rule__Select_Statement__Group__6__Impl rule__Select_Statement__Group__7
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
    // InternalCQL.g:2493:1: rule__Select_Statement__Group__6__Impl : ( ( rule__Select_Statement__Group_6__0 )* ) ;
    public final void rule__Select_Statement__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2497:1: ( ( ( rule__Select_Statement__Group_6__0 )* ) )
            // InternalCQL.g:2498:1: ( ( rule__Select_Statement__Group_6__0 )* )
            {
            // InternalCQL.g:2498:1: ( ( rule__Select_Statement__Group_6__0 )* )
            // InternalCQL.g:2499:2: ( rule__Select_Statement__Group_6__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_6()); 
            // InternalCQL.g:2500:2: ( rule__Select_Statement__Group_6__0 )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==40) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // InternalCQL.g:2500:3: rule__Select_Statement__Group_6__0
            	    {
            	    pushFollow(FOLLOW_32);
            	    rule__Select_Statement__Group_6__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop24;
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
    // InternalCQL.g:2508:1: rule__Select_Statement__Group__7 : rule__Select_Statement__Group__7__Impl ;
    public final void rule__Select_Statement__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2512:1: ( rule__Select_Statement__Group__7__Impl )
            // InternalCQL.g:2513:2: rule__Select_Statement__Group__7__Impl
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
    // InternalCQL.g:2519:1: rule__Select_Statement__Group__7__Impl : ( ( rule__Select_Statement__Group_7__0 ) ) ;
    public final void rule__Select_Statement__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2523:1: ( ( ( rule__Select_Statement__Group_7__0 ) ) )
            // InternalCQL.g:2524:1: ( ( rule__Select_Statement__Group_7__0 ) )
            {
            // InternalCQL.g:2524:1: ( ( rule__Select_Statement__Group_7__0 ) )
            // InternalCQL.g:2525:2: ( rule__Select_Statement__Group_7__0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_7()); 
            // InternalCQL.g:2526:2: ( rule__Select_Statement__Group_7__0 )
            // InternalCQL.g:2526:3: rule__Select_Statement__Group_7__0
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
    // InternalCQL.g:2535:1: rule__Select_Statement__Group_3__0 : rule__Select_Statement__Group_3__0__Impl rule__Select_Statement__Group_3__1 ;
    public final void rule__Select_Statement__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2539:1: ( rule__Select_Statement__Group_3__0__Impl rule__Select_Statement__Group_3__1 )
            // InternalCQL.g:2540:2: rule__Select_Statement__Group_3__0__Impl rule__Select_Statement__Group_3__1
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
    // InternalCQL.g:2547:1: rule__Select_Statement__Group_3__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2551:1: ( ( ',' ) )
            // InternalCQL.g:2552:1: ( ',' )
            {
            // InternalCQL.g:2552:1: ( ',' )
            // InternalCQL.g:2553:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_3_0()); 
            match(input,40,FOLLOW_2); 
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
    // InternalCQL.g:2562:1: rule__Select_Statement__Group_3__1 : rule__Select_Statement__Group_3__1__Impl ;
    public final void rule__Select_Statement__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2566:1: ( rule__Select_Statement__Group_3__1__Impl )
            // InternalCQL.g:2567:2: rule__Select_Statement__Group_3__1__Impl
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
    // InternalCQL.g:2573:1: rule__Select_Statement__Group_3__1__Impl : ( ( rule__Select_Statement__AttributesAssignment_3_1 ) ) ;
    public final void rule__Select_Statement__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2577:1: ( ( ( rule__Select_Statement__AttributesAssignment_3_1 ) ) )
            // InternalCQL.g:2578:1: ( ( rule__Select_Statement__AttributesAssignment_3_1 ) )
            {
            // InternalCQL.g:2578:1: ( ( rule__Select_Statement__AttributesAssignment_3_1 ) )
            // InternalCQL.g:2579:2: ( rule__Select_Statement__AttributesAssignment_3_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_3_1()); 
            // InternalCQL.g:2580:2: ( rule__Select_Statement__AttributesAssignment_3_1 )
            // InternalCQL.g:2580:3: rule__Select_Statement__AttributesAssignment_3_1
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
    // InternalCQL.g:2589:1: rule__Select_Statement__Group_6__0 : rule__Select_Statement__Group_6__0__Impl rule__Select_Statement__Group_6__1 ;
    public final void rule__Select_Statement__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2593:1: ( rule__Select_Statement__Group_6__0__Impl rule__Select_Statement__Group_6__1 )
            // InternalCQL.g:2594:2: rule__Select_Statement__Group_6__0__Impl rule__Select_Statement__Group_6__1
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
    // InternalCQL.g:2601:1: rule__Select_Statement__Group_6__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2605:1: ( ( ',' ) )
            // InternalCQL.g:2606:1: ( ',' )
            {
            // InternalCQL.g:2606:1: ( ',' )
            // InternalCQL.g:2607:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_6_0()); 
            match(input,40,FOLLOW_2); 
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
    // InternalCQL.g:2616:1: rule__Select_Statement__Group_6__1 : rule__Select_Statement__Group_6__1__Impl ;
    public final void rule__Select_Statement__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2620:1: ( rule__Select_Statement__Group_6__1__Impl )
            // InternalCQL.g:2621:2: rule__Select_Statement__Group_6__1__Impl
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
    // InternalCQL.g:2627:1: rule__Select_Statement__Group_6__1__Impl : ( ( rule__Select_Statement__SourcesAssignment_6_1 ) ) ;
    public final void rule__Select_Statement__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2631:1: ( ( ( rule__Select_Statement__SourcesAssignment_6_1 ) ) )
            // InternalCQL.g:2632:1: ( ( rule__Select_Statement__SourcesAssignment_6_1 ) )
            {
            // InternalCQL.g:2632:1: ( ( rule__Select_Statement__SourcesAssignment_6_1 ) )
            // InternalCQL.g:2633:2: ( rule__Select_Statement__SourcesAssignment_6_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_6_1()); 
            // InternalCQL.g:2634:2: ( rule__Select_Statement__SourcesAssignment_6_1 )
            // InternalCQL.g:2634:3: rule__Select_Statement__SourcesAssignment_6_1
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
    // InternalCQL.g:2643:1: rule__Select_Statement__Group_7__0 : rule__Select_Statement__Group_7__0__Impl rule__Select_Statement__Group_7__1 ;
    public final void rule__Select_Statement__Group_7__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2647:1: ( rule__Select_Statement__Group_7__0__Impl rule__Select_Statement__Group_7__1 )
            // InternalCQL.g:2648:2: rule__Select_Statement__Group_7__0__Impl rule__Select_Statement__Group_7__1
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
    // InternalCQL.g:2655:1: rule__Select_Statement__Group_7__0__Impl : ( 'WHERE' ) ;
    public final void rule__Select_Statement__Group_7__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2659:1: ( ( 'WHERE' ) )
            // InternalCQL.g:2660:1: ( 'WHERE' )
            {
            // InternalCQL.g:2660:1: ( 'WHERE' )
            // InternalCQL.g:2661:2: 'WHERE'
            {
             before(grammarAccess.getSelect_StatementAccess().getWHEREKeyword_7_0()); 
            match(input,41,FOLLOW_2); 
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
    // InternalCQL.g:2670:1: rule__Select_Statement__Group_7__1 : rule__Select_Statement__Group_7__1__Impl ;
    public final void rule__Select_Statement__Group_7__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2674:1: ( rule__Select_Statement__Group_7__1__Impl )
            // InternalCQL.g:2675:2: rule__Select_Statement__Group_7__1__Impl
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
    // InternalCQL.g:2681:1: rule__Select_Statement__Group_7__1__Impl : ( ( rule__Select_Statement__PredicatesAssignment_7_1 ) ) ;
    public final void rule__Select_Statement__Group_7__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2685:1: ( ( ( rule__Select_Statement__PredicatesAssignment_7_1 ) ) )
            // InternalCQL.g:2686:1: ( ( rule__Select_Statement__PredicatesAssignment_7_1 ) )
            {
            // InternalCQL.g:2686:1: ( ( rule__Select_Statement__PredicatesAssignment_7_1 ) )
            // InternalCQL.g:2687:2: ( rule__Select_Statement__PredicatesAssignment_7_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getPredicatesAssignment_7_1()); 
            // InternalCQL.g:2688:2: ( rule__Select_Statement__PredicatesAssignment_7_1 )
            // InternalCQL.g:2688:3: rule__Select_Statement__PredicatesAssignment_7_1
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


    // $ANTLR start "rule__Create_Statement__Group__0"
    // InternalCQL.g:2697:1: rule__Create_Statement__Group__0 : rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1 ;
    public final void rule__Create_Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2701:1: ( rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1 )
            // InternalCQL.g:2702:2: rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1
            {
            pushFollow(FOLLOW_35);
            rule__Create_Statement__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__0"


    // $ANTLR start "rule__Create_Statement__Group__0__Impl"
    // InternalCQL.g:2709:1: rule__Create_Statement__Group__0__Impl : ( 'CREATE' ) ;
    public final void rule__Create_Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2713:1: ( ( 'CREATE' ) )
            // InternalCQL.g:2714:1: ( 'CREATE' )
            {
            // InternalCQL.g:2714:1: ( 'CREATE' )
            // InternalCQL.g:2715:2: 'CREATE'
            {
             before(grammarAccess.getCreate_StatementAccess().getCREATEKeyword_0()); 
            match(input,42,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getCREATEKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__0__Impl"


    // $ANTLR start "rule__Create_Statement__Group__1"
    // InternalCQL.g:2724:1: rule__Create_Statement__Group__1 : rule__Create_Statement__Group__1__Impl rule__Create_Statement__Group__2 ;
    public final void rule__Create_Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2728:1: ( rule__Create_Statement__Group__1__Impl rule__Create_Statement__Group__2 )
            // InternalCQL.g:2729:2: rule__Create_Statement__Group__1__Impl rule__Create_Statement__Group__2
            {
            pushFollow(FOLLOW_9);
            rule__Create_Statement__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__1"


    // $ANTLR start "rule__Create_Statement__Group__1__Impl"
    // InternalCQL.g:2736:1: rule__Create_Statement__Group__1__Impl : ( 'STREAM' ) ;
    public final void rule__Create_Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2740:1: ( ( 'STREAM' ) )
            // InternalCQL.g:2741:1: ( 'STREAM' )
            {
            // InternalCQL.g:2741:1: ( 'STREAM' )
            // InternalCQL.g:2742:2: 'STREAM'
            {
             before(grammarAccess.getCreate_StatementAccess().getSTREAMKeyword_1()); 
            match(input,43,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getSTREAMKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__1__Impl"


    // $ANTLR start "rule__Create_Statement__Group__2"
    // InternalCQL.g:2751:1: rule__Create_Statement__Group__2 : rule__Create_Statement__Group__2__Impl rule__Create_Statement__Group__3 ;
    public final void rule__Create_Statement__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2755:1: ( rule__Create_Statement__Group__2__Impl rule__Create_Statement__Group__3 )
            // InternalCQL.g:2756:2: rule__Create_Statement__Group__2__Impl rule__Create_Statement__Group__3
            {
            pushFollow(FOLLOW_26);
            rule__Create_Statement__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__2"


    // $ANTLR start "rule__Create_Statement__Group__2__Impl"
    // InternalCQL.g:2763:1: rule__Create_Statement__Group__2__Impl : ( ( rule__Create_Statement__NameAssignment_2 ) ) ;
    public final void rule__Create_Statement__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2767:1: ( ( ( rule__Create_Statement__NameAssignment_2 ) ) )
            // InternalCQL.g:2768:1: ( ( rule__Create_Statement__NameAssignment_2 ) )
            {
            // InternalCQL.g:2768:1: ( ( rule__Create_Statement__NameAssignment_2 ) )
            // InternalCQL.g:2769:2: ( rule__Create_Statement__NameAssignment_2 )
            {
             before(grammarAccess.getCreate_StatementAccess().getNameAssignment_2()); 
            // InternalCQL.g:2770:2: ( rule__Create_Statement__NameAssignment_2 )
            // InternalCQL.g:2770:3: rule__Create_Statement__NameAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__NameAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getNameAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__2__Impl"


    // $ANTLR start "rule__Create_Statement__Group__3"
    // InternalCQL.g:2778:1: rule__Create_Statement__Group__3 : rule__Create_Statement__Group__3__Impl rule__Create_Statement__Group__4 ;
    public final void rule__Create_Statement__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2782:1: ( rule__Create_Statement__Group__3__Impl rule__Create_Statement__Group__4 )
            // InternalCQL.g:2783:2: rule__Create_Statement__Group__3__Impl rule__Create_Statement__Group__4
            {
            pushFollow(FOLLOW_29);
            rule__Create_Statement__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__3"


    // $ANTLR start "rule__Create_Statement__Group__3__Impl"
    // InternalCQL.g:2790:1: rule__Create_Statement__Group__3__Impl : ( '(' ) ;
    public final void rule__Create_Statement__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2794:1: ( ( '(' ) )
            // InternalCQL.g:2795:1: ( '(' )
            {
            // InternalCQL.g:2795:1: ( '(' )
            // InternalCQL.g:2796:2: '('
            {
             before(grammarAccess.getCreate_StatementAccess().getLeftParenthesisKeyword_3()); 
            match(input,35,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getLeftParenthesisKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__3__Impl"


    // $ANTLR start "rule__Create_Statement__Group__4"
    // InternalCQL.g:2805:1: rule__Create_Statement__Group__4 : rule__Create_Statement__Group__4__Impl rule__Create_Statement__Group__5 ;
    public final void rule__Create_Statement__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2809:1: ( rule__Create_Statement__Group__4__Impl rule__Create_Statement__Group__5 )
            // InternalCQL.g:2810:2: rule__Create_Statement__Group__4__Impl rule__Create_Statement__Group__5
            {
            pushFollow(FOLLOW_36);
            rule__Create_Statement__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__4"


    // $ANTLR start "rule__Create_Statement__Group__4__Impl"
    // InternalCQL.g:2817:1: rule__Create_Statement__Group__4__Impl : ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) ) ;
    public final void rule__Create_Statement__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2821:1: ( ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) ) )
            // InternalCQL.g:2822:1: ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) )
            {
            // InternalCQL.g:2822:1: ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) )
            // InternalCQL.g:2823:2: ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* )
            {
            // InternalCQL.g:2823:2: ( ( rule__Create_Statement__AttributesAssignment_4 ) )
            // InternalCQL.g:2824:3: ( rule__Create_Statement__AttributesAssignment_4 )
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 
            // InternalCQL.g:2825:3: ( rule__Create_Statement__AttributesAssignment_4 )
            // InternalCQL.g:2825:4: rule__Create_Statement__AttributesAssignment_4
            {
            pushFollow(FOLLOW_31);
            rule__Create_Statement__AttributesAssignment_4();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 

            }

            // InternalCQL.g:2828:2: ( ( rule__Create_Statement__AttributesAssignment_4 )* )
            // InternalCQL.g:2829:3: ( rule__Create_Statement__AttributesAssignment_4 )*
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 
            // InternalCQL.g:2830:3: ( rule__Create_Statement__AttributesAssignment_4 )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==RULE_ID) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // InternalCQL.g:2830:4: rule__Create_Statement__AttributesAssignment_4
            	    {
            	    pushFollow(FOLLOW_31);
            	    rule__Create_Statement__AttributesAssignment_4();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop25;
                }
            } while (true);

             after(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 

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
    // $ANTLR end "rule__Create_Statement__Group__4__Impl"


    // $ANTLR start "rule__Create_Statement__Group__5"
    // InternalCQL.g:2839:1: rule__Create_Statement__Group__5 : rule__Create_Statement__Group__5__Impl rule__Create_Statement__Group__6 ;
    public final void rule__Create_Statement__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2843:1: ( rule__Create_Statement__Group__5__Impl rule__Create_Statement__Group__6 )
            // InternalCQL.g:2844:2: rule__Create_Statement__Group__5__Impl rule__Create_Statement__Group__6
            {
            pushFollow(FOLLOW_37);
            rule__Create_Statement__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__5"


    // $ANTLR start "rule__Create_Statement__Group__5__Impl"
    // InternalCQL.g:2851:1: rule__Create_Statement__Group__5__Impl : ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) ) ;
    public final void rule__Create_Statement__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2855:1: ( ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) ) )
            // InternalCQL.g:2856:1: ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) )
            {
            // InternalCQL.g:2856:1: ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) )
            // InternalCQL.g:2857:2: ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* )
            {
            // InternalCQL.g:2857:2: ( ( rule__Create_Statement__DatatypesAssignment_5 ) )
            // InternalCQL.g:2858:3: ( rule__Create_Statement__DatatypesAssignment_5 )
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 
            // InternalCQL.g:2859:3: ( rule__Create_Statement__DatatypesAssignment_5 )
            // InternalCQL.g:2859:4: rule__Create_Statement__DatatypesAssignment_5
            {
            pushFollow(FOLLOW_38);
            rule__Create_Statement__DatatypesAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 

            }

            // InternalCQL.g:2862:2: ( ( rule__Create_Statement__DatatypesAssignment_5 )* )
            // InternalCQL.g:2863:3: ( rule__Create_Statement__DatatypesAssignment_5 )*
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 
            // InternalCQL.g:2864:3: ( rule__Create_Statement__DatatypesAssignment_5 )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( ((LA26_0>=14 && LA26_0<=20)) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // InternalCQL.g:2864:4: rule__Create_Statement__DatatypesAssignment_5
            	    {
            	    pushFollow(FOLLOW_38);
            	    rule__Create_Statement__DatatypesAssignment_5();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);

             after(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 

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
    // $ANTLR end "rule__Create_Statement__Group__5__Impl"


    // $ANTLR start "rule__Create_Statement__Group__6"
    // InternalCQL.g:2873:1: rule__Create_Statement__Group__6 : rule__Create_Statement__Group__6__Impl rule__Create_Statement__Group__7 ;
    public final void rule__Create_Statement__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2877:1: ( rule__Create_Statement__Group__6__Impl rule__Create_Statement__Group__7 )
            // InternalCQL.g:2878:2: rule__Create_Statement__Group__6__Impl rule__Create_Statement__Group__7
            {
            pushFollow(FOLLOW_37);
            rule__Create_Statement__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__7();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__6"


    // $ANTLR start "rule__Create_Statement__Group__6__Impl"
    // InternalCQL.g:2885:1: rule__Create_Statement__Group__6__Impl : ( ( rule__Create_Statement__Group_6__0 )* ) ;
    public final void rule__Create_Statement__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2889:1: ( ( ( rule__Create_Statement__Group_6__0 )* ) )
            // InternalCQL.g:2890:1: ( ( rule__Create_Statement__Group_6__0 )* )
            {
            // InternalCQL.g:2890:1: ( ( rule__Create_Statement__Group_6__0 )* )
            // InternalCQL.g:2891:2: ( rule__Create_Statement__Group_6__0 )*
            {
             before(grammarAccess.getCreate_StatementAccess().getGroup_6()); 
            // InternalCQL.g:2892:2: ( rule__Create_Statement__Group_6__0 )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==40) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // InternalCQL.g:2892:3: rule__Create_Statement__Group_6__0
            	    {
            	    pushFollow(FOLLOW_32);
            	    rule__Create_Statement__Group_6__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);

             after(grammarAccess.getCreate_StatementAccess().getGroup_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__6__Impl"


    // $ANTLR start "rule__Create_Statement__Group__7"
    // InternalCQL.g:2900:1: rule__Create_Statement__Group__7 : rule__Create_Statement__Group__7__Impl ;
    public final void rule__Create_Statement__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2904:1: ( rule__Create_Statement__Group__7__Impl )
            // InternalCQL.g:2905:2: rule__Create_Statement__Group__7__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group__7__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__7"


    // $ANTLR start "rule__Create_Statement__Group__7__Impl"
    // InternalCQL.g:2911:1: rule__Create_Statement__Group__7__Impl : ( ')' ) ;
    public final void rule__Create_Statement__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2915:1: ( ( ')' ) )
            // InternalCQL.g:2916:1: ( ')' )
            {
            // InternalCQL.g:2916:1: ( ')' )
            // InternalCQL.g:2917:2: ')'
            {
             before(grammarAccess.getCreate_StatementAccess().getRightParenthesisKeyword_7()); 
            match(input,36,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getRightParenthesisKeyword_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group__7__Impl"


    // $ANTLR start "rule__Create_Statement__Group_6__0"
    // InternalCQL.g:2927:1: rule__Create_Statement__Group_6__0 : rule__Create_Statement__Group_6__0__Impl rule__Create_Statement__Group_6__1 ;
    public final void rule__Create_Statement__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2931:1: ( rule__Create_Statement__Group_6__0__Impl rule__Create_Statement__Group_6__1 )
            // InternalCQL.g:2932:2: rule__Create_Statement__Group_6__0__Impl rule__Create_Statement__Group_6__1
            {
            pushFollow(FOLLOW_29);
            rule__Create_Statement__Group_6__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group_6__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group_6__0"


    // $ANTLR start "rule__Create_Statement__Group_6__0__Impl"
    // InternalCQL.g:2939:1: rule__Create_Statement__Group_6__0__Impl : ( ',' ) ;
    public final void rule__Create_Statement__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2943:1: ( ( ',' ) )
            // InternalCQL.g:2944:1: ( ',' )
            {
            // InternalCQL.g:2944:1: ( ',' )
            // InternalCQL.g:2945:2: ','
            {
             before(grammarAccess.getCreate_StatementAccess().getCommaKeyword_6_0()); 
            match(input,40,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getCommaKeyword_6_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group_6__0__Impl"


    // $ANTLR start "rule__Create_Statement__Group_6__1"
    // InternalCQL.g:2954:1: rule__Create_Statement__Group_6__1 : rule__Create_Statement__Group_6__1__Impl rule__Create_Statement__Group_6__2 ;
    public final void rule__Create_Statement__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2958:1: ( rule__Create_Statement__Group_6__1__Impl rule__Create_Statement__Group_6__2 )
            // InternalCQL.g:2959:2: rule__Create_Statement__Group_6__1__Impl rule__Create_Statement__Group_6__2
            {
            pushFollow(FOLLOW_36);
            rule__Create_Statement__Group_6__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group_6__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group_6__1"


    // $ANTLR start "rule__Create_Statement__Group_6__1__Impl"
    // InternalCQL.g:2966:1: rule__Create_Statement__Group_6__1__Impl : ( ( rule__Create_Statement__AttributesAssignment_6_1 ) ) ;
    public final void rule__Create_Statement__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2970:1: ( ( ( rule__Create_Statement__AttributesAssignment_6_1 ) ) )
            // InternalCQL.g:2971:1: ( ( rule__Create_Statement__AttributesAssignment_6_1 ) )
            {
            // InternalCQL.g:2971:1: ( ( rule__Create_Statement__AttributesAssignment_6_1 ) )
            // InternalCQL.g:2972:2: ( rule__Create_Statement__AttributesAssignment_6_1 )
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_6_1()); 
            // InternalCQL.g:2973:2: ( rule__Create_Statement__AttributesAssignment_6_1 )
            // InternalCQL.g:2973:3: rule__Create_Statement__AttributesAssignment_6_1
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__AttributesAssignment_6_1();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_6_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group_6__1__Impl"


    // $ANTLR start "rule__Create_Statement__Group_6__2"
    // InternalCQL.g:2981:1: rule__Create_Statement__Group_6__2 : rule__Create_Statement__Group_6__2__Impl ;
    public final void rule__Create_Statement__Group_6__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2985:1: ( rule__Create_Statement__Group_6__2__Impl )
            // InternalCQL.g:2986:2: rule__Create_Statement__Group_6__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__Group_6__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group_6__2"


    // $ANTLR start "rule__Create_Statement__Group_6__2__Impl"
    // InternalCQL.g:2992:1: rule__Create_Statement__Group_6__2__Impl : ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) ) ;
    public final void rule__Create_Statement__Group_6__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2996:1: ( ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) ) )
            // InternalCQL.g:2997:1: ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) )
            {
            // InternalCQL.g:2997:1: ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) )
            // InternalCQL.g:2998:2: ( rule__Create_Statement__DatatypesAssignment_6_2 )
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_6_2()); 
            // InternalCQL.g:2999:2: ( rule__Create_Statement__DatatypesAssignment_6_2 )
            // InternalCQL.g:2999:3: rule__Create_Statement__DatatypesAssignment_6_2
            {
            pushFollow(FOLLOW_2);
            rule__Create_Statement__DatatypesAssignment_6_2();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_6_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__Group_6__2__Impl"


    // $ANTLR start "rule__Model__StatementsAssignment"
    // InternalCQL.g:3008:1: rule__Model__StatementsAssignment : ( ruleStatement ) ;
    public final void rule__Model__StatementsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3012:1: ( ( ruleStatement ) )
            // InternalCQL.g:3013:2: ( ruleStatement )
            {
            // InternalCQL.g:3013:2: ( ruleStatement )
            // InternalCQL.g:3014:3: ruleStatement
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
    // InternalCQL.g:3023:1: rule__Statement__TypeAssignment_0_0 : ( ruleSelect_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3027:1: ( ( ruleSelect_Statement ) )
            // InternalCQL.g:3028:2: ( ruleSelect_Statement )
            {
            // InternalCQL.g:3028:2: ( ruleSelect_Statement )
            // InternalCQL.g:3029:3: ruleSelect_Statement
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
    // InternalCQL.g:3038:1: rule__Statement__TypeAssignment_0_1 : ( ruleCreate_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3042:1: ( ( ruleCreate_Statement ) )
            // InternalCQL.g:3043:2: ( ruleCreate_Statement )
            {
            // InternalCQL.g:3043:2: ( ruleCreate_Statement )
            // InternalCQL.g:3044:3: ruleCreate_Statement
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
    // InternalCQL.g:3053:1: rule__Atomic__ValueAssignment_0_1 : ( RULE_INT ) ;
    public final void rule__Atomic__ValueAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3057:1: ( ( RULE_INT ) )
            // InternalCQL.g:3058:2: ( RULE_INT )
            {
            // InternalCQL.g:3058:2: ( RULE_INT )
            // InternalCQL.g:3059:3: RULE_INT
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
    // InternalCQL.g:3068:1: rule__Atomic__ValueAssignment_1_1 : ( RULE_FLOAT_NUMBER ) ;
    public final void rule__Atomic__ValueAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3072:1: ( ( RULE_FLOAT_NUMBER ) )
            // InternalCQL.g:3073:2: ( RULE_FLOAT_NUMBER )
            {
            // InternalCQL.g:3073:2: ( RULE_FLOAT_NUMBER )
            // InternalCQL.g:3074:3: RULE_FLOAT_NUMBER
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
    // InternalCQL.g:3083:1: rule__Atomic__ValueAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Atomic__ValueAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3087:1: ( ( RULE_STRING ) )
            // InternalCQL.g:3088:2: ( RULE_STRING )
            {
            // InternalCQL.g:3088:2: ( RULE_STRING )
            // InternalCQL.g:3089:3: RULE_STRING
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
    // InternalCQL.g:3098:1: rule__Atomic__ValueAssignment_3_1 : ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) ;
    public final void rule__Atomic__ValueAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3102:1: ( ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) )
            // InternalCQL.g:3103:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            {
            // InternalCQL.g:3103:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            // InternalCQL.g:3104:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            {
             before(grammarAccess.getAtomicAccess().getValueAlternatives_3_1_0()); 
            // InternalCQL.g:3105:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            // InternalCQL.g:3105:4: rule__Atomic__ValueAlternatives_3_1_0
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
    // InternalCQL.g:3113:1: rule__Atomic__ValueAssignment_4_1 : ( ( RULE_ID ) ) ;
    public final void rule__Atomic__ValueAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3117:1: ( ( ( RULE_ID ) ) )
            // InternalCQL.g:3118:2: ( ( RULE_ID ) )
            {
            // InternalCQL.g:3118:2: ( ( RULE_ID ) )
            // InternalCQL.g:3119:3: ( RULE_ID )
            {
             before(grammarAccess.getAtomicAccess().getValueAttributeCrossReference_4_1_0()); 
            // InternalCQL.g:3120:3: ( RULE_ID )
            // InternalCQL.g:3121:4: RULE_ID
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
    // InternalCQL.g:3132:1: rule__Source__NameAssignment : ( RULE_ID ) ;
    public final void rule__Source__NameAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3136:1: ( ( RULE_ID ) )
            // InternalCQL.g:3137:2: ( RULE_ID )
            {
            // InternalCQL.g:3137:2: ( RULE_ID )
            // InternalCQL.g:3138:3: RULE_ID
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
    // InternalCQL.g:3147:1: rule__Attribute__SourceAssignment_0_0 : ( ( RULE_ID ) ) ;
    public final void rule__Attribute__SourceAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3151:1: ( ( ( RULE_ID ) ) )
            // InternalCQL.g:3152:2: ( ( RULE_ID ) )
            {
            // InternalCQL.g:3152:2: ( ( RULE_ID ) )
            // InternalCQL.g:3153:3: ( RULE_ID )
            {
             before(grammarAccess.getAttributeAccess().getSourceSourceCrossReference_0_0_0()); 
            // InternalCQL.g:3154:3: ( RULE_ID )
            // InternalCQL.g:3155:4: RULE_ID
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
    // InternalCQL.g:3166:1: rule__Attribute__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__Attribute__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3170:1: ( ( RULE_ID ) )
            // InternalCQL.g:3171:2: ( RULE_ID )
            {
            // InternalCQL.g:3171:2: ( RULE_ID )
            // InternalCQL.g:3172:3: RULE_ID
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
    // InternalCQL.g:3181:1: rule__ExpressionsModel__ElementsAssignment_1 : ( ruleExpression ) ;
    public final void rule__ExpressionsModel__ElementsAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3185:1: ( ( ruleExpression ) )
            // InternalCQL.g:3186:2: ( ruleExpression )
            {
            // InternalCQL.g:3186:2: ( ruleExpression )
            // InternalCQL.g:3187:3: ruleExpression
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
    // InternalCQL.g:3196:1: rule__Or__RightAssignment_1_2 : ( ruleAnd ) ;
    public final void rule__Or__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3200:1: ( ( ruleAnd ) )
            // InternalCQL.g:3201:2: ( ruleAnd )
            {
            // InternalCQL.g:3201:2: ( ruleAnd )
            // InternalCQL.g:3202:3: ruleAnd
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
    // InternalCQL.g:3211:1: rule__And__RightAssignment_1_2 : ( ruleEqualitiy ) ;
    public final void rule__And__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3215:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:3216:2: ( ruleEqualitiy )
            {
            // InternalCQL.g:3216:2: ( ruleEqualitiy )
            // InternalCQL.g:3217:3: ruleEqualitiy
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
    // InternalCQL.g:3226:1: rule__Equalitiy__OpAssignment_1_1 : ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Equalitiy__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3230:1: ( ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:3231:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:3231:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:3232:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:3233:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            // InternalCQL.g:3233:4: rule__Equalitiy__OpAlternatives_1_1_0
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
    // InternalCQL.g:3241:1: rule__Equalitiy__RightAssignment_1_2 : ( ruleComparison ) ;
    public final void rule__Equalitiy__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3245:1: ( ( ruleComparison ) )
            // InternalCQL.g:3246:2: ( ruleComparison )
            {
            // InternalCQL.g:3246:2: ( ruleComparison )
            // InternalCQL.g:3247:3: ruleComparison
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
    // InternalCQL.g:3256:1: rule__Comparison__OpAssignment_1_1 : ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Comparison__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3260:1: ( ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:3261:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:3261:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:3262:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getComparisonAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:3263:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            // InternalCQL.g:3263:4: rule__Comparison__OpAlternatives_1_1_0
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
    // InternalCQL.g:3271:1: rule__Comparison__RightAssignment_1_2 : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3275:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:3276:2: ( rulePlusOrMinus )
            {
            // InternalCQL.g:3276:2: ( rulePlusOrMinus )
            // InternalCQL.g:3277:3: rulePlusOrMinus
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
    // InternalCQL.g:3286:1: rule__PlusOrMinus__RightAssignment_1_1 : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__RightAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3290:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:3291:2: ( ruleMulOrDiv )
            {
            // InternalCQL.g:3291:2: ( ruleMulOrDiv )
            // InternalCQL.g:3292:3: ruleMulOrDiv
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
    // InternalCQL.g:3301:1: rule__MulOrDiv__OpAssignment_1_1 : ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) ;
    public final void rule__MulOrDiv__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3305:1: ( ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:3306:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:3306:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:3307:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:3308:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            // InternalCQL.g:3308:4: rule__MulOrDiv__OpAlternatives_1_1_0
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
    // InternalCQL.g:3316:1: rule__MulOrDiv__RightAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__MulOrDiv__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3320:1: ( ( rulePrimary ) )
            // InternalCQL.g:3321:2: ( rulePrimary )
            {
            // InternalCQL.g:3321:2: ( rulePrimary )
            // InternalCQL.g:3322:3: rulePrimary
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
    // InternalCQL.g:3331:1: rule__Primary__InnerAssignment_0_2 : ( ruleExpression ) ;
    public final void rule__Primary__InnerAssignment_0_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3335:1: ( ( ruleExpression ) )
            // InternalCQL.g:3336:2: ( ruleExpression )
            {
            // InternalCQL.g:3336:2: ( ruleExpression )
            // InternalCQL.g:3337:3: ruleExpression
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
    // InternalCQL.g:3346:1: rule__Primary__ExpressionAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__Primary__ExpressionAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3350:1: ( ( rulePrimary ) )
            // InternalCQL.g:3351:2: ( rulePrimary )
            {
            // InternalCQL.g:3351:2: ( rulePrimary )
            // InternalCQL.g:3352:3: rulePrimary
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
    // InternalCQL.g:3361:1: rule__Select_Statement__NameAssignment_0 : ( ( 'SELECT' ) ) ;
    public final void rule__Select_Statement__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3365:1: ( ( ( 'SELECT' ) ) )
            // InternalCQL.g:3366:2: ( ( 'SELECT' ) )
            {
            // InternalCQL.g:3366:2: ( ( 'SELECT' ) )
            // InternalCQL.g:3367:3: ( 'SELECT' )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            // InternalCQL.g:3368:3: ( 'SELECT' )
            // InternalCQL.g:3369:4: 'SELECT'
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            match(input,44,FOLLOW_2); 
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
    // InternalCQL.g:3380:1: rule__Select_Statement__AttributesAssignment_2 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3384:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3385:2: ( ruleAttribute )
            {
            // InternalCQL.g:3385:2: ( ruleAttribute )
            // InternalCQL.g:3386:3: ruleAttribute
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
    // InternalCQL.g:3395:1: rule__Select_Statement__AttributesAssignment_3_1 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3399:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3400:2: ( ruleAttribute )
            {
            // InternalCQL.g:3400:2: ( ruleAttribute )
            // InternalCQL.g:3401:3: ruleAttribute
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
    // InternalCQL.g:3410:1: rule__Select_Statement__SourcesAssignment_5 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3414:1: ( ( ruleSource ) )
            // InternalCQL.g:3415:2: ( ruleSource )
            {
            // InternalCQL.g:3415:2: ( ruleSource )
            // InternalCQL.g:3416:3: ruleSource
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
    // InternalCQL.g:3425:1: rule__Select_Statement__SourcesAssignment_6_1 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3429:1: ( ( ruleSource ) )
            // InternalCQL.g:3430:2: ( ruleSource )
            {
            // InternalCQL.g:3430:2: ( ruleSource )
            // InternalCQL.g:3431:3: ruleSource
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
    // InternalCQL.g:3440:1: rule__Select_Statement__PredicatesAssignment_7_1 : ( ruleExpressionsModel ) ;
    public final void rule__Select_Statement__PredicatesAssignment_7_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3444:1: ( ( ruleExpressionsModel ) )
            // InternalCQL.g:3445:2: ( ruleExpressionsModel )
            {
            // InternalCQL.g:3445:2: ( ruleExpressionsModel )
            // InternalCQL.g:3446:3: ruleExpressionsModel
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


    // $ANTLR start "rule__Create_Statement__NameAssignment_2"
    // InternalCQL.g:3455:1: rule__Create_Statement__NameAssignment_2 : ( RULE_ID ) ;
    public final void rule__Create_Statement__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3459:1: ( ( RULE_ID ) )
            // InternalCQL.g:3460:2: ( RULE_ID )
            {
            // InternalCQL.g:3460:2: ( RULE_ID )
            // InternalCQL.g:3461:3: RULE_ID
            {
             before(grammarAccess.getCreate_StatementAccess().getNameIDTerminalRuleCall_2_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getCreate_StatementAccess().getNameIDTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__NameAssignment_2"


    // $ANTLR start "rule__Create_Statement__AttributesAssignment_4"
    // InternalCQL.g:3470:1: rule__Create_Statement__AttributesAssignment_4 : ( ruleAttribute ) ;
    public final void rule__Create_Statement__AttributesAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3474:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3475:2: ( ruleAttribute )
            {
            // InternalCQL.g:3475:2: ( ruleAttribute )
            // InternalCQL.g:3476:3: ruleAttribute
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAttributeParserRuleCall_4_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getCreate_StatementAccess().getAttributesAttributeParserRuleCall_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__AttributesAssignment_4"


    // $ANTLR start "rule__Create_Statement__DatatypesAssignment_5"
    // InternalCQL.g:3485:1: rule__Create_Statement__DatatypesAssignment_5 : ( ruleDataType ) ;
    public final void rule__Create_Statement__DatatypesAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3489:1: ( ( ruleDataType ) )
            // InternalCQL.g:3490:2: ( ruleDataType )
            {
            // InternalCQL.g:3490:2: ( ruleDataType )
            // InternalCQL.g:3491:3: ruleDataType
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesDataTypeParserRuleCall_5_0()); 
            pushFollow(FOLLOW_2);
            ruleDataType();

            state._fsp--;

             after(grammarAccess.getCreate_StatementAccess().getDatatypesDataTypeParserRuleCall_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__DatatypesAssignment_5"


    // $ANTLR start "rule__Create_Statement__AttributesAssignment_6_1"
    // InternalCQL.g:3500:1: rule__Create_Statement__AttributesAssignment_6_1 : ( ruleAttribute ) ;
    public final void rule__Create_Statement__AttributesAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3504:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3505:2: ( ruleAttribute )
            {
            // InternalCQL.g:3505:2: ( ruleAttribute )
            // InternalCQL.g:3506:3: ruleAttribute
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAttributeParserRuleCall_6_1_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getCreate_StatementAccess().getAttributesAttributeParserRuleCall_6_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__AttributesAssignment_6_1"


    // $ANTLR start "rule__Create_Statement__DatatypesAssignment_6_2"
    // InternalCQL.g:3515:1: rule__Create_Statement__DatatypesAssignment_6_2 : ( ruleDataType ) ;
    public final void rule__Create_Statement__DatatypesAssignment_6_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3519:1: ( ( ruleDataType ) )
            // InternalCQL.g:3520:2: ( ruleDataType )
            {
            // InternalCQL.g:3520:2: ( ruleDataType )
            // InternalCQL.g:3521:3: ruleDataType
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesDataTypeParserRuleCall_6_2_0()); 
            pushFollow(FOLLOW_2);
            ruleDataType();

            state._fsp--;

             after(grammarAccess.getCreate_StatementAccess().getDatatypesDataTypeParserRuleCall_6_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Create_Statement__DatatypesAssignment_6_2"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000140000000002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000003000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x00000028000030F0L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x00000028000030F2L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000600000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000007800000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000007800002L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000600000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000600000002L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000018000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000018000002L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000004000000080L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000018000000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000004000000082L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000010000000002L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000030000000000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x00000000001FC000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000011000000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x00000000001FC002L});

}