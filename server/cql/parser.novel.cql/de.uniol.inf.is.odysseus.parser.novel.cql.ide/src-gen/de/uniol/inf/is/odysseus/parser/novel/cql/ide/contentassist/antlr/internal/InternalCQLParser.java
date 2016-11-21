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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_INT", "RULE_FLOAT_NUMBER", "RULE_STRING", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'TRUE'", "'FALSE'", "'INTEGER'", "'DOUBLE'", "'FLOAT'", "'STRING'", "'BOOLEAN'", "'STARTTIMESTAMP'", "'ENDTIMESTAMP'", "'=='", "'!='", "'>='", "'<='", "'<'", "'>'", "'*'", "'/'", "';'", "'OR'", "'AND'", "'+'", "'-'", "'('", "')'", "'NOT'", "'DISTINCT'", "'FROM'", "','", "'WHERE'", "'CREATE'", "'STREAM'", "'SELECT'"
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

                if ( (LA1_0==41||LA1_0==43) ) {
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
    // InternalCQL.g:187:1: ruleAttribute : ( ( rule__Attribute__NameAssignment ) ) ;
    public final void ruleAttribute() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:191:2: ( ( ( rule__Attribute__NameAssignment ) ) )
            // InternalCQL.g:192:2: ( ( rule__Attribute__NameAssignment ) )
            {
            // InternalCQL.g:192:2: ( ( rule__Attribute__NameAssignment ) )
            // InternalCQL.g:193:3: ( rule__Attribute__NameAssignment )
            {
             before(grammarAccess.getAttributeAccess().getNameAssignment()); 
            // InternalCQL.g:194:3: ( rule__Attribute__NameAssignment )
            // InternalCQL.g:194:4: rule__Attribute__NameAssignment
            {
            pushFollow(FOLLOW_2);
            rule__Attribute__NameAssignment();

            state._fsp--;


            }

             after(grammarAccess.getAttributeAccess().getNameAssignment()); 

            }


            }

        }
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

            if ( (LA2_0==43) ) {
                alt2=1;
            }
            else if ( (LA2_0==41) ) {
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

            if ( (LA8_0==32) ) {
                alt8=1;
            }
            else if ( (LA8_0==33) ) {
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
            case 34:
                {
                alt10=1;
                }
                break;
            case 36:
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


    // $ANTLR start "rule__Select_Statement__Alternatives_2"
    // InternalCQL.g:732:1: rule__Select_Statement__Alternatives_2 : ( ( '*' ) | ( ( rule__Select_Statement__Group_2_1__0 ) ) );
    public final void rule__Select_Statement__Alternatives_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:736:1: ( ( '*' ) | ( ( rule__Select_Statement__Group_2_1__0 ) ) )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==27) ) {
                alt11=1;
            }
            else if ( (LA11_0==RULE_ID) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // InternalCQL.g:737:2: ( '*' )
                    {
                    // InternalCQL.g:737:2: ( '*' )
                    // InternalCQL.g:738:3: '*'
                    {
                     before(grammarAccess.getSelect_StatementAccess().getAsteriskKeyword_2_0()); 
                    match(input,27,FOLLOW_2); 
                     after(grammarAccess.getSelect_StatementAccess().getAsteriskKeyword_2_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalCQL.g:743:2: ( ( rule__Select_Statement__Group_2_1__0 ) )
                    {
                    // InternalCQL.g:743:2: ( ( rule__Select_Statement__Group_2_1__0 ) )
                    // InternalCQL.g:744:3: ( rule__Select_Statement__Group_2_1__0 )
                    {
                     before(grammarAccess.getSelect_StatementAccess().getGroup_2_1()); 
                    // InternalCQL.g:745:3: ( rule__Select_Statement__Group_2_1__0 )
                    // InternalCQL.g:745:4: rule__Select_Statement__Group_2_1__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Select_Statement__Group_2_1__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getSelect_StatementAccess().getGroup_2_1()); 

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
    // $ANTLR end "rule__Select_Statement__Alternatives_2"


    // $ANTLR start "rule__Statement__Group__0"
    // InternalCQL.g:753:1: rule__Statement__Group__0 : rule__Statement__Group__0__Impl rule__Statement__Group__1 ;
    public final void rule__Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:757:1: ( rule__Statement__Group__0__Impl rule__Statement__Group__1 )
            // InternalCQL.g:758:2: rule__Statement__Group__0__Impl rule__Statement__Group__1
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
    // InternalCQL.g:765:1: rule__Statement__Group__0__Impl : ( ( rule__Statement__Alternatives_0 ) ) ;
    public final void rule__Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:769:1: ( ( ( rule__Statement__Alternatives_0 ) ) )
            // InternalCQL.g:770:1: ( ( rule__Statement__Alternatives_0 ) )
            {
            // InternalCQL.g:770:1: ( ( rule__Statement__Alternatives_0 ) )
            // InternalCQL.g:771:2: ( rule__Statement__Alternatives_0 )
            {
             before(grammarAccess.getStatementAccess().getAlternatives_0()); 
            // InternalCQL.g:772:2: ( rule__Statement__Alternatives_0 )
            // InternalCQL.g:772:3: rule__Statement__Alternatives_0
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
    // InternalCQL.g:780:1: rule__Statement__Group__1 : rule__Statement__Group__1__Impl ;
    public final void rule__Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:784:1: ( rule__Statement__Group__1__Impl )
            // InternalCQL.g:785:2: rule__Statement__Group__1__Impl
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
    // InternalCQL.g:791:1: rule__Statement__Group__1__Impl : ( ( ';' )? ) ;
    public final void rule__Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:795:1: ( ( ( ';' )? ) )
            // InternalCQL.g:796:1: ( ( ';' )? )
            {
            // InternalCQL.g:796:1: ( ( ';' )? )
            // InternalCQL.g:797:2: ( ';' )?
            {
             before(grammarAccess.getStatementAccess().getSemicolonKeyword_1()); 
            // InternalCQL.g:798:2: ( ';' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==29) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalCQL.g:798:3: ';'
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
    // InternalCQL.g:807:1: rule__Atomic__Group_0__0 : rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 ;
    public final void rule__Atomic__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:811:1: ( rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1 )
            // InternalCQL.g:812:2: rule__Atomic__Group_0__0__Impl rule__Atomic__Group_0__1
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
    // InternalCQL.g:819:1: rule__Atomic__Group_0__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:823:1: ( ( () ) )
            // InternalCQL.g:824:1: ( () )
            {
            // InternalCQL.g:824:1: ( () )
            // InternalCQL.g:825:2: ()
            {
             before(grammarAccess.getAtomicAccess().getIntConstantAction_0_0()); 
            // InternalCQL.g:826:2: ()
            // InternalCQL.g:826:3: 
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
    // InternalCQL.g:834:1: rule__Atomic__Group_0__1 : rule__Atomic__Group_0__1__Impl ;
    public final void rule__Atomic__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:838:1: ( rule__Atomic__Group_0__1__Impl )
            // InternalCQL.g:839:2: rule__Atomic__Group_0__1__Impl
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
    // InternalCQL.g:845:1: rule__Atomic__Group_0__1__Impl : ( ( rule__Atomic__ValueAssignment_0_1 ) ) ;
    public final void rule__Atomic__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:849:1: ( ( ( rule__Atomic__ValueAssignment_0_1 ) ) )
            // InternalCQL.g:850:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            {
            // InternalCQL.g:850:1: ( ( rule__Atomic__ValueAssignment_0_1 ) )
            // InternalCQL.g:851:2: ( rule__Atomic__ValueAssignment_0_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_0_1()); 
            // InternalCQL.g:852:2: ( rule__Atomic__ValueAssignment_0_1 )
            // InternalCQL.g:852:3: rule__Atomic__ValueAssignment_0_1
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
    // InternalCQL.g:861:1: rule__Atomic__Group_1__0 : rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 ;
    public final void rule__Atomic__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:865:1: ( rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1 )
            // InternalCQL.g:866:2: rule__Atomic__Group_1__0__Impl rule__Atomic__Group_1__1
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
    // InternalCQL.g:873:1: rule__Atomic__Group_1__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:877:1: ( ( () ) )
            // InternalCQL.g:878:1: ( () )
            {
            // InternalCQL.g:878:1: ( () )
            // InternalCQL.g:879:2: ()
            {
             before(grammarAccess.getAtomicAccess().getFloatConstantAction_1_0()); 
            // InternalCQL.g:880:2: ()
            // InternalCQL.g:880:3: 
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
    // InternalCQL.g:888:1: rule__Atomic__Group_1__1 : rule__Atomic__Group_1__1__Impl ;
    public final void rule__Atomic__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:892:1: ( rule__Atomic__Group_1__1__Impl )
            // InternalCQL.g:893:2: rule__Atomic__Group_1__1__Impl
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
    // InternalCQL.g:899:1: rule__Atomic__Group_1__1__Impl : ( ( rule__Atomic__ValueAssignment_1_1 ) ) ;
    public final void rule__Atomic__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:903:1: ( ( ( rule__Atomic__ValueAssignment_1_1 ) ) )
            // InternalCQL.g:904:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            {
            // InternalCQL.g:904:1: ( ( rule__Atomic__ValueAssignment_1_1 ) )
            // InternalCQL.g:905:2: ( rule__Atomic__ValueAssignment_1_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_1_1()); 
            // InternalCQL.g:906:2: ( rule__Atomic__ValueAssignment_1_1 )
            // InternalCQL.g:906:3: rule__Atomic__ValueAssignment_1_1
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
    // InternalCQL.g:915:1: rule__Atomic__Group_2__0 : rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 ;
    public final void rule__Atomic__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:919:1: ( rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1 )
            // InternalCQL.g:920:2: rule__Atomic__Group_2__0__Impl rule__Atomic__Group_2__1
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
    // InternalCQL.g:927:1: rule__Atomic__Group_2__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:931:1: ( ( () ) )
            // InternalCQL.g:932:1: ( () )
            {
            // InternalCQL.g:932:1: ( () )
            // InternalCQL.g:933:2: ()
            {
             before(grammarAccess.getAtomicAccess().getStringConstantAction_2_0()); 
            // InternalCQL.g:934:2: ()
            // InternalCQL.g:934:3: 
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
    // InternalCQL.g:942:1: rule__Atomic__Group_2__1 : rule__Atomic__Group_2__1__Impl ;
    public final void rule__Atomic__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:946:1: ( rule__Atomic__Group_2__1__Impl )
            // InternalCQL.g:947:2: rule__Atomic__Group_2__1__Impl
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
    // InternalCQL.g:953:1: rule__Atomic__Group_2__1__Impl : ( ( rule__Atomic__ValueAssignment_2_1 ) ) ;
    public final void rule__Atomic__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:957:1: ( ( ( rule__Atomic__ValueAssignment_2_1 ) ) )
            // InternalCQL.g:958:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            {
            // InternalCQL.g:958:1: ( ( rule__Atomic__ValueAssignment_2_1 ) )
            // InternalCQL.g:959:2: ( rule__Atomic__ValueAssignment_2_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_2_1()); 
            // InternalCQL.g:960:2: ( rule__Atomic__ValueAssignment_2_1 )
            // InternalCQL.g:960:3: rule__Atomic__ValueAssignment_2_1
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
    // InternalCQL.g:969:1: rule__Atomic__Group_3__0 : rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 ;
    public final void rule__Atomic__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:973:1: ( rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1 )
            // InternalCQL.g:974:2: rule__Atomic__Group_3__0__Impl rule__Atomic__Group_3__1
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
    // InternalCQL.g:981:1: rule__Atomic__Group_3__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:985:1: ( ( () ) )
            // InternalCQL.g:986:1: ( () )
            {
            // InternalCQL.g:986:1: ( () )
            // InternalCQL.g:987:2: ()
            {
             before(grammarAccess.getAtomicAccess().getBoolConstantAction_3_0()); 
            // InternalCQL.g:988:2: ()
            // InternalCQL.g:988:3: 
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
    // InternalCQL.g:996:1: rule__Atomic__Group_3__1 : rule__Atomic__Group_3__1__Impl ;
    public final void rule__Atomic__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1000:1: ( rule__Atomic__Group_3__1__Impl )
            // InternalCQL.g:1001:2: rule__Atomic__Group_3__1__Impl
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
    // InternalCQL.g:1007:1: rule__Atomic__Group_3__1__Impl : ( ( rule__Atomic__ValueAssignment_3_1 ) ) ;
    public final void rule__Atomic__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1011:1: ( ( ( rule__Atomic__ValueAssignment_3_1 ) ) )
            // InternalCQL.g:1012:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            {
            // InternalCQL.g:1012:1: ( ( rule__Atomic__ValueAssignment_3_1 ) )
            // InternalCQL.g:1013:2: ( rule__Atomic__ValueAssignment_3_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_3_1()); 
            // InternalCQL.g:1014:2: ( rule__Atomic__ValueAssignment_3_1 )
            // InternalCQL.g:1014:3: rule__Atomic__ValueAssignment_3_1
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
    // InternalCQL.g:1023:1: rule__Atomic__Group_4__0 : rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 ;
    public final void rule__Atomic__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1027:1: ( rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1 )
            // InternalCQL.g:1028:2: rule__Atomic__Group_4__0__Impl rule__Atomic__Group_4__1
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
    // InternalCQL.g:1035:1: rule__Atomic__Group_4__0__Impl : ( () ) ;
    public final void rule__Atomic__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1039:1: ( ( () ) )
            // InternalCQL.g:1040:1: ( () )
            {
            // InternalCQL.g:1040:1: ( () )
            // InternalCQL.g:1041:2: ()
            {
             before(grammarAccess.getAtomicAccess().getAttributeRefAction_4_0()); 
            // InternalCQL.g:1042:2: ()
            // InternalCQL.g:1042:3: 
            {
            }

             after(grammarAccess.getAtomicAccess().getAttributeRefAction_4_0()); 

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
    // InternalCQL.g:1050:1: rule__Atomic__Group_4__1 : rule__Atomic__Group_4__1__Impl ;
    public final void rule__Atomic__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1054:1: ( rule__Atomic__Group_4__1__Impl )
            // InternalCQL.g:1055:2: rule__Atomic__Group_4__1__Impl
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
    // InternalCQL.g:1061:1: rule__Atomic__Group_4__1__Impl : ( ( rule__Atomic__ValueAssignment_4_1 ) ) ;
    public final void rule__Atomic__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1065:1: ( ( ( rule__Atomic__ValueAssignment_4_1 ) ) )
            // InternalCQL.g:1066:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            {
            // InternalCQL.g:1066:1: ( ( rule__Atomic__ValueAssignment_4_1 ) )
            // InternalCQL.g:1067:2: ( rule__Atomic__ValueAssignment_4_1 )
            {
             before(grammarAccess.getAtomicAccess().getValueAssignment_4_1()); 
            // InternalCQL.g:1068:2: ( rule__Atomic__ValueAssignment_4_1 )
            // InternalCQL.g:1068:3: rule__Atomic__ValueAssignment_4_1
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


    // $ANTLR start "rule__ExpressionsModel__Group__0"
    // InternalCQL.g:1077:1: rule__ExpressionsModel__Group__0 : rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 ;
    public final void rule__ExpressionsModel__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1081:1: ( rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1 )
            // InternalCQL.g:1082:2: rule__ExpressionsModel__Group__0__Impl rule__ExpressionsModel__Group__1
            {
            pushFollow(FOLLOW_10);
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
    // InternalCQL.g:1089:1: rule__ExpressionsModel__Group__0__Impl : ( () ) ;
    public final void rule__ExpressionsModel__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1093:1: ( ( () ) )
            // InternalCQL.g:1094:1: ( () )
            {
            // InternalCQL.g:1094:1: ( () )
            // InternalCQL.g:1095:2: ()
            {
             before(grammarAccess.getExpressionsModelAccess().getExpressionsModelAction_0()); 
            // InternalCQL.g:1096:2: ()
            // InternalCQL.g:1096:3: 
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
    // InternalCQL.g:1104:1: rule__ExpressionsModel__Group__1 : rule__ExpressionsModel__Group__1__Impl ;
    public final void rule__ExpressionsModel__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1108:1: ( rule__ExpressionsModel__Group__1__Impl )
            // InternalCQL.g:1109:2: rule__ExpressionsModel__Group__1__Impl
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
    // InternalCQL.g:1115:1: rule__ExpressionsModel__Group__1__Impl : ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) ;
    public final void rule__ExpressionsModel__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1119:1: ( ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) ) )
            // InternalCQL.g:1120:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            {
            // InternalCQL.g:1120:1: ( ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* ) )
            // InternalCQL.g:1121:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) ) ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            {
            // InternalCQL.g:1121:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 ) )
            // InternalCQL.g:1122:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1123:3: ( rule__ExpressionsModel__ElementsAssignment_1 )
            // InternalCQL.g:1123:4: rule__ExpressionsModel__ElementsAssignment_1
            {
            pushFollow(FOLLOW_11);
            rule__ExpressionsModel__ElementsAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 

            }

            // InternalCQL.g:1126:2: ( ( rule__ExpressionsModel__ElementsAssignment_1 )* )
            // InternalCQL.g:1127:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            {
             before(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1()); 
            // InternalCQL.g:1128:3: ( rule__ExpressionsModel__ElementsAssignment_1 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=RULE_INT && LA13_0<=RULE_ID)||(LA13_0>=12 && LA13_0<=13)||LA13_0==34||LA13_0==36) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalCQL.g:1128:4: rule__ExpressionsModel__ElementsAssignment_1
            	    {
            	    pushFollow(FOLLOW_11);
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
    // InternalCQL.g:1138:1: rule__Or__Group__0 : rule__Or__Group__0__Impl rule__Or__Group__1 ;
    public final void rule__Or__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1142:1: ( rule__Or__Group__0__Impl rule__Or__Group__1 )
            // InternalCQL.g:1143:2: rule__Or__Group__0__Impl rule__Or__Group__1
            {
            pushFollow(FOLLOW_12);
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
    // InternalCQL.g:1150:1: rule__Or__Group__0__Impl : ( ruleAnd ) ;
    public final void rule__Or__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1154:1: ( ( ruleAnd ) )
            // InternalCQL.g:1155:1: ( ruleAnd )
            {
            // InternalCQL.g:1155:1: ( ruleAnd )
            // InternalCQL.g:1156:2: ruleAnd
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
    // InternalCQL.g:1165:1: rule__Or__Group__1 : rule__Or__Group__1__Impl ;
    public final void rule__Or__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1169:1: ( rule__Or__Group__1__Impl )
            // InternalCQL.g:1170:2: rule__Or__Group__1__Impl
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
    // InternalCQL.g:1176:1: rule__Or__Group__1__Impl : ( ( rule__Or__Group_1__0 )* ) ;
    public final void rule__Or__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1180:1: ( ( ( rule__Or__Group_1__0 )* ) )
            // InternalCQL.g:1181:1: ( ( rule__Or__Group_1__0 )* )
            {
            // InternalCQL.g:1181:1: ( ( rule__Or__Group_1__0 )* )
            // InternalCQL.g:1182:2: ( rule__Or__Group_1__0 )*
            {
             before(grammarAccess.getOrAccess().getGroup_1()); 
            // InternalCQL.g:1183:2: ( rule__Or__Group_1__0 )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==30) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalCQL.g:1183:3: rule__Or__Group_1__0
            	    {
            	    pushFollow(FOLLOW_13);
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
    // InternalCQL.g:1192:1: rule__Or__Group_1__0 : rule__Or__Group_1__0__Impl rule__Or__Group_1__1 ;
    public final void rule__Or__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1196:1: ( rule__Or__Group_1__0__Impl rule__Or__Group_1__1 )
            // InternalCQL.g:1197:2: rule__Or__Group_1__0__Impl rule__Or__Group_1__1
            {
            pushFollow(FOLLOW_12);
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
    // InternalCQL.g:1204:1: rule__Or__Group_1__0__Impl : ( () ) ;
    public final void rule__Or__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1208:1: ( ( () ) )
            // InternalCQL.g:1209:1: ( () )
            {
            // InternalCQL.g:1209:1: ( () )
            // InternalCQL.g:1210:2: ()
            {
             before(grammarAccess.getOrAccess().getOrLeftAction_1_0()); 
            // InternalCQL.g:1211:2: ()
            // InternalCQL.g:1211:3: 
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
    // InternalCQL.g:1219:1: rule__Or__Group_1__1 : rule__Or__Group_1__1__Impl rule__Or__Group_1__2 ;
    public final void rule__Or__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1223:1: ( rule__Or__Group_1__1__Impl rule__Or__Group_1__2 )
            // InternalCQL.g:1224:2: rule__Or__Group_1__1__Impl rule__Or__Group_1__2
            {
            pushFollow(FOLLOW_10);
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
    // InternalCQL.g:1231:1: rule__Or__Group_1__1__Impl : ( 'OR' ) ;
    public final void rule__Or__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1235:1: ( ( 'OR' ) )
            // InternalCQL.g:1236:1: ( 'OR' )
            {
            // InternalCQL.g:1236:1: ( 'OR' )
            // InternalCQL.g:1237:2: 'OR'
            {
             before(grammarAccess.getOrAccess().getORKeyword_1_1()); 
            match(input,30,FOLLOW_2); 
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
    // InternalCQL.g:1246:1: rule__Or__Group_1__2 : rule__Or__Group_1__2__Impl ;
    public final void rule__Or__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1250:1: ( rule__Or__Group_1__2__Impl )
            // InternalCQL.g:1251:2: rule__Or__Group_1__2__Impl
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
    // InternalCQL.g:1257:1: rule__Or__Group_1__2__Impl : ( ( rule__Or__RightAssignment_1_2 ) ) ;
    public final void rule__Or__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1261:1: ( ( ( rule__Or__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1262:1: ( ( rule__Or__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1262:1: ( ( rule__Or__RightAssignment_1_2 ) )
            // InternalCQL.g:1263:2: ( rule__Or__RightAssignment_1_2 )
            {
             before(grammarAccess.getOrAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1264:2: ( rule__Or__RightAssignment_1_2 )
            // InternalCQL.g:1264:3: rule__Or__RightAssignment_1_2
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
    // InternalCQL.g:1273:1: rule__And__Group__0 : rule__And__Group__0__Impl rule__And__Group__1 ;
    public final void rule__And__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1277:1: ( rule__And__Group__0__Impl rule__And__Group__1 )
            // InternalCQL.g:1278:2: rule__And__Group__0__Impl rule__And__Group__1
            {
            pushFollow(FOLLOW_14);
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
    // InternalCQL.g:1285:1: rule__And__Group__0__Impl : ( ruleEqualitiy ) ;
    public final void rule__And__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1289:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:1290:1: ( ruleEqualitiy )
            {
            // InternalCQL.g:1290:1: ( ruleEqualitiy )
            // InternalCQL.g:1291:2: ruleEqualitiy
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
    // InternalCQL.g:1300:1: rule__And__Group__1 : rule__And__Group__1__Impl ;
    public final void rule__And__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1304:1: ( rule__And__Group__1__Impl )
            // InternalCQL.g:1305:2: rule__And__Group__1__Impl
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
    // InternalCQL.g:1311:1: rule__And__Group__1__Impl : ( ( rule__And__Group_1__0 )* ) ;
    public final void rule__And__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1315:1: ( ( ( rule__And__Group_1__0 )* ) )
            // InternalCQL.g:1316:1: ( ( rule__And__Group_1__0 )* )
            {
            // InternalCQL.g:1316:1: ( ( rule__And__Group_1__0 )* )
            // InternalCQL.g:1317:2: ( rule__And__Group_1__0 )*
            {
             before(grammarAccess.getAndAccess().getGroup_1()); 
            // InternalCQL.g:1318:2: ( rule__And__Group_1__0 )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==31) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCQL.g:1318:3: rule__And__Group_1__0
            	    {
            	    pushFollow(FOLLOW_15);
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
    // InternalCQL.g:1327:1: rule__And__Group_1__0 : rule__And__Group_1__0__Impl rule__And__Group_1__1 ;
    public final void rule__And__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1331:1: ( rule__And__Group_1__0__Impl rule__And__Group_1__1 )
            // InternalCQL.g:1332:2: rule__And__Group_1__0__Impl rule__And__Group_1__1
            {
            pushFollow(FOLLOW_14);
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
    // InternalCQL.g:1339:1: rule__And__Group_1__0__Impl : ( () ) ;
    public final void rule__And__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1343:1: ( ( () ) )
            // InternalCQL.g:1344:1: ( () )
            {
            // InternalCQL.g:1344:1: ( () )
            // InternalCQL.g:1345:2: ()
            {
             before(grammarAccess.getAndAccess().getAndLeftAction_1_0()); 
            // InternalCQL.g:1346:2: ()
            // InternalCQL.g:1346:3: 
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
    // InternalCQL.g:1354:1: rule__And__Group_1__1 : rule__And__Group_1__1__Impl rule__And__Group_1__2 ;
    public final void rule__And__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1358:1: ( rule__And__Group_1__1__Impl rule__And__Group_1__2 )
            // InternalCQL.g:1359:2: rule__And__Group_1__1__Impl rule__And__Group_1__2
            {
            pushFollow(FOLLOW_10);
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
    // InternalCQL.g:1366:1: rule__And__Group_1__1__Impl : ( 'AND' ) ;
    public final void rule__And__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1370:1: ( ( 'AND' ) )
            // InternalCQL.g:1371:1: ( 'AND' )
            {
            // InternalCQL.g:1371:1: ( 'AND' )
            // InternalCQL.g:1372:2: 'AND'
            {
             before(grammarAccess.getAndAccess().getANDKeyword_1_1()); 
            match(input,31,FOLLOW_2); 
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
    // InternalCQL.g:1381:1: rule__And__Group_1__2 : rule__And__Group_1__2__Impl ;
    public final void rule__And__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1385:1: ( rule__And__Group_1__2__Impl )
            // InternalCQL.g:1386:2: rule__And__Group_1__2__Impl
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
    // InternalCQL.g:1392:1: rule__And__Group_1__2__Impl : ( ( rule__And__RightAssignment_1_2 ) ) ;
    public final void rule__And__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1396:1: ( ( ( rule__And__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1397:1: ( ( rule__And__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1397:1: ( ( rule__And__RightAssignment_1_2 ) )
            // InternalCQL.g:1398:2: ( rule__And__RightAssignment_1_2 )
            {
             before(grammarAccess.getAndAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1399:2: ( rule__And__RightAssignment_1_2 )
            // InternalCQL.g:1399:3: rule__And__RightAssignment_1_2
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
    // InternalCQL.g:1408:1: rule__Equalitiy__Group__0 : rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 ;
    public final void rule__Equalitiy__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1412:1: ( rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1 )
            // InternalCQL.g:1413:2: rule__Equalitiy__Group__0__Impl rule__Equalitiy__Group__1
            {
            pushFollow(FOLLOW_16);
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
    // InternalCQL.g:1420:1: rule__Equalitiy__Group__0__Impl : ( ruleComparison ) ;
    public final void rule__Equalitiy__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1424:1: ( ( ruleComparison ) )
            // InternalCQL.g:1425:1: ( ruleComparison )
            {
            // InternalCQL.g:1425:1: ( ruleComparison )
            // InternalCQL.g:1426:2: ruleComparison
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
    // InternalCQL.g:1435:1: rule__Equalitiy__Group__1 : rule__Equalitiy__Group__1__Impl ;
    public final void rule__Equalitiy__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1439:1: ( rule__Equalitiy__Group__1__Impl )
            // InternalCQL.g:1440:2: rule__Equalitiy__Group__1__Impl
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
    // InternalCQL.g:1446:1: rule__Equalitiy__Group__1__Impl : ( ( rule__Equalitiy__Group_1__0 )* ) ;
    public final void rule__Equalitiy__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1450:1: ( ( ( rule__Equalitiy__Group_1__0 )* ) )
            // InternalCQL.g:1451:1: ( ( rule__Equalitiy__Group_1__0 )* )
            {
            // InternalCQL.g:1451:1: ( ( rule__Equalitiy__Group_1__0 )* )
            // InternalCQL.g:1452:2: ( rule__Equalitiy__Group_1__0 )*
            {
             before(grammarAccess.getEqualitiyAccess().getGroup_1()); 
            // InternalCQL.g:1453:2: ( rule__Equalitiy__Group_1__0 )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( ((LA16_0>=21 && LA16_0<=22)) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalCQL.g:1453:3: rule__Equalitiy__Group_1__0
            	    {
            	    pushFollow(FOLLOW_17);
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
    // InternalCQL.g:1462:1: rule__Equalitiy__Group_1__0 : rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 ;
    public final void rule__Equalitiy__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1466:1: ( rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1 )
            // InternalCQL.g:1467:2: rule__Equalitiy__Group_1__0__Impl rule__Equalitiy__Group_1__1
            {
            pushFollow(FOLLOW_16);
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
    // InternalCQL.g:1474:1: rule__Equalitiy__Group_1__0__Impl : ( () ) ;
    public final void rule__Equalitiy__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1478:1: ( ( () ) )
            // InternalCQL.g:1479:1: ( () )
            {
            // InternalCQL.g:1479:1: ( () )
            // InternalCQL.g:1480:2: ()
            {
             before(grammarAccess.getEqualitiyAccess().getEqualityLeftAction_1_0()); 
            // InternalCQL.g:1481:2: ()
            // InternalCQL.g:1481:3: 
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
    // InternalCQL.g:1489:1: rule__Equalitiy__Group_1__1 : rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 ;
    public final void rule__Equalitiy__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1493:1: ( rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2 )
            // InternalCQL.g:1494:2: rule__Equalitiy__Group_1__1__Impl rule__Equalitiy__Group_1__2
            {
            pushFollow(FOLLOW_10);
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
    // InternalCQL.g:1501:1: rule__Equalitiy__Group_1__1__Impl : ( ( rule__Equalitiy__OpAssignment_1_1 ) ) ;
    public final void rule__Equalitiy__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1505:1: ( ( ( rule__Equalitiy__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1506:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1506:1: ( ( rule__Equalitiy__OpAssignment_1_1 ) )
            // InternalCQL.g:1507:2: ( rule__Equalitiy__OpAssignment_1_1 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1508:2: ( rule__Equalitiy__OpAssignment_1_1 )
            // InternalCQL.g:1508:3: rule__Equalitiy__OpAssignment_1_1
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
    // InternalCQL.g:1516:1: rule__Equalitiy__Group_1__2 : rule__Equalitiy__Group_1__2__Impl ;
    public final void rule__Equalitiy__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1520:1: ( rule__Equalitiy__Group_1__2__Impl )
            // InternalCQL.g:1521:2: rule__Equalitiy__Group_1__2__Impl
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
    // InternalCQL.g:1527:1: rule__Equalitiy__Group_1__2__Impl : ( ( rule__Equalitiy__RightAssignment_1_2 ) ) ;
    public final void rule__Equalitiy__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1531:1: ( ( ( rule__Equalitiy__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1532:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1532:1: ( ( rule__Equalitiy__RightAssignment_1_2 ) )
            // InternalCQL.g:1533:2: ( rule__Equalitiy__RightAssignment_1_2 )
            {
             before(grammarAccess.getEqualitiyAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1534:2: ( rule__Equalitiy__RightAssignment_1_2 )
            // InternalCQL.g:1534:3: rule__Equalitiy__RightAssignment_1_2
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
    // InternalCQL.g:1543:1: rule__Comparison__Group__0 : rule__Comparison__Group__0__Impl rule__Comparison__Group__1 ;
    public final void rule__Comparison__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1547:1: ( rule__Comparison__Group__0__Impl rule__Comparison__Group__1 )
            // InternalCQL.g:1548:2: rule__Comparison__Group__0__Impl rule__Comparison__Group__1
            {
            pushFollow(FOLLOW_18);
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
    // InternalCQL.g:1555:1: rule__Comparison__Group__0__Impl : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1559:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:1560:1: ( rulePlusOrMinus )
            {
            // InternalCQL.g:1560:1: ( rulePlusOrMinus )
            // InternalCQL.g:1561:2: rulePlusOrMinus
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
    // InternalCQL.g:1570:1: rule__Comparison__Group__1 : rule__Comparison__Group__1__Impl ;
    public final void rule__Comparison__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1574:1: ( rule__Comparison__Group__1__Impl )
            // InternalCQL.g:1575:2: rule__Comparison__Group__1__Impl
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
    // InternalCQL.g:1581:1: rule__Comparison__Group__1__Impl : ( ( rule__Comparison__Group_1__0 )* ) ;
    public final void rule__Comparison__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1585:1: ( ( ( rule__Comparison__Group_1__0 )* ) )
            // InternalCQL.g:1586:1: ( ( rule__Comparison__Group_1__0 )* )
            {
            // InternalCQL.g:1586:1: ( ( rule__Comparison__Group_1__0 )* )
            // InternalCQL.g:1587:2: ( rule__Comparison__Group_1__0 )*
            {
             before(grammarAccess.getComparisonAccess().getGroup_1()); 
            // InternalCQL.g:1588:2: ( rule__Comparison__Group_1__0 )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>=23 && LA17_0<=26)) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // InternalCQL.g:1588:3: rule__Comparison__Group_1__0
            	    {
            	    pushFollow(FOLLOW_19);
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
    // InternalCQL.g:1597:1: rule__Comparison__Group_1__0 : rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 ;
    public final void rule__Comparison__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1601:1: ( rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1 )
            // InternalCQL.g:1602:2: rule__Comparison__Group_1__0__Impl rule__Comparison__Group_1__1
            {
            pushFollow(FOLLOW_18);
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
    // InternalCQL.g:1609:1: rule__Comparison__Group_1__0__Impl : ( () ) ;
    public final void rule__Comparison__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1613:1: ( ( () ) )
            // InternalCQL.g:1614:1: ( () )
            {
            // InternalCQL.g:1614:1: ( () )
            // InternalCQL.g:1615:2: ()
            {
             before(grammarAccess.getComparisonAccess().getComparisionLeftAction_1_0()); 
            // InternalCQL.g:1616:2: ()
            // InternalCQL.g:1616:3: 
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
    // InternalCQL.g:1624:1: rule__Comparison__Group_1__1 : rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 ;
    public final void rule__Comparison__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1628:1: ( rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2 )
            // InternalCQL.g:1629:2: rule__Comparison__Group_1__1__Impl rule__Comparison__Group_1__2
            {
            pushFollow(FOLLOW_10);
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
    // InternalCQL.g:1636:1: rule__Comparison__Group_1__1__Impl : ( ( rule__Comparison__OpAssignment_1_1 ) ) ;
    public final void rule__Comparison__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1640:1: ( ( ( rule__Comparison__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1641:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1641:1: ( ( rule__Comparison__OpAssignment_1_1 ) )
            // InternalCQL.g:1642:2: ( rule__Comparison__OpAssignment_1_1 )
            {
             before(grammarAccess.getComparisonAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1643:2: ( rule__Comparison__OpAssignment_1_1 )
            // InternalCQL.g:1643:3: rule__Comparison__OpAssignment_1_1
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
    // InternalCQL.g:1651:1: rule__Comparison__Group_1__2 : rule__Comparison__Group_1__2__Impl ;
    public final void rule__Comparison__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1655:1: ( rule__Comparison__Group_1__2__Impl )
            // InternalCQL.g:1656:2: rule__Comparison__Group_1__2__Impl
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
    // InternalCQL.g:1662:1: rule__Comparison__Group_1__2__Impl : ( ( rule__Comparison__RightAssignment_1_2 ) ) ;
    public final void rule__Comparison__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1666:1: ( ( ( rule__Comparison__RightAssignment_1_2 ) ) )
            // InternalCQL.g:1667:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:1667:1: ( ( rule__Comparison__RightAssignment_1_2 ) )
            // InternalCQL.g:1668:2: ( rule__Comparison__RightAssignment_1_2 )
            {
             before(grammarAccess.getComparisonAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:1669:2: ( rule__Comparison__RightAssignment_1_2 )
            // InternalCQL.g:1669:3: rule__Comparison__RightAssignment_1_2
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
    // InternalCQL.g:1678:1: rule__PlusOrMinus__Group__0 : rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 ;
    public final void rule__PlusOrMinus__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1682:1: ( rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1 )
            // InternalCQL.g:1683:2: rule__PlusOrMinus__Group__0__Impl rule__PlusOrMinus__Group__1
            {
            pushFollow(FOLLOW_20);
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
    // InternalCQL.g:1690:1: rule__PlusOrMinus__Group__0__Impl : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1694:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:1695:1: ( ruleMulOrDiv )
            {
            // InternalCQL.g:1695:1: ( ruleMulOrDiv )
            // InternalCQL.g:1696:2: ruleMulOrDiv
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
    // InternalCQL.g:1705:1: rule__PlusOrMinus__Group__1 : rule__PlusOrMinus__Group__1__Impl ;
    public final void rule__PlusOrMinus__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1709:1: ( rule__PlusOrMinus__Group__1__Impl )
            // InternalCQL.g:1710:2: rule__PlusOrMinus__Group__1__Impl
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
    // InternalCQL.g:1716:1: rule__PlusOrMinus__Group__1__Impl : ( ( rule__PlusOrMinus__Group_1__0 )* ) ;
    public final void rule__PlusOrMinus__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1720:1: ( ( ( rule__PlusOrMinus__Group_1__0 )* ) )
            // InternalCQL.g:1721:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            {
            // InternalCQL.g:1721:1: ( ( rule__PlusOrMinus__Group_1__0 )* )
            // InternalCQL.g:1722:2: ( rule__PlusOrMinus__Group_1__0 )*
            {
             before(grammarAccess.getPlusOrMinusAccess().getGroup_1()); 
            // InternalCQL.g:1723:2: ( rule__PlusOrMinus__Group_1__0 )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>=32 && LA18_0<=33)) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // InternalCQL.g:1723:3: rule__PlusOrMinus__Group_1__0
            	    {
            	    pushFollow(FOLLOW_21);
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
    // InternalCQL.g:1732:1: rule__PlusOrMinus__Group_1__0 : rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 ;
    public final void rule__PlusOrMinus__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1736:1: ( rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1 )
            // InternalCQL.g:1737:2: rule__PlusOrMinus__Group_1__0__Impl rule__PlusOrMinus__Group_1__1
            {
            pushFollow(FOLLOW_10);
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
    // InternalCQL.g:1744:1: rule__PlusOrMinus__Group_1__0__Impl : ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) ;
    public final void rule__PlusOrMinus__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1748:1: ( ( ( rule__PlusOrMinus__Alternatives_1_0 ) ) )
            // InternalCQL.g:1749:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            {
            // InternalCQL.g:1749:1: ( ( rule__PlusOrMinus__Alternatives_1_0 ) )
            // InternalCQL.g:1750:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getAlternatives_1_0()); 
            // InternalCQL.g:1751:2: ( rule__PlusOrMinus__Alternatives_1_0 )
            // InternalCQL.g:1751:3: rule__PlusOrMinus__Alternatives_1_0
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
    // InternalCQL.g:1759:1: rule__PlusOrMinus__Group_1__1 : rule__PlusOrMinus__Group_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1763:1: ( rule__PlusOrMinus__Group_1__1__Impl )
            // InternalCQL.g:1764:2: rule__PlusOrMinus__Group_1__1__Impl
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
    // InternalCQL.g:1770:1: rule__PlusOrMinus__Group_1__1__Impl : ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) ;
    public final void rule__PlusOrMinus__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1774:1: ( ( ( rule__PlusOrMinus__RightAssignment_1_1 ) ) )
            // InternalCQL.g:1775:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            {
            // InternalCQL.g:1775:1: ( ( rule__PlusOrMinus__RightAssignment_1_1 ) )
            // InternalCQL.g:1776:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            {
             before(grammarAccess.getPlusOrMinusAccess().getRightAssignment_1_1()); 
            // InternalCQL.g:1777:2: ( rule__PlusOrMinus__RightAssignment_1_1 )
            // InternalCQL.g:1777:3: rule__PlusOrMinus__RightAssignment_1_1
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
    // InternalCQL.g:1786:1: rule__PlusOrMinus__Group_1_0_0__0 : rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 ;
    public final void rule__PlusOrMinus__Group_1_0_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1790:1: ( rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1 )
            // InternalCQL.g:1791:2: rule__PlusOrMinus__Group_1_0_0__0__Impl rule__PlusOrMinus__Group_1_0_0__1
            {
            pushFollow(FOLLOW_22);
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
    // InternalCQL.g:1798:1: rule__PlusOrMinus__Group_1_0_0__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1802:1: ( ( () ) )
            // InternalCQL.g:1803:1: ( () )
            {
            // InternalCQL.g:1803:1: ( () )
            // InternalCQL.g:1804:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0()); 
            // InternalCQL.g:1805:2: ()
            // InternalCQL.g:1805:3: 
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
    // InternalCQL.g:1813:1: rule__PlusOrMinus__Group_1_0_0__1 : rule__PlusOrMinus__Group_1_0_0__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1817:1: ( rule__PlusOrMinus__Group_1_0_0__1__Impl )
            // InternalCQL.g:1818:2: rule__PlusOrMinus__Group_1_0_0__1__Impl
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
    // InternalCQL.g:1824:1: rule__PlusOrMinus__Group_1_0_0__1__Impl : ( '+' ) ;
    public final void rule__PlusOrMinus__Group_1_0_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1828:1: ( ( '+' ) )
            // InternalCQL.g:1829:1: ( '+' )
            {
            // InternalCQL.g:1829:1: ( '+' )
            // InternalCQL.g:1830:2: '+'
            {
             before(grammarAccess.getPlusOrMinusAccess().getPlusSignKeyword_1_0_0_1()); 
            match(input,32,FOLLOW_2); 
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
    // InternalCQL.g:1840:1: rule__PlusOrMinus__Group_1_0_1__0 : rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 ;
    public final void rule__PlusOrMinus__Group_1_0_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1844:1: ( rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1 )
            // InternalCQL.g:1845:2: rule__PlusOrMinus__Group_1_0_1__0__Impl rule__PlusOrMinus__Group_1_0_1__1
            {
            pushFollow(FOLLOW_20);
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
    // InternalCQL.g:1852:1: rule__PlusOrMinus__Group_1_0_1__0__Impl : ( () ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1856:1: ( ( () ) )
            // InternalCQL.g:1857:1: ( () )
            {
            // InternalCQL.g:1857:1: ( () )
            // InternalCQL.g:1858:2: ()
            {
             before(grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0()); 
            // InternalCQL.g:1859:2: ()
            // InternalCQL.g:1859:3: 
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
    // InternalCQL.g:1867:1: rule__PlusOrMinus__Group_1_0_1__1 : rule__PlusOrMinus__Group_1_0_1__1__Impl ;
    public final void rule__PlusOrMinus__Group_1_0_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1871:1: ( rule__PlusOrMinus__Group_1_0_1__1__Impl )
            // InternalCQL.g:1872:2: rule__PlusOrMinus__Group_1_0_1__1__Impl
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
    // InternalCQL.g:1878:1: rule__PlusOrMinus__Group_1_0_1__1__Impl : ( '-' ) ;
    public final void rule__PlusOrMinus__Group_1_0_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1882:1: ( ( '-' ) )
            // InternalCQL.g:1883:1: ( '-' )
            {
            // InternalCQL.g:1883:1: ( '-' )
            // InternalCQL.g:1884:2: '-'
            {
             before(grammarAccess.getPlusOrMinusAccess().getHyphenMinusKeyword_1_0_1_1()); 
            match(input,33,FOLLOW_2); 
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
    // InternalCQL.g:1894:1: rule__MulOrDiv__Group__0 : rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 ;
    public final void rule__MulOrDiv__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1898:1: ( rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1 )
            // InternalCQL.g:1899:2: rule__MulOrDiv__Group__0__Impl rule__MulOrDiv__Group__1
            {
            pushFollow(FOLLOW_23);
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
    // InternalCQL.g:1906:1: rule__MulOrDiv__Group__0__Impl : ( rulePrimary ) ;
    public final void rule__MulOrDiv__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1910:1: ( ( rulePrimary ) )
            // InternalCQL.g:1911:1: ( rulePrimary )
            {
            // InternalCQL.g:1911:1: ( rulePrimary )
            // InternalCQL.g:1912:2: rulePrimary
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
    // InternalCQL.g:1921:1: rule__MulOrDiv__Group__1 : rule__MulOrDiv__Group__1__Impl ;
    public final void rule__MulOrDiv__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1925:1: ( rule__MulOrDiv__Group__1__Impl )
            // InternalCQL.g:1926:2: rule__MulOrDiv__Group__1__Impl
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
    // InternalCQL.g:1932:1: rule__MulOrDiv__Group__1__Impl : ( ( rule__MulOrDiv__Group_1__0 )* ) ;
    public final void rule__MulOrDiv__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1936:1: ( ( ( rule__MulOrDiv__Group_1__0 )* ) )
            // InternalCQL.g:1937:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            {
            // InternalCQL.g:1937:1: ( ( rule__MulOrDiv__Group_1__0 )* )
            // InternalCQL.g:1938:2: ( rule__MulOrDiv__Group_1__0 )*
            {
             before(grammarAccess.getMulOrDivAccess().getGroup_1()); 
            // InternalCQL.g:1939:2: ( rule__MulOrDiv__Group_1__0 )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>=27 && LA19_0<=28)) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalCQL.g:1939:3: rule__MulOrDiv__Group_1__0
            	    {
            	    pushFollow(FOLLOW_24);
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
    // InternalCQL.g:1948:1: rule__MulOrDiv__Group_1__0 : rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 ;
    public final void rule__MulOrDiv__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1952:1: ( rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1 )
            // InternalCQL.g:1953:2: rule__MulOrDiv__Group_1__0__Impl rule__MulOrDiv__Group_1__1
            {
            pushFollow(FOLLOW_23);
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
    // InternalCQL.g:1960:1: rule__MulOrDiv__Group_1__0__Impl : ( () ) ;
    public final void rule__MulOrDiv__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1964:1: ( ( () ) )
            // InternalCQL.g:1965:1: ( () )
            {
            // InternalCQL.g:1965:1: ( () )
            // InternalCQL.g:1966:2: ()
            {
             before(grammarAccess.getMulOrDivAccess().getMulOrDivLeftAction_1_0()); 
            // InternalCQL.g:1967:2: ()
            // InternalCQL.g:1967:3: 
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
    // InternalCQL.g:1975:1: rule__MulOrDiv__Group_1__1 : rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 ;
    public final void rule__MulOrDiv__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1979:1: ( rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2 )
            // InternalCQL.g:1980:2: rule__MulOrDiv__Group_1__1__Impl rule__MulOrDiv__Group_1__2
            {
            pushFollow(FOLLOW_10);
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
    // InternalCQL.g:1987:1: rule__MulOrDiv__Group_1__1__Impl : ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) ;
    public final void rule__MulOrDiv__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:1991:1: ( ( ( rule__MulOrDiv__OpAssignment_1_1 ) ) )
            // InternalCQL.g:1992:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            {
            // InternalCQL.g:1992:1: ( ( rule__MulOrDiv__OpAssignment_1_1 ) )
            // InternalCQL.g:1993:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAssignment_1_1()); 
            // InternalCQL.g:1994:2: ( rule__MulOrDiv__OpAssignment_1_1 )
            // InternalCQL.g:1994:3: rule__MulOrDiv__OpAssignment_1_1
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
    // InternalCQL.g:2002:1: rule__MulOrDiv__Group_1__2 : rule__MulOrDiv__Group_1__2__Impl ;
    public final void rule__MulOrDiv__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2006:1: ( rule__MulOrDiv__Group_1__2__Impl )
            // InternalCQL.g:2007:2: rule__MulOrDiv__Group_1__2__Impl
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
    // InternalCQL.g:2013:1: rule__MulOrDiv__Group_1__2__Impl : ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) ;
    public final void rule__MulOrDiv__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2017:1: ( ( ( rule__MulOrDiv__RightAssignment_1_2 ) ) )
            // InternalCQL.g:2018:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            {
            // InternalCQL.g:2018:1: ( ( rule__MulOrDiv__RightAssignment_1_2 ) )
            // InternalCQL.g:2019:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            {
             before(grammarAccess.getMulOrDivAccess().getRightAssignment_1_2()); 
            // InternalCQL.g:2020:2: ( rule__MulOrDiv__RightAssignment_1_2 )
            // InternalCQL.g:2020:3: rule__MulOrDiv__RightAssignment_1_2
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
    // InternalCQL.g:2029:1: rule__Primary__Group_0__0 : rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 ;
    public final void rule__Primary__Group_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2033:1: ( rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1 )
            // InternalCQL.g:2034:2: rule__Primary__Group_0__0__Impl rule__Primary__Group_0__1
            {
            pushFollow(FOLLOW_25);
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
    // InternalCQL.g:2041:1: rule__Primary__Group_0__0__Impl : ( () ) ;
    public final void rule__Primary__Group_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2045:1: ( ( () ) )
            // InternalCQL.g:2046:1: ( () )
            {
            // InternalCQL.g:2046:1: ( () )
            // InternalCQL.g:2047:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getBracketAction_0_0()); 
            // InternalCQL.g:2048:2: ()
            // InternalCQL.g:2048:3: 
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
    // InternalCQL.g:2056:1: rule__Primary__Group_0__1 : rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 ;
    public final void rule__Primary__Group_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2060:1: ( rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2 )
            // InternalCQL.g:2061:2: rule__Primary__Group_0__1__Impl rule__Primary__Group_0__2
            {
            pushFollow(FOLLOW_10);
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
    // InternalCQL.g:2068:1: rule__Primary__Group_0__1__Impl : ( '(' ) ;
    public final void rule__Primary__Group_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2072:1: ( ( '(' ) )
            // InternalCQL.g:2073:1: ( '(' )
            {
            // InternalCQL.g:2073:1: ( '(' )
            // InternalCQL.g:2074:2: '('
            {
             before(grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_0_1()); 
            match(input,34,FOLLOW_2); 
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
    // InternalCQL.g:2083:1: rule__Primary__Group_0__2 : rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 ;
    public final void rule__Primary__Group_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2087:1: ( rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3 )
            // InternalCQL.g:2088:2: rule__Primary__Group_0__2__Impl rule__Primary__Group_0__3
            {
            pushFollow(FOLLOW_26);
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
    // InternalCQL.g:2095:1: rule__Primary__Group_0__2__Impl : ( ( rule__Primary__InnerAssignment_0_2 ) ) ;
    public final void rule__Primary__Group_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2099:1: ( ( ( rule__Primary__InnerAssignment_0_2 ) ) )
            // InternalCQL.g:2100:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            {
            // InternalCQL.g:2100:1: ( ( rule__Primary__InnerAssignment_0_2 ) )
            // InternalCQL.g:2101:2: ( rule__Primary__InnerAssignment_0_2 )
            {
             before(grammarAccess.getPrimaryAccess().getInnerAssignment_0_2()); 
            // InternalCQL.g:2102:2: ( rule__Primary__InnerAssignment_0_2 )
            // InternalCQL.g:2102:3: rule__Primary__InnerAssignment_0_2
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
    // InternalCQL.g:2110:1: rule__Primary__Group_0__3 : rule__Primary__Group_0__3__Impl ;
    public final void rule__Primary__Group_0__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2114:1: ( rule__Primary__Group_0__3__Impl )
            // InternalCQL.g:2115:2: rule__Primary__Group_0__3__Impl
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
    // InternalCQL.g:2121:1: rule__Primary__Group_0__3__Impl : ( ')' ) ;
    public final void rule__Primary__Group_0__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2125:1: ( ( ')' ) )
            // InternalCQL.g:2126:1: ( ')' )
            {
            // InternalCQL.g:2126:1: ( ')' )
            // InternalCQL.g:2127:2: ')'
            {
             before(grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_0_3()); 
            match(input,35,FOLLOW_2); 
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
    // InternalCQL.g:2137:1: rule__Primary__Group_1__0 : rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 ;
    public final void rule__Primary__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2141:1: ( rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1 )
            // InternalCQL.g:2142:2: rule__Primary__Group_1__0__Impl rule__Primary__Group_1__1
            {
            pushFollow(FOLLOW_27);
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
    // InternalCQL.g:2149:1: rule__Primary__Group_1__0__Impl : ( () ) ;
    public final void rule__Primary__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2153:1: ( ( () ) )
            // InternalCQL.g:2154:1: ( () )
            {
            // InternalCQL.g:2154:1: ( () )
            // InternalCQL.g:2155:2: ()
            {
             before(grammarAccess.getPrimaryAccess().getNOTAction_1_0()); 
            // InternalCQL.g:2156:2: ()
            // InternalCQL.g:2156:3: 
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
    // InternalCQL.g:2164:1: rule__Primary__Group_1__1 : rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 ;
    public final void rule__Primary__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2168:1: ( rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2 )
            // InternalCQL.g:2169:2: rule__Primary__Group_1__1__Impl rule__Primary__Group_1__2
            {
            pushFollow(FOLLOW_10);
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
    // InternalCQL.g:2176:1: rule__Primary__Group_1__1__Impl : ( 'NOT' ) ;
    public final void rule__Primary__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2180:1: ( ( 'NOT' ) )
            // InternalCQL.g:2181:1: ( 'NOT' )
            {
            // InternalCQL.g:2181:1: ( 'NOT' )
            // InternalCQL.g:2182:2: 'NOT'
            {
             before(grammarAccess.getPrimaryAccess().getNOTKeyword_1_1()); 
            match(input,36,FOLLOW_2); 
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
    // InternalCQL.g:2191:1: rule__Primary__Group_1__2 : rule__Primary__Group_1__2__Impl ;
    public final void rule__Primary__Group_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2195:1: ( rule__Primary__Group_1__2__Impl )
            // InternalCQL.g:2196:2: rule__Primary__Group_1__2__Impl
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
    // InternalCQL.g:2202:1: rule__Primary__Group_1__2__Impl : ( ( rule__Primary__ExpressionAssignment_1_2 ) ) ;
    public final void rule__Primary__Group_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2206:1: ( ( ( rule__Primary__ExpressionAssignment_1_2 ) ) )
            // InternalCQL.g:2207:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            {
            // InternalCQL.g:2207:1: ( ( rule__Primary__ExpressionAssignment_1_2 ) )
            // InternalCQL.g:2208:2: ( rule__Primary__ExpressionAssignment_1_2 )
            {
             before(grammarAccess.getPrimaryAccess().getExpressionAssignment_1_2()); 
            // InternalCQL.g:2209:2: ( rule__Primary__ExpressionAssignment_1_2 )
            // InternalCQL.g:2209:3: rule__Primary__ExpressionAssignment_1_2
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
    // InternalCQL.g:2218:1: rule__Select_Statement__Group__0 : rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 ;
    public final void rule__Select_Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2222:1: ( rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1 )
            // InternalCQL.g:2223:2: rule__Select_Statement__Group__0__Impl rule__Select_Statement__Group__1
            {
            pushFollow(FOLLOW_28);
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
    // InternalCQL.g:2230:1: rule__Select_Statement__Group__0__Impl : ( ( rule__Select_Statement__NameAssignment_0 ) ) ;
    public final void rule__Select_Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2234:1: ( ( ( rule__Select_Statement__NameAssignment_0 ) ) )
            // InternalCQL.g:2235:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            {
            // InternalCQL.g:2235:1: ( ( rule__Select_Statement__NameAssignment_0 ) )
            // InternalCQL.g:2236:2: ( rule__Select_Statement__NameAssignment_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameAssignment_0()); 
            // InternalCQL.g:2237:2: ( rule__Select_Statement__NameAssignment_0 )
            // InternalCQL.g:2237:3: rule__Select_Statement__NameAssignment_0
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
    // InternalCQL.g:2245:1: rule__Select_Statement__Group__1 : rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 ;
    public final void rule__Select_Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2249:1: ( rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2 )
            // InternalCQL.g:2250:2: rule__Select_Statement__Group__1__Impl rule__Select_Statement__Group__2
            {
            pushFollow(FOLLOW_28);
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
    // InternalCQL.g:2257:1: rule__Select_Statement__Group__1__Impl : ( ( 'DISTINCT' )? ) ;
    public final void rule__Select_Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2261:1: ( ( ( 'DISTINCT' )? ) )
            // InternalCQL.g:2262:1: ( ( 'DISTINCT' )? )
            {
            // InternalCQL.g:2262:1: ( ( 'DISTINCT' )? )
            // InternalCQL.g:2263:2: ( 'DISTINCT' )?
            {
             before(grammarAccess.getSelect_StatementAccess().getDISTINCTKeyword_1()); 
            // InternalCQL.g:2264:2: ( 'DISTINCT' )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==37) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCQL.g:2264:3: 'DISTINCT'
                    {
                    match(input,37,FOLLOW_2); 

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
    // InternalCQL.g:2272:1: rule__Select_Statement__Group__2 : rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 ;
    public final void rule__Select_Statement__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2276:1: ( rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3 )
            // InternalCQL.g:2277:2: rule__Select_Statement__Group__2__Impl rule__Select_Statement__Group__3
            {
            pushFollow(FOLLOW_29);
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
    // InternalCQL.g:2284:1: rule__Select_Statement__Group__2__Impl : ( ( rule__Select_Statement__Alternatives_2 ) ) ;
    public final void rule__Select_Statement__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2288:1: ( ( ( rule__Select_Statement__Alternatives_2 ) ) )
            // InternalCQL.g:2289:1: ( ( rule__Select_Statement__Alternatives_2 ) )
            {
            // InternalCQL.g:2289:1: ( ( rule__Select_Statement__Alternatives_2 ) )
            // InternalCQL.g:2290:2: ( rule__Select_Statement__Alternatives_2 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAlternatives_2()); 
            // InternalCQL.g:2291:2: ( rule__Select_Statement__Alternatives_2 )
            // InternalCQL.g:2291:3: rule__Select_Statement__Alternatives_2
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Alternatives_2();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getAlternatives_2()); 

            }


            }

        }
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
    // InternalCQL.g:2299:1: rule__Select_Statement__Group__3 : rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 ;
    public final void rule__Select_Statement__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2303:1: ( rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4 )
            // InternalCQL.g:2304:2: rule__Select_Statement__Group__3__Impl rule__Select_Statement__Group__4
            {
            pushFollow(FOLLOW_9);
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
    // InternalCQL.g:2311:1: rule__Select_Statement__Group__3__Impl : ( 'FROM' ) ;
    public final void rule__Select_Statement__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2315:1: ( ( 'FROM' ) )
            // InternalCQL.g:2316:1: ( 'FROM' )
            {
            // InternalCQL.g:2316:1: ( 'FROM' )
            // InternalCQL.g:2317:2: 'FROM'
            {
             before(grammarAccess.getSelect_StatementAccess().getFROMKeyword_3()); 
            match(input,38,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getFROMKeyword_3()); 

            }


            }

        }
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
    // InternalCQL.g:2326:1: rule__Select_Statement__Group__4 : rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 ;
    public final void rule__Select_Statement__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2330:1: ( rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5 )
            // InternalCQL.g:2331:2: rule__Select_Statement__Group__4__Impl rule__Select_Statement__Group__5
            {
            pushFollow(FOLLOW_30);
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
    // InternalCQL.g:2338:1: rule__Select_Statement__Group__4__Impl : ( ( rule__Select_Statement__Group_4__0 ) ) ;
    public final void rule__Select_Statement__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2342:1: ( ( ( rule__Select_Statement__Group_4__0 ) ) )
            // InternalCQL.g:2343:1: ( ( rule__Select_Statement__Group_4__0 ) )
            {
            // InternalCQL.g:2343:1: ( ( rule__Select_Statement__Group_4__0 ) )
            // InternalCQL.g:2344:2: ( rule__Select_Statement__Group_4__0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_4()); 
            // InternalCQL.g:2345:2: ( rule__Select_Statement__Group_4__0 )
            // InternalCQL.g:2345:3: rule__Select_Statement__Group_4__0
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4__0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getGroup_4()); 

            }


            }

        }
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
    // InternalCQL.g:2353:1: rule__Select_Statement__Group__5 : rule__Select_Statement__Group__5__Impl ;
    public final void rule__Select_Statement__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2357:1: ( rule__Select_Statement__Group__5__Impl )
            // InternalCQL.g:2358:2: rule__Select_Statement__Group__5__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group__5__Impl();

            state._fsp--;


            }

        }
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
    // InternalCQL.g:2364:1: rule__Select_Statement__Group__5__Impl : ( ( rule__Select_Statement__Group_5__0 )? ) ;
    public final void rule__Select_Statement__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2368:1: ( ( ( rule__Select_Statement__Group_5__0 )? ) )
            // InternalCQL.g:2369:1: ( ( rule__Select_Statement__Group_5__0 )? )
            {
            // InternalCQL.g:2369:1: ( ( rule__Select_Statement__Group_5__0 )? )
            // InternalCQL.g:2370:2: ( rule__Select_Statement__Group_5__0 )?
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_5()); 
            // InternalCQL.g:2371:2: ( rule__Select_Statement__Group_5__0 )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==40) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalCQL.g:2371:3: rule__Select_Statement__Group_5__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Select_Statement__Group_5__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getSelect_StatementAccess().getGroup_5()); 

            }


            }

        }
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


    // $ANTLR start "rule__Select_Statement__Group_2_1__0"
    // InternalCQL.g:2380:1: rule__Select_Statement__Group_2_1__0 : rule__Select_Statement__Group_2_1__0__Impl rule__Select_Statement__Group_2_1__1 ;
    public final void rule__Select_Statement__Group_2_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2384:1: ( rule__Select_Statement__Group_2_1__0__Impl rule__Select_Statement__Group_2_1__1 )
            // InternalCQL.g:2385:2: rule__Select_Statement__Group_2_1__0__Impl rule__Select_Statement__Group_2_1__1
            {
            pushFollow(FOLLOW_31);
            rule__Select_Statement__Group_2_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_2_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_2_1__0"


    // $ANTLR start "rule__Select_Statement__Group_2_1__0__Impl"
    // InternalCQL.g:2392:1: rule__Select_Statement__Group_2_1__0__Impl : ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) ) ;
    public final void rule__Select_Statement__Group_2_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2396:1: ( ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) ) )
            // InternalCQL.g:2397:1: ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) )
            {
            // InternalCQL.g:2397:1: ( ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* ) )
            // InternalCQL.g:2398:2: ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) ) ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* )
            {
            // InternalCQL.g:2398:2: ( ( rule__Select_Statement__AttributesAssignment_2_1_0 ) )
            // InternalCQL.g:2399:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 
            // InternalCQL.g:2400:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )
            // InternalCQL.g:2400:4: rule__Select_Statement__AttributesAssignment_2_1_0
            {
            pushFollow(FOLLOW_32);
            rule__Select_Statement__AttributesAssignment_2_1_0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 

            }

            // InternalCQL.g:2403:2: ( ( rule__Select_Statement__AttributesAssignment_2_1_0 )* )
            // InternalCQL.g:2404:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 
            // InternalCQL.g:2405:3: ( rule__Select_Statement__AttributesAssignment_2_1_0 )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==RULE_ID) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // InternalCQL.g:2405:4: rule__Select_Statement__AttributesAssignment_2_1_0
            	    {
            	    pushFollow(FOLLOW_32);
            	    rule__Select_Statement__AttributesAssignment_2_1_0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

             after(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0()); 

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
    // $ANTLR end "rule__Select_Statement__Group_2_1__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group_2_1__1"
    // InternalCQL.g:2414:1: rule__Select_Statement__Group_2_1__1 : rule__Select_Statement__Group_2_1__1__Impl ;
    public final void rule__Select_Statement__Group_2_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2418:1: ( rule__Select_Statement__Group_2_1__1__Impl )
            // InternalCQL.g:2419:2: rule__Select_Statement__Group_2_1__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_2_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_2_1__1"


    // $ANTLR start "rule__Select_Statement__Group_2_1__1__Impl"
    // InternalCQL.g:2425:1: rule__Select_Statement__Group_2_1__1__Impl : ( ( rule__Select_Statement__Group_2_1_1__0 )* ) ;
    public final void rule__Select_Statement__Group_2_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2429:1: ( ( ( rule__Select_Statement__Group_2_1_1__0 )* ) )
            // InternalCQL.g:2430:1: ( ( rule__Select_Statement__Group_2_1_1__0 )* )
            {
            // InternalCQL.g:2430:1: ( ( rule__Select_Statement__Group_2_1_1__0 )* )
            // InternalCQL.g:2431:2: ( rule__Select_Statement__Group_2_1_1__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_2_1_1()); 
            // InternalCQL.g:2432:2: ( rule__Select_Statement__Group_2_1_1__0 )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==39) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // InternalCQL.g:2432:3: rule__Select_Statement__Group_2_1_1__0
            	    {
            	    pushFollow(FOLLOW_33);
            	    rule__Select_Statement__Group_2_1_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);

             after(grammarAccess.getSelect_StatementAccess().getGroup_2_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_2_1__1__Impl"


    // $ANTLR start "rule__Select_Statement__Group_2_1_1__0"
    // InternalCQL.g:2441:1: rule__Select_Statement__Group_2_1_1__0 : rule__Select_Statement__Group_2_1_1__0__Impl rule__Select_Statement__Group_2_1_1__1 ;
    public final void rule__Select_Statement__Group_2_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2445:1: ( rule__Select_Statement__Group_2_1_1__0__Impl rule__Select_Statement__Group_2_1_1__1 )
            // InternalCQL.g:2446:2: rule__Select_Statement__Group_2_1_1__0__Impl rule__Select_Statement__Group_2_1_1__1
            {
            pushFollow(FOLLOW_9);
            rule__Select_Statement__Group_2_1_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_2_1_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_2_1_1__0"


    // $ANTLR start "rule__Select_Statement__Group_2_1_1__0__Impl"
    // InternalCQL.g:2453:1: rule__Select_Statement__Group_2_1_1__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_2_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2457:1: ( ( ',' ) )
            // InternalCQL.g:2458:1: ( ',' )
            {
            // InternalCQL.g:2458:1: ( ',' )
            // InternalCQL.g:2459:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_2_1_1_0()); 
            match(input,39,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getCommaKeyword_2_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_2_1_1__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group_2_1_1__1"
    // InternalCQL.g:2468:1: rule__Select_Statement__Group_2_1_1__1 : rule__Select_Statement__Group_2_1_1__1__Impl ;
    public final void rule__Select_Statement__Group_2_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2472:1: ( rule__Select_Statement__Group_2_1_1__1__Impl )
            // InternalCQL.g:2473:2: rule__Select_Statement__Group_2_1_1__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_2_1_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_2_1_1__1"


    // $ANTLR start "rule__Select_Statement__Group_2_1_1__1__Impl"
    // InternalCQL.g:2479:1: rule__Select_Statement__Group_2_1_1__1__Impl : ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) ) ;
    public final void rule__Select_Statement__Group_2_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2483:1: ( ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) ) )
            // InternalCQL.g:2484:1: ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) )
            {
            // InternalCQL.g:2484:1: ( ( rule__Select_Statement__AttributesAssignment_2_1_1_1 ) )
            // InternalCQL.g:2485:2: ( rule__Select_Statement__AttributesAssignment_2_1_1_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_1_1()); 
            // InternalCQL.g:2486:2: ( rule__Select_Statement__AttributesAssignment_2_1_1_1 )
            // InternalCQL.g:2486:3: rule__Select_Statement__AttributesAssignment_2_1_1_1
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__AttributesAssignment_2_1_1_1();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_2_1_1__1__Impl"


    // $ANTLR start "rule__Select_Statement__Group_4__0"
    // InternalCQL.g:2495:1: rule__Select_Statement__Group_4__0 : rule__Select_Statement__Group_4__0__Impl rule__Select_Statement__Group_4__1 ;
    public final void rule__Select_Statement__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2499:1: ( rule__Select_Statement__Group_4__0__Impl rule__Select_Statement__Group_4__1 )
            // InternalCQL.g:2500:2: rule__Select_Statement__Group_4__0__Impl rule__Select_Statement__Group_4__1
            {
            pushFollow(FOLLOW_31);
            rule__Select_Statement__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4__0"


    // $ANTLR start "rule__Select_Statement__Group_4__0__Impl"
    // InternalCQL.g:2507:1: rule__Select_Statement__Group_4__0__Impl : ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) ) ;
    public final void rule__Select_Statement__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2511:1: ( ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) ) )
            // InternalCQL.g:2512:1: ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) )
            {
            // InternalCQL.g:2512:1: ( ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* ) )
            // InternalCQL.g:2513:2: ( ( rule__Select_Statement__SourcesAssignment_4_0 ) ) ( ( rule__Select_Statement__SourcesAssignment_4_0 )* )
            {
            // InternalCQL.g:2513:2: ( ( rule__Select_Statement__SourcesAssignment_4_0 ) )
            // InternalCQL.g:2514:3: ( rule__Select_Statement__SourcesAssignment_4_0 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 
            // InternalCQL.g:2515:3: ( rule__Select_Statement__SourcesAssignment_4_0 )
            // InternalCQL.g:2515:4: rule__Select_Statement__SourcesAssignment_4_0
            {
            pushFollow(FOLLOW_34);
            rule__Select_Statement__SourcesAssignment_4_0();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 

            }

            // InternalCQL.g:2518:2: ( ( rule__Select_Statement__SourcesAssignment_4_0 )* )
            // InternalCQL.g:2519:3: ( rule__Select_Statement__SourcesAssignment_4_0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 
            // InternalCQL.g:2520:3: ( rule__Select_Statement__SourcesAssignment_4_0 )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==RULE_ID) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // InternalCQL.g:2520:4: rule__Select_Statement__SourcesAssignment_4_0
            	    {
            	    pushFollow(FOLLOW_34);
            	    rule__Select_Statement__SourcesAssignment_4_0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0()); 

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
    // $ANTLR end "rule__Select_Statement__Group_4__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group_4__1"
    // InternalCQL.g:2529:1: rule__Select_Statement__Group_4__1 : rule__Select_Statement__Group_4__1__Impl ;
    public final void rule__Select_Statement__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2533:1: ( rule__Select_Statement__Group_4__1__Impl )
            // InternalCQL.g:2534:2: rule__Select_Statement__Group_4__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4__1"


    // $ANTLR start "rule__Select_Statement__Group_4__1__Impl"
    // InternalCQL.g:2540:1: rule__Select_Statement__Group_4__1__Impl : ( ( rule__Select_Statement__Group_4_1__0 )* ) ;
    public final void rule__Select_Statement__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2544:1: ( ( ( rule__Select_Statement__Group_4_1__0 )* ) )
            // InternalCQL.g:2545:1: ( ( rule__Select_Statement__Group_4_1__0 )* )
            {
            // InternalCQL.g:2545:1: ( ( rule__Select_Statement__Group_4_1__0 )* )
            // InternalCQL.g:2546:2: ( rule__Select_Statement__Group_4_1__0 )*
            {
             before(grammarAccess.getSelect_StatementAccess().getGroup_4_1()); 
            // InternalCQL.g:2547:2: ( rule__Select_Statement__Group_4_1__0 )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==39) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // InternalCQL.g:2547:3: rule__Select_Statement__Group_4_1__0
            	    {
            	    pushFollow(FOLLOW_33);
            	    rule__Select_Statement__Group_4_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop25;
                }
            } while (true);

             after(grammarAccess.getSelect_StatementAccess().getGroup_4_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4__1__Impl"


    // $ANTLR start "rule__Select_Statement__Group_4_1__0"
    // InternalCQL.g:2556:1: rule__Select_Statement__Group_4_1__0 : rule__Select_Statement__Group_4_1__0__Impl rule__Select_Statement__Group_4_1__1 ;
    public final void rule__Select_Statement__Group_4_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2560:1: ( rule__Select_Statement__Group_4_1__0__Impl rule__Select_Statement__Group_4_1__1 )
            // InternalCQL.g:2561:2: rule__Select_Statement__Group_4_1__0__Impl rule__Select_Statement__Group_4_1__1
            {
            pushFollow(FOLLOW_9);
            rule__Select_Statement__Group_4_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_1__0"


    // $ANTLR start "rule__Select_Statement__Group_4_1__0__Impl"
    // InternalCQL.g:2568:1: rule__Select_Statement__Group_4_1__0__Impl : ( ',' ) ;
    public final void rule__Select_Statement__Group_4_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2572:1: ( ( ',' ) )
            // InternalCQL.g:2573:1: ( ',' )
            {
            // InternalCQL.g:2573:1: ( ',' )
            // InternalCQL.g:2574:2: ','
            {
             before(grammarAccess.getSelect_StatementAccess().getCommaKeyword_4_1_0()); 
            match(input,39,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getCommaKeyword_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_1__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group_4_1__1"
    // InternalCQL.g:2583:1: rule__Select_Statement__Group_4_1__1 : rule__Select_Statement__Group_4_1__1__Impl ;
    public final void rule__Select_Statement__Group_4_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2587:1: ( rule__Select_Statement__Group_4_1__1__Impl )
            // InternalCQL.g:2588:2: rule__Select_Statement__Group_4_1__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_4_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_1__1"


    // $ANTLR start "rule__Select_Statement__Group_4_1__1__Impl"
    // InternalCQL.g:2594:1: rule__Select_Statement__Group_4_1__1__Impl : ( ( rule__Select_Statement__SourcesAssignment_4_1_1 ) ) ;
    public final void rule__Select_Statement__Group_4_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2598:1: ( ( ( rule__Select_Statement__SourcesAssignment_4_1_1 ) ) )
            // InternalCQL.g:2599:1: ( ( rule__Select_Statement__SourcesAssignment_4_1_1 ) )
            {
            // InternalCQL.g:2599:1: ( ( rule__Select_Statement__SourcesAssignment_4_1_1 ) )
            // InternalCQL.g:2600:2: ( rule__Select_Statement__SourcesAssignment_4_1_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_1_1()); 
            // InternalCQL.g:2601:2: ( rule__Select_Statement__SourcesAssignment_4_1_1 )
            // InternalCQL.g:2601:3: rule__Select_Statement__SourcesAssignment_4_1_1
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__SourcesAssignment_4_1_1();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_4_1__1__Impl"


    // $ANTLR start "rule__Select_Statement__Group_5__0"
    // InternalCQL.g:2610:1: rule__Select_Statement__Group_5__0 : rule__Select_Statement__Group_5__0__Impl rule__Select_Statement__Group_5__1 ;
    public final void rule__Select_Statement__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2614:1: ( rule__Select_Statement__Group_5__0__Impl rule__Select_Statement__Group_5__1 )
            // InternalCQL.g:2615:2: rule__Select_Statement__Group_5__0__Impl rule__Select_Statement__Group_5__1
            {
            pushFollow(FOLLOW_10);
            rule__Select_Statement__Group_5__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_5__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_5__0"


    // $ANTLR start "rule__Select_Statement__Group_5__0__Impl"
    // InternalCQL.g:2622:1: rule__Select_Statement__Group_5__0__Impl : ( 'WHERE' ) ;
    public final void rule__Select_Statement__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2626:1: ( ( 'WHERE' ) )
            // InternalCQL.g:2627:1: ( 'WHERE' )
            {
            // InternalCQL.g:2627:1: ( 'WHERE' )
            // InternalCQL.g:2628:2: 'WHERE'
            {
             before(grammarAccess.getSelect_StatementAccess().getWHEREKeyword_5_0()); 
            match(input,40,FOLLOW_2); 
             after(grammarAccess.getSelect_StatementAccess().getWHEREKeyword_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_5__0__Impl"


    // $ANTLR start "rule__Select_Statement__Group_5__1"
    // InternalCQL.g:2637:1: rule__Select_Statement__Group_5__1 : rule__Select_Statement__Group_5__1__Impl ;
    public final void rule__Select_Statement__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2641:1: ( rule__Select_Statement__Group_5__1__Impl )
            // InternalCQL.g:2642:2: rule__Select_Statement__Group_5__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__Group_5__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_5__1"


    // $ANTLR start "rule__Select_Statement__Group_5__1__Impl"
    // InternalCQL.g:2648:1: rule__Select_Statement__Group_5__1__Impl : ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) ) ;
    public final void rule__Select_Statement__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2652:1: ( ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) ) )
            // InternalCQL.g:2653:1: ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) )
            {
            // InternalCQL.g:2653:1: ( ( rule__Select_Statement__PredicatesAssignment_5_1 ) )
            // InternalCQL.g:2654:2: ( rule__Select_Statement__PredicatesAssignment_5_1 )
            {
             before(grammarAccess.getSelect_StatementAccess().getPredicatesAssignment_5_1()); 
            // InternalCQL.g:2655:2: ( rule__Select_Statement__PredicatesAssignment_5_1 )
            // InternalCQL.g:2655:3: rule__Select_Statement__PredicatesAssignment_5_1
            {
            pushFollow(FOLLOW_2);
            rule__Select_Statement__PredicatesAssignment_5_1();

            state._fsp--;


            }

             after(grammarAccess.getSelect_StatementAccess().getPredicatesAssignment_5_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__Group_5__1__Impl"


    // $ANTLR start "rule__Create_Statement__Group__0"
    // InternalCQL.g:2664:1: rule__Create_Statement__Group__0 : rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1 ;
    public final void rule__Create_Statement__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2668:1: ( rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1 )
            // InternalCQL.g:2669:2: rule__Create_Statement__Group__0__Impl rule__Create_Statement__Group__1
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
    // InternalCQL.g:2676:1: rule__Create_Statement__Group__0__Impl : ( 'CREATE' ) ;
    public final void rule__Create_Statement__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2680:1: ( ( 'CREATE' ) )
            // InternalCQL.g:2681:1: ( 'CREATE' )
            {
            // InternalCQL.g:2681:1: ( 'CREATE' )
            // InternalCQL.g:2682:2: 'CREATE'
            {
             before(grammarAccess.getCreate_StatementAccess().getCREATEKeyword_0()); 
            match(input,41,FOLLOW_2); 
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
    // InternalCQL.g:2691:1: rule__Create_Statement__Group__1 : rule__Create_Statement__Group__1__Impl rule__Create_Statement__Group__2 ;
    public final void rule__Create_Statement__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2695:1: ( rule__Create_Statement__Group__1__Impl rule__Create_Statement__Group__2 )
            // InternalCQL.g:2696:2: rule__Create_Statement__Group__1__Impl rule__Create_Statement__Group__2
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
    // InternalCQL.g:2703:1: rule__Create_Statement__Group__1__Impl : ( 'STREAM' ) ;
    public final void rule__Create_Statement__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2707:1: ( ( 'STREAM' ) )
            // InternalCQL.g:2708:1: ( 'STREAM' )
            {
            // InternalCQL.g:2708:1: ( 'STREAM' )
            // InternalCQL.g:2709:2: 'STREAM'
            {
             before(grammarAccess.getCreate_StatementAccess().getSTREAMKeyword_1()); 
            match(input,42,FOLLOW_2); 
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
    // InternalCQL.g:2718:1: rule__Create_Statement__Group__2 : rule__Create_Statement__Group__2__Impl rule__Create_Statement__Group__3 ;
    public final void rule__Create_Statement__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2722:1: ( rule__Create_Statement__Group__2__Impl rule__Create_Statement__Group__3 )
            // InternalCQL.g:2723:2: rule__Create_Statement__Group__2__Impl rule__Create_Statement__Group__3
            {
            pushFollow(FOLLOW_25);
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
    // InternalCQL.g:2730:1: rule__Create_Statement__Group__2__Impl : ( ( rule__Create_Statement__NameAssignment_2 ) ) ;
    public final void rule__Create_Statement__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2734:1: ( ( ( rule__Create_Statement__NameAssignment_2 ) ) )
            // InternalCQL.g:2735:1: ( ( rule__Create_Statement__NameAssignment_2 ) )
            {
            // InternalCQL.g:2735:1: ( ( rule__Create_Statement__NameAssignment_2 ) )
            // InternalCQL.g:2736:2: ( rule__Create_Statement__NameAssignment_2 )
            {
             before(grammarAccess.getCreate_StatementAccess().getNameAssignment_2()); 
            // InternalCQL.g:2737:2: ( rule__Create_Statement__NameAssignment_2 )
            // InternalCQL.g:2737:3: rule__Create_Statement__NameAssignment_2
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
    // InternalCQL.g:2745:1: rule__Create_Statement__Group__3 : rule__Create_Statement__Group__3__Impl rule__Create_Statement__Group__4 ;
    public final void rule__Create_Statement__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2749:1: ( rule__Create_Statement__Group__3__Impl rule__Create_Statement__Group__4 )
            // InternalCQL.g:2750:2: rule__Create_Statement__Group__3__Impl rule__Create_Statement__Group__4
            {
            pushFollow(FOLLOW_9);
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
    // InternalCQL.g:2757:1: rule__Create_Statement__Group__3__Impl : ( '(' ) ;
    public final void rule__Create_Statement__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2761:1: ( ( '(' ) )
            // InternalCQL.g:2762:1: ( '(' )
            {
            // InternalCQL.g:2762:1: ( '(' )
            // InternalCQL.g:2763:2: '('
            {
             before(grammarAccess.getCreate_StatementAccess().getLeftParenthesisKeyword_3()); 
            match(input,34,FOLLOW_2); 
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
    // InternalCQL.g:2772:1: rule__Create_Statement__Group__4 : rule__Create_Statement__Group__4__Impl rule__Create_Statement__Group__5 ;
    public final void rule__Create_Statement__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2776:1: ( rule__Create_Statement__Group__4__Impl rule__Create_Statement__Group__5 )
            // InternalCQL.g:2777:2: rule__Create_Statement__Group__4__Impl rule__Create_Statement__Group__5
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
    // InternalCQL.g:2784:1: rule__Create_Statement__Group__4__Impl : ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) ) ;
    public final void rule__Create_Statement__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2788:1: ( ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) ) )
            // InternalCQL.g:2789:1: ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) )
            {
            // InternalCQL.g:2789:1: ( ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* ) )
            // InternalCQL.g:2790:2: ( ( rule__Create_Statement__AttributesAssignment_4 ) ) ( ( rule__Create_Statement__AttributesAssignment_4 )* )
            {
            // InternalCQL.g:2790:2: ( ( rule__Create_Statement__AttributesAssignment_4 ) )
            // InternalCQL.g:2791:3: ( rule__Create_Statement__AttributesAssignment_4 )
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 
            // InternalCQL.g:2792:3: ( rule__Create_Statement__AttributesAssignment_4 )
            // InternalCQL.g:2792:4: rule__Create_Statement__AttributesAssignment_4
            {
            pushFollow(FOLLOW_34);
            rule__Create_Statement__AttributesAssignment_4();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 

            }

            // InternalCQL.g:2795:2: ( ( rule__Create_Statement__AttributesAssignment_4 )* )
            // InternalCQL.g:2796:3: ( rule__Create_Statement__AttributesAssignment_4 )*
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_4()); 
            // InternalCQL.g:2797:3: ( rule__Create_Statement__AttributesAssignment_4 )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==RULE_ID) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // InternalCQL.g:2797:4: rule__Create_Statement__AttributesAssignment_4
            	    {
            	    pushFollow(FOLLOW_34);
            	    rule__Create_Statement__AttributesAssignment_4();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop26;
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
    // InternalCQL.g:2806:1: rule__Create_Statement__Group__5 : rule__Create_Statement__Group__5__Impl rule__Create_Statement__Group__6 ;
    public final void rule__Create_Statement__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2810:1: ( rule__Create_Statement__Group__5__Impl rule__Create_Statement__Group__6 )
            // InternalCQL.g:2811:2: rule__Create_Statement__Group__5__Impl rule__Create_Statement__Group__6
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
    // InternalCQL.g:2818:1: rule__Create_Statement__Group__5__Impl : ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) ) ;
    public final void rule__Create_Statement__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2822:1: ( ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) ) )
            // InternalCQL.g:2823:1: ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) )
            {
            // InternalCQL.g:2823:1: ( ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* ) )
            // InternalCQL.g:2824:2: ( ( rule__Create_Statement__DatatypesAssignment_5 ) ) ( ( rule__Create_Statement__DatatypesAssignment_5 )* )
            {
            // InternalCQL.g:2824:2: ( ( rule__Create_Statement__DatatypesAssignment_5 ) )
            // InternalCQL.g:2825:3: ( rule__Create_Statement__DatatypesAssignment_5 )
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 
            // InternalCQL.g:2826:3: ( rule__Create_Statement__DatatypesAssignment_5 )
            // InternalCQL.g:2826:4: rule__Create_Statement__DatatypesAssignment_5
            {
            pushFollow(FOLLOW_38);
            rule__Create_Statement__DatatypesAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 

            }

            // InternalCQL.g:2829:2: ( ( rule__Create_Statement__DatatypesAssignment_5 )* )
            // InternalCQL.g:2830:3: ( rule__Create_Statement__DatatypesAssignment_5 )*
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_5()); 
            // InternalCQL.g:2831:3: ( rule__Create_Statement__DatatypesAssignment_5 )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( ((LA27_0>=14 && LA27_0<=20)) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // InternalCQL.g:2831:4: rule__Create_Statement__DatatypesAssignment_5
            	    {
            	    pushFollow(FOLLOW_38);
            	    rule__Create_Statement__DatatypesAssignment_5();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop27;
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
    // InternalCQL.g:2840:1: rule__Create_Statement__Group__6 : rule__Create_Statement__Group__6__Impl rule__Create_Statement__Group__7 ;
    public final void rule__Create_Statement__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2844:1: ( rule__Create_Statement__Group__6__Impl rule__Create_Statement__Group__7 )
            // InternalCQL.g:2845:2: rule__Create_Statement__Group__6__Impl rule__Create_Statement__Group__7
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
    // InternalCQL.g:2852:1: rule__Create_Statement__Group__6__Impl : ( ( rule__Create_Statement__Group_6__0 )* ) ;
    public final void rule__Create_Statement__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2856:1: ( ( ( rule__Create_Statement__Group_6__0 )* ) )
            // InternalCQL.g:2857:1: ( ( rule__Create_Statement__Group_6__0 )* )
            {
            // InternalCQL.g:2857:1: ( ( rule__Create_Statement__Group_6__0 )* )
            // InternalCQL.g:2858:2: ( rule__Create_Statement__Group_6__0 )*
            {
             before(grammarAccess.getCreate_StatementAccess().getGroup_6()); 
            // InternalCQL.g:2859:2: ( rule__Create_Statement__Group_6__0 )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==39) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // InternalCQL.g:2859:3: rule__Create_Statement__Group_6__0
            	    {
            	    pushFollow(FOLLOW_33);
            	    rule__Create_Statement__Group_6__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop28;
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
    // InternalCQL.g:2867:1: rule__Create_Statement__Group__7 : rule__Create_Statement__Group__7__Impl ;
    public final void rule__Create_Statement__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2871:1: ( rule__Create_Statement__Group__7__Impl )
            // InternalCQL.g:2872:2: rule__Create_Statement__Group__7__Impl
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
    // InternalCQL.g:2878:1: rule__Create_Statement__Group__7__Impl : ( ')' ) ;
    public final void rule__Create_Statement__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2882:1: ( ( ')' ) )
            // InternalCQL.g:2883:1: ( ')' )
            {
            // InternalCQL.g:2883:1: ( ')' )
            // InternalCQL.g:2884:2: ')'
            {
             before(grammarAccess.getCreate_StatementAccess().getRightParenthesisKeyword_7()); 
            match(input,35,FOLLOW_2); 
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
    // InternalCQL.g:2894:1: rule__Create_Statement__Group_6__0 : rule__Create_Statement__Group_6__0__Impl rule__Create_Statement__Group_6__1 ;
    public final void rule__Create_Statement__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2898:1: ( rule__Create_Statement__Group_6__0__Impl rule__Create_Statement__Group_6__1 )
            // InternalCQL.g:2899:2: rule__Create_Statement__Group_6__0__Impl rule__Create_Statement__Group_6__1
            {
            pushFollow(FOLLOW_9);
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
    // InternalCQL.g:2906:1: rule__Create_Statement__Group_6__0__Impl : ( ',' ) ;
    public final void rule__Create_Statement__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2910:1: ( ( ',' ) )
            // InternalCQL.g:2911:1: ( ',' )
            {
            // InternalCQL.g:2911:1: ( ',' )
            // InternalCQL.g:2912:2: ','
            {
             before(grammarAccess.getCreate_StatementAccess().getCommaKeyword_6_0()); 
            match(input,39,FOLLOW_2); 
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
    // InternalCQL.g:2921:1: rule__Create_Statement__Group_6__1 : rule__Create_Statement__Group_6__1__Impl rule__Create_Statement__Group_6__2 ;
    public final void rule__Create_Statement__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2925:1: ( rule__Create_Statement__Group_6__1__Impl rule__Create_Statement__Group_6__2 )
            // InternalCQL.g:2926:2: rule__Create_Statement__Group_6__1__Impl rule__Create_Statement__Group_6__2
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
    // InternalCQL.g:2933:1: rule__Create_Statement__Group_6__1__Impl : ( ( rule__Create_Statement__AttributesAssignment_6_1 ) ) ;
    public final void rule__Create_Statement__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2937:1: ( ( ( rule__Create_Statement__AttributesAssignment_6_1 ) ) )
            // InternalCQL.g:2938:1: ( ( rule__Create_Statement__AttributesAssignment_6_1 ) )
            {
            // InternalCQL.g:2938:1: ( ( rule__Create_Statement__AttributesAssignment_6_1 ) )
            // InternalCQL.g:2939:2: ( rule__Create_Statement__AttributesAssignment_6_1 )
            {
             before(grammarAccess.getCreate_StatementAccess().getAttributesAssignment_6_1()); 
            // InternalCQL.g:2940:2: ( rule__Create_Statement__AttributesAssignment_6_1 )
            // InternalCQL.g:2940:3: rule__Create_Statement__AttributesAssignment_6_1
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
    // InternalCQL.g:2948:1: rule__Create_Statement__Group_6__2 : rule__Create_Statement__Group_6__2__Impl ;
    public final void rule__Create_Statement__Group_6__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2952:1: ( rule__Create_Statement__Group_6__2__Impl )
            // InternalCQL.g:2953:2: rule__Create_Statement__Group_6__2__Impl
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
    // InternalCQL.g:2959:1: rule__Create_Statement__Group_6__2__Impl : ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) ) ;
    public final void rule__Create_Statement__Group_6__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2963:1: ( ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) ) )
            // InternalCQL.g:2964:1: ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) )
            {
            // InternalCQL.g:2964:1: ( ( rule__Create_Statement__DatatypesAssignment_6_2 ) )
            // InternalCQL.g:2965:2: ( rule__Create_Statement__DatatypesAssignment_6_2 )
            {
             before(grammarAccess.getCreate_StatementAccess().getDatatypesAssignment_6_2()); 
            // InternalCQL.g:2966:2: ( rule__Create_Statement__DatatypesAssignment_6_2 )
            // InternalCQL.g:2966:3: rule__Create_Statement__DatatypesAssignment_6_2
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
    // InternalCQL.g:2975:1: rule__Model__StatementsAssignment : ( ruleStatement ) ;
    public final void rule__Model__StatementsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2979:1: ( ( ruleStatement ) )
            // InternalCQL.g:2980:2: ( ruleStatement )
            {
            // InternalCQL.g:2980:2: ( ruleStatement )
            // InternalCQL.g:2981:3: ruleStatement
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
    // InternalCQL.g:2990:1: rule__Statement__TypeAssignment_0_0 : ( ruleSelect_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:2994:1: ( ( ruleSelect_Statement ) )
            // InternalCQL.g:2995:2: ( ruleSelect_Statement )
            {
            // InternalCQL.g:2995:2: ( ruleSelect_Statement )
            // InternalCQL.g:2996:3: ruleSelect_Statement
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
    // InternalCQL.g:3005:1: rule__Statement__TypeAssignment_0_1 : ( ruleCreate_Statement ) ;
    public final void rule__Statement__TypeAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3009:1: ( ( ruleCreate_Statement ) )
            // InternalCQL.g:3010:2: ( ruleCreate_Statement )
            {
            // InternalCQL.g:3010:2: ( ruleCreate_Statement )
            // InternalCQL.g:3011:3: ruleCreate_Statement
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
    // InternalCQL.g:3020:1: rule__Atomic__ValueAssignment_0_1 : ( RULE_INT ) ;
    public final void rule__Atomic__ValueAssignment_0_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3024:1: ( ( RULE_INT ) )
            // InternalCQL.g:3025:2: ( RULE_INT )
            {
            // InternalCQL.g:3025:2: ( RULE_INT )
            // InternalCQL.g:3026:3: RULE_INT
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
    // InternalCQL.g:3035:1: rule__Atomic__ValueAssignment_1_1 : ( RULE_FLOAT_NUMBER ) ;
    public final void rule__Atomic__ValueAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3039:1: ( ( RULE_FLOAT_NUMBER ) )
            // InternalCQL.g:3040:2: ( RULE_FLOAT_NUMBER )
            {
            // InternalCQL.g:3040:2: ( RULE_FLOAT_NUMBER )
            // InternalCQL.g:3041:3: RULE_FLOAT_NUMBER
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
    // InternalCQL.g:3050:1: rule__Atomic__ValueAssignment_2_1 : ( RULE_STRING ) ;
    public final void rule__Atomic__ValueAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3054:1: ( ( RULE_STRING ) )
            // InternalCQL.g:3055:2: ( RULE_STRING )
            {
            // InternalCQL.g:3055:2: ( RULE_STRING )
            // InternalCQL.g:3056:3: RULE_STRING
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
    // InternalCQL.g:3065:1: rule__Atomic__ValueAssignment_3_1 : ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) ;
    public final void rule__Atomic__ValueAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3069:1: ( ( ( rule__Atomic__ValueAlternatives_3_1_0 ) ) )
            // InternalCQL.g:3070:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            {
            // InternalCQL.g:3070:2: ( ( rule__Atomic__ValueAlternatives_3_1_0 ) )
            // InternalCQL.g:3071:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            {
             before(grammarAccess.getAtomicAccess().getValueAlternatives_3_1_0()); 
            // InternalCQL.g:3072:3: ( rule__Atomic__ValueAlternatives_3_1_0 )
            // InternalCQL.g:3072:4: rule__Atomic__ValueAlternatives_3_1_0
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
    // InternalCQL.g:3080:1: rule__Atomic__ValueAssignment_4_1 : ( ruleAttribute ) ;
    public final void rule__Atomic__ValueAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3084:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3085:2: ( ruleAttribute )
            {
            // InternalCQL.g:3085:2: ( ruleAttribute )
            // InternalCQL.g:3086:3: ruleAttribute
            {
             before(grammarAccess.getAtomicAccess().getValueAttributeParserRuleCall_4_1_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getAtomicAccess().getValueAttributeParserRuleCall_4_1_0()); 

            }


            }

        }
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
    // InternalCQL.g:3095:1: rule__Source__NameAssignment : ( RULE_ID ) ;
    public final void rule__Source__NameAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3099:1: ( ( RULE_ID ) )
            // InternalCQL.g:3100:2: ( RULE_ID )
            {
            // InternalCQL.g:3100:2: ( RULE_ID )
            // InternalCQL.g:3101:3: RULE_ID
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


    // $ANTLR start "rule__Attribute__NameAssignment"
    // InternalCQL.g:3110:1: rule__Attribute__NameAssignment : ( RULE_ID ) ;
    public final void rule__Attribute__NameAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3114:1: ( ( RULE_ID ) )
            // InternalCQL.g:3115:2: ( RULE_ID )
            {
            // InternalCQL.g:3115:2: ( RULE_ID )
            // InternalCQL.g:3116:3: RULE_ID
            {
             before(grammarAccess.getAttributeAccess().getNameIDTerminalRuleCall_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getAttributeAccess().getNameIDTerminalRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Attribute__NameAssignment"


    // $ANTLR start "rule__ExpressionsModel__ElementsAssignment_1"
    // InternalCQL.g:3125:1: rule__ExpressionsModel__ElementsAssignment_1 : ( ruleExpression ) ;
    public final void rule__ExpressionsModel__ElementsAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3129:1: ( ( ruleExpression ) )
            // InternalCQL.g:3130:2: ( ruleExpression )
            {
            // InternalCQL.g:3130:2: ( ruleExpression )
            // InternalCQL.g:3131:3: ruleExpression
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
    // InternalCQL.g:3140:1: rule__Or__RightAssignment_1_2 : ( ruleAnd ) ;
    public final void rule__Or__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3144:1: ( ( ruleAnd ) )
            // InternalCQL.g:3145:2: ( ruleAnd )
            {
            // InternalCQL.g:3145:2: ( ruleAnd )
            // InternalCQL.g:3146:3: ruleAnd
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
    // InternalCQL.g:3155:1: rule__And__RightAssignment_1_2 : ( ruleEqualitiy ) ;
    public final void rule__And__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3159:1: ( ( ruleEqualitiy ) )
            // InternalCQL.g:3160:2: ( ruleEqualitiy )
            {
            // InternalCQL.g:3160:2: ( ruleEqualitiy )
            // InternalCQL.g:3161:3: ruleEqualitiy
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
    // InternalCQL.g:3170:1: rule__Equalitiy__OpAssignment_1_1 : ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Equalitiy__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3174:1: ( ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:3175:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:3175:2: ( ( rule__Equalitiy__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:3176:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getEqualitiyAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:3177:3: ( rule__Equalitiy__OpAlternatives_1_1_0 )
            // InternalCQL.g:3177:4: rule__Equalitiy__OpAlternatives_1_1_0
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
    // InternalCQL.g:3185:1: rule__Equalitiy__RightAssignment_1_2 : ( ruleComparison ) ;
    public final void rule__Equalitiy__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3189:1: ( ( ruleComparison ) )
            // InternalCQL.g:3190:2: ( ruleComparison )
            {
            // InternalCQL.g:3190:2: ( ruleComparison )
            // InternalCQL.g:3191:3: ruleComparison
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
    // InternalCQL.g:3200:1: rule__Comparison__OpAssignment_1_1 : ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) ;
    public final void rule__Comparison__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3204:1: ( ( ( rule__Comparison__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:3205:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:3205:2: ( ( rule__Comparison__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:3206:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getComparisonAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:3207:3: ( rule__Comparison__OpAlternatives_1_1_0 )
            // InternalCQL.g:3207:4: rule__Comparison__OpAlternatives_1_1_0
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
    // InternalCQL.g:3215:1: rule__Comparison__RightAssignment_1_2 : ( rulePlusOrMinus ) ;
    public final void rule__Comparison__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3219:1: ( ( rulePlusOrMinus ) )
            // InternalCQL.g:3220:2: ( rulePlusOrMinus )
            {
            // InternalCQL.g:3220:2: ( rulePlusOrMinus )
            // InternalCQL.g:3221:3: rulePlusOrMinus
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
    // InternalCQL.g:3230:1: rule__PlusOrMinus__RightAssignment_1_1 : ( ruleMulOrDiv ) ;
    public final void rule__PlusOrMinus__RightAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3234:1: ( ( ruleMulOrDiv ) )
            // InternalCQL.g:3235:2: ( ruleMulOrDiv )
            {
            // InternalCQL.g:3235:2: ( ruleMulOrDiv )
            // InternalCQL.g:3236:3: ruleMulOrDiv
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
    // InternalCQL.g:3245:1: rule__MulOrDiv__OpAssignment_1_1 : ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) ;
    public final void rule__MulOrDiv__OpAssignment_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3249:1: ( ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) ) )
            // InternalCQL.g:3250:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            {
            // InternalCQL.g:3250:2: ( ( rule__MulOrDiv__OpAlternatives_1_1_0 ) )
            // InternalCQL.g:3251:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            {
             before(grammarAccess.getMulOrDivAccess().getOpAlternatives_1_1_0()); 
            // InternalCQL.g:3252:3: ( rule__MulOrDiv__OpAlternatives_1_1_0 )
            // InternalCQL.g:3252:4: rule__MulOrDiv__OpAlternatives_1_1_0
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
    // InternalCQL.g:3260:1: rule__MulOrDiv__RightAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__MulOrDiv__RightAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3264:1: ( ( rulePrimary ) )
            // InternalCQL.g:3265:2: ( rulePrimary )
            {
            // InternalCQL.g:3265:2: ( rulePrimary )
            // InternalCQL.g:3266:3: rulePrimary
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
    // InternalCQL.g:3275:1: rule__Primary__InnerAssignment_0_2 : ( ruleExpression ) ;
    public final void rule__Primary__InnerAssignment_0_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3279:1: ( ( ruleExpression ) )
            // InternalCQL.g:3280:2: ( ruleExpression )
            {
            // InternalCQL.g:3280:2: ( ruleExpression )
            // InternalCQL.g:3281:3: ruleExpression
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
    // InternalCQL.g:3290:1: rule__Primary__ExpressionAssignment_1_2 : ( rulePrimary ) ;
    public final void rule__Primary__ExpressionAssignment_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3294:1: ( ( rulePrimary ) )
            // InternalCQL.g:3295:2: ( rulePrimary )
            {
            // InternalCQL.g:3295:2: ( rulePrimary )
            // InternalCQL.g:3296:3: rulePrimary
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
    // InternalCQL.g:3305:1: rule__Select_Statement__NameAssignment_0 : ( ( 'SELECT' ) ) ;
    public final void rule__Select_Statement__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3309:1: ( ( ( 'SELECT' ) ) )
            // InternalCQL.g:3310:2: ( ( 'SELECT' ) )
            {
            // InternalCQL.g:3310:2: ( ( 'SELECT' ) )
            // InternalCQL.g:3311:3: ( 'SELECT' )
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            // InternalCQL.g:3312:3: ( 'SELECT' )
            // InternalCQL.g:3313:4: 'SELECT'
            {
             before(grammarAccess.getSelect_StatementAccess().getNameSELECTKeyword_0_0()); 
            match(input,43,FOLLOW_2); 
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


    // $ANTLR start "rule__Select_Statement__AttributesAssignment_2_1_0"
    // InternalCQL.g:3324:1: rule__Select_Statement__AttributesAssignment_2_1_0 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3328:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3329:2: ( ruleAttribute )
            {
            // InternalCQL.g:3329:2: ( ruleAttribute )
            // InternalCQL.g:3330:3: ruleAttribute
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_1_0_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_1_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__AttributesAssignment_2_1_0"


    // $ANTLR start "rule__Select_Statement__AttributesAssignment_2_1_1_1"
    // InternalCQL.g:3339:1: rule__Select_Statement__AttributesAssignment_2_1_1_1 : ( ruleAttribute ) ;
    public final void rule__Select_Statement__AttributesAssignment_2_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3343:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3344:2: ( ruleAttribute )
            {
            // InternalCQL.g:3344:2: ( ruleAttribute )
            // InternalCQL.g:3345:3: ruleAttribute
            {
             before(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_1_1_1_0()); 
            pushFollow(FOLLOW_2);
            ruleAttribute();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getAttributesAttributeParserRuleCall_2_1_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__AttributesAssignment_2_1_1_1"


    // $ANTLR start "rule__Select_Statement__SourcesAssignment_4_0"
    // InternalCQL.g:3354:1: rule__Select_Statement__SourcesAssignment_4_0 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_4_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3358:1: ( ( ruleSource ) )
            // InternalCQL.g:3359:2: ( ruleSource )
            {
            // InternalCQL.g:3359:2: ( ruleSource )
            // InternalCQL.g:3360:3: ruleSource
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_4_0_0()); 
            pushFollow(FOLLOW_2);
            ruleSource();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_4_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__SourcesAssignment_4_0"


    // $ANTLR start "rule__Select_Statement__SourcesAssignment_4_1_1"
    // InternalCQL.g:3369:1: rule__Select_Statement__SourcesAssignment_4_1_1 : ( ruleSource ) ;
    public final void rule__Select_Statement__SourcesAssignment_4_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3373:1: ( ( ruleSource ) )
            // InternalCQL.g:3374:2: ( ruleSource )
            {
            // InternalCQL.g:3374:2: ( ruleSource )
            // InternalCQL.g:3375:3: ruleSource
            {
             before(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_4_1_1_0()); 
            pushFollow(FOLLOW_2);
            ruleSource();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getSourcesSourceParserRuleCall_4_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__SourcesAssignment_4_1_1"


    // $ANTLR start "rule__Select_Statement__PredicatesAssignment_5_1"
    // InternalCQL.g:3384:1: rule__Select_Statement__PredicatesAssignment_5_1 : ( ruleExpressionsModel ) ;
    public final void rule__Select_Statement__PredicatesAssignment_5_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3388:1: ( ( ruleExpressionsModel ) )
            // InternalCQL.g:3389:2: ( ruleExpressionsModel )
            {
            // InternalCQL.g:3389:2: ( ruleExpressionsModel )
            // InternalCQL.g:3390:3: ruleExpressionsModel
            {
             before(grammarAccess.getSelect_StatementAccess().getPredicatesExpressionsModelParserRuleCall_5_1_0()); 
            pushFollow(FOLLOW_2);
            ruleExpressionsModel();

            state._fsp--;

             after(grammarAccess.getSelect_StatementAccess().getPredicatesExpressionsModelParserRuleCall_5_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Select_Statement__PredicatesAssignment_5_1"


    // $ANTLR start "rule__Create_Statement__NameAssignment_2"
    // InternalCQL.g:3399:1: rule__Create_Statement__NameAssignment_2 : ( RULE_ID ) ;
    public final void rule__Create_Statement__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3403:1: ( ( RULE_ID ) )
            // InternalCQL.g:3404:2: ( RULE_ID )
            {
            // InternalCQL.g:3404:2: ( RULE_ID )
            // InternalCQL.g:3405:3: RULE_ID
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
    // InternalCQL.g:3414:1: rule__Create_Statement__AttributesAssignment_4 : ( ruleAttribute ) ;
    public final void rule__Create_Statement__AttributesAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3418:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3419:2: ( ruleAttribute )
            {
            // InternalCQL.g:3419:2: ( ruleAttribute )
            // InternalCQL.g:3420:3: ruleAttribute
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
    // InternalCQL.g:3429:1: rule__Create_Statement__DatatypesAssignment_5 : ( ruleDataType ) ;
    public final void rule__Create_Statement__DatatypesAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3433:1: ( ( ruleDataType ) )
            // InternalCQL.g:3434:2: ( ruleDataType )
            {
            // InternalCQL.g:3434:2: ( ruleDataType )
            // InternalCQL.g:3435:3: ruleDataType
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
    // InternalCQL.g:3444:1: rule__Create_Statement__AttributesAssignment_6_1 : ( ruleAttribute ) ;
    public final void rule__Create_Statement__AttributesAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3448:1: ( ( ruleAttribute ) )
            // InternalCQL.g:3449:2: ( ruleAttribute )
            {
            // InternalCQL.g:3449:2: ( ruleAttribute )
            // InternalCQL.g:3450:3: ruleAttribute
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
    // InternalCQL.g:3459:1: rule__Create_Statement__DatatypesAssignment_6_2 : ( ruleDataType ) ;
    public final void rule__Create_Statement__DatatypesAssignment_6_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalCQL.g:3463:1: ( ( ruleDataType ) )
            // InternalCQL.g:3464:2: ( ruleDataType )
            {
            // InternalCQL.g:3464:2: ( ruleDataType )
            // InternalCQL.g:3465:3: ruleDataType
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
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x00000A0000000002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000003000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x00000014000030F0L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x00000014000030F2L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000600000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000007800000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000007800002L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000300000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000300000002L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000018000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000018000002L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000002008000080L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000002008000082L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x00000000001FC000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000008800000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x00000000001FC002L});

}